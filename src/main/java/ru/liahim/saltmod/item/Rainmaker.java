package ru.liahim.saltmod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.liahim.saltmod.entity.RainmakerEntity;

public class Rainmaker extends SaltItem {

	public static CompoundNBT tag = new CompoundNBT();

	public Rainmaker() {
		CompoundNBT tag1 = new CompoundNBT();
		ListNBT nbtlist = new ListNBT();
		tag1.putIntArray("Colors", new int[] { 2651799, 4312372 });
		tag1.putIntArray("FadeColors", new int[] { 15790320 });
		tag1.putBoolean("Trail", true);
		tag1.putByte("Type", (byte) 1);
		nbtlist.add(tag1);
		Rainmaker.tag.put("Explosions", nbtlist);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		tooltip.add(new TranslationTextComponent(this.getDefaultTranslationKey() + ".tooltip"));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		if (!world.isRemote) {
			Vec3d vec3d = context.getHitVec();
			Direction dir = context.getFace();
			RainmakerEntity roket = new RainmakerEntity(world,
					vec3d.x + dir.getXOffset() * 0.15D,
					vec3d.y + dir.getYOffset() * 0.15D,
					vec3d.z + dir.getZOffset() * 0.15D,
					context.getPlayer());
			world.addEntity(roket);
			context.getItem().shrink(1);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (player.isElytraFlying()) {
			if (!world.isRemote) {
				world.addEntity(new RainmakerEntity(world, player));
				if (!player.abilities.isCreativeMode) player.getHeldItem(hand).shrink(1);
			}
			return ActionResult.resultSuccess(player.getHeldItem(hand));
		} else return ActionResult.resultPass(player.getHeldItem(hand));
	}
}