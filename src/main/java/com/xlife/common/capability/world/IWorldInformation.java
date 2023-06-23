package com.xlife.common.capability.world;

import com.mojang.authlib.GameProfile;
import com.xlife.common.data.PlayerInformationData;
import com.xlife.common.data.PodiumData;

import java.util.Map;

/**
 * Stores all info to the world, i.e. player lives and podiums.
 */
public interface IWorldInformation {

    String getOldestLiving();

    void setOldestLiving(String name);

    String getLatestDeath();

    void setLatestDeath(String name);

    Map<GameProfile, PodiumData> getPodiums();

    PodiumData getPodiumById(GameProfile profile);

    Map<GameProfile, PlayerInformationData> getPlayers();

    PlayerInformationData getInformationById(GameProfile profile);

}