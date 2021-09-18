package com.jayhill.xlife.client.util;

import com.google.common.collect.Maps;
import com.jayhill.xlife.XLife;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@SuppressWarnings("all")
@Mod.EventBusSubscriber(modid = XLife.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HealthColorUtils {
    private static final Map<Integer, ResourceLocation> HEART_COLORS = Util.make(Maps.newHashMap(), textures -> {
        textures.put(1, new ResourceLocation(XLife.MOD_ID, "textures/gui/hearts/blue.png"));
        textures.put(2, new ResourceLocation(XLife.MOD_ID, "textures/gui/hearts/green.png"));
        textures.put(3, new ResourceLocation(XLife.MOD_ID, "textures/gui/hearts/orange.png"));
        textures.put(4, new ResourceLocation(XLife.MOD_ID, "textures/gui/hearts/pink.png"));
        textures.put(5, new ResourceLocation(XLife.MOD_ID, "textures/gui/hearts/purple.png"));
        textures.put(6, new ResourceLocation(XLife.MOD_ID, "textures/gui/hearts/yellow.png"));
        textures.put(7, new ResourceLocation(XLife.MOD_ID, "textures/gui/hearts/cyan.png"));
        textures.put(8, new ResourceLocation(XLife.MOD_ID, "textures/gui/hearts/lime.png"));
        textures.put(9, new ResourceLocation(XLife.MOD_ID, "textures/gui/hearts/black.png"));
    });

    /** Sets heart color when players health changes. */
    @SubscribeEvent
    public void onHeartColors(RenderGameOverlayEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getType() == ElementType.HEALTH) {
            if (mc.player.getMaxHealth() >= 2.1F && mc.player.getMaxHealth() <= 4.0F) {
                mc.textureManager.bind(HEART_COLORS.get(1));
            } else if (mc.player.getMaxHealth() >= 4.1F && mc.player.getMaxHealth() <= 6.0F) {
                mc.textureManager.bind(HEART_COLORS.get(2));
            } else if (mc.player.getMaxHealth() >= 6.1F && mc.player.getMaxHealth() <= 8.0F) {
                mc.textureManager.bind(HEART_COLORS.get(3));
            } else if (mc.player.getMaxHealth() >= 8.1F && mc.player.getMaxHealth() <= 10.0F) {
                mc.textureManager.bind(HEART_COLORS.get(4));
            } else if (mc.player.getMaxHealth() >= 10.1F && mc.player.getMaxHealth() <= 12.0F) {
                mc.textureManager.bind(HEART_COLORS.get(5));
            } else if (mc.player.getMaxHealth() >= 12.1F && mc.player.getMaxHealth() <= 14.0F) {
                mc.textureManager.bind(HEART_COLORS.get(6));
            } else if (mc.player.getMaxHealth() >= 14.1F && mc.player.getMaxHealth() <= 16.0F) {
                mc.textureManager.bind(HEART_COLORS.get(7));
            } else if (mc.player.getMaxHealth() >= 16.1F && mc.player.getMaxHealth() <= 18.0F) {
                mc.textureManager.bind(HEART_COLORS.get(8));
            } else if (mc.player.getMaxHealth() >= 18.1F && mc.player.getMaxHealth() <= 20.0F) {
                mc.textureManager.bind(HEART_COLORS.get(9));
            }
        }
    }

}