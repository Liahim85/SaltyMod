package ru.liahim.saltmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.liahim.saltmod.api.block.SaltBlocks;

public class SaltWortMessage implements IMessage {

	int x, y, z, i;
	
	public SaltWortMessage() {}

	public SaltWortMessage(int x, int y, int z, int i) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.i = i;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		i = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(i);
	}
	
	public static class Handler implements IMessageHandler<SaltWortMessage, IMessage> {

		@Override
		public IMessage onMessage(final SaltWortMessage message, final MessageContext ctx) {
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					WorldClient world = Minecraft.getMinecraft().world;
					BlockPos pos = new BlockPos(message.x, message.y, message.z);
					int i = message.i;
					if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityFlowerPot) {
						TileEntityFlowerPot te = (TileEntityFlowerPot) world.getTileEntity(pos);
						te.setItemStack(new ItemStack(Item.getItemFromBlock(SaltBlocks.SALTWORT), 1, i));
						te.markDirty();
					}
				}
			});
			return null;
		}
	}
}