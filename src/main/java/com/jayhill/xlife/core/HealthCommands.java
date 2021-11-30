package com.jayhill.xlife.core;

import com.jayhill.xlife.common.capability.health.HealthCapability;
import com.jayhill.xlife.common.capability.time.ITimeCapability;
import com.jayhill.xlife.common.capability.time.TimeCapability;
import com.jayhill.xlife.common.util.HealthBookUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.Collection;
import java.util.UUID;

import static net.minecraft.util.text.TextFormatting.*;
import static com.jayhill.xlife.common.util.HealthTextUtils.*;

@SuppressWarnings("all")
public class HealthCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("sethealth").requires(commandSource -> commandSource.hasPermissionLevel(2)).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("hearts", IntegerArgumentType.integer()).executes(context -> setHealth(context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "hearts"))))));
        dispatcher.register(Commands.literal("gethealth").requires(commandSource -> commandSource.hasPermissionLevel(2)).then(Commands.argument("targets", EntityArgument.players()).executes(context -> getHealth(context.getSource(), EntityArgument.getPlayers(context, "targets")))));
        dispatcher.register(Commands.literal("gethistory").requires(commandSource -> commandSource.hasPermissionLevel(0)).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("players", EntityArgument.player()).executes(context -> getHistory(context.getSource(), EntityArgument.getPlayer(context, "players"), EntityArgument.getPlayer(context, "targets"))))));
    }

    /** Sets the players health. */
    private static int setHealth(CommandSource source, Collection<ServerPlayerEntity> targets, int hearts) {
        int h = (hearts * 2);

        if (!(hearts >= 11)) {
            for (ServerPlayerEntity player : targets) {
                player.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
                    player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(h);
                    player.setHealth(h);
                    health.setMaxHealth(h);
                });

                source.sendFeedback(new TranslationTextComponent(player.getScoreboardName() + "'s health has been set to " + GRAY + "(" + getDynamicHearts(player.getMaxHealth()) + GRAY + ")"), false);
            }
        }
        return 1;
    }

    /** Gets the players health. */
    private static int getHealth(CommandSource source, Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {

            ITextComponent healthInfo = TextComponentUtils.toTextComponent((new StringTextComponent(getDynamicHearts(player.getMaxHealth()))).applyTextStyle((style) -> {
                style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("Max Health: " + player.getMaxHealth() + "\nHealth: " + player.getHealth())));
            }));

            source.sendFeedback(new TranslationTextComponent(player.getScoreboardName() + "'s current health is " + GRAY + "(" + "%s" + GRAY + ")", healthInfo), false);
        }
        return 1;
    }

    /** Gets a specified players' life history book. */
    private static int getHistory(CommandSource source, ServerPlayerEntity targets, ServerPlayerEntity targetPlayers) {
        ItemStack stack = Items.WRITTEN_BOOK.getDefaultInstance();
        ListNBT pages = new ListNBT();

        ITimeCapability time = targetPlayers.getCapability(TimeCapability.TIME_CAPABILITY).orElse(null);

        stack.getOrCreateTag().putString("title", "Life History of " + targetPlayers.getScoreboardName());
        stack.getOrCreateTag().putString("author", "@secondchances");
        stack.getOrCreateTag().putUniqueId("owner", targetPlayers.getUniqueID());
        stack.getOrCreateTag().put("pages", pages);

        if (stack.getTag() != null && stack.getTag().hasUniqueId("owner")) {
            UUID uuid = stack.getTag().getUniqueId("owner");

            targetPlayers.getCapability(HealthCapability.HEALTH_CAPABILITY).ifPresent(health -> {
                if (health.getMaxHealth() >= 2.0F && health.getMaxHealth() <= 6.0F) {
                    pages.add(HealthBookUtils.setPages(targetPlayers, 1));
                } else if (health.getMaxHealth() >= 8.0F && health.getMaxHealth() <= 10.0F) {
                    pages.add(HealthBookUtils.setPages(targetPlayers, 1));
                    pages.add(HealthBookUtils.setPages(targetPlayers, 2));
                } else if (health.getMaxHealth() >= 12.0F && health.getMaxHealth() <= 18.0F) {
                    pages.add(HealthBookUtils.setPages(targetPlayers, 1));
                    pages.add(HealthBookUtils.setPages(targetPlayers, 2));
                    pages.add(HealthBookUtils.setPages(targetPlayers, 3));
                } else {
                    pages.add(HealthBookUtils.setPages(targetPlayers, 1));
                    pages.add(HealthBookUtils.setPages(targetPlayers, 2));
                    pages.add(HealthBookUtils.setPages(targetPlayers, 3));
                    pages.add(HealthBookUtils.setPages(targetPlayers, 4));
                }

                targets.inventory.addItemStackToInventory(stack);

                targets.world.playSound((PlayerEntity)null, targets.getPosX(), targets.getPosY(), targets.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((targets.getRNG().nextFloat() - targets.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                targets.container.detectAndSendChanges();
            });
        }
        return 1;
    }

}