package com.gmail.Ventex240.witchery.recipe;

import com.gmail.Ventex240.witchery.Witchery;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;


public class WitchOvenRecipe implements Recipe<Inventory> {


    private final RecipeType<WitchOvenRecipe> type;
    private final Identifier id;
    private final Ingredient input;
    private final ItemStack mainOutput;
    private final float mainResultChance;
    private final int mainCount;
    private final ItemStack secondaryOutput;
    private final float secondaryResultChance;
    private final int secondaryCount;
    private final boolean consumesPot;
    private final float experience;
    private final int cookTime;

    public WitchOvenRecipe(Identifier id, Ingredient input, ItemStack mainOutput, float mainResultChance, int mainCount,
                           ItemStack secondaryOutput, float secondaryResultChance, int secondaryCount, boolean consumesPot, float experience, int cookTime) {
        this.type = Witchery.WITCH_OVEN_RECIPE;
        this.id = id;
        this.input = input;
        this.mainOutput = mainOutput;
        this.mainResultChance = mainResultChance;
        this.mainCount = mainCount;
        this.secondaryOutput = secondaryOutput;
        this.secondaryResultChance = secondaryResultChance;
        this.secondaryCount = secondaryCount;
        this.consumesPot = consumesPot;
        this.experience = experience;
        this.cookTime = cookTime;
    }

    public boolean matches(Inventory inventory, World world) {
        return this.input.test(inventory.getStack(0));
    }

    public ItemStack craft(Inventory inventory) {
        return this.mainOutput.copy();
    }

    public boolean fits(int width, int height) {
        return true;
    }


    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.input);
        return defaultedList;
    }

    public float getExperience() {
        return this.experience;
    }

    @Override
    public ItemStack getOutput() { //Main output, had to do this because of interface :(
        return this.mainOutput.copy();
    }

    public float getMainChance() {
        return this.mainResultChance;
    }

    public int getMainCount() { return this.mainCount; }

    public ItemStack getSecondaryOutput() {
        if (secondaryOutput == null) {
            return null;
        }
        return this.secondaryOutput.copy();
    }

    public float getSecondaryChance() {
        return this.secondaryResultChance;
    }

    public int getSecondaryCount() { return this.secondaryCount; }

    public int getCookTime() {
        return this.cookTime;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public boolean consumesPot() { return this.consumesPot; }



    @Override
    public ItemStack createIcon() {
        return new ItemStack(Witchery.WITCH_OVEN);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<WitchOvenRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "witch_oven";
    }

    public static class Serializer implements RecipeSerializer<WitchOvenRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "witch_oven";
        private final int defaultCookingTime = 200;


        public WitchOvenRecipe read(Identifier identifier, JsonObject jsonObject) {

            JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");

            Ingredient ingredient = Ingredient.fromJson(jsonElement);//just leaving this to do its own thing, lmao

            JsonObject tempResult = JsonHelper.getObject(jsonObject, "main_result");
            String tempResultItem = JsonHelper.getString(tempResult, "item");
            float mainResultChance = JsonHelper.getFloat(tempResult, "chance", 0.70F);
            int mainCount = JsonHelper.getInt(tempResult, "count", 1);

            Identifier tempIdentifier = new Identifier(tempResultItem);
            ItemStack mainItemStack = new ItemStack((ItemConvertible) Registry.ITEM.getOrEmpty(tempIdentifier).orElseThrow(() -> { //Checks that mainItemStack exists
                return new IllegalStateException("Item: " + tempResultItem + " does not exist");
            }));

            if (mainCount > 64) {
                Witchery.error("Witch Oven output: " + tempResultItem + " count cannot be over 64", new IllegalStateException());
                mainCount = 0;
            }


            float secondaryResultChance;
            int secondaryCount;
            ItemStack secondaryItemStack;
            try { //tries to find secondary_result, if none, sets values to null
                tempResult = JsonHelper.getObject(jsonObject, "secondary_result");
                String tempResultItem2 = JsonHelper.getString(tempResult, "item");
                secondaryResultChance = JsonHelper.getFloat(tempResult, "chance", 1.0F);
                secondaryCount = JsonHelper.getInt(tempResult, "count", 1);
                tempIdentifier = new Identifier(tempResultItem2);
                secondaryItemStack = new ItemStack((ItemConvertible) Registry.ITEM.getOrEmpty(tempIdentifier).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + tempResultItem2 + " does not exist");
                }));
                if (secondaryCount > 64) {
                    Witchery.error("Witch Oven output: " + tempResultItem + " count cannot be over 64", new IllegalStateException());
                    secondaryCount = 0;
                }

            } catch (JsonSyntaxException exception) {

                secondaryResultChance = 0;
                secondaryCount = 0;
                secondaryItemStack = null;

                }


            boolean consumesPot = JsonHelper.getBoolean(jsonObject, "consumes_pot", true);
            float experience = JsonHelper.getFloat(jsonObject, "experience", 0.0F);
            int cookTime = JsonHelper.getInt(jsonObject, "cookingtime", this.defaultCookingTime);

            return new WitchOvenRecipe(identifier, ingredient, mainItemStack, mainResultChance, mainCount,
                    secondaryItemStack, secondaryResultChance, secondaryCount, consumesPot, experience, cookTime);


        }

        public WitchOvenRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            ItemStack mainItemStack = packetByteBuf.readItemStack();
            float mainResultChance = packetByteBuf.readFloat();
            int mainCount = packetByteBuf.readInt();
            ItemStack secondaryItemStack = packetByteBuf.readItemStack();
            float secondaryResultChance = packetByteBuf.readFloat();
            int secondaryCount = packetByteBuf.readInt();
            boolean consumesPot = packetByteBuf.readBoolean();
            float experience = packetByteBuf.readFloat();
            int cookTime = packetByteBuf.readVarInt();

            return new WitchOvenRecipe(identifier, ingredient, mainItemStack, mainResultChance, mainCount,
                    secondaryItemStack, secondaryResultChance, secondaryCount, consumesPot, experience, cookTime);
        }

        public void write(PacketByteBuf packetByteBuf, WitchOvenRecipe recipe) {
            recipe.input.write(packetByteBuf);
            packetByteBuf.writeItemStack(recipe.getOutput()); //main output
            packetByteBuf.writeFloat(recipe.getMainChance());
            packetByteBuf.writeInt(recipe.getMainCount());
            packetByteBuf.writeItemStack(recipe.getSecondaryOutput());
            packetByteBuf.writeFloat(recipe.getSecondaryChance());
            packetByteBuf.writeInt(recipe.getSecondaryCount());
            packetByteBuf.writeBoolean(recipe.consumesPot());
            packetByteBuf.writeFloat(recipe.getExperience());
            packetByteBuf.writeVarInt(recipe.getCookTime());
        }
    }








}