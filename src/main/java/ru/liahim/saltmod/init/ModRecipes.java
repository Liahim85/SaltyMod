package ru.liahim.saltmod.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.api.registry.ExtractRegistry;

public class ModRecipes {

	public static void registerRecipes() {

		ExtractRegistry.instance().addExtracting(FluidRegistry.WATER, SaltItems.SALT_PINCH, 1000, 0.0F);

		//Smelting
		GameRegistry.addSmelting(SaltBlocks.SALT_ORE, new ItemStack(SaltItems.SALT, 1), 0.7F);
		GameRegistry.addSmelting(SaltBlocks.SALT_LAKE, new ItemStack(SaltItems.SALT, 1), 0.7F);
		GameRegistry.addSmelting(new ItemStack(SaltBlocks.SALT_BLOCK, 1, 0), new ItemStack(SaltBlocks.SALT_BLOCK, 1, 6), 0.0F);
		GameRegistry.addSmelting(new ItemStack(SaltBlocks.SALT_BLOCK, 1, 5), new ItemStack(SaltBlocks.SALT_BLOCK, 1, 7), 0.0F);
		GameRegistry.addSmelting(SaltItems.SALTWORT_SEED, new ItemStack(SaltItems.SODA, 1), 0.0F);

		//OreDictionary
		OreDictionary.registerOre("oreSalt", SaltBlocks.SALT_ORE);
		OreDictionary.registerOre("blockSalt", SaltBlocks.SALT_BLOCK);
		OreDictionary.registerOre("blockSaltCrystal", SaltBlocks.SALT_CRYSTAL);
		OreDictionary.registerOre("lumpSalt", SaltItems.SALT);
		OreDictionary.registerOre("dustSalt", SaltItems.SALT_PINCH);
		OreDictionary.registerOre("dustSoda", SaltItems.SODA);
		OreDictionary.registerOre("dustMilk", SaltItems.POWDERED_MILK);
		OreDictionary.registerOre("cropSaltwort", SaltItems.SALTWORT_SEED);
		OreDictionary.registerOre("materialMineralMud", SaltItems.MINERAL_MUD);
	}
}