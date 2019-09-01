package ru.liahim.saltmod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import ru.liahim.saltmod.api.item.SaltItems;

public class SaltFood extends ItemFood {

	private PotionEffect effect = null;
	private ItemStack container = ItemStack.EMPTY;

	public SaltFood(int amount, float saturation) {
		super(amount, saturation, false);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		if (effect != null && this != SaltItems.ESCARGOT) {
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
	public EnumAction getItemUseAction(ItemStack stack) {
		if (this == SaltItems.FERMENTED_SALTWORT) return EnumAction.DRINK;
		return EnumAction.EAT;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase living) {
		if (!world.isRemote && this == SaltItems.SALT_EGG) {
			world.spawnEntity(new EntityItem(world, living.posX, living.posY + living.height, living.posZ, new ItemStack(Items.DYE, 1, 15)));
		}
		stack = super.onItemUseFinish(stack, world, living);
		if (!this.container.isEmpty()) stack = this.container;
		return stack;
	}

    public Item setContainer(Item containerItem) {
        this.container = new ItemStack(containerItem);
        return this;
    }
}