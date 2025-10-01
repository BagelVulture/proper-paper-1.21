package net.bagelvulture.properpaper.recipe;

import net.bagelvulture.properpaper.ProperPaper;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<SieveRecipe> SIEVE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(ProperPaper.MOD_ID, "sieve"),
            new SieveRecipe.Serializer());
    public static final RecipeType<SieveRecipe> SIEVE_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(ProperPaper.MOD_ID, "sieve"), new RecipeType<SieveRecipe>() {
                @Override
                public String toString() {
                    return "sieve";
                }
            });

    public static final RecipeSerializer<DryingRackRecipe> DRYING_RACK_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(ProperPaper.MOD_ID, "drying_rack"),
            new DryingRackRecipe.Serializer());
    public static final RecipeType<DryingRackRecipe> DRYING_RACK_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(ProperPaper.MOD_ID, "drying_rack"), new RecipeType<DryingRackRecipe>() {
                @Override
                public String toString() {
                    return "drying_rack";
                }
            });

    public static final RecipeSerializer<HotRollerRecipe> HOT_ROLLER_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(ProperPaper.MOD_ID, "hot_roller"),
            new HotRollerRecipe.Serializer());
    public static final RecipeType<HotRollerRecipe> HOT_ROLLER_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(ProperPaper.MOD_ID, "hot_roller"), new RecipeType<HotRollerRecipe>() {
                @Override
                public String toString() {
                    return "hot_roller";
                }
            });

    public static void registerRecipes() {
    }
}