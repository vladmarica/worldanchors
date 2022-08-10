package com.vladmarica.worldanchors.tile;

import com.mojang.authlib.GameProfile;
import com.vladmarica.worldanchors.ChunkLoaderHandler;
import com.vladmarica.worldanchors.WorldAnchorsMod;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.world.ForgeChunkManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static com.vladmarica.worldanchors.WorldAnchorsMod.LOGGER;

public abstract class AbstractWorldAnchorTileEntity extends TileEntity
    implements ITickableTileEntity {
  private static final String KEY_OWNER = "owner";

  @Nullable private UUID owner;
  @Nullable Runnable nextTickTask;
  private int ticks = 0;

  public AbstractWorldAnchorTileEntity(TileEntityType<?> type) {
    super(type);
  }

  /**
   * Returns a {@link List} of chunk positions that this anchor should force-load. This method
   * should return the same value regardless of whether the anchor has the resources to be active.
   */
  protected abstract List<ChunkPos> getChunksToLoad();

  @Override
  public void onLoad() {
    super.onLoad();
    if (!level.isClientSide) {
      LOGGER.debug("onLoad() for block {}", getBlockPos().toString());

      // Force loading chunks during onLoad() can cause the game to deadlock during world loading,
      // so schedule the force loading to happen next tick
      nextTickTask =
          () -> {
            List<ChunkPos> chunksToLoad = getChunksToLoad();
            for (ChunkPos chunkPos : chunksToLoad) {
              boolean success = forceChunkInternal(chunkPos, /* add= */ true);
              LOGGER.debug(
                  "Force loading chunk {} for block {} (success = {})",
                  chunkPos.toString(),
                  getBlockPos().toString(),
                  success);
            }
          };

      LOGGER.debug("Schedule task to run next tick");
    }
  }

  @Override
  public void onChunkUnloaded() {
    super.onChunkUnloaded();
    nextTickTask = null;
    LOGGER.debug("Chunk unloaded");
  }

  @Override
  public void tick() {
    if (level.isClientSide) {
      return;
    }

    if (nextTickTask != null) {
      nextTickTask.run();
      nextTickTask = null;
    }

    ticks = (ticks + 1) % 20;
    if (ticks == 0) {
      LOGGER.debug("tick() ({})", worldPosition.toString());
    }
  }

  @Override
  @Nonnull
  public CompoundNBT save(CompoundNBT nbt) {
    if (owner != null) {
      nbt.putUUID(KEY_OWNER, owner);
    }

    return super.save(nbt);
  }

  @Override
  public void load(BlockState state, CompoundNBT nbt) {
    super.load(state, nbt);

    if (nbt.hasUUID(KEY_OWNER)) {
      owner = nbt.getUUID(KEY_OWNER);
    }
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return save(new CompoundNBT());
  }

  @Nullable
  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(worldPosition, -1, getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
    load(getBlockState(), packet.getTag());
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    nextTickTask = null;

    if (!level.isClientSide) {
      LOGGER.debug("setRemoved() for block {}", getBlockPos().toString());

      List<ChunkPos> ownedChunks =
          ChunkLoaderHandler.getChunksOwnedByBlockPos((ServerWorld) level, worldPosition);
      for (ChunkPos chunkPos : ownedChunks) {
        boolean success = forceChunkInternal(chunkPos, /* add= */ false);
        LOGGER.debug(
            "Force unloading chunk {} for block {} (success = {})",
            chunkPos.toString(),
            getBlockPos().toString(),
            success);
      }
    }
  }

  public void setOwner(GameProfile profile) {
    owner = profile.getId();
    setChanged();
    LOGGER.debug("Setting owner for [{}] to {}", getBlockPos().toString(), owner);
  }

  @Nullable
  public UUID getOwner() {
    return owner;
  }

  protected void sendBlockUpdate() {
    if (!level.isClientSide) {
      BlockState currentBlockState = level.getBlockState(worldPosition);
      level.sendBlockUpdated(
          worldPosition, currentBlockState, currentBlockState, Constants.BlockFlags.BLOCK_UPDATE);
    } else {
      LOGGER.warn("sendBlockUpdate() called on client");
    }
  }

  private boolean forceChunkInternal(ChunkPos chunkPos, boolean add) {
    if (!level.isClientSide) {
      ServerWorld world = (ServerWorld) level;
      return ForgeChunkManager.forceChunk(
          world,
          WorldAnchorsMod.MODID,
          /* owner= */ worldPosition,
          chunkPos.x,
          chunkPos.z,
          add,
          true);
    }

    return false;
  }
}
