package ru.liahim.saltmod.common;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.liahim.saltmod.api.block.IColoredBlock;
import ru.liahim.saltmod.api.item.SaltItems;
import ru.liahim.saltmod.entity.EntityRainmaker;
import ru.liahim.saltmod.entity.EntityRainmakerDust;
import ru.liahim.saltmod.entity.render.RenderRainmakerDust;
import ru.liahim.saltmod.init.ClientRegister;

public class ClientProxy extends CommonProxy {

	private static List<Block> blocksToColour = Lists.newArrayList();

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ClientRegister.init();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ClientRegister.registerItemRenderer();
		ClientRegister.registerBlockRenderer();
		registerColouring();
		RenderingRegistry.registerEntityRenderingHandler(EntityRainmaker.class,
				new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), SaltItems.RAINMAKER, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityRainmakerDust.class,
				new RenderRainmakerDust(Minecraft.getMinecraft().getRenderManager()));
	}

	@Override
	public void registerBlockColored(Block block) {
		if (block instanceof IColoredBlock) {
			IColoredBlock coloredBlock = (IColoredBlock) block;
			if (coloredBlock.getBlockColor() != null || coloredBlock.getItemColor() != null) {
				blocksToColour.add(block);
			}
		}
	}

	public void registerColouring() {
		for (Block block : blocksToColour) {
			IColoredBlock colorBlock = (IColoredBlock) block;
			if (colorBlock.getBlockColor() != null)
				Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(colorBlock.getBlockColor(), block);
			if (colorBlock.getItemColor() != null)
				Minecraft.getMinecraft().getItemColors().registerItemColorHandler(colorBlock.getItemColor(), block);
		}
	}
}