package com.jayhill.xlife.common.capability.stats;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.Arrays;

public class StatsCapability {
    @CapabilityInject(IStatsCapability.class)
    public static Capability<IStatsCapability> STATS_CAPABILITY = null;

    /** Register Capability. */
    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(IStatsCapability.class, new Storage(), DefaultStatsCapability::new);
    }

    public static class Storage implements Capability.IStorage<IStatsCapability> {

        public INBT writeNBT(Capability<IStatsCapability> capability, IStatsCapability instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            if (instance.getCause() != null) {
                tag.putString("time", Arrays.toString(instance.getTime()).replace("]", "").replace("[", ""));
                tag.putString("cause", Arrays.toString(instance.getCause()).replace("]", "").replace("[", ""));
            }
            return tag;
        }

        public void readNBT(Capability<IStatsCapability> capability, IStatsCapability instance, Direction side, INBT nbt) {
            String time = ((CompoundNBT)nbt).getString("time");
            String[] timeArray = time.split(", ");
            instance.setTime(timeArray);

            String cause = ((CompoundNBT)nbt).getString("cause");
            String[] causeArray = cause.split(", ");
            instance.setCause(causeArray);
        }
    }

}