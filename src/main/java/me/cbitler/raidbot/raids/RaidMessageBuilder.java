package me.cbitler.raidbot.raids;

import me.cbitler.raidbot.utility.I18n;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Yann 'Ze' Richard
 */
public class RaidMessageBuilder {

    // \u202f \u200b
    // \u2063

    //static String fieldValuePrefix  = "\u202f\u202f\u202f\u202f";
    static String fieldValuePrefix = "\t";
    static String raiderPrefix = fieldValuePrefix + fieldValuePrefix + ":small_blue_diamond: ";
    static String waitingListPrefix = fieldValuePrefix + fieldValuePrefix + ":small_orange_diamond: ";

    public static MessageEmbed buildEmbed(PendingRaid raid) {
        return buildEmbed(
                raid.getName(),
                raid.getDescription(),
                raid.getLeaderName(),
                raid.getDateTime(),
                buildRolesText(raid),
                "",
                raid.hasWaitingList()
        );
    }

    public static MessageEmbed buildEmbed(Raid raid) {
        return buildEmbed(
                raid.getName(),
                raid.getDescription(),
                raid.getRaidLeaderName(),
                raid.getRaidTime(),
                buildRolesText(raid),
                raid.messageId,
                raid.hasWaitingList()
        );
    }

    private static MessageEmbed buildEmbed(String name, String description, String leader, ZonedDateTime dateTime, String roleText, String messageId, boolean hasWaitingList) {
        EmbedBuilder builder = new EmbedBuilder();

        String author_img_url = "https://ffxiv.gamerescape.com/w/images/9/90/Player32_Icon.png";
        String thumbnail_img_url = "https://tabula-rasa.ovh/discord/The_Feast3_Icon.png";
        String footer_img_url = "https://ffxiv.gamerescape.com/w/images/f/f2/Mob19_Icon.png";
        String author_url = null;
        String greets = fieldValuePrefix + fieldValuePrefix + " By Yrline Hil'Wayard @Cerberus !";

        String greetsLine = "";
        boolean legende = false;
        String legendeLine = "";

        builder.setThumbnail(thumbnail_img_url);
        builder.setColor(new Color(16729856));

        builder.setAuthor(name, author_url, author_img_url);
        String desc = "```diff\n-> " + description + "\n```\n";
        builder.setDescription(desc);

        if (hasWaitingList) {
            legende = true;
            greetsLine = greets;
            legendeLine = "**" + raiderPrefix + "** " + I18n.getMessage("in_raid");
            legendeLine += "**" + waitingListPrefix + "** " + I18n.getMessage("on_waiting_list");
        }
        String date = dateTime.format(DateTimeFormatter.ofPattern("d/MM"));
        String time = dateTime.withZoneSameInstant(ZoneId.of("US/Central")).format(DateTimeFormatter.ofPattern("hh:mma z"));

        builder.addField(":gem: " + I18n.getMessage("created_by"), "**" + fieldValuePrefix + leader + "**", true);
        builder.addField(":watch: " + I18n.getMessage("date_time"), date + " @ " + time + "\n", true);
        builder.addField(":pushpin: " + I18n.getMessage("roles"), roleText, true);
        if (legende) {
            builder.addField(":warning: " + I18n.getMessage("legend") + " : ", legendeLine, true);
        }
        builder.setFooter("Raid ID : " + messageId + greetsLine, footer_img_url);

        return builder.build();
    }


    /**
     * Builds the text to go into the roles field in the embedded message
     * @param raid The raid object
     * @return The role text
     */
    private static String buildRolesText(PendingRaid raid) {
        StringBuilder text = new StringBuilder();

        for(RaidRole role : raid.getRolesWithNumbers()) {
            text.append("\n" + "**").append(fieldValuePrefix).append(role.name).append(" (0 / ").append(role.amount).append(") :** \n");
        }
        return text.toString();
    }

    /**
     * Build the role text, which shows the roles users are playing in the raids
     * @return The role text
     */
    private static String buildRolesText(Raid raid) {
        StringBuilder text = new StringBuilder();
        for(RaidRole role : raid.getRoles()) {
            int cpt = 0;
            int max = role.getAmount();
            StringBuilder players = new StringBuilder();
            String prefix = raiderPrefix;
            for (RaidUser user : raid.getUsersInRole(role.name)) {
                if (cpt >= max) {
                    prefix = waitingListPrefix;
                }
                players.append(prefix).append(user.name).append(" (").append(user.spec).append(")\n");
                cpt++;
            }
            //players += "\";
            text.append("\n" + "**").append(fieldValuePrefix).append(role.name).append(" (").append(cpt).append(" / ").append(role.amount).append("):** \n").append(players);
        }
        return text + "\n";
    }
}
