package com.gmail.Ventex240.witchery.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;

public class WitchOvenScreenHandler extends AbstractFurnaceScreenHandler {
    public WitchOvenScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerType.FURNACE, RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

}