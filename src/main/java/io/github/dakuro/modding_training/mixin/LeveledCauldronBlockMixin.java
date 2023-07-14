package io.github.dakuro.modding_training.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeveledCauldronBlock.class)
public abstract class LeveledCauldronBlockMixin {

	@Inject(method = "onEntityCollision", at = @At("HEAD"))
	protected void onEntityCollisionHandler(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci){

	}

}
