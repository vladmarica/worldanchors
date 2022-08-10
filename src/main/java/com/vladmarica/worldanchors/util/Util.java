package com.vladmarica.worldanchors.util;

import net.minecraft.util.math.BlockPos;

public final class Util {

  public static String blockPosToString(BlockPos blockPos) {
    return new StringBuilder("(")
        .append(blockPos.getX())
        .append(",")
        .append(blockPos.getY())
        .append(",")
        .append(blockPos.getZ())
        .append(")")
        .toString();
  }

  private Util() {}
}
