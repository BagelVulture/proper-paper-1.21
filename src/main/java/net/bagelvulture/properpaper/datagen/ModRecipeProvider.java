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
        // mod item recipes
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
                        ModItems.WOOD_CHIPS, 1,
                        Ingredient.fromTag(ItemTags.WOODEN_STAIRS), 1,
                        40
                ).criterion("has_item_in_tag", conditionsFromTag(ItemTags.WOODEN_STAIRS))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "wood_chips_from_stairs"));
        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.WOOD_CHIPS, 1,
                        Ingredient.fromTag(ItemTags.WOODEN_SLABS), 2,
                        30
                ).criterion("has_item_in_tag", conditionsFromTag(ItemTags.WOODEN_SLABS))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "wood_chips_from_slabs"));
        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.WOOD_CHIPS, 1,
                        Ingredient.fromTag(ItemTags.WOODEN_FENCES), 1,
                        20
                ).criterion("has_item_in_tag", conditionsFromTag(ItemTags.WOODEN_FENCES))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "wood_chips_from_fences"));
        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.WOOD_CHIPS, 1,
                        Ingredient.fromTag(ItemTags.FENCE_GATES), 1,
                        20
                ).criterion("has_item_in_tag", conditionsFromTag(ItemTags.FENCE_GATES))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "wood_chips_from_fence_gates"));
        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.WOOD_CHIPS, 2,
                        Ingredient.fromTag(ItemTags.WOODEN_DOORS), 1,
                        40
                ).criterion("has_item_in_tag", conditionsFromTag(ItemTags.WOODEN_DOORS))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "wood_chips_from_doors"));
        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.WOOD_CHIPS, 1,
                        Ingredient.fromTag(ItemTags.WOODEN_TRAPDOORS), 1,
                        30
                ).criterion("has_item_in_tag", conditionsFromTag(ItemTags.WOODEN_TRAPDOORS))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "wood_chips_from_trapdoors"));
        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.WOOD_CHIPS, 1,
                        Ingredient.fromTag(ItemTags.WOODEN_PRESSURE_PLATES), 1,
                        20
                ).criterion("has_item_in_tag", conditionsFromTag(ItemTags.WOODEN_PRESSURE_PLATES))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "wood_chips_from_pressure_plates"));
        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.WOOD_CHIPS, 1,
                        Ingredient.fromTag(ItemTags.WOODEN_BUTTONS), 6,
                        5
                ).criterion("has_item_in_tag", conditionsFromTag(ItemTags.WOODEN_BUTTONS))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "wood_chips_from_buttons"));

        MaceratorRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        ModItems.SAWDUST, 1,
                        Ingredient.ofItems(ModItems.WOOD_CHIPS), 1,
                        40
                ).criterion(hasItem(ModItems.WOOD_CHIPS), conditionsFromItem(ModItems.WOOD_CHIPS))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "sawdust"));



        // vanilla integration recipes
        SieveRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        Items.CLAY, 2,
                        Ingredient.ofItems(Items.MUD), 1,
                        400
                ).criterion(hasItem(Items.MUD), conditionsFromItem(Items.MUD))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "clay"));

        HotRollerRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        Items.GLASS_PANE, 3,
                        Ingredient.ofItems(Items.SAND), 1,
                        100
                ).criterion(hasItem(Items.SAND), conditionsFromItem(Items.SAND))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "glass_pane"));

        DryingRackRecipeJsonBuilder.create(
                        RecipeCategory.MISC,
                        Items.SPONGE, 1,
                        Ingredient.ofItems(Items.WET_SPONGE), 1,
                        400
                ).criterion(hasItem(Items.WET_SPONGE), conditionsFromItem(Items.WET_SPONGE))
                .offerTo(exporter, Identifier.of(ProperPaper.MOD_ID, "sponge"));



        // crafting the workbenches
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
                .input('A', ItemTags.PLANKS)
                .input('B', Items.STRING)
                .criterion("has_item_in_tag", conditionsFromTag(ItemTags.PLANKS))
                .offerTo(exporter);
    }
}