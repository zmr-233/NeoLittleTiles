package team.creative.neolittletiles.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import team.creative.neolittletiles.NeoLittleTilesRegistry;

/**
 * NeoTilesBlock - Core block for storing and managing NeoTiles
 * 
 * Simplified block implementation for MVP functionality:
 * - Stores tiles via block entity
 * - Handles placement/destruction interactions
 * - Provides basic collision detection
 * 
 * Based on analysis of BETiles.java rendering requirements
 */
public class NeoTilesBlock extends BaseEntityBlock {
    
    public static final String BLOCK_ID = "neotiles";
    public static final MapCodec<NeoTilesBlock> CODEC = simpleCodec(properties -> new NeoTilesBlock());
    
    public NeoTilesBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(0.5F)
            .noOcclusion()
        );
    }
    
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NeoTilesBlockEntity(pos, state);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof NeoTilesBlockEntity neoTilesBlockEntity) {
                System.out.println("NeoTilesBlock interaction at position: " + pos);
                System.out.println("Block entity has " + neoTilesBlockEntity.getTileCount() + " tiles");
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof NeoTilesBlockEntity neoTilesBlockEntity) {
                System.out.println("NeoTilesBlock destroyed at position: " + pos);
                System.out.println("Had " + neoTilesBlockEntity.getTileCount() + " tiles");
                // TODO: Drop tile items
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }
    
    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof NeoTilesBlockEntity neoTilesBlockEntity) {
            // TODO: Calculate from contained tile states
            return 0;
        }
        return 0;
    }
    
    /**
     * Get the NeoTilesBlockEntity at the given position
     * @param level The world level
     * @param pos Block position
     * @return NeoTilesBlockEntity instance or null
     */
    public static NeoTilesBlockEntity getBlockEntity(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof NeoTilesBlockEntity neoTilesBlockEntity) {
            return neoTilesBlockEntity;
        }
        return null;
    }
}