package com.gmail.Ventex240.witchery.item;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class Pepsi extends Item {
    public Pepsi(Settings settings) {
        super(settings);
    }
    private static int pepsiHunger = 12;
    private static float pepsiSaturation = 12;
    public static FoodComponent.Builder pepsiBehavior = new FoodComponent.Builder().hunger(Pepsi.pepsiHunger).saturationModifier(pepsiSaturation).alwaysEdible().snack();

}
