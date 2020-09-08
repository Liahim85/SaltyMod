package ru.liahim.saltmod.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.block.*;

public class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SaltyMod.MODID);

	public static final RegistryObject<Block> SALT_ORE = BLOCKS.register("salt_ore", () -> new SaltSource(true, false));
	public static final RegistryObject<Block> SALT_LAKE = BLOCKS.register("salt_lake", () -> new SaltSource(true, true));
	public static final RegistryObject<Block> SALT_BLOCK = BLOCKS.register("salt_block", () -> new SaltBlock());
	public static final RegistryObject<Block> SALT_BLOCK_CHISELED = BLOCKS.register("salt_block_chiseled", () -> new SaltBlock());
	public static final RegistryObject<Block> SALT_BLOCK_PILLAR = BLOCKS.register("salt_block_pillar", () -> new SaltPillar());
	public static final RegistryObject<Block> SALT_BRICK = BLOCKS.register("salt_brick", () -> new SaltBlock());
	public static final RegistryObject<Block> SALT_BLOCK_CRACKED = BLOCKS.register("salt_block_cracked", () -> new SaltBlock());
	public static final RegistryObject<Block> SALT_BRICK_CRACKED = BLOCKS.register("salt_brick_cracked", () -> new SaltBlock());
	public static final RegistryObject<Block> SALT_BRICK_CHISELED = BLOCKS.register("salt_brick_chiseled", () -> new SaltBlock());
	public static final RegistryObject<Block> SALT_CHAPITER = BLOCKS.register("salt_chapiter", () -> new SaltBlock());
	public static final RegistryObject<Block> SALT_BRICK_STAIRS = BLOCKS.register("salt_brick_stairs", () -> new SaltBrickStair(SALT_BRICK.get().getDefaultState()));
	public static final RegistryObject<Block> SALT_SLAB_BLOCK = BLOCKS.register("salt_slab_block", () -> new SaltSlab());
	public static final RegistryObject<Block> SALT_SLAB_BRICK = BLOCKS.register("salt_slab_brick", () -> new SaltSlab());
	public static final RegistryObject<Block> SALT_SLAB_PILLAR = BLOCKS.register("salt_slab_pillar", () -> new SaltSlab());
	public static final RegistryObject<Block> SALT_LAMP = BLOCKS.register("salt_lamp", () -> new SaltLamp());
	public static final RegistryObject<Block> SALT_DIRT_LAKE = BLOCKS.register("salt_dirt_lake", () -> new SaltSource(false, true));
	public static final RegistryObject<Block> SALT_DIRT = BLOCKS.register("salt_dirt", () -> new SaltDirt());
	public static final RegistryObject<Block> SALT_DIRT_LITE = BLOCKS.register("salt_dirt_lite", () -> new SaltDirtLite());
	public static final RegistryObject<Block> SALT_GRASS = BLOCKS.register("salt_grass", () -> new SaltGrass());
	public static final RegistryObject<Block> MUD_BLOCK = BLOCKS.register("mud_block", () -> new MudBlock());
	public static final RegistryObject<Block> EVAPORATOR = BLOCKS.register("evaporator", () -> new Evaporator());
	public static final RegistryObject<Block> SALT_CRYSTAL = BLOCKS.register("salt_crystal", () -> new SaltCrystal());
	public static final RegistryObject<Block> SALTWORT = BLOCKS.register("saltwort", () -> new SaltWort());
	public static final RegistryObject<Block> POTTED_SALTWORT = BLOCKS.register("potted_saltwort", () -> new SaltPot());
}