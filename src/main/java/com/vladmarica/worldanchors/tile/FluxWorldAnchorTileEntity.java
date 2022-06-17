package com.vladmarica.worldanchors.tile;

import com.vladmarica.worldanchors.EnderItemStackHandler;
import com.vladmarica.worldanchors.WorldAnchorsTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class FluxWorldAnchorTileEntity extends AbstractWorldAnchorTileEntity {

  public FluxWorldAnchorTileEntity() {
    super(WorldAnchorsTileEntities.FLUX_ANCHOR);
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
    return super.getCapability(cap);
  }
}
