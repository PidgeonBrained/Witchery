package com.gmail.Ventex240.witchery.screen;

import com.gmail.Ventex240.witchery.Witchery;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class HollowLogScreenHandler extends ScreenHandler {

    ScreenHandlerType<HollowLogScreenHandler> type;


    public HollowLogScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(Witchery.HOLLOW_LOG_SCREEN_HANDLER, syncId);
        type = Witchery.HOLLOW_LOG_SCREEN_HANDLER;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }
}
