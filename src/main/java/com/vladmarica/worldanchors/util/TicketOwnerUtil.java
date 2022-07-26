package com.vladmarica.worldanchors.util;

import com.vladmarica.worldanchors.WorldAnchorsMod;
import net.minecraftforge.common.world.ForgeChunkManager;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public final class TicketOwnerUtil {
  private static final String FIELD_MODID = "modId";
  private static final String FIELD_OWNER = "owner";

  @Nullable
  public static String getModId(ForgeChunkManager.TicketOwner<?> ticketOwner) {
    return (String) getFieldValue(ticketOwner, FIELD_MODID);
  }

  @Nullable
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<? super T>> T getOwner(ForgeChunkManager.TicketOwner<T> ticketOwner) {
    return (T) getFieldValue(ticketOwner, FIELD_OWNER);
  }

  @Nullable
  private static Object getFieldValue(
      ForgeChunkManager.TicketOwner<?> ticketOwner, String fieldName) {
    try {
      Field modIdField = ticketOwner.getClass().getDeclaredField(fieldName);
      modIdField.setAccessible(true);
      return modIdField.get(ticketOwner);
    } catch (NoSuchFieldException | IllegalAccessException ex) {
      WorldAnchorsMod.LOGGER.debug("Failed to get field {}", fieldName, ex);
      return null;
    }
  }

  private TicketOwnerUtil() {}
}
