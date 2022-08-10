package com.vladmarica.worldanchors.block;

import com.vladmarica.worldanchors.tile.EnderWorldAnchorTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class EnderWorldAnchorBlock extends AbstractWorldAnchorBlock {

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new EnderWorldAnchorTileEntity();
  }


  @Override
  public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (world.isClientSide) {
      return ActionResultType.SUCCESS;
    }

    NetworkHooks.openGui((ServerPlayerEntity) player, (EnderWorldAnchorTileEntity) world.getBlockEntity(pos), pos);
    return ActionResultType.CONSUME;
  }
}
