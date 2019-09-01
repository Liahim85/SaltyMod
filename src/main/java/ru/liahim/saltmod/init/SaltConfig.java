package ru.liahim.saltmod.init;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.minecraftforge.common.config.Configuration;
import ru.liahim.saltmod.SaltyMod;

public class SaltConfig extends Configuration {

	public static int saltOreFrequency;
	public static int saltOreSize;
	public static int saltLakeGroupRarity;
	public static int saltLakeQuantity;
	public static int saltLakeDistance;
	public static int saltLakeRadius;
	public static int saltLakeMinHeight;
	public static int saltLakeMaxHeight;
	public static int saltCrystalGrowSpeed;
	public static int saltWortGrowSpeed;
	public static boolean fizzyEffect;
	public static boolean mudArmorWaterDam;
	public static int mudRegenSpeed;
	public static int extractorVolume;
	public static Map<Integer, Integer> cloudLevel;
	private String[] loadedCloudLevel;
	public static boolean disableCascadingLog;

	private File file;

	public SaltConfig(File file) {
		super(file);
		this.file = file;
	}

	public void preInit() {
		String[] defaultCloudLevel = { "0=128", "69=192" };
		load();
		//Game
		disableCascadingLog = getBoolean("disableCascadingLog", "game", true, "Disable the message about cascading worldgen lag. Temporary measure until I find a solution to the problem.");
		//SaltyMod
		saltOreFrequency = getInt("SaltOreFrequency", "World", 4, 1, 10, "Salt ore frequency");
		saltOreSize = getInt("SaltOreSize", "World", 5, 1, 10, "Salt ore size");
		saltLakeGroupRarity = getInt("SaltLakeGroupRarity", "World", 500, 1, 1000, "Rarity of the salt lake groups");
		saltLakeQuantity = getInt("SaltLakeQuantity", "World", 5, 1, 10, "The maximum quantity of the salt lakes in the salt lake groups");
		saltLakeDistance = getInt("SaltLakeDistance", "World", 30, 10, 50, "The maximum distance between the salt lakes in the salt lake groups");
		saltLakeRadius = getInt("SaltLakeRadius", "World", 20, 5, 50, "The maximum radius of the salt lake");
		saltLakeMinHeight = getInt("SaltLakeMinAltitude", "World", 60, 0, 256, "The minimum altitude of the salt lake");
		saltLakeMaxHeight = getInt("SaltLakeMaxAltitude", "World", 75, 0, 256, "The maximum altitude of the salt lake");
		saltCrystalGrowSpeed = getInt("SaltCrystalGrowRate", "Farm", 14, 1, 20, "The salt crystals growth rate (1 - fastly, 20 - slowly)");
		saltWortGrowSpeed = getInt("SaltWortGrowRate", "Farm", 7, 1, 20, "The saltwort growth rate (1 - fastly, 20 - slowly)");
		extractorVolume = getInt("EvaporatorVolume", "Evaporator", 1, 1, 3, "The number of buckets in evaporator");
		fizzyEffect = getBoolean("FizzyEffect", "Effects", false, "Do Fizzy Drink removes all effects? (true - all effects, false - milk analogue)");
		mudArmorWaterDam = getBoolean("MudArmorWaterDamage", "Armor", true, "Mud Armor water damage");
		mudRegenSpeed = getInt("MudRegenSpeed", "Effects", 100, 10, 100, "Speed of Mud Armor & Block regeneration effect (10 - fastly, 100 - slowly)");
		loadedCloudLevel = getStringList("DimCloudLevel", "Rainmaker", defaultCloudLevel, "The height of the clouds in a specific dimension (DimensionID=CloudLevel)");
		if (saltLakeMinHeight > saltLakeMaxHeight) {
			int i = saltLakeMinHeight;
			saltLakeMinHeight = saltLakeMaxHeight;
			saltLakeMaxHeight = i;
		}
		save();
	}

    public void init() {}

	public void postInit() {
		cloudLevel = new HashMap<Integer, Integer>();
		Pattern splitpattern = Pattern.compile("=");
		for (int i = 0; i < loadedCloudLevel.length; i++) {
			String s = loadedCloudLevel[i];
			String[] pair = splitpattern.split(s);
			if (pair.length != 2) {
				SaltyMod.logger.warn("Invalid key-value pair at DimCloudLevel[" + i + "]");
				continue;
			}
			int dim;
			int level;
			try {
				dim = Integer.parseInt(pair[0]);
			} catch (NumberFormatException e) {
				SaltyMod.logger.warn("Cannot parse DimensionID \"" + pair[0] + "\" to integer point at DimCloudLevel line " + (i + 1));
				break;
			}
			try {
				level = Integer.parseInt(pair[1]);
			} catch (NumberFormatException e) {
				SaltyMod.logger.warn("Cannot parse CloudLevel \"" + pair[1] + "\" to integer point at DimCloudLevel line " + (i + 1));
				break;
			}
			cloudLevel.put(dim, level);
		}
	}
}