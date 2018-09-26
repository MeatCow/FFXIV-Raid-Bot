package me.cbitler.raidbot.utility;

import me.cbitler.raidbot.RaidBot;
import net.dv8tion.jda.core.entities.Emote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reactions {
    /**
     * List of reactions representing classes
     */
    static String[] specs = {
            "Paladin", //387295988282556417
            "Warrior", //387296167958151169
            "DarkKnight", //387296053659172869
            "WhiteMage", //387296192381321226
            "Scholar", //387296013947502592
            "Astrologian", //387296212421836800
            "Bard", //387296081823662081
            "Dragoon", //387296176770121738
            "Monk", // 387296044716916738
            "BlackMage", //387296205488521216
            "Summoner", //387296029533274113
            "Ninja", //387296159716081664
            "Machinist", //387296219988361218
            "RedMage", //387296089340117002
            "Samurai", //387296021710897152
            //"", //387296184114610185
            //"", //387296061997318146
            //"" //387296198928891905
    };

    static Emote[] reactions = {
            getEmoji("387295988282556417"), //
            getEmoji("387296167958151169"), //
            getEmoji("387296053659172869"), //
            getEmoji("387296192381321226"), //
            getEmoji("387296013947502592"), //
            getEmoji("387296212421836800"), //
            getEmoji("387296081823662081"), //
            getEmoji("387296176770121738"), //
            getEmoji("387296044716916738"), //
            getEmoji("387296205488521216"), //
            getEmoji("387296029533274113"), //
            getEmoji("387296159716081664"), //
            getEmoji("387296219988361218"), //
            getEmoji("387296089340117002"), //
            getEmoji("387296021710897152"), //
            //getEmoji("387296184114610185"), // Mirage
            //getEmoji("387296061997318146"), // Reaper
            //getEmoji("387296198928891905"), // Scourge
            getEmoji("387346852867211274") // X_
    };

    /**
     * Get an emoji from it's emote ID via JDA
     * @param id The ID of the emoji
     * @return The emote object representing that emoji
     */
    private static Emote getEmoji(String id) {
        return RaidBot.getInstance().getJda().getEmoteById(id);
    }

    /**
     * Get the list of reaction names as a list
     * @return The list of reactions as a list
     */
    public static List<String> getSpecs() {
        return new ArrayList<>(Arrays.asList(specs));
    }

    /**
     * Get the list of emote objects
     * @return The emotes
     */
    public static List<Emote> getEmotes() {
        return new ArrayList<>(Arrays.asList(reactions));
    }


}
