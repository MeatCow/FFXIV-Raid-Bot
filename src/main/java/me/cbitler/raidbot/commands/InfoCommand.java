package me.cbitler.raidbot.commands;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class InfoCommand implements Command {
    private final String information = "\n\n**FFXIV-Raid-Bot Information**:\n" +
            "Original GW2 version author: VoidWhisperer#5905\n" +
            "Inspired by TESO adaptation by : Acheron#3134\n" +
            "FFXIV's adaptation by: Yrline#3486 and updated to Endwalker by MeatCow#0713";
    @Override
    public void handleCommand(String command, String[] args, TextChannel channel, User author) {
        channel.sendMessage(information).queue();
    }
}
