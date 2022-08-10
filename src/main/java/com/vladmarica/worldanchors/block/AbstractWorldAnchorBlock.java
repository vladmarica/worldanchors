package com.vladmarica.worldanchors.block;

import com.vladmarica.worldanchors.tile.AbstractWorldAnchorTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class AbstractWorldAnchorBlock extends Block {

  public AbstractWorldAnchorBlock() {
    super(
        AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
            .strength(1.5F, 6.0F));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public void setPlacedBy(
      World world,
      BlockPos pos,
      BlockState state,
      @Nullable LivingEntity entity,
      ItemStack itemStack) {
    super.setPlacedBy(world, pos, state, entity, itemStack);

    if (world.isClientSide) {
      return;
    }

    if (entity instanceof PlayerEntity) {
      AbstractWorldAnchorTileEntity tileEntity =
          (AbstractWorldAnchorTileEntity) world.getBlockEntity(pos);
      tileEntity.setOwner(((PlayerEntity) entity).getGameProfile());
    } else {
      LOGGER.warn("World anchor placed by non-player at {}", pos);
    }
  }

  @Nullable
  @Override
  public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
}
