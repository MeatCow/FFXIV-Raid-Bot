package me.cbitler.raidbot.raids;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Raid in the midst of being set up.
 * Can be turned into Raid with the RaidManager.createRaid(PendingRaid) function
 *
 * @author Christopher Bitler
 */
public class PendingRaid {
    String name, description, announcementChannel, serverId, leaderName;
    ZonedDateTime dateTime;
    ZonedDateTime reminderTime;
    boolean hasWaitingList = false;
    List<RaidRole> rolesWithNumbers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDate(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getAnnouncementChannel() {
        return announcementChannel;
    }

    public void setAnnouncementChannel(String announcementChannel) {
        this.announcementChannel = announcementChannel;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public List<RaidRole> getRolesWithNumbers() {
        return rolesWithNumbers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWaitingList(boolean hasWaitingList) {
        this.hasWaitingList = hasWaitingList;
    }

    public boolean hasWaitingList() {
        return hasWaitingList;
    }

    public void disableReminder() {
        reminderTime = null;
    }

    public void setReminder(ZonedDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public ZonedDateTime getReminderTime() {
        return reminderTime;
    }
}
