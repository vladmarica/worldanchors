package com.vladmarica.worldanchors.mixin;

import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraft.util.math.BlockPos;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Mixin for {@link ForgeChunkManager.TicketOwner} that exposes private fields and the constructor.
 *
 * <p>{@link ForgeChunkManager.TicketOwner} is a generic class, but its generic type is bounded to
 * {@link Comparable}, meaning that this mixin can use that bound to replace the generic type.
 */
@SuppressWarnings("rawtypes")
@Mixin(ForgeChunkManager.TicketOwner.class)
public interface TicketOwnerMixin {
  /** Returns the ID of the mod that this ticket belongs to */
  @Accessor
  String getModId();

  /** Returns the owner of this ticket, either a {@link BlockPos} or {@link UUID}. */
  @Accessor
  Comparable getOwner();

  /**
   * Invoker for the {@link ForgeChunkManager.TicketOwner} constructor, which is private.
   *
   * @param modId The ID of the mod that this ticket belongs to
   * @param owner The owner of this ticket, must be either a {@link BlockPos} or {@link UUID}.
   * @return A new instance of {@link ForgeChunkManager.TicketOwner} with the given mod ID and
   *     owner.
   */
  @Invoker("<init>")
  static ForgeChunkManager.TicketOwner create(String modId, Comparable owner) {
    throw new AssertionError("TicketOwner constructor invoker called directly");
  }
}
