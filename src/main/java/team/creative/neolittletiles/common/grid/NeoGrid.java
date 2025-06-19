package team.creative.neolittletiles.common.grid;

/**
 * NeoGrid - Redesigned grid system for improved performance
 * 
 * Key improvements over LittleGrid:
 * - Constant-time coordinate operations
 * - Efficient bit operations for power-of-2 grids
 * - Reduced memory allocation
 * - Simplified conversion methods
 * 
 * Based on analysis from LOCAL/analysis.txt lines 116-122
 */
public class NeoGrid {
    
    public static final int BASE = 2;
    public static final int DEFAULT_SIZE = 16;
    
    private final int size;
    private final int shift; // log2(size) for bit operations
    private final double pixelSize;
    private final float pixelSizeF;
    
    public NeoGrid(int size) {
        if (!isPowerOfTwo(size)) {
            throw new IllegalArgumentException("Grid size must be power of 2: " + size);
        }
        this.size = size;
        this.shift = Integer.numberOfTrailingZeros(size);
        this.pixelSize = 1.0 / size;
        this.pixelSizeF = (float) pixelSize;
    }
    
    public int getSize() {
        return size;
    }
    
    public double getPixelSize() {
        return pixelSize;
    }
    
    public float getPixelSizeF() {
        return pixelSizeF;
    }
    
    // Efficient grid coordinate conversion using bit shifts
    public int toGrid(double pos) {
        return (int) Math.floor(pos * size);
    }
    
    public double toWorld(int grid) {
        return grid * pixelSize;
    }
    
    public float toWorldF(int grid) {
        return grid * pixelSizeF;
    }
    
    // Fast block offset calculation using bit operations
    public int toBlockOffset(int grid) {
        return grid >> shift;
    }
    
    public int toGridOffset(int grid) {
        return grid & (size - 1);
    }
    
    // Grid conversion methods for efficiency
    public NeoGrid convertTo(int newSize) {
        return new NeoGrid(newSize);
    }
    
    public int convertGrid(int grid, NeoGrid target) {
        if (size == target.size) return grid;
        if (size > target.size) {
            return grid >> (shift - target.shift);
        } else {
            return grid << (target.shift - shift);
        }
    }
    
    // Common grid sizes as constants for performance
    public static final NeoGrid GRID_1 = new NeoGrid(1);
    public static final NeoGrid GRID_2 = new NeoGrid(2);
    public static final NeoGrid GRID_4 = new NeoGrid(4);
    public static final NeoGrid GRID_8 = new NeoGrid(8);
    public static final NeoGrid GRID_16 = new NeoGrid(16);
    public static final NeoGrid GRID_32 = new NeoGrid(32);
    public static final NeoGrid GRID_64 = new NeoGrid(64);
    public static final NeoGrid GRID_128 = new NeoGrid(128);
    public static final NeoGrid GRID_256 = new NeoGrid(256);
    public static final NeoGrid GRID_512 = new NeoGrid(512);
    public static final NeoGrid GRID_1024 = new NeoGrid(1024);
    public static final NeoGrid GRID_2048 = new NeoGrid(2048);
    public static final NeoGrid GRID_4096 = new NeoGrid(4096);
    
    private static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    @Override
    public String toString() {
        return "NeoGrid[" + size + "]";
    }
}