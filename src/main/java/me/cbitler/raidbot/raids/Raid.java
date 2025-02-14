package me.cbitler.raidbot.raids;

import me.cbitler.raidbot.RaidBot;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a raid and has methods for adding/removing users, roles, etc
 */
public class Raid {
    String messageId, name, description, serverId, channelId, raidLeaderName;
    ZonedDateTime raidTime;
    boolean hasWaitingList;
    private final List<RaidRole> roles = new ArrayList<>();
    private final List<RaidUser> users = new ArrayList<>();
    private RaidReminder reminder = null;

    /**
     * Create a new Raid with the specified data
     *
     * @param messageId      The embedded message Id related to this raid. Often treated/displayed as the Raid ID
     * @param serverId       The serverId that the raid is on
     * @param channelId      The announcement channel's id for this raid
     * @param raidLeaderName The name of the raid leader
     * @param name           The name of the raid
     * @param raidTime       The date/time of the raid
     * @param hasWaitingList Does the raid have a waiting list?
     */
    public Raid(String messageId, String serverId, String channelId, String raidLeaderName, String name, String description, ZonedDateTime raidTime, ZonedDateTime reminderTime, boolean hasWaitingList) {
        this.messageId = messageId;
        this.serverId = serverId;
        this.channelId = channelId;
        this.raidLeaderName = raidLeaderName;
        this.name = name;
        this.description = description;
        this.raidTime = raidTime;
        this.hasWaitingList = hasWaitingList;
        setupReminder(reminderTime);
    }

    private void setupReminder(ZonedDateTime reminderTime) {
        if (reminderTime != null) {
            this.reminder = new RaidReminder(this, reminderTime);
        }
    }

    /**
     * Create a new Raid from a pending raid. Used alongside server/message ID input from Discord to create a Raid.
     *
     * @param messageId   The embedded message Id related to this raid. Often treated/displayed as the Raid ID
     * @param serverId    The serverId that the raid is on
     * @param channelId   The announcement channel's id for this raid
     * @param pendingRaid The pending raid being transformed into a full raid
     */
    public Raid(String messageId, String serverId, String channelId, PendingRaid pendingRaid) {
        this(messageId
                , serverId
                , channelId
                , pendingRaid.getLeaderName()
                , pendingRaid.getName()
                , pendingRaid.getDescription()
                , pendingRaid.getDateTime()
                , pendingRaid.getReminderTime()
                , pendingRaid.hasWaitingList());
    }

    /**
     * Get the message ID for this raid
     *
     * @return The message ID for this raid
     */
    public boolean hasWaitingList() {
        return hasWaitingList;
    }

    /**
     * Get the message ID for this raid
     * @return The message ID for this raid
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Get the server ID for this raid
     * @return The server ID for this raid
     */
    public String getServerId() { return serverId; }

    /**
     * Get the channel ID for this raid
     * @return The channel ID for this raid
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Get the name of this raid
     * @return The name of this raid
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the raid
     * @return The description of the raid
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the raid
     * @param description The description of the raid
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the date of this raid
     *
     * @return The date of this raid
     */
    public ZonedDateTime getRaidTime() {
        return raidTime;
    }

    /**
     * Get the raid leader's name
     * @return The raid leader's name
     */
    public String getRaidLeaderName() {
        return raidLeaderName;
    }

    /**
     * Get the list of roles in this raid
     *
     * @return The list of roles in this raid
     */
    public List<RaidRole> getRoles() {
        return roles;
    }

    /**
     * Gets the RaidReminder associated with this raid.
     *
     * @return The Raid reminder, null if no reminder for this raid.
     */
    public RaidReminder getReminder() {
        return reminder;
    }

    /**
     * Check if a specific role is valid, and whether it's full. Note that raids that have waiting lists enabled
     * will always return true, because you can have an unlimited amount of queuers.
     *
     * @param role The role to check
     * @return True if it is valid and not full, false otherwise
     */
    public boolean isValidNotFullRole(String role) {
        RaidRole r = getRole(role);
        // if hasWaitingList raid, there is no limit
        if (hasWaitingList) {
            return true;
        }

        if (r != null) {
            int max = r.getAmount();
            return getQuantityInRole(role) < max;
        }

        return false;
    }

    /**
     * Get the object representing a role
     * @param role The name of the role
     * @return The object representing the specified role
     */
    private RaidRole getRole(String role) {
        for(RaidRole r : roles) {
            if(r.getName().equalsIgnoreCase(role)) {
                return r;
            }
        }

        return null;
    }

    /**
     * Gets the quantity of users in a role
     * @param role The name of the role
     * @return The quantity of users in the role
     */
    private int getQuantityInRole(String role) {
        return (int) users.stream()
                .filter(user -> role.equalsIgnoreCase(user.getRole()))
                .count();
    }

    /**
     * Get list of users in a role
     * @param role The name of the role
     * @return The users in the role
     */
    public List<RaidUser> getUsersInRole(String role) {
        return users.stream()
                .filter(user -> role.equalsIgnoreCase(user.getRole()))
                .collect(Collectors.toList());
    }

    /**
     * Add a user to this raid.
     * This first creates the user and attempts to insert it into the database, if needed
     * Then it adds them to list of raid users with their role
     * @param id The id of the user
     * @param name The name of the user
     * @param spec The specialization they are playing
     * @param ordre Unix timestamp of when user have been added
     * @param db_insert Whether the user should be inserted. This is false when the roles are loaded from the database.
     * @return true if the user was added, false otherwise
     */
    public boolean addUser(String id, String name, String spec, String ordre, boolean db_insert, boolean update_message) {
        RaidUser user = new RaidUser(id, name, spec, ordre);

        if (db_insert) {
            try {
                RaidBot.getInstance().getDatabase().update("INSERT INTO `raidUsers` (`userId`, `username`, `spec`, `ordre`, `raidId`)" +
                        " VALUES (?,?,?,?,?)", new String[]{
                        id,
                        name,
                        spec,
                        ordre,
                        getMessageId()
                });
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        users.add(user);

        if(update_message) {
            updateMessage();
        }
        return true;
    }

    /**
     * Check if a specific user is in this raid
     * @param id The id of the user
     * @return True if they are in the raid, false otherwise
     */
    public boolean isUserInRaid(String id) {
        return users.stream()
                .anyMatch(user -> user.getId().equalsIgnoreCase(id));
    }

    /**
     * Remove a user from this raid. This also updates the database to remove them from the raid
     * @param id The user's id
     */
    public void removeUserById(String id) {
        users.removeIf(user -> user.getId().equalsIgnoreCase(id));

        try {
            RaidBot.getInstance().getDatabase().update("DELETE FROM `raidUsers` WHERE `userId` = ? AND `raidId` = ?",
                    new String[]{id, getMessageId()});
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateMessage();
    }

    /**
     * Send the dps report log links to the players in this raid
     * @param logLinks The list of links
     */
    public void messagePlayersWithLogLinks(List<String> logLinks) {
        StringBuilder logLinkMessage = new StringBuilder("ArcDPS reports from **" + this.getName() + "**:\n");
        for (String link : logLinks) {
            logLinkMessage.append(link).append("\n");
        }

        final String finalLogLinkMessage = logLinkMessage.toString();
        for (RaidUser user : users) {
            RaidBot.getInstance().getServer(this.serverId).getMemberById(user.id).getUser().openPrivateChannel().queue(
                    privateChannel -> privateChannel.sendMessage(finalLogLinkMessage).queue()
            );
        }
    }

    /**
     * Update the embedded message for the raid
     */
    public void updateMessage() {
        MessageEmbed embed = buildEmbed();
        try {
            RaidBot.getInstance().getServer(getServerId()).getTextChannelById(getChannelId())
                    .editMessageById(getMessageId(), embed).queue();
        } catch (Exception e) {
            System.out.println("Could not update embed for server: " + getServerId());
        }
    }

    /**
     * Build the embedded message that shows the information about this raid
     *
     * @return The embedded message representing this raid
     */
    private MessageEmbed buildEmbed() {
        return RaidMessageBuilder.buildEmbed(this);
    }

    /**
     * Returns list of current participants in the raid.
     *
     * @return Current raiders.
     */
    public List<RaidUser> getRaiders() {
        return users;
    }

    /**
     * Returns the TextChannel for this raid session.
     * This is the same channel that has the embed post
     *
     * @return The discord channel for making announcements
     */
    public TextChannel getAnnouncementChannel() {
        return RaidBot.getInstance().getServer(serverId).getTextChannelById(channelId);
    }

    /**
     * Get a RaidUser in this raid by their name
     *
     * @param name The user's name
     * @return The RaidUser if they are in this raid, null otherwise
     */
    public RaidUser getUserByName(String name) {
        return users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Remove a user by their username
     * @param name The name of the user being removed
     */
    public void removeUserByName(String name) {
        RaidUser r = null;
        if ((r = getUserByName(name)) != null) {
            removeUserById(r.getId());
        }
    }

}
