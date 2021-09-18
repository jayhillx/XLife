package com.jayhill.xlife.common.capability.stats;

public class DefaultStatsCapability implements IStatsCapability {

    /** Stores the cause of death and time player lasted per life. */
    private String[] cause = new String[]{"none", "none", "none", "none", "none", "none", "none", "none", "none", "none"};
    private String[] time = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};

    public void setCause(String[] cause) {
        this.cause = cause;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public String[] getCause() {
        return cause;
    }

    public String[] getTime() {
        return time;
    }

    public void onDeath(DefaultStatsCapability respawn) {
        this.cause = respawn.getCause();
        this.time = respawn.getTime();
    }

}