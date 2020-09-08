package ru.liahim.saltmod.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class SaltLamp extends SaltBlock {

	public SaltLamp() {
		super(Properties.create(Material.ROCK, MaterialColor.QUARTZ).lightValue(13));
	}

	@Override
	public boolean isCrystalBaze(BlockState state) {
		return false;
	}
}