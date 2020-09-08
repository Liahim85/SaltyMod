package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class SaltSlab extends SlabBlock implements ICrystalBase, IMeltingBlock {

	public SaltSlab() {
		super(Properties.create(Material.ROCK, MaterialColor.QUARTZ)
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
	public boolean isCrystalBaze(BlockState state) {
		return state.get(TYPE) == SlabType.DOUBLE;
	}

	@Override
	public Block getBlock() {
		return this;
	}

	@Override
	public int getMinY(BlockState state) {
		return state.get(TYPE) == SlabType.TOP ? 0 : -1;
	}

	@Override
	public int getMaxY(BlockState state) {
		return state.get(TYPE) == SlabType.BOTTOM ? 0 : 1;
	}
}