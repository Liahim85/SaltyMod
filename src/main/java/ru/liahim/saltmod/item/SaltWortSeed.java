package ru.liahim.saltmod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.block.SaltPot;
import ru.liahim.saltmod.common.CommonProxy;
import ru.liahim.saltmod.network.SaltWortMessage;

public class SaltWortSeed extends ItemFood {
	
	public SaltWortSeed() {
		super(1, 0.2F, false);
		this.setPotionEffect(new PotionEffect(MobEffects.REGENERATION, 40, 1), 0.8F);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		
		PotionEffect ptn_efct = new PotionEffect(MobEffects.REGENERATION, 40, 1);

	    String mess = "";

	    mess += (ptn_efct.getPotion().isBadEffect() ? TextFormatting.RED : TextFormatting.GRAY);
	    mess += I18n.format(ptn_efct.getEffectName()).trim();

	    if (ptn_efct.getAmplifier() == 1){mess += " II";}
	    else if (ptn_efct.getAmplifier() == 2){mess += " III";}
	    else if (ptn_efct.getAmplifier() == 3){mess += " IV";}
	    else if (ptn_efct.getAmplifier() == 4){mess += " V";}

	    if (ptn_efct.getDuration() > 20)
	        mess += " (" + Potion.getPotionDurationString(ptn_efct, 1) + ")";
	    
	    mess += TextFormatting.RESET;

	    tooltip.add(mess);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if (SaltBlocks.SALTWORT.canPlaceBlockAt(world, pos.up()) && side.getIndex() == 1 && world.isAirBlock(pos.up())) {
			world.setBlockState(pos.up(), SaltBlocks.SALTWORT.getDefaultState(), 3);
			world.playSound(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F, true);
			stack.setCount(stack.getCount() - 1);
			return EnumActionResult.SUCCESS;
		} else if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityFlowerPot &&
				(world.getBlockState(pos).getBlock() == Blocks.FLOWER_POT || world.getBlockState(pos).getBlock() == SaltBlocks.SALT_POT)) {
				TileEntityFlowerPot te = (TileEntityFlowerPot) world.getTileEntity(pos);
			if (te.getFlowerItemStack().isEmpty()) {
				if (!world.isRemote) {
					int i = world.rand.nextInt(2);
					world.setBlockState(pos, SaltBlocks.SALT_POT.getDefaultState().withProperty(SaltPot.CONTENTS, SaltPot.EnumFlowerType.byMetadata(i + 1)), 3);
					((TileEntityFlowerPot)world.getTileEntity(pos)).setItemStack(new ItemStack(SaltItems.SALTWORT_SEED, 1, i));
					((TileEntityFlowerPot)world.getTileEntity(pos)).markDirty();
					CommonProxy.network.sendToAllAround(new SaltWortMessage(pos.getX(), pos.getY(), pos.getZ(), i), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 256));
				}
				stack.setCount(stack.getCount() - 1);
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.FAIL;
	}
}