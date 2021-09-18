package com.jayhill.xlife.common.capability.stats;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StatsCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    public static final DefaultStatsCapability stats = new DefaultStatsCapability();
    final LazyOptional<IStatsCapability> statsOptional = LazyOptional.of(() -> stats);

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return StatsCapability.STATS_CAPABILITY.orEmpty(cap, this.statsOptional);
    }

    public CompoundNBT serializeNBT() {
        return StatsCapability.STATS_CAPABILITY == null ? new CompoundNBT() : (CompoundNBT)StatsCapability.STATS_CAPABILITY.writeNBT(stats, null);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        if (StatsCapability.STATS_CAPABILITY != null) {
            StatsCapability.STATS_CAPABILITY.readNBT(stats, null, nbt);
        }
    }

}