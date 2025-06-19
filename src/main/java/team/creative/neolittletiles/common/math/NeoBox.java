package team.creative.neolittletiles.common.math;

/**
 * NeoBox - Redesigned 3D box primitive for improved performance
 * 
 * Key improvements over LittleBox:
 * - Simplified coordinate operations
 * - Reduced method complexity
 * - Optimized intersection testing
 * - Efficient volume calculations
 * 
 * Based on analysis from LOCAL/analysis.txt lines 143-149
 */
public class NeoBox {
    
    public int minX, minY, minZ;
    public int maxX, maxY, maxZ;
    
    public NeoBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }
    
    public NeoBox(NeoBox other) {
        this(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }
    
    // Efficient volume calculation
    public int getVolume() {
        return (maxX - minX) * (maxY - minY) * (maxZ - minZ);
    }
    
    // Fast intersection test
    public boolean intersects(NeoBox other) {
        return maxX > other.minX && minX < other.maxX &&
               maxY > other.minY && minY < other.maxY &&
               maxZ > other.minZ && minZ < other.maxZ;
    }
    
    // Check if this box contains another box
    public boolean contains(NeoBox other) {
        return minX <= other.minX && maxX >= other.maxX &&
               minY <= other.minY && maxY >= other.maxY &&
               minZ <= other.minZ && maxZ >= other.maxZ;
    }
    
    // Expand to include another box
    public void union(NeoBox other) {
        minX = Math.min(minX, other.minX);
        minY = Math.min(minY, other.minY);
        minZ = Math.min(minZ, other.minZ);
        maxX = Math.max(maxX, other.maxX);
        maxY = Math.max(maxY, other.maxY);
        maxZ = Math.max(maxZ, other.maxZ);
    }
    
    // Calculate intersection with another box
    public NeoBox intersection(NeoBox other) {
        int newMinX = Math.max(minX, other.minX);
        int newMinY = Math.max(minY, other.minY);
        int newMinZ = Math.max(minZ, other.minZ);
        int newMaxX = Math.min(maxX, other.maxX);
        int newMaxY = Math.min(maxY, other.maxY);
        int newMaxZ = Math.min(maxZ, other.maxZ);
        
        if (newMinX < newMaxX && newMinY < newMaxY && newMinZ < newMaxZ) {
            return new NeoBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
        }
        return null; // No intersection
    }
    
    public boolean isValid() {
        return maxX > minX && maxY > minY && maxZ > minZ;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NeoBox other)) return false;
        return minX == other.minX && minY == other.minY && minZ == other.minZ &&
               maxX == other.maxX && maxY == other.maxY && maxZ == other.maxZ;
    }
    
    @Override
    public int hashCode() {
        return minX ^ minY ^ minZ ^ maxX ^ maxY ^ maxZ;
    }
    
    @Override
    public String toString() {
        return String.format("NeoBox[%d,%d,%d -> %d,%d,%d]", minX, minY, minZ, maxX, maxY, maxZ);
    }
}