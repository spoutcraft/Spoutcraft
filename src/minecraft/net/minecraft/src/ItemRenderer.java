package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EnumAction;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MapData;
import net.minecraft.src.MapItemRenderer;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.RenderPlayer;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
//Spout HD Start
import net.minecraft.client.Minecraft;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.CustomTextureManager;
import org.getspout.spout.item.SpoutCustomBlockDesign;
import org.getspout.spout.item.SpoutItemBlock;
import org.newdawn.slick.opengl.Texture;
import com.pclewis.mcpatcher.mod.TileSize;
//Spout end
public class ItemRenderer {

	private Minecraft mc;
	private ItemStack itemToRender = null;
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;
	private RenderBlocks renderBlocksInstance = new RenderBlocks();
	private MapItemRenderer mapItemRenderer;
	private int field_20099_f = -1;


	public ItemRenderer(Minecraft var1) {
		this.mc = var1;
		this.mapItemRenderer = new MapItemRenderer(var1.fontRenderer, var1.gameSettings, var1.renderEngine);
	}

	public void renderItem(EntityLiving var1, ItemStack var2) {
		GL11.glPushMatrix();
		//Spout Start
		String customTexture = SpoutClient.getInstance().getItemManager().getCustomItemTexture(var2.itemID, (short) var2.getItemDamage());
		String customTexturePlugin = SpoutClient.getInstance().getItemManager().getCustomItemTexturePlugin(var2.itemID, (short) var2.getItemDamage());
		SpoutCustomBlockDesign blockType = SpoutItemBlock.getCustomBlockDesign(var2.itemID, var2.getItemDamage());
		Texture customTextureObject = null;
		if (customTexture != null) {
			customTextureObject = CustomTextureManager.getTextureFromUrl(customTexturePlugin, customTexture);
		}
		
		if (blockType != null) {
			GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, CustomTextureManager.getTextureFromUrl(blockType.getTexturePlugin(), blockType.getTexureURL()).getTextureID());			
		} else if (customTextureObject != null) {
			GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, customTextureObject.getTextureID());
		} else if(var2.itemID < 256) {
			GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/terrain.png"));
		} else {
			GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/gui/items.png"));
		}
		//Spout End

		if(blockType != null || (customTextureObject == null && var2.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var2.itemID].getRenderType()))) {
			//Spout Start
			if (blockType != null) {
				blockType.renderBlockOnInventory(this.renderBlocksInstance, var1.getEntityBrightness(1.0F));
			} else {
				this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[var2.itemID], var2.getItemDamage(), var1.getEntityBrightness(1.0F));
			}
			//Spout End

		} else {

			Tessellator var3 = Tessellator.instance;
			int var4 = var1.getItemIcon(var2);
			//Spout HD Start
			float var5 = ((float)(var4 % 16 * TileSize.int_size) + 0.0F) / TileSize.float_size16;
			float var6 = ((float)(var4 % 16 * TileSize.int_size) + TileSize.float_sizeMinus0_01) / TileSize.float_size16;
			float var7 = ((float)(var4 / 16 * TileSize.int_size) + 0.0F) / TileSize.float_size16;
			float var8 = ((float)(var4 / 16 * TileSize.int_size) + TileSize.float_sizeMinus0_01) / TileSize.float_size16;
			//Spout HD End
			//Spout Start
			if(customTextureObject != null){
				var5 = 0;
				var6 = 1;
				var7 = 1;
				var8 = 0;
			}
			//Spout End
			float var9 = 1.0F;
			float var10 = 0.0F;
			float var11 = 0.3F;
			GL11.glEnable('\u803a');
			GL11.glTranslatef(-var10, -var11, 0.0F);
			float var12 = 1.5F;
			GL11.glScalef(var12, var12, var12);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
			float var13 = 0.0625F;
			var3.startDrawingQuads();
			var3.setNormal(0.0F, 0.0F, 1.0F);
			var3.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)var6, (double)var8);
			var3.addVertexWithUV((double)var9, 0.0D, 0.0D, (double)var5, (double)var8);
			var3.addVertexWithUV((double)var9, 1.0D, 0.0D, (double)var5, (double)var7);
			var3.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)var6, (double)var7);
			var3.draw();
			var3.startDrawingQuads();
			var3.setNormal(0.0F, 0.0F, -1.0F);
			var3.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - var13), (double)var6, (double)var7);
			var3.addVertexWithUV((double)var9, 1.0D, (double)(0.0F - var13), (double)var5, (double)var7);
			var3.addVertexWithUV((double)var9, 0.0D, (double)(0.0F - var13), (double)var5, (double)var8);
			var3.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - var13), (double)var6, (double)var8);
			var3.draw();
			var3.startDrawingQuads();
			var3.setNormal(-1.0F, 0.0F, 0.0F);

			int var14;
			float var15;
			float var17;
			float var16;
			//Spout HD Start
			for(var14 = 0; var14 < TileSize.int_size; ++var14) {
				var15 = (float)var14 / TileSize.float_size;
				var16 = var6 + (var5 - var6) * var15 - TileSize.float_texNudge;
				//Spout HD End
				var17 = var9 * var15;
				var3.addVertexWithUV((double)var17, 0.0D, (double)(0.0F - var13), (double)var16, (double)var8);
				var3.addVertexWithUV((double)var17, 0.0D, 0.0D, (double)var16, (double)var8);
				var3.addVertexWithUV((double)var17, 1.0D, 0.0D, (double)var16, (double)var7);
				var3.addVertexWithUV((double)var17, 1.0D, (double)(0.0F - var13), (double)var16, (double)var7);
			}

			var3.draw();
			var3.startDrawingQuads();
			var3.setNormal(1.0F, 0.0F, 0.0F);
			//Spout HD Start
			for(var14 = 0; var14 < TileSize.int_size; ++var14) {
				var15 = (float)var14 / TileSize.float_size;
				var16 = var6 + (var5 - var6) * var15 - TileSize.float_texNudge;
				var17 = var9 * var15 + TileSize.float_reciprocal;
				//Spout HD End
				var3.addVertexWithUV((double)var17, 1.0D, (double)(0.0F - var13), (double)var16, (double)var7);
				var3.addVertexWithUV((double)var17, 1.0D, 0.0D, (double)var16, (double)var7);
				var3.addVertexWithUV((double)var17, 0.0D, 0.0D, (double)var16, (double)var8);
				var3.addVertexWithUV((double)var17, 0.0D, (double)(0.0F - var13), (double)var16, (double)var8);
			}

			var3.draw();
			var3.startDrawingQuads();
			var3.setNormal(0.0F, 1.0F, 0.0F);
			//Spout HD Start
			for(var14 = 0; var14 < TileSize.int_size; ++var14) {
				var15 = (float)var14 / TileSize.float_size;
				var16 = var8 + (var7 - var8) * var15 - TileSize.float_texNudge;
				var17 = var9 * var15 + TileSize.float_reciprocal;
				//Spout HD End
				var3.addVertexWithUV(0.0D, (double)var17, 0.0D, (double)var6, (double)var16);
				var3.addVertexWithUV((double)var9, (double)var17, 0.0D, (double)var5, (double)var16);
				var3.addVertexWithUV((double)var9, (double)var17, (double)(0.0F - var13), (double)var5, (double)var16);
				var3.addVertexWithUV(0.0D, (double)var17, (double)(0.0F - var13), (double)var6, (double)var16);
			}

			var3.draw();
			var3.startDrawingQuads();
			var3.setNormal(0.0F, -1.0F, 0.0F);
			//Spout HD Start
			for(var14 = 0; var14 < TileSize.int_size; ++var14) {
				var15 = (float)var14 / TileSize.float_size;
				var16 = var8 + (var7 - var8) * var15 - TileSize.float_texNudge;
				//Spout HD End
				var17 = var9 * var15;
				var3.addVertexWithUV((double)var9, (double)var17, 0.0D, (double)var5, (double)var16);
				var3.addVertexWithUV(0.0D, (double)var17, 0.0D, (double)var6, (double)var16);
				var3.addVertexWithUV(0.0D, (double)var17, (double)(0.0F - var13), (double)var6, (double)var16);
				var3.addVertexWithUV((double)var9, (double)var17, (double)(0.0F - var13), (double)var5, (double)var16);
			}

			var3.draw();
			GL11.glDisable('\u803a');
		}

		GL11.glPopMatrix();
	}

	public void renderItemInFirstPerson(float var1) {
		float var2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * var1;
		EntityPlayerSP var3 = this.mc.thePlayer;
		float var4 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var1;
		GL11.glPushMatrix();
		GL11.glRotatef(var4, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		float var6;
		float var7;
		if(var3 instanceof EntityPlayerSP) {
			EntityPlayerSP var5 = (EntityPlayerSP)var3;
			var6 = var5.field_35225_ar + (var5.field_35223_ap - var5.field_35225_ar) * var1;
			var7 = var5.field_35226_aq + (var5.field_35222_e - var5.field_35226_aq) * var1;
			GL11.glRotatef((var3.rotationPitch - var6) * 0.1F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef((var3.rotationYaw - var7) * 0.1F, 0.0F, 1.0F, 0.0F);
		}

		ItemStack var14 = this.itemToRender;
		var6 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ));
		var6 = 1.0F;
		int var15 = this.mc.theWorld.func_35451_b(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ), 0);
		int var8 = var15 % 65536;
		int var9 = var15 / 65536;
		GL13.glMultiTexCoord2f('\u84c1', (float)var8 / 1.0F, (float)var9 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var10;
		float var17;
		float var18;
		if(var14 != null) {
			var15 = Item.itemsList[var14.itemID].getColorFromDamage(var14.getItemDamage());
			var17 = (float)(var15 >> 16 & 255) / 255.0F;
			var18 = (float)(var15 >> 8 & 255) / 255.0F;
			var10 = (float)(var15 & 255) / 255.0F;
			GL11.glColor4f(var6 * var17, var6 * var18, var6 * var10, 1.0F);
		} else {
			GL11.glColor4f(var6, var6, var6, 1.0F);
		}

		float var11;
		float var13;
		if(var14 != null && var14.itemID == Item.map.shiftedIndex) {
			GL11.glPushMatrix();
			var7 = 0.8F;
			var17 = var3.getSwingProgress(var1);
			var18 = MathHelper.sin(var17 * 3.1415927F);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var17) * 3.1415927F);
			GL11.glTranslatef(-var10 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var17) * 3.1415927F * 2.0F) * 0.2F, -var18 * 0.2F);
			var17 = 1.0F - var4 / 45.0F + 0.1F;
			if(var17 < 0.0F) {
				var17 = 0.0F;
			}

			if(var17 > 1.0F) {
				var17 = 1.0F;
			}

			var17 = -MathHelper.cos(var17 * 3.1415927F) * 0.5F + 0.5F;
			GL11.glTranslatef(0.0F, 0.0F * var7 - (1.0F - var2) * 1.2F - var17 * 0.5F + 0.04F, -0.9F * var7);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(var17 * -85.0F, 0.0F, 0.0F, 1.0F);
			GL11.glEnable('\u803a');
			GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getEntityTexture()));

			for(var9 = 0; var9 < 2; ++var9) {
				int var26 = var9 * 2 - 1;
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.0F, -0.6F, 1.1F * (float)var26);
				GL11.glRotatef((float)(-45 * var26), 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef((float)(-65 * var26), 0.0F, 1.0F, 0.0F);
				Render var21 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
				RenderPlayer var24 = (RenderPlayer)var21;
				var13 = 1.0F;
				GL11.glScalef(var13, var13, var13);
				var24.drawFirstPersonHand();
				GL11.glPopMatrix();
			}

			var18 = var3.getSwingProgress(var1);
			var10 = MathHelper.sin(var18 * var18 * 3.1415927F);
			var11 = MathHelper.sin(MathHelper.sqrt_float(var18) * 3.1415927F);
			GL11.glRotatef(-var10 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var11 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var11 * 80.0F, 1.0F, 0.0F, 0.0F);
			var18 = 0.38F;
			GL11.glScalef(var18, var18, var18);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
			var10 = 0.015625F;
			GL11.glScalef(var10, var10, var10);
			this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/misc/mapbg.png"));
			Tessellator var27 = Tessellator.instance;
			GL11.glNormal3f(0.0F, 0.0F, -1.0F);
			var27.startDrawingQuads();
			byte var25 = 7;
			var27.addVertexWithUV((double)(0 - var25), (double)(128 + var25), 0.0D, 0.0D, 1.0D);
			var27.addVertexWithUV((double)(128 + var25), (double)(128 + var25), 0.0D, 1.0D, 1.0D);
			var27.addVertexWithUV((double)(128 + var25), (double)(0 - var25), 0.0D, 1.0D, 0.0D);
			var27.addVertexWithUV((double)(0 - var25), (double)(0 - var25), 0.0D, 0.0D, 0.0D);
			var27.draw();
			MapData var23 = Item.map.func_28012_a(var14, this.mc.theWorld);
			this.mapItemRenderer.func_28157_a(this.mc.thePlayer, this.mc.renderEngine, var23);
			GL11.glPopMatrix();
		} else if(var14 != null) {
			GL11.glPushMatrix();
			var7 = 0.8F;
			float var12;
			if(var3.func_35205_Y() > 0) {
				EnumAction var16 = var14.func_35865_n();
				if(var16 == EnumAction.eat) {
					var18 = (float)var3.func_35205_Y() - var1 + 1.0F;
					var10 = 1.0F - var18 / (float)var14.func_35866_m();
					var12 = 1.0F - var10;
					var12 = var12 * var12 * var12;
					var12 = var12 * var12 * var12;
					var12 = var12 * var12 * var12;
					var13 = 1.0F - var12;
					GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos(var18 / 4.0F * 3.1415927F) * 0.1F) * (float)((double)var10 > 0.2D?1:0), 0.0F);
					GL11.glTranslatef(var13 * 0.6F, -var13 * 0.5F, 0.0F);
					GL11.glRotatef(var13 * 90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(var13 * 10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(var13 * 30.0F, 0.0F, 0.0F, 1.0F);
				}
			} else {
				var17 = var3.getSwingProgress(var1);
				var18 = MathHelper.sin(var17 * 3.1415927F);
				var10 = MathHelper.sin(MathHelper.sqrt_float(var17) * 3.1415927F);
				GL11.glTranslatef(-var10 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var17) * 3.1415927F * 2.0F) * 0.2F, -var18 * 0.2F);
			}

			GL11.glTranslatef(0.7F * var7, -0.65F * var7 - (1.0F - var2) * 0.6F, -0.9F * var7);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable('\u803a');
			var17 = var3.getSwingProgress(var1);
			var18 = MathHelper.sin(var17 * var17 * 3.1415927F);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var17) * 3.1415927F);
			GL11.glRotatef(-var18 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var10 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var10 * 80.0F, 1.0F, 0.0F, 0.0F);
			var17 = 0.4F;
			GL11.glScalef(var17, var17, var17);
			if(var3.func_35205_Y() > 0) {
				EnumAction var20 = var14.func_35865_n();
				if(var20 == EnumAction.block) {
					GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
					GL11.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
				} else if(var20 == EnumAction.bow) {
					GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
					GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
					var10 = (float)var14.func_35866_m() - ((float)var3.func_35205_Y() - var1 + 1.0F);
					var11 = var10 / 20.0F;
					var11 = (var11 * var11 + var11 * 2.0F) / 3.0F;
					if(var11 > 1.0F) {
						var11 = 1.0F;
					}

					if(var11 > 0.1F) {
						GL11.glTranslatef(0.0F, MathHelper.sin((var10 - 0.1F) * 1.3F) * 0.01F * (var11 - 0.1F), 0.0F);
					}

					GL11.glTranslatef(0.0F, 0.0F, var11 * 0.1F);
					GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glTranslatef(0.0F, 0.5F, 0.0F);
					var12 = 1.0F + var11 * 0.2F;
					GL11.glScalef(1.0F, 1.0F, var12);
					GL11.glTranslatef(0.0F, -0.5F, 0.0F);
					GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				}
			}

			if(var14.getItem().shouldRotateAroundWhenRendering()) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			this.renderItem(var3, var14);
			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			var7 = 0.8F;
			var17 = var3.getSwingProgress(var1);
			var18 = MathHelper.sin(var17 * 3.1415927F);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var17) * 3.1415927F);
			GL11.glTranslatef(-var10 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(var17) * 3.1415927F * 2.0F) * 0.4F, -var18 * 0.4F);
			GL11.glTranslatef(0.8F * var7, -0.75F * var7 - (1.0F - var2) * 0.6F, -0.9F * var7);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable('\u803a');
			var17 = var3.getSwingProgress(var1);
			var18 = MathHelper.sin(var17 * var17 * 3.1415927F);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var17) * 3.1415927F);
			GL11.glRotatef(var10 * 70.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var18 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getEntityTexture()));
			GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(5.6F, 0.0F, 0.0F);
			Render var19 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
			RenderPlayer var22 = (RenderPlayer)var19;
			var10 = 1.0F;
			GL11.glScalef(var10, var10, var10);
			var22.drawFirstPersonHand();
			GL11.glPopMatrix();
		}

		GL11.glDisable('\u803a');
		RenderHelper.disableStandardItemLighting();
	}

	public void renderOverlays(float var1) {
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		int var2;
		if(this.mc.thePlayer.isBurning()) {
			var2 = this.mc.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var2);
			this.renderFireInFirstPerson(var1);
		}

		if(this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
			var2 = MathHelper.floor_double(this.mc.thePlayer.posX);
			int var3 = MathHelper.floor_double(this.mc.thePlayer.posY);
			int var4 = MathHelper.floor_double(this.mc.thePlayer.posZ);
			int var5 = this.mc.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var5);
			int var6 = this.mc.theWorld.getBlockId(var2, var3, var4);
			if(this.mc.theWorld.isBlockNormalCube(var2, var3, var4)) {
				this.renderInsideOfBlock(var1, Block.blocksList[var6].getBlockTextureFromSide(2));
			} else {
				for(int var7 = 0; var7 < 8; ++var7) {
					float var8 = ((float)((var7 >> 0) % 2) - 0.5F) * this.mc.thePlayer.width * 0.9F;
					float var9 = ((float)((var7 >> 1) % 2) - 0.5F) * this.mc.thePlayer.height * 0.2F;
					float var10 = ((float)((var7 >> 2) % 2) - 0.5F) * this.mc.thePlayer.width * 0.9F;
					int var11 = MathHelper.floor_float((float)var2 + var8);
					int var12 = MathHelper.floor_float((float)var3 + var9);
					int var13 = MathHelper.floor_float((float)var4 + var10);
					if(this.mc.theWorld.isBlockNormalCube(var11, var12, var13)) {
						var6 = this.mc.theWorld.getBlockId(var11, var12, var13);
					}
				}
			}

			if(Block.blocksList[var6] != null) {
				this.renderInsideOfBlock(var1, Block.blocksList[var6].getBlockTextureFromSide(2));
			}
		}

		if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
			var2 = this.mc.renderEngine.getTexture("/misc/water.png");
			GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var2);
			this.renderWarpedTextureOverlay(var1);
		}

		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
	}

	private void renderInsideOfBlock(float var1, int var2) {
		Tessellator var3 = Tessellator.instance;
		this.mc.thePlayer.getEntityBrightness(var1);
		float var4 = 0.1F;
		GL11.glColor4f(var4, var4, var4, 0.5F);
		GL11.glPushMatrix();
		float var5 = -1.0F;
		float var6 = 1.0F;
		float var7 = -1.0F;
		float var8 = 1.0F;
		float var9 = -0.5F;
		float var10 = 0.0078125F;
		float var11 = (float)(var2 % 16) / 256.0F - var10;
		float var12 = ((float)(var2 % 16) + 15.99F) / 256.0F + var10;
		float var13 = (float)(var2 / 16) / 256.0F - var10;
		float var14 = ((float)(var2 / 16) + 15.99F) / 256.0F + var10;
		var3.startDrawingQuads();
		var3.addVertexWithUV((double)var5, (double)var7, (double)var9, (double)var12, (double)var14);
		var3.addVertexWithUV((double)var6, (double)var7, (double)var9, (double)var11, (double)var14);
		var3.addVertexWithUV((double)var6, (double)var8, (double)var9, (double)var11, (double)var13);
		var3.addVertexWithUV((double)var5, (double)var8, (double)var9, (double)var12, (double)var13);
		var3.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderWarpedTextureOverlay(float var1) {
		Tessellator var2 = Tessellator.instance;
		float var3 = this.mc.thePlayer.getEntityBrightness(var1);
		GL11.glColor4f(var3, var3, var3, 0.5F);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(770, 771);
		GL11.glPushMatrix();
		float var4 = 4.0F;
		float var5 = -1.0F;
		float var6 = 1.0F;
		float var7 = -1.0F;
		float var8 = 1.0F;
		float var9 = -0.5F;
		float var10 = -this.mc.thePlayer.rotationYaw / 64.0F;
		float var11 = this.mc.thePlayer.rotationPitch / 64.0F;
		var2.startDrawingQuads();
		var2.addVertexWithUV((double)var5, (double)var7, (double)var9, (double)(var4 + var10), (double)(var4 + var11));
		var2.addVertexWithUV((double)var6, (double)var7, (double)var9, (double)(0.0F + var10), (double)(var4 + var11));
		var2.addVertexWithUV((double)var6, (double)var8, (double)var9, (double)(0.0F + var10), (double)(0.0F + var11));
		var2.addVertexWithUV((double)var5, (double)var8, (double)var9, (double)(var4 + var10), (double)(0.0F + var11));
		var2.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(3042 /*GL_BLEND*/);
	}

	private void renderFireInFirstPerson(float var1) {
		Tessellator var2 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(770, 771);
		float var3 = 1.0F;

		for(int var4 = 0; var4 < 2; ++var4) {
			GL11.glPushMatrix();
			int var5 = Block.fire.blockIndexInTexture + var4 * 16;
			int var6 = (var5 & 15) << 4;
			int var7 = var5 & 240;
			float var8 = (float)var6 / 256.0F;
			float var9 = ((float)var6 + 15.99F) / 256.0F;
			float var10 = (float)var7 / 256.0F;
			float var11 = ((float)var7 + 15.99F) / 256.0F;
			float var12 = (0.0F - var3) / 2.0F;
			float var13 = var12 + var3;
			float var14 = 0.0F - var3 / 2.0F;
			float var15 = var14 + var3;
			float var16 = -0.5F;
			GL11.glTranslatef((float)(-(var4 * 2 - 1)) * 0.24F, -0.3F, 0.0F);
			GL11.glRotatef((float)(var4 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
			var2.startDrawingQuads();
			var2.addVertexWithUV((double)var12, (double)var14, (double)var16, (double)var9, (double)var11);
			var2.addVertexWithUV((double)var13, (double)var14, (double)var16, (double)var8, (double)var11);
			var2.addVertexWithUV((double)var13, (double)var15, (double)var16, (double)var8, (double)var10);
			var2.addVertexWithUV((double)var12, (double)var15, (double)var16, (double)var9, (double)var10);
			var2.draw();
			GL11.glPopMatrix();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(3042 /*GL_BLEND*/);
	}

	public void updateEquippedItem() {
		this.prevEquippedProgress = this.equippedProgress;
		EntityPlayerSP var1 = this.mc.thePlayer;
		ItemStack var2 = var1.inventory.getCurrentItem();
		boolean var4 = this.field_20099_f == var1.inventory.currentItem && var2 == this.itemToRender;
		if(this.itemToRender == null && var2 == null) {
			var4 = true;
		}

		if(var2 != null && this.itemToRender != null && var2 != this.itemToRender && var2.itemID == this.itemToRender.itemID && var2.getItemDamage() == this.itemToRender.getItemDamage()) {
			this.itemToRender = var2;
			var4 = true;
		}

		float var5 = 0.4F;
		float var6 = var4?1.0F:0.0F;
		float var7 = var6 - this.equippedProgress;
		if(var7 < -var5) {
			var7 = -var5;
		}

		if(var7 > var5) {
			var7 = var5;
		}

		this.equippedProgress += var7;
		if(this.equippedProgress < 0.1F) {
			this.itemToRender = var2;
			this.field_20099_f = var1.inventory.currentItem;
		}

	}

	public void func_9449_b() {
		this.equippedProgress = 0.0F;
	}

	public void func_9450_c() {
		this.equippedProgress = 0.0F;
	}
}
