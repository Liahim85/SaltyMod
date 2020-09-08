package ru.liahim.saltmod.init;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.world.features.SaltLakeFeature;
import ru.liahim.saltmod.world.features.SaltOreFeature;

public class ModFeatures {

	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SaltyMod.MODID);

	public static final RegistryObject<Feature<OreFeatureConfig>> SALT_ORE = FEATURES.register("ore", () -> new SaltOreFeature(OreFeatureConfig::deserialize));
	public static final RegistryObject<Feature<NoFeatureConfig>> SALT_LAKE = FEATURES.register("lake", () -> new SaltLakeFeature(NoFeatureConfig::deserialize));

	public static void addWorldgen() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (BiomeDictionary.getTypes(biome).stream().anyMatch(type -> type == BiomeDictionary.Type.OVERWORLD)) {
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
						SALT_ORE.get().withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.SALT_ORE.get().getDefaultState(), SaltConfig.Generation.saltOreSize.get()))
						.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(SaltConfig.Generation.saltOreFrequency.get(), 0, 0, 96))));
			}
		}
		for (Biome biome : SaltConfig.Generation.saltLakeBiomes) {
			if (biome != null) biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, SALT_LAKE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		}
	}
}