package team.creative.neolittletiles;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.creative.neolittletiles.common.block.NeoTilesBlock;
import team.creative.neolittletiles.common.block.NeoTilesBlockEntity;
import team.creative.neolittletiles.common.item.NeoBlueprintItem;
import team.creative.neolittletiles.common.item.NeoChisel;
import team.creative.neolittletiles.common.item.NeoHammer;

import java.util.function.Supplier;

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
    
    // Deferred registers
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, NeoLittleTiles.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, NeoLittleTiles.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NeoLittleTiles.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NeoLittleTiles.MODID);
    
    // Registry objects
    public static final Supplier<Block> NEOTILES_BLOCK = BLOCKS.register(NEOTILES_BLOCK_ID, NeoTilesBlock::new);
    public static final Supplier<BlockEntityType<NeoTilesBlockEntity>> NEOTILES_BLOCK_ENTITY_TYPE = BLOCK_ENTITIES.register(
        NEOTILES_BLOCK_ENTITY_ID, 
        () -> BlockEntityType.Builder.of(NeoTilesBlockEntity::new, NEOTILES_BLOCK.get()).build(null)
    );
    
    // Items
    public static final Supplier<Item> NEOCHISEL_ITEM = ITEMS.register(NEOCHISEL_ITEM_ID, () -> new NeoChisel(new Item.Properties()));
    public static final Supplier<Item> NEOHAMMER_ITEM = ITEMS.register(NEOHAMMER_ITEM_ID, () -> new NeoHammer(new Item.Properties()));
    public static final Supplier<Item> NEOBLUEPRINT_ITEM = ITEMS.register(NEOBLUEPRINT_ITEM_ID, () -> new NeoBlueprintItem(new Item.Properties()));
    public static final Supplier<Item> NEOTILES_BLOCK_ITEM = ITEMS.register(NEOTILES_BLOCK_ID, () -> new BlockItem(NEOTILES_BLOCK.get(), new Item.Properties()));
    
    // Creative tab
    public static final Supplier<CreativeModeTab> NEOLITTLETILES_TAB = CREATIVE_TABS.register("neolittletiles", () -> CreativeModeTab.builder()
        .title(net.minecraft.network.chat.Component.translatable("itemGroup.neolittletiles"))
        .icon(() -> NEOCHISEL_ITEM.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(NEOCHISEL_ITEM.get());
            output.accept(NEOHAMMER_ITEM.get());
            output.accept(NEOBLUEPRINT_ITEM.get());
            output.accept(NEOTILES_BLOCK_ITEM.get());
        })
        .build());
    
    /**
     * Register all deferred registers to the mod event bus
     * Called during mod construction
     */
    public static void register(IEventBus modEventBus) {
        System.out.println("Registering NeoLittleTiles deferred registers...");
        
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        
        System.out.println("NeoLittleTiles registration completed:");
        System.out.println("  - " + NEOTILES_BLOCK_ID + " block");
        System.out.println("  - " + NEOTILES_BLOCK_ENTITY_ID + " block entity");
        System.out.println("  - " + NEOCHISEL_ITEM_ID + " item");
        System.out.println("  - " + NEOHAMMER_ITEM_ID + " item");
        System.out.println("  - " + NEOBLUEPRINT_ITEM_ID + " item");
        System.out.println("  - neolittletiles creative tab");
    }
    
    /**
     * Legacy register method for compatibility
     */
    public static void register() {
        System.out.println("Warning: Using legacy register() method. Use register(IEventBus) instead.");
    }
    
    /**
     * Get NeoTiles block
     * @return Block instance
     */
    public static Block getNeoTilesBlock() {
        return NEOTILES_BLOCK.get();
    }
    
    /**
     * Get NeoTiles block entity type
     * @return BlockEntityType instance
     */
    public static BlockEntityType<NeoTilesBlockEntity> getNeoTilesBlockEntityType() {
        return NEOTILES_BLOCK_ENTITY_TYPE.get();
    }
    
    /**
     * Get NeoChisel item
     * @return Item instance
     */
    public static Item getNeoChiselItem() {
        return NEOCHISEL_ITEM.get();
    }
    
    /**
     * Get NeoHammer item
     * @return Item instance
     */
    public static Item getNeoHammerItem() {
        return NEOHAMMER_ITEM.get();
    }
    
    /**
     * Get NeoBlueprint item
     * @return Item instance
     */
    public static Item getNeoBlueprintItem() {
        return NEOBLUEPRINT_ITEM.get();
    }
}