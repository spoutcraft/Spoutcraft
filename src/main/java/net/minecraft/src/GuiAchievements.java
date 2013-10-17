package net.minecraft.src;

import java.util.Random;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiAchievements extends GuiScreen {

	/** The top x coordinate of the achievement map */
	private static final int guiMapTop = AchievementList.minDisplayColumn * 24 - 112;

	/** The left y coordinate of the achievement map */
	private static final int guiMapLeft = AchievementList.minDisplayRow * 24 - 112;

	/** The bottom x coordinate of the achievement map */
	private static final int guiMapBottom = AchievementList.maxDisplayColumn * 24 - 77;

	/** The right y coordinate of the achievement map */
	private static final int guiMapRight = AchievementList.maxDisplayRow * 24 - 77;
	private static final ResourceLocation achievementTextures = new ResourceLocation("textures/gui/achievement/achievement_background.png");
	protected int achievementsPaneWidth = 256;
	protected int achievementsPaneHeight = 202;

	/** The current mouse x coordinate */
	protected int mouseX;

	/** The current mouse y coordinate */
	protected int mouseY;
	protected double field_74117_m;
	protected double field_74115_n;

	/** The x position of the achievement map */
	protected double guiMapX;

	/** The y position of the achievement map */
	protected double guiMapY;
	protected double field_74124_q;
	protected double field_74123_r;

	/** Whether the Mouse Button is down or not */
	private int isMouseButtonDown;
	private StatFileWriter statFileWriter;

	public GuiAchievements(StatFileWriter par1StatFileWriter) {
		this.statFileWriter = par1StatFileWriter;
		short var2 = 141;
		short var3 = 141;
		this.field_74117_m = this.guiMapX = this.field_74124_q = (double)(AchievementList.openInventory.displayColumn * 24 - var2 / 2 - 12);
		this.field_74115_n = this.guiMapY = this.field_74123_r = (double)(AchievementList.openInventory.displayRow * 24 - var3 / 2);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiSmallButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, I18n.getString("gui.done")));
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

		super.actionPerformed(par1GuiButton);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if (par2 == this.mc.gameSettings.keyBindInventory.keyCode) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		} else {
			super.keyTyped(par1, par2);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		if (Mouse.isButtonDown(0)) {
			int var4 = (this.width - this.achievementsPaneWidth) / 2;
			int var5 = (this.height - this.achievementsPaneHeight) / 2;
			int var6 = var4 + 8;
			int var7 = var5 + 17;

			if ((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && par1 >= var6 && par1 < var6 + 224 && par2 >= var7 && par2 < var7 + 155) {
				if (this.isMouseButtonDown == 0) {
					this.isMouseButtonDown = 1;
				} else {
					this.guiMapX -= (double)(par1 - this.mouseX);
					this.guiMapY -= (double)(par2 - this.mouseY);
					this.field_74124_q = this.field_74117_m = this.guiMapX;
					this.field_74123_r = this.field_74115_n = this.guiMapY;
				}

				this.mouseX = par1;
				this.mouseY = par2;
			}

			if (this.field_74124_q < (double)guiMapTop) {
				this.field_74124_q = (double)guiMapTop;
			}

			if (this.field_74123_r < (double)guiMapLeft) {
				this.field_74123_r = (double)guiMapLeft;
			}

			if (this.field_74124_q >= (double)guiMapBottom) {
				this.field_74124_q = (double)(guiMapBottom - 1);
			}

			if (this.field_74123_r >= (double)guiMapRight) {
				this.field_74123_r = (double)(guiMapRight - 1);
			}
		} else {
			this.isMouseButtonDown = 0;
		}

		this.drawDefaultBackground();
		this.genAchievementBackground(par1, par2, par3);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.drawTitle();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.field_74117_m = this.guiMapX;
		this.field_74115_n = this.guiMapY;
		double var1 = this.field_74124_q - this.guiMapX;
		double var3 = this.field_74123_r - this.guiMapY;

		if (var1 * var1 + var3 * var3 < 4.0D) {
			this.guiMapX += var1;
			this.guiMapY += var3;
		} else {
			this.guiMapX += var1 * 0.85D;
			this.guiMapY += var3 * 0.85D;
		}
	}

	/**
	 * Draws the "Achievements" title at the top of the GUI.
	 */
	protected void drawTitle() {
		int var1 = (this.width - this.achievementsPaneWidth) / 2;
		int var2 = (this.height - this.achievementsPaneHeight) / 2;
		this.fontRenderer.drawString("Achievements", var1 + 15, var2 + 5, 4210752);
	}

	protected void genAchievementBackground(int par1, int par2, float par3) {
		int var4 = MathHelper.floor_double(this.field_74117_m + (this.guiMapX - this.field_74117_m) * (double)par3);
		int var5 = MathHelper.floor_double(this.field_74115_n + (this.guiMapY - this.field_74115_n) * (double)par3);

		if (var4 < guiMapTop) {
			var4 = guiMapTop;
		}

		if (var5 < guiMapLeft) {
			var5 = guiMapLeft;
		}

		if (var4 >= guiMapBottom) {
			var4 = guiMapBottom - 1;
		}

		if (var5 >= guiMapRight) {
			var5 = guiMapRight - 1;
		}

		int var6 = (this.width - this.achievementsPaneWidth) / 2;
		int var7 = (this.height - this.achievementsPaneHeight) / 2;
		int var8 = var6 + 16;
		int var9 = var7 + 17;
		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_GEQUAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		int var10 = var4 + 288 >> 4;
		int var11 = var5 + 288 >> 4;
		int var12 = (var4 + 288) % 16;
		int var13 = (var5 + 288) % 16;
		boolean var14 = true;
		boolean var15 = true;
		boolean var16 = true;
		boolean var17 = true;
		boolean var18 = true;
		Random var19 = new Random();
		int var20;
		int var23;
		int var22;

		for (var20 = 0; var20 * 16 - var13 < 155; ++var20) {
			float var21 = 0.6F - (float)(var11 + var20) / 25.0F * 0.3F;
			GL11.glColor4f(var21, var21, var21, 1.0F);

			for (var22 = 0; var22 * 16 - var12 < 224; ++var22) {
				var19.setSeed((long)(1234 + var10 + var22));
				var19.nextInt();
				var23 = var19.nextInt(1 + var11 + var20) + (var11 + var20) / 2;
				Icon var24 = Block.sand.getIcon(0, 0);

				if (var23 <= 37 && var11 + var20 != 35) {
					if (var23 == 22) {
						if (var19.nextInt(2) == 0) {
							var24 = Block.oreDiamond.getIcon(0, 0);
						} else {
							var24 = Block.oreRedstone.getIcon(0, 0);
						}
					} else if (var23 == 10) {
						var24 = Block.oreIron.getIcon(0, 0);
					} else if (var23 == 8) {
						var24 = Block.oreCoal.getIcon(0, 0);
					} else if (var23 > 4) {
						var24 = Block.stone.getIcon(0, 0);
					} else if (var23 > 0) {
						var24 = Block.dirt.getIcon(0, 0);
					}
				} else {
					var24 = Block.bedrock.getIcon(0, 0);
				}

				this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
				this.drawTexturedModelRectFromIcon(var8 + var22 * 16 - var12, var9 + var20 * 16 - var13, var24, 16, 16);
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		int var25;
		int var28;
		int var41;

		for (var20 = 0; var20 < AchievementList.achievementList.size(); ++var20) {
			Achievement var31 = (Achievement)AchievementList.achievementList.get(var20);

			if (var31.parentAchievement != null) {
				var22 = var31.displayColumn * 24 - var4 + 11 + var8;
				var23 = var31.displayRow * 24 - var5 + 11 + var9;
				var41 = var31.parentAchievement.displayColumn * 24 - var4 + 11 + var8;
				var25 = var31.parentAchievement.displayRow * 24 - var5 + 11 + var9;
				boolean var26 = this.statFileWriter.hasAchievementUnlocked(var31);
				boolean var27 = this.statFileWriter.canUnlockAchievement(var31);
				var28 = Math.sin((double)(Minecraft.getSystemTime() % 600L) / 600.0D * Math.PI * 2.0D) > 0.6D ? 255 : 130;
				int var29 = -16777216;

				if (var26) {
					var29 = -9408400;
				} else if (var27) {
					var29 = 65280 + (var28 << 24);
				}

				this.drawHorizontalLine(var22, var41, var23, var29);
				this.drawVerticalLine(var41, var23, var25, var29);
			}
		}

		Achievement var30 = null;
		RenderItem var32 = new RenderItem();
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		int var39;
		int var40;

		for (var22 = 0; var22 < AchievementList.achievementList.size(); ++var22) {
			Achievement var34 = (Achievement)AchievementList.achievementList.get(var22);
			var41 = var34.displayColumn * 24 - var4;
			var25 = var34.displayRow * 24 - var5;

			if (var41 >= -24 && var25 >= -24 && var41 <= 224 && var25 <= 155) {
				float var38;

				if (this.statFileWriter.hasAchievementUnlocked(var34)) {
					var38 = 1.0F;
					GL11.glColor4f(var38, var38, var38, 1.0F);
				} else if (this.statFileWriter.canUnlockAchievement(var34)) {
					var38 = Math.sin((double)(Minecraft.getSystemTime() % 600L) / 600.0D * Math.PI * 2.0D) < 0.6D ? 0.6F : 0.8F;
					GL11.glColor4f(var38, var38, var38, 1.0F);
				} else {
					var38 = 0.3F;
					GL11.glColor4f(var38, var38, var38, 1.0F);
				}

				this.mc.getTextureManager().bindTexture(achievementTextures);
				var40 = var8 + var41;
				var39 = var9 + var25;

				if (var34.getSpecial()) {
					this.drawTexturedModalRect(var40 - 2, var39 - 2, 26, 202, 26, 26);
				} else {
					this.drawTexturedModalRect(var40 - 2, var39 - 2, 0, 202, 26, 26);
				}

				if (!this.statFileWriter.canUnlockAchievement(var34)) {
					float var37 = 0.1F;
					GL11.glColor4f(var37, var37, var37, 1.0F);
					var32.renderWithColor = false;
				}

				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_CULL_FACE);
				var32.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), var34.theItemStack, var40 + 3, var39 + 3);
				GL11.glDisable(GL11.GL_LIGHTING);

				if (!this.statFileWriter.canUnlockAchievement(var34)) {
					var32.renderWithColor = true;
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

				if (par1 >= var8 && par2 >= var9 && par1 < var8 + 224 && par2 < var9 + 155 && par1 >= var40 && par1 <= var40 + 22 && par2 >= var39 && par2 <= var39 + 22) {
					var30 = var34;
				}
			}
		}

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(achievementTextures);
		this.drawTexturedModalRect(var6, var7, 0, 0, this.achievementsPaneWidth, this.achievementsPaneHeight);
		GL11.glPopMatrix();
		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		super.drawScreen(par1, par2, par3);

		if (var30 != null) {
			String var33 = I18n.getString(var30.getName());
			String var35 = var30.getDescription();
			var41 = par1 + 12;
			var25 = par2 - 4;

			if (this.statFileWriter.canUnlockAchievement(var30)) {
				var40 = Math.max(this.fontRenderer.getStringWidth(var33), 120);
				var39 = this.fontRenderer.splitStringWidth(var35, var40);

				if (this.statFileWriter.hasAchievementUnlocked(var30)) {
					var39 += 12;
				}

				this.drawGradientRect(var41 - 3, var25 - 3, var41 + var40 + 3, var25 + var39 + 3 + 12, -1073741824, -1073741824);
				this.fontRenderer.drawSplitString(var35, var41, var25 + 12, var40, -6250336);

				if (this.statFileWriter.hasAchievementUnlocked(var30)) {
					this.fontRenderer.drawStringWithShadow(I18n.getString("achievement.taken"), var41, var25 + var39 + 4, -7302913);
				}
			} else {
				var40 = Math.max(this.fontRenderer.getStringWidth(var33), 120);
				String var36 = I18n.getStringParams("achievement.requires", new Object[] {I18n.getString(var30.parentAchievement.getName())});
				var28 = this.fontRenderer.splitStringWidth(var36, var40);
				this.drawGradientRect(var41 - 3, var25 - 3, var41 + var40 + 3, var25 + var28 + 12 + 3, -1073741824, -1073741824);
				this.fontRenderer.drawSplitString(var36, var41, var25 + 12, var40, -9416624);
			}

			this.fontRenderer.drawStringWithShadow(var33, var41, var25, this.statFileWriter.canUnlockAchievement(var30) ? (var30.getSpecial() ? -128 : -1) : (var30.getSpecial() ? -8355776 : -8355712));
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		// Spout Removed - This is how you fix stuff by implementing stupidity.
		//RenderHelper.disableStandardItemLighting();
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return true;
	}
}