package net.bagelvulture.properpaper.screen.custom;

import net.bagelvulture.properpaper.ProperPaper;
import net.bagelvulture.properpaper.block.ModBlocks;
import net.bagelvulture.properpaper.util.ModDataComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.Identifier;
import net.minecraft.text.Text;

import java.util.Objects;

public class GuideBookScreen extends Screen {
    private final int totalPages = 17;
    private int currentPage = 0;

    private PageTurnWidget nextPageButton;
    private PageTurnWidget previousPageButton;

    private final ItemStack book;

    public GuideBookScreen(ItemStack book) {
        super(Text.empty());
        this.book = book;
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).dimensions(this.width / 2 - 100, 196, 200, 20).build());

        this.nextPageButton = this.addDrawableChild(new PageTurnWidget(((this.width - 192) / 2) + 116, 159, true, button -> this.goToNextPage(), true));
        this.previousPageButton = this.addDrawableChild(new PageTurnWidget(((this.width - 192) / 2) + 43, 159, false, button -> this.goToPreviousPage(), true));

        if (book != null && book.contains(ModDataComponentTypes.CURRENT_PAGE_COMP)) {
            this.currentPage = book.get(ModDataComponentTypes.CURRENT_PAGE_COMP);
        }

        this.updatePageButtons();
    }

    private void goToNextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
        }
        this.updatePageButtons();
    }

    private void goToPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
        }
        this.updatePageButtons();
    }

    private void updatePageButtons() {
        nextPageButton.visible = currentPage < totalPages - 1;
        previousPageButton.visible = currentPage > 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInGameBackground(context);
        context.drawTexture(
                Identifier.of(ProperPaper.MOD_ID, "textures/gui/guidebook/page_" + currentPage + ".png"),
                (this.width - 192) / 2, 2, 0, 0, 192, 192
        );
        switch (currentPage) {
            case 4 -> {
                renderScaledCrafter(context, ((this.width - 192) / 2) + 33, 53, 0.75f,
                        new ItemStack(Items.STONE),        ItemStack.EMPTY,                   new ItemStack(Items.STONE),
                        new ItemStack(Items.SMOOTH_STONE), new ItemStack(Items.SMOOTH_STONE), new ItemStack(Items.SMOOTH_STONE),
                        new ItemStack(Items.SMOOTH_STONE), new ItemStack(Items.SMOOTH_STONE), new ItemStack(Items.SMOOTH_STONE),
                        new ItemStack(ModBlocks.MACERATOR),
                        mouseX, mouseY);
            }
            case 10 -> {
                renderScaledCrafter(context, ((this.width - 192) / 2) + 33, 53, 0.75f,
                        new ItemStack(Items.OAK_PLANKS), new ItemStack(Items.STRING), new ItemStack(Items.OAK_PLANKS),
                        new ItemStack(Items.OAK_PLANKS), ItemStack.EMPTY,             new ItemStack(Items.OAK_PLANKS),
                        new ItemStack(Items.OAK_PLANKS), ItemStack.EMPTY,             new ItemStack(Items.OAK_PLANKS),
                        new ItemStack(ModBlocks.SIEVE),
                        mouseX, mouseY);
            }
            case 13 -> {
                renderScaledCrafter(context, ((this.width - 192) / 2) + 33, 53, 0.75f,
                        new ItemStack(Items.OAK_PLANKS), new ItemStack(Items.OAK_PLANKS), new ItemStack(Items.OAK_PLANKS),
                        new ItemStack(Items.OAK_PLANKS), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.OAK_PLANKS),
                        new ItemStack(Items.OAK_PLANKS), ItemStack.EMPTY,                 new ItemStack(Items.OAK_PLANKS),
                        new ItemStack(ModBlocks.DRYING_RACK),
                        mouseX, mouseY);
            }
            case 15 -> {
                renderScaledCrafter(context, ((this.width - 192) / 2) + 33, 53, 0.75f,
                        ItemStack.EMPTY,                   new ItemStack(Items.STONE),        ItemStack.EMPTY,
                        ItemStack.EMPTY,                   new ItemStack(Items.STONE),        ItemStack.EMPTY,
                        new ItemStack(Items.SMOOTH_STONE), new ItemStack(Items.SMOOTH_STONE), new ItemStack(Items.SMOOTH_STONE),
                        new ItemStack(ModBlocks.HOT_ROLLER),
                        mouseX, mouseY);
            }
            default -> {}
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        super.close();
        if (book != null && !book.isEmpty()) {
            book.set(ModDataComponentTypes.CURRENT_PAGE_COMP, currentPage);
        }
    }

    //below is all the helper methods for rendering custom stuff

    public void renderScaledCrafter(DrawContext context, int x, int y, float scale, ItemStack a, ItemStack b,
                    ItemStack c, ItemStack d, ItemStack e, ItemStack f, ItemStack g, ItemStack h, ItemStack i,
                    ItemStack j, int mouseX, int mouseY) {
        int anchorX = x + 17;
        int anchorY = y + 17;

        context.getMatrices().push();
        context.getMatrices().translate(anchorX, anchorY, 0);
        context.getMatrices().scale(scale, scale, 1);
        context.getMatrices().translate(-anchorX, -anchorY, 0);

        context.drawTexture(
            Identifier.of(ProperPaper.MOD_ID, "textures/gui/guidebook/crafting_table.png"),
            x, y, 0, 0, 148, 86 , 148 , 86
        );

        renderScaledItem(context, a, x + 17, y + 17);
        renderScaledItem(context, b, x + 35, y + 17);
        renderScaledItem(context, c, x + 53, y + 17);
        renderScaledItem(context, d, x + 17, y + 35);
        renderScaledItem(context, e, x + 35, y + 35);
        renderScaledItem(context, f, x + 53, y + 35);
        renderScaledItem(context, g, x + 17, y + 53);
        renderScaledItem(context, h, x + 35, y + 53);
        renderScaledItem(context, i, x + 53, y + 53);
        renderScaledItem(context, j, x + 111, y + 35);

        context.getMatrices().pop();

        renderToolTipWhenHovering(context, x + 17, y + 17, 16, 16, scale, mouseX, mouseY, a, anchorX, anchorY);
        renderToolTipWhenHovering(context, x + 35, y + 17, 16, 16, scale, mouseX, mouseY, b, anchorX, anchorY);
        renderToolTipWhenHovering(context, x + 53, y + 17, 16, 16, scale, mouseX, mouseY, c, anchorX, anchorY);
        renderToolTipWhenHovering(context, x + 17, y + 35, 16, 16, scale, mouseX, mouseY, d, anchorX, anchorY);
        renderToolTipWhenHovering(context, x + 35, y + 35, 16, 16, scale, mouseX, mouseY, e, anchorX, anchorY);
        renderToolTipWhenHovering(context, x + 53, y + 35, 16, 16, scale, mouseX, mouseY, f, anchorX, anchorY);
        renderToolTipWhenHovering(context, x + 17, y + 53, 16, 16, scale, mouseX, mouseY, g, anchorX, anchorY);
        renderToolTipWhenHovering(context, x + 35, y + 53, 16, 16, scale, mouseX, mouseY, h, anchorX, anchorY);
        renderToolTipWhenHovering(context, x + 53, y + 53, 16, 16, scale, mouseX, mouseY, i, anchorX, anchorY);
        renderToolTipWhenHovering(context, x + 109, y + 33, 20, 20, scale, mouseX, mouseY, j, anchorX, anchorY);
    }

    public void renderScaledItem(DrawContext context, ItemStack item, int x, int y) {
        var matrices = context.getMatrices();
        matrices.push();

        matrices.translate(x, y, 0.0f);

        context.drawItem(item, 0, 0);

        matrices.pop();
    }

    public void renderToolTipWhenHovering(DrawContext context, int x, int y, int width, int height, float scale, int mouseX, int mouseY, ItemStack a, int anchorX, int anchorY) {
        if (isPointWithinBounds(x, y, width, height, scale, mouseX, mouseY, anchorX, anchorY)) {
            if(!a.isEmpty()) {
                context.drawItemTooltip(textRenderer, a, mouseX, mouseY);
            }
        }
    }

    private boolean isPointWithinBounds(int x, int y, int width, int height, float scale, int pointX, int pointY, int anchorX, int anchorY) {
        float newx = anchorX + (x - anchorX) * scale;
        float newy = anchorY + (y - anchorY) * scale;
        float newh = height * scale;
        float neww = width * scale;

        return pointX >= newx && pointX < newx + neww && pointY >= newy && pointY < newy + newh;
    }
}