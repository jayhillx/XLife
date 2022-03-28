package com.xlife.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LifeCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    private final DefaultLifeCapability stats = new DefaultLifeCapability();
    final LazyOptional<ILifeCapability> statsOptional = LazyOptional.of(() -> stats);

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LifeCapability.X_LIFE_CAPABILITIES.orEmpty(cap, this.statsOptional);
    }

    public CompoundNBT serializeNBT() {
        return LifeCapability.X_LIFE_CAPABILITIES == null ? new CompoundNBT() : (CompoundNBT) LifeCapability.X_LIFE_CAPABILITIES.writeNBT(stats, null);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        if (LifeCapability.X_LIFE_CAPABILITIES != null) {
            LifeCapability.X_LIFE_CAPABILITIES.readNBT(stats, null, nbt);
        }
    }

}