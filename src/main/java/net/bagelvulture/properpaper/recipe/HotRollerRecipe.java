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

public record HotRollerRecipe(
        Ingredient inputItem,
        int inputCount,
        ItemStack output,
        int rollingTime
) implements Recipe<HotRollerRecipeInput> {

    @Override
    public boolean matches(HotRollerRecipeInput input, net.minecraft.world.World world) {
        if (world.isClient()) return false;
        ItemStack stack = input.getStackInSlot(0);
        return inputItem.test(stack) && stack.getCount() >= inputCount;
    }

    @Override
    public ItemStack craft(HotRollerRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
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
        return ModRecipes.HOT_ROLLER_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.HOT_ROLLER_TYPE;
    }

    public static class Serializer implements RecipeSerializer<HotRollerRecipe> {
        public static final MapCodec<HotRollerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(HotRollerRecipe::inputItem),
                Codec.INT.fieldOf("input_count").forGetter(HotRollerRecipe::inputCount),
                ItemStack.CODEC.fieldOf("result").forGetter(HotRollerRecipe::output),
                Codec.INT.fieldOf("rollingtime").forGetter(HotRollerRecipe::rollingTime)
        ).apply(inst, HotRollerRecipe::new));

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

        public static final PacketCodec<RegistryByteBuf, HotRollerRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, HotRollerRecipe::inputItem,
                        INT_CODEC, HotRollerRecipe::inputCount,
                        ItemStack.PACKET_CODEC, HotRollerRecipe::output,
                        INT_CODEC, HotRollerRecipe::rollingTime,
                        HotRollerRecipe::new
                );

        @Override
        public MapCodec<HotRollerRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, HotRollerRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}