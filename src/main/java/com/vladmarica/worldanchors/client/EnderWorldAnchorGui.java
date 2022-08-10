package com.vladmarica.worldanchors.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vladmarica.worldanchors.WorldAnchorsMod;
import com.vladmarica.worldanchors.tile.EnderWorldAnchorContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnderWorldAnchorGui extends ContainerScreen<EnderWorldAnchorContainer> {
  private static final ResourceLocation BG_TEXTURE =
      new ResourceLocation(WorldAnchorsMod.MODID + ":textures/gui/ender_gui.png");

  private static int BURN_PROGRESS_OFFSET_X = 80;
  private static int BURN_PROGRESS_OFFSET_Y = 16;
  private static int BURN_ICON_SIZE = 14;

  public EnderWorldAnchorGui(
      EnderWorldAnchorContainer container, PlayerInventory inventory, ITextComponent text) {
    super(container, inventory, text);
  }

  @Override
  public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTick) {
    renderBackground(matrix);
    super.render(matrix, mouseX, mouseY, partialTick);
    renderTooltip(matrix, mouseX, mouseY);
  }

  @Override
  protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
    // Draw the background of the gui
    minecraft.getTextureManager().bind(BG_TEXTURE);
    int left = (this.width - this.imageWidth) / 2;
    int top = (this.height - this.imageHeight) / 2;
    blit(matrix, left, top, 0, 0, imageWidth, imageHeight);

    long burnTime = menu.getCurrentBurnTime();

    int burnProgress = getBurnProgress();
    WorldAnchorsMod.LOGGER.debug(
        "[EnderWorldAnchorGui] burnTime = {}, progress = {}", burnTime, burnProgress);

    if (burnProgress > 0) {
      blit(
          matrix,
          left + BURN_PROGRESS_OFFSET_X,
          top + BURN_PROGRESS_OFFSET_Y + BURN_ICON_SIZE - burnProgress,
          176,
          BURN_ICON_SIZE - burnProgress,
          BURN_ICON_SIZE,
          burnProgress);
    }
  }

  private int getBurnProgress() {
    long currentBurnTime = menu.getCurrentBurnTime();
    if (currentBurnTime == 0) {
      return 0;
    }

    long totalBurnTime = menu.getTotalBurnTime();
    return Math.round(currentBurnTime / (float) totalBurnTime * BURN_ICON_SIZE);
  }
}
