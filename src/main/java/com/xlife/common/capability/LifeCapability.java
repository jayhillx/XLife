package com.xlife.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.Arrays;

public class LifeCapability {
    @CapabilityInject(ILifeCapability.class)
    public static Capability<ILifeCapability> X_LIFE_CAPABILITIES;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ILifeCapability.class, new Storage(), DefaultLifeCapability::new);
    }

    private static class Storage implements Capability.IStorage<ILifeCapability> {
        public INBT writeNBT(Capability<ILifeCapability> capability, ILifeCapability life, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putFloat("health", life.getMaxHealth());
            tag.putString("cause", Arrays.toString(life.getCause()).replace("]", "").replace("[", ""));
            tag.putString("message", Arrays.toString(life.getMessage()).replace("]", "").replace("[", ""));
            tag.putString("timeLasted", Arrays.toString(life.getTimeLasted()).replace("]", "").replace("[", ""));
            tag.putFloat("timeLiving", life.getTimeLiving());

            return tag;
        }

        public void readNBT(Capability<ILifeCapability> capability, ILifeCapability life, Direction side, INBT nbt) {
            CompoundNBT tag = (CompoundNBT) nbt;
            if (tag.contains("health")) {
                life.setMaxHealth(tag.getFloat("health"));
            } if (tag.contains("cause")) {
                String[] causeArray = tag.getString("cause").split(", ");
                life.setCause(causeArray);
            } if (tag.contains("message")) {
                String[] messageArray = tag.getString("message").split(", ");
                life.setMessage(messageArray);
            } if (tag.contains("timeLasted")) {
                String[] timeArray = tag.getString("timeLasted").split(", ");
                life.setTimeLasted(timeArray);
            } if (tag.contains("timeLiving")) {
                life.setTimeLiving(tag.getInt("timeLiving"));
            }
        }
    }

}