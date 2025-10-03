package net.bagelvulture.properpaper.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;

public record MaceratorRecipe(
        Ingredient inputItem,
        int inputCount,
        ItemStack output,
        int maceratingTime
) implements Recipe<MaceratorRecipeInput> {

    @Override
    public boolean matches(MaceratorRecipeInput input, net.minecraft.world.World world) {
        if (world.isClient()) return false;
        ItemStack stack = input.getStackInSlot(0);
        return inputItem.test(stack) && stack.getCount() >= inputCount;
    }

    @Override
    public ItemStack craft(MaceratorRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack stack = input.getStackInSlot(0);
        stack.decrement(inputCount);
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MACERATOR_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.MACERATOR_TYPE;
    }

    public static class Serializer implements RecipeSerializer<MaceratorRecipe> {
        public static final MapCodec<MaceratorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(MaceratorRecipe::inputItem),
                Codec.INT.fieldOf("input_count").forGetter(MaceratorRecipe::inputCount),
                ItemStack.CODEC.fieldOf("result").forGetter(MaceratorRecipe::output),
                Codec.INT.fieldOf("maceratingtime").forGetter(MaceratorRecipe::maceratingTime)
        ).apply(inst, MaceratorRecipe::new));

        public static final PacketCodec<RegistryByteBuf, Integer> INT_CODEC = new PacketCodec<>() {
            @Override
            public Integer decode(RegistryByteBuf buf) {
                return buf.readInt();
            }

            @Override
            public void encode(RegistryByteBuf buf, Integer value) {
                buf.writeInt(value);
            }
        };

        public static final PacketCodec<RegistryByteBuf, MaceratorRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, MaceratorRecipe::inputItem,
                        INT_CODEC, MaceratorRecipe::inputCount,
                        ItemStack.PACKET_CODEC, MaceratorRecipe::output,
                        INT_CODEC, MaceratorRecipe::maceratingTime,
                        MaceratorRecipe::new
                );

        @Override
        public MapCodec<MaceratorRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, MaceratorRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}