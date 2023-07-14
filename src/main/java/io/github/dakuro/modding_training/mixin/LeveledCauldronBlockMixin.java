package io.github.dakuro.modding_training.mixin;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.function.Predicate;

@Mixin(LeveledCauldronBlock.class)
public abstract class LeveledCauldronBlockMixin extends AbstractCauldronBlock {

	@Final
	@Shadow
	public static final IntProperty LEVEL = Properties.LEVEL_3;
	@Mutable
	@Final
	@Shadow
	private final Predicate<Biome.Precipitation> precipitationPredicate;

	public LeveledCauldronBlockMixin(Settings settings, Predicate<Biome.Precipitation> precipitationPredicate, Map<Item, CauldronBehavior> behaviorMap) {
		super(settings, behaviorMap);
		this.precipitationPredicate = precipitationPredicate;
		this.setDefaultState(this.stateManager.getDefaultState().with(LEVEL, 1));
	}

}
