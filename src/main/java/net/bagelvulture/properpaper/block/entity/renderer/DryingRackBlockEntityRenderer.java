package net.bagelvulture.properpaper.block.entity.renderer;

import net.bagelvulture.properpaper.block.entity.custom.DryingRackBlockEntity;
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

public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {
    public DryingRackBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(DryingRackBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        BakedModelManager modelManager = client.getBakedModelManager();

        BakedModel wetmodel = modelManager.getModel(Identifier.of("proper-paper", "block/wet_paper"));
        BakedModel drymodel = modelManager.getModel(Identifier.of("proper-paper", "block/dry_paper"));

        matrices.push();

        if (entity.count(ModItems.DAMP_PAPER) > 0) {
            client.getBlockRenderManager().getModelRenderer().render(
                    entity.getWorld(), wetmodel, Blocks.AIR.getDefaultState(), entity.getPos(), matrices,
                    vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().random, 0,
                    OverlayTexture.DEFAULT_UV
            );
        }
        matrices.pop();
    }
}