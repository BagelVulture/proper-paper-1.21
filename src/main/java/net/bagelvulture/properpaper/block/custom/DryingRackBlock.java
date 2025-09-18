package net.bagelvulture.properpaper.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DryingRackBlock extends HorizontalFacingBlock {
    public static final MapCodec<DryingRackBlock> CODEC = createCodec(DryingRackBlock::new);
    public static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0.0, 13.0, 0.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 2.0, 16.0, 2.0),
            Block.createCuboidShape(0.0, 0.0, 14.0, 2.0, 16.0, 16.0),
            Block.createCuboidShape(14.0, 0.0, 14.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(14.0, 0.0, 0.0, 16.0, 16.0, 2.0)
    );

    public DryingRackBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
       //if(!world.isClient()) {
       //    Entity entity = null;
       //    List<ChairEntity> entities = world.getEntitiesByType(ModEntities.CHAIR, new Box(pos), chair -> true);
       //    if(entities.isEmpty()) {
       //        entity = ModEntities.CHAIR.spawn((ServerWorld) world, pos, SpawnReason.TRIGGERED);
       //    } else {
       //        entity = entities.get(0);
       //    }

       //    player.startRiding(entity);
       //}

        return ActionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case NORTH, EAST, WEST, SOUTH -> SHAPE;
            case DOWN,UP -> null;
        };
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}