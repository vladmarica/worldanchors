package com.vladmarica.worldanchors;

import com.mojang.brigadier.Command;
import com.vladmarica.worldanchors.mixin.TicketOwnerMixin;
import com.vladmarica.worldanchors.util.Util;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.ForcedChunksSaveData;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = WorldAnchorsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class WorldAnchorsCommand {

  @SubscribeEvent
  public static void onRegisterCommands(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("worldanchors")
                .requires(d -> d.hasPermission(2))
                .then(Commands.literal("listchunks").executes(d -> listChunks(d.getSource()))));
  }

  /**
   * Handles the "listchunks" subcommand, which prints out all forced chunks in the current
   * dimension.
   */
  private static int listChunks(CommandSource source) {
    ServerWorld world = source.getLevel();
    ForcedChunksSaveData saveData = world.getDataStorage().get(ForcedChunksSaveData::new, "chunks");
    if (saveData == null) {
      source.sendSuccess(getSummaryString(0, 0, world), false);
      return Command.SINGLE_SUCCESS;
    }

    Map<String, Map<BlockPos, LongSet>> modToOwnedChunksMap = new HashMap<>();
    Function<String, Map<BlockPos, LongSet>> mapProducer = (key) -> new HashMap<>();
    AtomicInteger counter = new AtomicInteger();

    saveData
        .getBlockForcedChunks()
        .getChunks()
        .forEach(
            (owner, longs) -> {
              counter.accumulateAndGet(longs.size(), Integer::sum);
              String modId = ((TicketOwnerMixin) owner).getModId();
              BlockPos blockPos = (BlockPos) ((TicketOwnerMixin) owner).getOwner();
              modToOwnedChunksMap.computeIfAbsent(modId, mapProducer).put(blockPos, longs);
            });
    saveData
        .getBlockForcedChunks()
        .getTickingChunks()
        .forEach(
            (owner, longs) -> {
              counter.accumulateAndGet(longs.size(), Integer::sum);
              String modId = ((TicketOwnerMixin) owner).getModId();
              BlockPos blockPos = (BlockPos) ((TicketOwnerMixin) owner).getOwner();
              modToOwnedChunksMap.computeIfAbsent(modId, mapProducer).put(blockPos, longs);
            });

    IFormattableTextComponent resultTextComponent =
        getSummaryString(counter.get(), modToOwnedChunksMap.size(), world).append("\n");

    for (String modid : modToOwnedChunksMap.keySet()) {
      // Print out the name of each mode that has forced chunks
      resultTextComponent.append(
          new StringTextComponent("Mod " + modid + ":\n").withStyle(TextFormatting.AQUA));

      for (BlockPos blockPos : modToOwnedChunksMap.get(modid).keySet()) {
        // For each block chunk owner, print out the name of the block
        // It should be safe to call world.getBlockState() here since the chunk is forced loaded
        resultTextComponent.append(
            new StringTextComponent(
                    "  "
                        + world.getBlockState(blockPos).getBlock().getRegistryName().getPath()
                        + " "
                        + Util.blockPosToString(blockPos)
                        + ":\n")
                .withStyle(TextFormatting.WHITE));

        for (Long chunkLong : modToOwnedChunksMap.get(modid).get(blockPos)) {
          // For each block chunk owner, print out the list of chunks that this block keeps force
          // loaded
          ChunkPos chunkPos = new ChunkPos(chunkLong);
          resultTextComponent.append(
              new StringTextComponent("    " + chunkPos + "\n").withStyle(TextFormatting.GRAY));
        }
      }
    }

    source.sendSuccess(resultTextComponent, false);
    return Command.SINGLE_SUCCESS;
  }

  private static IFormattableTextComponent getSummaryString(
      int numChunks, int numMods, ServerWorld world) {
    return new StringTextComponent(
            String.format(
                "%d forced chunks by %d mods in dimension %s",
                numChunks, numMods, world.dimension().location()))
        .withStyle(TextFormatting.GREEN);
  }

  private WorldAnchorsCommand() {}
}
