package com.vladmarica.worldanchors.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

import static com.vladmarica.worldanchors.WorldAnchorsMod.LOGGER;

public abstract class AbstractWorldAnchorTileEntity extends TileEntity {
  private static final String KEY_OWNER = "owner";

  @Nullable private UUID owner;

  public AbstractWorldAnchorTileEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (!level.isClientSide) {
      LOGGER.debug("onLoad() for {}", getBlockPos().toString());
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

  public void setOwner(GameProfile profile) {
    owner = profile.getId();
    setChanged();
    LOGGER.debug("Setting owner for [{}] to {}", getBlockPos().toString(), owner);
  }

  @Nullable
  public UUID getOwner() {
    return owner;
  }
}
