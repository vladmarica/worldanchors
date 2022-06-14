package com.vladmarica.worldanchors;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class EnderItemStackHandler extends ItemStackHandler {

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    if (stack.getItem() != Items.ENDER_PEARL) {
      return false;
    }

    return super.isItemValid(slot, stack);
  }
}
