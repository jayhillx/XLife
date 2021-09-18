package com.jayhill.xlife.common.capability.time;

public interface ITimeCapability {

    void setTime(int time);

    int getTime();

    void onDeath(DefaultTimeCapability respawn);

}