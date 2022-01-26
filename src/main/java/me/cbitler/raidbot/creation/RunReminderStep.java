package me.cbitler.raidbot.creation;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.raids.PendingRaid;
import me.cbitler.raidbot.utility.I18n;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunReminderStep implements CreationStep {

    @Override
    public boolean handleDM(PrivateMessageReceivedEvent e) {
        String decision = e.getMessage().getRawContent().trim();

        RaidBot bot = RaidBot.getInstance();
        PendingRaid raid = bot.getPendingRaids().get(e.getAuthor().getId());

        switch (decision) {
            case "no":
            case "n":
            case "non":
                raid.disableReminder();
                return true;
            default:
                try {
                    Pattern pat = Pattern.compile("^((\\d*)h)?((\\d*)m?)?$"); // G2 = hours, G4 = minutes
                    Matcher m = pat.matcher(decision);

                    if (m.find()) {
                        String hours = removeEmpty(m.group(2));
                        String minutes = removeEmpty(m.group(4));
                        Duration reminderTime = Duration.parse(String.format("PT%sH%sM", hours, minutes));
                        raid.setReminder(raid.getDateTime().minus(reminderTime));
                        return true;
                    }
                    return false;
                } catch (DateTimeParseException ex) {
                    e.getChannel().sendMessage(I18n.getMessage("could_not_parse_date")).queue();
                    System.out.println(ex.getMessage());
                    return false;
                }
        }

    }

    private String removeEmpty(String hours) {
        if (hours != null && !hours.isEmpty()) {
            return hours;
        }
        return "0";
    }

    @Override
    public CreationStep getNextStep() {
        return new RunRoleSetupStep();
    }

    @Override
    public String getStepText() {
        return I18n.getMessage("raid_reminder_query");
    }
}
