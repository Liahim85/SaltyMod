package ru.liahim.saltmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;

public class SaltPot extends FlowerPotBlock implements INonItem {

	public SaltPot() {
		super(() -> (FlowerPotBlock) Blocks.FLOWER_POT.delegate.get(), () -> ModBlocks.SALTWORT.get(),
				Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0).notSolid());
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = new ItemStack(ModItems.SALTWORT_SEED);
		if (player.getHeldItem(hand).isEmpty()) player.setHeldItem(hand, stack);
		else if (!player.addItemStackToInventory(stack)) player.dropItem(stack, false);
		world.setBlockState(pos, getEmptyPot().getDefaultState());
		return ActionResultType.SUCCESS;
	}
}