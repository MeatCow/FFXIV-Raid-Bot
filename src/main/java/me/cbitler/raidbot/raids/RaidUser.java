package me.cbitler.raidbot.raids;

import me.cbitler.raidbot.utility.Reactions;

/**
 * Represents a raid user
 * This class is not commented as the method names should be self-explanatory
 *
 * @author Christopher Bitler
 */
public class RaidUser {
    String id;
    String name;
    String spec;
    String ordre;

    public RaidUser(String id, String name, String spec, String ordre) {
        this.id = id;
        this.name = name;
        this.spec = spec;
        this.ordre = ordre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getRole() {
        return Reactions.getRoleFromSpec(spec);
    }

}