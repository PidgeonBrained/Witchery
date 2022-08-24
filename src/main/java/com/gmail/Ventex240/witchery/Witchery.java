package com.gmail.Ventex240.witchery;

import com.gmail.Ventex240.witchery.block.AltarBlock;
import com.gmail.Ventex240.witchery.block.BabysBreathBlock;
import com.gmail.Ventex240.witchery.block.HollowLog;
import com.gmail.Ventex240.witchery.block.WitchOven;
import com.gmail.Ventex240.witchery.block.entity.HollowLogBlockEntity;
import com.gmail.Ventex240.witchery.block.entity.WitchOvenBlockEntity;
import com.gmail.Ventex240.witchery.item.Broom;
import com.gmail.Ventex240.witchery.item.Pepsi;
import com.gmail.Ventex240.witchery.recipe.WitchOvenRecipe;
import com.gmail.Ventex240.witchery.screen.WitchOvenScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.*;
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

	public static final Item ALCHEMIC_POPPET = new Item(new FabricItemSettings().group(ItemGroup.MISC)); //TODO:add witchery tab
	public static final Item ALMIGHTY_SOUL = new Item(new FabricItemSettings().group(ItemGroup.MISC));

	public static final AltarBlock ALTAR_BLOCK = new AltarBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.5F));
	public static final Item ALTAR_CLOTH = new Item(new FabricItemSettings().group(ItemGroup.MISC)); //TODO: change

	public static final CropBlock BABYS_BREATH_BLOCK =
			new BabysBreathBlock(AbstractBlock.Settings.of(Material.PLANT).nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
	public static final Item BABYS_BREATH_SEEDS =
			new AliasedBlockItem(BABYS_BREATH_BLOCK, new Item.Settings().group(ItemGroup.MISC));
	public static final Item BABYS_BREATH_FLOWER = new Item(new FabricItemSettings().group(ItemGroup.MISC));

	public static final Item BREATH_OF_LIFE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item BREATH_OF_THE_WOODS = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Broom BROOM = new Broom(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1));
	public static final Item CLAY_POT = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMMON_SOUL = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item DIVINE_POPPET = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item ESSENCE_OF_MAGIC = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item ESSENCE_OF_MALICE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item ESSENCE_OF_PURITY = new Item(new FabricItemSettings().group(ItemGroup.MISC));


	public static final HollowLog ACACIA_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog BIRCH_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog DARK_OAK_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog JUNGLE_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog OAK_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog SPRUCE_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog STRIPPED_ACACIA_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog STRIPPED_BIRCH_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog STRIPPED_DARK_OAK_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog STRIPPED_JUNGLE_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog STRIPPED_OAK_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final HollowLog STRIPPED_SPRUCE_HOLLOW_LOG = new HollowLog(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));

	public static BlockEntityType<HollowLogBlockEntity> HOLLOW_LOG_BLOCK_ENTITY;


	public static final Item MANDRAKE_ROOT = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item MANDRAKE_SEEDS = new Item(new FabricItemSettings().group(ItemGroup.MISC));

	public static final Pepsi PEPSI = new Pepsi(new FabricItemSettings().food(Pepsi.pepsiBehavior.build()).group(ItemGroup.FOOD));
	public static final Item POPPET = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item PROTECTION_POPPET = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item STRONG_SOUL = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item TRACE_OF_MAGIC = new Item(new FabricItemSettings().group(ItemGroup.MISC));


	public static final WitchOven WITCH_OVEN = new WitchOven(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3.5F));
	public static BlockEntityType<WitchOvenBlockEntity> WITCH_OVEN_ENTITY;
	public static ScreenHandlerType<WitchOvenScreenHandler> WITCH_OVEN_SCREEN_HANDLER;
	public static RecipeType<WitchOvenRecipe> WITCH_OVEN_RECIPE;



	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized
		// Proceed with mild caution.


		Registry.register(Registry.ITEM, new Identifier("witchery", "alchemic_poppet"), ALCHEMIC_POPPET);
		Registry.register(Registry.ITEM, new Identifier("witchery", "almighty_soul"), ALMIGHTY_SOUL);

		Registry.register(Registry.BLOCK, new Identifier("witchery", "altar_block"), ALTAR_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("witchery", "altar_block"), new BlockItem(ALTAR_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
		Registry.register(Registry.ITEM, new Identifier("witchery", "altar_cloth"), ALTAR_CLOTH);

		Registry.register(Registry.BLOCK, new Identifier("witchery","babys_breath_block"), BABYS_BREATH_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("witchery","babys_breath_seeds"), BABYS_BREATH_SEEDS);
		Registry.register(Registry.ITEM, new Identifier("witchery","babys_breath_flower"), BABYS_BREATH_FLOWER);

		Registry.register(Registry.ITEM, new Identifier("witchery", "breath_of_life"), BREATH_OF_LIFE);
		Registry.register(Registry.ITEM, new Identifier("witchery", "breath_of_the_woods"), BREATH_OF_THE_WOODS);
		Registry.register(Registry.ITEM, new Identifier("witchery", "broom"), BROOM);
		Registry.register(Registry.ITEM, new Identifier("witchery", "clay_pot"), CLAY_POT);
		Registry.register(Registry.ITEM, new Identifier("witchery", "common_soul"), COMMON_SOUL);
		Registry.register(Registry.ITEM, new Identifier("witchery", "divine_poppet"), DIVINE_POPPET);
		Registry.register(Registry.ITEM, new Identifier("witchery", "essence_of_magic"), ESSENCE_OF_MAGIC);
		Registry.register(Registry.ITEM, new Identifier("witchery", "essence_of_malice"), ESSENCE_OF_MALICE);
		Registry.register(Registry.ITEM, new Identifier("witchery", "essence_of_purity"), ESSENCE_OF_PURITY);

		
		Registry.register(Registry.BLOCK, new Identifier("witchery", "acacia_hollow_log"), ACACIA_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "acacia_hollow_log"), new BlockItem(ACACIA_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "birch_hollow_log"), BIRCH_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "birch_hollow_log"), new BlockItem(BIRCH_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "dark_oak_hollow_log"), DARK_OAK_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "dark_oak_hollow_log"), new BlockItem(DARK_OAK_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "jungle_hollow_log"), JUNGLE_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "jungle_hollow_log"), new BlockItem(JUNGLE_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "oak_hollow_log"), OAK_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "oak_hollow_log"), new BlockItem(OAK_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "spruce_hollow_log"), SPRUCE_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "spruce_hollow_log"), new BlockItem(SPRUCE_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "stripped_acacia_hollow_log"), STRIPPED_ACACIA_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "stripped_acacia_hollow_log"), new BlockItem(STRIPPED_ACACIA_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "stripped_birch_hollow_log"), STRIPPED_BIRCH_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "stripped_birch_hollow_log"), new BlockItem(STRIPPED_BIRCH_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "stripped_dark_oak_hollow_log"), STRIPPED_DARK_OAK_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "stripped_dark_oak_hollow_log"), new BlockItem(STRIPPED_DARK_OAK_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "stripped_jungle_hollow_log"), STRIPPED_JUNGLE_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "stripped_jungle_hollow_log"), new BlockItem(STRIPPED_JUNGLE_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "stripped_oak_hollow_log"), STRIPPED_OAK_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "stripped_oak_hollow_log"), new BlockItem(STRIPPED_OAK_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		Registry.register(Registry.BLOCK, new Identifier("witchery", "stripped_spruce_hollow_log"), STRIPPED_SPRUCE_HOLLOW_LOG);
		Registry.register(Registry.ITEM, new Identifier("witchery", "stripped_spruce_hollow_log"), new BlockItem(STRIPPED_SPRUCE_HOLLOW_LOG, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		HOLLOW_LOG_BLOCK_ENTITY = Registry.register(
				Registry.BLOCK_ENTITY_TYPE, "witchery:hollow_log_entity", FabricBlockEntityTypeBuilder.create(HollowLogBlockEntity::new,
						ACACIA_HOLLOW_LOG, BIRCH_HOLLOW_LOG, DARK_OAK_HOLLOW_LOG, JUNGLE_HOLLOW_LOG, OAK_HOLLOW_LOG, SPRUCE_HOLLOW_LOG,
						STRIPPED_ACACIA_HOLLOW_LOG, STRIPPED_BIRCH_HOLLOW_LOG, STRIPPED_DARK_OAK_HOLLOW_LOG, STRIPPED_JUNGLE_HOLLOW_LOG,
						STRIPPED_OAK_HOLLOW_LOG, STRIPPED_SPRUCE_HOLLOW_LOG).build(null));


		FuelRegistry.INSTANCE.add(ACACIA_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(BIRCH_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(DARK_OAK_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(JUNGLE_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(OAK_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(SPRUCE_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(STRIPPED_ACACIA_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(STRIPPED_BIRCH_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(STRIPPED_DARK_OAK_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(STRIPPED_JUNGLE_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(STRIPPED_OAK_HOLLOW_LOG, 300);
		FuelRegistry.INSTANCE.add(STRIPPED_SPRUCE_HOLLOW_LOG, 300);


		Registry.register(Registry.ITEM, new Identifier("witchery", "mandrake_root"), MANDRAKE_ROOT);
		Registry.register(Registry.ITEM, new Identifier("witchery", "mandrake_seeds"), MANDRAKE_SEEDS);

		Registry.register(Registry.ITEM, new Identifier("witchery", "pepsi"), PEPSI);
		Registry.register(Registry.ITEM, new Identifier("witchery", "poppet"), POPPET);
		Registry.register(Registry.ITEM, new Identifier("witchery", "protection_poppet"), PROTECTION_POPPET);
		Registry.register(Registry.ITEM, new Identifier("witchery", "strong_soul"), STRONG_SOUL);
		Registry.register(Registry.ITEM, new Identifier("witchery", "trace_of_magic"), TRACE_OF_MAGIC);

		Registry.register(Registry.BLOCK, new Identifier("witchery", "witch_oven"), WITCH_OVEN);
		Registry.register(Registry.ITEM, new Identifier("witchery", "witch_oven"), new BlockItem(WITCH_OVEN, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
		WITCH_OVEN_ENTITY = Registry.register(
				Registry.BLOCK_ENTITY_TYPE, "witchery:witch_oven_entity", FabricBlockEntityTypeBuilder.create(WitchOvenBlockEntity::new, WITCH_OVEN).build(null));
		WITCH_OVEN_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier("witchery", "witch_oven"), WitchOvenScreenHandler::new);

		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("witchery", WitchOvenRecipe.Serializer.ID),
				WitchOvenRecipe.Serializer.INSTANCE);
		Registry.register(Registry.RECIPE_TYPE, new Identifier("witchery", WitchOvenRecipe.Type.ID),
				WitchOvenRecipe.Type.INSTANCE);

		LOGGER.info("Witchery Initialized");

	}


	public static void error(String message, Throwable error) {
		LOGGER.error(message, error);
	}

}