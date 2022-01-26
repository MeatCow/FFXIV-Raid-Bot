package me.cbitler.raidbot.raids;

import me.cbitler.raidbot.RaidBot;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class RaidReminder {

    ZonedDateTime reminderTime;
    Timer tm;

    /**
     * Creates a reminder that will be sent to the raid's Discord channel at the specified time.
     * Will Mention all the raid members in the message.
     *
     * @param raid         The raid instance for which this is sending out a message.
     * @param reminderTime The time at which the reminder is sent to the Discord channel
     */
    public RaidReminder(Raid raid, ZonedDateTime reminderTime) throws IllegalArgumentException {
        if (reminderTime == null) {
            throw new IllegalArgumentException("Reminder time must not be null");
        }
        this.reminderTime = reminderTime;
        tm = new Timer();
        tm.schedule(new Reminder(raid), calculateDelay(this.reminderTime));
    }

    /**
     * @return The time at which the reminder will go off.
     */
    public ZonedDateTime getReminderTime() {
        return reminderTime;
    }

    /**
     * Calculates the time (in ms) to the reminder
     *
     * @param reminderTime When you want the reminder sent
     * @return Time in ms until the reminder needs to be sent
     */
    private long calculateDelay(ZonedDateTime reminderTime) {
        long delay = Duration.between(ZonedDateTime.now(), reminderTime).toMillis();
        return (delay > 0) ? delay : 0;
    }

    private class Reminder extends TimerTask {
        private Raid raid;

        public Reminder(Raid raid) {
            this.raid = raid;
        }

        @Override
        public void run() {
            StringBuilder message = new StringBuilder();

            for (RaidUser r : raid.getRaiders()) {
                message.append(RaidBot.getInstance().getJda().getUserById(r.getId()).getAsMention());
            }
            message.append("\nScheduled raid happening soon!");
            raid.getAnnouncementChannel().sendMessage(message.toString()).queue();

        }
    }

}
