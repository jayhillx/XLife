package com.xlife.common.capability;

import com.xlife.XLife;
import com.xlife.common.capability.player.IPlayerInformation;
import com.xlife.common.capability.player.PlayerCapability;
import com.xlife.common.capability.player.StoredPlayerInformation;
import com.xlife.common.capability.world.IWorldInformation;
import com.xlife.common.capability.world.WorldCapability;
import com.xlife.common.capability.world.StoredWorldInformation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = XLife.modId, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class XLifeCapabilities {

    /** Register both world & player capabilities. */
    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(IWorldInformation.class, new WorldCapability.Storage(), StoredWorldInformation::new);
        CapabilityManager.INSTANCE.register(IPlayerInformation.class, new PlayerCapability.Storage(), StoredPlayerInformation::new);
    }

    @SubscribeEvent
    public static void worldCapability(AttachCapabilitiesEvent<World> event) {
        event.addCapability(XLife.modLoc("data"), new WorldCapability.Provider());
    }

    @SubscribeEvent
    public static void playerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(XLife.modLoc("data"), new PlayerCapability.Provider());
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(PlayerCapability.PLAYER_DATA).ifPresent(oldStore -> event.getPlayer().getCapability(PlayerCapability.PLAYER_DATA).ifPresent(newStore -> newStore.clonePlayerInfo((StoredPlayerInformation)oldStore)));
    }

}