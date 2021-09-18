package com.jayhill.xlife.common.capability.health;

import com.jayhill.xlife.XLife;
import com.jayhill.xlife.common.util.HealthBookUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Handles things such as respawn, & death event. */
@SuppressWarnings("all")
public class HealthEvents {

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            HealthCapabilityProvider provider = new HealthCapabilityProvider();
            event.addCapability(new ResourceLocation(XLife.MOD_ID, "health"), provider);
        }
    }

    @SubscribeEvent
    public void cloneCapability(Clone event) {
        LazyOptional<IHealthCapability> capability = event.getOriginal().getCapability(HealthCapability.HEALTH_CAPABILITY);

        capability.ifPresent((oldStore) -> event.getPlayer().getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent((newStore) -> newStore.onDeath(oldStore)));
    }

    @SubscribeEvent
    public void onJoin(PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = Items.WRITTEN_BOOK.getDefaultInstance();

        CompoundNBT nbt = stack.getOrCreateTag();
        ListNBT pages = new ListNBT();

        player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(instance -> {
            if (instance.getMaxHealth() == 0.0F) {
                instance.setMaxHealth(2.0F);

                /** Health */
                player.setHealth(instance.getMaxHealth());
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(instance.getMaxHealth());

                /** Life Book */
                nbt.putString("title", "Life History of " + player.getScoreboardName());
                nbt.putString("author", "@secondchances");
                nbt.putString("uuid", player.getStringUUID());
                nbt.put("pages", pages);

                pages.add(HealthBookUtils.setPages(player, 1));

                stack.setTag(nbt);
                player.setItemInHand(Hand.MAIN_HAND, stack);
            }
        });
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity.getEntity();

            if (player.getMaxHealth() >= 2.0F && player.getMaxHealth() <= 22.0F) {
                player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(instance -> {
                    instance.setMaxHealth((int) (player.getMaxHealth() + 2));
                });
            }

            /** Summons lighting if a player dies on their last life. */
            if (player.getMaxHealth() == 20.0F) {
                LightningBoltEntity lighting = EntityType.LIGHTNING_BOLT.create(player.level);
                if (lighting != null) {
                    lighting.moveTo(Vector3d.atBottomCenterOf(player.getEntity().blockPosition()));
                    lighting.setVisualOnly(true);
                    lighting.setCause(player instanceof ServerPlayerEntity ? (ServerPlayerEntity)player : null);
                    player.level.addFreshEntity(lighting);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();

        player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
            if (player.getMaxHealth() >= 2.0F && player.getMaxHealth() <= 20.0F) {
                player.setHealth(health.getMaxHealth());
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health.getMaxHealth());

            } if (player.getMaxHealth() >= 22.0F) {
                player.setGameMode(GameType.SPECTATOR);
            }
        });
    }

}