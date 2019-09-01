package ru.liahim.saltmod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.liahim.saltmod.entity.EntityRainmaker;

public class Rainmaker extends Item {

	public static NBTTagCompound tag = new NBTTagCompound();
	private static NBTTagCompound tag1 = new NBTTagCompound();
	private static NBTTagList nbtlist = new NBTTagList();

	public Rainmaker() {
		Rainmaker.tag1.setIntArray("Colors", new int[] { 2651799, 4312372 });
		Rainmaker.tag1.setIntArray("FadeColors", new int[] { 15790320 });
		Rainmaker.tag1.setBoolean("Trail", true);
		Rainmaker.tag1.setByte("Type", (byte) 1);
		Rainmaker.nbtlist.appendTag(tag1);
		Rainmaker.tag.setTag("Explosions", nbtlist);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			EntityRainmaker entityRainmaker = new EntityRainmaker(world, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, player);
			world.spawnEntity(entityRainmaker);

			if (!player.isCreative()) {
				ItemStack stack = player.getHeldItem(hand);
				stack.shrink(1);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}