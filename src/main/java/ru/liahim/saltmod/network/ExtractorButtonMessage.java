package ru.liahim.saltmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.liahim.saltmod.tileEntity.TileEntityExtractor;

public class ExtractorButtonMessage implements IMessage {

	int x, y, z;

	public ExtractorButtonMessage() {}

	public ExtractorButtonMessage(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public static class Handler implements IMessageHandler<ExtractorButtonMessage, IMessage> {

		@Override
		public IMessage onMessage(final ExtractorButtonMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					World world = ctx.getServerHandler().player.world;
					TileEntity te = world.getTileEntity(new BlockPos(message.x, message.y, message.z));
					if (te instanceof TileEntityExtractor) {
						((TileEntityExtractor) te).getTank().setFluid(null);
					}
				}
			});
			return null;
		}
	}
}