package net.bagelvulture.properpaper.datagen.custom;

import net.bagelvulture.properpaper.recipe.HotRollerRecipe;
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

// usage:
//HotRollerRecipeJsonBuilder.create(
//                RecipeCategory.MISC,
//                Items.OUTPUT, 2,
//                Ingredient.ofItems(Items.INPUT), 7,
//                200                                                          // rolling time (in ticks)
//        ).criterion((hasItem(Items.INPUT), conditionsFromItem(Items.INPUT))
//        .offerTo(exporter, Identifier.of("ProperPaper.MOD_ID", "output"));


public class HotRollerRecipeJsonBuilder {
    private final RecipeCategory category;
    private final Ingredient input;
    private final int inputCount;
    private final int outputCount;
    private final ItemStack output;
    private final int rollingTime;
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();

    private HotRollerRecipeJsonBuilder(RecipeCategory category, ItemConvertible outputItem, int outputCount, Ingredient input, int inputCount, int rollingTime) {
        this.category = category;
        this.input = input;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.output = new ItemStack(outputItem, outputCount);
        this.rollingTime = rollingTime;
    }

    public static HotRollerRecipeJsonBuilder create(RecipeCategory category, ItemConvertible outputItem, int outputCount, Ingredient input, int inputCount, int rollingTime) {
        return new HotRollerRecipeJsonBuilder(category, outputItem, outputCount, input, inputCount, rollingTime);
    }

    public HotRollerRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        criteria.put(name, criterion);
        return this;
    }

    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }

        String path = recipeId.getPath();
        if (!path.endsWith("_from_rolling")) path += "_from_rolling";
        Identifier id = Identifier.of(recipeId.getNamespace(), path);

        Advancement.Builder advBuilder = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);

        criteria.forEach(advBuilder::criterion);

        HotRollerRecipe recipe = new HotRollerRecipe(input, inputCount, output, rollingTime);

        exporter.accept(id, recipe, advBuilder.build(id.withPrefixedPath("recipes/" + category.getName() + "/")));
    }
}