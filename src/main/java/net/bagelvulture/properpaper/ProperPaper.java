package net.bagelvulture.properpaper;

import net.bagelvulture.properpaper.block.ModBlocks;
import net.bagelvulture.properpaper.block.entity.ModBlockEntities;
import net.bagelvulture.properpaper.item.ModItemGroups;
import net.bagelvulture.properpaper.item.ModItems;
import net.bagelvulture.properpaper.recipe.ModRecipes;
import net.bagelvulture.properpaper.screen.ModScreenHandlers;
import net.bagelvulture.properpaper.util.ModDataComponentTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProperPaper implements ModInitializer {
	public static final String MOD_ID = "proper-paper";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final RegistryKey<DamageType> CRUSH =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(MOD_ID, "crush"));

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerModItemGroups();
		ModBlocks.registerModBlocks();

        ModDataComponentTypes.registerDataComponentTypes();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();

		ModRecipes.registerRecipes();
	}

    public static DamageSource crush(World world) {
        DynamicRegistryManager manager = world.getRegistryManager();
        RegistryEntry<DamageType> entry = manager
                .get(RegistryKeys.DAMAGE_TYPE)
                .entryOf(CRUSH);
        return new DamageSource(entry);
    }
}