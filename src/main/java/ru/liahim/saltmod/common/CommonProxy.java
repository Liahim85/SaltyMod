package ru.liahim.saltmod.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.dispenser.DispenserBehaviorRainmaiker;
import ru.liahim.saltmod.dispenser.DispenserBehaviorSaltPinch;
import ru.liahim.saltmod.entity.EntityRainmaker;
import ru.liahim.saltmod.entity.EntityRainmakerDust;
import ru.liahim.saltmod.inventory.gui.GuiExtractorHandler;
import ru.liahim.saltmod.network.ExtractorButtonMessage;
import ru.liahim.saltmod.network.SaltModEvent;
import ru.liahim.saltmod.network.SaltWortMessage;
import ru.liahim.saltmod.tileEntity.TileEntityExtractor;
import ru.liahim.saltmod.world.SaltCrystalGenerator;
import ru.liahim.saltmod.world.SaltLakeGenerator;
import ru.liahim.saltmod.world.SaltOreGenerator;

public class CommonProxy {

	public static SimpleNetworkWrapper network;

	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new SaltModEvent());
		NetworkRegistry.INSTANCE.registerGuiHandler(SaltyMod.instance, new GuiExtractorHandler());
		network = NetworkRegistry.INSTANCE.newSimpleChannel(SaltyMod.MODID);
		network.registerMessage(ExtractorButtonMessage.Handler.class, ExtractorButtonMessage.class, 0, Side.SERVER);
		network.registerMessage(SaltWortMessage.Handler.class, SaltWortMessage.class, 1, Side.CLIENT);
	}

	public void init(FMLInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntityExtractor.class, "tileEntityExtractor");
		EntityRegistry.registerModEntity(new ResourceLocation(SaltyMod.MODID, "entity_rainmaker"), EntityRainmaker.class, "entity_rainmaker", 0, SaltyMod.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(SaltyMod.MODID, "entity_rainmaker_dust"), EntityRainmakerDust.class, "entity_rainmaker_dust", 1, SaltyMod.instance, 64, 20, false);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(SaltItems.RAINMAKER, new DispenserBehaviorRainmaiker());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(SaltItems.SALT_PINCH, new DispenserBehaviorSaltPinch());

		GameRegistry.registerWorldGenerator(new SaltOreGenerator(), 0);
		GameRegistry.registerWorldGenerator(new SaltCrystalGenerator(), 10);
		GameRegistry.registerWorldGenerator(new SaltLakeGenerator(), 15);
	}

	public void postInit(FMLPostInitializationEvent event) {}
	public void registerBlockColored(Block block) {}
}