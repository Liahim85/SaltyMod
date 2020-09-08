package ru.liahim.saltmod.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import ru.liahim.saltmod.SaltyMod;

public class PacketHandler {

	private static final String PROTOCOL = "1";
	public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(SaltyMod.MODID, "chan"),
			() -> PROTOCOL,
			PROTOCOL::equals,
			PROTOCOL::equals
	);

	public static void init() {
		int id = 0;
		HANDLER.messageBuilder(EvaporatorMessage.class, id++).encoder(EvaporatorMessage::encode).decoder(EvaporatorMessage::new).consumer(EvaporatorMessage.Handler::onMessage).add();
	}
}