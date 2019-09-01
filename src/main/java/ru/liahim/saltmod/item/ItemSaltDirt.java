package ru.liahim.saltmod.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSaltDirt extends ItemBlock {

	public ItemSaltDirt(Block block) {
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {

		String nameBlock = "";
		switch (stack.getItemDamage()) {
		case 0:
			nameBlock = "salt_dirt";
			break;
		case 1:
			nameBlock = "salt_dirt_lake";
			break;
		}
		return "tile." + nameBlock;
	}
}