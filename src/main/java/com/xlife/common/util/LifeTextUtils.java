package com.xlife.common.util;

import com.xlife.common.capability.ILifeCapability;
import com.xlife.common.capability.LifeCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;

import static net.minecraft.util.text.TextFormatting.*;

public class LifeTextUtils {

    private static ITextComponent setRemainingLives(PlayerEntity player, String message) {
        return new TranslationTextComponent(message).withStyle(TextFormatting.RED).withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new HoverEvent.EntityHover(player.getType(), player.getUUID(), player.getName()))));
    }

    /** Sends a message to players. */
    public static void getRemainingLives(PlayerEntity player) {
        player.getCapability(LifeCapability.X_LIFE_CAPABILITIES).ifPresent(health -> {
            int lives = (int) (10 - ((health.getMaxHealth() - 2) / 2));

            for (ServerPlayerEntity players : player.getServer().getPlayerList().getPlayers()) {
                if (lives >= 2) {
                    players.sendMessage(setRemainingLives(player, player.getScoreboardName() + " has " + lives + " lives remaining . . ."), player.getUUID());
                } else if (lives == 1) {
                    players.sendMessage(setRemainingLives(player, player.getScoreboardName() + " has one life remaining!"), player.getUUID());
                } else {
                    players.sendMessage(setRemainingLives(player, player.getScoreboardName() + " has been eliminated!"), player.getUUID());
                }
            }
        });
    }

    public static String getTimeLiving(PlayerEntity player) {
        ILifeCapability instance = player.getCapability(LifeCapability.X_LIFE_CAPABILITIES).orElse(null);

        String[] timeArray = instance.getTimeLasted();
        int hearts = (int) ((instance.getMaxHealth() - 2) / 2);

        if (instance.getTimeLiving() == 1) {
            timeArray[hearts] = "1 second";
        } else if (instance.getTimeLiving() < 60) {
            timeArray[hearts] = instance.getTimeLiving() + " seconds";
        } else if (instance.getTimeLiving() >= 60 && instance.getTimeLiving() < 120) {
            timeArray[hearts] = "1 minute";
        } else if (instance.getTimeLiving() >= 120 && instance.getTimeLiving() < 3600) {
            timeArray[hearts] = instance.getTimeLiving() / 60 + " minutes";
        } else if (instance.getTimeLiving() >= 3600 && instance.getTimeLiving() < 7200) {
            timeArray[hearts] = "1 hour";
        } else {
            timeArray[hearts] = instance.getTimeLiving() / 3600 + " hours";
        }
        return timeArray[hearts];
    }

    private static String hearts(float health, boolean isLoss) {
        char h = 10084;
        StringBuilder value = new StringBuilder();

        int hearts = Math.round(health);

        if (isLoss) {
            if (hearts % 2 != 0) {
                hearts -= 1;
            }
        }

        while (hearts > 0) {
            value.append(h);
            hearts -= 2;
        }
        return value.toString();
    }

    public static String setHearts(float health) {
        return setHearts(health, health, 0, true);
    }

    /** Sets players health into char form. */
    public static String setHearts(float maxHealth, float health, float lostHealth, boolean isStill) {
        TextFormatting[] colors = new TextFormatting[]{RED, BLUE, DARK_GREEN, GOLD, LIGHT_PURPLE, DARK_PURPLE, YELLOW, DARK_AQUA, GREEN, BLACK, WHITE};
        int hearts = (int) (maxHealth - 1) / 2;

        if (isStill) {
            return colors[hearts] + hearts(health, false);
        } else {
            return GRAY + "(" + colors[hearts] + hearts(health, false) + DARK_GRAY + hearts(lostHealth, true) + GRAY + ")";
        }
    }

}