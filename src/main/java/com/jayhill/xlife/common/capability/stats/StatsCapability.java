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
    public static Capability<IStatsCapability> STATS_CAPABILITY;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(IStatsCapability.class, new Storage(), DefaultStatsCapability::new);
    }

    private static class Storage implements Capability.IStorage<IStatsCapability> {
        public INBT writeNBT(Capability<IStatsCapability> capability, IStatsCapability stats, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("cause", Arrays.toString(stats.getCause()).replace("]", "").replace("[", ""));
            tag.putString("message", Arrays.toString(stats.getMessage()).replace("]", "").replace("[", ""));
            tag.putString("time", Arrays.toString(stats.getTime()).replace("]", "").replace("[", ""));

            return tag;
        }

        public void readNBT(Capability<IStatsCapability> capability, IStatsCapability stats, Direction side, INBT nbt) {
            CompoundNBT tag = (CompoundNBT) nbt;
            if (tag.contains("cause")) {
                String[] causeArray = tag.getString("cause").split(", ");
                stats.setCause(causeArray);
            }
            if (tag.contains("message")) {
                String[] messageArray = tag.getString("message").split(", ");
                stats.setMessage(messageArray);
            }
            if (tag.contains("time")) {
                String[] timeArray = tag.getString("time").split(", ");
                stats.setTime(timeArray);
            }
        }
    }

}