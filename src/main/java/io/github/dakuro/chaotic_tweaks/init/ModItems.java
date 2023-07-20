package io.github.dakuro.chaotic_tweaks.init;

import io.github.dakuro.chaotic_tweaks.ChaoticTweaks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

@SuppressWarnings("UnstableApiUsage")
public class ModItems {

	// Declare items here
	// public static final Item TEST_ITEM = createItem("test_item", new Item(new QuiltItemSettings()), ItemGroups.TOOLS_AND_UTILITIES, "before", Items.WOODEN_SHOVEL);

	public static Item createItem(String name, Item item) {
		Registry.register(Registries.ITEM, new Identifier(ChaoticTweaks.MOD_ID, name), item);
		return item;
	}

	public static Item createItem(String name, Item item, RegistryKey<ItemGroup> group) {
		Registry.register(Registries.ITEM, new Identifier(ChaoticTweaks.MOD_ID, name), item);
		addItemGroup(group, entries -> entries.addItem(item));
		return item;
	}

	public static Item createItem(String name, Item item, RegistryKey<ItemGroup> group, String position, Item neighbor) {
		Registry.register(Registries.ITEM, new Identifier(ChaoticTweaks.MOD_ID, name), item);
		switch (position) {
			case "before" -> addItemGroup(group, entries -> entries.addBefore(neighbor, item));
			case "after" -> addItemGroup(group, entries -> entries.addAfter(neighbor, item));
		}
		return item;
	}

	public static void addItemGroup(RegistryKey<ItemGroup> group, ItemGroupEvents.ModifyEntries entries){
		ItemGroupEvents.modifyEntriesEvent(group).register(entries);
	}

	public static void initialize() {

	}
}
