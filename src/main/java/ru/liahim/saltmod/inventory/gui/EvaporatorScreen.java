package ru.liahim.saltmod.inventory.gui;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import ru.liahim.saltmod.inventory.container.EvaporatorContainer;
import ru.liahim.saltmod.network.EvaporatorMessage;
import ru.liahim.saltmod.network.PacketHandler;

@OnlyIn(Dist.CLIENT)
public class EvaporatorScreen extends ContainerScreen<EvaporatorContainer> {

	private static final ResourceLocation guiTextures = new ResourceLocation("saltmod:textures/gui/container/extractor.png");
	private final PlayerInventory playerInventory;
	private ImageButton button;

	public EvaporatorScreen(EvaporatorContainer container, PlayerInventory inv, ITextComponent title) {
		super(container, inv, title);
		this.playerInventory = inv;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(this.button = new ImageButton(this.guiLeft + 97, this.guiTop + 16, 3, 3, 190, 3, -3, guiTextures, (button) -> {
			PacketHandler.HANDLER.sendToServer(new EvaporatorMessage(this.container.getPosX(), this.container.getPosY(), this.container.getPosZ()));
		}));
		this.button.active = this.button.visible = false;
	}

	@Override
	public void tick() {
		super.tick();
		this.button.active = this.button.visible = this.container.getFluidAmount() > 0;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		if (this.container.getFluidAmount() > 0) {
			if ((mouseX >= this.guiLeft + 62) && (mouseY >= this.guiTop + 17) && (mouseX < this.guiLeft + 62 + 32) && (mouseY < this.guiTop + 17 + 32)) {
				@SuppressWarnings("deprecation")
				List<String> toolTip = Lists.newArrayList(new FluidStack(Registry.FLUID.getByValue(this.container.getFluidId()), this.container.getFluidAmount()).getDisplayName().getFormattedText());
				this.renderTooltip(toolTip, mouseX, mouseY);
			}
			if ((mouseX >= this.guiLeft + 97) && (mouseY >= this.guiTop + 16) && (mouseX < this.guiLeft + 97 + 3) && (mouseY < this.guiTop + 16 + 3)) {
				List<String> toolTip = Lists.newArrayList(new TranslationTextComponent("container.saltmod.discard").getFormattedText());
				this.renderTooltip(toolTip, mouseX, mouseY);
			}
		}
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = this.title.getFormattedText();
		this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2 - 10, 6, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1, 1, 1, 1);
		this.minecraft.getTextureManager().bindTexture(guiTextures);
		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		int value;
		if (this.container.isBurning()) {
			value = this.container.getBurningProgressScaled(13);
			this.blit(this.guiLeft + 71, this.guiTop + 54 + 12 - value, 176, 12 - value, 14, value + 1);
		}
		value = this.container.getExtractingProgressScaled(17);
		if (value > 0) this.blit(this.guiLeft + 96, this.guiTop + 36, 176, 14, value + 1, 10);
		value = this.container.getPressure();
		if (value > 0) this.blit(this.guiLeft + 59, this.guiTop + 32 - value, 176, 40 - value, 1, value);
		value = this.container.getFluidAmountScaled(32);
		if (value > 0) {
			drawTank(this.guiLeft + 62, this.guiTop + 17, 32, value, this.container.getFluidId());
			this.minecraft.getTextureManager().bindTexture(guiTextures);
		}
	}

	@SuppressWarnings("deprecation")
	protected void drawTank(int x, int y, int width, int amount, int fluidID) {
		Fluid fluid = Registry.FLUID.getByValue(fluidID);
		int color = fluid.getAttributes().getColor();
		TextureAtlasSprite sprite = this.minecraft.getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(fluid.getAttributes().getStillTexture());
		this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		RenderSystem.color4f(r, g, b, 1);
		y += 32;
		innerBlit(x, x + width, y - amount, y, 0, sprite.getMinU(), sprite.getMaxU(), sprite.getInterpolatedV(((float)32 - amount)/2), sprite.getMaxV());
		RenderSystem.color4f(1, 1, 1, 1);
	}
}