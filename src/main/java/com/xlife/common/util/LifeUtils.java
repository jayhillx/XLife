package com.xlife.common.util;

import com.mojang.authlib.GameProfile;
import com.xlife.common.capability.player.IPlayerInformation;
import com.xlife.common.capability.world.IWorldInformation;
import com.xlife.common.data.PlayerInformationData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

import static net.minecraft.util.text.TextFormatting.*;

public class LifeUtils {
    private static final Map<GameProfile, Integer> time = new HashMap<>();
    private static final TextFormatting[] formatting = new TextFormatting[]{RED, BLUE, DARK_GREEN, GOLD, LIGHT_PURPLE, DARK_PURPLE, YELLOW, AQUA, GREEN, BLACK, GRAY};

    /** @return name of the player with the longest time living. */
    public static String oldestLiving(IWorldInformation world) {

        for (PlayerInformationData playerData : world.getPlayers().values()) {
            if (playerData.getCurrentLife() != null) {
                time.put(playerData.getPlayer(), playerData.getCurrentLife().getTimeInSeconds());
            }
        }
        return time.isEmpty() ? "" : Collections.max(time.entrySet(), Map.Entry.comparingByValue()).getKey().getName();
    }

    public static String causeOfDeath(DamageSource source) {
        if (source.getTrueSource() instanceof LivingEntity) {
            if (source.getTrueSource() == null) {
                return "Something";
            } else {
                return source.getTrueSource().getName().getString();
            }
        } else {
            if (source == DamageSource.IN_FIRE) {
                return "Flames";
            } else if (source == DamageSource.LIGHTNING_BOLT) {
                return "Lightning";
            } else if (source == DamageSource.ON_FIRE) {
                return "Burning";
            } else if (source == DamageSource.LAVA) {
                return "Lava";
            } else if (source == DamageSource.HOT_FLOOR) {
                return "Magma";
            } else if (source == DamageSource.IN_WALL) {
                return "Suffocation";
            } else if (source == DamageSource.CRAMMING) {
                return "Cramming";
            } else if (source == DamageSource.DROWN) {
                return "Drowning";
            } else if (source == DamageSource.STARVE) {
                return "Starvation";
            } else if (source == DamageSource.CACTUS) {
                return "Cactus";
            } else if (source == DamageSource.FALL) {
                return "Falling";
            } else if (source == DamageSource.FLY_INTO_WALL) {
                return "Kinetic Energy";
            } else if (source == DamageSource.OUT_OF_WORLD) {
                return "Void";
            } else if (source == DamageSource.MAGIC) {
                return "Magic";
            } else if (source == DamageSource.WITHER) {
                return "Withering";
            } else if (source == DamageSource.ANVIL) {
                return "Falling Anvil";
            } else if (source == DamageSource.FALLING_BLOCK) {
                return "Falling Block";
            } else if (source == DamageSource.DRAGON_BREATH) {
                return "Dragon Breath";
            } else if (source == DamageSource.SWEET_BERRY_BUSH) {
                return "Berry Bush";
            } else if (source.isExplosion()) {
                return "Explosion";
            } else {
                return "Something";
            }
        }
    }

    public static String deathMessage(PlayerEntity player) {
        return player.getCombatTracker().getDeathMessage().getString();
    }

    public static String timeLiving(IPlayerInformation info) {
        if (info.getTicksLiving() == 1) {
            return "1 second";
        } else if (info.getTicksLiving() < 60) {
            return info.getTicksLiving() + " seconds";
        } else if (info.getTicksLiving() >= 60 && info.getTicksLiving() < 120) {
            return "1 minute";
        } else if (info.getTicksLiving() >= 120 && info.getTicksLiving() < 3600) {
            return info.getTicksLiving() / 60 + " minutes";
        } else if (info.getTicksLiving() >= 3600 && info.getTicksLiving() < 7200) {
            return "1 hour";
        } else {
            return info.getTicksLiving() / 3600 + " hours";
        }
    }

    public static String signHearts(int amount) {
        StringBuilder builder = new StringBuilder();
        int hearts = Math.round(amount);

        while (hearts > 0) {
            builder.append(symbol(amount));
            hearts -= 2;
        }
        return "{\"text\":\"" + builder + "\",\"color\":\"" + formatting[(amount / 2) - 1].getFriendlyName() + "\"}";
    }

    /** @param health determines whether character will return as a heart or a skull. */
    private static StringBuilder symbol(int health) {
        StringBuilder builder = new StringBuilder();
        if (health < 22) {
            return builder.append(new Character((char)10084));
        } else {
            return builder.append(new Character((char)9760));
        }
    }

}