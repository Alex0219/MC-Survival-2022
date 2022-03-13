package de.fileinputstream.mytraz.worldmanagement.rank;

import java.util.ArrayList;

/**
 * Created by Alexander on 04.06.2020 20:57
 * © 2020 Alexander Fiedler
 */
public enum RankEnum {

    SUPERADMIN("superadmin", 5, 4,"§4Admin §7| §4"),
    ADMIN("admin", 4, 3,"§4Admin §7| §4"),
    MOD("mod", 3, 2,"§cMod §7| §c"),
    VIP("vip", 2, 1,"§bVIP §7| §b"),
    STAMMSPIELER("stammspieler", 1, 1,"§6Stammspieler §7| §6"),
    SPIELER("spieler", 0, 1,"§a");

    String name;
    int id;
    int rankLevel;
    String prefix;

    RankEnum(final String name, final int id, final int rankLevel, String prefix) {
        this.name = name;
        this.id = id;
        this.rankLevel = rankLevel;
        this.prefix = prefix;
    }

    public static RankEnum getRankByName(final String rankName) {
        for (RankEnum ranks : values()) {
            if (ranks.getName().equalsIgnoreCase(rankName)) {
                return ranks;
            }
        }
        return null;
    }

    public static ArrayList<RankEnum> getAllRanks() {
        final ArrayList<RankEnum> tempRanks = new ArrayList<>();
        for (RankEnum rank : values()) {
            tempRanks.add(rank);
        }
        return tempRanks;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    //developer
    //DEveLOPer

    public int getRankLevel() {
        return rankLevel;
    }

    public String getPrefix() {
        return prefix;
    }
}
