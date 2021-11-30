package com.jayhill.xlife.common.capability.health;

import com.jayhill.xlife.XLife;
import com.jayhill.xlife.common.capability.time.TimeCapability;
import com.jayhill.xlife.common.util.HealthBookUtils;
import com.jayhill.xlife.common.util.HealthTextUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

/** Handles events such as respawn, & death event. */
@SuppressWarnings("all")
public class HealthEvents {

    @SubscribeEvent
    public void onAttach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            HealthCapabilityProvider provider = new HealthCapabilityProvider();
            event.addCapability(new ResourceLocation(XLife.MOD_ID, "health"), provider);
        }
    }

    @SubscribeEvent
    public void onJoin(PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();

        player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
            if (health.getMaxHealth() == 0.0F) {
                health.setMaxHealth(2.0F);

                player.setHealth(health.getMaxHealth());
                player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health.getMaxHealth());
            }
        });
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();

            player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
                if (health.getMaxHealth() >= 2.0F && health.getMaxHealth() <= 22.0F) {
                    health.setMaxHealth(health.getMaxHealth() + 2.0F);

                    HealthTextUtils.getRemainingLives(player);
                }

                if (health.getMaxHealth() == 22.0F) {
                    LightningBoltEntity lighting = new LightningBoltEntity(player.world, player.getPosY(), player.getPosY(), player.getPosY(), true);
                    if (lighting != null) {
                        lighting.setCaster(player instanceof ServerPlayerEntity ? (ServerPlayerEntity)player : null);
                        ((ServerWorld)player.world).addLightningBolt(lighting);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();

        player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
            if (health.getMaxHealth() >= 2.0F && health.getMaxHealth() <= 22.0F) {
                player.setHealth(health.getMaxHealth());
                player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health.getMaxHealth());
            }

            if (health.getMaxHealth() == 22.0F) {
                player.setGameType(GameType.SPECTATOR);
            }
        });
    }

    public static final List<Runnable> message = new ArrayList<>();

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            for (Runnable runnable : message) runnable.run(); {
                message.clear();
            }
        }
    }

    @SubscribeEvent
    public void onUseBook(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();
        ListNBT pages = new ListNBT();

        if (stack.getItem() == Items.WRITTEN_BOOK) {
            if (stack.getTag() != null && stack.getTag().hasUniqueId("owner")) {

                if (!player.world.isRemote) {
                    UUID uuid = stack.getTag().getUniqueId("owner");
                    ServerPlayerEntity players = player.getServer().getPlayerList().getPlayerByUUID(uuid);

                    if (players != null) {
                        players.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
                            players.getCapability(TimeCapability.TIME_CAPABILITY).ifPresent(time -> {

                                stack.getTag().put("pages", pages);
                                stack.getTag().remove("resolved");

                                if (health.getMaxHealth() >= 2.0F && health.getMaxHealth() <= 6.0F) {
                                    pages.add(HealthBookUtils.setPages(players, 1));
                                } else if (health.getMaxHealth() >= 8.0F && health.getMaxHealth() <= 12.0F) {
                                    pages.add(HealthBookUtils.setPages(players, 1));
                                    pages.add(HealthBookUtils.setPages(players, 2));
                                } else if (health.getMaxHealth() >= 14.0F && health.getMaxHealth() <= 18.0F) {
                                    pages.add(HealthBookUtils.setPages(players, 1));
                                    pages.add(HealthBookUtils.setPages(players, 2));
                                    pages.add(HealthBookUtils.setPages(players, 3));
                                } else {
                                    pages.add(HealthBookUtils.setPages(players, 1));
                                    pages.add(HealthBookUtils.setPages(players, 2));
                                    pages.add(HealthBookUtils.setPages(players, 3));
                                    pages.add(HealthBookUtils.setPages(players, 4));
                                }
                            });
                        });
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClone(Clone event) {
        LazyOptional<IHealthCapability> capability = event.getOriginal().getCapability(HealthCapability.HEALTH_CAPABILITY);

        capability.ifPresent(oldStore -> event.getPlayer().getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(newStore -> newStore.onClone((DefaultHealthCapability) oldStore)));
    }

}