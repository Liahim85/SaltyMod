package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import ru.liahim.saltmod.init.ModBlocks;

public class SaltGrass extends SaltDirtLite implements IColored {

	@Override
	public IBlockColor getBlockColor() {
		return BlockColoring.GRASS_COLORING;
	}

	@Override
	public IItemColor getItemColor() {
		return BlockColoring.BLOCK_ITEM_COLORING;
	}

	public SaltGrass() {
		super(Properties.create(Material.ORGANIC)
				.sound(SoundType.PLANT)
				.hardnessAndResistance(0.5F, 1F)
				.harvestTool(ToolType.SHOVEL)
				.harvestLevel(0)
				.tickRandomly());
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		BlockPos posUp = pos.up();
		if (world.getBlockState(posUp).getMaterial() == Material.SNOW) {
			world.setBlockState(posUp, Blocks.AIR.getDefaultState());
		}
		if (world.getLight(pos.up()) < 4) {
			int j = state.get(VARIANT).getMetadata();
			world.setBlockState(pos, ModBlocks.SALT_DIRT_LITE.get().getDefaultState().with(VARIANT, SaltDirtLite.EnumType.byMetadata(j)));
		} else if (world.getLight(pos.up()) >= 9) {
			for (int i = 0; i < 4; ++i) {
				BlockPos pos2 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
				BlockState state2 = world.getBlockState(pos2);
				if (state2.getBlock() == Blocks.DIRT && world.getLight(pos2.up()) >= 4
						&& !world.getBlockState(pos2.up()).isSolidSide(world, pos2.up(), Direction.DOWN)) {
					world.setBlockState(pos2, Blocks.GRASS.getDefaultState());
				}
			}
		}
	}

	@Override
	public boolean canIncrease(BlockState state) {
		return false;
	}

	@Override
	public boolean canReduce(BlockState state) {
		return false;
	}
}