package ru.liahim.saltmod.block;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IColored {

	@OnlyIn(Dist.CLIENT)
	public IBlockColor getBlockColor();

	@OnlyIn(Dist.CLIENT)
	public IItemColor getItemColor();
}