package com.xlife.common.event;

import com.xlife.common.capability.world.WorldCapability;
import com.xlife.common.data.PlayerInformationData;
import com.xlife.common.item.LifeBookItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemEvents {

    @SubscribeEvent
    public void onItemUse(RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();

        if (!player.world.isRemote && stack.getTag() != null) {
            if (stack.getItem() == Items.WRITTEN_BOOK) {

                if (player.getServer() != null) {
                    player.getServer().getWorld(DimensionType.OVERWORLD).getCapability(WorldCapability.WORLD_DATA).ifPresent((world) -> {
                        PlayerInformationData data = world.getInformationById(NBTUtil.readGameProfile(stack.getTag()));

                        ListNBT pages = new ListNBT();
                        if (data.getCurrentLife() != null) {
                            if (data.getCurrentLife().getHearts() >= 1) {
                                pages.add((new LifeBookItem()).addPageEntry(data, 1));
                            } if (data.getCurrentLife().getHearts() >= 4) {
                                pages.add((new LifeBookItem()).addPageEntry(data, 2));
                            } if (data.getCurrentLife().getHearts() >= 7) {
                                pages.add((new LifeBookItem()).addPageEntry(data, 3));
                            } if (data.getCurrentLife().getHearts() >= 10) {
                                pages.add((new LifeBookItem()).addPageEntry(data, 4));
                            }
                        }
                        stack.getTag().put("pages", pages);
                        stack.getTag().remove("resolved");
                    });
                }
            }
        }
    }

}