/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Renders;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.MinecraftForgeClient;

import Reika.DragonAPI.Instantiable.Effects.Glow;
import Reika.DragonAPI.Interfaces.TileEntity.RenderFetcher;
import Reika.DragonAPI.Libraries.IO.ReikaRenderHelper;
import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;
import Reika.DragonAPI.Libraries.Java.ReikaGLHelper.BlendMode;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.RotaryCraft.Auxiliary.HeatRippleRenderer;
import Reika.RotaryCraft.Auxiliary.IORenderer;
import Reika.RotaryCraft.Auxiliary.Interfaces.AlternatingRedstoneUser;
import Reika.RotaryCraft.Auxiliary.Interfaces.RedstoneUpgradeable;
import Reika.RotaryCraft.Base.RotaryTERenderer;
import Reika.RotaryCraft.Base.TileEntity.RotaryCraftTileEntity;
import Reika.RotaryCraft.Base.TileEntity.TileEntityEngine;
import Reika.RotaryCraft.Models.Engine.ModelAC;
import Reika.RotaryCraft.Models.Engine.ModelCombustion;
import Reika.RotaryCraft.Models.Engine.ModelDC;
import Reika.RotaryCraft.Models.Engine.ModelHydro;
import Reika.RotaryCraft.Models.Engine.ModelJet;
import Reika.RotaryCraft.Models.Engine.ModelMicroTurbine;
import Reika.RotaryCraft.Models.Engine.ModelPerformance;
import Reika.RotaryCraft.Models.Engine.ModelSteam;
import Reika.RotaryCraft.Models.Engine.ModelWind;
import Reika.RotaryCraft.Registry.EngineType;
import Reika.RotaryCraft.TileEntities.Engine.TileEntityHydroEngine;
import Reika.RotaryCraft.TileEntities.Engine.TileEntityJetEngine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSEngine extends RotaryTERenderer
{

	private ModelDC DCModel = new ModelDC();
	private ModelSteam SteamModel = new ModelSteam();
	private ModelCombustion CombModel = new ModelCombustion();
	private ModelAC ACModel = new ModelAC();
	private ModelPerformance PerfModel = new ModelPerformance();
	private ModelMicroTurbine MicroModel = new ModelMicroTurbine();
	private ModelJet JetModel = new ModelJet();
	private ModelHydro HydroModel = new ModelHydro();
	private ModelWind WindModel = new ModelWind();

	private static final Glow jetGlow = new Glow(255, 150, 20, 192).setScale(0.4);

	/**
	 * Renders the TileEntity for the position.
	 */
	public void renderTileEntityEngineAt(TileEntityEngine tile, double par2, double par4, double par6, float par8)
	{
		int var9;

		if (!tile.isInWorld())
			var9 = 0;
		else
			var9 = tile.getBlockMetadata();

		ModelDC var14 = DCModel;
		ModelSteam var15 = SteamModel;
		ModelCombustion var16 = CombModel;
		ModelAC var17 = ACModel;
		ModelPerformance var18 = PerfModel;
		ModelMicroTurbine var19 = MicroModel;
		ModelJet var20 = JetModel;
		ModelHydro var21 = HydroModel;
		ModelWind var22 = WindModel;

		switch(tile.getEngineType()) {
			case DC:
				this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/dc.png");
				break;
			case WIND:
				this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/windtex.png");
				break;
			case STEAM:
				this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/steamtex.png");
				break;
			case GAS:
				this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/combtex.png");
				break;
			case AC:
				this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/actex.png");
				break;
			case SPORT:
				this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/perftex.png");
				break;
			case HYDRO:
				TileEntityHydroEngine eng = (TileEntityHydroEngine)tile;
				String sg = "/Reika/RotaryCraft/Textures/TileEntityTex/"+(eng.isBedrock() ? "bedhydrotex.png" : "hydrotex.png");
				this.bindTextureByName(sg);
				break;
			case MICRO:
				this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/microtex.png");
				break;
			case JET:
				String s = ((TileEntityJetEngine)tile).canAfterBurn() ? "_b": "";
				this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/jettex"+s+".png");
				break;
		}

		this.setupGL(tile, par2, par4, par6);

		int var11 = 0;	 //used to rotate the model about metadata

		if (tile.isInWorld()) {
			switch(tile.getBlockMetadata()) {
				case 0:
					var11 = 0;
					break;
				case 1:
					var11 = 180;
					break;
				case 2:
					var11 = 90;
					break;
				case 3:
					var11 = 270;
					break;
			}

			if (tile.getEngineType().isJetFueled())
				var11 += 90;

			GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);

			this.prepareShader(tile);
		}
		else {
			double s; double d;
			//ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", this.itemMetadata));
			GL11.glRotatef(-90, 0.0F, 1.0F, 0.0F);
			switch(tile.getEngineType()) {
				case DC:
					this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/dc.png");
					var14.renderAll(tile, null);
					break;
				case WIND:
					GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
					s = 0.7;
					d = 0.375;
					GL11.glScaled(s, s, s);
					double d2 = 0.2;
					GL11.glTranslated(0, d, 0);
					GL11.glTranslated(d2, 0, 0);
					this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/windtex.png");
					var22.renderAll(tile, null);
					GL11.glTranslated(0, -d, 0);
					GL11.glTranslated(-d2, 0, 0);
					GL11.glScaled(1D/s, 1D/s, 1D/s);
					GL11.glRotatef(-90, 0.0F, 1.0F, 0.0F);
					break;
				case STEAM:
					this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/steamtex.png");
					var15.renderAll(tile, null);
					break;
				case GAS:
					this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/combtex.png");
					var16.renderAll(tile, null);
					break;
				case AC:
					this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/actex.png");
					var17.renderAll(tile, null);
					break;
				case SPORT:
					this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/perftex.png");
					var18.renderAll(tile, null, Float.MIN_NORMAL, 0);
					break;
				case HYDRO:
					TileEntityHydroEngine eng = (TileEntityHydroEngine)tile;
					String sg = "/Reika/RotaryCraft/Textures/TileEntityTex/"+(eng.isBedrock() ? "bedhydrotex.png" : "hydrotex.png");
					this.bindTextureByName(sg);
					s = 0.7;
					d = 0.375;
					GL11.glTranslated(0, d, 0);
					GL11.glScaled(s, s, s);
					var21.renderAll(tile, ReikaJavaLibrary.makeListFrom(eng.failed, eng.isBedrock()), 0, 0);
					GL11.glScaled(1D/s, 1D/s, 1D/s);
					GL11.glTranslated(0, -d, 0);
					break;
				case MICRO:
					this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/microtex.png");
					GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
					var19.renderAll(tile, null);
					break;
				case JET:
					this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/jettex.png");
					GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
					var20.renderAll(tile, null);
					break;
			}

			this.closeGL(tile);
			return;
		}

		switch (tile.getEngineType()) {
			case DC:
				var14.renderAll(tile, null, -tile.phi);
				break;
			case WIND:
				GL11.glRotatef(-90, 0.0F, 1.0F, 0.0F);
				var22.renderAll(tile, null, -tile.phi);
				GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
				break;
			case STEAM:
				var15.renderAll(tile, null, -tile.phi);
				break;
			case GAS:
				var16.renderAll(tile, null, -tile.phi);
				break;
			case AC:
				var17.renderAll(tile, null, -tile.phi);
				break;
			case SPORT:
				var18.renderAll(tile, null, -tile.phi);
				break;
			case HYDRO:
				TileEntityHydroEngine eng = (TileEntityHydroEngine)tile;
				var21.renderAll(tile, ReikaJavaLibrary.makeListFrom(eng.failed, eng.isBedrock()), -tile.phi, 0);
				break;
			case MICRO:
				var19.renderAll(tile, null, -tile.phi);
				break;
			case JET:
				var20.renderAll(tile, null, -tile.phi);
				break;
		}

		this.closeGL(tile);
	}

	private void prepareShader(TileEntityEngine tile) {
		if (tile.getEngineType() == EngineType.JET) {
			TileEntityJetEngine te = (TileEntityJetEngine)tile;
			double dx = 0.625*tile.getWriteDirection().offsetX;
			double dz = 0.625*tile.getWriteDirection().offsetZ;
			EntityPlayer ep = Minecraft.getMinecraft().thePlayer;
			GL11.glPushMatrix();
			GL11.glTranslated(0, 1, -0.625);
			double dist = ep.getDistance(tile.xCoord+0.5+dx, tile.yCoord+0.5, tile.zCoord+0.5+dz);
			float f = 0;
			if (te.omega > 0) {
				f = Math.max(f, (float)Math.sqrt(te.omega*0.5F/EngineType.JET.getSpeed()));
			}
			if (te.temperature > 100) {
				f = Math.max(f, Math.min(1, (te.temperature-100F)/(te.getMaxExhaustTemperature()-100F)));
			}
			double dd = 0.25;
			float fac = te.isAfterburning() ? 1 : 0.75F;
			for (double d = 0; d <= 3; d += dd) {
				dx += dd*tile.getWriteDirection().offsetX;
				dz += dd*tile.getWriteDirection().offsetZ;
				HeatRippleRenderer.instance.addHeatRippleEffectIfLOS(tile, tile.xCoord+0.5+dx, tile.yCoord+0.5, tile.zCoord+0.5+dz, ep, dist, f, fac, 1, 1);
				GL11.glTranslated(0, 0, -dd);
				fac *= te.isAfterburning() ? 0.875 : 0.825;
				if (fac <= 0.01)
					break;
			}
			GL11.glPopMatrix();
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8)
	{
		if (this.doRenderModel((RotaryCraftTileEntity)tile))
			this.renderTileEntityEngineAt((TileEntityEngine)tile, par2, par4, par6, par8);
		if (tile instanceof RedstoneUpgradeable) {
			if (((RotaryCraftTileEntity)tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 0) {
				this.renderRedstoneFrame((TileEntityEngine)tile, par2, par4, par6, par8);
			}
		}
		if (((RotaryCraftTileEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
			IORenderer.renderIO(tile, par2, par4, par6);/*
			TileEntityEngine eng = (TileEntityEngine)tile;
			if (eng.type == EngineType.JET && eng.power > 0)
				this.renderGlow(tile, par2, par4, par6);
			 */
			//TileEntityEngine eng = (TileEntityEngine)tile;
			//eng.power = 1;
			//if (eng.getEngineType() == EngineType.JET && eng.power > 0) {
			//	jetGlow.setPosition(tile.xCoord+0.5, tile.yCoord+0.5, tile.zCoord+0.5);
			//	jetGlow.render();
			//}
		}
	}

	private void renderRedstoneFrame(TileEntityEngine tile, double par2, double par4, double par6, float par8) {
		RedstoneUpgradeable ar = (RedstoneUpgradeable)tile;
		if (!ar.hasRedstoneUpgrade())
			return;
		boolean bright = true;
		if (tile instanceof AlternatingRedstoneUser)
			bright = (tile.getTicksExisted()/3)%2 == 0;
		int c = bright ? 0xff0000 : 0x900000;
		int c2 = bright ? 0xffa7a7 : 0xda0000;
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glPushMatrix();
		GL11.glTranslated(par2, par4, par6);
		if (bright)
			GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		double o = 0.005;
		double t = 0.05;
		double p = 0.125;
		double h = tile.isFlipped ? 1-p-o : p+o+t;
		double h2 = tile.isFlipped ? 1-p-o-t : p+o;
		double w = 0.475;

		ReikaTextureHelper.bindTerrainTexture();
		IIcon ico = Blocks.redstone_block.blockIcon;
		float u = ico.getMinU();
		float v = ico.getMinV();
		float du = ico.getMaxU();
		float dv = ico.getMaxV();

		Tessellator v5 = Tessellator.instance;
		v5.startDrawingQuads();
		v5.setBrightness(240);
		v5.setColorRGBA_I(c, 255/*240*/);
		if (tile.isFlipped) {
			v5.addVertexWithUV(0.5-w, h2, 0.5-w, u, dv);
			v5.addVertexWithUV(0.5+w, h2, 0.5-w, du, dv);
			v5.addVertexWithUV(0.5+w, h2, 0.5+w, du, v);
			v5.addVertexWithUV(0.5-w, h2, 0.5+w, u, v);
		}
		else {
			v5.addVertexWithUV(0.5-w, h, 0.5+w, u, v);
			v5.addVertexWithUV(0.5+w, h, 0.5+w, du, v);
			v5.addVertexWithUV(0.5+w, h, 0.5-w, du, dv);
			v5.addVertexWithUV(0.5-w, h, 0.5-w, u, dv);
		}

		v5.addVertexWithUV(0.5+w, h2, 0.5-w, u, v);
		v5.addVertexWithUV(0.5-w, h2, 0.5-w, du, v);
		v5.addVertexWithUV(0.5-w, h, 0.5-w, du, dv);
		v5.addVertexWithUV(0.5+w, h, 0.5-w, u, dv);

		v5.addVertexWithUV(0.5+w, h, 0.5+w, u, v);
		v5.addVertexWithUV(0.5-w, h, 0.5+w, du, v);
		v5.addVertexWithUV(0.5-w, h2, 0.5+w, du, dv);
		v5.addVertexWithUV(0.5+w, h2, 0.5+w, u, dv);

		v5.addVertexWithUV(0.5-w, h, 0.5+w, u, v);
		v5.addVertexWithUV(0.5-w, h, 0.5-w, du, v);
		v5.addVertexWithUV(0.5-w, h2, 0.5-w, du, dv);
		v5.addVertexWithUV(0.5-w, h2, 0.5+w, u, dv);

		v5.addVertexWithUV(0.5+w, h2, 0.5+w, u, v);
		v5.addVertexWithUV(0.5+w, h2, 0.5-w, du, v);
		v5.addVertexWithUV(0.5+w, h, 0.5-w, du, dv);
		v5.addVertexWithUV(0.5+w, h, 0.5+w, u, dv);
		v5.draw();

		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

	private void renderGlow(TileEntity tile, double par2, double par4, double par6) {
		Tessellator v5 = Tessellator.instance;
		GL11.glTranslated(par2, par4, par6);
		int meta = tile.getBlockMetadata();
		double x = 0;
		double z = 0;
		boolean side = meta < 2;
		int r = 255;
		int g = 200;
		int b = 20;
		int a = 32;

		x = 0.125;
		z = 0.125;

		double s = 0.125;

		ReikaRenderHelper.prepareGeoDraw(a < 255);
		double d = -0.5*s*2;
		BlendMode.PREALPHA.apply();
		for (float i = 0; i < 360; i += 22.5F) {
			GL11.glTranslated(0.5, 0.5, 0);
			GL11.glRotated(i, 0, 0, 1);
			GL11.glTranslated(0, d, 0);
			GL11.glScaled(s, s, s);
			v5.startDrawingQuads();
			v5.setColorRGBA(r, g, b, a);
			if (side) {
				v5.addVertex(x, 0, 0);
				v5.addVertex(x, 0, 1);
				v5.addVertex(x, 1, 1);
				v5.addVertex(x, 1, 0);
			}
			else {
				v5.addVertex(0, 0, z);
				v5.addVertex(1, 0, z);
				v5.addVertex(1, 1, z);
				v5.addVertex(0, 1, z);
			}
			v5.draw();
			GL11.glScaled(1D/s, 1D/s, 1D/s);
			GL11.glTranslated(0, -d, 0);
			GL11.glRotated(-i, 0, 0, 1);
			GL11.glTranslated(-0.5, -0.5, 0);
			GL11.glTranslated(0, 0, 0.01);
		}
		BlendMode.DEFAULT.apply();
		ReikaRenderHelper.exitGeoDraw();
		GL11.glTranslated(-par2, -par4, -par6);
	}

	@Override
	public String getImageFileName(RenderFetcher te) {
		if (te == null)
			return null;
		TileEntityEngine tile = (TileEntityEngine)te;
		switch(tile.getEngineType()) {
			case DC:
				return "dc.png";
			case WIND:
				return "windtex.png";
			case STEAM:
				return "steamtex.png";
			case GAS:
				return "combtex.png";
			case AC:
				return "actex.png";
			case SPORT:
				return "perftex.png";
			case HYDRO:
				return "hydrotex.png";
			case MICRO:
				return "microtex.png";
			case JET:
				return "jettex.png";
		}
		return null;
	}
}
