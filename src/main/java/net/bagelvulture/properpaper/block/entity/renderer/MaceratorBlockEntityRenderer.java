package net.bagelvulture.properpaper.block.entity.renderer;

import net.bagelvulture.properpaper.block.entity.custom.MaceratorBlockEntity;
import net.minecraft.block.Blocks;
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

public class MaceratorBlockEntityRenderer implements BlockEntityRenderer<MaceratorBlockEntity> {
    public MaceratorBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(MaceratorBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        BakedModelManager modelManager = client.getBakedModelManager();

        BakedModel rollers = modelManager.getModel(Identifier.of("proper-paper", "block/macerator_spinny_things"));

        matrices.push();

        client.getBlockRenderManager().getModelRenderer().render(
                entity.getWorld(), rollers, Blocks.AIR.getDefaultState(), entity.getPos(), matrices,
                vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().random, 0,
                OverlayTexture.DEFAULT_UV
        );

        matrices.pop();
    }
}