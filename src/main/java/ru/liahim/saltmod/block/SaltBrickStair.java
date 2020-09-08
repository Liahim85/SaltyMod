package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class SaltBrickStair extends StairsBlock implements IMeltingBlock {

	public SaltBrickStair(BlockState state) {
		super(() -> state, Properties.create(Material.ROCK, MaterialColor.QUARTZ)
				.hardnessAndResistance(5F, 10F)
				.harvestTool(ToolType.PICKAXE)
				.harvestLevel(1)
				.tickRandomly());
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		SaltBlock.saltDamage(world, entity, 1);
		super.onEntityWalk(world, pos, entity);
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		melt(state, world, pos, rand);
	}

	@Override
	public Block getBlock() {
		return this;
	}
}