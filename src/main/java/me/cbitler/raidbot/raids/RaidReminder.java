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
     * @param reminderTime The time at which the reminder is sent to the Discord channel. If in the past, no reminder is sent
     */
    public RaidReminder(Raid raid, ZonedDateTime reminderTime) throws IllegalArgumentException {
        if (reminderTime == null) {
            throw new IllegalArgumentException("Reminder time must not be null");
        }
        if (reminderTime.isBefore(ZonedDateTime.now())) {
            return;
        }
        this.reminderTime = reminderTime;
        tm = new Timer();

        long delay = calculateDelay(this.reminderTime).toMillis();

        if (delay > 0) {
            tm.schedule(new Reminder(raid), delay);
        }
    }

    /**
     * @return The time at which the reminder will go off.
     */
    public ZonedDateTime getReminderTime() {
        return reminderTime;
    }

    /**
     * Calculates the time between now and the argument
     *
     * @param otherTime The time until which you need to calculate the difference
     * @return Difference between now and argument
     */
    private Duration calculateDelay(ZonedDateTime otherTime) {
        return Duration.between(ZonedDateTime.now(), otherTime);
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
            message.append("\nScheduled raid happening in " + calculateDelay(raid.getRaidTime()).toMinutes() + " minutes");
            raid.getAnnouncementChannel().sendMessage(message.toString()).queue();

        }
    }

}
