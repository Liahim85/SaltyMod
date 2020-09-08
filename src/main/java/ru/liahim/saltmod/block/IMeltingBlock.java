package ru.liahim.saltmod.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public interface IMeltingBlock {

	public static final Set<BlockPos> meltingPoses = new HashSet<BlockPos>();

	public default void melt(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		BlockPos pos2;
		BlockPos checkPos;
		Block block;
		BlockState checkState;
		ArrayList<BlockPos> list = new ArrayList<BlockPos>();
		int min = getMinY(state);
		int max = getMinY(state);
		for (int x2 = -1; x2 <= 1; x2++) {
			for (int y2 = min; y2 <= max; y2++) {
				for (int z2 = -1; z2 <= 1; z2++) {
					if (x2 != 0 || y2 != 0 || z2 != 0) {
						pos2 = pos.add(x2, y2, z2);
						block = world.getBlockState(pos2).getBlock();
						if (block == Blocks.ICE || block == Blocks.SNOW_BLOCK || (block == Blocks.SNOW && y2 != -1)) {
							for (Direction dir : Direction.values()) {
								checkPos = pos2.offset(dir);
								if (checkPos.getX() == pos.getX() || checkPos.getY() == pos.getY() || checkPos.getZ() == pos.getZ()) {
									checkState = world.getBlockState(checkPos);
									if (checkState.getBlock() instanceof IMeltingBlock || checkState.getMaterial() == Material.WATER) {
										list.add(pos2);
									}
								}
							}
						}
					}
				}
			}
		}
		if (!list.isEmpty()) {
			pos2 = list.get(rand.nextInt(list.size()));
			checkState = world.getBlockState(pos2);
			block = checkState.getBlock();
			if (block == Blocks.ICE || block == Blocks.SNOW_BLOCK) world.setBlockState(pos2, Blocks.WATER.getDefaultState());
			else if (block == Blocks.SNOW) world.setBlockState(pos2, Blocks.WATER.getDefaultState().with(BlockStateProperties.LEVEL_0_15, checkState.get(BlockStateProperties.LEVEL_1_8) * 2 - 1));
			world.getPendingBlockTicks().scheduleTick(pos, getBlock(), 10);
			meltingPoses.add(pos);
		} else if (meltingPoses.contains(pos)) meltingPoses.remove(pos);
	}

	public Block getBlock();

	public default int getMinY(BlockState state) {
		return -1;
	}

	public default int getMaxY(BlockState state) {
		return 1;
	}
}