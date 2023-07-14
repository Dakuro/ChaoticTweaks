package io.github.dakuro.modding_training.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PowderSnowCauldronBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
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
				// Prevents entities that can walk on Powder Snow being stuck in cauldron
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
			entity.setInPowderSnow(true);
		}
	}

	// Overrides from AbstractBlock to adapt Collision Shape to cauldron
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getOutlineShape(state, world, pos, context);
	}

	// Determines if an entity can walk on Powder Snow
	@Unique
	boolean canWalkOnPowderSnow(Entity entity) {
		if (entity.getType().isIn(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
			return true;
		} else {
			return entity instanceof LivingEntity && ((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET).isOf(Items.LEATHER_BOOTS);
		}
	}

}
