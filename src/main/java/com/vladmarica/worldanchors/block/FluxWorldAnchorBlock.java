package com.vladmarica.worldanchors.block;

import com.vladmarica.worldanchors.tile.EnderWorldAnchorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class FluxWorldAnchorBlock extends AbstractWorldAnchorBlock {
  private static final Property<Boolean> PROPERTY_ACTIVE = BooleanProperty.create("active");

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(PROPERTY_ACTIVE);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    return super.getStateForPlacement(context).setValue(PROPERTY_ACTIVE, true);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new EnderWorldAnchorTileEntity();
  }
}
