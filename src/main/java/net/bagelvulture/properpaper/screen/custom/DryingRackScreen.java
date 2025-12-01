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
    private static final Identifier WET_TEXTURE =
            Identifier.of(ProperPaper.MOD_ID, "textures/gui/dryingrack/slotbgw.png");
    private static final Identifier DRY_TEXTURE =
            Identifier.of(ProperPaper.MOD_ID, "textures/gui/dryingrack/slotbgd.png");

    public DryingRackScreen(DryingRackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        for (int i = 0; i < 4; i++) {
            int arrowHeight = handler.getScaledSlotProgress(i);
            assert handler.blockEntity.getWorld() != null;
            if (arrowHeight > 0) {
                context.drawTexture(WET_TEXTURE, x + 25 + i * 36, y + 32,
                        0, 0, 18, 19,
                        18, 19);
                context.drawTexture(DRY_TEXTURE, x + 25 + i * 36, y + 32,
                        0, 0, 18, arrowHeight,
                        18, 19);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}