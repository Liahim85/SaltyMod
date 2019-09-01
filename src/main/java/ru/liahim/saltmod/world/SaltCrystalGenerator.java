package ru.liahim.saltmod.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.block.SaltCrystal;

public class SaltCrystalGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (world.provider.getDimension() == 0) {
			generateOverworld(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	public void generateOverworld(World world, Random rand, int chunkX, int chunkZ) {
		for (int y = 8; y < 40; y++) {
			for (int x = chunkX; x < chunkX + 16; x++) {
				for (int z = chunkZ; z < chunkZ + 16; z++) {
					BlockPos pos = new BlockPos(x, y, z);
					if (world.getBlockState(pos.down()).getBlock() == SaltBlocks.SALT_ORE && world.isAirBlock(pos)
							&& world.getLight(pos) < 13) {
						if (rand.nextInt(2) == 0) {
							world.setBlockState(pos, SaltBlocks.SALT_CRYSTAL.getDefaultState().withProperty(SaltCrystal.STAGE, SaltCrystal.EnumType.MEDIUM), 3);
						} else {
							world.setBlockState(pos, SaltBlocks.SALT_CRYSTAL.getDefaultState(), 3);
						}
					}
				}
			}
		}
	}
}