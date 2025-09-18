package net.bagelvulture.properpaper.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static net.bagelvulture.properpaper.block.ModBlocks.*;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
        addDrop(DRYING_RACK);
        addDrop(HOT_ROLLER);
        addDrop(MACERATOR);
        addDrop(SIEVE);
    }

    @Override
    public void generate() {

    }
}