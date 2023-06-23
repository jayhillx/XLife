package com.xlife;

import com.xlife.common.capability.XLifeCapabilities;
import com.xlife.common.command.XLifeCommands;
import com.xlife.common.event.ItemEvents;
import com.xlife.common.event.LifeEvents;
import com.xlife.common.event.PodiumEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("xlife")
public class XLife {
    public static final String modId = "xlife";

    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(modId, path);
    }

    /**
     * todo:
     * tweak set health command
     * fix suggestions for saved players argument
     * rework podium command
     * health in player tablist
     */
    public XLife() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(new ItemEvents());
        MinecraftForge.EVENT_BUS.register(new LifeEvents());
        MinecraftForge.EVENT_BUS.register(new PodiumEvents());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        XLifeCapabilities.registerCapabilities();
        XLifeCommands.registerArgumentTypes();
    }

}