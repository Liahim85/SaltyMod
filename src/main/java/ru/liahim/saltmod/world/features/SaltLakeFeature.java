package ru.liahim.saltmod.world.features;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import ru.liahim.saltmod.block.SaltCrystal;
import ru.liahim.saltmod.block.SaltDirtLite;
import ru.liahim.saltmod.block.SaltSource;
import ru.liahim.saltmod.block.SaltWort;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.SaltConfig;

public class SaltLakeFeature extends Feature<NoFeatureConfig> {

	public SaltLakeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		boolean check = false;
			if (rand.nextInt(SaltConfig.Generation.saltLakeGroupRarity.get()) == 0) {
			BlockPos center = pos;
			for (int G = 0; G < SaltConfig.Generation.saltLakeQuantity.get(); G++) {
				check |= generate(world, generator, rand, center);
				center = pos.add(rand.nextInt(SaltConfig.Generation.saltLakeDistance.get()) - SaltConfig.Generation.saltLakeDistance.get()/2,
						0,
						rand.nextInt(SaltConfig.Generation.saltLakeDistance.get()) - SaltConfig.Generation.saltLakeDistance.get()/2);
			}
		}
		return check;
	}

	private static boolean isFull(IWorld world, BlockPos pos) {
		return world.getBlockState(pos).getShape(world, pos) == VoxelShapes.fullCube();
	}

	private static boolean isFullAndTop(IWorld world, BlockPos pos) {
		return isFull(world, pos) && world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).getY() == pos.getY() + 1;
	}

	private boolean generate(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos) {
		BlockPos center = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.add(rand.nextInt(16), 0, rand.nextInt(16))).down();
		if (center.getY() >= SaltConfig.Generation.saltLakeMinHeight.get() && center.getY() < SaltConfig.Generation.saltLakeMaxHeight.get()) {
			if (isFullAndTop(world, center) && Streams.stream(Direction.Plane.HORIZONTAL.iterator()).allMatch(dir -> isFullAndTop(world, center.offset(dir, 2)))) {
				int radius = SaltConfig.Generation.saltLakeRadius.get();
				byte[][] bits = new byte[radius * 2 + 1][radius * 2 + 1];
				for (int x = -radius; x <= radius; x++) {
    				for (int z = -radius; z <= radius; z++) {
    					int h = generator.getHeight(center.getX() + x, center.getZ() + z, Heightmap.Type.WORLD_SURFACE_WG) - 1;
    					if (center.getY() < h - 1 || isFull(world, center.add(x, 2, z))) {
    						bits[x + radius][z + radius] = -2;
    					} else if (center.getY() > h || world.getBlockState(center.add(x, 0, z)).getMaterial().isLiquid()) {
    						bits[x + radius][z + radius] = -2;
    					} else if (center.getY() == h || center.getY() == h - 1) {
    						bits[x + radius][z + radius] = 0;
    					} else bits[x + radius][z + radius] = -1;
    				}
				}
				byte j;
				boolean check = false;
				for (byte i = -2; i > -4; --i) {
					for (int x = 0; x < bits.length; x++) {
	    				for (int z = 0; z < bits[0].length; z++) {
	    					if (bits[x][z] == 0 && (i == -2 || rand.nextBoolean())) {
	    						check = false;
		    					if (x > 0) {
		    						j = bits[x - 1][z];
		    						if (j >= i && j < -1) check = true;
		    					}
		    					if (!check && x < radius * 2) {
		    						j = bits[x + 1][z];
		    						if (j >= i && j < -1) check = true;
		    					}
		    					if (!check && z > 0) {
		    						j = bits[x][z - 1];
		    						if (j >= i && j < -1) check = true;
		    					}
		    					if (!check && z < radius * 2) {
		    						j = bits[x][z + 1];
		    						if (j >= i && j < -1) check = true;
		    					}
		    					if (check) bits[x][z] = (byte) (i - 1);
	    					}
	    				}
					}
				}
				if (bits[radius][radius] == 0) bits[radius][radius] = 1;
				for (byte i = 2; i <= radius; i++) {
					for (int x = radius - i; x <= radius + i; x++) {
	    				for (int z = radius - i; z <= radius + i; z++) {
	    					if (bits[x][z] == 0 && rand.nextBoolean()) {
	    						check = false;
		    					if (x > 0) {
		    						j = bits[x - 1][z];
		    						if (j > 0 && j < i) check = true;
		    					}
		    					if (!check && x < radius * 2) {
		    						j = bits[x + 1][z];
		    						if (j > 0 && j < i) check = true;
		    					}
		    					if (!check && z > 0) {
		    						j = bits[x][z - 1];
		    						if (j > 0 && j < i) check = true;
		    					}
		    					if (!check && z < radius * 2) {
		    						j = bits[x][z + 1];
		    						if (j > 0 && j < i) check = true;
		    					}
		    					if (check) bits[x][z] = i;
	    					}
	    				}
					}
				}
				int count = 0;
				for (int x = 0; x < bits.length; x++) {
    				for (int z = 0; z < bits[0].length; z++) {
    					if (bits[x][z] <= 0) {
    						check = false;
	    					if (x > 0) {
	    						j = bits[x - 1][z];
	    						if (j > 0) check = true;
	    					}
	    					if (!check && x < radius * 2) {
	    						j = bits[x + 1][z];
	    						if (j > 0) check = true;
	    					}
	    					if (!check && z > 0) {
	    						j = bits[x][z - 1];
	    						if (j > 0) check = true;
	    					}
	    					if (!check && z < radius * 2) {
	    						j = bits[x][z + 1];
	    						if (j > 0) check = true;
	    					}
	    					if (check) bits[x][z] = -2;
	    					else {
	    						check = false;
		    					if (x > 0 && z > 0) {
		    						j = bits[x - 1][z - 1];
		    						if (j > 0) check = true;
		    					}
		    					if (!check && x < radius * 2 && z > 0) {
		    						j = bits[x + 1][z - 1];
		    						if (j > 0) check = true;
		    					}
		    					if (!check && x > 0 && z < radius * 2) {
		    						j = bits[x - 1][z + 1];
		    						if (j > 0) check = true;
		    					}
		    					if (!check && x < radius * 2 && z < radius * 2) {
		    						j = bits[x + 1][z + 1];
		    						if (j > 0) check = true;
		    					}
		    					if (check) bits[x][z] = -1;
		    					else bits[x][z] = 0;
	    					}
    					} else {
    						check = false;
	    					if (x > 0) {
	    						j = bits[x - 1][z];
	    						if (j <= 0) check = true;
	    					}
	    					if (!check && x < radius * 2) {
	    						j = bits[x + 1][z];
	    						if (j <= 0) check = true;
	    					}
	    					if (!check && z > 0) {
	    						j = bits[x][z - 1];
	    						if (j <= 0) check = true;
	    					}
	    					if (!check && z < radius * 2) {
	    						j = bits[x][z + 1];
	    						if (j <= 0) check = true;
	    					}
	    					if (check) bits[x][z] = 2;
	    					else bits[x][z] = 1;
	    					++count;
    					}
    				}
				}
				if (count < (radius * radius * Math.PI) / 10) return false;
				check = false;
				BlockPos checkPos;
				Block checkBlock;
				List<BlockPos> saltwortPoses = Lists.newArrayList();
				for (int x = -radius; x <= radius; x++) {
    				for (int z = -radius; z <= radius; z++) {
    					byte i = bits[x + radius][z + radius];
    					checkPos = center.add(x, 0, z);
    					if (i > 0) {
    						check = true;
    						if (world.getBlockState(checkPos.up(2)).getBlock() instanceof BushBlock) {
	    						world.setBlockState(checkPos.up(2), Blocks.AIR.getDefaultState(), 3);
    						}
    						world.setBlockState(checkPos.up(), Blocks.AIR.getDefaultState(), 3);
    						world.setBlockState(checkPos, Blocks.AIR.getDefaultState(), 3);
    						boolean mud = rand.nextInt(3) == 0;
    						if (mud) {
    							world.setBlockState(checkPos.down(), ModBlocks.SALT_DIRT_LAKE.get().getDefaultState(), 3); //.with(SaltSource.VARIANT, SaltSource.EnumType.byMetadata(getShoresMeta(world, checkPos.down(), true))), 3);
    							world.setBlockState(checkPos.down(2), ModBlocks.MUD_BLOCK.get().getDefaultState(), 3);
    						} else world.setBlockState(checkPos.down(), ModBlocks.SALT_LAKE.get().getDefaultState(), 3); //.with(SaltSource.VARIANT, SaltSource.EnumType.byMetadata(getShoresMeta(world, checkPos.down(), true))), 3);
		        			world.setBlockState(checkPos.down(4), Blocks.STONE.getDefaultState(), 3);
		        			if (rand.nextInt(2) == 0) {
		        				if (!mud) world.setBlockState(checkPos.down(2), ModBlocks.SALT_ORE.get().getDefaultState(), 3);
		        				world.setBlockState(checkPos.down(5), Blocks.STONE.getDefaultState(), 3);
		        				if (rand.nextInt(5) == 0) {
		        					world.setBlockState(checkPos.down(3), ModBlocks.SALT_ORE.get().getDefaultState(), 3);
		        				} else world.setBlockState(checkPos.down(3), Blocks.STONE.getDefaultState(), 3);
		        			} else if (!mud) world.setBlockState(checkPos.down(2), Blocks.STONE.getDefaultState(), 3);
		        			if (rand.nextInt(5) == 0 && world.getHeight(Heightmap.Type.MOTION_BLOCKING, checkPos.getX(), checkPos.getZ()) - 1 > checkPos.getY()) {
		        				if (rand.nextInt(4) == 0) {
		        					world.setBlockState(checkPos, ModBlocks.SALT_CRYSTAL.get().getDefaultState().with(SaltCrystal.AGE, 1), 3);
		        				} else world.setBlockState(checkPos, ModBlocks.SALT_CRYSTAL.get().getDefaultState().with(SaltCrystal.AGE, 2), 3);
		        			}
    					} else if (i < 0) {
        					if (i < -1 && !isFull(world, checkPos.up(2))) {
        						if (world.getBlockState(checkPos.up(2)).getBlock() instanceof BushBlock) {
        							world.setBlockState(checkPos.up(2), Blocks.AIR.getDefaultState(), 3);
        						}
        						world.setBlockState(checkPos.up(), Blocks.AIR.getDefaultState(), 3);
        					}
        					checkBlock = world.getBlockState(checkPos).getBlock();
        					if (checkBlock == Blocks.STONE) {
    							if (i < -1) world.setBlockState(checkPos, ModBlocks.SALT_ORE.get().getDefaultState().with(SaltSource.VARIANT, SaltSource.EnumType.byMetadata(getShoresMeta(x, z, radius, bits, true))), 3);
    						} else if (checkBlock == Blocks.GRASS_BLOCK || checkBlock == Blocks.COARSE_DIRT || checkBlock == Blocks.DIRT) {
    							checkBlock = world.getBlockState(checkPos.up()).getBlock();
        						if (checkBlock instanceof BushBlock && checkBlock != ModBlocks.SALTWORT.get()) {
        							world.setBlockState(checkPos.up(), Blocks.AIR.getDefaultState(), 3);
        						}
    							if (!isFull(world, checkPos.up())) {
    								world.setBlockState(checkPos, ModBlocks.SALT_GRASS.get().getDefaultState().with(SaltDirtLite.VARIANT, SaltDirtLite.EnumType.byMetadata(getShoresMeta(x, z, radius, bits, false))), 3);
    								if (rand.nextInt(10) == 0) {
	        							world.setBlockState(checkPos.up(), ModBlocks.SALTWORT.get().getDefaultState().with(SaltWort.AGE, 4), 3);
	        							saltwortPoses.add(checkPos.up());
	        						}
    							} else world.setBlockState(checkPos, ModBlocks.SALT_DIRT_LITE.get().getDefaultState().with(SaltDirtLite.VARIANT, SaltDirtLite.EnumType.byMetadata(getShoresMeta(x, z, radius, bits, false))), 3);
        					} else if (checkBlock == ModBlocks.SALT_LAKE.get()) {
        						world.setBlockState(checkPos, ModBlocks.SALT_LAKE.get().getDefaultState().with(SaltSource.VARIANT, SaltSource.EnumType.byMetadata(getShoresMeta(x, z, radius, bits, true))), 3);
        					} else if (checkBlock == ModBlocks.SALT_DIRT_LAKE.get()) {
        						world.setBlockState(checkPos, ModBlocks.SALT_DIRT_LAKE.get().getDefaultState().with(SaltSource.VARIANT, SaltSource.EnumType.byMetadata(getShoresMeta(x, z, radius, bits, true))), 3);
        					}
    					}
    				}
				}
				if (!saltwortPoses.isEmpty()) {
					saltwortPoses.forEach(pos1 -> {
						if (rand.nextBoolean()) {
							Streams.stream(Direction.Plane.HORIZONTAL.iterator()).filter(dir -> {
								return rand.nextBoolean() && world.getBlockState(pos1.offset(dir)).getBlock() == Blocks.AIR &&
										(world.getBlockState(pos1.offset(dir).down()).getBlock() == ModBlocks.SALT_GRASS.get() ||
										world.getBlockState(pos1.offset(dir).down()).getBlock() == ModBlocks.SALT_DIRT_LITE.get());
							}).forEach(dir -> world.setBlockState(pos1.offset(dir), ModBlocks.SALTWORT.get().getDefaultState().with(SaltWort.AGE, rand.nextInt(2) + 2), 3));
						}
					});
				}
				return check;
			}
		}
		return false;
	}

	private int getShoresMeta(int x, int z, int radius, byte[][] bits, boolean isStone) {
		byte f = 0;
		if (z > 0 && bits[x + radius][z + radius - 1] > 0) f += 1;
		if (x < radius * 2 && bits[x + radius + 1][z + radius] > 0) f += 2;
		if (z < radius * 2 && bits[x + radius][z + radius + 1] > 0) f += 4;
		if (x > 0 && bits[x + radius - 1][z + radius] > 0) f += 8;
		if (isStone) return f;
		byte c = 0;
		if (x < radius * 2 && z > 0 && bits[x + radius + 1][z + radius - 1] > 0) c += 1;
		if (x < radius * 2 && z < radius * 2 && bits[x + radius + 1][z + radius + 1] > 0) c += 2;
		if (x > 0 && z < radius * 2 && bits[x + radius - 1][z + radius + 1] > 0) c += 4;
		if (x > 0 && z > 0 && bits[x + radius - 1][z + radius - 1] > 0) c += 8;
		return getGrassMeta(f, c);
	}

	private byte getGrassMeta(byte f, byte c) {
		byte meta = 0;
    	if(f==0&&c==1){meta=3;}else if(f==0&&c==2){meta=4;}
    	else if(f==0&&c==4){meta=5;}else if(f==0&&c==8){meta=6;}
    	else if((f==0&&c==9)||(f==1&&(c==0||c==1||c==8||c==9))){meta=7;}
    	else if((f==0&&c==3)||(f==2&&(c==0||c==1||c==2||c==3))){meta=8;}
    	else if((f==0&&c==6)||(f==4&&(c==0||c==2||c==4||c==6))){meta=9;}
    	else if((f==0&&c==12)||(f==8&&(c==0||c==4||c==8||c==12))){meta=10;}
    	else if((f==0&&c==11)||f==1&&(c==2||c==3||c==10||c==11)||f==2&&(c>=8&&c<=11)||(f==3&&((c>=0&&c<=3)||(c>=8&&c<=11)))){meta=11;}
    	else if((f==0&&c==7)||f==2&&(c>=4&&c<=7)||f==4&&(c==1||c==3||c==5||c==7)||(f==6&&((c>=0&&c<=3)||(c>=4&&c<=7)))){meta=12;}
    	else if((f==0&&c==14)||f==4&&(c==8||c==10||c==12||c==14)||f==8&&(c==2||c==6||c==10||c==14)||(f==12&&(c==0||c==2||c==4||c==6||c==8||c==10||c==12||c==14))){meta=13;}
    	else if((f==0&&c==13)||f==1&&(c==4||c==5||c==12||c==13)||f==8&&(c==1||c==5||c==9||c==13)||(f==9&&(c==0||c==1||c==4||c==5||c==8||c==9||c==12||c==13))){meta=14;}
    	else{meta=15;}
		return meta;
	}
}