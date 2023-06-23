package com.xlife.common.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayerListItemPacket;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModifiedTablist {

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        if (player.getServer() != null) {
            for (ServerPlayerEntity players : player.getServer().getPlayerList().getPlayers()) {
                SPlayerListItemPacket packet = new SPlayerListItemPacket();

            }
        }
    }

}