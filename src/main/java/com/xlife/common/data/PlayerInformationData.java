package com.xlife.common.data;

import com.mojang.authlib.GameProfile;
import com.xlife.common.util.TagUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.*;

public class PlayerInformationData {
    private GameProfile player;
    private final Map<Integer, LivesData> lives = new HashMap<>();

    public PlayerInformationData(CompoundNBT nbt) {
        this.player = TagUtils.readPlayer(nbt);

        ListNBT lives = nbt.getList("Lives", 10);
        for (int i = 0; i < lives.size(); ++i) {
            LivesData data = new LivesData(lives.getCompound(i));
            this.lives.put(data.getHearts(), data);
        }
    }

    /** @return the player this information belongs to. */
    public GameProfile getPlayer() {
        return this.player;
    }

    public void setPlayer(GameProfile player) {
        this.player = player;
    }

    public Map<Integer, LivesData> getLives() {
        return this.lives;
    }

    public LivesData getLife(int life) {
        return this.lives.get(life);
    }

    public LivesData getCurrentLife() {
        return this.lives.get(this.lives.size());
    }

    public CompoundNBT write(CompoundNBT nbt) {
        if (this.player != null) {
            TagUtils.writePlayer(nbt, this.player);
        }

        ListNBT lives = new ListNBT();
        for (LivesData data : this.lives.values()) {
            lives.add(data.write(new CompoundNBT()));
        }
        nbt.put("Lives", lives);
        return nbt;
    }

}