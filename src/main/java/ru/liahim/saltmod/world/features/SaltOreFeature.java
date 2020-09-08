package ru.liahim.saltmod.world.features;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import ru.liahim.saltmod.block.SaltCrystal;
import ru.liahim.saltmod.init.ModBlocks;

public class SaltOreFeature extends OreFeature {

	public SaltOreFeature(Function<Dynamic<?>, ? extends OreFeatureConfig> configFactory) {
		super(configFactory);
	}

	@Override
	protected boolean func_207803_a(IWorld world, Random random, OreFeatureConfig config, double xMax, double xMin, double zMax, double zMin, double yMax, double yMin,
			int p_207803_16_, int p_207803_17_, int p_207803_18_, int p_207803_19_, int p_207803_20_) {
		int i = 0;
		BitSet bitset = new BitSet(p_207803_19_ * p_207803_20_ * p_207803_19_);
		BlockPos.Mutable pos = new BlockPos.Mutable();
		double[] adouble = new double[config.size * 4];

		for (int j = 0; j < config.size; ++j) {
			float f = (float) j / (float) config.size;
			double d0 = MathHelper.lerp(f, xMax, xMin);
			double d2 = MathHelper.lerp(f, yMax, yMin);
			double d4 = MathHelper.lerp(f, zMax, zMin);
			double d6 = random.nextDouble() * config.size / 16.0D;
			double d7 = ((MathHelper.sin((float) Math.PI * f) + 1.0F) * d6 + 1.0D) / 2.0D;
			adouble[j * 4 + 0] = d0;
			adouble[j * 4 + 1] = d2;
			adouble[j * 4 + 2] = d4;
			adouble[j * 4 + 3] = d7;
		}

		for (int l2 = 0; l2 < config.size - 1; ++l2) {
			if (!(adouble[l2 * 4 + 3] <= 0.0D)) {
				for (int j3 = l2 + 1; j3 < config.size; ++j3) {
					if (!(adouble[j3 * 4 + 3] <= 0.0D)) {
						double d12 = adouble[l2 * 4 + 0] - adouble[j3 * 4 + 0];
						double d13 = adouble[l2 * 4 + 1] - adouble[j3 * 4 + 1];
						double d14 = adouble[l2 * 4 + 2] - adouble[j3 * 4 + 2];
						double d15 = adouble[l2 * 4 + 3] - adouble[j3 * 4 + 3];
						if (d15 * d15 > d12 * d12 + d13 * d13 + d14 * d14) {
							if (d15 > 0.0D) {
								adouble[j3 * 4 + 3] = -1.0D;
							} else {
								adouble[l2 * 4 + 3] = -1.0D;
							}
						}
					}
				}
			}
		}

		for (int i3 = 0; i3 < config.size; ++i3) {
			double d11 = adouble[i3 * 4 + 3];
			if (!(d11 < 0.0D)) {
				double d1 = adouble[i3 * 4 + 0];
				double d3 = adouble[i3 * 4 + 1];
				double d5 = adouble[i3 * 4 + 2];
				int k = Math.max(MathHelper.floor(d1 - d11), p_207803_16_);
				int k3 = Math.max(MathHelper.floor(d3 - d11), p_207803_17_);
				int l = Math.max(MathHelper.floor(d5 - d11), p_207803_18_);
				int i1 = Math.max(MathHelper.floor(d1 + d11), k);
				int j1 = Math.max(MathHelper.floor(d3 + d11), k3);
				int k1 = Math.max(MathHelper.floor(d5 + d11), l);

				for (int x = k; x <= i1; ++x) {
					double d8 = (x + 0.5D - d1) / d11;
					if (d8 * d8 < 1.0D) {
						for (int y = k3; y <= j1; ++y) {
							double d9 = (y + 0.5D - d3) / d11;
							if (d8 * d8 + d9 * d9 < 1.0D) {
								for (int z = l; z <= k1; ++z) {
									double d10 = (z + 0.5D - d5) / d11;
									if (d8 * d8 + d9 * d9 + d10 * d10 < 1.0D) {
										int k2 = x - p_207803_16_ + (y - p_207803_17_) * p_207803_19_
												+ (z - p_207803_18_) * p_207803_19_ * p_207803_20_;
										if (!bitset.get(k2)) {
											bitset.set(k2);
											pos.setPos(x, y, z);
											if (config.target.getTargetBlockPredicate().test(world.getBlockState(pos))) {
												world.setBlockState(pos, config.state, 2);
												if (y < 40) {
													pos.setPos(x, y + 1, z);
													if (world.getBlockState(pos).isAir(world, pos) && world.getHeight(Heightmap.Type.MOTION_BLOCKING, x, z) > y + 1 && world.getLightFor(LightType.BLOCK, pos) < 13) {
														world.setBlockState(pos, ModBlocks.SALT_CRYSTAL.get().getDefaultState().with(SaltCrystal.AGE, random.nextInt(2)), 2);
													}
												}
												++i;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return i > 0;
	}
}