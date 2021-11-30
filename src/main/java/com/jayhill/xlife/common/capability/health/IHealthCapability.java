package com.jayhill.xlife.common.capability.health;

public interface IHealthCapability {

    float getMaxHealth();

    void setMaxHealth(float health);

    void onClone(DefaultHealthCapability respawn);

}