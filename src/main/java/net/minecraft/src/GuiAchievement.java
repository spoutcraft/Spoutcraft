package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiAchievement extends Gui {

	/** Holds the instance of the game (Minecraft) */
	private Minecraft theGame;

	/** Holds the latest width scaled to fit the game window. */
	private int achievementWindowWidth;

	/** Holds the latest height scaled to fit the game window. */
	private int achievementWindowHeight;
	private String achievementGetLocalText;
	private String achievementStatName;

	/** Holds the achievement that will be displayed on the GUI. */
	private Achievement theAchievement;
	private long achievementTime;

	/**
	 * Holds a instance of RenderItem, used to draw the achievement icons on screen (is based on ItemStack)
	 */
	private RenderItem itemRender;
	private boolean haveAchiement;
	// Spout Start
	private boolean customNotification = false;
	private int itemId;
	private short data = -1;
	private int time = -1;
	// Spout End

	public GuiAchievement(Minecraft par1Minecraft) {
		this.theGame = par1Minecraft;
		this.itemRender = new RenderItem();
	}

	/**
	 * Queue a taken achievement to be displayed.
	 */
	public void queueTakenAchievement(Achievement par1Achievement) {
		this.achievementGetLocalText = StatCollector.translateToLocal("achievement.get");
		this.achievementStatName = StatCollector.translateToLocal(par1Achievement.getName());
		this.achievementTime = Minecraft.getSystemTime();
		this.theAchievement = par1Achievement;
		this.haveAchiement = false;
	}

	/**
	 * Queue a information about a achievement to be displayed.
	 */
	public void queueAchievementInformation(Achievement par1Achievement) {
		this.achievementGetLocalText = StatCollector.translateToLocal(par1Achievement.getName());
		this.achievementStatName = par1Achievement.getDescription();
		this.achievementTime = Minecraft.getSystemTime() - 2500L;
		this.theAchievement = par1Achievement;
		this.haveAchiement = true;
	}

	// Spout Start
	public void queueNotification(String title, String message, int toRender) {
		queueNotification(title, message, toRender, (short) -1, -1);
	}

	public void queueNotification(String title, String message, int toRender, short data, int time) {
		achievementGetLocalText = title;
		achievementStatName = message;
		achievementTime = Minecraft.getSystemTime();
		theAchievement = null;
		haveAchiement = false;
		customNotification = true;
		this.itemId = toRender;
		this.time = time;
		this.data = data;
	}
	// Spout End

	/**
	 * Update the display of the achievement window to match the game window.
	 */
	private void updateAchievementWindowScale() {
		GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		this.achievementWindowWidth = this.theGame.displayWidth;
		this.achievementWindowHeight = this.theGame.displayHeight;
		ScaledResolution var1 = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
		this.achievementWindowWidth = var1.getScaledWidth();
		this.achievementWindowHeight = var1.getScaledHeight();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)this.achievementWindowWidth, (double)this.achievementWindowHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	/**
	 * Updates the small achievement tooltip window, showing a queued achievement if is needed.
	 */
	public void updateAchievementWindow() {
		// Spout Start
		if ((this.theAchievement != null || this.customNotification == true) && this.achievementTime != 0L) {
			double delayTime = 3000.0D;
			if (customNotification) {
				if (time < 1) {
					delayTime = 7500;
				} else {
					delayTime = time;
				}
			}
			double var1 = (double)(Minecraft.getSystemTime() - this.achievementTime) / delayTime;
		// Spout End

			if (!this.haveAchiement && (var1 < 0.0D || var1 > 1.0D)) {
				this.achievementTime = 0L;
			} else {
				this.updateAchievementWindowScale();
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(false);
				double var3 = var1 * 2.0D;

				if (var3 > 1.0D) {
					var3 = 2.0D - var3;
				}

				var3 *= 4.0D;
				var3 = 1.0D - var3;

				if (var3 < 0.0D) {
					var3 = 0.0D;
				}

				var3 *= var3;
				var3 *= var3;
				int var5 = this.achievementWindowWidth - 160;
				int var6 = 0 - (int)(var3 * 36.0D);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				this.theGame.renderEngine.bindTexture("/achievement/bg.png");
				GL11.glDisable(GL11.GL_LIGHTING);
				this.drawTexturedModalRect(var5, var6, 96, 202, 160, 32);

				if (this.haveAchiement) {
					this.theGame.fontRenderer.drawSplitString(this.achievementStatName, var5 + 30, var6 + 7, 120, -1);
				} else {
					this.theGame.fontRenderer.drawString(this.achievementGetLocalText, var5 + 30, var6 + 7, -256);
					this.theGame.fontRenderer.drawString(this.achievementStatName, var5 + 30, var6 + 18, -1);
				}

				RenderHelper.enableGUIStandardItemLighting();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GL11.glEnable(GL11.GL_COLOR_MATERIAL);
				GL11.glEnable(GL11.GL_LIGHTING);
				// Spout Start
				ItemStack toRender = theAchievement != null ? theAchievement.theItemStack : null;
				if (customNotification){
					if (data < 1) {
						toRender = new ItemStack(itemId, 1, 0);
					} else {
						toRender = new ItemStack(itemId, 1, data);
					}
				}
				if (toRender != null) {
					this.itemRender.renderItemIntoGUI(this.theGame.fontRenderer, this.theGame.renderEngine, toRender, var5 + 8, var6 + 8);
				}
				// Spout End
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}
	}
}
