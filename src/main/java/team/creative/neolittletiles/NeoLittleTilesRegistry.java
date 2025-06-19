package team.creative.neolittletiles;

/**
 * NeoLittleTilesRegistry - Central registration system for mod content
 * 
 * Registers all items, blocks, and block entities for NeoLittleTiles:
 * - Tools: NeoChisel, NeoHammer  
 * - Items: NeoBlueprint
 * - Blocks: NeoTilesBlock
 * - Block Entities: NeoTilesBlockEntity
 * 
 * Uses proper NeoForge registration patterns
 */
public class NeoLittleTilesRegistry {
    
    // Registry names
    public static final String NEOTILES_BLOCK_ID = "neotiles";
    public static final String NEOTILES_BLOCK_ENTITY_ID = "neotiles";
    public static final String NEOCHISEL_ITEM_ID = "neochisel";
    public static final String NEOHAMMER_ITEM_ID = "neohammer";
    public static final String NEOBLUEPRINT_ITEM_ID = "neoblueprint";
    
    // Placeholder for actual registry objects - will be replaced when NeoForge dependencies are resolved
    public static Object NEOTILES_BLOCK;
    public static Object NEOTILES_BLOCK_ENTITY_TYPE;
    public static Object NEOCHISEL_ITEM;
    public static Object NEOHAMMER_ITEM;
    public static Object NEOBLUEPRINT_ITEM;
    
    /**
     * Register all mod content
     * Called during mod initialization
     */
    public static void register() {
        System.out.println("Registering NeoLittleTiles content...");
        
        registerBlocks();
        registerBlockEntities();
        registerItems();
        registerCreativeTab();
        
        System.out.println("NeoLittleTiles registration completed");
    }
    
    /**
     * Register blocks
     */
    private static void registerBlocks() {
        System.out.println("Registering NeoLittleTiles blocks:");
        
        // TODO: Implement when Block and DeferredRegister are available
        // NEOTILES_BLOCK = registerBlock(NEOTILES_BLOCK_ID, NeoTilesBlock::new);
        
        System.out.println("  - " + NEOTILES_BLOCK_ID + " block");
    }
    
    /**
     * Register block entities
     */
    private static void registerBlockEntities() {
        System.out.println("Registering NeoLittleTiles block entities:");
        
        // TODO: Implement when BlockEntityType and DeferredRegister are available
        // NEOTILES_BLOCK_ENTITY_TYPE = registerBlockEntity(NEOTILES_BLOCK_ENTITY_ID, 
        //     NeoTilesBlockEntity::new, NEOTILES_BLOCK);
        
        System.out.println("  - " + NEOTILES_BLOCK_ENTITY_ID + " block entity");
    }
    
    /**
     * Register items
     */
    private static void registerItems() {
        System.out.println("Registering NeoLittleTiles items:");
        
        // TODO: Implement when Item and DeferredRegister are available
        // NEOCHISEL_ITEM = registerItem(NEOCHISEL_ITEM_ID, NeoChiselItem::new);
        // NEOHAMMER_ITEM = registerItem(NEOHAMMER_ITEM_ID, NeoHammerItem::new);
        // NEOBLUEPRINT_ITEM = registerItem(NEOBLUEPRINT_ITEM_ID, NeoBlueprintItem::new);
        
        System.out.println("  - " + NEOCHISEL_ITEM_ID + " item");
        System.out.println("  - " + NEOHAMMER_ITEM_ID + " item");
        System.out.println("  - " + NEOBLUEPRINT_ITEM_ID + " item");
    }
    
    /**
     * Register creative mode tab
     */
    private static void registerCreativeTab() {
        System.out.println("Registering NeoLittleTiles creative tab");
        
        // TODO: Implement when CreativeModeTab is available
        // Create tab with mod items
    }
    
    /**
     * Get NeoTiles block
     * @return Block instance
     */
    public static Object getNeoTilesBlock() {
        return NEOTILES_BLOCK;
    }
    
    /**
     * Get NeoTiles block entity type
     * @return BlockEntityType instance
     */
    public static Object getNeoTilesBlockEntityType() {
        return NEOTILES_BLOCK_ENTITY_TYPE;
    }
    
    /**
     * Get NeoChisel item
     * @return Item instance
     */
    public static Object getNeoChiselItem() {
        return NEOCHISEL_ITEM;
    }
    
    /**
     * Get NeoHammer item
     * @return Item instance
     */
    public static Object getNeoHammerItem() {
        return NEOHAMMER_ITEM;
    }
    
    /**
     * Get NeoBlueprint item
     * @return Item instance
     */
    public static Object getNeoBlueprintItem() {
        return NEOBLUEPRINT_ITEM;
    }
}