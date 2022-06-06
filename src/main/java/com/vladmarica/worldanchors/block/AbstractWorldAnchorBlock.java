package com.vladmarica.worldanchors.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public abstract class AbstractWorldAnchorBlock extends Block {
  public AbstractWorldAnchorBlock() {
    super(
        AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
            .strength(1.5F, 6.0F));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return super.hasTileEntity(state);
  }
}
