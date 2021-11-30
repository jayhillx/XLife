package com.jayhill.xlife.common.capability.stats;

public interface IStatsCapability {

    String[] getCause();

    String[] getMessage();

    String[] getTime();

    void setCause(String[] cause);

    void setMessage(String[] message);

    void setTime(String[] time);

    void onClone(DefaultStatsCapability respawn);

}