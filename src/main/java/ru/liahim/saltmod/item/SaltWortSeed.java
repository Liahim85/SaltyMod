package ru.liahim.saltmod.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModFoods;

public class SaltwortSeed extends SaltFood {

	public SaltwortSeed() {
		super(ModFoods.SALTWORT);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		ItemStack stack = context.getItem();
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState state = world.getBlockState(pos);
		PlayerEntity player = context.getPlayer();
		if (context.getFace() == Direction.UP && world.isAirBlock(pos.up()) && ModBlocks.SALTWORT.get().canSustainPlant(state, world, pos, Direction.UP, (IPlantable) ModBlocks.SALTWORT.get())) {
			world.setBlockState(pos.up(), ModBlocks.SALTWORT.get().getDefaultState());
			world.playSound(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F, true);
			if (!player.abilities.isCreativeMode) stack.shrink(1);
			return ActionResultType.SUCCESS;
		} else if (state.getBlock() == Blocks.FLOWER_POT) {
			world.setBlockState(pos, ModBlocks.POTTED_SALTWORT.get().getDefaultState());
			player.addStat(Stats.POT_FLOWER);
            if (!player.abilities.isCreativeMode) stack.shrink(1);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}
}