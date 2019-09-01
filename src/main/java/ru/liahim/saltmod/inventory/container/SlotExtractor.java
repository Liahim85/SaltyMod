package ru.liahim.saltmod.inventory.container;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.api.registry.ExtractRegistry;
import ru.liahim.saltmod.init.ModAdvancements;

public class SlotExtractor extends Slot {

	private EntityPlayer thePlayer;
	private int count;

	public SlotExtractor(EntityPlayer player, IInventory inv, int x, int y, int z) {
		super(inv, x, y, z);
		this.thePlayer = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) {
			this.count += Math.min(amount, this.getStack().getCount());
		}
		return super.decrStackSize(amount);
	}

	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack stack) {
		this.onCrafting(stack);
		return super.onTake(player, stack);
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount) {
		this.count += amount;
		this.onCrafting(stack);
	}

	@Override
	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.thePlayer.world, this.thePlayer, this.count);
		if (!this.thePlayer.world.isRemote) {
			int i = this.count;
			float f = ExtractRegistry.instance().getExtractExperience(stack);
			int j;

			if (f == 0.0F) i = 0;
			else if (f < 1.0F) {
				j = MathHelper.floor(i * f);
				if (j < MathHelper.ceil(i * f) && (float) Math.random() < i * f - j) ++j;
				i = j;
			}

			while (i > 0) {
				j = EntityXPOrb.getXPSplit(i);
				i -= j;
				this.thePlayer.world.spawnEntity(new EntityXPOrb(this.thePlayer.world, this.thePlayer.posX,
						this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, j));
			}
		}
		this.count = 0;

		if (stack.getItem() == SaltItems.SALT_PINCH && this.thePlayer instanceof EntityPlayerMP) {
			ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)this.thePlayer, new ItemStack(Items.WATER_BUCKET));
		}
	}
}