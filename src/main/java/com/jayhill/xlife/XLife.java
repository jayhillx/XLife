package com.jayhill.xlife;

import com.jayhill.xlife.client.util.HealthColorUtils;
import com.jayhill.xlife.common.capability.health.HealthCapability;
import com.jayhill.xlife.common.capability.health.HealthEvents;
import com.jayhill.xlife.common.capability.stats.StatsCapability;
import com.jayhill.xlife.common.capability.stats.StatsEvents;
import com.jayhill.xlife.common.capability.time.TimeCapability;
import com.jayhill.xlife.common.capability.time.TimeEvents;
import com.jayhill.xlife.core.HealthCommands;
import com.jayhill.xlife.core.HealthMessages;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("xlife")
public class XLife {
    public static final String MOD_ID = "xlife";

    public XLife() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(new HealthEvents());
        MinecraftForge.EVENT_BUS.register(new StatsEvents());
        MinecraftForge.EVENT_BUS.register(new TimeEvents());
        MinecraftForge.EVENT_BUS.register(new HealthColorUtils());
        MinecraftForge.EVENT_BUS.register(this);
    }

    /** Registers capabilities & events. */
    private void setup(final FMLCommonSetupEvent event) {
        HealthCapability.registerCapability();
        StatsCapability.registerCapability();
        TimeCapability.registerCapability();

        MinecraftForge.EVENT_BUS.addListener(HealthMessages::onDeath);
        MinecraftForge.EVENT_BUS.addListener(HealthMessages::onTick);
    }

    @SubscribeEvent
    public void serverSetup(final FMLServerStartingEvent event) {
        HealthCommands.register(event.getServer().getCommands().getDispatcher());
    }

}