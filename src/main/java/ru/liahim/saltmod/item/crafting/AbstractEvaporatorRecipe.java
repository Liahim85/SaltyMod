package ru.liahim.saltmod.item.crafting;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ru.liahim.saltmod.inventory.container.ITankInventory;

public abstract class AbstractEvaporatorRecipe implements IRecipe<ITankInventory> {

	protected final IRecipeType<?> type;
	protected final ResourceLocation id;
	protected final String group;
	protected final IngredientFluid ingredient;
	protected final Fluid fluid;
	protected final ItemStack result;
	protected final float experience;
	protected final int evaporatingTime;

	public AbstractEvaporatorRecipe(IRecipeType<?> type, ResourceLocation id, String group, IngredientFluid fluid, ItemStack result, float experience) {
		this.type = type;
		this.id = id;
		this.group = group;
		this.ingredient = fluid;
		this.fluid = fluid.getFluid().getFluid();
		this.result = result;
		this.experience = experience;
		this.evaporatingTime = fluid.getFluid().getAmount();
	}

	@Override
	public boolean matches(ITankInventory inv, World world) {
		return this.ingredient.test(inv.getFluidStackInTank(0));
	}

	@Override
	public ItemStack getCraftingResult(ITankInventory inv) {
		return this.result.copy();
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.result;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	public Fluid getFluid() {
		return this.fluid;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	public float getExperience() {
		return this.experience;
	}

	public int getEvaporatingTime() {
		return this.evaporatingTime;
	}

	@Override
	public IRecipeType<?> getType() {
		return this.type;
	}
}