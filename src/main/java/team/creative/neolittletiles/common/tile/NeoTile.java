package team.creative.neolittletiles.common.tile;

import team.creative.neolittletiles.common.grid.NeoGrid;
import team.creative.neolittletiles.common.math.NeoBox;
// Placeholder for BlockState - will be replaced when Minecraft dependencies are properly resolved

/**
 * NeoTile - Redesigned tile primitive for improved performance
 * 
 * Key improvements over LittleTile:
 * - Flattened hierarchy structure
 * - Direct box storage without indirection
 * - Simplified state management
 * - Efficient serialization format
 * 
 * Based on analysis from LOCAL/analysis.txt lines 16-22
 */
public class NeoTile {
    
    private final NeoBox box;
    private final Object state; // Placeholder for BlockState
    private int color = 0xFFFFFFFF; // ARGB format, default white
    
    public NeoTile(NeoBox box, Object state) {
        this.box = new NeoBox(box);
        this.state = state;
    }
    
    public NeoTile(NeoBox box, Object state, int color) {
        this.box = new NeoBox(box);
        this.state = state;
        this.color = color;
    }
    
    public NeoBox getBox() {
        return box;
    }
    
    public Object getState() {
        return state;
    }
    
    public int getColor() {
        return color;
    }
    
    public void setColor(int color) {
        this.color = color;
    }
    
    public boolean hasColor() {
        return color != 0xFFFFFFFF;
    }
    
    // Calculate volume in grid units
    public int getVolume() {
        return box.getVolume();
    }
    
    // Calculate percentage volume relative to a full block
    public double getPercentVolume(NeoGrid grid) {
        return getVolume() / (double) (grid.getSize() * grid.getSize() * grid.getSize());
    }
    
    // Check if tile intersects with another
    public boolean intersects(NeoTile other) {
        return box.intersects(other.box);
    }
    
    // Check if tile intersects with a box
    public boolean intersects(NeoBox other) {
        return box.intersects(other);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NeoTile other)) return false;
        return box.equals(other.box) && state.equals(other.state) && color == other.color;
    }
    
    @Override
    public int hashCode() {
        return box.hashCode() ^ state.hashCode() ^ color;
    }
    
    @Override
    public String toString() {
        return String.format("NeoTile[%s, %s, color=0x%08X]", box, state, color);
    }
}