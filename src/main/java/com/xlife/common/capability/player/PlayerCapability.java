package com.xlife.common.capability.player;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerCapability {
    @CapabilityInject(IPlayerInformation.class)
    public static Capability<IPlayerInformation> PLAYER_DATA;

    public static class Storage implements Capability.IStorage<IPlayerInformation> {
        public INBT writeNBT(Capability<IPlayerInformation> capability, IPlayerInformation info, Direction side) {
            CompoundNBT nbt = new CompoundNBT();

            nbt.putFloat("Health", info.getMaxHealth());
            nbt.putInt("TimeLiving", info.getTicksLiving());
            return nbt;
        }

        public void readNBT(Capability<IPlayerInformation> capability, IPlayerInformation info, Direction side, INBT inbt) {
            CompoundNBT nbt = ((CompoundNBT)inbt);

            info.setMaxHealth(nbt.getFloat("Health"));
            info.setTicksLiving(nbt.getInt("TimeLiving"));
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        private final StoredPlayerInformation info = new StoredPlayerInformation();
        final LazyOptional<IPlayerInformation> infoOptional = LazyOptional.of(() -> info);

        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return PLAYER_DATA.orEmpty(cap, this.infoOptional);
        }

        public CompoundNBT serializeNBT() {
            return PLAYER_DATA == null ? new CompoundNBT() : (CompoundNBT)PLAYER_DATA.writeNBT(info, null);
        }

        public void deserializeNBT(CompoundNBT nbt) {
            if (PLAYER_DATA != null) {
                PLAYER_DATA.readNBT(info, null, nbt);
            }
        }
    }

}