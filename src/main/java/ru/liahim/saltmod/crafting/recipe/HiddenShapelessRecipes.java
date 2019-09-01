package ru.liahim.saltmod.crafting.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

public class HiddenShapelessRecipes extends ShapelessRecipes {

	public HiddenShapelessRecipes(String group, ItemStack output, NonNullList<Ingredient> ingredients) {
		super(group, output, ingredients);
	}

	@Override
	public boolean isDynamic() {
		return true;
	}
}