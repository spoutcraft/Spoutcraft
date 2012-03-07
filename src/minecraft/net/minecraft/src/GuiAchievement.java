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
	
	//Spout Start
	private boolean customNotification = false;
	private int itemId;
	private short data = -1;
	private int time = -1;
	//Spout End

	/**
	 * Holds a instance of RenderItem, used to draw the achievement icons on
	 * screen (is based on ItemStack)
	 */
	private RenderItem itemRender;
	private boolean haveAchiement;

	public GuiAchievement(Minecraft par1Minecraft) {
		theGame = par1Minecraft;
		itemRender = new RenderItem();
	}

	/**
	 * Queue a taken achievement to be displayed.
	 */
	public void queueTakenAchievement(Achievement par1Achievement) {
		achievementGetLocalText = StatCollector
				.translateToLocal("achievement.get");
		achievementStatName = StatCollector.translateToLocal(par1Achievement
				.getName());
		achievementTime = System.currentTimeMillis();
		theAchievement = par1Achievement;
		haveAchiement = false;
		//Spout Start
		customNotification = false;
		time = -1;
		data = -1;
		//Spout End
	}

	/**
	 * Queue a information about a achievement to be displayed.
	 */
	public void queueAchievementInformation(Achievement par1Achievement) {
		achievementGetLocalText = StatCollector.translateToLocal(par1Achievement.getName());
		achievementStatName = par1Achievement.getDescription();
		achievementTime = System.currentTimeMillis() - 2500L;
		theAchievement = par1Achievement;
		haveAchiement = true;
		//Spout Start
		customNotification = false;
		time = -1;
		data = -1;
		//Spout End
	}
	
	//Spout Start
	public void queueNotification(String title, String message, int toRender) {
		achievementGetLocalText = title;
		achievementStatName = message;
		achievementTime = System.currentTimeMillis();
		theAchievement = null;
		haveAchiement = false;
		customNotification = true;
		this.itemId = toRender;
		this.time = -1;
		data = -1;
	}

	public void queueNotification(String title, String message, int toRender, short data, int time) {
		achievementGetLocalText = title;
		achievementStatName = message;
		achievementTime = System.currentTimeMillis();
		theAchievement = null;
		haveAchiement = false;
		customNotification = true;
		this.itemId = toRender;
		this.time = time;
		this.data = data;
	}
	//Spout End

	/**
	 * Update the display of the achievement window to match the game window.
	 */
	private void updateAchievementWindowScale() {
		GL11.glViewport(0, 0, theGame.displayWidth, theGame.displayHeight);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		achievementWindowWidth = theGame.displayWidth;
		achievementWindowHeight = theGame.displayHeight;
		ScaledResolution scaledresolution = new ScaledResolution(
				theGame.gameSettings, theGame.displayWidth,
				theGame.displayHeight);
		achievementWindowWidth = scaledresolution.getScaledWidth();
		achievementWindowHeight = scaledresolution.getScaledHeight();
		GL11.glClear(256);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, achievementWindowWidth, achievementWindowHeight,
				0.0D, 1000D, 3000D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000F);
	}

	/**
	 * Updates the small achievement tooltip window, showing a queued
	 * achievement if is needed.
	 */
	public void updateAchievementWindow() {
		if ((theAchievement == null || achievementTime == 0L) && !customNotification) { //Spout
			return;
		}
		
		//Spout Start
		double delayTime = 3000;
		if (customNotification) {
			if (time < 1) {
				delayTime = 7500;
			}
			else {
				delayTime = time;
			}
		}
		double var1 = (double)(System.currentTimeMillis() - this.achievementTime) / delayTime;
		//Spout End

		double d = (double) (System.currentTimeMillis() - achievementTime) / 3000D;

		if (!haveAchiement && !haveAchiement && (d < 0.0D || d > 1.0D)) {
			achievementTime = 0L;
			return;
		}

		updateAchievementWindowScale();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		double d1 = d * 2D;

		if (d1 > 1.0D) {
			d1 = 2D - d1;
		}

		d1 *= 4D;
		d1 = 1.0D - d1;

		if (d1 < 0.0D) {
			d1 = 0.0D;
		}

		d1 *= d1;
		d1 *= d1;
		int i = achievementWindowWidth - 160;
		int j = 0 - (int) (d1 * 36D);
		int k = theGame.renderEngine.getTexture("/achievement/bg.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, k);
		GL11.glDisable(GL11.GL_LIGHTING);
		drawTexturedModalRect(i, j, 96, 202, 160, 32);

		if (haveAchiement) {
			theGame.fontRenderer.drawSplitString(achievementStatName, i + 30,
					j + 7, 120, -1);
		} else {
			theGame.fontRenderer.drawString(achievementGetLocalText, i + 30,
					j + 7, -256);
			theGame.fontRenderer.drawString(achievementStatName, i + 30,
					j + 18, -1);
		}

		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		//Spout Start
		ItemStack toRender = theAchievement != null ? theAchievement.theItemStack : null;
		if (customNotification){
			if (data < 1) {
				toRender = new ItemStack(itemId, 1, 0);
			}
			else {
				toRender = new ItemStack(itemId, 1, data);
			}
		}
		if (toRender != null) {
			this.itemRender.renderItemIntoGUI(this.theGame.fontRenderer, this.theGame.renderEngine, toRender, i + 8, j + 8);
		}
		//Spout End
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
