package ru.liahim.saltmod.item;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.init.ModAdvancements;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class Muffin extends ItemFood {

	public Muffin() {
		super(3, 3.3F, false);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}

	@Override
	public void onFoodEaten(ItemStack item, World world, EntityPlayer player) {
		boolean chek = false;

		if (player.getFoodStats().getFoodLevel() == 20) {
			chek = true;
		}

		if (!world.isRemote && chek) {
			player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 2400));
			if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.MUFFIN));
		}

		if (world.isRemote && player.getFoodStats().getFoodLevel() == 20) {
			Random rand = new Random();
			player.sendMessage(new TextComponentString(I18n.format(getUnlocalizedName() + ".mess." + rand.nextInt(4))));
		}
	}
}