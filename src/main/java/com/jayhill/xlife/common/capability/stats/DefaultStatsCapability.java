package com.jayhill.xlife.common.capability.stats;

public class DefaultStatsCapability implements IStatsCapability {

    /** Stores the players life information. */
    private String[] cause = new String[]{"none", "none", "none", "none", "none", "none", "none", "none", "none", "none"};
    private String[] message = new String[]{"none", "none", "none", "none", "none", "none", "none", "none", "none", "none"};
    private String[] time = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};

    public String[] getCause() {
        return this.cause;
    }

    public String[] getMessage() {
        return this.message;
    }

    public String[] getTime() {
        return this.time;
    }

    public void setCause(String[] cause) {
        this.cause = cause;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public void onClone(DefaultStatsCapability respawn) {
        this.cause = respawn.getCause();
        this.time = respawn.getTime();
        this.message = respawn.getMessage();
    }

}