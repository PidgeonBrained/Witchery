package com.gmail.Ventex240.witchery.block.entity;

import com.gmail.Ventex240.witchery.Witchery;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;


public class WitchOvenEntity extends AbstractFurnaceBlockEntity {
    public WitchOvenEntity(BlockPos pos, BlockState state) {
        super(Witchery.WITCH_OVEN_ENTITY, pos, state, RecipeType.SMELTING); //TODO: CHANGE RECIPE TYPE
    }

    protected Text getContainerName() {
        return new TranslatableText("container.witch_oven");
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}