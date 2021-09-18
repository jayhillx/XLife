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

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            StatsCapabilityProvider provider = new StatsCapabilityProvider();
            event.addCapability(new ResourceLocation(XLife.MOD_ID, "stats"), provider);
        }
    }

    @SubscribeEvent
    public void cloneCapability(PlayerEvent.Clone event) {
        LazyOptional<IStatsCapability> capability = event.getOriginal().getCapability(StatsCapability.STATS_CAPABILITY);

        capability.ifPresent((oldStore) -> event.getPlayer().getCapability(StatsCapability.STATS_CAPABILITY).ifPresent((newStore) -> newStore.onDeath((DefaultStatsCapability) oldStore)));
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Entity causeEntity = event.getSource().getEntity();
        DamageSource source = event.getSource();

        /** Sets Cause. */
        String cause = null;
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;

            if (causeEntity instanceof LivingEntity) {
                if (causeEntity instanceof PlayerEntity) {
                    if (causeEntity == player) {
                        cause = "Yourself";
                    } else {
                        cause = ((PlayerEntity)causeEntity).getName().getString();
                    }
                } else {
                    cause = ((LivingEntity)causeEntity).getName().getString();
                }
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

            String finalCause = cause;
            player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
                player.getCapability(StatsCapability.STATS_CAPABILITY).ifPresent(stats -> {
                    int hearts = (int) (health.getMaxHealth() - 2) / 2;

                    if (hearts >= 1 && hearts <= 10) {
                        String[] causeArray = stats.getCause();
                        causeArray[hearts - 1] = finalCause;
                        stats.setCause(causeArray);
                    }

                    player.getCapability(TimeCapability.TIME_CAPABILITY).ifPresent(time -> {
                        if (hearts >= 1 && hearts <= 10) {
                            String[] timeArray = stats.getTime();

                            if (time.getTime() == 1) {
                                timeArray[hearts - 1] = "1 second";
                            } else if (time.getTime() < 60) {
                                timeArray[hearts - 1] = time.getTime() + " seconds";
                            } else if (time.getTime() >= 60 && time.getTime() < 120) {
                                timeArray[hearts - 1] = "1 minute";
                            } else if (time.getTime() >= 120 && time.getTime() < 3600) {
                                timeArray[hearts - 1] = time.getTime() / 60 + " minutes";
                            } else if (time.getTime() >= 3600 && time.getTime() < 7200) {
                                timeArray[hearts - 1] = "1 hour";
                            } else {
                                timeArray[hearts - 1] = time.getTime() / 3600 + " hours";
                            }

                            stats.setTime(timeArray);
                            time.setTime(0);
                        }
                    });
                });
            });
        }
    }
    
}