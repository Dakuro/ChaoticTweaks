package io.github.dakuro.chaotic_tweaks.mixin;

import net.minecraft.block.PowderSnowBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.PowderSnowBlock.canWalkOnPowderSnow;


@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {

	// Add support for boots enchanted with Frost Walker
	@Redirect(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setInPowderSnow(Z)V"))
	private void customSetInPowderSnow(Entity entity, boolean inPowderSnow){
		entity.setInPowderSnow(!canWalkOnPowderSnow(entity));
	}

	// Add support for boots enchanted with Frost Walker
	@Inject(method = "canWalkOnPowderSnow", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private static void canWalkOnPowderSnowCustom(Entity entity, CallbackInfoReturnable<Boolean> cir){
		cir.setReturnValue(entity instanceof LivingEntity && canBootsResistFrost(((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET)));
	}

	// Checks if boots are made of leather or enchanted with Frost Walker
	@Unique
	private static boolean canBootsResistFrost(ItemStack bootsToCheck){
		if (bootsToCheck.isOf(Items.LEATHER_BOOTS)){
			return true;
		} return EnchantmentHelper.getLevel(Enchantments.FROST_WALKER, bootsToCheck) != 0;
	}
}
