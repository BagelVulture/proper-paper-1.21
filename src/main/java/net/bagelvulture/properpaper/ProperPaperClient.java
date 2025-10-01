package net.bagelvulture.properpaper;

import net.bagelvulture.properpaper.block.ModBlocks;
import net.bagelvulture.properpaper.block.entity.ModBlockEntities;
import net.bagelvulture.properpaper.block.entity.renderer.DryingRackBlockEntityRenderer;
import net.bagelvulture.properpaper.block.entity.renderer.HotRollerBlockEntityRenderer;
import net.bagelvulture.properpaper.block.entity.renderer.SieveBlockEntityRenderer;
import net.bagelvulture.properpaper.screen.ModScreenHandlers;
import net.bagelvulture.properpaper.screen.custom.DryingRackScreen;
import net.bagelvulture.properpaper.screen.custom.HotRollerScreen;
import net.bagelvulture.properpaper.screen.custom.SieveScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;

public class ProperPaperClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SIEVE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DRYING_RACK, RenderLayer.getCutout());

        HandledScreens.register(ModScreenHandlers.SIEVE_SCREEN_HANDLER, SieveScreen::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SIEVE_BE, SieveBlockEntityRenderer::new);
        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.addModels(Identifier.of("proper-paper", "block/paper_pulp"));
        });

        HandledScreens.register(ModScreenHandlers.DRYING_RACK_SCREEN_HANDLER, DryingRackScreen::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DRYING_RACK_BE, DryingRackBlockEntityRenderer::new);
        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.addModels(Identifier.of("proper-paper", "block/wet_paper"));
        });
        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.addModels(Identifier.of("proper-paper", "block/dry_paper"));
        });

        HandledScreens.register(ModScreenHandlers.HOT_ROLLER_SCREEN_HANDLER, HotRollerScreen::new);
        BlockEntityRendererFactories.register(ModBlockEntities.HOT_ROLLER_BE, HotRollerBlockEntityRenderer::new);

        //HandledScreens.register(ModScreenHandlers.MACERATOR_SCREEN_HANDLER, MaceratorScreen::new);
        //BlockEntityRendererFactories.register(ModBlockEntities.MACERATOR_BE, MaceratorBlockEntityRenderer::new);
        //ModelLoadingPlugin.register(pluginContext -> {
        //    pluginContext.addModels(Identifier.of("proper-paper", "block/macerator_spinny_things"));
        //});
    }
}
