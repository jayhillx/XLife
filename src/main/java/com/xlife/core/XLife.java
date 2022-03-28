package com.xlife.core;

import com.xlife.client.ClientLifeEvents;
import com.xlife.common.capability.LifeCapability;
import com.xlife.common.capability.LifeEvents;
import com.xlife.core.init.LifeCommands;
import net.minecraft.util.ResourceLocation;
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

    /** Sets resource location with modID. */
    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public XLife() {
        IEventBus event = FMLJavaModLoadingContext.get().getModEventBus();
        event.addListener(this::setup);
        event.addListener(this::setupClient);

        MinecraftForge.EVENT_BUS.register(new LifeEvents());
        MinecraftForge.EVENT_BUS.register(this);
    }

    /** Registers capabilities & events. */
    private void setup(final FMLCommonSetupEvent event) {
        LifeCapability.registerCapability();
    }

    private void setupClient(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientLifeEvents());
    }

    @SubscribeEvent
    public void setupServer(final FMLServerStartingEvent event) {
        LifeCommands.registerCommands(event.getServer().getCommands().getDispatcher());
    }

}