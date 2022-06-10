package com.gmail.Ventex240.witchery.mixin;

import com.gmail.Ventex240.witchery.Witchery;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class WitcheryMixin extends LivingEntity {


	@Shadow @Final private PlayerAbilities abilities;
	@Shadow public abstract void sendAbilitiesUpdate();

	private boolean broomEquipped = false;


	protected WitcheryMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("TAIL"), method = "tick") //For Broom
	private void tick(CallbackInfo info) {
		if (!this.abilities.creativeMode && !this.isSpectator()) {
			if (this.getOffHandStack().getItem() == Witchery.BROOM) {
				this.abilities.allowFlying = true;
				broomEquipped = true;
				this.isFallFlying();
			}
			else if (broomEquipped){ //If no longer in offhand, but was previously equipped, remove flight
				this.abilities.allowFlying = false;
				this.abilities.flying = false;
				broomEquipped = false;
			}
			else { this.sendAbilitiesUpdate(); }
		}
	}


}
