package ru.liahim.saltmod.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.ModItems;
import ru.liahim.saltmod.tileentity.EvaporatorTileEntity;

public class SlotExtractor extends Slot {

	private PlayerEntity thePlayer;
	private int count;

	public SlotExtractor(PlayerEntity player, IInventory inv, int x, int y, int z) {
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
	public ItemStack onTake(PlayerEntity player, ItemStack stack) {
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
		if (!this.thePlayer.world.isRemote && this.inventory instanceof EvaporatorTileEntity) {
			((EvaporatorTileEntity) this.inventory).spawnExpOrbs(this.thePlayer, this.count);
		}
		if (stack.getItem() == ModItems.SALT_PINCH && this.thePlayer instanceof ServerPlayerEntity) {
			ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)this.thePlayer, new ItemStack(Items.WATER_BUCKET));
		}
	}
}