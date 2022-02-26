package me.cbitler.raidbot.raids;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.database.Database;
import me.cbitler.raidbot.database.QueryResult;
import me.cbitler.raidbot.utility.Reactions;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Serves as a manager for all of the raids. This includes creating, loading, and deleting raids
 *
 * @author Christopher Bitler
 */
public class RaidManager {

    static List<Raid> raids = new ArrayList<>();

    /**
     * Create a raid. This turns a PendingRaid object into a Raid object and inserts it into the list of raids.
     * It also sends the associated embedded message and adds the reactions for people to join to the embed
     *
     * @param raid The pending raid to create
     */
    public static void createRaid(PendingRaid raid) {
        MessageEmbed message = buildEmbed(raid);

        Guild guild = RaidBot.getInstance().getServer(raid.getServerId());
        List<TextChannel> channels = guild.getTextChannelsByName(raid.getAnnouncementChannel(), true);
        if (channels.size() > 0) {
            // We always go with the first channel if there is more than one
            try {
                channels.get(0).sendMessage(message).queue(message1 -> {

                    Raid newRaid = new Raid(message1.getId(), message1.getGuild().getId(), message1.getChannel().getId(), raid);
                    newRaid.getRoles().addAll(raid.rolesWithNumbers);
                    boolean inserted = insertToDatabase(newRaid);

                    if (inserted) {
                        raids.add(newRaid);

                        for (Emote emote : Reactions.getEmotes()) {
                            message1.addReaction(emote).queue();
                        }
                        message1.pin().queue();

                    } else {
                        message1.delete().queue();
                    }
                });
            } catch (Exception e) {
                System.out.println("Error encountered in sending message.");
                e.printStackTrace();
                throw e;
            }
        }
    }

    /**
     * Insert a raid into the database
     *
     * @param raid The raid to insert
     * @return True if inserted, false otherwise
     */
    private static boolean insertToDatabase(Raid raid) {
        RaidBot bot = RaidBot.getInstance();
        Database db = bot.getDatabase();

        try {
            db.update("INSERT INTO `raids` (`raidId`, `serverId`, `channelId`, `leader`, `name`, `description`, `dateTime`, `reminderTime`, `hasWaitingList`, `roles`) VALUES (?,?,?,?,?,?,?,?,?,?)", new Object[]{
                    raid.getMessageId(),
                    raid.getServerId(),
                    raid.getChannelId(),
                    raid.getRaidLeaderName(),
                    raid.getName(),
                    raid.getDescription(),
                    raid.getRaidTime().format(Database.TIME_FORMAT),
                    formatReminder(raid.getReminder()),
                    raid.hasWaitingList(),
                    formatRolesForDatabase(raid.getRoles())
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Verifies if reminder is null before inserting to DB. If it's not null, it will format the zonedatetime
     * into database's acceptable format.
     * Necessary operation because the reminder is optional
     *
     * @param reminder RaidReminder to be changed to text
     * @return String of ZoneDateTime from the RaidReminder object
     */
    private static String formatReminder(RaidReminder reminder) {
        if (reminder != null) {
            return reminder.getReminderTime().format(Database.TIME_FORMAT);
        }
        return null;
    }

    /**
     * Load raids
     * This first queries all of the raids and loads the raid data and adds the raids to the raid list
     * Then, it queries the raid users and inserts them into their relevant raids, updating the embedded messages
     * Finally, it queries the raid users roles and inserts those to the raids
     */
    public static void loadRaids() {
        RaidBot bot = RaidBot.getInstance();
        Database db = bot.getDatabase();

        try {
            QueryResult results = db.query("SELECT * FROM `raids`", new Object[]{});
            while (results.getResults().next()) {
                String name = results.getResults().getString("name");
                String description = results.getResults().getString("description");
                if (description == null) {
                    description = "N/A";
                }
                ZonedDateTime dateTime = ZonedDateTime.parse(results.getResults().getString("dateTime"), Database.TIME_FORMAT);
                ZonedDateTime reminderTime = null;
                String rolesText = results.getResults().getString("roles");
                String messageId = results.getResults().getString("raidId");
                String serverId = results.getResults().getString("serverId");
                String channelId = results.getResults().getString("channelId");
                boolean hasWaitingList = results.getResults().getBoolean("hasWaitingList");

                String leaderName = null;
                try {
                    leaderName = results.getResults().getString("leader");
                } catch (Exception e) {
                    System.out.println(e);
                }
                String reminder = results.getResults().getString("reminderTime");
                if (reminder != null) {
                    reminderTime = ZonedDateTime.parse(reminder, Database.TIME_FORMAT);
                }

                Raid raid = new Raid(messageId, serverId, channelId, leaderName, name, description, dateTime, reminderTime, hasWaitingList);
                String[] roleSplit = rolesText.split(";");
                for (String roleAndAmount : roleSplit) {
                    String[] parts = roleAndAmount.split(":");
                    int amnt = Integer.parseInt(parts[0]);
                    String role = parts[1];
                    raid.getRoles().add(new RaidRole(amnt, role));
                }
                raids.add(raid);
                System.out.println("Adding raid : " + messageId + " " + serverId + " " + channelId + " " + leaderName + " " + name + " " + description + " " + dateTime + " " + " " + reminderTime + " " + hasWaitingList);
            }
            results.getResults().close();
            results.getStmt().close();

            QueryResult userResults = db.query("SELECT * FROM `raidUsers` ORDER BY ordre ASC");

            while (userResults.getResults().next()) {
                String id = userResults.getResults().getString("userId");
                String name = userResults.getResults().getString("username");
                String spec = userResults.getResults().getString("spec");
                String ordre = userResults.getResults().getString("ordre");
                if (ordre == null) {
                    ordre = "1";
                }
                String raidId = userResults.getResults().getString("raidId");

                Raid raid = RaidManager.getRaid(raidId);
                if (raid != null) {
                    raid.addUser(id, name, spec, ordre, false, false);
                }
            }

            int cpt = 0;
            for (Raid raid : raids) {
                raid.updateMessage();
                cpt++;
            }
            System.out.println("All raids loaded : " + cpt);
        } catch (SQLException e) {
            System.out.println("Couldn't load raids.. exiting");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Delete the raid from the database and maps, and delete the message if it is still there
     *
     * @param messageId The raid ID
     * @return true if deleted, false if not deleted
     */
    public static boolean deleteRaid(String messageId) {
        Raid r = getRaid(messageId);
        if (r != null) {
            try {
                RaidBot.getInstance().getServer(r.getServerId())
                        .getTextChannelById(r.getChannelId()).getMessageById(messageId).queue(message -> message.delete().queue());
            } catch (Exception e) {
                // Nothing, the message doesn't exist - it can happen
            }

            raids.removeIf(raid -> raid.getMessageId().equalsIgnoreCase(messageId));

            try {
                RaidBot.getInstance().getDatabase().update("DELETE FROM `raids` WHERE `raidId` = ?", new String[]{
                        messageId
                });
                RaidBot.getInstance().getDatabase().update("DELETE FROM `raidUsers` WHERE `raidId` = ?", new String[]{
                        messageId
                });
            } catch (Exception e) {
                System.out.println("Error encountered deleting raid");
                System.out.println(e.getMessage());
            }

            return true;
        }

        return false;
    }

    /**
     * Get a raid from the discord message ID
     *
     * @param messageId The discord message ID associated with the raid's embedded message
     * @return The raid object related to that messageId, if it exist.
     */
    public static Raid getRaid(String messageId) {
        return raids.stream()
                .filter(raid -> raid.getMessageId().equalsIgnoreCase(messageId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Formats the roles associated with a raid in a form that can be inserted into a database row.
     * This combines them as [number]:[name];[number]:[name];...
     *
     * @param raidRoleList The list of roles to convert
     * @return The formatted string
     */
    private static String formatRolesForDatabase(List<RaidRole> raidRoleList) {
        StringBuilder data = new StringBuilder();

        for (int i = 0; i < raidRoleList.size(); i++) {
            RaidRole role = raidRoleList.get(i);
            if (i == raidRoleList.size() - 1) {
                data.append(role.amount).append(":").append(role.name);
            } else {
                data.append(role.amount).append(":").append(role.name).append(";");
            }
        }

        return data.toString();
    }

    /**
     * Create a message embed to show the raid
     *
     * @param raid The raid object
     * @return The embedded message
     */
    private static MessageEmbed buildEmbed(PendingRaid raid) {
        return RaidMessageBuilder.buildEmbed(raid);
    }
}
