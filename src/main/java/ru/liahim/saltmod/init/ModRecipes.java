package ru.liahim.saltmod.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.item.crafting.EvaporatorRecipe;
import ru.liahim.saltmod.item.crafting.EvaporatorRecipeSerializer;
import ru.liahim.saltmod.item.crafting.NonRemainingShapelessRecipes;

public class ModRecipes {

	public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SaltyMod.MODID);

	public static final RegistryObject<EvaporatorRecipeSerializer<EvaporatorRecipe>> EVAPORATING = RECIPES.register("evaporating", () -> new EvaporatorRecipeSerializer<>(EvaporatorRecipe::new));
	public static final RegistryObject<IRecipeSerializer<NonRemainingShapelessRecipes>> NON_REMAININGG_SHAPELESS = RECIPES.register("non_remaining_shapeless", () -> new NonRemainingShapelessRecipes.Serializer());
}