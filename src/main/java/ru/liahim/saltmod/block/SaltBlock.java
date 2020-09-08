package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MagmaCubeEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.SaltConfig;

public class SaltBlock extends Block implements ICrystalBase, IMeltingBlock {

	public SaltBlock(Properties properties) {
		super(properties
				.hardnessAndResistance(5F, 10F)
				.harvestTool(ToolType.PICKAXE)
				.harvestLevel(1)
				.tickRandomly());
	}

	public SaltBlock() {
		this(Properties.create(Material.ROCK, MaterialColor.QUARTZ));
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		SaltBlock.saltDamage(world, entity, 1);
		super.onEntityWalk(world, pos, entity);
	}

	public static boolean saltDamage(World world, Entity entity, int damage) {
		if ((entity instanceof SlimeEntity && !(entity instanceof MagmaCubeEntity)) || entity instanceof WitchEntity) {
			return entity.attackEntityFrom(DamageSource.CACTUS, damage);
		}
		return false;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		melt(state, world, pos, rand);
		BlockPos posUp = new BlockPos(pos.up());
		if (!IMeltingBlock.meltingPoses.contains(pos) && checkSaltBlocks(world, posUp)) {
			if (!meltingPoses.contains(pos) && rand.nextInt(SaltConfig.Game.saltCrystalGrowSpeed.get()) == 0) {
				BlockState crystal = world.getBlockState(posUp);
				if (crystal.isAir(world, posUp)) world.setBlockState(posUp, ModBlocks.SALT_CRYSTAL.get().getDefaultState().with(SaltCrystal.AGE, 2));
				else if (crystal.getBlock() == ModBlocks.SALT_CRYSTAL.get()) {
					int age = crystal.get(SaltCrystal.AGE);
					if (age > 0) world.setBlockState(posUp, crystal.with(SaltCrystal.AGE, age - 1));
				}
			}
		}
	}

	private boolean checkSaltBlocks(ServerWorld world, BlockPos pos) {
		if (world.canSeeSky(pos) || world.getLight(pos) > 14) return false;
		BlockState state;
		for (Direction dir : Direction.values()) {
			if (dir != Direction.UP) {
				state = world.getBlockState(pos.offset(dir));
				if (!(state.getBlock() instanceof ICrystalBase) || !((ICrystalBase)state.getBlock()).isCrystalBaze(state))
					return false;
			}
		}
		for (Direction dir : Direction.Plane.HORIZONTAL) {
			state = world.getBlockState(pos.offset(dir).offset(dir.rotateY()));
			if (state.getMaterial() != Material.WATER) return false;
		}
		return true;
	}

	@Override
	public boolean isCrystalBaze(BlockState state) {
		return true;
	}

	@Override
	public Block getBlock() {
		return this;
	}
}