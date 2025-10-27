package net.bagelvulture.properpaper.item.custom;

import net.bagelvulture.properpaper.screen.custom.GuideBookScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WrittenBookContentComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.text.RawFilteredPair;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class GuideBook extends WrittenBookItem {

    public GuideBook(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!stack.contains(DataComponentTypes.WRITTEN_BOOK_CONTENT)) {

            WrittenBookContentComponent content = new WrittenBookContentComponent(
                    RawFilteredPair.of("The Proper Way to Make Paper"),
                    "BagelVulture",
                    0,
                    List.of(RawFilteredPair.of(Text.empty())),
                    true
            );

            stack.set(DataComponentTypes.WRITTEN_BOOK_CONTENT, content);
        }

        if (world.isClient) {
            MinecraftClient.getInstance().setScreen(new GuideBookScreen(stack));
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}