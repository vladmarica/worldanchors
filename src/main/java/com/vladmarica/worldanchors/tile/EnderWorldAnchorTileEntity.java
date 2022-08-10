package com.vladmarica.worldanchors.tile;

import com.google.common.collect.ImmutableList;
import com.vladmarica.worldanchors.EnderItemStackHandler;
import com.vladmarica.worldanchors.WorldAnchorsMod;
import com.vladmarica.worldanchors.WorldAnchorsTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class EnderWorldAnchorTileEntity extends AbstractWorldAnchorTileEntity implements INamedContainerProvider, EnderItemStackHandler.Listener {
  private static final String KEY_INVENTORY = "Inv";
  private static final String KEY_CURRENT_BURN_TIME = "BurnTime";
  private static final String KEY_TOTAL_BURN_TIME = "TotalBurnTime";
  private static final int FUEL_SLOT_ID = 0;

  /** The number of ticks remaining that the fuel will burn for */
  private long currentBurnTime = 0;

  /** The total number of ticks that the fuel will burn for */
  private long totalBurnTime = 0;

  /** The inventory handler for this tile entity, has a single slot for fuel */
  private final EnderItemStackHandler inventoryHandler;

  public EnderWorldAnchorTileEntity() {
    super(WorldAnchorsTileEntities.ENDER_ANCHOR);
    inventoryHandler = new EnderItemStackHandler(this);
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> inventoryHandler).cast();
    }

    return super.getCapability(cap);
  }

  @Nonnull
  @Override
  public CompoundNBT save(CompoundNBT nbt) {
    nbt.put(KEY_INVENTORY, inventoryHandler.serializeNBT());
    nbt.putLong(KEY_CURRENT_BURN_TIME, currentBurnTime);
    nbt.putLong(KEY_TOTAL_BURN_TIME, totalBurnTime);
    return super.save(nbt);
  }

  @Override
  public void load(BlockState state, CompoundNBT nbt) {
    inventoryHandler.deserializeNBT(nbt.getCompound(KEY_INVENTORY));
    currentBurnTime = nbt.getLong(KEY_CURRENT_BURN_TIME);
    totalBurnTime = nbt.getLong(KEY_TOTAL_BURN_TIME);
    super.load(state, nbt);
  }

  @Override
  protected List<ChunkPos> getChunksToLoad() {
    ChunkPos rootChunkPos = new ChunkPos(worldPosition);
    return ImmutableList.of(
        rootChunkPos,
        new ChunkPos(rootChunkPos.x - 1, rootChunkPos.z),
        new ChunkPos(rootChunkPos.x + 1, rootChunkPos.z));
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent("Ender World Anchor");
  }

  @Nullable
  @Override
  public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
    return new EnderWorldAnchorContainer(windowId, inv, getBlockPos());
  }

  @Override
  public void onInventoryChange(int slot) {
    WorldAnchorsMod.LOGGER.debug("onInventoryChange({})", slot);
    setChanged();
  }

  @Override
  public void tick() {
    super.tick();

    if (level.isClientSide) {
      return;
    }

    if (currentBurnTime == 0) {
      ItemStack fuelStack = inventoryHandler.getStackInSlot(FUEL_SLOT_ID);
      if (!fuelStack.isEmpty()) {
        totalBurnTime = getBurnTimeForFuel(fuelStack.getItem());
        currentBurnTime = totalBurnTime;
        inventoryHandler.extractItem(FUEL_SLOT_ID, /* amount= */ 1, /* simulate= */ false);
        sendBlockUpdate();
      }
    } else if (currentBurnTime > 0) {
      currentBurnTime--;
      if (currentBurnTime == 0) {
        totalBurnTime = 0;
      }

      sendBlockUpdate(); // TODO(vladmarica): only send this packet per second instead of per tick
    }
  }

  private long getBurnTimeForFuel(Item item) {
    // TODO(vladmarica): figure out actual burn time logic (e.g. reading from config)
    if (item == Items.ENDER_PEARL) {
      return 80;
    }

    return 0;
  }

  public long getCurrentBurnTime() {
    return currentBurnTime;
  }

  public long getTotalBurnTime() {
    return totalBurnTime;
  }
}
