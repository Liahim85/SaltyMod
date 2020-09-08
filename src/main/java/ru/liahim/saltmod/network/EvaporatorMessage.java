package ru.liahim.saltmod.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fml.network.NetworkEvent;
import ru.liahim.saltmod.tileentity.EvaporatorTileEntity;

public class EvaporatorMessage {

	int x, y, z;

	public EvaporatorMessage() {}

	public EvaporatorMessage(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public EvaporatorMessage (PacketBuffer buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public static class Handler {

		public static boolean onMessage(EvaporatorMessage message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					World world = ctx.get().getSender().world;
					TileEntity te = world.getTileEntity(new BlockPos(message.x, message.y, message.z));
					if (te instanceof EvaporatorTileEntity) {
						((EvaporatorTileEntity)te).drain(EvaporatorTileEntity.maxCap, FluidAction.EXECUTE);
					}
				}
			});
			return true;
		}
	}
}