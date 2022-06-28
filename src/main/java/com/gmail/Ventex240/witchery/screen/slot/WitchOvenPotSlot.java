package com.gmail.Ventex240.witchery.screen.slot;

import com.gmail.Ventex240.witchery.screen.WitchOvenScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class WitchOvenPotSlot extends Slot {
    private final WitchOvenScreenHandler handler;

    public WitchOvenPotSlot(WitchOvenScreenHandler handler, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.handler = handler;
    }

    public boolean canInsert(ItemStack stack) {
        return this.handler.isPot(stack);
    }

    public int getMaxItemCount(ItemStack stack) {
        return super.getMaxItemCount(stack);
    }


}
