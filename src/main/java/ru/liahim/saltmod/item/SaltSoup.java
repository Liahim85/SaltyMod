package ru.liahim.saltmod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class SaltSoup extends SaltFood {

	public SaltSoup(Properties properties) {
		super(properties.containerItem(Items.BOWL));
	}

	public SaltSoup(Food food) {
		this(new Item.Properties().food(food));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
		return entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.isCreativeMode
				? super.onItemUseFinish(stack, world, entity)
				: this.getContainerItem(stack);
	}
}