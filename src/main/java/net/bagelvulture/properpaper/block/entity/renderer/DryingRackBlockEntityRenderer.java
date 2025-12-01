package net.bagelvulture.properpaper.block.entity.renderer;

import net.bagelvulture.properpaper.block.custom.DryingRackBlock;
import net.bagelvulture.properpaper.block.entity.custom.DryingRackBlockEntity;
import net.bagelvulture.properpaper.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {
    public DryingRackBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(DryingRackBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager();
        BlockModelRenderer modelRenderer = MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer();

        BakedModel wetModel = modelManager.getModel(Identifier.of("proper-paper", "block/wet_paper"));
        BakedModel dryModel = modelManager.getModel(Identifier.of("proper-paper", "block/dry_paper"));
        BakedModel wetspModel = modelManager.getModel(Identifier.of("proper-paper", "block/wet_sponge"));
        BakedModel dryspModel = modelManager.getModel(Identifier.of("proper-paper", "block/dry_sponge"));

        matrices.push();

        for (int i = 0; i < 4; i++) {
            if (entity.getItems().get(i).isEmpty()) continue;
            double[] xyOffsets = {0.1875, 0.375, 0.625, 0.8125};

            BakedModel modelToRender;
            if (entity.getItems().get(i).getItem() == ModItems.DAMP_PAPER) {
                modelToRender = wetModel;
            } else if (entity.getItems().get(i).getItem() == ModItems.ROUGH_PAPER) {
                modelToRender = dryModel;
            } else if (entity.getItems().get(i).getItem() == Items.SPONGE) {
                modelToRender = dryspModel;
            } else if (entity.getItems().get(i).getItem() == Items.WET_SPONGE) {
                modelToRender = wetspModel;
            } else {
                continue;
            }

            matrices.push();

            Direction facing = entity.getCachedState().get(DryingRackBlock.FACING);

            switch (facing) {
                case NORTH -> matrices.translate(xyOffsets[i], 0.1875, 0.1875);
                case SOUTH -> {
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                    matrices.translate(xyOffsets[i] - 1, 0.1875, -0.8125);
                }
                case EAST -> {
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270));
                    matrices.translate(xyOffsets[i], 0.1875, -0.8125);
                }
                case WEST -> {
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                    matrices.translate(xyOffsets[i] - 1, 0.1875, 0.1875);
                }
            }

            modelRenderer.render(matrices.peek(), vertexConsumers.getBuffer(RenderLayer.getCutout()),
                    Blocks.AIR.getDefaultState(), modelToRender, 1.0f, 1.0f, 1.0f, light, overlay);

            matrices.pop();
        }

        matrices.pop();
    }
}