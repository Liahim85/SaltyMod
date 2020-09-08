package ru.liahim.saltmod.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import ru.liahim.saltmod.entity.RainmakerDustEntity;

public class RainmakerDustRenderer extends EntityRenderer<RainmakerDustEntity> {

	public RainmakerDustRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(RainmakerDustEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLight) {

	}

	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getEntityTexture(RainmakerDustEntity entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}