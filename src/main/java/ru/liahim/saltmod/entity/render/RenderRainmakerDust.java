package ru.liahim.saltmod.entity.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderRainmakerDust extends Render {

	public RenderRainmakerDust(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float width, float height) {}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {return null;}
}
