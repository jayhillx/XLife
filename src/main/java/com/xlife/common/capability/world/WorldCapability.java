package com.xlife.common.capability.world;

import com.xlife.common.data.PlayerInformationData;
import com.xlife.common.data.PodiumData;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WorldCapability {
    @CapabilityInject(IWorldInformation.class)
    public static Capability<IWorldInformation> WORLD_DATA;

    public static class Storage implements Capability.IStorage<IWorldInformation> {
        public INBT writeNBT(Capability<IWorldInformation> capability, IWorldInformation info, Direction side) {
            CompoundNBT nbt = new CompoundNBT();

            ListNBT list = new ListNBT();
            for (PlayerInformationData data : info.getPlayers().values()) {
                list.add(data.write(new CompoundNBT()));
            }
            nbt.put("Players", list);

            ListNBT list1 = new ListNBT();
            for (PodiumData data : info.getPodiums().values()) {
                list1.add(data.write(new CompoundNBT()));
            }
            nbt.put("Statues", list1);

            nbt.putString("OldestLiving", info.getOldestLiving());
            nbt.putString("LatestDeath", info.getLatestDeath());
            return nbt;
        }

        public void readNBT(Capability<IWorldInformation> capability, IWorldInformation info, Direction side, INBT inbt) {
            CompoundNBT nbt = ((CompoundNBT)inbt);

            ListNBT list = nbt.getList("Players", 10);
            for (int i = 0; i < list.size(); ++i) {
                PlayerInformationData data = new PlayerInformationData(list.getCompound(i));
                info.getPlayers().put(data.getPlayer(), data);
            }

            if (nbt.contains("Statues")) {
                ListNBT list1 = nbt.getList("Statues", 10);
                for (int i = 0; i < list1.size(); ++i) {
                    PodiumData data = new PodiumData(list1.getCompound(i));
                    info.getPodiums().put(data.getPlayerId(), data);
                }
            }

            info.setOldestLiving(nbt.getString("OldestLiving"));
            info.setLatestDeath(nbt.getString("LatestDeath"));
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        private final StoredWorldInformation info = new StoredWorldInformation();
        final LazyOptional<IWorldInformation> infoOptional = LazyOptional.of(() -> info);

        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return WORLD_DATA.orEmpty(cap, this.infoOptional);
        }

        public CompoundNBT serializeNBT() {
            return WORLD_DATA == null ? new CompoundNBT() : (CompoundNBT)WORLD_DATA.writeNBT(info, null);
        }

        public void deserializeNBT(CompoundNBT nbt) {
            if (WORLD_DATA != null) {
                WORLD_DATA.readNBT(info, null, nbt);
            }
        }
    }

}