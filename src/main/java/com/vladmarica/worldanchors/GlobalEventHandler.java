package com.vladmarica.worldanchors;

import com.vladmarica.worldanchors.block.AbstractWorldAnchorBlock;
import com.vladmarica.worldanchors.tile.AbstractWorldAnchorTileEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WorldAnchorsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GlobalEventHandler {

  @SubscribeEvent
  public static void onBlockBreak(BlockEvent.BreakEvent event) {
    if (event.getState().getBlock() instanceof AbstractWorldAnchorBlock) {
      AbstractWorldAnchorTileEntity tileEntity =
          (AbstractWorldAnchorTileEntity) event.getWorld().getBlockEntity(event.getPos());
      if (tileEntity != null
          && !event.getPlayer().getGameProfile().getId().equals(tileEntity.getOwner())) {
        event.setCanceled(true);
      }
    }
  }

  private GlobalEventHandler() {}
}
