package ru.liahim.saltmod.init;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.api.item.ExItems;
import ru.liahim.saltmod.api.item.SaltItems;

public class ClientRegister {
	
	public static String modid = SaltyMod.MODID;

	public static void registerBlockRenderer() {
		registerBlocks(SaltBlocks.SALT_ORE);
		registerBlocks(SaltBlocks.SALT_LAKE);
		registerBlocks(SaltBlocks.SALT_BRICK_STAIRS);
		registerMultyBlocks(SaltBlocks.SALT_BLOCK, 0, "salt_block");
		registerMultyBlocks(SaltBlocks.SALT_BLOCK, 1, "salt_block_chiseled");
		registerMultyBlocks(SaltBlocks.SALT_BLOCK, 2, "salt_block_pillar");
		registerMultyBlocks(SaltBlocks.SALT_BLOCK, 5, "salt_brick");
		registerMultyBlocks(SaltBlocks.SALT_BLOCK, 6, "salt_block_cracked");
		registerMultyBlocks(SaltBlocks.SALT_BLOCK, 7, "salt_brick_cracked");
		registerMultyBlocks(SaltBlocks.SALT_BLOCK, 8, "salt_brick_chiseled");
		registerMultyBlocks(SaltBlocks.SALT_BLOCK, 9, "salt_chapiter");
		registerMultyBlocks(SaltBlocks.SALT_SLAB, 0, "salt_slab_block");
		registerMultyBlocks(SaltBlocks.SALT_SLAB, 1, "salt_slab_brick");
		registerMultyBlocks(SaltBlocks.SALT_SLAB, 2, "salt_slab_pillar");
		registerBlocks(SaltBlocks.SALT_LAMP);
		registerMultyBlocks(SaltBlocks.SALT_DIRT, 1, "salt_dirt_lake");
		registerMultyBlocks(SaltBlocks.SALT_DIRT, 0, "salt_dirt");
		registerBlocks(SaltBlocks.SALT_DIRT_LITE);
		registerBlocks(SaltBlocks.SALT_GRASS);
		registerBlocks(SaltBlocks.MUD_BLOCK);
		registerBlocks(SaltBlocks.EXTRACTOR);
		registerBlocks(SaltBlocks.SALT_CRYSTAL);
	}

	public static void init() {
		ModelBakery.registerItemVariants(Item.getItemFromBlock(SaltBlocks.SALT_BLOCK),
				new ResourceLocation(modid, "salt_block"), new ResourceLocation(modid, "salt_block_chiseled"),
				new ResourceLocation(modid, "salt_block_pillar"), new ResourceLocation(modid, "salt_brick"),
				new ResourceLocation(modid, "salt_block_cracked"), new ResourceLocation(modid, "salt_brick_cracked"),
				new ResourceLocation(modid, "salt_brick_chiseled"), new ResourceLocation(modid, "salt_chapiter"));

		ModelBakery.registerItemVariants(Item.getItemFromBlock(SaltBlocks.SALT_SLAB),
				new ResourceLocation(modid, "salt_slab_block"), new ResourceLocation(modid, "salt_slab_brick"),
				new ResourceLocation(modid, "salt_slab_pillar"));

		ModelBakery.registerItemVariants(Item.getItemFromBlock(SaltBlocks.SALT_DIRT),
				new ResourceLocation(modid, "salt_dirt"), new ResourceLocation(modid, "salt_dirt_lake"));

		ModelBakery.registerItemVariants(SaltItems.ACHIEV_ITEM, new ResourceLocation(modid, "achiev_item_0"),
				new ResourceLocation(modid, "achiev_item_1"), new ResourceLocation(modid, "achiev_item_2"));
	}

	public static void registerItemRenderer() {
		registerItems(SaltItems.SALT);
		registerItems(SaltItems.SALT_PINCH);
		registerItems(SaltItems.SALTWORT_SEED);
		registerItems(SaltItems.SODA);
		registerItems(SaltItems.MINERAL_MUD);

		registerItems(SaltItems.SALT_BEEF_COOKED);
		registerItems(SaltItems.SALT_PORKCHOP_COOKED);
		registerItems(SaltItems.SALT_MUTTON_COOKED);
		registerItems(SaltItems.SALT_POTATO_BAKED);
		registerItems(SaltItems.SALT_CHICKEN_COOKED);
		registerItems(SaltItems.SALT_RABBIT_COOKED);
		registerItems(SaltItems.SALT_FISH_COD);
		registerItems(SaltItems.SALT_FISH_COD_COOKED);
		registerItems(SaltItems.SALT_FISH_SALMON);
		registerItems(SaltItems.SALT_FISH_SALMON_COOKED);
		registerItems(SaltItems.SALT_FISH_CLOWNFISH);
		registerItems(SaltItems.SALT_BEETROOT);
		registerItems(SaltItems.CORNED_BEEF);
		registerItems(SaltItems.SALT_BREAD);
		registerItems(SaltItems.SALT_EGG);
		registerItems(SaltItems.SALT_RABBIT_STEW);
		registerItems(SaltItems.SALT_MUSHROOM_STEW);
		registerItems(SaltItems.SALT_BEETROOT_SOUP);
		registerItems(SaltItems.PUMPKIN_PORRIDGE);
		registerItems(SaltItems.VEGETABLE_STEW);
		registerItems(SaltItems.SALT_VEGETABLE_STEW);
		registerItems(SaltItems.POTATO_MUSHROOM);
		registerItems(SaltItems.SALT_POTATO_MUSHROOM);
		registerItems(SaltItems.FISH_SOUP);
		registerItems(SaltItems.SALT_FISH_SOUP);
		registerItems(SaltItems.FISH_SALMON_SOUP);
		registerItems(SaltItems.SALT_FISH_SALMON_SOUP);
		registerItems(SaltItems.SALTWORT_BEEF);
		registerItems(SaltItems.SALTWORT_PORKCHOP);
		registerItems(SaltItems.SALTWORT_MUTTON);
		registerItems(SaltItems.BEETROOT_SALAD);
		registerItems(SaltItems.SALT_BEETROOT_SALAD);
		registerItems(SaltItems.HUFC);
		registerItems(SaltItems.SALT_HUFC);
		registerItems(SaltItems.DANDELION_SALAD);
		registerItems(SaltItems.SALT_DANDELION_SALAD);
		registerItems(SaltItems.WHEAT_SPROUTS);
		registerItems(SaltItems.SALT_WHEAT_SPROUTS);
		registerItems(SaltItems.FRUIT_SALAD);
		registerItems(SaltItems.GRATED_CARROT);
		registerItems(SaltItems.SALTWORT_SALAD);
		registerItems(SaltItems.CARROT_PIE);
		registerItems(SaltItems.APPLE_PIE);
		registerItems(SaltItems.POTATO_PIE);
		registerItems(SaltItems.ONION_PIE);
		registerItems(SaltItems.FISH_PIE);
		registerItems(SaltItems.FISH_SALMON_PIE);
		registerItems(SaltItems.MUSHROOM_PIE);
		registerItems(SaltItems.PICKLED_MUSHROOM);
		registerItems(SaltItems.PICKLED_FERN);

		registerItems(SaltItems.SALTWORT_PIE);
		registerItems(SaltItems.FERMENTED_SALTWORT);
		registerItems(SaltItems.FIZZY_DRINK);
		registerItems(SaltItems.MUFFIN);

		registerItems(SaltItems.MUD_HELMET);
		registerItems(SaltItems.MUD_CHESTPLATE);
		registerItems(SaltItems.MUD_LEGGINGS);
		registerItems(SaltItems.MUD_BOOTS);

		registerItems(SaltItems.POWDERED_MILK);

		registerItems(SaltItems.ESCARGOT);
		registerItems(SaltItems.SALT_STAR);
		registerItems(SaltItems.RAINMAKER);

		registerMultyItems(SaltItems.ACHIEV_ITEM, 0, "achiev_item_0");
		registerMultyItems(SaltItems.ACHIEV_ITEM, 1, "achiev_item_1");
		registerMultyItems(SaltItems.ACHIEV_ITEM, 2, "achiev_item_2");

		if (FluidRegistry.isFluidRegistered("blood")) {
			ClientRegister.registerItems(ExItems.HEMOGLOBIN);
		}
		if (Loader.isModLoaded("biomesoplenty")) {
			registerItems(ExItems.BOP_PICKLED_TURNIP);
			registerItems(ExItems.BOP_SALT_RICE_BOWL);
			registerItems(ExItems.BOP_SALT_SALAD_SHROOM);
			registerItems(ExItems.BOP_SALT_SALAD_VEGGIE);
			registerItems(ExItems.BOP_SALT_SHROOM_POWDER);
			//registerItems(ExItems.BOP_POISON);
		}
		if (Loader.isModLoaded("twilightforest")) {
			registerItems(ExItems.TF_PICKLED_MUSHGLOOM);
			registerItems(ExItems.TF_SALT_HYDRA_CHOP);
			registerItems(ExItems.TF_SALT_MEEF_STROGANOFF);
			registerItems(ExItems.TF_SALT_MEFF_STEAK);
			registerItems(ExItems.TF_SALT_VENISON_COOKED);
			registerItems(ExItems.TF_SALTWORT_MEEF_STEAK);
			registerItems(ExItems.TF_SALTWORT_VENISON);
		}
	}

	public static void registerItems(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	    .register(item, 0, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}

	public static void registerMultyItems(Item item, int meta, String file) {
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(modid + ":" + file, "inventory"));
	}

	public static void registerBlocks(Block block) {
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	    .register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}

	public static void registerMultyBlocks(Block block, int meta, String file) {
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	    .register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(modid + ":" + file, "inventory"));
	}
}