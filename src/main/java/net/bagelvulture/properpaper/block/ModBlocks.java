package net.bagelvulture.properpaper.block;

import net.bagelvulture.properpaper.ProperPaper;
import net.bagelvulture.properpaper.block.custom.DryingRackBlock;
import net.bagelvulture.properpaper.block.custom.HotRollerBlock;
import net.bagelvulture.properpaper.block.custom.MaceratorBlock;
import net.bagelvulture.properpaper.block.custom.SieveBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block DRYING_RACK = registerBlock("drying_rack",
            new DryingRackBlock(AbstractBlock.Settings.create().nonOpaque().strength(2f)));
    public static final Block HOT_ROLLER = registerBlock("hot_roller",
            new HotRollerBlock(AbstractBlock.Settings.create().strength(2f)));
    public static final Block MACERATOR = registerBlock("macerator",
            new MaceratorBlock(AbstractBlock.Settings.create().strength(2f)));
    public static final Block SIEVE = registerBlock("sieve",
            new SieveBlock(AbstractBlock.Settings.create().nonOpaque().strength(2f)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(ProperPaper.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(ProperPaper.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {

    }
}