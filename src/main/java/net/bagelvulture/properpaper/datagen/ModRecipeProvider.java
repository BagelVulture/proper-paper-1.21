package net.bagelvulture.properpaper.datagen;


import net.bagelvulture.properpaper.ProperPaper;
import net.bagelvulture.properpaper.block.ModBlocks;
import net.bagelvulture.properpaper.datagen.custom.DryingRackRecipeJsonBuilder;
import net.bagelvulture.properpaper.datagen.custom.HotRollerRecipeJsonBuilder;
import net.bagelvulture.properpaper.datagen.custom.MaceratorRecipeJsonBuilder;
import net.bagelvulture.properpaper.datagen.custom.SieveRecipeJsonBuilder;
import net.bagelvulture.properpaper.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
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
                        80
                ).criterion(hasItem(ModItems.PAPER_PULP), conditionsFromItem(ModItems.PAPER_PULP))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "damp_paper"));

        DryingRackRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.ROUGH_PAPER, 1,
                        Ingredient.ofItems(ModItems.DAMP_PAPER), 1,
                        120
                ).criterion(hasItem(ModItems.DAMP_PAPER), conditionsFromItem(ModItems.DAMP_PAPER))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "rough_paper"));

        HotRollerRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        Items.PAPER, 1,
                        Ingredient.ofItems(ModItems.ROUGH_PAPER), 1,
                        40
                ).criterion(hasItem(ModItems.ROUGH_PAPER), conditionsFromItem(ModItems.ROUGH_PAPER))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "paper"));

        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.WOOD_CHIPS, 1,
                        Ingredient.fromTag(ItemTags.PLANKS), 1,
                        40
                ).criterion("has_item_in_tag", conditionsFromTag(ItemTags.PLANKS))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "wood_chips_from_planks"));

        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.WOOD_CHIPS, 4,
                        Ingredient.fromTag(ItemTags.LOGS), 1,
                        40
                ).criterion("has_item_in_tag", conditionsFromTag(ItemTags.LOGS))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "wood_chips_from_logs"));

        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.SAWDUST, 1,
                        Ingredient.ofItems(ModItems.WOOD_CHIPS), 1,
                        40
                ).criterion(hasItem(ModItems.WOOD_CHIPS), conditionsFromItem(ModItems.WOOD_CHIPS))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "sawdust"));



        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.DRYING_RACK)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("A A")
                .input('A', ItemTags.PLANKS)
                .input('B', Items.IRON_INGOT)
                .criterion("has_item_in_tag", conditionsFromTag(ItemTags.PLANKS))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.HOT_ROLLER)
                .pattern(" B ")
                .pattern(" B ")
                .pattern("AAA")
                .input('A', Items.SMOOTH_STONE)
                .input('B', Items.STONE)
                .criterion(hasItem(Items.STONE), conditionsFromItem(Items.STONE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.MACERATOR)
                .pattern("B B")
                .pattern("AAA")
                .pattern("AAA")
                .input('A', Items.SMOOTH_STONE)
                .input('B', Items.STONE)
                .criterion(hasItem(Items.STONE), conditionsFromItem(Items.STONE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.SIEVE)
                .pattern("ABA")
                .pattern("A A")
                .pattern("A A")
                .input('B', ItemTags.PLANKS)
                .input('C', Items.STRING)
                .criterion("has_item_in_tag", conditionsFromTag(ItemTags.PLANKS))
                .offerTo(exporter);
    }
}