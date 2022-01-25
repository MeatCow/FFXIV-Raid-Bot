package me.cbitler.raidbot.commands;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class HelpCommand implements Command {
    private final String helpMessage = "FFXIV-Raid-Bot Help:\n" +
            "Commands:\n" +
            "**!setRaidLeaderRole [role]** - Set the role that serves as a raid leader. This is only usable by people with the manage server permission\n" +
            "**!createRaid** - Start the raid creation process. Usable by people with the raid leader role\n" +
            "**!removeFromRaid [raid id] [name]** - Remove a player from a raid. Only usable by raid leaders\n" +
            "**!endRaid [raid id]** - End a raid, removing the message. Only usable by raid leaders\n" +
            "**!help** - You are looking at it\n" +
            "**!info** - Information about the bot and it's authors\n" +
            "\n\n" +
            "Help information:\n" +
            "To use this bot, set the raid leader role, and then anyone with that role can use !createRaid. This will take them through" +
            " a raid setup process with the bot prompting them for information. After that, it will post the raid in the channel specified" +
            " Once that is there, people can join it by clicking on the reaction for their specialization";
    @Override
    public void handleCommand(String command, String[] args, TextChannel channel, User author) {
        channel.sendMessage(helpMessage).queue();
    }
}
