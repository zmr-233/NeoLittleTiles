package team.creative.neolittletiles.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import team.creative.neolittletiles.common.action.NeoAction;
import team.creative.neolittletiles.common.action.NeoPlaceAction;
import team.creative.neolittletiles.common.action.NeoDestroyAction;
import team.creative.neolittletiles.common.block.NeoTilesBlock;
import team.creative.neolittletiles.common.block.NeoTilesBlockEntity;
import team.creative.neolittletiles.common.grid.NeoGrid;
import team.creative.neolittletiles.common.gui.NeoConfigGuiLayer;
import team.creative.neolittletiles.common.math.NeoBox;
import team.creative.neolittletiles.common.tile.NeoTile;

/**
 * NeoChisel - Simplified chisel tool for tile placement and destruction
 * 
 * MVP chisel implementation:
 * - Left click to place tiles
 * - Right click to destroy tiles
 * - Uses current held block as tile material
 * - Fixed grid size for simplicity
 * 
 * Based on analysis of ItemLittleChisel.java and LittleToolShaper.java
 */
public class NeoChisel extends Item {
    
    public static final String ITEM_ID = "neochisel";
    private static final NeoGrid DEFAULT_GRID = NeoGrid.GRID_16;
    
    public NeoChisel(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        
        if (!level.isClientSide && player != null) {
            // Check for sneak-click to open configuration GUI
            if (player.isShiftKeyDown()) {
                openConfigurationGUI(player);
                return InteractionResult.SUCCESS;
            }
            
            // Create a small tile placement
            NeoBox placementBox = getPlacementBox(context);
            BlockState blockState = Blocks.STONE.defaultBlockState(); // Default to stone for MVP
            
            // Ensure we have a NeoTiles block at this position
            if (!(level.getBlockState(pos).getBlock() instanceof NeoTilesBlock)) {
                level.setBlock(pos, team.creative.neolittletiles.NeoLittleTilesRegistry.getNeoTilesBlock().defaultBlockState(), 3);
            }
            
            // Add tile to block entity
            NeoTilesBlockEntity blockEntity = NeoTilesBlock.getBlockEntity(level, pos);
            if (blockEntity != null) {
                NeoTile tile = new NeoTile(placementBox, blockState);
                boolean success = blockEntity.addTile(tile);
                
                System.out.println("NeoChisel placement: " + (success ? "SUCCESS" : "FAIL"));
                System.out.println("Placed tile: " + tile);
                
                return success ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            }
        }
        
        return InteractionResult.PASS;
    }
    
    /**
     * Get placement box from context
     * @param context Use context with hit result
     * @return Placement box for tile
     */
    private NeoBox getPlacementBox(UseOnContext context) {
        // For MVP, create a quarter-block tile at the hit location
        int size = DEFAULT_GRID.getSize() / 4; // 4x4x4 in a 16x16x16 grid
        
        // Get the face that was clicked and offset accordingly
        var face = context.getClickedFace();
        var hitPos = context.getClickLocation();
        var blockPos = context.getClickedPos();
        
        // Calculate relative position within the block (0-15 in grid coordinates)
        int relX = (int) ((hitPos.x - blockPos.getX()) * DEFAULT_GRID.getSize());
        int relY = (int) ((hitPos.y - blockPos.getY()) * DEFAULT_GRID.getSize());
        int relZ = (int) ((hitPos.z - blockPos.getZ()) * DEFAULT_GRID.getSize());
        
        // Clamp to valid range
        relX = Math.max(0, Math.min(DEFAULT_GRID.getSize() - size, relX));
        relY = Math.max(0, Math.min(DEFAULT_GRID.getSize() - size, relY));
        relZ = Math.max(0, Math.min(DEFAULT_GRID.getSize() - size, relZ));
        
        return new NeoBox(relX, relY, relZ, relX + size, relY + size, relZ + size);
    }
    
    /**
     * Handle left click - place tiles
     * @param level World level
     * @param player Player using the tool
     * @param hand Player hand (main/offhand)
     * @param hitResult Ray trace hit result
     * @return Interaction result
     */
    public static Object onLeftClick(Object level, Object player, Object hand, Object hitResult) {
        // TODO: Implement when proper classes are available
        
        System.out.println("NeoChisel left click - placing tile");
        
        // Get placement parameters
        NeoBox placementBox = getPlacementBox(hitResult, DEFAULT_GRID);
        Object blockState = getHeldBlockState(player, hand);
        int color = getChiselColor(player);
        
        // Create and execute placement action
        NeoPlaceAction placeAction = new NeoPlaceAction(placementBox, blockState, color);
        NeoAction.Result result = placeAction.execute(player);
        
        System.out.println("Placement result: " + result);
        
        // TODO: Return proper InteractionResult when available
        return result == NeoAction.Result.SUCCESS ? "SUCCESS" : "FAIL";
    }
    
    /**
     * Handle right click - destroy tiles  
     * @param level World level
     * @param player Player using the tool
     * @param hand Player hand (main/offhand)
     * @param hitResult Ray trace hit result
     * @return Interaction result
     */
    public static Object onRightClick(Object level, Object player, Object hand, Object hitResult) {
        // TODO: Implement when proper classes are available
        
        System.out.println("NeoChisel right click - destroying tiles");
        
        // Get destruction parameters
        NeoBox destructionBox = getDestructionBox(hitResult, DEFAULT_GRID);
        
        // Create and execute destruction action
        NeoDestroyAction destroyAction = new NeoDestroyAction(destructionBox);
        NeoAction.Result result = destroyAction.execute(player);
        
        System.out.println("Destruction result: " + result);
        
        // TODO: Return proper InteractionResult when available
        return result == NeoAction.Result.SUCCESS ? "SUCCESS" : "FAIL";
    }
    
    /**
     * Calculate placement box from hit result
     * @param hitResult Ray trace result
     * @param grid Grid system to use
     * @return Box for tile placement
     */
    private static NeoBox getPlacementBox(Object hitResult, NeoGrid grid) {
        // TODO: Implement proper ray trace calculation when BlockHitResult is available
        
        // For MVP, create a simple 1x1x1 grid unit box
        int size = grid.getSize() / 4; // Quarter block by default
        return new NeoBox(0, 0, 0, size, size, size);
    }
    
    /**
     * Calculate destruction box from hit result  
     * @param hitResult Ray trace result
     * @param grid Grid system to use
     * @return Box for tile destruction
     */
    private static NeoBox getDestructionBox(Object hitResult, NeoGrid grid) {
        // TODO: Implement proper ray trace calculation when BlockHitResult is available
        
        // For MVP, create a simple 1x1x1 grid unit box
        int size = grid.getSize() / 4; // Quarter block by default
        return new NeoBox(0, 0, 0, size, size, size);
    }
    
    /**
     * Get the block state from player's held item
     * @param player Player holding the chisel
     * @param hand Hand holding the chisel
     * @return Block state to place
     */
    private static Object getHeldBlockState(Object player, Object hand) {
        // TODO: Implement when Player and ItemStack are available
        
        // For MVP, return a default block state
        return "minecraft:stone";
    }
    
    /**
     * Get the chisel color from player data
     * @param player Player using the chisel
     * @return Color for placed tiles
     */
    private static int getChiselColor(Object player) {
        // TODO: Implement chisel color system when Player is available
        
        // For MVP, return white (no color)
        return 0xFFFFFFFF;
    }
    
    /**
     * Handle mouse wheel scroll for grid size adjustment
     * @param player Player using the tool
     * @param scrollDelta Scroll wheel delta
     * @return true if scroll was handled
     */
    public static boolean onMouseWheel(Object player, int scrollDelta) {
        // TODO: Implement grid size adjustment
        
        System.out.println("NeoChisel mouse wheel: " + scrollDelta);
        
        // For MVP, just log the action
        return true;
    }
    
    /**
     * Get tooltip information for the chisel
     * @param stack Item stack
     * @param level World level
     * @param tooltip Tooltip list to add to
     * @param flag Tooltip flag
     */
    public static void appendHoverText(Object stack, Object level, Object tooltip, Object flag) {
        // TODO: Implement when Component and List<Component> are available
        
        System.out.println("NeoChisel tooltip requested");
        
        // Would add:
        // - Current grid size
        // - Current block type
        // - Current color
        // - Usage instructions
    }
    
    /**
     * Check if the chisel can be used at the given location
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
     * Open configuration GUI for this tool
     * @param player Player to open GUI for
     */
    private void openConfigurationGUI(Player player) {
        // TODO: Implement proper GUI opening when CreativeCore integration is ready
        System.out.println("Opening NeoChisel configuration GUI for player: " + player.getName().getString());
        
        // For MVP, just log the action
        NeoConfigGuiLayer configGui = NeoConfigGuiLayer.createForTool(ITEM_ID, player);
        configGui.show();
    }
}