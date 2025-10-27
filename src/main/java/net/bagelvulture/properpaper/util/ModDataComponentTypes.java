package net.bagelvulture.properpaper.util;

import com.mojang.serialization.Codec;
import net.bagelvulture.properpaper.ProperPaper;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<Integer> CURRENT_PAGE_COMP =
            register("current_page_comp", builder -> builder.codec(Codec.INT));


    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(ProperPaper.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
    }
}