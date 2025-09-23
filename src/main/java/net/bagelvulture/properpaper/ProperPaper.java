package net.bagelvulture.properpaper;

import net.bagelvulture.properpaper.block.ModBlocks;
import net.bagelvulture.properpaper.block.entity.ModBlockEntities;
import net.bagelvulture.properpaper.item.ModItemGroups;
import net.bagelvulture.properpaper.item.ModItems;
import net.bagelvulture.properpaper.recipe.ModRecipes;
import net.bagelvulture.properpaper.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProperPaper implements ModInitializer {
	public static final String MOD_ID = "proper-paper";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerModItemGroups();
		ModBlocks.registerModBlocks();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();

		ModRecipes.registerRecipes();
	}
}