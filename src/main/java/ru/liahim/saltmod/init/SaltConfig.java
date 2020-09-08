package ru.liahim.saltmod.init;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import ru.liahim.saltmod.SaltyMod;

@Mod.EventBusSubscriber(modid = SaltyMod.MODID)
public class SaltConfig {

	public static Common COMMON_CONFIG;

	public static class Common {

		public Common(ForgeConfigSpec.Builder builder) {

			builder.comment("World Generation Settings.").push("Generation");
			{
				Generation.saltOreFrequency = builder
						.comment("Salt ore frequency").defineInRange("SaltOreFrequency", 4,
						1, 10);
				Generation.saltOreSize = builder.comment("Salt ore size").defineInRange("SaltOreSize", 5, 1, 10);
				Generation.saltLakeGroupRarity = builder.comment("Rarity of the salt lake groups")
						.defineInRange("SaltLakeGroupRarity", 500, 1, 1000);
				Generation.saltLakeQuantity = builder
						.comment("The maximum quantity of the salt lakes in the salt lake groups")
						.defineInRange("SaltLakeQuantity", 5, 1, 10);
				Generation.saltLakeDistance = builder
						.comment("The maximum distance between the salt lakes in the salt lake groups")
						.defineInRange("SaltLakeDistance", 30, 10, 50);
				Generation.saltLakeRadius = builder.comment("The maximum radius of the salt lake")
						.defineInRange("SaltLakeRadius", 20, 5, 50);
				Generation.saltLakeMinHeight = builder.comment("The minimum altitude of the salt lake")
						.defineInRange("SaltLakeMinAltitude", 60, 0, 256);
				Generation.saltLakeMaxHeight = builder.comment("The maximum altitude of the salt lake")
						.defineInRange("SaltLakeMaxAltitude", 75, 0, 256);
				Generation.loadedSaltLakeBiomes = builder
						.comment("List of biomes for salt lakes generating")
						.worldRestart()
						.defineList("SaltLakeBiomes", Arrays.asList(
								"minecraft:plains",
								"minecraft:mountains",
								"minecraft:forest",
								"minecraft:taiga",
								"minecraft:wooded_hills",
								"minecraft:taiga_hills",
								"minecraft:mountain_edge",
								"minecraft:jungle",
								"minecraft:jungle_hills",
								"minecraft:jungle_edge",
								"minecraft:stone_shore",
								"minecraft:birch_forest",
								"minecraft:birch_forest_hills",
								"minecraft:dark_forest",
								"minecraft:giant_tree_taiga",
								"minecraft:giant_tree_taiga_hills",
								"minecraft:wooded_mountains",
								"minecraft:savanna",
								"minecraft:savanna_plateau",
								"minecraft:badlands",
								"minecraft:wooded_badlands_plateau",
								"minecraft:badlands_plateau",
								"minecraft:sunflower_plains",
								"minecraft:gravelly_mountains",
								"minecraft:flower_forest",
								"minecraft:taiga_mountains",
								"minecraft:modified_jungle",
								"minecraft:modified_jungle_edge",
								"minecraft:tall_birch_forest",
								"minecraft:tall_birch_hills",
								"minecraft:dark_forest_hills",
								"minecraft:giant_spruce_taiga",
								"minecraft:giant_spruce_taiga_hills",
								"minecraft:modified_gravelly_mountains",
								"minecraft:shattered_savanna",
								"minecraft:shattered_savanna_plateau",
								"minecraft:eroded_badlands",
								"minecraft:modified_wooded_badlands_plateau",
								"minecraft:modified_badlands_plateau",
								"minecraft:bamboo_jungle",
								"minecraft:bamboo_jungle_hills"),
								s -> s instanceof String);

			}
			builder.pop();
			builder.comment("Game Settings.").push("Game");
			{
				Game.saltCrystalGrowSpeed = builder.comment("The salt crystals growth rate (1 - fastly, 20 - slowly)")
						.defineInRange("SaltCrystalGrowRate", 14, 1, 20);
				Game.saltWortGrowSpeed = builder.comment("The saltwort growth rate (1 - fastly, 20 - slowly)")
						.defineInRange("SaltWortGrowRate", 7, 1, 20);
				Game.extractorVolume = builder.comment("The number of buckets in evaporator")
						.defineInRange("EvaporatorVolume", 1, 1, 3);
				Game.fizzyEffect = builder
						.comment("Do Fizzy Drink removes all effects? (true - all effects, false - milk analogue)")
						.define("FizzyEffect", false);
				Game.mudArmorWaterDam = builder.comment("Mud Armor water damage").define("MudArmorWaterDamage", true);
				Game.mudRegenSpeed = builder
						.comment("Speed of Mud Armor & Block regeneration effect (10 - fastly, 100 - slowly)")
						.defineInRange("MudRegenSpeed", 100, 10, 100);
				Game.loadedCloudLevel = builder
						.comment("The height of the clouds in a specific dimension (DimensionType=CloudLevel)")
						.defineList("DimCloudLevel", Arrays.asList("minecraft:overworld=128", "mist:misty_world=192"),
								s -> s instanceof String);
			}
			builder.pop();
		}
	}

	public Generation GENERATION = new Generation();
	public Game GAME = new Game();

	public static class Generation {

		public static ForgeConfigSpec.IntValue saltOreFrequency;
		public static ForgeConfigSpec.IntValue saltOreSize;
		public static ForgeConfigSpec.IntValue saltLakeGroupRarity;
		public static ForgeConfigSpec.IntValue saltLakeQuantity;
		public static ForgeConfigSpec.IntValue saltLakeDistance;
		public static ForgeConfigSpec.IntValue saltLakeRadius;
		public static ForgeConfigSpec.IntValue saltLakeMinHeight;
		public static ForgeConfigSpec.IntValue saltLakeMaxHeight;
		private static ForgeConfigSpec.ConfigValue<List<? extends String>> loadedSaltLakeBiomes;
		public static List<Biome> saltLakeBiomes;
	}

	public static class Game {

		public static ForgeConfigSpec.IntValue saltCrystalGrowSpeed;
		public static ForgeConfigSpec.IntValue saltWortGrowSpeed;
		public static ForgeConfigSpec.BooleanValue fizzyEffect;
		public static ForgeConfigSpec.BooleanValue mudArmorWaterDam;
		public static ForgeConfigSpec.IntValue mudRegenSpeed;
		public static ForgeConfigSpec.IntValue extractorVolume;
		private static ForgeConfigSpec.ConfigValue<List<? extends String>> loadedCloudLevel;
		public static Map<Integer, Integer> cloudLevel;
	}

	@SubscribeEvent
	public static void onConfigChanged(ModConfig.Reloading event) {
		if (event.getConfig().getModId().equals(SaltyMod.MODID)) {
			updateConfig();
		}
	}

	public static void updateConfig() {
		addSaltLakeBiomes();
		calculateClouds();
		if (Generation.saltLakeMinHeight.get() > Generation.saltLakeMaxHeight.get()) {
			ForgeConfigSpec.IntValue i = Generation.saltLakeMinHeight;
			Generation.saltLakeMinHeight = Generation.saltLakeMaxHeight;
			Generation.saltLakeMaxHeight = i;
		}
	}

	private static void addSaltLakeBiomes() {
		Generation.saltLakeBiomes = Lists.newArrayList();
		for (int i = 0; i < Generation.loadedSaltLakeBiomes.get().size(); i++) {
			String s = Generation.loadedSaltLakeBiomes.get().get(i);
			Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(s));
			if (biome == null) {
				SaltyMod.LOGGER.warn("Invalid key '" + s + "' at SaltLakeBiomes[" + i + "]");
				continue;
			}
			Generation.saltLakeBiomes.add(biome);
		}
	}

	private static void calculateClouds() {
		Game.cloudLevel = new HashMap<Integer, Integer>();
		Pattern splitpattern = Pattern.compile("=");
		for (int i = 0; i < Game.loadedCloudLevel.get().size(); i++) {
			String s = Game.loadedCloudLevel.get().get(i);
			String[] pair = splitpattern.split(s);
			if (pair.length != 2) {
				SaltyMod.LOGGER.warn("Invalid key-value pair at DimCloudLevel[" + i + "]");
				continue;
			}
			int dim;
			int level;
			try {
				dim = Integer.parseInt(pair[0]);
			} catch (NumberFormatException e) {
				SaltyMod.LOGGER.warn("Cannot parse DimensionID \"" + pair[0]
						+ "\" to integer point at DimCloudLevel line " + (i + 1));
				break;
			}
			try {
				level = Integer.parseInt(pair[1]);
			} catch (NumberFormatException e) {
				SaltyMod.LOGGER.warn("Cannot parse CloudLevel \"" + pair[1]
						+ "\" to integer point at DimCloudLevel line " + (i + 1));
				break;
			}
			Game.cloudLevel.put(dim, level);
		}
	}
}