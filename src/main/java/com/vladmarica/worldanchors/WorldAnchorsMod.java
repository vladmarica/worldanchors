package com.vladmarica.worldanchors;

import com.vladmarica.worldanchors.client.EnderWorldAnchorGui;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
    Config.register(ModLoadingContext.get());
  }

  @SubscribeEvent
  public void onCommonSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(
        () ->
            ForgeChunkManager.setForcedChunkLoadingCallback(
                MODID, new ChunkLoaderHandler.ChunkLoadingValidator()));
  }

  @SubscribeEvent
  public void onClientSetup(FMLClientSetupEvent event) {
    ScreenManager.register(WorldAnchorsContainers.ENDER_ANCHOR_CONTAINER, EnderWorldAnchorGui::new);
  }
}
