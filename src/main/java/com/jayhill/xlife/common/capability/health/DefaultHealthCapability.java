package com.jayhill.xlife.common.capability.health;

public class DefaultHealthCapability implements IHealthCapability {

    /** Players stored max health. */
    private float maxHealth = 0;

    public void setMaxHealth(float health) {
        this.maxHealth = health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void onDeath(IHealthCapability respawn) {
        this.maxHealth = respawn.getMaxHealth();
    }
    
}