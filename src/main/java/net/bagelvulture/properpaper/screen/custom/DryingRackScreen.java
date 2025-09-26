package net.bagelvulture.properpaper.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bagelvulture.properpaper.ProperPaper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DryingRackScreen extends HandledScreen<DryingRackScreenHandler> {
    private static final Identifier GUI_TEXTURE =
            Identifier.of(ProperPaper.MOD_ID, "textures/gui/dryingrack/dryingrack_gui.png");
    private static final Identifier ARROW_TEXTURE =
            Identifier.of(ProperPaper.MOD_ID, "textures/gui/arrow_progress.png");

    public DryingRackScreen(DryingRackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressArrows(context, x, y);
    }

    private void renderProgressArrows(DrawContext context, int x, int y) {
        int baseSlotX = x + 44;
        int slotY = y + 34;
        int arrowYOffset = 24;

        for (int i = 0; i < 4; i++) {
            int arrowWidth = handler.getScaledSlotProgress(i);
            if (arrowWidth > 0) {
                context.drawTexture(ARROW_TEXTURE, baseSlotX + i * 24, slotY + arrowYOffset,
                        0, 0, arrowWidth, 16,
                        24, 16);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}