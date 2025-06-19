package team.creative.neolittletiles.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.creative.neolittletiles.NeoLittleTilesRegistry;
import team.creative.neolittletiles.common.grid.NeoGrid;
import team.creative.neolittletiles.common.math.NeoBox;
import team.creative.neolittletiles.common.tile.NeoTile;

import javax.annotation.Nullable;
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
public class NeoTilesBlockEntity extends BlockEntity {
    
    private final List<NeoTile> tiles = new ArrayList<>();
    private NeoGrid grid = NeoGrid.GRID_16; // Default grid
    private boolean needsUpdate = true;
    
    public NeoTilesBlockEntity(BlockPos pos, BlockState blockState) {
        super(NeoLittleTilesRegistry.getNeoTilesBlockEntityType(), pos, blockState);
    }
    
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
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putInt("GridSize", grid.getSize());
        nbt.putInt("TileCount", tiles.size());
        System.out.println("Saving " + tiles.size() + " tiles to NBT (grid: " + grid.getSize() + ")");
        // TODO: Serialize individual tiles when needed for persistence
    }
    
    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        int gridSize = nbt.getInt("GridSize");
        if (gridSize > 0) {
            grid = NeoGrid.getBySize(gridSize);
        }
        int tileCount = nbt.getInt("TileCount");
        System.out.println("Loading " + tileCount + " tiles from NBT (grid: " + grid.getSize() + ")");
        needsUpdate = true;
        // TODO: Deserialize individual tiles when needed for persistence
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt, registries);
        return nbt;
    }
    
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (pkt.getTag() != null) {
            loadAdditional(pkt.getTag(), lookupProvider);
        }
    }
    
    @Override
    public String toString() {
        return String.format("NeoTilesBlockEntity[tiles=%d, grid=%s, needsUpdate=%s]", 
                           tiles.size(), grid, needsUpdate);
    }
}