package com.jayhill.xlife.common.capability.time;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class TimeCapability {
    @CapabilityInject(ITimeCapability.class)
    public static Capability<ITimeCapability> TIME_CAPABILITY;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ITimeCapability.class, new Storage(), DefaultTimeCapability::new);
    }

    private static class Storage implements Capability.IStorage<ITimeCapability> {
        public INBT writeNBT(Capability<ITimeCapability> capability, ITimeCapability time, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("time", time.getStoredTime());

            return tag;
        }

        public void readNBT(Capability<ITimeCapability> capability, ITimeCapability time, Direction side, INBT nbt) {
            CompoundNBT tag = (CompoundNBT) nbt;
            if (tag.contains("time")) {
                time.setStoredTime(tag.getInt("time"));
            }
        }
    }

}