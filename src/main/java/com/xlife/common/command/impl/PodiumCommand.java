package com.xlife.common.command.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.xlife.common.capability.world.WorldCapability;
import com.xlife.common.command.XLifeCommands;
import com.xlife.common.command.argument.SavedPlayerArgument;
import com.xlife.common.data.PlayerInformationData;
import com.xlife.common.data.PodiumData;
import com.xlife.common.data.PodiumData.Statue;
import com.xlife.common.util.LifeUtils;
import net.minecraft.block.WallSignBlock;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.xlife.common.command.XLifeCommands.sendCommand;

public class PodiumCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("podium").requires((source) -> {
            return source.hasPermissionLevel(2);
        }).then(Commands.literal("add").then(Commands.argument("player", SavedPlayerArgument.player()).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes((context) -> {
            return setStatue(context.getSource(), context.getArgument("player", String.class), BlockPosArgument.getLoadedBlockPos(context, "pos"));
        })))).then(Commands.literal("remove").then(Commands.argument("player", SavedPlayerArgument.player()).executes((context) -> {
            return removeStatue(context.getSource(), context.getArgument("player", String.class));
        }))));

        dispatcher.register(Commands.literal("setsign").requires((source) -> {
            return source.hasPermissionLevel(2);
        }).then(Commands.argument("player", SavedPlayerArgument.player()).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes((context) -> {
            return setSign(context.getSource(), context.getArgument("player", String.class), BlockPosArgument.getLoadedBlockPos(context, "pos"));
        }))));
    }

    private static int setSign(CommandSource source, String name, BlockPos pos) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();

        if (player.getServer() != null) {
            player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                GameProfile profile = player.getServer().getPlayerProfileCache().getGameProfileForUsername(name);

                if (world.getPlayers().containsKey(profile)) {
                    if (player.world.getBlockState(pos).getBlock() instanceof WallSignBlock) {
                        PodiumData podiumData = world.getPodiumById(profile);
                        podiumData.setSignPos(pos);

                        PlayerInformationData playerData = world.getInformationById(profile);

                        BlockPos signPos = world.getPodiumById(player.getGameProfile()).getSignPos();
                        XLifeCommands.sendCommand(player.world, "/data merge block " + signPos.getX() + " " + signPos.getY() + " " + signPos.getZ() + " {Text1:'{\"text\":\"" + podiumData.getPlayerId().getName() + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"gethistory " + podiumData.getPlayerId().getName() + " @s\"}}',Text2:'" + LifeUtils.signHearts(playerData.getCurrentLife().getHearts() * 2) + "',Text3:'{\"text\":\"\"}',Text4:'{\"text\":\"[Life Book]\",\"color\":\"dark_gray\"}'}");
                    } else {
                        source.sendFeedback(new StringTextComponent("Block must be a wall sign").applyTextStyle(TextFormatting.RED), false);
                    }
                } else {
                    source.sendFeedback(new StringTextComponent("That player does not exist").applyTextStyle(TextFormatting.RED), false);
                }
            });
        }
        return 1;
    }

    private static int setStatue(CommandSource source, String name, BlockPos pos) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();

        if (player.getServer() != null) {
            player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                GameProfile profile = player.getServer().getPlayerProfileCache().getGameProfileForUsername(name);

                if (world.getPlayers().containsKey(profile)) {
                    List<BlockPos> list = new ArrayList<>();

                    PodiumData podiumData = world.getPodiumById(profile);
                    podiumData.setPlayerId(profile);

                    list.add(pos);
                    list.add(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
                    list.add(new BlockPos(pos.getX(), pos.getY() + 2, pos.getZ()));

                    for (BlockPos p : list) {
                        Statue statue = new Statue(new CompoundNBT());
                        statue.setId(UUID.randomUUID());
                        statue.setPos(p);
                        podiumData.getStatue().put(statue.getId(), statue);
                        sendCommand(player.world, "/summon falling_block " + statue.getPos().getX() + " " + statue.getPos().getY() + " " + statue.getPos().getZ() + " {BlockState:{Name:\"minecraft:gold_block\"},NoGravity:1,Time:-2147483648,UUIDMost:" + statue.getId().getMostSignificantBits() + "L,UUIDLeast:" + statue.getId().getLeastSignificantBits() + "L}");
                        sendCommand(player.world, "/setblock " + statue.getPos().getX() + " " + statue.getPos().getY() + " " + statue.getPos().getZ() + " minecraft:barrier");
                    }
                    Statue statue = new Statue(new CompoundNBT());
                    statue.setId(UUID.randomUUID());
                    statue.setPos(new BlockPos(pos.getX(), pos.getY() + 3, pos.getZ()));
                    podiumData.getStatue().put(statue.getId(), statue);

                    podiumData.setSignPos(pos);
                    sendCommand(player.world, "/summon armor_stand " + statue.getPos().getX() + " " + statue.getPos().getY() + " " + statue.getPos().getZ() + " {Rotation:[90.0f],HandItems:[{},{}],ArmorItems:[{},{},{},{id:\"minecraft:player_head\",Count:1b,tag:{SkullOwner:" + podiumData.getPlayerId().getName() + "}}],Invulnerable:1b,NoGravity:1b,ShowArms:1b,UUIDMost:" + statue.getId().getMostSignificantBits() + "L,UUIDLeast:" + statue.getId().getLeastSignificantBits() + "L}");
                } else {
                    source.sendFeedback(new StringTextComponent("That player does not exist").applyTextStyle(TextFormatting.RED), false);
                }
            });
        }
        return 1;
    }

    /** Removes the players' statue from the world and the save itself. */
    private static int removeStatue(CommandSource source, String name) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();

        if (player.getServer() != null) {
            player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                GameProfile profile = player.getServer().getPlayerProfileCache().getGameProfileForUsername(name);

                if (world.getPlayers().containsKey(profile)) {
                    PodiumData podiumData = world.getPodiumById(profile);

                    sendCommand(player.world, "/setblock " + podiumData.getSignPos().getX() + " " + podiumData.getSignPos().getY() + " " + podiumData.getSignPos().getZ() + " " + " minecraft:air");

                    for (Statue statue : podiumData.getStatue().values()) {
                        BlockPos p = statue.getPos();

                        sendCommand(player.world, "/fill " + p.getX() + " " + p.getY() + " " + p.getZ() + " " + p.getX() + " " + p.getY() + " " + p.getZ() + " minecraft:air");
                        sendCommand(player.world, "/kill " + statue.getId());
                    }
                    world.getPodiums().remove(profile);
                } else {
                    source.sendFeedback(new StringTextComponent("That player does not exist").applyTextStyle(TextFormatting.RED), false);
                }
            });
        }
        return 1;
    }

}