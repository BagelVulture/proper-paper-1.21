package net.bagelvulture.properpaper.block.entity.renderer;

import net.bagelvulture.properpaper.block.custom.HotRollerBlock;
import net.bagelvulture.properpaper.block.entity.custom.HotRollerBlockEntity;
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

public class HotRollerBlockEntityRenderer implements BlockEntityRenderer<HotRollerBlockEntity> {
    public HotRollerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(HotRollerBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if (entity.getWorld() == null) return;

        int progress = entity.getProgress();
        int maxProgress = entity.getMaxProgress();

        if (progress <= 0 || maxProgress <= 0) return;

        MinecraftClient client = MinecraftClient.getInstance();
        BakedModelManager modelManager = client.getBakedModelManager();
        BakedModel paperModel = modelManager.getModel(Identifier.of("proper-paper", "block/paper_rolling"));

        float progressRatio = (progress + tickDelta) / (float) maxProgress;
        progressRatio = Math.min(Math.max(progressRatio, 0f), 1f);

        float zOffset = progressRatio - 0.5f;

        matrices.push();

        matrices.translate(0.5, 0, 0.5);

        Direction facing = entity.getCachedState().get(HotRollerBlock.FACING);
        float yRotation = switch (facing) {
            case NORTH -> 0f;
            case WEST -> 90f;
            case EAST -> -90f;
            default -> 180f;
        };
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yRotation));

        matrices.translate(-0.5, 0, zOffset - 0.5);

        client.getBlockRenderManager().getModelRenderer().render(
                matrices.peek(), vertexConsumers.getBuffer(RenderLayer.getCutout()), null, paperModel,
                1.0f, 1.0f, 1.0f, light, OverlayTexture.DEFAULT_UV
        );

        matrices.pop();
    }
}