package team.creative.neolittletiles.test;

import team.creative.neolittletiles.common.grid.NeoGrid;
import team.creative.neolittletiles.common.math.NeoBox;
import team.creative.neolittletiles.common.tile.NeoTile;

/**
 * Unit tests for NeoTile class
 * Tests tile operations without requiring Minecraft client startup
 */
public class NeoTileTest {
    
    public static void main(String[] args) {
        runAllTests();
    }
    
    public static void runAllTests() {
        System.out.println("=== NeoTile Unit Tests ===");
        
        testTileCreation();
        testColorOperations();
        testVolumeCalculations();
        testIntersectionDetection();
        testEqualsAndHashCode();
        
        System.out.println("All NeoTile tests completed successfully!");
    }
    
    private static void testTileCreation() {
        System.out.println("Testing tile creation...");
        
        NeoBox box = new NeoBox(0, 0, 0, 8, 8, 8);
        String state = "minecraft:stone"; // Mock block state
        
        // Test basic tile creation
        NeoTile tile1 = new NeoTile(box, state);
        assert tile1.getBox().equals(box) : "Tile box should match input";
        assert tile1.getState().equals(state) : "Tile state should match input";
        assert tile1.getColor() == 0xFFFFFFFF : "Default color should be white";
        assert !tile1.hasColor() : "Default tile should not have custom color";
        
        // Test tile creation with color
        int customColor = 0xFF0000FF; // Blue
        NeoTile tile2 = new NeoTile(box, state, customColor);
        assert tile2.getColor() == customColor : "Custom color should be preserved";
        assert tile2.hasColor() : "Tile with custom color should report hasColor true";
        
        System.out.println("Tile creation tests passed!");
    }
    
    private static void testColorOperations() {
        System.out.println("Testing color operations...");
        
        NeoBox box = new NeoBox(0, 0, 0, 4, 4, 4);
        String state = "minecraft:wool";
        NeoTile tile = new NeoTile(box, state);
        
        // Test default color
        assert tile.getColor() == 0xFFFFFFFF : "Default color should be white";
        assert !tile.hasColor() : "Default tile should not have custom color";
        
        // Test setting color
        int redColor = 0xFFFF0000;
        tile.setColor(redColor);
        assert tile.getColor() == redColor : "Color should be updated";
        assert tile.hasColor() : "Tile should now have custom color";
        
        // Test setting back to default
        tile.setColor(0xFFFFFFFF);
        assert !tile.hasColor() : "Setting to white should clear hasColor flag";
        
        System.out.println("Color operations tests passed!");
    }
    
    private static void testVolumeCalculations() {
        System.out.println("Testing volume calculations...");
        
        // Test basic volume
        NeoBox box1 = new NeoBox(0, 0, 0, 8, 8, 8);
        String state = "minecraft:stone";
        NeoTile tile1 = new NeoTile(box1, state);
        assert tile1.getVolume() == 512 : "8x8x8 tile should have volume 512";
        
        // Test percentage volume calculation
        NeoGrid grid16 = NeoGrid.GRID_16;
        double percentVolume = tile1.getPercentVolume(grid16);
        double expected = 512.0 / (16 * 16 * 16); // 512 / 4096 = 0.125
        assert Math.abs(percentVolume - expected) < 1e-10 : "Percent volume should be 0.125";
        
        // Test full block volume
        NeoBox fullBox = new NeoBox(0, 0, 0, 16, 16, 16);
        NeoTile fullTile = new NeoTile(fullBox, state);
        double fullPercent = fullTile.getPercentVolume(grid16);
        assert Math.abs(fullPercent - 1.0) < 1e-10 : "Full block should have 100% volume";
        
        System.out.println("Volume calculations tests passed!");
    }
    
    private static void testIntersectionDetection() {
        System.out.println("Testing intersection detection...");
        
        String state = "minecraft:stone";
        
        // Create overlapping tiles
        NeoBox box1 = new NeoBox(0, 0, 0, 10, 10, 10);
        NeoBox box2 = new NeoBox(5, 5, 5, 15, 15, 15);
        NeoBox box3 = new NeoBox(20, 20, 20, 30, 30, 30);
        
        NeoTile tile1 = new NeoTile(box1, state);
        NeoTile tile2 = new NeoTile(box2, state);
        NeoTile tile3 = new NeoTile(box3, state);
        
        // Test tile-tile intersection
        assert tile1.intersects(tile2) : "Overlapping tiles should intersect";
        assert tile2.intersects(tile1) : "Intersection should be symmetric";
        assert !tile1.intersects(tile3) : "Non-overlapping tiles should not intersect";
        
        // Test tile-box intersection
        NeoBox testBox = new NeoBox(8, 8, 8, 18, 18, 18);
        assert tile1.intersects(testBox) : "Tile should intersect with overlapping box";
        assert !tile3.intersects(testBox) : "Tile should not intersect with non-overlapping box";
        
        System.out.println("Intersection detection tests passed!");
    }
    
    private static void testEqualsAndHashCode() {
        System.out.println("Testing equals and hashCode...");
        
        NeoBox box1 = new NeoBox(0, 0, 0, 8, 8, 8);
        NeoBox box2 = new NeoBox(0, 0, 0, 8, 8, 8);
        NeoBox box3 = new NeoBox(1, 0, 0, 9, 8, 8);
        String state1 = "minecraft:stone";
        String state2 = "minecraft:dirt";
        int color1 = 0xFFFF0000;
        int color2 = 0xFF00FF00;
        
        // Test identical tiles
        NeoTile tile1 = new NeoTile(box1, state1, color1);
        NeoTile tile2 = new NeoTile(box2, state1, color1);
        assert tile1.equals(tile2) : "Identical tiles should be equal";
        assert tile1.hashCode() == tile2.hashCode() : "Equal tiles should have same hash code";
        
        // Test different boxes
        NeoTile tile3 = new NeoTile(box3, state1, color1);
        assert !tile1.equals(tile3) : "Tiles with different boxes should not be equal";
        
        // Test different states
        NeoTile tile4 = new NeoTile(box1, state2, color1);
        assert !tile1.equals(tile4) : "Tiles with different states should not be equal";
        
        // Test different colors
        NeoTile tile5 = new NeoTile(box1, state1, color2);
        assert !tile1.equals(tile5) : "Tiles with different colors should not be equal";
        
        // Test self-equality
        assert tile1.equals(tile1) : "Tile should equal itself";
        
        // Test null and different types
        assert !tile1.equals(null) : "Tile should not equal null";
        assert !tile1.equals("not a tile") : "Tile should not equal non-tile object";
        
        System.out.println("Equals and hashCode tests passed!");
    }
}