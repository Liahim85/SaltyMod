package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.tileEntity.TileEntityExtractor;

public class Extractor extends BlockContainer {
    
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	private final boolean isBurning;
	private final boolean isExtract;
	private static boolean keepInventory;

	public Extractor(boolean burn, boolean ext) {
		super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.isBurning = burn;
		this.isExtract = ext;
		if (burn) this.setLightLevel(0.9F);
		this.setHardness(5F);
		this.setResistance(10F);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(SaltBlocks.EXTRACTOR);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		this.setDefaultFacing(worldIn, pos, state);
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			IBlockState iblockstate = worldIn.getBlockState(pos.north());
			IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
			IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
			IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = state.getValue(FACING);
			if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock()) {
				enumfacing = EnumFacing.SOUTH;
			} else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock()) {
				enumfacing = EnumFacing.NORTH;
			} else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock()) {
				enumfacing = EnumFacing.EAST;
			} else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock()) {
				enumfacing = EnumFacing.WEST;
			}
			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("incomplete-switch")
	public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand) {
		if (this.isBurning) {
			EnumFacing enumfacing = state.getValue(FACING);
			double d0 = pos.getX() + 0.5D;
			double d1 = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			double d2 = pos.getZ() + 0.5D;
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			double d5 = pos.getX() + rand.nextDouble() * 0.4F + 0.3F;
			double d6 = pos.getZ() + rand.nextDouble() * 0.4F + 0.3F;
			double d7 = (double) pos.getX() + (double) rand.nextFloat();
			double d8 = (double) pos.getZ() + (double) rand.nextFloat();
			boolean clear = !worldIn.isSideSolid(pos.up(), EnumFacing.DOWN) && FluidRegistry.lookupFluidForBlock(worldIn.getBlockState(pos.up()).getBlock()) == null;
			boolean ceiling = worldIn.isSideSolid(pos.up(2), EnumFacing.DOWN);
			if (rand.nextDouble() < 0.1D) {
				worldIn.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
			switch (enumfacing) {
			case WEST:
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
				break;
			case EAST:
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
				break;
			case NORTH:
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
				break;
			case SOUTH:
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
			}
			if (isExtract && clear) {
				worldIn.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, d5, pos.getY() + 1.1D, d6, 0.0D, 0.1D, 0.0D, new int[0]);

				if (ceiling && rand.nextInt(10) == 0)
					worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d7, pos.getY() + 1.95D, d8, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		else {
			TileEntity te = world.getTileEntity(pos);
			ItemStack stack = player.getHeldItem(hand);
			if (te != null && te instanceof TileEntityExtractor) {
				if (!fillTank(world, pos, (TileEntityExtractor)te, stack, player)) {
					if (!drainTank(world, pos, (TileEntityExtractor)te, stack, player)) {
						player.openGui(SaltyMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
					}
				}
			}
			return true;
		}
	}

	public static boolean fillTank(World world, BlockPos pos, TileEntityExtractor tank, ItemStack stack, EntityPlayer player) {
		if (!stack.isEmpty()) {
			FluidStack fStack;
			IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
			if (fluidHandler != null) {
				boolean isBucket = fluidHandler instanceof FluidBucketWrapper;
				fStack = fluidHandler.drain(isBucket ? Fluid.BUCKET_VOLUME : tank.getMaxCap() - tank.getFluidAmount(), false);
				if (fStack != null) {
					int used = tank.fill(fStack, true);
					if (used > 0) {
						if (!player.isCreative()) {
							fluidHandler.drain(isBucket ? Fluid.BUCKET_VOLUME : fStack.amount, true);
							playerInvChange(world, pos, stack, player, fluidHandler.getContainer());
						}
						if (used >= Fluid.BUCKET_VOLUME) {
							if (fStack.getFluid() == FluidRegistry.LAVA) {
								world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
							} else {
								world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
							}
						}
						return true;
					}
				}
			} else if (stack.getItem() == Items.POTIONITEM) {
				fStack = new FluidStack(FluidRegistry.WATER, 333);
				int used = tank.fill(fStack, true);
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

	private boolean drainTank(World world, BlockPos pos, TileEntityExtractor tank, ItemStack stack, EntityPlayer player) {
		if (!stack.isEmpty()) {
			FluidStack available = tank.drain(tank.getMaxCap(), false);
			if (available != null) {
				IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
				if (fluidHandler != null) {
					int count = fluidHandler.fill(available, false);
					if (count > 0) {
						if (!player.isCreative()) {
							fluidHandler.fill(available, true);
							playerInvChange(world, pos, stack, player, fluidHandler.getContainer());
						}
						world.playSound(null, pos, available.getFluid().getFillSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
						tank.drain(count, true);
						return true;
					}
				} else if (available.getFluid() == FluidRegistry.WATER && stack.getItem() == Items.GLASS_BOTTLE && available.amount >= 333) {
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
					if (!player.isCreative()) {
						playerInvChange(world, pos, stack, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER));
					}
					tank.drain(333, true);
					return true;
				} 
			}
		}
		return false;
	}

	private static void playerInvChange(World world, BlockPos pos, ItemStack held, EntityPlayer player, ItemStack stack) {
		held.shrink(1);
		if (held.getCount() <= 0) {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
		}
		if (!player.inventory.getCurrentItem().isEmpty()) {
			if (!player.inventory.addItemStackToInventory(stack)) {
				world.spawnEntity(new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, stack));
			} else if (player instanceof EntityPlayerMP) {
				((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
			}
		} else {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
			if (player instanceof EntityPlayerMP) {
				((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
			}
		}
	}

	public static void setState(boolean active, boolean extract, World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;
		if (active) {
			if (extract) {
				worldIn.setBlockState(pos, SaltBlocks.EXTRACTOR_STEAM.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)),	3);
				worldIn.setBlockState(pos, SaltBlocks.EXTRACTOR_STEAM.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)),	3);
			} else {
				worldIn.setBlockState(pos, SaltBlocks.EXTRACTOR_LIT.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
				worldIn.setBlockState(pos, SaltBlocks.EXTRACTOR_LIT.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
			}
		} else {
			worldIn.setBlockState(pos, SaltBlocks.EXTRACTOR.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
			worldIn.setBlockState(pos, SaltBlocks.EXTRACTOR.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		}
		keepInventory = false;
		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityExtractor();
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		if (stack.hasDisplayName()) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof TileEntityExtractor) {
				((TileEntityExtractor) te).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof TileEntityExtractor) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityExtractor) te);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		TileEntityExtractor te = (TileEntityExtractor) worldIn.getTileEntity(pos);
		return te.getTank().getFluid() != null ? te.getFluidAmountScaled(15) : 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(SaltBlocks.EXTRACTOR);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);
		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}
		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}
}