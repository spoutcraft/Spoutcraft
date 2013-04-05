package net.minecraft.src;

import com.prupe.mcpatcher.mod.CTMUtils;
import com.prupe.mcpatcher.mod.ColorizeBlock;
import com.prupe.mcpatcher.mod.Colorizer;
import com.prupe.mcpatcher.mod.GlassPaneRenderer;
import com.prupe.mcpatcher.mod.RenderPass;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
//Spout Start
import org.spoutcraft.client.config.Configuration;
//Spout End

public class RenderBlocks {

	/** The IBlockAccess used by this instance of RenderBlocks */
	public IBlockAccess blockAccess;

	/**
	 * If set to >=0, all block faces will be rendered using this texture index
	 */
	private Icon overrideBlockTexture = null;

	/**
	 * Set to true if the texture should be flipped horizontally during render*Face
	 */
	private boolean flipTexture = false;

	/**
	 * If true, renders all faces on all blocks rather than using the logic in Block.shouldSideBeRendered.  Unused.
	 */
	private boolean renderAllFaces = false;

	/** Fancy grass side matching biome */
	public static boolean fancyGrass = true;
	public boolean useInventoryTint = true;

	/** The minimum X value for rendering (default 0.0). */
	private double renderMinX;

	/** The maximum X value for rendering (default 1.0). */
	private double renderMaxX;

	/** The minimum Y value for rendering (default 0.0). */
	private double renderMinY;

	/** The maximum Y value for rendering (default 1.0). */
	private double renderMaxY;

	/** The minimum Z value for rendering (default 0.0). */
	private double renderMinZ;

	/** The maximum Z value for rendering (default 1.0). */
	private double renderMaxZ;

	/**
	 * Set by overrideBlockBounds, to keep this class from changing the visual bounding box.
	 */
	private boolean lockBlockBounds = false;
	private boolean partialRenderBounds = false;
	private final Minecraft minecraftRB;
	private int uvRotateEast = 0;
	private int uvRotateWest = 0;
	private int uvRotateSouth = 0;
	private int uvRotateNorth = 0;
	private int uvRotateTop = 0;
	private int uvRotateBottom = 0;

	/** Whether ambient occlusion is enabled or not */
	private boolean enableAO;

	/**
	 * Used as a scratch variable for ambient occlusion on the north/bottom/east corner.
	 */
	private float aoLightValueScratchXYZNNN;

	/**
	 * Used as a scratch variable for ambient occlusion between the bottom face and the north face.
	 */
	private float aoLightValueScratchXYNN;

	/**
	 * Used as a scratch variable for ambient occlusion on the north/bottom/west corner.
	 */
	private float aoLightValueScratchXYZNNP;

	/**
	 * Used as a scratch variable for ambient occlusion between the bottom face and the east face.
	 */
	private float aoLightValueScratchYZNN;

	/**
	 * Used as a scratch variable for ambient occlusion between the bottom face and the west face.
	 */
	private float aoLightValueScratchYZNP;

	/**
	 * Used as a scratch variable for ambient occlusion on the south/bottom/east corner.
	 */
	private float aoLightValueScratchXYZPNN;

	/**
	 * Used as a scratch variable for ambient occlusion between the bottom face and the south face.
	 */
	private float aoLightValueScratchXYPN;

	/**
	 * Used as a scratch variable for ambient occlusion on the south/bottom/west corner.
	 */
	private float aoLightValueScratchXYZPNP;

	/**
	 * Used as a scratch variable for ambient occlusion on the north/top/east corner.
	 */
	private float aoLightValueScratchXYZNPN;

	/**
	 * Used as a scratch variable for ambient occlusion between the top face and the north face.
	 */
	private float aoLightValueScratchXYNP;

	/**
	 * Used as a scratch variable for ambient occlusion on the north/top/west corner.
	 */
	private float aoLightValueScratchXYZNPP;

	/**
	 * Used as a scratch variable for ambient occlusion between the top face and the east face.
	 */
	private float aoLightValueScratchYZPN;

	/**
	 * Used as a scratch variable for ambient occlusion on the south/top/east corner.
	 */
	private float aoLightValueScratchXYZPPN;

	/**
	 * Used as a scratch variable for ambient occlusion between the top face and the south face.
	 */
	private float aoLightValueScratchXYPP;

	/**
	 * Used as a scratch variable for ambient occlusion between the top face and the west face.
	 */
	private float aoLightValueScratchYZPP;

	/**
	 * Used as a scratch variable for ambient occlusion on the south/top/west corner.
	 */
	private float aoLightValueScratchXYZPPP;

	/**
	 * Used as a scratch variable for ambient occlusion between the north face and the east face.
	 */
	private float aoLightValueScratchXZNN;

	/**
	 * Used as a scratch variable for ambient occlusion between the south face and the east face.
	 */
	private float aoLightValueScratchXZPN;

	/**
	 * Used as a scratch variable for ambient occlusion between the north face and the west face.
	 */
	private float aoLightValueScratchXZNP;

	/**
	 * Used as a scratch variable for ambient occlusion between the south face and the west face.
	 */
	private float aoLightValueScratchXZPP;

	/** Ambient occlusion brightness XYZNNN */
	private int aoBrightnessXYZNNN;

	/** Ambient occlusion brightness XYNN */
	private int aoBrightnessXYNN;

	/** Ambient occlusion brightness XYZNNP */
	private int aoBrightnessXYZNNP;

	/** Ambient occlusion brightness YZNN */
	private int aoBrightnessYZNN;

	/** Ambient occlusion brightness YZNP */
	private int aoBrightnessYZNP;

	/** Ambient occlusion brightness XYZPNN */
	private int aoBrightnessXYZPNN;

	/** Ambient occlusion brightness XYPN */
	private int aoBrightnessXYPN;

	/** Ambient occlusion brightness XYZPNP */
	private int aoBrightnessXYZPNP;

	/** Ambient occlusion brightness XYZNPN */
	private int aoBrightnessXYZNPN;

	/** Ambient occlusion brightness XYNP */
	private int aoBrightnessXYNP;

	/** Ambient occlusion brightness XYZNPP */
	private int aoBrightnessXYZNPP;

	/** Ambient occlusion brightness YZPN */
	private int aoBrightnessYZPN;

	/** Ambient occlusion brightness XYZPPN */
	private int aoBrightnessXYZPPN;

	/** Ambient occlusion brightness XYPP */
	private int aoBrightnessXYPP;

	/** Ambient occlusion brightness YZPP */
	private int aoBrightnessYZPP;

	/** Ambient occlusion brightness XYZPPP */
	private int aoBrightnessXYZPPP;

	/** Ambient occlusion brightness XZNN */
	private int aoBrightnessXZNN;

	/** Ambient occlusion brightness XZPN */
	private int aoBrightnessXZPN;

	/** Ambient occlusion brightness XZNP */
	private int aoBrightnessXZNP;

	/** Ambient occlusion brightness XZPP */
	private int aoBrightnessXZPP;

	/** Brightness top left */
	private int brightnessTopLeft;

	/** Brightness bottom left */
	private int brightnessBottomLeft;

	/** Brightness bottom right */
	private int brightnessBottomRight;

	/** Brightness top right */
	private int brightnessTopRight;

	/** Red color value for the top left corner */
	private float colorRedTopLeft;

	/** Red color value for the bottom left corner */
	private float colorRedBottomLeft;

	/** Red color value for the bottom right corner */
	private float colorRedBottomRight;

	/** Red color value for the top right corner */
	private float colorRedTopRight;

	/** Green color value for the top left corner */
	private float colorGreenTopLeft;

	/** Green color value for the bottom left corner */
	private float colorGreenBottomLeft;

	/** Green color value for the bottom right corner */
	private float colorGreenBottomRight;

	/** Green color value for the top right corner */
	private float colorGreenTopRight;

	/** Blue color value for the top left corner */
	private float colorBlueTopLeft;

	/** Blue color value for the bottom left corner */
	private float colorBlueBottomLeft;

	/** Blue color value for the bottom right corner */
	private float colorBlueBottomRight;

	/** Blue color value for the top right corner */
	private float colorBlueTopRight;

	// Spout Start
	public short[] customIds = null;
	// Spout End

	public RenderBlocks(IBlockAccess par1IBlockAccess) {
		this.blockAccess = par1IBlockAccess;
		this.minecraftRB = Minecraft.getMinecraft();
	}

	public RenderBlocks() {
		this.minecraftRB = Minecraft.getMinecraft();
	}

	/**
	 * Sets overrideBlockTexture
	 */
	public void setOverrideBlockTexture(Icon par1Icon) {
		this.overrideBlockTexture = par1Icon;
	}

	/**
	 * Clear override block texture
	 */
	public void clearOverrideBlockTexture() {
		this.overrideBlockTexture = null;
	}

	public boolean hasOverrideBlockTexture() {
		return this.overrideBlockTexture != null;
	}

	/**
	 * Sets the bounding box for the block to draw in, e.g. 0.25-0.75 on all axes for a half-size, centered block.
	 */
	public void setRenderBounds(double par1, double par3, double par5, double par7, double par9, double par11) {
		if (!this.lockBlockBounds) {
			this.renderMinX = par1;
			this.renderMaxX = par7;
			this.renderMinY = par3;
			this.renderMaxY = par9;
			this.renderMinZ = par5;
			this.renderMaxZ = par11;
			this.partialRenderBounds = this.minecraftRB.gameSettings.ambientOcclusion >= 2 && (this.renderMinX > 0.0D || this.renderMaxX < 1.0D || this.renderMinY > 0.0D || this.renderMaxY < 1.0D || this.renderMinZ > 0.0D || this.renderMaxZ < 1.0D);
		}
	}

	/**
	 * Like setRenderBounds, but automatically pulling the bounds from the given Block.
	 */
	public void setRenderBoundsFromBlock(Block par1Block) {
		if (!this.lockBlockBounds) {
			this.renderMinX = par1Block.getBlockBoundsMinX();
			this.renderMaxX = par1Block.getBlockBoundsMaxX();
			this.renderMinY = par1Block.getBlockBoundsMinY();
			this.renderMaxY = par1Block.getBlockBoundsMaxY();
			this.renderMinZ = par1Block.getBlockBoundsMinZ();
			this.renderMaxZ = par1Block.getBlockBoundsMaxZ();
			this.partialRenderBounds = this.minecraftRB.gameSettings.ambientOcclusion >= 2 && (this.renderMinX > 0.0D || this.renderMaxX < 1.0D || this.renderMinY > 0.0D || this.renderMaxY < 1.0D || this.renderMinZ > 0.0D || this.renderMaxZ < 1.0D);
		}
	}

	/**
	 * Like setRenderBounds, but locks the values so that RenderBlocks won't change them.  If you use this, you must call
	 * unlockBlockBounds after you finish rendering!
	 */
	public void overrideBlockBounds(double par1, double par3, double par5, double par7, double par9, double par11) {
		this.renderMinX = par1;
		this.renderMaxX = par7;
		this.renderMinY = par3;
		this.renderMaxY = par9;
		this.renderMinZ = par5;
		this.renderMaxZ = par11;
		this.lockBlockBounds = true;
		this.partialRenderBounds = this.minecraftRB.gameSettings.ambientOcclusion >= 2 && (this.renderMinX > 0.0D || this.renderMaxX < 1.0D || this.renderMinY > 0.0D || this.renderMaxY < 1.0D || this.renderMinZ > 0.0D || this.renderMaxZ < 1.0D);
	}

	/**
	 * Unlocks the visual bounding box so that RenderBlocks can change it again.
	 */
	public void unlockBlockBounds() {
		this.lockBlockBounds = false;
	}

	/**
	 * Renders a block using the given texture instead of the block's own default texture
	 */
	public void renderBlockUsingTexture(Block par1Block, int par2, int par3, int par4, Icon par5Icon) {
		this.setOverrideBlockTexture(par5Icon);
		this.renderBlockByRenderType(par1Block, par2, par3, par4);
		this.clearOverrideBlockTexture();
	}

	/**
	 * Render all faces of a block
	 */
	public void renderBlockAllFaces(Block par1Block, int par2, int par3, int par4) {
		this.renderAllFaces = true;
		this.renderBlockByRenderType(par1Block, par2, par3, par4);
		this.renderAllFaces = false;
	}

	/**
	 * Renders the block at the given coordinates using the block's rendering type
	 */
	public boolean renderBlockByRenderType(Block par1Block, int par2, int par3, int par4) {
		int var5 = par1Block.getRenderType();

		if (var5 == -1) {
			return false;
		} else {
			par1Block.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
			this.setRenderBoundsFromBlock(par1Block);
			return var5 == 0 ? this.renderStandardBlock(par1Block, par2, par3, par4) : (var5 == 4 ? this.renderBlockFluids(par1Block, par2, par3, par4) : (var5 == 31 ? this.renderBlockLog(par1Block, par2, par3, par4) : (var5 == 1 ? this.renderCrossedSquares(par1Block, par2, par3, par4) : (var5 == 2 ? this.renderBlockTorch(par1Block, par2, par3, par4) : (var5 == 20 ? this.renderBlockVine(par1Block, par2, par3, par4) : (var5 == 11 ? this.renderBlockFence((BlockFence)par1Block, par2, par3, par4) : (var5 == 39 ? this.renderBlockQuartz(par1Block, par2, par3, par4) : (var5 == 5 ? this.renderBlockRedstoneWire(par1Block, par2, par3, par4) : (var5 == 13 ? this.renderBlockCactus(par1Block, par2, par3, par4) : (var5 == 9 ? this.renderBlockMinecartTrack((BlockRailBase)par1Block, par2, par3, par4) : (var5 == 19 ? this.renderBlockStem(par1Block, par2, par3, par4) : (var5 == 23 ? this.renderBlockLilyPad(par1Block, par2, par3, par4) : (var5 == 6 ? this.renderBlockCrops(par1Block, par2, par3, par4) : (var5 == 3 ? this.renderBlockFire((BlockFire)par1Block, par2, par3, par4) : (var5 == 8 ? this.renderBlockLadder(par1Block, par2, par3, par4) : (var5 == 7 ? this.renderBlockDoor(par1Block, par2, par3, par4) : (var5 == 10 ? this.renderBlockStairs((BlockStairs)par1Block, par2, par3, par4) : (var5 == 27 ? this.renderBlockDragonEgg((BlockDragonEgg)par1Block, par2, par3, par4) : (var5 == 32 ? this.renderBlockWall((BlockWall)par1Block, par2, par3, par4) : (var5 == 12 ? this.renderBlockLever(par1Block, par2, par3, par4) : (var5 == 29 ? this.renderBlockTripWireSource(par1Block, par2, par3, par4) : (var5 == 30 ? this.renderBlockTripWire(par1Block, par2, par3, par4) : (var5 == 14 ? this.renderBlockBed(par1Block, par2, par3, par4) : (var5 == 15 ? this.renderBlockRepeater((BlockRedstoneRepeater)par1Block, par2, par3, par4) : (var5 == 36 ? this.renderBlockRedstoneLogic((BlockRedstoneLogic)par1Block, par2, par3, par4) : (var5 == 37 ? this.renderBlockComparator((BlockComparator)par1Block, par2, par3, par4) : (var5 == 16 ? this.renderPistonBase(par1Block, par2, par3, par4, false) : (var5 == 17 ? this.renderPistonExtension(par1Block, par2, par3, par4, true) : (var5 == 18 ? this.renderBlockPane((BlockPane)par1Block, par2, par3, par4) : (var5 == 21 ? this.renderBlockFenceGate((BlockFenceGate)par1Block, par2, par3, par4) : (var5 == 24 ? this.renderBlockCauldron((BlockCauldron)par1Block, par2, par3, par4) : (var5 == 33 ? this.renderBlockFlowerpot((BlockFlowerPot)par1Block, par2, par3, par4) : (var5 == 35 ? this.renderBlockAnvil((BlockAnvil)par1Block, par2, par3, par4) : (var5 == 25 ? this.renderBlockBrewingStand((BlockBrewingStand)par1Block, par2, par3, par4) : (var5 == 26 ? this.renderBlockEndPortalFrame((BlockEndPortalFrame)par1Block, par2, par3, par4) : (var5 == 28 ? this.renderBlockCocoa((BlockCocoa)par1Block, par2, par3, par4) : (var5 == 34 ? this.renderBlockBeacon((BlockBeacon)par1Block, par2, par3, par4) : (var5 == 38 ? this.renderBlockHopper((BlockHopper)par1Block, par2, par3, par4) : false))))))))))))))))))))))))))))))))))))));
		}
	}

	/**
	 * Render BlockEndPortalFrame
	 */
	private boolean renderBlockEndPortalFrame(BlockEndPortalFrame par1BlockEndPortalFrame, int par2, int par3, int par4) {
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
			this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
			this.renderStandardBlock(par1BlockEndPortalFrame, par2, par3, par4);
			this.uvRotateTop = 0;
			return true;
		} else {
			this.renderAllFaces = true;
			this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
			this.renderStandardBlock(par1BlockEndPortalFrame, par2, par3, par4);
			this.setOverrideBlockTexture(par1BlockEndPortalFrame.func_94398_p());
			this.setRenderBounds(0.25D, 0.8125D, 0.25D, 0.75D, 1.0D, 0.75D);
			this.renderStandardBlock(par1BlockEndPortalFrame, par2, par3, par4);
			this.renderAllFaces = false;
			this.clearOverrideBlockTexture();
			this.uvRotateTop = 0;
			return true;
		}
	}

	/**
	 * render a bed at the given coordinates
	 */
	private boolean renderBlockBed(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var7 = BlockBed.getDirection(var6);
		boolean var8 = BlockBed.isBlockHeadOfBed(var6);
		float var9 = 0.5F;
		float var10 = 1.0F;
		float var11 = 0.8F;
		float var12 = 0.6F;
		int var25 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		var5.setBrightness(var25);
		var5.setColorOpaque_F(var9, var9, var9);
		Icon var27 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0);
		var27 = CTMUtils.getTile(this, par1Block, par2, par3, par4, var27, var5);

		if (var27 == null) {
			return false;
		} else {
			var5 = CTMUtils.getTessellator(var27);
			double var28 = (double)var27.getMinU();
			double var30 = (double)var27.getMaxU();
			double var32 = (double)var27.getMinV();
			double var34 = (double)var27.getMaxV();
			double var36 = (double)par2 + this.renderMinX;
			double var38 = (double)par2 + this.renderMaxX;
			double var40 = (double)par3 + this.renderMinY + 0.1875D;
			double var42 = (double)par4 + this.renderMinZ;
			double var44 = (double)par4 + this.renderMaxZ;
			var5.addVertexWithUV(var36, var40, var44, var28, var34);
			var5.addVertexWithUV(var36, var40, var42, var28, var32);
			var5.addVertexWithUV(var38, var40, var42, var30, var32);
			var5.addVertexWithUV(var38, var40, var44, var30, var34);
			var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
			var5.setColorOpaque_F(var10, var10, var10);
			var27 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1);
			var27 = CTMUtils.getTile(this, par1Block, par2, par3, par4, var27, var5);

			if (var27 == null) {
				return false;
			} else {
				var5 = CTMUtils.getTessellator(var27);
				var28 = (double)var27.getMinU();
				var30 = (double)var27.getMaxU();
				var32 = (double)var27.getMinV();
				var34 = (double)var27.getMaxV();
				var36 = var28;
				var38 = var30;
				var40 = var32;
				var42 = var32;
				var44 = var28;
				double var46 = var30;
				double var48 = var34;
				double var50 = var34;

				if (var7 == 0) {
					var38 = var28;
					var40 = var34;
					var44 = var30;
					var50 = var32;
				} else if (var7 == 2) {
					var36 = var30;
					var42 = var34;
					var46 = var28;
					var48 = var32;
				} else if (var7 == 3) {
					var36 = var30;
					var42 = var34;
					var46 = var28;
					var48 = var32;
					var38 = var28;
					var40 = var34;
					var44 = var30;
					var50 = var32;
				}

				double var52 = (double)par2 + this.renderMinX;
				double var54 = (double)par2 + this.renderMaxX;
				double var56 = (double)par3 + this.renderMaxY;
				double var58 = (double)par4 + this.renderMinZ;
				double var60 = (double)par4 + this.renderMaxZ;
				var5.addVertexWithUV(var54, var56, var60, var44, var48);
				var5.addVertexWithUV(var54, var56, var58, var36, var40);
				var5.addVertexWithUV(var52, var56, var58, var38, var42);
				var5.addVertexWithUV(var52, var56, var60, var46, var50);
				int var62 = Direction.headInvisibleFace[var7];

				if (var8) {
					var62 = Direction.headInvisibleFace[Direction.footInvisibleFaceRemap[var7]];
				}

				byte var63 = 4;

				switch (var7) {
					case 0:
						var63 = 5;
						break;

					case 1:
						var63 = 3;

					case 2:
					default:
						break;

					case 3:
						var63 = 2;
				}

				if (var62 != 2 && (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 - 1, 2))) {
					var5.setBrightness(this.renderMinZ > 0.0D ? var25 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
					var5.setColorOpaque_F(var11, var11, var11);
					this.flipTexture = var63 == 2;
					this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2));
				}

				if (var62 != 3 && (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 + 1, 3))) {
					var5.setBrightness(this.renderMaxZ < 1.0D ? var25 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
					var5.setColorOpaque_F(var11, var11, var11);
					this.flipTexture = var63 == 3;
					this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));
				}

				if (var62 != 4 && (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 - 1, par3, par4, 4))) {
					var5.setBrightness(this.renderMinZ > 0.0D ? var25 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
					var5.setColorOpaque_F(var12, var12, var12);
					this.flipTexture = var63 == 4;
					this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4));
				}

				if (var62 != 5 && (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 + 1, par3, par4, 5))) {
					var5.setBrightness(this.renderMaxZ < 1.0D ? var25 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
					var5.setColorOpaque_F(var12, var12, var12);
					this.flipTexture = var63 == 5;
					this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5));
				}

				this.flipTexture = false;
				return true;
			}
		}
	}

	/**
	 * Render BlockBrewingStand
	 */
	private boolean renderBlockBrewingStand(BlockBrewingStand par1BlockBrewingStand, int par2, int par3, int par4) {
		this.setRenderBounds(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.875D, 0.5625D);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		this.setOverrideBlockTexture(par1BlockBrewingStand.func_94448_e());
		this.setRenderBounds(0.5625D, 0.0D, 0.3125D, 0.9375D, 0.125D, 0.6875D);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		this.setRenderBounds(0.125D, 0.0D, 0.0625D, 0.5D, 0.125D, 0.4375D);
		this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
		this.setRenderBounds(0.125D, 0.0D, 0.5625D, 0.5D, 0.125D, 0.9375D);
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
		Icon var32 = this.getBlockIconFromSideAndMetadata(par1BlockBrewingStand, 0, 0);

		if (this.hasOverrideBlockTexture()) {
			var32 = this.overrideBlockTexture;
		}

		var32 = CTMUtils.getTile(this, par1BlockBrewingStand, par2, par3, par4, var32, var5);

		if (var32 == null) {
			return false;
		} else {
			var5 = CTMUtils.getTessellator(var32);
			double var33 = (double)var32.getMinV();
			double var14 = (double)var32.getMaxV();
			int var16 = this.blockAccess.getBlockMetadata(par2, par3, par4);

			for (int var17 = 0; var17 < 3; ++var17) {
				double var18 = (double)var17 * Math.PI * 2.0D / 3.0D + (Math.PI / 2D);
				double var20 = (double)var32.getInterpolatedU(8.0D);
				double var22 = (double)var32.getMaxU();

				if ((var16 & 1 << var17) != 0) {
					var22 = (double)var32.getMinU();
				}

				double var24 = (double)par2 + 0.5D;
				double var26 = (double)par2 + 0.5D + Math.sin(var18) * 8.0D / 16.0D;
				double var28 = (double)par4 + 0.5D;
				double var30 = (double)par4 + 0.5D + Math.cos(var18) * 8.0D / 16.0D;
				var5.addVertexWithUV(var24, (double)(par3 + 1), var28, var20, var33);
				var5.addVertexWithUV(var24, (double)(par3 + 0), var28, var20, var14);
				var5.addVertexWithUV(var26, (double)(par3 + 0), var30, var22, var14);
				var5.addVertexWithUV(var26, (double)(par3 + 1), var30, var22, var33);
				var5.addVertexWithUV(var26, (double)(par3 + 1), var30, var22, var33);
				var5.addVertexWithUV(var26, (double)(par3 + 0), var30, var22, var14);
				var5.addVertexWithUV(var24, (double)(par3 + 0), var28, var20, var14);
				var5.addVertexWithUV(var24, (double)(par3 + 1), var28, var20, var33);
			}

			par1BlockBrewingStand.setBlockBoundsForItemRender();
			return true;
		}
	}

	/**
	 * Render block cauldron
	 */
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
		Icon var16 = par1BlockCauldron.getBlockTextureFromSide(2);
		var12 = 0.125F;
		this.renderSouthFace(par1BlockCauldron, (double)((float)par2 - 1.0F + var12), (double)par3, (double)par4, var16);
		this.renderNorthFace(par1BlockCauldron, (double)((float)par2 + 1.0F - var12), (double)par3, (double)par4, var16);
		this.renderWestFace(par1BlockCauldron, (double)par2, (double)par3, (double)((float)par4 - 1.0F + var12), var16);
		this.renderEastFace(par1BlockCauldron, (double)par2, (double)par3, (double)((float)par4 + 1.0F - var12), var16);
		Icon var17 = BlockCauldron.func_94375_b("cauldron_inner");
		this.renderTopFace(par1BlockCauldron, (double)par2, (double)((float)par3 - 1.0F + 0.25F), (double)par4, var17);
		this.renderBottomFace(par1BlockCauldron, (double)par2, (double)((float)par3 + 1.0F - 0.75F), (double)par4, var17);
		int var14 = this.blockAccess.getBlockMetadata(par2, par3, par4);

		if (var14 > 0) {
			Icon var15 = BlockFluid.func_94424_b("water");
			ColorizeBlock.computeWaterColor();
			var5.setColorOpaque_F(ColorizeBlock.waterColor[0], ColorizeBlock.waterColor[1], ColorizeBlock.waterColor[2]);

			if (var14 > 3) {
				var14 = 3;
			}

			this.renderTopFace(par1BlockCauldron, (double)par2, (double)((float)par3 - 1.0F + (6.0F + (float)var14 * 3.0F) / 16.0F), (double)par4, var15);
		}

		return true;
	}

	/**
	 * Renders flower pot
	 */
	private boolean renderBlockFlowerpot(BlockFlowerPot par1BlockFlowerPot, int par2, int par3, int par4) {
		this.renderStandardBlock(par1BlockFlowerPot, par2, par3, par4);
		Tessellator var5 = Tessellator.instance;
		var5.setBrightness(par1BlockFlowerPot.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var6 = 1.0F;
		int var7 = par1BlockFlowerPot.colorMultiplier(this.blockAccess, par2, par3, par4);
		Icon var8 = this.getBlockIconFromSide(par1BlockFlowerPot, 0);
		float var9 = (float)(var7 >> 16 & 255) / 255.0F;
		float var10 = (float)(var7 >> 8 & 255) / 255.0F;
		float var11 = (float)(var7 & 255) / 255.0F;
		float var12;
		float var14;

		if (EntityRenderer.anaglyphEnable) {
			var12 = (var9 * 30.0F + var10 * 59.0F + var11 * 11.0F) / 100.0F;
			float var13 = (var9 * 30.0F + var10 * 70.0F) / 100.0F;
			var14 = (var9 * 30.0F + var11 * 70.0F) / 100.0F;
			var9 = var12;
			var10 = var13;
			var11 = var14;
		}

		var5.setColorOpaque_F(var6 * var9, var6 * var10, var6 * var11);
		var12 = 0.1865F;
		this.renderSouthFace(par1BlockFlowerPot, (double)((float)par2 - 0.5F + var12), (double)par3, (double)par4, var8);
		this.renderNorthFace(par1BlockFlowerPot, (double)((float)par2 + 0.5F - var12), (double)par3, (double)par4, var8);
		this.renderWestFace(par1BlockFlowerPot, (double)par2, (double)par3, (double)((float)par4 - 0.5F + var12), var8);
		this.renderEastFace(par1BlockFlowerPot, (double)par2, (double)par3, (double)((float)par4 + 0.5F - var12), var8);
		this.renderTopFace(par1BlockFlowerPot, (double)par2, (double)((float)par3 - 0.5F + var12 + 0.1875F), (double)par4, this.getBlockIcon(Block.dirt));
		int var19 = this.blockAccess.getBlockMetadata(par2, par3, par4);

		if (var19 != 0) {
			var14 = 0.0F;
			float var15 = 4.0F;
			float var16 = 0.0F;
			BlockFlower var17 = null;

			switch (var19) {
				case 1:
					var17 = Block.plantRed;
					break;

				case 2:
					var17 = Block.plantYellow;

				case 3:
				case 4:
				case 5:
				case 6:
				default:
					break;

				case 7:
					var17 = Block.mushroomRed;
					break;

				case 8:
					var17 = Block.mushroomBrown;
			}

			var5.addTranslation(var14 / 16.0F, var15 / 16.0F, var16 / 16.0F);

			if (var17 != null) {
				this.renderBlockByRenderType(var17, par2, par3, par4);
			} else if (var19 == 9) {
				this.renderAllFaces = true;
				float var18 = 0.125F;
				this.setRenderBounds((double)(0.5F - var18), 0.0D, (double)(0.5F - var18), (double)(0.5F + var18), 0.25D, (double)(0.5F + var18));
				this.renderStandardBlock(Block.cactus, par2, par3, par4);
				this.setRenderBounds((double)(0.5F - var18), 0.25D, (double)(0.5F - var18), (double)(0.5F + var18), 0.5D, (double)(0.5F + var18));
				this.renderStandardBlock(Block.cactus, par2, par3, par4);
				this.setRenderBounds((double)(0.5F - var18), 0.5D, (double)(0.5F - var18), (double)(0.5F + var18), 0.75D, (double)(0.5F + var18));
				this.renderStandardBlock(Block.cactus, par2, par3, par4);
				this.renderAllFaces = false;
				this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			} else if (var19 == 3) {
				this.drawCrossedSquares(Block.sapling, 0, (double)par2, (double)par3, (double)par4, 0.75F);
			} else if (var19 == 5) {
				this.drawCrossedSquares(Block.sapling, 2, (double)par2, (double)par3, (double)par4, 0.75F);
			} else if (var19 == 4) {
				this.drawCrossedSquares(Block.sapling, 1, (double)par2, (double)par3, (double)par4, 0.75F);
			} else if (var19 == 6) {
				this.drawCrossedSquares(Block.sapling, 3, (double)par2, (double)par3, (double)par4, 0.75F);
			} else if (var19 == 11) {
				var7 = Block.tallGrass.colorMultiplier(this.blockAccess, par2, par3, par4);
				var9 = (float)(var7 >> 16 & 255) / 255.0F;
				var10 = (float)(var7 >> 8 & 255) / 255.0F;
				var11 = (float)(var7 & 255) / 255.0F;
				var5.setColorOpaque_F(var6 * var9, var6 * var10, var6 * var11);
				this.drawCrossedSquares(Block.tallGrass, 2, (double)par2, (double)par3, (double)par4, 0.75F);
			} else if (var19 == 10) {
				this.drawCrossedSquares(Block.deadBush, 2, (double)par2, (double)par3, (double)par4, 0.75F);
			}

			var5.addTranslation(-var14 / 16.0F, -var15 / 16.0F, -var16 / 16.0F);
		}

		return true;
	}

	/**
	 * Renders anvil
	 */
	private boolean renderBlockAnvil(BlockAnvil par1BlockAnvil, int par2, int par3, int par4) {
		return this.renderBlockAnvilMetadata(par1BlockAnvil, par2, par3, par4, this.blockAccess.getBlockMetadata(par2, par3, par4));
	}

	/**
	 * Renders anvil block with metadata
	 */
	public boolean renderBlockAnvilMetadata(BlockAnvil par1BlockAnvil, int par2, int par3, int par4, int par5) {
		Tessellator var6 = Tessellator.instance;
		var6.setBrightness(par1BlockAnvil.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var7 = 1.0F;
		int var8 = par1BlockAnvil.colorMultiplier(this.blockAccess, par2, par3, par4);
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
		return this.renderBlockAnvilOrient(par1BlockAnvil, par2, par3, par4, par5, false);
	}

	/**
	 * Renders anvil block with orientation
	 */
	private boolean renderBlockAnvilOrient(BlockAnvil par1BlockAnvil, int par2, int par3, int par4, int par5, boolean par6) {
		int var7 = par6 ? 0 : par5 & 3;
		boolean var8 = false;
		float var9 = 0.0F;

		switch (var7) {
			case 0:
				this.uvRotateSouth = 2;
				this.uvRotateNorth = 1;
				this.uvRotateTop = 3;
				this.uvRotateBottom = 3;
				break;

			case 1:
				this.uvRotateEast = 1;
				this.uvRotateWest = 2;
				this.uvRotateTop = 2;
				this.uvRotateBottom = 1;
				var8 = true;
				break;

			case 2:
				this.uvRotateSouth = 1;
				this.uvRotateNorth = 2;
				break;

			case 3:
				this.uvRotateEast = 2;
				this.uvRotateWest = 1;
				this.uvRotateTop = 1;
				this.uvRotateBottom = 2;
				var8 = true;
		}

		var9 = this.renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 0, var9, 0.75F, 0.25F, 0.75F, var8, par6, par5);
		var9 = this.renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 1, var9, 0.5F, 0.0625F, 0.625F, var8, par6, par5);
		var9 = this.renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 2, var9, 0.25F, 0.3125F, 0.5F, var8, par6, par5);
		this.renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 3, var9, 0.625F, 0.375F, 1.0F, var8, par6, par5);
		this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		this.uvRotateEast = 0;
		this.uvRotateWest = 0;
		this.uvRotateSouth = 0;
		this.uvRotateNorth = 0;
		this.uvRotateTop = 0;
		this.uvRotateBottom = 0;
		return true;
	}

	/**
	 * Renders anvil block with rotation
	 */
	private float renderBlockAnvilRotate(BlockAnvil par1BlockAnvil, int par2, int par3, int par4, int par5, float par6, float par7, float par8, float par9, boolean par10, boolean par11, int par12) {
		if (par10) {
			float var13 = par7;
			par7 = par9;
			par9 = var13;
		}

		par7 /= 2.0F;
		par9 /= 2.0F;
		par1BlockAnvil.field_82521_b = par5;
		this.setRenderBounds((double)(0.5F - par7), (double)par6, (double)(0.5F - par9), (double)(0.5F + par7), (double)(par6 + par8), (double)(0.5F + par9));

		if (par11) {
			Tessellator var14 = Tessellator.instance;
			var14.startDrawingQuads();
			var14.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(par1BlockAnvil, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 0, par12));
			var14.draw();
			var14.startDrawingQuads();
			var14.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(par1BlockAnvil, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 1, par12));
			var14.draw();
			var14.startDrawingQuads();
			var14.setNormal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(par1BlockAnvil, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 2, par12));
			var14.draw();
			var14.startDrawingQuads();
			var14.setNormal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(par1BlockAnvil, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 3, par12));
			var14.draw();
			var14.startDrawingQuads();
			var14.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(par1BlockAnvil, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 4, par12));
			var14.draw();
			var14.startDrawingQuads();
			var14.setNormal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(par1BlockAnvil, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 5, par12));
			var14.draw();
		} else {
			this.renderStandardBlock(par1BlockAnvil, par2, par3, par4);
		}

		return par6 + par8;
	}

	/**
	 * Renders a torch block at the given coordinates
	 */
	public boolean renderBlockTorch(Block par1Block, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		Tessellator var6 = Tessellator.instance;
		var6.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double var7 = 0.4000000059604645D;
		double var9 = 0.5D - var7;
		double var11 = 0.20000000298023224D;

		if (var5 == 1) {
			this.renderTorchAtAngle(par1Block, (double)par2 - var9, (double)par3 + var11, (double)par4, -var7, 0.0D, 0);
		} else if (var5 == 2) {
			this.renderTorchAtAngle(par1Block, (double)par2 + var9, (double)par3 + var11, (double)par4, var7, 0.0D, 0);
		} else if (var5 == 3) {
			this.renderTorchAtAngle(par1Block, (double)par2, (double)par3 + var11, (double)par4 - var9, 0.0D, -var7, 0);
		} else if (var5 == 4) {
			this.renderTorchAtAngle(par1Block, (double)par2, (double)par3 + var11, (double)par4 + var9, 0.0D, var7, 0);
		} else {
			this.renderTorchAtAngle(par1Block, (double)par2, (double)par3, (double)par4, 0.0D, 0.0D, 0);
		}

		return true;
	}

	/**
	 * render a redstone repeater at the given coordinates
	 */
	private boolean renderBlockRepeater(BlockRedstoneRepeater par1BlockRedstoneRepeater, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var6 = var5 & 3;
		int var7 = (var5 & 12) >> 2;
		Tessellator var8 = Tessellator.instance;
		var8.setBrightness(par1BlockRedstoneRepeater.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var8.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double var9 = -0.1875D;
		boolean var11 = par1BlockRedstoneRepeater.func_94476_e(this.blockAccess, par2, par3, par4, var5);
		double var12 = 0.0D;
		double var14 = 0.0D;
		double var16 = 0.0D;
		double var18 = 0.0D;

		switch (var6) {
			case 0:
				var18 = -0.3125D;
				var14 = BlockRedstoneRepeater.repeaterTorchOffset[var7];
				break;

			case 1:
				var16 = 0.3125D;
				var12 = -BlockRedstoneRepeater.repeaterTorchOffset[var7];
				break;

			case 2:
				var18 = 0.3125D;
				var14 = -BlockRedstoneRepeater.repeaterTorchOffset[var7];
				break;

			case 3:
				var16 = -0.3125D;
				var12 = BlockRedstoneRepeater.repeaterTorchOffset[var7];
		}

		if (!var11) {
			this.renderTorchAtAngle(par1BlockRedstoneRepeater, (double)par2 + var12, (double)par3 + var9, (double)par4 + var14, 0.0D, 0.0D, 0);
		} else {
			Icon var20 = this.getBlockIcon(Block.bedrock);
			this.setOverrideBlockTexture(var20);
			float var21 = 2.0F;
			float var22 = 14.0F;
			float var23 = 7.0F;
			float var24 = 9.0F;

			switch (var6) {
				case 1:
				case 3:
					var21 = 7.0F;
					var22 = 9.0F;
					var23 = 2.0F;
					var24 = 14.0F;

				case 0:
				case 2:
				default:
					this.setRenderBounds((double)(var21 / 16.0F + (float)var12), 0.125D, (double)(var23 / 16.0F + (float)var14), (double)(var22 / 16.0F + (float)var12), 0.25D, (double)(var24 / 16.0F + (float)var14));
					double var25 = (double)var20.getInterpolatedU((double)var21);
					double var27 = (double)var20.getInterpolatedV((double)var23);
					double var29 = (double)var20.getInterpolatedU((double)var22);
					double var31 = (double)var20.getInterpolatedV((double)var24);
					var8.addVertexWithUV((double)((float)par2 + var21 / 16.0F) + var12, (double)((float)par3 + 0.25F), (double)((float)par4 + var23 / 16.0F) + var14, var25, var27);
					var8.addVertexWithUV((double)((float)par2 + var21 / 16.0F) + var12, (double)((float)par3 + 0.25F), (double)((float)par4 + var24 / 16.0F) + var14, var25, var31);
					var8.addVertexWithUV((double)((float)par2 + var22 / 16.0F) + var12, (double)((float)par3 + 0.25F), (double)((float)par4 + var24 / 16.0F) + var14, var29, var31);
					var8.addVertexWithUV((double)((float)par2 + var22 / 16.0F) + var12, (double)((float)par3 + 0.25F), (double)((float)par4 + var23 / 16.0F) + var14, var29, var27);
					this.renderStandardBlock(par1BlockRedstoneRepeater, par2, par3, par4);
					this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
					this.clearOverrideBlockTexture();
			}
		}

		var8.setBrightness(par1BlockRedstoneRepeater.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var8.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		this.renderTorchAtAngle(par1BlockRedstoneRepeater, (double)par2 + var16, (double)par3 + var9, (double)par4 + var18, 0.0D, 0.0D, 0);
		this.renderBlockRedstoneLogic(par1BlockRedstoneRepeater, par2, par3, par4);
		return true;
	}

	private boolean renderBlockComparator(BlockComparator par1BlockComparator, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		var5.setBrightness(par1BlockComparator.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var7 = var6 & 3;
		double var8 = 0.0D;
		double var10 = -0.1875D;
		double var12 = 0.0D;
		double var14 = 0.0D;
		double var16 = 0.0D;
		Icon var18;

		if (par1BlockComparator.func_94490_c(var6)) {
			var18 = Block.torchRedstoneActive.getBlockTextureFromSide(0);
		} else {
			var10 -= 0.1875D;
			var18 = Block.torchRedstoneIdle.getBlockTextureFromSide(0);
		}

		switch (var7) {
			case 0:
				var12 = -0.3125D;
				var16 = 1.0D;
				break;

			case 1:
				var8 = 0.3125D;
				var14 = -1.0D;
				break;

			case 2:
				var12 = 0.3125D;
				var16 = -1.0D;
				break;

			case 3:
				var8 = -0.3125D;
				var14 = 1.0D;
		}

		this.renderTorchAtAngle(par1BlockComparator, (double)par2 + 0.25D * var14 + 0.1875D * var16, (double)((float)par3 - 0.1875F), (double)par4 + 0.25D * var16 + 0.1875D * var14, 0.0D, 0.0D, var6);
		this.renderTorchAtAngle(par1BlockComparator, (double)par2 + 0.25D * var14 + -0.1875D * var16, (double)((float)par3 - 0.1875F), (double)par4 + 0.25D * var16 + -0.1875D * var14, 0.0D, 0.0D, var6);
		this.setOverrideBlockTexture(var18);
		this.renderTorchAtAngle(par1BlockComparator, (double)par2 + var8, (double)par3 + var10, (double)par4 + var12, 0.0D, 0.0D, var6);
		this.clearOverrideBlockTexture();
		this.renderBlockRedstoneLogicMetadata(par1BlockComparator, par2, par3, par4, var7);
		return true;
	}

	private boolean renderBlockRedstoneLogic(BlockRedstoneLogic par1BlockRedstoneLogic, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		this.renderBlockRedstoneLogicMetadata(par1BlockRedstoneLogic, par2, par3, par4, this.blockAccess.getBlockMetadata(par2, par3, par4) & 3);
		return true;
	}

	private void renderBlockRedstoneLogicMetadata(BlockRedstoneLogic par1BlockRedstoneLogic, int par2, int par3, int par4, int par5) {
		this.renderStandardBlock(par1BlockRedstoneLogic, par2, par3, par4);
		Tessellator var6 = Tessellator.instance;
		var6.setBrightness(par1BlockRedstoneLogic.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		int var7 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		Icon var8 = this.getBlockIconFromSideAndMetadata(par1BlockRedstoneLogic, 1, var7);
		var8 = CTMUtils.getTile(this, par1BlockRedstoneLogic, par2, par3, par4, var8, var6);

		if (var8 != null) {
			var6 = CTMUtils.getTessellator(var8);
			double var9 = (double)var8.getMinU();
			double var11 = (double)var8.getMaxU();
			double var13 = (double)var8.getMinV();
			double var15 = (double)var8.getMaxV();
			double var17 = 0.125D;
			double var19 = (double)(par2 + 1);
			double var21 = (double)(par2 + 1);
			double var23 = (double)(par2 + 0);
			double var25 = (double)(par2 + 0);
			double var27 = (double)(par4 + 0);
			double var29 = (double)(par4 + 1);
			double var31 = (double)(par4 + 1);
			double var33 = (double)(par4 + 0);
			double var35 = (double)par3 + var17;

			if (par5 == 2) {
				var19 = var21 = (double)(par2 + 0);
				var23 = var25 = (double)(par2 + 1);
				var27 = var33 = (double)(par4 + 1);
				var29 = var31 = (double)(par4 + 0);
			} else if (par5 == 3) {
				var19 = var25 = (double)(par2 + 0);
				var21 = var23 = (double)(par2 + 1);
				var27 = var29 = (double)(par4 + 0);
				var31 = var33 = (double)(par4 + 1);
			} else if (par5 == 1) {
				var19 = var25 = (double)(par2 + 1);
				var21 = var23 = (double)(par2 + 0);
				var27 = var29 = (double)(par4 + 1);
				var31 = var33 = (double)(par4 + 0);
			}

			var6.addVertexWithUV(var25, var35, var33, var9, var13);
			var6.addVertexWithUV(var23, var35, var31, var9, var15);
			var6.addVertexWithUV(var21, var35, var29, var11, var15);
			var6.addVertexWithUV(var19, var35, var27, var11, var13);
		}
	}

	/**
	 * Render all faces of the piston base
	 */
	public void renderPistonBaseAllFaces(Block par1Block, int par2, int par3, int par4) {
		this.renderAllFaces = true;
		this.renderPistonBase(par1Block, par2, par3, par4, true);
		this.renderAllFaces = false;
	}

	/**
	 * renders a block as a piston base
	 */
	private boolean renderPistonBase(Block par1Block, int par2, int par3, int par4, boolean par5) {
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		boolean var7 = par5 || (var6 & 8) != 0;
		int var8 = BlockPistonBase.getOrientation(var6);

		if (var7) {
			switch (var8) {
				case 0:
					this.uvRotateEast = 3;
					this.uvRotateWest = 3;
					this.uvRotateSouth = 3;
					this.uvRotateNorth = 3;
					this.setRenderBounds(0.0D, 0.25D, 0.0D, 1.0D, 1.0D, 1.0D);
					break;

				case 1:
					this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
					break;

				case 2:
					this.uvRotateSouth = 1;
					this.uvRotateNorth = 2;
					this.setRenderBounds(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D);
					break;

				case 3:
					this.uvRotateSouth = 2;
					this.uvRotateNorth = 1;
					this.uvRotateTop = 3;
					this.uvRotateBottom = 3;
					this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D);
					break;

				case 4:
					this.uvRotateEast = 1;
					this.uvRotateWest = 2;
					this.uvRotateTop = 2;
					this.uvRotateBottom = 1;
					this.setRenderBounds(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
					break;

				case 5:
					this.uvRotateEast = 2;
					this.uvRotateWest = 1;
					this.uvRotateTop = 1;
					this.uvRotateBottom = 2;
					this.setRenderBounds(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D);
			}

			((BlockPistonBase)par1Block).func_96479_b((float)this.renderMinX, (float)this.renderMinY, (float)this.renderMinZ, (float)this.renderMaxX, (float)this.renderMaxY, (float)this.renderMaxZ);
			this.renderStandardBlock(par1Block, par2, par3, par4);
			this.uvRotateEast = 0;
			this.uvRotateWest = 0;
			this.uvRotateSouth = 0;
			this.uvRotateNorth = 0;
			this.uvRotateTop = 0;
			this.uvRotateBottom = 0;
			this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			((BlockPistonBase)par1Block).func_96479_b((float)this.renderMinX, (float)this.renderMinY, (float)this.renderMinZ, (float)this.renderMaxX, (float)this.renderMaxY, (float)this.renderMaxZ);
		} else {
			switch (var8) {
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

	/**
	 * Render piston rod up/down
	 */
	private void renderPistonRodUD(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
		Icon var16 = BlockPistonBase.func_94496_b("piston_side");

		if (this.hasOverrideBlockTexture()) {
			var16 = this.overrideBlockTexture;
		}

		Tessellator var17 = Tessellator.instance;
		double var18 = (double)var16.getMinU();
		double var20 = (double)var16.getMinV();
		double var22 = (double)var16.getInterpolatedU(par14);
		double var24 = (double)var16.getInterpolatedV(4.0D);
		var17.setColorOpaque_F(par13, par13, par13);
		var17.addVertexWithUV(par1, par7, par9, var22, var20);
		var17.addVertexWithUV(par1, par5, par9, var18, var20);
		var17.addVertexWithUV(par3, par5, par11, var18, var24);
		var17.addVertexWithUV(par3, par7, par11, var22, var24);
	}

	/**
	 * Render piston rod south/north
	 */
	private void renderPistonRodSN(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
		Icon var16 = BlockPistonBase.func_94496_b("piston_side");

		if (this.hasOverrideBlockTexture()) {
			var16 = this.overrideBlockTexture;
		}

		Tessellator var17 = Tessellator.instance;
		double var18 = (double)var16.getMinU();
		double var20 = (double)var16.getMinV();
		double var22 = (double)var16.getInterpolatedU(par14);
		double var24 = (double)var16.getInterpolatedV(4.0D);
		var17.setColorOpaque_F(par13, par13, par13);
		var17.addVertexWithUV(par1, par5, par11, var22, var20);
		var17.addVertexWithUV(par1, par5, par9, var18, var20);
		var17.addVertexWithUV(par3, par7, par9, var18, var24);
		var17.addVertexWithUV(par3, par7, par11, var22, var24);
	}

	/**
	 * Render piston rod east/west
	 */
	private void renderPistonRodEW(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
		Icon var16 = BlockPistonBase.func_94496_b("piston_side");

		if (this.hasOverrideBlockTexture()) {
			var16 = this.overrideBlockTexture;
		}

		Tessellator var17 = Tessellator.instance;
		double var18 = (double)var16.getMinU();
		double var20 = (double)var16.getMinV();
		double var22 = (double)var16.getInterpolatedU(par14);
		double var24 = (double)var16.getInterpolatedV(4.0D);
		var17.setColorOpaque_F(par13, par13, par13);
		var17.addVertexWithUV(par3, par5, par9, var22, var20);
		var17.addVertexWithUV(par1, par5, par9, var18, var20);
		var17.addVertexWithUV(par1, par7, par11, var18, var24);
		var17.addVertexWithUV(par3, par7, par11, var22, var24);
	}

	/**
	 * Render all faces of the piston extension
	 */
	public void renderPistonExtensionAllFaces(Block par1Block, int par2, int par3, int par4, boolean par5) {
		this.renderAllFaces = true;
		this.renderPistonExtension(par1Block, par2, par3, par4, par5);
		this.renderAllFaces = false;
	}

	/**
	 * renders the pushing part of a piston
	 */
	private boolean renderPistonExtension(Block par1Block, int par2, int par3, int par4, boolean par5) {
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var7 = BlockPistonExtension.getDirectionMeta(var6);
		float var11 = par1Block.getBlockBrightness(this.blockAccess, par2, par3, par4);
		float var12 = par5 ? 1.0F : 0.5F;
		double var13 = par5 ? 16.0D : 8.0D;

		switch (var7) {
			case 0:
				this.uvRotateEast = 3;
				this.uvRotateWest = 3;
				this.uvRotateSouth = 3;
				this.uvRotateNorth = 3;
				this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
				this.renderStandardBlock(par1Block, par2, par3, par4);
				this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + var12), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.625F), var11 * 0.8F, var13);
				this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + var12), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.375F), var11 * 0.8F, var13);
				this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.375F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + var12), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.625F), var11 * 0.6F, var13);
				this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.625F), (double)((float)par3 + 0.25F), (double)((float)par3 + 0.25F + var12), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.375F), var11 * 0.6F, var13);
				break;

			case 1:
				this.setRenderBounds(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
				this.renderStandardBlock(par1Block, par2, par3, par4);
				this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.625F), (double)((float)par3 - 0.25F + 1.0F - var12), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.625F), var11 * 0.8F, var13);
				this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.375F), (double)((float)par3 - 0.25F + 1.0F - var12), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.375F), var11 * 0.8F, var13);
				this.renderPistonRodUD((double)((float)par2 + 0.375F), (double)((float)par2 + 0.375F), (double)((float)par3 - 0.25F + 1.0F - var12), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.375F), (double)((float)par4 + 0.625F), var11 * 0.6F, var13);
				this.renderPistonRodUD((double)((float)par2 + 0.625F), (double)((float)par2 + 0.625F), (double)((float)par3 - 0.25F + 1.0F - var12), (double)((float)par3 - 0.25F + 1.0F), (double)((float)par4 + 0.625F), (double)((float)par4 + 0.375F), var11 * 0.6F, var13);
				break;

			case 2:
				this.uvRotateSouth = 1;
				this.uvRotateNorth = 2;
				this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
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
				this.setRenderBounds(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
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
				this.setRenderBounds(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
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
				this.setRenderBounds(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
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
		this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		return true;
	}

	/**
	 * Renders a lever block at the given coordinates
	 */
	public boolean renderBlockLever(Block par1Block, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var6 = var5 & 7;
		boolean var7 = (var5 & 8) > 0;
		Tessellator var8 = Tessellator.instance;
		boolean var9 = this.hasOverrideBlockTexture();

		if (!var9) {
			this.setOverrideBlockTexture(this.getBlockIcon(Block.cobblestone));
		}

		float var10 = 0.25F;
		float var11 = 0.1875F;
		float var12 = 0.1875F;

		if (var6 == 5) {
			this.setRenderBounds((double)(0.5F - var11), 0.0D, (double)(0.5F - var10), (double)(0.5F + var11), (double)var12, (double)(0.5F + var10));
		} else if (var6 == 6) {
			this.setRenderBounds((double)(0.5F - var10), 0.0D, (double)(0.5F - var11), (double)(0.5F + var10), (double)var12, (double)(0.5F + var11));
		} else if (var6 == 4) {
			this.setRenderBounds((double)(0.5F - var11), (double)(0.5F - var10), (double)(1.0F - var12), (double)(0.5F + var11), (double)(0.5F + var10), 1.0D);
		} else if (var6 == 3) {
			this.setRenderBounds((double)(0.5F - var11), (double)(0.5F - var10), 0.0D, (double)(0.5F + var11), (double)(0.5F + var10), (double)var12);
		} else if (var6 == 2) {
			this.setRenderBounds((double)(1.0F - var12), (double)(0.5F - var10), (double)(0.5F - var11), 1.0D, (double)(0.5F + var10), (double)(0.5F + var11));
		} else if (var6 == 1) {
			this.setRenderBounds(0.0D, (double)(0.5F - var10), (double)(0.5F - var11), (double)var12, (double)(0.5F + var10), (double)(0.5F + var11));
		} else if (var6 == 0) {
			this.setRenderBounds((double)(0.5F - var10), (double)(1.0F - var12), (double)(0.5F - var11), (double)(0.5F + var10), 1.0D, (double)(0.5F + var11));
		} else if (var6 == 7) {
			this.setRenderBounds((double)(0.5F - var11), (double)(1.0F - var12), (double)(0.5F - var10), (double)(0.5F + var11), 1.0D, (double)(0.5F + var10));
		}

		this.renderStandardBlock(par1Block, par2, par3, par4);

		if (!var9) {
			this.clearOverrideBlockTexture();
		}

		var8.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var13 = 1.0F;

		if (Block.lightValue[par1Block.blockID] > 0) {
			var13 = 1.0F;
		}

		var8.setColorOpaque_F(var13, var13, var13);
		Icon var14 = this.getBlockIconFromSide(par1Block, 0);

		if (this.hasOverrideBlockTexture()) {
			var14 = this.overrideBlockTexture;
		}

		var14 = CTMUtils.getTile(this, par1Block, par2, par3, par4, var14, var8);

		if (var14 == null) {
			return false;
		} else {
			var8 = CTMUtils.getTessellator(var14);
			double var15 = (double)var14.getMinU();
			double var17 = (double)var14.getMinV();
			double var19 = (double)var14.getMaxU();
			double var21 = (double)var14.getMaxV();
			Vec3[] var23 = new Vec3[8];
			float var24 = 0.0625F;
			float var25 = 0.0625F;
			float var26 = 0.625F;
			var23[0] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var24), 0.0D, (double)(-var25));
			var23[1] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var24, 0.0D, (double)(-var25));
			var23[2] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var24, 0.0D, (double)var25);
			var23[3] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var24), 0.0D, (double)var25);
			var23[4] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var24), (double)var26, (double)(-var25));
			var23[5] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var24, (double)var26, (double)(-var25));
			var23[6] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var24, (double)var26, (double)var25);
			var23[7] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var24), (double)var26, (double)var25);

			for (int var27 = 0; var27 < 8; ++var27) {
				if (var7) {
					var23[var27].zCoord -= 0.0625D;
					var23[var27].rotateAroundX(((float)Math.PI * 2F / 9F));
				} else {
					var23[var27].zCoord += 0.0625D;
					var23[var27].rotateAroundX(-((float)Math.PI * 2F / 9F));
				}

				if (var6 == 0 || var6 == 7) {
					var23[var27].rotateAroundZ((float)Math.PI);
				}

				if (var6 == 6 || var6 == 0) {
					var23[var27].rotateAroundY(((float)Math.PI / 2F));
				}

				if (var6 > 0 && var6 < 5) {
					var23[var27].yCoord -= 0.375D;
					var23[var27].rotateAroundX(((float)Math.PI / 2F));

					if (var6 == 4) {
						var23[var27].rotateAroundY(0.0F);
					}

					if (var6 == 3) {
						var23[var27].rotateAroundY((float)Math.PI);
					}

					if (var6 == 2) {
						var23[var27].rotateAroundY(((float)Math.PI / 2F));
					}

					if (var6 == 1) {
						var23[var27].rotateAroundY(-((float)Math.PI / 2F));
					}

					var23[var27].xCoord += (double)par2 + 0.5D;
					var23[var27].yCoord += (double)((float)par3 + 0.5F);
					var23[var27].zCoord += (double)par4 + 0.5D;
				} else if (var6 != 0 && var6 != 7) {
					var23[var27].xCoord += (double)par2 + 0.5D;
					var23[var27].yCoord += (double)((float)par3 + 0.125F);
					var23[var27].zCoord += (double)par4 + 0.5D;
				} else {
					var23[var27].xCoord += (double)par2 + 0.5D;
					var23[var27].yCoord += (double)((float)par3 + 0.875F);
					var23[var27].zCoord += (double)par4 + 0.5D;
				}
			}

			Vec3 var32 = null;
			Vec3 var28 = null;
			Vec3 var29 = null;
			Vec3 var30 = null;

			for (int var31 = 0; var31 < 6; ++var31) {
				if (var31 == 0) {
					var15 = (double)var14.getInterpolatedU(7.0D);
					var17 = (double)var14.getInterpolatedV(6.0D);
					var19 = (double)var14.getInterpolatedU(9.0D);
					var21 = (double)var14.getInterpolatedV(8.0D);
				} else if (var31 == 2) {
					var15 = (double)var14.getInterpolatedU(7.0D);
					var17 = (double)var14.getInterpolatedV(6.0D);
					var19 = (double)var14.getInterpolatedU(9.0D);
					var21 = (double)var14.getMaxV();
				}

				if (var31 == 0) {
					var32 = var23[0];
					var28 = var23[1];
					var29 = var23[2];
					var30 = var23[3];
				} else if (var31 == 1) {
					var32 = var23[7];
					var28 = var23[6];
					var29 = var23[5];
					var30 = var23[4];
				} else if (var31 == 2) {
					var32 = var23[1];
					var28 = var23[0];
					var29 = var23[4];
					var30 = var23[5];
				} else if (var31 == 3) {
					var32 = var23[2];
					var28 = var23[1];
					var29 = var23[5];
					var30 = var23[6];
				} else if (var31 == 4) {
					var32 = var23[3];
					var28 = var23[2];
					var29 = var23[6];
					var30 = var23[7];
				} else if (var31 == 5) {
					var32 = var23[0];
					var28 = var23[3];
					var29 = var23[7];
					var30 = var23[4];
				}

				var8.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, var15, var21);
				var8.addVertexWithUV(var28.xCoord, var28.yCoord, var28.zCoord, var19, var21);
				var8.addVertexWithUV(var29.xCoord, var29.yCoord, var29.zCoord, var19, var17);
				var8.addVertexWithUV(var30.xCoord, var30.yCoord, var30.zCoord, var15, var17);
			}

			return true;
		}
	}

	/**
	 * Renders a trip wire source block at the given coordinates
	 */
	public boolean renderBlockTripWireSource(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var7 = var6 & 3;
		boolean var8 = (var6 & 4) == 4;
		boolean var9 = (var6 & 8) == 8;
		boolean var10 = !this.blockAccess.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
		boolean var11 = this.hasOverrideBlockTexture();

		if (!var11) {
			this.setOverrideBlockTexture(this.getBlockIcon(Block.planks));
		}

		float var12 = 0.25F;
		float var13 = 0.125F;
		float var14 = 0.125F;
		float var15 = 0.3F - var12;
		float var16 = 0.3F + var12;

		if (var7 == 2) {
			this.setRenderBounds((double)(0.5F - var13), (double)var15, (double)(1.0F - var14), (double)(0.5F + var13), (double)var16, 1.0D);
		} else if (var7 == 0) {
			this.setRenderBounds((double)(0.5F - var13), (double)var15, 0.0D, (double)(0.5F + var13), (double)var16, (double)var14);
		} else if (var7 == 1) {
			this.setRenderBounds((double)(1.0F - var14), (double)var15, (double)(0.5F - var13), 1.0D, (double)var16, (double)(0.5F + var13));
		} else if (var7 == 3) {
			this.setRenderBounds(0.0D, (double)var15, (double)(0.5F - var13), (double)var14, (double)var16, (double)(0.5F + var13));
		}

		this.renderStandardBlock(par1Block, par2, par3, par4);

		if (!var11) {
			this.clearOverrideBlockTexture();
		}

		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var17 = 1.0F;

		if (Block.lightValue[par1Block.blockID] > 0) {
			var17 = 1.0F;
		}

		var5.setColorOpaque_F(var17, var17, var17);
		Icon var18 = this.getBlockIconFromSide(par1Block, 0);

		if (this.hasOverrideBlockTexture()) {
			var18 = this.overrideBlockTexture;
		}

		var18 = CTMUtils.getTile(this, par1Block, par2, par3, par4, var18, var5);

		if (var18 == null) {
			return false;
		} else {
			var5 = CTMUtils.getTessellator(var18);
			double var19 = (double)var18.getMinU();
			double var21 = (double)var18.getMinV();
			double var23 = (double)var18.getMaxU();
			double var25 = (double)var18.getMaxV();
			Vec3[] var27 = new Vec3[8];
			float var28 = 0.046875F;
			float var29 = 0.046875F;
			float var30 = 0.3125F;
			var27[0] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var28), 0.0D, (double)(-var29));
			var27[1] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var28, 0.0D, (double)(-var29));
			var27[2] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var28, 0.0D, (double)var29);
			var27[3] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var28), 0.0D, (double)var29);
			var27[4] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var28), (double)var30, (double)(-var29));
			var27[5] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var28, (double)var30, (double)(-var29));
			var27[6] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var28, (double)var30, (double)var29);
			var27[7] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var28), (double)var30, (double)var29);

			for (int var31 = 0; var31 < 8; ++var31) {
				var27[var31].zCoord += 0.0625D;

				if (var9) {
					var27[var31].rotateAroundX(0.5235988F);
					var27[var31].yCoord -= 0.4375D;
				} else if (var8) {
					var27[var31].rotateAroundX(0.08726647F);
					var27[var31].yCoord -= 0.4375D;
				} else {
					var27[var31].rotateAroundX(-((float)Math.PI * 2F / 9F));
					var27[var31].yCoord -= 0.375D;
				}

				var27[var31].rotateAroundX(((float)Math.PI / 2F));

				if (var7 == 2) {
					var27[var31].rotateAroundY(0.0F);
				}

				if (var7 == 0) {
					var27[var31].rotateAroundY((float)Math.PI);
				}

				if (var7 == 1) {
					var27[var31].rotateAroundY(((float)Math.PI / 2F));
				}

				if (var7 == 3) {
					var27[var31].rotateAroundY(-((float)Math.PI / 2F));
				}

				var27[var31].xCoord += (double)par2 + 0.5D;
				var27[var31].yCoord += (double)((float)par3 + 0.3125F);
				var27[var31].zCoord += (double)par4 + 0.5D;
			}

			Vec3 var62 = null;
			Vec3 var32 = null;
			Vec3 var33 = null;
			Vec3 var34 = null;
			byte var35 = 7;
			byte var36 = 9;
			byte var37 = 9;
			byte var38 = 16;

			for (int var39 = 0; var39 < 6; ++var39) {
				if (var39 == 0) {
					var62 = var27[0];
					var32 = var27[1];
					var33 = var27[2];
					var34 = var27[3];
					var19 = (double)var18.getInterpolatedU((double)var35);
					var21 = (double)var18.getInterpolatedV((double)var37);
					var23 = (double)var18.getInterpolatedU((double)var36);
					var25 = (double)var18.getInterpolatedV((double)(var37 + 2));
				} else if (var39 == 1) {
					var62 = var27[7];
					var32 = var27[6];
					var33 = var27[5];
					var34 = var27[4];
				} else if (var39 == 2) {
					var62 = var27[1];
					var32 = var27[0];
					var33 = var27[4];
					var34 = var27[5];
					var19 = (double)var18.getInterpolatedU((double)var35);
					var21 = (double)var18.getInterpolatedV((double)var37);
					var23 = (double)var18.getInterpolatedU((double)var36);
					var25 = (double)var18.getInterpolatedV((double)var38);
				} else if (var39 == 3) {
					var62 = var27[2];
					var32 = var27[1];
					var33 = var27[5];
					var34 = var27[6];
				} else if (var39 == 4) {
					var62 = var27[3];
					var32 = var27[2];
					var33 = var27[6];
					var34 = var27[7];
				} else if (var39 == 5) {
					var62 = var27[0];
					var32 = var27[3];
					var33 = var27[7];
					var34 = var27[4];
				}

				var5.addVertexWithUV(var62.xCoord, var62.yCoord, var62.zCoord, var19, var25);
				var5.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, var23, var25);
				var5.addVertexWithUV(var33.xCoord, var33.yCoord, var33.zCoord, var23, var21);
				var5.addVertexWithUV(var34.xCoord, var34.yCoord, var34.zCoord, var19, var21);
			}

			float var63 = 0.09375F;
			float var40 = 0.09375F;
			float var41 = 0.03125F;
			var27[0] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var63), 0.0D, (double)(-var40));
			var27[1] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var63, 0.0D, (double)(-var40));
			var27[2] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var63, 0.0D, (double)var40);
			var27[3] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var63), 0.0D, (double)var40);
			var27[4] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var63), (double)var41, (double)(-var40));
			var27[5] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var63, (double)var41, (double)(-var40));
			var27[6] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)var63, (double)var41, (double)var40);
			var27[7] = this.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-var63), (double)var41, (double)var40);

			for (int var42 = 0; var42 < 8; ++var42) {
				var27[var42].zCoord += 0.21875D;

				if (var9) {
					var27[var42].yCoord -= 0.09375D;
					var27[var42].zCoord -= 0.1625D;
					var27[var42].rotateAroundX(0.0F);
				} else if (var8) {
					var27[var42].yCoord += 0.015625D;
					var27[var42].zCoord -= 0.171875D;
					var27[var42].rotateAroundX(0.17453294F);
				} else {
					var27[var42].rotateAroundX(0.87266463F);
				}

				if (var7 == 2) {
					var27[var42].rotateAroundY(0.0F);
				}

				if (var7 == 0) {
					var27[var42].rotateAroundY((float)Math.PI);
				}

				if (var7 == 1) {
					var27[var42].rotateAroundY(((float)Math.PI / 2F));
				}

				if (var7 == 3) {
					var27[var42].rotateAroundY(-((float)Math.PI / 2F));
				}

				var27[var42].xCoord += (double)par2 + 0.5D;
				var27[var42].yCoord += (double)((float)par3 + 0.3125F);
				var27[var42].zCoord += (double)par4 + 0.5D;
			}

			byte var65 = 5;
			byte var43 = 11;
			byte var44 = 3;
			byte var45 = 9;

			for (int var46 = 0; var46 < 6; ++var46) {
				if (var46 == 0) {
					var62 = var27[0];
					var32 = var27[1];
					var33 = var27[2];
					var34 = var27[3];
					var19 = (double)var18.getInterpolatedU((double)var65);
					var21 = (double)var18.getInterpolatedV((double)var44);
					var23 = (double)var18.getInterpolatedU((double)var43);
					var25 = (double)var18.getInterpolatedV((double)var45);
				} else if (var46 == 1) {
					var62 = var27[7];
					var32 = var27[6];
					var33 = var27[5];
					var34 = var27[4];
				} else if (var46 == 2) {
					var62 = var27[1];
					var32 = var27[0];
					var33 = var27[4];
					var34 = var27[5];
					var19 = (double)var18.getInterpolatedU((double)var65);
					var21 = (double)var18.getInterpolatedV((double)var44);
					var23 = (double)var18.getInterpolatedU((double)var43);
					var25 = (double)var18.getInterpolatedV((double)(var44 + 2));
				} else if (var46 == 3) {
					var62 = var27[2];
					var32 = var27[1];
					var33 = var27[5];
					var34 = var27[6];
				} else if (var46 == 4) {
					var62 = var27[3];
					var32 = var27[2];
					var33 = var27[6];
					var34 = var27[7];
				} else if (var46 == 5) {
					var62 = var27[0];
					var32 = var27[3];
					var33 = var27[7];
					var34 = var27[4];
				}

				var5.addVertexWithUV(var62.xCoord, var62.yCoord, var62.zCoord, var19, var25);
				var5.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, var23, var25);
				var5.addVertexWithUV(var33.xCoord, var33.yCoord, var33.zCoord, var23, var21);
				var5.addVertexWithUV(var34.xCoord, var34.yCoord, var34.zCoord, var19, var21);
			}

			if (var8) {
				double var64 = var27[0].yCoord;
				float var48 = 0.03125F;
				float var49 = 0.5F - var48 / 2.0F;
				float var50 = var49 + var48;
				Icon var51 = this.getBlockIcon(Block.tripWire);
				var18 = CTMUtils.getTile(this, par1Block, par2, par3, par4, var18, var5);

				if (var18 == null) {
					return false;
				}

				var5 = CTMUtils.getTessellator(var18);
				double var52 = (double)var18.getMinU();
				double var54 = (double)var18.getInterpolatedV(var8 ? 2.0D : 0.0D);
				double var56 = (double)var18.getMaxU();
				double var58 = (double)var18.getInterpolatedV(var8 ? 4.0D : 2.0D);
				double var60 = (double)(var10 ? 3.5F : 1.5F) / 16.0D;
				var17 = par1Block.getBlockBrightness(this.blockAccess, par2, par3, par4) * 0.75F;
				var5.setColorOpaque_F(var17, var17, var17);

				if (var7 == 2) {
					var5.addVertexWithUV((double)((float)par2 + var49), (double)par3 + var60, (double)par4 + 0.25D, var52, var54);
					var5.addVertexWithUV((double)((float)par2 + var50), (double)par3 + var60, (double)par4 + 0.25D, var52, var58);
					var5.addVertexWithUV((double)((float)par2 + var50), (double)par3 + var60, (double)par4, var56, var58);
					var5.addVertexWithUV((double)((float)par2 + var49), (double)par3 + var60, (double)par4, var56, var54);
					var5.addVertexWithUV((double)((float)par2 + var49), var64, (double)par4 + 0.5D, var52, var54);
					var5.addVertexWithUV((double)((float)par2 + var50), var64, (double)par4 + 0.5D, var52, var58);
					var5.addVertexWithUV((double)((float)par2 + var50), (double)par3 + var60, (double)par4 + 0.25D, var56, var58);
					var5.addVertexWithUV((double)((float)par2 + var49), (double)par3 + var60, (double)par4 + 0.25D, var56, var54);
				} else if (var7 == 0) {
					var5.addVertexWithUV((double)((float)par2 + var49), (double)par3 + var60, (double)par4 + 0.75D, var52, var54);
					var5.addVertexWithUV((double)((float)par2 + var50), (double)par3 + var60, (double)par4 + 0.75D, var52, var58);
					var5.addVertexWithUV((double)((float)par2 + var50), var64, (double)par4 + 0.5D, var56, var58);
					var5.addVertexWithUV((double)((float)par2 + var49), var64, (double)par4 + 0.5D, var56, var54);
					var5.addVertexWithUV((double)((float)par2 + var49), (double)par3 + var60, (double)(par4 + 1), var52, var54);
					var5.addVertexWithUV((double)((float)par2 + var50), (double)par3 + var60, (double)(par4 + 1), var52, var58);
					var5.addVertexWithUV((double)((float)par2 + var50), (double)par3 + var60, (double)par4 + 0.75D, var56, var58);
					var5.addVertexWithUV((double)((float)par2 + var49), (double)par3 + var60, (double)par4 + 0.75D, var56, var54);
				} else if (var7 == 1) {
					var5.addVertexWithUV((double)par2, (double)par3 + var60, (double)((float)par4 + var50), var52, var58);
					var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var60, (double)((float)par4 + var50), var56, var58);
					var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var60, (double)((float)par4 + var49), var56, var54);
					var5.addVertexWithUV((double)par2, (double)par3 + var60, (double)((float)par4 + var49), var52, var54);
					var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var60, (double)((float)par4 + var50), var52, var58);
					var5.addVertexWithUV((double)par2 + 0.5D, var64, (double)((float)par4 + var50), var56, var58);
					var5.addVertexWithUV((double)par2 + 0.5D, var64, (double)((float)par4 + var49), var56, var54);
					var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var60, (double)((float)par4 + var49), var52, var54);
				} else {
					var5.addVertexWithUV((double)par2 + 0.5D, var64, (double)((float)par4 + var50), var52, var58);
					var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var60, (double)((float)par4 + var50), var56, var58);
					var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var60, (double)((float)par4 + var49), var56, var54);
					var5.addVertexWithUV((double)par2 + 0.5D, var64, (double)((float)par4 + var49), var52, var54);
					var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var60, (double)((float)par4 + var50), var52, var58);
					var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var60, (double)((float)par4 + var50), var56, var58);
					var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var60, (double)((float)par4 + var49), var56, var54);
					var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var60, (double)((float)par4 + var49), var52, var54);
				}
			}

			return true;
		}
	}

	/**
	 * Renders a trip wire block at the given coordinates
	 */
	public boolean renderBlockTripWire(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		Icon var6 = this.getBlockIconFromSide(par1Block, 0);
		int var7 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		boolean var8 = (var7 & 4) == 4;
		boolean var9 = (var7 & 2) == 2;

		if (this.hasOverrideBlockTexture()) {
			var6 = this.overrideBlockTexture;
		}

		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var10 = par1Block.getBlockBrightness(this.blockAccess, par2, par3, par4) * 0.75F;
		var5.setColorOpaque_F(var10, var10, var10);
		var6 = CTMUtils.getTile(this, par1Block, par2, par3, par4, var6, var5);

		if (var6 == null) {
			return false;
		} else {
			var5 = CTMUtils.getTessellator(var6);
			double var11 = (double)var6.getMinU();
			double var13 = (double)var6.getInterpolatedV(var8 ? 2.0D : 0.0D);
			double var15 = (double)var6.getMaxU();
			double var17 = (double)var6.getInterpolatedV(var8 ? 4.0D : 2.0D);
			double var19 = (double)(var9 ? 3.5F : 1.5F) / 16.0D;
			boolean var21 = BlockTripWire.func_72148_a(this.blockAccess, par2, par3, par4, var7, 1);
			boolean var22 = BlockTripWire.func_72148_a(this.blockAccess, par2, par3, par4, var7, 3);
			boolean var23 = BlockTripWire.func_72148_a(this.blockAccess, par2, par3, par4, var7, 2);
			boolean var24 = BlockTripWire.func_72148_a(this.blockAccess, par2, par3, par4, var7, 0);
			float var25 = 0.03125F;
			float var26 = 0.5F - var25 / 2.0F;
			float var27 = var26 + var25;

			if (!var23 && !var22 && !var24 && !var21) {
				var23 = true;
				var24 = true;
			}

			if (var23) {
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.25D, var11, var13);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.25D, var11, var17);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4, var15, var17);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4, var15, var13);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4, var15, var13);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4, var15, var17);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.25D, var11, var17);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.25D, var11, var13);
			}

			if (var23 || var24 && !var22 && !var21) {
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.5D, var11, var13);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.5D, var11, var17);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.25D, var15, var17);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.25D, var15, var13);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.25D, var15, var13);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.25D, var15, var17);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.5D, var11, var17);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.5D, var11, var13);
			}

			if (var24 || var23 && !var22 && !var21) {
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.75D, var11, var13);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.75D, var11, var17);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.5D, var15, var17);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.5D, var15, var13);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.5D, var15, var13);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.5D, var15, var17);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.75D, var11, var17);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.75D, var11, var13);
			}

			if (var24) {
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)(par4 + 1), var11, var13);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)(par4 + 1), var11, var17);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.75D, var15, var17);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.75D, var15, var13);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)par4 + 0.75D, var15, var13);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)par4 + 0.75D, var15, var17);
				var5.addVertexWithUV((double)((float)par2 + var27), (double)par3 + var19, (double)(par4 + 1), var11, var17);
				var5.addVertexWithUV((double)((float)par2 + var26), (double)par3 + var19, (double)(par4 + 1), var11, var13);
			}

			if (var21) {
				var5.addVertexWithUV((double)par2, (double)par3 + var19, (double)((float)par4 + var27), var11, var17);
				var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var19, (double)((float)par4 + var27), var15, var17);
				var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var19, (double)((float)par4 + var26), var15, var13);
				var5.addVertexWithUV((double)par2, (double)par3 + var19, (double)((float)par4 + var26), var11, var13);
				var5.addVertexWithUV((double)par2, (double)par3 + var19, (double)((float)par4 + var26), var11, var13);
				var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var19, (double)((float)par4 + var26), var15, var13);
				var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var19, (double)((float)par4 + var27), var15, var17);
				var5.addVertexWithUV((double)par2, (double)par3 + var19, (double)((float)par4 + var27), var11, var17);
			}

			if (var21 || var22 && !var23 && !var24) {
				var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var19, (double)((float)par4 + var27), var11, var17);
				var5.addVertexWithUV((double)par2 + 0.5D, (double)par3 + var19, (double)((float)par4 + var27), var15, var17);
				var5.addVertexWithUV((double)par2 + 0.5D, (double)par3 + var19, (double)((float)par4 + var26), var15, var13);
				var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var19, (double)((float)par4 + var26), var11, var13);
				var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var19, (double)((float)par4 + var26), var11, var13);
				var5.addVertexWithUV((double)par2 + 0.5D, (double)par3 + var19, (double)((float)par4 + var26), var15, var13);
				var5.addVertexWithUV((double)par2 + 0.5D, (double)par3 + var19, (double)((float)par4 + var27), var15, var17);
				var5.addVertexWithUV((double)par2 + 0.25D, (double)par3 + var19, (double)((float)par4 + var27), var11, var17);
			}

			if (var22 || var21 && !var23 && !var24) {
				var5.addVertexWithUV((double)par2 + 0.5D, (double)par3 + var19, (double)((float)par4 + var27), var11, var17);
				var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var19, (double)((float)par4 + var27), var15, var17);
				var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var19, (double)((float)par4 + var26), var15, var13);
				var5.addVertexWithUV((double)par2 + 0.5D, (double)par3 + var19, (double)((float)par4 + var26), var11, var13);
				var5.addVertexWithUV((double)par2 + 0.5D, (double)par3 + var19, (double)((float)par4 + var26), var11, var13);
				var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var19, (double)((float)par4 + var26), var15, var13);
				var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var19, (double)((float)par4 + var27), var15, var17);
				var5.addVertexWithUV((double)par2 + 0.5D, (double)par3 + var19, (double)((float)par4 + var27), var11, var17);
			}

			if (var22) {
				var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var19, (double)((float)par4 + var27), var11, var17);
				var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var19, (double)((float)par4 + var27), var15, var17);
				var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var19, (double)((float)par4 + var26), var15, var13);
				var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var19, (double)((float)par4 + var26), var11, var13);
				var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var19, (double)((float)par4 + var26), var11, var13);
				var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var19, (double)((float)par4 + var26), var15, var13);
				var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var19, (double)((float)par4 + var27), var15, var17);
				var5.addVertexWithUV((double)par2 + 0.75D, (double)par3 + var19, (double)((float)par4 + var27), var11, var17);
			}

			return true;
		}
	}

	/**
	 * Renders a fire block at the given coordinates
	 */
	public boolean renderBlockFire(BlockFire par1BlockFire, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		Icon var6 = par1BlockFire.func_94438_c(0);
		Icon var7 = par1BlockFire.func_94438_c(1);
		Icon var8 = var6;

		if (this.hasOverrideBlockTexture()) {
			var8 = this.overrideBlockTexture;
		}

		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		var5.setBrightness(par1BlockFire.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var8 = CTMUtils.getTile(this, par1BlockFire, par2, par3, par4, var8, var5);

		if (var8 == null) {
			return false;
		} else {
			var5 = CTMUtils.getTessellator(var8);
			double var9 = (double)var8.getMinU();
			double var11 = (double)var8.getMinV();
			double var13 = (double)var8.getMaxU();
			double var15 = (double)var8.getMaxV();
			float var17 = 1.4F;
			double var32;
			double var20;
			double var22;
			double var24;
			double var26;
			double var28;
			double var30;

			if (!this.blockAccess.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !Block.fire.canBlockCatchFire(this.blockAccess, par2, par3 - 1, par4)) {
				float var36 = 0.2F;
				float var19 = 0.0625F;

				if ((par2 + par3 + par4 & 1) == 1) {
					var8 = CTMUtils.getTile(this, par1BlockFire, par2, par3, par4, var7, var5);

					if (var8 == null) {
						return false;
					}

					var5 = CTMUtils.getTessellator(var8);
					var9 = (double)var8.getMinU();
					var11 = (double)var8.getMinV();
					var13 = (double)var8.getMaxU();
					var15 = (double)var8.getMaxV();
				}

				if ((par2 / 2 + par3 / 2 + par4 / 2 & 1) == 1) {
					var20 = var13;
					var13 = var9;
					var9 = var20;
				}

				if (Block.fire.canBlockCatchFire(this.blockAccess, par2 - 1, par3, par4)) {
					var5.addVertexWithUV((double)((float)par2 + var36), (double)((float)par3 + var17 + var19), (double)(par4 + 1), var13, var11);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1), var13, var15);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
					var5.addVertexWithUV((double)((float)par2 + var36), (double)((float)par3 + var17 + var19), (double)(par4 + 0), var9, var11);
					var5.addVertexWithUV((double)((float)par2 + var36), (double)((float)par3 + var17 + var19), (double)(par4 + 0), var9, var11);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1), var13, var15);
					var5.addVertexWithUV((double)((float)par2 + var36), (double)((float)par3 + var17 + var19), (double)(par4 + 1), var13, var11);
				}

				if (Block.fire.canBlockCatchFire(this.blockAccess, par2 + 1, par3, par4)) {
					var5.addVertexWithUV((double)((float)(par2 + 1) - var36), (double)((float)par3 + var17 + var19), (double)(par4 + 0), var9, var11);
					var5.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
					var5.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1), var13, var15);
					var5.addVertexWithUV((double)((float)(par2 + 1) - var36), (double)((float)par3 + var17 + var19), (double)(par4 + 1), var13, var11);
					var5.addVertexWithUV((double)((float)(par2 + 1) - var36), (double)((float)par3 + var17 + var19), (double)(par4 + 1), var13, var11);
					var5.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1), var13, var15);
					var5.addVertexWithUV((double)(par2 + 1 - 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
					var5.addVertexWithUV((double)((float)(par2 + 1) - var36), (double)((float)par3 + var17 + var19), (double)(par4 + 0), var9, var11);
				}

				if (Block.fire.canBlockCatchFire(this.blockAccess, par2, par3, par4 - 1)) {
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17 + var19), (double)((float)par4 + var36), var13, var11);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var13, var15);
					var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
					var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17 + var19), (double)((float)par4 + var36), var9, var11);
					var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17 + var19), (double)((float)par4 + var36), var9, var11);
					var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var9, var15);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 0), var13, var15);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17 + var19), (double)((float)par4 + var36), var13, var11);
				}

				if (Block.fire.canBlockCatchFire(this.blockAccess, par2, par3, par4 + 1)) {
					var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17 + var19), (double)((float)(par4 + 1) - var36), var9, var11);
					var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + var19), (double)(par4 + 1 - 0), var9, var15);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1 - 0), var13, var15);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17 + var19), (double)((float)(par4 + 1) - var36), var13, var11);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17 + var19), (double)((float)(par4 + 1) - var36), var13, var11);
					var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 0) + var19), (double)(par4 + 1 - 0), var13, var15);
					var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 0) + var19), (double)(par4 + 1 - 0), var9, var15);
					var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17 + var19), (double)((float)(par4 + 1) - var36), var9, var11);
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
					var8 = CTMUtils.getTile(this, par1BlockFire, par2, par3, par4, var6, var5);

					if (var8 == null) {
						return false;
					}

					var5 = CTMUtils.getTessellator(var8);
					var9 = (double)var8.getMinU();
					var11 = (double)var8.getMinV();
					var13 = (double)var8.getMaxU();
					var15 = (double)var8.getMaxV();
					++par3;
					var17 = -0.2F;

					if ((par2 + par3 + par4 & 1) == 0) {
						var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 0), var13, var11);
						var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 0), var13, var15);
						var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 1), var9, var15);
						var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 1), var9, var11);
						var8 = CTMUtils.getTile(this, par1BlockFire, par2, par3, par4, var7, var5);

						if (var8 == null) {
							return false;
						}

						var5 = CTMUtils.getTessellator(var8);
						var9 = (double)var8.getMinU();
						var11 = (double)var8.getMinV();
						var13 = (double)var8.getMaxU();
						var15 = (double)var8.getMaxV();
						var5.addVertexWithUV(var30, (double)((float)par3 + var17), (double)(par4 + 1), var13, var11);
						var5.addVertexWithUV(var22, (double)(par3 + 0), (double)(par4 + 1), var13, var15);
						var5.addVertexWithUV(var22, (double)(par3 + 0), (double)(par4 + 0), var9, var15);
						var5.addVertexWithUV(var30, (double)((float)par3 + var17), (double)(par4 + 0), var9, var11);
					} else {
						var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var34, var13, var11);
						var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var26, var13, var15);
						var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var26, var9, var15);
						var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var34, var9, var11);
						var8 = CTMUtils.getTile(this, par1BlockFire, par2, par3, par4, var7, var5);

						if (var8 == null) {
							return false;
						}

						var5 = CTMUtils.getTessellator(var8);
						var9 = (double)var8.getMinU();
						var11 = (double)var8.getMinV();
						var13 = (double)var8.getMaxU();
						var15 = (double)var8.getMaxV();
						var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var32, var13, var11);
						var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var24, var13, var15);
						var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var24, var9, var15);
						var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var32, var9, var11);
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
				var5.addVertexWithUV(var26, (double)((float)par3 + var17), (double)(par4 + 1), var13, var11);
				var5.addVertexWithUV(var18, (double)(par3 + 0), (double)(par4 + 1), var13, var15);
				var5.addVertexWithUV(var18, (double)(par3 + 0), (double)(par4 + 0), var9, var15);
				var5.addVertexWithUV(var26, (double)((float)par3 + var17), (double)(par4 + 0), var9, var11);
				var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 0), var13, var11);
				var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 0), var13, var15);
				var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 1), var9, var15);
				var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 1), var9, var11);
				var8 = CTMUtils.getTile(this, par1BlockFire, par2, par3, par4, var7, var5);

				if (var8 == null) {
					return false;
				}

				var5 = CTMUtils.getTessellator(var8);
				var9 = (double)var8.getMinU();
				var11 = (double)var8.getMinV();
				var13 = (double)var8.getMaxU();
				var15 = (double)var8.getMaxV();
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var32, var13, var11);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var24, var13, var15);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var24, var9, var15);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var32, var9, var11);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var30, var13, var11);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var22, var13, var15);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var22, var9, var15);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var30, var9, var11);
				var18 = (double)par2 + 0.5D - 0.5D;
				var20 = (double)par2 + 0.5D + 0.5D;
				var22 = (double)par4 + 0.5D - 0.5D;
				var24 = (double)par4 + 0.5D + 0.5D;
				var26 = (double)par2 + 0.5D - 0.4D;
				var28 = (double)par2 + 0.5D + 0.4D;
				var30 = (double)par4 + 0.5D - 0.4D;
				var32 = (double)par4 + 0.5D + 0.4D;
				var5.addVertexWithUV(var26, (double)((float)par3 + var17), (double)(par4 + 0), var9, var11);
				var5.addVertexWithUV(var18, (double)(par3 + 0), (double)(par4 + 0), var9, var15);
				var5.addVertexWithUV(var18, (double)(par3 + 0), (double)(par4 + 1), var13, var15);
				var5.addVertexWithUV(var26, (double)((float)par3 + var17), (double)(par4 + 1), var13, var11);
				var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 1), var9, var11);
				var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 1), var9, var15);
				var5.addVertexWithUV(var20, (double)(par3 + 0), (double)(par4 + 0), var13, var15);
				var5.addVertexWithUV(var28, (double)((float)par3 + var17), (double)(par4 + 0), var13, var11);
				var8 = CTMUtils.getTile(this, par1BlockFire, par2, par3, par4, var6, var5);

				if (var8 == null) {
					return false;
				}

				var5 = CTMUtils.getTessellator(var8);
				var9 = (double)var8.getMinU();
				var11 = (double)var8.getMinV();
				var13 = (double)var8.getMaxU();
				var15 = (double)var8.getMaxV();
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var32, var9, var11);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var24, var9, var15);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var24, var13, var15);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var32, var13, var11);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)par3 + var17), var30, var9, var11);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), var22, var9, var15);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), var22, var13, var15);
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)par3 + var17), var30, var13, var11);
			}

			return true;
		}
	}

	/**
	 * Renders a redstone wire block at the given coordinates
	 */
	public boolean renderBlockRedstoneWire(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		Icon var7 = BlockRedstoneWire.func_94409_b("redstoneDust_cross");
		Icon var8 = BlockRedstoneWire.func_94409_b("redstoneDust_line");
		Icon var9 = BlockRedstoneWire.func_94409_b("redstoneDust_cross_overlay");
		Icon var10 = BlockRedstoneWire.func_94409_b("redstoneDust_line_overlay");
		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var11 = 1.0F;
		float var13;
		float var14;
		float var15;

		if (ColorizeBlock.computeRedstoneWireColor(var6)) {
			var13 = Colorizer.setColor[0];
			var14 = Colorizer.setColor[1];
			var15 = Colorizer.setColor[2];
		} else {
			float var12 = (float)var6 / 15.0F;
			var13 = var12 * 0.6F + 0.4F;

			if (var6 == 0) {
				var13 = 0.3F;
			}

			var14 = var12 * var12 * 0.7F - 0.5F;
			var15 = var12 * var12 * 0.6F - 0.7F;
		}

		if (var14 < 0.0F) {
			var14 = 0.0F;
		}

		if (var15 < 0.0F) {
			var15 = 0.0F;
		}

		var5.setColorOpaque_F(var13, var14, var15);
		boolean var20 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3, par4, 1) || !this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3 - 1, par4, -1);
		boolean var21 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3, par4, 3) || !this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3 - 1, par4, -1);
		boolean var22 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3, par4 - 1, 2) || !this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 - 1, par4 - 1, -1);
		boolean var23 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3, par4 + 1, 0) || !this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 - 1, par4 + 1, -1);

		if (!this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
			if (this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3 + 1, par4, -1)) {
				var20 = true;
			}

			if (this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3 + 1, par4, -1)) {
				var21 = true;
			}

			if (this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 + 1, par4 - 1, -1)) {
				var22 = true;
			}

			if (this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 + 1, par4 + 1, -1)) {
				var23 = true;
			}
		}

		float var24 = (float)(par2 + 0);
		float var25 = (float)(par2 + 1);
		float var26 = (float)(par4 + 0);
		float var27 = (float)(par4 + 1);
		int var28 = 0;

		if ((var20 || var21) && !var22 && !var23) {
			var28 = 1;
		}

		if ((var22 || var23) && !var21 && !var20) {
			var28 = 2;
		}

		if (var28 == 0) {
			int var29 = 0;
			int var30 = 0;
			int var31 = 16;
			int var32 = 16;

			if (!var20) {
				var24 += 0.3125F;
			}

			if (!var20) {
				var29 += 5;
			}

			if (!var21) {
				var25 -= 0.3125F;
			}

			if (!var21) {
				var31 -= 5;
			}

			if (!var22) {
				var26 += 0.3125F;
			}

			if (!var22) {
				var30 += 5;
			}

			if (!var23) {
				var27 -= 0.3125F;
			}

			if (!var23) {
				var32 -= 5;
			}

			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var27, (double)var7.getInterpolatedU((double)var31), (double)var7.getInterpolatedV((double)var32));
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var26, (double)var7.getInterpolatedU((double)var31), (double)var7.getInterpolatedV((double)var30));
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var26, (double)var7.getInterpolatedU((double)var29), (double)var7.getInterpolatedV((double)var30));
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var27, (double)var7.getInterpolatedU((double)var29), (double)var7.getInterpolatedV((double)var32));
			var5.setColorOpaque_F(var11, var11, var11);
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var27, (double)var9.getInterpolatedU((double)var31), (double)var9.getInterpolatedV((double)var32));
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var26, (double)var9.getInterpolatedU((double)var31), (double)var9.getInterpolatedV((double)var30));
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var26, (double)var9.getInterpolatedU((double)var29), (double)var9.getInterpolatedV((double)var30));
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var27, (double)var9.getInterpolatedU((double)var29), (double)var9.getInterpolatedV((double)var32));
		} else if (var28 == 1) {
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var27, (double)var8.getMaxU(), (double)var8.getMaxV());
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var26, (double)var8.getMaxU(), (double)var8.getMinV());
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var26, (double)var8.getMinU(), (double)var8.getMinV());
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var27, (double)var8.getMinU(), (double)var8.getMaxV());
			var5.setColorOpaque_F(var11, var11, var11);
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var27, (double)var10.getMaxU(), (double)var10.getMaxV());
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var26, (double)var10.getMaxU(), (double)var10.getMinV());
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var26, (double)var10.getMinU(), (double)var10.getMinV());
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var27, (double)var10.getMinU(), (double)var10.getMaxV());
		} else {
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var27, (double)var8.getMaxU(), (double)var8.getMaxV());
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var26, (double)var8.getMinU(), (double)var8.getMaxV());
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var26, (double)var8.getMinU(), (double)var8.getMinV());
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var27, (double)var8.getMaxU(), (double)var8.getMinV());
			var5.setColorOpaque_F(var11, var11, var11);
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var27, (double)var10.getMaxU(), (double)var10.getMaxV());
			var5.addVertexWithUV((double)var25, (double)par3 + 0.015625D, (double)var26, (double)var10.getMinU(), (double)var10.getMaxV());
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var26, (double)var10.getMinU(), (double)var10.getMinV());
			var5.addVertexWithUV((double)var24, (double)par3 + 0.015625D, (double)var27, (double)var10.getMaxU(), (double)var10.getMinV());
		}

		if (!this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
			if (this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4) == Block.redstoneWire.blockID) {
				var5.setColorOpaque_F(var11 * var13, var11 * var14, var11 * var15);
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), (double)var8.getMaxU(), (double)var8.getMinV());
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 1), (double)var8.getMinU(), (double)var8.getMinV());
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 0), (double)var8.getMinU(), (double)var8.getMaxV());
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), (double)var8.getMaxU(), (double)var8.getMaxV());
				var5.setColorOpaque_F(var11, var11, var11);
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), (double)var10.getMaxU(), (double)var10.getMinV());
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 1), (double)var10.getMinU(), (double)var10.getMinV());
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)(par3 + 0), (double)(par4 + 0), (double)var10.getMinU(), (double)var10.getMaxV());
				var5.addVertexWithUV((double)par2 + 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), (double)var10.getMaxU(), (double)var10.getMaxV());
			}

			if (this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4) == Block.redstoneWire.blockID) {
				var5.setColorOpaque_F(var11 * var13, var11 * var14, var11 * var15);
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 1), (double)var8.getMinU(), (double)var8.getMaxV());
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), (double)var8.getMaxU(), (double)var8.getMaxV());
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), (double)var8.getMaxU(), (double)var8.getMinV());
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 0), (double)var8.getMinU(), (double)var8.getMinV());
				var5.setColorOpaque_F(var11, var11, var11);
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 1), (double)var10.getMinU(), (double)var10.getMaxV());
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1), (double)var10.getMaxU(), (double)var10.getMaxV());
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 0), (double)var10.getMaxU(), (double)var10.getMinV());
				var5.addVertexWithUV((double)(par2 + 1) - 0.015625D, (double)(par3 + 0), (double)(par4 + 0), (double)var10.getMinU(), (double)var10.getMinV());
			}

			if (this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1) == Block.redstoneWire.blockID) {
				var5.setColorOpaque_F(var11 * var13, var11 * var14, var11 * var15);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + 0.015625D, (double)var8.getMinU(), (double)var8.getMaxV());
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, (double)var8.getMaxU(), (double)var8.getMaxV());
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, (double)var8.getMaxU(), (double)var8.getMinV());
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + 0.015625D, (double)var8.getMinU(), (double)var8.getMinV());
				var5.setColorOpaque_F(var11, var11, var11);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + 0.015625D, (double)var10.getMinU(), (double)var10.getMaxV());
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, (double)var10.getMaxU(), (double)var10.getMaxV());
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)par4 + 0.015625D, (double)var10.getMaxU(), (double)var10.getMinV());
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + 0.015625D, (double)var10.getMinU(), (double)var10.getMinV());
			}

			if (this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1) == Block.redstoneWire.blockID) {
				var5.setColorOpaque_F(var11 * var13, var11 * var14, var11 * var15);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, (double)var8.getMaxU(), (double)var8.getMinV());
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, (double)var8.getMinU(), (double)var8.getMinV());
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, (double)var8.getMinU(), (double)var8.getMaxV());
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, (double)var8.getMaxU(), (double)var8.getMaxV());
				var5.setColorOpaque_F(var11, var11, var11);
				var5.addVertexWithUV((double)(par2 + 1), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, (double)var10.getMaxU(), (double)var10.getMinV());
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, (double)var10.getMinU(), (double)var10.getMinV());
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - 0.015625D, (double)var10.getMinU(), (double)var10.getMaxV());
				var5.addVertexWithUV((double)(par2 + 0), (double)((float)(par3 + 1) + 0.021875F), (double)(par4 + 1) - 0.015625D, (double)var10.getMaxU(), (double)var10.getMaxV());
			}
		}

		return true;
	}

	/**
	 * Renders a minecart track block at the given coordinates
	 */
	public boolean renderBlockMinecartTrack(BlockRailBase par1BlockRailBase, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		Icon var7 = this.getBlockIconFromSideAndMetadata(par1BlockRailBase, 0, var6);

		if (this.hasOverrideBlockTexture()) {
			var7 = this.overrideBlockTexture;
		}

		if (par1BlockRailBase.isPowered()) {
			var6 &= 7;
		}

		var5.setBrightness(par1BlockRailBase.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		var7 = CTMUtils.getTile(this, par1BlockRailBase, par2, par3, par4, var7, var5);

		if (var7 == null) {
			return false;
		} else {
			var5 = CTMUtils.getTessellator(var7);
			double var8 = (double)var7.getMinU();
			double var10 = (double)var7.getMinV();
			double var12 = (double)var7.getMaxU();
			double var14 = (double)var7.getMaxV();
			double var16 = 0.0625D;
			double var18 = (double)(par2 + 1);
			double var20 = (double)(par2 + 1);
			double var22 = (double)(par2 + 0);
			double var24 = (double)(par2 + 0);
			double var26 = (double)(par4 + 0);
			double var28 = (double)(par4 + 1);
			double var30 = (double)(par4 + 1);
			double var32 = (double)(par4 + 0);
			double var34 = (double)par3 + var16;
			double var36 = (double)par3 + var16;
			double var38 = (double)par3 + var16;
			double var40 = (double)par3 + var16;

			if (var6 != 1 && var6 != 2 && var6 != 3 && var6 != 7) {
				if (var6 == 8) {
					var18 = var20 = (double)(par2 + 0);
					var22 = var24 = (double)(par2 + 1);
					var26 = var32 = (double)(par4 + 1);
					var28 = var30 = (double)(par4 + 0);
				} else if (var6 == 9) {
					var18 = var24 = (double)(par2 + 0);
					var20 = var22 = (double)(par2 + 1);
					var26 = var28 = (double)(par4 + 0);
					var30 = var32 = (double)(par4 + 1);
				}
			} else {
				var18 = var24 = (double)(par2 + 1);
				var20 = var22 = (double)(par2 + 0);
				var26 = var28 = (double)(par4 + 1);
				var30 = var32 = (double)(par4 + 0);
			}

			if (var6 != 2 && var6 != 4) {
				if (var6 == 3 || var6 == 5) {
					++var36;
					++var38;
				}
			} else {
				++var34;
				++var40;
			}

			var5.addVertexWithUV(var18, var34, var26, var12, var10);
			var5.addVertexWithUV(var20, var36, var28, var12, var14);
			var5.addVertexWithUV(var22, var38, var30, var8, var14);
			var5.addVertexWithUV(var24, var40, var32, var8, var10);
			var5.addVertexWithUV(var24, var40, var32, var8, var10);
			var5.addVertexWithUV(var22, var38, var30, var8, var14);
			var5.addVertexWithUV(var20, var36, var28, var12, var14);
			var5.addVertexWithUV(var18, var34, var26, var12, var10);
			return true;
		}
	}

	/**
	 * Renders a ladder block at the given coordinates
	 */
	public boolean renderBlockLadder(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		Icon var6 = this.getBlockIconFromSide(par1Block, 0);

		if (this.hasOverrideBlockTexture()) {
			var6 = this.overrideBlockTexture;
		}

		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var7 = 1.0F;
		var5.setColorOpaque_F(var7, var7, var7);
		var6 = CTMUtils.getTile(this, par1Block, par2, par3, par4, var6, var5);

		if (var6 == null) {
			return false;
		} else {
			var5 = CTMUtils.getTessellator(var6);
			double var20 = (double)var6.getMinU();
			double var9 = (double)var6.getMinV();
			double var11 = (double)var6.getMaxU();
			double var13 = (double)var6.getMaxV();
			int var15 = this.blockAccess.getBlockMetadata(par2, par3, par4);
			double var16 = 0.0D;
			double var18 = 0.05000000074505806D;

			if (var15 == 5) {
				var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 1) + var16, (double)(par4 + 1) + var16, var20, var9);
				var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 0) - var16, (double)(par4 + 1) + var16, var20, var13);
				var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 0) - var16, (double)(par4 + 0) - var16, var11, var13);
				var5.addVertexWithUV((double)par2 + var18, (double)(par3 + 1) + var16, (double)(par4 + 0) - var16, var11, var9);
			}

			if (var15 == 4) {
				var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 0) - var16, (double)(par4 + 1) + var16, var11, var13);
				var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 1) + var16, (double)(par4 + 1) + var16, var11, var9);
				var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 1) + var16, (double)(par4 + 0) - var16, var20, var9);
				var5.addVertexWithUV((double)(par2 + 1) - var18, (double)(par3 + 0) - var16, (double)(par4 + 0) - var16, var20, var13);
			}

			if (var15 == 3) {
				var5.addVertexWithUV((double)(par2 + 1) + var16, (double)(par3 + 0) - var16, (double)par4 + var18, var11, var13);
				var5.addVertexWithUV((double)(par2 + 1) + var16, (double)(par3 + 1) + var16, (double)par4 + var18, var11, var9);
				var5.addVertexWithUV((double)(par2 + 0) - var16, (double)(par3 + 1) + var16, (double)par4 + var18, var20, var9);
				var5.addVertexWithUV((double)(par2 + 0) - var16, (double)(par3 + 0) - var16, (double)par4 + var18, var20, var13);
			}

			if (var15 == 2) {
				var5.addVertexWithUV((double)(par2 + 1) + var16, (double)(par3 + 1) + var16, (double)(par4 + 1) - var18, var20, var9);
				var5.addVertexWithUV((double)(par2 + 1) + var16, (double)(par3 + 0) - var16, (double)(par4 + 1) - var18, var20, var13);
				var5.addVertexWithUV((double)(par2 + 0) - var16, (double)(par3 + 0) - var16, (double)(par4 + 1) - var18, var11, var13);
				var5.addVertexWithUV((double)(par2 + 0) - var16, (double)(par3 + 1) + var16, (double)(par4 + 1) - var18, var11, var9);
			}

			return true;
		}
	}

	/**
	 * Render block vine
	 */
	public boolean renderBlockVine(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		Icon var6 = this.getBlockIconFromSide(par1Block, 0);

		if (this.hasOverrideBlockTexture()) {
			var6 = this.overrideBlockTexture;
		}

		float var7 = 1.0F;
		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		int var8 = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var9 = (float)(var8 >> 16 & 255) / 255.0F;
		float var10 = (float)(var8 >> 8 & 255) / 255.0F;
		float var11 = (float)(var8 & 255) / 255.0F;
		var5.setColorOpaque_F(var7 * var9, var7 * var10, var7 * var11);
		var6 = CTMUtils.getTile(this, par1Block, par2, par3, par4, var6, var5);

		if (var6 == null) {
			return false;
		} else {
			var5 = CTMUtils.getTessellator(var6);
			double var19 = (double)var6.getMinU();
			double var20 = (double)var6.getMinV();
			double var12 = (double)var6.getMaxU();
			double var14 = (double)var6.getMaxV();
			double var16 = 0.05000000074505806D;
			int var18 = this.blockAccess.getBlockMetadata(par2, par3, par4);

			if ((var18 & 2) != 0) {
				var5.addVertexWithUV((double)par2 + var16, (double)(par3 + 1), (double)(par4 + 1), var19, var20);
				var5.addVertexWithUV((double)par2 + var16, (double)(par3 + 0), (double)(par4 + 1), var19, var14);
				var5.addVertexWithUV((double)par2 + var16, (double)(par3 + 0), (double)(par4 + 0), var12, var14);
				var5.addVertexWithUV((double)par2 + var16, (double)(par3 + 1), (double)(par4 + 0), var12, var20);
				var5.addVertexWithUV((double)par2 + var16, (double)(par3 + 1), (double)(par4 + 0), var12, var20);
				var5.addVertexWithUV((double)par2 + var16, (double)(par3 + 0), (double)(par4 + 0), var12, var14);
				var5.addVertexWithUV((double)par2 + var16, (double)(par3 + 0), (double)(par4 + 1), var19, var14);
				var5.addVertexWithUV((double)par2 + var16, (double)(par3 + 1), (double)(par4 + 1), var19, var20);
			}

			if ((var18 & 8) != 0) {
				var5.addVertexWithUV((double)(par2 + 1) - var16, (double)(par3 + 0), (double)(par4 + 1), var12, var14);
				var5.addVertexWithUV((double)(par2 + 1) - var16, (double)(par3 + 1), (double)(par4 + 1), var12, var20);
				var5.addVertexWithUV((double)(par2 + 1) - var16, (double)(par3 + 1), (double)(par4 + 0), var19, var20);
				var5.addVertexWithUV((double)(par2 + 1) - var16, (double)(par3 + 0), (double)(par4 + 0), var19, var14);
				var5.addVertexWithUV((double)(par2 + 1) - var16, (double)(par3 + 0), (double)(par4 + 0), var19, var14);
				var5.addVertexWithUV((double)(par2 + 1) - var16, (double)(par3 + 1), (double)(par4 + 0), var19, var20);
				var5.addVertexWithUV((double)(par2 + 1) - var16, (double)(par3 + 1), (double)(par4 + 1), var12, var20);
				var5.addVertexWithUV((double)(par2 + 1) - var16, (double)(par3 + 0), (double)(par4 + 1), var12, var14);
			}

			if ((var18 & 4) != 0) {
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + var16, var12, var14);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)par4 + var16, var12, var20);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)par4 + var16, var19, var20);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + var16, var19, var14);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)par4 + var16, var19, var14);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)par4 + var16, var19, var20);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)par4 + var16, var12, var20);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)par4 + var16, var12, var14);
			}

			if ((var18 & 1) != 0) {
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)(par4 + 1) - var16, var19, var20);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - var16, var19, var14);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - var16, var12, var14);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)(par4 + 1) - var16, var12, var20);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1), (double)(par4 + 1) - var16, var12, var20);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)(par4 + 1) - var16, var12, var14);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 0), (double)(par4 + 1) - var16, var19, var14);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1), (double)(par4 + 1) - var16, var19, var20);
			}

			if (this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1) - var16, (double)(par4 + 0), var19, var20);
				var5.addVertexWithUV((double)(par2 + 1), (double)(par3 + 1) - var16, (double)(par4 + 1), var19, var14);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1) - var16, (double)(par4 + 1), var12, var14);
				var5.addVertexWithUV((double)(par2 + 0), (double)(par3 + 1) - var16, (double)(par4 + 0), var12, var20);
			}

			return true;
		}
	}

	public boolean renderBlockPane(BlockPane par1BlockPane, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getHeight();
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
		Icon var64;
		Icon var65;
		int var66;

		if (this.hasOverrideBlockTexture()) {
			var64 = this.overrideBlockTexture;
			var65 = this.overrideBlockTexture;
		} else {
			var66 = this.blockAccess.getBlockMetadata(par2, par3, par4);
			var64 = this.getBlockIconFromSideAndMetadata(par1BlockPane, 0, var66);
			var65 = par1BlockPane.getSideTextureIndex();
		}

		var66 = var64.getOriginX();
		int var15 = var64.getOriginY();
		double var16 = (double)var64.getMinU();
		double var18 = (double)var64.getInterpolatedU(8.0D);
		double var20 = (double)var64.getMaxU();
		double var22 = (double)var64.getMinV();
		double var24 = (double)var64.getMaxV();
		int var26 = var65.getOriginX();
		int var27 = var65.getOriginY();
		double var28 = (double)var65.getInterpolatedU(7.0D);
		double var30 = (double)var65.getInterpolatedU(9.0D);
		double var32 = (double)var65.getMinV();
		double var34 = (double)var65.getInterpolatedV(8.0D);
		double var36 = (double)var65.getMaxV();
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
		GlassPaneRenderer.render(this, par1BlockPane, var64, par2, par3, par4, var58, var59, var60, var61);
		boolean var62 = par1BlockPane.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1);
		boolean var63 = par1BlockPane.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0);

		if ((!var60 || !var61) && (var60 || var61 || var58 || var59)) {
			if (var60 && !var61) {
				if (!GlassPaneRenderer.active) {
					var6.addVertexWithUV(var38, (double)(par3 + 1), var46, var16, var22);
					var6.addVertexWithUV(var38, (double)(par3 + 0), var46, var16, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var18, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var18, var22);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var16, var22);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var16, var24);
					var6.addVertexWithUV(var38, (double)(par3 + 0), var46, var18, var24);
					var6.addVertexWithUV(var38, (double)(par3 + 1), var46, var18, var22);
				}

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
				if (!GlassPaneRenderer.active) {
					var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var18, var22);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var18, var24);
					var6.addVertexWithUV(var42, (double)(par3 + 0), var46, var20, var24);
					var6.addVertexWithUV(var42, (double)(par3 + 1), var46, var20, var22);
					var6.addVertexWithUV(var42, (double)(par3 + 1), var46, var18, var22);
					var6.addVertexWithUV(var42, (double)(par3 + 0), var46, var18, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var20, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var20, var22);
				}

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
			if (!GlassPaneRenderer.active) {
				var6.addVertexWithUV(var38, (double)(par3 + 1), var46, var16, var22);
				var6.addVertexWithUV(var38, (double)(par3 + 0), var46, var16, var24);
				var6.addVertexWithUV(var42, (double)(par3 + 0), var46, var20, var24);
				var6.addVertexWithUV(var42, (double)(par3 + 1), var46, var20, var22);
				var6.addVertexWithUV(var42, (double)(par3 + 1), var46, var16, var22);
				var6.addVertexWithUV(var42, (double)(par3 + 0), var46, var16, var24);
				var6.addVertexWithUV(var38, (double)(par3 + 0), var46, var20, var24);
				var6.addVertexWithUV(var38, (double)(par3 + 1), var46, var20, var22);
			}

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
				if (!GlassPaneRenderer.active) {
					var6.addVertexWithUV(var40, (double)(par3 + 1), var44, var16, var22);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var44, var16, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var18, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var18, var22);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var16, var22);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var16, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var44, var18, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var44, var18, var22);
				}

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
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var44, var30, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var46, var30, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var46, var28, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var44, var28, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var46, var30, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var44, var30, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var44, var28, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var46, var28, var32);
				}

				if (var63 || par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1)) {
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var44, var30, var32);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var46, var30, var34);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var46, var28, var34);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var44, var28, var32);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var46, var30, var32);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var44, var30, var34);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var44, var28, var34);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var46, var28, var32);
				}
			} else if (!var58 && var59) {
				if (!GlassPaneRenderer.active) {
					var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var18, var22);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var18, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var48, var20, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var48, var20, var22);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var48, var18, var22);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var48, var18, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 0), var46, var20, var24);
					var6.addVertexWithUV(var40, (double)(par3 + 1), var46, var20, var22);
				}

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
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var46, var28, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var48, var28, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var48, var30, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var46, var30, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var48, var28, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var46, var28, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var46, var30, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var48, var30, var34);
				}

				if (var63 || par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1)) {
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var46, var28, var34);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var48, var28, var36);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var48, var30, var36);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var46, var30, var34);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var48, var28, var34);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var46, var28, var36);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var46, var30, var36);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var48, var30, var34);
				}
			}
		} else {
			if (!GlassPaneRenderer.active) {
				var6.addVertexWithUV(var40, (double)(par3 + 1), var48, var16, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var48, var16, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var44, var20, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var44, var20, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var44, var16, var22);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var44, var16, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 0), var48, var20, var24);
				var6.addVertexWithUV(var40, (double)(par3 + 1), var48, var20, var22);
			}

			if (var62) {
				var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var48, var30, var36);
				var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var44, var30, var32);
				var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var44, var28, var32);
				var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var48, var28, var36);
				var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var44, var30, var36);
				var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var48, var30, var32);
				var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var48, var28, var32);
				var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var44, var28, var36);
			} else {
				if (par3 < var5 - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1)) {
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var44, var30, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var46, var30, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var46, var28, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var44, var28, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var46, var30, var32);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var44, var30, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var44, var28, var34);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var46, var28, var32);
				}

				if (par3 < var5 - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1)) {
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var46, var28, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var48, var28, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var48, var30, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var46, var30, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var48, var28, var34);
					var6.addVertexWithUV(var50, (double)(par3 + 1) + 0.005D, var46, var28, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var46, var30, var36);
					var6.addVertexWithUV(var52, (double)(par3 + 1) + 0.005D, var48, var30, var34);
				}
			}

			if (var63) {
				var6.addVertexWithUV(var52, (double)par3 - 0.005D, var48, var30, var36);
				var6.addVertexWithUV(var52, (double)par3 - 0.005D, var44, var30, var32);
				var6.addVertexWithUV(var50, (double)par3 - 0.005D, var44, var28, var32);
				var6.addVertexWithUV(var50, (double)par3 - 0.005D, var48, var28, var36);
				var6.addVertexWithUV(var52, (double)par3 - 0.005D, var44, var30, var36);
				var6.addVertexWithUV(var52, (double)par3 - 0.005D, var48, var30, var32);
				var6.addVertexWithUV(var50, (double)par3 - 0.005D, var48, var28, var32);
				var6.addVertexWithUV(var50, (double)par3 - 0.005D, var44, var28, var36);
			} else {
				if (par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1)) {
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var44, var30, var32);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var46, var30, var34);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var46, var28, var34);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var44, var28, var32);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var46, var30, var32);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var44, var30, var34);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var44, var28, var34);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var46, var28, var32);
				}

				if (par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1)) {
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var46, var28, var34);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var48, var28, var36);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var48, var30, var36);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var46, var30, var34);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var48, var28, var34);
					var6.addVertexWithUV(var50, (double)par3 - 0.005D, var46, var28, var36);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var46, var30, var36);
					var6.addVertexWithUV(var52, (double)par3 - 0.005D, var48, var30, var34);
				}
			}
		}

		return true;
	}

	/**
	 * Renders any block requiring croseed squares such as reeds, flowers, and mushrooms
	 */
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

		this.drawCrossedSquares(par1Block, this.blockAccess.getBlockMetadata(par2, par3, par4), var19, var20, var15, 1.0F);
		return true;
	}

	/**
	 * Render block stem
	 */
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
		int var15 = var5.getState(this.blockAccess, par2, par3, par4);

		if (var15 < 0) {
			this.renderBlockStemSmall(var5, this.blockAccess.getBlockMetadata(par2, par3, par4), this.renderMaxY, (double)par2, (double)((float)par3 - 0.0625F), (double)par4);
		} else {
			this.renderBlockStemSmall(var5, this.blockAccess.getBlockMetadata(par2, par3, par4), 0.5D, (double)par2, (double)((float)par3 - 0.0625F), (double)par4);
			this.renderBlockStemBig(var5, this.blockAccess.getBlockMetadata(par2, par3, par4), var15, this.renderMaxY, (double)par2, (double)((float)par3 - 0.0625F), (double)par4);
		}

		return true;
	}

	/**
	 * Render block crops
	 */
	public boolean renderBlockCrops(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		this.renderBlockCropsImpl(par1Block, this.blockAccess.getBlockMetadata(par2, par3, par4), (double)par2, (double)((float)par3 - 0.0625F), (double)par4);
		return true;
	}

	/**
	 * Renders a torch at the given coordinates, with the base slanting at the given delta
	 */
	public void renderTorchAtAngle(Block par1Block, double par2, double par4, double par6, double par8, double par10, int par12) {
		Tessellator var13 = Tessellator.instance;
		Icon var14 = this.getBlockIconFromSideAndMetadata(par1Block, 0, par12);

		if (this.hasOverrideBlockTexture()) {
			var14 = this.overrideBlockTexture;
		}

		double var15 = (double)var14.getMinU();
		double var17 = (double)var14.getMinV();
		double var19 = (double)var14.getMaxU();
		double var21 = (double)var14.getMaxV();
		double var23 = (double)var14.getInterpolatedU(7.0D);
		double var25 = (double)var14.getInterpolatedV(6.0D);
		double var27 = (double)var14.getInterpolatedU(9.0D);
		double var29 = (double)var14.getInterpolatedV(8.0D);
		double var31 = (double)var14.getInterpolatedU(7.0D);
		double var33 = (double)var14.getInterpolatedV(13.0D);
		double var35 = (double)var14.getInterpolatedU(9.0D);
		double var37 = (double)var14.getInterpolatedV(15.0D);
		par2 += 0.5D;
		par6 += 0.5D;
		double var39 = par2 - 0.5D;
		double var41 = par2 + 0.5D;
		double var43 = par6 - 0.5D;
		double var45 = par6 + 0.5D;
		double var47 = 0.0625D;
		double var49 = 0.625D;
		var13.addVertexWithUV(par2 + par8 * (1.0D - var49) - var47, par4 + var49, par6 + par10 * (1.0D - var49) - var47, var23, var25);
		var13.addVertexWithUV(par2 + par8 * (1.0D - var49) - var47, par4 + var49, par6 + par10 * (1.0D - var49) + var47, var23, var29);
		var13.addVertexWithUV(par2 + par8 * (1.0D - var49) + var47, par4 + var49, par6 + par10 * (1.0D - var49) + var47, var27, var29);
		var13.addVertexWithUV(par2 + par8 * (1.0D - var49) + var47, par4 + var49, par6 + par10 * (1.0D - var49) - var47, var27, var25);
		var13.addVertexWithUV(par2 + var47 + par8, par4, par6 - var47 + par10, var35, var33);
		var13.addVertexWithUV(par2 + var47 + par8, par4, par6 + var47 + par10, var35, var37);
		var13.addVertexWithUV(par2 - var47 + par8, par4, par6 + var47 + par10, var31, var37);
		var13.addVertexWithUV(par2 - var47 + par8, par4, par6 - var47 + par10, var31, var33);
		var13.addVertexWithUV(par2 - var47, par4 + 1.0D, var43, var15, var17);
		var13.addVertexWithUV(par2 - var47 + par8, par4 + 0.0D, var43 + par10, var15, var21);
		var13.addVertexWithUV(par2 - var47 + par8, par4 + 0.0D, var45 + par10, var19, var21);
		var13.addVertexWithUV(par2 - var47, par4 + 1.0D, var45, var19, var17);
		var13.addVertexWithUV(par2 + var47, par4 + 1.0D, var45, var15, var17);
		var13.addVertexWithUV(par2 + par8 + var47, par4 + 0.0D, var45 + par10, var15, var21);
		var13.addVertexWithUV(par2 + par8 + var47, par4 + 0.0D, var43 + par10, var19, var21);
		var13.addVertexWithUV(par2 + var47, par4 + 1.0D, var43, var19, var17);
		var13.addVertexWithUV(var39, par4 + 1.0D, par6 + var47, var15, var17);
		var13.addVertexWithUV(var39 + par8, par4 + 0.0D, par6 + var47 + par10, var15, var21);
		var13.addVertexWithUV(var41 + par8, par4 + 0.0D, par6 + var47 + par10, var19, var21);
		var13.addVertexWithUV(var41, par4 + 1.0D, par6 + var47, var19, var17);
		var13.addVertexWithUV(var41, par4 + 1.0D, par6 - var47, var15, var17);
		var13.addVertexWithUV(var41 + par8, par4 + 0.0D, par6 - var47 + par10, var15, var21);
		var13.addVertexWithUV(var39 + par8, par4 + 0.0D, par6 - var47 + par10, var19, var21);
		var13.addVertexWithUV(var39, par4 + 1.0D, par6 - var47, var19, var17);
	}

	/**
	 * Utility function to draw crossed swuares
	 */
	// Spout Start
	public void drawCrossedSquares(Block par1Block, int par2, double par3, double par5, double par7, float par9) {
		drawCrossedSquares(par1Block, par2, par3, par5, par7, par9, false);
	}

	public void drawCrossedSquares(Block par1Block, int par2, double par3, double par5, double par7, float par9, boolean customUV) {
		// Spout End
		Tessellator var10 = Tessellator.instance;
		Icon var11 = this.getBlockIconFromSideAndMetadata(par1Block, 0, par2);

		if (this.hasOverrideBlockTexture()) {
			var11 = this.overrideBlockTexture;
		}

		var11 = CTMUtils.getTile(this, par1Block, (int)par3, (int)par5, (int)par7, var11, var10);

		if (var11 != null) {
			var10 = CTMUtils.getTessellator(var11);
			double var12 = (double)var11.getMinU();
			double var14 = (double)var11.getMinV();
			double var16 = (double)var11.getMaxU();
			double var18 = (double)var11.getMaxV();
			double var20 = 0.45D * (double)par9;
			double var22 = par3 + 0.5D - var20;
			double var24 = par3 + 0.5D + var20;
			double var26 = par7 + 0.5D - var20;
			double var28 = par7 + 0.5D + var20;
			// Spout Start
			if (customUV) {
				var12 = 1;
				var14 = 0;
				var16 = 1;
				var18 = 0;
			}
			// Spout End
			var10.addVertexWithUV(var22, par5 + (double)par9, var26, var12, var14);
			var10.addVertexWithUV(var22, par5 + 0.0D, var26, var12, var18);
			var10.addVertexWithUV(var24, par5 + 0.0D, var28, var16, var18);
			var10.addVertexWithUV(var24, par5 + (double)par9, var28, var16, var14);
			var10.addVertexWithUV(var24, par5 + (double)par9, var28, var12, var14);
			var10.addVertexWithUV(var24, par5 + 0.0D, var28, var12, var18);
			var10.addVertexWithUV(var22, par5 + 0.0D, var26, var16, var18);
			var10.addVertexWithUV(var22, par5 + (double)par9, var26, var16, var14);
			var10.addVertexWithUV(var22, par5 + (double)par9, var28, var12, var14);
			var10.addVertexWithUV(var22, par5 + 0.0D, var28, var12, var18);
			var10.addVertexWithUV(var24, par5 + 0.0D, var26, var16, var18);
			var10.addVertexWithUV(var24, par5 + (double)par9, var26, var16, var14);
			var10.addVertexWithUV(var24, par5 + (double)par9, var26, var12, var14);
			var10.addVertexWithUV(var24, par5 + 0.0D, var26, var12, var18);
			var10.addVertexWithUV(var22, par5 + 0.0D, var28, var16, var18);
			var10.addVertexWithUV(var22, par5 + (double)par9, var28, var16, var14);
		}
	}

	/**
	 * Render block stem small
	 */
	public void renderBlockStemSmall(Block par1Block, int par2, double par3, double par5, double par7, double par9) {
		Tessellator var11 = Tessellator.instance;
		Icon var12 = this.getBlockIconFromSideAndMetadata(par1Block, 0, par2);

		if (this.hasOverrideBlockTexture()) {
			var12 = this.overrideBlockTexture;
		}

		double var13 = (double)var12.getMinU();
		double var15 = (double)var12.getMinV();
		double var17 = (double)var12.getMaxU();
		double var19 = (double)var12.getInterpolatedV(par3 * 16.0D);
		double var21 = par5 + 0.5D - 0.44999998807907104D;
		double var23 = par5 + 0.5D + 0.44999998807907104D;
		double var25 = par9 + 0.5D - 0.44999998807907104D;
		double var27 = par9 + 0.5D + 0.44999998807907104D;
		var11.addVertexWithUV(var21, par7 + par3, var25, var13, var15);
		var11.addVertexWithUV(var21, par7 + 0.0D, var25, var13, var19);
		var11.addVertexWithUV(var23, par7 + 0.0D, var27, var17, var19);
		var11.addVertexWithUV(var23, par7 + par3, var27, var17, var15);
		var11.addVertexWithUV(var23, par7 + par3, var27, var13, var15);
		var11.addVertexWithUV(var23, par7 + 0.0D, var27, var13, var19);
		var11.addVertexWithUV(var21, par7 + 0.0D, var25, var17, var19);
		var11.addVertexWithUV(var21, par7 + par3, var25, var17, var15);
		var11.addVertexWithUV(var21, par7 + par3, var27, var13, var15);
		var11.addVertexWithUV(var21, par7 + 0.0D, var27, var13, var19);
		var11.addVertexWithUV(var23, par7 + 0.0D, var25, var17, var19);
		var11.addVertexWithUV(var23, par7 + par3, var25, var17, var15);
		var11.addVertexWithUV(var23, par7 + par3, var25, var13, var15);
		var11.addVertexWithUV(var23, par7 + 0.0D, var25, var13, var19);
		var11.addVertexWithUV(var21, par7 + 0.0D, var27, var17, var19);
		var11.addVertexWithUV(var21, par7 + par3, var27, var17, var15);
	}

	/**
	 * Render BlockLilyPad
	 */
	public boolean renderBlockLilyPad(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		Icon var6 = this.getBlockIconFromSide(par1Block, 1);

		if (this.hasOverrideBlockTexture()) {
			var6 = this.overrideBlockTexture;
		}

		float var7 = 0.015625F;
		var6 = CTMUtils.getTile(this, par1Block, par2, par3, par4, var6, var5);

		if (var6 == null) {
			return false;
		} else {
			var5 = CTMUtils.getTessellator(var6);
			double var8 = (double)var6.getMinU();
			double var10 = (double)var6.getMinV();
			double var12 = (double)var6.getMaxU();
			double var14 = (double)var6.getMaxV();
			long var16 = (long)(par2 * 3129871) ^ (long)par4 * 116129781L ^ (long)par3;
			var16 = var16 * var16 * 42317861L + var16 * 11L;
			int var18 = (int)(var16 >> 16 & 3L);
			var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
			float var19 = (float)par2 + 0.5F;
			float var20 = (float)par4 + 0.5F;
			float var21 = (float)(var18 & 1) * 0.5F * (float)(1 - var18 / 2 % 2 * 2);
			float var22 = (float)(var18 + 1 & 1) * 0.5F * (float)(1 - (var18 + 1) / 2 % 2 * 2);
			var5.setColorOpaque_I(par1Block.getBlockColor());
			var5.addVertexWithUV((double)(var19 + var21 - var22), (double)((float)par3 + var7), (double)(var20 + var21 + var22), var8, var10);
			var5.addVertexWithUV((double)(var19 + var21 + var22), (double)((float)par3 + var7), (double)(var20 - var21 + var22), var12, var10);
			var5.addVertexWithUV((double)(var19 - var21 + var22), (double)((float)par3 + var7), (double)(var20 - var21 - var22), var12, var14);
			var5.addVertexWithUV((double)(var19 - var21 - var22), (double)((float)par3 + var7), (double)(var20 + var21 - var22), var8, var14);
			var5.setColorOpaque_I((par1Block.getBlockColor() & 16711422) >> 1);
			var5.addVertexWithUV((double)(var19 - var21 - var22), (double)((float)par3 + var7), (double)(var20 + var21 - var22), var8, var14);
			var5.addVertexWithUV((double)(var19 - var21 + var22), (double)((float)par3 + var7), (double)(var20 - var21 - var22), var12, var14);
			var5.addVertexWithUV((double)(var19 + var21 + var22), (double)((float)par3 + var7), (double)(var20 - var21 + var22), var12, var10);
			var5.addVertexWithUV((double)(var19 + var21 - var22), (double)((float)par3 + var7), (double)(var20 + var21 + var22), var8, var10);
			return true;
		}
	}

	/**
	 * Render block stem big
	 */
	public void renderBlockStemBig(BlockStem par1BlockStem, int par2, int par3, double par4, double par6, double par8, double par10) {
		Tessellator var12 = Tessellator.instance;
		Icon var13 = par1BlockStem.func_94368_p();

		if (this.hasOverrideBlockTexture()) {
			var13 = this.overrideBlockTexture;
		}

		double var14 = (double)var13.getMinU();
		double var16 = (double)var13.getMinV();
		double var18 = (double)var13.getMaxU();
		double var20 = (double)var13.getMaxV();
		double var22 = par6 + 0.5D - 0.5D;
		double var24 = par6 + 0.5D + 0.5D;
		double var26 = par10 + 0.5D - 0.5D;
		double var28 = par10 + 0.5D + 0.5D;
		double var30 = par6 + 0.5D;
		double var32 = par10 + 0.5D;

		if ((par3 + 1) / 2 % 2 == 1) {
			double var34 = var18;
			var18 = var14;
			var14 = var34;
		}

		if (par3 < 2) {
			var12.addVertexWithUV(var22, par8 + par4, var32, var14, var16);
			var12.addVertexWithUV(var22, par8 + 0.0D, var32, var14, var20);
			var12.addVertexWithUV(var24, par8 + 0.0D, var32, var18, var20);
			var12.addVertexWithUV(var24, par8 + par4, var32, var18, var16);
			var12.addVertexWithUV(var24, par8 + par4, var32, var18, var16);
			var12.addVertexWithUV(var24, par8 + 0.0D, var32, var18, var20);
			var12.addVertexWithUV(var22, par8 + 0.0D, var32, var14, var20);
			var12.addVertexWithUV(var22, par8 + par4, var32, var14, var16);
		} else {
			var12.addVertexWithUV(var30, par8 + par4, var28, var14, var16);
			var12.addVertexWithUV(var30, par8 + 0.0D, var28, var14, var20);
			var12.addVertexWithUV(var30, par8 + 0.0D, var26, var18, var20);
			var12.addVertexWithUV(var30, par8 + par4, var26, var18, var16);
			var12.addVertexWithUV(var30, par8 + par4, var26, var18, var16);
			var12.addVertexWithUV(var30, par8 + 0.0D, var26, var18, var20);
			var12.addVertexWithUV(var30, par8 + 0.0D, var28, var14, var20);
			var12.addVertexWithUV(var30, par8 + par4, var28, var14, var16);
		}
	}

	/**
	 * Render block crops implementation
	 */
	public void renderBlockCropsImpl(Block par1Block, int par2, double par3, double par5, double par7) {
		Tessellator var9 = Tessellator.instance;
		Icon var10 = this.getBlockIconFromSideAndMetadata(par1Block, 0, par2);

		if (this.hasOverrideBlockTexture()) {
			var10 = this.overrideBlockTexture;
		}

		double var11 = (double)var10.getMinU();
		double var13 = (double)var10.getMinV();
		double var15 = (double)var10.getMaxU();
		double var17 = (double)var10.getMaxV();
		double var19 = par3 + 0.5D - 0.25D;
		double var21 = par3 + 0.5D + 0.25D;
		double var23 = par7 + 0.5D - 0.5D;
		double var25 = par7 + 0.5D + 0.5D;
		var9.addVertexWithUV(var19, par5 + 1.0D, var23, var11, var13);
		var9.addVertexWithUV(var19, par5 + 0.0D, var23, var11, var17);
		var9.addVertexWithUV(var19, par5 + 0.0D, var25, var15, var17);
		var9.addVertexWithUV(var19, par5 + 1.0D, var25, var15, var13);
		var9.addVertexWithUV(var19, par5 + 1.0D, var25, var11, var13);
		var9.addVertexWithUV(var19, par5 + 0.0D, var25, var11, var17);
		var9.addVertexWithUV(var19, par5 + 0.0D, var23, var15, var17);
		var9.addVertexWithUV(var19, par5 + 1.0D, var23, var15, var13);
		var9.addVertexWithUV(var21, par5 + 1.0D, var25, var11, var13);
		var9.addVertexWithUV(var21, par5 + 0.0D, var25, var11, var17);
		var9.addVertexWithUV(var21, par5 + 0.0D, var23, var15, var17);
		var9.addVertexWithUV(var21, par5 + 1.0D, var23, var15, var13);
		var9.addVertexWithUV(var21, par5 + 1.0D, var23, var11, var13);
		var9.addVertexWithUV(var21, par5 + 0.0D, var23, var11, var17);
		var9.addVertexWithUV(var21, par5 + 0.0D, var25, var15, var17);
		var9.addVertexWithUV(var21, par5 + 1.0D, var25, var15, var13);
		var19 = par3 + 0.5D - 0.5D;
		var21 = par3 + 0.5D + 0.5D;
		var23 = par7 + 0.5D - 0.25D;
		var25 = par7 + 0.5D + 0.25D;
		var9.addVertexWithUV(var19, par5 + 1.0D, var23, var11, var13);
		var9.addVertexWithUV(var19, par5 + 0.0D, var23, var11, var17);
		var9.addVertexWithUV(var21, par5 + 0.0D, var23, var15, var17);
		var9.addVertexWithUV(var21, par5 + 1.0D, var23, var15, var13);
		var9.addVertexWithUV(var21, par5 + 1.0D, var23, var11, var13);
		var9.addVertexWithUV(var21, par5 + 0.0D, var23, var11, var17);
		var9.addVertexWithUV(var19, par5 + 0.0D, var23, var15, var17);
		var9.addVertexWithUV(var19, par5 + 1.0D, var23, var15, var13);
		var9.addVertexWithUV(var21, par5 + 1.0D, var25, var11, var13);
		var9.addVertexWithUV(var21, par5 + 0.0D, var25, var11, var17);
		var9.addVertexWithUV(var19, par5 + 0.0D, var25, var15, var17);
		var9.addVertexWithUV(var19, par5 + 1.0D, var25, var15, var13);
		var9.addVertexWithUV(var19, par5 + 1.0D, var25, var11, var13);
		var9.addVertexWithUV(var19, par5 + 0.0D, var25, var11, var17);
		var9.addVertexWithUV(var21, par5 + 0.0D, var25, var15, var17);
		var9.addVertexWithUV(var21, par5 + 1.0D, var25, var15, var13);
	}

	/**
	 * Renders a block based on the BlockFluids class at the given coordinates
	 */
	public boolean renderBlockFluids(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
		float var7 = (float)(var6 >> 16 & 255) / 255.0F;
		float var8 = (float)(var6 >> 8 & 255) / 255.0F;
		float var9 = (float)(var6 & 255) / 255.0F;
		boolean var10 = RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 + 1, par4, 1);
		boolean var11 = RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 - 1, par4, 0);
		boolean[] var12 = new boolean[] {RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 - 1, 2), RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 + 1, 3), RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 - 1, par3, par4, 4), RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 + 1, par3, par4, 5)};

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
			float var53;
			float var52;

			if (this.renderAllFaces || var10) {
				var13 = true;
				Icon var34 = this.getBlockIconFromSideAndMetadata(par1Block, 1, var23);
				float var35 = (float)BlockFluid.getFlowDirection(this.blockAccess, par2, par3, par4, var22);

				if (var35 > -999.0F) {
					var34 = this.getBlockIconFromSideAndMetadata(par1Block, 2, var23);
				}

				var24 -= var32;
				var26 -= var32;
				var28 -= var32;
				var30 -= var32;
				double var38;
				double var36;
				double var42;
				double var40;
				double var46;
				double var44;
				double var50;
				double var48;

				if (var35 < -999.0F) {
					var36 = (double)var34.getInterpolatedU(0.0D);
					var44 = (double)var34.getInterpolatedV(0.0D);
					var38 = var36;
					var46 = (double)var34.getInterpolatedV(16.0D);
					var40 = (double)var34.getInterpolatedU(16.0D);
					var48 = var46;
					var42 = var40;
					var50 = var44;
				} else {
					var52 = MathHelper.sin(var35) * 0.25F;
					var53 = MathHelper.cos(var35) * 0.25F;
					var36 = (double)var34.getInterpolatedU((double)(8.0F + (-var53 - var52) * 16.0F));
					var44 = (double)var34.getInterpolatedV((double)(8.0F + (-var53 + var52) * 16.0F));
					var38 = (double)var34.getInterpolatedU((double)(8.0F + (-var53 + var52) * 16.0F));
					var46 = (double)var34.getInterpolatedV((double)(8.0F + (var53 + var52) * 16.0F));
					var40 = (double)var34.getInterpolatedU((double)(8.0F + (var53 + var52) * 16.0F));
					var48 = (double)var34.getInterpolatedV((double)(8.0F + (var53 - var52) * 16.0F));
					var42 = (double)var34.getInterpolatedU((double)(8.0F + (var53 - var52) * 16.0F));
					var50 = (double)var34.getInterpolatedV((double)(8.0F + (-var53 - var52) * 16.0F));
				}

				var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
				var52 = 1.0F;
				var5.setColorOpaque_F(var15 * var52 * var7, var15 * var52 * var8, var15 * var52 * var9);
				var5.addVertexWithUV((double)(par2 + 0), (double)par3 + var24, (double)(par4 + 0), var36, var44);
				var5.addVertexWithUV((double)(par2 + 0), (double)par3 + var26, (double)(par4 + 1), var38, var46);
				var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var28, (double)(par4 + 1), var40, var48);
				var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var30, (double)(par4 + 0), var42, var50);
			}

			if (this.renderAllFaces || var11) {
				var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
				float var58 = 1.0F;
				var5.setColorOpaque_F(var14 * var58 * var7, var14 * var58 * var8, var14 * var58 * var9);
				this.renderBottomFace(par1Block, (double)par2, (double)par3 + var32, (double)par4, this.getBlockIconFromSide(par1Block, 0));
				var13 = true;
			}

			for (int var57 = 0; var57 < 4; ++var57) {
				int var59 = par2;
				int var37 = par4;

				if (var57 == 0) {
					var37 = par4 - 1;
				}

				if (var57 == 1) {
					++var37;
				}

				if (var57 == 2) {
					var59 = par2 - 1;
				}

				if (var57 == 3) {
					++var59;
				}

				Icon var60 = this.getBlockIconFromSideAndMetadata(par1Block, var57 + 2, var23);

				if (this.renderAllFaces || var12[var57]) {
					double var39;
					double var43;
					double var41;
					double var47;
					double var45;
					double var49;

					if (var57 == 0) {
						var39 = var24;
						var41 = var30;
						var43 = (double)par2;
						var47 = (double)(par2 + 1);
						var45 = (double)par4 + var32;
						var49 = (double)par4 + var32;
					} else if (var57 == 1) {
						var39 = var28;
						var41 = var26;
						var43 = (double)(par2 + 1);
						var47 = (double)par2;
						var45 = (double)(par4 + 1) - var32;
						var49 = (double)(par4 + 1) - var32;
					} else if (var57 == 2) {
						var39 = var26;
						var41 = var24;
						var43 = (double)par2 + var32;
						var47 = (double)par2 + var32;
						var45 = (double)(par4 + 1);
						var49 = (double)par4;
					} else {
						var39 = var30;
						var41 = var28;
						var43 = (double)(par2 + 1) - var32;
						var47 = (double)(par2 + 1) - var32;
						var45 = (double)par4;
						var49 = (double)(par4 + 1);
					}

					var13 = true;
					float var51 = var60.getInterpolatedU(0.0D);
					var52 = var60.getInterpolatedU(8.0D);
					var53 = var60.getInterpolatedV((1.0D - var39) * 16.0D * 0.5D);
					float var54 = var60.getInterpolatedV((1.0D - var41) * 16.0D * 0.5D);
					float var55 = var60.getInterpolatedV(8.0D);
					var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, var59, par3, var37));
					float var56 = 1.0F;

					if (var57 < 2) {
						var56 *= var16;
					} else {
						var56 *= var17;
					}

					var5.setColorOpaque_F(var15 * var56 * var7, var15 * var56 * var8, var15 * var56 * var9);
					var5.addVertexWithUV(var43, (double)par3 + var39, var45, (double)var51, (double)var53);
					var5.addVertexWithUV(var47, (double)par3 + var41, var49, (double)var52, (double)var54);
					var5.addVertexWithUV(var47, (double)(par3 + 0), var49, (double)var52, (double)var55);
					var5.addVertexWithUV(var43, (double)(par3 + 0), var45, (double)var51, (double)var55);
				}
			}

			this.renderMinY = var18;
			this.renderMaxY = var20;
			return var13;
		}
	}

	/**
	 * Get fluid height
	 */
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

	/**
	 * Renders a falling sand block
	 */
	public void renderBlockSandFalling(Block par1Block, World par2World, int par3, int par4, int par5, int par6) {
		float var7 = 0.5F;
		float var8 = 1.0F;
		float var9 = 0.8F;
		float var10 = 0.6F;
		Tessellator var11 = Tessellator.instance;
		var11.startDrawingQuads();
		var11.setBrightness(par1Block.getMixedBrightnessForBlock(par2World, par3, par4, par5));
		float var12 = 1.0F;
		float var13 = 1.0F;

		if (var13 < var12) {
			var13 = var12;
		}

		Colorizer.setColorF(ColorizeBlock.colorizeBlock(par1Block, par3, par4, par5, 0));
		var11.setColorOpaque_F(Colorizer.setColor[0] * var13, Colorizer.setColor[1] * var13, Colorizer.setColor[2] * var13);
		this.renderBottomFace(par1Block, -0.5D, -0.5D, -0.5D, this.getBlockIconFromSideAndMetadata(par1Block, 0, par6));
		var13 = 1.0F;

		if (var13 < var12) {
			var13 = var12;
		}

		var11.setColorOpaque_F(Colorizer.setColor[0] * var13, Colorizer.setColor[1] * var13, Colorizer.setColor[2] * var13);
		this.renderTopFace(par1Block, -0.5D, -0.5D, -0.5D, this.getBlockIconFromSideAndMetadata(par1Block, 1, par6));
		var13 = 1.0F;

		if (var13 < var12) {
			var13 = var12;
		}

		var11.setColorOpaque_F(Colorizer.setColor[0] * var13, Colorizer.setColor[1] * var13, Colorizer.setColor[2] * var13);
		this.renderEastFace(par1Block, -0.5D, -0.5D, -0.5D, this.getBlockIconFromSideAndMetadata(par1Block, 2, par6));
		var13 = 1.0F;

		if (var13 < var12) {
			var13 = var12;
		}

		var11.setColorOpaque_F(Colorizer.setColor[0] * var13, Colorizer.setColor[1] * var13, Colorizer.setColor[2] * var13);
		this.renderWestFace(par1Block, -0.5D, -0.5D, -0.5D, this.getBlockIconFromSideAndMetadata(par1Block, 3, par6));
		var13 = 1.0F;

		if (var13 < var12) {
			var13 = var12;
		}

		var11.setColorOpaque_F(Colorizer.setColor[0] * var13, Colorizer.setColor[1] * var13, Colorizer.setColor[2] * var13);
		this.renderNorthFace(par1Block, -0.5D, -0.5D, -0.5D, this.getBlockIconFromSideAndMetadata(par1Block, 4, par6));
		var13 = 1.0F;

		if (var13 < var12) {
			var13 = var12;
		}

		var11.setColorOpaque_F(Colorizer.setColor[0] * var13, Colorizer.setColor[1] * var13, Colorizer.setColor[2] * var13);
		this.renderSouthFace(par1Block, -0.5D, -0.5D, -0.5D, this.getBlockIconFromSideAndMetadata(par1Block, 5, par6));
		var11.draw();
	}

	/**
	 * Renders a standard cube block at the given coordinates
	 */
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

		// Spout Start
		return Configuration.ambientOcclusion && Block.lightValue[par1Block.blockID] == 0 ? this.renderStandardBlockWithAmbientOcclusion(par1Block, par2, par3, par4, var6, var7, var8) : this.renderStandardBlockWithColorMultiplier(par1Block, par2, par3, par4, var6, var7, var8);
		// Spout End
	}

	/**
	 * Renders a log block at the given coordinates
	 */
	public boolean renderBlockLog(Block par1Block, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var6 = var5 & 12;

		if (var6 == 4) {
			this.uvRotateEast = 1;
			this.uvRotateWest = 1;
			this.uvRotateTop = 1;
			this.uvRotateBottom = 1;
		} else if (var6 == 8) {
			this.uvRotateSouth = 1;
			this.uvRotateNorth = 1;
		}

		boolean var7 = this.renderStandardBlock(par1Block, par2, par3, par4);
		this.uvRotateSouth = 0;
		this.uvRotateEast = 0;
		this.uvRotateWest = 0;
		this.uvRotateNorth = 0;
		this.uvRotateTop = 0;
		this.uvRotateBottom = 0;
		return var7;
	}

	public boolean renderBlockQuartz(Block par1Block, int par2, int par3, int par4) {
		int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);

		if (var5 == 3) {
			this.uvRotateEast = 1;
			this.uvRotateWest = 1;
			this.uvRotateTop = 1;
			this.uvRotateBottom = 1;
		} else if (var5 == 4) {
			this.uvRotateSouth = 1;
			this.uvRotateNorth = 1;
		}

		boolean var6 = this.renderStandardBlock(par1Block, par2, par3, par4);
		this.uvRotateSouth = 0;
		this.uvRotateEast = 0;
		this.uvRotateWest = 0;
		this.uvRotateNorth = 0;
		this.uvRotateTop = 0;
		this.uvRotateBottom = 0;
		return var6;
	}

	public boolean renderStandardBlockWithAmbientOcclusion(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
		this.enableAO = true;
		boolean var8 = false;
		float var9 = 0.0F;
		float var10 = 0.0F;
		float var11 = 0.0F;
		float var12 = 0.0F;
		boolean var13 = true;
		int var14 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		Tessellator var15 = Tessellator.instance;
		var15.setBrightness(983055);

		if (this.getBlockIcon(par1Block).getIconName().equals("grass_top")) {
			var13 = false;
		} else if (this.hasOverrideBlockTexture()) {
			var13 = false;
		}

		boolean var17;
		boolean var16;
		boolean var19;
		boolean var18;
		float var21;
		int var20;

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 - 1, par4, 0)) {
			if (this.renderMinY <= 0.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

			if (!var19 && !var17) {
				this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
				this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
			} else {
				this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
				this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
			}

			if (!var18 && !var17) {
				this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
				this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
			} else {
				this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
				this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
			}

			if (!var19 && !var16) {
				this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
				this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
			} else {
				this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
				this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
			}

			if (!var18 && !var16) {
				this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
				this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
			} else {
				this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
				this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
			}

			if (this.renderMinY <= 0.0D) {
				++par3;
			}

			var20 = var14;

			if (this.renderMinY <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
			var9 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + var21) / 4.0F;
			var12 = (this.aoLightValueScratchYZNP + var21 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
			var11 = (var21 + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
			var10 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + var21 + this.aoLightValueScratchYZNN) / 4.0F;
			this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, var20);
			this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, var20);
			this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, var20);
			this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, var20);

			if (var13) {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * RenderPass.getAOBaseMultiplier(0.5F);
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * RenderPass.getAOBaseMultiplier(0.5F);
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * RenderPass.getAOBaseMultiplier(0.5F);
			} else {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = RenderPass.getAOBaseMultiplier(0.5F);
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = RenderPass.getAOBaseMultiplier(0.5F);
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = RenderPass.getAOBaseMultiplier(0.5F);
			}

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
			this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
			var8 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 + 1, par4, 1)) {
			if (this.renderMaxY >= 1.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];

			if (!var19 && !var17) {
				this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
				this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
			} else {
				this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
				this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
			}

			if (!var19 && !var16) {
				this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
				this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
			} else {
				this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
				this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
			}

			if (!var18 && !var17) {
				this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
				this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
			} else {
				this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
				this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
			}

			if (!var18 && !var16) {
				this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
				this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
			} else {
				this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
				this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
			}

			if (this.renderMaxY >= 1.0D) {
				--par3;
			}

			var20 = var14;

			if (this.renderMaxY >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
			var12 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + var21) / 4.0F;
			var9 = (this.aoLightValueScratchYZPP + var21 + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
			var10 = (var21 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
			var11 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + var21 + this.aoLightValueScratchYZPN) / 4.0F;
			this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, var20);
			this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, var20);
			this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, var20);
			this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var20);
			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7;
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
			this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
			var8 = true;
		}

		Icon var22;

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 - 1, 2)) {
			if (this.renderMinZ <= 0.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

			if (!var17 && !var19) {
				this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
			} else {
				this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
				this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
			}

			if (!var17 && !var18) {
				this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
			} else {
				this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
				this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
			}

			if (!var16 && !var19) {
				this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
			} else {
				this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
				this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
			}

			if (!var16 && !var18) {
				this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
			} else {
				this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
				this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
			}

			if (this.renderMinZ <= 0.0D) {
				++par4;
			}

			var20 = var14;

			if (this.renderMinZ <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
			var9 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + var21 + this.aoLightValueScratchYZPN) / 4.0F;
			var10 = (var21 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
			var11 = (this.aoLightValueScratchYZNN + var21 + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
			var12 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + var21) / 4.0F;
			this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var20);
			this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, var20);
			this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, var20);
			this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, var20);

			if (!var13 ? (!fancyGrass ? CTMUtils.isBetterGrass(this.blockAccess, par1Block, par2, par3, par4, 2) : false) : true) {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * RenderPass.getAOBaseMultiplier(0.8F);
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * RenderPass.getAOBaseMultiplier(0.8F);
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * RenderPass.getAOBaseMultiplier(0.8F);
			} else {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = RenderPass.getAOBaseMultiplier(0.8F);
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = RenderPass.getAOBaseMultiplier(0.8F);
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = RenderPass.getAOBaseMultiplier(0.8F);
			}

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
			var22 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2);
			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, var22);

			if (fancyGrass && var22.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
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
				this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var8 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 + 1, 3)) {
			if (this.renderMaxZ >= 1.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];

			if (!var17 && !var19) {
				this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
			} else {
				this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
				this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
			}

			if (!var17 && !var18) {
				this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
			} else {
				this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
				this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
			}

			if (!var16 && !var19) {
				this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
			} else {
				this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
				this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
			}

			if (!var16 && !var18) {
				this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
			} else {
				this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
				this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
			}

			if (this.renderMaxZ >= 1.0D) {
				--par4;
			}

			var20 = var14;

			if (this.renderMaxZ >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
			var9 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + var21 + this.aoLightValueScratchYZPP) / 4.0F;
			var12 = (var21 + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
			var11 = (this.aoLightValueScratchYZNP + var21 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
			var10 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + var21) / 4.0F;
			this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, var20);
			this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, var20);
			this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var20);
			this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, var20);

			if (!var13 ? (!fancyGrass ? CTMUtils.isBetterGrass(this.blockAccess, par1Block, par2, par3, par4, 3) : false) : true) {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * RenderPass.getAOBaseMultiplier(0.8F);
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * RenderPass.getAOBaseMultiplier(0.8F);
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * RenderPass.getAOBaseMultiplier(0.8F);
			} else {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = RenderPass.getAOBaseMultiplier(0.8F);
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = RenderPass.getAOBaseMultiplier(0.8F);
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = RenderPass.getAOBaseMultiplier(0.8F);
			}

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
			var22 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3);
			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));

			if (fancyGrass && var22.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
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
				this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var8 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 - 1, par3, par4, 4)) {
			if (this.renderMinX <= 0.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];

			if (!var18 && !var17) {
				this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
			} else {
				this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
				this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
			}

			if (!var19 && !var17) {
				this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
			} else {
				this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
				this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
			}

			if (!var18 && !var16) {
				this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
			} else {
				this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
				this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
			}

			if (!var19 && !var16) {
				this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
			} else {
				this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
				this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
			}

			if (this.renderMinX <= 0.0D) {
				++par2;
			}

			var20 = var14;

			if (this.renderMinX <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
			var12 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + var21 + this.aoLightValueScratchXZNP) / 4.0F;
			var9 = (var21 + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
			var10 = (this.aoLightValueScratchXZNN + var21 + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
			var11 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + var21) / 4.0F;
			this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, var20);
			this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, var20);
			this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, var20);
			this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, var20);

			if (!var13 ? (!fancyGrass ? CTMUtils.isBetterGrass(this.blockAccess, par1Block, par2, par3, par4, 4) : false) : true) {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * RenderPass.getAOBaseMultiplier(0.6F);
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * RenderPass.getAOBaseMultiplier(0.6F);
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * RenderPass.getAOBaseMultiplier(0.6F);
			} else {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = RenderPass.getAOBaseMultiplier(0.6F);
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = RenderPass.getAOBaseMultiplier(0.6F);
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = RenderPass.getAOBaseMultiplier(0.6F);
			}

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
			var22 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4);
			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, var22);

			if (fancyGrass && var22.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
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
				this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var8 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 + 1, par3, par4, 5)) {
			if (this.renderMaxX >= 1.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];

			if (!var17 && !var19) {
				this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
			} else {
				this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
				this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
			}

			if (!var17 && !var18) {
				this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
			} else {
				this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
				this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
			}

			if (!var16 && !var19) {
				this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
			} else {
				this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
				this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
			}

			if (!var16 && !var18) {
				this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
			} else {
				this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
				this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
			}

			if (this.renderMaxX >= 1.0D) {
				--par2;
			}

			var20 = var14;

			if (this.renderMaxX >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
			var9 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + var21 + this.aoLightValueScratchXZPP) / 4.0F;
			var10 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + var21) / 4.0F;
			var11 = (this.aoLightValueScratchXZPN + var21 + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
			var12 = (var21 + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
			this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var20);
			this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, var20);
			this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, var20);
			this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, var20);

			if (!var13 ? (!fancyGrass ? CTMUtils.isBetterGrass(this.blockAccess, par1Block, par2, par3, par4, 5) : false) : true) {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * RenderPass.getAOBaseMultiplier(0.6F);
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * RenderPass.getAOBaseMultiplier(0.6F);
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * RenderPass.getAOBaseMultiplier(0.6F);
			} else {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = RenderPass.getAOBaseMultiplier(0.6F);
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = RenderPass.getAOBaseMultiplier(0.6F);
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = RenderPass.getAOBaseMultiplier(0.6F);
			}

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
			var22 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5);
			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, var22);

			if (fancyGrass && var22.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
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
				this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var8 = true;
		}

		this.enableAO = false;
		return var8;
	}

	public boolean func_102027_b(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
		this.enableAO = true;
		boolean var8 = false;
		float var9 = 0.0F;
		float var10 = 0.0F;
		float var11 = 0.0F;
		float var12 = 0.0F;
		boolean var13 = true;
		int var14 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		Tessellator var15 = Tessellator.instance;
		var15.setBrightness(983055);

		if (this.getBlockIcon(par1Block).getIconName().equals("grass_top")) {
			var13 = false;
		} else if (this.hasOverrideBlockTexture()) {
			var13 = false;
		}

		boolean var17;
		boolean var16;
		boolean var19;
		boolean var18;
		float var21;
		int var20;

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 - 1, par4, 0)) {
			if (this.renderMinY <= 0.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

			if (!var19 && !var17) {
				this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
				this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
			} else {
				this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
				this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
			}

			if (!var18 && !var17) {
				this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
				this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
			} else {
				this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
				this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
			}

			if (!var19 && !var16) {
				this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
				this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
			} else {
				this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
				this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
			}

			if (!var18 && !var16) {
				this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
				this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
			} else {
				this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
				this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
			}

			if (this.renderMinY <= 0.0D) {
				++par3;
			}

			var20 = var14;

			if (this.renderMinY <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
			var9 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + var21) / 4.0F;
			var12 = (this.aoLightValueScratchYZNP + var21 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
			var11 = (var21 + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
			var10 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + var21 + this.aoLightValueScratchYZNN) / 4.0F;
			this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, var20);
			this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, var20);
			this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, var20);
			this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, var20);

			if (var13) {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.5F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.5F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.5F;
			} else {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.5F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.5F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.5F;
			}

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
			this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
			var8 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 + 1, par4, 1)) {
			if (this.renderMaxY >= 1.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];

			if (!var19 && !var17) {
				this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
				this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
			} else {
				this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
				this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
			}

			if (!var19 && !var16) {
				this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
				this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
			} else {
				this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
				this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
			}

			if (!var18 && !var17) {
				this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
				this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
			} else {
				this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
				this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
			}

			if (!var18 && !var16) {
				this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
				this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
			} else {
				this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
				this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
			}

			if (this.renderMaxY >= 1.0D) {
				--par3;
			}

			var20 = var14;

			if (this.renderMaxY >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
			var12 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + var21) / 4.0F;
			var9 = (this.aoLightValueScratchYZPP + var21 + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
			var10 = (var21 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
			var11 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + var21 + this.aoLightValueScratchYZPN) / 4.0F;
			this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, var20);
			this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, var20);
			this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, var20);
			this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var20);
			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7;
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
			this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
			var8 = true;
		}

		float var23;
		float var22;
		float var25;
		float var24;
		int var27;
		int var26;
		int var29;
		int var28;
		Icon var30;

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 - 1, 2)) {
			if (this.renderMinZ <= 0.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

			if (!var17 && !var19) {
				this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
			} else {
				this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
				this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
			}

			if (!var17 && !var18) {
				this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
			} else {
				this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
				this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
			}

			if (!var16 && !var19) {
				this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
			} else {
				this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
				this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
			}

			if (!var16 && !var18) {
				this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
			} else {
				this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
				this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
			}

			if (this.renderMinZ <= 0.0D) {
				++par4;
			}

			var20 = var14;

			if (this.renderMinZ <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
			var22 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + var21 + this.aoLightValueScratchYZPN) / 4.0F;
			var23 = (var21 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
			var24 = (this.aoLightValueScratchYZNN + var21 + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
			var25 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + var21) / 4.0F;
			var9 = (float)((double)var22 * this.renderMaxY * (1.0D - this.renderMinX) + (double)var23 * this.renderMinY * this.renderMinX + (double)var24 * (1.0D - this.renderMaxY) * this.renderMinX + (double)var25 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
			var10 = (float)((double)var22 * this.renderMaxY * (1.0D - this.renderMaxX) + (double)var23 * this.renderMaxY * this.renderMaxX + (double)var24 * (1.0D - this.renderMaxY) * this.renderMaxX + (double)var25 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
			var11 = (float)((double)var22 * this.renderMinY * (1.0D - this.renderMaxX) + (double)var23 * this.renderMinY * this.renderMaxX + (double)var24 * (1.0D - this.renderMinY) * this.renderMaxX + (double)var25 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
			var12 = (float)((double)var22 * this.renderMinY * (1.0D - this.renderMinX) + (double)var23 * this.renderMinY * this.renderMinX + (double)var24 * (1.0D - this.renderMinY) * this.renderMinX + (double)var25 * (1.0D - this.renderMinY) * (1.0D - this.renderMinX));
			var26 = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var20);
			var27 = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, var20);
			var28 = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, var20);
			var29 = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, var20);
			this.brightnessTopLeft = this.mixAoBrightness(var26, var27, var28, var29, this.renderMaxY * (1.0D - this.renderMinX), this.renderMaxY * this.renderMinX, (1.0D - this.renderMaxY) * this.renderMinX, (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
			this.brightnessBottomLeft = this.mixAoBrightness(var26, var27, var28, var29, this.renderMaxY * (1.0D - this.renderMaxX), this.renderMaxY * this.renderMaxX, (1.0D - this.renderMaxY) * this.renderMaxX, (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
			this.brightnessBottomRight = this.mixAoBrightness(var26, var27, var28, var29, this.renderMinY * (1.0D - this.renderMaxX), this.renderMinY * this.renderMaxX, (1.0D - this.renderMinY) * this.renderMaxX, (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
			this.brightnessTopRight = this.mixAoBrightness(var26, var27, var28, var29, this.renderMinY * (1.0D - this.renderMinX), this.renderMinY * this.renderMinX, (1.0D - this.renderMinY) * this.renderMinX, (1.0D - this.renderMinY) * (1.0D - this.renderMinX));

			if (var13) {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.8F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.8F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.8F;
			} else {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
			}

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
			var30 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2);
			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, var30);

			if (fancyGrass && var30.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
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
				this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var8 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 + 1, 3)) {
			if (this.renderMaxZ >= 1.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];

			if (!var17 && !var19) {
				this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
			} else {
				this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
				this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
			}

			if (!var17 && !var18) {
				this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
			} else {
				this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
				this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
			}

			if (!var16 && !var19) {
				this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
			} else {
				this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
				this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
			}

			if (!var16 && !var18) {
				this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
			} else {
				this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
				this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
			}

			if (this.renderMaxZ >= 1.0D) {
				--par4;
			}

			var20 = var14;

			if (this.renderMaxZ >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
			var22 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + var21 + this.aoLightValueScratchYZPP) / 4.0F;
			var23 = (var21 + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
			var24 = (this.aoLightValueScratchYZNP + var21 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
			var25 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + var21) / 4.0F;
			var9 = (float)((double)var22 * this.renderMaxY * (1.0D - this.renderMinX) + (double)var23 * this.renderMaxY * this.renderMinX + (double)var24 * (1.0D - this.renderMaxY) * this.renderMinX + (double)var25 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
			var10 = (float)((double)var22 * this.renderMinY * (1.0D - this.renderMinX) + (double)var23 * this.renderMinY * this.renderMinX + (double)var24 * (1.0D - this.renderMinY) * this.renderMinX + (double)var25 * (1.0D - this.renderMinY) * (1.0D - this.renderMinX));
			var11 = (float)((double)var22 * this.renderMinY * (1.0D - this.renderMaxX) + (double)var23 * this.renderMinY * this.renderMaxX + (double)var24 * (1.0D - this.renderMinY) * this.renderMaxX + (double)var25 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
			var12 = (float)((double)var22 * this.renderMaxY * (1.0D - this.renderMaxX) + (double)var23 * this.renderMaxY * this.renderMaxX + (double)var24 * (1.0D - this.renderMaxY) * this.renderMaxX + (double)var25 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
			var26 = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, var20);
			var27 = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, var20);
			var28 = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var20);
			var29 = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, var20);
			this.brightnessTopLeft = this.mixAoBrightness(var26, var29, var28, var27, this.renderMaxY * (1.0D - this.renderMinX), (1.0D - this.renderMaxY) * (1.0D - this.renderMinX), (1.0D - this.renderMaxY) * this.renderMinX, this.renderMaxY * this.renderMinX);
			this.brightnessBottomLeft = this.mixAoBrightness(var26, var29, var28, var27, this.renderMinY * (1.0D - this.renderMinX), (1.0D - this.renderMinY) * (1.0D - this.renderMinX), (1.0D - this.renderMinY) * this.renderMinX, this.renderMinY * this.renderMinX);
			this.brightnessBottomRight = this.mixAoBrightness(var26, var29, var28, var27, this.renderMinY * (1.0D - this.renderMaxX), (1.0D - this.renderMinY) * (1.0D - this.renderMaxX), (1.0D - this.renderMinY) * this.renderMaxX, this.renderMinY * this.renderMaxX);
			this.brightnessTopRight = this.mixAoBrightness(var26, var29, var28, var27, this.renderMaxY * (1.0D - this.renderMaxX), (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX), (1.0D - this.renderMaxY) * this.renderMaxX, this.renderMaxY * this.renderMaxX);

			if (var13) {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.8F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.8F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.8F;
			} else {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
			}

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
			var30 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3);
			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));

			if (fancyGrass && var30.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
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
				this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var8 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 - 1, par3, par4, 4)) {
			if (this.renderMinX <= 0.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];

			if (!var18 && !var17) {
				this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
			} else {
				this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
				this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
			}

			if (!var19 && !var17) {
				this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
			} else {
				this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
				this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
			}

			if (!var18 && !var16) {
				this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
			} else {
				this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
				this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
			}

			if (!var19 && !var16) {
				this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
			} else {
				this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
				this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
			}

			if (this.renderMinX <= 0.0D) {
				++par2;
			}

			var20 = var14;

			if (this.renderMinX <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
			var22 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + var21 + this.aoLightValueScratchXZNP) / 4.0F;
			var23 = (var21 + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
			var24 = (this.aoLightValueScratchXZNN + var21 + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
			var25 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + var21) / 4.0F;
			var9 = (float)((double)var23 * this.renderMaxY * this.renderMaxZ + (double)var24 * this.renderMaxY * (1.0D - this.renderMaxZ) + (double)var25 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ) + (double)var22 * (1.0D - this.renderMaxY) * this.renderMaxZ);
			var10 = (float)((double)var23 * this.renderMaxY * this.renderMinZ + (double)var24 * this.renderMaxY * (1.0D - this.renderMinZ) + (double)var25 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ) + (double)var22 * (1.0D - this.renderMaxY) * this.renderMinZ);
			var11 = (float)((double)var23 * this.renderMinY * this.renderMinZ + (double)var24 * this.renderMinY * (1.0D - this.renderMinZ) + (double)var25 * (1.0D - this.renderMinY) * (1.0D - this.renderMinZ) + (double)var22 * (1.0D - this.renderMinY) * this.renderMinZ);
			var12 = (float)((double)var23 * this.renderMinY * this.renderMaxZ + (double)var24 * this.renderMinY * (1.0D - this.renderMaxZ) + (double)var25 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ) + (double)var22 * (1.0D - this.renderMinY) * this.renderMaxZ);
			var26 = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, var20);
			var27 = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, var20);
			var28 = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, var20);
			var29 = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, var20);
			this.brightnessTopLeft = this.mixAoBrightness(var27, var28, var29, var26, this.renderMaxY * this.renderMaxZ, this.renderMaxY * (1.0D - this.renderMaxZ), (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ), (1.0D - this.renderMaxY) * this.renderMaxZ);
			this.brightnessBottomLeft = this.mixAoBrightness(var27, var28, var29, var26, this.renderMaxY * this.renderMinZ, this.renderMaxY * (1.0D - this.renderMinZ), (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ), (1.0D - this.renderMaxY) * this.renderMinZ);
			this.brightnessBottomRight = this.mixAoBrightness(var27, var28, var29, var26, this.renderMinY * this.renderMinZ, this.renderMinY * (1.0D - this.renderMinZ), (1.0D - this.renderMinY) * (1.0D - this.renderMinZ), (1.0D - this.renderMinY) * this.renderMinZ);
			this.brightnessTopRight = this.mixAoBrightness(var27, var28, var29, var26, this.renderMinY * this.renderMaxZ, this.renderMinY * (1.0D - this.renderMaxZ), (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ), (1.0D - this.renderMinY) * this.renderMaxZ);

			if (var13) {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.6F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.6F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.6F;
			} else {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
			}

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
			var30 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4);
			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, var30);

			if (fancyGrass && var30.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
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
				this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var8 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 + 1, par3, par4, 5)) {
			if (this.renderMaxX >= 1.0D) {
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
			var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
			var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
			var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
			var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];

			if (!var17 && !var19) {
				this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
			} else {
				this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
				this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
			}

			if (!var17 && !var18) {
				this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
			} else {
				this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
				this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
			}

			if (!var16 && !var19) {
				this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
			} else {
				this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
				this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
			}

			if (!var16 && !var18) {
				this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
			} else {
				this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
				this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
			}

			if (this.renderMaxX >= 1.0D) {
				--par2;
			}

			var20 = var14;

			if (this.renderMaxX >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4)) {
				var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
			}

			var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
			var22 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + var21 + this.aoLightValueScratchXZPP) / 4.0F;
			var23 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + var21) / 4.0F;
			var24 = (this.aoLightValueScratchXZPN + var21 + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
			var25 = (var21 + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
			var9 = (float)((double)var22 * (1.0D - this.renderMinY) * this.renderMaxZ + (double)var23 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ) + (double)var24 * this.renderMinY * (1.0D - this.renderMaxZ) + (double)var25 * this.renderMinY * this.renderMaxZ);
			var10 = (float)((double)var22 * (1.0D - this.renderMinY) * this.renderMinZ + (double)var23 * (1.0D - this.renderMinY) * (1.0D - this.renderMinZ) + (double)var24 * this.renderMinY * (1.0D - this.renderMinZ) + (double)var25 * this.renderMinY * this.renderMinZ);
			var11 = (float)((double)var22 * (1.0D - this.renderMaxY) * this.renderMinZ + (double)var23 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ) + (double)var24 * this.renderMaxY * (1.0D - this.renderMinZ) + (double)var25 * this.renderMaxY * this.renderMinZ);
			var12 = (float)((double)var22 * (1.0D - this.renderMaxY) * this.renderMaxZ + (double)var23 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ) + (double)var24 * this.renderMaxY * (1.0D - this.renderMaxZ) + (double)var25 * this.renderMaxY * this.renderMaxZ);
			var26 = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var20);
			var27 = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, var20);
			var28 = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, var20);
			var29 = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, var20);
			this.brightnessTopLeft = this.mixAoBrightness(var26, var29, var28, var27, (1.0D - this.renderMinY) * this.renderMaxZ, (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ), this.renderMinY * (1.0D - this.renderMaxZ), this.renderMinY * this.renderMaxZ);
			this.brightnessBottomLeft = this.mixAoBrightness(var26, var29, var28, var27, (1.0D - this.renderMinY) * this.renderMinZ, (1.0D - this.renderMinY) * (1.0D - this.renderMinZ), this.renderMinY * (1.0D - this.renderMinZ), this.renderMinY * this.renderMinZ);
			this.brightnessBottomRight = this.mixAoBrightness(var26, var29, var28, var27, (1.0D - this.renderMaxY) * this.renderMinZ, (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ), this.renderMaxY * (1.0D - this.renderMinZ), this.renderMaxY * this.renderMinZ);
			this.brightnessTopRight = this.mixAoBrightness(var26, var29, var28, var27, (1.0D - this.renderMaxY) * this.renderMaxZ, (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ), this.renderMaxY * (1.0D - this.renderMaxZ), this.renderMaxY * this.renderMaxZ);

			if (var13) {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.6F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.6F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.6F;
			} else {
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
			}

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
			var30 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5);
			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, var30);

			if (fancyGrass && var30.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
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
				this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var8 = true;
		}

		this.enableAO = false;
		return var8;
	}

	/**
	 * Get ambient occlusion brightness
	 */
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

	private int mixAoBrightness(int par1, int par2, int par3, int par4, double par5, double par7, double par9, double par11) {
		int var13 = (int)((double)(par1 >> 16 & 255) * par5 + (double)(par2 >> 16 & 255) * par7 + (double)(par3 >> 16 & 255) * par9 + (double)(par4 >> 16 & 255) * par11) & 255;
		int var14 = (int)((double)(par1 & 255) * par5 + (double)(par2 & 255) * par7 + (double)(par3 & 255) * par9 + (double)(par4 & 255) * par11) & 255;
		return var13 << 16 | var14;
	}

	/**
	 * Renders a standard cube block at the given coordinates, with a given color ratio.  Args: block, x, y, z, r, g, b
	 */
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

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 - 1, par4, 0)) {
			var8.setBrightness(this.renderMinY > 0.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
			var8.setColorOpaque_F(var17, var20, var23);
			this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
			var9 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 + 1, par4, 1)) {
			var8.setBrightness(this.renderMaxY < 1.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
			var8.setColorOpaque_F(var14, var15, var16);
			this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
			var9 = true;
		}

		Icon var28;

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 - 1, 2)) {
			var8.setBrightness(this.renderMinZ > 0.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));

			if (CTMUtils.isBetterGrass(this.blockAccess, par1Block, par2, par3, par4, 2)) {
				var8.setColorOpaque_F(var18 * par5, var21 * par6, var24 * par7);
			} else {
				var8.setColorOpaque_F(var18, var21, var24);
			}

			var28 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2);
			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, var28);

			if (fancyGrass && var28.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
				var8.setColorOpaque_F(var18 * par5, var21 * par6, var24 * par7);
				this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var9 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 + 1, 3)) {
			var8.setBrightness(this.renderMaxZ < 1.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));

			if (CTMUtils.isBetterGrass(this.blockAccess, par1Block, par2, par3, par4, 3)) {
				var8.setColorOpaque_F(var18 * par5, var21 * par6, var24 * par7);
			} else {
				var8.setColorOpaque_F(var18, var21, var24);
			}

			var28 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3);
			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, var28);

			if (fancyGrass && var28.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
				var8.setColorOpaque_F(var18 * par5, var21 * par6, var24 * par7);
				this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var9 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 - 1, par3, par4, 4)) {
			var8.setBrightness(this.renderMinX > 0.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));

			if (CTMUtils.isBetterGrass(this.blockAccess, par1Block, par2, par3, par4, 4)) {
				var8.setColorOpaque_F(var19 * par5, var22 * par6, var25 * par7);
			} else {
				var8.setColorOpaque_F(var19, var22, var25);
			}

			var28 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4);
			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, var28);

			if (fancyGrass && var28.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
				var8.setColorOpaque_F(var19 * par5, var22 * par6, var25 * par7);
				this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var9 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 + 1, par3, par4, 5)) {
			var8.setBrightness(this.renderMaxX < 1.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));

			if (CTMUtils.isBetterGrass(this.blockAccess, par1Block, par2, par3, par4, 5)) {
				var8.setColorOpaque_F(var19 * par5, var22 * par6, var25 * par7);
			} else {
				var8.setColorOpaque_F(var19, var22, var25);
			}

			var28 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5);
			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, var28);

			if (fancyGrass && var28.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture()) {
				var8.setColorOpaque_F(var19 * par5, var22 * par6, var25 * par7);
				this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.getIconSideOverlay());
			}

			var9 = true;
		}

		return var9;
	}

	/**
	 * Renders a Cocoa block at the given coordinates
	 */
	private boolean renderBlockCocoa(BlockCocoa par1BlockCocoa, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		var5.setBrightness(par1BlockCocoa.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		int var7 = BlockDirectional.getDirection(var6);
		int var8 = BlockCocoa.func_72219_c(var6);
		Icon var9 = par1BlockCocoa.func_94468_i_(var8);
		int var10 = 4 + var8 * 2;
		int var11 = 5 + var8 * 2;
		double var12 = 15.0D - (double)var10;
		double var14 = 15.0D;
		double var16 = 4.0D;
		double var18 = 4.0D + (double)var11;
		double var20 = (double)var9.getInterpolatedU(var12);
		double var22 = (double)var9.getInterpolatedU(var14);
		double var24 = (double)var9.getInterpolatedV(var16);
		double var26 = (double)var9.getInterpolatedV(var18);
		double var28 = 0.0D;
		double var30 = 0.0D;

		switch (var7) {
			case 0:
				var28 = 8.0D - (double)(var10 / 2);
				var30 = 15.0D - (double)var10;
				break;

			case 1:
				var28 = 1.0D;
				var30 = 8.0D - (double)(var10 / 2);
				break;

			case 2:
				var28 = 8.0D - (double)(var10 / 2);
				var30 = 1.0D;
				break;

			case 3:
				var28 = 15.0D - (double)var10;
				var30 = 8.0D - (double)(var10 / 2);
		}

		double var32 = (double)par2 + var28 / 16.0D;
		double var34 = (double)par2 + (var28 + (double)var10) / 16.0D;
		double var36 = (double)par3 + (12.0D - (double)var11) / 16.0D;
		double var38 = (double)par3 + 0.75D;
		double var40 = (double)par4 + var30 / 16.0D;
		double var42 = (double)par4 + (var30 + (double)var10) / 16.0D;
		var5.addVertexWithUV(var32, var36, var40, var20, var26);
		var5.addVertexWithUV(var32, var36, var42, var22, var26);
		var5.addVertexWithUV(var32, var38, var42, var22, var24);
		var5.addVertexWithUV(var32, var38, var40, var20, var24);
		var5.addVertexWithUV(var34, var36, var42, var20, var26);
		var5.addVertexWithUV(var34, var36, var40, var22, var26);
		var5.addVertexWithUV(var34, var38, var40, var22, var24);
		var5.addVertexWithUV(var34, var38, var42, var20, var24);
		var5.addVertexWithUV(var34, var36, var40, var20, var26);
		var5.addVertexWithUV(var32, var36, var40, var22, var26);
		var5.addVertexWithUV(var32, var38, var40, var22, var24);
		var5.addVertexWithUV(var34, var38, var40, var20, var24);
		var5.addVertexWithUV(var32, var36, var42, var20, var26);
		var5.addVertexWithUV(var34, var36, var42, var22, var26);
		var5.addVertexWithUV(var34, var38, var42, var22, var24);
		var5.addVertexWithUV(var32, var38, var42, var20, var24);
		int var44 = var10;

		if (var8 >= 2) {
			var44 = var10 - 1;
		}

		var9 = CTMUtils.getTile(this, par1BlockCocoa, par2, par3, par4, var9, var5);

		if (var9 == null) {
			return false;
		} else {
			var5 = CTMUtils.getTessellator(var9);
			var20 = (double)var9.getMinU();
			var22 = (double)var9.getInterpolatedU((double)var44);
			var24 = (double)var9.getMinV();
			var26 = (double)var9.getInterpolatedV((double)var44);
			var5.addVertexWithUV(var32, var38, var42, var20, var26);
			var5.addVertexWithUV(var34, var38, var42, var22, var26);
			var5.addVertexWithUV(var34, var38, var40, var22, var24);
			var5.addVertexWithUV(var32, var38, var40, var20, var24);
			var5.addVertexWithUV(var32, var36, var40, var20, var24);
			var5.addVertexWithUV(var34, var36, var40, var22, var24);
			var5.addVertexWithUV(var34, var36, var42, var22, var26);
			var5.addVertexWithUV(var32, var36, var42, var20, var26);
			var20 = (double)var9.getInterpolatedU(12.0D);
			var22 = (double)var9.getMaxU();
			var24 = (double)var9.getMinV();
			var26 = (double)var9.getInterpolatedV(4.0D);
			var28 = 8.0D;
			var30 = 0.0D;
			double var45;

			switch (var7) {
				case 0:
					var28 = 8.0D;
					var30 = 12.0D;
					var45 = var20;
					var20 = var22;
					var22 = var45;
					break;

				case 1:
					var28 = 0.0D;
					var30 = 8.0D;
					break;

				case 2:
					var28 = 8.0D;
					var30 = 0.0D;
					break;

				case 3:
					var28 = 12.0D;
					var30 = 8.0D;
					var45 = var20;
					var20 = var22;
					var22 = var45;
			}

			var32 = (double)par2 + var28 / 16.0D;
			var34 = (double)par2 + (var28 + 4.0D) / 16.0D;
			var36 = (double)par3 + 0.75D;
			var38 = (double)par3 + 1.0D;
			var40 = (double)par4 + var30 / 16.0D;
			var42 = (double)par4 + (var30 + 4.0D) / 16.0D;

			if (var7 != 2 && var7 != 0) {
				if (var7 == 1 || var7 == 3) {
					var5.addVertexWithUV(var34, var36, var40, var20, var26);
					var5.addVertexWithUV(var32, var36, var40, var22, var26);
					var5.addVertexWithUV(var32, var38, var40, var22, var24);
					var5.addVertexWithUV(var34, var38, var40, var20, var24);
					var5.addVertexWithUV(var32, var36, var40, var22, var26);
					var5.addVertexWithUV(var34, var36, var40, var20, var26);
					var5.addVertexWithUV(var34, var38, var40, var20, var24);
					var5.addVertexWithUV(var32, var38, var40, var22, var24);
				}
			} else {
				var5.addVertexWithUV(var32, var36, var40, var22, var26);
				var5.addVertexWithUV(var32, var36, var42, var20, var26);
				var5.addVertexWithUV(var32, var38, var42, var20, var24);
				var5.addVertexWithUV(var32, var38, var40, var22, var24);
				var5.addVertexWithUV(var32, var36, var42, var20, var26);
				var5.addVertexWithUV(var32, var36, var40, var22, var26);
				var5.addVertexWithUV(var32, var38, var40, var22, var24);
				var5.addVertexWithUV(var32, var38, var42, var20, var24);
			}

			return true;
		}
	}

	/**
	 * Renders beacon block
	 */
	private boolean renderBlockBeacon(BlockBeacon par1BlockBeacon, int par2, int par3, int par4) {
		float var5 = 0.1875F;
		this.setOverrideBlockTexture(this.getBlockIcon(Block.obsidian));
		this.setRenderBounds(0.125D, 0.0062500000931322575D, 0.125D, 0.875D, (double)var5, 0.875D);
		this.renderStandardBlock(par1BlockBeacon, par2, par3, par4);
		this.setOverrideBlockTexture(this.getBlockIcon(Block.glass));
		this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		this.renderStandardBlock(par1BlockBeacon, par2, par3, par4);
		this.setOverrideBlockTexture(par1BlockBeacon.func_94446_i());
		this.setRenderBounds(0.1875D, (double)var5, 0.1875D, 0.8125D, 0.875D, 0.8125D);
		this.renderStandardBlock(par1BlockBeacon, par2, par3, par4);
		this.clearOverrideBlockTexture();
		return true;
	}

	/**
	 * Renders a cactus block at the given coordinates
	 */
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

	/**
	 * Render block cactus implementation
	 */
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

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 - 1, par4, 0)) {
			var8.setBrightness(this.renderMinY > 0.0D ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
			var8.setColorOpaque_F(var14, var18, var22);
			this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
			var9 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 + 1, par4, 1)) {
			var8.setBrightness(this.renderMaxY < 1.0D ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
			var8.setColorOpaque_F(var15, var19, var23);
			this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
			var9 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 - 1, 2)) {
			var8.setBrightness(this.renderMinZ > 0.0D ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
			var8.setColorOpaque_F(var16, var20, var24);
			var8.addTranslation(0.0F, 0.0F, var26);
			this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2));
			var8.addTranslation(0.0F, 0.0F, -var26);
			var9 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 + 1, 3)) {
			var8.setBrightness(this.renderMaxZ < 1.0D ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
			var8.setColorOpaque_F(var16, var20, var24);
			var8.addTranslation(0.0F, 0.0F, -var26);
			this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));
			var8.addTranslation(0.0F, 0.0F, var26);
			var9 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 - 1, par3, par4, 4)) {
			var8.setBrightness(this.renderMinX > 0.0D ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
			var8.setColorOpaque_F(var17, var21, var25);
			var8.addTranslation(var26, 0.0F, 0.0F);
			this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4));
			var8.addTranslation(-var26, 0.0F, 0.0F);
			var9 = true;
		}

		if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 + 1, par3, par4, 5)) {
			var8.setBrightness(this.renderMaxX < 1.0D ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
			var8.setColorOpaque_F(var17, var21, var25);
			var8.addTranslation(-var26, 0.0F, 0.0F);
			this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5));
			var8.addTranslation(var26, 0.0F, 0.0F);
			var9 = true;
		}

		return var9;
	}

	public boolean renderBlockFence(BlockFence par1BlockFence, int par2, int par3, int par4) {
		boolean var5 = false;
		float var6 = 0.375F;
		float var7 = 0.625F;
		this.setRenderBounds((double)var6, 0.0D, (double)var6, (double)var7, 1.0D, (double)var7);
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
		float var16 = var10 ? 0.0F : var6;
		float var17 = var11 ? 1.0F : var7;
		float var18 = var12 ? 0.0F : var6;
		float var19 = var13 ? 1.0F : var7;

		if (var8) {
			this.setRenderBounds((double)var16, (double)var14, (double)var6, (double)var17, (double)var15, (double)var7);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			var5 = true;
		}

		if (var9) {
			this.setRenderBounds((double)var6, (double)var14, (double)var18, (double)var7, (double)var15, (double)var19);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			var5 = true;
		}

		var14 = 0.375F;
		var15 = 0.5625F;

		if (var8) {
			this.setRenderBounds((double)var16, (double)var14, (double)var6, (double)var17, (double)var15, (double)var7);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			var5 = true;
		}

		if (var9) {
			this.setRenderBounds((double)var6, (double)var14, (double)var18, (double)var7, (double)var15, (double)var19);
			this.renderStandardBlock(par1BlockFence, par2, par3, par4);
			var5 = true;
		}

		par1BlockFence.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
		return var5;
	}

	/**
	 * Renders wall block
	 */
	public boolean renderBlockWall(BlockWall par1BlockWall, int par2, int par3, int par4) {
		boolean var5 = par1BlockWall.canConnectWallTo(this.blockAccess, par2 - 1, par3, par4);
		boolean var6 = par1BlockWall.canConnectWallTo(this.blockAccess, par2 + 1, par3, par4);
		boolean var7 = par1BlockWall.canConnectWallTo(this.blockAccess, par2, par3, par4 - 1);
		boolean var8 = par1BlockWall.canConnectWallTo(this.blockAccess, par2, par3, par4 + 1);
		boolean var9 = var7 && var8 && !var5 && !var6;
		boolean var10 = !var7 && !var8 && var5 && var6;
		boolean var11 = this.blockAccess.isAirBlock(par2, par3 + 1, par4);

		if ((var9 || var10) && var11) {
			if (var9) {
				this.setRenderBounds(0.3125D, 0.0D, 0.0D, 0.6875D, 0.8125D, 1.0D);
				this.renderStandardBlock(par1BlockWall, par2, par3, par4);
			} else {
				this.setRenderBounds(0.0D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
				this.renderStandardBlock(par1BlockWall, par2, par3, par4);
			}
		} else {
			this.setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
			this.renderStandardBlock(par1BlockWall, par2, par3, par4);

			if (var5) {
				this.setRenderBounds(0.0D, 0.0D, 0.3125D, 0.25D, 0.8125D, 0.6875D);
				this.renderStandardBlock(par1BlockWall, par2, par3, par4);
			}

			if (var6) {
				this.setRenderBounds(0.75D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
				this.renderStandardBlock(par1BlockWall, par2, par3, par4);
			}

			if (var7) {
				this.setRenderBounds(0.3125D, 0.0D, 0.0D, 0.6875D, 0.8125D, 0.25D);
				this.renderStandardBlock(par1BlockWall, par2, par3, par4);
			}

			if (var8) {
				this.setRenderBounds(0.3125D, 0.0D, 0.75D, 0.6875D, 0.8125D, 1.0D);
				this.renderStandardBlock(par1BlockWall, par2, par3, par4);
			}
		}

		par1BlockWall.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
		return true;
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
			this.setRenderBounds((double)(0.5F - var10), (double)var12, (double)(0.5F - var10), (double)(0.5F + var10), (double)var11, (double)(0.5F + var10));
			this.renderStandardBlock(par1BlockDragonEgg, par2, par3, par4);
		}

		var5 = true;
		this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		return var5;
	}

	/**
	 * Render block fence gate
	 */
	public boolean renderBlockFenceGate(BlockFenceGate par1BlockFenceGate, int par2, int par3, int par4) {
		boolean var5 = true;
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
		boolean var7 = BlockFenceGate.isFenceGateOpen(var6);
		int var8 = BlockDirectional.getDirection(var6);
		float var9 = 0.375F;
		float var10 = 0.5625F;
		float var11 = 0.75F;
		float var12 = 0.9375F;
		float var13 = 0.3125F;
		float var14 = 1.0F;

		if ((var8 == 2 || var8 == 0) && this.blockAccess.getBlockId(par2 - 1, par3, par4) == Block.cobblestoneWall.blockID && this.blockAccess.getBlockId(par2 + 1, par3, par4) == Block.cobblestoneWall.blockID || (var8 == 3 || var8 == 1) && this.blockAccess.getBlockId(par2, par3, par4 - 1) == Block.cobblestoneWall.blockID && this.blockAccess.getBlockId(par2, par3, par4 + 1) == Block.cobblestoneWall.blockID) {
			var9 -= 0.1875F;
			var10 -= 0.1875F;
			var11 -= 0.1875F;
			var12 -= 0.1875F;
			var13 -= 0.1875F;
			var14 -= 0.1875F;
		}

		this.renderAllFaces = true;
		float var15;
		float var17;
		float var16;
		float var18;

		if (var8 != 3 && var8 != 1) {
			var15 = 0.0F;
			var16 = 0.125F;
			var17 = 0.4375F;
			var18 = 0.5625F;
			this.setRenderBounds((double)var15, (double)var13, (double)var17, (double)var16, (double)var14, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			var15 = 0.875F;
			var16 = 1.0F;
			this.setRenderBounds((double)var15, (double)var13, (double)var17, (double)var16, (double)var14, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		} else {
			this.uvRotateTop = 1;
			var15 = 0.4375F;
			var16 = 0.5625F;
			var17 = 0.0F;
			var18 = 0.125F;
			this.setRenderBounds((double)var15, (double)var13, (double)var17, (double)var16, (double)var14, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			var17 = 0.875F;
			var18 = 1.0F;
			this.setRenderBounds((double)var15, (double)var13, (double)var17, (double)var16, (double)var14, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			this.uvRotateTop = 0;
		}

		if (var7) {
			if (var8 == 2 || var8 == 0) {
				this.uvRotateTop = 1;
			}

			if (var8 == 3) {
				this.setRenderBounds(0.8125D, (double)var9, 0.0D, 0.9375D, (double)var12, 0.125D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.8125D, (double)var9, 0.875D, 0.9375D, (double)var12, 1.0D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.5625D, (double)var9, 0.0D, 0.8125D, (double)var10, 0.125D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.5625D, (double)var9, 0.875D, 0.8125D, (double)var10, 1.0D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.5625D, (double)var11, 0.0D, 0.8125D, (double)var12, 0.125D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.5625D, (double)var11, 0.875D, 0.8125D, (double)var12, 1.0D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			} else if (var8 == 1) {
				this.setRenderBounds(0.0625D, (double)var9, 0.0D, 0.1875D, (double)var12, 0.125D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.0625D, (double)var9, 0.875D, 0.1875D, (double)var12, 1.0D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.1875D, (double)var9, 0.0D, 0.4375D, (double)var10, 0.125D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.1875D, (double)var9, 0.875D, 0.4375D, (double)var10, 1.0D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.1875D, (double)var11, 0.0D, 0.4375D, (double)var12, 0.125D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.1875D, (double)var11, 0.875D, 0.4375D, (double)var12, 1.0D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			} else if (var8 == 0) {
				this.setRenderBounds(0.0D, (double)var9, 0.8125D, 0.125D, (double)var12, 0.9375D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.875D, (double)var9, 0.8125D, 1.0D, (double)var12, 0.9375D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.0D, (double)var9, 0.5625D, 0.125D, (double)var10, 0.8125D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.875D, (double)var9, 0.5625D, 1.0D, (double)var10, 0.8125D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.0D, (double)var11, 0.5625D, 0.125D, (double)var12, 0.8125D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.875D, (double)var11, 0.5625D, 1.0D, (double)var12, 0.8125D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			} else if (var8 == 2) {
				this.setRenderBounds(0.0D, (double)var9, 0.0625D, 0.125D, (double)var12, 0.1875D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.875D, (double)var9, 0.0625D, 1.0D, (double)var12, 0.1875D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.0D, (double)var9, 0.1875D, 0.125D, (double)var10, 0.4375D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.875D, (double)var9, 0.1875D, 1.0D, (double)var10, 0.4375D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.0D, (double)var11, 0.1875D, 0.125D, (double)var12, 0.4375D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
				this.setRenderBounds(0.875D, (double)var11, 0.1875D, 1.0D, (double)var12, 0.4375D);
				this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			}
		} else if (var8 != 3 && var8 != 1) {
			var15 = 0.375F;
			var16 = 0.5F;
			var17 = 0.4375F;
			var18 = 0.5625F;
			this.setRenderBounds((double)var15, (double)var9, (double)var17, (double)var16, (double)var12, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			var15 = 0.5F;
			var16 = 0.625F;
			this.setRenderBounds((double)var15, (double)var9, (double)var17, (double)var16, (double)var12, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			var15 = 0.625F;
			var16 = 0.875F;
			this.setRenderBounds((double)var15, (double)var9, (double)var17, (double)var16, (double)var10, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			this.setRenderBounds((double)var15, (double)var11, (double)var17, (double)var16, (double)var12, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			var15 = 0.125F;
			var16 = 0.375F;
			this.setRenderBounds((double)var15, (double)var9, (double)var17, (double)var16, (double)var10, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			this.setRenderBounds((double)var15, (double)var11, (double)var17, (double)var16, (double)var12, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		} else {
			this.uvRotateTop = 1;
			var15 = 0.4375F;
			var16 = 0.5625F;
			var17 = 0.375F;
			var18 = 0.5F;
			this.setRenderBounds((double)var15, (double)var9, (double)var17, (double)var16, (double)var12, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			var17 = 0.5F;
			var18 = 0.625F;
			this.setRenderBounds((double)var15, (double)var9, (double)var17, (double)var16, (double)var12, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			var17 = 0.625F;
			var18 = 0.875F;
			this.setRenderBounds((double)var15, (double)var9, (double)var17, (double)var16, (double)var10, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			this.setRenderBounds((double)var15, (double)var11, (double)var17, (double)var16, (double)var12, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			var17 = 0.125F;
			var18 = 0.375F;
			this.setRenderBounds((double)var15, (double)var9, (double)var17, (double)var16, (double)var10, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
			this.setRenderBounds((double)var15, (double)var11, (double)var17, (double)var16, (double)var12, (double)var18);
			this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
		}

		this.renderAllFaces = false;
		this.uvRotateTop = 0;
		this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		return var5;
	}

	private boolean renderBlockHopper(BlockHopper par1BlockHopper, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		var5.setBrightness(par1BlockHopper.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
		float var6 = 1.0F;
		int var7 = par1BlockHopper.colorMultiplier(this.blockAccess, par2, par3, par4);
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
		return this.renderBlockHopperMetadata(par1BlockHopper, par2, par3, par4, this.blockAccess.getBlockMetadata(par2, par3, par4), false);
	}

	private boolean renderBlockHopperMetadata(BlockHopper par1BlockHopper, int par2, int par3, int par4, int par5, boolean par6) {
		Tessellator var7 = Tessellator.instance;
		int var8 = BlockHopper.func_94451_c(par5);
		double var9 = 0.625D;
		this.setRenderBounds(0.0D, var9, 0.0D, 1.0D, 1.0D, 1.0D);

		if (par6) {
			var7.startDrawingQuads();
			var7.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockHopper, 0, par5));
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockHopper, 1, par5));
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockHopper, 2, par5));
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockHopper, 3, par5));
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockHopper, 4, par5));
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, this.getBlockIconFromSideAndMetadata(par1BlockHopper, 5, par5));
			var7.draw();
		} else {
			this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
		}

		float var13;

		if (!par6) {
			var7.setBrightness(par1BlockHopper.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
			float var11 = 1.0F;
			int var12 = par1BlockHopper.colorMultiplier(this.blockAccess, par2, par3, par4);
			var13 = (float)(var12 >> 16 & 255) / 255.0F;
			float var14 = (float)(var12 >> 8 & 255) / 255.0F;
			float var15 = (float)(var12 & 255) / 255.0F;

			if (EntityRenderer.anaglyphEnable) {
				float var16 = (var13 * 30.0F + var14 * 59.0F + var15 * 11.0F) / 100.0F;
				float var17 = (var13 * 30.0F + var14 * 70.0F) / 100.0F;
				float var18 = (var13 * 30.0F + var15 * 70.0F) / 100.0F;
				var13 = var16;
				var14 = var17;
				var15 = var18;
			}

			var7.setColorOpaque_F(var11 * var13, var11 * var14, var11 * var15);
		}

		Icon var24 = BlockHopper.func_94453_b("hopper");
		Icon var25 = BlockHopper.func_94453_b("hopper_inside");
		var13 = 0.125F;

		if (par6) {
			var7.startDrawingQuads();
			var7.setNormal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(par1BlockHopper, (double)(-1.0F + var13), 0.0D, 0.0D, var24);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(par1BlockHopper, (double)(1.0F - var13), 0.0D, 0.0D, var24);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(par1BlockHopper, 0.0D, 0.0D, (double)(-1.0F + var13), var24);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(par1BlockHopper, 0.0D, 0.0D, (double)(1.0F - var13), var24);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(par1BlockHopper, 0.0D, -1.0D + var9, 0.0D, var25);
			var7.draw();
		} else {
			this.renderSouthFace(par1BlockHopper, (double)((float)par2 - 1.0F + var13), (double)par3, (double)par4, var24);
			this.renderNorthFace(par1BlockHopper, (double)((float)par2 + 1.0F - var13), (double)par3, (double)par4, var24);
			this.renderWestFace(par1BlockHopper, (double)par2, (double)par3, (double)((float)par4 - 1.0F + var13), var24);
			this.renderEastFace(par1BlockHopper, (double)par2, (double)par3, (double)((float)par4 + 1.0F - var13), var24);
			this.renderTopFace(par1BlockHopper, (double)par2, (double)((float)par3 - 1.0F) + var9, (double)par4, var25);
		}

		this.setOverrideBlockTexture(var24);
		double var26 = 0.25D;
		double var27 = 0.25D;
		this.setRenderBounds(var26, var27, var26, 1.0D - var26, var9 - 0.002D, 1.0D - var26);

		if (par6) {
			var7.startDrawingQuads();
			var7.setNormal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, var24);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, var24);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, var24);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, var24);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, var24);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(par1BlockHopper, 0.0D, 0.0D, 0.0D, var24);
			var7.draw();
		} else {
			this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
		}

		if (!par6) {
			double var20 = 0.375D;
			double var22 = 0.25D;
			this.setOverrideBlockTexture(var24);

			if (var8 == 0) {
				this.setRenderBounds(var20, 0.0D, var20, 1.0D - var20, 0.25D, 1.0D - var20);
				this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
			}

			if (var8 == 2) {
				this.setRenderBounds(var20, var27, 0.0D, 1.0D - var20, var27 + var22, var26);
				this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
			}

			if (var8 == 3) {
				this.setRenderBounds(var20, var27, 1.0D - var26, 1.0D - var20, var27 + var22, 1.0D);
				this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
			}

			if (var8 == 4) {
				this.setRenderBounds(0.0D, var27, var20, var26, var27 + var22, 1.0D - var20);
				this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
			}

			if (var8 == 5) {
				this.setRenderBounds(1.0D - var26, var27, var20, 1.0D, var27 + var22, 1.0D - var20);
				this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
			}
		}

		this.clearOverrideBlockTexture();
		return true;
	}

	/**
	 * Renders a stair block at the given coordinates
	 */
	public boolean renderBlockStairs(BlockStairs par1BlockStairs, int par2, int par3, int par4) {
		par1BlockStairs.func_82541_d(this.blockAccess, par2, par3, par4);
		this.setRenderBoundsFromBlock(par1BlockStairs);
		this.renderStandardBlock(par1BlockStairs, par2, par3, par4);
		boolean var5 = par1BlockStairs.func_82542_g(this.blockAccess, par2, par3, par4);
		this.setRenderBoundsFromBlock(par1BlockStairs);
		this.renderStandardBlock(par1BlockStairs, par2, par3, par4);

		if (var5 && par1BlockStairs.func_82544_h(this.blockAccess, par2, par3, par4)) {
			this.setRenderBoundsFromBlock(par1BlockStairs);
			this.renderStandardBlock(par1BlockStairs, par2, par3, par4);
		}

		return true;
	}

	/**
	 * Renders a door block at the given coordinates
	 */
	public boolean renderBlockDoor(Block par1Block, int par2, int par3, int par4) {
		Tessellator var5 = Tessellator.instance;
		int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);

		if ((var6 & 8) != 0) {
			if (this.blockAccess.getBlockId(par2, par3 - 1, par4) != par1Block.blockID) {
				return false;
			}
		} else if (this.blockAccess.getBlockId(par2, par3 + 1, par4) != par1Block.blockID) {
			return false;
		}

		boolean var7 = false;
		float var8 = 0.5F;
		float var9 = 1.0F;
		float var10 = 0.8F;
		float var11 = 0.6F;
		int var12 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
		var5.setBrightness(this.renderMinY > 0.0D ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
		var5.setColorOpaque_F(var8, var8, var8);
		this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
		var7 = true;
		var5.setBrightness(this.renderMaxY < 1.0D ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
		var5.setColorOpaque_F(var9, var9, var9);
		this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
		var7 = true;
		var5.setBrightness(this.renderMinZ > 0.0D ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
		var5.setColorOpaque_F(var10, var10, var10);
		Icon var14 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2);
		this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, var14);
		var7 = true;
		this.flipTexture = false;
		var5.setBrightness(this.renderMaxZ < 1.0D ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
		var5.setColorOpaque_F(var10, var10, var10);
		var14 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3);
		this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, var14);
		var7 = true;
		this.flipTexture = false;
		var5.setBrightness(this.renderMinX > 0.0D ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
		var5.setColorOpaque_F(var11, var11, var11);
		var14 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4);
		this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, var14);
		var7 = true;
		this.flipTexture = false;
		var5.setBrightness(this.renderMaxX < 1.0D ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
		var5.setColorOpaque_F(var11, var11, var11);
		var14 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5);
		this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, var14);
		var7 = true;
		this.flipTexture = false;
		return var7;
	}

	/**
	 * Renders the given texture to the bottom face of the block. Args: block, x, y, z, texture
	 */
	public void renderBottomFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
		Tessellator var9 = Tessellator.instance;

		if (this.hasOverrideBlockTexture()) {
			par8Icon = this.overrideBlockTexture;
		}

		par8Icon = CTMUtils.getTile(this, par1Block, (int)par2, (int)par4, (int)par6, 0, par8Icon, var9);

		if (par8Icon != null) {
			var9 = CTMUtils.getTessellator(par8Icon);
			double var10 = (double)par8Icon.getInterpolatedU(this.renderMinX * 16.0D);
			double var12 = (double)par8Icon.getInterpolatedU(this.renderMaxX * 16.0D);
			double var14 = (double)par8Icon.getInterpolatedV(this.renderMinZ * 16.0D);
			double var16 = (double)par8Icon.getInterpolatedV(this.renderMaxZ * 16.0D);

			if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
				var10 = (double)par8Icon.getMinU();
				var12 = (double)par8Icon.getMaxU();
			}

			if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
				var14 = (double)par8Icon.getMinV();
				var16 = (double)par8Icon.getMaxV();
			}

			double var18 = var12;
			double var20 = var10;
			double var22 = var14;
			double var24 = var16;

			if (this.uvRotateBottom == 2) {
				var10 = (double)par8Icon.getInterpolatedU(this.renderMinZ * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(this.renderMaxZ * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinX * 16.0D);
				var22 = var14;
				var24 = var16;
				var18 = var10;
				var20 = var12;
				var14 = var16;
				var16 = var22;
			} else if (this.uvRotateBottom == 1) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(this.renderMinX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(this.renderMaxX * 16.0D);
				var18 = var12;
				var20 = var10;
				var10 = var12;
				var12 = var20;
				var22 = var16;
				var24 = var14;
			} else if (this.uvRotateBottom == 3) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
				var18 = var12;
				var20 = var10;
				var22 = var14;
				var24 = var16;
			}

			double var26 = par2 + this.renderMinX;
			double var28 = par2 + this.renderMaxX;
			double var30 = par4 + this.renderMinY;
			double var32 = par6 + this.renderMinZ;
			double var34 = par6 + this.renderMaxZ;

			if (this.enableAO) {
				var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
				var9.setBrightness(this.brightnessTopLeft);
				var9.addVertexWithUV(var26, var30, var34, var20, var24);
				var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
				var9.setBrightness(this.brightnessBottomLeft);
				var9.addVertexWithUV(var26, var30, var32, var10, var14);
				var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
				var9.setBrightness(this.brightnessBottomRight);
				var9.addVertexWithUV(var28, var30, var32, var18, var22);
				var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
				var9.setBrightness(this.brightnessTopRight);
				var9.addVertexWithUV(var28, var30, var34, var12, var16);
			} else {
				var9.addVertexWithUV(var26, var30, var34, var20, var24);
				var9.addVertexWithUV(var26, var30, var32, var10, var14);
				var9.addVertexWithUV(var28, var30, var32, var18, var22);
				var9.addVertexWithUV(var28, var30, var34, var12, var16);
			}
		}
	}

	/**
	 * Renders the given texture to the top face of the block. Args: block, x, y, z, texture
	 */
	public void renderTopFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
		Tessellator var9 = Tessellator.instance;

		if (this.hasOverrideBlockTexture()) {
			par8Icon = this.overrideBlockTexture;
		}

		par8Icon = CTMUtils.getTile(this, par1Block, (int)par2, (int)par4, (int)par6, 1, par8Icon, var9);

		if (par8Icon != null) {
			var9 = CTMUtils.getTessellator(par8Icon);
			double var10 = (double)par8Icon.getInterpolatedU(this.renderMinX * 16.0D);
			double var12 = (double)par8Icon.getInterpolatedU(this.renderMaxX * 16.0D);
			double var14 = (double)par8Icon.getInterpolatedV(this.renderMinZ * 16.0D);
			double var16 = (double)par8Icon.getInterpolatedV(this.renderMaxZ * 16.0D);

			if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
				var10 = (double)par8Icon.getMinU();
				var12 = (double)par8Icon.getMaxU();
			}

			if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
				var14 = (double)par8Icon.getMinV();
				var16 = (double)par8Icon.getMaxV();
			}

			double var18 = var12;
			double var20 = var10;
			double var22 = var14;
			double var24 = var16;

			if (this.uvRotateTop == 1) {
				var10 = (double)par8Icon.getInterpolatedU(this.renderMinZ * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(this.renderMaxZ * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinX * 16.0D);
				var22 = var14;
				var24 = var16;
				var18 = var10;
				var20 = var12;
				var14 = var16;
				var16 = var22;
			} else if (this.uvRotateTop == 2) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(this.renderMinX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(this.renderMaxX * 16.0D);
				var18 = var12;
				var20 = var10;
				var10 = var12;
				var12 = var20;
				var22 = var16;
				var24 = var14;
			} else if (this.uvRotateTop == 3) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
				var18 = var12;
				var20 = var10;
				var22 = var14;
				var24 = var16;
			}

			double var26 = par2 + this.renderMinX;
			double var28 = par2 + this.renderMaxX;
			double var30 = par4 + this.renderMaxY;
			double var32 = par6 + this.renderMinZ;
			double var34 = par6 + this.renderMaxZ;

			if (this.enableAO) {
				var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
				var9.setBrightness(this.brightnessTopLeft);
				var9.addVertexWithUV(var28, var30, var34, var12, var16);
				var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
				var9.setBrightness(this.brightnessBottomLeft);
				var9.addVertexWithUV(var28, var30, var32, var18, var22);
				var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
				var9.setBrightness(this.brightnessBottomRight);
				var9.addVertexWithUV(var26, var30, var32, var10, var14);
				var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
				var9.setBrightness(this.brightnessTopRight);
				var9.addVertexWithUV(var26, var30, var34, var20, var24);
			} else {
				var9.addVertexWithUV(var28, var30, var34, var12, var16);
				var9.addVertexWithUV(var28, var30, var32, var18, var22);
				var9.addVertexWithUV(var26, var30, var32, var10, var14);
				var9.addVertexWithUV(var26, var30, var34, var20, var24);
			}
		}
	}

	/**
	 * Renders the given texture to the east (z-negative) face of the block.  Args: block, x, y, z, texture
	 */
	public void renderEastFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
		Tessellator var9 = Tessellator.instance;

		if (this.hasOverrideBlockTexture()) {
			par8Icon = this.overrideBlockTexture;
		}

		par8Icon = CTMUtils.getTile(this, par1Block, (int)par2, (int)par4, (int)par6, 2, par8Icon, var9);

		if (par8Icon != null) {
			var9 = CTMUtils.getTessellator(par8Icon);
			double var10 = (double)par8Icon.getInterpolatedU(this.renderMinX * 16.0D);
			double var12 = (double)par8Icon.getInterpolatedU(this.renderMaxX * 16.0D);
			double var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
			double var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinY * 16.0D);
			double var18;

			if (this.flipTexture) {
				var18 = var10;
				var10 = var12;
				var12 = var18;
			}

			if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
				var10 = (double)par8Icon.getMinU();
				var12 = (double)par8Icon.getMaxU();
			}

			if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
				var14 = (double)par8Icon.getMinV();
				var16 = (double)par8Icon.getMaxV();
			}

			var18 = var12;
			double var20 = var10;
			double var22 = var14;
			double var24 = var16;

			if (this.uvRotateEast == 2) {
				var10 = (double)par8Icon.getInterpolatedU(this.renderMinY * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(this.renderMaxY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
				var22 = var14;
				var24 = var16;
				var18 = var10;
				var20 = var12;
				var14 = var16;
				var16 = var22;
			} else if (this.uvRotateEast == 1) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(this.renderMaxX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(this.renderMinX * 16.0D);
				var18 = var12;
				var20 = var10;
				var10 = var12;
				var12 = var20;
				var22 = var16;
				var24 = var14;
			} else if (this.uvRotateEast == 3) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(this.renderMaxY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(this.renderMinY * 16.0D);
				var18 = var12;
				var20 = var10;
				var22 = var14;
				var24 = var16;
			}

			double var26 = par2 + this.renderMinX;
			double var28 = par2 + this.renderMaxX;
			double var30 = par4 + this.renderMinY;
			double var32 = par4 + this.renderMaxY;
			double var34 = par6 + this.renderMinZ;

			if (this.enableAO) {
				var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
				var9.setBrightness(this.brightnessTopLeft);
				var9.addVertexWithUV(var26, var32, var34, var18, var22);
				var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
				var9.setBrightness(this.brightnessBottomLeft);
				var9.addVertexWithUV(var28, var32, var34, var10, var14);
				var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
				var9.setBrightness(this.brightnessBottomRight);
				var9.addVertexWithUV(var28, var30, var34, var20, var24);
				var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
				var9.setBrightness(this.brightnessTopRight);
				var9.addVertexWithUV(var26, var30, var34, var12, var16);
			} else {
				var9.addVertexWithUV(var26, var32, var34, var18, var22);
				var9.addVertexWithUV(var28, var32, var34, var10, var14);
				var9.addVertexWithUV(var28, var30, var34, var20, var24);
				var9.addVertexWithUV(var26, var30, var34, var12, var16);
			}
		}
	}

	/**
	 * Renders the given texture to the west (z-positive) face of the block.  Args: block, x, y, z, texture
	 */
	public void renderWestFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
		Tessellator var9 = Tessellator.instance;

		if (this.hasOverrideBlockTexture()) {
			par8Icon = this.overrideBlockTexture;
		}

		par8Icon = CTMUtils.getTile(this, par1Block, (int)par2, (int)par4, (int)par6, 3, par8Icon, var9);

		if (par8Icon != null) {
			var9 = CTMUtils.getTessellator(par8Icon);
			double var10 = (double)par8Icon.getInterpolatedU(this.renderMinX * 16.0D);
			double var12 = (double)par8Icon.getInterpolatedU(this.renderMaxX * 16.0D);
			double var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
			double var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinY * 16.0D);
			double var18;

			if (this.flipTexture) {
				var18 = var10;
				var10 = var12;
				var12 = var18;
			}

			if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
				var10 = (double)par8Icon.getMinU();
				var12 = (double)par8Icon.getMaxU();
			}

			if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
				var14 = (double)par8Icon.getMinV();
				var16 = (double)par8Icon.getMaxV();
			}

			var18 = var12;
			double var20 = var10;
			double var22 = var14;
			double var24 = var16;

			if (this.uvRotateWest == 1) {
				var10 = (double)par8Icon.getInterpolatedU(this.renderMinY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(this.renderMaxY * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
				var22 = var14;
				var24 = var16;
				var18 = var10;
				var20 = var12;
				var14 = var16;
				var16 = var22;
			} else if (this.uvRotateWest == 2) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(this.renderMinX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(this.renderMaxX * 16.0D);
				var18 = var12;
				var20 = var10;
				var10 = var12;
				var12 = var20;
				var22 = var16;
				var24 = var14;
			} else if (this.uvRotateWest == 3) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinX * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(this.renderMaxY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(this.renderMinY * 16.0D);
				var18 = var12;
				var20 = var10;
				var22 = var14;
				var24 = var16;
			}

			double var26 = par2 + this.renderMinX;
			double var28 = par2 + this.renderMaxX;
			double var30 = par4 + this.renderMinY;
			double var32 = par4 + this.renderMaxY;
			double var34 = par6 + this.renderMaxZ;

			if (this.enableAO) {
				var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
				var9.setBrightness(this.brightnessTopLeft);
				var9.addVertexWithUV(var26, var32, var34, var10, var14);
				var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
				var9.setBrightness(this.brightnessBottomLeft);
				var9.addVertexWithUV(var26, var30, var34, var20, var24);
				var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
				var9.setBrightness(this.brightnessBottomRight);
				var9.addVertexWithUV(var28, var30, var34, var12, var16);
				var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
				var9.setBrightness(this.brightnessTopRight);
				var9.addVertexWithUV(var28, var32, var34, var18, var22);
			} else {
				var9.addVertexWithUV(var26, var32, var34, var10, var14);
				var9.addVertexWithUV(var26, var30, var34, var20, var24);
				var9.addVertexWithUV(var28, var30, var34, var12, var16);
				var9.addVertexWithUV(var28, var32, var34, var18, var22);
			}
		}
	}

	/**
	 * Renders the given texture to the north (x-negative) face of the block.  Args: block, x, y, z, texture
	 */
	public void renderNorthFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
		Tessellator var9 = Tessellator.instance;

		if (this.hasOverrideBlockTexture()) {
			par8Icon = this.overrideBlockTexture;
		}

		par8Icon = CTMUtils.getTile(this, par1Block, (int)par2, (int)par4, (int)par6, 4, par8Icon, var9);

		if (par8Icon != null) {
			var9 = CTMUtils.getTessellator(par8Icon);
			double var10 = (double)par8Icon.getInterpolatedU(this.renderMinZ * 16.0D);
			double var12 = (double)par8Icon.getInterpolatedU(this.renderMaxZ * 16.0D);
			double var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
			double var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinY * 16.0D);
			double var18;

			if (this.flipTexture) {
				var18 = var10;
				var10 = var12;
				var12 = var18;
			}

			if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
				var10 = (double)par8Icon.getMinU();
				var12 = (double)par8Icon.getMaxU();
			}

			if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
				var14 = (double)par8Icon.getMinV();
				var16 = (double)par8Icon.getMaxV();
			}

			var18 = var12;
			double var20 = var10;
			double var22 = var14;
			double var24 = var16;

			if (this.uvRotateNorth == 1) {
				var10 = (double)par8Icon.getInterpolatedU(this.renderMinY * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(this.renderMaxY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
				var22 = var14;
				var24 = var16;
				var18 = var10;
				var20 = var12;
				var14 = var16;
				var16 = var22;
			} else if (this.uvRotateNorth == 2) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(this.renderMinZ * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(this.renderMaxZ * 16.0D);
				var18 = var12;
				var20 = var10;
				var10 = var12;
				var12 = var20;
				var22 = var16;
				var24 = var14;
			} else if (this.uvRotateNorth == 3) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(this.renderMaxY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(this.renderMinY * 16.0D);
				var18 = var12;
				var20 = var10;
				var22 = var14;
				var24 = var16;
			}

			double var26 = par2 + this.renderMinX;
			double var28 = par4 + this.renderMinY;
			double var30 = par4 + this.renderMaxY;
			double var32 = par6 + this.renderMinZ;
			double var34 = par6 + this.renderMaxZ;

			if (this.enableAO) {
				var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
				var9.setBrightness(this.brightnessTopLeft);
				var9.addVertexWithUV(var26, var30, var34, var18, var22);
				var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
				var9.setBrightness(this.brightnessBottomLeft);
				var9.addVertexWithUV(var26, var30, var32, var10, var14);
				var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
				var9.setBrightness(this.brightnessBottomRight);
				var9.addVertexWithUV(var26, var28, var32, var20, var24);
				var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
				var9.setBrightness(this.brightnessTopRight);
				var9.addVertexWithUV(var26, var28, var34, var12, var16);
			} else {
				var9.addVertexWithUV(var26, var30, var34, var18, var22);
				var9.addVertexWithUV(var26, var30, var32, var10, var14);
				var9.addVertexWithUV(var26, var28, var32, var20, var24);
				var9.addVertexWithUV(var26, var28, var34, var12, var16);
			}
		}
	}

	/**
	 * Renders the given texture to the south (x-positive) face of the block.  Args: block, x, y, z, texture
	 */
	public void renderSouthFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
		Tessellator var9 = Tessellator.instance;

		if (this.hasOverrideBlockTexture()) {
			par8Icon = this.overrideBlockTexture;
		}

		par8Icon = CTMUtils.getTile(this, par1Block, (int)par2, (int)par4, (int)par6, 5, par8Icon, var9);

		if (par8Icon != null) {
			var9 = CTMUtils.getTessellator(par8Icon);
			double var10 = (double)par8Icon.getInterpolatedU(this.renderMinZ * 16.0D);
			double var12 = (double)par8Icon.getInterpolatedU(this.renderMaxZ * 16.0D);
			double var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
			double var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinY * 16.0D);
			double var18;

			if (this.flipTexture) {
				var18 = var10;
				var10 = var12;
				var12 = var18;
			}

			if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
				var10 = (double)par8Icon.getMinU();
				var12 = (double)par8Icon.getMaxU();
			}

			if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
				var14 = (double)par8Icon.getMinV();
				var16 = (double)par8Icon.getMaxV();
			}

			var18 = var12;
			double var20 = var10;
			double var22 = var14;
			double var24 = var16;

			if (this.uvRotateSouth == 2) {
				var10 = (double)par8Icon.getInterpolatedU(this.renderMinY * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(this.renderMaxY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
				var22 = var14;
				var24 = var16;
				var18 = var10;
				var20 = var12;
				var14 = var16;
				var16 = var22;
			} else if (this.uvRotateSouth == 1) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(this.renderMaxZ * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(this.renderMinZ * 16.0D);
				var18 = var12;
				var20 = var10;
				var10 = var12;
				var12 = var20;
				var22 = var16;
				var24 = var14;
			} else if (this.uvRotateSouth == 3) {
				var10 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
				var12 = (double)par8Icon.getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
				var14 = (double)par8Icon.getInterpolatedV(this.renderMaxY * 16.0D);
				var16 = (double)par8Icon.getInterpolatedV(this.renderMinY * 16.0D);
				var18 = var12;
				var20 = var10;
				var22 = var14;
				var24 = var16;
			}

			double var26 = par2 + this.renderMaxX;
			double var28 = par4 + this.renderMinY;
			double var30 = par4 + this.renderMaxY;
			double var32 = par6 + this.renderMinZ;
			double var34 = par6 + this.renderMaxZ;

			if (this.enableAO) {
				var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
				var9.setBrightness(this.brightnessTopLeft);
				var9.addVertexWithUV(var26, var28, var34, var20, var24);
				var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
				var9.setBrightness(this.brightnessBottomLeft);
				var9.addVertexWithUV(var26, var28, var32, var12, var16);
				var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
				var9.setBrightness(this.brightnessBottomRight);
				var9.addVertexWithUV(var26, var30, var32, var18, var22);
				var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
				var9.setBrightness(this.brightnessTopRight);
				var9.addVertexWithUV(var26, var30, var34, var10, var14);
			} else {
				var9.addVertexWithUV(var26, var28, var34, var20, var24);
				var9.addVertexWithUV(var26, var28, var32, var12, var16);
				var9.addVertexWithUV(var26, var30, var32, var18, var22);
				var9.addVertexWithUV(var26, var30, var34, var10, var14);
			}
		}
	}

	/**
	 * Is called to render the image of a block on an inventory, as a held item, or as a an item on the ground
	 */
	public void renderBlockAsItem(Block par1Block, int par2, float par3) {
		Tessellator var4 = Tessellator.instance;
		boolean var5 = par1Block.blockID == Block.grass.blockID;

		if (par1Block == Block.dispenser || par1Block == Block.dropper || par1Block == Block.furnaceIdle) {
			par2 = 3;
		}

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
		this.setRenderBoundsFromBlock(par1Block);
		int var14;

		if (var6 != 0 && var6 != 31 && var6 != 39 && var6 != 16 && var6 != 26) {
			if (var6 == 1) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				this.drawCrossedSquares(par1Block, par2, -0.5D, -0.5D, -0.5D, 1.0F);
				var4.draw();
			} else if (var6 == 19) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				par1Block.setBlockBoundsForItemRender();
				this.renderBlockStemSmall(par1Block, par2, this.renderMaxY, -0.5D, -0.5D, -0.5D);
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
				CTMUtils.start();
				this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 0, var4));
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(0.0F, 1.0F, 0.0F);
				CTMUtils.start();
				this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 1, var4));
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(0.0F, 0.0F, -1.0F);
				var4.addTranslation(0.0F, 0.0F, var7);
				CTMUtils.start();
				this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 2, var4));
				var4.addTranslation(0.0F, 0.0F, -var7);
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(0.0F, 0.0F, 1.0F);
				var4.addTranslation(0.0F, 0.0F, -var7);
				CTMUtils.start();
				this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 3, var4));
				var4.addTranslation(0.0F, 0.0F, var7);
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(-1.0F, 0.0F, 0.0F);
				var4.addTranslation(var7, 0.0F, 0.0F);
				CTMUtils.start();
				this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 4, var4));
				var4.addTranslation(-var7, 0.0F, 0.0F);
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(1.0F, 0.0F, 0.0F);
				var4.addTranslation(-var7, 0.0F, 0.0F);
				CTMUtils.start();
				this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 5, var4));
				var4.addTranslation(var7, 0.0F, 0.0F);
				var4.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if (var6 == 22) {
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				ChestItemRenderHelper.instance.renderChest(par1Block, par2, par3);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			} else if (var6 == 6) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBlockCropsImpl(par1Block, par2, -0.5D, -0.5D, -0.5D);
				var4.draw();
			} else if (var6 == 2) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				this.renderTorchAtAngle(par1Block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D, 0);
				var4.draw();
			} else if (var6 == 10) {
				for (var14 = 0; var14 < 2; ++var14) {
					if (var14 == 0) {
						this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
					}

					if (var14 == 1) {
						this.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					CTMUtils.start();
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 0, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					CTMUtils.start();
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 1, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					CTMUtils.start();
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					CTMUtils.start();
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 3, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 4, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 5, var4));
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
					this.setRenderBounds((double)(0.5F - var11), (double)var13, (double)(0.5F - var11), (double)(0.5F + var11), (double)var12, (double)(0.5F + var11));
					var4.setNormal(0.0F, -1.0F, 0.0F);
					CTMUtils.start();
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 0, var4));
					var4.setNormal(0.0F, 1.0F, 0.0F);
					CTMUtils.start();
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 1, var4));
					var4.setNormal(0.0F, 0.0F, -1.0F);
					CTMUtils.start();
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 2, var4));
					var4.setNormal(0.0F, 0.0F, 1.0F);
					CTMUtils.start();
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 3, var4));
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 4, var4));
					var4.setNormal(1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 5, var4));
				}

				var4.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			} else if (var6 == 11) {
				for (var14 = 0; var14 < 4; ++var14) {
					var8 = 0.125F;

					if (var14 == 0) {
						this.setRenderBounds((double)(0.5F - var8), 0.0D, 0.0D, (double)(0.5F + var8), 1.0D, (double)(var8 * 2.0F));
					}

					if (var14 == 1) {
						this.setRenderBounds((double)(0.5F - var8), 0.0D, (double)(1.0F - var8 * 2.0F), (double)(0.5F + var8), 1.0D, 1.0D);
					}

					var8 = 0.0625F;

					if (var14 == 2) {
						this.setRenderBounds((double)(0.5F - var8), (double)(1.0F - var8 * 3.0F), (double)(-var8 * 2.0F), (double)(0.5F + var8), (double)(1.0F - var8), (double)(1.0F + var8 * 2.0F));
					}

					if (var14 == 3) {
						this.setRenderBounds((double)(0.5F - var8), (double)(0.5F - var8 * 3.0F), (double)(-var8 * 2.0F), (double)(0.5F + var8), (double)(0.5F - var8), (double)(1.0F + var8 * 2.0F));
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					CTMUtils.start();
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 0, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					CTMUtils.start();
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 1, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					CTMUtils.start();
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					CTMUtils.start();
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 3, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 4, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 5, var4));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			} else if (var6 == 21) {
				for (var14 = 0; var14 < 3; ++var14) {
					var8 = 0.0625F;

					if (var14 == 0) {
						this.setRenderBounds((double)(0.5F - var8), 0.30000001192092896D, 0.0D, (double)(0.5F + var8), 1.0D, (double)(var8 * 2.0F));
					}

					if (var14 == 1) {
						this.setRenderBounds((double)(0.5F - var8), 0.30000001192092896D, (double)(1.0F - var8 * 2.0F), (double)(0.5F + var8), 1.0D, 1.0D);
					}

					var8 = 0.0625F;

					if (var14 == 2) {
						this.setRenderBounds((double)(0.5F - var8), 0.5D, 0.0D, (double)(0.5F + var8), (double)(1.0F - var8), 1.0D);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					CTMUtils.start();
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 0, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					CTMUtils.start();
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 1, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					CTMUtils.start();
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					CTMUtils.start();
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 3, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 4, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 5, var4));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}
			} else if (var6 == 32) {
				for (var14 = 0; var14 < 2; ++var14) {
					if (var14 == 0) {
						this.setRenderBounds(0.0D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
					}

					if (var14 == 1) {
						this.setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					CTMUtils.start();
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 0, par2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					CTMUtils.start();
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 1, par2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					CTMUtils.start();
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 2, par2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					CTMUtils.start();
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 3, par2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 4, par2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 5, par2, var4));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			} else if (var6 == 35) {
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				this.renderBlockAnvilOrient((BlockAnvil)par1Block, 0, 0, 0, par2, true);
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if (var6 == 34) {
				for (var14 = 0; var14 < 3; ++var14) {
					if (var14 == 0) {
						this.setRenderBounds(0.125D, 0.0D, 0.125D, 0.875D, 0.1875D, 0.875D);
						this.setOverrideBlockTexture(this.getBlockIcon(Block.obsidian));
					} else if (var14 == 1) {
						this.setRenderBounds(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.875D, 0.8125D);
						this.setOverrideBlockTexture(Block.beacon.func_94446_i());
					} else if (var14 == 2) {
						this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
						this.setOverrideBlockTexture(this.getBlockIcon(Block.glass));
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					CTMUtils.start();
					this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 0, par2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					CTMUtils.start();
					this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 1, par2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					CTMUtils.start();
					this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 2, par2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					CTMUtils.start();
					this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 3, par2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 4, par2, var4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					CTMUtils.start();
					this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 5, par2, var4));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
				this.clearOverrideBlockTexture();
			} else if (var6 == 38) {
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				this.renderBlockHopperMetadata((BlockHopper)par1Block, 0, 0, 0, 0, true);
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			}
		} else {
			if (var6 == 16) {
				par2 = 1;
			}

			par1Block.setBlockBoundsForItemRender();
			this.setRenderBoundsFromBlock(par1Block);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			var4.startDrawingQuads();
			var4.setNormal(0.0F, -1.0F, 0.0F);
			CTMUtils.start();
			this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 0, par2, var4));
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
			CTMUtils.start();
			this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 1, par2, var4));
			var4.draw();

			if (var5 && this.useInventoryTint) {
				GL11.glColor4f(par3, par3, par3, 1.0F);
			}

			var4.startDrawingQuads();
			var4.setNormal(0.0F, 0.0F, -1.0F);
			CTMUtils.start();
			this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 2, par2, var4));
			var4.draw();
			var4.startDrawingQuads();
			var4.setNormal(0.0F, 0.0F, 1.0F);
			CTMUtils.start();
			this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 3, par2, var4));
			var4.draw();
			var4.startDrawingQuads();
			var4.setNormal(-1.0F, 0.0F, 0.0F);
			CTMUtils.start();
			this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 4, par2, var4));
			var4.draw();
			var4.startDrawingQuads();
			var4.setNormal(1.0F, 0.0F, 0.0F);
			CTMUtils.start();
			this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, CTMUtils.getTile(this, par1Block, 5, par2, var4));
			var4.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}

		CTMUtils.finish();
	}

	/**
	 * Checks to see if the item's render type indicates that it should be rendered as a regular block or not.
	 */
	public static boolean renderItemIn3d(int par0) {
		return par0 == 0 ? true : (par0 == 31 ? true : (par0 == 39 ? true : (par0 == 13 ? true : (par0 == 10 ? true : (par0 == 11 ? true : (par0 == 27 ? true : (par0 == 22 ? true : (par0 == 21 ? true : (par0 == 16 ? true : (par0 == 26 ? true : (par0 == 32 ? true : (par0 == 34 ? true : par0 == 35))))))))))));
	}

	public Icon getBlockIcon(Block par1Block, IBlockAccess par2IBlockAccess, int par3, int par4, int par5, int par6) {
		return this.getIconSafe(par1Block.getBlockTexture(par2IBlockAccess, par3, par4, par5, par6));
	}

	public Icon getBlockIconFromSideAndMetadata(Block par1Block, int par2, int par3) {
		return this.getIconSafe(par1Block.getBlockTextureFromSideAndMetadata(par2, par3));
	}

	public Icon getBlockIconFromSide(Block par1Block, int par2) {
		return this.getIconSafe(par1Block.getBlockTextureFromSide(par2));
	}

	public Icon getBlockIcon(Block par1Block) {
		return this.getIconSafe(par1Block.getBlockTextureFromSide(1));
	}

	public Icon getIconSafe(Icon par1Icon) {
		return par1Icon == null ? this.minecraftRB.renderEngine.getMissingIcon(0) : par1Icon;
	}
}
