package com.xlife.common.data;

import com.mojang.authlib.GameProfile;
import com.xlife.common.util.TagUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class PodiumData {
    private GameProfile player;
    private BlockPos signPos;
    private final Map<UUID, Statue> statuePos = new HashMap<>();

    public PodiumData(CompoundNBT nbt) {
        this.player = TagUtils.readPlayer(nbt);
        this.signPos = TagUtils.readBlockPos(nbt.getCompound("SignPos"));

        ListNBT lives = nbt.getList("StatuePos", 10);
        for (int i = 0; i < lives.size(); ++i) {
            Statue data = new Statue(lives.getCompound(i));
            this.statuePos.put(data.getId(), data);
        }
    }

    /** @return the player this podium and sign belongs to. */
    public GameProfile getPlayerId() {
        return this.player;
    }

    public void setPlayerId(GameProfile profile) {
        this.player = profile;
    }

    public BlockPos getSignPos() {
        return this.signPos;
    }

    public void setSignPos(BlockPos pos) {
        this.signPos = pos;
    }

    public Map<UUID, Statue> getStatue() {
        return statuePos;
    }

    public CompoundNBT write(CompoundNBT nbt) {
        if (this.player != null) {
            TagUtils.writePlayer(nbt, this.player);
        }

        nbt.put("SignPos", TagUtils.writeBlockPos(this.signPos));

        ListNBT lives = new ListNBT();
        for (Statue data : this.statuePos.values()) {
            lives.add(data.write(new CompoundNBT()));
        }
        nbt.put("StatuePos", lives);
        return nbt;
    }

    /**
     * Used for storing the position and uuid of the podium block entity.
     */
    public static class Statue {
        private UUID id;
        private BlockPos pos;

        public Statue(CompoundNBT nbt) {
            this.id = nbt.getUniqueId("Id");
            this.pos = NBTUtil.readBlockPos(nbt.getCompound("Pos"));
        }

        public UUID getId() {
            return this.id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public void setPos(BlockPos pos) {
            this.pos = pos;
        }

        public CompoundNBT write(CompoundNBT nbt) {
            nbt.putUniqueId("Id", this.id);
            nbt.put("Pos", NBTUtil.writeBlockPos(this.pos));
            return nbt;
        }
    }

}