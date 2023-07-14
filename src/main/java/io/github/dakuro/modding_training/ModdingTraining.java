package io.github.dakuro.modding_training;

import io.github.dakuro.modding_training.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ModdingTraining implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Modding Training");
	public static final String MOD_ID = "modding_training";

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());

		ModItems.initialize();
	}

	// Map of BucketItems that can be put inside a cauldron
	public static HashMap<Item, HashMap<String, Object>> BucketToCauldron = new HashMap<>() {
		{
			put(Items.LAVA_BUCKET, new HashMap<>() {
				{
					put("block", Blocks.LAVA_CAULDRON);
					put("block_state", Blocks.LAVA_CAULDRON.getDefaultState());
					put("has_levels", false);
				}
			});

			put(Items.WATER_BUCKET, new HashMap<>() {
				{
					put("block", Blocks.WATER_CAULDRON);
					put("block_state", Blocks.WATER_CAULDRON.getDefaultState().with(Properties.LEVEL_3, 3));
					put("has_levels", true);
				}
			});

			put(Items.POWDER_SNOW_BUCKET, new HashMap<>() {
				{
					put("block", Blocks.POWDER_SNOW_CAULDRON);
					put("block_state", Blocks.POWDER_SNOW_CAULDRON.getDefaultState().with(Properties.LEVEL_3, 3));
					put("has_levels", true);
				}
			});
		}
	};

	// Map of BottleItems that can be put inside a cauldron
	public static HashMap<String, HashMap<String, Object>> BottleToCauldron = new HashMap<>(){
		{
			String itemID = "minecraft:water";
			put(itemID, new HashMap<>() {
				{
					ItemStack water_bottle = new ItemStack(Items.POTION);
					NbtCompound nbt_tags = new NbtCompound();
					nbt_tags.putString("Potion", itemID);
					water_bottle.setNbt(nbt_tags);

					put("stack", water_bottle);
					put("block", Blocks.WATER_CAULDRON);
				}
			});
		}
	};

	// Map of CauldronBlocks that can be emptied by a bucket
	public static HashMap<Block, Item> CauldronToBucket = new HashMap<>(){
		{
			for (Map.Entry<Item, HashMap<String, Object>> entry : BucketToCauldron.entrySet()) {
				put((Block)entry.getValue().get("block"), entry.getKey());
			}
		}
	};

	// Map of CauldronBlocks that can be emptied by a bottle
	public static HashMap<Block, ItemStack> CauldronToBottle = new HashMap<>(){
		{
			for (Map.Entry<String, HashMap<String, Object>> entry : BottleToCauldron.entrySet()) {
				put((Block)entry.getValue().get("block"), (ItemStack)entry.getValue().get("stack"));
			}
		}
	};

	// Determines if a bottle has the required NBT to be put inside a cauldron
	public static boolean ValidBottle(ItemStack bottle) {
		if (!bottle.hasNbt()) {
			return false;
		} else {
			assert bottle.getNbt() != null;
			if (!bottle.getNbt().contains("Potion")) {
				return false;
			}
		}

		return BottleToCauldron.containsKey(bottle.getNbt().getString("Potion"));
	}

	// Enumeration of the different fluid levels a cauldron can have
	public enum CauldronLevel {
		EMPTY(0), LOW(1), MEDIUM(2), FULL(3);

		private final int value;
		CauldronLevel(int value) {
			this.value = value;
		}

	}

	// Used to get the Fluid level of a cauldron
	public static CauldronLevel GetCauldronLevel(BlockState block_state) {
		Block block = block_state.getBlock();
		if (!CauldronToBucket.containsKey(block)) { return CauldronLevel.EMPTY; }
		if (block == Blocks.CAULDRON) { return CauldronLevel.EMPTY; }

		HashMap<String, Object> cauldron = BucketToCauldron.get(CauldronToBucket.get(block));
		if (!((Boolean) cauldron.get("has_levels"))) { return CauldronLevel.FULL; }

		return switch (block_state.get(Properties.LEVEL_3)) {
			case 3 -> CauldronLevel.FULL;
			case 2 -> CauldronLevel.MEDIUM;
			case 1 -> CauldronLevel.LOW;
			default -> CauldronLevel.EMPTY;
		};
	}

	// Handles Fluid level changes when a bottle is used on a cauldron
	public static BlockState NewCauldronBottleLevel(Block cauldron_type, BlockState block_state, Boolean increase) {
		int cauldron_level = GetCauldronLevel(block_state).value;
		if (increase) { cauldron_level++; } else { cauldron_level--; }
		if (cauldron_level >= 3) { cauldron_level = 3; }
		if (cauldron_level <= 0) { cauldron_level = 0; }

		if (cauldron_level == 0) { return Blocks.CAULDRON.getDefaultState(); }
		return cauldron_type.getDefaultState().with(Properties.LEVEL_3, cauldron_level);
	}

}
