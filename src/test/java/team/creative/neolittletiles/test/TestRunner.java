package team.creative.neolittletiles.test;

import team.creative.neolittletiles.common.grid.NeoGrid;
import team.creative.neolittletiles.common.math.NeoBox;
import team.creative.neolittletiles.common.tile.NeoTile;

/**
 * Main test runner for NeoLittleTiles core classes
 * Runs all unit tests without requiring Minecraft client startup
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("NeoLittleTiles Core Classes Test Suite");
        System.out.println("========================================");
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Run all test classes
            NeoGridTest.runAllTests();
            System.out.println();
            
            NeoBoxTest.runAllTests();
            System.out.println();
            
            NeoTileTest.runAllTests();
            System.out.println();
            
            // Run integration tests
            runIntegrationTests();
            
        } catch (AssertionError e) {
            System.err.println("TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("UNEXPECTED ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("========================================");
        System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
        System.out.println("Total execution time: " + duration + "ms");
        System.out.println("========================================");
    }
    
    private static void runIntegrationTests() {
        System.out.println("=== Integration Tests ===");
        
        testGridBoxTileIntegration();
        testPerformanceBenchmarks();
        
        System.out.println("All integration tests completed successfully!");
    }
    
    private static void testGridBoxTileIntegration() {
        System.out.println("Testing Grid-Box-Tile integration...");
        
        // Create a grid and test tile operations
        NeoGrid grid = NeoGrid.GRID_16;
        NeoBox box = new NeoBox(0, 0, 0, 8, 8, 8);
        NeoTile tile = new NeoTile(box, "minecraft:stone", 0xFFFF0000);
        
        // Test volume calculations across components
        int boxVolume = box.getVolume();
        int tileVolume = tile.getVolume();
        assert boxVolume == tileVolume : "Box and tile volumes should match";
        
        double percentVolume = tile.getPercentVolume(grid);
        double expectedPercent = boxVolume / (double) (grid.getSize() * grid.getSize() * grid.getSize());
        assert Math.abs(percentVolume - expectedPercent) < 1e-10 : "Percent volume calculation should be correct";
        
        // Test coordinate conversions
        double worldX = grid.toWorld(8);
        int gridX = grid.toGrid(worldX);
        assert gridX == 8 : "Grid conversion should be reversible";
        
        System.out.println("Grid-Box-Tile integration tests passed!");
    }
    
    private static void testPerformanceBenchmarks() {
        System.out.println("Running performance benchmarks...");
        
        // Benchmark grid operations
        long start = System.nanoTime();
        NeoGrid grid = NeoGrid.GRID_16;
        for (int i = 0; i < 10000; i++) {
            grid.toGrid(i * 0.1);
            grid.toWorld(i);
            grid.toBlockOffset(i);
            grid.toGridOffset(i);
        }
        long gridTime = System.nanoTime() - start;
        
        // Benchmark box operations
        start = System.nanoTime();
        NeoBox box1 = new NeoBox(0, 0, 0, 10, 10, 10);
        NeoBox box2 = new NeoBox(5, 5, 5, 15, 15, 15);
        for (int i = 0; i < 10000; i++) {
            box1.intersects(box2);
            box1.getVolume();
            box1.contains(box2);
        }
        long boxTime = System.nanoTime() - start;
        
        // Benchmark tile operations
        start = System.nanoTime();
        NeoTile tile1 = new NeoTile(box1, "minecraft:stone");
        NeoTile tile2 = new NeoTile(box2, "minecraft:dirt");
        for (int i = 0; i < 10000; i++) {
            tile1.intersects(tile2);
            tile1.getVolume();
            tile1.getPercentVolume(grid);
        }
        long tileTime = System.nanoTime() - start;
        
        System.out.println("Performance benchmarks (10,000 operations each):");
        System.out.println("- Grid operations: " + (gridTime / 1_000_000) + "ms");
        System.out.println("- Box operations: " + (boxTime / 1_000_000) + "ms");
        System.out.println("- Tile operations: " + (tileTime / 1_000_000) + "ms");
        
        // Verify performance is reasonable (should complete in under 100ms each)
        assert gridTime < 100_000_000 : "Grid operations should be fast";
        assert boxTime < 100_000_000 : "Box operations should be fast";
        assert tileTime < 100_000_000 : "Tile operations should be fast";
        
        System.out.println("Performance benchmarks passed!");
    }
}