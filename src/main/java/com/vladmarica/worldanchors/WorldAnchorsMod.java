package com.vladmarica.worldanchors;

import net.minecraftforge.common.MinecraftForge;
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

  private void onCommonSetup(final FMLCommonSetupEvent event) {}
}
