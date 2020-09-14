package ru.liahim.saltmod.item;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.ModItems;

public class Muffin extends SaltFood {

	public Muffin(Food food) {
		super(food);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		tooltip.add(new TranslationTextComponent(this.getDefaultTranslationKey() + ".tooltip"));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
		ItemStack result = super.onItemUseFinish(stack, world, entity);
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			if (player.getFoodStats().getFoodLevel() == 20) {
				if (player instanceof ServerPlayerEntity) {
					player.addPotionEffect(new EffectInstance(Effects.SATURATION, 2400, 0));
					ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)player, new ItemStack(ModItems.MUFFIN.get()));
				} else player.sendMessage(new TranslationTextComponent(this.getDefaultTranslationKey() + ".mess." + player.getRNG().nextInt(4)));
			}
		}
		return result;
	}
}