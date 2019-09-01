package ru.liahim.saltmod.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.liahim.saltmod.entity.EntityRainmaker;

public class DispenserBehaviorRainmaiker implements IBehaviorDispenseItem {

	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) {
		World world = source.getWorld();
		EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
        IPosition iposition = BlockDispenser.getDispensePosition(source);
        double d0 = source.getX() + enumfacing.getFrontOffsetX();
        double d1 = source.getBlockPos().getY() + 0.2F;
        double d2 = source.getZ() + enumfacing.getFrontOffsetZ();
        BlockPos posFace = new BlockPos(iposition.getX(), iposition.getY(), iposition.getZ());

        if (world.isAirBlock(posFace)) {
        	EntityRainmaker entityRainmaker = new EntityRainmaker(source.getWorld(), d0, d1, d2, null);
        	source.getWorld().spawnEntity(entityRainmaker);
        	source.getWorld().playEvent(2000, source.getBlockPos(), enumfacing.getFrontOffsetX() + 1 + (enumfacing.getFrontOffsetZ() + 1) * 3);
        	source.getWorld().playEvent(1004, source.getBlockPos(), 0);
        	stack.splitStack(1);
		} else {
			source.getWorld().playEvent(1001, source.getBlockPos(), 0);
		}
        return stack;
    }
}