package net.bagelvulture.properpaper.item;

import net.bagelvulture.properpaper.ProperPaper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class ModItemGroups {
    public static final ItemGroup PROPER_PAPER_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,Identifier.of
            (ProperPaper.MOD_ID, "proper-paper_item_group"), FabricItemGroup.builder().icon(() -> new ItemStack
                    (ModItems.DAMP_PAPER)).displayName(Text.translatable("itemgroup.proper-paper.proper-paper_item_group"))
            .entries((displayContext, entries) -> {
                entries.add(ModItems.WOOD_CHIPS);
                entries.add(ModItems.SAWDUST);
                entries.add(ModItems.PAPER_PULP);
                entries.add(ModItems.DAMP_PAPER);
                entries.add(ModItems.ROUGH_PAPER);
            }).build()
    );

    public static void registerModItemGroups() {
    }
}
