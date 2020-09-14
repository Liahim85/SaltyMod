package ru.liahim.saltmod.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;
import ru.liahim.saltmod.init.SaltConfig;

public class SaltWort extends BushBlock implements IGrowable, INonItem {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_5;
	protected static final VoxelShape[] SHAPE = new VoxelShape[] {
			Block.makeCuboidShape(6, 0, 6, 10,  6, 10),
			Block.makeCuboidShape(6, 0, 6, 10,  8, 10),
			Block.makeCuboidShape(5, 0, 5, 11, 10, 11),
			Block.makeCuboidShape(4, 0, 4, 12, 12, 12),
			Block.makeCuboidShape(3, 0, 3, 13, 14, 13),
			Block.makeCuboidShape(5, 0, 5, 10,  7, 10)
	};

	public SaltWort() {
		super(Properties.create(Material.PLANTS)
				.sound(SoundType.PLANT)
				.doesNotBlockMovement()
				.hardnessAndResistance(0)
				.tickRandomly());
		this.setDefaultState(this.stateContainer.getBaseState().with(AGE, 0));
		((FireBlock)Blocks.FIRE).setFireInfo(this, 60, 100);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return SHAPE[state.get(AGE)];
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
		Block block = state.getBlock();
		return block instanceof ISaltDirt || block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		BlockPos soilPos = pos.down();
		BlockState soilState = world.getBlockState(soilPos);
		Block soil = soilState.getBlock();
		int age = state.get(AGE);
	
		if (rand.nextInt(SaltConfig.Game.saltWortGrowSpeed.get()) == 0) {
			if (age == 0) world.setBlockState(pos, this.getDefaultState().with(AGE, 1));
			else if (soil instanceof ISaltDirt && ((ISaltDirt)soil).canReduce(soilState)) {
				if (age < 5 && (age == 0 || world.getLight(pos) >= 12)) {
					if (age < 4) {
						world.setBlockState(pos, this.getDefaultState().with(AGE, age + 1));
						((ISaltDirt)soil).ruduceSalt(soilState, world, soilPos);
					}
					BlockState soilState2 = world.getBlockState(soilPos);
					Block soil2 = soilState.getBlock();
					if (((ISaltDirt)soil2).canReduce(soilState2)) {
						if (BlockPos.getAllInBox(pos.add(-2, 0, -2), pos.add(2, 0, 2))
								.filter(checkPos -> world.getBlockState(checkPos).getBlock() == ModBlocks.SALTWORT.get())
								.count() < 7) {

							BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1))
							.forEach(checkPos -> {
								if (rand.nextInt(8) == 0 && world.isAirBlock(checkPos)) {
									BlockState checkState = world.getBlockState(checkPos.down());
									if (checkState.getBlock() instanceof ISaltDirt && ((ISaltDirt)checkState.getBlock()).canReduce(checkState)) {
										world.setBlockState(checkPos, this.getDefaultState());
										((ISaltDirt)checkState.getBlock()).ruduceSalt(checkState, world, checkPos.down());
										if (age == 4) ((ISaltDirt)soil2).ruduceSalt(soilState, world, soilPos);
									}
								}
							});
						}
					}
				}
			} else if (age == 1) world.setBlockState(pos, this.getDefaultState().with(AGE, 5));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack heldItem = player.inventory.getCurrentItem();
		if (heldItem.getItem() == Items.SHEARS) {
			if (!world.isRemote && state.get(AGE) == 4 && !heldItem.isEmpty()) {
				world.setBlockState(pos, this.getDefaultState().with(AGE, 2));
	
				int i = world.rand.nextInt(3) + 1;				
				int fort = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
				if (fort > 0) i = world.rand.nextInt(5 - fort) + fort;
	
				ItemStack item = new ItemStack(ModItems.SALTWORT_SEED.get(), i);
				ItemEntity entity_item = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, item);
				entity_item.setDefaultPickupDelay();
				world.addEntity(entity_item);
				world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.2F);

				if (!player.isCreative()) heldItem.damageItem(1, player, player1 -> player1.sendBreakAnimation(EquipmentSlotType.MAINHAND));
				return ActionResultType.SUCCESS;
			}
		} else if (heldItem.getItem() == ModItems.SALT_PINCH.get()) {
			BlockState soil = world.getBlockState(pos.down());
			if (soil.getBlock() instanceof ISaltDirt) return soil.getBlock().onBlockActivated(soil, world, pos.down(), player, hand, hit);
		}
        return ActionResultType.FAIL;
    }

	public boolean fertilize(ServerWorld world, BlockPos pos) {
		boolean check = false;
		Iterator<BlockPos> iterator = BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1)).iterator();
		while (iterator.hasNext()) {
			BlockPos checkPos = iterator.next();
			BlockState state = world.getBlockState(checkPos);
			if (state.getBlock() == this) {
				int age = state.get(AGE);
				if (age < 4) {
					BlockState soilState = world.getBlockState(checkPos.down());
					Block soil = soilState.getBlock();
					if (age == 0) {
						if (!checkPos.equals(pos)) world.playEvent(2005, checkPos, 0);
						check = world.setBlockState(checkPos, state.with(AGE, 1));
					} else if (soil instanceof ISaltDirt && ((ISaltDirt)soil).canReduce(soilState)) {
						if (!checkPos.equals(pos)) world.playEvent(2005, checkPos, 0);
						((ISaltDirt)soil).ruduceSalt(soilState, world, checkPos.down());
						check = world.setBlockState(checkPos, state.with(AGE, age + 1));
					}
				}
			}
		};
		return check;
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
		return world instanceof ServerWorld && this.fertilize((ServerWorld) world, pos);
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, BlockState state) {
		return false;
	}

	@Override
	public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state) {}
}