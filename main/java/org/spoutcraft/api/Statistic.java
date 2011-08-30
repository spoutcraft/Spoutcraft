package org.spoutcraft.api;

import java.util.HashMap;
import java.util.Map;

public enum Statistic {
    DAMAGE_DEALT(2020),
    DAMAGE_TAKEN(2021),
    DEATHS(2022),
    MOB_KILLS(2023),
    PLAYER_KILLS(2024),
    FISH_CAUGHT(2025),
    MINE_BLOCK(16777216, true),
    USE_ITEM(6908288, false),
    BREAK_ITEM(16973824, true);

    private final static Map<Integer, Statistic> statistics = new HashMap<Integer, Statistic>();
    private final int id;
    private final boolean isSubstat;
    private final boolean isBlock;

    private Statistic(int id) {
        this(id, false, false);
    }

    private Statistic(int id, boolean isBlock) {
        this(id, true, isBlock);
    }

    private Statistic(int id, boolean isSubstat, boolean isBlock) {
        this.id = id;
        this.isSubstat = isSubstat;
        this.isBlock = isBlock;
    }

    public int getId() {
        return id;
    }

    public boolean isSubstatistic() {
        return isSubstat;
    }

    public boolean isBlock() {
        return isSubstat && isBlock;
    }

    public static Statistic getStatistic(int id) {
        return statistics.get(id);
    }

    static {
        for (Statistic stat : values()) {
            statistics.put(stat.getId(), stat);
        }
    }
}

