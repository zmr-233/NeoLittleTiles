package team.creative.neolittletiles.common.converter;

import team.creative.neolittletiles.common.grid.NeoGrid;
import team.creative.neolittletiles.common.math.NeoBox;
import team.creative.neolittletiles.common.tile.NeoTile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NeoBlueprint - NBT-based blueprint system for LittleTiles compatibility
 * 
 * Implements NBT parsing for LittleTiles Blueprint format:
 * - SNBT string parsing to structured data
 * - Material-based tile organization
 * - Color encoding/decoding (32-bit RGBA)
 * - Grid coordinate conversion
 * - Chisels & Bits compatibility
 * 
 * Based on analysis of LittleTiles Blueprint NBT structure
 */
public class NeoBlueprint {
    
    // NBT structure keys from LittleTiles format
    private static final String KEY_CONTENT = "c";
    private static final String KEY_TILES = "t";
    private static final String KEY_CHILDREN = "c";
    private static final String KEY_STRUCTURE = "s";
    private static final String KEY_GRID = "grid";
    private static final String KEY_MIN = "min";
    private static final String KEY_SIZE = "size";
    private static final String KEY_BOXES = "boxes";
    private static final String KEY_TRANSLUCENT = "trans";
    
    // Default grid for LittleTiles compatibility
    private static final int DEFAULT_LITTLETILES_GRID = 16;
    
    private Map<String, Object> nbtData;
    private NeoGrid sourceGrid;
    private int[] minCoords;
    private int[] sizeCoords;
    
    /**
     * Create blueprint from SNBT string (LittleTiles export format)
     * @param snbtString SNBT format string from LittleTiles
     * @return Parsed blueprint or null if invalid
     */
    public static NeoBlueprint fromSNBT(String snbtString) {
        try {
            Map<String, Object> nbtData = parseSNBT(snbtString);
            return new NeoBlueprint(nbtData);
        } catch (Exception e) {
            System.err.println("Failed to parse SNBT: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Create blueprint from NBT-like data structure
     * @param nbtData Parsed NBT data
     */
    public NeoBlueprint(Map<String, Object> nbtData) {
        this.nbtData = nbtData;
        this.sourceGrid = extractGrid();
        this.minCoords = extractMinCoords();
        this.sizeCoords = extractSizeCoords();
    }
    
    /**
     * Convert blueprint to NeoTiles
     * @param targetGrid Target grid system for conversion
     * @return List of converted NeoTiles
     */
    public List<NeoTile> convertToNeoTiles(NeoGrid targetGrid) {
        List<NeoTile> tiles = new ArrayList<>();
        
        // Get content section
        Map<String, Object> content = getContentSection();
        if (content == null) {
            System.out.println("No content section found in blueprint");
            return tiles;
        }
        
        // Get tiles section
        Map<String, Object> tilesData = getMapValue(content, KEY_TILES);
        if (tilesData == null) {
            System.out.println("No tiles data found in blueprint");
            return tiles;
        }
        
        // Convert each material group
        for (Map.Entry<String, Object> materialEntry : tilesData.entrySet()) {
            String blockState = materialEntry.getKey();
            List<Object> tileArrays = getListValue(materialEntry.getValue());
            
            if (tileArrays != null) {
                tiles.addAll(convertMaterialGroup(blockState, tileArrays, targetGrid));
            }
        }
        
        System.out.println("Converted blueprint: " + tiles.size() + " tiles from " + 
                         tilesData.size() + " materials");
        return tiles;
    }
    
    /**
     * Convert tiles for a specific material
     * @param blockState Material block state
     * @param tileArrays List of tile data arrays
     * @param targetGrid Target grid system
     * @return List of converted tiles
     */
    private List<NeoTile> convertMaterialGroup(String blockState, List<Object> tileArrays, 
                                              NeoGrid targetGrid) {
        List<NeoTile> tiles = new ArrayList<>();
        int currentColor = 0xFFFFFFFF; // Default white
        
        for (Object arrayData : tileArrays) {
            List<Integer> intArray = getIntListValue(arrayData);
            if (intArray == null || intArray.isEmpty()) continue;
            
            // First array is color marker
            if (intArray.size() == 1) {
                currentColor = intArray.get(0);
                continue;
            }
            
            // Box arrays have 6+ elements
            if (intArray.size() >= 6) {
                NeoBox box = convertBox(intArray, targetGrid);
                if (box != null && box.isValid()) {
                    NeoTile tile = new NeoTile(box, blockState, currentColor);
                    tiles.add(tile);
                }
            }
        }
        
        return tiles;
    }
    
    /**
     * Convert box coordinates from source to target grid
     * @param coords Box coordinates [minX, minY, minZ, maxX, maxY, maxZ, ...]
     * @param targetGrid Target grid system
     * @return Converted NeoBox
     */
    private NeoBox convertBox(List<Integer> coords, NeoGrid targetGrid) {
        if (coords.size() < 6) return null;
        
        int minX = coords.get(0);
        int minY = coords.get(1);
        int minZ = coords.get(2);
        int maxX = coords.get(3);
        int maxY = coords.get(4);
        int maxZ = coords.get(5);
        
        // Convert grid coordinates if needed
        if (!sourceGrid.equals(targetGrid)) {
            minX = sourceGrid.convertGrid(minX, targetGrid);
            minY = sourceGrid.convertGrid(minY, targetGrid);
            minZ = sourceGrid.convertGrid(minZ, targetGrid);
            maxX = sourceGrid.convertGrid(maxX, targetGrid);
            maxY = sourceGrid.convertGrid(maxY, targetGrid);
            maxZ = sourceGrid.convertGrid(maxZ, targetGrid);
        }
        
        return new NeoBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    /**
     * Extract grid system from blueprint
     * @return Source grid system
     */
    private NeoGrid extractGrid() {
        Object gridValue = nbtData.get(KEY_GRID);
        if (gridValue instanceof Integer) {
            int gridSize = (Integer) gridValue;
            try {
                return new NeoGrid(gridSize);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid grid size " + gridSize + ", using default");
            }
        }
        return new NeoGrid(DEFAULT_LITTLETILES_GRID);
    }
    
    /**
     * Extract minimum coordinates from blueprint
     * @return Minimum coordinates [x, y, z]
     */
    private int[] extractMinCoords() {
        List<Integer> minList = getIntListValue(nbtData.get(KEY_MIN));
        if (minList != null && minList.size() >= 3) {
            return new int[]{minList.get(0), minList.get(1), minList.get(2)};
        }
        return new int[]{0, 0, 0};
    }
    
    /**
     * Extract size coordinates from blueprint
     * @return Size coordinates [width, height, depth]
     */
    private int[] extractSizeCoords() {
        List<Integer> sizeList = getIntListValue(nbtData.get(KEY_SIZE));
        if (sizeList != null && sizeList.size() >= 3) {
            return new int[]{sizeList.get(0), sizeList.get(1), sizeList.get(2)};
        }
        return new int[]{1, 1, 1};
    }
    
    /**
     * Get content section from blueprint
     * @return Content data map
     */
    private Map<String, Object> getContentSection() {
        return getMapValue(nbtData, KEY_CONTENT);
    }
    
    /**
     * Parse SNBT string to data structure
     * @param snbt SNBT format string
     * @return Parsed data map
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseSNBT(String snbt) {
        System.out.println("Parsing SNBT: " + snbt.substring(0, Math.min(100, snbt.length())) + "...");
        
        Object parsed = SNBTParser.parse(snbt);
        if (parsed instanceof Map) {
            return (Map<String, Object>) parsed;
        } else {
            throw new IllegalArgumentException("SNBT root must be a compound tag");
        }
    }
    
    /**
     * Get map value from object
     * @param obj Object to extract from
     * @param key Key to look for
     * @return Map value or null
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMapValue(Object obj, String key) {
        if (obj instanceof Map) {
            Object value = ((Map<String, Object>) obj).get(key);
            if (value instanceof Map) {
                return (Map<String, Object>) value;
            }
        }
        return null;
    }
    
    /**
     * Get map value from map
     * @param map Map to extract from
     * @param key Key to look for
     * @return Map value or null
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMapValue(Map<String, Object> map, String key) {
        if (map != null) {
            Object value = map.get(key);
            if (value instanceof Map) {
                return (Map<String, Object>) value;
            }
        }
        return null;
    }
    
    /**
     * Get list value from object
     * @param obj Object to extract from
     * @return List value or null
     */
    @SuppressWarnings("unchecked")
    private static List<Object> getListValue(Object obj) {
        if (obj instanceof List) {
            return (List<Object>) obj;
        }
        return null;
    }
    
    /**
     * Get integer list from object
     * @param obj Object to extract from
     * @return Integer list or null
     */
    @SuppressWarnings("unchecked")
    private static List<Integer> getIntListValue(Object obj) {
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            List<Integer> intList = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Integer) {
                    intList.add((Integer) item);
                } else if (item instanceof Number) {
                    intList.add(((Number) item).intValue());
                }
            }
            return intList.isEmpty() ? null : intList;
        }
        return null;
    }
    
    /**
     * Check if this is a valid LittleTiles blueprint
     * @return true if valid
     */
    public boolean isValid() {
        return nbtData != null && sourceGrid != null;
    }
    
    /**
     * Get blueprint statistics
     * @return Statistics string
     */
    public String getStats() {
        Map<String, Object> content = getContentSection();
        if (content == null) return "Invalid blueprint";
        
        Map<String, Object> tilesData = getMapValue(content, KEY_TILES);
        int materialCount = tilesData != null ? tilesData.size() : 0;
        
        return String.format("Blueprint[grid=%s, materials=%d, size=%dx%dx%d]",
                           sourceGrid, materialCount, 
                           sizeCoords[0], sizeCoords[1], sizeCoords[2]);
    }
    
    /**
     * Get source grid system
     * @return Source grid
     */
    public NeoGrid getSourceGrid() {
        return sourceGrid;
    }
    
    /**
     * Get minimum coordinates
     * @return Min coordinates array
     */
    public int[] getMinCoords() {
        return minCoords.clone();
    }
    
    /**
     * Get size coordinates
     * @return Size coordinates array
     */
    public int[] getSizeCoords() {
        return sizeCoords.clone();
    }
}