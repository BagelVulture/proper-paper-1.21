package net.bagelvulture.properpaper.block.entity.renderer;

import net.bagelvulture.properpaper.block.custom.MaceratorBlock;
import net.bagelvulture.properpaper.block.entity.custom.MaceratorBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class MaceratorBlockEntityRenderer implements BlockEntityRenderer<MaceratorBlockEntity> {
    public MaceratorBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(MaceratorBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getWorld() == null) return;

        Direction facing = entity.getCachedState().get(MaceratorBlock.FACING);
        MinecraftClient client = MinecraftClient.getInstance();
        BakedModelManager modelManager = client.getBakedModelManager();
        BakedModel rollers = modelManager.getModel(Identifier.of("proper-paper", "block/macerator_spinny_things"));

        float time = (entity.getWorld().getTime() + tickDelta);
        float speed = 4.0f;

        matrices.push();
        switch (facing) {
            case NORTH, SOUTH -> {
                matrices.translate(0.5, 0.8125, 0.28125);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((time * speed) % 360));
            }
            case EAST, WEST -> {
                matrices.translate(0.28125, 0.8125, 0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((time * speed) % 360));
            }
        }
        renderModel(client, rollers, matrices, vertexConsumers, light);
        matrices.pop();


        matrices.push();
        switch (facing) {
            case NORTH, SOUTH -> {
                matrices.translate(0.5, 0.8125, 0.71875);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(((-time * speed) % 360) + 22.5f));
            }
            case EAST, WEST -> {
                matrices.translate(0.71875, 0.8125, 0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(((-time * speed) % 360) + 22.5f));
            }
        }
        renderModel(client, rollers, matrices, vertexConsumers, light);
        matrices.pop();
    }

    private void renderModel(MinecraftClient client, BakedModel model, MatrixStack matrices,
                             VertexConsumerProvider vertexConsumers, int light) {
        client.getBlockRenderManager().getModelRenderer().render(
                matrices.peek(), vertexConsumers.getBuffer(RenderLayer.getCutout()), null, model,
                1.0f, 1.0f, 1.0f, light, OverlayTexture.DEFAULT_UV
        );
    }
}