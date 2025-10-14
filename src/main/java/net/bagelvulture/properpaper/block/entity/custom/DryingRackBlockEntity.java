package net.bagelvulture.properpaper.block.entity.custom;

import net.bagelvulture.properpaper.recipe.DryingRackRecipe;
import net.bagelvulture.properpaper.recipe.DryingRackRecipeInput;
import net.bagelvulture.properpaper.recipe.ModRecipes;
import net.bagelvulture.properpaper.screen.custom.DryingRackScreenHandler;
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

public class DryingRackBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    private final int[] progress = new int[4];
    private final int[] maxProgress = new int[4];

    protected final PropertyDelegate propertyDelegate;

    public DryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRYING_RACK_BE, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                if (index >= 0 && index < 4) return progress[index];
                if (index >= 4 && index < 8) return maxProgress[index - 4];
                return 0;
            }

            @Override
            public void set(int index, int value) {
                if (index >= 0 && index < 4) progress[index] = value;
                if (index >= 4 && index < 8) maxProgress[index - 4] = value;
            }

            @Override
            public int size() {
                return 8;
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
        return Text.translatable("block.proper-paper.drying_rack");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new DryingRackScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.writeNbt(nbt, inventory, registryLookup);
        for (int i = 0; i < 4; i++) {
            nbt.putInt("drying_rack.progress." + i, progress[i]);
            nbt.putInt("drying_rack.max_progress." + i, maxProgress[i]);
        }
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        for (int i = 0; i < 4; i++) {
            progress[i] = nbt.contains("drying_rack.progress." + i) ? nbt.getInt("drying_rack.progress." + i) : 0;
            maxProgress[i] = nbt.contains("drying_rack.max_progress." + i) ? nbt.getInt("drying_rack.max_progress." + i) : 72;
        }
        super.readNbt(nbt, registryLookup);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world == null || world.isClient) return;

        boolean changed = false;

        for (int slot = 0; slot < 4; slot++) {
            ItemStack stack = inventory.get(slot);

            if (stack.isEmpty()) {
                if (progress[slot] != 0 || maxProgress[slot] != 72) {
                    progress[slot] = 0;
                    maxProgress[slot] = 72;
                    changed = true;
                }
                continue;
            }

            Optional<RecipeEntry<DryingRackRecipe>> recipeEntry = this.getRecipeForSlot(slot, world);
            if (recipeEntry.isPresent()) {
                DryingRackRecipe recipe = recipeEntry.get().value();
                if (progress[slot] == 0) {
                    maxProgress[slot] = recipe.dryingTime();
                }

                progress[slot]++;
                changed = true;

                if (progress[slot] >= maxProgress[slot]) {
                    ItemStack out = recipe.output().copy();
                    inventory.set(slot, out);
                    progress[slot] = 0;
                    maxProgress[slot] = 72;
                }
            } else {
                if (progress[slot] != 0 || maxProgress[slot] != 72) {
                    progress[slot] = 0;
                    maxProgress[slot] = 72;
                    changed = true;
                }
            }
        }

        if (changed) {
            markDirty();
            world.updateListeners(pos, state, state, 3);
        }
    }

    private Optional<RecipeEntry<DryingRackRecipe>> getRecipeForSlot(int slot, World world) {
        ItemStack forRecipe = inventory.get(slot);
        return world.getRecipeManager().getFirstMatch(ModRecipes.DRYING_RACK_TYPE, new DryingRackRecipeInput(forRecipe), world);
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
        if (world != null) world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }
}