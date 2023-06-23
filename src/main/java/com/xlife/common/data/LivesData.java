package com.xlife.common.data;

import net.minecraft.nbt.CompoundNBT;

public class LivesData {
    private int hearts;
    private int timeInSeconds;
    private String time;
    private String cause;
    private String message;

    public LivesData(CompoundNBT nbt) {
        this.hearts = nbt.getInt("Hearts");
        this.timeInSeconds = nbt.getInt("TimeInSeconds");
        this.time = nbt.getString("Time");
        this.cause = nbt.getString("Cause");
        this.message = nbt.getString("Message");
    }

    public int getHearts() {
        return this.hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getTimeInSeconds() {
        return this.timeInSeconds;
    }

    public void setTimeInSeconds(int time) {
        this.timeInSeconds = time;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCauseOfDeath() {
        return this.cause;
    }

    public void setCauseOfDeath(String cause) {
        this.cause = cause;
    }

    public String getDeathMessage() {
        return this.message;
    }

    public void setDeathMessage(String message) {
        this.message = message;
    }

    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putInt("Hearts", this.hearts);
        nbt.putInt("TimeInSeconds", this.timeInSeconds);
        nbt.putString("Time", this.time);
        nbt.putString("Cause", this.cause);
        nbt.putString("Message", this.message);
        return nbt;
    }

}