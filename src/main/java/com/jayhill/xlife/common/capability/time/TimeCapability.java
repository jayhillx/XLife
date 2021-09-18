package com.jayhill.xlife.common.capability.time;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class TimeCapability {
    @CapabilityInject(ITimeCapability.class)
    public static Capability<ITimeCapability> TIME_CAPABILITY = null;

    /** Register Capability. */
    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ITimeCapability.class, new Storage(), DefaultTimeCapability::new);
    }

    public static class Storage implements Capability.IStorage<ITimeCapability> {

        public INBT writeNBT(Capability<ITimeCapability> capability, ITimeCapability instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("time", instance.getTime());
            return tag;
        }

        public void readNBT(Capability<ITimeCapability> capability, ITimeCapability instance, Direction side, INBT nbt) {
            int time = ((CompoundNBT)nbt).getInt("time");
            instance.setTime(time);
        }
    }

}