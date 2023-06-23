package com.xlife.common.capability.player;

/**
 * Stores health & time living on the player.
 */
public interface IPlayerInformation {

    float getMaxHealth();

    void setMaxHealth(float amount);

    int getTicksLiving();

    void setTicksLiving(int ticks);

    void tick();

    void clonePlayerInfo(StoredPlayerInformation info);

}