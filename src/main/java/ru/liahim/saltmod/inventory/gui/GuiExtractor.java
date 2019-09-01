package ru.liahim.saltmod.inventory.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ru.liahim.saltmod.api.registry.ExtractRegistry;
import ru.liahim.saltmod.common.CommonProxy;
import ru.liahim.saltmod.init.SaltConfig;
import ru.liahim.saltmod.inventory.container.ContainerExtractor;
import ru.liahim.saltmod.network.ExtractorButtonMessage;
import ru.liahim.saltmod.tileEntity.TileEntityExtractor;

@SideOnly(Side.CLIENT)
public class GuiExtractor extends GuiContainer {

    private static final ResourceLocation guiTextures = new ResourceLocation("saltmod:textures/gui/container/extractor.png");
    private final InventoryPlayer playerInventory;
    private IInventory tileExtractor;
    private static final int maxCap = Fluid.BUCKET_VOLUME * SaltConfig.extractorVolume;
    private GuiExtractorButton button;

	public GuiExtractor(InventoryPlayer playerInv, IInventory extractorInv) {
		super(new ContainerExtractor(playerInv, extractorInv));
		this.playerInventory = playerInv;
		this.tileExtractor = extractorInv;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(this.button = new GuiExtractorButton(GuiExtractor.guiTextures, 1, this.guiLeft + 97, this.guiTop + 16));
		this.button.enabled = false;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.button.enabled = this.tileExtractor.getField(4) > 0;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		BlockPos pos = ((TileEntity) this.tileExtractor).getPos();
		CommonProxy.network.sendToServer(new ExtractorButtonMessage(pos.getX(), pos.getY(), pos.getZ()));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = this.tileExtractor.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2 - 10, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
		/*if (this.tileExtractor.getField(4) > 0) {
			s = String.valueOf(this.tileExtractor.getField(4));
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 81 - this.fontRendererObj.getStringWidth(s), this.ySize - 124, 0xFFFFFF);
		}*/
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		if (TileEntityExtractor.isBurning(this.tileExtractor)) {
			int i1 = this.getBurnTimeRemainingScaled(13);
			this.drawTexturedModalRect(k + 71, l + 54 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
		}

		if (this.tileExtractor.getField(4) > 0) {
			int i1 = this.getExtractProgressScaled(17);
			this.drawTexturedModalRect(k + 96, l + 36, 176, 14, i1 + 1, 10);
		}

		if (this.tileExtractor.getField(5) > 0) {
			int i1 = this.tileExtractor.getField(5);
			this.drawTexturedModalRect(k + 59, l + 32 - i1, 176, 40 - i1, 1, i1);
		}

		if (this.getFluidAmountScaled(32) > 0) {
			drawTank(k, l, 62, 17, 32, this.getFluidAmountScaled(32), this.tileExtractor.getField(3));
			this.mc.getTextureManager().bindTexture(guiTextures);
		}
	}

	public int getExtractProgressScaled(int scale) {
		int vol = ExtractRegistry.instance().getExtractFluidVolum(getFromId(this.tileExtractor.getField(3)));
		if (vol == 0) vol = 1000; 
		return this.tileExtractor.getField(2) * scale / vol;
	}

	private Fluid getFromId(int id) {
		for (Entry<Fluid, Integer> entry : FluidRegistry.getRegisteredFluidIDs().entrySet()) {
			if (entry.getValue() == id) return entry.getKey(); 
		}
		return null;
	}

	public int getBurnTimeRemainingScaled(int scale) {
		int time = this.tileExtractor.getField(1);
		if (time == 0) time = 200; 
		return this.tileExtractor.getField(0) * scale / time;
	}

	public int getFluidAmountScaled(int scale) {
		return MathHelper.ceil((float) this.tileExtractor.getField(4) * scale / GuiExtractor.maxCap);
	}

	protected void drawTank(int w, int h, int wp, int hp, int width, int amount, int fluidID) {
		if (fluidID == 0) { return; }
		
		Fluid fluid = getFromId(fluidID);
		int color = fluid.getColor();
		ResourceLocation fluidTexture = fluid.getStill();
		TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(fluidTexture.toString());
		this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		int tWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int tHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		GL11.glColor4f(r, g, b, 1.0f);
		this.drawModalRectWithCustomSizedTexture(w + wp, h + hp + 32 - amount, sprite.getOriginX() * 2, sprite.getOriginY() * 2 + 32 - amount, width, amount, tWidth * 2, tHeight * 2);
		//this.drawTexturedModalRect(w + wp, h + hp + 32 - amount, sprite, width, amount);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		int w = (this.width - this.xSize) / 2;
		int h = (this.height - this.ySize) / 2;

		if ((mouseX >= w + 62) && (mouseY >= h + 17) && (mouseX < w + 62 + 32) && (mouseY < h + 17 + 32)) {
			ArrayList toolTip = new ArrayList();
			if (this.tileExtractor.getField(4) > 0) {
				toolTip.add(new FluidStack(getFromId(this.tileExtractor.getField(3)), this.tileExtractor.getField(4)).getLocalizedName());
			}
			drawText(toolTip, mouseX, mouseY, this.fontRenderer);
		}

		if ((mouseX >= w + 97) && (mouseY >= h + 16) && (mouseX < w + 97 + 3) && (mouseY < h + 16 + 3)) {
			ArrayList toolTip = new ArrayList();
			if (this.tileExtractor.getField(4) > 0) {
				toolTip.add(I18n.format("container.discard"));
			}
			drawText(toolTip, mouseX, mouseY, this.fontRenderer);
		}
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	protected void drawText(List list, int mouseX, int mouseY, FontRenderer font) {
		if (!list.isEmpty()) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				int l = font.getStringWidth(s);
				if (l > k) k = l;
			}

			int i1 = mouseX + 12;
			int j1 = mouseY - 12;
			int k1 = 8;

			if (list.size() > 1) k1 += 2 + (list.size() - 1) * 10;
			if (i1 + k > this.width) i1 -= 28 + k;
			if (j1 + k1 + 6 > this.height) j1 = this.height - k1 - 6;

			this.zLevel = 300.0F;
			itemRender.zLevel = 300.0F;
			int l1 = -267386864;
			this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
			this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

			for (int k2 = 0; k2 < list.size(); ++k2) {
				String s1 = (String) list.get(k2);
				font.drawStringWithShadow(s1, i1, j1, -1);

				if (k2 == 0) j1 += 2;
				j1 += 10;
			}

			this.zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
}