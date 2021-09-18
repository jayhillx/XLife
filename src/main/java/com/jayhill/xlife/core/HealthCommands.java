package com.jayhill.xlife.core;

import com.jayhill.xlife.common.capability.health.HealthCapability;
import com.jayhill.xlife.common.capability.stats.StatsCapability;
import com.jayhill.xlife.common.capability.time.ITimeCapability;
import com.jayhill.xlife.common.capability.time.TimeCapability;
import com.jayhill.xlife.common.util.HealthBookUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;

import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings("all")
public class HealthCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        /** Commands. */
        dispatcher.register(Commands.literal("sethealth").requires(commandSource -> commandSource.hasPermission(2)).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("hearts", IntegerArgumentType.integer()).executes(context -> setHealthCommand(context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "hearts"))))));
        dispatcher.register(Commands.literal("gethealth").requires(commandSource -> commandSource.hasPermission(2)).then(Commands.argument("targets", EntityArgument.players()).executes(context -> getHealthCommand(context.getSource(), EntityArgument.getPlayers(context, "targets")))));
        dispatcher.register(Commands.literal("debug").requires(commandSource -> commandSource.hasPermission(2)).then(Commands.argument("targets", EntityArgument.players()).executes(context -> debug(context.getSource(), EntityArgument.getPlayers(context, "targets")))));
        dispatcher.register(Commands.literal("history").requires(commandSource -> commandSource.hasPermission(0)).then(Commands.argument("targets", EntityArgument.players()).executes(context -> getLifeBook(context.getSource(), EntityArgument.getPlayers(context, "targets")))));
    }

    private static int setHealthCommand(CommandSource source, Collection<ServerPlayerEntity> targets, int hearts) {
        int h = (hearts * 2);
        if (!(hearts >= 11)) {
            for (ServerPlayerEntity player : targets) {
                player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
                    player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(h);
                    player.setHealth(h);
                    health.setMaxHealth(h);

                    player.getCapability(StatsCapability.STATS_CAPABILITY).ifPresent(stats -> {
                        for (int i = 9; i >= hearts - 1; --i) {
                            String[] stringArray = stats.getCause();
                            String[] intArray = stats.getTime();
                            stringArray[i] = "none";
                            stats.setCause(stringArray);
                            intArray[i] = "0";
                            stats.setTime(intArray);
                        }
                    });

                    player.getCapability(TimeCapability.TIME_CAPABILITY).ifPresent(time -> {
                        time.setTime(0);
                    });
                });

                source.sendSuccess(new TranslationTextComponent("Your health has been set to " + TextFormatting.WHITE + "(" + hearts(player.getMaxHealth() / 2) + TextFormatting.WHITE + ")"), true);
            }
        }
        return 1;
    }

    private static int getHealthCommand(CommandSource source, Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {

            ITextComponent healthInfo = TextComponentUtils.fromMessage((new StringTextComponent(hearts(player.getMaxHealth() / 2))).withStyle((style) -> {
                return style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("Health: " + player.getHealth() + "\nMax Health: " + player.getMaxHealth())));
            }));

            source.sendSuccess(new TranslationTextComponent("Your current health is (%s)", healthInfo), true);
        }
        return 1;
    }

    private static String hearts(float hearts) {
        char h = 10084;

        if (hearts == 1) {
            return TextFormatting.RED + "" + h;
        } else if (hearts == 2) {
            return TextFormatting.BLUE + "" + h+h;
        } else if (hearts == 3) {
            return TextFormatting.DARK_GREEN + "" + h+h+h;
        } else if (hearts == 4) {
            return TextFormatting.GOLD + "" + h+h+h+h;
        } else if (hearts == 5) {
            return TextFormatting.LIGHT_PURPLE + "" + h+h+h+h+h;
        } else if (hearts == 6) {
            return TextFormatting.DARK_PURPLE + "" + h+h+h+h+h+h;
        } else if (hearts == 7) {
            return TextFormatting.YELLOW + "" + h+h+h+h+h+h+h;
        } else if (hearts == 8) {
            return TextFormatting.DARK_AQUA + "" + h+h+h+h+h+h+h+h;
        } else if (hearts == 9) {
            return TextFormatting.GREEN + "" + h+h+h+h+h+h+h+h+h;
        } else if (hearts == 10) {
            return TextFormatting.BLACK + "" + h+h+h+h+h+h+h+h+h+h;
        } else {
            return "";
        }
    }

    public static int debug(CommandSource source, Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {
            player.getCapability(StatsCapability.STATS_CAPABILITY).ifPresent(stats -> {
                player.getCapability(TimeCapability.TIME_CAPABILITY).ifPresent(time -> {
                    source.sendSuccess(new TranslationTextComponent("Living: " + living(player)), true);
                    source.sendSuccess(new TranslationTextComponent("Times: " + Arrays.toString(stats.getTime())), true);
                    source.sendSuccess(new TranslationTextComponent("Cause: " + Arrays.toString(stats.getCause())), true);
                });
            });
        }
        return 1;
    }

    private static String living(PlayerEntity player) {
        ITimeCapability time = player.getCapability(TimeCapability.TIME_CAPABILITY).orElse(null);

        if (time.getTime() == 1) {
            return "1 second";
        } else if (time.getTime() < 60) {
            return time.getTime() + " seconds";
        } else if (time.getTime() > 60 && time.getTime() < 120) {
            return "1 minute & " + time.getTime() % 60 + " seconds";
        } else if (time.getTime() >= 120 && time.getTime() < 3600) {
            return time.getTime() / 60 + " minutes & " + time.getTime() % 60 + " seconds";
        } else if (time.getTime() >= 3600 && time.getTime() < 7200) {
            return "1 hour";
        } else {
            return time.getTime() / 3600 + " hours";
        }
    }

    private static int getLifeBook(CommandSource source, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        for (ServerPlayerEntity players : targets) {
            ItemStack stack = Items.WRITTEN_BOOK.getDefaultInstance();

            CompoundNBT nbt = stack.getOrCreateTag();
            ListNBT pages = new ListNBT();

            /** Life Book */
            nbt.putString("title", "Life History of " + players.getScoreboardName());
            nbt.putString("author", "@secondchances");
            nbt.putString("uuid", players.getStringUUID());
            nbt.put("pages", pages);

            if (players.getMaxHealth() >= 2.0F && players.getMaxHealth() <= 6.0F) {
                pages.add(HealthBookUtils.setPages(players, 1));
            } else if (players.getMaxHealth() >= 8.0F && players.getMaxHealth() <= 12.0F) {
                pages.add(HealthBookUtils.setPages(players, 1));
                pages.add(HealthBookUtils.setPages(players, 2));
            } else if (players.getMaxHealth() >= 14.0F && players.getMaxHealth() <= 18.0F) {
                pages.add(HealthBookUtils.setPages(players, 1));
                pages.add(HealthBookUtils.setPages(players, 2));
                pages.add(HealthBookUtils.setPages(players, 3));
            } else {
                pages.add(HealthBookUtils.setPages(players, 1));
                pages.add(HealthBookUtils.setPages(players, 2));
                pages.add(HealthBookUtils.setPages(players, 3));
                pages.add(HealthBookUtils.setPages(players, 4));
            }

            stack.setTag(nbt);
            players.addItem(stack);

            players.level.playSound((PlayerEntity)null, players.getX(), players.getY(), players.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((players.getRandom().nextFloat() - players.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            players.inventoryMenu.broadcastChanges();
        }
        return targets.size();
    }

}