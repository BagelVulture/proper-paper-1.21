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

public record DryingRackRecipe(
        Ingredient inputItem,
        int inputCount,
        ItemStack output,
        int dryingTime
) implements Recipe<DryingRackRecipeInput> {

    @Override
    public boolean matches(DryingRackRecipeInput input, net.minecraft.world.World world) {
        if (world.isClient()) return false;
        ItemStack stack = input.getStackInSlot(0);
        return inputItem.test(stack) && stack.getCount() >= inputCount;
    }

    @Override
    public ItemStack craft(DryingRackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
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
        return ModRecipes.DRYING_RACK_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.DRYING_RACK_TYPE;
    }

    public static class Serializer implements RecipeSerializer<DryingRackRecipe> {
        public static final MapCodec<DryingRackRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(DryingRackRecipe::inputItem),
                Codec.INT.fieldOf("input_count").forGetter(DryingRackRecipe::inputCount),
                ItemStack.CODEC.fieldOf("result").forGetter(DryingRackRecipe::output),
                Codec.INT.fieldOf("dryingtime").forGetter(DryingRackRecipe::dryingTime)
        ).apply(inst, DryingRackRecipe::new));

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

        public static final PacketCodec<RegistryByteBuf, DryingRackRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, DryingRackRecipe::inputItem,
                        INT_CODEC, DryingRackRecipe::inputCount,
                        ItemStack.PACKET_CODEC, DryingRackRecipe::output,
                        INT_CODEC, DryingRackRecipe::dryingTime,
                        DryingRackRecipe::new
                );

        @Override
        public MapCodec<DryingRackRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, DryingRackRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
