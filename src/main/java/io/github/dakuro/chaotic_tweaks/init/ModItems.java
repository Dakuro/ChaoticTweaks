package io.github.dakuro.chaotic_tweaks.init;

import io.github.dakuro.chaotic_tweaks.ChaoticTweaks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ModItems {
	Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

	// Declare items here

	private static <T extends Item> T createItem(String name, T item) {
		ITEMS.put(item, new Identifier(ChaoticTweaks.MOD_ID, name));
		return item;
	}

	static void initialize() {
		ITEMS.keySet().forEach(item -> {
			Registry.register(Registries.ITEM, ITEMS.get(item), item);
		});
	}
}
