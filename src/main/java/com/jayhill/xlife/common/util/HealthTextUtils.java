package com.jayhill.xlife.common.util;

import com.jayhill.xlife.common.capability.health.HealthCapability;
import com.jayhill.xlife.common.capability.health.HealthEvents;
import com.jayhill.xlife.common.capability.health.IHealthCapability;
import com.jayhill.xlife.common.capability.stats.IStatsCapability;
import com.jayhill.xlife.common.capability.stats.StatsCapability;
import com.jayhill.xlife.common.capability.time.ITimeCapability;
import com.jayhill.xlife.common.capability.time.TimeCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static net.minecraft.util.text.TextFormatting.*;

@SuppressWarnings("all")
public class HealthTextUtils {

    /** Sends a message to players. */
    private static ITextComponent setRemainingLives(String message) {
        return new TranslationTextComponent(RED + message);
    }

    public static void getRemainingLives(PlayerEntity player) {
        player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
            int lives = (int) (10 - ((health.getMaxHealth() - 2) / 2));

            for (ServerPlayerEntity players : player.getServer().getPlayerList().getPlayers()) {
                HealthEvents.message.add(() -> {
                    if (lives >= 2) {
                        players.sendMessage(setRemainingLives(player.getScoreboardName() + " has " + lives + " lives remaining . . ."));
                    } else if (lives == 1) {
                        players.sendMessage(setRemainingLives(player.getScoreboardName() + " has one life remaining!"));
                    } else {
                        players.sendMessage(setRemainingLives(player.getScoreboardName() + " has been eliminated!"));
                    }
                });
            }
        });
    }

    public static String getTimeLiving(PlayerEntity player) {
        IHealthCapability health = player.getCapability(HealthCapability.HEALTH_CAPABILITY).orElse(null);
        IStatsCapability stats = player.getCapability(StatsCapability.STATS_CAPABILITY).orElse(null);
        ITimeCapability time = player.getCapability(TimeCapability.TIME_CAPABILITY).orElse(null);

        String[] timeArray = stats.getTime();
        int hearts = (int) ((health.getMaxHealth() - 2) / 2);

        if (time.getStoredTime() == 1) {
            return timeArray[hearts - 1] = "1 second";
        } else if (time.getStoredTime() < 60) {
            return timeArray[hearts - 1] = time.getStoredTime() + " seconds";
        } else if (time.getStoredTime() >= 60 && time.getStoredTime() < 120) {
            return timeArray[hearts - 1] = "1 minute";
        } else if (time.getStoredTime() >= 120 && time.getStoredTime() < 3600) {
            return timeArray[hearts - 1] = time.getStoredTime() / 60 + " minutes";
        } else if (time.getStoredTime() >= 3600 && time.getStoredTime() < 7200) {
            return timeArray[hearts - 1] = "1 hour";
        } else {
            return timeArray[hearts - 1] = time.getStoredTime() / 3600 + " hours";
        }
    }

    /** Sets the players health in char form. */
    public static String getDynamicHearts(float maxHealth) {
        char h = 10084;

        if (maxHealth == 2.0F) {
            return RED + "" + h;
        } else if (maxHealth == 4.0F) {
            return BLUE + "" + h+h;
        } else if (maxHealth == 6.0F) {
            return DARK_GREEN + "" + h+h+h;
        } else if (maxHealth == 8.0F) {
            return GOLD + "" + h+h+h+h;
        } else if (maxHealth == 10.0F) {
            return LIGHT_PURPLE + "" + h+h+h+h+h;
        } else if (maxHealth == 12.0F) {
            return DARK_PURPLE + "" + h+h+h+h+h+h;
        } else if (maxHealth == 14.0F) {
            return YELLOW + "" + h+h+h+h+h+h+h;
        } else if (maxHealth == 16.0F) {
            return DARK_AQUA + "" + h+h+h+h+h+h+h+h;
        } else if (maxHealth == 18.0F) {
            return GREEN + "" + h+h+h+h+h+h+h+h+h;
        } else if (maxHealth == 20.0F) {
            return BLACK + "" + h+h+h+h+h+h+h+h+h+h;
        }
        return "";
    }

}