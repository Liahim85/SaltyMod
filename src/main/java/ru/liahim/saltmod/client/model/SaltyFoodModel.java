package ru.liahim.saltmod.client.model;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.data.IModelData;
import ru.liahim.saltmod.SaltyMod;

public class SaltyFoodModel<T extends IBakedModel> extends BakedModelWrapper<T> {

	public static final ResourceLocation[] SALT = new ResourceLocation[] {
			new ResourceLocation(SaltyMod.MODID, "item/salt_0"),
			new ResourceLocation(SaltyMod.MODID, "item/salt_1"),
			new ResourceLocation(SaltyMod.MODID, "item/salt_2")
	};

	protected final ImmutableList<BakedQuad> quads;

	@SuppressWarnings("deprecation")
	public SaltyFoodModel(T base, TextureAtlasSprite sprite) {
		super(base);
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		builder.addAll(base.getQuads(null, null, new Random()));
		builder.addAll(ItemLayerModel.getQuadsForSprite(1, sprite, TransformationMatrix.identity()));
		this.quads = builder.build();
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
		return this.quads;
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
		return this.quads;
	}

	@Override
	public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
		return ForgeHooksClient.handlePerspective(getBakedModel(), cameraTransformType, mat);
	}

	public static ResourceLocation getSaltTexture(int index) {
		return SALT[index < SALT.length ? index : 0];
	}
}