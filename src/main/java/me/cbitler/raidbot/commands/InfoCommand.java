package me.cbitler.raidbot.commands;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class InfoCommand implements Command {
    private final String information = "FFXIV-Raid-Bot Information:\n" +
            "Original GW2 version Author: VoidWhisperer#5905 , " +
            "Inspired by TESO adaptation by : Acheron#3134\n\n" +
            "FFXIV's adaptation by: Yrline#3486\n\n" +
            "Wanna please me ? Send me Gils !! ==> Yrline @ Cerberus ;) \n";
    @Override
    public void handleCommand(String command, String[] args, TextChannel channel, User author) {
        channel.sendMessage(information).queue();
    }
}
