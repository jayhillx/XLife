package com.xlife.common.capability;

public class DefaultLifeCapability implements ILifeCapability {

    /** Stores the players life information. */
    private float health = 0;
    private String[] cause = new String[]{"", "", "", "", "", "", "", "", "", ""};
    private String[] message = new String[]{"", "", "", "", "", "", "", "", "", ""};
    private String[] timeLasted = new String[]{"", "", "", "", "", "", "", "", "", ""};
    private int timeLiving = 0;

    public float getMaxHealth() {
        return this.health;
    }

    public void setMaxHealth(float health) {
        this.health = health;
    }

    public String[] getCause() {
        return this.cause;
    }

    public void setCause(String[] cause) {
        this.cause = cause;
    }

    public String[] getMessage() {
        return this.message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }

    public String[] getTimeLasted() {
        return this.timeLasted;
    }

    public void setTimeLasted(String[] time) {
        this.timeLasted = time;
    }

    public int getTimeLiving() {
        return this.timeLiving;
    }

    public void setTimeLiving(int time) {
        this.timeLiving = time;
    }

    public void onClone(DefaultLifeCapability respawn) {
        this.health = respawn.getMaxHealth();
        this.cause = respawn.getCause();
        this.message = respawn.getMessage();
        this.timeLasted = respawn.getTimeLasted();
        this.timeLiving = respawn.getTimeLiving();
    }

}