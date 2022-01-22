package me.cbitler.raidbot.creation;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.raids.PendingRaid;
import me.cbitler.raidbot.raids.RaidRole;
import me.cbitler.raidbot.utility.I18n;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

/**
 * Role setup step for the raid.
 * This one should take multiple inputs and as a result it doesn't finish until the user
 * types 'done'.
 *
 * @author Christopher Bitler
 */
public class RunRoleSetupStep implements CreationStep {

    /**
     * Handle user input - should be in the format [number]:[role] unless it is 'done'.
     *
     * @param e The direct message event
     * @return True if the user entered 'done', false otherwise
     */
    public boolean handleDM(PrivateMessageReceivedEvent e) {
        RaidBot bot = RaidBot.getInstance();
        PendingRaid raid = bot.getPendingRaids().get(e.getAuthor().getId());
        String incomingMessage = e.getMessage().getRawContent().trim();

        if (incomingMessage.equalsIgnoreCase("done")) {
            if (raid.getRolesWithNumbers().size() > 0) {
                return true;
            } else {
                e.getChannel().sendMessage(I18n.getMessage("add_minimum_roles")).queue();
                return false;
            }
        } else {
            if (incomingMessage.equalsIgnoreCase("D")) {
                raid.getRolesWithNumbers().add(new RaidRole(1, "Tank"));
                raid.getRolesWithNumbers().add(new RaidRole(1, "Heal"));
                raid.getRolesWithNumbers().add(new RaidRole(2, "DPS"));
                e.getChannel().sendMessage(I18n.getMessage("preformed_dungeon_added") + "\n" + I18n.getMessage("done_query")).queue();
            } else if (incomingMessage.equalsIgnoreCase("R")) {
                raid.getRolesWithNumbers().add(new RaidRole(2, "Tank"));
                raid.getRolesWithNumbers().add(new RaidRole(2, "Heal"));
                raid.getRolesWithNumbers().add(new RaidRole(4, "DPS"));
                e.getChannel().sendMessage(I18n.getMessage("preformed_raid_added") + "\n" + I18n.getMessage("done_query")).queue();
            } else {
                String[] parts = incomingMessage.split(":");
                if (parts.length < 2) {
                    e.getChannel().sendMessage(I18n.getMessage("preformed_setup_info")).queue();
                } else {
                    try {
                        int amnt = Integer.parseInt(parts[0].trim());
                        String roleName = parts[1].trim();
                        // TODO: Standardize for role : DPS Heal Tank
                        if (roleName.equalsIgnoreCase("DPS")) {
                            roleName = "DPS";
                        } else if (roleName.equalsIgnoreCase("Heal")) {
                            roleName = "Heal";
                        } else if (roleName.equalsIgnoreCase("Tank")) {
                            roleName = "Tank";
                        }

                        raid.getRolesWithNumbers().add(new RaidRole(amnt, roleName));
                        String sb = I18n.getMessage("i_have_added") + " " + amnt + " " + roleName + " " + I18n.getMessage("to_your_raid") + " " + I18n.getMessage("done_query");
                        e.getChannel().sendMessage(sb).queue();
                    } catch (Exception ex) {
                        System.out.println(ex);
                        e.getChannel().sendMessage(I18n.getMessage("role_input_error")).queue();
                    }
                }
            }
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getStepText() {
        return I18n.getMessage("preformed_setup_info");
    }

    /**
     * {@inheritDoc}
     */
    public CreationStep getNextStep() {
        return new RunQueuedRaidStep();
    }
}
