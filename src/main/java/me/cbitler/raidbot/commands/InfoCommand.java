package me.cbitler.raidbot.commands;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class InfoCommand implements Command {
    private final String information = "GW2-Raid-Bot Information:\n" +
            "Author: VoidWhisperer#5905\n" +
            "Modified by: Yrline#3486\n" +
            "Changed inspired by: Acheron#3134\n" +
            "Send Gils to Yrline @ Cerberus !";

    @Override
    public void handleCommand(String command, String[] args, TextChannel channel, User author) {
        channel.sendMessage(information).queue();
    }
}
