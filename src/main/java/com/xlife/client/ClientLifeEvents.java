package com.xlife.client;

import com.google.common.collect.Maps;
import com.xlife.core.XLife;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = XLife.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientLifeEvents {
    private static final Map<Integer, ResourceLocation> HEART_COLORS = Util.make(Maps.newHashMap(), textures -> {
        textures.put(1, XLife.getId("textures/gui/hearts/blue.png"));
        textures.put(2, XLife.getId("textures/gui/hearts/green.png"));
        textures.put(3, XLife.getId("textures/gui/hearts/orange.png"));
        textures.put(4, XLife.getId("textures/gui/hearts/pink.png"));
        textures.put(5, XLife.getId("textures/gui/hearts/purple.png"));
        textures.put(6, XLife.getId("textures/gui/hearts/yellow.png"));
        textures.put(7, XLife.getId("textures/gui/hearts/cyan.png"));
        textures.put(8, XLife.getId("textures/gui/hearts/lime.png"));
        textures.put(9, XLife.getId("textures/gui/hearts/black.png"));
    });

    /** Sets heart color when players max health changes. */
    @SubscribeEvent
    public void renderPre(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        float health = mc.player.getMaxHealth();

        if (event.getType() == ElementType.HEALTH) {
            if (health >= 2.1F && health <= 4.0F) {
                mc.textureManager.bind(HEART_COLORS.get(1));
            } else if (health >= 4.1F && health <= 6.0F) {
                mc.textureManager.bind(HEART_COLORS.get(2));
            } else if (health >= 6.1F && health <= 8.0F) {
                mc.textureManager.bind(HEART_COLORS.get(3));
            } else if (health >= 8.1F && health <= 10.0F) {
                mc.textureManager.bind(HEART_COLORS.get(4));
            } else if (health >= 10.1F && health <= 12.0F) {
                mc.textureManager.bind(HEART_COLORS.get(5));
            } else if (health >= 12.1F && health <= 14.0F) {
                mc.textureManager.bind(HEART_COLORS.get(6));
            } else if (health >= 14.1F && health <= 16.0F) {
                mc.textureManager.bind(HEART_COLORS.get(7));
            } else if (health >= 16.1F && health <= 18.0F) {
                mc.textureManager.bind(HEART_COLORS.get(8));
            } else if (health >= 18.1F && health <= 20.0F) {
                mc.textureManager.bind(HEART_COLORS.get(9));
            }
        }
    }

    @SubscribeEvent
    public void renderPost(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getType() == ElementType.HEALTH) {
            mc.textureManager.bind(AbstractGui.GUI_ICONS_LOCATION);
        }
    }

    /** Syncs players health to the client on respawn. */
    @SubscribeEvent
    public void onClientRespawn(ClientPlayerNetworkEvent.RespawnEvent event) {
        PlayerEntity newPlayer = event.getNewPlayer();
        PlayerEntity oldPlayer = event.getOldPlayer();

        newPlayer.setHealth(oldPlayer.getMaxHealth());
        newPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(oldPlayer.getMaxHealth());
    }

}