package ru.liahim.saltmod.init;

import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.liahim.saltmod.SaltyMod;
import ru.liahim.saltmod.client.renderer.RainmakerDustRenderer;
import ru.liahim.saltmod.client.renderer.RainmakerRenderer;
import ru.liahim.saltmod.entity.RainmakerEntity;
import ru.liahim.saltmod.entity.RainmakerDustEntity;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SaltyMod.MODID);

	public static final RegistryObject<EntityType<RainmakerEntity>> RAINMAKER = register("rainmaker", EntityType.Builder.<RainmakerEntity>create(RainmakerEntity::new, EntityClassification.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(4).setUpdateInterval(10).size(0.25F, 0.25F));
	public static final RegistryObject<EntityType<RainmakerDustEntity>> RAINMAKER_DUST = register("rainmaker_dust", EntityType.Builder.<RainmakerDustEntity>create(RainmakerDustEntity::new, EntityClassification.MISC).size(0.25F, 0.25F));

	private static <E extends Entity> RegistryObject<EntityType<E>> register(String id, EntityType.Builder<E> builder) {
		return ENTITIES.register(id, () -> builder.build(new ResourceLocation(SaltyMod.MODID, id).toString()));
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerEntityRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(RAINMAKER.get(), RainmakerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(RAINMAKER_DUST.get(), RainmakerDustRenderer::new);
	}
}