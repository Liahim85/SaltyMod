package ru.liahim.saltmod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SaltFood extends SaltItem {

	public SaltFood(Properties properties) {
		super(properties);
	}

	public SaltFood(Food food) {
		this(new Item.Properties().food(food));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		this.getFood().getEffects().forEach(map -> {
			EffectInstance eff = map.getLeft();
			ITextComponent text = new TranslationTextComponent(eff.getEffectName());
			if (eff.getAmplifier() > 0) text.appendText(" ").appendSibling(new TranslationTextComponent("potion.potency." + eff.getAmplifier()));
			if (eff.getDuration() > 20) text.appendText(" (").appendText(EffectUtils.getPotionDurationString(eff, 1)).appendText(")");
			tooltip.add(text.applyTextStyle(eff.getPotion().getEffectType().getColor()));
		});
	}
}