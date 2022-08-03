package com.gmail.Ventex240.witchery.screen;

import com.gmail.Ventex240.witchery.Witchery;
import com.gmail.Ventex240.witchery.block.entity.WitchOvenBlockEntity;
import com.gmail.Ventex240.witchery.recipe.WitchOvenRecipe;
import com.gmail.Ventex240.witchery.screen.slot.WitchOvenFuelSlot;
import com.gmail.Ventex240.witchery.screen.slot.WitchOvenMainOutputSlot;
import com.gmail.Ventex240.witchery.screen.slot.WitchOvenPotSlot;
import com.gmail.Ventex240.witchery.screen.slot.WitchOvenSecondaryOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class WitchOvenScreenHandler extends AbstractRecipeScreenHandler<Inventory> {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;
    private final WitchOvenRecipe.Type recipeType;
    private final RecipeBookCategory category;

    public WitchOvenScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(5), new ArrayPropertyDelegate(4));
    }

    public WitchOvenScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Witchery.WITCH_OVEN_SCREEN_HANDLER, syncId);
        this.recipeType = WitchOvenRecipe.Type.INSTANCE;
        this.category = RecipeBookCategory.FURNACE; //TODO:change
        checkSize(inventory, 5);
        checkDataCount(propertyDelegate, 4);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.world;
        this.addSlot(new Slot(inventory, 0, 48, 16)); //Input slot
        this.addSlot(new WitchOvenPotSlot(this, inventory, 1, 25, 35)); //Pot Input Slot
        this.addSlot(new WitchOvenFuelSlot(this, inventory, 2, 48, 56)); //Fuel Slot
        this.addSlot(new WitchOvenMainOutputSlot(playerInventory.player, inventory, 3, 99, 37)); //Main Output
        this.addSlot(new WitchOvenSecondaryOutputSlot(playerInventory.player, inventory, 4, 129, 41)); //Secondary Output

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + 2));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142 + 2));
        }

        this.addProperties(propertyDelegate);
    }

    public void populateRecipeFinder(RecipeMatcher finder) {
        if (this.inventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider)this.inventory).provideRecipeInputs(finder);
        }

    }

    public void clearCraftingSlots() {
        this.getSlot(0).setStack(ItemStack.EMPTY);
        this.getSlot(1).setStack(ItemStack.EMPTY);
        this.getSlot(3).setStack(ItemStack.EMPTY);
        this.getSlot(4).setStack(ItemStack.EMPTY);

    }

    public boolean matches(Recipe<? super Inventory> recipe) {
        return recipe.matches(this.inventory, this.world);
    }

    public int getCraftingResultSlotIndex() {
        return 2;
    }

    public int getCraftingWidth() {
        return 1;
    }

    public int getCraftingHeight() {
        return 1;
    }

    public int getCraftingSlotCount() {
        return 5;
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack transferSlot(PlayerEntity player, int index) { //index is the # of the slot you're trying to remove from
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 3 || index == 4) {
                if (!this.insertItem(itemStack2, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index != 1 && index != 2 && index != 0) {
                 if (this.isPot(itemStack2)) {
                     if (!this.insertItem(itemStack2, 1, 2, false)) {
                         return ItemStack.EMPTY;
                     }
                 } else if (this.isSmeltable(itemStack2)) {
                     if (!this.insertItem(itemStack2, 0, 1, false)) {
                         return ItemStack.EMPTY;
                     }
                 } else if (this.isFuel(itemStack2)) {
                     if (!this.insertItem(itemStack2, 2, 3, false)) {
                         return ItemStack.EMPTY;
                     }
                } else if (index >= 5 && index < 32) {
                    if (!this.insertItem(itemStack2, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 32 && index < 41 && !this.insertItem(itemStack2, 5, 32, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 5, 41, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    protected boolean isSmeltable(ItemStack itemStack) {
        return this.world.getRecipeManager().getFirstMatch(this.recipeType, new SimpleInventory(itemStack), this.world).isPresent();
    }

    public boolean isPot(ItemStack itemstack) {
        return itemstack.isOf(Witchery.CLAY_POT);
    }

    public boolean isFuel(ItemStack itemStack) {
        return WitchOvenBlockEntity.canUseAsFuel(itemStack);
    }

    public int getCookProgress() {
        int i = this.propertyDelegate.get(2);
        int j = this.propertyDelegate.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getFuelProgress() {
        int i = this.propertyDelegate.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.propertyDelegate.get(0) * 13 / i;
    }

    public boolean isBurning() {
        return this.propertyDelegate.get(0) > 0;
    }

    public RecipeBookCategory getCategory() {
        return this.category;
    }

    public boolean canInsertIntoSlot(int index) {
        return index != 1;
    }
}
