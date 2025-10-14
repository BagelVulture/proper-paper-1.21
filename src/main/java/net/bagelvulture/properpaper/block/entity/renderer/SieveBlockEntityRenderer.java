package net.bagelvulture.properpaper.block.entity.renderer;

import net.bagelvulture.properpaper.block.entity.custom.SieveBlockEntity;
import net.bagelvulture.properpaper.item.ModItems;
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

public class SieveBlockEntityRenderer implements BlockEntityRenderer<SieveBlockEntity> {
    public SieveBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(SieveBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager();

        Identifier modelId = Identifier.of("proper-paper", "block/paper_pulp");
        BakedModel model = modelManager.getModel(modelId);

        matrices.push();

        if (entity.count(ModItems.PAPER_PULP) > 0) {
            MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(
                    entity.getWorld(), model, Blocks.AIR.getDefaultState(), entity.getPos(), matrices,
                    vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().random, 0,
                    OverlayTexture.DEFAULT_UV
            );
        }
        matrices.pop();
    }
}