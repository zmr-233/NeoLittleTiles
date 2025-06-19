package team.creative.neolittletiles.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import team.creative.neolittletiles.common.action.NeoAction;
import team.creative.neolittletiles.common.action.NeoDestroyAction;
import team.creative.neolittletiles.common.block.NeoTilesBlock;
import team.creative.neolittletiles.common.block.NeoTilesBlockEntity;
import team.creative.neolittletiles.common.grid.NeoGrid;
import team.creative.neolittletiles.common.math.NeoBox;

import java.util.List;

/**
 * NeoHammer - Specialized destruction tool for efficient tile removal
 * 
 * Hammer tool features:
 * - Left click to destroy tiles in larger areas
 * - Right click for precision single-tile destruction
 * - Configurable destruction size via mouse wheel
 * - Efficient multi-tile destruction
 * 
 * Based on LittleTiles hammer tool functionality
 */
public class NeoHammer extends Item {
    
    public static final String ITEM_ID = "neohammer";
    private static final NeoGrid DEFAULT_GRID = NeoGrid.GRID_16;
    
    // Destruction sizes (in grid units)
    private static final int[] DESTRUCTION_SIZES = {1, 2, 4, 8, 16, 32};
    
    public NeoHammer(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        
        if (!level.isClientSide) {
            // Check if we're targeting a NeoTiles block
            if (level.getBlockState(pos).getBlock() instanceof NeoTilesBlock) {
                NeoTilesBlockEntity blockEntity = NeoTilesBlock.getBlockEntity(level, pos);
                if (blockEntity != null) {
                    // Create destruction area
                    NeoBox destructionArea = getDestructionArea(context);
                    
                    // Remove tiles from block entity
                    var removedTiles = blockEntity.removeTiles(destructionArea);
                    
                    System.out.println("NeoHammer destruction: removed " + removedTiles.size() + " tiles");
                    System.out.println("Destruction area: " + destructionArea);
                    
                    // If no tiles remain, consider removing the block
                    if (!blockEntity.hasTiles()) {
                        // TODO: Remove block or keep for future tiles
                        System.out.println("Block entity is now empty");
                    }
                    
                    return removedTiles.isEmpty() ? InteractionResult.FAIL : InteractionResult.SUCCESS;
                }
            }
        }
        
        return InteractionResult.PASS;
    }
    
    /**
     * Get destruction area from context
     * @param context Use context with hit result
     * @return Destruction area for tiles
     */
    private NeoBox getDestructionArea(UseOnContext context) {
        // For MVP, create a 2x2x2 destruction area
        int size = DEFAULT_GRID.getSize() / 8; // 2x2x2 in a 16x16x16 grid
        
        var hitPos = context.getClickLocation();
        var blockPos = context.getClickedPos();
        
        // Calculate relative position within the block (0-15 in grid coordinates)
        int relX = (int) ((hitPos.x - blockPos.getX()) * DEFAULT_GRID.getSize());
        int relY = (int) ((hitPos.y - blockPos.getY()) * DEFAULT_GRID.getSize());
        int relZ = (int) ((hitPos.z - blockPos.getZ()) * DEFAULT_GRID.getSize());
        
        // Center the destruction area on the hit point
        relX = Math.max(0, Math.min(DEFAULT_GRID.getSize() - size, relX - size/2));
        relY = Math.max(0, Math.min(DEFAULT_GRID.getSize() - size, relY - size/2));
        relZ = Math.max(0, Math.min(DEFAULT_GRID.getSize() - size, relZ - size/2));
        
        return new NeoBox(relX, relY, relZ, relX + size, relY + size, relZ + size);
    }
    
    /**
     * Handle left click - area destruction
     * @param level World level
     * @param player Player using the tool
     * @param hand Player hand (main/offhand)
     * @param hitResult Ray trace hit result
     * @return Interaction result
     */
    public static Object onLeftClick(Object level, Object player, Object hand, Object hitResult) {
        System.out.println("NeoHammer left click - area destruction");
        
        // Get destruction parameters
        int destructionSize = getDestructionSize(player);
        NeoBox destructionArea = getDestructionArea(hitResult, DEFAULT_GRID, destructionSize);
        
        // Create and execute destruction action
        NeoDestroyAction destroyAction = new NeoDestroyAction(destructionArea);
        NeoAction.Result result = destroyAction.execute(player);
        
        System.out.println("Area destruction result: " + result + " (size: " + destructionSize + ")");
        
        // TODO: Return proper InteractionResult when available
        return result == NeoAction.Result.SUCCESS ? "SUCCESS" : "FAIL";
    }
    
    /**
     * Handle right click - precision destruction
     * @param level World level
     * @param player Player using the tool
     * @param hand Player hand (main/offhand)
     * @param hitResult Ray trace hit result
     * @return Interaction result
     */
    public static Object onRightClick(Object level, Object player, Object hand, Object hitResult) {
        System.out.println("NeoHammer right click - precision destruction");
        
        // Get single tile destruction area
        NeoBox destructionArea = getPrecisionDestructionArea(hitResult, DEFAULT_GRID);
        
        // Create and execute precision destruction action
        NeoDestroyAction destroyAction = new NeoDestroyAction(destructionArea);
        NeoAction.Result result = destroyAction.execute(player);
        
        System.out.println("Precision destruction result: " + result);
        
        // TODO: Return proper InteractionResult when available
        return result == NeoAction.Result.SUCCESS ? "SUCCESS" : "FAIL";
    }
    
    /**
     * Calculate area destruction box from hit result
     * @param hitResult Ray trace result
     * @param grid Grid system to use
     * @param size Destruction size in grid units
     * @return Box for area destruction
     */
    private static NeoBox getDestructionArea(Object hitResult, NeoGrid grid, int size) {
        // TODO: Implement proper ray trace calculation when BlockHitResult is available
        
        // For MVP, create destruction area centered at origin
        int halfSize = size / 2;
        return new NeoBox(-halfSize, 0, -halfSize, halfSize, size, halfSize);
    }
    
    /**
     * Calculate precision destruction box from hit result
     * @param hitResult Ray trace result
     * @param grid Grid system to use
     * @return Box for single tile destruction
     */
    private static NeoBox getPrecisionDestructionArea(Object hitResult, NeoGrid grid) {
        // TODO: Implement proper ray trace calculation when BlockHitResult is available
        
        // For MVP, create single grid unit box
        return new NeoBox(0, 0, 0, 1, 1, 1);
    }
    
    /**
     * Get current destruction size for player
     * @param player Player using the hammer
     * @return Destruction size in grid units
     */
    private static int getDestructionSize(Object player) {
        // TODO: Implement player data storage when Player is available
        
        // For MVP, return default size
        return DESTRUCTION_SIZES[2]; // Size 4
    }
    
    /**
     * Set destruction size for player
     * @param player Player using the hammer
     * @param sizeIndex Index in DESTRUCTION_SIZES array
     */
    private static void setDestructionSize(Object player, int sizeIndex) {
        // TODO: Implement player data storage when Player is available
        
        int clampedIndex = Math.max(0, Math.min(sizeIndex, DESTRUCTION_SIZES.length - 1));
        int size = DESTRUCTION_SIZES[clampedIndex];
        System.out.println("Hammer destruction size set to: " + size);
    }
    
    /**
     * Handle mouse wheel scroll for destruction size adjustment
     * @param player Player using the tool
     * @param scrollDelta Scroll wheel delta
     * @return true if scroll was handled
     */
    public static boolean onMouseWheel(Object player, int scrollDelta) {
        int currentIndex = getCurrentSizeIndex(player);
        int newIndex = currentIndex + (scrollDelta > 0 ? 1 : -1);
        
        if (newIndex >= 0 && newIndex < DESTRUCTION_SIZES.length) {
            setDestructionSize(player, newIndex);
            return true;
        }
        
        return false;
    }
    
    /**
     * Get current destruction size index for player
     * @param player Player using the hammer
     * @return Size index
     */
    private static int getCurrentSizeIndex(Object player) {
        // TODO: Implement player data storage when Player is available
        
        // For MVP, return default index
        return 2; // Index for size 4
    }
    
    /**
     * Get tooltip information for the hammer
     * @param stack Item stack
     * @param level World level
     * @param tooltip Tooltip list to add to
     * @param flag Tooltip flag
     */
    public static void appendHoverText(Object stack, Object level, Object tooltip, Object flag) {
        // TODO: Implement when Component and List<Component> are available
        
        System.out.println("NeoHammer tooltip requested");
        
        // Would add:
        // - Current destruction size
        // - Usage instructions (Left: Area, Right: Precision)
        // - Mouse wheel adjustment info
    }
    
    /**
     * Check if the hammer can be used at the given location
     * @param level World level
     * @param pos Block position
     * @param player Player attempting to use
     * @return true if can use
     */
    public static boolean canUseAt(Object level, Object pos, Object player) {
        // TODO: Implement permission checking when classes are available
        
        // For MVP, always allow usage
        return true;
    }
    
    /**
     * Get destruction preview for rendering
     * @param hitResult Ray trace result
     * @param player Player using hammer
     * @return Preview box for rendering
     */
    public static NeoBox getDestructionPreview(Object hitResult, Object player) {
        int size = getDestructionSize(player);
        return getDestructionArea(hitResult, DEFAULT_GRID, size);
    }
    
    /**
     * Calculate durability damage for destruction
     * @param destructionArea Area being destroyed
     * @return Durability damage amount
     */
    public static int calculateDurabilityDamage(NeoBox destructionArea) {
        // Damage based on destruction volume
        int volume = destructionArea.getVolume();
        return Math.max(1, volume / 64); // 1 damage per 64 grid units
    }
}