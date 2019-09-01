package ru.liahim.saltmod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.SaltConfig;

public class FizzyDrink extends Item {

	public FizzyDrink() {
		this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		EntityPlayer player = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null;
		if (!worldIn.isRemote) {
			if (SaltConfig.fizzyEffect) entityLiving.clearActivePotions();
			else entityLiving.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
			if (entityLiving.isBurning()) {
				if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.FIZZY_DRINK));
				entityLiving.extinguish();
			}
		}
		if ((player != null && !player.isCreative()) || player == null) {
			return new ItemStack(Items.GLASS_BOTTLE);
		}
		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack item) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack item) {
		return EnumAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}