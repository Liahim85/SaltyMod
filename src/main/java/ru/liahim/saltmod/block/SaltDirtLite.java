package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.init.ModAdvancements;

public class SaltDirtLite extends Block {

	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", SaltDirtLite.EnumType.class);

	public SaltDirtLite() {
		super(Material.GROUND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, SaltDirtLite.EnumType.EMPTY));
		this.setTickRandomly(true);
		this.setSoundType(SoundType.GROUND);
		this.setHardness(0.5F);
		this.setResistance(1F);
		this.setHarvestLevel("shovel", 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, SaltDirtLite.EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((SaltDirtLite.EnumType) state.getValue(VARIANT)).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!world.isRemote) {
			if (world.getBlockState(pos.up()).getMaterial() == Material.SNOW) {
				world.setBlockToAir(pos.up());
			} else if (!world.getBlockState(pos.up()).isFullCube() && world.getLight(pos.up()) > 7) {
				int j = world.getBlockState(pos).getBlock().getMetaFromState(state);
				if (j > 2) {
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();

					for (int x1 = x - 1; x1 < x + 2; x1++) {
						for (int z1 = z - 1; z1 < z + 2; z1++) {

							BlockPos pos2 = new BlockPos(x1, y, z1);

							if ((world.getBlockState(pos2).getBlock() == Blocks.GRASS || world.getBlockState(pos2) == SaltBlocks.SALT_GRASS)
									&& world.getBlockState(pos).getBlock() == this
									&& world.getLightFromNeighbors(pos.up()) > 7 && rand.nextInt(5) == 0) {
								world.setBlockState(pos, SaltBlocks.SALT_GRASS.getDefaultState().withProperty(SaltGrass.VARIANT, SaltGrass.EnumType.byMetadata(j)), 3);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.inventory.getCurrentItem();
		if (!heldItem.isEmpty() && heldItem.getItem() == SaltItems.SALT_PINCH) {
			if (player instanceof EntityPlayerMP && world.getBlockState(pos.up()).getBlock() == SaltBlocks.SALTWORT) {
				ModAdvancements.SALT_COMMON.trigger((EntityPlayerMP)player, new ItemStack(SaltItems.SALTWORT_SEED));
			}
			int meta = world.getBlockState(pos).getBlock().getMetaFromState(state);
			if (meta == 0 || meta > 2) {
				world.setBlockState(pos, this.getStateFromMeta(1), 3);
				if (!player.isCreative()) heldItem.setCount(heldItem.getCount() - 1);
			} else if (meta == 1) {
				world.setBlockState(pos, this.getStateFromMeta(2), 3);
				if (!player.isCreative()) heldItem.setCount(heldItem.getCount() - 1);
			} else if (meta == 2) {
				world.setBlockState(pos, SaltBlocks.SALT_DIRT.getDefaultState(), 3);
				if (!player.isCreative()) heldItem.setCount(heldItem.getCount() - 1);
			}
			return true;
		}

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

	public enum EnumType implements IStringSerializable {

		EMPTY(0, "empty"),
		MEDIUM(1, "medium"),
		FULL(2, "full"),
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

        private static final SaltDirtLite.EnumType[] META_LOOKUP = new SaltDirtLite.EnumType [values().length];
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

		public static SaltDirtLite.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) meta = 0;
			return META_LOOKUP[meta];
		}

		static {
			for (SaltDirtLite.EnumType type : values()) {
				META_LOOKUP[type.getMetadata()] = type;
			}
		}
	}
}