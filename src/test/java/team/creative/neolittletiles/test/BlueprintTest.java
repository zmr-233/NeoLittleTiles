package team.creative.neolittletiles.test;

import team.creative.neolittletiles.common.converter.NeoBlueprint;
import team.creative.neolittletiles.common.converter.SNBTParser;
import team.creative.neolittletiles.common.grid.NeoGrid;
import team.creative.neolittletiles.common.tile.NeoTile;

import java.util.List;
import java.util.Map;

/**
 * Blueprint NBT conversion test - Tests SNBT parsing and LittleTiles compatibility
 * Validates NBT-based structure conversion without requiring client startup
 */
public class BlueprintTest {
    
    public static void main(String[] args) {
        runAllTests();
    }
    
    public static void runAllTests() {
        System.out.println("=========================================");
        System.out.println("NeoLittleTiles Blueprint NBT Tests");
        System.out.println("=========================================");
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        
        try {
            testSNBTParser();
            System.out.println();
            
            testBlueprintCreation();
            System.out.println();
            
            testBlueprintConversion();
            System.out.println();
            
            testLittleTilesCompatibility();
            System.out.println();
            
            testChiselsBitsCompatibility();
            
        } catch (AssertionError e) {
            System.err.println("BLUEPRINT TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("UNEXPECTED ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println();
        System.out.println("=========================================");
        System.out.println("ALL BLUEPRINT TESTS PASSED SUCCESSFULLY!");
        System.out.println("Total execution time: " + duration + "ms");
        System.out.println("NeoLittleTiles Blueprint NBT system is functional");
        System.out.println("=========================================");
    }
    
    private static void testSNBTParser() {
        System.out.println("=== SNBT Parser Test ===");
        
        // Test basic compound
        String simpleCompound = "{\"key\":\"value\", \"number\":42}";
        Object parsed = SNBTParser.parse(simpleCompound);
        assert parsed instanceof Map : "Should parse compound as Map";
        
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) parsed;
        assert "value".equals(map.get("key")) : "Should parse string value";
        assert Integer.valueOf(42).equals(map.get("number")) : "Should parse number value";
        
        // Test list parsing
        String listTest = "[1, 2, 3, \"text\"]";
        Object listParsed = SNBTParser.parse(listTest);
        assert listParsed instanceof List : "Should parse list";
        
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) listParsed;
        assert list.size() == 4 : "Should have 4 elements";
        assert Integer.valueOf(1).equals(list.get(0)) : "First element should be 1";
        assert "text".equals(list.get(3)) : "Last element should be text";
        
        // Test typed array
        String intArray = "[I;1,2,3,4]";
        Object arrayParsed = SNBTParser.parse(intArray);
        assert arrayParsed instanceof List : "Should parse int array as list";
        
        // Test nested structure
        String nested = "{\"outer\":{\"inner\":\"deep\", \"list\":[1,2,3]}}";
        Object nestedParsed = SNBTParser.parse(nested);
        assert nestedParsed instanceof Map : "Should parse nested structure";
        
        System.out.println("SNBT parser tests passed!");
    }
    
    private static void testBlueprintCreation() {
        System.out.println("=== Blueprint Creation Test ===");
        
        // Create mock blueprint data
        String mockBlueprint = "{\n" +
            "  \"grid\": 16,\n" +
            "  \"min\": [I;0,0,0],\n" +
            "  \"size\": [I;16,16,16],\n" +
            "  \"boxes\": 2,\n" +
            "  \"c\": {\n" +
            "    \"t\": {\n" +
            "      \"minecraft:stone\": [\n" +
            "        [I;-1],\n" +
            "        [I;0,0,0,8,8,8]\n" +
            "      ],\n" +
            "      \"minecraft:dirt\": [\n" +
            "        [I;-65536],\n" +
            "        [I;8,0,0,16,8,8]\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";
        
        NeoBlueprint blueprint = NeoBlueprint.fromSNBT(mockBlueprint);
        assert blueprint != null : "Should create blueprint from SNBT";
        assert blueprint.isValid() : "Blueprint should be valid";
        
        // Test blueprint properties
        NeoGrid sourceGrid = blueprint.getSourceGrid();
        assert sourceGrid.getSize() == 16 : "Source grid should be 16";
        
        int[] minCoords = blueprint.getMinCoords();
        assert minCoords[0] == 0 && minCoords[1] == 0 && minCoords[2] == 0 : "Min coords should be origin";
        
        int[] sizeCoords = blueprint.getSizeCoords();
        assert sizeCoords[0] == 16 && sizeCoords[1] == 16 && sizeCoords[2] == 16 : "Size should be 16x16x16";
        
        System.out.println("Blueprint stats: " + blueprint.getStats());
        System.out.println("Blueprint creation tests passed!");
    }
    
    private static void testBlueprintConversion() {
        System.out.println("=== Blueprint Conversion Test ===");
        
        // Create simplified blueprint for conversion testing
        String testBlueprint = "{\n" +
            "  \"grid\": 16,\n" +
            "  \"c\": {\n" +
            "    \"t\": {\n" +
            "      \"minecraft:stone\": [\n" +
            "        [I;-1],\n" +
            "        [I;0,0,0,8,8,8],\n" +
            "        [I;8,8,8,16,16,16]\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";
        
        NeoBlueprint blueprint = NeoBlueprint.fromSNBT(testBlueprint);
        assert blueprint != null : "Should create test blueprint";
        
        // Convert to NeoTiles
        NeoGrid targetGrid = NeoGrid.GRID_16;
        List<NeoTile> tiles = blueprint.convertToNeoTiles(targetGrid);
        
        assert !tiles.isEmpty() : "Should convert to tiles";
        System.out.println("Converted " + tiles.size() + " tiles from blueprint");
        
        // Verify tile properties
        for (NeoTile tile : tiles) {
            assert tile.getBox().isValid() : "All tiles should have valid boxes";
            assert tile.getState().equals("minecraft:stone") : "All tiles should be stone";
            System.out.println("  Tile: " + tile);
        }
        
        System.out.println("Blueprint conversion tests passed!");
    }
    
    private static void testLittleTilesCompatibility() {
        System.out.println("=== LittleTiles Compatibility Test ===");
        
        // Test LittleTiles export format structure
        String littleTilesExport = "{\n" +
            "  \"grid\": 16,\n" +
            "  \"min\": [I;-8,-8,-8],\n" +
            "  \"size\": [I;16,16,16],\n" +
            "  \"boxes\": 3,\n" +
            "  \"trans\": 0b,\n" +
            "  \"c\": {\n" +
            "    \"t\": {\n" +
            "      \"minecraft:oak_planks\": [\n" +
            "        [I;-1],\n" +
            "        [I;0,0,0,16,4,16],\n" +
            "        [I;0,12,0,16,16,16]\n" +
            "      ],\n" +
            "      \"minecraft:glass\": [\n" +
            "        [I;-16711681],\n" +
            "        [I;2,4,2,14,12,14]\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";
        
        NeoBlueprint blueprint = NeoBlueprint.fromSNBT(littleTilesExport);
        assert blueprint != null : "Should parse LittleTiles export";
        assert blueprint.isValid() : "LittleTiles export should be valid";
        
        // Test conversion
        List<NeoTile> tiles = blueprint.convertToNeoTiles(NeoGrid.GRID_16);
        assert tiles.size() >= 2 : "Should have tiles from both materials";
        
        // Check for different materials
        boolean hasOakPlanks = false;
        boolean hasGlass = false;
        
        for (NeoTile tile : tiles) {
            String state = (String) tile.getState();
            if ("minecraft:oak_planks".equals(state)) {
                hasOakPlanks = true;
            } else if ("minecraft:glass".equals(state)) {
                hasGlass = true;
                assert tile.hasColor() : "Glass tile should have color";
            }
        }
        
        assert hasOakPlanks : "Should have oak planks tiles";
        assert hasGlass : "Should have glass tiles";
        
        System.out.println("LittleTiles compatibility tests passed!");
    }
    
    private static void testChiselsBitsCompatibility() {
        System.out.println("=== Chisels & Bits Compatibility Test ===");
        
        // Test Chisels & Bits format (16x16x16 grid with fine detail)
        String chiselsBitsExport = "{\n" +
            "  \"grid\": 16,\n" +
            "  \"c\": {\n" +
            "    \"t\": {\n" +
            "      \"minecraft:stone\": [\n" +
            "        [I;-1],\n" +
            "        [I;0,0,0,1,1,1],\n" +
            "        [I;1,0,0,2,1,1],\n" +
            "        [I;0,1,0,1,2,1],\n" +
            "        [I;1,1,0,2,2,1]\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";
        
        NeoBlueprint blueprint = NeoBlueprint.fromSNBT(chiselsBitsExport);
        assert blueprint != null : "Should parse Chisels & Bits export";
        
        List<NeoTile> tiles = blueprint.convertToNeoTiles(NeoGrid.GRID_16);
        assert tiles.size() == 4 : "Should have 4 individual 1x1x1 tiles";
        
        // Verify fine detail preservation
        for (NeoTile tile : tiles) {
            int volume = tile.getVolume();
            assert volume == 1 : "Each C&B tile should be 1 grid unit volume";
            
            double percentVolume = tile.getPercentVolume(NeoGrid.GRID_16);
            double expectedPercent = 1.0 / (16 * 16 * 16);
            assert Math.abs(percentVolume - expectedPercent) < 1e-10 : "Percent volume should be tiny";
        }
        
        System.out.println("Chisels & Bits compatibility tests passed!");
    }
}