package com.vladmarica.worldanchors;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WorldAnchorsMod.MODID)
public class WorldAnchorsMod {
  public static final Logger LOGGER = LogManager.getLogger();
  public static final String MODID = "worldanchors";

  public WorldAnchorsMod() {
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onCommonSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(
        () ->
            ForgeChunkManager.setForcedChunkLoadingCallback(
                MODID, new ChunkLoaderHandler.ChunkLoadingValidator()));
  }
}
