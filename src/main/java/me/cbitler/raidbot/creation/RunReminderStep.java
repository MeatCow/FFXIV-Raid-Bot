package me.cbitler.raidbot.creation;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.database.Database;
import me.cbitler.raidbot.raids.PendingRaid;
import me.cbitler.raidbot.utility.I18n;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

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
                    ZonedDateTime time = ZonedDateTime.parse(decision, Database.TIME_FORMAT);
                    raid.setReminder(time);
                    return true;
                } catch (DateTimeParseException ex) {
                    e.getChannel().sendMessage(I18n.getMessage("could_not_parse_date")).queue();
                    System.out.println(ex.getMessage());
                    System.out.println(ex.getParsedString());
                    return false;
                }
        }

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
