package ru.liahim.saltmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.liahim.saltmod.common.CommonProxy;
import ru.liahim.saltmod.common.SaltTab;
import ru.liahim.saltmod.init.ExternalItems;
import ru.liahim.saltmod.init.ModAdvancements;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;
import ru.liahim.saltmod.init.ModRecipes;
import ru.liahim.saltmod.init.SaltConfig;

@Mod(modid = SaltyMod.MODID, name = SaltyMod.NAME, version = SaltyMod.VERSION, updateJSON = "https://gist.githubusercontent.com/Liahim85/7a650fb7d6b742bf8812b9bb0dadc2b3/raw/salt_updates.json")
public class SaltyMod {

	public static final String MODID = "saltmod";
	public static final String NAME = "Salty Mod";
	public static final String VERSION = "1.12.6.1";
	public static final Logger logger = LogManager.getLogger(NAME);

	public static SaltConfig config;
	public static CreativeTabs saltTab = new SaltTab("salt_tab");

	@Instance(MODID)
	public static SaltyMod instance;

	@SidedProxy(clientSide = "ru.liahim.saltmod.common.ClientProxy", serverSide = "ru.liahim.saltmod.common.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.info("Starting SaltMod PreInitialization");
		config = new SaltConfig(event.getSuggestedConfigurationFile());
		config.preInit();
		ModBlocks.registerBlocks();
		ModItems.registerItems();
		ExternalItems.preInit(event);
		ModRecipes.registerRecipes();
		ModAdvancements.load();
		SaltyMod.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		config.init();
		SaltyMod.proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		config.postInit();
		ExternalItems.postInit();
		SaltyMod.proxy.postInit(event);
	}
}