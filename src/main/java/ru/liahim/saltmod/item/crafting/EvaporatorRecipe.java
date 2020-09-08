package ru.liahim.saltmod.item.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModRecipeTypes;
import ru.liahim.saltmod.init.ModRecipes;

public class EvaporatorRecipe extends AbstractEvaporatorRecipe {

	public EvaporatorRecipe(ResourceLocation id, String group, IngredientFluid fluid, ItemStack result, float experience) {
		super(ModRecipeTypes.EVAPORATING, id, group, fluid, result, experience);
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(ModBlocks.EVAPORATOR.get());
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.EVAPORATING.get();
	}
}