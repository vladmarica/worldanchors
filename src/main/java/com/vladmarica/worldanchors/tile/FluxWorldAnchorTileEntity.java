package com.vladmarica.worldanchors.tile;

import com.google.common.collect.ImmutableList;
import com.vladmarica.worldanchors.WorldAnchorsMod;
import com.vladmarica.worldanchors.WorldAnchorsTileEntities;
import com.vladmarica.worldanchors.block.FluxWorldAnchorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FluxWorldAnchorTileEntity extends AbstractWorldAnchorTileEntity {

  private static final String KEY_STORED_ENERGY = "stored_energy";
  private static final int RF_PER_TICK = 30;

  private final LazyOptional<FluxEnergyStorage> fluxEnergyStorageOptional =
      LazyOptional.of(() -> new FluxEnergyStorage(20000));

  private boolean isActive = false;

  public FluxWorldAnchorTileEntity() {
    super(WorldAnchorsTileEntities.FLUX_ANCHOR);
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return fluxEnergyStorageOptional.cast();
    }

    return super.getCapability(cap);
  }

  @Override
  protected void invalidateCaps() {
    super.invalidateCaps();
    fluxEnergyStorageOptional.invalidate();
  }

  @Override
  public void tick() {
    super.tick();

    if (level.isClientSide) {
      return;
    }

    fluxEnergyStorageOptional.ifPresent(
        storage -> {
          if (storage.consume(RF_PER_TICK)) {
            setChanged();
            if (!isActive) {
              isActive = true;
              level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(FluxWorldAnchorBlock.PROPERTY_ACTIVE, true));
            }
          } else {
            if (isActive) {
              isActive = false;

              level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(FluxWorldAnchorBlock.PROPERTY_ACTIVE, false));
            }
          }
          WorldAnchorsMod.LOGGER.debug("Current storage: {}", storage.getEnergyStored());
        });
  }

  @Nonnull
  @Override
  public CompoundNBT save(CompoundNBT nbt) {
    fluxEnergyStorageOptional.ifPresent(storage -> nbt.putInt(KEY_STORED_ENERGY, storage.getEnergyStored()));
    return super.save(nbt);
  }

  @Override
  public void load(BlockState state, CompoundNBT nbt) {
    super.load(state, nbt);

    if (nbt.contains(KEY_STORED_ENERGY)) {
      fluxEnergyStorageOptional.ifPresent(storage -> storage.currentEnergyStored = nbt.getInt(KEY_STORED_ENERGY));
    }
  }

  @Override
  protected List<ChunkPos> getChunksToLoad() {
    return ImmutableList.of(new ChunkPos(worldPosition));
  }
}
