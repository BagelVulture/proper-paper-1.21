package net.bagelvulture.properpaper.datagen;

import net.bagelvulture.properpaper.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.WOOD_CHIPS, Models.GENERATED);
        itemModelGenerator.register(ModItems.SAWDUST, Models.GENERATED);
        itemModelGenerator.register(ModItems.PAPER_PULP, Models.GENERATED);
        itemModelGenerator.register(ModItems.DAMP_PAPER, Models.GENERATED);
        itemModelGenerator.register(ModItems.ROUGH_PAPER, Models.GENERATED);
    }
}