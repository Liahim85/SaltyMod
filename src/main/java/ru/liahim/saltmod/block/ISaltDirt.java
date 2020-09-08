package ru.liahim.saltmod.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public interface ISaltDirt {

	public boolean canIncrease(BlockState state);
	public boolean canReduce(BlockState state);
	public boolean increaseSalt(BlockState state, ServerWorld world, BlockPos pos);
	public boolean ruduceSalt(BlockState state, ServerWorld world, BlockPos pos);
}