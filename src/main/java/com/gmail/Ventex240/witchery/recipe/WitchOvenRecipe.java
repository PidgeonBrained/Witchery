package com.gmail.Ventex240.witchery.recipe;

import com.gmail.Ventex240.witchery.Witchery;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class WitchOvenRecipe extends AbstractCookingRecipe {
    public WitchOvenRecipe(Identifier id, String group, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(Witchery.WITCH_OVEN_RECIPE, id, group, input, output, experience, cookTime);
    }

    //@Override
   // public ItemStack getRecipeKindIcon() {
        //return new ItemStack(Items.BLACKSTONE);
   // }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Witchery.WITCH_OVEN_RECIPE_SERIALIZER;
    }
}