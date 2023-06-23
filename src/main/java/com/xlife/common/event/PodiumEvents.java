package com.xlife.common.event;

import com.xlife.common.capability.player.PlayerCapability;
import com.xlife.common.capability.world.WorldCapability;
import com.xlife.common.command.XLifeCommands;
import com.xlife.common.data.PodiumData;
import com.xlife.common.util.LifeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class PodiumEvents {

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        if (event.phase == TickEvent.Phase.START) {
            player.getCapability(PlayerCapability.PLAYER_DATA).ifPresent((life) -> {

                if (player.getServer() != null) {
                    player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                        PodiumData podiumData = world.getPodiumById(player.getGameProfile());
                        int i = (int) (life.getMaxHealth() / 2);

                        for (UUID statueId : podiumData.getStatue().keySet()) {
                            Entity entity = player.getServer().getWorld(player.dimension).getEntityByUuid(statueId);

                            if (entity != null) {
                                BlockPos signPos = world.getPodiumById(player.getGameProfile()).getSignPos();

                                XLifeCommands.sendCommand(player.world, "/data merge block " + signPos.getX() + " " + signPos.getY() + " " + signPos.getZ() + " {Text1:'{\"text\":\"" + podiumData.getPlayerId().getName() + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"gethistory " + podiumData.getPlayerId().getName() + " @s\"}}',Text2:'" + LifeUtils.signHearts(i * 2) + "',Text3:'{\"text\":\"\"}',Text4:'{\"text\":\"[Life Book]\",\"color\":\"dark_gray\"}'}");
                                XLifeCommands.sendCommand(player.world, "/tp " + entity.getUniqueID() + " " + entity.posX + " " + (podiumData.getStatue().get(statueId).getPos().getY() - (0.2 * i) + 0.2) + " " + entity.posZ);
                            }
                        }
                    });
                }
            });
        }
    }

}