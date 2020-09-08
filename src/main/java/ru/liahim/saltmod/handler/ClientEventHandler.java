package ru.liahim.saltmod.handler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.block.IColored;
import ru.liahim.saltmod.client.model.SaltyFoodModel;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModContainers;
import ru.liahim.saltmod.init.ModEntities;
import ru.liahim.saltmod.proxy.ClientProxy;

@Mod.EventBusSubscriber(modid = SaltyMod.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(ModBlocks.SALT_GRASS.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ModBlocks.SALT_CRYSTAL.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ModBlocks.SALTWORT.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ModBlocks.POTTED_SALTWORT.get(), RenderType.getCutoutMipped());
		ModEntities.registerEntityRenderer();
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void blockColorSetup(ColorHandlerEvent.Block event) {
		ModBlocks.BLOCKS.getEntries().stream()
		.map(RegistryObject::get)
		.filter(block -> block instanceof IColored)
		.map(block -> (IColored)block)
		.filter(color -> color.getBlockColor() != null)
		.forEach(color -> event.getBlockColors().register(color.getBlockColor(), (Block)color));
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void itemColorSetup(ColorHandlerEvent.Item event) {
		ClientProxy.colorSet.forEach(item -> {
			IItemColor color = null;
			if (item instanceof IColored) color = ((IColored)item).getItemColor();
			else if (item instanceof BlockItem) {
				Block block = ((BlockItem)item).getBlock();
				if (block instanceof IColored) color = ((IColored)block).getItemColor();
			}
			if (color != null) event.getItemColors().register(color, item);
		});
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void clientSetup(FMLClientSetupEvent evt) {
		ModContainers.renderScreens();
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void textureRegister(TextureStitchEvent.Pre event) {
		for (ResourceLocation res : SaltyFoodModel.SALT) event.addSprite(res);
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		ClientProxy.saltyFoods.forEach((resIn, pair) -> {
			IBakedModel base = event.getModelRegistry().get(new ModelResourceLocation(resIn, "inventory"));
			ResourceLocation res = new ResourceLocation(SaltyMod.MODID, pair.getFirst() + resIn.getPath());
			@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
			SaltyFoodModel model = new SaltyFoodModel(base, Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(SaltyFoodModel.getSaltTexture(pair.getSecond())));
			event.getModelRegistry().put(new ModelResourceLocation(res, "inventory"), model);
		});
	}
}