package com.xlife.common.command.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.xlife.common.capability.world.WorldCapability;
import com.xlife.common.command.argument.SavedPlayerArgument;
import com.xlife.common.data.PlayerInformationData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

import java.util.Collection;
import java.util.Collections;

public class LifeBookCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("gethistory").requires((source) -> {
            return source.hasPermissionLevel(0);
        }).executes((context) -> {
            return getHistory(context.getSource());
        }).then(Commands.argument("player", SavedPlayerArgument.player()).executes((context) -> {
            return getHistory(SavedPlayerArgument.getPlayer(context, "player"), context.getSource().asPlayer());
        })).then(Commands.argument("player", SavedPlayerArgument.player()).then(Commands.argument("targets", EntityArgument.player()).executes((context) -> {
            return getHistory(SavedPlayerArgument.getPlayer(context, "player"), EntityArgument.getPlayer(context, "targets"));
        }))));
    }

    private static int getHistory(CommandSource source) throws CommandSyntaxException {
        return getHistory(Collections.singleton(source.asPlayer().getGameProfile()), source.asPlayer());
    }

    private static int getHistory(Collection<GameProfile> profiles, ServerPlayerEntity player) {
        Item item = Items.WRITTEN_BOOK;

        if (item != null) {
            player.world.getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                for (GameProfile profile : profiles) {
                    PlayerInformationData data = world.getInformationById(profile);

                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("title", "Life History of " + data.getPlayer().getName());
                    nbt.putString("author", "@secondchances");
                    nbt.put("pages", new ListNBT());
                    NBTUtil.writeGameProfile(nbt, data.getPlayer());

                    try {
                        ItemStack stack = new ItemInput(item, nbt).createStack(1, false);
                        boolean flag = player.inventory.addItemStackToInventory(stack);

                        if (flag && stack.isEmpty()) {
                            stack.setCount(1);
                            ItemEntity itemEntity = player.dropItem(stack, false);
                            if (itemEntity != null) {
                                itemEntity.makeFakeItem();
                            }
                            player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            player.container.detectAndSendChanges();
                        }
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return 1;
    }

}