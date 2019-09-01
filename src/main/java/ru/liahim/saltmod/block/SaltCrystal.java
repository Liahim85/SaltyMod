package ru.liahim.saltmod.block;

import java.util.ArrayList;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.init.ModAdvancements;

public class SaltCrystal extends BlockBush {
	
	public static final PropertyEnum STAGE = PropertyEnum.create("stage", SaltCrystal.EnumType.class);
	protected boolean silkdrop = false;

	public SaltCrystal() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, SaltCrystal.EnumType.BIG));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(STAGE, SaltCrystal.EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((SaltCrystal.EnumType) state.getValue(STAGE)).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { STAGE });
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
		float g = 0.1875F;
		float v = 0.375F;
		if (meta == 1) v = 0.625F;
		else if (meta == 2) v = 0.875F;
		return new AxisAlignedBB(0.0F + g, 0.0F, 0.0F + g, 1.0F - g, 1.0F - v, 1.0F - g);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return this.canBlockStay(world, pos, world.getBlockState(pos));
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		return world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP) ? true : false;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		int silk = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("silk_touch"), player.getHeldItemMainhand());
		if (!world.isRemote) {
			if (!player.getHeldItemMainhand().isEmpty() && silk > 0) silkdrop = true;
			if (world.getBlockState(pos.down()).getBlock() == SaltBlocks.SALT_ORE) {
				crystalFind(world, pos, player);
			}

			if (world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)) == 0
					&& (world.getBlockState(pos.down()).getBlock() == SaltBlocks.SALT_BLOCK || world.getBlockState(
							pos.down()).getBlock() == SaltBlocks.SALT_SLAB_DOUBLE)) {
				if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.SALT_PINCH));
			}
		}
	}

	protected void crystalFind(World world, BlockPos pos, EntityPlayer player) {
		if (world.provider.getDimension() == 0 && pos.getY() < 40) {
			if (player instanceof EntityPlayerMP) ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltBlocks.SALT_CRYSTAL));
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
		if (state.getBlock().getMetaFromState(state) == 0) {
			if (silkdrop) drop.add(new ItemStack(this));
			else drop.add(new ItemStack(SaltItems.SALT_PINCH));
		}
		silkdrop = false;
		return drop;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (!world.isRemote) {
			if (entity instanceof EntityLivingBase
					&& EntityList.getEntityString(entity) != null
					&& ((EntityList.getEntityString(entity).toLowerCase().contains("slime") && !EntityList
							.getEntityString(entity).toLowerCase().contains("lava")) || EntityList
							.getEntityString(entity).toLowerCase().contains("witch"))) {
				entity.attackEntityFrom(DamageSource.CACTUS, 30.0F);

				world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.5F, 1.8F);
				state.getBlock().dropBlockAsItem(world, pos, state, 0);
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
			}

			else if (entity instanceof EntityPlayer
					&& world.getBlockState(pos.down()).getBlock() == SaltBlocks.SALT_ORE) {
				crystalFind(world, pos, (EntityPlayer) entity);
			}
		}
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MapColor.AIR;
	}

	public enum EnumType implements IStringSerializable {

		BIG(0, "sise_b"),
		MEDIUM(1, "sise_m"),
		SMALL(2, "sise_s");

		private static final SaltCrystal.EnumType[] META_LOOKUP = new SaltCrystal.EnumType[values().length];
		private final int meta;
		private final String name;

		private EnumType(int meta, String name) {
			this.meta = meta;
			this.name = name;
		}

		public int getMetadata() {
			return this.meta;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return getName();
		}

		public static SaltCrystal.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) meta = 0;
			return META_LOOKUP[meta];
		}

		static {
			for (SaltCrystal.EnumType type : values()) {
				META_LOOKUP[type.getMetadata()] = type;
			}
		}
	}
}