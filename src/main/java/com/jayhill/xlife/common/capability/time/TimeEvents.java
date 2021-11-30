package com.jayhill.xlife.common.capability.time;

import com.jayhill.xlife.XLife;
import com.jayhill.xlife.common.capability.health.HealthCapability;
import com.jayhill.xlife.common.capability.stats.StatsCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.*;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** Handles events such as time counter, & titles. */
@SuppressWarnings("all")
public class TimeEvents {

    @SubscribeEvent
    public void onAttach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            TimeCapabilityProvider provider = new TimeCapabilityProvider();
            event.addCapability(new ResourceLocation(XLife.MOD_ID, "time"), provider);
        }
    }

    private int tickCount;

    private int getTickCount() {
        return this.tickCount;
    }

    private void setTickCount(int count) {
        this.tickCount = count;
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
        PlayerEntity player = event.player;

        if (event.phase == Phase.START && event.side.isServer()) {
            if (this.getTickCount() == 20) {
                player.getCapability(TimeCapability.TIME_CAPABILITY).ifPresent(time -> {
                    time.setStoredTime(time.getStoredTime() + 1);
                });
                this.setTickCount(0);
            } else {
                if (player.getServer() != null && player.getHealth() != 0.0F && player.getServer().getPlayerList().getPlayerByUUID(player.getUniqueID()) != null) {
                    this.setTickCount(this.getTickCount() + 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();

        if (player.getServer() != null) {
            player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
                player.getCapability(StatsCapability.STATS_CAPABILITY).ifPresent(stats -> {
                    int lives = (int) (10 - ((health.getMaxHealth() - 2) / 2));
                    int hearts = (int) (health.getMaxHealth() / 2) - 2;
                    String[] timeArray = stats.getTime();

                    if (hearts < 10) {
                        if (lives >= 2) {
                            player.getServer().getCommandManager().handleCommand(player.getServer().getCommandSource().withFeedbackDisabled(), "/title " + player.getScoreboardName() + " title \"" + lives + " lives remain\"");
                        } else if (lives == 1) {
                            player.getServer().getCommandManager().handleCommand(player.getServer().getCommandSource().withFeedbackDisabled(), "/title " + player.getScoreboardName() + " title \"1 life remains\"");
                        } else {
                            player.getServer().getCommandManager().handleCommand(player.getServer().getCommandSource().withFeedbackDisabled(), "/title " + player.getScoreboardName() + " title \"Eliminated\"");
                        }

                        player.getServer().getCommandManager().handleCommand(player.getServer().getCommandSource().withFeedbackDisabled(), "/title " + player.getScoreboardName() + " subtitle \"You lasted " + timeArray[hearts] + "\"");
                    }
                });
            });

            /** Plays Ender Dragon growl sound. */
            player.world.playSound(null, player.getEntity().getPosition(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.AMBIENT, 1F, 1F);
        }
    }

    @SubscribeEvent
    public void onClone(Clone event) {
        LazyOptional<ITimeCapability> capability = event.getOriginal().getCapability(TimeCapability.TIME_CAPABILITY);

        capability.ifPresent(oldStore -> event.getPlayer().getCapability(TimeCapability.TIME_CAPABILITY).ifPresent(newStore -> newStore.onClone((DefaultTimeCapability) oldStore)));
    }

}