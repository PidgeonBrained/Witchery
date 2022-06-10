package com.gmail.Ventex240.witchery;

import com.gmail.Ventex240.witchery.block.BabysBreathBlock;
import com.gmail.Ventex240.witchery.block.WitchOven;
//import com.gmail.Ventex240.witchery.fluid.Cum;
import com.gmail.Ventex240.witchery.block.entity.WitchOvenEntity;
import com.gmail.Ventex240.witchery.item.Broom;
import com.gmail.Ventex240.witchery.item.Pepsi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.*;
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


	public static final Pepsi PEPSI_ITEM = new Pepsi(new FabricItemSettings().food(Pepsi.pepsiBehavior.build()).group(ItemGroup.FOOD));
	public static final Broom BROOM = new Broom(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1));

	public static FlowableFluid STILL_CUM;
	public static FlowableFluid FLOWING_CUM;
	public static Item CUM_BUCKET;


	public static final CropBlock BABYS_BREATH_BLOCK =
			new BabysBreathBlock(AbstractBlock.Settings.of(Material.PLANT).nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));

	public static final Item BABYS_BREATH_SEEDS =
			new AliasedBlockItem(BABYS_BREATH_BLOCK, new Item.Settings().group(ItemGroup.MISC));


	public static final WitchOven WITCH_OVEN = new WitchOven(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3.5F));
	public static BlockEntityType<WitchOvenEntity> WITCH_OVEN_ENTITY;


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Testing this shit out!");
		Registry.register(Registry.ITEM, new Identifier("witchery", "pepsi"), PEPSI_ITEM);

		Registry.register(Registry.ITEM, new Identifier("witchery", "broom"), BROOM);

		/*STILL_CUM = Registry.register(Registry.FLUID, new Identifier("witchery", "acid"), new Cum.Still());
		FLOWING_CUM = Registry.register(Registry.FLUID, new Identifier("witchery", "flowing_acid"), new Cum.Flowing());
		CUM_BUCKET = Registry.register(Registry.ITEM, new Identifier("witchery", "acid_bucket"),
				new BucketItem(STILL_CUM, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));
		CUM = Registry.register(Registry.BLOCK, new Identifier(witchery, "cum"), new FluidBlock(STILL_ACID, FabricBlockSettings.copy(Blocks.WATER)){});*/

		Registry.register(Registry.BLOCK, new Identifier("witchery","babys_breath_block"), BABYS_BREATH_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("witchery","babys_breath_seeds"), BABYS_BREATH_SEEDS);

		Registry.register(Registry.BLOCK, new Identifier("witchery", "witch_oven"), WITCH_OVEN);
		Registry.register(Registry.ITEM, new Identifier("witchery", "witch_oven"), new BlockItem(WITCH_OVEN, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
		WITCH_OVEN_ENTITY = Registry.register(
				Registry.BLOCK_ENTITY_TYPE, "tutorial:demo_block_entity", FabricBlockEntityTypeBuilder.create(WitchOvenEntity::new, WITCH_OVEN).build(null));



	}
}