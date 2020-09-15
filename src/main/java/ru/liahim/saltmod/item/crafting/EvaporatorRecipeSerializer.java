package ru.liahim.saltmod.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class EvaporatorRecipeSerializer<T extends AbstractEvaporatorRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

	private final EvaporatorRecipeSerializer.IFactory<T> factory;

	public EvaporatorRecipeSerializer(EvaporatorRecipeSerializer.IFactory<T> factory) {
		this.factory = factory;
	}

	@Override
	@SuppressWarnings("deprecation")
	public T read(ResourceLocation recipeId, JsonObject json) {
		String s = JSONUtils.getString(json, "group", "");
		IngredientFluid fluid;
		if (json.get("ingredient").isJsonObject()) fluid = IngredientFluid.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
		else {
			String s1 = JSONUtils.getString(json, "ingredient");
			ResourceLocation res = new ResourceLocation(s1);
			fluid = new IngredientFluid(new FluidStack(Registry.FLUID.getValue(res).orElseThrow(() -> {
		           return new JsonSyntaxException("Unknown fluid '" + res + "'");
	        }), 1000));
		}
		if (!json.has("result")) throw new JsonSyntaxException("Missing result, expected to find a string or object");
		ItemStack itemstack;
		if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
		else {
			String s1 = JSONUtils.getString(json, "result");
			ResourceLocation resourcelocation = new ResourceLocation(s1);
			itemstack = new ItemStack(Registry.ITEM.getValue(resourcelocation).orElseThrow(() -> {
				return new IllegalStateException("Item: " + s1 + " does not exist");
			}));
		}
		float f = JSONUtils.getFloat(json, "experience", 0.0F);
		return this.factory.create(recipeId, s, fluid, itemstack, f);
	}

	@Override
	public T read(ResourceLocation recipeId, PacketBuffer buffer) {
		String s = buffer.readString(32767);
		IngredientFluid fluid = IngredientFluid.read(buffer);
		ItemStack itemstack = buffer.readItemStack();
		float f = buffer.readFloat();
		return this.factory.create(recipeId, s, fluid, itemstack, f);
	}

	@Override
	public void write(PacketBuffer buffer, T recipe) {
		buffer.writeString(recipe.group);
		recipe.ingredient.write(buffer);
		buffer.writeItemStack(recipe.result);
		buffer.writeFloat(recipe.experience);
	}

	public interface IFactory<T extends AbstractEvaporatorRecipe> {
		T create(ResourceLocation id, String group, IngredientFluid fluid, ItemStack result, float experience);
	}
}