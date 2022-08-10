package com.vladmarica.worldanchors.tile;

import com.vladmarica.worldanchors.WorldAnchorsContainers;
import com.vladmarica.worldanchors.WorldAnchorsMod;
import com.vladmarica.worldanchors.container.BaseContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class EnderWorldAnchorContainer extends BaseContainer {
  private static final int FUEL_SLOT_X = 80;
  private static final int FUEL_SLOT_Y = 33;

  private World world;

  public EnderWorldAnchorContainer(int windowId, PlayerInventory inventory, BlockPos blockPos) {
    super(WorldAnchorsContainers.ENDER_ANCHOR_CONTAINER, windowId, blockPos);

    world = inventory.player.level;

    EnderWorldAnchorTileEntity tile =
        (EnderWorldAnchorTileEntity) inventory.player.level.getBlockEntity(blockPos);
    LazyOptional<IItemHandler> itemHandlerOptional =
        tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    if (!itemHandlerOptional.isPresent()) {
      throw new RuntimeException("Item handler is not present!");
    }

    itemHandlerOptional.ifPresent(
        (itemHandler) -> {
          addSlot(new SlotItemHandler(itemHandler, 0, FUEL_SLOT_X, FUEL_SLOT_Y));
        });

    addPlayerInventorySlots(inventory);
  }

  @Override
  public ItemStack quickMoveStack(PlayerEntity player, int slotId) {
    // TODO(vladmarica): actually implement the quick move logic\
    WorldAnchorsMod.LOGGER.debug("quickMoveStack({})", slotId);

    ItemStack result = ItemStack.EMPTY;
    Slot slot = slots.get(slotId);
    if (slot == null || !slot.hasItem()) {
      return ItemStack.EMPTY;
    }

    ItemStack itemstack = slot.getItem();
    result = itemstack.copy();

    // Slot 0 is the fuel slot - do not allow quick moving it
    if (slotId != 0) {
      // Attempt to move the itemstack from the current slot to the fuel slot
      if (!moveItemStackTo(
          itemstack, /* startSlotId= */ 0, /* endSlotId= */ 1, /* reverse= */ false)) {
        return ItemStack.EMPTY;
      }
    }

    // Check if the itemstack was fully moved and call the appropriate methods on the slot
    if (itemstack.isEmpty()) {
      slot.set(ItemStack.EMPTY);
    } else {
      slot.setChanged();
    }

    if (itemstack.getCount() == result.getCount()) {
      return ItemStack.EMPTY;
    }

    // Trigger "on take" effects, such as gaining XP when taking out of a furnace
    // Does nothing for world anchor containers, of course
    slot.onTake(player, itemstack);

    return result;
  }

  @Override
  public boolean stillValid(PlayerEntity player) {
    return player.level.getBlockEntity(pos) instanceof EnderWorldAnchorTileEntity;
  }


  public long getCurrentBurnTime() {
    @Nullable TileEntity tile = world.getBlockEntity(pos);
    if (tile != null) {
      return ((EnderWorldAnchorTileEntity) tile).getCurrentBurnTime();
    }

    return 0;
  }

  public long getTotalBurnTime() {
    @Nullable TileEntity tile = world.getBlockEntity(pos);
    if (tile != null) {
      return ((EnderWorldAnchorTileEntity) tile).getTotalBurnTime();
    }

    return 0;
  }
}
