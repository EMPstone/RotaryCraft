/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Renders;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import Reika.DragonAPI.Interfaces.RenderFetcher;
import Reika.RotaryCraft.Auxiliary.EnchantmentRenderer;
import Reika.RotaryCraft.Auxiliary.IORenderer;
import Reika.RotaryCraft.Base.RotaryCraftTileEntity;
import Reika.RotaryCraft.Base.RotaryTERenderer;
import Reika.RotaryCraft.Models.ModelWoodcutter;
import Reika.RotaryCraft.TileEntities.Farming.TileEntityWoodcutter;

public class RenderWoodcutter extends RotaryTERenderer
{

	private ModelWoodcutter WoodcutterModel = new ModelWoodcutter();
	//private ModelWoodcutterV WoodcutterModelV = new ModelWoodcutterV();

	/**
	 * Renders the TileEntity for the position.
	 */
	public void renderTileEntityWoodcutterAt(TileEntityWoodcutter tile, double par2, double par4, double par6, float par8)
	{
		int var9;

		if (!tile.isInWorld())
			var9 = 0;
		else
			var9 = tile.getBlockMetadata();

		ModelWoodcutter var14;
		var14 = WoodcutterModel;
		//ModelWoodcutterV var15;
		//var14 = this.WoodcutterModelV;
		this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/woodcuttertex.png");

		this.setupGL(tile, par2, par4, par6);

		int var11 = 0;	 //used to rotate the model about metadata

		if (tile.isInWorld()) {
			switch(tile.getBlockMetadata()) {
			case 0:
				var11 = 180;
				break;
			case 1:
				var11 = 0;
				break;
			case 2:
				var11 = 270;
				break;
			case 3:
				var11 = 90;
				break;
			}

			GL11.glRotatef((float)var11-90, 0.0F, 1.0F, 0.0F);

		}
		float var13;

		var14.renderAll(null, tile.phi);

		this.closeGL(tile);
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8)
	{
		if (this.isValidMachineRenderpass((RotaryCraftTileEntity)tile))
			this.renderTileEntityWoodcutterAt((TileEntityWoodcutter)tile, par2, par4, par6, par8);
		if (((RotaryCraftTileEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
			IORenderer.renderIO(tile, par2, par4, par6);
			if (((TileEntityWoodcutter)tile).hasEnchantments())
				EnchantmentRenderer.renderShine(0, 0, 0, par2, par4, par6);
		}
	}

	@Override
	public String getImageFileName(RenderFetcher te) {
		return "woodcuttertex.png";
	}
}
