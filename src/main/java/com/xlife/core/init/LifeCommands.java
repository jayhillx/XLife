package com.xlife.core.init;

import com.xlife.common.capability.LifeCapability;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

import static com.xlife.common.util.LifeTextUtils.*;

public class LifeCommands {

    /** Base for sending commands. */
    public static void sendCommand(PlayerEntity player, String command) {
        player.getServer().getCommands().performCommand(player.getServer().createCommandSourceStack().withSuppressedOutput(), command);
    }

    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("sethealth").requires(commandSource -> commandSource.hasPermission(2)).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("hearts", IntegerArgumentType.integer()).executes(context -> setHealth(context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "hearts"))))));
        dispatcher.register(Commands.literal("gethealth").requires(commandSource -> commandSource.hasPermission(2)).then(Commands.argument("targets", EntityArgument.players()).executes(context -> getHealth(context.getSource(), EntityArgument.getPlayers(context, "targets")))));
        dispatcher.register(Commands.literal("gethistory").requires(commandSource -> commandSource.hasPermission(0)).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("players", EntityArgument.player()).executes(context -> getHistoryBook(context.getSource(), EntityArgument.getPlayer(context, "players"), EntityArgument.getPlayer(context, "targets"))))));
    }

    private static int setHealth(CommandSource source, Collection<ServerPlayerEntity> targets, int hearts) {
        if (!(hearts >= 11.0F)) {
            for (ServerPlayerEntity player : targets) {
                player.getCapability(LifeCapability.X_LIFE_CAPABILITIES).ifPresent(instance -> {
                    int health = hearts * 2;

                    instance.setMaxHealth(health);
                    player.setHealth(health);
                    player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
                });
                source.sendSuccess(new TranslationTextComponent(player.getScoreboardName() + "'s health has been set to " + setHearts(player.getMaxHealth(), player.getHealth(), 0, false)), false);
            }
        }
        return 1;
    }

    private static int getHealth(CommandSource source, Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {
            source.sendSuccess(new TranslationTextComponent(player.getScoreboardName() + "'s current health is " + setHearts(player.getMaxHealth(), player.getHealth(), player.getMaxHealth() - player.getHealth(), false)), false);
        }
        return 1;
    }

    private static int getHistoryBook(CommandSource source, ServerPlayerEntity targets, ServerPlayerEntity targetPlayers) {
        ItemStack stack = Items.WRITTEN_BOOK.getDefaultInstance();
        ListNBT pages = new ListNBT();

        targets.getCapability(LifeCapability.X_LIFE_CAPABILITIES).ifPresent(instance -> {
            stack.getOrCreateTag().putString("title", "Life History of " + targetPlayers.getScoreboardName());
            stack.getOrCreateTag().putString("author", "@secondchances");
            stack.getOrCreateTag().putUUID("owner", targetPlayers.getUUID());
            stack.getOrCreateTag().putInt("time", instance.getTimeLiving());
            stack.getOrCreateTag().put("pages", pages);
        });

        targets.inventory.add(stack);

        targets.level.playSound(null, targets.getX(), targets.getY(), targets.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((targets.getRandom().nextFloat() - targets.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        targets.inventoryMenu.broadcastChanges();
        return 1;
    }

}