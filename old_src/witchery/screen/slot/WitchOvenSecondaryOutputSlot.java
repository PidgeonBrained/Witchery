package com.gmail.Ventex240.witchery.screen.slot;

import com.gmail.Ventex240.witchery.block.entity.WitchOvenBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

public class WitchOvenSecondaryOutputSlot extends Slot {
    private final PlayerEntity player;
    private int amount;

    public WitchOvenSecondaryOutputSlot(PlayerEntity player, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
    }

    public boolean canInsert(ItemStack stack) {
        return false;
    }

    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }

        return super.takeStack(amount);
    }

    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);
        super.onTakeItem(player, stack);
    }

    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }

    protected void onCrafted(ItemStack stack) {
        stack.onCraft(this.player.world, this.player, this.amount);
        if (this.player instanceof ServerPlayerEntity && this.inventory instanceof WitchOvenBlockEntity) {
            ((WitchOvenBlockEntity)this.inventory).dropExperienceForRecipesUsed((ServerPlayerEntity)this.player);
        }

        this.amount = 0;
    }
}
