package ru.liahim.saltmod.tileentity;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import ru.liahim.saltmod.block.Evaporator;
import ru.liahim.saltmod.init.ModRecipeTypes;
import ru.liahim.saltmod.init.ModTileEntities;
import ru.liahim.saltmod.init.SaltConfig;
import ru.liahim.saltmod.inventory.container.EvaporatorContainer;
import ru.liahim.saltmod.inventory.container.ITankInventory;
import ru.liahim.saltmod.item.crafting.AbstractEvaporatorRecipe;

public class EvaporatorTileEntity extends LockableTileEntity implements ITankInventory, ISidedInventory, ITickableTileEntity, IFluidHandler {

	protected final IRecipeType<AbstractEvaporatorRecipe> recipeType = ModRecipeTypes.EVAPORATING;
	private final Map<ResourceLocation, Integer> recipesInUse = Maps.newHashMap();
	private static final int[] slotsBottom = new int[] { 0, 1 };
	private static final int[] slotsSides = new int[] { 1 };
	protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
	private int burningTime;
	private int burningTimeTotal;
	private int extractTime;
	private int cookTimeTotal;
	private int liquidID;
	private int liquidLevel;

	// red stone
	private int liquidChange;
	private int redSS;

	// steam
	private int steamLevel;
	private int steamTime;
	private int pressure;

	// tank
	public static final int maxCap = FluidAttributes.BUCKET_VOLUME * SaltConfig.Game.extractorVolume.get();
	protected FluidTank tank = new FluidTank(maxCap);
	private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

	@SuppressWarnings("deprecation")
	@Override
	public void tick() {
		if (!this.world.isRemote) {
			boolean burn = this.isBurning();
			boolean teUpdate = false;
			BlockPos posUp = this.pos.up();
			boolean clear = !this.world.getBlockState(posUp).isSolidSide(this.world, posUp, Direction.DOWN);
			IFluidState fluidState = this.world.getFluidState(posUp);
			boolean isFluidUp = !fluidState.isEmpty();
			AbstractEvaporatorRecipe recipe = null;
			if (isFluidUp) {
				Fluid fluid = fluidState.getFluid();
				if (fluid != null && !fluid.getAttributes().isGaseous() && fluid.isSource(fluidState)) {
					int den = fluid.getAttributes().getViscosity() / 200;
					if (this.liquidLevel == 0 || (EvaporatorTileEntity.maxCap - this.liquidLevel >= den && Registry.FLUID.getId(fluid) == this.liquidID)) {
						this.world.setBlockState(posUp, Blocks.AIR.getDefaultState());
						this.fill(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
						isFluidUp = false;
					}
				}
			}
			if (this.liquidLevel > 0 && liquidChange == 0) {
				liquidChange = this.liquidLevel;
				this.cookTimeTotal = this.getCookTime();
				teUpdate = true;
				if (recipe == null) recipe = this.getRecipe();
				if (this.canExtract(recipe)) Evaporator.setState(this.isBurning(), true, this.world, this.pos);
			}
			if (this.liquidLevel == 0 && liquidChange > 0) {
				liquidChange = 0;
				this.cookTimeTotal = 0;
				extractTime = 0;
				teUpdate = true;
				Evaporator.setState(this.isBurning(), false, this.world, this.pos);
			}
			if (this.isBurning()) --this.burningTime;

			if (liquidChange != this.liquidLevel && redSS != this.getFluidAmountScaled(15)) {
				liquidChange = this.liquidLevel;
				redSS = this.getFluidAmountScaled(15);
				teUpdate = true;
			}

			this.liquidID = this.tank.getFluid() != null ? Registry.FLUID.getId(this.tank.getFluid().getFluid()) : 0;
			this.liquidLevel = this.getFluidAmount();

			ItemStack fuel = this.items.get(1);
			if (this.isBurning() || !fuel.isEmpty() && this.getFluidAmount() > 0) {
				if (recipe == null) recipe =  this.getRecipe();
				if (!this.isBurning() && this.canExtract(recipe) && !isFluidUp) {
					this.burningTime = this.getBurnTime(fuel);
					this.burningTimeTotal = this.burningTime;
					if (this.isBurning()) {
						teUpdate = true;
						if (fuel.hasContainerItem()) this.items.set(1, fuel.getContainerItem());
						else if (!fuel.isEmpty()) {
							fuel.shrink(1);
							if (fuel.isEmpty()) {
								this.items.set(1, fuel.getItem().getContainerItem(fuel));
							}
						}
					}
				}

				if (this.isBurning() && this.canExtract(recipe)) {
					if (clear && !isFluidUp) {
						++this.extractTime;
						if (this.extractTime == this.cookTimeTotal) {
							this.extractTime = 0;
							this.extract(recipe);
							teUpdate = true;
						}
						this.tank.drain(1, FluidAction.EXECUTE);
					} else if (isFluidUp) {
					} else pressure(recipe);
				} else this.extractTime = 0;
			} else if (!this.isBurning() && this.extractTime > 0) {
				this.extractTime = MathHelper.clamp(this.extractTime - Math.max(this.cookTimeTotal / 100, 1), 0, this.cookTimeTotal);
			}

			if (burn != this.isBurning()) {
				teUpdate = true;
				if (recipe == null) recipe =  this.getRecipe();
				Evaporator.setState(this.isBurning(), this.canExtract(recipe), this.world, this.pos);
			}

			if ((this.steamLevel != 0 && clear) || (this.liquidLevel == 0 && !clear) || !this.isBurning()) {
				this.pressure = 0;
				this.steamLevel = 0;
				this.steamTime = 0;
			}

			if (teUpdate) this.markDirty();
		}
	}

	protected int getBurnTime(ItemStack fuel) {
		return ForgeHooks.getBurnTime(fuel);
	}

	public boolean isBurning() {
		return this.burningTime > 0;
	}

	protected int getCookTime() {
		return this.world.getRecipeManager()
				.getRecipe(this.recipeType, this, this.world)
				.map(AbstractEvaporatorRecipe::getEvaporatingTime).orElse(1000);
	}

	protected AbstractEvaporatorRecipe getRecipe() {
		return this.world.getRecipeManager().getRecipe(this.recipeType, this, this.world).orElse(null);
	}

	public void pressure(AbstractEvaporatorRecipe recipe) {
		this.pressure = this.steamLevel / ((32 - getFluidAmountScaled(32) + 1) * 4);
		++this.steamTime;
		if (this.steamTime % (pressure + 1) == 0) {
			++this.extractTime;
			this.steamTime = 0;
			if (this.extractTime == this.cookTimeTotal) {
				this.extractTime = 0;
				this.extract(recipe);
				this.markDirty();
			}
			this.drain(1, FluidAction.EXECUTE);
		}
		++this.steamLevel;
		if (pressure >= 16) {
			this.world.setBlockState(this.pos, Blocks.AIR.getDefaultState());
			this.world.createExplosion(null, this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, 2.5F, Explosion.Mode.BREAK);
		}
	}

	public boolean canExtract(AbstractEvaporatorRecipe recipe) {
		if (this.getFluidAmount() == 0 || recipe == null) return false;
		else {
			ItemStack output = recipe.getRecipeOutput();
			if (output.isEmpty()) return false;
			if (this.items.get(0).isEmpty()) return true;
			if (!this.items.get(0).isItemEqual(output)) return false;
			int result = items.get(0).getCount() + output.getCount();
			return result <= getInventoryStackLimit() && result <= this.items.get(0).getMaxStackSize();
		}
	}

	public void extract(AbstractEvaporatorRecipe recipe) {
		if (this.canExtract(recipe)) {
			ItemStack output = recipe.getRecipeOutput();
			if (this.items.get(0).isEmpty()) this.items.set(0, output.copy());
			else if (this.items.get(0).isItemEqual(output)) this.items.get(0).grow(output.getCount());
		}
	}

	public EvaporatorTileEntity() {
		super(ModTileEntities.EVAPORATOR.get());
	}

	public void spawnExpOrbs(PlayerEntity player, int count) {
		float experience = this.getExpOrbs();
		if (experience == 0) count = 0;
		else if (experience < 1.0F) {
			int i = MathHelper.floor(count * experience);
			if (i < MathHelper.ceil(count * experience)
					&& Math.random() < count * experience - i) {
				++i;
			}
			count = i;
		}
		while (count > 0) {
			int j = ExperienceOrbEntity.getXPSplit(count);
			count -= j;
			player.world.addEntity(new ExperienceOrbEntity(player.world, player.getPosX(), player.getPosY() + 0.5D, player.getPosZ() + 0.5D, j));
		}
	}

	protected float getExpOrbs() {
		return this.world.getRecipeManager()
				.getRecipe(this.recipeType, this, this.world)
				.map(AbstractEvaporatorRecipe::getExperience).orElse(0F);
	}

	@Override
	public int getSizeInventory() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.items) if (!stack.isEmpty()) return false;
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.items.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) return false;
		else return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	@Override
	public FluidStack getFluidStackInTank(int index) {
		return this.getFluidInTank(index);
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return side == Direction.DOWN ? slotsBottom : (side != Direction.UP ? slotsSides : new int[0]);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStack, Direction direction) {
		return this.isItemValidForSlot(index, itemStack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return direction != Direction.DOWN || index != 1 || stack.getItem() == Items.BUCKET;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 1 && AbstractFurnaceTileEntity.isFuel(stack);
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.saltmod.evaporator");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new EvaporatorContainer(id, player, this, this.furnaceData);
	}

	//////////// NBT ////////////

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);
		this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(tag, this.items);
		this.burningTime = tag.getInt("BurnTime");
		this.burningTimeTotal = tag.getInt("BurnTimeTotal");
		this.extractTime = tag.getInt("CookTime");
		this.cookTimeTotal = tag.getInt("CookTimeTotal");
		this.steamLevel = tag.getInt("SteamLevel");
		tank.readFromNBT(tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		super.write(tag);
		tag.putInt("BurnTime", this.burningTime);
		tag.putInt("BurnTimeTotal", this.burningTimeTotal);
		tag.putInt("CookTime", this.extractTime);
		tag.putInt("CookTimeTotal", this.cookTimeTotal);
		tag.putInt("SteamLevel", this.steamLevel);
		tank.writeToNBT(tag);
		ItemStackHelper.saveAllItems(tag, this.items);
		tag.putShort("RecipesUsedSize", (short) this.recipesInUse.size());
		int i = 0;
		for (Entry<ResourceLocation, Integer> entry : this.recipesInUse.entrySet()) {
			tag.putString("RecipeLocation" + i, entry.getKey().toString());
			tag.putInt("RecipeAmount" + i, entry.getValue());
			++i;
		}
		return tag;
	}

	//////////// TANK ////////////

	public int getFluidAmountScaled(int scale) {
		return MathHelper.ceil((float) this.tank.getFluidAmount() * scale / EvaporatorTileEntity.maxCap);
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		return this.tank.getFluid();
	}

	public int getFluidAmount() {
		return this.tank.getFluidAmount();
	}

	@Override
	public int getTankCapacity(int tank) {
		return EvaporatorTileEntity.maxCap;
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return this.tank.isFluidValid(tank, stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return this.tank.fill(resource, action);
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		return this.tank.drain(resource, action);
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		return this.tank.drain(maxDrain, action);
	}

	LazyOptional<?> handlerTop = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.UP));
	LazyOptional<?> handlerBottom = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.DOWN));
	LazyOptional<?> handlerSide = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.NORTH));

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return holder.cast();
		else if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == Direction.DOWN) return handlerBottom.cast();
            else if (facing == Direction.UP) return handlerTop.cast();
            else return handlerSide.cast();
		}
		return super.getCapability(capability, facing);
	}

	public final IIntArray furnaceData = new IIntArray() {
		@Override
		public int get(int index) {
			switch (index) {
			case 0:
				return EvaporatorTileEntity.this.burningTime;
			case 1:
				return EvaporatorTileEntity.this.burningTimeTotal;
			case 2:
				return EvaporatorTileEntity.this.extractTime;
			case 3:
				return EvaporatorTileEntity.this.cookTimeTotal;
			case 4:
				return EvaporatorTileEntity.this.liquidID;
			case 5:
				return EvaporatorTileEntity.this.liquidLevel;
			case 6:
				return EvaporatorTileEntity.this.pressure;
			case 7:
				return EvaporatorTileEntity.this.pos.getX();
			case 8:
				return EvaporatorTileEntity.this.pos.getY();
			case 9:
				return EvaporatorTileEntity.this.pos.getZ();
			default:
				return 0;
			}
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 0:
				EvaporatorTileEntity.this.burningTime = value;
				break;
			case 1:
				EvaporatorTileEntity.this.burningTimeTotal = value;
				break;
			case 2:
				EvaporatorTileEntity.this.extractTime = value;
				break;
			case 3:
				EvaporatorTileEntity.this.cookTimeTotal = value;
				break;
			case 4:
				EvaporatorTileEntity.this.liquidID = value;
				break;
			case 5:
				EvaporatorTileEntity.this.liquidLevel = value;
				break;
			case 6:
				EvaporatorTileEntity.this.pressure = value;
			}
		}

		@Override
		public int size() {
			return 10;
		}
	};
}