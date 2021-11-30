package com.jayhill.xlife.common.capability.time;

public interface ITimeCapability {

    int getStoredTime();

    void setStoredTime(int time);

    void onClone(DefaultTimeCapability respawn);

}