package ru.liahim.saltmod.crafting.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class PotionTypeIngredientFactory implements IIngredientFactory {

	@Override
	public Ingredient parse(JsonContext context, JsonObject json) {
		final String typeName = JsonUtils.getString(json, "potion");
		final PotionType type = PotionType.getPotionTypeForName(typeName);
		PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), type);

		if (type == null) {
			throw new JsonSyntaxException("Unknown type '" + typeName + "'");
		}

		final ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), type);

		if (potion.isEmpty()) {
			throw new JsonSyntaxException("No potion registered for type '" + typeName + "'");
		}

		return new SaltIngredientNBT(potion);
	}
}