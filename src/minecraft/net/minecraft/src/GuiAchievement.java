package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Achievement;
import net.minecraft.src.Gui;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiAchievement extends Gui {

	private Minecraft theGame;
	private int achievementWindowWidth;
	private int achievementWindowHeight;
	private String achievementGetLocalText;
	private String achievementStatName;
	private Achievement theAchievement;
	private long achievementTime;
	private RenderItem itemRender;
	private boolean haveAchiement;
	//Spout Start
	private boolean customNotification = false;
	private int itemId;
	private short data = -1;
	private int time = -1;
	//Spout End


	public GuiAchievement(Minecraft var1) {
		this.theGame = var1;
		this.itemRender = new RenderItem();
	}

	public void queueTakenAchievement(Achievement var1) {
		this.achievementGetLocalText = StatCollector.translateToLocal("achievement.get");
		this.achievementStatName = StatCollector.translateToLocal(var1.func_44020_i());
		this.achievementTime = System.currentTimeMillis();
		this.theAchievement = var1;
		this.haveAchiement = false;
		//Spout Start
		customNotification = false;
		time = -1;
		data = -1;
		//Spout End
	}

	public void queueAchievementInformation(Achievement var1) {
		this.achievementGetLocalText = StatCollector.translateToLocal(var1.func_44020_i());
		this.achievementStatName = var1.getDescription();
		this.achievementTime = System.currentTimeMillis() - 2500L;
		this.theAchievement = var1;
		this.haveAchiement = true;
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

	private void updateAchievementWindowScale() {
		GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
		GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
		GL11.glLoadIdentity();
		this.achievementWindowWidth = this.theGame.displayWidth;
		this.achievementWindowHeight = this.theGame.displayHeight;
		ScaledResolution var1 = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
		this.achievementWindowWidth = var1.getScaledWidth();
		this.achievementWindowHeight = var1.getScaledHeight();
		GL11.glClear(256);
		GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)this.achievementWindowWidth, (double)this.achievementWindowHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	public void updateAchievementWindow() {
		if(this.theAchievement != null && this.achievementTime != 0L || customNotification) { //Spout
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
			
			if(!this.haveAchiement && !this.haveAchiement && (var1 < 0.0D || var1 > 1.0D)) {
				this.achievementTime = 0L;
			} else {
				this.updateAchievementWindowScale();
				GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
				GL11.glDepthMask(false);
				double var3 = var1 * 2.0D;
				if(var3 > 1.0D) {
					var3 = 2.0D - var3;
				}

				var3 *= 4.0D;
				var3 = 1.0D - var3;
				if(var3 < 0.0D) {
					var3 = 0.0D;
				}

				var3 *= var3;
				var3 *= var3;
				int var5 = this.achievementWindowWidth - 160;
				int var6 = 0 - (int)(var3 * 36.0D);
				int var7 = this.theGame.renderEngine.getTexture("/achievement/bg.png");
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
				GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var7);
				GL11.glDisable(2896 /*GL_LIGHTING*/);
				this.drawTexturedModalRect(var5, var6, 96, 202, 160, 32);
				if(this.haveAchiement) {
					this.theGame.fontRenderer.drawSplitString(this.achievementStatName, var5 + 30, var6 + 7, 120, -1);
				} else {
					this.theGame.fontRenderer.drawString(this.achievementGetLocalText, var5 + 30, var6 + 7, -256);
					this.theGame.fontRenderer.drawString(this.achievementStatName, var5 + 30, var6 + 18, -1);
				}

				RenderHelper.func_41089_c();
				GL11.glDisable(2896 /*GL_LIGHTING*/);
				GL11.glEnable('\u803a');
				GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
				GL11.glEnable(2896 /*GL_LIGHTING*/);
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
					this.itemRender.renderItemIntoGUI(this.theGame.fontRenderer, this.theGame.renderEngine, toRender, var5 + 8, var6 + 8);
				}
				//Spout End
				GL11.glDisable(2896 /*GL_LIGHTING*/);
				GL11.glDepthMask(true);
				GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
			}
		}
	}
}
