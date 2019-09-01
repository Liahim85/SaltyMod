package ru.liahim.saltmod.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.liahim.saltmod.api.block.SaltBlocks;

public class SaltTab extends CreativeTabs {

	public SaltTab(String lable) {
		super(lable);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		return new ItemStack(SaltBlocks.SALT_ORE);
	}
}