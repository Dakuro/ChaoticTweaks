package io.github.dakuro.modding_training.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PowderSnowCauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PowderSnowCauldronBlock.class)
public abstract class PowderSnowCauldronBlockMixin extends LeveledCauldronBlockMixin {

	@Override
	protected void onEntityCollisionHandler(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
		if (!(entity instanceof LivingEntity)) {
			entity.setMovementMultiplier(state, new Vec3d(0.9F, 1.5, 0.9F));
			if (world.isClient) {
				RandomGenerator randomGenerator = world.getRandom();
				boolean bl = entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ();
				if (bl && randomGenerator.nextBoolean()) {
					world.addParticle(
						ParticleTypes.SNOWFLAKE,
						entity.getX(),
						(double)(pos.getY() + 1),
						entity.getZ(),
						(double)(MathHelper.nextBetween(randomGenerator, -1.0F, 1.0F) * 0.083333336F),
						0.05F,
						(double)(MathHelper.nextBetween(randomGenerator, -1.0F, 1.0F) * 0.083333336F)
					);
				}
			}
		}

		entity.setInPowderSnow(true);

	}

}
