package io.github.dakuro.chaotic_tweaks.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PowderSnowCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.function.Predicate;

@SuppressWarnings("deprecation")
@Mixin(PowderSnowCauldronBlock.class)
public abstract class PowderSnowCauldronBlockMixin extends LeveledCauldronBlockMixin {

	// Super to match parent class
	public PowderSnowCauldronBlockMixin(Settings settings, Predicate<Biome.Precipitation> precipitationPredicate, Map<Item, CauldronBehavior> behaviorMap) {
		super(settings, precipitationPredicate, behaviorMap);
	}

	// Overrides from AbstractBlock to implement PowderSnowBlock features
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (this.isEntityTouchingFluid(state, pos, entity)) {
			if (!(entity instanceof LivingEntity) || entity.getBlockStateAtPos().isOf(this)) {
				// If an entity cannot walk on Powder Snow, it is stuck in cauldron
				if (!canWalkOnPowderSnow(entity)) {
					entity.setMovementMultiplier(state, new Vec3d(0.9F, 1.5, 0.9F));
				}
				// Generates Snowflake particles
				if (world.isClient) {
					RandomGenerator randomGenerator = world.getRandom();
					boolean bl = entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ();
					if (bl && randomGenerator.nextBoolean()) {
						world.addParticle(
							ParticleTypes.SNOWFLAKE,
							entity.getX(),
							pos.getY() + 1,
							entity.getZ(),
							MathHelper.nextBetween(randomGenerator, -1.0F, 1.0F) * 0.083333336F,
							0.05F,
							MathHelper.nextBetween(randomGenerator, -1.0F, 1.0F) * 0.083333336F
						);
					}
				}
			}
			entity.setInPowderSnow(!canWalkOnPowderSnow(entity));
		}
	}

	// Determines if an entity can walk on Powder Snow
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	@Unique
	boolean canWalkOnPowderSnow(Entity entity) {
		if (entity.getType().isIn(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
			return true;
		} else {
			return entity instanceof LivingEntity && canBootsResistFrost(((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET));
		}
	}

	// Checks if boots are made of leather or enchanted with Frost Walker
	@Unique
	boolean canBootsResistFrost(ItemStack bootsToCheck){
		if (bootsToCheck.isOf(Items.LEATHER_BOOTS)){
			return true;
		} return EnchantmentHelper.getLevel(Enchantments.FROST_WALKER, bootsToCheck) != 0;
	}

}
