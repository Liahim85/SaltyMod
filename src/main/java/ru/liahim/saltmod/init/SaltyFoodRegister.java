package ru.liahim.saltmod.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.IForgeRegistry;
import ru.liahim.saltmod.SaltyMod;

public class SaltyFoodRegister {

	public static final SaltyFoodRegister Instance = new SaltyFoodRegister();
	private static final String filePath = "/data/" + SaltyMod.MODID + "/saltyfood/";
	private final Map<ResourceLocation,SaltyObject> ITEMS = Maps.newHashMap();
	public static final List<ShapelessRecipe> recipes = Lists.newArrayList();

	public void register(IForgeRegistry<Item> registry) {
		read();
		ModItems.registerSaltyFood(registry, ITEMS);
	}

	private void read() {
        List<String> filenames = new ArrayList<>();
		try (InputStream in = getClass().getResourceAsStream(filePath);
				BufferedReader br = in != null ? new BufferedReader(new InputStreamReader(in)) : null) {
			if (br != null) {
				String resource;
				while ((resource = br.readLine()) != null) {
					if (resource.endsWith(".json")) filenames.add(resource);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			for (String file : filenames) {
				String modName = file.substring(0, file.length() - 5);
				if (!modName.equals("example_mod")) {
					if (!ModList.get().isLoaded(modName)) SaltyMod.LOGGER.warn("Unknown domain '" + modName + "'");
					else parse(modName, IOUtils.toString(getClass().getResourceAsStream(filePath + file), StandardCharsets.UTF_8));
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }

	private void parse(String fileNane, String raw) {
		JsonObject obj = JSONUtils.fromJson(raw);
		String prefix = JSONUtils.getString(obj, "prefix", "");
		JsonArray arr = JSONUtils.getJsonArray(obj, "values", null);
		if (arr != null && !arr.isJsonNull()) {
			arr.forEach(element -> {
				String item = "";
				String itemPrefix = prefix;
				int count = 1;
				int texture = 0;
				boolean model = true;
				boolean name = true;
				boolean recipe = true;
				SaltyFoodBuilder food = null;
				if (element.isJsonObject()) {
					item = JSONUtils.getString((JsonObject) element, "item", "");
					count = JSONUtils.getInt((JsonObject) element, "salt", 1);
					texture = JSONUtils.getInt((JsonObject) element, "texture", 0);
					if (texture < 0) texture = 0;
					model = JSONUtils.getBoolean((JsonObject) element, "model", true);
					name = JSONUtils.getBoolean((JsonObject) element, "name", true);
					recipe = JSONUtils.getBoolean((JsonObject) element, "recipe", true);
					itemPrefix = JSONUtils.getString((JsonObject) element, "prefix", itemPrefix);
					JsonObject objFood = JSONUtils.getJsonObject((JsonObject) element, "food", null);
					if (objFood != null && !objFood.isJsonNull() && objFood.isJsonObject()) {
						int heal = JSONUtils.getInt(objFood, "heal", -1);
						float saturation = JSONUtils.getFloat(objFood, "saturation", -1);
						int meat = 0;
						int always = 0;
						int fast = 0;
						if (JSONUtils.hasField(objFood, "meat")) {
							meat = JSONUtils.getBoolean(objFood, "meat") ? 1 : -1;
						}
						if (JSONUtils.hasField(objFood, "always")) {
							always = JSONUtils.getBoolean(objFood, "always") ? 1 : -1;
						}
						if (JSONUtils.hasField(objFood, "fast")) {
							fast = JSONUtils.getBoolean(objFood, "fast") ? 1 : -1;
						}
						food = new SaltyFoodBuilder().heal(heal).saturation(saturation).meat(meat).setAlwaysEdible(always).fastToEat(fast);
					}
				} else item = JSONUtils.getString(element, "value");
				if (!item.isEmpty()) {
					ResourceLocation res = new ResourceLocation(fileNane, item);
					if (ITEMS.containsKey(res)) SaltyMod.LOGGER.warn("Duplicate value '" + res + "' for salty foods");
					else ITEMS.put(res, new SaltyObject(itemPrefix, count, texture, model, name, recipe, food));
				}
			});
		}
	}

	public static class SaltyObject {

		private final String prefix;
		private final int saltCount;
		private final int textureIndex;
		private final boolean needModel;
		private final boolean needName;
		private final boolean needRecipe;
		private final SaltyFoodBuilder food;

		public SaltyObject(String prefix, int saltCount, int textureIndex, boolean needModel, boolean needName, boolean needRecipe, SaltyFoodBuilder food) {
			this.prefix = prefix;
			this.saltCount = saltCount;
			this.textureIndex = textureIndex;
			this.needModel = needModel;
			this.needName = needName;
			this.needRecipe = needRecipe;
			this.food = food;
		}

		public String getPrefix() {
			return prefix;
		}

		public int getSaltCount() {
			return saltCount;
		}

		public int getTextureIndex() {
			return textureIndex;
		}

		public boolean needModel() {
			return needModel;
		}

		public boolean needName() {
			return needName;
		}

		public boolean needRecipe() {
			return needRecipe;
		}

		public SaltyFoodBuilder getFoodBuilder() {
			return food;
		}
	}

	public static class SaltyFoodBuilder {

		private int heal;
		private float saturation;
		private int meat;
		private int alwaysEdible;
		private int fastToEat;

		public SaltyFoodBuilder heal(int heal) {
			this.heal = heal;
			return this;
		}

		public SaltyFoodBuilder saturation(float saturation) {
			this.saturation = saturation;
			return this;
		}

		public SaltyFoodBuilder meat(int meat) {
			this.meat = meat;
			return this;
		}

		public SaltyFoodBuilder setAlwaysEdible(int always) {
			this.alwaysEdible = always;
			return this;
		}

		public SaltyFoodBuilder fastToEat(int fast) {
			this.fastToEat = fast;
			return this;
		}

		public int getHeal() {
			return heal;
		}

		public float getSaturation() {
			return saturation;
		}

		public int getMeat() {
			return meat;
		}

		public int getAlwaysEdible() {
			return alwaysEdible;
		}

		public int getFastToEat() {
			return fastToEat;
		}
	}
}