package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;

public class SaltDirtLite extends Block implements ISaltDirt {

	public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);

	public SaltDirtLite(Properties properties) {
		super(properties);
	}

	public SaltDirtLite() {
		this(Properties.create(Material.EARTH)
				.sound(SoundType.GROUND)
				.hardnessAndResistance(0.5F, 1F)
				.harvestTool(ToolType.SHOVEL)
				.harvestLevel(0)
				.tickRandomly());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(VARIANT);
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		BlockPos posUp = pos.up();
		if (world.getBlockState(posUp).getMaterial() == Material.SNOW) {
			world.setBlockState(posUp, Blocks.AIR.getDefaultState());
		} else if (rand.nextInt(5) == 0 && world.getLight(posUp) > 7) {
			int j = state.get(VARIANT).getMetadata();
			if (j > 2) {
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						BlockPos pos2 = pos.add(x, 0, z);
						if ((world.getBlockState(pos2).getBlock() == Blocks.GRASS || world.getBlockState(pos2).getBlock() == ModBlocks.SALT_GRASS.get())
								&& !world.getBlockState(pos2.up()).isSolidSide(world, pos2.up(), Direction.DOWN)) {
							world.setBlockState(pos, ModBlocks.SALT_GRASS.get().getDefaultState().with(VARIANT, SaltGrass.EnumType.byMetadata(j)));
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack heldItem = player.inventory.getCurrentItem();
		if (this.canIncrease(state) && !heldItem.isEmpty() && heldItem.getItem() == ModItems.SALT_PINCH) {
			if (player instanceof ServerPlayerEntity && world.getBlockState(pos.up()).getBlock() == ModBlocks.SALTWORT.get()) {
				ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)player, new ItemStack(ModItems.SALTWORT_SEED));
			}
			if (!world.isRemote && this.increaseSalt(state, (ServerWorld) world, pos) && !player.abilities.isCreativeMode) heldItem.shrink(1);
			return ActionResultType.SUCCESS;
		}
		if (player.isCreative() && !heldItem.isEmpty() && heldItem.getItem() == ModItems.SALT) {
			int i = state.get(VARIANT).getMetadata();
			if (hit.getFace().getIndex() <= 1) {
				if (i == 0) i = 3;
				else if (i < 3 || i > 5) i = 0;
				else i += 1;}
			if (hit.getFace() == Direction.NORTH) {
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
			if (hit.getFace() == Direction.EAST) {
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
			if (hit.getFace() == Direction.SOUTH) {
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
			if (hit.getFace() == Direction.WEST) {
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
			world.setBlockState(pos, this.getDefaultState().with(VARIANT, EnumType.byMetadata(i)));
			return ActionResultType.SUCCESS;
		}
        return ActionResultType.PASS;
    }

	@Override
	public boolean canIncrease(BlockState state) {
		return true;
	}

	@Override
	public boolean canReduce(BlockState state) {
		int i = state.get(VARIANT).getMetadata();
		return i == 1 || i == 2;
	}

	@Override
	public boolean increaseSalt(BlockState state, ServerWorld world, BlockPos pos) {
		int i = state.get(VARIANT).getMetadata();
		if (i == 0 || i > 2) return world.setBlockState(pos, this.getDefaultState().with(VARIANT, EnumType.MEDIUM));
		else if (i == 1) return world.setBlockState(pos, this.getDefaultState().with(VARIANT, EnumType.FULL));
		else if (i == 2) return world.setBlockState(pos, ModBlocks.SALT_DIRT.get().getDefaultState());
		return false;
	}

	@Override
	public boolean ruduceSalt(BlockState state, ServerWorld world, BlockPos pos) {
		int i = state.get(VARIANT).getMetadata();
		if (i == 2) return world.setBlockState(pos, this.getDefaultState().with(VARIANT, EnumType.MEDIUM));
		else if (i == 1) return world.setBlockState(pos, this.getDefaultState().with(VARIANT, EnumType.EMPTY));
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