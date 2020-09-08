package ru.liahim.saltmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import ru.liahim.saltmod.init.ModBlocks;

public class SaltDirt extends Block implements ISaltDirt {

	public SaltDirt() {
		super(Properties.create(Material.EARTH)
				.hardnessAndResistance(0.5F, 1.0F)
				.sound(SoundType.GROUND)
				.harvestTool(ToolType.SHOVEL)
				.harvestLevel(0));
	}

	@Override
	public boolean canIncrease(BlockState state) {
		return false;
	}

	@Override
	public boolean canReduce(BlockState state) {
		return true;
	}

	@Override
	public boolean increaseSalt(BlockState state, ServerWorld world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean ruduceSalt(BlockState state, ServerWorld world, BlockPos pos) {
		return world.setBlockState(pos, ModBlocks.SALT_DIRT_LITE.get().getDefaultState().with(SaltDirtLite.VARIANT, SaltDirtLite.EnumType.FULL));
	}
}