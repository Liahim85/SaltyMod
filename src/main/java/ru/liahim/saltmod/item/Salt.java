package ru.liahim.saltmod.item;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;

public class Salt extends Item {

	public Salt() {}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		if (target instanceof EntityCow) processInteract((EntityAnimal)target, player, stack);
		else if (target instanceof EntityHorse) {
			EntityHorse horse = (EntityHorse)target;
			boolean flag = false;
			if (horse.getHealth() < horse.getMaxHealth()) {
				horse.heal(2.0F);
				flag = true;
			} else if (horse.isChild()) {
				Random rand = new Random();
				horse.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, horse.posX + rand.nextFloat() * horse.width * 2.0F - horse.width, horse.posY + 0.5D + rand.nextFloat() * horse.height, horse.posZ + rand.nextFloat() * horse.width * 2.0F - horse.width, 0.0D, 0.0D, 0.0D, new int[0]);
				if (!horse.world.isRemote) horse.addGrowth(10);
				flag = true;
			}
			if (flag) {
				if (!horse.isSilent()) {
					horse.world.playSound(null, horse.posX, horse.posY, horse.posZ, SoundEvents.ENTITY_HORSE_EAT,
							horse.getSoundCategory(), 1.0F, 1.0F + (new Random().nextFloat() - new Random().nextFloat()) * 0.2F);
				}
				stack.shrink(1);
				return true;
			}
		}
		return false;
	}

	private boolean processInteract(EntityAnimal entity, EntityPlayer player, ItemStack stack) {
		if (!stack.isEmpty()) {
			if (entity.getGrowingAge() == 0 && !entity.isInLove()) {
				consumeItemFromStack(player, stack);
				entity.setInLove(player);
				return true;
			}
			if (entity.isChild()) {
				consumeItemFromStack(player, stack);
				entity.ageUp((int) (-entity.getGrowingAge() / 20 * 0.1F), true);
				return true;
			}
		}
		return false;
	}

	private void consumeItemFromStack(EntityPlayer player, ItemStack stack) {
		if (!player.isCreative()) {
			stack.shrink(1);
		}
	}
}