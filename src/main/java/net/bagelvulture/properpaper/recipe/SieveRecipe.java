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

public record SieveRecipe(
        Ingredient inputItem,
        int inputCount,
        ItemStack output,
        int sieveingTime
) implements Recipe<SieveRecipeInput> {

    @Override
    public boolean matches(SieveRecipeInput input, net.minecraft.world.World world) {
        if (world.isClient()) return false;
        ItemStack stack = input.getStackInSlot(0);
        return inputItem.test(stack) && stack.getCount() >= inputCount;
    }

    @Override
    public ItemStack craft(SieveRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
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
        return ModRecipes.SIEVE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SIEVE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<SieveRecipe> {
        public static final MapCodec<SieveRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(SieveRecipe::inputItem),
                Codec.INT.fieldOf("input_count").forGetter(SieveRecipe::inputCount),
                ItemStack.CODEC.fieldOf("result").forGetter(SieveRecipe::output),
                Codec.INT.fieldOf("sieveingtime").forGetter(SieveRecipe::sieveingTime)
        ).apply(inst, SieveRecipe::new));

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

        public static final PacketCodec<RegistryByteBuf, SieveRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, SieveRecipe::inputItem,
                        INT_CODEC, SieveRecipe::inputCount,
                        ItemStack.PACKET_CODEC, SieveRecipe::output,
                        INT_CODEC, SieveRecipe::sieveingTime,
                        SieveRecipe::new
                );

        @Override
        public MapCodec<SieveRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, SieveRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
