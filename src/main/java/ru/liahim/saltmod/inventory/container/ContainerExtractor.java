package ru.liahim.saltmod.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerExtractor extends Container {

	private final IInventory tileExtractor;
	private int lastExtractTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerExtractor(InventoryPlayer inv, IInventory extractorInventory) {
		this.tileExtractor = extractorInventory;
		this.addSlotToContainer(new SlotExtractor(inv.player, extractorInventory, 0, 116, 33));
		this.addSlotToContainer(new SlotExtractorFuel(extractorInventory, 1, 44, 53));
		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileExtractor);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener iCrafting = this.listeners.get(i);

			if (this.lastExtractTime != this.tileExtractor.getField(2)) {
				(iCrafting).sendWindowProperty(this, 2, this.tileExtractor.getField(2));
			}

			if (this.lastBurnTime != this.tileExtractor.getField(0)) {
				iCrafting.sendWindowProperty(this, 0, this.tileExtractor.getField(0));
			}

			if (this.lastItemBurnTime != this.tileExtractor.getField(1)) {
				iCrafting.sendWindowProperty(this, 1, this.tileExtractor.getField(1));
			}

			iCrafting.sendWindowProperty(this, 3, this.tileExtractor.getField(3));
			iCrafting.sendWindowProperty(this, 4, this.tileExtractor.getField(4));
			iCrafting.sendWindowProperty(this, 5, this.tileExtractor.getField(5));
		}

		this.lastExtractTime = this.tileExtractor.getField(2);
		this.lastBurnTime = this.tileExtractor.getField(0);
		this.lastItemBurnTime = this.tileExtractor.getField(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.tileExtractor.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileExtractor.isUsableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0) {
				if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 1) {
				if (TileEntityFurnace.isItemFuel(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 2 && index < 29) {
					if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else slot.onSlotChanged();

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}
}