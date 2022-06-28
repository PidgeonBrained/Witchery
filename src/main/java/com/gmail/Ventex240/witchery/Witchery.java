package com.gmail.Ventex240.witchery;

import com.gmail.Ventex240.witchery.block.BabysBreathBlock;
import com.gmail.Ventex240.witchery.block.WitchOven;
import com.gmail.Ventex240.witchery.block.entity.WitchOvenBlockEntity;
import com.gmail.Ventex240.witchery.item.Broom;
import com.gmail.Ventex240.witchery.item.Pepsi;
import com.gmail.Ventex240.witchery.recipe.WitchOvenRecipe;
import com.gmail.Ventex240.witchery.screen.WitchOvenScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class Witchery implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("witchery");


	public static final Pepsi PEPSI = new Pepsi(new FabricItemSettings().food(Pepsi.pepsiBehavior.build()).group(ItemGroup.FOOD));
	public static final Broom BROOM = new Broom(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1));
	public static final Item CLAY_POT = new Item(new FabricItemSettings().group(ItemGroup.MISC));




	public static final CropBlock BABYS_BREATH_BLOCK =
			new BabysBreathBlock(AbstractBlock.Settings.of(Material.PLANT).nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
	public static final Item BABYS_BREATH_SEEDS =
			new AliasedBlockItem(BABYS_BREATH_BLOCK, new Item.Settings().group(ItemGroup.MISC));
	public static final Item BABYS_BREATH_FLOWER = new Item(new FabricItemSettings().group(ItemGroup.MISC));


	public static final WitchOven WITCH_OVEN = new WitchOven(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3.5F));
	public static BlockEntityType<WitchOvenBlockEntity> WITCH_OVEN_ENTITY;
	public static ScreenHandlerType<WitchOvenScreenHandler> WITCH_OVEN_SCREEN_HANDLER;
	public static RecipeType<WitchOvenRecipe> WITCH_OVEN_RECIPE;
	public static RecipeSerializer<WitchOvenRecipe> WITCH_OVEN_RECIPE_SERIALIZER;



	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.WITCH_OVEN_ENTITY
		// Proceed with mild caution.

		LOGGER.info("Testing this shit out!");
		Registry.register(Registry.ITEM, new Identifier("witchery", "pepsi"), PEPSI);

		Registry.register(Registry.ITEM, new Identifier("witchery", "broom"), BROOM);

		Registry.register(Registry.ITEM, new Identifier("witchery", "clay_pot"), CLAY_POT);


		Registry.register(Registry.BLOCK, new Identifier("witchery","babys_breath_block"), BABYS_BREATH_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("witchery","babys_breath_seeds"), BABYS_BREATH_SEEDS);
		Registry.register(Registry.ITEM, new Identifier("witchery","babys_breath_flower"), BABYS_BREATH_FLOWER);

		Registry.register(Registry.BLOCK, new Identifier("witchery", "witch_oven"), WITCH_OVEN);
		Registry.register(Registry.ITEM, new Identifier("witchery", "witch_oven"), new BlockItem(WITCH_OVEN, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
		WITCH_OVEN_ENTITY = Registry.register(
				Registry.BLOCK_ENTITY_TYPE, "witchery:witch_oven_entity", FabricBlockEntityTypeBuilder.create(WitchOvenBlockEntity::new, WITCH_OVEN).build(null));
		WITCH_OVEN_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier("witchery", "witch_oven"), WitchOvenScreenHandler::new);

		WITCH_OVEN_RECIPE = Registry.register(Registry.RECIPE_TYPE, new Identifier("witchery", "witch_oven"), new RecipeType<WitchOvenRecipe>() {
			@Override
			public String toString() {
					return "witch_oven_recipe";
			}
		});
		//WITCH_OVEN_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
				//new Identifier("witchery", "test_furnace"), new CookingRecipeSerializer(WitchOvenRecipe::new, 200));

	}
}