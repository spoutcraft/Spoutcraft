package net.minecraft.src;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;

import com.prupe.mcpatcher.cc.ColorizeBlock;

import org.spoutcraft.api.block.design.BlockDesign;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.CustomTextureManager;

// Spout Start
//Spout End

public class ItemRenderer {
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
	private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");

	/** A reference to the Minecraft object. */
	private Minecraft mc;
	private ItemStack itemToRender;

	/**
	 * How far the current item has been equipped (0 disequipped and 1 fully up)
	 */
	private float equippedProgress;
	private float prevEquippedProgress;

	/** Instance of RenderBlocks. */
	private RenderBlocks renderBlocksInstance = new RenderBlocks();
	public final MapItemRenderer mapItemRenderer;

	/** The index of the currently held item (0-8, or -1 if not yet updated) */
	private int equippedItemSlot = -1;
	// Spout Start
	private Random rand = new Random();
	// Spout End

	public ItemRenderer(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
		this.mapItemRenderer = new MapItemRenderer(par1Minecraft.gameSettings, par1Minecraft.getTextureManager());
	}

	/**
	 * Renders the item stack for being in an entity's hand Args: itemStack
	 */
	public void renderItem(EntityLivingBase par1EntityLivingBase, ItemStack par2ItemStack, int par3) {
		GL11.glPushMatrix();
		TextureManager var99 = this.mc.getTextureManager();
		// Spout Start
		Block var4block = Block.blocksList[par2ItemStack.itemID];
		boolean custom = false;
		BlockDesign design = null;
		if (par2ItemStack.itemID == 318) {
			org.spoutcraft.api.material.CustomItem item = MaterialData.getCustomItem(par2ItemStack.getItemDamage());
			if (item != null) {
				String textureURI = item.getTexture();
				if (textureURI == null) {
					org.spoutcraft.api.material.CustomBlock block = MaterialData.getCustomBlock(par2ItemStack.getItemDamage());
					design = block != null ? block.getBlockDesign() : null;
					textureURI = design != null ? design.getTextureURL() : null;
				}
				if (textureURI != null) {
					Texture texture = CustomTextureManager.getTextureFromUrl(item.getAddon(), textureURI);
					if (texture != null) {
						SpoutClient.getHandle().renderEngine.bindTexture(texture.getTextureID());
						custom = true;
					}
				}
			}
		}

		if (!custom) {
			if (par2ItemStack.itemID < 256) {
				this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			} else {
				this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
			}
		}

		if (design != null) {
			GL11.glDisable(GL11.GL_LIGHTING);
			design.renderItemstack(null, -0.5F, -0.5F, -0.5F, 0, 1F, rand);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
		else if(var4block != null && RenderBlocks.renderItemIn3d(var4block.getRenderType())) {
			this.renderBlocksInstance.renderBlockAsItem(Block.blocksList[par2ItemStack.itemID], par2ItemStack.getItemDamage(), 1.0F);
		} else {
			Tessellator var5 = Tessellator.instance;
			
			Icon var4 = par1EntityLivingBase.getItemIcon(par2ItemStack, par3);
			if (var4 == null) {
				GL11.glPopMatrix();
				return;
			}

			float var6 = var4.getMinU();
			float var7 = var4.getMaxU();
			float var8 = var4.getMinV();
			float var9 = var4.getMaxV();
			float var10 = 0.0F;
			float var11 = 0.3F;
			if (custom){
				var6 = 0;
				var7 = 1;
				var8 = 1;
				var9 = 0;
			}
			// Spout end
			//ToDo: may need this
			//var99.bindTexture(var4.getResourceLocation(par2ItemStack.getItemSpriteNumber()));
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(-var10, -var11, 0.0F);
			float var12 = 1.5F;
			GL11.glScalef(var12, var12, var12);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);

			if (par2ItemStack != null) {
				ColorizeBlock.colorizeWaterBlockGL(par2ItemStack.itemID);
			}

			renderItemIn2D(var5, var7, var8, var6, var9, var4.getIconWidth(), var4.getIconHeight(), 0.0625F);
			
			if (par2ItemStack != null && par2ItemStack.hasEffect() && par3 == 0) {
				GL11.glDepthFunc(GL11.GL_EQUAL);
				GL11.glDisable(GL11.GL_LIGHTING);
				var99.bindTexture(RES_ITEM_GLINT);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
				float var13 = 0.76F;
				GL11.glColor4f(0.5F * var13, 0.25F * var13, 0.8F * var13, 1.0F);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glPushMatrix();
				float var14 = 0.125F;
				GL11.glScalef(var14, var14, var14);
				float var15 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
				GL11.glTranslatef(var15, 0.0F, 0.0F);
				GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
				renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glScalef(var14, var14, var14);
				var15 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
				GL11.glTranslatef(-var15, 0.0F, 0.0F);
				GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
				renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}

		GL11.glPopMatrix();
	}

	// MCPatcher Start
	/**
	 * Renders an item held in hand as a 2D texture with thickness
	 */
	public static void renderItemIn2D(Tessellator par0Tessellator, float par1, float par2, float par3, float par4, int par5, int par6, float par7) {
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, 0.0F, 1.0F);
		par0Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)par1, (double)par4);
		par0Tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, (double)par3, (double)par4);
		par0Tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, (double)par3, (double)par2);
		par0Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)par1, (double)par2);
		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, 0.0F, -1.0F);
		par0Tessellator.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - par7), (double)par1, (double)par2);
		par0Tessellator.addVertexWithUV(1.0D, 1.0D, (double)(0.0F - par7), (double)par3, (double)par2);
		par0Tessellator.addVertexWithUV(1.0D, 0.0D, (double)(0.0F - par7), (double)par3, (double)par4);
		par0Tessellator.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - par7), (double)par1, (double)par4);
		par0Tessellator.draw();
		float var8 = 0.5F * (par1 - par3) / (float)par5;
		float var9 = 0.5F * (par4 - par2) / (float)par6;
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		int var10;
		float var11;
		float var12;

		for (var10 = 0; var10 < par5; ++var10) {
			var11 = (float)var10 / (float)par5;
			var12 = par1 + (par3 - par1) * var11 - var8;
			par0Tessellator.addVertexWithUV((double)var11, 0.0D, (double)(0.0F - par7), (double)var12, (double)par4);
			par0Tessellator.addVertexWithUV((double)var11, 0.0D, 0.0D, (double)var12, (double)par4);
			par0Tessellator.addVertexWithUV((double)var11, 1.0D, 0.0D, (double)var12, (double)par2);
			par0Tessellator.addVertexWithUV((double)var11, 1.0D, (double)(0.0F - par7), (double)var12, (double)par2);
		}

		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(1.0F, 0.0F, 0.0F);
		float var13;

		for (var10 = 0; var10 < par5; ++var10) {
			var11 = (float)var10 / (float)par5;
			var12 = par1 + (par3 - par1) * var11 - var8;
			var13 = var11 + 1.0F / (float)par5;
			par0Tessellator.addVertexWithUV((double)var13, 1.0D, (double)(0.0F - par7), (double)var12, (double)par2);
			par0Tessellator.addVertexWithUV((double)var13, 1.0D, 0.0D, (double)var12, (double)par2);
			par0Tessellator.addVertexWithUV((double)var13, 0.0D, 0.0D, (double)var12, (double)par4);
			par0Tessellator.addVertexWithUV((double)var13, 0.0D, (double)(0.0F - par7), (double)var12, (double)par4);
		}

		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, 1.0F, 0.0F);

		for (var10 = 0; var10 < par6; ++var10) {
			var11 = (float)var10 / (float)par6;
			var12 = par4 + (par2 - par4) * var11 - var9;
			var13 = var11 + 1.0F / (float)par6;
			par0Tessellator.addVertexWithUV(0.0D, (double)var13, 0.0D, (double)par1, (double)var12);
			par0Tessellator.addVertexWithUV(1.0D, (double)var13, 0.0D, (double)par3, (double)var12);
			par0Tessellator.addVertexWithUV(1.0D, (double)var13, (double)(0.0F - par7), (double)par3, (double)var12);
			par0Tessellator.addVertexWithUV(0.0D, (double)var13, (double)(0.0F - par7), (double)par1, (double)var12);
		}

		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, -1.0F, 0.0F);

		for (var10 = 0; var10 < par6; ++var10) {
			var11 = (float)var10 / (float)par6;
			var12 = par4 + (par2 - par4) * var11 - var9;
			par0Tessellator.addVertexWithUV(1.0D, (double)var11, 0.0D, (double)par3, (double)var12);
			par0Tessellator.addVertexWithUV(0.0D, (double)var11, 0.0D, (double)par1, (double)var12);
			par0Tessellator.addVertexWithUV(0.0D, (double)var11, (double)(0.0F - par7), (double)par1, (double)var12);
			par0Tessellator.addVertexWithUV(1.0D, (double)var11, (double)(0.0F - par7), (double)par3, (double)var12);
		}

		par0Tessellator.draw();
	}

	/**
	 * Renders the active item in the player's hand when in first person mode. Args: partialTickTime
	 */
	public void renderItemInFirstPerson(float par1) {
		float var2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * par1;
		EntityClientPlayerMP var3 = this.mc.thePlayer;
		float var4 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * par1;
		GL11.glPushMatrix();
		GL11.glRotatef(var4, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * par1, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		EntityPlayerSP var5 = (EntityPlayerSP)var3;
		float var6 = var5.prevRenderArmPitch + (var5.renderArmPitch - var5.prevRenderArmPitch) * par1;
		float var7 = var5.prevRenderArmYaw + (var5.renderArmYaw - var5.prevRenderArmYaw) * par1;
		GL11.glRotatef((var3.rotationPitch - var6) * 0.1F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef((var3.rotationYaw - var7) * 0.1F, 0.0F, 1.0F, 0.0F);
		ItemStack var8 = this.itemToRender;
		float var9 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ));
		var9 = 1.0F;
		int var10 = this.mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ), 0);
		int var11 = var10 % 65536;
		int var12 = var10 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var11 / 1.0F, (float)var12 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var13;
		float var20;
		float var22;

		if (var8 != null) {
			var10 = Item.itemsList[var8.itemID].getColorFromItemStack(var8, 0);
			var20 = (float)(var10 >> 16 & 255) / 255.0F;
			var22 = (float)(var10 >> 8 & 255) / 255.0F;
			var13 = (float)(var10 & 255) / 255.0F;
			GL11.glColor4f(var9 * var20, var9 * var22, var9 * var13, 1.0F);
		} else {
			GL11.glColor4f(var9, var9, var9, 1.0F);
		}

		float var14;
		float var15;
		float var16;
		float var21;
		Render var27;
		RenderPlayer var26;

		if (var8 != null && var8.itemID == Item.map.itemID) {
			GL11.glPushMatrix();
			var21 = 0.8F;
			var20 = var3.getSwingProgress(par1);
			var22 = MathHelper.sin(var20 * (float)Math.PI);
			var13 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI);
			GL11.glTranslatef(-var13 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI * 2.0F) * 0.2F, -var22 * 0.2F);
			var20 = 1.0F - var4 / 45.0F + 0.1F;

			if (var20 < 0.0F) {
				var20 = 0.0F;
			}

			if (var20 > 1.0F) {
				var20 = 1.0F;
			}

			var20 = -MathHelper.cos(var20 * (float)Math.PI) * 0.5F + 0.5F;
			GL11.glTranslatef(0.0F, 0.0F * var21 - (1.0F - var2) * 1.2F - var20 * 0.5F + 0.04F, -0.9F * var21);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(var20 * -85.0F, 0.0F, 0.0F, 1.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			this.mc.getTextureManager().bindTexture(var3.getLocationSkin());

			for (var12 = 0; var12 < 2; ++var12) {
				int var24 = var12 * 2 - 1;
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.0F, -0.6F, 1.1F * (float)var24);
				GL11.glRotatef((float)(-45 * var24), 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef((float)(-65 * var24), 0.0F, 1.0F, 0.0F);
				var27 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
				var26 = (RenderPlayer)var27;
				var16 = 1.0F;
				GL11.glScalef(var16, var16, var16);
				var26.renderFirstPersonArm(this.mc.thePlayer);
				GL11.glPopMatrix();
			}

			var22 = var3.getSwingProgress(par1);
			var13 = MathHelper.sin(var22 * var22 * (float)Math.PI);
			var14 = MathHelper.sin(MathHelper.sqrt_float(var22) * (float)Math.PI);
			GL11.glRotatef(-var13 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var14 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var14 * 80.0F, 1.0F, 0.0F, 0.0F);
			var15 = 0.38F;
			GL11.glScalef(var15, var15, var15);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
			var16 = 0.015625F;
			GL11.glScalef(var16, var16, var16);
			this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
			Tessellator var30 = Tessellator.instance;
			GL11.glNormal3f(0.0F, 0.0F, -1.0F);
			var30.startDrawingQuads();
			byte var29 = 7;
			var30.addVertexWithUV((double)(0 - var29), (double)(128 + var29), 0.0D, 0.0D, 1.0D);
			var30.addVertexWithUV((double)(128 + var29), (double)(128 + var29), 0.0D, 1.0D, 1.0D);
			var30.addVertexWithUV((double)(128 + var29), (double)(0 - var29), 0.0D, 1.0D, 0.0D);
			var30.addVertexWithUV((double)(0 - var29), (double)(0 - var29), 0.0D, 0.0D, 0.0D);
			var30.draw();
			MapData var19 = Item.map.getMapData(var8, this.mc.theWorld);

			if (var19 != null) {
				this.mapItemRenderer.renderMap(this.mc.thePlayer, this.mc.getTextureManager(), var19);
			}

			GL11.glPopMatrix();
		} else if (var8 != null) {
			GL11.glPushMatrix();
			var21 = 0.8F;

			if (var3.getItemInUseCount() > 0) {
				EnumAction var23 = var8.getItemUseAction();

				if (var23 == EnumAction.eat || var23 == EnumAction.drink) {
					var22 = (float)var3.getItemInUseCount() - par1 + 1.0F;
					var13 = 1.0F - var22 / (float)var8.getMaxItemUseDuration();
					var14 = 1.0F - var13;
					var14 = var14 * var14 * var14;
					var14 = var14 * var14 * var14;
					var14 = var14 * var14 * var14;
					var15 = 1.0F - var14;
					GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos(var22 / 4.0F * (float)Math.PI) * 0.1F) * (float)((double)var13 > 0.2D ? 1 : 0), 0.0F);
					GL11.glTranslatef(var15 * 0.6F, -var15 * 0.5F, 0.0F);
					GL11.glRotatef(var15 * 90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(var15 * 10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(var15 * 30.0F, 0.0F, 0.0F, 1.0F);
				}
			} else {
				var20 = var3.getSwingProgress(par1);
				var22 = MathHelper.sin(var20 * (float)Math.PI);
				var13 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI);
				GL11.glTranslatef(-var13 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI * 2.0F) * 0.2F, -var22 * 0.2F);
			}

			GL11.glTranslatef(0.7F * var21, -0.65F * var21 - (1.0F - var2) * 0.6F, -0.9F * var21);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			var20 = var3.getSwingProgress(par1);
			var22 = MathHelper.sin(var20 * var20 * (float)Math.PI);
			var13 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI);
			GL11.glRotatef(-var22 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var13 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var13 * 80.0F, 1.0F, 0.0F, 0.0F);
			var14 = 0.4F;
			GL11.glScalef(var14, var14, var14);
			float var17;
			float var18;

			if (var3.getItemInUseCount() > 0) {
				EnumAction var25 = var8.getItemUseAction();

				if (var25 == EnumAction.block) {
					GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
					GL11.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
				} else if (var25 == EnumAction.bow) {
					GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
					GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
					var16 = (float)var8.getMaxItemUseDuration() - ((float)var3.getItemInUseCount() - par1 + 1.0F);
					var17 = var16 / 20.0F;
					var17 = (var17 * var17 + var17 * 2.0F) / 3.0F;

					if (var17 > 1.0F) {
						var17 = 1.0F;
					}

					if (var17 > 0.1F) {
						GL11.glTranslatef(0.0F, MathHelper.sin((var16 - 0.1F) * 1.3F) * 0.01F * (var17 - 0.1F), 0.0F);
					}

					GL11.glTranslatef(0.0F, 0.0F, var17 * 0.1F);
					GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glTranslatef(0.0F, 0.5F, 0.0F);
					var18 = 1.0F + var17 * 0.2F;
					GL11.glScalef(1.0F, 1.0F, var18);
					GL11.glTranslatef(0.0F, -0.5F, 0.0F);
					GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				}
			}

			if (var8.getItem().shouldRotateAroundWhenRendering()) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var8.getItem().requiresMultipleRenderPasses()) {
				this.renderItem(var3, var8, 0);
				int var28 = Item.itemsList[var8.itemID].getColorFromItemStack(var8, 1);
				var16 = (float)(var28 >> 16 & 255) / 255.0F;
				var17 = (float)(var28 >> 8 & 255) / 255.0F;
				var18 = (float)(var28 & 255) / 255.0F;
				GL11.glColor4f(var9 * var16, var9 * var17, var9 * var18, 1.0F);
				this.renderItem(var3, var8, 1);
			} else {
				this.renderItem(var3, var8, 0);
			}

			GL11.glPopMatrix();
		} else if (!var3.isInvisible()) {
			GL11.glPushMatrix();
			var21 = 0.8F;
			var20 = var3.getSwingProgress(par1);
			var22 = MathHelper.sin(var20 * (float)Math.PI);
			var13 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI);
			GL11.glTranslatef(-var13 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI * 2.0F) * 0.4F, -var22 * 0.4F);
			GL11.glTranslatef(0.8F * var21, -0.75F * var21 - (1.0F - var2) * 0.6F, -0.9F * var21);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			var20 = var3.getSwingProgress(par1);
			var22 = MathHelper.sin(var20 * var20 * (float)Math.PI);
			var13 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI);
			GL11.glRotatef(var13 * 70.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var22 * 20.0F, 0.0F, 0.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(var3.getLocationSkin());
			GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(5.6F, 0.0F, 0.0F);
			var27 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
			var26 = (RenderPlayer)var27;
			var16 = 1.0F;
			GL11.glScalef(var16, var16, var16);
			var26.renderFirstPersonArm(this.mc.thePlayer);
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}

	/**
	 * Renders all the overlays that are in first person mode. Args: partialTickTime
	 */
	public void renderOverlays(float par1) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		if (this.mc.thePlayer.isBurning()) {			
			this.renderFireInFirstPerson(par1);
		}

		if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
			int var2 = MathHelper.floor_double(this.mc.thePlayer.posX);
			int var3 = MathHelper.floor_double(this.mc.thePlayer.posY);
			int var4 = MathHelper.floor_double(this.mc.thePlayer.posZ);			
			int var5 = this.mc.theWorld.getBlockId(var2, var3, var4);

			if (this.mc.theWorld.isBlockNormalCube(var2, var3, var4)) {
				this.renderInsideOfBlock(par1, Block.blocksList[var5].getBlockTextureFromSide(2));
			} else {
				for (int var6 = 0; var6 < 8; ++var6) {
					float var7 = ((float)((var6 >> 0) % 2) - 0.5F) * this.mc.thePlayer.width * 0.9F;
					float var8 = ((float)((var6 >> 1) % 2) - 0.5F) * this.mc.thePlayer.height * 0.2F;
					float var9 = ((float)((var6 >> 2) % 2) - 0.5F) * this.mc.thePlayer.width * 0.9F;
					int var10 = MathHelper.floor_float((float)var2 + var7);
					int var11 = MathHelper.floor_float((float)var3 + var8);
					int var12 = MathHelper.floor_float((float)var4 + var9);

					if (this.mc.theWorld.isBlockNormalCube(var10, var11, var12)) {
						var5 = this.mc.theWorld.getBlockId(var10, var11, var12);
					}
				}
			}

			if (Block.blocksList[var5] != null) {
				this.renderInsideOfBlock(par1, Block.blocksList[var5].getBlockTextureFromSide(2));
			}
		}

		if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {		
			this.renderWarpedTextureOverlay(par1);
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	/**
	 * Renders the texture of the block the player is inside as an overlay. Args: partialTickTime, blockTextureIndex
	 */
	private void renderInsideOfBlock(float par1, Icon par2Icon) {
		this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		Tessellator var3 = Tessellator.instance;
		float var4 = 0.1F;
		GL11.glColor4f(var4, var4, var4, 0.5F);
		GL11.glPushMatrix();
		float var5 = -1.0F;
		float var6 = 1.0F;
		float var7 = -1.0F;
		float var8 = 1.0F;
		float var9 = -0.5F;
		float var10 = par2Icon.getMinU();
		float var11 = par2Icon.getMaxU();
		float var12 = par2Icon.getMinV();
		float var13 = par2Icon.getMaxV();
		var3.startDrawingQuads();
		var3.addVertexWithUV((double)var5, (double)var7, (double)var9, (double)var11, (double)var13);
		var3.addVertexWithUV((double)var6, (double)var7, (double)var9, (double)var10, (double)var13);
		var3.addVertexWithUV((double)var6, (double)var8, (double)var9, (double)var10, (double)var12);
		var3.addVertexWithUV((double)var5, (double)var8, (double)var9, (double)var11, (double)var12);
		var3.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Renders a texture that warps around based on the direction the player is looking. Texture needs to be bound before
	 * being called. Used for the water overlay. Args: parialTickTime
	 */
	private void renderWarpedTextureOverlay(float par1) {
		this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
		Tessellator var2 = Tessellator.instance;
		float var3 = this.mc.thePlayer.getBrightness(par1);
		GL11.glColor4f(var3, var3, var3, 0.5F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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
		GL11.glDisable(GL11.GL_BLEND);
	}

	/**
	 * Renders the fire on the screen for first person mode. Arg: partialTickTime
	 */
	private void renderFireInFirstPerson(float par1) {
		Tessellator var2 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float var3 = 1.0F;

		for (int var4 = 0; var4 < 2; ++var4) {
			GL11.glPushMatrix();
			Icon var5 = Block.fire.getFireIcon(1);
			this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			float var6 = var5.getMinU();
			float var7 = var5.getMaxU();
			float var8 = var5.getMinV();
			float var9 = var5.getMaxV();
			float var10 = (0.0F - var3) / 2.0F;
			float var11 = var10 + var3;
			float var12 = 0.0F - var3 / 2.0F;
			float var13 = var12 + var3;
			float var14 = -0.5F;
			GL11.glTranslatef((float)(-(var4 * 2 - 1)) * 0.24F, -0.3F, 0.0F);
			GL11.glRotatef((float)(var4 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
			var2.startDrawingQuads();
			var2.addVertexWithUV((double)var10, (double)var12, (double)var14, (double)var7, (double)var9);
			var2.addVertexWithUV((double)var11, (double)var12, (double)var14, (double)var6, (double)var9);
			var2.addVertexWithUV((double)var11, (double)var13, (double)var14, (double)var6, (double)var8);
			var2.addVertexWithUV((double)var10, (double)var13, (double)var14, (double)var7, (double)var8);
			var2.draw();
			GL11.glPopMatrix();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void updateEquippedItem() {
		this.prevEquippedProgress = this.equippedProgress;
		EntityClientPlayerMP var1 = this.mc.thePlayer;
		ItemStack var2 = var1.inventory.getCurrentItem();
		boolean var3 = this.equippedItemSlot == var1.inventory.currentItem && var2 == this.itemToRender;

		if (this.itemToRender == null && var2 == null) {
			var3 = true;
		}

		if (var2 != null && this.itemToRender != null && var2 != this.itemToRender && var2.itemID == this.itemToRender.itemID && var2.getItemDamage() == this.itemToRender.getItemDamage()) {
			this.itemToRender = var2;
			var3 = true;
		}

		float var4 = 0.4F;
		float var5 = var3 ? 1.0F : 0.0F;
		float var6 = var5 - this.equippedProgress;

		if (var6 < -var4) {
			var6 = -var4;
		}

		if (var6 > var4) {
			var6 = var4;
		}

		this.equippedProgress += var6;

		if (this.equippedProgress < 0.1F) {
			this.itemToRender = var2;
			this.equippedItemSlot = var1.inventory.currentItem;
		}
	}

	/**
	 * Resets equippedProgress
	 */
	public void resetEquippedProgress() {
		this.equippedProgress = 0.0F;
	}

	/**
	 * Resets equippedProgress
	 */
	public void resetEquippedProgress2() {
		this.equippedProgress = 0.0F;
	}
}
