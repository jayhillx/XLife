package com.jayhill.xlife.common.capability.health;

public class DefaultHealthCapability implements IHealthCapability {

    /** Stores a number to set players max health. */
    private float maxHealth = 0;

    public float getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(float health) {
        this.maxHealth = health;
    }

    public void onClone(DefaultHealthCapability clone) {
        this.maxHealth = clone.getMaxHealth();
    }
    
}