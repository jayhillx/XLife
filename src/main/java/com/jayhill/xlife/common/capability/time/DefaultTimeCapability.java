package com.jayhill.xlife.common.capability.time;

public class DefaultTimeCapability implements ITimeCapability {

    /** Stores the time player has been alive. */
    private int time;

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    public void onDeath(DefaultTimeCapability respawn) {
        this.time = respawn.time;
    }

}