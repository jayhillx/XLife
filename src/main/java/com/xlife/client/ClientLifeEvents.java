package com.xlife.client;

import com.google.common.collect.Maps;
import com.xlife.XLife;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = XLife.modId, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientLifeEvents {
    private static final Map<Integer, ResourceLocation> HEART_COLORS = Util.make(Maps.newHashMap(), textures -> {
        textures.put(1, AbstractGui.GUI_ICONS_LOCATION);
        textures.put(2, XLife.modLoc("textures/gui/hearts/blue.png"));
        textures.put(3, XLife.modLoc("textures/gui/hearts/green.png"));
        textures.put(4, XLife.modLoc("textures/gui/hearts/orange.png"));
        textures.put(5, XLife.modLoc("textures/gui/hearts/pink.png"));
        textures.put(6, XLife.modLoc("textures/gui/hearts/purple.png"));
        textures.put(7, XLife.modLoc("textures/gui/hearts/yellow.png"));
        textures.put(8, XLife.modLoc("textures/gui/hearts/cyan.png"));
        textures.put(9, XLife.modLoc("textures/gui/hearts/lime.png"));
        textures.put(10, XLife.modLoc("textures/gui/hearts/black.png"));
    });

    @SubscribeEvent
    public static void renderPre(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        int health = Math.round(mc.player.getMaxHealth());

        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
            mc.textureManager.bindTexture(HEART_COLORS.get(health <= 20 ? health / 2 : 1));
        }
    }

    @SubscribeEvent
    public static void renderPost(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
            mc.textureManager.bindTexture(HEART_COLORS.get(1));
        }
    }

    /**
     * Syncs players health to the client on respawn.
     */
    @SubscribeEvent
    public static void onClientRespawn(ClientPlayerNetworkEvent.RespawnEvent event) {
        PlayerEntity newPlayer = event.getNewPlayer();
        PlayerEntity oldPlayer = event.getOldPlayer();

        if (newPlayer.getMaxHealth() == 20.0F) {
            newPlayer.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0F);
        }

        newPlayer.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(oldPlayer.getMaxHealth());
        newPlayer.setHealth(oldPlayer.getMaxHealth());
    }

    @SubscribeEvent
    public static void test(ClientPlayerNetworkEvent.RespawnEvent event) {
        if (event.getPlayer() != null) {
            NetworkPlayerInfo info = new NetworkPlayerInfo(event.getPlayer().getGameProfile());

            info.setDisplayName(new StringTextComponent(" ()"));
        }
    }

}