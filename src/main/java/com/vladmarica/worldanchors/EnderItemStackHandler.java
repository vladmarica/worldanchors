package com.vladmarica.worldanchors;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class EnderItemStackHandler extends ItemStackHandler {
  private Listener listener;

  public EnderItemStackHandler(Listener listener) {
    super(/* size= */ 1);
    this.listener = listener;
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    if (stack.getItem() != Items.ENDER_PEARL) {
      return false;
    }

    return super.isItemValid(slot, stack);
  }

  @Override
  protected void onContentsChanged(int slot) {
    listener.onInventoryChange(slot);
  }

  public interface Listener {
    void onInventoryChange(int slot);
  }
}
