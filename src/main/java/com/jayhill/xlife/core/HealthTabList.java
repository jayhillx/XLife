package com.jayhill.xlife.core;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayerListItemPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class HealthTabList {
    private static final Field fieldPlayers = ObfuscationReflectionHelper.findField(SPlayerListItemPacket.class, "field_179769_b");
    private static final Field fieldDisplayName = ObfuscationReflectionHelper.findField(SPlayerListItemPacket.AddPlayerData.class, "field_179965_e");

    @SubscribeEvent
    public void onLogin(PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();

        if (player.getServer() != null) {
            for (ServerPlayerEntity serverPlayers : player.getServer().getPlayerList().getPlayers()) {
                try {
                    SPlayerListItemPacket packet = new SPlayerListItemPacket();
                    List<SPlayerListItemPacket.AddPlayerData> packetValue = (List<SPlayerListItemPacket.AddPlayerData>) fieldPlayers.get(packet);

                    for (SPlayerListItemPacket.AddPlayerData values : packetValue) {

                    }
                    
                } catch (ClassCastException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}