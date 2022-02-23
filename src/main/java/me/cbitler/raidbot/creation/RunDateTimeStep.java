package me.cbitler.raidbot.creation;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.database.Database;
import me.cbitler.raidbot.raids.PendingRaid;
import me.cbitler.raidbot.utility.I18n;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

/**
 * Step to set the date of the raid
 *
 * @author Christopher Bitler
 */
public class RunDateTimeStep implements CreationStep {

    /**
     * Handle inputting the date and time for the raid
     *
     * @param e The direct message event
     * @return True if the date was set, false otherwise
     */
    public boolean handleDM(PrivateMessageReceivedEvent e) {
        RaidBot bot = RaidBot.getInstance();
        PendingRaid raid = bot.getPendingRaids().get(e.getAuthor().getId());
        if (raid == null) {
            return false;
        }
        String decision = e.getMessage().getRawContent().toUpperCase();
        try {
            ZonedDateTime dateTime = ZonedDateTime.parse(decision, Database.TIME_FORMAT);
            raid.setDate(dateTime);
            return true;
        } catch (DateTimeParseException ex) {
            e.getChannel().sendMessage(I18n.getMessage("could_not_parse_date")).queue();
            System.out.println(ex.getMessage());
            System.out.println(ex.getParsedString());
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getStepText() {
        return I18n.getMessage("raid_date_query");
    }

    /**
     * {@inheritDoc}
     */
    public CreationStep getNextStep() {
        return new RunReminderStep();
    }
}
