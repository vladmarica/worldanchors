package com.vladmarica.worldanchors;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.vladmarica.worldanchors.block.AbstractWorldAnchorBlock;
import com.vladmarica.worldanchors.mixin.TicketOwnerMixin;
import com.vladmarica.worldanchors.tile.AbstractWorldAnchorTileEntity;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ForcedChunksSaveData;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import static com.vladmarica.worldanchors.WorldAnchorsMod.LOGGER;

@Mod.EventBusSubscriber(modid = WorldAnchorsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ChunkLoaderHandler {

  @SubscribeEvent
  public static void onWorldLoad(WorldEvent.Load event) {
    if (event.getWorld().isClientSide()) {
      return;
    }

    ServerWorld world = (ServerWorld) event.getWorld();
    WorldAnchorsSavedData savedData =
        world.getDataStorage().computeIfAbsent(WorldAnchorsSavedData::new, WorldAnchorsMod.MODID);
    LOGGER.info("World {} loaded", world.dimension().location().toString());
  }

  @SubscribeEvent
  public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
    if (event.getEntity() == null || event.getEntity().level.isClientSide) {
      return;
    }
  }

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

  public static class ChunkLoadingValidator implements ForgeChunkManager.LoadingValidationCallback {

    @Override
    public void validateTickets(ServerWorld world, ForgeChunkManager.TicketHelper ticketHelper) {

      for (BlockPos pos : ticketHelper.getBlockTickets().keySet()) {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(pos).append("] owns ");

        Pair<LongSet, LongSet> chunksPair = ticketHelper.getBlockTickets().get(pos);
        for (Long chunkLong : chunksPair.getFirst()) {
          builder.append(new ChunkPos(chunkLong).toString()).append(", ");
        }

        for (Long tickingChunkLong : chunksPair.getSecond()) {
          builder.append(new ChunkPos(tickingChunkLong).toString()).append("(T), ");
        }

        LOGGER.debug(builder.toString());
      }
    }

    private void removeAllTickets(ForgeChunkManager.TicketHelper ticketHelper) {
      ticketHelper.getBlockTickets().keySet().forEach(ticketHelper::removeAllTickets);
      ticketHelper.getEntityTickets().keySet().forEach(ticketHelper::removeAllTickets);
    }
  }


  public static List<ChunkPos> getChunksOwnedByBlockPos(ServerWorld world, BlockPos blockPos) {
    ForcedChunksSaveData forcedChunksSaveData = getForcedChunksSaveData(world);
    if (forcedChunksSaveData == null) {
      return Lists.newArrayList();
    }

    @SuppressWarnings("unchecked")
    ForgeChunkManager.TicketOwner<BlockPos> owner = TicketOwnerMixin.create(WorldAnchorsMod.MODID, blockPos);
    ForgeChunkManager.TicketTracker<BlockPos> tracker = forcedChunksSaveData.getBlockForcedChunks();

    LongSet chunkLongs = tracker.getTickingChunks().get(owner);
    return chunkLongs.stream().map(ChunkPos::new).collect(Collectors.toList());
  }

  /**
   * Returns the {@link ForcedChunksSaveData} for the current world. May return {@code null} if the world
   * has no forced chunks.
   */
  @Nullable
  public static ForcedChunksSaveData getForcedChunksSaveData(ServerWorld world) {
    return world.getDataStorage().get(ForcedChunksSaveData::new, "chunks");
  }

  private ChunkLoaderHandler() {}
}
