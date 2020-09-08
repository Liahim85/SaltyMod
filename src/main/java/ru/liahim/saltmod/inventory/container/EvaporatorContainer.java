package ru.liahim.saltmod.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.liahim.saltmod.init.ModContainers;
import ru.liahim.saltmod.tileentity.EvaporatorTileEntity;

public class EvaporatorContainer extends RecipeBookContainer<IInventory> {

	private final IInventory evaporatorInv;
	private final IIntArray evaporatorData;
	private final World world;

	public EvaporatorContainer(int id, PlayerInventory playerInv) {
		this(id, playerInv, new Inventory(2), new IntArray(10));
	}

	public EvaporatorContainer(int id, PlayerInventory playerInv, IInventory evaporatorInv, IIntArray evaporatorData) {
		super(ModContainers.EVAPORATOR.get(), id);
		assertInventorySize(evaporatorInv, 2);
		assertIntArraySize(evaporatorData, 10);
		this.evaporatorInv = evaporatorInv;
		this.evaporatorData = evaporatorData;
		this.world = playerInv.player.world;
		this.addSlot(new SlotExtractor(playerInv.player, evaporatorInv, 0, 116, 33));
		this.addSlot(new SlotExtractorFuel(evaporatorInv, 1, 44, 53));
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
		}
		this.trackIntArray(evaporatorData);
	}

	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return this.evaporatorInv.isUsableByPlayer(player);
	}

	@Override
	public void fillStackedContents(RecipeItemHelper itemHelper) {
		if (this.evaporatorInv instanceof IRecipeHelperPopulator) {
			((IRecipeHelperPopulator) this.evaporatorInv).fillStackedContents(itemHelper);
		}
	}

	@Override
	public void clear() {
		this.evaporatorInv.clear();
	}

	@Override
	public boolean matches(IRecipe<? super IInventory> recipe) {
		return recipe.matches(this.evaporatorInv, this.world);
	}

	@Override
	public int getOutputSlot() {
		return 1;
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	@Override
	public int getSize() {
		return 2;
	}

	@OnlyIn(Dist.CLIENT)
	public int getExtractingProgressScaled(int scale) {
		int i = this.evaporatorData.get(3);
		if (i == 0) i = 1000;
		return this.evaporatorData.get(2) * scale / i;
	}

	@OnlyIn(Dist.CLIENT)
	public int getBurningProgressScaled(int scale) {
		int i = this.evaporatorData.get(1);
		if (i == 0) i = 200;
		return this.evaporatorData.get(0) * scale / i;
	}

	@OnlyIn(Dist.CLIENT)
	public int getFluidAmountScaled(int scale) {
		return MathHelper.ceil((float)this.getFluidAmount() * scale / EvaporatorTileEntity.maxCap);
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isBurning() {
		return this.evaporatorData.get(0) > 0;
	}

	@OnlyIn(Dist.CLIENT)
	public int getFluidId() {
		return this.evaporatorData.get(4);
	}

	@OnlyIn(Dist.CLIENT)
	public int getFluidAmount() {
		return this.evaporatorData.get(5);
	}

	@OnlyIn(Dist.CLIENT)
	public int getPressure() {
		return this.evaporatorData.get(6);
	}

	@OnlyIn(Dist.CLIENT)
	public int getPosX() {
		return this.evaporatorData.get(7);
	}

	@OnlyIn(Dist.CLIENT)
	public int getPosY() {
		return this.evaporatorData.get(8);
	}

	@OnlyIn(Dist.CLIENT)
	public int getPosZ() {
		return this.evaporatorData.get(9);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
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
				if (AbstractFurnaceTileEntity.isFuel(itemstack1)) {
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
			} else
				slot.onSlotChanged();

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}
}