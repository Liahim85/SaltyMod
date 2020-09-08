package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.network.NetworkHooks;
import ru.liahim.saltmod.tileentity.EvaporatorTileEntity;

public class Evaporator extends ContainerBlock {

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final EnumProperty<EnumType> LIT = EnumProperty.create("lit", EnumType.class);

	public Evaporator() {
		super(Block.Properties.create(Material.ROCK)
				.hardnessAndResistance(5, 10)
				.lightValue(13));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(LIT, EnumType.NORMAL));
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getLightValue(BlockState state) {
		return state.get(LIT) == EnumType.NORMAL ? 0 : super.getLightValue(state);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new EvaporatorTileEntity();
	}

	public static void setState(boolean active, boolean extract, World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (active) {
			if (extract) world.setBlockState(pos, state.with(LIT, EnumType.STEAM));
			else world.setBlockState(pos, state.with(LIT, EnumType.LIT));
		} else world.setBlockState(pos, state.with(LIT, EnumType.NORMAL));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (state.get(LIT) != EnumType.NORMAL) {
			double d0 = pos.getX() + 0.5D;
			double d1 = pos.getY();
			double d2 = pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D) world.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			Direction direction = state.get(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = rand.nextDouble() * 0.6D - 0.3D;
			double d4 = direction$axis == Direction.Axis.X ? direction.getXOffset() * 0.52D : d3;
			double d5 = rand.nextDouble() * 6.0D / 16.0D;
			double d6 = direction$axis == Direction.Axis.Z ? direction.getZOffset() * 0.52D : d3;
			world.addParticle(ParticleTypes.SMOKE, d0 + d4, d1 + d5, d2 + d6, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.FLAME, d0 + d4, d1 + d5, d2 + d6, 0.0D, 0.0D, 0.0D);
			if (state.get(LIT) == EnumType.STEAM) {
				BlockPos up = pos.up();
				if (!world.getBlockState(up).isSolidSide(world, up, Direction.DOWN) && world.getFluidState(up).isEmpty()) {
					double d7 = d0 + rand.nextDouble() * 0.4F - 0.2F;
					double d8 = d2 + rand.nextDouble() * 0.4F - 0.2F;
					world.addParticle(ParticleTypes.CLOUD, d7, d1 + 1.1D, d8, 0.0D, 0.1D, 0.0D);
					up = pos.up(2);
					if (rand.nextInt(10) == 0 && world.getBlockState(up).isSolidSide(world, up, Direction.DOWN)) {
						double d9 = d0 + rand.nextFloat() - 0.5D;
						double d10 = d2 + rand.nextFloat() - 0.5D;
						world.addParticle(ParticleTypes.DRIPPING_WATER, d9, d1 + 1.95D, d10, 0.0D, 0.0D, 0.0D);
					}
				}
			}
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote) return ActionResultType.SUCCESS;
		else {
			this.interactWith(world, pos, (ServerPlayerEntity) player, hand);
			return ActionResultType.SUCCESS;
		}
	}

	protected void interactWith(World world, BlockPos pos, ServerPlayerEntity player, Hand hand) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof EvaporatorTileEntity) {
			ItemStack stack = player.getHeldItem(hand);
			if (!fillTank(world, pos, (EvaporatorTileEntity)te, stack, player)) {
				if (!drainTank(world, pos, (EvaporatorTileEntity)te, stack, player)) {
					NetworkHooks.openGui(player, (EvaporatorTileEntity)te, pos);
				}
			}
		}
	}

	public static boolean fillTank(World world, BlockPos pos, EvaporatorTileEntity tank, ItemStack stack, ServerPlayerEntity player) {
		if (!stack.isEmpty()) {
			FluidStack fStack;
			IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack).orElse(null);
			if (fluidHandler != null) {
				boolean isBucket = fluidHandler instanceof FluidBucketWrapper;
				fStack = fluidHandler.drain(isBucket ? FluidAttributes.BUCKET_VOLUME : tank.getTankCapacity(0) - tank.getFluidAmount(), FluidAction.SIMULATE);
				if (fStack != null) {
					int used = tank.fill(fStack, FluidAction.EXECUTE);
					if (used > 0) {
						if (!player.isCreative()) {
							fluidHandler.drain(isBucket ? FluidAttributes.BUCKET_VOLUME : fStack.getAmount(), FluidAction.EXECUTE);
							playerInvChange(world, pos, stack, player, fluidHandler.getContainer());
						}
						if (used >= FluidAttributes.BUCKET_VOLUME) {
							if (fStack.getFluid() == Fluids.LAVA) world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
							else world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
						return true;
					}
				}
			} else if (stack.getItem() == Items.POTION) {
				fStack = new FluidStack(Fluids.WATER, 333);
				int used = tank.fill(fStack, FluidAction.EXECUTE);
				if (used > 0) {
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
					if (!player.isCreative()) {
						playerInvChange(world, pos, stack, player, new ItemStack(Items.GLASS_BOTTLE));
					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean drainTank(World world, BlockPos pos, EvaporatorTileEntity tank, ItemStack stack, ServerPlayerEntity player) {
		if (!stack.isEmpty()) {
			FluidStack available = tank.drain(tank.getTankCapacity(0), FluidAction.SIMULATE);
			if (available != null) {
				IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack).orElse(null);
				if (fluidHandler != null) {
					int count = fluidHandler.fill(available, FluidAction.SIMULATE);
					if (count > 0) {
						if (!player.isCreative()) {
							fluidHandler.fill(available, FluidAction.EXECUTE);
							playerInvChange(world, pos, stack, player, fluidHandler.getContainer());
						}
						if (available.getFluid() == Fluids.LAVA) world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
						else  world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
						tank.drain(count, FluidAction.EXECUTE);
						return true;
					}
				} else if (available.getFluid() == Fluids.WATER && stack.getItem() == Items.GLASS_BOTTLE && available.getAmount() >= 333) {
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
					if (!player.isCreative()) {
						playerInvChange(world, pos, stack, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER));
					}
					tank.drain(333, FluidAction.EXECUTE);
					return true;
				} 
			}
		}
		return false;
	}

	private static void playerInvChange(World world, BlockPos pos, ItemStack held, ServerPlayerEntity player, ItemStack stack) {
		held.shrink(1);
		if (held.getCount() <= 0) {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
		}
		if (!player.inventory.getCurrentItem().isEmpty()) {
			if (!player.inventory.addItemStackToInventory(stack)) {
				world.addEntity(new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, stack));
			} else player.sendContainerToPlayer(player.container);
		} else {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
			player.sendContainerToPlayer(player.container);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof EvaporatorTileEntity) {
				((EvaporatorTileEntity)te).setCustomName(stack.getDisplayName());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof EvaporatorTileEntity) {
				InventoryHelper.dropInventoryItems(world, pos, (EvaporatorTileEntity) te);
				world.updateComparatorOutputLevel(pos, this);
			}
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		return te instanceof EvaporatorTileEntity ? ((EvaporatorTileEntity)te).getFluidAmountScaled(15) : 0;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
	}

	public enum EnumType implements IStringSerializable {

		NORMAL("normal"),
		LIT("lit"),
		STEAM("steam");

	    private final String name;

		private EnumType(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}