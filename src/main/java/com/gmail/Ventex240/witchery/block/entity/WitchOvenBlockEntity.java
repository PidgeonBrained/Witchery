package com.gmail.Ventex240.witchery.block.entity;

import com.gmail.Ventex240.witchery.Witchery;
import com.gmail.Ventex240.witchery.screen.WitchOvenScreenHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.SharedConstants;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WitchOvenBlockEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider {
    protected static final int field_31286 = 0;
    protected static final int field_31287 = 1;
    protected static final int field_31288 = 2;
    public static final int field_31289 = 0;
    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2, 1};
    private static final int[] SIDE_SLOTS = new int[]{1};
    public static final int field_31290 = 1;
    public static final int field_31291 = 2;
    public static final int field_31292 = 3;
    public static final int field_31293 = 4;
    public static final int field_31294 = 200;
    public static final int field_31295 = 2;
    protected DefaultedList<ItemStack> inventory;
    int burnTime;
    int fuelTime;
    int cookTime;
    int cookTimeTotal;
    protected final PropertyDelegate propertyDelegate;
    private final Object2IntOpenHashMap<Identifier> recipesUsed;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;

    public WitchOvenBlockEntity(BlockPos pos, BlockState state) {
        this(Witchery.WITCH_OVEN_ENTITY, pos, state, RecipeType.SMELTING); //TODO: CHANGE RECIPE TYPE
    }
    public WitchOvenBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType) {
        super(blockEntityType, pos, state);
        this.inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch(index) {
                    case 0:
                        return WitchOvenBlockEntity.this.burnTime;
                    case 1:
                        return WitchOvenBlockEntity.this.fuelTime;
                    case 2:
                        return WitchOvenBlockEntity.this.cookTime;
                    case 3:
                        return WitchOvenBlockEntity.this.cookTimeTotal;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0:
                        WitchOvenBlockEntity.this.burnTime = value;
                        break;
                    case 1:
                        WitchOvenBlockEntity.this.fuelTime = value;
                        break;
                    case 2:
                        WitchOvenBlockEntity.this.cookTime = value;
                        break;
                    case 3:
                        WitchOvenBlockEntity.this.cookTimeTotal = value;
                }

            }

            public int size() {
                return 4;
            }
        };
        this.recipesUsed = new Object2IntOpenHashMap();
        this.recipeType = recipeType;
    }

    public static Map<Item, Integer> createFuelTimeMap() {
        Map<Item, Integer> map = Maps.newLinkedHashMap();
        addFuel(map, (ItemConvertible)Items.LAVA_BUCKET, 20000);
        addFuel(map, (ItemConvertible)Blocks.COAL_BLOCK, 16000);
        addFuel(map, (ItemConvertible)Items.BLAZE_ROD, 2400);
        addFuel(map, (ItemConvertible)Items.COAL, 1600);
        addFuel(map, (ItemConvertible)Items.CHARCOAL, 1600);
        addFuel(map, (TagKey)ItemTags.LOGS, 300);
        addFuel(map, (TagKey)ItemTags.PLANKS, 300);
        addFuel(map, (TagKey)ItemTags.WOODEN_STAIRS, 300);
        addFuel(map, (TagKey)ItemTags.WOODEN_SLABS, 150);
        addFuel(map, (TagKey)ItemTags.WOODEN_TRAPDOORS, 300);
        addFuel(map, (TagKey)ItemTags.WOODEN_PRESSURE_PLATES, 300);
        addFuel(map, (ItemConvertible)Blocks.OAK_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.BIRCH_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.SPRUCE_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.JUNGLE_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.DARK_OAK_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.ACACIA_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.OAK_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.BIRCH_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.SPRUCE_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.JUNGLE_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.DARK_OAK_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.ACACIA_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.NOTE_BLOCK, 300);
        addFuel(map, (ItemConvertible)Blocks.BOOKSHELF, 300);
        addFuel(map, (ItemConvertible)Blocks.LECTERN, 300);
        addFuel(map, (ItemConvertible)Blocks.JUKEBOX, 300);
        addFuel(map, (ItemConvertible)Blocks.CHEST, 300);
        addFuel(map, (ItemConvertible)Blocks.TRAPPED_CHEST, 300);
        addFuel(map, (ItemConvertible)Blocks.CRAFTING_TABLE, 300);
        addFuel(map, (ItemConvertible)Blocks.DAYLIGHT_DETECTOR, 300);
        addFuel(map, (TagKey)ItemTags.BANNERS, 300);
        addFuel(map, (ItemConvertible)Items.BOW, 300);
        addFuel(map, (ItemConvertible)Items.FISHING_ROD, 300);
        addFuel(map, (ItemConvertible)Blocks.LADDER, 300);
        addFuel(map, (TagKey)ItemTags.SIGNS, 200);
        addFuel(map, (ItemConvertible)Items.WOODEN_SHOVEL, 200);
        addFuel(map, (ItemConvertible)Items.WOODEN_SWORD, 200);
        addFuel(map, (ItemConvertible)Items.WOODEN_HOE, 200);
        addFuel(map, (ItemConvertible)Items.WOODEN_AXE, 200);
        addFuel(map, (ItemConvertible)Items.WOODEN_PICKAXE, 200);
        addFuel(map, (TagKey)ItemTags.WOODEN_DOORS, 200);
        addFuel(map, (TagKey)ItemTags.BOATS, 1200);
        addFuel(map, (TagKey)ItemTags.WOOL, 100);
        addFuel(map, (TagKey)ItemTags.WOODEN_BUTTONS, 100);
        addFuel(map, (ItemConvertible)Items.STICK, 100);
        addFuel(map, (TagKey)ItemTags.SAPLINGS, 100);
        addFuel(map, (ItemConvertible)Items.BOWL, 100);
        addFuel(map, (TagKey)ItemTags.CARPETS, 67);
        addFuel(map, (ItemConvertible)Blocks.DRIED_KELP_BLOCK, 4001);
        addFuel(map, (ItemConvertible)Items.CROSSBOW, 300);
        addFuel(map, (ItemConvertible)Blocks.BAMBOO, 50);
        addFuel(map, (ItemConvertible)Blocks.DEAD_BUSH, 100);
        addFuel(map, (ItemConvertible)Blocks.SCAFFOLDING, 400);
        addFuel(map, (ItemConvertible)Blocks.LOOM, 300);
        addFuel(map, (ItemConvertible)Blocks.BARREL, 300);
        addFuel(map, (ItemConvertible)Blocks.CARTOGRAPHY_TABLE, 300);
        addFuel(map, (ItemConvertible)Blocks.FLETCHING_TABLE, 300);
        addFuel(map, (ItemConvertible)Blocks.SMITHING_TABLE, 300);
        addFuel(map, (ItemConvertible)Blocks.COMPOSTER, 300);
        addFuel(map, (ItemConvertible)Blocks.AZALEA, 100);
        addFuel(map, (ItemConvertible)Blocks.FLOWERING_AZALEA, 100);
        return map;
    }

    private static boolean isNonFlammableWood(Item item) {
        return item.getRegistryEntry().isIn(ItemTags.NON_FLAMMABLE_WOOD);
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime) {
        Iterator var3 = Registry.ITEM.iterateEntries(tag).iterator();

        while(var3.hasNext()) {
            RegistryEntry<Item> registryEntry = (RegistryEntry)var3.next();
            if (!isNonFlammableWood((Item)registryEntry.value())) {
                fuelTimes.put((Item)registryEntry.value(), fuelTime);
            }
        }

    }

    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        Item item2 = item.asItem();
        if (isNonFlammableWood(item2)) {
            if (SharedConstants.isDevelopment) {
                throw (IllegalStateException)Util.throwOrPause(new IllegalStateException("A developer tried to explicitly make fire resistant item " + item2.getName((ItemStack)null).getString() + " a furnace fuel. That will not work!"));
            }
        } else {
            fuelTimes.put(item2, fuelTime);
        }
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        this.burnTime = nbt.getShort("BurnTime");
        this.cookTime = nbt.getShort("CookTime");
        this.cookTimeTotal = nbt.getShort("CookTimeTotal");
        this.fuelTime = this.getFuelTime((ItemStack)this.inventory.get(1));
        NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");
        Iterator var3 = nbtCompound.getKeys().iterator();

        while(var3.hasNext()) {
            String string = (String)var3.next();
            this.recipesUsed.put(new Identifier(string), nbtCompound.getInt(string));
        }

    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("BurnTime", (short)this.burnTime);
        nbt.putShort("CookTime", (short)this.cookTime);
        nbt.putShort("CookTimeTotal", (short)this.cookTimeTotal);
        Inventories.writeNbt(nbt, this.inventory);
        NbtCompound nbtCompound = new NbtCompound();
        this.recipesUsed.forEach((identifier, count) -> {
            nbtCompound.putInt(identifier.toString(), count);
        });
        nbt.put("RecipesUsed", nbtCompound);
    }

    public static void tick(World world, BlockPos pos, BlockState state, WitchOvenBlockEntity blockEntity) {
        boolean bl = blockEntity.isBurning();
        boolean bl2 = false;
        if (blockEntity.isBurning()) {
            --blockEntity.burnTime;
        }

        ItemStack itemStack = (ItemStack)blockEntity.inventory.get(1);
        if (!blockEntity.isBurning() && (itemStack.isEmpty() || ((ItemStack)blockEntity.inventory.get(0)).isEmpty())) {
            if (!blockEntity.isBurning() && blockEntity.cookTime > 0) {
                blockEntity.cookTime = MathHelper.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
            }
        } else {
            Recipe<?> recipe = (Recipe)world.getRecipeManager().getFirstMatch(blockEntity.recipeType, blockEntity, world).orElse(null);
            int i = blockEntity.getMaxCountPerStack();
            if (!blockEntity.isBurning() && canAcceptRecipeOutput(recipe, blockEntity.inventory, i)) {
                blockEntity.burnTime = blockEntity.getFuelTime(itemStack);
                blockEntity.fuelTime = blockEntity.burnTime;
                if (blockEntity.isBurning()) {
                    bl2 = true;
                    if (!itemStack.isEmpty()) {
                        Item item = itemStack.getItem();
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getRecipeRemainder();
                            blockEntity.inventory.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                        }
                    }
                }
            }

            if (blockEntity.isBurning() && canAcceptRecipeOutput(recipe, blockEntity.inventory, i)) {
                ++blockEntity.cookTime;
                if (blockEntity.cookTime == blockEntity.cookTimeTotal) {
                    blockEntity.cookTime = 0;
                    blockEntity.cookTimeTotal = getCookTime(world, blockEntity.recipeType, blockEntity);
                    if (craftRecipe(recipe, blockEntity.inventory, i)) {
                        blockEntity.setLastRecipe(recipe);
                    }

                    bl2 = true;
                }
            } else {
                blockEntity.cookTime = 0;
            }
        }

        if (bl != blockEntity.isBurning()) {
            bl2 = true;
            state = (BlockState)state.with(AbstractFurnaceBlock.LIT, blockEntity.isBurning());
            world.setBlockState(pos, state, 3);
        }

        if (bl2) {
            markDirty(world, pos, state);
        }

    }

    private static boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (!((ItemStack)slots.get(0)).isEmpty() && recipe != null) {
            ItemStack itemStack = recipe.getOutput();
            if (itemStack.isEmpty()) {
                return false;
            } else {
                ItemStack itemStack2 = (ItemStack)slots.get(2);
                if (itemStack2.isEmpty()) {
                    return true;
                } else if (!itemStack2.isItemEqualIgnoreDamage(itemStack)) {
                    return false;
                } else if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxCount()) {
                    return true;
                } else {
                    return itemStack2.getCount() < itemStack.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    private static boolean craftRecipe(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (recipe != null && canAcceptRecipeOutput(recipe, slots, count)) {
            ItemStack itemStack = (ItemStack)slots.get(0);
            ItemStack itemStack2 = recipe.getOutput();
            ItemStack itemStack3 = (ItemStack)slots.get(2);
            if (itemStack3.isEmpty()) {
                slots.set(2, itemStack2.copy());
            } else if (itemStack3.isOf(itemStack2.getItem())) {
                itemStack3.increment(1);
            }

            if (itemStack.isOf(Blocks.WET_SPONGE.asItem()) && !((ItemStack)slots.get(1)).isEmpty() && ((ItemStack)slots.get(1)).isOf(Items.BUCKET)) {
                slots.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemStack.decrement(1);
            return true;
        } else {
            return false;
        }
    }

    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return (Integer)createFuelTimeMap().getOrDefault(item, 0);
        }
    }

    private static int getCookTime(World world, RecipeType<? extends AbstractCookingRecipe> recipeType, Inventory inventory) {
        return (Integer)world.getRecipeManager().getFirstMatch(recipeType, inventory, world).map(AbstractCookingRecipe::getCookTime).orElse(200);
    }

    public static boolean canUseAsFuel(ItemStack stack) {
        return createFuelTimeMap().containsKey(stack.getItem());
    }

    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        } else {
            return side == Direction.UP ? TOP_SLOTS : SIDE_SLOTS;
        }
    }

    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot == 1) {
            return stack.isOf(Items.WATER_BUCKET) || stack.isOf(Items.BUCKET);
        } else {
            return true;
        }
    }

    public int size() {
        return this.inventory.size();
    }

    public boolean isEmpty() {
        Iterator var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    public ItemStack getStack(int slot) {
        return (ItemStack)this.inventory.get(slot);
    }

    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = (ItemStack)this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areNbtEqual(stack, itemStack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        if (slot == 0 && !bl) {
            this.cookTimeTotal = getCookTime(this.world, this.recipeType, this);
            this.cookTime = 0;
            this.markDirty();
        }

    }

    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 2) {
            return false;
        } else if (slot != 1) {
            return true;
        } else {
            ItemStack itemStack = (ItemStack)this.inventory.get(1);
            return canUseAsFuel(stack) || stack.isOf(Items.BUCKET) && !itemStack.isOf(Items.BUCKET);
        }
    }

    public void clear() {
        this.inventory.clear();
    }

    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            Identifier identifier = recipe.getId();
            this.recipesUsed.addTo(identifier, 1);
        }

    }

    @Nullable
    public Recipe<?> getLastRecipe() {
        return null;
    }

    public void unlockLastRecipe(PlayerEntity player) {
    }

    public void dropExperienceForRecipesUsed(ServerPlayerEntity player) {
        List<Recipe<?>> list = this.getRecipesUsedAndDropExperience(player.getWorld(), player.getPos());
        player.unlockRecipes(list);
        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesUsedAndDropExperience(ServerWorld world, Vec3d pos) {
        List<Recipe<?>> list = Lists.newArrayList();
        ObjectIterator var4 = this.recipesUsed.object2IntEntrySet().iterator();

        while(var4.hasNext()) {
            Entry<Identifier> entry = (Entry)var4.next();
            world.getRecipeManager().get((Identifier)entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                dropExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipe).getExperience());
            });
        }

        return list;
    }

    private static void dropExperience(ServerWorld world, Vec3d pos, int multiplier, float experience) {
        int i = MathHelper.floor((float)multiplier * experience);
        float f = MathHelper.fractionalPart((float)multiplier * experience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        ExperienceOrbEntity.spawn(world, pos, i);
    }

    public void provideRecipeInputs(RecipeMatcher finder) {
        Iterator var2 = this.inventory.iterator();

        while(var2.hasNext()) {
            ItemStack itemStack = (ItemStack)var2.next();
            finder.addInput(itemStack);
        }

    }
    protected Text getContainerName() {
        return new TranslatableText("container.witch_oven");
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new WitchOvenScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
