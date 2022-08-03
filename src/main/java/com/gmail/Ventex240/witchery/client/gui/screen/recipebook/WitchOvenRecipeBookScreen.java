package com.gmail.Ventex240.witchery.client.gui.screen.recipebook;

import com.gmail.Ventex240.witchery.block.entity.WitchOvenBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class WitchOvenRecipeBookScreen extends RecipeBookWidget { //TODO: maybe delete?
    private static final Text TOGGLE_SMELTABLE_RECIPES_TEXT = new TranslatableText("gui.recipebook.toggleRecipes.smeltable");

    public WitchOvenRecipeBookScreen() {
    }

    protected Text getToggleCraftableButtonText() {
        return TOGGLE_SMELTABLE_RECIPES_TEXT;
    }

    protected Set<Item> getAllowedFuels() {
        return WitchOvenBlockEntity.createFuelTimeMap().keySet();
    }
    @Nullable
    private Ingredient fuels;


    protected void setBookButtonTexture() {
        this.toggleCraftableButton.setTextureUV(152, 182, 28, 18, TEXTURE);
    }

    public void slotClicked(@Nullable Slot slot) {
        super.slotClicked(slot);
        if (slot != null && slot.id < this.craftingScreenHandler.getCraftingSlotCount()) {
            this.ghostSlots.reset();
        }

    }

    public void showGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
        ItemStack itemStack = recipe.getOutput();
        this.ghostSlots.setRecipe(recipe);
        this.ghostSlots.addSlot(Ingredient.ofStacks(new ItemStack[]{itemStack}), ((Slot)slots.get(2)).x, ((Slot)slots.get(2)).y);
        DefaultedList<Ingredient> defaultedList = recipe.getIngredients();
        Slot slot = (Slot)slots.get(1);
        if (slot.getStack().isEmpty()) {
            if (this.fuels == null) {
                this.fuels = Ingredient.ofStacks(this.getAllowedFuels().stream().map(ItemStack::new));
            }

            this.ghostSlots.addSlot(this.fuels, slot.x, slot.y);
        }

        Iterator<Ingredient> iterator = defaultedList.iterator();

        for(int i = 0; i < 2; ++i) {
            if (!iterator.hasNext()) {
                return;
            }

            Ingredient ingredient = (Ingredient)iterator.next();
            if (!ingredient.isEmpty()) {
                Slot slot2 = (Slot)slots.get(i);
                this.ghostSlots.addSlot(ingredient, slot2.x, slot2.y);
            }
        }

    }


}