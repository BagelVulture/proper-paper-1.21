package net.bagelvulture.properpaper.datagen.custom;

import net.bagelvulture.properpaper.recipe.MaceratorRecipe;
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

import java.util.LinkedHashMap;
import java.util.Map;

public class MaceratorRecipeJsonBuilder {
    private final RecipeCategory category;
    private final Ingredient input;
    private final int inputCount;
    private final int outputCount;
    private final ItemStack output;
    private final int maceratingTime;
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();

    private MaceratorRecipeJsonBuilder(RecipeCategory category, ItemConvertible outputItem, int outputCount, Ingredient input, int inputCount, int maceratingTime) {
        this.category = category;
        this.input = input;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.output = new ItemStack(outputItem, outputCount);
        this.maceratingTime = maceratingTime;
    }

    public static MaceratorRecipeJsonBuilder create(RecipeCategory category, ItemConvertible outputItem, int outputCount, Ingredient input, int inputCount, int maceratingTime) {
        return new MaceratorRecipeJsonBuilder(category, outputItem, outputCount, input, inputCount, maceratingTime);
    }

    public MaceratorRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        criteria.put(name, criterion);
        return this;
    }

    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }

        String path = recipeId.getPath();
        if (!path.endsWith("_from_macerating")) path += "_from_macerating";
        Identifier id = Identifier.of(recipeId.getNamespace(), path);

        Advancement.Builder advBuilder = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);

        criteria.forEach(advBuilder::criterion);

        MaceratorRecipe recipe = new MaceratorRecipe(input, inputCount, output, maceratingTime);

        exporter.accept(id, recipe, advBuilder.build(id.withPrefixedPath("recipes/" + category.getName() + "/")));
    }
}