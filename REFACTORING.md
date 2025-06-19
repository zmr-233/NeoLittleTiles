# NeoLittleTiles Refactoring Documentation

## Overview

NeoLittleTiles is a complete rewrite of the LittleTiles mod, designed to address critical performance and maintainability issues identified in the original codebase.

## Key Problems Addressed

### Performance Issues
- **Grid System**: Replaced complex grid calculations with efficient bit operations
- **Coordinate Math**: Optimized LittleBox operations with simplified algorithms  
- **Memory Allocation**: Reduced object creation in action system and rendering
- **Rendering**: Streamlined buffer management and culling systems

### Architectural Issues
- **Hierarchy Flattening**: Simplified LittleGroup structure to reduce recursion
- **System Decoupling**: Separated rendering, data, and logic layers
- **Action System**: Reduced command pattern overhead
- **NBT Optimization**: Streamlined serialization format

## Core Components

### Grid System (`team.creative.neolittletiles.common.grid`)
- `NeoGrid`: Power-of-2 grids with bit operation optimizations
- Constant-time coordinate conversions
- Efficient block/grid coordinate mapping

### Math Primitives (`team.creative.neolittletiles.common.math`)
- `NeoBox`: Simplified 3D box with optimized intersection testing
- Reduced method complexity compared to LittleBox
- Direct coordinate operations without indirection

### Tile System (`team.creative.neolittletiles.common.tile`)
- `NeoTile`: Flattened structure with direct box storage
- Simplified state management
- Efficient volume calculations

### Action System (`team.creative.neolittletiles.common.action`)
- `NeoAction`: Reduced object allocation pattern
- Streamlined validation and permission checking
- Simplified network synchronization

## Architecture Decisions

### Maintained Compatibility
- NBT format compatibility with original LittleTiles
- CreativeCore dependency for UI infrastructure
- Existing structure definitions preserved
- Tool functionality parity maintained

### Performance Optimizations
- Power-of-2 grid sizes for bit operations
- Constant-time coordinate lookups  
- Lazy loading for complex structures
- Memory-efficient caching systems

### Code Quality Improvements
- Reduced complexity from 1000+ line files
- Clear separation of concerns
- Simplified inheritance hierarchies
- Efficient data structures based on Rust reference implementation

## Implementation Status

### Phase 1: Core Infrastructure ✅
- [x] Project setup and configuration
- [x] Basic grid system
- [x] Math primitives (NeoBox)
- [x] Tile foundations
- [x] Action framework

### Phase 2: Data Management (TODO)
- [ ] Serialization system
- [ ] Block entity integration
- [ ] Structure system
- [ ] Inventory management

### Phase 3: Rendering (TODO)
- [ ] Optimized rendering pipeline
- [ ] Buffer management
- [ ] Culling systems
- [ ] Client-side optimizations

### Phase 4: Tools & GUI (TODO)  
- [ ] Tool system integration
- [ ] CreativeCore GUI integration
- [ ] Configuration management
- [ ] User interaction systems

## Reference Documentation

- **Analysis Results**: `LOCAL/analysis.txt` - Detailed technical analysis
- **Legacy Documentation**: `LOCAL/docs_old/` - Original implementation research
- **Rust Reference**: `/home/zmr233/01_Projects/02_Graph/04_Topology/voxel_cad/code/voxel-cad/src/little_tiles.rs`

## Development Guidelines

1. **Performance First**: All implementations should prioritize performance over convenience
2. **Memory Efficiency**: Minimize object allocation in hot paths
3. **Maintainability**: Keep classes under 500 lines when possible
4. **Testing**: Implement comprehensive tests for core mathematical operations
5. **Documentation**: Document all performance-critical algorithms

## Building and Testing

```bash
# Build the project
./gradlew build

# Run client for testing
./gradlew runClient

# Run tests
./gradlew test
```

## Configuration

The project is configured with:
- **Memory**: 5GB allocation for development (complex tile structures)
- **Dependencies**: CreativeCore 2.13.5+ for UI infrastructure
- **Target**: Minecraft 1.21.1 with NeoForge 21.1.173+
- **Java**: 21 (required for modern Minecraft)