package ru.liahim.saltmod.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import ru.liahim.saltmod.init.ModRecipes;

public class NonRemainingShapelessRecipes extends ShapelessRecipe {

	static int MAX_WIDTH = 3;
	static int MAX_HEIGHT = 3;
	private final NonNullList<Ingredient> recipeItems;

	public NonRemainingShapelessRecipes(ResourceLocation id, String group, ItemStack recipeOutput, NonNullList<Ingredient> recipeItems) {
		super(id, group, recipeOutput, recipeItems);
		this.recipeItems = recipeItems;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.NON_REMAININGG_SHAPELESS.get();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<NonRemainingShapelessRecipes> {

		@Override
		public NonRemainingShapelessRecipes read(ResourceLocation recipeId, JsonObject json) {
			String s = JSONUtils.getString(json, "group", "");
			NonNullList<Ingredient> list = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
			if (list.isEmpty()) throw new JsonParseException("No ingredients for non remaining shapeless recipe");
			else if (list.size() > MAX_WIDTH * MAX_HEIGHT) throw new JsonParseException("Too many ingredients for non remaining shapeless recipe the max is " + (MAX_WIDTH * MAX_HEIGHT));
			else {
				ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
				return new NonRemainingShapelessRecipes(recipeId, s, result, list);
			}
		}

		private static NonNullList<Ingredient> readIngredients(JsonArray array) {
			NonNullList<Ingredient> list = NonNullList.create();
			for (int i = 0; i < array.size(); ++i) {
				Ingredient ingredient = Ingredient.deserialize(array.get(i));
				if (!ingredient.hasNoMatchingItems()) list.add(ingredient);
			}
			return list;
		}

		@Override
		public NonRemainingShapelessRecipes read(ResourceLocation recipeId, PacketBuffer buffer) {
			String s = buffer.readString(32767);
			int i = buffer.readVarInt();
			NonNullList<Ingredient> list = NonNullList.withSize(i, Ingredient.EMPTY);
			for (int j = 0; j < list.size(); ++j) list.set(j, Ingredient.read(buffer));
			ItemStack stack = buffer.readItemStack();
			return new NonRemainingShapelessRecipes(recipeId, s, stack, list);
		}

		@Override
		public void write(PacketBuffer buffer, NonRemainingShapelessRecipes recipe) {
			buffer.writeString(recipe.getGroup());
			buffer.writeVarInt(recipe.recipeItems.size());
			for (Ingredient ingredient : recipe.recipeItems) ingredient.write(buffer);
			buffer.writeItemStack(recipe.getRecipeOutput());
		}
	}
}