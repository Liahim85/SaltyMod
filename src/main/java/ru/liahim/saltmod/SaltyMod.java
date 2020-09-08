package ru.liahim.saltmod;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import ru.liahim.saltmod.block.IColored;
import ru.liahim.saltmod.block.INonItem;
import ru.liahim.saltmod.handler.*;
import ru.liahim.saltmod.init.*;
import ru.liahim.saltmod.proxy.*;
import ru.liahim.saltmod.network.PacketHandler;

@Mod(SaltyMod.MODID)
@EventBusSubscriber(modid = SaltyMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class SaltyMod {

	public static final String MODID = "saltmod";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

	public SaltyMod() {
		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::commonSetup);
		bus.register(new ClientEventHandler());
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::onServerAboutToStart);
		MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
		{
			final Pair<SaltConfig.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(SaltConfig.Common::new);
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.getRight());
			SaltConfig.COMMON_CONFIG = specPair.getLeft();
		}
		ModBlocks.BLOCKS.register(bus);
		ModRecipes.RECIPES.register(bus);
		ModTileEntities.TILE_ENTITIES.register(bus);
		ModContainers.CONTAINERS.register(bus);
		ModFeatures.FEATURES.register(bus);
		ModEntities.ENTITIES.register(bus);
	}

	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		LOGGER.debug("Registered BlockItems");
		ModBlocks.BLOCKS.getEntries().stream()
				.map(RegistryObject::get)
				.filter(block -> !(block instanceof INonItem))
				.forEach(block -> {
					final Item.Properties prop = new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP);
					final BlockItem blockItem = new BlockItem(block, prop);
					blockItem.setRegistryName(block.getRegistryName());
					registry.register(blockItem);
					if (block instanceof IColored) SaltyMod.proxy.addToColorSet(blockItem);
				});
		LOGGER.debug("Registered Items");
		ModItems.MOD_ITEMS.forEach(item -> {
				registry.register(item);
				if (item instanceof IColored) SaltyMod.proxy.addToColorSet(item);
		});
		LOGGER.debug("Registered Salty Food");
		SaltyFoodRegister.Instance.register(registry);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		ModAdvancements.init();
		PacketHandler.init();
		SaltConfig.updateConfig();
		ModFeatures.addWorldgen();
	}

	@SuppressWarnings("deprecation")
	private void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
		if (resourceManager instanceof IReloadableResourceManager) {
			((IReloadableResourceManager)resourceManager).addReloadListener((IResourceManagerReloadListener) resourceManagerReload -> {
				RecipeManager recipeManager = event.getServer().getRecipeManager();
				Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> map = Maps.newHashMap(ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, recipeManager, "field_199522_d"));
				for (IRecipeType<?> type : map.keySet()) {
	                map.put(type, Maps.newHashMap(map.get(type)));
	            }
				SaltyFoodRegister.recipes.forEach(recipe -> {
					map.computeIfAbsent(recipe.getType(), i -> Maps.newHashMap()).put(recipe.getId(), recipe);
				});
				ObfuscationReflectionHelper.setPrivateValue(RecipeManager.class, recipeManager, ImmutableMap.copyOf(map), "field_199522_d");
			});
		}
	}
}