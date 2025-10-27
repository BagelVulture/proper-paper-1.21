package net.bagelvulture.properpaper.block.entity.custom;

import net.bagelvulture.properpaper.recipe.ModRecipes;
import net.bagelvulture.properpaper.recipe.SieveRecipe;
import net.bagelvulture.properpaper.recipe.SieveRecipeInput;
import net.bagelvulture.properpaper.screen.custom.SieveScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.bagelvulture.properpaper.block.entity.ImplementedInventory;
import net.bagelvulture.properpaper.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SieveBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    public SieveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SIEVE_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SieveBlockEntity.this.progress;
                    case 1 -> SieveBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: SieveBlockEntity.this.progress = value;
                    case 1: SieveBlockEntity.this.maxProgress = value;
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
        return Text.translatable("block.proper-paper.sieve");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SieveScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("sieve.progress", progress);
        nbt.putInt("sieve.max_progress", maxProgress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("sieve.progress");
        maxProgress = nbt.getInt("sieve.max_progress");
        super.readNbt(nbt, registryLookup);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        Optional<RecipeEntry<SieveRecipe>> recipeEntry = getCurrentRecipe();
        if (recipeEntry.isPresent()) {
            SieveRecipe sieveRecipe = recipeEntry.get().value();
            if (progress == 0) {
                maxProgress = sieveRecipe.sieveingTime();
            }
            increaseCraftingProgress(pos);
            markDirty(world, pos, state);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
                dripIntoCauldron();
            }
        } else {
            resetProgress();
        }
        world.updateListeners(pos, state, state, 3);
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
    }

    private void craftItem() {
        Optional<RecipeEntry<SieveRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) return;

        SieveRecipe sieveRecipe = recipe.get().value();
        ItemStack output = sieveRecipe.output();

        this.removeStack(INPUT_SLOT, sieveRecipe.inputCount());
        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));
        world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress(BlockPos pos) {
        Optional<RecipeEntry<SieveRecipe>> recipeEntry = getCurrentRecipe();
        if (recipeEntry.isEmpty()) return;

        SieveRecipe recipe = recipeEntry.get().value();
        ItemStack result = recipe.output();

        if (canInsertItemIntoOutputSlot(result) && canInsertAmountIntoOutputSlot(result.getCount())) {
            this.progress++;
        }

        if(Math.random() >= 0.85 && !world.isClient()) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.FALLING_WATER,
                    pos.getX() + ((Math.random()) / 2) + 0.25, pos.getY() + 0.8, pos.getZ() + ((Math.random()) / 2) + 0.25,
                    1,
                    0.0, 0.0, 0.0,
                    0.0
            );
        }
    }

    private Optional<RecipeEntry<SieveRecipe>> getCurrentRecipe() {
        return this.getWorld().getRecipeManager()
                .getFirstMatch(ModRecipes.SIEVE_TYPE, new SieveRecipeInput(inventory.get(INPUT_SLOT)), this.getWorld());
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
        if (world != null) world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }

    public void dripIntoCauldron() {
        if (world == null) return;

        for (int i = 1; i <= 4; i++) {
            BlockPos targetPos = pos.down(i);
            BlockState state = world.getBlockState(targetPos);

            if (!world.isAir(targetPos) && !state.isOf(Blocks.CAULDRON)
                    && !state.isOf(Blocks.WATER_CAULDRON)
                    && !state.isOf(Blocks.LAVA_CAULDRON)
                    && !state.isOf(Blocks.POWDER_SNOW_CAULDRON)) {
                break;
            }

            if (state.isOf(Blocks.CAULDRON)) {
                world.setBlockState(targetPos, Blocks.WATER_CAULDRON.getDefaultState()
                        .with(LeveledCauldronBlock.LEVEL, 1));
                world.syncWorldEvent(WorldEvents.POINTED_DRIPSTONE_DRIPS_WATER_INTO_CAULDRON, targetPos, 0);
                world.playSound(null, targetPos, SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER, SoundCategory.BLOCKS, 0.8f, 1.0f);
                return;
            }

            if (state.isOf(Blocks.WATER_CAULDRON)) {
                int level = state.get(LeveledCauldronBlock.LEVEL);
                if (level < 3) {
                    world.setBlockState(targetPos, state.with(LeveledCauldronBlock.LEVEL, level + 1));
                    world.syncWorldEvent(WorldEvents.POINTED_DRIPSTONE_DRIPS_WATER_INTO_CAULDRON, targetPos, 0);
                    world.playSound(null, targetPos, SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER, SoundCategory.BLOCKS, 0.8f, 1.0f);
                }
                return;
            }
        }
    }
}