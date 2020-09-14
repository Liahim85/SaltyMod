package ru.liahim.saltmod.init;

import java.util.Map;
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
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
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
	public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SaltyMod.MODID);

	public static final RegistryObject<Item> ACHIEV_ITEM = MOD_ITEMS.register("achiev_item", () -> new AchievItem());
	public static final RegistryObject<Item> SALT = MOD_ITEMS.register("salt", () -> new SaltItem());
	public static final RegistryObject<Item> SALT_PINCH = MOD_ITEMS.register("salt_pinch", () -> new SaltItem());
	public static final RegistryObject<Item> SALTWORT_SEED = MOD_ITEMS.register("saltwort_seed", () -> new SaltwortSeed());
	public static final RegistryObject<Item> SODA = MOD_ITEMS.register("soda", () -> new SaltItem());
	public static final RegistryObject<Item> MINERAL_MUD = MOD_ITEMS.register("mineral_mud", () -> new SaltItem());

	public static final RegistryObject<Item> SALT_COOKED_BEEF = MOD_ITEMS.register("salt_cooked_beef", () -> new SaltDelegate(Items.COOKED_BEEF));
	public static final RegistryObject<Item> SALT_COOKED_PORKCHOP = MOD_ITEMS.register("salt_cooked_porkchop", () -> new SaltDelegate(Items.COOKED_PORKCHOP));
	public static final RegistryObject<Item> SALT_COOKED_MUTTON = MOD_ITEMS.register("salt_cooked_mutton", () -> new SaltDelegate(Items.COOKED_MUTTON));
	public static final RegistryObject<Item> SALT_BAKED_POTATO = MOD_ITEMS.register("salt_baked_potato", () -> new SaltDelegate(Items.BAKED_POTATO));
	public static final RegistryObject<Item> SALT_COOKED_CHICKEN = MOD_ITEMS.register("salt_cooked_chicken", () -> new SaltDelegate(Items.COOKED_CHICKEN));
	public static final RegistryObject<Item> SALT_COOKED_RABBIT = MOD_ITEMS.register("salt_cooked_rabbit", () -> new SaltDelegate(Items.COOKED_RABBIT));
	public static final RegistryObject<Item> SALT_COD = MOD_ITEMS.register("salt_cod", () -> new SaltDelegate(Items.COD));
	public static final RegistryObject<Item> SALT_COOKED_COD = MOD_ITEMS.register("salt_cooked_cod", () -> new SaltDelegate(Items.COOKED_COD));
	public static final RegistryObject<Item> SALT_SALMON = MOD_ITEMS.register("salt_salmon", () -> new SaltDelegate(Items.SALMON));
	public static final RegistryObject<Item> SALT_COOKED_SALMON = MOD_ITEMS.register("salt_cooked_salmon", () -> new SaltDelegate(Items.COOKED_SALMON));
	public static final RegistryObject<Item> SALT_TROPICAL_FISH = MOD_ITEMS.register("salt_tropical_fish", () -> new SaltDelegate(Items.TROPICAL_FISH));
	public static final RegistryObject<Item> SALT_BEETROOT = MOD_ITEMS.register("salt_beetroot", () -> new SaltDelegate(Items.BEETROOT));
	public static final RegistryObject<Item> SALT_ROTTEN_FLESH = MOD_ITEMS.register("salt_rotten_flesh", () -> new SaltDelegate(Items.ROTTEN_FLESH));
	public static final RegistryObject<Item> SALT_BREAD = MOD_ITEMS.register("salt_bread", () -> new SaltDelegate(Items.BREAD));
	public static final RegistryObject<Item> SALT_EGG = MOD_ITEMS.register("salt_egg", () -> new SaltDelegate(Items.EGG));
	public static final RegistryObject<Item> SALT_RABBIT_STEW = MOD_ITEMS.register("salt_rabbit_stew", () -> new SaltDelegate(Items.RABBIT_STEW));
	public static final RegistryObject<Item> SALT_MUSHROOM_STEW = MOD_ITEMS.register("salt_mushroom_stew", () -> new SaltDelegate(Items.MUSHROOM_STEW));
	public static final RegistryObject<Item> SALT_BEETROOT_SOUP = MOD_ITEMS.register("salt_beetroot_soup", () -> new SaltDelegate(Items.BEETROOT_SOUP));
	
	public static final RegistryObject<Item> PUMPKIN_PORRIDGE = MOD_ITEMS.register("pumpkin_porridge", () -> new SaltSoup(ModFoods.PUMPKIN_PORRIDGE));
	public static final RegistryObject<Item> SALT_PUMPKIN_PORRIDGE = MOD_ITEMS.register("salt_pumpkin_porridge", () -> new SaltDelegate(ModItems.PUMPKIN_PORRIDGE.get()));
	public static final RegistryObject<Item> VEGETABLE_STEW = MOD_ITEMS.register("vegetable_stew", () -> new SaltSoup(ModFoods.VEGETABLE_STEW));
	public static final RegistryObject<Item> SALT_VEGETABLE_STEW = MOD_ITEMS.register("salt_vegetable_stew", () -> new SaltDelegate(ModItems.VEGETABLE_STEW.get()));
	public static final RegistryObject<Item> POTATO_MUSHROOM = MOD_ITEMS.register("potato_mushroom", () -> new SaltSoup(ModFoods.POTATO_MUSHROOM));
	public static final RegistryObject<Item> SALT_POTATO_MUSHROOM = MOD_ITEMS.register("salt_potato_mushroom", () -> new SaltDelegate(ModItems.POTATO_MUSHROOM.get()));
	public static final RegistryObject<Item> COD_SOUP = MOD_ITEMS.register("cod_soup", () -> new SaltSoup(ModFoods.COD_SOUP));
	public static final RegistryObject<Item> SALT_COD_SOUP = MOD_ITEMS.register("salt_cod_soup", () -> new SaltDelegate(ModItems.COD_SOUP.get()));
	public static final RegistryObject<Item> SALMON_SOUP = MOD_ITEMS.register("salmon_soup", () -> new SaltSoup(ModFoods.SALMON_SOUP));
	public static final RegistryObject<Item> SALT_SALMON_SOUP = MOD_ITEMS.register("salt_salmon_soup", () -> new SaltDelegate(ModItems.SALMON_SOUP.get()));
	public static final RegistryObject<Item> SALTWORT_BEEF = MOD_ITEMS.register("saltwort_beef", () -> new SaltSoup(ModFoods.SALTWORT_BEEF));
	public static final RegistryObject<Item> SALTWORT_PORKCHOP = MOD_ITEMS.register("saltwort_porkchop", () -> new SaltSoup(ModFoods.SALTWORT_PORKCHOP));
	public static final RegistryObject<Item> SALTWORT_MUTTON = MOD_ITEMS.register("saltwort_mutton", () -> new SaltSoup(ModFoods.SALTWORT_MUTTON));
	public static final RegistryObject<Item> BEETROOT_SALAD = MOD_ITEMS.register("beetroot_salad", () -> new SaltSoup(ModFoods.BEETROOT_SALAD));
	public static final RegistryObject<Item> SALT_BEETROOT_SALAD = MOD_ITEMS.register("salt_beetroot_salad", () -> new SaltDelegate(ModItems.BEETROOT_SALAD.get(), new SaltyFoodBuilder().saturation(0.65F), false));
	public static final RegistryObject<Item> HUFC = MOD_ITEMS.register("hufc", () -> new SaltSoup(ModFoods.HUFC));
	public static final RegistryObject<Item> SALT_HUFC = MOD_ITEMS.register("salt_hufc", () -> new SaltDelegate(ModItems.HUFC.get()));
	public static final RegistryObject<Item> DANDELION_SALAD = MOD_ITEMS.register("dandelion_salad", () -> new SaltSoup(ModFoods.DANDELION_SALAD));
	public static final RegistryObject<Item> SALT_DANDELION_SALAD = MOD_ITEMS.register("salt_dandelion_salad", () -> new SaltDelegate(ModItems.DANDELION_SALAD.get(), new SaltyFoodBuilder().saturation(0.35F), false));
	public static final RegistryObject<Item> WHEAT_SPROUTS = MOD_ITEMS.register("wheat_sprouts", () -> new SaltSoup(ModFoods.WHEAT_SPROUTS));
	public static final RegistryObject<Item> SALT_WHEAT_SPROUTS = MOD_ITEMS.register("salt_wheat_sprouts", () -> new SaltDelegate(ModItems.WHEAT_SPROUTS.get()));
	public static final RegistryObject<Item> FRUIT_SALAD = MOD_ITEMS.register("fruit_salad", () -> new SaltSoup(ModFoods.FRUIT_SALAD));
	public static final RegistryObject<Item> GRATED_CARROT = MOD_ITEMS.register("grated_carrot", () -> new SaltSoup(ModFoods.GRATED_CARROT));
	public static final RegistryObject<Item> SALTWORT_SALAD = MOD_ITEMS.register("saltwort_salad", () -> new SaltSoup(ModFoods.SALTWORT_SALAD));
	public static final RegistryObject<Item> CARROT_PIE = MOD_ITEMS.register("carrot_pie", () -> new SaltFood(ModFoods.CARROT_PIE));
	public static final RegistryObject<Item> APPLE_PIE = MOD_ITEMS.register("apple_pie", () -> new SaltFood(ModFoods.APPLE_PIE));
	public static final RegistryObject<Item> POTATO_PIE = MOD_ITEMS.register("potato_pie", () -> new SaltFood(ModFoods.POTATO_PIE));
	public static final RegistryObject<Item> ONION_PIE = MOD_ITEMS.register("onion_pie", () -> new SaltFood(ModFoods.ONION_PIE));
	public static final RegistryObject<Item> COD_PIE = MOD_ITEMS.register("cod_pie", () -> new SaltFood(ModFoods.COD_PIE));
	public static final RegistryObject<Item> SALMON_PIE = MOD_ITEMS.register("salmon_pie", () -> new SaltFood(ModFoods.SALMON_PIE));
	public static final RegistryObject<Item> MUSHROOM_PIE = MOD_ITEMS.register("mushroom_pie", () -> new SaltFood(ModFoods.MUSHROOM_PIE));
	public static final RegistryObject<Item> SALTWORT_PIE = MOD_ITEMS.register("saltwort_pie", () -> new SaltFood(ModFoods.SALTWORT_PIE));
	public static final RegistryObject<Item> FERMENTED_SALTWORT = MOD_ITEMS.register("fermented_saltwort", () -> new SaltBottle(ModFoods.FERMENTED_SALTWORT));
	public static final RegistryObject<Item> PICKLED_MUSHROOM = MOD_ITEMS.register("pickled_mushroom", () -> new SaltBottle(ModFoods.PICKLED_MUSHROOM));
	public static final RegistryObject<Item> PICKLED_FERN = MOD_ITEMS.register("pickled_fern", () -> new SaltBottle(ModFoods.PICKLED_FERN));
	public static final RegistryObject<Item> MUFFIN = MOD_ITEMS.register("muffin", () -> new Muffin(ModFoods.MUFFIN));
	public static final RegistryObject<Item> FIZZY_DRINK = MOD_ITEMS.register("fizzy_drink", () -> new FizzyDrink());

	public static final RegistryObject<Item> MUD_HELMET = MOD_ITEMS.register("mud_helmet", () -> new MudArmor(EquipmentSlotType.HEAD));
	public static final RegistryObject<Item> MUD_CHESTPLATE = MOD_ITEMS.register("mud_chestplate", () -> new MudArmor(EquipmentSlotType.CHEST));
	public static final RegistryObject<Item> MUD_LEGGINGS =	MOD_ITEMS.register("mud_leggings", () -> new MudArmor(EquipmentSlotType.LEGS));
	public static final RegistryObject<Item> MUD_BOOTS = MOD_ITEMS.register("mud_boots", () -> new MudArmor(EquipmentSlotType.FEET));

	public static final RegistryObject<Item> POWDERED_MILK = MOD_ITEMS.register("powdered_milk", () -> new SaltItem());
	public static final RegistryObject<Item> HEMOGLOBIN = MOD_ITEMS.register("hemoglobin", () -> new SaltFood(ModFoods.HEMOGLOBIN));
	public static final RegistryObject<Item> ESCARGOT = MOD_ITEMS.register("escargot", () -> new SaltFood(ModFoods.ESCARGOT));

	public static final RegistryObject<Item> SALT_STAR = MOD_ITEMS.register("salt_star", () -> new SaltItem());
	public static final RegistryObject<Item> RAINMAKER = MOD_ITEMS.register("rainmaker", () -> new Rainmaker());

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