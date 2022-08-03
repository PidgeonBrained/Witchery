package com.gmail.Ventex240.witchery.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Broom extends Item {
    public Broom(Settings settings) {
        super(settings);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        playerEntity.playSound(SoundEvents.ENTITY_PHANTOM_FLAP, 1.0F, 1.0F);
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        if (hand == Hand.MAIN_HAND && playerEntity.getOffHandStack().isEmpty()) {

            playerEntity.equipStack(EquipmentSlot.OFFHAND, itemStack.copy());
            itemStack.setCount(0);
            return TypedActionResult.success(itemStack);
        } else {
            return TypedActionResult.fail(itemStack);
        }
    }
}
