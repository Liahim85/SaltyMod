package ru.liahim.saltmod.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import ru.liahim.saltmod.entity.RainmakerEntity;

public class RainmakerRenderer extends EntityRenderer<RainmakerEntity> {

	private final ItemRenderer itemRenderer;

	public RainmakerRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.itemRenderer = Minecraft.getInstance().getItemRenderer();
	}

	@Override
	public void render(RainmakerEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLight) {
		matrixStack.push();
		matrixStack.rotate(this.renderManager.getCameraOrientation());
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
		if (entity.shotAtAngle()) {
			matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F));
			matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
			matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
		}
		this.itemRenderer.renderItem(entity.getItem(), ItemCameraTransforms.TransformType.GROUND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
		matrixStack.pop();
		super.render(entity, entityYaw, partialTicks, matrixStack, bufferIn, packedLight);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getEntityTexture(RainmakerEntity entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}