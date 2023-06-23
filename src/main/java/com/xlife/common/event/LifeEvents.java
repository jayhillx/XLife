package com.xlife.common.event;

import com.xlife.common.capability.player.PlayerCapability;
import com.xlife.common.capability.world.WorldCapability;
import com.xlife.common.command.XLifeCommands;
import com.xlife.common.data.LivesData;
import com.xlife.common.data.PlayerInformationData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Timer;
import java.util.TimerTask;

import static com.xlife.common.util.LifeUtils.*;

public class LifeEvents {

    @SubscribeEvent
    public void onJoin(PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();

        player.getCapability(PlayerCapability.PLAYER_DATA).ifPresent((life) -> {
            if (life.getMaxHealth() == 0) {
                life.setMaxHealth(2.0F);

                player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(life.getMaxHealth());
                player.setHealth(life.getMaxHealth());

                if (player.getServer() != null) {
                    player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                        PlayerInformationData playerData = world.getInformationById(player.getGameProfile());
                        playerData.setPlayer(player.getGameProfile());
                    });
                }
            }

            if (life.getMaxHealth() == 22.0F) {
                player.setGameType(GameType.SPECTATOR);
            }
        });
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)event.getEntity();

            player.getCapability(PlayerCapability.PLAYER_DATA).ifPresent((life) -> {
                int i = (int) life.getMaxHealth();

                if (i >= 2.0F && i <= 22.0F) {
                    life.setMaxHealth(i + 2.0F);

                    if (player.getServer() != null) {
                        for (ServerPlayerEntity players : player.getServer().getPlayerList().getPlayers()) {
                            int lives = 10 - (i / 2);

                            new Timer().schedule(new TimerTask() {
                                public void run() {
                                    if (lives >= 2) {
                                        players.sendMessage(player.getDisplayName().appendText(" has " + lives + " lives remaining . . .").applyTextStyle(TextFormatting.RED));
                                    } else if (lives == 1) {
                                        players.sendMessage(player.getDisplayName().appendText(" has one life remaining!").applyTextStyle(TextFormatting.RED));
                                    } else {
                                        players.sendMessage(player.getDisplayName().appendText(" has been eliminated!").applyTextStyle(TextFormatting.RED));
                                    }
                                }
                            }, 7);
                        }

                        player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                            PlayerInformationData playerData = world.getInformationById(player.getGameProfile());

                            LivesData currentLife = playerData.getCurrentLife();
                            currentLife.setCauseOfDeath(causeOfDeath(event.getSource()));
                            currentLife.setDeathMessage(deathMessage(player));
                            playerData.getLives().put(currentLife.getHearts(), currentLife);

                            if (playerData.getLives().size() < 11) {
                                LivesData newCurrentLife = new LivesData(new CompoundNBT());
                                newCurrentLife.setHearts((i + 2) / 2);
                                playerData.getLives().put(newCurrentLife.getHearts(), newCurrentLife);
                            }
                            world.setLatestDeath(player.getScoreboardName());
                        });
                    }
                }
                
                if (i == 22.0F) {
                    ((ServerWorld)player.world).addLightningBolt(new LightningBoltEntity(player.world, player.posX, player.posY, player.posZ, true));
                }
            });
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();

        player.getCapability(PlayerCapability.PLAYER_DATA).ifPresent((life) -> {
            int i = (int) life.getMaxHealth();

            if (i >= 2.0F && i <= 22.0F) {
                player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(life.getMaxHealth());
                player.setHealth(life.getMaxHealth());

                life.setTicksLiving(0);

                if (player.getServer() != null) {
                    player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                        PlayerInformationData data = world.getInformationById(player.getGameProfile());
                        int lives = 10 - ((i - 2) / 2);

                        if (lives >= 2) {
                            XLifeCommands.sendCommand(player.world, "/title " + player.getScoreboardName() + " title \"" + lives + " lives remain\"");
                        } else if (lives == 1) {
                            XLifeCommands.sendCommand(player.world, "/title " + player.getScoreboardName() + " title \"1 life remains\"");
                        } else {
                            XLifeCommands.sendCommand(player.world, "/title " + player.getScoreboardName() + " title \"Eliminated\"");
                        }
                        XLifeCommands.sendCommand(player.world, "/title " + player.getScoreboardName() + " subtitle \"You lasted " + data.getLife((i / 2) - 1).getTime() + "\"");
                    });
                }
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.AMBIENT, 1F, 1F);
            }

            if (i == 22.0F) {
                player.setGameType(GameType.SPECTATOR);
            }
        });
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.getServer() != null) {
            event.world.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {

                world.setOldestLiving(oldestLiving(world));
            });
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        if (event.phase == TickEvent.Phase.START) {
            player.getCapability(PlayerCapability.PLAYER_DATA).ifPresent((life) -> {

                if (player.isAlive()) {
                    life.tick();
                }

                if (player.getServer() != null) {
                    player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                        PlayerInformationData playerData = world.getInformationById(player.getGameProfile());

                        if (playerData.getCurrentLife() != null) {
                            playerData.getCurrentLife().setTime(timeLiving(life));
                            playerData.getCurrentLife().setTimeInSeconds(life.getTicksLiving());
                        }
                    });
                }
            });
        }
    }

}