package net.bagelvulture.properpaper.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SieveBlock extends Block {
    public static final MapCodec<SieveBlock> CODEC = createCodec(SieveBlock::new);
    public static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0.0, 11.0, 0.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 2.0, 16.0, 2.0),
            Block.createCuboidShape(0.0, 0.0, 14.0, 2.0, 16.0, 16.0),
            Block.createCuboidShape(14.0, 0.0, 14.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(14.0, 0.0, 0.0, 16.0, 16.0, 2.0)
    );


    public SieveBlock(Settings settings) {
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
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }
}