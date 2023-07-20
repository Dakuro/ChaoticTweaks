package io.github.dakuro.chaotic_tweaks.init;

import io.github.dakuro.chaotic_tweaks.ChaoticTweaks;
import io.github.dakuro.chaotic_tweaks.block.ShaulkronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public interface ModBlocks {

//	Block MOD_BLOCK = createBlock("mod_block", new ModBlock(QuiltBlockSettings.of(Material.METAL, MapColor.DEEPSLATE_GRAY).strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.COPPER)), true);
	Block TEST = createBlock("test", new ShaulkronBlock(QuiltBlockSettings.copyOf(Blocks.ANVIL)), new QuiltItemSettings(), ItemGroups.FUNCTIONAL_BLOCKS);
	Block TEST2 = createBlock("test2", new ShaulkronBlock(QuiltBlockSettings.copyOf(Blocks.ANVIL)), new QuiltItemSettings(), ItemGroups.FUNCTIONAL_BLOCKS, "after", Blocks.ANVIL.asItem());

	static Block createBlock(String name, Block block) {
		Registry.register(Registries.BLOCK, new Identifier(ChaoticTweaks.MOD_ID, name), block);
		return block;
	}

	static Block createBlock(String name, Block block, QuiltItemSettings itemSettings) {
		Registry.register(Registries.BLOCK, new Identifier(ChaoticTweaks.MOD_ID, name), block);
		ModItems.createItem(name, new BlockItem(block, itemSettings));
		return block;
	}

	static Block createBlock(String name, Block block, QuiltItemSettings itemSettings, RegistryKey<ItemGroup> group) {
		Registry.register(Registries.BLOCK, new Identifier(ChaoticTweaks.MOD_ID, name), block);
		ModItems.createItem(name, new BlockItem(block, itemSettings), group);
		return block;
	}

	static Block createBlock(String name, Block block, QuiltItemSettings itemSettings, RegistryKey<ItemGroup> group, String position, Item neighbor) {
		Registry.register(Registries.BLOCK, new Identifier(ChaoticTweaks.MOD_ID, name), block);
		ModItems.createItem(name, new BlockItem(block, itemSettings), group, position, neighbor);
		return block;
	}

	static void initialize() {
	}
}
