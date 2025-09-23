package net.bagelvulture.properpaper.datagen;


import net.bagelvulture.properpaper.ProperPaper;
import net.bagelvulture.properpaper.datagen.custom.SieveRecipeJsonBuilder;
import net.bagelvulture.properpaper.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        SieveRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.DAMP_PAPER, 2,
                        Ingredient.ofItems(ModItems.PAPER_PULP), 1,
                        40
                ).criterion(hasItem(ModItems.PAPER_PULP), conditionsFromItem(ModItems.PAPER_PULP))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "paper_pulp"));
    }
}