/*package com.gmail.Ventex240.witchery.fluid;

import com.gmail.Ventex240.witchery.Witchery;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;

public class Cum extends FlowableFluid {
    @Override
    public Fluid getStill() {
        return Witchery.STILL_CUM;
    }

    @Override
    public Fluid getFlowing() {
        return Witchery.FLOWING_CUM;
    }

    @Override
    public Item getBucketItem() {
        return Witchery.CUM_BUCKET;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return Witchery.CUM.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    public static class Flowing extends Cum {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends Cum {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
*/