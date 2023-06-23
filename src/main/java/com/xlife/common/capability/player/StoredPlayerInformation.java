package com.xlife.common.capability.player;

public class StoredPlayerInformation implements IPlayerInformation {

    private float health = 0;
    private int ticks = 0;
    private int tickCount;

    public float getMaxHealth() {
        return this.health;
    }

    public void setMaxHealth(float amount) {
        this.health = amount;
    }

    public int getTicksLiving() {
        return this.ticks;
    }

    public void setTicksLiving(int ticks) {
        this.ticks = ticks;
    }

    /** Ticks every second a player is living. */
    public void tick() {
        if (this.tickCount == 20) {
            this.setTicksLiving(this.getTicksLiving() + 1);
            this.tickCount = 0;
        } else {
            ++this.tickCount;
        }
    }

    public void clonePlayerInfo(StoredPlayerInformation info) {
        this.health = info.getMaxHealth();
        this.ticks = info.getTicksLiving();
    }

}