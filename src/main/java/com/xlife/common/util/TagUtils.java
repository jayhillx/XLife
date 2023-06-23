package com.xlife.common.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class TagUtils {

    public static GameProfile readPlayer(CompoundNBT nbt) {
        String name = nbt.getString("Name");
        String id = nbt.getString("Id");

        try {
            UUID uuid;
            try {
                uuid = UUID.fromString(id);
            } catch (Throwable throwable) {
                uuid = null;
            }
            return new GameProfile(uuid, name);
        } catch (Throwable var13) {
            return null;
        }
    }

    public static void writePlayer(CompoundNBT nbt, GameProfile player) {
        nbt.putString("Name", player.getName());
        nbt.putString("Id", player.getId().toString());
    }

    public static BlockPos readBlockPos(CompoundNBT nbt) {
        return new BlockPos(nbt.getInt("X"), nbt.getInt("Y"), nbt.getInt("Z"));
    }

    public static CompoundNBT writeBlockPos(BlockPos pos) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("X", pos.getX());
        nbt.putInt("Y", pos.getY());
        nbt.putInt("Z", pos.getZ());
        return nbt;
    }

}