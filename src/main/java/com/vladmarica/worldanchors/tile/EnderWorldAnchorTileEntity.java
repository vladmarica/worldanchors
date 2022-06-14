package com.vladmarica.worldanchors.tile;

import com.vladmarica.worldanchors.EnderItemStackHandler;
import com.vladmarica.worldanchors.WorldAnchorsBlocks;
import com.vladmarica.worldanchors.WorldAnchorsTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class EnderWorldAnchorTileEntity extends AbstractWorldAnchorTileEntity {
  private static final String KEY_INVENTORY = "inv";

  private final LazyOptional<EnderItemStackHandler> itemStackHandler =
      LazyOptional.of(EnderItemStackHandler::new);

  public EnderWorldAnchorTileEntity() {
    super(WorldAnchorsTileEntities.ENDER_ANCHOR);
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return itemStackHandler.cast();
    }

    return super.getCapability(cap);
  }

  @Nonnull
  @Override
  public CompoundNBT save(CompoundNBT nbt) {
    itemStackHandler.ifPresent((handler) -> nbt.put(KEY_INVENTORY, handler.serializeNBT()));
    return super.save(nbt);
  }

  @Override
  public void load(BlockState state, CompoundNBT nbt) {
    itemStackHandler.ifPresent((handler) -> handler.deserializeNBT(nbt.getCompound(KEY_INVENTORY)));
    super.load(state, nbt);
  }
}
