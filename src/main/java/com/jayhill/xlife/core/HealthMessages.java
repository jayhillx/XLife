package com.jayhill.xlife.core;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.TickEvent.*;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class HealthMessages {

    private static final List<Runnable> delayedTasks = new ArrayList<>();

    /** Sends a message to player(s). */
    public static ITextComponent getRemainingLives(PlayerEntity player, String message) {

        return new TranslationTextComponent(message, player.getDisplayName()).withStyle(TextFormatting.RED).withStyle(style ->
                style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new HoverEvent.EntityHover(player.getType(), player.getUUID(), player.getName())))
        );
    }

    public static void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity.getEntity();

            /** Sends remaining lives in server chat. */
            delayedTasks.add(() -> {
                for (ServerPlayerEntity players : player.getServer().getPlayerList().getPlayers()) {
                    int lives = (int) (10 - (player.getMaxHealth() / 2));

                    if (lives >= 2) {
                        players.sendMessage(HealthMessages.getRemainingLives(player, player.getScoreboardName() + " has " + lives + " lives remaining . . ."), player.getUUID());
                    } else if (lives == 1) {
                        players.sendMessage(HealthMessages.getRemainingLives(player, player.getScoreboardName() + " has one life remaining!"), player.getUUID());
                    } else {
                        players.sendMessage(HealthMessages.getRemainingLives(player, player.getScoreboardName() + " has been eliminated!"), player.getUUID());
                    }
                }
            });
        }
    }

    public static void onTick(PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == Phase.END) {

            for (Runnable runnable : delayedTasks) runnable.run(); {
                delayedTasks.clear();
            }
        }
    }

}