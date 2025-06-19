package team.creative.neolittletiles.test;

import team.creative.neolittletiles.common.grid.NeoGrid;

/**
 * Unit tests for NeoGrid class
 * Tests core functionality without requiring Minecraft client startup
 */
public class NeoGridTest {
    
    public static void main(String[] args) {
        runAllTests();
    }
    
    public static void runAllTests() {
        System.out.println("=== NeoGrid Unit Tests ===");
        
        testGridCreation();
        testCoordinateConversion();
        testBitOperations();
        testGridConversion();
        testCommonGridConstants();
        
        System.out.println("All NeoGrid tests completed successfully!");
    }
    
    private static void testGridCreation() {
        System.out.println("Testing grid creation...");
        
        // Test valid power-of-2 sizes
        NeoGrid grid16 = new NeoGrid(16);
        assert grid16.getSize() == 16 : "Grid size should be 16";
        assert Math.abs(grid16.getPixelSize() - 1.0/16) < 1e-10 : "Pixel size should be 1/16";
        
        // Test invalid sizes (should throw exception)
        try {
            new NeoGrid(15); // Not power of 2
            assert false : "Should throw exception for non-power-of-2 size";
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            new NeoGrid(0); // Zero size
            assert false : "Should throw exception for zero size";
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        System.out.println("Grid creation tests passed!");
    }
    
    private static void testCoordinateConversion() {
        System.out.println("Testing coordinate conversion...");
        
        NeoGrid grid = new NeoGrid(16);
        
        // Test world to grid conversion
        assert grid.toGrid(0.0) == 0 : "0.0 should convert to 0";
        assert grid.toGrid(1.0) == 16 : "1.0 should convert to 16";
        assert grid.toGrid(0.5) == 8 : "0.5 should convert to 8";
        
        // Test grid to world conversion
        assert Math.abs(grid.toWorld(0) - 0.0) < 1e-10 : "0 should convert to 0.0";
        assert Math.abs(grid.toWorld(16) - 1.0) < 1e-10 : "16 should convert to 1.0";
        assert Math.abs(grid.toWorld(8) - 0.5) < 1e-10 : "8 should convert to 0.5";
        
        // Test float conversion
        assert Math.abs(grid.toWorldF(8) - 0.5f) < 1e-7f : "8 should convert to 0.5f";
        
        System.out.println("Coordinate conversion tests passed!");
    }
    
    private static void testBitOperations() {
        System.out.println("Testing bit operations...");
        
        NeoGrid grid = new NeoGrid(16); // shift = 4
        
        // Test block offset calculation
        assert grid.toBlockOffset(16) == 1 : "Grid 16 should be in block 1";
        assert grid.toBlockOffset(32) == 2 : "Grid 32 should be in block 2";
        assert grid.toBlockOffset(15) == 0 : "Grid 15 should be in block 0";
        
        // Test grid offset calculation
        assert grid.toGridOffset(16) == 0 : "Grid 16 should have offset 0";
        assert grid.toGridOffset(17) == 1 : "Grid 17 should have offset 1";
        assert grid.toGridOffset(31) == 15 : "Grid 31 should have offset 15";
        
        System.out.println("Bit operations tests passed!");
    }
    
    private static void testGridConversion() {
        System.out.println("Testing grid conversion...");
        
        NeoGrid grid16 = new NeoGrid(16);
        NeoGrid grid32 = new NeoGrid(32);
        NeoGrid grid8 = new NeoGrid(8);
        
        // Test upscaling (16 -> 32)
        assert grid16.convertGrid(16, grid32) == 32 : "Grid 16 should convert to 32 in larger grid";
        assert grid16.convertGrid(8, grid32) == 16 : "Grid 8 should convert to 16 in larger grid";
        
        // Test downscaling (16 -> 8)
        assert grid16.convertGrid(16, grid8) == 8 : "Grid 16 should convert to 8 in smaller grid";
        assert grid16.convertGrid(32, grid8) == 16 : "Grid 32 should convert to 16 in smaller grid";
        
        // Test same size conversion
        assert grid16.convertGrid(16, grid16) == 16 : "Same grid should return same value";
        
        System.out.println("Grid conversion tests passed!");
    }
    
    private static void testCommonGridConstants() {
        System.out.println("Testing common grid constants...");
        
        assert NeoGrid.GRID_1.getSize() == 1 : "GRID_1 should have size 1";
        assert NeoGrid.GRID_16.getSize() == 16 : "GRID_16 should have size 16";
        assert NeoGrid.GRID_4096.getSize() == 4096 : "GRID_4096 should have size 4096";
        
        // Test that constants are properly initialized
        assert NeoGrid.GRID_16.getPixelSize() == 1.0/16 : "GRID_16 should have correct pixel size";
        
        System.out.println("Common grid constants tests passed!");
    }
}