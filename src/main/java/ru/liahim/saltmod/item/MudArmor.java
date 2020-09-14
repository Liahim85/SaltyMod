package ru.liahim.saltmod.item;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.World;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.ModArmorMaterials;
import ru.liahim.saltmod.init.ModItemGroups;
import ru.liahim.saltmod.init.ModItems;
import ru.liahim.saltmod.init.SaltConfig;

public class MudArmor extends ArmorItem {

	public MudArmor(EquipmentSlotType slot) {
		super(ModArmorMaterials.MUD_MATERIAL, slot, new Properties().group(ModItemGroups.MOD_ITEM_GROUP));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		if (slot == this.getEquipmentSlot().getIndex() && !world.isRemote && !stack.isEmpty()
				&& SaltConfig.Game.mudArmorWaterDam.get() && entity instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) entity;
			Random rand = living.getRNG();
			if (slot == EquipmentSlotType.HEAD.getIndex() && stack.getItem() == ModItems.MUD_HELMET.get()) {
				if (((world.isRaining() && living.isWet() && !living.areEyesInFluid(FluidTags.WATER)) || living
						.areEyesInFluid(FluidTags.WATER)) && rand.nextInt(100) == 0) {
					stack.damageItem(1, living, le -> le.sendBreakAnimation(EquipmentSlotType.HEAD));
				}
				if (stack.getDamage() >= stack.getMaxDamage()) {
					living.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
					if (living instanceof ServerPlayerEntity) ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)living, new ItemStack(ModItems.MINERAL_MUD.get()));
				}
			}

			if (slot == EquipmentSlotType.CHEST.getIndex() && stack.getItem() == ModItems.MUD_CHESTPLATE.get()) {
				if ((world.isRaining() || (living.areEyesInFluid(FluidTags.WATER) && living.isInWater()))
						&& living.isWet() && rand.nextInt(100) == 0) {
					stack.damageItem(1, living, le -> le.sendBreakAnimation(EquipmentSlotType.CHEST));
				}
				if (!world.isRaining() && !living.areEyesInFluid(FluidTags.WATER) && living.isInWater()
						&& living.isWet() && rand.nextInt(200) == 0) {
					stack.damageItem(1, living, le -> le.sendBreakAnimation(EquipmentSlotType.CHEST));
				}
				if (stack.getDamage() >= stack.getMaxDamage()) {
					living.setItemStackToSlot(EquipmentSlotType.CHEST, ItemStack.EMPTY);
					if (living instanceof ServerPlayerEntity) ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)living, new ItemStack(ModItems.MINERAL_MUD.get()));
				}
			}

			if (slot == EquipmentSlotType.LEGS.getIndex() && stack.getItem() == ModItems.MUD_LEGGINGS.get()) {
				if (living.isInWater() && living.isWet() && rand.nextInt(100) == 0) {
					stack.damageItem(1, living, le -> le.sendBreakAnimation(EquipmentSlotType.LEGS));
				}
				if (world.isRaining() && !living.isInWater() && living.isWet() && rand.nextInt(200) == 0) {
					stack.damageItem(1, living, le -> le.sendBreakAnimation(EquipmentSlotType.LEGS));
				}
				if (stack.getDamage() >= stack.getMaxDamage()) {
					living.setItemStackToSlot(EquipmentSlotType.LEGS, ItemStack.EMPTY);
					if (living instanceof ServerPlayerEntity) ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)living, new ItemStack(ModItems.MINERAL_MUD.get()));
				}
			}

			if (slot == EquipmentSlotType.FEET.getIndex() && stack.getItem() == ModItems.MUD_BOOTS.get()) {
				if (living.isInWater() && living.isWet() && rand.nextInt(100) == 0) {
					stack.damageItem(1, living, le -> le.sendBreakAnimation(EquipmentSlotType.FEET));
				}
				if (!living.isInWater() && world.isRaining() && living.isWet() && rand.nextInt(200) == 0) {
					stack.damageItem(1, living, le -> le.sendBreakAnimation(EquipmentSlotType.FEET));
				}
				if (stack.getDamage() >= stack.getMaxDamage()) {
					living.setItemStackToSlot(EquipmentSlotType.FEET, ItemStack.EMPTY);
					if (living instanceof ServerPlayerEntity) ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)living, new ItemStack(ModItems.MINERAL_MUD.get()));
				}
			}
		}
	}
}