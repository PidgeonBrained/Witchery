package com.gmail.Ventex240.witchery;

import com.gmail.Ventex240.witchery.screen.WitchOvenScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;

import static com.gmail.Ventex240.witchery.Witchery.BABYS_BREATH_BLOCK;


@Environment(EnvType.CLIENT)
public class WitcheryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BABYS_BREATH_BLOCK);

        ScreenRegistry.register(Witchery.WITCH_OVEN_SCREEN_HANDLER, WitchOvenScreen::new);
    }
}