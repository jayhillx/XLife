package com.xlife.common.capability;

import com.xlife.common.util.LifeBookUtils;
import com.xlife.common.util.LifeTextUtils;
import com.xlife.core.XLife;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.xlife.core.init.LifeCommands.*;

/** Handles every player event in X Life. */
public class LifeEvents {

    private static int tickCount;
    private static String cause;

    private static int getTickCount() {
        return tickCount;
    }

    private static void setTickCount(int count) {
        tickCount = count;
    }

    @SubscribeEvent
    public void onJoin(PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();

        player.getCapability(LifeCapability.X_LIFE_CAPABILITIES).ifPresent(instance -> {
            if (instance.getMaxHealth() == 0) {
                instance.setMaxHealth(2.0F);

                // Gives new player Life History Book.
                sendCommand(player, "/getHistory " + player.getScoreboardName() + " " + player.getScoreboardName());

                player.setHealth(instance.getMaxHealth());
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(instance.getMaxHealth());
            }
        });
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        Entity sourceEntity = event.getSource().getEntity();

        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();

            player.getCapability(LifeCapability.X_LIFE_CAPABILITIES).ifPresent(instance -> {
                int hearts = (int) (instance.getMaxHealth() - 2) / 2;

                if (instance.getMaxHealth() >= 2.0F && instance.getMaxHealth() <= 22.0F) {
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
                            cause = "Withered";
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

                    String[] causeArray = instance.getCause();
                    causeArray[hearts] = cause;

                    String[] messageArray = instance.getMessage();
                    messageArray[hearts] = player.getCombatTracker().getDeathMessage().getString();

                    String[] timeArray = instance.getTimeLasted();
                    timeArray[hearts] = LifeTextUtils.getTimeLiving(player);

                    // Update capability values.
                    instance.setMaxHealth(instance.getMaxHealth() + 2.0F);
                    instance.setCause(causeArray);
                    instance.setMessage(messageArray);
                    instance.setTimeLasted(timeArray);
                    instance.setTimeLiving(0);
                }

                LifeTextUtils.getRemainingLives(player);

                // Spawns lightning when a player dies on their final life.
                if (instance.getMaxHealth() == 22.0F) {
                    LightningBoltEntity lighting = EntityType.LIGHTNING_BOLT.create(player.level);

                    if (lighting != null) {
                        lighting.moveTo(Vector3d.atBottomCenterOf(player.blockPosition()));
                        lighting.setVisualOnly(true);
                        player.level.addFreshEntity(lighting);
                    }
                }
            });

            player.getServer().setMotd(TextFormatting.RED + "Latest Death: " + TextFormatting.RESET + player.getScoreboardName());
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();

        player.getCapability(LifeCapability.X_LIFE_CAPABILITIES).ifPresent(instance -> {
            String[] timeArray = instance.getTimeLasted();

            if (instance.getMaxHealth() >= 2.0F && instance.getMaxHealth() <= 22.0F) {
                // Health
                player.setHealth(instance.getMaxHealth());
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(instance.getMaxHealth());
                // Title
                if (player.getServer() != null) {
                    if (instance.getMaxHealth() < 20.0F) {
                        int lives = (int) (10 - ((instance.getMaxHealth() - 2) / 2));

                        if (lives >= 2) {
                            sendCommand(player, "/title " + player.getScoreboardName() + " title \"" + lives + " lives remain\"");
                        } else if (lives == 1) {
                            sendCommand(player, "/title " + player.getScoreboardName() + " title \"1 life remains\"");
                        } else {
                            sendCommand(player, "/title " + player.getScoreboardName() + " title \"Eliminated\"");
                        }
                        sendCommand(player, "/title " + player.getScoreboardName() + " subtitle \"You lasted " + timeArray[(int) (instance.getMaxHealth() / 2) - 2] + "\"");
                    }
                }

                if (instance.getMaxHealth() == 22.0F) {
                    player.setGameMode(GameType.SPECTATOR);
                }
                player.level.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundCategory.AMBIENT, 1F, 1F);
            }
        });
    }

    @SubscribeEvent
    public void onItemUse(RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();

        if (player.level.isClientSide || stack.getItem() != Items.WRITTEN_BOOK || stack.getTag() == null || !stack.getTag().hasUUID("owner")) {
            return;
        }
        ServerPlayerEntity players = player.getServer().getPlayerList().getPlayer(stack.getTag().getUUID("owner"));
        if (players == null) {
            return;
        }

        players.getCapability(LifeCapability.X_LIFE_CAPABILITIES).ifPresent(life -> {
            // Creates the books pages.
            ListNBT pages = new ListNBT();
            // Refresh books content.
            stack.getTag().put("pages", pages);
            stack.getTag().remove("resolved");

            if (life.getMaxHealth() >= 2.0F) {
                pages.add(LifeBookUtils.setPages(players, 1));
            } if (life.getMaxHealth() >= 8.0F) {
                pages.add(LifeBookUtils.setPages(players, 2));
            } if (life.getMaxHealth() >= 14.0F) {
                pages.add(LifeBookUtils.setPages(players, 3));
            } if (life.getMaxHealth() >= 20.0F) {
                pages.add(LifeBookUtils.setPages(players, 4));
            }
        });
    }

    @SubscribeEvent
    public void onName(TabListNameFormat event) {
        PlayerEntity player = event.getPlayer();

        player.getCapability(LifeCapability.X_LIFE_CAPABILITIES).ifPresent(life -> {
            if (life.getMaxHealth() < 22.0F) {
                event.setDisplayName(new TranslationTextComponent(player.getScoreboardName() + " " + LifeTextUtils.setHearts(life.getMaxHealth(), player.getHealth(), player.getMaxHealth() - player.getHealth(), false)));
            } else if (player.isSpectator()) {
                event.setDisplayName(player.getDisplayName());
            }
        });
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        player.getCapability(LifeCapability.X_LIFE_CAPABILITIES).ifPresent(instance -> {
            if (player.getServer() != null) {

                if (event.phase == TickEvent.Phase.START && event.side.isServer()) {
                    if (getTickCount() == 20) {
                        instance.setTimeLiving(instance.getTimeLiving() + 1);
                        setTickCount(0);
                    } else {
                        if (player.isAlive()) {
                            setTickCount(getTickCount() + 1);
                        }
                    }
                }

                for (ServerPlayerEntity players : player.getServer().getPlayerList().getPlayers()) {
                    players.refreshTabListName();
                }
            }
        });
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            LifeCapabilityProvider provider = new LifeCapabilityProvider();
            event.addCapability(XLife.getId("life"), provider);
        }
    }

    @SubscribeEvent
    public void cloneCapability(Clone event) {
        LazyOptional<ILifeCapability> capability = event.getOriginal().getCapability(LifeCapability.X_LIFE_CAPABILITIES);

        capability.ifPresent(oldStore -> event.getPlayer().getCapability(LifeCapability.X_LIFE_CAPABILITIES).ifPresent(newStore -> newStore.onClone((DefaultLifeCapability) oldStore)));
    }

}