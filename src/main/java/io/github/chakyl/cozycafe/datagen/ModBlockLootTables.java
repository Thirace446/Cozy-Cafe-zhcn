//package io.github.chakyl.cozycafe.datagen;
//
//import io.github.chakyl.cozycafe.CozyRegistry;
//import net.minecraft.data.loot.BlockLootSubProvider;
//import net.minecraft.world.flag.FeatureFlags;
//import net.minecraft.world.level.block.Block;
//
//import java.util.Set;
//
//public class ModBlockLootTables extends BlockLootSubProvider {
//    public ModBlockLootTables() {
//        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
//    }
//
//    @Override
//    protected void generate() {
//
//        generateDropSelf(CozyRegistry.BlockRegistry.CAFE_MANAGER);
//        generateDropSelf(CozyRegistry.BlockRegistry.CAFE_MENU);
//        generateDropSelf(CozyRegistry.BlockRegistry.PLATING_STATION);
//        generateDropSelf(CozyRegistry.BlockRegistry.CAFE_SIGN);
//
//    }
//
//    public void generateDropSelf(RegistryObject<Block> block) {
//        this.dropSelf(block.get());
//    }
//
//
//    @Override
//    protected Iterable<Block> getKnownBlocks() {
//        return CozyRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
//    }
//}