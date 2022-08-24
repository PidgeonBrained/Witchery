package com.gmail.Ventex240.witchery.block;

import com.gmail.Ventex240.witchery.Witchery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends Block { //Im thinking it'll only check that there's 6 blocks when the cloth is place over the blocks


    public static final BooleanProperty VALID;
    public static final BooleanProperty ALTAR_DISPLAY_TYPE; //False for middle, true for corner

    static {
        VALID = Properties.ENABLED;
        ALTAR_DISPLAY_TYPE = Properties.CONDITIONAL; //TODO:properly implement blockstates, etc.
    }


    public AltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(VALID,false).with(ALTAR_DISPLAY_TYPE,false));
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
    }


    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        if (state == state.with(VALID, true)) {
            return PistonBehavior.BLOCK;
        }
        else {
            return super.getPistonBehavior(state);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VALID, ALTAR_DISPLAY_TYPE);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state == state.with(VALID, false)) { //If altar block is currently invalid
            if (((hand == Hand.MAIN_HAND && player.getMainHandStack().isOf(Witchery.ALTAR_CLOTH)))) { //if player right clicks altar with altar cloth
                if(world.isClient()) {
                    return ActionResult.SUCCESS;
                }
                else {
                    int blockNum = this.isAltarValid(world, pos);
                    if (blockNum != -1) {
                        player.getMainHandStack().decrement(1);
                        validateAltar(world, pos, blockNum);
                        return ActionResult.CONSUME;
                    }
                    else {
                        player.sendMessage(new LiteralText("Altar is invalid."), true);
                    }
                }

            }
        }

        return ActionResult.FAIL;

    }

    /*TODO: possibly make altar block helper class that'll store the pos of all of the blocks. Each tick, the valid blocks will check and make sure
    TODO: they're still in the position they're supposed to be in, and also make sure all the blocks are still present
     */


    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (state == state.with(VALID, true)) {
            ItemStack altarCloth = new ItemStack(Witchery.ALTAR_CLOTH);
            dropStacks(state, world, pos, blockEntity, player, altarCloth);
        }
        super.afterBreak(world, player, pos, state, blockEntity, stack);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
    }


    public int isAltarValid(World world, BlockPos pos) { //Returns 1-12 if altar is valid, and -1 if it's not valid
        BlockState[] nearbyBlocks, blocksAbove;
        for (int i=1;i<=12;i++) {
            nearbyBlocks = getNearbyBlockStates(world, pos, i);
            blocksAbove = getBlockStatesAboveAltar(world, pos, i);
            for (int j = 0; j<5;j++) {
                if ((!nearbyBlocks[j].isOf(Witchery.ALTAR_BLOCK)) || (nearbyBlocks[j] == nearbyBlocks[j].with(VALID, true)) || !(blocksAbove[j].isOf(Blocks.AIR))) {
                    break;
                }

                if (j==4) {
                    System.out.println("altar is valid owo");
                    return i;
                }
            }
        }

        return -1;
    }

    public void validateAltar(World world, BlockPos pos, int blockNum) {
        BlockPos altarBlockOne = getAltarBlockOne(pos, blockNum);
        BlockState altarCorner = world.getBlockState(pos).with(VALID, true).with(ALTAR_DISPLAY_TYPE, true);
        BlockState altarEdge = world.getBlockState(pos).with(VALID, true).with(ALTAR_DISPLAY_TYPE, false);
        if (blockNum <= 6 && blockNum >= 1) {
            world.setBlockState(altarBlockOne, altarCorner);//1
            world.setBlockState(altarBlockOne.west(), altarEdge);//2
            world.setBlockState(altarBlockOne.add(-2,0,0), altarCorner);//3
            world.setBlockState(altarBlockOne.north(), altarCorner);//4
            world.setBlockState(altarBlockOne.add(-1,0,-1), altarEdge);//5
            world.setBlockState(altarBlockOne.add(-2,0,-1), altarCorner);//6
        }
        else if (blockNum <= 12 && blockNum >= 7) {
            world.setBlockState(altarBlockOne, altarCorner);//7
            world.setBlockState(altarBlockOne.north(), altarEdge);//8
            world.setBlockState(altarBlockOne.add(0,0,-2), altarCorner);//9
            world.setBlockState(altarBlockOne.east(), altarCorner);//10
            world.setBlockState(altarBlockOne.add(1,0,-1), altarEdge);//11
            world.setBlockState(altarBlockOne.add(1,0,-2), altarCorner);//12
        }


    }
    


    @Nullable
    public BlockState[] getNearbyBlockStates (World world, BlockPos pos, int blockNum) {

        BlockState[] stateReturn = new BlockState[5];

        switch (blockNum) {
            case 1: //Block is in lower right corner of horizontal orientation
                stateReturn[0] = world.getBlockState(pos.north());
                stateReturn[1] = world.getBlockState(pos.add(-2, 0, 0));
                stateReturn[2] = world.getBlockState(pos.west());
                stateReturn[3] = world.getBlockState(pos.add(-1, 0, -1));
                stateReturn[4] = world.getBlockState(pos.add(-2, 0, -1));
                break;
            case 2://Block is in lower middle of horizontal orientation
                stateReturn[0] = world.getBlockState(pos.east());
                stateReturn[1] = world.getBlockState(pos.west());
                stateReturn[2] = world.getBlockState(pos.add(1, 0, -1));
                stateReturn[3] = world.getBlockState(pos.north());
                stateReturn[4] = world.getBlockState(pos.add(-1, 0, -1));
                break;
            case 3://Block is in lower left corner of horizontal orientation
                stateReturn[0] = world.getBlockState(pos.add(2, 0, 0));
                stateReturn[1] = world.getBlockState(pos.east());
                stateReturn[2] = world.getBlockState(pos.add(2, 0, -1));
                stateReturn[3] = world.getBlockState(pos.add(1, 0, -1));
                stateReturn[4] = world.getBlockState(pos.north());
                break;
            case 4://Block is in upper right corner of horizontal orientation
                stateReturn[0] = world.getBlockState(pos.south());
                stateReturn[1] = world.getBlockState(pos.add(-1, 0, 1));
                stateReturn[2] = world.getBlockState(pos.add(-2, 0, 1));
                stateReturn[3] = world.getBlockState(pos.west());
                stateReturn[4] = world.getBlockState(pos.add(-2, 0, 0));
                break;
            case 5://Block is in upper middle of horizontal orientation
                stateReturn[0] = world.getBlockState(pos.add(1, 0, 1));
                stateReturn[1] = world.getBlockState(pos.south());
                stateReturn[2] = world.getBlockState(pos.add(-1, 0, 1));
                stateReturn[3] = world.getBlockState(pos.east());
                stateReturn[4] = world.getBlockState(pos.west());
                break;
            case 6://Block is in upper left corner of horizontal orientation
                stateReturn[0] = world.getBlockState(pos.add(2, 0, 1));
                stateReturn[1] = world.getBlockState(pos.add(1, 0, 1));
                stateReturn[2] = world.getBlockState(pos.south());
                stateReturn[3] = world.getBlockState(pos.add(2, 0, 0));
                stateReturn[4] = world.getBlockState(pos.east());
                break;
            case 7://Block is in lower left corner of vertical orientation
                stateReturn[0] = world.getBlockState(pos.north());
                stateReturn[1] = world.getBlockState(pos.add(0, 0, -2));
                stateReturn[2] = world.getBlockState(pos.east());
                stateReturn[3] = world.getBlockState(pos.add(1, 0, -1));
                stateReturn[4] = world.getBlockState(pos.add(1, 0, -2));
                break;
            case 8://Block is in middle left of vertical orientation
                stateReturn[0] = world.getBlockState(pos.south());
                stateReturn[1] = world.getBlockState(pos.north());
                stateReturn[2] = world.getBlockState(pos.add(1, 0, 1));
                stateReturn[3] = world.getBlockState(pos.east());
                stateReturn[4] = world.getBlockState(pos.add(1, 0, -1));
                break;
            case 9://Block is in upper left corner of vertical orientation
                stateReturn[0] = world.getBlockState(pos.add(0, 0, 2));
                stateReturn[1] = world.getBlockState(pos.south());
                stateReturn[2] = world.getBlockState(pos.add(1, 0, 2));
                stateReturn[3] = world.getBlockState(pos.add(1, 0, 1));
                stateReturn[4] = world.getBlockState(pos.east());
                break;
            case 10://Block is in lower right corner of vertical orientation
                stateReturn[0] = world.getBlockState(pos.west());
                stateReturn[1] = world.getBlockState(pos.add(-1, 0, -1));
                stateReturn[2] = world.getBlockState(pos.add(-1, 0, -2));
                stateReturn[3] = world.getBlockState(pos.north());
                stateReturn[4] = world.getBlockState(pos.add(0, 0, -2));
                break;
            case 11://Block is in middle right of vertical orientation
                stateReturn[0] = world.getBlockState(pos.add(-1, 0, 1));
                stateReturn[1] = world.getBlockState(pos.west());
                stateReturn[2] = world.getBlockState(pos.add(-1, 0, -1));
                stateReturn[3] = world.getBlockState(pos.south());
                stateReturn[4] = world.getBlockState(pos.north());
                break;
            case 12://Block is in upper right corner of vertical orientation
                stateReturn[0] = world.getBlockState(pos.add(-1, 0, 2));
                stateReturn[1] = world.getBlockState(pos.add(-1, 0, 1));
                stateReturn[2] = world.getBlockState(pos.west());
                stateReturn[3] = world.getBlockState(pos.add(0, 0, 2));
                stateReturn[4] = world.getBlockState(pos.south());
                break;
            default:
                stateReturn = null;
                break;
        }

        return stateReturn;
    }

    @Nullable
    public BlockState[] getBlockStatesAboveAltar(World world, BlockPos pos, int blockNum) {
        BlockState[] stateReturn = new BlockState[6];
        BlockPos altarBlockOne = getAltarBlockOne(pos, blockNum).up();

        if(blockNum < 7) {
            stateReturn[0] = world.getBlockState(altarBlockOne);//over 1
            stateReturn[1] = world.getBlockState(altarBlockOne.west());//over 2
            stateReturn[2] = world.getBlockState(altarBlockOne.add(-2,0,0));//over 3
            stateReturn[3] = world.getBlockState(altarBlockOne.north());//over 4
            stateReturn[4] = world.getBlockState(altarBlockOne.add(-1,0,-1));//over 5
            stateReturn[5] = world.getBlockState(altarBlockOne.add(-2,0,-1));//over 6
        }
        else if (blockNum>=7 && blockNum <= 12) {
            stateReturn[0] = world.getBlockState(altarBlockOne);//over 7
            stateReturn[1] = world.getBlockState(altarBlockOne.north());//over 8
            stateReturn[2] = world.getBlockState(altarBlockOne.add(0,0,-2));//over 9
            stateReturn[3] = world.getBlockState(altarBlockOne.east());//over 10
            stateReturn[4] = world.getBlockState(altarBlockOne.add(1,0,-1));//over 11
            stateReturn[5] = world.getBlockState(altarBlockOne.add(1,0,-2));//over 12
        } else {return null; }

        return stateReturn;

    }

    public BlockPos getAltarBlockOne(BlockPos pos, int blockNum) {//returns the block pos for either 1 or 7 in the altar
        BlockPos altarBlockOne;
        switch (blockNum) {//Centers altarBlockOne to the first block in the altar, 1 for horizontal, 7 for vertical
            case 1:
                altarBlockOne = pos;
                break;
            case 2:
                altarBlockOne = pos.east();
                break;
            case 3:
                altarBlockOne = pos.add(2,0,0);
                break;
            case 4:
                altarBlockOne = pos.south();
                break;
            case 5:
                altarBlockOne = pos.add(1,0,1);
                break;
            case 6:
                altarBlockOne = pos.add(2,0,1);
                break;
            case 7:
                altarBlockOne = pos;
                break;
            case 8:
                altarBlockOne = pos.south();
                break;
            case 9:
                altarBlockOne = pos.add(0,0,2);
                break;
            case 10:
                altarBlockOne = pos.west();
                break;
            case 11:
                altarBlockOne = pos.add(-1,0,1);
                break;
            case 12:
                altarBlockOne = pos.add(-1,0,2);
                break;
            default:
                return null;
        }
        return altarBlockOne;

    }



    public void invalidateAltar(World world, BlockPos pos, int blockNum) {
        BlockPos altarBlockOne = getAltarBlockOne(pos, blockNum);
        BlockState altarInvalid = world.getBlockState(pos).with(VALID, false).with(ALTAR_DISPLAY_TYPE, false);
        if (blockNum <= 6 && blockNum >= 1) {
            world.setBlockState(altarBlockOne, altarInvalid);//1
            world.setBlockState(altarBlockOne.west(), altarInvalid);//2
            world.setBlockState(altarBlockOne.add(-2,0,0), altarInvalid);//3
            world.setBlockState(altarBlockOne.north(), altarInvalid);//4
            world.setBlockState(altarBlockOne.add(-1,0,-1), altarInvalid);//5
            world.setBlockState(altarBlockOne.add(-2,0,-1), altarInvalid);//6
        }
        else if (blockNum <= 12 && blockNum >= 7) {
            world.setBlockState(altarBlockOne, altarInvalid);//7
            world.setBlockState(altarBlockOne.north(), altarInvalid);//8
            world.setBlockState(altarBlockOne.add(0,0,-2), altarInvalid);//9
            world.setBlockState(altarBlockOne.east(), altarInvalid);//10
            world.setBlockState(altarBlockOne.add(1,0,-1), altarInvalid);//11
            world.setBlockState(altarBlockOne.add(1,0,-2), altarInvalid);//12
        }
    }
}