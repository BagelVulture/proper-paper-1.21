package net.bagelvulture.properpaper.item;


import net.bagelvulture.properpaper.ProperPaper;
import net.bagelvulture.properpaper.item.custom.GuideBook;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item WOOD_CHIPS = registerItem("wood_chips", new Item(new Item.Settings()));
    public static final Item SAWDUST = registerItem("sawdust", new Item(new Item.Settings()));
    public static final Item PAPER_PULP = registerItem("paper_pulp", new Item(new Item.Settings()));
    public static final Item DAMP_PAPER = registerItem("damp_paper", new Item(new Item.Settings()));
    public static final Item ROUGH_PAPER = registerItem("rough_paper", new Item(new Item.Settings()));

    public static final Item GUIDE_BOOK = registerItem("guide_book", new GuideBook(new Item.Settings()));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(ProperPaper.MOD_ID, name), item);
    }

    public static void registerModItems() {
    }
}