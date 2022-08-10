package com.vladmarica.worldanchors.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class BaseContainer extends Container {
  protected BlockPos pos;

  public BaseContainer(@Nullable ContainerType<?> containerType, int windowId, BlockPos pos) {
    super(containerType, windowId);
    this.pos = pos;
  }

  protected void addPlayerInventorySlots(PlayerInventory inventory) {
    // Add player inventory slots
    for(int i = 0; i < 3; ++i) {
      for(int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }

    // Add player hotbar slots
    for(int k = 0; k < 9; ++k) {
      this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
    }
  }
}
