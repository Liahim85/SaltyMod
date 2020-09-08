package ru.liahim.saltmod.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.fluids.FluidStack;

public interface ITankInventory extends IInventory {

	FluidStack getFluidStackInTank(int index);
}