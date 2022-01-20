package me.cbitler.raidbot.creation;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.raids.PendingRaid;
import me.cbitler.raidbot.utility.I18n;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

/**
 * Step to set the date of the raid
 * @author Christopher Bitler
 */
public class RunDateStep implements CreationStep {

    /**
     * Handle inputting the date for the raid
     * @param e The direct message event
     * @return True if the date was set, false otherwise
     */
    public boolean handleDM(PrivateMessageReceivedEvent e) {
        RaidBot bot = RaidBot.getInstance();
        PendingRaid raid = bot.getPendingRaids().get(e.getAuthor().getId());
        if (raid == null) {
            return false;
        }

        raid.setDate(e.getMessage().getRawContent());

        return true;
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
        return new RunTimeStep();
    }
}
