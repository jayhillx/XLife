package com.jayhill.xlife.common.capability.stats;

public interface IStatsCapability {

    void setCause(String[] cause);

    void setTime(String[] time);

    String[] getCause();

    String[] getTime();

    void onDeath(DefaultStatsCapability respawn);

}