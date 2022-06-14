package com.vladmarica.worldanchors.block;

import com.vladmarica.worldanchors.tile.EnderWorldAnchorTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EnderWorldAnchorBlock extends AbstractWorldAnchorBlock {

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new EnderWorldAnchorTileEntity();
  }
}
