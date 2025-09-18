package net.bagelvulture.properpaper;

import net.bagelvulture.properpaper.block.ModBlocks;
import net.bagelvulture.properpaper.item.ModItemGroups;
import net.bagelvulture.properpaper.item.ModItems;
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
	}
}