package com.xlife.common.capability.world;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.xlife.common.data.LivesData;
import com.xlife.common.data.PlayerInformationData;
import com.xlife.common.data.PodiumData;
import net.minecraft.nbt.CompoundNBT;

import java.util.Map;

/**
 * Stores everything x life needs to function.
 */
public class StoredWorldInformation implements IWorldInformation {

    private final Map<GameProfile, PlayerInformationData> playerById = Maps.newHashMap();
    private final Map<GameProfile, PodiumData> statueById = Maps.newHashMap();

    private String oldestLiving = "";
    private String latestDeath = "";

    public String getOldestLiving() {
        return this.oldestLiving;
    }

    public void setOldestLiving(String name) {
        this.oldestLiving = name;
    }

    public String getLatestDeath() {
        return this.latestDeath;
    }

    public void setLatestDeath(String name) {
        this.latestDeath = name;
    }

    public Map<GameProfile, PodiumData> getPodiums() {
        return this.statueById;
    }

    /** @return new player statue if the uuid given is not in the map. */
    public PodiumData getPodiumById(GameProfile profile) {
        return this.statueById.computeIfAbsent(profile, (orNewId) -> {
            PodiumData podiumData = new PodiumData(new CompoundNBT());
            podiumData.setPlayerId(profile);

            return podiumData;
        });
    }

    public Map<GameProfile, PlayerInformationData> getPlayers() {
        return this.playerById;
    }

    /** @return new player data if the uuid given is not in the map. */
    public PlayerInformationData getInformationById(GameProfile profile) {
        return this.playerById.computeIfAbsent(profile, (orNewId) -> {
            LivesData lives = new LivesData(new CompoundNBT());
            lives.setHearts(1);

            PlayerInformationData playerData = new PlayerInformationData(new CompoundNBT());
            playerData.setPlayer(profile);
            playerData.getLives().put(lives.getHearts(), lives);

            return playerData;
        });
    }

}