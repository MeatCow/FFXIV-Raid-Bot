package me.cbitler.raidbot.handlers;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.raids.Raid;
import me.cbitler.raidbot.raids.RaidManager;
//import me.cbitler.raidbot.selection.PickFlexRoleStep;
import me.cbitler.raidbot.selection.PickRoleStep;
import me.cbitler.raidbot.selection.SelectionStep;
import me.cbitler.raidbot.utility.Reactions;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.function.Consumer;

public class ReactionHandler extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        Raid raid = RaidManager.getRaid(e.getMessageId());
        if(e.getUser().isBot()) {
            return;
        }
        if (raid != null) {
            if (Reactions.getSpecs().contains(e.getReactionEmote().getEmote().getName())) {
                if(e.getReactionEmote().getEmote().getName().equalsIgnoreCase("X_")) {
                   raid.removeUser(e.getUser().getId());
                } else {
                    if ( !raid.isUserInRaid(e.getUser().getId()) ) {
                        String spec = e.getReactionEmote().getEmote().getName();
                        String role = Reactions.getRoleFromSpec(spec);
                        if(raid.isValidNotFullRole(role)) {
                            // Determine directly role from choosen specs
                            raid.addUser(
                                e.getUser().getId(),
                                e.getMember().getEffectiveName(),
                                spec,
                                role,
                                true,
                                true
                            );
                        } else {
                            e.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Allez, on apprend à compter et on choisi un job dans lequel il reste de la place ?").queue());
                    }
                    } else {
                        e.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Alerte Alzheimer ! Tu as déjà choisi un job... Si tu ne veux plus participer avec ce job utilise le \"X\" rouge.").queue());
                    }
                }
            } else if(e.getReactionEmote().getEmote().getName().equalsIgnoreCase("X_")) {
                raid.removeUser(e.getUser().getId());
            }

            e.getReaction().removeReaction(e.getUser()).queue();
        }
    }

}
