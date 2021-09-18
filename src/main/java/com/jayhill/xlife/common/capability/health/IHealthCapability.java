package com.jayhill.xlife.common.capability.health;

public interface IHealthCapability {

    void setMaxHealth(float health);

    float getMaxHealth();

    void onDeath(IHealthCapability respawn);

}