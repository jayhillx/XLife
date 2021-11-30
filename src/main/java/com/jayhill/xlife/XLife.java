package com.jayhill.xlife;

import com.jayhill.xlife.client.ClientHealthEvents;
import com.jayhill.xlife.common.capability.health.HealthCapability;
import com.jayhill.xlife.common.capability.health.HealthEvents;
import com.jayhill.xlife.common.capability.stats.StatsCapability;
import com.jayhill.xlife.common.capability.stats.StatsEvents;
import com.jayhill.xlife.common.capability.time.TimeCapability;
import com.jayhill.xlife.common.capability.time.TimeEvents;
import com.jayhill.xlife.core.HealthCommands;
import com.jayhill.xlife.core.HealthTabList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("xlife")
public class XLife {
    public static final String MOD_ID = "xlife";

    public XLife() {
        IEventBus event = FMLJavaModLoadingContext.get().getModEventBus();

        event.addListener(this::setup);
        event.addListener(this::setupClient);

        MinecraftForge.EVENT_BUS.register(new HealthEvents());
        MinecraftForge.EVENT_BUS.register(new StatsEvents());
        MinecraftForge.EVENT_BUS.register(new TimeEvents());
        MinecraftForge.EVENT_BUS.register(new HealthTabList());
        MinecraftForge.EVENT_BUS.register(this);
    }

    /** Registers capabilities & events. */
    private void setup(final FMLCommonSetupEvent event) {
        HealthCapability.registerCapability();
        StatsCapability.registerCapability();
        TimeCapability.registerCapability();
    }

    private void setupClient(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientHealthEvents());
    }

    @SubscribeEvent
    public void serverSetup(final FMLServerStartingEvent event) {
        HealthCommands.register(event.getServer().getFunctionManager().getCommandDispatcher());
    }

}