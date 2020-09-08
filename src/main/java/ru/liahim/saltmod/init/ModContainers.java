package ru.liahim.saltmod.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.inventory.container.EvaporatorContainer;
import ru.liahim.saltmod.inventory.gui.EvaporatorScreen;

public class ModContainers {
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, SaltyMod.MODID);

	public static final RegistryObject<ContainerType<EvaporatorContainer>> EVAPORATOR = CONTAINERS.register("evaporator", () -> new ContainerType<>(EvaporatorContainer::new));

	@OnlyIn(Dist.CLIENT)
	public static void renderScreens() {
		ScreenManager.registerFactory(EVAPORATOR.get(), EvaporatorScreen::new);
	}
}