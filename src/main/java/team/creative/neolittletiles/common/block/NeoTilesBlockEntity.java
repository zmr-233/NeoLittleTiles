package team.creative.neolittletiles.common.block;

import team.creative.neolittletiles.common.grid.NeoGrid;
import team.creative.neolittletiles.common.math.NeoBox;
import team.creative.neolittletiles.common.tile.NeoTile;

import java.util.ArrayList;
import java.util.List;

/**
 * NeoTilesBlockEntity - Block entity for storing and managing tiles
 * 
 * Simplified block entity for MVP functionality:
 * - Stores list of NeoTiles
 * - Handles tile addition/removal
 * - Provides tile querying capabilities
 * - Manages serialization (placeholder)
 * 
 * Based on analysis of BlockParentCollection storage requirements
 */
public class NeoTilesBlockEntity {
    
    private final List<NeoTile> tiles = new ArrayList<>();
    private NeoGrid grid = NeoGrid.GRID_16; // Default grid
    private boolean needsUpdate = true;
    
    // Placeholder for BlockEntity - will be replaced when Minecraft dependencies are resolved
    
    /**
     * Add a tile to this block entity
     * @param tile The tile to add
     * @return true if successfully added
     */
    public boolean addTile(NeoTile tile) {
        if (tile == null || !tile.getBox().isValid()) {
            return false;
        }
        
        // Check for overlaps with existing tiles
        for (NeoTile existing : tiles) {
            if (existing.intersects(tile)) {
                System.out.println("Warning: Tile overlap detected, merging not implemented in MVP");
                // TODO: Implement proper tile merging/splitting
            }
        }
        
        tiles.add(tile);
        needsUpdate = true;
        markDirty();
        return true;
    }
    
    /**
     * Remove tiles that intersect with the given box
     * @param box The area to clear
     * @return List of removed tiles
     */
    public List<NeoTile> removeTiles(NeoBox box) {
        List<NeoTile> removed = new ArrayList<>();
        tiles.removeIf(tile -> {
            if (tile.intersects(box)) {
                removed.add(tile);
                return true;
            }
            return false;
        });
        
        if (!removed.isEmpty()) {
            needsUpdate = true;
            markDirty();
        }
        
        return removed;
    }
    
    /**
     * Get all tiles in this block entity
     * @return Read-only list of tiles
     */
    public List<NeoTile> getTiles() {
        return new ArrayList<>(tiles);
    }
    
    /**
     * Get tiles that intersect with the given box
     * @param box The area to query
     * @return List of intersecting tiles
     */
    public List<NeoTile> getTiles(NeoBox box) {
        List<NeoTile> result = new ArrayList<>();
        for (NeoTile tile : tiles) {
            if (tile.intersects(box)) {
                result.add(tile);
            }
        }
        return result;
    }
    
    /**
     * Check if any tiles exist in this block entity
     * @return true if has tiles
     */
    public boolean hasTiles() {
        return !tiles.isEmpty();
    }
    
    /**
     * Get the total number of tiles
     * @return Tile count
     */
    public int getTileCount() {
        return tiles.size();
    }
    
    /**
     * Get the grid system used by this block entity
     * @return Current grid
     */
    public NeoGrid getGrid() {
        return grid;
    }
    
    /**
     * Set the grid system for this block entity
     * @param grid New grid system
     */
    public void setGrid(NeoGrid grid) {
        if (grid != null && !grid.equals(this.grid)) {
            this.grid = grid;
            needsUpdate = true;
            markDirty();
        }
    }
    
    /**
     * Calculate total volume of all tiles
     * @return Total volume in grid units
     */
    public int getTotalVolume() {
        return tiles.stream().mapToInt(NeoTile::getVolume).sum();
    }
    
    /**
     * Check if this block entity needs rendering update
     * @return true if needs update
     */
    public boolean needsRenderUpdate() {
        return needsUpdate;
    }
    
    /**
     * Mark render update as completed
     */
    public void clearRenderUpdate() {
        needsUpdate = false;
    }
    
    /**
     * Mark this block entity as dirty for saving
     */
    private void markDirty() {
        // TODO: Call proper setChanged() when BlockEntity is available
        System.out.println("NeoTilesBlockEntity marked dirty");
    }
    
    /**
     * Serialize tiles to NBT format
     * @return NBT compound data
     */
    public Object saveToNBT() {
        // TODO: Implement NBT serialization when CompoundTag is available
        System.out.println("Saving " + tiles.size() + " tiles to NBT");
        return null;
    }
    
    /**
     * Deserialize tiles from NBT format
     * @param nbt NBT compound data
     */
    public void loadFromNBT(Object nbt) {
        // TODO: Implement NBT deserialization when CompoundTag is available
        System.out.println("Loading tiles from NBT");
        needsUpdate = true;
    }
    
    @Override
    public String toString() {
        return String.format("NeoTilesBlockEntity[tiles=%d, grid=%s, needsUpdate=%s]", 
                           tiles.size(), grid, needsUpdate);
    }
}