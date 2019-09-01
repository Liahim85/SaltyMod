package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.liahim.saltmod.api.item.SaltItems;

public class SaltOre extends Block {

	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", SaltOre.EnumType.class);

	public SaltOre() {
		super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, SaltOre.EnumType.CLEAR));
		this.setHardness(3F);
		this.setResistance(10F);
		this.setHarvestLevel("pickaxe", 1);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, SaltOre.EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((SaltOre.EnumType) state.getValue(VARIANT)).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {
		return SaltItems.SALT;
	}

	@Override
	public int quantityDropped(Random random) {
		return 1 + random.nextInt(3);
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		if (fortune > 0) {
			int j = random.nextInt(fortune + 1);
			if (j > 2) return j = 2;
			return quantityDropped(random) + j;
		}
		return quantityDropped(random);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (player.isCreative() && side.getIndex() > 1) {
			ItemStack heldItem = player.inventory.getCurrentItem();
			if (!heldItem.isEmpty() && heldItem.getItem() == SaltItems.SALT) {
				int i = state.getBlock().getMetaFromState(state);
				if (side == EnumFacing.NORTH) {if (i % 2 < 1) i += 1; else i -= 1;}
				if (side == EnumFacing.EAST) {if (i % 4 < 2) i += 2; else i -= 2;}
				if (side == EnumFacing.SOUTH) {if (i % 8 < 4) i += 4; else i -= 4;}
				if (side == EnumFacing.WEST) {if (i < 8) i += 8; else i -= 8;}
				world.setBlockState(pos, this.getStateFromMeta(i), 3);
				return true;
			}
		}
		return false;
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		return 1;
	}

	public enum EnumType implements IStringSerializable {

		CLEAR(0, "clear"),
		SIDE_N(1, "side_n"),
		SIDE_E(2, "side_e"),
		SIDE_NE(3, "side_ne"),
		SIDE_S(4, "side_s"),
		SIDE_NS(5, "side_ns"),		
		SIDE_ES(6, "side_es"),
		SIDE_NES(7, "side_nes"),
		SIDE_W(8, "side_w"),
		SIDE_WN(9, "side_wn"),
		SIDE_WE(10, "side_we"),
		SIDE_WNE(11, "side_wne"),
		SIDE_SW(12, "side_sw"),
		SIDE_SWN(13, "side_swn"),
		SIDE_ESW(14, "side_esw"),
		SIDE_NESW(15, "side_nesw");

		private static final SaltOre.EnumType[] META_LOOKUP = new SaltOre.EnumType[values().length];
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

		public static SaltOre.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) meta = 0;
			return META_LOOKUP[meta];
		}

		static {
			for (SaltOre.EnumType type : values()) {
				META_LOOKUP[type.getMetadata()] = type;
			}
		}
	}
}