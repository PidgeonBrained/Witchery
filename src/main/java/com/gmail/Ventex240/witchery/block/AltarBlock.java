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
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends Block { //Im thinking it'll only check that there's 6 blocks when the cloth is place over the blocks


    public static final BooleanProperty VALID; //False if in block form, True if cloth is draped over it
    public static final BooleanProperty ALTAR_DISPLAY_TYPE; //False for middle, true for corner
    public static final IntProperty BLOCK_NUM; //Defaults to 0 if not in a valid altar. Otherwise, should be assigned to 0-12. See documentation for the specifics of the numbering


    private World world; //Unfortunately I have to save the position and the world so that it can be updated in piston Todo:get rid of saving the world
    private BlockPos pos;


    static {
        VALID = Properties.ENABLED;
        ALTAR_DISPLAY_TYPE = Properties.CONDITIONAL; //TODO:properly implement blockstates, etc.
        BLOCK_NUM = Properties.AGE_15;
    }


    public AltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(VALID,false).with(ALTAR_DISPLAY_TYPE,false).with(BLOCK_NUM, 0));
    }


    class AltarBlockHelperFactory {
        private AltarBlock altar1, altar2, altar3, altar4, altar5, altar6;
        public AltarBlockHelperFactory() {

        }
        public void setBlockOne(AltarBlock block) {
            altar1 = block;
        }
        public void setBlockTwo(AltarBlock block) {
            altar2 = block;
        }
        public void setBlockThree(AltarBlock block) {
            altar3 = block;
        }
        public void setBlockFour(AltarBlock block) {
            altar4 = block;
        }
        public void setBlockFive(AltarBlock block) {
            altar5 = block;
        }
        public void setBlockSix(AltarBlock block) {
            altar6 = block;
        }

        public void invalidateOne() {
            altar1.invalidateSelf();
        }


    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        this.world = world;
        this.pos = pos;
        super.onPlaced(world, pos, state, placer, itemStack);
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VALID, ALTAR_DISPLAY_TYPE, BLOCK_NUM);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state == state.with(VALID, false) && !player.isSpectator()) { //If altar block is currently invalid, also checks if the player is in spectator
            if (((hand == Hand.MAIN_HAND && player.getMainHandStack().isOf(Witchery.ALTAR_CLOTH)))) { //if player right clicks altar with altar cloth
                if(world.isClient()) {
                    return ActionResult.SUCCESS;
                }
                else {
                    int tempBlockNum = this.isAltarValid(world, pos);
                    if (tempBlockNum != 0) {
                        if (!player.isCreative()) { //Will only consume cloth if player is in survival or adventure
                            player.getMainHandStack().decrement(1);
                        }

                        validateAltar(world, pos, tempBlockNum);
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
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {


        if (moved) { //The block closest to the piston will drop the cloth, as well as handle the unmoved blocks. The other moved blocks, if there are any, will handle themselves.
            if (newState == newState.with(VALID, true)) { //If altar was valid when it was moved
                int blockNum = getBlockNum(newState);

                if (pos == this.pos.up() || pos == this.pos.down()) { //if 1 block was moved
                    int[] blocksToInvalidate;
                    switch (blockNum){
                        case 1: blocksToInvalidate = new int[]{2, 3, 4, 5, 6}; break;
                        case 2: blocksToInvalidate = new int[]{1, 3, 4, 5, 6}; break;
                        case 3: blocksToInvalidate = new int[]{1, 2, 4, 5, 6}; break;
                        case 4: blocksToInvalidate = new int[]{1, 2, 3, 5, 6}; break;
                        case 5: blocksToInvalidate = new int[]{1, 2, 3, 4, 6}; break;
                        case 6: blocksToInvalidate = new int[]{1, 2, 3, 4, 5}; break;
                        case 7: blocksToInvalidate = new int[]{8, 9, 10, 11, 12}; break;
                        case 8: blocksToInvalidate = new int[]{7, 9, 10, 11, 12}; break;
                        case 9: blocksToInvalidate = new int[]{7, 8, 10, 11, 12}; break;
                        case 10: blocksToInvalidate = new int[]{7, 8, 9, 11, 12}; break;
                        case 11: blocksToInvalidate = new int[]{7, 8, 9, 10, 12}; break;
                        case 12: blocksToInvalidate = new int[]{7, 8, 9, 10, 11}; break;
                        default: blocksToInvalidate = new int[]{}; break;
                    }

                    invalidateAltar(world, this.pos, getBlockNum(state), blocksToInvalidate); //Uses old pos
                    invalidateSelf(world, pos); //Uses new pos
                    ItemStack altarCloth = new ItemStack(Witchery.ALTAR_CLOTH);
                    dropStack(this.world, this.pos, altarCloth);
                }
                else { //if 2 or 3 blocks were moved
                    switch(blockNum) {
                        case 1:
                            if (pos == this.pos)

                    }
                }

            }
        }
        //TODO: add functionality for breaking the block when the altar is valid
        this.world = world;
        this.pos = pos;
//
        super.onStateReplaced(state, world, pos, newState, moved);

    }


    @Override
    public PistonBehavior getPistonBehavior(BlockState state) { //TODO: stop using this
        /*if (state == state.with(VALID, true)) {
            invalidateAltar(this.world, this.pos, getBlockNum(state));
            ItemStack altarCloth = new ItemStack(Witchery.ALTAR_CLOTH);
            dropStack(this.world, this.pos, altarCloth);

        }*/

        return super.getPistonBehavior(state);
    }



    
    public int isAltarValid(World world, BlockPos pos) { //Returns 1-12 if altar is valid, and 0 if it's not valid
        //Checks that the nearby altar blocks aren't part of another altar, and that there isn't stuff above the altar.

        BlockState[] nearbyBlocks, blocksAbove;
        for (int i=1;i<=12;i++) {
            nearbyBlocks = getNearbyBlockStates(world, pos, i);
            blocksAbove = getBlockStatesAboveAltar(world, pos, i);
            for (int j = 0; j<5;j++) {
                if ((!nearbyBlocks[j].isOf(Witchery.ALTAR_BLOCK)) || (nearbyBlocks[j] == nearbyBlocks[j].with(VALID, true)) || !(blocksAbove[j].isOf(Blocks.AIR))) {
                    //TODO: maybe make it so that the blocks above don't have to be air
                    break;
                }

                if (j==4) {
                    return i;
                }
            }
        }

        return 0;
    }


    public void validateAltar(World world, BlockPos pos, int blockNum) {
        //Updates the blockstates of all the nearby blocks to form altar
        BlockPos altarBlockOne = getAltarBlockOne(pos, blockNum);
        BlockState altarCorner = world.getBlockState(pos).with(VALID, true).with(ALTAR_DISPLAY_TYPE, true);
        BlockState altarEdge = world.getBlockState(pos).with(VALID, true).with(ALTAR_DISPLAY_TYPE, false);
        if (blockNum <= 6 && blockNum >= 1) {
            world.setBlockState(altarBlockOne, altarCorner.with(BLOCK_NUM, 1));
            world.setBlockState(altarBlockOne.west(), altarEdge.with(BLOCK_NUM, 2));
            world.setBlockState(altarBlockOne.add(-2,0,0), altarCorner.with(BLOCK_NUM, 3));
            world.setBlockState(altarBlockOne.north(), altarCorner.with(BLOCK_NUM, 4));
            world.setBlockState(altarBlockOne.add(-1,0,-1), altarEdge.with(BLOCK_NUM, 5));
            world.setBlockState(altarBlockOne.add(-2,0,-1), altarCorner.with(BLOCK_NUM, 6));
        }
        else if (blockNum <= 12 && blockNum >= 7) {
            world.setBlockState(altarBlockOne, altarCorner.with(BLOCK_NUM, 7));
            world.setBlockState(altarBlockOne.north(), altarEdge.with(BLOCK_NUM, 8));
            world.setBlockState(altarBlockOne.add(0,0,-2), altarCorner.with(BLOCK_NUM, 9));
            world.setBlockState(altarBlockOne.east(), altarCorner.with(BLOCK_NUM, 10));
            world.setBlockState(altarBlockOne.add(1,0,-1), altarEdge.with(BLOCK_NUM, 11));
            world.setBlockState(altarBlockOne.add(1,0,-2), altarCorner.with(BLOCK_NUM, 12));
        }

    }


    public void invalidateAltar(World world, BlockPos pos, int blockNum) {
        //Updates the blockstates of all the blocks part of the same altar in order to invalid it
        BlockPos altarBlockOne = getAltarBlockOne(pos, blockNum);
        BlockState altarInvalid = world.getBlockState(pos).with(VALID, false).with(ALTAR_DISPLAY_TYPE, false);
        if (blockNum <= 6 && blockNum >= 1) {
            world.setBlockState(altarBlockOne, altarInvalid.with(BLOCK_NUM, 0));//1
            world.setBlockState(altarBlockOne.west(), altarInvalid.with(BLOCK_NUM, 0));//2
            world.setBlockState(altarBlockOne.add(-2,0,0), altarInvalid.with(BLOCK_NUM, 0));//3
            world.setBlockState(altarBlockOne.north(), altarInvalid.with(BLOCK_NUM, 0));//4
            world.setBlockState(altarBlockOne.add(-1,0,-1), altarInvalid.with(BLOCK_NUM, 0));//5
            world.setBlockState(altarBlockOne.add(-2,0,-1), altarInvalid.with(BLOCK_NUM, 0));//6
        }
        else if (blockNum <= 12 && blockNum >= 7) {
            world.setBlockState(altarBlockOne, altarInvalid.with(BLOCK_NUM, 0));//7
            world.setBlockState(altarBlockOne.north(), altarInvalid.with(BLOCK_NUM, 0));//8
            world.setBlockState(altarBlockOne.add(0,0,-2), altarInvalid.with(BLOCK_NUM, 0));//9
            world.setBlockState(altarBlockOne.east(), altarInvalid.with(BLOCK_NUM, 0));//10
            world.setBlockState(altarBlockOne.add(1,0,-1), altarInvalid.with(BLOCK_NUM, 0));//11
            world.setBlockState(altarBlockOne.add(1,0,-2), altarInvalid.with(BLOCK_NUM, 0));//12
        }
    }

    public void invalidateAltar(World world, BlockPos pos, int blockNum, int[] blockNumsToInvalidate) {
        //invalidate the altar partially by specifying what blockNums you want to invalidate. Be sure to pass this method the block's old pos if it moved.
        BlockPos altarBlockOne = getAltarBlockOne(pos, blockNum);
        BlockState altarInvalid = world.getBlockState(pos).with(VALID, false).with(ALTAR_DISPLAY_TYPE, false);
        for(int invalidBlockNum : blockNumsToInvalidate){ //Enhanced for loop
            switch (invalidBlockNum) {
                case 1:
                    world.setBlockState(altarBlockOne, altarInvalid.with(BLOCK_NUM, 0));//1
                    break;
                case 2:
                    world.setBlockState(altarBlockOne.west(), altarInvalid.with(BLOCK_NUM, 0));//2
                    break;
                case 3:
                    world.setBlockState(altarBlockOne.add(-2,0,0), altarInvalid.with(BLOCK_NUM, 0));//3
                    break;
                case 4:
                    world.setBlockState(altarBlockOne.north(), altarInvalid.with(BLOCK_NUM, 0));//4
                    break;
                case 5:
                    world.setBlockState(altarBlockOne.add(-1,0,-1), altarInvalid.with(BLOCK_NUM, 0));//5
                    break;
                case 6:
                    world.setBlockState(altarBlockOne.add(-2,0,-1), altarInvalid.with(BLOCK_NUM, 0));//6
                    break;
                case 7:
                    world.setBlockState(altarBlockOne, altarInvalid.with(BLOCK_NUM, 0));//7
                    break;
                case 8:
                    world.setBlockState(altarBlockOne.north(), altarInvalid.with(BLOCK_NUM, 0));//8
                    break;
                case 9:
                    world.setBlockState(altarBlockOne.add(0,0,-2), altarInvalid.with(BLOCK_NUM, 0));//9
                    break;
                case 10:
                    world.setBlockState(altarBlockOne.east(), altarInvalid.with(BLOCK_NUM, 0));//10
                    break;
                case 11:
                    world.setBlockState(altarBlockOne.add(1,0,-1), altarInvalid.with(BLOCK_NUM, 0));//11
                    break;
                case 12:
                    world.setBlockState(altarBlockOne.add(1,0,-2), altarInvalid.with(BLOCK_NUM, 0));//12
                    break;
            }
        }

    }

    public void invalidateSelf(World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(VALID, false).with(ALTAR_DISPLAY_TYPE, false).with(BLOCK_NUM, 0));
    }
    


    @Nullable
    public BlockState[] getNearbyBlockStates (World world, BlockPos pos, int blockNum) {
        //Grabs the blockstates of the 5 other blocks around the block specified, and wraps them up into an array.
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
        //Gets the blockstates of the blocks above the altar and wraps them up into an array
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

    public BlockPos getAltarBlockOne(BlockPos pos, int blockNum) {
        //returns the block pos for either 1 or 7 in the altar
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

    public int getBlockNum(BlockState state) {
        //Gets the block num from the blockstate
        if (state == state.with(BLOCK_NUM, 1)){
            return 1;
        }
        else if (state == state.with(BLOCK_NUM, 2)){
            return 2;
        }
        else if (state == state.with(BLOCK_NUM, 3)){
            return 3;
        }
        else if (state == state.with(BLOCK_NUM, 4)){
            return 4;
        }
        else if (state == state.with(BLOCK_NUM, 5)){
            return 5;
        }
        else if (state == state.with(BLOCK_NUM, 6)){
            return 6;
        }
        else if (state == state.with(BLOCK_NUM, 7)){
            return 7;
        }
        else if (state == state.with(BLOCK_NUM, 8)){
            return 8;
        }
        else if (state == state.with(BLOCK_NUM, 9)){
            return 9;
        }
        else if (state == state.with(BLOCK_NUM, 10)){
            return 10;
        }
        else if (state == state.with(BLOCK_NUM, 11)){
            return 11;
        }
        else if (state == state.with(BLOCK_NUM, 12)){
            return 12;
        }
        else {
            return 0;
        }
    }


}