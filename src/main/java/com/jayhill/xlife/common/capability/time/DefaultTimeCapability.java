package com.jayhill.xlife.common.capability.time;

public class DefaultTimeCapability implements ITimeCapability {

    /** Stores the time the player has been alive. */
    private int time;

    public int getStoredTime() {
        return this.time;
    }

    public void setStoredTime(int time) {
        this.time = time;
    }

    public void onClone(DefaultTimeCapability respawn) {
        this.time = respawn.getStoredTime();
    }

}