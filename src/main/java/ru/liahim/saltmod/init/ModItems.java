package ru.liahim.saltmod.init;

import static ru.liahim.saltmod.api.item.SaltItems.*;
import static ru.liahim.saltmod.api.item.ExItems.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.item.AchievItem;
import ru.liahim.saltmod.item.FizzyDrink;
import ru.liahim.saltmod.item.MudArmor;
import ru.liahim.saltmod.item.Muffin;
import ru.liahim.saltmod.item.Rainmaker;
import ru.liahim.saltmod.item.Salt;
import ru.liahim.saltmod.item.SaltFood;
import ru.liahim.saltmod.item.SaltFoodSoup;
import ru.liahim.saltmod.item.SaltWortSeed;

public class ModItems {

	public static ArmorMaterial MUD_MATERIAL = addArmorMaterial("mud_material", "saltmod:mud_armor", 4, new int[] {1, 1, 1, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);

	public static void registerItems() {
		SaltyMod.logger.info("Start to initialize Items");
		//Achiev
		ACHIEV_ITEM =				registerItem(new AchievItem(), "achiev_item", null);

		//Main Items
		SALT =						registerItem(new Salt(), "salt");
		SALT_PINCH =				registerItem(new Item(), "salt_pinch");
		SALTWORT_SEED =				registerFood(new SaltWortSeed(), "saltwort_seed");
		//0.4
		SODA =						registerItem(new Item(), "soda");
		MINERAL_MUD =				registerItem(new Item(), "mineral_mud");

		//Food Items
		SALT_BEEF_COOKED =			registerFood(new SaltFood(9, 0.8F), "salt_beef_cooked");
		//14.4
		SALT_PORKCHOP_COOKED =		registerFood(new SaltFood(9, 0.8F), "salt_porkchop_cooked");
		//14.4
		SALT_MUTTON_COOKED =		registerFood(new SaltFood(7, 0.8F), "salt_mutton_cooked");
		//11.2
		SALT_POTATO_BAKED =			registerFood(new SaltFood(7, 0.6F), "salt_potato_baked");
		//8.4
		SALT_CHICKEN_COOKED =		registerFood(new SaltFood(7, 0.6F), "salt_chicken_cooked");
		//8.4
		SALT_RABBIT_COOKED =		registerFood(new SaltFood(6, 0.6F), "salt_rabbit_cooked");
		//7.2
		SALT_FISH_COD =				registerFood(new SaltFood(5, 0.4F), "salt_fish_cod");
		//4.0
		SALT_FISH_COD_COOKED =		registerFood(new SaltFood(6, 0.6F), "salt_fish_cod_cooked");
		//7.2
		SALT_FISH_SALMON =			registerFood(new SaltFood(6, 0.4F), "salt_fish_salmon");
		//4.8
		SALT_FISH_SALMON_COOKED =	registerFood(new SaltFood(7, 0.6F), "salt_fish_salmon_cooked");
		//8.4
		SALT_FISH_CLOWNFISH =		registerFood(new SaltFood(3, 0.3F), "salt_fish_clownfish");
		//1.8
		SALT_BEETROOT =				registerFood(new SaltFood(2, 0.5F), "salt_beetroot");
		//2.0
		CORNED_BEEF =				registerFood(new SaltFood(5, 0.3F), "corned_beef");
		//3.0
		SALT_BREAD =				registerFood(new SaltFood(6, 0.6F), "salt_bread");
		//7.2
		SALT_EGG =					registerFood((ItemFood)new SaltFood(4, 0.6F).setMaxStackSize(16), "salt_egg");
		//4.8
		SALT_RABBIT_STEW =			registerFood(new SaltFoodSoup(11, 0.6F), "salt_rabbit_stew");
		//13.2
		SALT_MUSHROOM_STEW =		registerFood(new SaltFoodSoup(7, 0.6F), "salt_mushroom_stew");
		//8.4
		SALT_BEETROOT_SOUP =		registerFood(new SaltFoodSoup(7, 0.6F), "salt_beetroot_soup");
		//8.4
		PUMPKIN_PORRIDGE =			registerFood(new SaltFoodSoup(6, 0.4F), "pumpkin_porridge");
		//4.8
		VEGETABLE_STEW =			registerFood(new SaltFoodSoup(5, 0.6F), "vegetable_stew");
		//6.0
		SALT_VEGETABLE_STEW =		registerFood(new SaltFoodSoup(6, 0.6F), "salt_vegetable_stew");
		//7.2
		POTATO_MUSHROOM =			registerFood(new SaltFoodSoup(5, 0.6F), "potato_mushroom");
		//6.0
		SALT_POTATO_MUSHROOM =		registerFood(new SaltFoodSoup(6, 0.6F), "salt_potato_mushroom");
		//7.2
		FISH_SOUP =					registerFood(new SaltFoodSoup(6, 0.6F), "fish_soup");
		//7.2
		SALT_FISH_SOUP =			registerFood(new SaltFoodSoup(7, 0.6F), "salt_fish_soup");
		//8.4
		FISH_SALMON_SOUP =			registerFood(new SaltFoodSoup(7, 0.5F), "fish_salmon_soup");
		//7.0
		SALT_FISH_SALMON_SOUP =		registerFood(new SaltFoodSoup(8, 0.5F), "salt_fish_salmon_soup");
		//8.0
		SALTWORT_BEEF =				registerFood(new SaltFoodSoup(10, 0.7F), "saltwort_beef", new PotionEffect(MobEffects.REGENERATION, 100, 0));
		//14.0
		SALTWORT_PORKCHOP =			registerFood(new SaltFoodSoup(10, 0.7F), "saltwort_porkchop", new PotionEffect(MobEffects.REGENERATION, 100, 0));
		//14.0
		SALTWORT_MUTTON =			registerFood(new SaltFoodSoup(8, 0.7F), "saltwort_mutton", new PotionEffect(MobEffects.REGENERATION, 100, 0));
		//11.2
		BEETROOT_SALAD =			registerFood(new SaltFoodSoup(5, 0.7F), "beetroot_salad");
		//7.0
		SALT_BEETROOT_SALAD =		registerFood(new SaltFoodSoup(6, 0.65F), "salt_beetroot_salad");
		//7.8
		HUFC =						registerFood(new SaltFoodSoup(6, 0.6F), "hufc", new PotionEffect(MobEffects.HASTE, 800, 1));
		//7.2
		SALT_HUFC =					registerFood(new SaltFoodSoup(7, 0.6F), "salt_hufc", new PotionEffect(MobEffects.HASTE, 1200, 1));
		//8.4
		DANDELION_SALAD =			registerFood(new SaltFoodSoup(4, 0.3F), "dandelion_salad", new PotionEffect(MobEffects.HEALTH_BOOST, 800, 0));
		//2.4
		SALT_DANDELION_SALAD =		registerFood(new SaltFoodSoup(5, 0.35F), "salt_dandelion_salad", new PotionEffect(MobEffects.HEALTH_BOOST, 1200, 0));
		//3.5
		WHEAT_SPROUTS =				registerFood(new SaltFoodSoup(3, 0.6F), "wheat_sprouts", new PotionEffect(MobEffects.HEALTH_BOOST, 600, 0));
		//3.6
		SALT_WHEAT_SPROUTS =		registerFood(new SaltFoodSoup(4, 0.6F), "salt_wheat_sprouts", new PotionEffect(MobEffects.HEALTH_BOOST, 900, 0));
		//4.8
		FRUIT_SALAD =				registerFood(new SaltFoodSoup(5, 0.5F), "fruit_salad", new PotionEffect(MobEffects.SPEED, 800, 0));
		//5.0
		GRATED_CARROT =				registerFood(new SaltFoodSoup(5, 0.5F), "grated_carrot", new PotionEffect(MobEffects.NIGHT_VISION, 800, 0));
		//5.0
		SALTWORT_SALAD =			registerFood(new SaltFoodSoup(6, 0.2F), "saltwort_salad", new PotionEffect(MobEffects.REGENERATION, 200, 1));
		//2.4
		CARROT_PIE =				registerFood(new SaltFood(8, 0.45F), "carrot_pie");
		//7.2
		APPLE_PIE =					registerFood(new SaltFood(8, 0.4F), "apple_pie");
		//6.4
		POTATO_PIE =				registerFood(new SaltFood(8, 0.3F), "potato_pie");
		//4.8
		ONION_PIE =					registerFood(new SaltFood(7, 0.35F), "onion_pie");
		//4.9
		FISH_PIE =					registerFood(new SaltFood(8, 0.55F), "fish_pie");
		//8.8
		FISH_SALMON_PIE =			registerFood(new SaltFood(9, 0.55F), "fish_salmon_pie");
		//9.9
		MUSHROOM_PIE =				registerFood(new SaltFood(8, 0.45F), "mushroom_pie");
		//7.2
		SALTWORT_PIE =				registerFood(new SaltFood(6, 0.3F), "saltwort_pie", new PotionEffect(MobEffects.REGENERATION, 100, 0));
		//3.6
		FERMENTED_SALTWORT =		registerFood(new SaltFood(5, 0.35F), "fermented_saltwort", Items.GLASS_BOTTLE, new PotionEffect(MobEffects.REGENERATION, 600, 2));
		//3.5
		PICKLED_MUSHROOM =			registerFood(new SaltFood(8, 0.4F), "pickled_mushroom", Items.GLASS_BOTTLE);
		//6.4
		PICKLED_FERN =				registerFood(new SaltFood(4, 0.6F), "pickled_fern", Items.GLASS_BOTTLE, new PotionEffect(MobEffects.RESISTANCE, 800));
		//4.8
		FIZZY_DRINK =				registerItem(new FizzyDrink(), "fizzy_drink");
		MUFFIN =					registerFood(new Muffin(), "muffin");
		//19.8

		//Armor
		MUD_HELMET =				registerItem(new MudArmor(MUD_MATERIAL, EntityEquipmentSlot.HEAD), "mud_helmet");
		MUD_CHESTPLATE =			registerItem(new MudArmor(MUD_MATERIAL, EntityEquipmentSlot.CHEST), "mud_chestplate");
		MUD_LEGGINGS =				registerItem(new MudArmor(MUD_MATERIAL, EntityEquipmentSlot.LEGS), "mud_leggings");
		MUD_BOOTS =					registerItem(new MudArmor(MUD_MATERIAL, EntityEquipmentSlot.FEET), "mud_boots");

		//Milk
		POWDERED_MILK =				registerItem(new Item(), "powdered_milk");

		//Escargot
		ESCARGOT =					registerFood(new SaltFood(3, 0.6F), "escargot", null, new PotionEffect(MobEffects.NAUSEA, 300, 0), 0.3F);
		//2.4

		//Rainmaker
		SALT_STAR =					registerItem(new Item(), "salt_star");
		RAINMAKER =					registerItem(new Rainmaker(), "rainmaker");

		createExternalItems();
		SaltyMod.logger.info("Finished initializing Items");
    }

	private static void createExternalItems() {
		//Blood
		HEMOGLOBIN =				new SaltFood(2, 1.0F).setPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 1), 1.0F).setAlwaysEdible();

		//BOP
		BOP_POISON =				new Item().setCreativeTab(SaltyMod.saltTab);
	    BOP_SALT_SALAD_VEGGIE =		(ItemFood) new SaltFoodSoup(7, 0.7F).setPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1100, 1), 0.1F).setAlwaysEdible().setMaxStackSize(1).setCreativeTab(SaltyMod.saltTab);
	    BOP_SALT_SALAD_SHROOM =		(ItemFood) new SaltFoodSoup(7, 0.7F).setPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 550, 1), 0.1F).setAlwaysEdible().setMaxStackSize(1).setCreativeTab(SaltyMod.saltTab);
	    BOP_SALT_RICE_BOWL =		(ItemFood) new SaltFoodSoup(3, 0.6F).setMaxStackSize(1).setCreativeTab(SaltyMod.saltTab);
	    BOP_SALT_SHROOM_POWDER =	(ItemFood) new SaltFood(2, 0.3F).setPotionEffect(new PotionEffect(MobEffects.NAUSEA, 225, 0), 0.3F).setAlwaysEdible().setCreativeTab(SaltyMod.saltTab);
	    BOP_PICKLED_TURNIP =		(ItemFood) new SaltFood(7, 0.75F).setContainer(Items.GLASS_BOTTLE).setMaxStackSize(1).setCreativeTab(SaltyMod.saltTab);

		//TF
	    TF_SALT_VENISON_COOKED =	(ItemFood) new SaltFood(9, 0.5F).setCreativeTab(SaltyMod.saltTab);
	    TF_SALT_MEFF_STEAK =		(ItemFood) new SaltFood(7, 0.5F).setCreativeTab(SaltyMod.saltTab);
	    TF_SALT_MEEF_STROGANOFF =	(ItemFood) new SaltFoodSoup(9, 0.05F).setMaxStackSize(1).setCreativeTab(SaltyMod.saltTab);
	    TF_SALT_HYDRA_CHOP =		(ItemFood) new SaltFood(19, 0.5F).setPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0), 1.0F).setCreativeTab(SaltyMod.saltTab);
	    TF_PICKLED_MUSHGLOOM =		(ItemFood) ((SaltFood)new SaltFood(4, 0.6F).setPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1200, 0), 1.0F).setAlwaysEdible()).setContainer(Items.GLASS_BOTTLE).setMaxStackSize(1).setCreativeTab(SaltyMod.saltTab);
		TF_SALTWORT_VENISON =		(ItemFood) new SaltFoodSoup(10, 0.45F).setPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0), 1.0F).setMaxStackSize(1).setCreativeTab(SaltyMod.saltTab);
		TF_SALTWORT_MEEF_STEAK =	(ItemFood) new SaltFoodSoup(8, 0.45F).setPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0), 1.0F).setMaxStackSize(1).setCreativeTab(SaltyMod.saltTab);
	}

	private static Item registerItem(Item item, String registryName, CreativeTabs tab) {
		item.setRegistryName(registryName).setUnlocalizedName(registryName).setCreativeTab(tab);
		ForgeRegistries.ITEMS.register(item);
		return item;
	}

	public static Item registerItem(Item item, String registryName) {
		return registerItem(item, registryName, SaltyMod.saltTab);
	}

	private static ItemFood registerFood(ItemFood food, String registryName, Item container, PotionEffect effect, float probability) {
		if (container != null) {
			if (food instanceof SaltFood) ((SaltFood) food).setContainer(container);
			else if (!(food instanceof SaltFoodSoup)) food.setContainerItem(container);
			food.setMaxStackSize(1);
		}
		if (effect != null) {
			food.setPotionEffect(effect, probability).setAlwaysEdible();
		}
		return (ItemFood) registerItem(food, registryName);
	}

	private static ItemFood registerFood(ItemFood food, String registryName, Item container, PotionEffect effect) {
		return registerFood(food, registryName, container, effect, 1.0F);
	}

	private static ItemFood registerFood(ItemFood food, String registryName, PotionEffect effect) {
		return registerFood(food, registryName, null, effect, 1.0F);
	}

	private static ItemFood registerFood(ItemFood food, String registryName, Item container) {
		return registerFood(food, registryName, container, null, 1.0F);
	}

	public static ItemFood registerFood(ItemFood food, String registryName) {
		return registerFood(food, registryName, null, null, 1.0F);
	}

	private static ItemArmor.ArmorMaterial addArmorMaterial(String enumName, String textureName, int durability, int[] reductionAmounts, int enchantability, SoundEvent soundOnEquip, float toughness) {
		return EnumHelper.addEnum(ItemArmor.ArmorMaterial.class, enumName, new Class<?>[]{String.class, int.class, int[].class, int.class, SoundEvent.class, float.class}, textureName, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
	}
}