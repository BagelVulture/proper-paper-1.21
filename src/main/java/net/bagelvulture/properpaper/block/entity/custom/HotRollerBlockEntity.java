package net.bagelvulture.properpaper.block.entity.custom;

import net.bagelvulture.properpaper.recipe.*;
import net.bagelvulture.properpaper.screen.custom.HotRollerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.bagelvulture.properpaper.block.entity.ImplementedInventory;
import net.bagelvulture.properpaper.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HotRollerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    public HotRollerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOT_ROLLER_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> HotRollerBlockEntity.this.progress;
                    case 1 -> HotRollerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: HotRollerBlockEntity.this.progress = value;
                    case 1: HotRollerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.proper-paper.hot_roller");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new HotRollerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("hot_roller.progress", progress);
        nbt.putInt("hot_roller.max_progress", maxProgress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("hot_roller.progress");
        maxProgress = nbt.getInt("hot_roller.max_progress");
        super.readNbt(nbt, registryLookup);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        Optional<RecipeEntry<HotRollerRecipe>> recipeEntry = getCurrentRecipe();
        if (recipeEntry.isPresent()) {
            HotRollerRecipe hotRollerRecipe = recipeEntry.get().value();
            if (progress == 0) {
                maxProgress = hotRollerRecipe.rollingTime();
                if (world != null && !world.isClient) {
                    world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
                }
            }
            increaseCraftingProgress();
            if (world != null) {
                markDirty(world, pos, state);
            }

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
        if (world != null && !world.isClient) {
            world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
        }
    }

    private void craftItem() {
        Optional<RecipeEntry<HotRollerRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) return;

        HotRollerRecipe hotRollerRecipe = recipe.get().value();
        ItemStack output = hotRollerRecipe.output();

        this.removeStack(INPUT_SLOT, hotRollerRecipe.inputCount());
        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));
        world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        Optional<RecipeEntry<HotRollerRecipe>> recipeEntry = getCurrentRecipe();
        if (recipeEntry.isEmpty()) return;

        HotRollerRecipe recipe = recipeEntry.get().value();
        ItemStack result = recipe.output();

        if (canInsertItemIntoOutputSlot(result) && canInsertAmountIntoOutputSlot(result.getCount())) {
            this.progress++;

            if (world != null && !world.isClient) {
                world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
            }
        }
    }

    private Optional<RecipeEntry<HotRollerRecipe>> getCurrentRecipe() {
        return this.getWorld().getRecipeManager()
                .getFirstMatch(ModRecipes.HOT_ROLLER_TYPE, new HotRollerRecipeInput(inventory.get(INPUT_SLOT)), this.getWorld());
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
        int currentCount = this.getStack(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }




    @Override
    public void markDirty() {
        world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }


    public int getProgress() {
        return this.progress;
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }
}