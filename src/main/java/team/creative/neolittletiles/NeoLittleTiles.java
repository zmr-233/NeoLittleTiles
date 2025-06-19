package team.creative.neolittletiles;

import net.neoforged.fml.common.Mod;
import team.creative.neolittletiles.client.gui.NeoLittleTilesGuiRegistry;

/**
 * NeoLittleTiles - High-Performance Tile Construction System
 * 
 * Main mod class for the complete rewrite of LittleTiles.
 * Focuses on performance optimization and architectural improvements.
 * 
 * Key Features:
 * - Optimized grid system with bit operations
 * - Flattened tile hierarchy for reduced memory usage
 * - Streamlined action system
 * - Compatible with existing LittleTiles structures
 */
@Mod(NeoLittleTiles.MODID)
public class NeoLittleTiles {
    
    public static final String MODID = "neolittletiles";
    public static final String NAME = "NeoLittleTiles";
    public static final String VERSION = "1.0.0-pre1";
    
    public NeoLittleTiles() {
        System.out.println("NeoLittleTiles initialized - High-Performance Tile Construction System");
        System.out.println("Version: " + VERSION);
        
        // Register mod content
        NeoLittleTilesRegistry.register();
        
        // Register GUI system
        NeoLittleTilesGuiRegistry.register();
        
        System.out.println("Core classes loaded successfully:");
        System.out.println("  - NeoGrid: Optimized grid system with bit operations");
        System.out.println("  - NeoBox: Efficient 3D coordinate system");
        System.out.println("  - NeoTile: Flattened tile structure");
        System.out.println("  - NeoAction: Reduced allocation action system");
        System.out.println("  - NeoBlueprint: NBT-based LittleTiles compatibility");
        System.out.println("Items registered:");
        System.out.println("  - NeoChisel: Precision tile placement tool");
        System.out.println("  - NeoHammer: Efficient tile destruction tool");
        System.out.println("  - NeoBlueprint: Structure save/load system");
        System.out.println("Ready for high-performance tile construction!");
    }
}