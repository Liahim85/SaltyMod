package ru.liahim.saltmod.item;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import ru.liahim.saltmod.init.SaltyFoodRegister.SaltyFoodBuilder;

public class SaltDelegate extends SaltFood {

	private final Item base;
	private final boolean needName;

	@SuppressWarnings("deprecation")
	public SaltDelegate(Item base, SaltyFoodBuilder food, boolean needName) {
		super(new Item.Properties()
				.food(getFood(base.getFood(), food))
				.containerItem(base.getContainerItem())
				.defaultMaxDamage(base.getMaxDamage())
				.maxStackSize(base.getMaxStackSize()));
		this.base = base;
		this.needName = needName;
	}

	public SaltDelegate(Item base) {
		this(base, null, false);
	}

	private static Food getFood(Food food, SaltyFoodBuilder newFood) {
		int heal = newFood != null && newFood.getHeal() >= 0 ? newFood.getHeal() : food != null ? food.getHealing() + 1 : 1;
		float saturation = newFood != null && newFood.getSaturation() >= 0 ? newFood.getSaturation() : food != null ? food.getSaturation() : 0.6F;
		boolean meat = newFood != null && newFood.getMeat() != 0 ? newFood.getMeat() < 0 ? false : true : food != null ? food.isMeat() : false;
		boolean always = newFood != null && newFood.getAlwaysEdible() != 0 ? newFood.getAlwaysEdible() < 0 ? false : true : food != null ? food.canEatWhenFull() : false;
		boolean fast = newFood != null && newFood.getFastToEat() != 0 ? newFood.getFastToEat() < 0 ? false : true : food != null ? food.isFastEating() : false;
		Food.Builder bilder = new Food.Builder()
				.hunger(heal)
				.saturation(saturation);
		if (meat) bilder = bilder.meat();
		if (always) bilder = bilder.setAlwaysEdible();
		if (fast) bilder = bilder.fastToEat();
		if (food != null) {
			for (Pair<EffectInstance, Float> pair : food.getEffects()) {
				if (pair.getLeft().getPotion().getEffectType() != EffectType.HARMFUL) {
					EffectInstance eff = new EffectInstance(pair.getLeft().getPotion(), (pair.getLeft().getDuration()*3)/2, pair.getLeft().getAmplifier());
					bilder = bilder.effect(() -> eff, pair.getRight());
				}
			}
		}
		return bilder.build();
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		if (this.needName) return new TranslationTextComponent("item.saltmod.pre_salt")
				.appendText(this.base.getDisplayName(stack).getFormattedText())
				.appendText(new TranslationTextComponent("item.saltmod.post_salt").getFormattedText());
		else return super.getDisplayName(stack);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
		return this.base.onItemUseFinish(stack, world, entity);
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return this.base.getContainerItem(stack);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return this.base.hasContainerItem(stack);
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return this.base.getMaxDamage(stack);
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return this.base.getRarity(stack);
	}
}