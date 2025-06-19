package team.creative.neolittletiles.client.render;

import team.creative.neolittletiles.common.grid.NeoGrid;
import team.creative.neolittletiles.common.math.NeoBox;

/**
 * NeoRenderBox - Rendering representation of NeoBox with BlockState
 * 
 * Simplified render box for MVP functionality:
 * - Converts grid coordinates to world coordinates
 * - Stores block state and color information
 * - Provides bounds for rendering pipeline
 * 
 * Based on analysis of LittleRenderBox.java requirements
 */
public class NeoRenderBox {
    
    private final NeoBox box;
    private final NeoGrid grid;
    private final Object blockState; // Placeholder for BlockState
    private final int color;
    
    // Cached world coordinates for performance
    private double minX, minY, minZ;
    private double maxX, maxY, maxZ;
    private boolean worldCoordsCalculated = false;
    
    public NeoRenderBox(NeoBox box, NeoGrid grid, Object blockState) {
        this(box, grid, blockState, 0xFFFFFFFF);
    }
    
    public NeoRenderBox(NeoBox box, NeoGrid grid, Object blockState, int color) {
        this.box = new NeoBox(box);
        this.grid = grid;
        this.blockState = blockState;
        this.color = color;
    }
    
    /**
     * Get the original box in grid coordinates
     * @return Grid coordinate box
     */
    public NeoBox getBox() {
        return box;
    }
    
    /**
     * Get the grid system for coordinate conversion
     * @return Grid instance
     */
    public NeoGrid getGrid() {
        return grid;
    }
    
    /**
     * Get the block state for this render box
     * @return Block state object
     */
    public Object getBlockState() {
        return blockState;
    }
    
    /**
     * Get the color tint for this render box
     * @return ARGB color value
     */
    public int getColor() {
        return color;
    }
    
    /**
     * Check if this render box has a custom color
     * @return true if color is not default white
     */
    public boolean hasColor() {
        return color != 0xFFFFFFFF;
    }
    
    /**
     * Calculate world coordinates from grid coordinates
     */
    private void calculateWorldCoords() {
        if (!worldCoordsCalculated) {
            minX = grid.toWorld(box.minX);
            minY = grid.toWorld(box.minY);
            minZ = grid.toWorld(box.minZ);
            maxX = grid.toWorld(box.maxX);
            maxY = grid.toWorld(box.maxY);
            maxZ = grid.toWorld(box.maxZ);
            worldCoordsCalculated = true;
        }
    }
    
    /**
     * Get minimum X coordinate in world space
     * @return World X coordinate
     */
    public double getMinX() {
        calculateWorldCoords();
        return minX;
    }
    
    /**
     * Get minimum Y coordinate in world space
     * @return World Y coordinate
     */
    public double getMinY() {
        calculateWorldCoords();
        return minY;
    }
    
    /**
     * Get minimum Z coordinate in world space
     * @return World Z coordinate
     */
    public double getMinZ() {
        calculateWorldCoords();
        return minZ;
    }
    
    /**
     * Get maximum X coordinate in world space
     * @return World X coordinate
     */
    public double getMaxX() {
        calculateWorldCoords();
        return maxX;
    }
    
    /**
     * Get maximum Y coordinate in world space
     * @return World Y coordinate
     */
    public double getMaxY() {
        calculateWorldCoords();
        return maxY;
    }
    
    /**
     * Get maximum Z coordinate in world space
     * @return World Z coordinate
     */
    public double getMaxZ() {
        calculateWorldCoords();
        return maxZ;
    }
    
    /**
     * Get the world-space width (X dimension)
     * @return Width in world units
     */
    public double getWidth() {
        calculateWorldCoords();
        return maxX - minX;
    }
    
    /**
     * Get the world-space height (Y dimension)
     * @return Height in world units
     */
    public double getHeight() {
        calculateWorldCoords();
        return maxY - minY;
    }
    
    /**
     * Get the world-space depth (Z dimension)
     * @return Depth in world units
     */
    public double getDepth() {
        calculateWorldCoords();
        return maxZ - minZ;
    }
    
    /**
     * Check if this render box intersects with another in world space
     * @param other Other render box
     * @return true if they intersect
     */
    public boolean intersects(NeoRenderBox other) {
        calculateWorldCoords();
        other.calculateWorldCoords();
        
        return maxX > other.minX && minX < other.maxX &&
               maxY > other.minY && minY < other.maxY &&
               maxZ > other.minZ && minZ < other.maxZ;
    }
    
    /**
     * Get the volume of this render box in world units
     * @return Volume in world space
     */
    public double getWorldVolume() {
        calculateWorldCoords();
        return (maxX - minX) * (maxY - minY) * (maxZ - minZ);
    }
    
    /**
     * Check if this render box is valid for rendering
     * @return true if valid
     */
    public boolean isValid() {
        return box.isValid() && blockState != null;
    }
    
    @Override
    public String toString() {
        calculateWorldCoords();
        return String.format("NeoRenderBox[grid=%.3f,%.3f,%.3f -> %.3f,%.3f,%.3f, state=%s, color=0x%08X]",
                           minX, minY, minZ, maxX, maxY, maxZ, blockState, color);
    }
}