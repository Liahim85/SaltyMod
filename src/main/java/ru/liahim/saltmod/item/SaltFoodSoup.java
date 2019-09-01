package ru.liahim.saltmod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class SaltFoodSoup extends ItemSoup {

	private PotionEffect effect = null;
    private final float saturation;

	public SaltFoodSoup(int amount, float saturation) {
		super(amount);
		this.saturation = saturation;
	}

	public void setEffect(PotionEffect effect) {
		this.effect = effect;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		if (effect != null) {
			String mess = "";
			mess += (effect.getPotion().isBadEffect() ? TextFormatting.RED : TextFormatting.GRAY);
			mess += I18n.format(effect.getEffectName()).trim();

			if (effect.getAmplifier() == 1) mess += " II";
			else if (effect.getAmplifier() == 2) mess += " III";
			else if (effect.getAmplifier() == 3) mess += " IV";
			else if (effect.getAmplifier() == 4) mess += " V";

			if (effect.getDuration() > 20) {
				mess += " (" + Potion.getPotionDurationString(effect, 1) + ")";
			}

			mess += TextFormatting.RESET;
			tooltip.add(mess);
		}
	}

	@Override
	public ItemFood setPotionEffect(PotionEffect effect, float probability) {
		super.setPotionEffect(effect, probability);
		if (probability == 1.0F) this.effect = effect;
		return this;
	}

	@Override
	public float getSaturationModifier(ItemStack stack) {
		return this.saturation;
	}
}