package com.jayhill.xlife.common.capability.stats;

import com.jayhill.xlife.XLife;
import com.jayhill.xlife.common.capability.health.HealthCapability;
import com.jayhill.xlife.common.capability.time.TimeCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** Handles things such as death cause, & time lasted. */
@SuppressWarnings("all")
public class StatsEvents {

    private String cause;

    @SubscribeEvent
    public void onAttach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            StatsCapabilityProvider provider = new StatsCapabilityProvider();
            event.addCapability(new ResourceLocation(XLife.MOD_ID, "stats"), provider);
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        Entity sourceEntity = event.getSource().getTrueSource();

        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();

            if (sourceEntity instanceof LivingEntity) {
                cause = sourceEntity.getName().getString();
            } else {
                if (source == DamageSource.IN_FIRE) {
                    cause = "Flames";
                } else if (source == DamageSource.LIGHTNING_BOLT) {
                    cause = "Lightning";
                } else if (source == DamageSource.ON_FIRE) {
                    cause = "Burning";
                } else if (source == DamageSource.LAVA) {
                    cause = "Lava";
                } else if (source == DamageSource.HOT_FLOOR) {
                    cause = "Magma";
                } else if (source == DamageSource.IN_WALL) {
                    cause = "Suffocation";
                } else if (source == DamageSource.CRAMMING) {
                    cause = "Cramming";
                } else if (source == DamageSource.DROWN) {
                    cause = "Drowning";
                } else if (source == DamageSource.STARVE) {
                    cause = "Starvation";
                } else if (source == DamageSource.CACTUS) {
                    cause = "Cactus";
                } else if (source == DamageSource.FALL) {
                    cause = "Falling";
                } else if (source == DamageSource.FLY_INTO_WALL) {
                    cause = "Kinetic Energy";
                } else if (source == DamageSource.OUT_OF_WORLD) {
                    cause = "Void";
                } else if (source == DamageSource.MAGIC) {
                    cause = "Magic";
                } else if (source == DamageSource.WITHER) {
                    cause = "Wither";
                } else if (source == DamageSource.ANVIL) {
                    cause = "Falling Anvil";
                } else if (source == DamageSource.FALLING_BLOCK) {
                    cause = "Falling Block";
                } else if (source == DamageSource.DRAGON_BREATH) {
                    cause = "Dragon Breath";
                } else if (source == DamageSource.SWEET_BERRY_BUSH) {
                    cause = "Berry Bush";
                } else if (source.isExplosion()) {
                    cause = "Explosion";
                } else {
                    cause = "Something";
                }
            }

            player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
                player.getCapability(StatsCapability.STATS_CAPABILITY).ifPresent(stats -> {
                    player.getCapability(TimeCapability.TIME_CAPABILITY).ifPresent(time -> {
                        int hearts = (int) ((health.getMaxHealth() - 2) / 2);

                        if (hearts >= 1 && hearts <= 10) {
                            String[] causeArray = stats.getCause();
                            causeArray[hearts - 1] = cause;
                            stats.setCause(causeArray);

                            String[] messageArray = stats.getMessage();
                            messageArray[hearts - 1] = player.getCombatTracker().getDeathMessage().getString();
                            stats.setMessage(messageArray);

                            String[] timeArray = stats.getTime();

                            if (time.getStoredTime() == 1) {
                                timeArray[hearts - 1] = "1 second";
                            } else if (time.getStoredTime() < 60) {
                                timeArray[hearts - 1] = time.getStoredTime() + " seconds";
                            } else if (time.getStoredTime() >= 60 && time.getStoredTime() < 120) {
                                timeArray[hearts - 1] = "1 minute";
                            } else if (time.getStoredTime() >= 120 && time.getStoredTime() < 3600) {
                                timeArray[hearts - 1] = time.getStoredTime() / 60 + " minutes";
                            } else if (time.getStoredTime() >= 3600 && time.getStoredTime() < 7200) {
                                timeArray[hearts - 1] = "1 hour";
                            } else {
                                timeArray[hearts - 1] = time.getStoredTime() / 3600 + " hours";
                            }

                            stats.setTime(timeArray);
                            time.setStoredTime(0);
                        }
                    });
                });
            });
        }
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        LazyOptional<IStatsCapability> capability = event.getOriginal().getCapability(StatsCapability.STATS_CAPABILITY);

        capability.ifPresent(oldStore -> event.getPlayer().getCapability(StatsCapability.STATS_CAPABILITY).ifPresent(newStore -> newStore.onClone((DefaultStatsCapability) oldStore)));
    }

}