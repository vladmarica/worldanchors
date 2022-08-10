package com.vladmarica.worldanchors;

import com.vladmarica.worldanchors.tile.EnderWorldAnchorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = WorldAnchorsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WorldAnchorsMod.MODID)
public final class WorldAnchorsContainers {

  private static final String ENDER_ANCHOR_NAME = "ender_anchor";

  @ObjectHolder(ENDER_ANCHOR_NAME)
  public static ContainerType<EnderWorldAnchorContainer> ENDER_ANCHOR_CONTAINER;

  @SubscribeEvent
  public static void onContainerRegistration(RegistryEvent.Register<ContainerType<?>> event) {
    event
        .getRegistry()
        .register(
            IForgeContainerType.create(
                    (windowId, inv, data) ->
                        new EnderWorldAnchorContainer(windowId, inv, data.readBlockPos()))
                .setRegistryName(ENDER_ANCHOR_NAME));
  }

  private WorldAnchorsContainers() {}
}
