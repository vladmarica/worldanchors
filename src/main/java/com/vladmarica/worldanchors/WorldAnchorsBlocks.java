package com.vladmarica.worldanchors;

import com.vladmarica.worldanchors.block.AbstractWorldAnchorBlock;
import com.vladmarica.worldanchors.block.EnderWorldAnchorBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = WorldAnchorsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WorldAnchorsMod.MODID)
public class WorldAnchorsBlocks {
  private static final String ENDER_WORLD_ANCHOR_NAME = "ender_anchor";
  private static final Item.Properties ITEM_PROPERTIES =
      new Item.Properties().tab(ItemGroup.TAB_MISC);

  @ObjectHolder(ENDER_WORLD_ANCHOR_NAME)
  public static AbstractWorldAnchorBlock ENDER_WORLD_ANCHOR;

  @SubscribeEvent
  public static void onBlockRegistration(RegistryEvent.Register<Block> event) {
    event
        .getRegistry()
        .registerAll(new EnderWorldAnchorBlock().setRegistryName(ENDER_WORLD_ANCHOR_NAME));
  }

  @SubscribeEvent
  public static void onItemRegistration(RegistryEvent.Register<Item> event) {
    event
        .getRegistry()
        .registerAll(
            new BlockItem(ENDER_WORLD_ANCHOR, ITEM_PROPERTIES)
                .setRegistryName(ENDER_WORLD_ANCHOR_NAME));
  }
}
