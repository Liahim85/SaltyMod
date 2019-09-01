package ru.liahim.saltmod.item;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.SaltConfig;

public class MudArmor extends ItemArmor {

	public MudArmor(ArmorMaterial material, EntityEquipmentSlot type) {
		super(material, 0, type);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack material) {
		return material.getItem() == SaltItems.MINERAL_MUD || super.getIsRepairable(toRepair, material);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if (!world.isRemote && !stack.isEmpty() && SaltConfig.mudArmorWaterDam) {
			Random rand = new Random();

			if (stack.getItem() == SaltItems.MUD_HELMET) {
				if (((world.isRaining() && player.isWet() && !player.isInsideOfMaterial(Material.WATER)) || player
						.isInsideOfMaterial(Material.WATER)) && rand.nextInt(100) == 0) {
					stack.damageItem(1, player);
				}

				if (stack.getItemDamage() >= stack.getMaxDamage()) {
					player.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
					if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.MINERAL_MUD));
				}
			}

			if (stack.getItem() == SaltItems.MUD_CHESTPLATE) {
				if ((world.isRaining() || (player.isInsideOfMaterial(Material.WATER) && player.isInWater()))
						&& player.isWet() && rand.nextInt(100) == 0) {
					stack.damageItem(1, player);
				}

				if (!world.isRaining() && !player.isInsideOfMaterial(Material.WATER) && player.isInWater()
						&& player.isWet() && rand.nextInt(200) == 0) {
					stack.damageItem(1, player);
				}

				if (stack.getItemDamage() >= stack.getMaxDamage()) {
					player.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
					if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.MINERAL_MUD));
				}
			}

			if (stack.getItem() == SaltItems.MUD_LEGGINGS) {
				if (player.isInWater() && player.isWet() && rand.nextInt(100) == 0) {
					stack.damageItem(1, player);
				}

				if (world.isRaining() && !player.isInWater() && player.isWet() && rand.nextInt(200) == 0) {
					stack.damageItem(1, player);
				}

				if (stack.getItemDamage() >= stack.getMaxDamage()) {
					player.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
					if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.MINERAL_MUD));
				}
			}

			if (stack.getItem() == SaltItems.MUD_BOOTS) {
				if (player.isInWater() && player.isWet() && rand.nextInt(100) == 0) {
					stack.damageItem(1, player);
				}

				if (!player.isInWater() && world.isRaining() && player.isWet() && rand.nextInt(200) == 0) {
					stack.damageItem(1, player);
				}

				if (stack.getItemDamage() >= stack.getMaxDamage()) {
					player.setItemStackToSlot(EntityEquipmentSlot.FEET, ItemStack.EMPTY);
					if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.MINERAL_MUD));
				}
			}
		}
	}
}