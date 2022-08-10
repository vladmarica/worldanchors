package com.vladmarica.worldanchors;

import com.google.common.collect.Lists;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = WorldAnchorsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config {

  private static ForgeConfigSpec COMMON_CONFIG;

  static {
    ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    // Common config values
    builder.push("anchors");
    builder.comment("Config for the Ender World Anchor").push("ender");
    builder.define("fuel", Lists.newArrayList(Items.ENDER_EYE.getRegistryName() + "=100"));
    builder.pop();

    builder.comment("Config for the Flux World Anchor").push("flux");
    builder.define("rf_per_tick", 100);
    builder.define("rf_buffer_size", 10000);
    builder.pop(2);

    COMMON_CONFIG = builder.build();
  }

  public static void register(ModLoadingContext context) {
    context.registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
  }

  private Config() {}
}
