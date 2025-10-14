package net.bagelvulture.properpaper.block.entity.custom;

import com.ibm.icu.text.MessagePattern;
import net.bagelvulture.properpaper.block.entity.ImplementedInventory;
import net.bagelvulture.properpaper.block.entity.ModBlockEntities;
import net.bagelvulture.properpaper.recipe.MaceratorRecipe;
import net.bagelvulture.properpaper.recipe.MaceratorRecipeInput;
import net.bagelvulture.properpaper.recipe.ModRecipes;
import net.bagelvulture.properpaper.screen.custom.MaceratorScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
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
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MaceratorBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    public MaceratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MACERATOR_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> MaceratorBlockEntity.this.progress;
                    case 1 -> MaceratorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: MaceratorBlockEntity.this.progress = value;
                    case 1: MaceratorBlockEntity.this.maxProgress = value;
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
        return Text.translatable("block.proper-paper.macerator");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MaceratorScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("macerator.progress", progress);
        nbt.putInt("macerator.max_progress", maxProgress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("macerator.progress");
        maxProgress = nbt.getInt("macerator.max_progress");
        super.readNbt(nbt, registryLookup);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        Optional<RecipeEntry<MaceratorRecipe>> recipeEntry = getCurrentRecipe();
        if (recipeEntry.isPresent()) {
            MaceratorRecipe maceratorRecipe = recipeEntry.get().value();
            if (progress == 0) {
                maxProgress = maceratorRecipe.maceratingTime();
            }
            increaseCraftingProgress();
            markDirty(world, pos, state);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
            if(!world.isClient() && !this.getStack(INPUT_SLOT).isEmpty() && progress != 0) {
                ((ServerWorld) world).spawnParticles(
                        new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack(INPUT_SLOT).getItem().getDefaultStack()),
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                        4, 0, 0, 0, 0.1);
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
    }

    private void craftItem() {
        Optional<RecipeEntry<MaceratorRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) return;

        MaceratorRecipe maceratorRecipe = recipe.get().value();
        ItemStack output = maceratorRecipe.output();

        this.removeStack(INPUT_SLOT, maceratorRecipe.inputCount());
        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));
        world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        Optional<RecipeEntry<MaceratorRecipe>> recipeEntry = getCurrentRecipe();
        if (recipeEntry.isEmpty()) return;

        MaceratorRecipe recipe = recipeEntry.get().value();
        ItemStack result = recipe.output();

        if (canInsertItemIntoOutputSlot(result) && canInsertAmountIntoOutputSlot(result.getCount())) {
            this.progress++;
        }
    }

    private Optional<RecipeEntry<MaceratorRecipe>> getCurrentRecipe() {
        return this.getWorld().getRecipeManager()
                .getFirstMatch(ModRecipes.MACERATOR_TYPE, new MaceratorRecipeInput(inventory.get(INPUT_SLOT)), this.getWorld());
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
}