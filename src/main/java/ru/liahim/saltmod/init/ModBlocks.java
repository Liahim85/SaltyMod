package ru.liahim.saltmod.init;

import static ru.liahim.saltmod.api.block.SaltBlocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.block.Extractor;
import ru.liahim.saltmod.block.MudBlock;
import ru.liahim.saltmod.block.SaltBlock;
import ru.liahim.saltmod.block.SaltBrickStair;
import ru.liahim.saltmod.block.SaltCrystal;
import ru.liahim.saltmod.block.SaltDirt;
import ru.liahim.saltmod.block.SaltDirtLite;
import ru.liahim.saltmod.block.SaltDoubleSlab;
import ru.liahim.saltmod.block.SaltGrass;
import ru.liahim.saltmod.block.SaltHalfSlab;
import ru.liahim.saltmod.block.SaltLake;
import ru.liahim.saltmod.block.SaltLamp;
import ru.liahim.saltmod.block.SaltOre;
import ru.liahim.saltmod.block.SaltPot;
import ru.liahim.saltmod.block.SaltWort;
import ru.liahim.saltmod.item.ItemSaltBlock;
import ru.liahim.saltmod.item.ItemSaltDirt;

public class ModBlocks {

	public static void registerBlocks() {
		SaltyMod.logger.info("Start to initialize Blocks");
		SALT_ORE = registerBlock(new SaltOre(), "salt_ore");
		SALT_LAKE = registerBlock(new SaltLake(), "salt_lake");
		SALT_BLOCK = registerBlockWithoutItem(new SaltBlock(), "salt_block");
		registerItemBlock(new ItemSaltBlock(SALT_BLOCK));
		SALT_BRICK_STAIRS = registerBlock(new SaltBrickStair(), "salt_brick_stairs");
		SALT_SLAB = (BlockSlab) registerBlockWithoutItem(new SaltHalfSlab(), "salt_slab");
		SALT_SLAB_DOUBLE = (BlockSlab) registerBlockWithoutItem(new SaltDoubleSlab(), "salt_slab_double");
		registerItemBlock(new ItemSlab(SALT_SLAB, SALT_SLAB, SALT_SLAB_DOUBLE));
		SALT_LAMP = registerBlock(new SaltLamp(), "salt_lamp");
		SALT_DIRT = registerBlockWithoutItem(new SaltDirt(), "salt_dirt");
		registerItemBlock(new ItemSaltDirt(SALT_DIRT));
		SALT_DIRT_LITE = registerBlock(new SaltDirtLite(), "salt_dirt_lite");
		SALT_GRASS = registerBlock(new SaltGrass(), "salt_grass");
		MUD_BLOCK = registerBlock(new MudBlock(), "mud_block");
		EXTRACTOR = registerBlock(new Extractor(false, false), "extractor");
		EXTRACTOR_LIT = registerBlockWithoutItem(new Extractor(true, false), "extractor_lit", null);
		EXTRACTOR_STEAM = registerBlockWithoutItem(new Extractor(true, true), "extractor_steam", null);
		SALT_CRYSTAL = registerBlock(new SaltCrystal(), "salt_crystal");
		SALTWORT = registerBlockWithoutItem(new SaltWort(), "saltwort", null);
		SALT_POT = registerBlockWithoutItem(new SaltPot(), "salt_pot", null);
    	SaltyMod.logger.info("Finished initializing Blocks");
    }

	private static Block registerBlock(Block block, String registryName) {
		return registerBlock(block, registryName, SaltyMod.saltTab);
	}

	private static Block registerBlock(Block block, String registryName, CreativeTabs tab) {
		ItemBlock itemBlock = new ItemBlock(block);
		block.setRegistryName(registryName).setUnlocalizedName(registryName).setCreativeTab(tab);
		itemBlock.setRegistryName(registryName);
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(itemBlock);
		SaltyMod.proxy.registerBlockColored(block);
		return block;
	}

	private static Block registerBlockWithoutItem(Block block, String registryName) {
		return registerBlockWithoutItem(block, registryName, SaltyMod.saltTab);
	}

	private static Block registerBlockWithoutItem(Block block, String registryName, CreativeTabs tab) {
		block.setRegistryName(registryName).setUnlocalizedName(registryName).setCreativeTab(tab);
		ForgeRegistries.BLOCKS.register(block);
		SaltyMod.proxy.registerBlockColored(block);
		return block;
	}

	private static void registerItemBlock(ItemBlock itemBlock) {
		itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName());
		ForgeRegistries.ITEMS.register(itemBlock);
	}
}