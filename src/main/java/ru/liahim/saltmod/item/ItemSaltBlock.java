package ru.liahim.saltmod.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSaltBlock extends ItemBlock {

	public ItemSaltBlock(Block block) {
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
			nameBlock = "salt_block";
			break;
		case 1:
			nameBlock = "salt_block_chiseled";
			break;
		case 2:
			nameBlock = "salt_block_pillar";
			break;
		case 5:
			nameBlock = "salt_brick";
			break;
		case 6:
			nameBlock = "salt_block_cracked";
			break;
		case 7:
			nameBlock = "salt_brick_cracked";
			break;
		case 8:
			nameBlock = "salt_brick_chiseled";
			break;
		case 9:
			nameBlock = "salt_chapiter";
			break;
		}
		return "tile." + nameBlock;
	}
}