package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;

public class SaltSource extends Block {

	public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);
	private final boolean isOre;
	private final boolean isLake;

	public SaltSource(boolean isOre, boolean isLake) {
		super(isOre
				? isLake ? Properties.create(Material.ROCK, MaterialColor.QUARTZ)
						.hardnessAndResistance(3F, 10F)
						.harvestTool(ToolType.PICKAXE)
						.harvestLevel(1)
						.tickRandomly()
					: Properties.create(Material.ROCK)
						.hardnessAndResistance(3F, 10F)
						.harvestTool(ToolType.PICKAXE)
						.harvestLevel(1)
				: isLake ? Properties.create(Material.EARTH, MaterialColor.QUARTZ)
						.sound(SoundType.GROUND)
						.hardnessAndResistance(0.5F, 1F)
						.harvestTool(ToolType.SHOVEL)
						.harvestLevel(0)
						.tickRandomly()
					: Properties.create(Material.EARTH)
						.sound(SoundType.GROUND)
						.hardnessAndResistance(0.5F, 1F)
						.harvestTool(ToolType.SHOVEL)
						.harvestLevel(0));
		this.isOre = isOre;
		this.isLake = isLake;
		this.setDefaultState(this.stateContainer.getBaseState().with(VARIANT, EnumType.CLEAR));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(VARIANT);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if ((this.isLake || this.isOre) && (player.isCreative() && hit.getFace().getIndex() > 1)) {
			ItemStack heldItem = player.inventory.getCurrentItem();
			if (!heldItem.isEmpty() && heldItem.getItem() == ModItems.SALT) {
				int i = state.get(VARIANT).getMetadata();
				if (hit.getFace() == Direction.NORTH) {if (i % 2 < 1) i += 1; else i -= 1;}
				else if (hit.getFace() == Direction.EAST) {if (i % 4 < 2) i += 2; else i -= 2;}
				else if (hit.getFace() == Direction.SOUTH) {if (i % 8 < 4) i += 4; else i -= 4;}
				else if (hit.getFace() == Direction.WEST) {if (i < 8) i += 8; else i -= 8;}
				world.setBlockState(pos, state.with(VARIANT, EnumType.byMetadata(i)));
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		if (this.isLake) {
			SaltBlock.saltDamage(world, entity, 1);
			if (entity instanceof ServerPlayerEntity) {
				ModAdvancements.SALT_COMMON.trigger((ServerPlayerEntity)entity, new ItemStack(ModBlocks.SALT_LAKE.get()));
			}
		}
		super.onEntityWalk(world, pos, entity);
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (!world.isRemote) {
			if (world.getBlockState(pos.up()).getMaterial() == Material.SNOW) world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
			else if (world.getBlockState(pos.up()).getMaterial() == Material.SNOW_BLOCK || world.getBlockState(pos.up()).getMaterial() == Material.ICE) {
				world.setBlockState(pos.up(), Blocks.WATER.getDefaultState());
			}
		}
	}

	@Override
	public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
		return this.isOre && silktouch == 0 ? 1 : 0;
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

		private static final SaltSource.EnumType[] META_LOOKUP = new SaltSource.EnumType[values().length];
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

		public static SaltSource.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) meta = 0;
			return META_LOOKUP[meta];
		}

		static {
			for (SaltSource.EnumType type : values()) {
				META_LOOKUP[type.getMetadata()] = type;
			}
		}
	}
}