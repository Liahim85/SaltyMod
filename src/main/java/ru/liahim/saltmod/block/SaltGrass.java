package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.liahim.saltmod.api.block.BlockColoring;
import ru.liahim.saltmod.api.block.IColoredBlock;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.api.item.SaltItems;

public class SaltGrass extends Block implements IColoredBlock {

	@Override
	@SideOnly(Side.CLIENT)
	public IBlockColor getBlockColor() {
		return BlockColoring.GRASS_COLORING;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IItemColor getItemColor() {
		return BlockColoring.BLOCK_ITEM_COLORING;
	}

	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", SaltGrass.EnumType.class);

	public SaltGrass() {
		super(Material.GRASS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, SaltGrass.EnumType.EMPTY));
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
		this.setHardness(0.5F);
		this.setResistance(1F);
		this.setHarvestLevel("shovel", 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, SaltGrass.EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((SaltGrass.EnumType) state.getValue(VARIANT)).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!world.isRemote) {
			if (world.getBlockState(pos.up()).getMaterial() == Material.SNOW) {
				world.setBlockToAir(pos.up());
			}
			if (world.getLightFromNeighbors(pos.up()) < 4 && world.getBlockLightOpacity(pos.up()) > 2) {
				int j = world.getBlockState(pos).getBlock().getMetaFromState(state);
				world.setBlockState(pos, SaltBlocks.SALT_DIRT_LITE.getDefaultState().withProperty(SaltDirtLite.VARIANT, SaltDirtLite.EnumType.byMetadata(j)), 3);
			} else if (world.getLightFromNeighbors(pos.up()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos pos2 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
					IBlockState state2 = world.getBlockState(pos2);

					if (state2.getBlock() == Blocks.DIRT
							&& state2.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT
							&& world.getLightFromNeighbors(pos2.up()) >= 4
							&& world.getBlockLightOpacity(pos2.up()) <= 2) {
						world.setBlockState(pos2, Blocks.GRASS.getDefaultState());
					}
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.inventory.getCurrentItem();
		if (player.isCreative()) {
			if (!heldItem.isEmpty() && heldItem.getItem() == SaltItems.SALT) {
				int i = state.getBlock().getMetaFromState(state);
				if (side.getIndex() <= 1) {
					if (i == 0) i = 3;
					else if (i < 3 || i > 5) i = 0;
					else i += 1;}
				if (side == EnumFacing.NORTH) {
					if (i == 4) i = 11;
					else if (i == 5) i = 14;
					else if (i < 7) i = 7;
					else if (i == 7) i = 0;
					else if (i == 8) i = 11;
					else if (i == 9) i = 15;
					else if (i == 10) i = 14;
					else if (i == 11) i = 8;
					else if (i == 14) i = 10;
					else if (i < 15) i = 15;
					else i = 9;}
				if (side == EnumFacing.EAST) {
					if (i == 5) i = 12;
					else if (i == 6) i = 11;
					else if (i < 7) i = 8;
					else if (i == 7) i = 11;
					else if (i == 8) i = 0;
					else if (i == 9) i = 12;
					else if (i == 10) i = 15;
					else if (i == 11) i = 7;
					else if (i == 12) i = 9;
					else if (i < 15) i = 15;
					else i = 10;}
				if (side == EnumFacing.SOUTH) {
					if (i == 3) i = 12;
					else if (i == 6) i = 13;
					else if (i < 7) i = 9;
					else if (i == 7) i = 15;
					else if (i == 8) i = 12;
					else if (i == 9) i = 0;
					else if (i == 10) i = 13;
					else if (i == 12) i = 8;
					else if (i == 13) i = 10;
					else if (i < 15) i = 15;
					else i = 7;}
				if (side == EnumFacing.WEST) {
					if (i == 3) i = 14;
					else if (i == 4) i = 13;
					else if (i < 7) i = 10;
					else if (i == 7) i = 14;
					else if (i == 8) i = 15;
					else if (i == 9) i = 13;
					else if (i == 10) i = 0;
					else if (i == 13) i = 9;
					else if (i == 14) i = 7;
					else if (i < 15) i = 15;
					else i = 8;}
				world.setBlockState(pos, this.getStateFromMeta(i), 3);
				return true;
			}
		}
        return false;
    }

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {
		return SaltBlocks.SALT_DIRT_LITE.getItemDropped(SaltBlocks.SALT_DIRT_LITE.getDefaultState(), random, fortune);
	}

	public enum EnumType implements IStringSerializable {

		EMPTY(0, "empty"),
		EMPTY_1(1, "empty_1"),
		EMPTY_2(2, "empty_2"),
		SIDE_CNE(3, "side_cne"),
		SIDE_CES(4, "side_ces"),
		SIDE_CSW(5, "side_csw"),
		SIDE_CWN(6, "side_cwn"),
		SIDE_N(7, "side_n"),
		SIDE_E(8, "side_e"),
		SIDE_S(9, "side_s"),
		SIDE_W(10, "side_w"),
		SIDE_NE(11, "side_ne"),
		SIDE_ES(12, "side_es"),
		SIDE_SW(13, "side_sw"),
		SIDE_WN(14, "side_wn"),
		SIDE_NESW(15, "side_nesw");

		private static final SaltGrass.EnumType[] META_LOOKUP = new SaltGrass.EnumType[values().length];
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

		public static SaltGrass.EnumType byMetadata(int meta) {
			if (meta < 0 || meta == 1 || meta == 2 || meta >= META_LOOKUP.length) meta = 0;
			return META_LOOKUP[meta];
		}

		static {
			for (SaltGrass.EnumType type : values()) {
				META_LOOKUP[type.getMetadata()] = type;
			}
		}
	}
}