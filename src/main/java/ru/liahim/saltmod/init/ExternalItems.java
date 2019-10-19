package ru.liahim.saltmod.init;

import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.api.item.ExItems;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.api.registry.ExtractRegistry;
import ru.liahim.saltmod.crafting.recipe.HiddenShapelessRecipes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ExternalItems {

	public static Fluid milk;

	public static void preInit(FMLPreInitializationEvent event) {

		if (FluidRegistry.isFluidRegistered("blood")) {
			ModItems.registerFood(ExItems.HEMOGLOBIN, "hemoglobin");
		}
		if (Loader.isModLoaded("biomesoplenty")) {
			ModItems.registerFood(ExItems.BOP_SALT_SALAD_VEGGIE, "bop_salt_salad_veggie");
			ModItems.registerFood(ExItems.BOP_SALT_SALAD_SHROOM, "bop_salt_salad_shroom");
			ModItems.registerFood(ExItems.BOP_SALT_RICE_BOWL, "bop_salt_rice_bowl");
			ModItems.registerFood(ExItems.BOP_PICKLED_TURNIP, "bop_pickled_turnip");
			ModItems.registerFood(ExItems.BOP_SALT_SHROOM_POWDER, "bop_salt_shroom_powder");
			//ModItems.registerItem(ExItems.BOP_POISON, "bop_poison");
		}
		if (Loader.isModLoaded("twilightforest")) {
			ModItems.registerFood(ExItems.TF_SALT_VENISON_COOKED, "tf_salt_venison_cooked");
			ModItems.registerFood(ExItems.TF_SALTWORT_VENISON, "tf_saltwort_venison");
			ModItems.registerFood(ExItems.TF_SALT_MEFF_STEAK, "tf_salt_meef_steak");
			ModItems.registerFood(ExItems.TF_SALTWORT_MEEF_STEAK, "tf_saltwort_meef_steak");
			ModItems.registerFood(ExItems.TF_SALT_MEEF_STROGANOFF, "tf_salt_meef_stroganoff");
			ModItems.registerFood(ExItems.TF_SALT_HYDRA_CHOP, "tf_salt_hydra_chop");
			ModItems.registerFood(ExItems.TF_PICKLED_MUSHGLOOM, "tf_pickled_mushgloom");
		}
	}

	public static void postInit() {

		// Milk
    	if (FluidRegistry.isFluidRegistered("milk")) {
    		Fluid milk = FluidRegistry.getFluid("milk");
    		ExtractRegistry.instance().addExtracting(milk, SaltItems.POWDERED_MILK, 1000, 0.0F);
    	} else {
    		milk = new Fluid("milk", new ResourceLocation(SaltyMod.MODID, "blocks/milk"), new ResourceLocation(SaltyMod.MODID, "blocks/milk"));
    		FluidRegistry.registerFluid(milk);
    		ExtractRegistry.instance().addExtracting(milk, SaltItems.POWDERED_MILK, 1000, 0.0F);
    	}
    	// Mist
		if (FluidRegistry.isFluidRegistered("mist_acid")) {
			Item sponge_slime = ForgeRegistries.ITEMS.getValue(new ResourceLocation("mist", "mist_sponge_slime"));
			if (sponge_slime != null) {
				Fluid acid = FluidRegistry.getFluid("mist_acid");
				ExtractRegistry.instance().addExtracting(acid, sponge_slime, 1000, 1.0F);
			}
		}
		// Blood
		if (FluidRegistry.isFluidRegistered("blood")) {
			Fluid blood = FluidRegistry.getFluid("blood");
			ExtractRegistry.instance().addExtracting(blood, ExItems.HEMOGLOBIN, 1000, 1.0F);
		}
		// BOP
		if (Loader.isModLoaded("biomesoplenty")) {
			Item saladveggie = ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "saladveggie"));
			if (saladveggie != null) addRecipe("bop_salt_salad_veggie", new ItemStack(ExItems.BOP_SALT_SALAD_VEGGIE), new ItemStack(saladveggie), new ItemStack(SaltItems.SALT_PINCH));
			Item saladshroom = ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "saladshroom"));
			if (saladshroom != null) addRecipe("bop_salt_salad_shroom", new ItemStack(ExItems.BOP_SALT_SALAD_SHROOM), new ItemStack(saladshroom), new ItemStack(SaltItems.SALT_PINCH));
			Item ricebowl = ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "ricebowl"));
			if (ricebowl != null) addRecipe("bop_salt_rice_bowl", new ItemStack(ExItems.BOP_SALT_RICE_BOWL), new ItemStack(ricebowl), new ItemStack(SaltItems.SALT_PINCH));
			Item turnip = ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "turnip"));
			if (turnip != null) addRecipe("bop_pickled_turnip", new ItemStack(ExItems.BOP_PICKLED_TURNIP), new ItemStack(turnip), new ItemStack(turnip), new ItemStack(Items.POTIONITEM), new ItemStack(SaltItems.SALT_PINCH));
			Item shroompowder = ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "shroompowder"));
			if (shroompowder != null) addRecipe("bop_salt_shroom_powder", new ItemStack(ExItems.BOP_SALT_SHROOM_POWDER), new ItemStack(shroompowder), new ItemStack(SaltItems.SALT_PINCH));
			Item bop_dart = ForgeRegistries.ITEMS.getValue(new ResourceLocation("biomesoplenty", "dart"));
			ItemStack bop_poisondart = new ItemStack(bop_dart, 1, 1);
			if (bop_dart != null && FluidRegistry.isFluidRegistered("poison")) {
				Fluid poisonFl = FluidRegistry.getFluid("poison");
				ExtractRegistry.instance().addExtracting(poisonFl, ExItems.BOP_POISON, 1000, 1.0F);
				addRecipe("bop_poisondart", bop_poisondart, new ItemStack(bop_dart), new ItemStack(ExItems.BOP_POISON));
			}
		}
		// TF Items & Recipe
		if (Loader.isModLoaded("twilightforest")) {
			Item venisonCooked = ForgeRegistries.ITEMS.getValue(new ResourceLocation("twilightforest", "cooked_venison"));
			if (venisonCooked != null) {
				addRecipe("tf_salt_venison_cooked", new ItemStack(ExItems.TF_SALT_VENISON_COOKED), new ItemStack(SaltItems.SALT_PINCH), new ItemStack(venisonCooked));
				addRecipe("tf_saltwort_venison", new ItemStack(ExItems.TF_SALTWORT_VENISON), new ItemStack(venisonCooked), new ItemStack(SaltItems.SALTWORT_SEED), new ItemStack(SaltItems.SALTWORT_SEED), new ItemStack(Items.BOWL));
			}
			Item meefSteak = ForgeRegistries.ITEMS.getValue(new ResourceLocation("twilightforest", "cooked_meef"));
			if (meefSteak != null) {
				addRecipe("tf_salt_meef_steak", new ItemStack(ExItems.TF_SALT_MEFF_STEAK), new ItemStack(SaltItems.SALT_PINCH), new ItemStack(meefSteak));
				addRecipe("tf_saltwort_meef_steak", new ItemStack(ExItems.TF_SALTWORT_MEEF_STEAK), new ItemStack(meefSteak), new ItemStack(SaltItems.SALTWORT_SEED), new ItemStack(SaltItems.SALTWORT_SEED), new ItemStack(Items.BOWL));
			}
			Item meefStroganoff = ForgeRegistries.ITEMS.getValue(new ResourceLocation("twilightforest", "meef_stroganoff"));
			if (meefStroganoff != null) addRecipe("tf_salt_meef_stroganoff", new ItemStack(ExItems.TF_SALT_MEEF_STROGANOFF), new ItemStack(SaltItems.SALT_PINCH), new ItemStack(meefStroganoff));
			Item hydraChop = ForgeRegistries.ITEMS.getValue(new ResourceLocation("twilightforest", "hydra_chop"));
			if (hydraChop != null) addRecipe("tf_salt_hydra_chop", new ItemStack(ExItems.TF_SALT_HYDRA_CHOP), new ItemStack(SaltItems.SALT_PINCH), new ItemStack(hydraChop));
			Block mushgloom = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("twilightforest", "twilight_plant"));
			if (mushgloom != Blocks.AIR) addRecipe("tf_pickled_mushgloom", new ItemStack(ExItems.TF_PICKLED_MUSHGLOOM), new ItemStack(SaltItems.SALT_PINCH), new ItemStack(Items.POTIONITEM), new ItemStack(mushgloom, 1, 4), new ItemStack(mushgloom, 1, 4));
		}
	}

	private static void addRecipe(String name, ItemStack out, ItemStack... in) {
		addRecipe(name, name, out, in);
	}

	private static void addRecipe(String name, String group, ItemStack out, ItemStack... in) {
		Ingredient[] ingr = new Ingredient[in.length];
		for (int i = 0; i < in.length; ++i) {
			ingr[i] = Ingredient.fromStacks(in[i]);
		}
		ForgeRegistries.RECIPES.register(new HiddenShapelessRecipes(group, out, NonNullList.<Ingredient>from(Ingredient.EMPTY, ingr)).setRegistryName(new ResourceLocation(SaltyMod.MODID, name)));
	}
}