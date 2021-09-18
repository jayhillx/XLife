package com.jayhill.xlife.common.capability.health;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class HealthCapability {
    @CapabilityInject(IHealthCapability.class)
    public static Capability<IHealthCapability> HEALTH_CAPABILITY = null;

    /** Register Capability. */
    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(IHealthCapability.class, new Storage(), DefaultHealthCapability::new);
    }

    public static class Storage implements Capability.IStorage<IHealthCapability> {

        public INBT writeNBT(Capability<IHealthCapability> capability, IHealthCapability instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putFloat("health", instance.getMaxHealth());

            return tag;
        }

        public void readNBT(Capability<IHealthCapability> capability, IHealthCapability instance, Direction side, INBT nbt) {
            CompoundNBT tag = (CompoundNBT) nbt;
            if (tag.contains("health")) {
                instance.setMaxHealth(tag.getFloat("health"));
            }
        }
    }

}