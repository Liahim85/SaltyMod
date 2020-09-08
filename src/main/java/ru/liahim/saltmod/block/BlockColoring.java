package ru.liahim.saltmod.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.ILightReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlockColoring {

	public static final IBlockColor GRASS_COLORING = new IBlockColor() {
		@Override
		public int getColor(BlockState state, ILightReader world, BlockPos pos, int tintIndex) {
			return world != null && pos != null ? tintIndex == 1 ? BiomeColors.getGrassColor(world, pos)
				: 0xFFFFFFFF : GrassColors.get(0.5D, 1.0D);
		}
	};

	public static final IItemColor BLOCK_ITEM_COLORING = new IItemColor() {
		@Override
		public int getColor(ItemStack stack, int tintIndex) {
			BlockState state = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
			IBlockColor blockColor = ((IColored)state.getBlock()).getBlockColor();
			return blockColor == null ? 0xFFFFFF : blockColor.getColor(state, null, null, tintIndex);
		}
	};
}