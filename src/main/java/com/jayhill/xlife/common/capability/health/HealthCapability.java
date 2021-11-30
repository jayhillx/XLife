package com.jayhill.xlife.common.capability.health;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class HealthCapability {
    @CapabilityInject(IHealthCapability.class)
    public static Capability<IHealthCapability> HEALTH_CAPABILITY;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(IHealthCapability.class, new Storage(), DefaultHealthCapability::new);
    }

    private static class Storage implements Capability.IStorage<IHealthCapability> {
        public INBT writeNBT(Capability<IHealthCapability> capability, IHealthCapability health, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putFloat("health", health.getMaxHealth());

            return tag;
        }

        public void readNBT(Capability<IHealthCapability> capability, IHealthCapability health, Direction side, INBT nbt) {
            CompoundNBT tag = (CompoundNBT) nbt;
            if (tag.contains("health")) {
                health.setMaxHealth(tag.getFloat("health"));
            }
        }
    }

}