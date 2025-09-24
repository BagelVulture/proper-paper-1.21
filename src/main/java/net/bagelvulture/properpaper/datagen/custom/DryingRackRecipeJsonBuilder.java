package net.bagelvulture.properpaper.datagen.custom;

import net.bagelvulture.properpaper.recipe.DryingRackRecipe;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

// usage:
//DryingRackRecipeJsonBuilder.create(
//                RecipeCategory.MISC,
//                Items.OUTPUT, 2,
//                Ingredient.ofItems(Items.INPUT), 7,
//                200                                                          // drying time (in ticks)
//        ).criterion((hasItem(Items.INPUT), conditionsFromItem(Items.INPUT))
//        .offerTo(exporter, Identifier.of("ProperPaper.MOD_ID", "output"));


public class DryingRackRecipeJsonBuilder {
    private final RecipeCategory category;
    private final Ingredient input;
    private final int inputCount;
    private final int outputCount;
    private final ItemStack output;
    private final int dryingTime;
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private DryingRackRecipeJsonBuilder(RecipeCategory category, ItemConvertible outputItem, int outputCount, Ingredient input, int inputCount, int dryingTime) {
        this.category = category;
        this.input = input;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.output = new ItemStack(outputItem, outputCount);
        this.dryingTime = dryingTime;
    }

    public static DryingRackRecipeJsonBuilder create(RecipeCategory category, ItemConvertible outputItem, int outputCount, Ingredient input, int inputCount, int dryingTime) {
        return new DryingRackRecipeJsonBuilder(category, outputItem, outputCount, input, inputCount, dryingTime);
    }

    public DryingRackRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        criteria.put(name, criterion);
        return this;
    }

    public DryingRackRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }

        String path = recipeId.getPath();
        if (!path.endsWith("_from_drying")) path += "_from_drying";
        Identifier id = Identifier.of(recipeId.getNamespace(), path);

        Advancement.Builder advBuilder = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);

        criteria.forEach(advBuilder::criterion);

        DryingRackRecipe recipe = new DryingRackRecipe(input, inputCount, output, dryingTime);

        exporter.accept(id, recipe, advBuilder.build(id.withPrefixedPath("recipes/" + category.getName() + "/")));
    }
}