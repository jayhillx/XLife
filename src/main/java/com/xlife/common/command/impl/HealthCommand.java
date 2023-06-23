package com.xlife.common.command.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.xlife.common.capability.player.PlayerCapability;
import com.xlife.common.capability.world.WorldCapability;
import com.xlife.common.data.LivesData;
import com.xlife.common.data.PlayerInformationData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.Collection;

public class HealthCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("sethealth").requires((commandSource) -> {
            return commandSource.hasPermissionLevel(2);
        }).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("hearts", IntegerArgumentType.integer()).executes((context) -> {
            return setHealth(EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "hearts"));
        }))));
    }

    private static int setHealth(Collection<ServerPlayerEntity> players, int hearts) {
        if (hearts < 11.0F) {
            for (ServerPlayerEntity player : players) {
                player.getCapability(PlayerCapability.PLAYER_DATA).ifPresent((life) -> {
                    int health = hearts * 2;

                    player.world.getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                        PlayerInformationData playerData = world.getInformationById(player.getGameProfile());
                        playerData.getLives().keySet().removeIf(i -> i > hearts);

                        int ticks = 0;
                        if (playerData.getLife(hearts) != null) {
                            ticks = playerData.getLife(hearts).getTimeInSeconds();
                        }

                        life.setMaxHealth(health);
                        life.setTicksLiving(ticks);

                        player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(life.getMaxHealth());
                        player.setHealth(life.getMaxHealth());

                        LivesData newCurrentLife = new LivesData(new CompoundNBT());
                        newCurrentLife.setCauseOfDeath("");
                        newCurrentLife.setDeathMessage("");
                        newCurrentLife.setHearts(hearts);
                        newCurrentLife.setTimeInSeconds(ticks);
                        playerData.getLives().put(hearts, newCurrentLife);
                    });
                });
            }
        }
        return 1;
    }

}