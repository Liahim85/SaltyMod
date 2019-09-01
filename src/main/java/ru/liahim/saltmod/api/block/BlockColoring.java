package ru.liahim.saltmod.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockColoring {

	public static final IBlockColor GRASS_COLORING = new IBlockColor() {
		@Override
		@SideOnly(Side.CLIENT)
		public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
			return world != null && pos != null ? tintIndex == 1 ? BiomeColorHelper.getGrassColorAtPos(world, pos)
				: 0xFFFFFFFF : ColorizerGrass.getGrassColor(0.5D, 1.0D);
		}
	};

	public static final IItemColor BLOCK_ITEM_COLORING = new IItemColor() {
		@Override
		public int colorMultiplier(ItemStack stack, int tintIndex) {
			IBlockState state = ((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
			IBlockColor blockColor = ((IColoredBlock)state.getBlock()).getBlockColor();
			return blockColor == null ? 0xFFFFFF : blockColor.colorMultiplier(state, null, null, tintIndex);
		}
	};
}