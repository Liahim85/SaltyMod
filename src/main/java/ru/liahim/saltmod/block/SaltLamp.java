package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SaltLamp extends Block {

	public SaltLamp() {
		super(Material.ROCK, MapColor.QUARTZ);
		this.setTickRandomly(true);
		this.setHardness(5F);
		this.setResistance(10F);
		this.setLightLevel(0.9F);
		this.setHarvestLevel("pickaxe", 1);
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		this.saltDamage(worldIn, entityIn);
		super.onEntityWalk(worldIn, pos, entityIn);
	}

	private void saltDamage(World worldIn, Entity entity) {
		if (entity instanceof EntityLivingBase
				&& EntityList.getEntityString(entity) != null
				&& ((EntityList.getEntityString(entity).toLowerCase().contains("slime") && !EntityList
						.getEntityString(entity).toLowerCase().contains("lava")) || EntityList.getEntityString(entity)
						.toLowerCase().contains("witch"))) {
			entity.attackEntityFrom(DamageSource.CACTUS, 1.0F);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!world.isRemote) {
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			
			for (int x2 = x - 1; x2 < x + 2; x2++) {
			for (int y2 = y - 1; y2 < y + 2; y2++) {
			for (int z2 = z - 1; z2 < z + 2; z2++) {
				BlockPos pos2 = new BlockPos(x2, y2, z2);
				Block block = world.getBlockState(pos2).getBlock();
				if ((block == Blocks.ICE || block == Blocks.SNOW || (block == Blocks.SNOW_LAYER && y2 != y-1)) &&
					((x2-1 == x && (world.getBlockState(pos2.west()).getBlock() == this || world.getBlockState(pos2.west()).getMaterial() == Material.WATER)) ||
					(x2+1 == x && (world.getBlockState(pos2.east()).getBlock() == this || world.getBlockState(pos2.east()).getMaterial() == Material.WATER)) ||
					(y2-1 == y && (world.getBlockState(pos2.down()).getBlock() == this || world.getBlockState(pos2.down()).getMaterial() == Material.WATER)) ||
					(y2+1 == y && (world.getBlockState(pos2.up()).getBlock() == this || world.getBlockState(pos2.up()).getMaterial() == Material.WATER)) ||
					(z2-1 == z && (world.getBlockState(pos2.north()).getBlock() == this || world.getBlockState(pos2.north()).getMaterial() == Material.WATER)) ||
					(z2+1 == z && (world.getBlockState(pos2.south()).getBlock() == this || world.getBlockState(pos2.south()).getMaterial() == Material.WATER))))
				
				{world.scheduleUpdate(pos, this, 5);
				
					if (rand.nextInt(20) == 0) {
						if(block == Blocks.ICE || block == Blocks.SNOW)
						{world.setBlockState(pos2, Blocks.WATER.getDefaultState(), 3);}
						if(block == Blocks.SNOW_LAYER && y2 != y-1)
						{world.setBlockToAir(pos2);}
					}
				}
			}
			}
			}
		}
	}
}