package ru.liahim.saltmod.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.init.SaltConfig;

public class SaltOreGenerator implements IWorldGenerator {

	private final WorldGenMinable gen = new WorldGenMinable(SaltBlocks.SALT_ORE.getDefaultState(), SaltConfig.saltOreSize);

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (world.provider.getDimension() == 0) {
			generateOverworld(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	public void generateOverworld(World world, Random rand, int chunkX, int chunkZ) {
		for (int i = 0; i < SaltConfig.saltOreFrequency; i++) {
			int randPosX = chunkX + rand.nextInt(16);
			int randPosY = rand.nextInt(96);
			int randPosZ = chunkZ + rand.nextInt(16);
			gen.generate(world, rand, new BlockPos(randPosX, randPosY, randPosZ));
		}
	}
}