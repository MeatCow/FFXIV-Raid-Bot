package me.cbitler.raidbot.handlers;

import me.cbitler.raidbot.raids.Raid;
import me.cbitler.raidbot.raids.RaidManager;
import me.cbitler.raidbot.utility.I18n;
import me.cbitler.raidbot.utility.Reactions;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.sql.Timestamp;

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
                   raid.removeUserById(e.getUser().getId());
                } else {
                    if ( !raid.isUserInRaid(e.getUser().getId()) ) {
                        String spec = e.getReactionEmote().getEmote().getName();
                        String role = Reactions.getRoleFromSpec(spec);
                        if(raid.isValidNotFullRole(role)) {
                            // Determine directly role from choosen specs
                            // Timestamp
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            String ordre = String.valueOf(timestamp.getTime());
                            System.out.println("Add " + e.getMember().getEffectiveName() + " to raid for " + spec +"/"+role +" role. Order : " + ordre);
                            raid.addUser(
                                e.getUser().getId(),
                                e.getMember().getEffectiveName(),
                                spec,
                                ordre,
                                true,
                                true
                            );
                        } else {
                            e.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(I18n.getMessage("role_full_error")).queue());
                        }
                    } else {
                        e.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(I18n.getMessage("role_already_picked")).queue());
                    }
                }
            } else if(e.getReactionEmote().getEmote().getName().equalsIgnoreCase("X_")) {
                raid.removeUserById(e.getUser().getId());
            }
            e.getReaction().removeReaction(e.getUser()).queue();
        }
    }
}
