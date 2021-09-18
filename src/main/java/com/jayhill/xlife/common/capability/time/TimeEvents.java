package com.jayhill.xlife.common.capability.time;

import com.jayhill.xlife.XLife;
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

/**
 * Handles things such as time counter, & titles.
 */
@SuppressWarnings("all")
public class TimeEvents {

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            TimeCapabilityProvider provider = new TimeCapabilityProvider();
            event.addCapability(new ResourceLocation(XLife.MOD_ID, "time"), provider);
        }
    }

    @SubscribeEvent
    public void cloneCapability(Clone event) {
        LazyOptional<ITimeCapability> capability = event.getOriginal().getCapability(TimeCapability.TIME_CAPABILITY);

        capability.ifPresent((oldStore) -> event.getPlayer().getCapability(TimeCapability.TIME_CAPABILITY).ifPresent((newStore) -> newStore.onDeath((DefaultTimeCapability) oldStore)));
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
                    time.setTime(time.getTime() + 1);
                });
                this.setTickCount(0);
            } else {
                if (player.getServer() != null && player.getHealth() != 0.0F && player.getServer().getPlayerList().getPlayer(player.getUUID()) != null) {
                    this.setTickCount(this.getTickCount() + 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();

        int lives = (int) (10 - player.getMaxHealth() / 2 + 1);

        if (player.getServer() != null) {
            if (lives >= 2) {
                player.getServer().getCommands().performCommand(player.getServer().createCommandSourceStack().withPermission(4), "/title " + player.getScoreboardName() + " title \"" + lives + " lives remain\"");
            } else if (lives == 1) {
                player.getServer().getCommands().performCommand(player.getServer().createCommandSourceStack().withPermission(4), "/title " + player.getScoreboardName() + " title \"1 life remains\"");
            } else {
                player.getServer().getCommands().performCommand(player.getServer().createCommandSourceStack().withPermission(4), "/title " + player.getScoreboardName() + " title \"Eliminated\"");
            }

            this.subtitle(player);

            /** Plays Ender Dragon growl sound. */
            player.level.playSound(null, player.getEntity().blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundCategory.AMBIENT, 1F, 1F);
        }
    }

    public void subtitle(PlayerEntity player) {
        player.getCapability(StatsCapability.STATS_CAPABILITY).ifPresent(stats -> {
            if (player.getServer() != null) {
                String[] timeArray = stats.getTime();
                int hearts = (int) (player.getMaxHealth() - 2) / 2;

                player.getServer().getCommands().performCommand(player.getServer().createCommandSourceStack().withPermission(4), "/title " + player.getScoreboardName() + " subtitle \"You lasted " + timeArray[hearts - 1] + "\"");
            }
        });
    }

}