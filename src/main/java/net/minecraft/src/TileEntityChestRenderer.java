package net.minecraft.src;

import java.util.Calendar;

import net.minecraft.src.TileEntityChest;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

// Spout Start
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.block.SpoutcraftChunk;
// Spout End

public class TileEntityChestRenderer extends TileEntitySpecialRenderer {
	private static final ResourceLocation RES_TRAPPED_DOUBLE = new ResourceLocation("textures/entity/chest/trapped_double.png");
	private static final ResourceLocation RES_CHRISTMAS_DOUBLE = new ResourceLocation("textures/entity/chest/christmas_double.png");
	private static final ResourceLocation RES_NORMAL_DOUBLE = new ResourceLocation("textures/entity/chest/normal_double.png");
	private static final ResourceLocation RES_TRAPPED_SINGLE = new ResourceLocation("textures/entity/chest/trapped.png");
	private static final ResourceLocation RES_CHRISTMAS_SINGLE = new ResourceLocation("textures/entity/chest/christmas.png");
	private static final ResourceLocation RES_NORMAL_SINGLE = new ResourceLocation("textures/entity/chest/normal.png");

	/** The normal small chest model. */
	private ModelChest chestModel = new ModelChest();

	/** The large double chest model. */
	private ModelChest largeChestModel = new ModelLargeChest();

	/** If true, chests will be rendered with the Christmas present textures. */
	private boolean isChristmas;

	public TileEntityChestRenderer() {
		Calendar var1 = Calendar.getInstance();

		if (var1.get(2) + 1 == 12 && var1.get(5) >= 24 && var1.get(5) <= 26) {
			this.isChristmas = true;
		}
	}

	/**
	 * Renders the TileEntity for the chest at a position.
	 */
	public void renderTileEntityChestAt(TileEntityChest par1TileEntityChest, double par2, double par4, double par6, float par8) {
		int var9;
		// Spout Start
		short customId = 0;		
		if (SpoutClient.getInstance().getRawWorld() != null && par1TileEntityChest.hasWorldObj()) {
			SpoutcraftChunk sChunk = Spoutcraft.getChunkAt(par1TileEntityChest.worldObj, par1TileEntityChest.xCoord, par1TileEntityChest.yCoord, par1TileEntityChest.zCoord);
			customId = sChunk.getCustomBlockId(par1TileEntityChest.xCoord, par1TileEntityChest.yCoord, par1TileEntityChest.zCoord);	
			short[] customBlockIds = sChunk.getCustomBlockIds();
			byte[] customBlockData = sChunk.getCustomBlockData();			

			if (customId > 0) {				
				return; // Don't render the chest image or animation if its a custom chest.
			}
		}
		// Spout End
		
		if (!par1TileEntityChest.hasWorldObj()) {
			var9 = 0;
		} else {
			Block var10 = par1TileEntityChest.getBlockType();
			var9 = par1TileEntityChest.getBlockMetadata();

			if (var10 instanceof BlockChest && var9 == 0) {
				((BlockChest)var10).unifyAdjacentChests(par1TileEntityChest.getWorldObj(), par1TileEntityChest.xCoord, par1TileEntityChest.yCoord, par1TileEntityChest.zCoord);
				var9 = par1TileEntityChest.getBlockMetadata();
			}

			par1TileEntityChest.checkForAdjacentChests();
		}

		if (par1TileEntityChest.adjacentChestZNeg == null && par1TileEntityChest.adjacentChestXNeg == null) {
			ModelChest var14;
			
			if (par1TileEntityChest.adjacentChestXPos == null && par1TileEntityChest.adjacentChestZPosition == null) {
				var14 = this.chestModel;

				if (par1TileEntityChest.getChestType() == 1) {
					this.bindTexture(RES_TRAPPED_SINGLE);
				} else if (this.isChristmas) {
					this.bindTexture(RES_CHRISTMAS_SINGLE);
				} else {
					this.bindTexture(RES_NORMAL_SINGLE);
				}
			} else {
				var14 = this.largeChestModel;

				if (par1TileEntityChest.getChestType() == 1) {
					this.bindTexture(RES_TRAPPED_DOUBLE);
				} else if (this.isChristmas) {
					this.bindTexture(RES_CHRISTMAS_DOUBLE);
				} else {
					this.bindTexture(RES_NORMAL_DOUBLE);
				}
			}

			GL11.glPushMatrix();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float)par2, (float)par4 + 1.0F, (float)par6 + 1.0F);
			GL11.glScalef(1.0F, -1.0F, -1.0F);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			short var11 = 0;

			if (var9 == 2) {
				var11 = 180;
			}

			if (var9 == 3) {
				var11 = 0;
			}

			if (var9 == 4) {
				var11 = 90;
			}

			if (var9 == 5) {
				var11 = -90;
			}

			if (var9 == 2 && par1TileEntityChest.adjacentChestXPos != null) {
				GL11.glTranslatef(1.0F, 0.0F, 0.0F);
			}

			if (var9 == 5 && par1TileEntityChest.adjacentChestZPosition != null) {
				GL11.glTranslatef(0.0F, 0.0F, -1.0F);
			}

			GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			float var12 = par1TileEntityChest.prevLidAngle + (par1TileEntityChest.lidAngle - par1TileEntityChest.prevLidAngle) * par8;
			float var13;

			if (par1TileEntityChest.adjacentChestZNeg != null) {
				var13 = par1TileEntityChest.adjacentChestZNeg.prevLidAngle + (par1TileEntityChest.adjacentChestZNeg.lidAngle - par1TileEntityChest.adjacentChestZNeg.prevLidAngle) * par8;

				if (var13 > var12) {
					var12 = var13;
				}
			}

			if (par1TileEntityChest.adjacentChestXNeg != null) {
				var13 = par1TileEntityChest.adjacentChestXNeg.prevLidAngle + (par1TileEntityChest.adjacentChestXNeg.lidAngle - par1TileEntityChest.adjacentChestXNeg.prevLidAngle) * par8;

				if (var13 > var12) {
					var12 = var13;
				}
			}

			var12 = 1.0F - var12;
			var12 = 1.0F - var12 * var12 * var12;
			var14.chestLid.rotateAngleX = -(var12 * (float)Math.PI / 2.0F);
			var14.renderAll();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
		this.renderTileEntityChestAt((TileEntityChest)par1TileEntity, par2, par4, par6, par8);
	}
}
