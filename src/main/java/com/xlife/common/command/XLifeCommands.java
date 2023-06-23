package com.xlife.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.xlife.common.command.argument.SavedPlayerArgument;
import com.xlife.common.command.impl.HealthCommand;
import com.xlife.common.command.impl.LifeBookCommand;
import com.xlife.common.command.impl.PodiumCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.world.World;

public class XLifeCommands {

    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
        HealthCommand.register(dispatcher);
        LifeBookCommand.register(dispatcher);
        PodiumCommand.register(dispatcher);
    }

    public static void registerArgumentTypes() {
        ArgumentTypes.register("saved_players", SavedPlayerArgument.class, new ArgumentSerializer<>(SavedPlayerArgument::player));
    }

    /**
     * Base for running a command for a player.
     */
    public static void sendCommand(World world, String command) {
        if (world.getServer() != null) {
            world.getServer().getCommandManager().handleCommand(world.getServer().getCommandSource().withFeedbackDisabled(), command);
        }
    }

}