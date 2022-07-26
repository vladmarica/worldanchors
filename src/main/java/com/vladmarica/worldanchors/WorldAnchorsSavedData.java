package com.vladmarica.worldanchors;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;

public class WorldAnchorsSavedData extends WorldSavedData {
  private static final String KEY_ANCHORS_LIST = "anchors";

  public WorldAnchorsSavedData() {
    super(WorldAnchorsMod.MODID);
  }

  @Override
  public void load(CompoundNBT nbt) {
    WorldAnchorsMod.LOGGER.info("ChunkLoaderSavedData loaded");
  }

  @Override
  public CompoundNBT save(CompoundNBT nbt) {
    WorldAnchorsMod.LOGGER.info("ChunkLoaderSavedData saved");

    ListNBT anchorsList = new ListNBT();
    nbt.put(KEY_ANCHORS_LIST, anchorsList);

    return nbt;
  }
}
