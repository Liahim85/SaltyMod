package ru.liahim.saltmod.init;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.init.SaltyFoodRegister.SaltyFoodBuilder;
import ru.liahim.saltmod.init.SaltyFoodRegister.SaltyObject;
import ru.liahim.saltmod.item.AchievItem;
import ru.liahim.saltmod.item.FizzyDrink;
import ru.liahim.saltmod.item.MudArmor;
import ru.liahim.saltmod.item.Muffin;
import ru.liahim.saltmod.item.Rainmaker;
import ru.liahim.saltmod.item.SaltBottle;
import ru.liahim.saltmod.item.SaltDelegate;
import ru.liahim.saltmod.item.SaltFood;
import ru.liahim.saltmod.item.SaltItem;
import ru.liahim.saltmod.item.SaltSoup;
import ru.liahim.saltmod.item.SaltwortSeed;

public class ModItems {

	public static final Tag<Item> SALT_TAG = new ItemTags.Wrapper(new ResourceLocation(SaltyMod.MODID, "salt"));
	public static final List<Item> MOD_ITEMS = Lists.newArrayList();

	public static final Item ACHIEV_ITEM = register("achiev_item", new AchievItem());
	public static final Item SALT = register("salt", new SaltItem());
	public static final Item SALT_PINCH = register("salt_pinch", new SaltItem());
	public static final Item SALTWORT_SEED = register("saltwort_seed", new SaltwortSeed());
	public static final Item SODA = register("soda", new SaltItem());
	public static final Item MINERAL_MUD = register("mineral_mud", new SaltItem());

	public static final Item SALT_COOKED_BEEF = register("salt_cooked_beef", new SaltDelegate(Items.COOKED_BEEF));
	public static final Item SALT_COOKED_PORKCHOP = register("salt_cooked_porkchop", new SaltDelegate(Items.COOKED_PORKCHOP));
	public static final Item SALT_COOKED_MUTTON = register("salt_cooked_mutton", new SaltDelegate(Items.COOKED_MUTTON));
	public static final Item SALT_BAKED_POTATO = register("salt_baked_potato", new SaltDelegate(Items.BAKED_POTATO));
	public static final Item SALT_COOKED_CHICKEN = register("salt_cooked_chicken", new SaltDelegate(Items.COOKED_CHICKEN));
	public static final Item SALT_COOKED_RABBIT = register("salt_cooked_rabbit", new SaltDelegate(Items.COOKED_RABBIT));
	public static final Item SALT_COD = register("salt_cod", new SaltDelegate(Items.COD));
	public static final Item SALT_COOKED_COD = register("salt_cooked_cod", new SaltDelegate(Items.COOKED_COD));
	public static final Item SALT_SALMON = register("salt_salmon", new SaltDelegate(Items.SALMON));
	public static final Item SALT_COOKED_SALMON = register("salt_cooked_salmon", new SaltDelegate(Items.COOKED_SALMON));
	public static final Item SALT_TROPICAL_FISH = register("salt_tropical_fish", new SaltDelegate(Items.TROPICAL_FISH));
	public static final Item SALT_BEETROOT = register("salt_beetroot", new SaltDelegate(Items.BEETROOT));
	public static final Item SALT_ROTTEN_FLESH = register("salt_rotten_flesh", new SaltDelegate(Items.ROTTEN_FLESH));
	public static final Item SALT_BREAD = register("salt_bread", new SaltDelegate(Items.BREAD));
	public static final Item SALT_EGG = register("salt_egg", new SaltDelegate(Items.EGG));
	public static final Item SALT_RABBIT_STEW = register("salt_rabbit_stew", new SaltDelegate(Items.RABBIT_STEW));
	public static final Item SALT_MUSHROOM_STEW = register("salt_mushroom_stew", new SaltDelegate(Items.MUSHROOM_STEW));
	public static final Item SALT_BEETROOT_SOUP = register("salt_beetroot_soup", new SaltDelegate(Items.BEETROOT_SOUP));
	
	public static final Item PUMPKIN_PORRIDGE = register("pumpkin_porridge", new SaltSoup(ModFoods.PUMPKIN_PORRIDGE));
	public static final Item SALT_PUMPKIN_PORRIDGE = register("salt_pumpkin_porridge", new SaltDelegate(ModItems.PUMPKIN_PORRIDGE));
	public static final Item VEGETABLE_STEW = register("vegetable_stew", new SaltSoup(ModFoods.VEGETABLE_STEW));
	public static final Item SALT_VEGETABLE_STEW = register("salt_vegetable_stew", new SaltDelegate(ModItems.VEGETABLE_STEW));
	public static final Item POTATO_MUSHROOM = register("potato_mushroom", new SaltSoup(ModFoods.POTATO_MUSHROOM));
	public static final Item SALT_POTATO_MUSHROOM = register("salt_potato_mushroom", new SaltDelegate(ModItems.POTATO_MUSHROOM));
	public static final Item COD_SOUP = register("cod_soup", new SaltSoup(ModFoods.COD_SOUP));
	public static final Item SALT_COD_SOUP = register("salt_cod_soup", new SaltDelegate(ModItems.COD_SOUP));
	public static final Item SALMON_SOUP = register("salmon_soup", new SaltSoup(ModFoods.SALMON_SOUP));
	public static final Item SALT_SALMON_SOUP = register("salt_salmon_soup", new SaltDelegate(ModItems.SALMON_SOUP));
	public static final Item SALTWORT_BEEF = register("saltwort_beef", new SaltSoup(ModFoods.SALTWORT_BEEF));
	public static final Item SALTWORT_PORKCHOP = register("saltwort_porkchop", new SaltSoup(ModFoods.SALTWORT_PORKCHOP));
	public static final Item SALTWORT_MUTTON = register("saltwort_mutton", new SaltSoup(ModFoods.SALTWORT_MUTTON));
	public static final Item BEETROOT_SALAD = register("beetroot_salad", new SaltSoup(ModFoods.BEETROOT_SALAD));
	public static final Item SALT_BEETROOT_SALAD = register("salt_beetroot_salad", new SaltDelegate(ModItems.BEETROOT_SALAD, new SaltyFoodBuilder().saturation(0.65F), false));
	public static final Item HUFC = register("hufc", new SaltSoup(ModFoods.HUFC));
	public static final Item SALT_HUFC = register("salt_hufc", new SaltDelegate(ModItems.HUFC));
	public static final Item DANDELION_SALAD = register("dandelion_salad", new SaltSoup(ModFoods.DANDELION_SALAD));
	public static final Item SALT_DANDELION_SALAD = register("salt_dandelion_salad", new SaltDelegate(ModItems.DANDELION_SALAD, new SaltyFoodBuilder().saturation(0.35F), false));
	public static final Item WHEAT_SPROUTS = register("wheat_sprouts", new SaltSoup(ModFoods.WHEAT_SPROUTS));
	public static final Item SALT_WHEAT_SPROUTS = register("salt_wheat_sprouts", new SaltDelegate(ModItems.WHEAT_SPROUTS));
	public static final Item FRUIT_SALAD = register("fruit_salad", new SaltSoup(ModFoods.FRUIT_SALAD));
	public static final Item GRATED_CARROT = register("grated_carrot", new SaltSoup(ModFoods.GRATED_CARROT));
	public static final Item SALTWORT_SALAD = register("saltwort_salad", new SaltSoup(ModFoods.SALTWORT_SALAD));
	public static final Item CARROT_PIE = register("carrot_pie", new SaltFood(ModFoods.CARROT_PIE));
	public static final Item APPLE_PIE = register("apple_pie", new SaltFood(ModFoods.APPLE_PIE));
	public static final Item POTATO_PIE = register("potato_pie", new SaltFood(ModFoods.POTATO_PIE));
	public static final Item ONION_PIE = register("onion_pie", new SaltFood(ModFoods.ONION_PIE));
	public static final Item COD_PIE = register("cod_pie", new SaltFood(ModFoods.COD_PIE));
	public static final Item SALMON_PIE = register("salmon_pie", new SaltFood(ModFoods.SALMON_PIE));
	public static final Item MUSHROOM_PIE = register("mushroom_pie", new SaltFood(ModFoods.MUSHROOM_PIE));
	public static final Item SALTWORT_PIE = register("saltwort_pie", new SaltFood(ModFoods.SALTWORT_PIE));
	public static final Item FERMENTED_SALTWORT = register("fermented_saltwort", new SaltBottle(ModFoods.FERMENTED_SALTWORT));
	public static final Item PICKLED_MUSHROOM = register("pickled_mushroom", new SaltBottle(ModFoods.PICKLED_MUSHROOM));
	public static final Item PICKLED_FERN = register("pickled_fern", new SaltBottle(ModFoods.PICKLED_FERN));
	public static final Item MUFFIN = register("muffin", new Muffin(ModFoods.MUFFIN));
	public static final Item FIZZY_DRINK = register("fizzy_drink", new FizzyDrink());

	public static final Item MUD_HELMET = register("mud_helmet", new MudArmor(EquipmentSlotType.HEAD));
	public static final Item MUD_CHESTPLATE = register("mud_chestplate", new MudArmor(EquipmentSlotType.CHEST));
	public static final Item MUD_LEGGINGS =	register("mud_leggings", new MudArmor(EquipmentSlotType.LEGS));
	public static final Item MUD_BOOTS = register("mud_boots", new MudArmor(EquipmentSlotType.FEET));

	public static final Item POWDERED_MILK = register("powdered_milk", new SaltItem());
	public static final Item HEMOGLOBIN = register("hemoglobin", new SaltFood(ModFoods.HEMOGLOBIN));
	public static final Item ESCARGOT = register("escargot", new SaltFood(ModFoods.ESCARGOT));

	public static final Item SALT_STAR = register("salt_star", new SaltItem());
	public static final Item RAINMAKER = register("rainmaker", new Rainmaker());

	private static Item register(String name, Item item) {
		MOD_ITEMS.add(item.setRegistryName(new ResourceLocation(SaltyMod.MODID, name)));
		return item;
	}

	public static void registerSaltyFood(IForgeRegistry<Item> registry, Map<ResourceLocation,SaltyObject> items) {
		final Ingredient saltPinch = Ingredient.fromTag(SALT_TAG);
		items.forEach((res, obj) -> {
			Item item = ForgeRegistries.ITEMS.getValue(res);
			if (!ForgeRegistries.ITEMS.containsKey(res) || item == null) SaltyMod.LOGGER.warn("Can't find item '" + res + "' to add salty food");
			else if (!item.isFood() && obj.getFoodBuilder() == null) SaltyMod.LOGGER.warn("Item '" + res + "' must be a food");
			else if (res.getPath().length() >= 5 && res.getPath().substring(0, 5).equals("salt_")) SaltyMod.LOGGER.warn("Can't salt salty food '" + res + "'");
			else {
				String prefix = obj.getPrefix();
				if (!prefix.isEmpty()) prefix = prefix + "_";
				prefix = "salt_" + prefix;
				ResourceLocation saltRes = new ResourceLocation(SaltyMod.MODID, prefix + res.getPath());
				if (!ForgeRegistries.ITEMS.containsKey(saltRes)) {
					Item saltyFood = new SaltDelegate(item, obj.getFoodBuilder(), obj.needName());
					saltyFood.setRegistryName(saltRes);
					registry.register(saltyFood);
					if (obj.needModel()) SaltyMod.proxy.registerSaltyFoodModel(res, prefix, obj.getTextureIndex());
					if (obj.needRecipe()) {
						Ingredient[] ingrs = new Ingredient[obj.getSaltCount() + 1];
						ingrs[0] = Ingredient.fromStacks(new ItemStack(item));
						for (int i = 1; i < ingrs.length; i++) ingrs[i] = saltPinch;
						NonNullList<Ingredient> ingredients = NonNullList.<Ingredient>from(saltPinch, ingrs);
						SaltyFoodRegister.recipes.add(new ShapelessRecipe(saltRes, prefix + res.getPath(), new ItemStack(saltyFood), ingredients));
					}
				} else SaltyMod.LOGGER.warn("Salty food '" + res + "' already exists");
			}
		});
	}
}