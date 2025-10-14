package net.bagelvulture.properpaper.mixin;

import net.bagelvulture.properpaper.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.WeakHashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(LeveledCauldronBlock.class)
public abstract class WaterCauldronMixin {

    private static final Map<UUID, Long> PROCESSING_ITEMS = new WeakHashMap<>();

    @Inject(method = "onEntityCollision", at = @At("HEAD"))
    private void onEntityCollision(BlockState state, World world, BlockPos pos, net.minecraft.entity.Entity entity, CallbackInfo ci) {
        if (world.isClient) return;
        if (state.getBlock() != Blocks.WATER_CAULDRON) return;
        if (!(entity instanceof ItemEntity itemEntity)) return;

        ItemStack stack = itemEntity.getStack();
        if (!stack.isOf(ModItems.SAWDUST)) return;

        UUID id = itemEntity.getUuid();
        long currentTime = world.getTime();

        if (!PROCESSING_ITEMS.containsKey(id)) {
            PROCESSING_ITEMS.put(id, currentTime);
            return;
        }

        long startTime = PROCESSING_ITEMS.get(id);
        if (currentTime - startTime >= 50) {
            PROCESSING_ITEMS.remove(id);

            int count = stack.getCount();
            itemEntity.setStack(new ItemStack(ModItems.PAPER_PULP, count));
        }
    }
}