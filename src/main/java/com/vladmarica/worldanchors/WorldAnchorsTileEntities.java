package com.vladmarica.worldanchors;

import com.vladmarica.worldanchors.tile.EnderWorldAnchorTileEntity;
import com.vladmarica.worldanchors.tile.FluxWorldAnchorTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = WorldAnchorsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WorldAnchorsMod.MODID)
public class WorldAnchorsTileEntities {

  private static final String ENDER_ANCHOR_NAME = "ender_anchor";
  private static final String FLUX_ANCHOR_NAME = "flux_anchor";

  @ObjectHolder(ENDER_ANCHOR_NAME)
  public static TileEntityType<EnderWorldAnchorTileEntity> ENDER_ANCHOR;

  @ObjectHolder(FLUX_ANCHOR_NAME)
  public static TileEntityType<FluxWorldAnchorTileEntity> FLUX_ANCHOR;

  @SubscribeEvent
  public static void onTileEntityRegistration(RegistryEvent.Register<TileEntityType<?>> event) {
    IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
    registry.register(
        TileEntityType.Builder.of(
                EnderWorldAnchorTileEntity::new, WorldAnchorsBlocks.ENDER_WORLD_ANCHOR)
            .build(null)
            .setRegistryName(ENDER_ANCHOR_NAME));

    registry.register(
        TileEntityType.Builder.of(
                FluxWorldAnchorTileEntity::new, WorldAnchorsBlocks.ENDER_WORLD_ANCHOR)
            .build(null)
            .setRegistryName(FLUX_ANCHOR_NAME));
  }

  private WorldAnchorsTileEntities() {}
}
