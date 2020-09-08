package ru.liahim.saltmod.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import ru.liahim.saltmod.item.crafting.AbstractEvaporatorRecipe;

public class ModRecipeTypes {

	public static final IRecipeType<AbstractEvaporatorRecipe> EVAPORATING = register("evaporating");

	static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
		return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(key), new IRecipeType<T>() {
			@Override
			public String toString() {
				return key;
			}
		});
	}
}