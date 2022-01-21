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
            {"raid_name_setup", "Raid Setup:\nYou can type cancel at anypoint during this process to cancel the raid setup\n\nEnter the name for the raid run:"},
            {"waiting_list_query", "Would you like a waiting list set up for this raid?[yes/no]"},
            {"yes_no_request", "Please answer[Yes/No]:"},
            {"cannot_find_channel", "Please give a valid channel name."},
            {"select_channel", "Enter the channel for raid run announcement:"},
            {"raid_date_query", "Enter the date for the raid run:"},
            {"raid_description", "Enter a description for the raid run:"},
            {"add_minimum_roles", "You must add at least one role."},
            {"preformed_setup_info", "Enter the roles for raid run(format:[amount]:[Role name]).\nUse preformated roles:'D'for Dungeons(4players)or'R'for Raids(8Players). \n\nType done to finish entering roles:"},
            {"preformed_dungeon_added", "Preformed Dungeon roles added!"},
            {"preformed_raid_added", "Preformed Raid roles added!"},
            {"done_query", "If you are done,please enter*done*"},
            {"role_input_error", "Invalid input:Make sure it's in the format of [number]:[role], ie. 1:DPS"},
            {"i_have_added", "I have added"},
            {"to_your_raid", "to your raid."},
            {"raid_time_query", "Enter the time for raid run:"},
            {"raid_cancelled", "Raid cancelled."},
            {"raid_erase_error", "An error occured ending the raid."},
            {"raid_nonexistent", "That raid doesn't exist on this server."},
            {"leader_role_updated", "Raid leader role updated to:"},
            {"raid_creation_cancelled", "Raid creation cancelled."},
            {"raid_creation_confirmed", "Raid created!"},
            {"insufficient_permissions", "Cannot create raid-does the bot have permission to post in the specified channel?"},
            {"role_selection_cancelled", "Role selection cancelled."},
            {"role_full_error", "Please choose a valid role that is not full."},
            {"role_already_picked", "You have already selected a role.Press the X reaction to remove your choice before."},
            {"created_by", "Leader:"},
            {"date_time", "Date/Time"},
            {"roles", "Roles:"},
            {"in_raid", ":on roster\n"},
            {"on_waiting_list", ":on waiting list\n"},
            {"legend", "Legend"},
            {"added_to_raid", "Added to raid!"},
            {"select_a_role", "Please select a role"},
            {"or_hit_cancel", "or hit*cancel*to cancel role selection."},
    };
}
