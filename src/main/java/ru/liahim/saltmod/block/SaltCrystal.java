package ru.liahim.saltmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;

public class SaltCrystal extends BushBlock {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_2;
	protected static final VoxelShape[] SHAPE = new VoxelShape[] {
			Block.makeCuboidShape(3, 0, 3, 13, 10, 13),
			Block.makeCuboidShape(3, 0, 3, 13,  6, 13),
			Block.makeCuboidShape(3, 0, 3, 13,  2, 13)
	};

	public SaltCrystal() {
		super(Properties.create(Material.GLASS, MaterialColor.AIR)
				.sound(SoundType.GLASS)
				.doesNotBlockMovement());
		this.setDefaultState(this.stateContainer.getBaseState().with(AGE, 0));
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
		return state.isSolidSide(world, pos, Direction.UP);
	}

	@Override
	public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (player instanceof ServerPlayerEntity) {
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0) {
				if (world.getBlockState(pos.down()).getBlock() == ModBlocks.SALT_ORE.get()) crystalFind(world, pos, (ServerPlayerEntity) player);
			} else if (state.get(AGE) == 0 && world.getBlockState(pos.down()).getBlock() instanceof ICrystalBase) {
				ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)player, new ItemStack(ModItems.SALT_PINCH.get()));
			}
		}
	}

	protected void crystalFind(World world, BlockPos pos, ServerPlayerEntity player) {
		if (world.dimension.getType() == DimensionType.OVERWORLD && pos.getY() < 40) {
			ModAdvancements.SALT_COMMON.trigger(player, new ItemStack(ModBlocks.SALT_CRYSTAL.get()));
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isRemote) {
			if (SaltBlock.saltDamage(world, entity, 30)) {
				world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.5F, 1.8F);
				world.destroyBlock(pos, true);
			} else if (entity instanceof ServerPlayerEntity && world.getBlockState(pos.down()).getBlock() == ModBlocks.SALT_ORE.get()) {
				crystalFind(world, pos, (ServerPlayerEntity)entity);
			}
		}
	}
}