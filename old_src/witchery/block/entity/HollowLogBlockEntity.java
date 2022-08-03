package com.gmail.Ventex240.witchery.block.entity;

import com.gmail.Ventex240.witchery.Witchery;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class HollowLogBlockEntity extends LockableContainerBlockEntity {

    private static BlockEntityType<HollowLogBlockEntity> type;
    private DefaultedList<ItemStack> inventory;

    public HollowLogBlockEntity(BlockPos pos, BlockState state) {
        super(Witchery.HOLLOW_LOG_ENTITY, pos, state);
        this.type = Witchery.HOLLOW_LOG_ENTITY;
        this.inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
    }

    @Override
    protected Text getContainerName() {
        return null;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return null;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return null;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return null;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void onOpen(PlayerEntity player) {
        super.onOpen(player);
    }

    @Override
    public void onClose(PlayerEntity player) {
        super.onClose(player);
    }

    @Override
    public void clear() {

    }
}
