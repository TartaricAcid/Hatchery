package com.gendeathrow.hatchery.block.shredder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.generator.DigesterGeneratorTileEntity;
import com.gendeathrow.hatchery.core.init.ModFluids;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiShredder extends GuiContainer
{

	private static final ResourceLocation GUI_GENERATOR_INVENTORY = new ResourceLocation(Hatchery.MODID, "textures/gui/shredder_gui.png");
	ShredderTileEntity SHREDDER;
	
	public GuiShredder(InventoryPlayer inventory, ShredderTileEntity tile) 
	{
		super(new ContainerShredder(inventory, tile, Minecraft.getMinecraft().thePlayer));
		xSize = 176;
		ySize = 166;
		this.SHREDDER = tile;
	}
	
	int previousRFLevel = 0;
	int rfEnergyLevels = 0;

	List<String> hover =  new ArrayList<String>();
	DecimalFormat formatter = new DecimalFormat("#,###");	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) 
	{
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;

		previousRFLevel = rfEnergyLevels;
		
		rfEnergyLevels = (int) (((float)SHREDDER.energy.getEnergyStored() / SHREDDER.energy.getMaxEnergyStored()) * 58);
		
		
		int rfOut = previousRFLevel - rfEnergyLevels > 0 ? previousRFLevel - rfEnergyLevels: 0;
		


		fontRendererObj.drawString(new TextComponentTranslation("container.upgrades").getFormattedText(), xSize - 40, 34, 4210752);

		
		GlStateManager.pushMatrix();
		GlStateManager.translate(-this.guiLeft, -this.guiTop, 0);
		
		hover.clear();
		
		if(x > xOffSet+16 && x < xOffSet+28 && y > yOffSet+13 && y < yOffSet+70)
		{
			hover.add(formatter.format((int)SHREDDER.energy.getEnergyStored()) +"/ "+ formatter.format(this.SHREDDER.energy.getMaxEnergyStored())+" RF");
		}


		if(hover.size() > 0)
			this.drawHoveringText(hover, x, y);
		
		GlStateManager.translate(this.guiLeft, this.guiTop, 0);
		GlStateManager.popMatrix();
	}  
	
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,int mouseX, int mouseY) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		mc.getTextureManager().bindTexture(GUI_GENERATOR_INVENTORY);

		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		
		drawTexturedModalRect(guiLeft + 15, guiTop + 13 + 58 - rfEnergyLevels, 220, 58 - rfEnergyLevels, 13, rfEnergyLevels);
	}

}
