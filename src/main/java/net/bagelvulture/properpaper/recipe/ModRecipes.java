package net.bagelvulture.properpaper.recipe;

import net.bagelvulture.properpaper.ProperPaper;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<SieveRecipe> SIEVE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(ProperPaper.MOD_ID, "seive"),
            new SieveRecipe.Serializer());
    public static final RecipeType<SieveRecipe> SIEVE_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(ProperPaper.MOD_ID, "seive"), new RecipeType<SieveRecipe>() {
                @Override
                public String toString() {
                    return "seive";
                }
            });

    public static void registerRecipes() {
    }
}