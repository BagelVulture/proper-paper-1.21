package net.bagelvulture.properpaper.block.entity;

import net.bagelvulture.properpaper.ProperPaper;
import net.bagelvulture.properpaper.block.ModBlocks;
import net.bagelvulture.properpaper.block.entity.custom.SieveBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<SieveBlockEntity> SIEVE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(ProperPaper.MOD_ID, "sieve_be"),
                    BlockEntityType.Builder.create(SieveBlockEntity::new, ModBlocks.SIEVE).build(null));


    public static void registerBlockEntities() {
    }
}