package ru.liahim.saltmod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.ModItems;
import ru.liahim.saltmod.init.SaltConfig;

public class FizzyDrink extends SaltBottle {

	public FizzyDrink() {
		super(new Item.Properties().maxStackSize(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		tooltip.add(new TranslationTextComponent(this.getDefaultTranslationKey() + ".tooltip"));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
		if (!world.isRemote) {
			if (SaltConfig.Game.fizzyEffect.get()) entity.clearActivePotions();
			else entity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
			if (entity.isBurning()) {
				if (entity instanceof ServerPlayerEntity) ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)entity, new ItemStack(ModItems.FIZZY_DRINK));
				entity.extinguish();
			}
		}
		return super.onItemUseFinish(stack, world, entity);
	}

	@Override
	public int getUseDuration(ItemStack item) {
		return 16;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		player.setActiveHand(hand);
        return ActionResult.resultConsume(player.getHeldItem(hand));
	}
}