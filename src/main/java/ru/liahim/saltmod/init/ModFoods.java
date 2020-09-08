package ru.liahim.saltmod.init;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoods {

	public static final Food SALTWORT = (new Food.Builder())
			.hunger(1)
			.saturation(0.2F)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 40, 1), 0.8F)
			.setAlwaysEdible()
			.build();
	public static final Food PUMPKIN_PORRIDGE = (new Food.Builder())
			.hunger(6)
			.saturation(0.4F)
			.build();
	public static final Food VEGETABLE_STEW = (new Food.Builder())
			.hunger(5)
			.saturation(0.6F)
			.build();
	public static final Food POTATO_MUSHROOM = (new Food.Builder())
			.hunger(5)
			.saturation(0.6F)
			.build();
	public static final Food COD_SOUP = (new Food.Builder())
			.hunger(6)
			.saturation(0.6F)
			.build();
	public static final Food SALMON_SOUP = (new Food.Builder())
			.hunger(7)
			.saturation(0.5F)
			.build();
	public static final Food SALTWORT_BEEF = (new Food.Builder())
			.hunger(10)
			.saturation(0.7F)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 100, 0), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food SALTWORT_PORKCHOP = (new Food.Builder())
			.hunger(10)
			.saturation(0.7F)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 100, 0), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food SALTWORT_MUTTON = (new Food.Builder())
			.hunger(8)
			.saturation(0.7F)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 100, 0), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food BEETROOT_SALAD = (new Food.Builder())
			.hunger(5)
			.saturation(0.7F)
			.build();
	public static final Food HUFC = (new Food.Builder())
			.hunger(6)
			.saturation(0.6F)
			.effect(() -> new EffectInstance(Effects.HASTE, 800, 1), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food DANDELION_SALAD = (new Food.Builder())
			.hunger(4)
			.saturation(0.3F)
			.effect(() -> new EffectInstance(Effects.HEALTH_BOOST, 800, 0), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food WHEAT_SPROUTS = (new Food.Builder())
			.hunger(3)
			.saturation(0.6F)
			.effect(() -> new EffectInstance(Effects.HEALTH_BOOST, 600, 0), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food FRUIT_SALAD = (new Food.Builder())
			.hunger(5)
			.saturation(0.5F)
			.effect(() -> new EffectInstance(Effects.SPEED, 800, 0), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food GRATED_CARROT = (new Food.Builder())
			.hunger(5)
			.saturation(0.5F)
			.effect(() -> new EffectInstance(Effects.NIGHT_VISION, 800, 0), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food SALTWORT_SALAD = (new Food.Builder())
			.hunger(6)
			.saturation(0.2F)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 200, 1), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food CARROT_PIE = (new Food.Builder())
			.hunger(8)
			.saturation(0.45F)
			.build();
	public static final Food APPLE_PIE = (new Food.Builder())
			.hunger(8)
			.saturation(0.4F)
			.build();
	public static final Food POTATO_PIE = (new Food.Builder())
			.hunger(8)
			.saturation(0.3F)
			.build();
	public static final Food ONION_PIE = (new Food.Builder())
			.hunger(7)
			.saturation(0.35F)
			.build();
	public static final Food COD_PIE = (new Food.Builder())
			.hunger(8)
			.saturation(0.55F)
			.build();
	public static final Food SALMON_PIE = (new Food.Builder())
			.hunger(9)
			.saturation(0.55F)
			.build();
	public static final Food MUSHROOM_PIE = (new Food.Builder())
			.hunger(8)
			.saturation(0.45F)
			.build();
	public static final Food SALTWORT_PIE = (new Food.Builder())
			.hunger(6)
			.saturation(0.3F)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 100, 0), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food FERMENTED_SALTWORT = (new Food.Builder())
			.hunger(5)
			.saturation(0.35F)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 600, 2), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food PICKLED_MUSHROOM = (new Food.Builder())
			.hunger(8)
			.saturation(0.4F)
			.build();
	public static final Food PICKLED_FERN = (new Food.Builder())
			.hunger(4)
			.saturation(0.6F)
			.effect(() -> new EffectInstance(Effects.RESISTANCE, 800, 0), 1.0F)
			.setAlwaysEdible()
			.build();
	public static final Food MUFFIN = (new Food.Builder())
			.hunger(3)
			.saturation(3.3F)
			.build();
	public static final Food ESCARGOT = (new Food.Builder())
			.hunger(3)
			.saturation(0.6F)
			.effect(() -> new EffectInstance(Effects.NAUSEA, 300, 0), 0.3F)
			.build();
	public static final Food HEMOGLOBIN = (new Food.Builder())
			.hunger(2)
			.saturation(1.0F)
			.effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 1), 1.0F)
			.setAlwaysEdible()
			.build();
}