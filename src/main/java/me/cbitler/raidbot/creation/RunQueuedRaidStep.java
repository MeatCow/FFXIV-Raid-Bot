package me.cbitler.raidbot.creation;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.raids.PendingRaid;
import me.cbitler.raidbot.utility.I18n;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

/**
 * Set the time for the raid
 * @author Christopher Bitler
 */
public class RunQueuedRaidStep implements CreationStep {

    /**
     * Handle setting the time for the raid
     * @param e The direct message event
     * @return True if the time is set, false otherwise
     */
    public boolean handleDM(PrivateMessageReceivedEvent e) {
        RaidBot bot = RaidBot.getInstance();
        PendingRaid raid = bot.getPendingRaids().get(e.getAuthor().getId());
        if (raid == null) {
            return false;
        }
        String decision = e.getMessage().getRawContent().trim().toLowerCase();
        String yn = "";

        switch(decision)
        {
            case "yes":
            case "y":
            case "oui":
            case "o":
                yn = "1";
                break;
            case "no":
            case "n":
            case "non":
                yn = "0";
                break;
            default:
                e.getChannel().sendMessage(I18n.getMessage("yes_no_request")).queue();
                return false;
        }

        raid.setQueued(yn); //TODO: Change to boolean

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String getStepText() {
        return I18n.getMessage("waiting_list_query");
    }

    /**
     * {@inheritDoc}
     */
    public CreationStep getNextStep() {
        return null;
    }
}
