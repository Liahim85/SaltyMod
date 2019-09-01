package ru.liahim.saltmod.inventory.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import ru.liahim.saltmod.inventory.container.ContainerExtractor;
import ru.liahim.saltmod.tileEntity.TileEntityExtractor;

public class GuiExtractorHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile_entity = world.getTileEntity(new BlockPos(x, y, z));
		switch (id) {
		case 0:
			return new ContainerExtractor(player.inventory, (TileEntityExtractor) tile_entity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity tile_entity = world.getTileEntity(new BlockPos(x, y, z));
		switch (id) {
		case 0:
			return new GuiExtractor(player.inventory, (TileEntityExtractor) tile_entity);
		}
		return null;
	}
}