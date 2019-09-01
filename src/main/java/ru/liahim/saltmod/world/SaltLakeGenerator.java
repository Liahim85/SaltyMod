package ru.liahim.saltmod.world;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.IWorldGenerator;
import ru.liahim.saltmod.api.block.SaltBlocks;
import ru.liahim.saltmod.block.SaltCrystal;
import ru.liahim.saltmod.block.SaltDirt;
import ru.liahim.saltmod.block.SaltDirtLite;
import ru.liahim.saltmod.block.SaltGrass;
import ru.liahim.saltmod.block.SaltLake;
import ru.liahim.saltmod.block.SaltOre;
import ru.liahim.saltmod.block.SaltWort;
import ru.liahim.saltmod.init.SaltConfig;

public class SaltLakeGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {   
		switch(world.provider.getDimension()) {
			case 0: {
				if (SaltConfig.disableCascadingLog) ForgeModContainer.logCascadingWorldGeneration = false;
				generateOverworld(world, random, chunkX*16, chunkZ*16);
			}
			break;
		}
	}
	
	public void generateOverworld(World world, Random rand, int X1, int Z1) {
		
		if (!world.isRemote) {
		
		int rad = SaltConfig.saltLakeRadius;
		
        int randPosX = X1 + rand.nextInt(16);
        int randPosZ = Z1 + rand.nextInt(16);
        
//Chance
    	if (rand.nextInt(SaltConfig.saltLakeGroupRarity) == 0 &&
    		(world.getBiome(new BlockPos(randPosX, 60, randPosZ)) != Biomes.SWAMPLAND &&
           	 world.getBiome(new BlockPos(randPosX, 60, randPosZ)) != Biomes.MUTATED_SWAMPLAND)){
    		
//The number of lakes in the group
    	for (int G = 0; G < SaltConfig.saltLakeQuantity; G++){
    	
//Site selection
        for (int randPosY = SaltConfig.saltLakeMinHeight; randPosY < SaltConfig.saltLakeMaxHeight; randPosY++){

        BlockPos posCenter = new BlockPos(randPosX, randPosY, randPosZ);        
        	
        if ((world.getBlockState(posCenter).getBlock() == Blocks.GRASS ||
        	 world.getBlockState(posCenter).getBlock() == Blocks.STONE) &&
        	 world.getBlockState(posCenter.west(2)).isFullCube() &&
        	 world.getBlockState(posCenter.east(2)).isFullCube() &&
        	 world.getBlockState(posCenter.north(2)).isFullCube() &&
        	 world.getBlockState(posCenter.south(2)).isFullCube() &&
        	 world.getBlockState(posCenter.down(3)).isFullCube() &&
        	 world.getLightFromNeighbors(posCenter.up()) >= 13 &&
        	 world.getLightFromNeighbors(posCenter.add(-2, 1, 0)) >= 13 &&
        	 world.getLightFromNeighbors(posCenter.add(2, 1, 0)) >= 13 &&
        	 world.getLightFromNeighbors(posCenter.add(0, 1, -2)) >= 13 &&
        	 world.getLightFromNeighbors(posCenter.add(0, 1, 2)) >= 13){

        		world.setBlockToAir(posCenter.up());
        		world.setBlockToAir(posCenter.down());
        		world.setBlockState(posCenter.down(2), SaltBlocks.SALT_LAKE.getDefaultState(), 3);
        		world.setBlockState(posCenter.down(3), SaltBlocks.SALT_ORE.getDefaultState(), 3);
        		world.setBlockState(posCenter.down(4), SaltBlocks.SALT_ORE.getDefaultState(), 3);
        		world.setBlockState(posCenter.down(5), Blocks.STONE.getDefaultState(), 3);
        		world.setBlockState(posCenter.down(6), Blocks.STONE.getDefaultState(), 3);
        	
//Growth
        	for (int i = 2; i <= rad; i++) {
        		
            for (int x = randPosX - i; x <= randPosX + i; x++) {
            for (int z = randPosZ - i; z <= randPosZ + i; z++) {
            	
            	BlockPos pos = new BlockPos(x, randPosY, z);
            	BlockPos pos_2 = new BlockPos(pos.down(2));
            	
            	if (rand.nextInt(2) == 0 &&
                   	world.getBlockState(pos.west()).isFullCube() &&
                   	world.getBlockState(pos.east()).isFullCube() &&
                   	world.getBlockState(pos.north()).isFullCube() &&
                   	world.getBlockState(pos.south()).isFullCube() &&
                   	world.getBlockState(pos.down(3)).isFullCube() &&
            		world.getLightFromNeighbors(pos.up()) >= 14 &&
            	   (world.getBlockState(pos_2.west()).getBlock() == SaltBlocks.SALT_LAKE ||
            		world.getBlockState(pos_2.east()).getBlock() == SaltBlocks.SALT_LAKE ||
            		world.getBlockState(pos_2.north()).getBlock() == SaltBlocks.SALT_LAKE ||
            		world.getBlockState(pos_2.south()).getBlock() == SaltBlocks.SALT_LAKE))
            		{
            			world.setBlockToAir(pos.up());
                		world.setBlockToAir(pos.down());
            			world.setBlockState(pos_2, SaltBlocks.SALT_BLOCK.getDefaultState(), 3);
            		}
            }
            }
            
            for (int x = randPosX - i; x <= randPosX + i; x++) {
            for (int z = randPosZ - i; z <= randPosZ + i; z++) {
            
            	BlockPos pos = new BlockPos(x, randPosY, z);
            	
            	if (world.getBlockState(pos.down(2)).getBlock() == SaltBlocks.SALT_BLOCK) {
            		
        			world.setBlockState(pos.down(2), SaltBlocks.SALT_LAKE.getDefaultState(), 3);
        			world.setBlockState(pos.down(5), Blocks.STONE.getDefaultState(), 3);
        			
        			if (rand.nextInt(2) == 0) {
        				world.setBlockState(pos.down(3), SaltBlocks.SALT_ORE.getDefaultState(), 3);
        				world.setBlockState(pos.down(6), Blocks.STONE.getDefaultState(), 3);

        				if  (rand.nextInt(5) == 0) {
        					world.setBlockState(pos.down(4), SaltBlocks.SALT_ORE.getDefaultState(), 3);
        				} else {
        					world.setBlockState(pos.down(4), Blocks.STONE.getDefaultState(), 3);
            			}
        			} else {
        				world.setBlockState(pos.down(3), Blocks.STONE.getDefaultState(), 3);
        			}
        			
        			if (world.getLightFromNeighbors(pos.up()) <= 14 && rand.nextInt(5) == 0) {
        				if (rand.nextInt(4) == 0) {
        					world.setBlockState(pos.down(1), SaltBlocks.SALT_CRYSTAL.getDefaultState().withProperty(SaltCrystal.STAGE, SaltCrystal.EnumType.MEDIUM), 3);
        				} else {
        					world.setBlockState(pos.down(1), SaltBlocks.SALT_CRYSTAL.getDefaultState().withProperty(SaltCrystal.STAGE, SaltCrystal.EnumType.SMALL), 3);
        				}
        			}
            	}
            }
            }
            
            }
            
//Making shores
            for (int x = randPosX - rad; x <= randPosX + rad; x++) {
            for (int z = randPosZ - rad; z <= randPosZ + rad; z++) {
            	
            	BlockPos pos = new BlockPos(x, randPosY, z);
            	BlockPos pos1 = pos.up();
            	BlockPos pos_1 = pos.down();
            	BlockPos pos_2 = pos.down(2);
            	BlockPos pos_3 = pos.down(3);
            	
            	if (world.getBlockState(pos_2).getBlock() == SaltBlocks.SALT_LAKE) {
            		world.setBlockToAir(pos);
            	} else {
        			int jf = 0;
					if (world.getBlockState(pos_2.north()).getBlock() == SaltBlocks.SALT_LAKE)
					{jf = jf + 1;}
					if (world.getBlockState(pos_2.east()).getBlock() == SaltBlocks.SALT_LAKE)
					{jf = jf + 2;}
					if (world.getBlockState(pos_2.south()).getBlock() == SaltBlocks.SALT_LAKE)
					{jf = jf + 4;}
					if (world.getBlockState(pos_2.west()).getBlock() == SaltBlocks.SALT_LAKE)
					{jf = jf + 8;}
					
    				int jc = 0;
    				if (world.getBlockState(pos_2.add(1, 0, -1)).getBlock() == SaltBlocks.SALT_LAKE)
    				{jc = jc + 1;}
    				if (world.getBlockState(pos_2.add(1, 0, 1)).getBlock() == SaltBlocks.SALT_LAKE)
    				{jc = jc + 2;}
    				if (world.getBlockState(pos_2.add(-1, 0, 1)).getBlock() == SaltBlocks.SALT_LAKE)
    				{jc = jc + 4;}
    				if (world.getBlockState(pos_2.add(-1, 0, -1)).getBlock() == SaltBlocks.SALT_LAKE)
    				{jc = jc + 8;}
    				
    				int j = 0;
                	if(jf==0&&jc==1){j=3;}else if(jf==0&&jc==2){j=4;}
                	else if(jf==0&&jc==4){j=5;}else if(jf==0&&jc==8){j=6;}
                	else if((jf==0&&jc==9)||(jf==1&&(jc==0||jc==1||jc==8||jc==9))){j=7;}
                	else if((jf==0&&jc==3)||(jf==2&&(jc==0||jc==1||jc==2||jc==3))){j=8;}
                	else if((jf==0&&jc==6)||(jf==4&&(jc==0||jc==2||jc==4||jc==6))){j=9;}
                	else if((jf==0&&jc==12)||(jf==8&&(jc==0||jc==4||jc==8||jc==12))){j=10;}
                	else if((jf==0&&jc==11)||jf==1&&(jc==2||jc==3||jc==10||jc==11)||jf==2&&(jc>=8&&jc<=11)||(jf==3&&((jc>=0&&jc<=3)||(jc>=8&&jc<=11)))){j=11;}
                	else if((jf==0&&jc==7)||jf==2&&(jc>=4&&jc<=7)||jf==4&&(jc==1||jc==3||jc==5||jc==7)||(jf==6&&((jc>=0&&jc<=3)||(jc>=4&&jc<=7)))){j=12;}
                	else if((jf==0&&jc==14)||jf==4&&(jc==8||jc==10||jc==12||jc==14)||jf==8&&(jc==2||jc==6||jc==10||jc==14)||(jf==12&&(jc==0||jc==2||jc==4||jc==6||jc==8||jc==10||jc==12||jc==14))){j=13;}
                	else if((jf==0&&jc==13)||jf==1&&(jc==4||jc==5||jc==12||jc==13)||jf==8&&(jc==1||jc==5||jc==9||jc==13)||(jf==9&&(jc==0||jc==1||jc==4||jc==5||jc==8||jc==9||jc==12||jc==13))){j=14;}
                	else{j=15;}
					
            		if (jf > 0){

            			if (world.getBlockState(pos_2).getBlock() != SaltBlocks.SALT_DIRT &&
            				world.getBlockState(pos_2).getBlock() != SaltBlocks.SALT_DIRT_LITE &&
            				world.getBlockState(pos_2).getBlock() != SaltBlocks.SALT_ORE)
            			{
            				if (world.isAirBlock(pos_2.west()) || world.isAirBlock(pos_2.east()) ||
            					world.isAirBlock(pos_2.north()) || world.isAirBlock(pos_2.south()))
            				{
            					world.setBlockState(pos_2, Blocks.STONE.getDefaultState(), 3);
            				} else {world.setBlockState(pos_2, SaltBlocks.SALT_ORE.getDefaultState(), 3);}
            			}
            			
            			if (rand.nextInt(2) != 0) {
            				world.setBlockState(pos_3, Blocks.STONE.getDefaultState(), 3);
            			}
            					
                		if (world.getBlockState(pos_1).getBlock() == Blocks.STONE ||
                			world.getBlockState(pos_1).getBlock() == Blocks.COAL_ORE ||
                			world.getBlockState(pos_1).getBlock() == Blocks.IRON_ORE ||
                		   (world.getBlockState(pos_1).getBlock() == SaltBlocks.SALT_ORE &&
                		    world.getBlockState(pos_1).getBlock().getMetaFromState(world.getBlockState(pos_1)) == 0))
            			{
            				world.setBlockState(pos_1, SaltBlocks.SALT_ORE.getDefaultState().withProperty(SaltOre.VARIANT, SaltOre.EnumType.byMetadata(jf)), 3);
            			}
                			
            			if (world.getBlockState(pos_1).getBlock() == Blocks.DIRT) {
            				world.setBlockState(pos_1, SaltBlocks.SALT_DIRT_LITE.getDefaultState().withProperty(SaltDirtLite.VARIANT, SaltDirtLite.EnumType.byMetadata(j)), 3);
            			}
            			
            			if (world.isAirBlock(pos_1)) {
                			int jld = 0;
        					if (world.getBlockState(pos_3.north()).getBlock() == SaltBlocks.SALT_LAKE || world.getBlockState(pos_3.north()).getBlock() == SaltBlocks.SALT_DIRT)
        					{jld = jld + 1;}
        					if (world.getBlockState(pos_3.east()).getBlock() == SaltBlocks.SALT_LAKE || world.getBlockState(pos_3.east()).getBlock() == SaltBlocks.SALT_DIRT)
        					{jld = jld + 2;}
        					if (world.getBlockState(pos_3.south()).getBlock() == SaltBlocks.SALT_LAKE || world.getBlockState(pos_3.south()).getBlock() == SaltBlocks.SALT_DIRT)
        					{jld = jld + 4;}
        					if (world.getBlockState(pos_3.west()).getBlock() == SaltBlocks.SALT_LAKE || world.getBlockState(pos_3.west()).getBlock() == SaltBlocks.SALT_DIRT)
        					{jld = jld + 8;}
        					
        					if (jld > 0)
        					{world.setBlockState(pos_2, SaltBlocks.SALT_LAKE.getDefaultState().withProperty(SaltLake.VARIANT, SaltLake.EnumType.byMetadata(jld)), 3);}
        					else
            				{world.setBlockState(pos_2, SaltBlocks.SALT_DIRT.getDefaultState().withProperty(SaltDirt.VARIANT, SaltDirt.EnumType.LAKE), 3);}
        				}
            					
                		if (world.getLightFromNeighbors(pos1) >= 12) {
            				if (world.getBlockState(pos_1.west()).getBlock() == SaltBlocks.SALT_LAKE ||
            					world.getBlockState(pos_1.east()).getBlock() == SaltBlocks.SALT_LAKE ||
            					world.getBlockState(pos_1.north()).getBlock() == SaltBlocks.SALT_LAKE ||
            					world.getBlockState(pos_1.south()).getBlock() == SaltBlocks.SALT_LAKE ||
            					world.getBlockState(pos_1.west()).getBlock() == SaltBlocks.SALT_DIRT ||
            					world.getBlockState(pos_1.east()).getBlock() == SaltBlocks.SALT_DIRT ||
            					world.getBlockState(pos_1.north()).getBlock() == SaltBlocks.SALT_DIRT ||
            					world.getBlockState(pos_1.south()).getBlock() == SaltBlocks.SALT_DIRT)
                            {
            					
                    			int jl = 0;
            					if (world.getBlockState(pos_2.north()).getBlock() == SaltBlocks.SALT_LAKE || world.getBlockState(pos_2.north()).getBlock() == SaltBlocks.SALT_DIRT)
            					{jl = jl + 1;}
            					if (world.getBlockState(pos_2.east()).getBlock() == SaltBlocks.SALT_LAKE || world.getBlockState(pos_2.east()).getBlock() == SaltBlocks.SALT_DIRT)
            					{jl = jl + 2;}
            					if (world.getBlockState(pos_2.south()).getBlock() == SaltBlocks.SALT_LAKE || world.getBlockState(pos_2.south()).getBlock() == SaltBlocks.SALT_DIRT)
            					{jl = jl + 4;}
            					if (world.getBlockState(pos_2.west()).getBlock() == SaltBlocks.SALT_LAKE || world.getBlockState(pos_2.west()).getBlock() == SaltBlocks.SALT_DIRT)
            					{jl = jl + 8;}
            					
            					world.setBlockState(pos_1, SaltBlocks.SALT_LAKE.getDefaultState().withProperty(SaltLake.VARIANT, SaltLake.EnumType.byMetadata(jl)), 3);
            					
                            }

            				if (world.getBlockState(pos.west()).getMaterial() != Material.WATER &&
            					world.getBlockState(pos.east()).getMaterial() != Material.WATER &&
            					world.getBlockState(pos.north()).getMaterial() != Material.WATER &&
            					world.getBlockState(pos.south()).getMaterial() != Material.WATER &&
            					world.getBlockState(pos.west()).getMaterial() != Material.LAVA &&
            					world.getBlockState(pos.east()).getMaterial() != Material.LAVA &&
            					world.getBlockState(pos.north()).getMaterial() != Material.LAVA &&
            					world.getBlockState(pos.south()).getMaterial() != Material.LAVA)
            				{

                    			if (world.getBlockState(pos_1).getBlock() == SaltBlocks.SALT_DIRT_LITE &&
                    			   (world.getBlockState(pos).getBlock() == Blocks.GRASS)) {
                    				world.setBlockState(pos_1, SaltBlocks.SALT_GRASS.getDefaultState().withProperty(SaltGrass.VARIANT, SaltGrass.EnumType.byMetadata(j)), 3);
                    			}
                    			
            					world.setBlockToAir(pos1);
                				world.setBlockToAir(pos);
                				
                				if (rand.nextInt(10) == 0 &&
                				   (world.getBlockState(pos_1).getBlock() == SaltBlocks.SALT_GRASS ||
                					world.getBlockState(pos_1).getBlock() == SaltBlocks.SALT_DIRT_LITE)) {
            						world.setBlockState(pos, SaltBlocks.SALTWORT.getDefaultState().withProperty(SaltWort.STAGE, SaltWort.EnumType.STAGE_4), 3);
            					}
             				}
                		} else {
            				if (world.getLightFromNeighbors(pos.up(2)) >= 12) {
            					if (rand.nextInt(2) == 0 &&
            						world.getBlockState(pos1.west()).getMaterial() != Material.WATER &&
            						world.getBlockState(pos1.east()).getMaterial() != Material.WATER &&
            						world.getBlockState(pos1.north()).getMaterial() != Material.WATER &&
            						world.getBlockState(pos1.south()).getMaterial() != Material.WATER &&
            						world.getBlockState(pos1.west()).getMaterial() != Material.LAVA &&
            						world.getBlockState(pos1.east()).getMaterial() != Material.LAVA &&
            						world.getBlockState(pos1.north()).getMaterial() != Material.LAVA &&
            						world.getBlockState(pos1.south()).getMaterial() != Material.LAVA)
            					{

            						if (world.getBlockState(pos).getBlock() == Blocks.DIRT) {
            							world.setBlockState(pos, world.getBlockState(pos1), 3);
            						} else if (world.getBlockState(pos).getBlock() == SaltBlocks.SALT_DIRT_LITE) {
            							int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
            							world.setBlockState(pos, SaltBlocks.SALT_GRASS.getDefaultState().withProperty(SaltGrass.VARIANT, SaltGrass.EnumType.byMetadata(meta)), 3);
            						}
            						
            						world.setBlockToAir(pos.up(2));
            						world.setBlockToAir(pos1);
            					}
            				}
            			}
            		}
            		
            		else if (jc > 0){
            			
                        if (world.getBlockState(pos_1).isFullCube() &&
                        	world.getBlockState(pos_2).getBlock() != SaltBlocks.SALT_DIRT_LITE &&
                        	world.getBlockState(pos_2).getBlock() != SaltBlocks.SALT_ORE) {
                        	world.setBlockState(pos_2, Blocks.STONE.getDefaultState(), 3);
                       	}
                        	
                       	if (world.getBlockState(pos_1).getBlock() == Blocks.GRASS) {
    						world.setBlockToAir(pos);
                       		world.setBlockState(pos_1, SaltBlocks.SALT_GRASS.getDefaultState().withProperty(SaltGrass.VARIANT, SaltGrass.EnumType.byMetadata(j)), 3);
                       	} else if (world.getBlockState(pos_1).getBlock() == Blocks.DIRT) {
                       		world.setBlockState(pos_1, SaltBlocks.SALT_DIRT_LITE.getDefaultState().withProperty(SaltDirtLite.VARIANT, SaltDirtLite.EnumType.byMetadata(j)), 3);
                       	}
            		}
            	}
            }
            }
            
//The bottom and SaltWort
            for (int x = randPosX - rad; x <= randPosX + rad; x++) {
            for (int z = randPosZ - rad; z <= randPosZ + rad; z++) {
            	
            	BlockPos pos = new BlockPos(x, randPosY, z);
            	
            	if (world.getBlockState(pos.down(2)).getBlock() == SaltBlocks.SALT_LAKE) {
            		if (rand.nextInt(3) == 0) {
            			world.setBlockState(pos.down(2), SaltBlocks.SALT_DIRT.getDefaultState().withProperty(SaltDirt.VARIANT, SaltDirt.EnumType.LAKE), 3);
            			world.setBlockState(pos.down(3), SaltBlocks.MUD_BLOCK.getDefaultState(), 3);
            		}
            	}
            	
            	if (world.getBlockState(pos.down()).getBlock() == SaltBlocks.SALT_GRASS || world.getBlockState(pos.down()).getBlock() == SaltBlocks.SALT_DIRT_LITE) {
            		if (world.isAirBlock(pos)) {
            			if ((world.getBlockState(pos.west()).getBlock() == SaltBlocks.SALTWORT && world.getBlockState(pos.west()).getBlock().getMetaFromState(world.getBlockState(pos.west())) == 4) ||
            				(world.getBlockState(pos.east()).getBlock() == SaltBlocks.SALTWORT && world.getBlockState(pos.east()).getBlock().getMetaFromState(world.getBlockState(pos.east())) == 4) ||
            				(world.getBlockState(pos.north()).getBlock() == SaltBlocks.SALTWORT && world.getBlockState(pos.north()).getBlock().getMetaFromState(world.getBlockState(pos.north())) == 4) ||
            				(world.getBlockState(pos.south()).getBlock() == SaltBlocks.SALTWORT && world.getBlockState(pos.south()).getBlock().getMetaFromState(world.getBlockState(pos.south())) == 4))
            			{
            				if (rand.nextInt(2) == 0) {
            					world.setBlockState(pos, SaltBlocks.SALTWORT.getDefaultState().withProperty(SaltWort.STAGE, SaltWort.EnumType.byMetadata(rand.nextInt(2) + 2)), 3);
            				}
            			}
            		}
            	}
            }
            }
        }
        }
        
        randPosX = randPosX + rand.nextInt(SaltConfig.saltLakeDistance) - SaltConfig.saltLakeDistance/2;
        randPosZ = randPosZ + rand.nextInt(SaltConfig.saltLakeDistance) - SaltConfig.saltLakeDistance/2;
        
		}
		}
		}
	}
}