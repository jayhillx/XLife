package com.xlife.common.network.server;

import com.xlife.XLife;
import com.xlife.common.capability.world.WorldCapability;
import com.xlife.common.command.XLifeCommands;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import static net.minecraft.util.text.TextFormatting.*;

@Mod.EventBusSubscriber(modid = XLife.modId, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class XLifeServerHandler {

    @SubscribeEvent
    public static void onServerStarting(final FMLServerStartingEvent event) {
        XLifeCommands.registerCommands(event.getServer().getCommandManager().getDispatcher());

        event.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
            String s = "              ";

            event.getServer().setMOTD(GREEN + s + "Oldest Living: " + GRAY + world.getOldestLiving() + "\n" + RED + s + "Latest Death: " + GRAY + world.getLatestDeath());
        });
    }

}