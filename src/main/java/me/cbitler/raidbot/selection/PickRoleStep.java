package me.cbitler.raidbot.selection;

import me.cbitler.raidbot.raids.Raid;
import me.cbitler.raidbot.raids.RaidUser;
import me.cbitler.raidbot.utility.I18n;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Step for picking a role for a raid
 * @author Christopher Bitler
 */
public class PickRoleStep implements SelectionStep {
    Raid raid;
    String spec;

    /**
     * Create a new step for this role selection with the specified raid and spec
     * that the user chose
     * @param raid The raid
     * @param spec The specialization that the user chose
     */
    public PickRoleStep(Raid raid, String spec) {
        this.raid = raid;
        this.spec = spec;
    }

    /**
     * Handle the user input - checks to see if the role they are picking is valid
     * and not full, and if so, adding them to that role
     * @param e The private message event
     * @return True if the user chose a valid, not full, role, false otherwise
     */
    @Override
    public boolean handleDM(PrivateMessageReceivedEvent e) {
        String role = e.getMessage().getRawContent();
        if (raid.isValidNotFullRole(role)) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String ordre = String.valueOf(timestamp.getTime());
            raid.addUser(e.getAuthor().getId(), e.getAuthor().getName(), spec, ordre, true, true);
            e.getChannel().sendMessage(I18n.getMessage("added_to_raid")).queue();
            return true;
        } else {
            e.getChannel().sendMessage(I18n.getMessage("role_full_error")).queue();
            return false;
        }
    }

    /**
     * Get the next step - no next step here as this is a one step process
     * @return null
     */
    @Override
    public SelectionStep getNextStep() {
        return null;
    }

    /**
     * The step text changes the text based on the available roles.
     * @return The step text
     */
    @Override
    public String getStepText() {
        String text = I18n.getMessage("select_a_role") + " (";
        for (int i = 0; i < raid.getRoles().size(); i++) {
            if (i == raid.getRoles().size()-1) {
                text += raid.getRoles().get(i).getName();
            } else {
                text += (raid.getRoles().get(i).getName() + ", ");
            }
        }
        text += ") " + I18n.getMessage("or_hit_cancel");

        return text;
    }
}
