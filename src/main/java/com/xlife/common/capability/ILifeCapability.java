package com.xlife.common.capability;

/** Health, Cause & Message, Time Living & Lasted. */
public interface ILifeCapability {

    float getMaxHealth();

    void setMaxHealth(float health);

    String[] getCause();

    void setCause(String[] cause);

    String[] getMessage();

    void setMessage(String[] message);

    String[] getTimeLasted();

    void setTimeLasted(String[] time);

    int getTimeLiving();

    void setTimeLiving(int time);

    void onClone(DefaultLifeCapability respawn);

}