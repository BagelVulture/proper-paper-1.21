package net.bagelvulture.properpaper.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record SieveRecipeInput(ItemStack input) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        return input;
    }

    @Override
    public int getSize() {
        return input.getCount();
    }
}