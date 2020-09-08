package ru.liahim.saltmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

public class MudBlock extends FallingBlock {

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 14, 16);

	public MudBlock() {
		super(Properties.create(Material.EARTH, MaterialColor.GRAY)
				.sound(SoundType.GROUND)
				.hardnessAndResistance(0.5F, 1F)
				.harvestTool(ToolType.SHOVEL)
				.harvestLevel(0)
				.speedFactor(0.4F));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isViewBlocking(BlockState state, IBlockReader world, BlockPos pos) {
		return true;
	}
}