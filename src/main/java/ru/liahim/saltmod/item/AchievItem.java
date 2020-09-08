package ru.liahim.saltmod.item;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AchievItem extends Item {

	public AchievItem() {
		super(new Item.Properties());
		this.addPropertyOverride(new ResourceLocation("type"), new IItemPropertyGetter() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
				return stack.hasTag() ? stack.getTag().getInt("SaltAchievType") : 0;
			}
		});
	}
}