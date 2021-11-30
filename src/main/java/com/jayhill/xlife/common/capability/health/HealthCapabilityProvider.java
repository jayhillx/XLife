package com.jayhill.xlife.common.capability.health;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HealthCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    private final DefaultHealthCapability health = new DefaultHealthCapability();
    final LazyOptional<IHealthCapability> healthOptional = LazyOptional.of(() -> health);

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return HealthCapability.HEALTH_CAPABILITY.orEmpty(cap, healthOptional);
    }

    public CompoundNBT serializeNBT() {
        return HealthCapability.HEALTH_CAPABILITY == null ? new CompoundNBT() : (CompoundNBT)HealthCapability.HEALTH_CAPABILITY.writeNBT(health, null);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        if (HealthCapability.HEALTH_CAPABILITY != null) {
            HealthCapability.HEALTH_CAPABILITY.readNBT(health, null, nbt);
        }
    }

}