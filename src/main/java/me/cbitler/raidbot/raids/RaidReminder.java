package me.cbitler.raidbot.raids;

import java.sql.Time;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class RaidReminder {

    Date date = null;
    Time time = null;
    TimeZone tz = null;

    public RaidReminder(Raid raid) {
        Timer tm = new Timer();


    }

    private class Reminder extends TimerTask {

        public Reminder(Raid raid) {

        }

        @Override
        public void run() {

        }
    }
}
