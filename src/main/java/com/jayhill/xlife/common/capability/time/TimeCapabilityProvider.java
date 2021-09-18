package com.jayhill.xlife.common.capability.time;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TimeCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    private final DefaultTimeCapability time = new DefaultTimeCapability();
    final LazyOptional<ITimeCapability> timeOptional = LazyOptional.of(() -> time);

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return TimeCapability.TIME_CAPABILITY.orEmpty(cap, this.timeOptional);
    }

    public CompoundNBT serializeNBT() {
        return TimeCapability.TIME_CAPABILITY == null ? new CompoundNBT() : (CompoundNBT)TimeCapability.TIME_CAPABILITY.writeNBT(time, null);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        if (TimeCapability.TIME_CAPABILITY != null) {
            TimeCapability.TIME_CAPABILITY.readNBT(time, null, nbt);
        }
    }

}