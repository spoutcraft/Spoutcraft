package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.BlockBed;
import net.minecraft.src.BlockBrewingStand;
import net.minecraft.src.BlockCauldron;
import net.minecraft.src.BlockDirectional;
import net.minecraft.src.BlockDoor;
import net.minecraft.src.BlockDragonEgg;
import net.minecraft.src.BlockEndPortalFrame;
import net.minecraft.src.BlockFence;
import net.minecraft.src.BlockFenceGate;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.BlockPane;
import net.minecraft.src.BlockPistonBase;
import net.minecraft.src.BlockPistonExtension;
import net.minecraft.src.BlockRail;
import net.minecraft.src.BlockRedstoneRepeater;
import net.minecraft.src.BlockRedstoneWire;
import net.minecraft.src.BlockStem;
import net.minecraft.src.ChestItemRenderHelper;
import net.minecraft.src.Direction;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

//Spout start
import org.spoutcraft.client.config.ConfigReader;
import com.pclewis.mcpatcher.mod.Colorizer;
//Spout end

public class RenderBlocks {

	public IBlockAccess blockAccess; // Spout private -> public
	private int overrideBlockTexture = -1;
	private boolean flipTexture = false;
	private boolean renderAllFaces = false;
	public static boolean fancyGrass = true;
	public boolean useInventoryTint = true;
	private int uvRotateEast = 0;
	private int uvRotateWest = 0;
	private int uvRotateSouth = 0;
	private int uvRotateNorth = 0;
	private int uvRotateTop = 0;
	private int uvRotateBottom = 0;
	private boolean enableAO;
	private float lightValueOwn;
	private float aoLightValueXNeg;
	private float aoLightValueYNeg;
	private float aoLightValueZNeg;
	private float aoLightValueXPos;
	private float aoLightValueYPos;
	private float aoLightValueZPos;
	private float aoLightValueScratchXYZNNN;
	private float aoLightValueScratchXYNN;
	private float aoLightValueScratchXYZNNP;
	private float aoLightValueScratchYZNN;
	private float aoLightValueScratchYZNP;
	private float aoLightValueScratchXYZPNN;
	private float aoLightValueScratchXYPN;
	private float aoLightValueScratchXYZPNP;
	private float aoLightValueScratchXYZNPN;
	private float aoLightValueScratchXYNP;
	private float aoLightValueScratchXYZNPP;
	private float aoLightValueScratchYZPN;
	private float aoLightValueScratchXYZPPN;
	private float aoLightValueScratchXYPP;
	private float aoLightValueScratchYZPP;
	private float aoLightValueScratchXYZPPP;
	private float aoLightValueScratchXZNN;
	private float aoLightValueScratchXZPN;
	private float aoLightValueScratchXZNP;
	private float aoLightValueScratchXZPP;
	private int aoBrightnessXYZNNN;
	private int aoBrightnessXYNN;
	private int aoBrightnessXYZNNP;
	private int aoBrightnessYZNN;
	private int aoBrightnessYZNP;
	private int aoBrightnessXYZPNN;
	private int aoBrightnessXYPN;
	private int aoBrightnessXYZPNP;
	private int aoBrightnessXYZNPN;
	private int aoBrightnessXYNP;
	private int aoBrightnessXYZNPP;
	private int aoBrightnessYZPN;
	private int aoBrightnessXYZPPN;
	private int aoBrightnessXYPP;
	private int aoBrightnessYZPP;
	private int aoBrightnessXYZPPP;
	private int aoBrightnessXZNN;
	private int aoBrightnessXZPN;
	private int aoBrightnessXZNP;
	private int aoBrightnessXZPP;
	private int aoType = 1;
	private int brightnessTopLeft;
	private int brightnessBottomLeft;
	private int brightnessBottomRight;
	private int brightnessTopRight;
	private float colorRedTopLeft;
	private float colorRedBottomLeft;
	private float colorRedBottomRight;
	private float colorRedTopRight;
	private float colorGreenTopLeft;
	private float colorGreenBottomLeft;
	private float colorGreenBottomRight;
	private float colorGreenTopRight;
	private float colorBlueTopLeft;
	private float colorBlueBottomLeft;
	private float colorBlueBottomRight;
	private float colorBlueTopRight;
	private boolean aoGrassXYZCPN;
	private boolean aoGrassXYZPPC;
	private boolean aoGrassXYZNPC;
	private boolean aoGrassXYZCPP;
	private boolean aoGrassXYZNCN;
	private boolean aoGrassXYZPCP;
	private boolean aoGrassXYZNCP;
	private boolean aoGrassXYZPCN;
	private boolean aoGrassXYZCNN;
	private boolean aoGrassXYZPNC;
	private boolean aoGrassXYZNNC;
	private boolean aoGrassXYZCNP;
	//Spout start
	public short[] customIds = null;
	//Spout end

	public RenderBlocks(IBlockAccess par1IBlockAccess) {
		this.blockAccess = par1IBlockAccess;
	}

	public RenderBlocks() {}

	public void clearOverrideBlockTexture() {
		this.overrideBlockTexture = -1;
	}

	public void renderBlockUsingTexture(Block par1Block, int par2, int par3, int par4, int par5) {
		this.overrideBlockTexture = par5;
		this.renderBlockByRenderType(par1Block, par2, par3, par4);
		this.overrideBlockTexture = -1;
	}

	public void renderBlockAllFaces(Block par1Block, int par2, int par3, int par4) {
		this.renderAllFaces = true;
		this.renderBlockByRenderType(par1Block, par2, par3, par4);
		this.renderAllFaces = false;
	}

	public boolean renderBlockByRenderType(Block par1Block, int par2, int par3, int par4) {
		int var5 = par1Block.getRenderType();
		par1Block.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
		return var5 == 0?this.renderStandardBlock(par1Block, par2, par3, par4):(var5 == 4?this.renderBlockFluids(par1Block, par2, par3, par4):(var5 == 13?this.renderBlockCactus(par1Block, par2, par3, par4):(var5 == 1?this.renderCrossedSquares(par1Block, par2, par3, par4):(var5 == 19?this.renderBlockStem(par1Block, par2, par3, par4):(var5 == 23?this.renderBlockLilyPad(par1Block, par2, par3, par4):(var5 == 6?this.renderBlockCrops(par1Block, par2, par3, par4):(var5 == 2?this.renderBlockTorch(par1Block, par2, par3, par4):(var5 == 3?this.renderBlockFire(par1Block, par2, par3, par4):(var5 == 5?this.renderBlockRedstoneWire(par1Block, par2, par3, par4):(var5 == 8?this.renderBlockLadder(par1Block, par2, par3, par4):(var5 == 7?this.renderBlockDoor(par1Block, par2, par3, par4):(var5 == 9?this.renderBlockMinecartTrack((BlockRail)par1Block, par2, par3, par4):(var5 == 10?this.renderBlockStairs(par1Block, par2, par3, par4):(var5 == 27?this.renderBlockDragonEgg((BlockDragonEgg)par1Block, par2, par3, par4):(var5 == 11?this.renderBlockFence((BlockFence)par1Block, par2, par3, par4):(var5 == 12?this.renderBlockLever(par1Block, par2, par3, par4):(var5 == 14?this.renderBlockBed(par1Block, par2, par3, par4):(var5 == 15?this.renderBlockRepeater(par1Block, par2, par3, par4):(var5 == 16?this.renderPistonBase(par1Block, par2, par3, par4, false):(var5 == 17?this.renderPistonExtension(par1Block, par2, par3, par4, true):(var5 == 18?this.renderBlockPane((BlockPane)par1Block, par2, par3, par4):(var5 == 20?this.renderBlockVine(par1Block, par2, par3, par4):(var5 == 21?this.renderBlockFenceGate((BlockFenceGate)par1Block, par2, par3, par4):(var5 == 24?this.renderBlockCauldron((BlockCauldron)par1Block, par2, par3, par4):(var5 == 25?this.renderBlockBrewingStand((BlockBrewingStand)par1Block, par2, par3, par4):(var5 == 26?this.renderBlockEndPortalFrame(par1Block, par2, par3, par4):false))))))))))))))))))))))))));
	}

	private boolean renderBlockEndPortalFrame(Block par1Block, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var6 = var5 & 3;
		if (var6 == 0) {
			this.uvRotateTop = 3;
		} else if (var6 == 3) {
			this.uvRotateTop = 1;
		} else if (var6 == 1) {
			this.uvRotateTop = 2;
		}

		if (!BlockEndPortalFrame.isEnderEyeInserted(var5)) {
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			par1Block.setBlockBoundsForItemRender();
			this.uvRotateTop = 0;
			return true;
		} else {
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.overrideBlockTexture = 174;
			par1Block.setBlockBounds(0.25F, 0.8125F, 0.25F, 0.75F, 1.0F, 0.75F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.clearOverrideBlockTexture();
			par1Block.setBlockBoundsForItemRender();
			this.uvRotateTop = 0;
			return true;
		}
	}

	private boolean renderBlockBed(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var7 = BlockBed.func_48216_a(var6);
		boolean var8 = BlockBed.isBlockFootOfBed(var6);
		float var9 = 0.5F;
		float var10 = 1.0F;
		float var11 = 0.8F;
		float var12 = 0.6F;
		int var25 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		var5.setBrightness(var25);
		var5.setColorOpaque_F(var9, var9, var9);
		int var27 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0);
		int var28 = (var27 & 15) << 4;
		int var29 = var27 & 240;
		double var30 = (double)((float)var28 / 256.0F);
		double var32 = ((double)(var28 + 16) - 0.01D) / 256.0D;
		double var34 = (double)((float)var29 / 256.0F);
		double var36 = ((double)(var29 + 16) - 0.01D) / 256.0D;
		double var38 = (double)par2 + par1Block.minX;
		double var40 = (double)par2 + par1Block.maxX;
		double var42 = (double)par3 + par1Block.minY + 0.1875D;
		double var44 = (double)par4 + par1Block.minZ;
		double var46 = (double)par4 + par1Block.maxZ;
		var5.addVertexWithUV(var38, var42, var46, var30, var36);
		var5.addVertexWithUV(var38, var42, var44, var30, var34);
		var5.addVertexWithUV(var40, var42, var44, var32, var34);
		var5.addVertexWithUV(var40, var42, var46, var32, var36);
		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
		var5.setColorOpaque_F(var10, var10, var10);
		var27 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1);
		var28 = (var27 & 15) << 4;
		var29 = var27 & 240;
		var30 = (double)((float)var28 / 256.0F);
		var32 = ((double)(var28 + 16) - 0.01D) / 256.0D;
		var34 = (double)((float)var29 / 256.0F);
		var36 = ((double)(var29 + 16) - 0.01D) / 256.0D;
		var38 = var30;
		var40 = var32;
		var42 = var34;
		var44 = var34;
		var46 = var30;
		double var48 = var32;
		double var50 = var36;
		double var52 = var36;
		if (var7 == 0) {
			var40 = var30;
			var42 = var36;
			var46 = var32;
			var52 = var34;
		} else if (var7 == 2) {
			var38 = var32;
			var44 = var36;
			var48 = var30;
			var50 = var34;
		} else if (var7 == 3) {
			var38 = var32;
			var44 = var36;
			var48 = var30;
			var50 = var34;
			var40 = var30;
			var42 = var36;
			var46 = var32;
			var52 = var34;
		}

		double var54 = (double)par2 + par1Block.minX;
		double var56 = (double)par2 + par1Block.maxX;
		double var58 = (double)par3 + par1Block.maxY;
		double var60 = (double)par4 + par1Block.minZ;
		double var62 = (double)par4 + par1Block.maxZ;
		var5.addVertexWithUV(var56, var58, var62, var46, var50);
		var5.addVertexWithUV(var56, var58, var60, var38, var42);
		var5.addVertexWithUV(var54, var58, var60, var40, var44);
		var5.addVertexWithUV(var54, var58, var62, var48, var52);
		var27 = Direction.headInvisibleFace[var7];
		if (var8) {
			var27 = Direction.headInvisibleFace[Direction.footInvisibleFaceRemap[var7]];
		}

		byte var64 = 4;
		switch(var7) {
		case 0:
			var64 = 5;
			break;
		case 1:
			var64 = 3;
		case 2:
		default:
			break;
		case 3:
			var64 = 2;
		}

		if (var27 != 2 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2))) {
			var5.setBrightness(par1Block.minZ > 0.0D?var25:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
			var5.setColorOpaque_F(var11, var11, var11);
			this.flipTexture = var64 == 2;
			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2));
		}

		if (var27 != 3 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3))) {
			var5.setBrightness(par1Block.maxZ < 1.0D?var25:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
			var5.setColorOpaque_F(var11, var11, var11);
			this.flipTexture = var64 == 3;
			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3));
		}

		if (var27 != 4 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4))) {
			var5.setBrightness(par1Block.minZ > 0.0D?var25:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
			var5.setColorOpaque_F(var12, var12, var12);
			this.flipTexture = var64 == 4;
			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4));
		}

		if (var27 != 5 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5))) {
			var5.setBrightness(par1Block.maxZ < 1.0D?var25:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
			var5.setColorOpaque_F(var12, var12, var12);
			this.flipTexture = var64 == 5;
			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5));
		}

		this.flipTexture = false;
		return true;
	}

	private boolean renderBlockBrewingStand(BlockBrewingStand par1BlockBrewingStand, int par2, int par3, int par4) {
		par1BlockBrewingStand.setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		this.overrideBlockTexture = 156;
		par1BlockBrewingStand.setBlockBounds(0.5625F, 0.0F, 0.3125F, 0.9375F, 0.125F, 0.6875F);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		par1BlockBrewingStand.setBlockBounds(0.125F, 0.0F, 0.0625F, 0.5F, 0.125F, 0.4375F);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		par1BlockBrewingStand.setBlockBounds(0.125F, 0.0F, 0.5625F, 0.5F, 0.125F, 0.9375F);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		this.clearOverrideBlockTexture();
		Tessellator var5 = Tessellator.instance;
		var5.setBrightness(par1BlockBrewingStand.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var6 = 1.0F;
		int var7 = par1BlockBrewingStand.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var8 = (float)(var7 >> 16 & 255) / 255.0F;
		float var9 = (float)(var7 >> 8 & 255) / 255.0F;
		float var10 = (float)(var7 & 255) / 255.0F;
		if (EntityRenderer.anaglyphEnable) {
			float var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
			float var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
			float var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
			var8 = var11;
			var9 = var12;
			var10 = var13;
		}

		var5.setColorOpaque_F(var6 * var8, var6 * var9, var6 * var10);
		int var34 = par1BlockBrewingStand.getBlockTextureFromSideAndMetadata(0, 0);
		if (this.overrideBlockTexture >= 0) {
			var34 = this.overrideBlockTexture;
		}

		int var35 = (var34 & 15) << 4;
		int var36 = var34 & 240;
		double var14 = (double)((float)var36 / 256.0F);
		double var16 = (double)(((float)var36 + 15.99F) / 256.0F);
		int var18 = this.blockAccess.getBlockMetadata(par2, par3, par4);

		for (int var19 = 0; var19 < 3; ++var19) {
			double var20 = (double)var19 * 3.141592653589793D * 2.0D / 3.0D + 1.5707963267948966D;
			double var22 = (double)(((float)var35 + 8.0F) / 256.0F);
			double var24 = (double)(((float)var35 + 15.99F) / 256.0F);
			if ((var18 & 1 << var19) != 0) {
				var22 = (double)(((float)var35 + 7.99F) / 256.0F);
				var24 = (double)(((float)var35 + 0.0F) / 256.0F);
			}

			double var26 = (double)par2 + 0.5D;
			double var28 = (double)par2 + 0.5D + Math.sin(var20) * 8.0D / 16.0D;
			double var30 = (double)par4 + 0.5D;
			double var32 = (double)par4 + 0.5D + Math.cos(var20) * 8.0D / 16.0D;
			var5.addVertexWithUV(var26, (double)(par3 + 1), var30, var22, var14);
			var5.addVertexWithUV(var26, (double)(par3 + 0), var30, var22, var16);
			var5.addVertexWithUV(var28, (double)(par3 + 0), var32, var24, var16);
			var5.addVertexWithUV(var28, (double)(par3 + 1), var32, var24, var14);
			var5.addVertexWithUV(var28, (double)(par3 + 1), var32, var24, var14);
			var5.addVertexWithUV(var28, (double)(par3 + 0), var32, var24, var16);
			var5.addVertexWithUV(var26, (double)(par3 + 0), var30, var22, var16);
			var5.addVertexWithUV(var26, (double)(par3 + 1), var30, var22, var14);
		}

		par1BlockBrewingStand.setBlockBoundsForItemRender();
		return true;
	}

	private boolean renderBlockCauldron(BlockCauldron par1BlockCauldron, int par2, int par3, int par4) {
		this.renderStandardBlock(par1BlockCauldron, par2, par3, par4);
		Tessellator var5 = Tessellator.instance;
		var5.setBrightness(par1BlockCauldron.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var6 = 1.0F;
		int var7 = par1BlockCauldron.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var8 = (float)(var7 >> 16 & 255) / 255.0F;
		float var9 = (float)(var7 >> 8 & 255) / 255.0F;
		float var10 = (float)(var7 & 255) / 255.0F;
		float var12;
		if (EntityRenderer.anaglyphEnable) {
			float var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
			var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
			float var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
			var8 = var11;
			var9 = var12;
			var10 = var13;
		}

		var5.setColorOpaque_F(var6 * var8, var6 * var9, var6 * var10);
		short var16 = 154;
		var12 = 0.125F;
		this.renderSouthFace(par1BlockCauldron, (double)((float)par2 - 1.0F + var12), (double)par3, (double)par4, var16);
		this.renderNorthFace(par1BlockCauldron, (double)((float)par2 + 1.0F - var12), (double)par3, (double)par4, var16);
		this.renderWestFace(par1BlockCauldron, (double)par2, (double)par3, (double)((float)par4 - 1.0F + var12), var16);
		this.renderEastFace(par1BlockCauldron, (double)par2, (double)par3, (double)((float)par4 + 1.0F - var12), var16);
		short var17 = 139;
		this.renderTopFace(par1BlockCauldron, (double)par2, (double)((float)par3 - 1.0F + 0.25F), (double)par4, var17);
		this.renderBottomFace(par1BlockCauldron, (double)par2, (double)((float)par3 + 1.0F - 0.75F), (double)par4, var17);
		int var14 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		if (var14 > 0) {
			short var15 = 205;
//Spout HD Start
			Colorizer.computeWaterColor();
			var5.setColorOpaque_F(Colorizer.waterColor[0], Colorizer.waterColor[1], Colorizer.waterColor[2]);
			if (var14 > 3) {
				var14 = 3;
			}
//Spout HD end
			this.renderTopFace(par1BlockCauldron, (double)par2, (double)((float)par3 - 1.0F + (6.0F + (float)var14 * 3.0F) / 16.0F), (double)par4, var15);
		}

		return true;
	}

	public boolean renderBlockTorch(Block par1Block, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		Tessellator var6 = Tessellator.instance;
		var6.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double var7 = 0.4000000059604645D;
		double var9 = 0.5D - var7;
		double var11 = 0.20000000298023224D;
		if (var5 == 1) {
			this.renderTorchAtAngle(par1Block, (double)par2 - var9, (double)par3 + var11, (double)par4, -var7, 0.0D);
		} else if (var5 == 2) {
			this.renderTorchAtAngle(par1Block, (double)par2 + var9, (double)par3 + var11, (double)par4, var7, 0.0D);
		} else if (var5 == 3) {
			this.renderTorchAtAngle(par1Block, (double)par2, (double)par3 + var11, (double)par4 - var9, 0.0D, -var7);
		} else if (var5 == 4) {
			this.renderTorchAtAngle(par1Block, (double)par2, (double)par3 + var11, (double)par4 + var9, 0.0D, var7);
		} else {
			this.renderTorchAtAngle(par1Block, (double)par2, (double)par3, (double)par4, 0.0D, 0.0D);
		}

		return true;
	}

	private boolean renderBlockRepeater(Block par1Block, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var6 = var5 & 3;
		int var7 = (var5 & 12) >> 2;
		this.renderStandardBlock(par1Block, par2, par3, par4);
		Tessellator var8 = Tessellator.instance;
		var8.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var8.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double var9 = -0.1875D;
		double var11 = 0.0D;
		double var13 = 0.0D;
		double var15 = 0.0D;
		double var17 = 0.0D;
		switch(var6) {
		case 0:
			var17 = -0.3125D;
			var13 = BlockRedstoneRepeater.repeaterTorchOffset[var7];
			break;
		case 1:
			var15 = 0.3125D;
			var11 = -BlockRedstoneRepeater.repeaterTorchOffset[var7];
			break;
		case 2:
			var17 = 0.3125D;
			var13 = -BlockRedstoneRepeater.repeaterTorchOffset[var7];
			break;
		case 3:
			var15 = -0.3125D;
			var11 = BlockRedstoneRepeater.repeaterTorchOffset[var7];
		}

		this.renderTorchAtAngle(par1Block, (double)par2 + var11, (double)par3 + var9, (double)par4 + var13, 0.0D, 0.0D);
		this.renderTorchAtAngle(par1Block, (double)par2 + var15, (double)par3 + var9, (double)par4 + var17, 0.0D, 0.0D);
		int var19 = par1Block.getBlockTextureFromSide(1);
		int var20 = (var19 & 15) << 4;
		int var21 = var19 & 240;
		double var22 = (double)((float)var20 / 256.0F);
		double var24 = (double)(((float)var20 + 15.99F) / 256.0F);
		double var26 = (double)((float)var21 / 256.0F);
		double var28 = (double)(((float)var21 + 15.99F) / 256.0F);
		double var30 = 0.125D;
		double var32 = (double)(par2 + 1);
		double var34 = (double)(par2 + 1);
		double var36 = (double)(par2 + 0);
		double var38 = (double)(par2 + 0);
		double var40 = (double)(par4 + 0);
		double var42 = (double)(par4 + 1);
		double var44 = (double)(par4 + 1);
		double var46 = (double)(par4 + 0);
		double var48 = (double)par3 + var30;
		if (var6 == 2) {
			var32 = var34 = (double)(par2 + 0);
			var36 = var38 = (double)(par2 + 1);
			var40 = var46 = (double)(par4 + 1);
			var42 = var44 = (double)(par4 + 0);
		} else if (var6 == 3) {
			var32 = var38 = (double)(par2 + 0);
			var34 = var36 = (double)(par2 + 1);
			var40 = var42 = (double)(par4 + 0);
			var44 = var46 = (double)(par4 + 1);
		} else if (var6 == 1) {
			var32 = var38 = (double)(par2 + 1);
			var34 = var36 = (double)(par2 + 0);
			var40 = var42 = (double)(par4 + 1);
			var44 = var46 = (double)(par4 + 0);
		}

		var8.addVertexWithUV(var38, var48, var46, var22, var26);
		var8.addVertexWithUV(var36, var48, var44, var22, var28);
		var8.addVertexWithUV(var34, var48, var42, var24, var28);
		var8.addVertexWithUV(var32, var48, var40, var24, var26);
		return true;
	}

	public void renderPistonBaseAllFaces(Block par1Block, int par2, int par3, int par4) {
		this.renderAllFaces = true;
		this.renderPistonBase(par1Block, par2, par3, par4, true);
		this.renderAllFaces = false;
	}

	private boolean renderPistonBase(Block par1Block, int par2, int par3, int par4, boolean par5) {
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		boolean var7 = par5 || (var6 & 8) != 0;
		int var8 = BlockPistonBase.getOrientation(var6);
		if (var7) {
			switch(var8) {
			case 0:
				this.uvRotateEast = 3;
				this.uvRotateWest = 3;
				this.uvRotateSouth = 3;
				this.uvRotateNorth = 3;
				par1Block.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 1:
				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
				break;
			case 2:
				this.uvRotateSouth = 1;
				this.uvRotateNorth = 2;
				par1Block.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
				break;
			case 3:
				this.uvRotateSouth = 2;
				this.uvRotateNorth = 1;
				this.uvRotateTop = 3;
				this.uvRotateBottom = 3;
				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
				break;
			case 4:
				this.uvRotateEast = 1;
				this.uvRotateWest = 2;
				this.uvRotateTop = 2;
				this.uvRotateBottom = 1;
				par1Block.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 5:
				this.uvRotateEast = 2;
				this.uvRotateWest = 1;
				this.uvRotateTop = 1;
				this.uvRotateBottom = 2;
				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
			}

			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.uvRotateEast = 0;
			this.uvRotateWest = 0;
			this.uvRotateSouth = 0;
			this.uvRotateNorth = 0;
			this.uvRotateTop = 0;
			this.uvRotateBottom = 0;
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			switch(var8) {
			case 0:
				this.uvRotateEast = 3;
				this.uvRotateWest = 3;
				this.uvRotateSouth = 3;
				this.uvRotateNorth = 3;
			case 1:
			default:
				break;
			case 2:
				this.uvRotateSouth = 1;
				this.uvRotateNorth = 2;
				break;
			case 3:
				this.uvRotateSouth = 2;
				this.uvRotateNorth = 1;
				this.uvRotateTop = 3;
				this.uvRotateBottom = 3;
				break;
			case 4:
				this.uvRotateEast = 1;
				this.uvRotateWest = 2;
				this.uvRotateTop = 2;
				this.uvRotateBottom = 1;
				break;
			case 5:
				this.uvRotateEast = 2;
				this.uvRotateWest = 1;
				this.uvRotateTop = 1;
				this.uvRotateBottom = 2;
			}

			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.uvRotateEast = 0;
			this.uvRotateWest = 0;
			this.uvRotateSouth = 0;
			this.uvRotateNorth = 0;
			this.uvRotateTop = 0;
			this.uvRotateBottom = 0;
		}

		return true;
	}

	private void renderPistonRodUD(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
		int var16 = 108;
		if (this.overrideBlockTexture >= 0) {
			var16 = this.overrideBlockTexture;
		}

		int var17 = (var16 & 15) << 4;
		int var18 = var16 & 240;
		Tessellator var19 = Tessellator.instance;
		double var20 = (double)((float)(var17 + 0) / 256.0F);
		double var22 = (double)((float)(var18 + 0) / 256.0F);
		double var24 = ((double)var17 + par14 - 0.01D) / 256.0D;
		double var26 = ((double)((float)var18 + 4.0F) - 0.01D) / 256.0D;
		var19.setColorOpaque_F(par13, par13, par13);
		var19.addVertexWithUV(par1, par7, par9, var24, var22);
		var19.addVertexWithUV(par1, par5, par9, var20, var22);
		var19.addVertexWithUV(par3, par5, par11, var20, var26);
		var19.addVertexWithUV(par3, par7, par11, var24, var26);
	}

	private void renderPistonRodSN(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
		int var16 = 108;
		if (this.overrideBlockTexture >= 0) {
			var16 = this.overrideBlockTexture;
		}

		int var17 = (var16 & 15) << 4;
		int var18 = var16 & 240;
		Tessellator var19 = Tessellator.instance;
		double var20 = (double)((float)(var17 + 0) / 256.0F);
		double var22 = (double)((float)(var18 + 0) / 256.0F);
		double var24 = ((double)var17 + par14 - 0.01D) / 256.0D;
		double var26 = ((double)((float)var18 + 4.0F) - 0.01D) / 256.0D;
		var19.setColorOpaque_F(par13, par13, par13);
		var19.addVertexWithUV(par1, par5, par11, var24, var22);
		var19.addVertexWithUV(par1, par5, par9, var20, var22);
		var19.addVertexWithUV(par3, par7, par9, var20, var26);
		var19.addVertexWithUV(par3, par7, par11, var24, var26);
	}

	private void renderPistonRodEW(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
		int var16 = 108;
		if (this.overrideBlockTexture >= 0) {
			var16 = this.overrideBlockTexture;
		}

		int var17 = (var16 & 15) << 4;
		int var18 = var16 & 240;
		Tessellator var19 = Tessellator.instance;
		double var20 = (double)((float)(var17 + 0) / 256.0F);
		double var22 = (double)((float)(var18 + 0) / 256.0F);
		double var24 = ((double)var17 + par14 - 0.01D) / 256.0D;
		double var26 = ((double)((float)var18 + 4.0F) - 0.01D) / 256.0D;
		var19.setColorOpaque_F(par13, par13, par13);
		var19.addVertexWithUV(par3, par5, par9, var24, var22);
		var19.addVertexWithUV(par1, par5, par9, var20, var22);
		var19.addVertexWithUV(par1, par7, par11, var20, var26);
		var19.addVertexWithUV(par3, par7, par11, var24, var26);
	}

	public void renderPistonExtensionAllFaces(Block par1Block, int par2, int par3, int par4, boolean par5) {
		this.renderAllFaces = true;
		this.renderPistonExtension(par1Block, par2, par3, par4, par5);
		this.renderAllFaces = false;
	}

	private boolean renderPistonExtension(Block par1Block, int par2, int par3, int par4, boolean par5) {
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var7 = BlockPistonExtension.getDirectionMeta(var6);
		float var11 = par1Block.getBlockBrightness(this.blockAccess, par2, par3, par4);
		float var12 = par5?1.0F:0.5F;
		double var13 = par5?16.0D:8.0D;
		switch(var7) {
		case 0:
			this.uvRotateEast = 3;
			this.uvRotateWest = 3;
			this.uvRotateSouth = 3;
			this.uvRotateNorth = 3;
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + var12), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.625F), var11 * 0.8F, var13);
			this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + var12), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.375F), var11 * 0.8F, var13);
			this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + var12), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.625F), var11 * 0.6F, var13);
			this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + var12), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.375F), var11 * 0.6F, var13);
			break;
		case 1:
			par1Block.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.625F), (double)((float)par3 - 0.25F + 1.0F - var12), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.625F), var11 * 0.8F, var13);
			this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.375F), (double)((float)par3 - 0.25F + 1.0F - var12), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.375F), var11 * 0.8F, var13);
			this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.375F), (double)((float)par3 - 0.25F + 1.0F - var12), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.625F), var11 * 0.6F, var13);
			this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.625F), (double)((float)par3 - 0.25F + 1.0F - var12), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.375F), var11 * 0.6F, var13);
			break;
		case 2:
			this.uvRotateSouth = 1;
			this.uvRotateNorth = 2;
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodSN((double)((float)par2 + 0.375F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.25F), (double)((float)par4 + 0.25F + var12), var11 * 0.6F, var13);
			this.renderPistonRodSN((double)((float)par2 + 0.625F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.25F), (double)((float)par4 + 0.25F + var12), var11 * 0.6F, var13);
			this.renderPistonRodSN((double)((float)par2 + 0.375F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.25F), (double)((float)par4 + 0.25F + var12), var11 * 0.5F, var13);
			this.renderPistonRodSN((double)((float)par2 + 0.625F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.25F), (double)((float)par4 + 0.25F + var12), var11, var13);
			break;
		case 3:
			this.uvRotateSouth = 2;
			this.uvRotateNorth = 1;
			this.uvRotateTop = 3;
			this.uvRotateBottom = 3;
			par1Block.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodSN((double)((float)par2 + 0.375F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par4 - 0.25F + 1.0F - var12), (double)((float)par4 - 0.25F + 1.0F), var11 * 0.6F, var13);
			this.renderPistonRodSN((double)((float)par2 + 0.625F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par4 - 0.25F + 1.0F - var12), (double)((float)par4 - 0.25F + 1.0F), var11 * 0.6F, var13);
			this.renderPistonRodSN((double)((float)par2 + 0.375F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.375F), (double)((float)par4 - 0.25F + 1.0F - var12), (double)((float)par4 - 0.25F + 1.0F), var11 * 0.5F, var13);
			this.renderPistonRodSN((double)((float)par2 + 0.625F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.625F), (double)((float)par4 - 0.25F + 1.0F - var12), (double)((float)par4 - 0.25F + 1.0F), var11, var13);
			break;
		case 4:
			this.uvRotateEast = 1;
			this.uvRotateWest = 2;
			this.uvRotateTop = 2;
			this.uvRotateBottom = 1;
			par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodEW((double)((float)par2 + 0.25F), (double)((float)par2 + 0.25F + var12), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.375F), var11 * 0.5F, var13);
			this.renderPistonRodEW((double)((float)par2 + 0.25F), (double)((float)par2 + 0.25F + var12), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.625F), var11, var13);
			this.renderPistonRodEW((double)((float)par2 + 0.25F), (double)((float)par2 + 0.25F + var12), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.375F), var11 * 0.6F, var13);
			this.renderPistonRodEW((double)((float)par2 + 0.25F), (double)((float)par2 + 0.25F + var12), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.625F), var11 * 0.6F, var13);
			break;
		case 5:
			this.uvRotateEast = 2;
			this.uvRotateWest = 1;
			this.uvRotateTop = 1;
			this.uvRotateBottom = 2;
			par1Block.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.renderPistonRodEW((double)((float)par2 - 0.25F + 1.0F - var12), (double)((float)par2 - 0.25F + 1.0F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.375F), var11 * 0.5F, var13);
			this.renderPistonRodEW((double)((float)par2 - 0.25F + 1.0F - var12), (double)((float)par2 - 0.25F + 1.0F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.625F), var11, var13);
			this.renderPistonRodEW((double)((float)par2 - 0.25F + 1.0F - var12), (double)((float)par2 - 0.25F + 1.0F), (double)((float)par3 + 0.375F), (double)((float)par3 + 0.625F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.375F), var11 * 0.6F, var13);
			this.renderPistonRodEW((double)((float)par2 - 0.25F + 1.0F - var12), (double)((float)par2 - 0.25F + 1.0F), (double)((float)par3 + 0.625F), (double)((float)par3 + 0.375F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.625F), var11 * 0.6F, var13);
		}

		this.uvRotateEast = 0;
		this.uvRotateWest = 0;
		this.uvRotateSouth = 0;
		this.uvRotateNorth = 0;
		this.uvRotateTop = 0;
		this.uvRotateBottom = 0;
		par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}

	public boolean renderBlockLever(Block par1Block, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var6 = var5 & 7;
		boolean var7 = (var5 & 8) > 0;
		Tessellator var8 = Tessellator.instance;
		boolean var9 = this.overrideBlockTexture >= 0;
		if (!var9) {
			this.overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
		}

		float var10 = 0.25F;
		float var11 = 0.1875F;
		float var12 = 0.1875F;
		if (var6 == 5) {
			par1Block.setBlockBounds(0.5F - var11, 0.0F, 0.5F - var10, 0.5F + var11, var12, 0.5F + var10);
		} else if (var6 == 6) {
			par1Block.setBlockBounds(0.5F - var10, 0.0F, 0.5F - var11, 0.5F + var10, var12, 0.5F + var11);
		} else if (var6 == 4) {
			par1Block.setBlockBounds(0.5F - var11, 0.5F - var10, 1.0F - var12, 0.5F + var11, 0.5F + var10, 1.0F);
		} else if (var6 == 3) {
			par1Block.setBlockBounds(0.5F - var11, 0.5F - var10, 0.0F, 0.5F + var11, 0.5F + var10, var12);
		} else if (var6 == 2) {
			par1Block.setBlockBounds(1.0F - var12, 0.5F - var10, 0.5F - var11, 1.0F, 0.5F + var10, 0.5F + var11);
		} else if (var6 == 1) {
			par1Block.setBlockBounds(0.0F, 0.5F - var10, 0.5F - var11, var12, 0.5F + var10, 0.5F + var11);
		}

		this.renderStandardBlock(par1Block, par2, par3, par4);
		if (!var9) {
			this.overrideBlockTexture = -1;
		}

		var8.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var13 = 1.0F;
		if (Block.lightValue[par1Block.blockID] > 0) {
			var13 = 1.0F;
		}

		var8.setColorOpaque_F(var13, var13, var13);
		int var14 = par1Block.getBlockTextureFromSide(0);
		if (this.overrideBlockTexture >= 0) {
			var14 = this.overrideBlockTexture;
		}

		int var15 = (var14 & 15) << 4;
		int var16 = var14 & 240;
		float var17 = (float)var15 / 256.0F;
		float var18 = ((float)var15 + 15.99F) / 256.0F;
		float var19 = (float)var16 / 256.0F;
		float var20 = ((float)var16 + 15.99F) / 256.0F;
		Vec3D[] var21 = new Vec3D[8];
		float var22 = 0.0625F;
		float var23 = 0.0625F;
		float var24 = 0.625F;
		var21[0] = Vec3D.createVector((double)(-var22), 0.0D, (double)(-var23));
		var21[1] = Vec3D.createVector((double)var22, 0.0D, (double)(-var23));
		var21[2] = Vec3D.createVector((double)var22, 0.0D, (double)var23);
		var21[3] = Vec3D.createVector((double)(-var22), 0.0D, (double)var23);
		var21[4] = Vec3D.createVector((double)(-var22), (double)var24, (double)(-var23));
		var21[5] = Vec3D.createVector((double)var22, (double)var24, (double)(-var23));
		var21[6] = Vec3D.createVector((double)var22, (double)var24, (double)var23);
		var21[7] = Vec3D.createVector((double)(-var22), (double)var24, (double)var23);

		for (int var25 = 0; var25 < 8; ++var25) {
			if (var7) {
				var21[var25].zCoord -= 0.0625D;
				var21[var25].rotateAroundX(0.69813174F);
			} else {
				var21[var25].zCoord += 0.0625D;
				var21[var25].rotateAroundX(-0.69813174F);
			}

			if (var6 == 6) {
				var21[var25].rotateAroundY(1.5707964F);
			}

			if (var6 < 5) {
				var21[var25].yCoord -= 0.375D;
				var21[var25].rotateAroundX(1.5707964F);
				if (var6 == 4) {
					var21[var25].rotateAroundY(0.0F);
				}

				if (var6 == 3) {
					var21[var25].rotateAroundY(3.1415927F);
				}

				if (var6 == 2) {
					var21[var25].rotateAroundY(1.5707964F);
				}

				if (var6 == 1) {
					var21[var25].rotateAroundY(-1.5707964F);
				}

				var21[var25].xCoord += (double)par2 + 0.5D;
				var21[var25].yCoord += (double)((float)par3 + 0.5F);
				var21[var25].zCoord += (double)par4 + 0.5D;
			} else {
				var21[var25].xCoord += (double)par2 + 0.5D;
				var21[var25].yCoord += (double)((float)par3 + 0.125F);
				var21[var25].zCoord += (double)par4 + 0.5D;
			}
		}

		Vec3D var30 = null;
		Vec3D var26 = null;
		Vec3D var27 = null;
		Vec3D var28 = null;

		for (int var29 = 0; var29 < 6; ++var29) {
			if (var29 == 0) {
				var17 = (float)(var15 + 7) / 256.0F;
				var18 = ((float)(var15 + 9) - 0.01F) / 256.0F;
				var19 = (float)(var16 + 6) / 256.0F;
				var20 = ((float)(var16 + 8) - 0.01F) / 256.0F;
			} else if (var29 == 2) {
				var17 = (float)(var15 + 7) / 256.0F;
				var18 = ((float)(var15 + 9) - 0.01F) / 256.0F;
				var19 = (float)(var16 + 6) / 256.0F;
				var20 = ((float)(var16 + 16) - 0.01F) / 256.0F;
			}

			if (var29 == 0) {
				var30 = var21[0];
				var26 = var21[1];
				var27 = var21[2];
				var28 = var21[3];
			} else if (var29 == 1) {
				var30 = var21[7];
				var26 = var21[6];
				var27 = var21[5];
				var28 = var21[4];
			} else if (var29 == 2) {
				var30 = var21[1];
				var26 = var21[0];
				var27 = var21[4];
				var28 = var21[5];
			} else if (var29 == 3) {
				var30 = var21[2];
				var26 = var21[1];
				var27 = var21[5];
				var28 = var21[6];
			} else if (var29 == 4) {
				var30 = var21[3];
				var26 = var21[2];
				var27 = var21[6];
				var28 = var21[7];
			} else if (var29 == 5) {
				var30 = var21[0];
				var26 = var21[3];
				var27 = var21[7];
				var28 = var21[4];
			}

			var8.addVertexWithUV(var30.xCoord, var30.yCoord, var30.zCoord, (double)var17, (double)var20);
			var8.addVertexWithUV(var26.xCoord, var26.yCoord, var26.zCoord, (double)var18, (double)var20);
			var8.addVertexWithUV(var27.xCoord, var27.yCoord, var27.zCoord, (double)var18, (double)var19);
			var8.addVertexWithUV(var28.xCoord, var28.yCoord, var28.zCoord, (double)var17, (double)var19);
		}

		return true;
	}

	public boolean renderBlockFire(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = par1Block.getBlockTextureFromSide(0);
		if (this.overrideBlockTexture >= 0) {
			var6 = this.overrideBlockTexture;
		}

		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		int var7 = (var6 & 15) << 4;
		int var8 = var6 & 240;
		double var9 = (double)((float)var7 / 256.0F);
		double var11 = (double)(((float)var7 + 15.99F) / 256.0F);
		double var13 = (double)((float)var8 / 256.0F);
		double var15 = (double)(((float)var8 + 15.99F) / 256.0F);
		float var17 = 1.4F;
		double var20;
		double var22;
		double var24;
		double var26;
		double var28;
		double var30;
		double var32;
		if (!this.blockAccess.isBlockNormalCube(par2, par3 - 1, par4) && !Block.fire.canBlockCatchFire(this.blockAccess, par2, par3 - 1, par4)) {
			float var36 = 0.2F;
			float var19 = 0.0625F;
			if ((par2 + par3 + par4 & 1) == 1) {
				var9 = (double)((float)var7 / 256.0F);
				var11 = (double)(((float)var7 + 15.99F) / 256.0F);
				var13 = (double)((float)(var8 + 16) / 256.0F);
				var15 = (double)(((float)var8 + 15.99F + 16.0F) / 256.0F);
			}

			if ((par2 / 2 + par3 / 2 + par4 / 2 & 1) == 1) {
				var20 = var11;
				var11 = var9;
				var9 = var20;
			}

			if (Block.fire.canBlockCatchFire(this.blockAccess, par2 - 1, par3, par4)) {
				var5.addVertexWithUV((double)((float)par2 + var36), (double)((float)par3 + var17 + var19), (double)(par4 + 1), var11, var13);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1), var11, var15);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
				var5.addVertexWithUV((double)((float)par2 + var36), (double)((float)par3 + var17 + var19), (double)(par4 + 0), var9, var13);
				var5.addVertexWithUV((double)((float)par2 + var36), (double)((float)par3 + var17 + var19), (double)(par4 + 0), var9, var13);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1), var11, var15);
				var5.addVertexWithUV((double)((float)par2 + var36), (double)((float)par3 + var17 + var19), (double)(par4 + 1), var11, var13);
			}

			if (Block.fire.canBlockCatchFire(this.blockAccess, par2 + 1, par3, par4)) {
				var5.addVertexWithUV((double)((float)(par2 + 1) - var36), (double)((float)par3 + var17 + var19), (double)(par4 + 0), var9, var13);
				var5.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
				var5.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1), var11, var15);
				var5.addVertexWithUV((double)((float)(par2 + 1) - var36), (double)((float)par3 + var17 + var19), (double)(par4 + 1), var11, var13);
				var5.addVertexWithUV((double)((float)(par2 + 1) - var36), (double)((float)par3 + var17 + var19), (double)(par4 + 1), var11, var13);
				var5.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1), var11, var15);
				var5.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
				var5.addVertexWithUV((double)((float)(par2 + 1) - var36), (double)((float)par3 + var17 + var19), (double)(par4 + 0), var9, var13);
			}

			if (Block.fire.canBlockCatchFire(this.blockAccess, par2, par3, par4 - 1)) {
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17 + var19), (double)((float)par4 + var36), var11, var13);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var11, var15);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17 + var19), (double)((float)par4 + var36), var9, var13);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17 + var19), (double)((float)par4 + var36), var9, var13);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var11, var15);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17 + var19), (double)((float)par4 + var36), var11, var13);
			}

			if (Block.fire.canBlockCatchFire(this.blockAccess, par2, par3, par4 + 1)) {
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17 + var19), (double)((float)(par4 + 1) - var36), var9, var13);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + var19), (double)(par4 + 1 - 0), var9, var15);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1 - 0), var11, var15);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17 + var19), (double)((float)(par4 + 1) - var36), var11, var13);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17 + var19), (double)((float)(par4 + 1) - var36), var11, var13);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1 - 0), var11, var15);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + var19), (double)(par4 + 1 - 0), var9, var15);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17 + var19), (double)((float)(par4 + 1) - var36), var9, var13);
			}

			if (Block.fire.canBlockCatchFire(this.blockAccess, par2, par3 + 1, par4)) {
				var20 = (double)par2 + 0.5D + 0.5D;
				var22 = (double)par2 + 0.5D - 0.5D;
				var24 = (double)par4 + 0.5D + 0.5D;
				var26 = (double)par4 + 0.5D - 0.5D;
				var28 = (double)par2 + 0.5D - 0.5D;
				var30 = (double)par2 + 0.5D + 0.5D;
				var32 = (double)par4 + 0.5D - 0.5D;
				double var34 = (double)par4 + 0.5D + 0.5D;
				var9 = (double)((float)var7 / 256.0F);
				var11 = (double)(((float)var7 + 15.99F) / 256.0F);
				var13 = (double)((float)var8 / 256.0F);
				var15 = (double)(((float)var8 + 15.99F) / 256.0F);
				++par3;
				var17 = -0.2F;
				if ((par2 + par3 + par4 & 1) == 0) {
					var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 0), var11, var13);
					var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 0), var11, var15);
					var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 1), var9, var15);
					var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 1), var9, var13);
					var9 = (double)((float)var7 / 256.0F);
					var11 = (double)(((float)var7 + 15.99F) / 256.0F);
					var13 = (double)((float)(var8 + 16) / 256.0F);
					var15 = (double)(((float)var8 + 15.99F + 16.0F) / 256.0F);
					var5.addVertexWithUV(var30, (double)((float)par3 + var17), (double)(par4 + 1), var11, var13);
					var5.addVertexWithUV(var22, (double)(par3 + 0), (double)(par4 + 1), var11, var15);
					var5.addVertexWithUV(var22, (double)(par3 + 0), (double)(par4 + 0), var9, var15);
					var5.addVertexWithUV(var30, (double)((float)par3 + var17), (double)(par4 + 0), var9, var13);
				} else {
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var34, var11, var13);
					var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var26, var11, var15);
					var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var26, var9, var15);
					var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var34, var9, var13);
					var9 = (double)((float)var7 / 256.0F);
					var11 = (double)(((float)var7 + 15.99F) / 256.0F);
					var13 = (double)((float)(var8 + 16) / 256.0F);
					var15 = (double)(((float)var8 + 15.99F + 16.0F) / 256.0F);
					var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var32, var11, var13);
					var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var24, var11, var15);
					var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var24, var9, var15);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var32, var9, var13);
				}
			}
		} else {
			double var18 = (double)par2 + 0.5D + 0.2D;
			var20 = (double)par2 + 0.5D - 0.2D;
			var22 = (double)par4 + 0.5D + 0.2D;
			var24 = (double)par4 + 0.5D - 0.2D;
			var26 = (double)par2 + 0.5D - 0.3D;
			var28 = (double)par2 + 0.5D + 0.3D;
			var30 = (double)par4 + 0.5D - 0.3D;
			var32 = (double)par4 + 0.5D + 0.3D;
			var5.addVertexWithUV(var26, (double)((float)par3 + var17), (double)(par4 + 1), var11, var13);
			var5.addVertexWithUV(var18, (double)(par3 + 0), (double)(par4 + 1), var11, var15);
			var5.addVertexWithUV(var18, (double)(par3 + 0), (double)(par4 + 0), var9, var15);
			var5.addVertexWithUV(var26, (double)((float)par3 + var17), (double)(par4 + 0), var9, var13);
			var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 0), var11, var13);
			var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 0), var11, var15);
			var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 1), var9, var15);
			var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 1), var9, var13);
			var9 = (double)((float)var7 / 256.0F);
			var11 = (double)(((float)var7 + 15.99F) / 256.0F);
			var13 = (double)((float)(var8 + 16) / 256.0F);
			var15 = (double)(((float)var8 + 15.99F + 16.0F) / 256.0F);
			var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var32, var11, var13);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var24, var11, var15);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var24, var9, var15);
			var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var32, var9, var13);
			var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var30, var11, var13);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var22, var11, var15);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var22, var9, var15);
			var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var30, var9, var13);
			var18 = (double)par2 + 0.5D - 0.5D;
			var20 = (double)par2 + 0.5D + 0.5D;
			var22 = (double)par4 + 0.5D - 0.5D;
			var24 = (double)par4 + 0.5D + 0.5D;
			var26 = (double)par2 + 0.5D - 0.4D;
			var28 = (double)par2 + 0.5D + 0.4D;
			var30 = (double)par4 + 0.5D - 0.4D;
			var32 = (double)par4 + 0.5D + 0.4D;
			var5.addVertexWithUV(var26, (double)((float)par3 + var17), (double)(par4 + 0), var9, var13);
			var5.addVertexWithUV(var18, (double)(par3 + 0), (double)(par4 + 0), var9, var15);
			var5.addVertexWithUV(var18, (double)(par3 + 0), (double)(par4 + 1), var11, var15);
			var5.addVertexWithUV(var26, (double)((float)par3 + var17), (double)(par4 + 1), var11, var13);
			var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 1), var9, var13);
			var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 1), var9, var15);
			var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 0), var11, var15);
			var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 0), var11, var13);
			var9 = (double)((float)var7 / 256.0F);
			var11 = (double)(((float)var7 + 15.99F) / 256.0F);
			var13 = (double)((float)var8 / 256.0F);
			var15 = (double)(((float)var8 + 15.99F) / 256.0F);
			var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var32, var9, var13);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var24, var9, var15);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var24, var11, var15);
			var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var32, var11, var13);
			var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var30, var9, var13);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var22, var9, var15);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var22, var11, var15);
			var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var30, var11, var13);
		}

		return true;
	}

	public boolean renderBlockRedstoneWire(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var7 = par1Block.getBlockTextureFromSideAndMetadata(1, var6);
		if (this.overrideBlockTexture >= 0) {
			var7 = this.overrideBlockTexture;
		}

		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var8 = 1.0F;
//Spout HD Start
		float var10;
		float var11;
		float var12;
		if (Colorizer.computeRedstoneWireColor(var6)) {
			var10 = Colorizer.setColor[0];
			var11 = Colorizer.setColor[1];
			var12 = Colorizer.setColor[2];
		} else {
			float var9 = (float)var6 / 15.0F;
			var10 = var9 * 0.6F + 0.4F;
			if (var6 == 0) {
				var10 = 0.3F;
			}

			var11 = var9 * var9 * 0.7F - 0.5F;
			var12 = var9 * var9 * 0.6F - 0.7F;
		}
//Spout HD end
		if (var11 < 0.0F) {
			var11 = 0.0F;
		}

		if (var12 < 0.0F) {
			var12 = 0.0F;
		}

		var5.setColorOpaque_F(var10, var11, var12);
		int var13 = (var7 & 15) << 4;
		int var14 = var7 & 240;
		double var15 = (double)((float)var13 / 256.0F);
		double var17 = (double)(((float)var13 + 15.99F) / 256.0F);
		double var19 = (double)((float)var14 / 256.0F);
		double var21 = (double)(((float)var14 + 15.99F) / 256.0F);
		boolean var29 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3, par4, 1) || !this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3 - 1, par4, -1);
		boolean var30 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3, par4, 3) || !this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3 - 1, par4, -1);
		boolean var31 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3, par4 - 1, 2) || !this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 - 1, par4 - 1, -1);
		boolean var32 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3, par4 + 1, 0) || !this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 - 1, par4 + 1, -1);
		if (!this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
			if (this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3 + 1, par4, -1)) {
				var29 = true;
			}

			if (this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3 + 1, par4, -1)) {
				var30 = true;
			}

			if (this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 + 1, par4 - 1, -1)) {
				var31 = true;
			}

			if (this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 + 1, par4 + 1, -1)) {
				var32 = true;
			}
		}

		float var34 = (float)(par2 + 0);
		float var35 = (float)(par2 + 1);
		float var36 = (float)(par4 + 0);
		float var37 = (float)(par4 + 1);
		byte var38 = 0;
		if ((var29 || var30) && !var31 && !var32) {
			var38 = 1;
		}

		if ((var31 || var32) && !var30 && !var29) {
			var38 = 2;
		}

		if (var38 != 0) {
			var15 = (double)((float)(var13 + 16) / 256.0F);
			var17 = (double)(((float)(var13 + 16) + 15.99F) / 256.0F);
			var19 = (double)((float)var14 / 256.0F);
			var21 = (double)(((float)var14 + 15.99F) / 256.0F);
		}

		if (var38 == 0) {
			if (!var29) {
				var34 += 0.3125F;
			}

			if (!var29) {
				var15 += 0.01953125D;
			}

			if (!var30) {
				var35 -= 0.3125F;
			}

			if (!var30) {
				var17 -= 0.01953125D;
			}

			if (!var31) {
				var36 += 0.3125F;
			}

			if (!var31) {
				var19 += 0.01953125D;
			}

			if (!var32) {
				var37 -= 0.3125F;
			}

			if (!var32) {
				var21 -= 0.01953125D;
			}

			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var37, var17, var21);
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var36, var17, var19);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var36, var15, var19);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var37, var15, var21);
			var5.setColorOpaque_F(var8, var8, var8);
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var37, var17, var21 + 0.0625D);
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var36, var17, var19 + 0.0625D);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var36, var15, var19 + 0.0625D);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var37, var15, var21 + 0.0625D);
		} else if (var38 == 1) {
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var37, var17, var21);
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var36, var17, var19);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var36, var15, var19);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var37, var15, var21);
			var5.setColorOpaque_F(var8, var8, var8);
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var37, var17, var21 + 0.0625D);
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var36, var17, var19 + 0.0625D);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var36, var15, var19 + 0.0625D);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var37, var15, var21 + 0.0625D);
		} else if (var38 == 2) {
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var37, var17, var21);
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var36, var15, var21);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var36, var15, var19);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var37, var17, var19);
			var5.setColorOpaque_F(var8, var8, var8);
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var37, var17, var21 + 0.0625D);
			var5.addVertexWithUV((double)var35, (double)par3 + 0.015625D, (double)var36, var15, var21 + 0.0625D);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var36, var15, var19 + 0.0625D);
			var5.addVertexWithUV((double)var34, (double)par3 + 0.015625D, (double)var37, var17, var19 + 0.0625D);
		}

		if (!this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
			var15 = (double)((float)(var13 + 16) / 256.0F);
			var17 = (double)(((float)(var13 + 16) + 15.99F) / 256.0F);
			var19 = (double)((float)var14 / 256.0F);
			var21 = (double)(((float)var14 + 15.99F) / 256.0F);
			if (this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4) == Block.redstoneWire.blockID) {
				var5.setColorOpaque_F(var8 * var10, var8 * var11, var8 * var12);
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), var17, var19);
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 1), var15, var19);
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 0), var15, var21);
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), var17, var21);
				var5.setColorOpaque_F(var8, var8, var8);
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), var17, var19 + 0.0625D);
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 1), var15, var19 + 0.0625D);
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 0), var15, var21 + 0.0625D);
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), var17, var21 + 0.0625D);
			}

			if (this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4) == Block.redstoneWire.blockID) {
				var5.setColorOpaque_F(var8 * var10, var8 * var11, var8 * var12);
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 1), var15, var21);
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), var17, var21);
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), var17, var19);
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 0), var15, var19);
				var5.setColorOpaque_F(var8, var8, var8);
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 1), var15, var21 + 0.0625D);
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), var17, var21 + 0.0625D);
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), var17, var19 + 0.0625D);
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 0), var15, var19 + 0.0625D);
			}

			if (this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1) == Block.redstoneWire.blockID) {
				var5.setColorOpaque_F(var8 * var10, var8 * var11, var8 * var12);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + 0.015625D, var15, var21);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, var17, var21);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, var17, var19);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + 0.015625D, var15, var19);
				var5.setColorOpaque_F(var8, var8, var8);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + 0.015625D, var15, var21 + 0.0625D);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, var17, var21 + 0.0625D);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, var17, var19 + 0.0625D);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + 0.015625D, var15, var19 + 0.0625D);
			}

			if (this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1) == Block.redstoneWire.blockID) {
				var5.setColorOpaque_F(var8 * var10, var8 * var11, var8 * var12);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, var17, var19);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, var15, var19);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, var15, var21);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, var17, var21);
				var5.setColorOpaque_F(var8, var8, var8);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, var17, var19 + 0.0625D);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, var15, var19 + 0.0625D);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, var15, var21 + 0.0625D);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, var17, var21 + 0.0625D);
			}
		}

		return true;
	}

	public boolean renderBlockMinecartTrack(BlockRail par1BlockRail, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var7 = par1BlockRail.getBlockTextureFromSideAndMetadata(0, var6);
		if (this.overrideBlockTexture >= 0) {
			var7 = this.overrideBlockTexture;
		}

		if (par1BlockRail.isPowered()) {
			var6 &= 7;
		}

		var5.setBrightness(par1BlockRail.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		int var8 = (var7 & 15) << 4;
		int var9 = var7 & 240;
		double var10 = (double)((float)var8 / 256.0F);
		double var12 = (double)(((float)var8 + 15.99F) / 256.0F);
		double var14 = (double)((float)var9 / 256.0F);
		double var16 = (double)(((float)var9 + 15.99F) / 256.0F);
		double var18 = 0.0625D;
		double var20 = (double)(par2 + 1);
		double var22 = (double)(par2 + 1);
		double var24 = (double)(par2 + 0);
		double var26 = (double)(par2 + 0);
		double var28 = (double)(par4 + 0);
		double var30 = (double)(par4 + 1);
		double var32 = (double)(par4 + 1);
		double var34 = (double)(par4 + 0);
		double var36 = (double)par3 + var18;
		double var38 = (double)par3 + var18;
		double var40 = (double)par3 + var18;
		double var42 = (double)par3 + var18;
		if (var6 != 1 && var6 != 2 && var6 != 3 && var6 != 7) {
			if (var6 == 8) {
				var20 = var22 = (double)(par2 + 0);
				var24 = var26 = (double)(par2 + 1);
				var28 = var34 = (double)(par4 + 1);
				var30 = var32 = (double)(par4 + 0);
			} else if (var6 == 9) {
				var20 = var26 = (double)(par2 + 0);
				var22 = var24 = (double)(par2 + 1);
				var28 = var30 = (double)(par4 + 0);
				var32 = var34 = (double)(par4 + 1);
			}
		} else {
			var20 = var26 = (double)(par2 + 1);
			var22 = var24 = (double)(par2 + 0);
			var28 = var30 = (double)(par4 + 1);
			var32 = var34 = (double)(par4 + 0);
		}

		if (var6 != 2 && var6 != 4) {
			if (var6 == 3 || var6 == 5) {
				++var38;
				++var40;
			}
		} else {
			++var36;
			++var42;
		}

		var5.addVertexWithUV(var20, var36, var28, var12, var14);
		var5.addVertexWithUV(var22, var38, var30, var12, var16);
		var5.addVertexWithUV(var24, var40, var32, var10, var16);
		var5.addVertexWithUV(var26, var42, var34, var10, var14);
		var5.addVertexWithUV(var26, var42, var34, var10, var14);
		var5.addVertexWithUV(var24, var40, var32, var10, var16);
		var5.addVertexWithUV(var22, var38, var30, var12, var16);
		var5.addVertexWithUV(var20, var36, var28, var12, var14);
		return true;
	}

	public boolean renderBlockLadder(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = par1Block.getBlockTextureFromSide(0);
		if (this.overrideBlockTexture >= 0) {
			var6 = this.overrideBlockTexture;
		}

		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var7 = 1.0F;
		var5.setColorOpaque_F(var7, var7, var7);
		int var22 = (var6 & 15) << 4;
		int var8 = var6 & 240;
		double var9 = (double)((float)var22 / 256.0F);
		double var11 = (double)(((float)var22 + 15.99F) / 256.0F);
		double var13 = (double)((float)var8 / 256.0F);
		double var15 = (double)(((float)var8 + 15.99F) / 256.0F);
		int var17 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		double var18 = 0.0D;
		double var20 = 0.05000000074505806D;
		if (var17 == 5) {
			var5.addVertexWithUV((double)par2 + var20, (double)(par3 + 1) + var18, (double)(par4 + 1) + var18, var9, var13);
			var5.addVertexWithUV((double)par2 + var20, (double)(par3 + 0) - var18, (double)(par4 + 1) + var18, var9, var15);
			var5.addVertexWithUV((double)par2 + var20, (double)(par3 + 0) - var18, (double)(par4 + 0) - var18, var11, var15);
			var5.addVertexWithUV((double)par2 + var20, (double)(par3 + 1) + var18, (double)(par4 + 0) - var18, var11, var13);
		}

		if (var17 == 4) {
			var5.addVertexWithUV((double)(par2 + 1) - var20, (double)(par3 + 0) - var18, (double)(par4 + 1) + var18, var11, var15);
			var5.addVertexWithUV((double)(par2 + 1) - var20, (double)(par3 + 1) + var18, (double)(par4 + 1) + var18, var11, var13);
			var5.addVertexWithUV((double)(par2 + 1) - var20, (double)(par3 + 1) + var18, (double)(par4 + 0) - var18, var9, var13);
			var5.addVertexWithUV((double)(par2 + 1) - var20, (double)(par3 + 0) - var18, (double)(par4 + 0) - var18, var9, var15);
		}

		if (var17 == 3) {
			var5.addVertexWithUV((double)(par2 + 1) + var18, (double)(par3 + 0) - var18, (double)par4 + var20, var11, var15);
			var5.addVertexWithUV((double)(par2 + 1) + var18, (double)(par3 + 1) + var18, (double)par4 + var20, var11, var13);
			var5.addVertexWithUV((double)(par2 + 0) - var18, (double)(par3 + 1) + var18, (double)par4 + var20, var9, var13);
			var5.addVertexWithUV((double)(par2 + 0) - var18, (double)(par3 + 0) - var18, (double)par4 + var20, var9, var15);
		}

		if (var17 == 2) {
			var5.addVertexWithUV((double)(par2 + 1) + var18, (double)(par3 + 1) + var18, (double)(par4 + 1) - var20, var9, var13);
			var5.addVertexWithUV((double)(par2 + 1) + var18, (double)(par3 + 0) - var18, (double)(par4 + 1) - var20, var9, var15);
			var5.addVertexWithUV((double)(par2 + 0) - var18, (double)(par3 + 0) - var18, (double)(par4 + 1) - var20, var11, var15);
			var5.addVertexWithUV((double)(par2 + 0) - var18, (double)(par3 + 1) + var18, (double)(par4 + 1) - var20, var11, var13);
		}

		return true;
	}

	public boolean renderBlockVine(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = par1Block.getBlockTextureFromSide(0);
		if (this.overrideBlockTexture >= 0) {
			var6 = this.overrideBlockTexture;
		}

		float var7 = 1.0F;
		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		int var8 = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var9 = (float)(var8 >> 16 & 255) / 255.0F;
		float var10 = (float)(var8 >> 8 & 255) / 255.0F;
		float var11 = (float)(var8 & 255) / 255.0F;
		var5.setColorOpaque_F(var7 * var9, var7 * var10, var7 * var11);
		var8 = (var6 & 15) << 4;
		int var21 = var6 & 240;
		double var22 = (double)((float)var8 / 256.0F);
		double var12 = (double)(((float)var8 + 15.99F) / 256.0F);
		double var14 = (double)((float)var21 / 256.0F);
		double var16 = (double)(((float)var21 + 15.99F) / 256.0F);
		double var18 = 0.05000000074505806D;
		int var20 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		if ((var20 & 2) != 0) {
			var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 1), (double)(par4 + 1), var22, var14);
			var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 0), (double)(par4 + 1), var22, var16);
			var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 0), (double)(par4 + 0), var12, var16);
			var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 1), (double)(par4 + 0), var12, var14);
			var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 1), (double)(par4 + 0), var12, var14);
			var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 0), (double)(par4 + 0), var12, var16);
			var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 0), (double)(par4 + 1), var22, var16);
			var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 1), (double)(par4 + 1), var22, var14);
		}

		if ((var20 & 8) != 0) {
			var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 0), (double)(par4 + 1), var12, var16);
			var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 1), (double)(par4 + 1), var12, var14);
			var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 1), (double)(par4 + 0), var22, var14);
			var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 0), (double)(par4 + 0), var22, var16);
			var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 0), (double)(par4 + 0), var22, var16);
			var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 1), (double)(par4 + 0), var22, var14);
			var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 1), (double)(par4 + 1), var12, var14);
			var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 0), (double)(par4 + 1), var12, var16);
		}

		if ((var20 & 4) != 0) {
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + var18, var12, var16);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)par4 + var18, var12, var14);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)par4 + var18, var22, var14);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + var18, var22, var16);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + var18, var22, var16);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)par4 + var18, var22, var14);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)par4 + var18, var12, var14);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + var18, var12, var16);
		}

		if ((var20 & 1) != 0) {
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)(par4 + 1) - var18, var22, var14);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - var18, var22, var16);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - var18, var12, var16);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)(par4 + 1) - var18, var12, var14);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)(par4 + 1) - var18, var12, var14);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - var18, var12, var16);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - var18, var22, var16);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)(par4 + 1) - var18, var22, var14);
		}

		if (this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1) - var18, (double)(par4 + 0), var22, var14);
			var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1) - var18, (double)(par4 + 1), var22, var16);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1) - var18, (double)(par4 + 1), var12, var16);
			var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1) - var18, (double)(par4 + 0), var12, var14);
		}

		return true;
	}

	public boolean renderBlockPane(BlockPane par1BlockPane, int par2, int par3, int par4) {
		int var5 = this.blockAccess.func_48453_b();
		Tessellator var6 = Tessellator.instance;
		var6.setBrightness(par1BlockPane.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var7 = 1.0F;
		int var8 = par1BlockPane.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var9 = (float)(var8 >> 16 & 255) / 255.0F;
		float var10 = (float)(var8 >> 8 & 255) / 255.0F;
		float var11 = (float)(var8 & 255) / 255.0F;
		if (EntityRenderer.anaglyphEnable) {
			float var12 = (var9 * 30.0F + var10 * 59.0F + var11 * 11.0F) / 100.0F;
			float var13 = (var9 * 30.0F + var10 * 70.0F) / 100.0F;
			float var14 = (var9 * 30.0F + var11 * 70.0F) / 100.0F;
			var9 = var12;
			var10 = var13;
			var11 = var14;
		}

		var6.setColorOpaque_F(var7 * var9, var7 * var10, var7 * var11);
		boolean var64 = false;
		boolean var67 = false;
		int var68;
		int var65;
		int var66;
		if (this.overrideBlockTexture >= 0) {
			var65 = this.overrideBlockTexture;
			var66 = this.overrideBlockTexture;
		} else {
			var68 = this.blockAccess.getBlockMetadata(par2, par3, par4);
			var65 = par1BlockPane.getBlockTextureFromSideAndMetadata(0, var68);
			var66 = par1BlockPane.getSideTextureIndex();
		}

		var68 = (var65 & 15) << 4;
		int var15 = var65 & 240;
		double var16 = (double)((float)var68 / 256.0F);
		double var18 = (double)(((float)var68 + 7.99F) / 256.0F);
		double var20 = (double)(((float)var68 + 15.99F) / 256.0F);
		double var22 = (double)((float)var15 / 256.0F);
		double var24 = (double)(((float)var15 + 15.99F) / 256.0F);
		int var26 = (var66 & 15) << 4;
		int var27 = var66 & 240;
		double var28 = (double)((float)(var26 + 7) / 256.0F);
		double var30 = (double)(((float)var26 + 8.99F) / 256.0F);
		double var32 = (double)((float)var27 / 256.0F);
		double var34 = (double)((float)(var27 + 8) / 256.0F);
		double var36 = (double)(((float)var27 + 15.99F) / 256.0F);
		double var38 = (double)par2;
		double var40 = (double)par2 + 0.5D;
		double var42 = (double)(par2 + 1);
		double var44 = (double)par4;
		double var46 = (double)par4 + 0.5D;
		double var48 = (double)(par4 + 1);
		double var50 = (double)par2 + 0.5D - 0.0625D;
		double var52 = (double)par2 + 0.5D + 0.0625D;
		double var54 = (double)par4 + 0.5D - 0.0625D;
		double var56 = (double)par4 + 0.5D + 0.0625D;
		boolean var58 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2, par3, par4 - 1));
		boolean var59 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2, par3, par4 + 1));
		boolean var60 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2 - 1, par3, par4));
		boolean var61 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2 + 1, par3, par4));
		boolean var62 = par1BlockPane.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1);
		boolean var63 = par1BlockPane.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0);
		if ((!var60 || !var61) && (var60 || var61 || var58 || var59)) {
			if (var60 && !var61) {
				var6.addVertexWithUV(var38, (double)(par3 + 1), var46, var16, var22);
				var6.addVertexWithUV(var38, (double)(par3 + 0), var46, var16, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var18, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var18, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var16, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var16, var24);
				var6.addVertexWithUV(var38, (double)(par3 + 0), var46, var18, var24);
				var6.addVertexWithUV(var38, (double)(par3 + 1), var46, var18, var22);
				if (!var59 && !var58) {
					var6.addVertexWithUV(var40, (double)(par3 + 1), var56, var28, var32);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var56, var28, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var54, var30, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var54, var30, var32);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var54, var28, var32);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var54, var28, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var56, var30, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var56, var30, var32);
				}

				if (var62 || par3 < var5 - 1 && this.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4)) {
					var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var56, var30, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var54, var28, var36);
					var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var56, var30, var36);
					var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var54, var28, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var54, var28, var34);
				}

				if (var63 || par3 > 1 && this.blockAccess.isAirBlock(par2 - 1, par3 - 1, par4)) {
					var6.addVertexWithUV(var38, (double)par3 - 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var56, var30, var36);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var54, var28, var36);
					var6.addVertexWithUV(var38, (double)par3 - 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var38, (double)par3 - 0.01D, var56, var30, var36);
					var6.addVertexWithUV(var38, (double)par3 - 0.01D, var54, var28, var36);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var54, var28, var34);
				}
			} else if (!var60 && var61) {
				var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var18, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var18, var24);
				var6.addVertexWithUV(var42, (double)(par3 + 0), var46, var20, var24);
				var6.addVertexWithUV(var42, (double)(par3 + 1), var46, var20, var22);
				var6.addVertexWithUV(var42, (double)(par3 + 1), var46, var18, var22);
				var6.addVertexWithUV(var42, (double)(par3 + 0), var46, var18, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var20, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var20, var22);
				if (!var59 && !var58) {
					var6.addVertexWithUV(var40, (double)(par3 + 1), var54, var28, var32);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var54, var28, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var56, var30, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var56, var30, var32);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var56, var28, var32);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var56, var28, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var54, var30, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var54, var30, var32);
				}

				if (var62 || par3 < var5 - 1 && this.blockAccess.isAirBlock(par2 + 1, par3 + 1, par4)) {
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var56, var30, var32);
					var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var54, var28, var32);
					var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var56, var30, var32);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var54, var28, var32);
				}

				if (var63 || par3 > 1 && this.blockAccess.isAirBlock(par2 + 1, par3 - 1, par4)) {
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var56, var30, var32);
					var6.addVertexWithUV(var42, (double)par3 - 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var42, (double)par3 - 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var54, var28, var32);
					var6.addVertexWithUV(var42, (double)par3 - 0.01D, var56, var30, var32);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var42, (double)par3 - 0.01D, var54, var28, var32);
				}
			}
		} else {
			var6.addVertexWithUV(var38, (double)(par3 + 1), var46, var16, var22);
			var6.addVertexWithUV(var38, (double)(par3 + 0), var46, var16, var24);
			var6.addVertexWithUV(var42, (double)(par3 + 0), var46, var20, var24);
			var6.addVertexWithUV(var42, (double)(par3 + 1), var46, var20, var22);
			var6.addVertexWithUV(var42, (double)(par3 + 1), var46, var16, var22);
			var6.addVertexWithUV(var42, (double)(par3 + 0), var46, var16, var24);
			var6.addVertexWithUV(var38, (double)(par3 + 0), var46, var20, var24);
			var6.addVertexWithUV(var38, (double)(par3 + 1), var46, var20, var22);
			if (var62) {
				var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var56, var30, var36);
				var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var56, var30, var32);
				var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var54, var28, var32);
				var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var54, var28, var36);
				var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var56, var30, var36);
				var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var56, var30, var32);
				var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var54, var28, var32);
				var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var54, var28, var36);
			} else {
				if (par3 < var5 - 1 && this.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4)) {
					var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var56, var30, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var54, var28, var36);
					var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var56, var30, var36);
					var6.addVertexWithUV(var38, (double)(par3 + 1) + 0.01D, var54, var28, var36);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var54, var28, var34);
				}

				if (par3 < var5 - 1 && this.blockAccess.isAirBlock(par2 + 1, par3 + 1, par4)) {
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var56, var30, var32);
					var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var54, var28, var32);
					var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var56, var30, var32);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var40, (double)(par3 + 1) + 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var42, (double)(par3 + 1) + 0.01D, var54, var28, var32);
				}
			}

			if (var63) {
				var6.addVertexWithUV(var38, (double)par3 - 0.01D, var56, var30, var36);
				var6.addVertexWithUV(var42, (double)par3 - 0.01D, var56, var30, var32);
				var6.addVertexWithUV(var42, (double)par3 - 0.01D, var54, var28, var32);
				var6.addVertexWithUV(var38, (double)par3 - 0.01D, var54, var28, var36);
				var6.addVertexWithUV(var42, (double)par3 - 0.01D, var56, var30, var36);
				var6.addVertexWithUV(var38, (double)par3 - 0.01D, var56, var30, var32);
				var6.addVertexWithUV(var38, (double)par3 - 0.01D, var54, var28, var32);
				var6.addVertexWithUV(var42, (double)par3 - 0.01D, var54, var28, var36);
			} else {
				if (par3 > 1 && this.blockAccess.isAirBlock(par2 - 1, par3 - 1, par4)) {
					var6.addVertexWithUV(var38, (double)par3 - 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var56, var30, var36);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var54, var28, var36);
					var6.addVertexWithUV(var38, (double)par3 - 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var38, (double)par3 - 0.01D, var56, var30, var36);
					var6.addVertexWithUV(var38, (double)par3 - 0.01D, var54, var28, var36);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var54, var28, var34);
				}

				if (par3 > 1 && this.blockAccess.isAirBlock(par2 + 1, par3 - 1, par4)) {
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var56, var30, var32);
					var6.addVertexWithUV(var42, (double)par3 - 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var42, (double)par3 - 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var54, var28, var32);
					var6.addVertexWithUV(var42, (double)par3 - 0.01D, var56, var30, var32);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var56, var30, var34);
					var6.addVertexWithUV(var40, (double)par3 - 0.01D, var54, var28, var34);
					var6.addVertexWithUV(var42, (double)par3 - 0.01D, var54, var28, var32);
				}
			}
		}

		if ((!var58 || !var59) && (var60 || var61 || var58 || var59)) {
			if (var58 && !var59) {
				var6.addVertexWithUV(var40, (double)(par3 + 1), var44, var16, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var44, var16, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var18, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var18, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var16, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var16, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var44, var18, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var44, var18, var22);
				if (!var61 && !var60) {
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var28, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 0), var46, var28, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 0), var46, var30, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var30, var32);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var28, var32);
					var6.addVertexWithUV(var52, (double)(par3 + 0), var46, var28, var36);
					var6.addVertexWithUV(var50, (double)(par3 + 0), var46, var30, var36);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var30, var32);
				}

				if (var62 || par3 < var5 - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1)) {
					var6.addVertexWithUV(var50, (double)(par3 + 1), var44, var30, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var30, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var28, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var44, var28, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var30, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var44, var30, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var44, var28, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var28, var32);
				}

				if (var63 || par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1)) {
					var6.addVertexWithUV(var50, (double)par3, var44, var30, var32);
					var6.addVertexWithUV(var50, (double)par3, var46, var30, var34);
					var6.addVertexWithUV(var52, (double)par3, var46, var28, var34);
					var6.addVertexWithUV(var52, (double)par3, var44, var28, var32);
					var6.addVertexWithUV(var50, (double)par3, var46, var30, var32);
					var6.addVertexWithUV(var50, (double)par3, var44, var30, var34);
					var6.addVertexWithUV(var52, (double)par3, var44, var28, var34);
					var6.addVertexWithUV(var52, (double)par3, var46, var28, var32);
				}
			} else if (!var58 && var59) {
				var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var18, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var18, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var48, var20, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var48, var20, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var48, var18, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var48, var18, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var20, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var20, var22);
				if (!var61 && !var60) {
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var28, var32);
					var6.addVertexWithUV(var52, (double)(par3 + 0), var46, var28, var36);
					var6.addVertexWithUV(var50, (double)(par3 + 0), var46, var30, var36);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var30, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var28, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 0), var46, var28, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 0), var46, var30, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var30, var32);
				}

				if (var62 || par3 < var5 - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1)) {
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var28, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var48, var28, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var48, var30, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var30, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var48, var28, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var28, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var30, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var48, var30, var34);
				}

				if (var63 || par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1)) {
					var6.addVertexWithUV(var50, (double)par3, var46, var28, var34);
					var6.addVertexWithUV(var50, (double)par3, var48, var28, var36);
					var6.addVertexWithUV(var52, (double)par3, var48, var30, var36);
					var6.addVertexWithUV(var52, (double)par3, var46, var30, var34);
					var6.addVertexWithUV(var50, (double)par3, var48, var28, var34);
					var6.addVertexWithUV(var50, (double)par3, var46, var28, var36);
					var6.addVertexWithUV(var52, (double)par3, var46, var30, var36);
					var6.addVertexWithUV(var52, (double)par3, var48, var30, var34);
				}
			}
		} else {
			var6.addVertexWithUV(var40, (double)(par3 + 1), var48, var16, var22);
			var6.addVertexWithUV(var40, (double)(par3 + 0), var48, var16, var24);
			var6.addVertexWithUV(var40, (double)(par3 + 0), var44, var20, var24);
			var6.addVertexWithUV(var40, (double)(par3 + 1), var44, var20, var22);
			var6.addVertexWithUV(var40, (double)(par3 + 1), var44, var16, var22);
			var6.addVertexWithUV(var40, (double)(par3 + 0), var44, var16, var24);
			var6.addVertexWithUV(var40, (double)(par3 + 0), var48, var20, var24);
			var6.addVertexWithUV(var40, (double)(par3 + 1), var48, var20, var22);
			if (var62) {
				var6.addVertexWithUV(var52, (double)(par3 + 1), var48, var30, var36);
				var6.addVertexWithUV(var52, (double)(par3 + 1), var44, var30, var32);
				var6.addVertexWithUV(var50, (double)(par3 + 1), var44, var28, var32);
				var6.addVertexWithUV(var50, (double)(par3 + 1), var48, var28, var36);
				var6.addVertexWithUV(var52, (double)(par3 + 1), var44, var30, var36);
				var6.addVertexWithUV(var52, (double)(par3 + 1), var48, var30, var32);
				var6.addVertexWithUV(var50, (double)(par3 + 1), var48, var28, var32);
				var6.addVertexWithUV(var50, (double)(par3 + 1), var44, var28, var36);
			} else {
				if (par3 < var5 - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1)) {
					var6.addVertexWithUV(var50, (double)(par3 + 1), var44, var30, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var30, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var28, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var44, var28, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var30, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var44, var30, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var44, var28, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var28, var32);
				}

				if (par3 < var5 - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1)) {
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var28, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var48, var28, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var48, var30, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var30, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var48, var28, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1), var46, var28, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var46, var30, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1), var48, var30, var34);
				}
			}

			if (var63) {
				var6.addVertexWithUV(var52, (double)par3, var48, var30, var36);
				var6.addVertexWithUV(var52, (double)par3, var44, var30, var32);
				var6.addVertexWithUV(var50, (double)par3, var44, var28, var32);
				var6.addVertexWithUV(var50, (double)par3, var48, var28, var36);
				var6.addVertexWithUV(var52, (double)par3, var44, var30, var36);
				var6.addVertexWithUV(var52, (double)par3, var48, var30, var32);
				var6.addVertexWithUV(var50, (double)par3, var48, var28, var32);
				var6.addVertexWithUV(var50, (double)par3, var44, var28, var36);
			} else {
				if (par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1)) {
					var6.addVertexWithUV(var50, (double)par3, var44, var30, var32);
					var6.addVertexWithUV(var50, (double)par3, var46, var30, var34);
					var6.addVertexWithUV(var52, (double)par3, var46, var28, var34);
					var6.addVertexWithUV(var52, (double)par3, var44, var28, var32);
					var6.addVertexWithUV(var50, (double)par3, var46, var30, var32);
					var6.addVertexWithUV(var50, (double)par3, var44, var30, var34);
					var6.addVertexWithUV(var52, (double)par3, var44, var28, var34);
					var6.addVertexWithUV(var52, (double)par3, var46, var28, var32);
				}

				if (par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1)) {
					var6.addVertexWithUV(var50, (double)par3, var46, var28, var34);
					var6.addVertexWithUV(var50, (double)par3, var48, var28, var36);
					var6.addVertexWithUV(var52, (double)par3, var48, var30, var36);
					var6.addVertexWithUV(var52, (double)par3, var46, var30, var34);
					var6.addVertexWithUV(var50, (double)par3, var48, var28, var34);
					var6.addVertexWithUV(var50, (double)par3, var46, var28, var36);
					var6.addVertexWithUV(var52, (double)par3, var46, var30, var36);
					var6.addVertexWithUV(var52, (double)par3, var48, var30, var34);
				}
			}
		}

		return true;
	}

	public boolean renderCrossedSquares(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var6 = 1.0F;
		int var7 = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var8 = (float)(var7 >> 16 & 255) / 255.0F;
		float var9 = (float)(var7 >> 8 & 255) / 255.0F;
		float var10 = (float)(var7 & 255) / 255.0F;
		if (EntityRenderer.anaglyphEnable) {
			float var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
			float var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
			float var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
			var8 = var11;
			var9 = var12;
			var10 = var13;
		}

		var5.setColorOpaque_F(var6 * var8, var6 * var9, var6 * var10);
		double var19 = (double)par2;
		double var20 = (double)par3;
		double var15 = (double)par4;
		if (par1Block == Block.tallGrass) {
			long var17 = (long)(par2 * 3129871) ^ (long)par4 * 116129781L ^ (long)par3;
			var17 = var17 * var17 * 42317861L + var17 * 11L;
			var19 += ((double)((float)(var17 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
			var20 += ((double)((float)(var17 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
			var15 += ((double)((float)(var17 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
		}

		this.drawCrossedSquares(par1Block, this.blockAccess.getBlockMetadata(par2, par3, par4), var19, var20, var15);
		return true;
	}

	public boolean renderBlockStem(Block par1Block, int par2, int par3, int par4) {
		BlockStem var5 = (BlockStem)par1Block;
		Tessellator var6 = Tessellator.instance;
		var6.setBrightness(var5.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var7 = 1.0F;
		int var8 = var5.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var9 = (float)(var8 >> 16 & 255) / 255.0F;
		float var10 = (float)(var8 >> 8 & 255) / 255.0F;
		float var11 = (float)(var8 & 255) / 255.0F;
		if (EntityRenderer.anaglyphEnable) {
			float var12 = (var9 * 30.0F + var10 * 59.0F + var11 * 11.0F) / 100.0F;
			float var13 = (var9 * 30.0F + var10 * 70.0F) / 100.0F;
			float var14 = (var9 * 30.0F + var11 * 70.0F) / 100.0F;
			var9 = var12;
			var10 = var13;
			var11 = var14;
		}

		var6.setColorOpaque_F(var7 * var9, var7 * var10, var7 * var11);
		var5.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
		int var15 = var5.func_35296_f(this.blockAccess, par2, par3, par4);
		if (var15 < 0) {
			this.renderBlockStemSmall(var5, this.blockAccess.getBlockMetadata(par2, par3, par4), var5.maxY, (double)par2, (double)par3, (double)par4);
		} else {
			this.renderBlockStemSmall(var5, this.blockAccess.getBlockMetadata(par2, par3, par4), 0.5D, (double)par2, (double)par3, (double)par4);
			this.renderBlockStemBig(var5, this.blockAccess.getBlockMetadata(par2, par3, par4), var15, var5.maxY, (double)par2, (double)par3, (double)par4);
		}

		return true;
	}

	public boolean renderBlockCrops(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		this.renderBlockCropsImpl(par1Block, this.blockAccess.getBlockMetadata(par2, par3, par4), (double)par2, (double)((float)par3 - 0.0625F), (double)par4);
		return true;
	}

	public void renderTorchAtAngle(Block par1Block, double par2, double par4, double par6, double par8, double par10) {
		Tessellator var12 = Tessellator.instance;
		int var13 = par1Block.getBlockTextureFromSide(0);
		if (this.overrideBlockTexture >= 0) {
			var13 = this.overrideBlockTexture;
		}

		int var14 = (var13 & 15) << 4;
		int var15 = var13 & 240;
		float var16 = (float)var14 / 256.0F;
		float var17 = ((float)var14 + 15.99F) / 256.0F;
		float var18 = (float)var15 / 256.0F;
		float var19 = ((float)var15 + 15.99F) / 256.0F;
		double var20 = (double)var16 + 0.02734375D;
		double var22 = (double)var18 + 0.0234375D;
		double var24 = (double)var16 + 0.03515625D;
		double var26 = (double)var18 + 0.03125D;
		par2 += 0.5D;
		par6 += 0.5D;
		double var28 = par2 - 0.5D;
		double var30 = par2 + 0.5D;
		double var32 = par6 - 0.5D;
		double var34 = par6 + 0.5D;
		double var36 = 0.0625D;
		double var38 = 0.625D;
		var12.addVertexWithUV(par2 + par8 * (1.0D - var38) - var36, par4 + var38, par6 + par10 * (1.0D - var38) - var36, var20, var22);
		var12.addVertexWithUV(par2 + par8 * (1.0D - var38) - var36, par4 + var38, par6 + par10 * (1.0D - var38) + var36, var20, var26);
		var12.addVertexWithUV(par2 + par8 * (1.0D - var38) + var36, par4 + var38, par6 + par10 * (1.0D - var38) + var36, var24, var26);
		var12.addVertexWithUV(par2 + par8 * (1.0D - var38) + var36, par4 + var38, par6 + par10 * (1.0D - var38) - var36, var24, var22);
		var12.addVertexWithUV(par2 - var36, par4 + 1.0D, var32, (double)var16, (double)var18);
		var12.addVertexWithUV(par2 - var36 + par8, par4 + 0.0D, var32 + par10, (double)var16, (double)var19);
		var12.addVertexWithUV(par2 - var36 + par8, par4 + 0.0D, var34 + par10, (double)var17, (double)var19);
		var12.addVertexWithUV(par2 - var36, par4 + 1.0D, var34, (double)var17, (double)var18);
		var12.addVertexWithUV(par2 + var36, par4 + 1.0D, var34, (double)var16, (double)var18);
		var12.addVertexWithUV(par2 + par8 + var36, par4 + 0.0D, var34 + par10, (double)var16, (double)var19);
		var12.addVertexWithUV(par2 + par8 + var36, par4 + 0.0D, var32 + par10, (double)var17, (double)var19);
		var12.addVertexWithUV(par2 + var36, par4 + 1.0D, var32, (double)var17, (double)var18);
		var12.addVertexWithUV(var28, par4 + 1.0D, par6 + var36, (double)var16, (double)var18);
		var12.addVertexWithUV(var28 + par8, par4 + 0.0D, par6 + var36 + par10, (double)var16, (double)var19);
		var12.addVertexWithUV(var30 + par8, par4 + 0.0D, par6 + var36 + par10, (double)var17, (double)var19);
		var12.addVertexWithUV(var30, par4 + 1.0D, par6 + var36, (double)var17, (double)var18);
		var12.addVertexWithUV(var30, par4 + 1.0D, par6 - var36, (double)var16, (double)var18);
		var12.addVertexWithUV(var30 + par8, par4 + 0.0D, par6 - var36 + par10, (double)var16, (double)var19);
		var12.addVertexWithUV(var28 + par8, par4 + 0.0D, par6 - var36 + par10, (double)var17, (double)var19);
		var12.addVertexWithUV(var28, par4 + 1.0D, par6 - var36, (double)var17, (double)var18);
	}

	// Spout start
	public void drawCrossedSquares(Block var1, int var2, double var3, double var5, double var7) {
		drawCrossedSquares(var1, var2, var3, var5, var7, false);
	}

	public void drawCrossedSquares(Block par1Block, int par2, double par3, double par5, double par7, boolean customUV) {
		// Spout end
		Tessellator var9 = Tessellator.instance;
		int var10 = par1Block.getBlockTextureFromSideAndMetadata(0, par2);
		if (this.overrideBlockTexture >= 0) {
			var10 = this.overrideBlockTexture;
		}

		int var11 = (var10 & 15) << 4;
		int var12 = var10 & 240;
			double var13 = (double)((float)var11 / 256.0F);
		double var15 = (double)(((float)var11 + 15.99F) / 256.0F);
		double var17 = (double)((float)var12 / 256.0F);
		double var19 = (double)(((float)var12 + 15.99F) / 256.0F);
		double var21 = par3 + 0.5D - 0.45D;
		double var23 = par3 + 0.5D + 0.45D;
		double var25 = par7 + 0.5D - 0.45D;
		double var27 = par7 + 0.5D + 0.45D;
		// Spout start
		if (customUV) {
			var13 = 1;
			var15 = 0;
			var17 = 1;
			var19 = 0;
		}
		// Spout end
		var9.addVertexWithUV(var21, par5 + 1.0D, var25, var13, var17);
		var9.addVertexWithUV(var21, par5 + 0.0D, var25, var13, var19);
		var9.addVertexWithUV(var23, par5 + 0.0D, var27, var15, var19);
		var9.addVertexWithUV(var23, par5 + 1.0D, var27, var15, var17);
		var9.addVertexWithUV(var23, par5 + 1.0D, var27, var13, var17);
		var9.addVertexWithUV(var23, par5 + 0.0D, var27, var13, var19);
		var9.addVertexWithUV(var21, par5 + 0.0D, var25, var15, var19);
		var9.addVertexWithUV(var21, par5 + 1.0D, var25, var15, var17);
		var9.addVertexWithUV(var21, par5 + 1.0D, var27, var13, var17);
		var9.addVertexWithUV(var21, par5 + 0.0D, var27, var13, var19);
		var9.addVertexWithUV(var23, par5 + 0.0D, var25, var15, var19);
		var9.addVertexWithUV(var23, par5 + 1.0D, var25, var15, var17);
		var9.addVertexWithUV(var23, par5 + 1.0D, var25, var13, var17);
		var9.addVertexWithUV(var23, par5 + 0.0D, var25, var13, var19);
		var9.addVertexWithUV(var21, par5 + 0.0D, var27, var15, var19);
		var9.addVertexWithUV(var21, par5 + 1.0D, var27, var15, var17);
	}

	public void renderBlockStemSmall(Block par1Block, int par2, double par3, double par5, double par7, double par9) {
		Tessellator var11 = Tessellator.instance;
		int var12 = par1Block.getBlockTextureFromSideAndMetadata(0, par2);
		if (this.overrideBlockTexture >= 0) {
			var12 = this.overrideBlockTexture;
		}

		int var13 = (var12 & 15) << 4;
		int var14 = var12 & 240;
		double var15 = (double)((float)var13 / 256.0F);
		double var17 = (double)(((float)var13 + 15.99F) / 256.0F);
		double var19 = (double)((float)var14 / 256.0F);
		double var21 = ((double)var14 + 15.989999771118164D * par3) / 256.0D;
		double var23 = par5 + 0.5D - 0.44999998807907104D;
		double var25 = par5 + 0.5D + 0.44999998807907104D;
		double var27 = par9 + 0.5D - 0.44999998807907104D;
		double var29 = par9 + 0.5D + 0.44999998807907104D;
		var11.addVertexWithUV(var23, par7 + par3, var27, var15, var19);
		var11.addVertexWithUV(var23, par7 + 0.0D, var27, var15, var21);
		var11.addVertexWithUV(var25, par7 + 0.0D, var29, var17, var21);
		var11.addVertexWithUV(var25, par7 + par3, var29, var17, var19);
		var11.addVertexWithUV(var25, par7 + par3, var29, var15, var19);
		var11.addVertexWithUV(var25, par7 + 0.0D, var29, var15, var21);
		var11.addVertexWithUV(var23, par7 + 0.0D, var27, var17, var21);
		var11.addVertexWithUV(var23, par7 + par3, var27, var17, var19);
		var11.addVertexWithUV(var23, par7 + par3, var29, var15, var19);
		var11.addVertexWithUV(var23, par7 + 0.0D, var29, var15, var21);
		var11.addVertexWithUV(var25, par7 + 0.0D, var27, var17, var21);
		var11.addVertexWithUV(var25, par7 + par3, var27, var17, var19);
		var11.addVertexWithUV(var25, par7 + par3, var27, var15, var19);
		var11.addVertexWithUV(var25, par7 + 0.0D, var27, var15, var21);
		var11.addVertexWithUV(var23, par7 + 0.0D, var29, var17, var21);
		var11.addVertexWithUV(var23, par7 + par3, var29, var17, var19);
	}

	public boolean renderBlockLilyPad(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = par1Block.blockIndexInTexture;
		if (this.overrideBlockTexture >= 0) {
			var6 = this.overrideBlockTexture;
		}

		int var7 = (var6 & 15) << 4;
		int var8 = var6 & 240;
		float var9 = 0.015625F;
		double var10 = (double)((float)var7 / 256.0F);
		double var12 = (double)(((float)var7 + 15.99F) / 256.0F);
		double var14 = (double)((float)var8 / 256.0F);
		double var16 = (double)(((float)var8 + 15.99F) / 256.0F);
		long var18 = (long)(par2 * 3129871) ^ (long)par4 * 116129781L ^ (long)par3;
		var18 = var18 * var18 * 42317861L + var18 * 11L;
		int var20 = (int)(var18 >> 16 & 3L);
		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var21 = (float)par2 + 0.5F;
		float var22 = (float)par4 + 0.5F;
		float var23 = (float)(var20 & 1) * 0.5F * (float)(1 - var20 / 2 % 2 * 2);
		float var24 = (float)(var20 + 1 & 1) * 0.5F * (float)(1 - (var20 + 1) / 2 % 2 * 2);
		var5.setColorOpaque_I(par1Block.getBlockColor());
		var5.addVertexWithUV((double)(var21 + var23 - var24), (double)((float)par3 + var9), (double)(var22 + var23 + var24), var10, var14);
		var5.addVertexWithUV((double)(var21 + var23 + var24), (double)((float)par3 + var9), (double)(var22 - var23 + var24), var12, var14);
		var5.addVertexWithUV((double)(var21 - var23 + var24), (double)((float)par3 + var9), (double)(var22 - var23 - var24), var12, var16);
		var5.addVertexWithUV((double)(var21 - var23 - var24), (double)((float)par3 + var9), (double)(var22 + var23 - var24), var10, var16);
		var5.setColorOpaque_I((par1Block.getBlockColor() & 16711422) >> 1);
		var5.addVertexWithUV((double)(var21 - var23 - var24), (double)((float)par3 + var9), (double)(var22 + var23 - var24), var10, var16);
		var5.addVertexWithUV((double)(var21 - var23 + var24), (double)((float)par3 + var9), (double)(var22 - var23 - var24), var12, var16);
		var5.addVertexWithUV((double)(var21 + var23 + var24), (double)((float)par3 + var9), (double)(var22 - var23 + var24), var12, var14);
		var5.addVertexWithUV((double)(var21 + var23 - var24), (double)((float)par3 + var9), (double)(var22 + var23 + var24), var10, var14);
		return true;
	}

	public void renderBlockStemBig(Block par1Block, int par2, int par3, double par4, double par6, double par8, double par10) {
		Tessellator var12 = Tessellator.instance;
		int var13 = par1Block.getBlockTextureFromSideAndMetadata(0, par2) + 16;
		if (this.overrideBlockTexture >= 0) {
			var13 = this.overrideBlockTexture;
		}

		int var14 = (var13 & 15) << 4;
		int var15 = var13 & 240;
		double var16 = (double)((float)var14 / 256.0F);
		double var18 = (double)(((float)var14 + 15.99F) / 256.0F);
		double var20 = (double)((float)var15 / 256.0F);
		double var22 = ((double)var15 + 15.989999771118164D * par4) / 256.0D;
		double var24 = par6 + 0.5D - 0.5D;
		double var26 = par6 + 0.5D + 0.5D;
		double var28 = par10 + 0.5D - 0.5D;
		double var30 = par10 + 0.5D + 0.5D;
		double var32 = par6 + 0.5D;
		double var34 = par10 + 0.5D;
		if ((par3 + 1) / 2 % 2 == 1) {
			double var36 = var18;
			var18 = var16;
			var16 = var36;
		}

		if (par3 < 2) {
			var12.addVertexWithUV(var24, par8 + par4, var34, var16, var20);
			var12.addVertexWithUV(var24, par8 + 0.0D, var34, var16, var22);
			var12.addVertexWithUV(var26, par8 + 0.0D, var34, var18, var22);
			var12.addVertexWithUV(var26, par8 + par4, var34, var18, var20);
			var12.addVertexWithUV(var26, par8 + par4, var34, var18, var20);
			var12.addVertexWithUV(var26, par8 + 0.0D, var34, var18, var22);
			var12.addVertexWithUV(var24, par8 + 0.0D, var34, var16, var22);
			var12.addVertexWithUV(var24, par8 + par4, var34, var16, var20);
		} else {
			var12.addVertexWithUV(var32, par8 + par4, var30, var16, var20);
			var12.addVertexWithUV(var32, par8 + 0.0D, var30, var16, var22);
			var12.addVertexWithUV(var32, par8 + 0.0D, var28, var18, var22);
			var12.addVertexWithUV(var32, par8 + par4, var28, var18, var20);
			var12.addVertexWithUV(var32, par8 + par4, var28, var18, var20);
			var12.addVertexWithUV(var32, par8 + 0.0D, var28, var18, var22);
			var12.addVertexWithUV(var32, par8 + 0.0D, var30, var16, var22);
			var12.addVertexWithUV(var32, par8 + par4, var30, var16, var20);
		}

	}

	public void renderBlockCropsImpl(Block par1Block, int par2, double par3, double par5, double par7) {
		Tessellator var9 = Tessellator.instance;
		int var10 = par1Block.getBlockTextureFromSideAndMetadata(0, par2);
		if (this.overrideBlockTexture >= 0) {
			var10 = this.overrideBlockTexture;
		}

		int var11 = (var10 & 15) << 4;
		int var12 = var10 & 240;
		double var13 = (double)((float)var11 / 256.0F);
		double var15 = (double)(((float)var11 + 15.99F) / 256.0F);
		double var17 = (double)((float)var12 / 256.0F);
		double var19 = (double)(((float)var12 + 15.99F) / 256.0F);
		double var21 = par3 + 0.5D - 0.25D;
		double var23 = par3 + 0.5D + 0.25D;
		double var25 = par7 + 0.5D - 0.5D;
		double var27 = par7 + 0.5D + 0.5D;
		var9.addVertexWithUV(var21, par5 + 1.0D, var25, var13, var17);
		var9.addVertexWithUV(var21, par5 + 0.0D, var25, var13, var19);
		var9.addVertexWithUV(var21, par5 + 0.0D, var27, var15, var19);
		var9.addVertexWithUV(var21, par5 + 1.0D, var27, var15, var17);
		var9.addVertexWithUV(var21, par5 + 1.0D, var27, var13, var17);
		var9.addVertexWithUV(var21, par5 + 0.0D, var27, var13, var19);
		var9.addVertexWithUV(var21, par5 + 0.0D, var25, var15, var19);
		var9.addVertexWithUV(var21, par5 + 1.0D, var25, var15, var17);
		var9.addVertexWithUV(var23, par5 + 1.0D, var27, var13, var17);
		var9.addVertexWithUV(var23, par5 + 0.0D, var27, var13, var19);
		var9.addVertexWithUV(var23, par5 + 0.0D, var25, var15, var19);
		var9.addVertexWithUV(var23, par5 + 1.0D, var25, var15, var17);
		var9.addVertexWithUV(var23, par5 + 1.0D, var25, var13, var17);
		var9.addVertexWithUV(var23, par5 + 0.0D, var25, var13, var19);
		var9.addVertexWithUV(var23, par5 + 0.0D, var27, var15, var19);
		var9.addVertexWithUV(var23, par5 + 1.0D, var27, var15, var17);
		var21 = par3 + 0.5D - 0.5D;
		var23 = par3 + 0.5D + 0.5D;
		var25 = par7 + 0.5D - 0.25D;
		var27 = par7 + 0.5D + 0.25D;
		var9.addVertexWithUV(var21, par5 + 1.0D, var25, var13, var17);
		var9.addVertexWithUV(var21, par5 + 0.0D, var25, var13, var19);
		var9.addVertexWithUV(var23, par5 + 0.0D, var25, var15, var19);
		var9.addVertexWithUV(var23, par5 + 1.0D, var25, var15, var17);
		var9.addVertexWithUV(var23, par5 + 1.0D, var25, var13, var17);
		var9.addVertexWithUV(var23, par5 + 0.0D, var25, var13, var19);
		var9.addVertexWithUV(var21, par5 + 0.0D, var25, var15, var19);
		var9.addVertexWithUV(var21, par5 + 1.0D, var25, var15, var17);
		var9.addVertexWithUV(var23, par5 + 1.0D, var27, var13, var17);
		var9.addVertexWithUV(var23, par5 + 0.0D, var27, var13, var19);
		var9.addVertexWithUV(var21, par5 + 0.0D, var27, var15, var19);
		var9.addVertexWithUV(var21, par5 + 1.0D, var27, var15, var17);
		var9.addVertexWithUV(var21, par5 + 1.0D, var27, var13, var17);
		var9.addVertexWithUV(var21, par5 + 0.0D, var27, var13, var19);
		var9.addVertexWithUV(var23, par5 + 0.0D, var27, var15, var19);
		var9.addVertexWithUV(var23, par5 + 1.0D, var27, var15, var17);
	}

	public boolean renderBlockFluids(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var7 = (float)(var6 >> 16 & 255) / 255.0F;
		float var8 = (float)(var6 >> 8 & 255) / 255.0F;
		float var9 = (float)(var6 & 255) / 255.0F;
		boolean var10 = par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1);
		boolean var11 = par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0);
		boolean[] var12 = new boolean[]{par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2), par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3), par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4), par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)};
		if (!var10 && !var11 && !var12[0] && !var12[1] && !var12[2] && !var12[3]) {
			return false;
		} else {
			boolean var13 = false;
			float var14 = 0.5F;
			float var15 = 1.0F;
			float var16 = 0.8F;
			float var17 = 0.6F;
			double var18 = 0.0D;
			double var20 = 1.0D;
			Material var22 = par1Block.blockMaterial;
			int var23 = this.blockAccess.getBlockMetadata(par2, par3, par4);
			double var24 = (double)this.getFluidHeight(par2, par3, par4, var22);
			double var26 = (double)this.getFluidHeight(par2, par3, par4 + 1, var22);
			double var28 = (double)this.getFluidHeight(par2 + 1, par3, par4 + 1, var22);
			double var30 = (double)this.getFluidHeight(par2 + 1, par3, par4, var22);
			double var32 = 0.0010000000474974513D;
			int var34;
			int var37;
			if (this.renderAllFaces || var10) {
				var13 = true;
				var34 = par1Block.getBlockTextureFromSideAndMetadata(1, var23);
				float var35 = (float)BlockFluid.func_293_a(this.blockAccess, par2, par3, par4, var22);
				if (var35 > -999.0F) {
					var34 = par1Block.getBlockTextureFromSideAndMetadata(2, var23);
				}

				var24 -= var32;
				var26 -= var32;
				var28 -= var32;
				var30 -= var32;
				int var36 = (var34 & 15) << 4;
				var37 = var34 & 240;
				double var38 = ((double)var36 + 8.0D) / 256.0D;
				double var40 = ((double)var37 + 8.0D) / 256.0D;
				if (var35 < -999.0F) {
					var35 = 0.0F;
				} else {
					var38 = (double)((float)(var36 + 16) / 256.0F);
					var40 = (double)((float)(var37 + 16) / 256.0F);
				}

				double var42 = (double)(MathHelper.sin(var35) * 8.0F) / 256.0D;
				double var44 = (double)(MathHelper.cos(var35) * 8.0F) / 256.0D;
				var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
				float var46 = 1.0F;
				var5.setColorOpaque_F(var15 * var46 * var7, var15 * var46 * var8, var15 * var46 * var9);
				var5.addVertexWithUV((double)(par2 + 0), (double)par3 + var24, (double)(par4 + 0), var38 - var44 - var42, var40 - var44 + var42);
				var5.addVertexWithUV((double)(par2 + 0), (double)par3 + var26, (double)(par4 + 1), var38 - var44 + var42, var40 + var44 + var42);
				var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var28, (double)(par4 + 1), var38 + var44 + var42, var40 + var44 - var42);
				var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var30, (double)(par4 + 0), var38 + var44 - var42, var40 - var44 - var42);
			}

			if (this.renderAllFaces || var11) {
				var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
				float var65 = 1.0F;
				var5.setColorOpaque_F(var14 * var65, var14 * var65, var14 * var65);
				this.renderBottomFace(par1Block, (double)par2, (double)par3 + var32, (double)par4, par1Block.getBlockTextureFromSide(0));
				var13 = true;
			}

			for (var34 = 0; var34 < 4; ++var34) {
				int var64 = par2;
				var37 = par4;
				if (var34 == 0) {
					var37 = par4 - 1;
				}

				if (var34 == 1) {
					++var37;
				}

				if (var34 == 2) {
					var64 = par2 - 1;
				}

				if (var34 == 3) {
					++var64;
				}

				int var66 = par1Block.getBlockTextureFromSideAndMetadata(var34 + 2, var23);
				int var39 = (var66 & 15) << 4;
				int var67 = var66 & 240;
				if (this.renderAllFaces || var12[var34]) {
					double var43;
					double var41;
					double var47;
					double var45;
					double var51;
					double var49;
					if (var34 == 0) {
						var41 = var24;
						var43 = var30;
						var45 = (double)par2;
						var49 = (double)(par2 + 1);
						var47 = (double)par4 + var32;
						var51 = (double)par4 + var32;
					} else if (var34 == 1) {
						var41 = var28;
						var43 = var26;
						var45 = (double)(par2 + 1);
						var49 = (double)par2;
						var47 = (double)(par4 + 1) - var32;
						var51 = (double)(par4 + 1) - var32;
					} else if (var34 == 2) {
						var41 = var26;
						var43 = var24;
						var45 = (double)par2 + var32;
						var49 = (double)par2 + var32;
						var47 = (double)(par4 + 1);
						var51 = (double)par4;
					} else {
						var41 = var30;
						var43 = var28;
						var45 = (double)(par2 + 1) - var32;
						var49 = (double)(par2 + 1) - var32;
						var47 = (double)par4;
						var51 = (double)(par4 + 1);
					}

					var13 = true;
					double var53 = (double)((float)(var39 + 0) / 256.0F);
					double var55 = ((double)(var39 + 16) - 0.01D) / 256.0D;
					double var57 = ((double)var67 + (1.0D - var41) * 16.0D) / 256.0D;
					double var59 = ((double)var67 + (1.0D - var43) * 16.0D) / 256.0D;
					double var61 = ((double)(var67 + 16) - 0.01D) / 256.0D;
					var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, var64, par3, var37));
					float var63 = 1.0F;
					if (var34 < 2) {
						var63 *= var16;
					} else {
						var63 *= var17;
					}

					var5.setColorOpaque_F(var15 * var63 * var7, var15 * var63 * var8, var15 * var63 * var9);
					var5.addVertexWithUV(var45, (double)par3 + var41, var47, var53, var57);
					var5.addVertexWithUV(var49, (double)par3 + var43, var51, var55, var59);
					var5.addVertexWithUV(var49, (double)(par3 + 0), var51, var55, var61);
					var5.addVertexWithUV(var45, (double)(par3 + 0), var47, var53, var61);
				}
			}

			par1Block.minY = var18;
			par1Block.maxY = var20;
			return var13;
		}
	}

	private float getFluidHeight(int par1, int par2, int par3, Material par4Material) {
		int var5 = 0;
		float var6 = 0.0F;

		for (int var7 = 0; var7 < 4; ++var7) {
			int var8 = par1 - (var7 & 1);
			int var10 = par3 - (var7 >> 1 & 1);
			if (this.blockAccess.getBlockMaterial(var8, par2 + 1, var10) == par4Material) {
				return 1.0F;
			}

			Material var11 = this.blockAccess.getBlockMaterial(var8, par2, var10);
			if (var11 == par4Material) {
				int var12 = this.blockAccess.getBlockMetadata(var8, par2, var10);
				if (var12 >= 8 || var12 == 0) {
					var6 += BlockFluid.getFluidHeightPercent(var12) * 10.0F;
					var5 += 10;
				}

				var6 += BlockFluid.getFluidHeightPercent(var12);
				++var5;
			} else if (!var11.isSolid()) {
				++var6;
				++var5;
			}
		}

		return 1.0F - var6 / (float)var5;
	}

	public void renderBlockFallingSand(Block par1Block, World par2World, int par3, int par4, int par5) {
		float var6 = 0.5F;
		float var7 = 1.0F;
		float var8 = 0.8F;
		float var9 = 0.6F;
		Tessellator var10 = Tessellator.instance;
		var10.startDrawingQuads();
		var10.setBrightness(par1Block.getMixedBrightnessForBlock(par2World, par3, par4, par5));
		float var11 = 1.0F;
		float var12 = 1.0F;
		if (var12 < var11) {
			var12 = var11;
		}
//Spout HD Start
		Colorizer.setColorF(Colorizer.colorizeBlock(par1Block, par3, par4, par5, 0));
		var10.setColorOpaque_F(Colorizer.setColor[0] * var12, Colorizer.setColor[1] * var12, Colorizer.setColor[2] * var12);
		this.renderBottomFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(0));
		var12 = 1.0F;
		if (var12 < var11) {
			var12 = var11;
		}

		var10.setColorOpaque_F(Colorizer.setColor[0] * var12, Colorizer.setColor[1] * var12, Colorizer.setColor[2] * var12);
		this.renderTopFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(1));
		var12 = 1.0F;
		if (var12 < var11) {
			var12 = var11;
		}

		var10.setColorOpaque_F(Colorizer.setColor[0] * var12, Colorizer.setColor[1] * var12, Colorizer.setColor[2] * var12);
		this.renderEastFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(2));
		var12 = 1.0F;
		if (var12 < var11) {
			var12 = var11;
		}

		var10.setColorOpaque_F(Colorizer.setColor[0] * var12, Colorizer.setColor[1] * var12, Colorizer.setColor[2] * var12);
		this.renderWestFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(3));
		var12 = 1.0F;
		if (var12 < var11) {
			var12 = var11;
		}

		var10.setColorOpaque_F(Colorizer.setColor[0] * var12, Colorizer.setColor[1] * var12, Colorizer.setColor[2] * var12);
		this.renderNorthFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(4));
		var12 = 1.0F;
		if (var12 < var11) {
			var12 = var11;
		}

		var10.setColorOpaque_F(Colorizer.setColor[0] * var12, Colorizer.setColor[1] * var12, Colorizer.setColor[2] * var12);
//Spout HD end
		this.renderSouthFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSide(5));
		var10.draw();
	}

	public boolean renderStandardBlock(Block par1Block, int par2, int par3, int par4) {
		int var5 = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var6 = (float)(var5 >> 16 & 255) / 255.0F;
		float var7 = (float)(var5 >> 8 & 255) / 255.0F;
		float var8 = (float)(var5 & 255) / 255.0F;
		if (EntityRenderer.anaglyphEnable) {
			float var9 = (var6 * 30.0F + var7 * 59.0F + var8 * 11.0F) / 100.0F;
			float var10 = (var6 * 30.0F + var7 * 70.0F) / 100.0F;
			float var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
			var6 = var9;
			var7 = var10;
			var8 = var11;
		}

		return Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[par1Block.blockID] == 0?this.renderStandardBlockWithAmbientOcclusion(par1Block, par2, par3, par4, var6, var7, var8):this.renderStandardBlockWithColorMultiplier(par1Block, par2, par3, par4, var6, var7, var8);
	}

	public boolean renderStandardBlockWithAmbientOcclusion(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
		this.enableAO = true;
		boolean var8 = false;
		//Spout start
		this.dirtyAmbientOcclusionCache = true;
		/* removed
		float var9 = this.lightValueOwn;
		float var10 = this.lightValueOwn;
		float var11 = this.lightValueOwn;
		float var12 = this.lightValueOwn;
		*/
		float var9, var10, var11, var12;
		//Spout end
		boolean var13 = true;
		boolean var14 = true;
		boolean var15 = true;
		boolean var16 = true;
		boolean var17 = true;
		boolean var18 = true;
		//Spout start
		/* removed
		this.lightValueOwn = var1.getAmbientOcclusionLightValue(this.blockAccess, var2, var3, var4);
		this.aoLightValueXNeg = var1.getAmbientOcclusionLightValue(this.blockAccess, var2 - 1, var3, var4);
		this.aoLightValueYNeg = var1.getAmbientOcclusionLightValue(this.blockAccess, var2, var3 - 1, var4);
		this.aoLightValueZNeg = var1.getAmbientOcclusionLightValue(this.blockAccess, var2, var3, var4 - 1);
		this.aoLightValueXPos = var1.getAmbientOcclusionLightValue(this.blockAccess, var2 + 1, var3, var4);
		this.aoLightValueYPos = var1.getAmbientOcclusionLightValue(this.blockAccess, var2, var3 + 1, var4);
		this.aoLightValueZPos = var1.getAmbientOcclusionLightValue(this.blockAccess, var2, var3, var4 + 1);
		*/
		//Spout end
		int var19 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		//Spout start
		
		int var20 = var19;
		int var21 = var19;
		int var22 = var19;
		int var23 = var19;
		int var24 = var19;
		int var25 = var19;
		/* removed
		if (par1Block.minY <= 0.0D) {
			var21 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
		}

		if (par1Block.maxY >= 1.0D) {
			var24 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
		}

		if (par1Block.minX <= 0.0D) {
			var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
		}

		if (par1Block.maxX >= 1.0D) {
			var23 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
		}

		if (par1Block.minZ <= 0.0D) {
			var22 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
		}

		if (par1Block.maxZ >= 1.0D) {
			var25 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
		}

		this.aoGrassXYZPPC = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
		this.aoGrassXYZPNC = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
		this.aoGrassXYZPCP = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
		this.aoGrassXYZPCN = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
		this.aoGrassXYZNPC = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
		this.aoGrassXYZNNC = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
		this.aoGrassXYZNCN = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
		this.aoGrassXYZNCP = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
		this.aoGrassXYZCPP = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
		this.aoGrassXYZCPN = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
		this.aoGrassXYZCNP = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
		this.aoGrassXYZCNN = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

		*/

		//Spout end

		Tessellator var26 = Tessellator.instance;
		var26.setBrightness(983055);
		if (par1Block.blockIndexInTexture == 3) {
			var18 = false;
			var17 = false;
			var16 = false;
			var15 = false;
			var13 = false;
			// Spout HD start
			if (par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2) == 0) {
				var15 = true;
			}

			if (par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3) == 0) {
				var16 = true;
			}

			if (par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4) == 0) {
				var17 = true;
			}

			if (par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5) == 0) {
				var18 = true;
			}
			// Spout HD end
		}

		if (this.overrideBlockTexture >= 0) {
			var18 = false;
			var17 = false;
			var16 = false;
			var15 = false;
			var13 = false;
			// Spout HD start
			if (par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2) == 0) {
				var15 = true;
			}

			if (par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3) == 0) {
				var16 = true;
			}

			if (par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4) == 0) {
				var17 = true;
			}

			if (par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5) == 0) {
				var18 = true;
			}
			// Spout HD end
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0)) {
			//Spout start
			if(this.dirtyAmbientOcclusionCache) {
				this.calculateAmbientOcclusionLightValues(par1Block, par2, par3, par4);
			}
			if (par1Block.minY <= 0.0D) {
				var21 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
			}
			//Spout end
			if (this.aoType > 0) {
				if (par1Block.minY <= 0.0D) {
					--par3;
				}

				this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
				this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
				this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
				this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
				this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
				this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
				this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
				this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
				if (!this.aoGrassXYZCNN && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
					this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
				} else {
					this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
					this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
				}

				if (!this.aoGrassXYZCNP && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
					this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
				} else {
					this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
					this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
				}

				if (!this.aoGrassXYZCNN && !this.aoGrassXYZPNC) {
					this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
					this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
				} else {
					this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
					this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
				}

				if (!this.aoGrassXYZCNP && !this.aoGrassXYZPNC) {
					this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
					this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
				} else {
					this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
					this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
				}

				if (par1Block.minY <= 0.0D) {
					++par3;
				}
				//Spout Start
				if(org.spoutcraft.client.config.ConfigReader.fancyLight && org.spoutcraft.client.config.ConfigReader.smoothLighting > 0F) {
					this.aoLightValueScratchXYZNNP = adjustAmbientOcclusion(this.aoLightValueScratchXYZNNP, this.aoLightValueYNeg);
					this.aoLightValueScratchXYNN = adjustAmbientOcclusion(this.aoLightValueScratchXYNN, this.aoLightValueYNeg);
					this.aoLightValueScratchYZNP = adjustAmbientOcclusion(this.aoLightValueScratchYZNP, this.aoLightValueYNeg);
					this.aoLightValueScratchXYZPNP = adjustAmbientOcclusion(this.aoLightValueScratchXYZPNP, this.aoLightValueYNeg);
					this.aoLightValueScratchXYPN = adjustAmbientOcclusion(this.aoLightValueScratchXYPN, this.aoLightValueYNeg);
					this.aoLightValueScratchYZNN = adjustAmbientOcclusion(this.aoLightValueScratchYZNN, this.aoLightValueYNeg);
					this.aoLightValueScratchXYZPNN = adjustAmbientOcclusion(this.aoLightValueScratchXYZPNN, this.aoLightValueYNeg);
					this.aoLightValueScratchXYZNNN = adjustAmbientOcclusion(this.aoLightValueScratchXYZNNN, this.aoLightValueYNeg);
				}
				//Spout end
				
				var9 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + this.aoLightValueYNeg) / 4.0F;
				var12 = (this.aoLightValueScratchYZNP + this.aoLightValueYNeg + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
				var11 = (this.aoLightValueYNeg + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
				var10 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + this.aoLightValueYNeg + this.aoLightValueScratchYZNN) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, var21);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, var21);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, var21);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, var21);
			} else {
				var12 = this.aoLightValueYNeg;
				var11 = this.aoLightValueYNeg;
				var10 = this.aoLightValueYNeg;
				var9 = this.aoLightValueYNeg;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = this.aoBrightnessXYNN;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var13?par5:1.0F) * 0.5F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var13?par6:1.0F) * 0.5F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var13?par7:1.0F) * 0.5F;
			this.colorRedTopLeft *= var9;
			this.colorGreenTopLeft *= var9;
			this.colorBlueTopLeft *= var9;
			this.colorRedBottomLeft *= var10;
			this.colorGreenBottomLeft *= var10;
			this.colorBlueBottomLeft *= var10;
			this.colorRedBottomRight *= var11;
			this.colorGreenBottomRight *= var11;
			this.colorBlueBottomRight *= var11;
			this.colorRedTopRight *= var12;
			this.colorGreenTopRight *= var12;
			this.colorBlueTopRight *= var12;
			this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0));
			var8 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1)) {
			//Spout start
			if(this.dirtyAmbientOcclusionCache) {
				this.calculateAmbientOcclusionLightValues(par1Block, par2, par3, par4);
			}
			if (par1Block.maxY >= 1.0D) {
				var24 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
			}
			//Spout end
			if (this.aoType > 0) {
				if (par1Block.maxY >= 1.0D) {
					++par3;
				}

				this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
				this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
				this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
				this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
				this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
				this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
				this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
				this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
				if (!this.aoGrassXYZCPN && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
					this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
				} else {
					this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
					this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
				}

				if (!this.aoGrassXYZCPN && !this.aoGrassXYZPPC) {
					this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
					this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
				} else {
					this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
					this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
				}

				if (!this.aoGrassXYZCPP && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
					this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
				} else {
					this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
					this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
				}

				if (!this.aoGrassXYZCPP && !this.aoGrassXYZPPC) {
					this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
					this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
				} else {
					this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
					this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
				}

				if (par1Block.maxY >= 1.0D) {
					--par3;
				}
				//Spout start
				if(org.spoutcraft.client.config.ConfigReader.fancyLight && org.spoutcraft.client.config.ConfigReader.smoothLighting > 0F) {
					this.aoLightValueScratchXYZNPP = adjustAmbientOcclusion(this.aoLightValueScratchXYZNPP, this.aoLightValueYPos);
					this.aoLightValueScratchXYNP = adjustAmbientOcclusion(this.aoLightValueScratchXYNP, this.aoLightValueYPos);
					this.aoLightValueScratchYZPP = adjustAmbientOcclusion(this.aoLightValueScratchYZPP, this.aoLightValueYPos);
					this.aoLightValueScratchXYZPPP = adjustAmbientOcclusion(this.aoLightValueScratchXYZPPP, this.aoLightValueYPos);
					this.aoLightValueScratchXYPP = adjustAmbientOcclusion(this.aoLightValueScratchXYPP, this.aoLightValueYPos);
					this.aoLightValueScratchYZPN = adjustAmbientOcclusion(this.aoLightValueScratchYZPN, this.aoLightValueYPos);
					this.aoLightValueScratchXYZPPN = adjustAmbientOcclusion(this.aoLightValueScratchXYZPPN, this.aoLightValueYPos);
					this.aoLightValueScratchXYZNPN = adjustAmbientOcclusion(this.aoLightValueScratchXYZNPN, this.aoLightValueYPos);
				}
				//Spout end

				var12 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + this.aoLightValueYPos) / 4.0F;
				var9 = (this.aoLightValueScratchYZPP + this.aoLightValueYPos + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
				var10 = (this.aoLightValueYPos + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
				var11 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + this.aoLightValueYPos + this.aoLightValueScratchYZPN) / 4.0F;
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, var24);
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, var24);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, var24);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var24);
			} else {
				var12 = this.aoLightValueYPos;
				var11 = this.aoLightValueYPos;
				var10 = this.aoLightValueYPos;
				var9 = this.aoLightValueYPos;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = var24;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = var14?par5:1.0F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = var14?par6:1.0F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = var14?par7:1.0F;
			this.colorRedTopLeft *= var9;
			this.colorGreenTopLeft *= var9;
			this.colorBlueTopLeft *= var9;
			this.colorRedBottomLeft *= var10;
			this.colorGreenBottomLeft *= var10;
			this.colorBlueBottomLeft *= var10;
			this.colorRedBottomRight *= var11;
			this.colorGreenBottomRight *= var11;
			this.colorBlueBottomRight *= var11;
			this.colorRedTopRight *= var12;
			this.colorGreenTopRight *= var12;
			this.colorBlueTopRight *= var12;
			this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1));
			var8 = true;
		}

		int var27;
		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2)) {
			//Spout start
			if(this.dirtyAmbientOcclusionCache) {
				this.calculateAmbientOcclusionLightValues(par1Block, par2, par3, par4);
			}
			if (par1Block.minZ <= 0.0D) {
				var22 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
			}
			//Spout end
			if (this.aoType > 0) {
				if (par1Block.minZ <= 0.0D) {
					--par4;
				}

				this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
				this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
				this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
				this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
				this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
				this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
				this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
				this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
				if (!this.aoGrassXYZNCN && !this.aoGrassXYZCNN) {
					this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
					this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
				}

				if (!this.aoGrassXYZNCN && !this.aoGrassXYZCPN) {
					this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
					this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
				}

				if (!this.aoGrassXYZPCN && !this.aoGrassXYZCNN) {
					this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
					this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
				}

				if (!this.aoGrassXYZPCN && !this.aoGrassXYZCPN) {
					this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
					this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
				}

				if (par1Block.minZ <= 0.0D) {
					++par4;
				}	
				//Spout start
				if(org.spoutcraft.client.config.ConfigReader.fancyLight && org.spoutcraft.client.config.ConfigReader.smoothLighting > 0F) {
					this.aoLightValueScratchXZNN = adjustAmbientOcclusion(this.aoLightValueScratchXZNN, this.aoLightValueZNeg);
					this.aoLightValueScratchXYZNPN = adjustAmbientOcclusion(this.aoLightValueScratchXYZNPN, this.aoLightValueZNeg);
					this.aoLightValueScratchYZPN = adjustAmbientOcclusion(this.aoLightValueScratchYZPN, this.aoLightValueZNeg);
					this.aoLightValueScratchXZPN = adjustAmbientOcclusion(this.aoLightValueScratchXZPN, this.aoLightValueZNeg);
					this.aoLightValueScratchXYZPPN = adjustAmbientOcclusion(this.aoLightValueScratchXYZPPN, this.aoLightValueZNeg);
					this.aoLightValueScratchYZNN = adjustAmbientOcclusion(this.aoLightValueScratchYZNN, this.aoLightValueZNeg);
					this.aoLightValueScratchXYZPNN = adjustAmbientOcclusion(this.aoLightValueScratchXYZPNN, this.aoLightValueZNeg);
					this.aoLightValueScratchXYZNNN = adjustAmbientOcclusion(this.aoLightValueScratchXYZNNN, this.aoLightValueZNeg);
				}
				//Spout end

				var9 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + this.aoLightValueZNeg + this.aoLightValueScratchYZPN) / 4.0F;
				var10 = (this.aoLightValueZNeg + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
				var11 = (this.aoLightValueScratchYZNN + this.aoLightValueZNeg + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
				var12 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + this.aoLightValueZNeg) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var22);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, var22);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, var22);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, var22);
			} else {
				var12 = this.aoLightValueZNeg;
				var11 = this.aoLightValueZNeg;
				var10 = this.aoLightValueZNeg;
				var9 = this.aoLightValueZNeg;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = var22;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var15?par5:1.0F) * 0.8F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var15?par6:1.0F) * 0.8F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var15?par7:1.0F) * 0.8F;
			this.colorRedTopLeft *= var9;
			this.colorGreenTopLeft *= var9;
			this.colorBlueTopLeft *= var9;
			this.colorRedBottomLeft *= var10;
			this.colorGreenBottomLeft *= var10;
			this.colorBlueBottomLeft *= var10;
			this.colorRedBottomRight *= var11;
			this.colorGreenBottomRight *= var11;
			this.colorBlueBottomRight *= var11;
			this.colorRedTopRight *= var12;
			this.colorGreenTopRight *= var12;
			this.colorBlueTopRight *= var12;
			var27 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2);
			//Spout start
			if (ConfigReader.betterGrass != 0) {
				if(var27 == 3) {
					var27 = getSideGrassTexture(this.blockAccess, par2, par3, par4, 2);
					if(var27 == 0) {
						this.colorRedTopLeft *= par5;
						this.colorRedBottomLeft *= par5;
						this.colorRedBottomRight *= par5;
						this.colorRedTopRight *= par5;
						this.colorGreenTopLeft *= par6;
						this.colorGreenBottomLeft *= par6;
						this.colorGreenBottomRight *= par6;
						this.colorGreenTopRight *= par6;
						this.colorBlueTopLeft *= par7;
						this.colorBlueBottomLeft *= par7;
						this.colorBlueBottomRight *= par7;
						this.colorBlueTopRight *= par7;
					}
				}

				if(var27 == 68) {
					var27 = getSideSnowGrassTexture(this.blockAccess, par2, par3, par4, 2);
				}
			}
			//Spout end
			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, var27);
			if (fancyGrass && var27 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= par5;
				this.colorRedBottomLeft *= par5;
				this.colorRedBottomRight *= par5;
				this.colorRedTopRight *= par5;
				this.colorGreenTopLeft *= par6;
				this.colorGreenBottomLeft *= par6;
				this.colorGreenBottomRight *= par6;
				this.colorGreenTopRight *= par6;
				this.colorBlueTopLeft *= par7;
				this.colorBlueBottomLeft *= par7;
				this.colorBlueBottomRight *= par7;
				this.colorBlueTopRight *= par7;
				this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			var8 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3)) {
			//Spout start
			if(this.dirtyAmbientOcclusionCache) {
				this.calculateAmbientOcclusionLightValues(par1Block, par2, par3, par4);
			}
			if (par1Block.maxZ >= 1.0D) {
				var25 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
			}
			//Spout end
			if (this.aoType > 0) {
				if (par1Block.maxZ >= 1.0D) {
					++par4;
				}

				this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
				this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
				this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
				this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
				this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
				this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
				this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
				this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
				if (!this.aoGrassXYZNCP && !this.aoGrassXYZCNP) {
					this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
					this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
				}

				if (!this.aoGrassXYZNCP && !this.aoGrassXYZCPP) {
					this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
					this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
				}

				if (!this.aoGrassXYZPCP && !this.aoGrassXYZCNP) {
					this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
					this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
				}

				if (!this.aoGrassXYZPCP && !this.aoGrassXYZCPP) {
					this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
					this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
				}

				if (par1Block.maxZ >= 1.0D) {
					--par4;
				}

				//Spout start
				if(org.spoutcraft.client.config.ConfigReader.fancyLight && org.spoutcraft.client.config.ConfigReader.smoothLighting > 0F) {
					this.aoLightValueScratchXZNP = adjustAmbientOcclusion(this.aoLightValueScratchXZNP, this.aoLightValueZPos);
					this.aoLightValueScratchXYZNPP = adjustAmbientOcclusion(this.aoLightValueScratchXYZNPP, this.aoLightValueZPos);
					this.aoLightValueScratchYZPP = adjustAmbientOcclusion(this.aoLightValueScratchYZPP, this.aoLightValueZPos);
					this.aoLightValueScratchXZPP = adjustAmbientOcclusion(this.aoLightValueScratchXZPP, this.aoLightValueZPos);
					this.aoLightValueScratchXYZPPP = adjustAmbientOcclusion(this.aoLightValueScratchXYZPPP, this.aoLightValueZPos);
					this.aoLightValueScratchYZNP = adjustAmbientOcclusion(this.aoLightValueScratchYZNP, this.aoLightValueZPos);
					this.aoLightValueScratchXYZPNP = adjustAmbientOcclusion(this.aoLightValueScratchXYZPNP, this.aoLightValueZPos);
					this.aoLightValueScratchXYZNNP = adjustAmbientOcclusion(this.aoLightValueScratchXYZNNP, this.aoLightValueZPos);
				}
				//Spout end

				var9 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + this.aoLightValueZPos + this.aoLightValueScratchYZPP) / 4.0F;
				var12 = (this.aoLightValueZPos + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
				var11 = (this.aoLightValueScratchYZNP + this.aoLightValueZPos + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
				var10 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + this.aoLightValueZPos) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, var25);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, var25);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var25);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, var25);
			} else {
				var12 = this.aoLightValueZPos;
				var11 = this.aoLightValueZPos;
				var10 = this.aoLightValueZPos;
				var9 = this.aoLightValueZPos;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = var25;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var16?par5:1.0F) * 0.8F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var16?par6:1.0F) * 0.8F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var16?par7:1.0F) * 0.8F;
			this.colorRedTopLeft *= var9;
			this.colorGreenTopLeft *= var9;
			this.colorBlueTopLeft *= var9;
			this.colorRedBottomLeft *= var10;
			this.colorGreenBottomLeft *= var10;
			this.colorBlueBottomLeft *= var10;
			this.colorRedBottomRight *= var11;
			this.colorGreenBottomRight *= var11;
			this.colorBlueBottomRight *= var11;
			this.colorRedTopRight *= var12;
			this.colorGreenTopRight *= var12;
			this.colorBlueTopRight *= var12;
			var27 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3);
			//Spout start
			if (ConfigReader.betterGrass != 0) {
				if(var27 == 3) {
					var27 = getSideGrassTexture(this.blockAccess, par2, par3, par4, 3);
					if(var27 == 0) {
						this.colorRedTopLeft *= par5;
						this.colorRedBottomLeft *= par5;
						this.colorRedBottomRight *= par5;
						this.colorRedTopRight *= par5;
						this.colorGreenTopLeft *= par6;
						this.colorGreenBottomLeft *= par6;
						this.colorGreenBottomRight *= par6;
						this.colorGreenTopRight *= par6;
						this.colorBlueTopLeft *= par7;
						this.colorBlueBottomLeft *= par7;
						this.colorBlueBottomRight *= par7;
						this.colorBlueTopRight *= par7;
					}
				}

				if(var27 == 68) {
					var27 = getSideSnowGrassTexture(this.blockAccess, par2, par3, par4, 3);
				}
			}
			//Spout end
			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3));
			if (fancyGrass && var27 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= par5;
				this.colorRedBottomLeft *= par5;
				this.colorRedBottomRight *= par5;
				this.colorRedTopRight *= par5;
				this.colorGreenTopLeft *= par6;
				this.colorGreenBottomLeft *= par6;
				this.colorGreenBottomRight *= par6;
				this.colorGreenTopRight *= par6;
				this.colorBlueTopLeft *= par7;
				this.colorBlueBottomLeft *= par7;
				this.colorBlueBottomRight *= par7;
				this.colorBlueTopRight *= par7;
				this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			var8 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4)) {
			//Spout start
			if(this.dirtyAmbientOcclusionCache) {
				this.calculateAmbientOcclusionLightValues(par1Block, par2, par3, par4);
			}
			if (par1Block.minX <= 0.0D) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
			}
			//Spout end
			if (this.aoType > 0) {
				if (par1Block.minX <= 0.0D) {
					--par2;
				}

				this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
				this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
				this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
				this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
				this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
				this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
				this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
				this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
				if (!this.aoGrassXYZNCN && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
					this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
				}

				if (!this.aoGrassXYZNCP && !this.aoGrassXYZNNC) {
					this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
					this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
				}

				if (!this.aoGrassXYZNCN && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
					this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
				} else {
					this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
					this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
				}

				if (!this.aoGrassXYZNCP && !this.aoGrassXYZNPC) {
					this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
					this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
				} else {
					this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
					this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
				}

				if (par1Block.minX <= 0.0D) {
					++par2;
				}

				//Spout start
				if(org.spoutcraft.client.config.ConfigReader.fancyLight && org.spoutcraft.client.config.ConfigReader.smoothLighting > 0F) {
					this.aoLightValueScratchXYNN = adjustAmbientOcclusion(this.aoLightValueScratchXYNN, this.aoLightValueXNeg);
					this.aoLightValueScratchXYZNNP = adjustAmbientOcclusion(this.aoLightValueScratchXYZNNP, this.aoLightValueXNeg);
					this.aoLightValueScratchXZNP = adjustAmbientOcclusion(this.aoLightValueScratchXZNP, this.aoLightValueXNeg);
					this.aoLightValueScratchXYNP = adjustAmbientOcclusion(this.aoLightValueScratchXYNP, this.aoLightValueXNeg);
					this.aoLightValueScratchXYZNPP = adjustAmbientOcclusion(this.aoLightValueScratchXYZNPP, this.aoLightValueXNeg);
					this.aoLightValueScratchXZNN = adjustAmbientOcclusion(this.aoLightValueScratchXZNN, this.aoLightValueXNeg);
					this.aoLightValueScratchXYZNPN = adjustAmbientOcclusion(this.aoLightValueScratchXYZNPN, this.aoLightValueXNeg);
					this.aoLightValueScratchXYZNNN = adjustAmbientOcclusion(this.aoLightValueScratchXYZNNN, this.aoLightValueXNeg);
				}
				//Spout end

				var12 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + this.aoLightValueXNeg + this.aoLightValueScratchXZNP) / 4.0F;
				var9 = (this.aoLightValueXNeg + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
				var10 = (this.aoLightValueScratchXZNN + this.aoLightValueXNeg + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
				var11 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + this.aoLightValueXNeg) / 4.0F;
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, var20);
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, var20);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, var20);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, var20);
			} else {
				var12 = this.aoLightValueXNeg;
				var11 = this.aoLightValueXNeg;
				var10 = this.aoLightValueXNeg;
				var9 = this.aoLightValueXNeg;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = var20;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var17?par5:1.0F) * 0.6F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var17?par6:1.0F) * 0.6F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var17?par7:1.0F) * 0.6F;
			this.colorRedTopLeft *= var9;
			this.colorGreenTopLeft *= var9;
			this.colorBlueTopLeft *= var9;
			this.colorRedBottomLeft *= var10;
			this.colorGreenBottomLeft *= var10;
			this.colorBlueBottomLeft *= var10;
			this.colorRedBottomRight *= var11;
			this.colorGreenBottomRight *= var11;
			this.colorBlueBottomRight *= var11;
			this.colorRedTopRight *= var12;
			this.colorGreenTopRight *= var12;
			this.colorBlueTopRight *= var12;
			var27 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4);
			//Spout start
			if (ConfigReader.betterGrass != 0) {
				if(var27 == 3) {
					var27 = getSideGrassTexture(this.blockAccess, par2, par3, par4, 4);
					if(var27 == 0) {
						this.colorRedTopLeft *= par5;
						this.colorRedBottomLeft *= par5;
						this.colorRedBottomRight *= par5;
						this.colorRedTopRight *= par5;
						this.colorGreenTopLeft *= par6;
						this.colorGreenBottomLeft *= par6;
						this.colorGreenBottomRight *= par6;
						this.colorGreenTopRight *= par6;
						this.colorBlueTopLeft *= par7;
						this.colorBlueBottomLeft *= par7;
						this.colorBlueBottomRight *= par7;
						this.colorBlueTopRight *= par7;
					}
				}

				if(var27 == 68) {
					var27 = getSideSnowGrassTexture(this.blockAccess, par2, par3, par4, 4);
				}
			}
			//Spout end
			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, var27);
			if (fancyGrass && var27 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= par5;
				this.colorRedBottomLeft *= par5;
				this.colorRedBottomRight *= par5;
				this.colorRedTopRight *= par5;
				this.colorGreenTopLeft *= par6;
				this.colorGreenBottomLeft *= par6;
				this.colorGreenBottomRight *= par6;
				this.colorGreenTopRight *= par6;
				this.colorBlueTopLeft *= par7;
				this.colorBlueBottomLeft *= par7;
				this.colorBlueBottomRight *= par7;
				this.colorBlueTopRight *= par7;
				this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			var8 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)) {
			//Spout start
			if(this.dirtyAmbientOcclusionCache) {
				this.calculateAmbientOcclusionLightValues(par1Block, par2, par3, par4);
			}
			if (par1Block.maxX >= 1.0D) {
				var23 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
			}
			//Spout end
			if (this.aoType > 0) {
				if (par1Block.maxX >= 1.0D) {
					++par2;
				}

				this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
				this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
				this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
				this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
				this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
				this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
				this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
				this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
				if (!this.aoGrassXYZPNC && !this.aoGrassXYZPCN) {
					this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
					this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
				}

				if (!this.aoGrassXYZPNC && !this.aoGrassXYZPCP) {
					this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
					this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
				}

				if (!this.aoGrassXYZPPC && !this.aoGrassXYZPCN) {
					this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
					this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
				} else {
					this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
					this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
				}

				if (!this.aoGrassXYZPPC && !this.aoGrassXYZPCP) {
					this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
					this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
				} else {
					this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
					this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
				}

				if (par1Block.maxX >= 1.0D) {
					--par2;
				}

				//Spout start
				if(org.spoutcraft.client.config.ConfigReader.fancyLight && org.spoutcraft.client.config.ConfigReader.smoothLighting > 0F) {
					this.aoLightValueScratchXYPN = adjustAmbientOcclusion(this.aoLightValueScratchXYPN, this.aoLightValueXPos);
					this.aoLightValueScratchXYZPNP = adjustAmbientOcclusion(this.aoLightValueScratchXYZPNP, this.aoLightValueXPos);
					this.aoLightValueScratchXZPP = adjustAmbientOcclusion(this.aoLightValueScratchXZPP, this.aoLightValueXPos);
					this.aoLightValueScratchXYPP = adjustAmbientOcclusion(this.aoLightValueScratchXYPP, this.aoLightValueXPos);
					this.aoLightValueScratchXYZPPP = adjustAmbientOcclusion(this.aoLightValueScratchXYZPPP, this.aoLightValueXPos);
					this.aoLightValueScratchXZPN = adjustAmbientOcclusion(this.aoLightValueScratchXZPN, this.aoLightValueXPos);
					this.aoLightValueScratchXYZPPN = adjustAmbientOcclusion(this.aoLightValueScratchXYZPPN, this.aoLightValueXPos);
					this.aoLightValueScratchXYZPNN = adjustAmbientOcclusion(this.aoLightValueScratchXYZPNN, this.aoLightValueXPos);
				}
				//Spout end

				var9 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + this.aoLightValueXPos + this.aoLightValueScratchXZPP) / 4.0F;
				var12 = (this.aoLightValueXPos + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
				var11 = (this.aoLightValueScratchXZPN + this.aoLightValueXPos + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
				var10 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + this.aoLightValueXPos) / 4.0F;
				this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var23);
				this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, var23);
				this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, var23);
				this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, var23);
			} else {
				var12 = this.aoLightValueXPos;
				var11 = this.aoLightValueXPos;
				var10 = this.aoLightValueXPos;
				var9 = this.aoLightValueXPos;
				this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = var23;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var18?par5:1.0F) * 0.6F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var18?par6:1.0F) * 0.6F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var18?par7:1.0F) * 0.6F;
			this.colorRedTopLeft *= var9;
			this.colorGreenTopLeft *= var9;
			this.colorBlueTopLeft *= var9;
			this.colorRedBottomLeft *= var10;
			this.colorGreenBottomLeft *= var10;
			this.colorBlueBottomLeft *= var10;
			this.colorRedBottomRight *= var11;
			this.colorGreenBottomRight *= var11;
			this.colorBlueBottomRight *= var11;
			this.colorRedTopRight *= var12;
			this.colorGreenTopRight *= var12;
			this.colorBlueTopRight *= var12;
			var27 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5);
			//Spout start
			if (ConfigReader.betterGrass != 0) {
				if(var27 == 3) {
					var27 = getSideGrassTexture(this.blockAccess, par2, par3, par4, 5);
					if(var27 == 0) {
						this.colorRedTopLeft *= par5;
						this.colorRedBottomLeft *= par5;
						this.colorRedBottomRight *= par5;
						this.colorRedTopRight *= par5;
						this.colorGreenTopLeft *= par6;
						this.colorGreenBottomLeft *= par6;
						this.colorGreenBottomRight *= par6;
						this.colorGreenTopRight *= par6;
						this.colorBlueTopLeft *= par7;
						this.colorBlueBottomLeft *= par7;
						this.colorBlueBottomRight *= par7;
						this.colorBlueTopRight *= par7;
					}
				}

				if(var27 == 68) {
					var27 = getSideSnowGrassTexture(this.blockAccess, par2, par3, par4, 5);
				}
			}
			//Spout end
			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, var27);
			if (fancyGrass && var27 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= par5;
				this.colorRedBottomLeft *= par5;
				this.colorRedBottomRight *= par5;
				this.colorRedTopRight *= par5;
				this.colorGreenTopLeft *= par6;
				this.colorGreenBottomLeft *= par6;
				this.colorGreenBottomRight *= par6;
				this.colorGreenTopRight *= par6;
				this.colorBlueTopLeft *= par7;
				this.colorBlueBottomLeft *= par7;
				this.colorBlueBottomRight *= par7;
				this.colorBlueTopRight *= par7;
				this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			var8 = true;
		}

		this.enableAO = false;
		return var8;
	}

	private int getAoBrightness(int par1, int par2, int par3, int par4) {
		if (par1 == 0) {
			par1 = par4;
		}

		if (par2 == 0) {
			par2 = par4;
		}

		if (par3 == 0) {
			par3 = par4;
		}

		return par1 + par2 + par3 + par4 >> 2 & 16711935;
	}

	public boolean renderStandardBlockWithColorMultiplier(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
		this.enableAO = false;
		Tessellator var8 = Tessellator.instance;
		boolean var9 = false;
		float var10 = 0.5F;
		float var11 = 1.0F;
		float var12 = 0.8F;
		float var13 = 0.6F;
		float var14 = var11 * par5;
		float var15 = var11 * par6;
		float var16 = var11 * par7;
		float var17 = var10;
		float var18 = var12;
		float var19 = var13;
		float var20 = var10;
		float var21 = var12;
		float var22 = var13;
		float var23 = var10;
		float var24 = var12;
		float var25 = var13;
		if (par1Block != Block.grass) {
			var17 = var10 * par5;
			var18 = var12 * par5;
			var19 = var13 * par5;
			var20 = var10 * par6;
			var21 = var12 * par6;
			var22 = var13 * par6;
			var23 = var10 * par7;
			var24 = var12 * par7;
			var25 = var13 * par7;
		}

		int var26 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0)) {
			var8.setBrightness(par1Block.minY > 0.0D?var26:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
			var8.setColorOpaque_F(var17, var20, var23);
			this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0));
			var9 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1)) {
			var8.setBrightness(par1Block.maxY < 1.0D?var26:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
			var8.setColorOpaque_F(var14, var15, var16);
			this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1));
			var9 = true;
		}

		int var28;
		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2)) {
			var8.setBrightness(par1Block.minZ > 0.0D?var26:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
			var8.setColorOpaque_F(var18, var21, var24);
			var28 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2);
			//Spout start
			if(var28 == 0) {
				var8.setColorOpaque_F(par5 * par5, par6 * par6, par7 * par7);
			}
			//Spout end
			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, var28);
			if (fancyGrass && var28 == 3 && this.overrideBlockTexture < 0) {
				var8.setColorOpaque_F(var18 * par5, var21 * par6, var24 * par7);
				this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			var9 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3)) {
			var8.setBrightness(par1Block.maxZ < 1.0D?var26:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
			var8.setColorOpaque_F(var18, var21, var24);
			var28 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3);
			//Spout start
			if(var28 == 0) {
				var8.setColorOpaque_F(par5 * par5, par6 * par6, par7 * par7);
			}
			//Spout end
			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, var28);
			if (fancyGrass && var28 == 3 && this.overrideBlockTexture < 0) {
				var8.setColorOpaque_F(var18 * par5, var21 * par6, var24 * par7);
				this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			var9 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4)) {
			var8.setBrightness(par1Block.minX > 0.0D?var26:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
			var8.setColorOpaque_F(var19, var22, var25);
			var28 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4);
			//Spout start
			if(var28 == 0) {
				var8.setColorOpaque_F(par5 * par5, par6 * par6, par7 * par7);
			}
			//Spout end
			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, var28);
			if (fancyGrass && var28 == 3 && this.overrideBlockTexture < 0) {
				var8.setColorOpaque_F(var19 * par5, var22 * par6, var25 * par7);
				this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			var9 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)) {
			var8.setBrightness(par1Block.maxX < 1.0D?var26:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
			var8.setColorOpaque_F(var19, var22, var25);
			var28 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5);
			//Spout start
			if(var28 == 0) {
				var8.setColorOpaque_F(par5 * par5, par6 * par6, par7 * par7);
			}
			//Spout end
			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, var28);
			if (fancyGrass && var28 == 3 && this.overrideBlockTexture < 0) {
				var8.setColorOpaque_F(var19 * par5, var22 * par6, var25 * par7);
				this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
			}

			var9 = true;
		}

		return var9;
	}

	public boolean renderBlockCactus(Block par1Block, int par2, int par3, int par4) {
		int var5 = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var6 = (float)(var5 >> 16 & 255) / 255.0F;
		float var7 = (float)(var5 >> 8 & 255) / 255.0F;
		float var8 = (float)(var5 & 255) / 255.0F;
		if (EntityRenderer.anaglyphEnable) {
			float var9 = (var6 * 30.0F + var7 * 59.0F + var8 * 11.0F) / 100.0F;
			float var10 = (var6 * 30.0F + var7 * 70.0F) / 100.0F;
			float var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
			var6 = var9;
			var7 = var10;
			var8 = var11;
		}

		return this.renderBlockCactusImpl(par1Block, par2, par3, par4, var6, var7, var8);
	}

	public boolean renderBlockCactusImpl(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
		Tessellator var8 = Tessellator.instance;
		boolean var9 = false;
		float var10 = 0.5F;
		float var11 = 1.0F;
		float var12 = 0.8F;
		float var13 = 0.6F;
		float var14 = var10 * par5;
		float var15 = var11 * par5;
		float var16 = var12 * par5;
		float var17 = var13 * par5;
		float var18 = var10 * par6;
		float var19 = var11 * par6;
		float var20 = var12 * par6;
		float var21 = var13 * par6;
		float var22 = var10 * par7;
		float var23 = var11 * par7;
		float var24 = var12 * par7;
		float var25 = var13 * par7;
		float var26 = 0.0625F;
		int var28 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0)) {
			var8.setBrightness(par1Block.minY > 0.0D?var28:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
			var8.setColorOpaque_F(var14, var18, var22);
			this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0));
			var9 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1)) {
			var8.setBrightness(par1Block.maxY < 1.0D?var28:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
			var8.setColorOpaque_F(var15, var19, var23);
			this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1));
			var9 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2)) {
			var8.setBrightness(par1Block.minZ > 0.0D?var28:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
			var8.setColorOpaque_F(var16, var20, var24);
			var8.setTranslationF(0.0F, 0.0F, var26);
			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2));
			var8.setTranslationF(0.0F, 0.0F, -var26);
			var9 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3)) {
			var8.setBrightness(par1Block.maxZ < 1.0D?var28:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
			var8.setColorOpaque_F(var16, var20, var24);
			var8.setTranslationF(0.0F, 0.0F, -var26);
			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3));
			var8.setTranslationF(0.0F, 0.0F, var26);
			var9 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4)) {
			var8.setBrightness(par1Block.minX > 0.0D?var28:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
			var8.setColorOpaque_F(var17, var21, var25);
			var8.setTranslationF(var26, 0.0F, 0.0F);
			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4));
			var8.setTranslationF(-var26, 0.0F, 0.0F);
			var9 = true;
		}

		if (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)) {
			var8.setBrightness(par1Block.maxX < 1.0D?var28:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
			var8.setColorOpaque_F(var17, var21, var25);
			var8.setTranslationF(-var26, 0.0F, 0.0F);
			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5));
			var8.setTranslationF(var26, 0.0F, 0.0F);
			var9 = true;
		}

		return var9;
	}

	public boolean renderBlockFence(BlockFence par1BlockFence, int par2, int par3, int par4) {
		boolean var5 = false;
		float var6 = 0.375F;
		float var7 = 0.625F;
		par1BlockFence.setBlockBounds(var6, 0.0F, var6, var7, 1.0F, var7);
		this.renderStandardBlock(par1BlockFence, par2, par3, par4);
		var5 = true;
		boolean var8 = false;
		boolean var9 = false;
		if (par1BlockFence.canConnectFenceTo(this.blockAccess, par2 - 1, par3, par4) || par1BlockFence.canConnectFenceTo(this.blockAccess, par2 + 1, par3, par4)) {
			var8 = true;
		}

		if (par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 - 1) || par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 + 1)) {
			var9 = true;
		}

		boolean var10 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2 - 1, par3, par4);
		boolean var11 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2 + 1, par3, par4);
		boolean var12 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 - 1);
		boolean var13 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 + 1);
		if (!var8 && !var9) {
			var8 = true;
		}

		var6 = 0.4375F;
		var7 = 0.5625F;
		float var14 = 0.75F;
		float var15 = 0.9375F;
		float var16 = var10?0.0F:var6;
		float var17 = var11?1.0F:var7;
		float var18 = var12?0.0F:var6;
		float var19 = var13?1.0F:var7;
		if (var8) {
			par1BlockFence.setBlockBounds(var16, var14, var6, var17, var15, var7);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			var5 = true;
		}

		if (var9) {
			par1BlockFence.setBlockBounds(var6, var14, var18, var7, var15, var19);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			var5 = true;
		}

		var14 = 0.375F;
		var15 = 0.5625F;
		if (var8) {
			par1BlockFence.setBlockBounds(var16, var14, var6, var17, var15, var7);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			var5 = true;
		}

		if (var9) {
			par1BlockFence.setBlockBounds(var6, var14, var18, var7, var15, var19);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			var5 = true;
		}

		par1BlockFence.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
		return var5;
	}

	public boolean renderBlockDragonEgg(BlockDragonEgg par1BlockDragonEgg, int par2, int par3, int par4) {
		boolean var5 = false;
		int var6 = 0;

		for (int var7 = 0; var7 < 8; ++var7) {
			byte var8 = 0;
			byte var9 = 1;
			if (var7 == 0) {
				var8 = 2;
			}

			if (var7 == 1) {
				var8 = 3;
			}

			if (var7 == 2) {
				var8 = 4;
			}

			if (var7 == 3) {
				var8 = 5;
				var9 = 2;
			}

			if (var7 == 4) {
				var8 = 6;
				var9 = 3;
			}

			if (var7 == 5) {
				var8 = 7;
				var9 = 5;
			}

			if (var7 == 6) {
				var8 = 6;
				var9 = 2;
			}

			if (var7 == 7) {
				var8 = 3;
			}

			float var10 = (float)var8 / 16.0F;
			float var11 = 1.0F - (float)var6 / 16.0F;
			float var12 = 1.0F - (float)(var6 + var9) / 16.0F;
			var6 += var9;
			par1BlockDragonEgg.setBlockBounds(0.5F - var10, var12, 0.5F - var10, 0.5F + var10, var11, 0.5F + var10);
			this.renderStandardBlock(par1BlockDragonEgg, par2, par3, par4);
		}

		var5 = true;
		par1BlockDragonEgg.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return var5;
	}

	public boolean renderBlockFenceGate(BlockFenceGate par1BlockFenceGate, int par2, int par3, int par4) {
		boolean var5 = true;
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		boolean var7 = BlockFenceGate.isFenceGateOpen(var6);
		int var8 = BlockDirectional.func_48216_a(var6);
		float var15;
		float var17;
		float var16;
		float var18;
		if (var8 != 3 && var8 != 1) {
			var15 = 0.0F;
			var16 = 0.125F;
			var17 = 0.4375F;
			var18 = 0.5625F;
			par1BlockFenceGate.setBlockBounds(var15, 0.3125F, var17, var16, 1.0F, var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			var15 = 0.875F;
			var16 = 1.0F;
			par1BlockFenceGate.setBlockBounds(var15, 0.3125F, var17, var16, 1.0F, var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		} else {
			var15 = 0.4375F;
			var16 = 0.5625F;
			var17 = 0.0F;
			var18 = 0.125F;
			par1BlockFenceGate.setBlockBounds(var15, 0.3125F, var17, var16, 1.0F, var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			var17 = 0.875F;
			var18 = 1.0F;
			par1BlockFenceGate.setBlockBounds(var15, 0.3125F, var17, var16, 1.0F, var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		}

		if (!var7) {
			if (var8 != 3 && var8 != 1) {
				var15 = 0.375F;
				var16 = 0.5F;
				var17 = 0.4375F;
				var18 = 0.5625F;
				par1BlockFenceGate.setBlockBounds(var15, 0.375F, var17, var16, 0.9375F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				var15 = 0.5F;
				var16 = 0.625F;
				par1BlockFenceGate.setBlockBounds(var15, 0.375F, var17, var16, 0.9375F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				var15 = 0.625F;
				var16 = 0.875F;
				par1BlockFenceGate.setBlockBounds(var15, 0.375F, var17, var16, 0.5625F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				par1BlockFenceGate.setBlockBounds(var15, 0.75F, var17, var16, 0.9375F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				var15 = 0.125F;
				var16 = 0.375F;
				par1BlockFenceGate.setBlockBounds(var15, 0.375F, var17, var16, 0.5625F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				par1BlockFenceGate.setBlockBounds(var15, 0.75F, var17, var16, 0.9375F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			} else {
				var15 = 0.4375F;
				var16 = 0.5625F;
				var17 = 0.375F;
				var18 = 0.5F;
				par1BlockFenceGate.setBlockBounds(var15, 0.375F, var17, var16, 0.9375F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				var17 = 0.5F;
				var18 = 0.625F;
				par1BlockFenceGate.setBlockBounds(var15, 0.375F, var17, var16, 0.9375F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				var17 = 0.625F;
				var18 = 0.875F;
				par1BlockFenceGate.setBlockBounds(var15, 0.375F, var17, var16, 0.5625F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				par1BlockFenceGate.setBlockBounds(var15, 0.75F, var17, var16, 0.9375F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				var17 = 0.125F;
				var18 = 0.375F;
				par1BlockFenceGate.setBlockBounds(var15, 0.375F, var17, var16, 0.5625F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				par1BlockFenceGate.setBlockBounds(var15, 0.75F, var17, var16, 0.9375F, var18);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			}
		} else if (var8 == 3) {
			par1BlockFenceGate.setBlockBounds(0.8125F, 0.375F, 0.0F, 0.9375F, 0.9375F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.8125F, 0.375F, 0.875F, 0.9375F, 0.9375F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.5625F, 0.375F, 0.0F, 0.8125F, 0.5625F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.5625F, 0.375F, 0.875F, 0.8125F, 0.5625F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.5625F, 0.75F, 0.0F, 0.8125F, 0.9375F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.5625F, 0.75F, 0.875F, 0.8125F, 0.9375F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		} else if (var8 == 1) {
			par1BlockFenceGate.setBlockBounds(0.0625F, 0.375F, 0.0F, 0.1875F, 0.9375F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.0625F, 0.375F, 0.875F, 0.1875F, 0.9375F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.1875F, 0.375F, 0.0F, 0.4375F, 0.5625F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.1875F, 0.375F, 0.875F, 0.4375F, 0.5625F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.1875F, 0.75F, 0.0F, 0.4375F, 0.9375F, 0.125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.1875F, 0.75F, 0.875F, 0.4375F, 0.9375F, 1.0F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		} else if (var8 == 0) {
			par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.8125F, 0.125F, 0.9375F, 0.9375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.8125F, 1.0F, 0.9375F, 0.9375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.5625F, 0.125F, 0.5625F, 0.8125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.5625F, 1.0F, 0.5625F, 0.8125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.0F, 0.75F, 0.5625F, 0.125F, 0.9375F, 0.8125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.75F, 0.5625F, 1.0F, 0.9375F, 0.8125F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		} else if (var8 == 2) {
			par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.0625F, 0.125F, 0.9375F, 0.1875F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.0625F, 1.0F, 0.9375F, 0.1875F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.1875F, 0.125F, 0.5625F, 0.4375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.1875F, 1.0F, 0.5625F, 0.4375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.0F, 0.75F, 0.1875F, 0.125F, 0.9375F, 0.4375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			par1BlockFenceGate.setBlockBounds(0.875F, 0.75F, 0.1875F, 1.0F, 0.9375F, 0.4375F);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		}

		par1BlockFenceGate.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return var5;
	}

	public boolean renderBlockStairs(Block par1Block, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var6 = var5 & 3;
		float var7 = 0.0F;
		float var8 = 0.5F;
		float var9 = 0.5F;
		float var10 = 1.0F;
		if ((var5 & 4) != 0) {
			var7 = 0.5F;
			var8 = 1.0F;
			var9 = 0.0F;
			var10 = 0.5F;
		}

		par1Block.setBlockBounds(0.0F, var7, 0.0F, 1.0F, var8, 1.0F);
		this.renderStandardBlock(par1Block, par2, par3, par4);
		if (var6 == 0) {
			par1Block.setBlockBounds(0.5F, var9, 0.0F, 1.0F, var10, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
		} else if (var6 == 1) {
			par1Block.setBlockBounds(0.0F, var9, 0.0F, 0.5F, var10, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
		} else if (var6 == 2) {
			par1Block.setBlockBounds(0.0F, var9, 0.5F, 1.0F, var10, 1.0F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
		} else if (var6 == 3) {
			par1Block.setBlockBounds(0.0F, var9, 0.0F, 1.0F, var10, 0.5F);
			this.renderStandardBlock(par1Block, par2, par3, par4);
		}

		par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}

	public boolean renderBlockDoor(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		BlockDoor var6 = (BlockDoor)par1Block;
		boolean var7 = false;
		float var8 = 0.5F;
		float var9 = 1.0F;
		float var10 = 0.8F;
		float var11 = 0.6F;
		int var12 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		var5.setBrightness(par1Block.minY > 0.0D?var12:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
		var5.setColorOpaque_F(var8, var8, var8);
		this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0));
		var7 = true;
		var5.setBrightness(par1Block.maxY < 1.0D?var12:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
		var5.setColorOpaque_F(var9, var9, var9);
		this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1));
		var7 = true;
		var5.setBrightness(par1Block.minZ > 0.0D?var12:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
		var5.setColorOpaque_F(var10, var10, var10);
		int var14 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2);
		if (var14 < 0) {
			this.flipTexture = true;
			var14 = -var14;
		}

		this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, var14);
		var7 = true;
		this.flipTexture = false;
		var5.setBrightness(par1Block.maxZ < 1.0D?var12:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
		var5.setColorOpaque_F(var10, var10, var10);
		var14 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3);
		if (var14 < 0) {
			this.flipTexture = true;
			var14 = -var14;
		}

		this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, var14);
		var7 = true;
		this.flipTexture = false;
		var5.setBrightness(par1Block.minX > 0.0D?var12:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
		var5.setColorOpaque_F(var11, var11, var11);
		var14 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4);
		if (var14 < 0) {
			this.flipTexture = true;
			var14 = -var14;
		}

		this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, var14);
		var7 = true;
		this.flipTexture = false;
		var5.setBrightness(par1Block.maxX < 1.0D?var12:par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
		var5.setColorOpaque_F(var11, var11, var11);
		var14 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5);
		if (var14 < 0) {
			this.flipTexture = true;
			var14 = -var14;
		}

		this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, var14);
		var7 = true;
		this.flipTexture = false;
		return var7;
	}

	public void renderBottomFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator var9 = Tessellator.instance;
		if (this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int var10 = (par8 & 15) << 4;
		int var11 = par8 & 240;
		double var12 = ((double)var10 + par1Block.minX * 16.0D) / 256.0D;
		double var14 = ((double)var10 + par1Block.maxX * 16.0D - 0.01D) / 256.0D;
		double var16 = ((double)var11 + par1Block.minZ * 16.0D) / 256.0D;
		double var18 = ((double)var11 + par1Block.maxZ * 16.0D - 0.01D) / 256.0D;
		if (par1Block.minX < 0.0D || par1Block.maxX > 1.0D) {
			var12 = (double)(((float)var10 + 0.0F) / 256.0F);
			var14 = (double)(((float)var10 + 15.99F) / 256.0F);
		}

		if (par1Block.minZ < 0.0D || par1Block.maxZ > 1.0D) {
			var16 = (double)(((float)var11 + 0.0F) / 256.0F);
			var18 = (double)(((float)var11 + 15.99F) / 256.0F);
		}

		double var20 = var14;
		double var22 = var12;
		double var24 = var16;
		double var26 = var18;
		if (this.uvRotateBottom == 2) {
			var12 = ((double)var10 + par1Block.minZ * 16.0D) / 256.0D;
			var16 = ((double)(var11 + 16) - par1Block.maxX * 16.0D) / 256.0D;
			var14 = ((double)var10 + par1Block.maxZ * 16.0D) / 256.0D;
			var18 = ((double)(var11 + 16) - par1Block.minX * 16.0D) / 256.0D;
			var24 = var16;
			var26 = var18;
			var20 = var12;
			var22 = var14;
			var16 = var18;
			var18 = var24;
		} else if (this.uvRotateBottom == 1) {
			var12 = ((double)(var10 + 16) - par1Block.maxZ * 16.0D) / 256.0D;
			var16 = ((double)var11 + par1Block.minX * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.minZ * 16.0D) / 256.0D;
			var18 = ((double)var11 + par1Block.maxX * 16.0D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var12 = var14;
			var14 = var22;
			var24 = var18;
			var26 = var16;
		} else if (this.uvRotateBottom == 3) {
			var12 = ((double)(var10 + 16) - par1Block.minX * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.maxX * 16.0D - 0.01D) / 256.0D;
			var16 = ((double)(var11 + 16) - par1Block.minZ * 16.0D) / 256.0D;
			var18 = ((double)(var11 + 16) - par1Block.maxZ * 16.0D - 0.01D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var24 = var16;
			var26 = var18;
		}

		double var28 = par2 + par1Block.minX;
		double var30 = par2 + par1Block.maxX;
		double var32 = par4 + par1Block.minY;
		double var34 = par6 + par1Block.minZ;
		double var36 = par6 + par1Block.maxZ;
		if (this.enableAO) {
			var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			var9.setBrightness(this.brightnessTopLeft);
			var9.addVertexWithUV(var28, var32, var36, var22, var26);
			var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			var9.setBrightness(this.brightnessBottomLeft);
			var9.addVertexWithUV(var28, var32, var34, var12, var16);
			var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			var9.setBrightness(this.brightnessBottomRight);
			var9.addVertexWithUV(var30, var32, var34, var20, var24);
			var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			var9.setBrightness(this.brightnessTopRight);
			var9.addVertexWithUV(var30, var32, var36, var14, var18);
		} else {
			var9.addVertexWithUV(var28, var32, var36, var22, var26);
			var9.addVertexWithUV(var28, var32, var34, var12, var16);
			var9.addVertexWithUV(var30, var32, var34, var20, var24);
			var9.addVertexWithUV(var30, var32, var36, var14, var18);
		}

	}

	public void renderTopFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator var9 = Tessellator.instance;
		if (this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int var10 = (par8 & 15) << 4;
		int var11 = par8 & 240;
		double var12 = ((double)var10 + par1Block.minX * 16.0D) / 256.0D;
		double var14 = ((double)var10 + par1Block.maxX * 16.0D - 0.01D) / 256.0D;
		double var16 = ((double)var11 + par1Block.minZ * 16.0D) / 256.0D;
		double var18 = ((double)var11 + par1Block.maxZ * 16.0D - 0.01D) / 256.0D;
		if (par1Block.minX < 0.0D || par1Block.maxX > 1.0D) {
			var12 = (double)(((float)var10 + 0.0F) / 256.0F);
			var14 = (double)(((float)var10 + 15.99F) / 256.0F);
		}

		if (par1Block.minZ < 0.0D || par1Block.maxZ > 1.0D) {
			var16 = (double)(((float)var11 + 0.0F) / 256.0F);
			var18 = (double)(((float)var11 + 15.99F) / 256.0F);
		}

		double var20 = var14;
		double var22 = var12;
		double var24 = var16;
		double var26 = var18;
		if (this.uvRotateTop == 1) {
			var12 = ((double)var10 + par1Block.minZ * 16.0D) / 256.0D;
			var16 = ((double)(var11 + 16) - par1Block.maxX * 16.0D) / 256.0D;
			var14 = ((double)var10 + par1Block.maxZ * 16.0D) / 256.0D;
			var18 = ((double)(var11 + 16) - par1Block.minX * 16.0D) / 256.0D;
			var24 = var16;
			var26 = var18;
			var20 = var12;
			var22 = var14;
			var16 = var18;
			var18 = var24;
		} else if (this.uvRotateTop == 2) {
			var12 = ((double)(var10 + 16) - par1Block.maxZ * 16.0D) / 256.0D;
			var16 = ((double)var11 + par1Block.minX * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.minZ * 16.0D) / 256.0D;
			var18 = ((double)var11 + par1Block.maxX * 16.0D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var12 = var14;
			var14 = var22;
			var24 = var18;
			var26 = var16;
		} else if (this.uvRotateTop == 3) {
			var12 = ((double)(var10 + 16) - par1Block.minX * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.maxX * 16.0D - 0.01D) / 256.0D;
			var16 = ((double)(var11 + 16) - par1Block.minZ * 16.0D) / 256.0D;
			var18 = ((double)(var11 + 16) - par1Block.maxZ * 16.0D - 0.01D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var24 = var16;
			var26 = var18;
		}

		double var28 = par2 + par1Block.minX;
		double var30 = par2 + par1Block.maxX;
		double var32 = par4 + par1Block.maxY;
		double var34 = par6 + par1Block.minZ;
		double var36 = par6 + par1Block.maxZ;
		if (this.enableAO) {
			var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			var9.setBrightness(this.brightnessTopLeft);
			var9.addVertexWithUV(var30, var32, var36, var14, var18);
			var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			var9.setBrightness(this.brightnessBottomLeft);
			var9.addVertexWithUV(var30, var32, var34, var20, var24);
			var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			var9.setBrightness(this.brightnessBottomRight);
			var9.addVertexWithUV(var28, var32, var34, var12, var16);
			var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			var9.setBrightness(this.brightnessTopRight);
			var9.addVertexWithUV(var28, var32, var36, var22, var26);
		} else {
			var9.addVertexWithUV(var30, var32, var36, var14, var18);
			var9.addVertexWithUV(var30, var32, var34, var20, var24);
			var9.addVertexWithUV(var28, var32, var34, var12, var16);
			var9.addVertexWithUV(var28, var32, var36, var22, var26);
		}

	}

	public void renderEastFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator var9 = Tessellator.instance;
		if (this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int var10 = (par8 & 15) << 4;
		int var11 = par8 & 240;
		double var12 = ((double)var10 + par1Block.minX * 16.0D) / 256.0D;
		double var14 = ((double)var10 + par1Block.maxX * 16.0D - 0.01D) / 256.0D;
		double var16 = ((double)(var11 + 16) - par1Block.maxY * 16.0D) / 256.0D;
		double var18 = ((double)(var11 + 16) - par1Block.minY * 16.0D - 0.01D) / 256.0D;
		double var20;
		if (this.flipTexture) {
			var20 = var12;
			var12 = var14;
			var14 = var20;
		}

		if (par1Block.minX < 0.0D || par1Block.maxX > 1.0D) {
			var12 = (double)(((float)var10 + 0.0F) / 256.0F);
			var14 = (double)(((float)var10 + 15.99F) / 256.0F);
		}

		if (par1Block.minY < 0.0D || par1Block.maxY > 1.0D) {
			var16 = (double)(((float)var11 + 0.0F) / 256.0F);
			var18 = (double)(((float)var11 + 15.99F) / 256.0F);
		}

		var20 = var14;
		double var22 = var12;
		double var24 = var16;
		double var26 = var18;
		if (this.uvRotateEast == 2) {
			var12 = ((double)var10 + par1Block.minY * 16.0D) / 256.0D;
			var16 = ((double)(var11 + 16) - par1Block.minX * 16.0D) / 256.0D;
			var14 = ((double)var10 + par1Block.maxY * 16.0D) / 256.0D;
			var18 = ((double)(var11 + 16) - par1Block.maxX * 16.0D) / 256.0D;
			var24 = var16;
			var26 = var18;
			var20 = var12;
			var22 = var14;
			var16 = var18;
			var18 = var24;
		} else if (this.uvRotateEast == 1) {
			var12 = ((double)(var10 + 16) - par1Block.maxY * 16.0D) / 256.0D;
			var16 = ((double)var11 + par1Block.maxX * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.minY * 16.0D) / 256.0D;
			var18 = ((double)var11 + par1Block.minX * 16.0D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var12 = var14;
			var14 = var22;
			var24 = var18;
			var26 = var16;
		} else if (this.uvRotateEast == 3) {
			var12 = ((double)(var10 + 16) - par1Block.minX * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.maxX * 16.0D - 0.01D) / 256.0D;
			var16 = ((double)var11 + par1Block.maxY * 16.0D) / 256.0D;
			var18 = ((double)var11 + par1Block.minY * 16.0D - 0.01D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var24 = var16;
			var26 = var18;
		}

		double var28 = par2 + par1Block.minX;
		double var30 = par2 + par1Block.maxX;
		double var32 = par4 + par1Block.minY;
		double var34 = par4 + par1Block.maxY;
		double var36 = par6 + par1Block.minZ;
		if (this.enableAO) {
			var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			var9.setBrightness(this.brightnessTopLeft);
			var9.addVertexWithUV(var28, var34, var36, var20, var24);
			var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			var9.setBrightness(this.brightnessBottomLeft);
			var9.addVertexWithUV(var30, var34, var36, var12, var16);
			var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			var9.setBrightness(this.brightnessBottomRight);
			var9.addVertexWithUV(var30, var32, var36, var22, var26);
			var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			var9.setBrightness(this.brightnessTopRight);
			var9.addVertexWithUV(var28, var32, var36, var14, var18);
		} else {
			var9.addVertexWithUV(var28, var34, var36, var20, var24);
			var9.addVertexWithUV(var30, var34, var36, var12, var16);
			var9.addVertexWithUV(var30, var32, var36, var22, var26);
			var9.addVertexWithUV(var28, var32, var36, var14, var18);
		}

	}

	public void renderWestFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator var9 = Tessellator.instance;
		if (this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int var10 = (par8 & 15) << 4;
		int var11 = par8 & 240;
		double var12 = ((double)var10 + par1Block.minX * 16.0D) / 256.0D;
		double var14 = ((double)var10 + par1Block.maxX * 16.0D - 0.01D) / 256.0D;
		double var16 = ((double)(var11 + 16) - par1Block.maxY * 16.0D) / 256.0D;
		double var18 = ((double)(var11 + 16) - par1Block.minY * 16.0D - 0.01D) / 256.0D;
		double var20;
		if (this.flipTexture) {
			var20 = var12;
			var12 = var14;
			var14 = var20;
		}

		if (par1Block.minX < 0.0D || par1Block.maxX > 1.0D) {
			var12 = (double)(((float)var10 + 0.0F) / 256.0F);
			var14 = (double)(((float)var10 + 15.99F) / 256.0F);
		}

		if (par1Block.minY < 0.0D || par1Block.maxY > 1.0D) {
			var16 = (double)(((float)var11 + 0.0F) / 256.0F);
			var18 = (double)(((float)var11 + 15.99F) / 256.0F);
		}

		var20 = var14;
		double var22 = var12;
		double var24 = var16;
		double var26 = var18;
		if (this.uvRotateWest == 1) {
			var12 = ((double)var10 + par1Block.minY * 16.0D) / 256.0D;
			var18 = ((double)(var11 + 16) - par1Block.minX * 16.0D) / 256.0D;
			var14 = ((double)var10 + par1Block.maxY * 16.0D) / 256.0D;
			var16 = ((double)(var11 + 16) - par1Block.maxX * 16.0D) / 256.0D;
			var24 = var16;
			var26 = var18;
			var20 = var12;
			var22 = var14;
			var16 = var18;
			var18 = var24;
		} else if (this.uvRotateWest == 2) {
			var12 = ((double)(var10 + 16) - par1Block.maxY * 16.0D) / 256.0D;
			var16 = ((double)var11 + par1Block.minX * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.minY * 16.0D) / 256.0D;
			var18 = ((double)var11 + par1Block.maxX * 16.0D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var12 = var14;
			var14 = var22;
			var24 = var18;
			var26 = var16;
		} else if (this.uvRotateWest == 3) {
			var12 = ((double)(var10 + 16) - par1Block.minX * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.maxX * 16.0D - 0.01D) / 256.0D;
			var16 = ((double)var11 + par1Block.maxY * 16.0D) / 256.0D;
			var18 = ((double)var11 + par1Block.minY * 16.0D - 0.01D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var24 = var16;
			var26 = var18;
		}

		double var28 = par2 + par1Block.minX;
		double var30 = par2 + par1Block.maxX;
		double var32 = par4 + par1Block.minY;
		double var34 = par4 + par1Block.maxY;
		double var36 = par6 + par1Block.maxZ;
		if (this.enableAO) {
			var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			var9.setBrightness(this.brightnessTopLeft);
			var9.addVertexWithUV(var28, var34, var36, var12, var16);
			var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			var9.setBrightness(this.brightnessBottomLeft);
			var9.addVertexWithUV(var28, var32, var36, var22, var26);
			var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			var9.setBrightness(this.brightnessBottomRight);
			var9.addVertexWithUV(var30, var32, var36, var14, var18);
			var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			var9.setBrightness(this.brightnessTopRight);
			var9.addVertexWithUV(var30, var34, var36, var20, var24);
		} else {
			var9.addVertexWithUV(var28, var34, var36, var12, var16);
			var9.addVertexWithUV(var28, var32, var36, var22, var26);
			var9.addVertexWithUV(var30, var32, var36, var14, var18);
			var9.addVertexWithUV(var30, var34, var36, var20, var24);
		}

	}

	public void renderNorthFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator var9 = Tessellator.instance;
		if (this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int var10 = (par8 & 15) << 4;
		int var11 = par8 & 240;
		double var12 = ((double)var10 + par1Block.minZ * 16.0D) / 256.0D;
		double var14 = ((double)var10 + par1Block.maxZ * 16.0D - 0.01D) / 256.0D;
		double var16 = ((double)(var11 + 16) - par1Block.maxY * 16.0D) / 256.0D;
		double var18 = ((double)(var11 + 16) - par1Block.minY * 16.0D - 0.01D) / 256.0D;
		double var20;
		if (this.flipTexture) {
			var20 = var12;
			var12 = var14;
			var14 = var20;
		}

		if (par1Block.minZ < 0.0D || par1Block.maxZ > 1.0D) {
			var12 = (double)(((float)var10 + 0.0F) / 256.0F);
			var14 = (double)(((float)var10 + 15.99F) / 256.0F);
		}

		if (par1Block.minY < 0.0D || par1Block.maxY > 1.0D) {
			var16 = (double)(((float)var11 + 0.0F) / 256.0F);
			var18 = (double)(((float)var11 + 15.99F) / 256.0F);
		}

		var20 = var14;
		double var22 = var12;
		double var24 = var16;
		double var26 = var18;
		if (this.uvRotateNorth == 1) {
			var12 = ((double)var10 + par1Block.minY * 16.0D) / 256.0D;
			var16 = ((double)(var11 + 16) - par1Block.maxZ * 16.0D) / 256.0D;
			var14 = ((double)var10 + par1Block.maxY * 16.0D) / 256.0D;
			var18 = ((double)(var11 + 16) - par1Block.minZ * 16.0D) / 256.0D;
			var24 = var16;
			var26 = var18;
			var20 = var12;
			var22 = var14;
			var16 = var18;
			var18 = var24;
		} else if (this.uvRotateNorth == 2) {
			var12 = ((double)(var10 + 16) - par1Block.maxY * 16.0D) / 256.0D;
			var16 = ((double)var11 + par1Block.minZ * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.minY * 16.0D) / 256.0D;
			var18 = ((double)var11 + par1Block.maxZ * 16.0D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var12 = var14;
			var14 = var22;
			var24 = var18;
			var26 = var16;
		} else if (this.uvRotateNorth == 3) {
			var12 = ((double)(var10 + 16) - par1Block.minZ * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.maxZ * 16.0D - 0.01D) / 256.0D;
			var16 = ((double)var11 + par1Block.maxY * 16.0D) / 256.0D;
			var18 = ((double)var11 + par1Block.minY * 16.0D - 0.01D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var24 = var16;
			var26 = var18;
		}

		double var28 = par2 + par1Block.minX;
		double var30 = par4 + par1Block.minY;
		double var32 = par4 + par1Block.maxY;
		double var34 = par6 + par1Block.minZ;
		double var36 = par6 + par1Block.maxZ;
		if (this.enableAO) {
			var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			var9.setBrightness(this.brightnessTopLeft);
			var9.addVertexWithUV(var28, var32, var36, var20, var24);
			var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			var9.setBrightness(this.brightnessBottomLeft);
			var9.addVertexWithUV(var28, var32, var34, var12, var16);
			var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			var9.setBrightness(this.brightnessBottomRight);
			var9.addVertexWithUV(var28, var30, var34, var22, var26);
			var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			var9.setBrightness(this.brightnessTopRight);
			var9.addVertexWithUV(var28, var30, var36, var14, var18);
		} else {
			var9.addVertexWithUV(var28, var32, var36, var20, var24);
			var9.addVertexWithUV(var28, var32, var34, var12, var16);
			var9.addVertexWithUV(var28, var30, var34, var22, var26);
			var9.addVertexWithUV(var28, var30, var36, var14, var18);
		}

	}

	public void renderSouthFace(Block par1Block, double par2, double par4, double par6, int par8) {
		Tessellator var9 = Tessellator.instance;
		if (this.overrideBlockTexture >= 0) {
			par8 = this.overrideBlockTexture;
		}

		int var10 = (par8 & 15) << 4;
		int var11 = par8 & 240;
		double var12 = ((double)var10 + par1Block.minZ * 16.0D) / 256.0D;
		double var14 = ((double)var10 + par1Block.maxZ * 16.0D - 0.01D) / 256.0D;
		double var16 = ((double)(var11 + 16) - par1Block.maxY * 16.0D) / 256.0D;
		double var18 = ((double)(var11 + 16) - par1Block.minY * 16.0D - 0.01D) / 256.0D;
		double var20;
		if (this.flipTexture) {
			var20 = var12;
			var12 = var14;
			var14 = var20;
		}

		if (par1Block.minZ < 0.0D || par1Block.maxZ > 1.0D) {
			var12 = (double)(((float)var10 + 0.0F) / 256.0F);
			var14 = (double)(((float)var10 + 15.99F) / 256.0F);
		}

		if (par1Block.minY < 0.0D || par1Block.maxY > 1.0D) {
			var16 = (double)(((float)var11 + 0.0F) / 256.0F);
			var18 = (double)(((float)var11 + 15.99F) / 256.0F);
		}

		var20 = var14;
		double var22 = var12;
		double var24 = var16;
		double var26 = var18;
		if (this.uvRotateSouth == 2) {
			var12 = ((double)var10 + par1Block.minY * 16.0D) / 256.0D;
			var16 = ((double)(var11 + 16) - par1Block.minZ * 16.0D) / 256.0D;
			var14 = ((double)var10 + par1Block.maxY * 16.0D) / 256.0D;
			var18 = ((double)(var11 + 16) - par1Block.maxZ * 16.0D) / 256.0D;
			var24 = var16;
			var26 = var18;
			var20 = var12;
			var22 = var14;
			var16 = var18;
			var18 = var24;
		} else if (this.uvRotateSouth == 1) {
			var12 = ((double)(var10 + 16) - par1Block.maxY * 16.0D) / 256.0D;
			var16 = ((double)var11 + par1Block.maxZ * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.minY * 16.0D) / 256.0D;
			var18 = ((double)var11 + par1Block.minZ * 16.0D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var12 = var14;
			var14 = var22;
			var24 = var18;
			var26 = var16;
		} else if (this.uvRotateSouth == 3) {
			var12 = ((double)(var10 + 16) - par1Block.minZ * 16.0D) / 256.0D;
			var14 = ((double)(var10 + 16) - par1Block.maxZ * 16.0D - 0.01D) / 256.0D;
			var16 = ((double)var11 + par1Block.maxY * 16.0D) / 256.0D;
			var18 = ((double)var11 + par1Block.minY * 16.0D - 0.01D) / 256.0D;
			var20 = var14;
			var22 = var12;
			var24 = var16;
			var26 = var18;
		}

		double var28 = par2 + par1Block.maxX;
		double var30 = par4 + par1Block.minY;
		double var32 = par4 + par1Block.maxY;
		double var34 = par6 + par1Block.minZ;
		double var36 = par6 + par1Block.maxZ;
		if (this.enableAO) {
			var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			var9.setBrightness(this.brightnessTopLeft);
			var9.addVertexWithUV(var28, var30, var36, var22, var26);
			var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			var9.setBrightness(this.brightnessBottomLeft);
			var9.addVertexWithUV(var28, var30, var34, var14, var18);
			var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			var9.setBrightness(this.brightnessBottomRight);
			var9.addVertexWithUV(var28, var32, var34, var20, var24);
			var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			var9.setBrightness(this.brightnessTopRight);
			var9.addVertexWithUV(var28, var32, var36, var12, var16);
		} else {
			var9.addVertexWithUV(var28, var30, var36, var22, var26);
			var9.addVertexWithUV(var28, var30, var34, var14, var18);
			var9.addVertexWithUV(var28, var32, var34, var20, var24);
			var9.addVertexWithUV(var28, var32, var36, var12, var16);
		}

	}

	public void renderBlockAsItem(Block par1Block, int par2, float par3) {
		Tessellator var4 = Tessellator.instance;
		boolean var5 = par1Block.blockID == Block.grass.blockID;
		int var6;
		float var7;
		float var8;
		float var9;
		if (this.useInventoryTint) {
			var6 = par1Block.getRenderColor(par2);
			if (var5) {
				var6 = 16777215;
			}

			var7 = (float)(var6 >> 16 & 255) / 255.0F;
			var8 = (float)(var6 >> 8 & 255) / 255.0F;
			var9 = (float)(var6 & 255) / 255.0F;
			GL11.glColor4f(var7 * par3, var8 * par3, var9 * par3, 1.0F);
		}

		var6 = par1Block.getRenderType();
		int var14;
		if (var6 != 0 && var6 != 16) {
			if (var6 == 1) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				this.drawCrossedSquares(par1Block, par2, -0.5D, -0.5D, -0.5D);
				var4.draw();
			} else if (var6 == 19) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				par1Block.setBlockBoundsForItemRender();
				this.renderBlockStemSmall(par1Block, par2, par1Block.maxY, -0.5D, -0.5D, -0.5D);
				var4.draw();
			} else if (var6 == 23) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				par1Block.setBlockBoundsForItemRender();
				var4.draw();
			} else if (var6 == 13) {
				par1Block.setBlockBoundsForItemRender();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				var7 = 0.0625F;
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(0.0F, 1.0F, 0.0F);
				this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(0.0F, 0.0F, -1.0F);
				var4.setTranslationF(0.0F, 0.0F, var7);
				this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
				var4.setTranslationF(0.0F, 0.0F, -var7);
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(0.0F, 0.0F, 1.0F);
				var4.setTranslationF(0.0F, 0.0F, -var7);
				this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
				var4.setTranslationF(0.0F, 0.0F, var7);
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(-1.0F, 0.0F, 0.0F);
				var4.setTranslationF(var7, 0.0F, 0.0F);
				this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
				var4.setTranslationF(-var7, 0.0F, 0.0F);
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(1.0F, 0.0F, 0.0F);
				var4.setTranslationF(-var7, 0.0F, 0.0F);
				this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
				var4.setTranslationF(var7, 0.0F, 0.0F);
				var4.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if (var6 == 22) {
				ChestItemRenderHelper.instance.func_35609_a(par1Block, par2, par3);
				GL11.glEnable('\u803a');
			} else if (var6 == 6) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBlockCropsImpl(par1Block, par2, -0.5D, -0.5D, -0.5D);
				var4.draw();
			} else if (var6 == 2) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				this.renderTorchAtAngle(par1Block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
				var4.draw();
			} else if (var6 == 10) {
				for (var14 = 0; var14 < 2; ++var14) {
					if (var14 == 0) {
						par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
					}

					if (var14 == 1) {
						par1Block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}
			} else if (var6 == 27) {
				var14 = 0;
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				var4.startDrawingQuads();

				for (int var15 = 0; var15 < 8; ++var15) {
					byte var16 = 0;
					byte var17 = 1;
					if (var15 == 0) {
						var16 = 2;
					}

					if (var15 == 1) {
						var16 = 3;
					}

					if (var15 == 2) {
						var16 = 4;
					}

					if (var15 == 3) {
						var16 = 5;
						var17 = 2;
					}

					if (var15 == 4) {
						var16 = 6;
						var17 = 3;
					}

					if (var15 == 5) {
						var16 = 7;
						var17 = 5;
					}

					if (var15 == 6) {
						var16 = 6;
						var17 = 2;
					}

					if (var15 == 7) {
						var16 = 3;
					}

					float var11 = (float)var16 / 16.0F;
					float var12 = 1.0F - (float)var14 / 16.0F;
					float var13 = 1.0F - (float)(var14 + var17) / 16.0F;
					var14 += var17;
					par1Block.setBlockBounds(0.5F - var11, var13, 0.5F - var11, 0.5F + var11, var12, 0.5F + var11);
					var4.setNormal(0.0F, -1.0F, 0.0F);
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
					var4.setNormal(0.0F, 1.0F, 0.0F);
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
					var4.setNormal(0.0F, 0.0F, -1.0F);
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
					var4.setNormal(0.0F, 0.0F, 1.0F);
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
					var4.setNormal(1.0F, 0.0F, 0.0F);
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
				}

				var4.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else if (var6 == 11) {
				for (var14 = 0; var14 < 4; ++var14) {
					var8 = 0.125F;
					if (var14 == 0) {
						par1Block.setBlockBounds(0.5F - var8, 0.0F, 0.0F, 0.5F + var8, 1.0F, var8 * 2.0F);
					}

					if (var14 == 1) {
						par1Block.setBlockBounds(0.5F - var8, 0.0F, 1.0F - var8 * 2.0F, 0.5F + var8, 1.0F, 1.0F);
					}

					var8 = 0.0625F;
					if (var14 == 2) {
						par1Block.setBlockBounds(0.5F - var8, 1.0F - var8 * 3.0F, -var8 * 2.0F, 0.5F + var8, 1.0F - var8, 1.0F + var8 * 2.0F);
					}

					if (var14 == 3) {
						par1Block.setBlockBounds(0.5F - var8, 0.5F - var8 * 3.0F, -var8 * 2.0F, 0.5F + var8, 0.5F - var8, 1.0F + var8 * 2.0F);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else if (var6 == 21) {
				for (var14 = 0; var14 < 3; ++var14) {
					var8 = 0.0625F;
					if (var14 == 0) {
						par1Block.setBlockBounds(0.5F - var8, 0.3F, 0.0F, 0.5F + var8, 1.0F, var8 * 2.0F);
					}

					if (var14 == 1) {
						par1Block.setBlockBounds(0.5F - var8, 0.3F, 1.0F - var8 * 2.0F, 0.5F + var8, 1.0F, 1.0F);
					}

					var8 = 0.0625F;
					if (var14 == 2) {
						par1Block.setBlockBounds(0.5F - var8, 0.5F, 0.0F, 0.5F + var8, 1.0F - var8, 1.0F);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			}
		} else {
			if (var6 == 16) {
				par2 = 1;
			}

			par1Block.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			var4.startDrawingQuads();
			var4.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(0, par2));
			var4.draw();
			if (var5 && this.useInventoryTint) {
				var14 = par1Block.getRenderColor(par2);
				var8 = (float)(var14 >> 16 & 255) / 255.0F;
				var9 = (float)(var14 >> 8 & 255) / 255.0F;
				float var10 = (float)(var14 & 255) / 255.0F;
				GL11.glColor4f(var8 * par3, var9 * par3, var10 * par3, 1.0F);
			}

			var4.startDrawingQuads();
			var4.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(1, par2));
			var4.draw();
			if (var5 && this.useInventoryTint) {
				GL11.glColor4f(par3, par3, par3, 1.0F);
			}

			var4.startDrawingQuads();
			var4.setNormal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(2, par2));
			var4.draw();
			var4.startDrawingQuads();
			var4.setNormal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(3, par2));
			var4.draw();
			var4.startDrawingQuads();
			var4.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(4, par2));
			var4.draw();
			var4.startDrawingQuads();
			var4.setNormal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(5, par2));
			var4.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}

	}

	public static boolean renderItemIn3d(int par0) {
		return par0 == 0?true:(par0 == 13?true:(par0 == 10?true:(par0 == 11?true:(par0 == 27?true:(par0 == 22?true:(par0 == 21?true:par0 == 16))))));
	}
	//Spout start
	public static int getSideGrassTexture(IBlockAccess world, int x, int y, int z, int blockface) {
		if (ConfigReader.betterGrass == 0) {
			return 3;
		} else {
			if(ConfigReader.betterGrass == 2) {
				--y;
				switch(blockface) {
				case 2:
					--z;
					break;
				case 3:
					++z;
					break;
				case 4:
					--x;
					break;
				case 5:
					++x;
				}

				int id = world.getBlockId(x, y, z);
				if(id != 2) {
					return 3;
				}
			}

			return 0;
		}
	}

	public static int getSideSnowGrassTexture(IBlockAccess world, int x, int y, int z, int blockface) {
		if (ConfigReader.betterGrass == 0) {
			return 68;
		} else {
			if (ConfigReader.betterGrass == 2) {
				switch(blockface) {
				case 2:
					--z;
					break;
				case 3:
					++z;
					break;
				case 4:
					--x;
					break;
				case 5:
					++x;
				}

				int id = world.getBlockId(x, y, z);
				if(id != 78 && id != 80) {
					return 68;
				}
			}

			return 66;
		}
	}
	
	public static float adjustAmbientOcclusion(float lightValue, float lightPosition) {
		if(Minecraft.theMinecraft.theWorld.worldProvider.lightBrightnessTable == null) {
			return lightValue;
		}
		if(lightValue > Minecraft.theMinecraft.theWorld.worldProvider.lightBrightnessTable[0]) {
			return lightValue;
		}
		if (lightPosition <= Minecraft.theMinecraft.theWorld.worldProvider.lightBrightnessTable[1]) {
			return lightValue;
		}
		return lightValue + (lightPosition - lightValue) * (1.0F - org.spoutcraft.client.config.ConfigReader.brightnessSlider);
	}
	
	private boolean dirtyAmbientOcclusionCache = true;
	private void calculateAmbientOcclusionLightValues(Block block, int x, int y, int z) {
		this.aoLightValueXNeg = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y, z);
		this.aoLightValueYNeg = block.getAmbientOcclusionLightValue(this.blockAccess, x, y - 1, z);
		this.aoLightValueZNeg = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z - 1);
		this.aoLightValueXPos = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y, z);
		this.aoLightValueYPos = block.getAmbientOcclusionLightValue(this.blockAccess, x, y + 1, z);
		this.aoLightValueZPos = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z + 1);
		this.aoGrassXYZPPC = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y + 1, z)];
		this.aoGrassXYZPNC = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y - 1, z)];
		this.aoGrassXYZPCP = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y, z + 1)];
		this.aoGrassXYZPCN = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y, z - 1)];
		this.aoGrassXYZNPC = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y + 1, z)];
		this.aoGrassXYZNNC = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y - 1, z)];
		this.aoGrassXYZNCN = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y, z - 1)];
		this.aoGrassXYZNCP = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y, z + 1)];
		this.aoGrassXYZCPP = Block.canBlockGrass[this.blockAccess.getBlockId(x, y + 1, z + 1)];
		this.aoGrassXYZCPN = Block.canBlockGrass[this.blockAccess.getBlockId(x, y + 1, z - 1)];
		this.aoGrassXYZCNP = Block.canBlockGrass[this.blockAccess.getBlockId(x, y - 1, z + 1)];
		this.aoGrassXYZCNN = Block.canBlockGrass[this.blockAccess.getBlockId(x, y - 1, z - 1)];
		this.dirtyAmbientOcclusionCache = false;
	}
	//Spout end
}
