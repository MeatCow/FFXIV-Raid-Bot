package me.cbitler.raidbot.res;

import java.util.Enumeration;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

public class MessageBundle extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return resources;
    }

    private final Object[][] resources = {
            {"raid_name_setup", "Raid Setup\nYou can type *cancel* at any point during this process to cancel the raid setup\n\nEnter the name for the raid run:"},
            {"waiting_list_query", "Would you like a waiting list set up for this raid? [Yes/No]"},
            {"yes_no_request", "Please answer [Yes/No]:"},
            {"cannot_find_channel", "Please give a valid channel name."},
            {"select_channel", "Enter the channel for raid run announcement:"},
            {"raid_date_query", "Enter the date for the raid run:"},
            {"raid_description", "Enter a description for the raid run:"},
            {"add_minimum_roles", "You must add at least one role."},
            {"preformed_setup_info", "Enter the roles for your raid run, in the format of [Max] : [DPS|Heal|Tank].\n" +
                    "You can also use preformatted roles:\n" +
                    "- 'D' for Dungeons (4 players, 1 Tank, 1 Heal, 2 DPS)\n" +
                    "- 'R' for Raids (8 players, 2 Tank, 2 Heal, 4 DPS).\n\n" +
                    "Enter *done*, when you are finished entering roles:"},
            {"preformed_dungeon_added", "Preformed dungeon roles added!"},
            {"preformed_raid_added", "Preformed raid roles added!"},
            {"done_query", "If you are done, please enter *done*"},
            {"role_input_error", "Invalid input : Make sure it's in the format of [number]:[role], ie. 1:DPS"},
            {"i_have_added", "I have added"},
            {"to_your_raid", "to your raid."},
            {"raid_time_query", "Enter the time for raid run:"},
            {"raid_cancelled", "Raid cancelled."},
            {"raid_erase_error", "An error occurred ending the raid."},
            {"raid_nonexistent", "That raid doesn't exist on this server."},
            {"leader_role_updated", "Raid leader role updated to:"},
            {"raid_creation_cancelled", "Raid creation cancelled."},
            {"raid_creation_confirmed", "Raid created!"},
            {"insufficient_permissions", "Cannot create raid - Does the bot have permission to post in the specified channel?"},
            {"role_selection_cancelled", "Role selection cancelled."},
            {"role_full_error", "Please choose a valid role that is not full."},
            {"role_already_picked", "You have already selected a role. React using X before selecting a new role."},
            {"created_by", "Leader:"},
            {"date_time", "Date/Time"},
            {"roles", "Roles:"},
            {"in_raid", ": on roster\n"},
            {"on_waiting_list", ": on waiting list\n"},
            {"legend", "Legend"},
            {"added_to_raid", "Added to raid!"},
            {"select_a_role", "Please select a role"},
            {"or_hit_cancel", "or hit *cancel* to cancel role selection."},
    };
}
