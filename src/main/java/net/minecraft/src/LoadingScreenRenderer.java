package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class LoadingScreenRenderer implements IProgressUpdate {
	private String field_73727_a = "";

	/** A reference to the Minecraft object. */
	private Minecraft mc;

	/**
	 * The text currently displayed (i.e. the argument to the last call to printText or func_73722_d)
	 */
	private String currentlyDisplayedText = "";
	private long field_73723_d = Minecraft.getSystemTime();
	private boolean field_73724_e = false;

	public LoadingScreenRenderer(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
	}

	/**
	 * this string, followed by "working..." and then the "% complete" are the 3 lines shown. This resets progress to 0,
	 * and the WorkingString to "working...".
	 */
	public void resetProgressAndMessage(String par1Str) {
		this.field_73724_e = false;
		this.func_73722_d(par1Str);
	}

	/**
	 * "Saving level", or the loading,or downloading equivelent
	 */
	public void displayProgressMessage(String par1Str) {
		this.field_73724_e = true;
		this.func_73722_d(par1Str);
	}

	public void func_73722_d(String par1Str) {
		this.currentlyDisplayedText = par1Str;

		if (!this.mc.running) {
			if (!this.field_73724_e) {
				throw new MinecraftError();
			}
		} else {
			ScaledResolution var2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			GL11.glClear(256);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, var2.getScaledWidth_double(), var2.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	/**
	 * This is called with "Working..." by resetProgressAndMessage
	 */
	public void resetProgresAndWorkingMessage(String par1Str) {		
		if (!this.mc.running) {
			if (!this.field_73724_e) {				
				throw new MinecraftError();
			}
		} else {			
			this.field_73723_d = 0L;
			this.field_73727_a = par1Str;
			this.setLoadingProgress(1); //Spout, this shouldn't be needed...
			this.field_73723_d = 0L;
		}
	}

	/**
	 * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
	 */
	public void setLoadingProgress(int par1) {
		if (!this.mc.running) {
			if (!this.field_73724_e) {
				throw new MinecraftError();
			}
		} else {
			long var2 = Minecraft.getSystemTime();			
			if (var2 - this.field_73723_d >= 100L) {				
				this.field_73723_d = var2;
				ScaledResolution var4 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
				int var5 = var4.getScaledWidth();
				int var6 = var4.getScaledHeight();
				GL11.glClear(256);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0.0D, var4.getScaledWidth_double(), var4.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				GL11.glTranslatef(0.0F, 0.0F, -200.0F);
				GL11.glClear(16640);
				Tessellator var7 = Tessellator.instance;
				int var8 = this.mc.renderEngine.getTexture("/gui/background.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var8);
				float var9 = 32.0F;
				var7.startDrawingQuads();
				var7.setColorOpaque_I(4210752);
				var7.addVertexWithUV(0.0D, (double)var6, 0.0D, 0.0D, (double)((float)var6 / var9));
				var7.addVertexWithUV((double)var5, (double)var6, 0.0D, (double)((float)var5 / var9), (double)((float)var6 / var9));
				var7.addVertexWithUV((double)var5, 0.0D, 0.0D, (double)((float)var5 / var9), 0.0D);
				var7.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
				var7.draw();

				if (par1 >= 0) {					
					byte var10 = 100;
					byte var11 = 2;
					int var12 = var5 / 2 - var10 / 2;
					int var13 = var6 / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var7.startDrawingQuads();
					var7.setColorOpaque_I(8421504);
					var7.addVertex((double)var12, (double)var13, 0.0D);
					var7.addVertex((double)var12, (double)(var13 + var11), 0.0D);
					var7.addVertex((double)(var12 + var10), (double)(var13 + var11), 0.0D);
					var7.addVertex((double)(var12 + var10), (double)var13, 0.0D);
					var7.setColorOpaque_I(8454016);
					var7.addVertex((double)var12, (double)var13, 0.0D);
					var7.addVertex((double)var12, (double)(var13 + var11), 0.0D);
					var7.addVertex((double)(var12 + par1), (double)(var13 + var11), 0.0D);
					var7.addVertex((double)(var12 + par1), (double)var13, 0.0D);
					var7.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				this.mc.fontRenderer.drawStringWithShadow(this.currentlyDisplayedText, (var5 - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, var6 / 2 - 4 - 16, 16777215);				
				this.mc.fontRenderer.drawStringWithShadow(this.field_73727_a, (var5 - this.mc.fontRenderer.getStringWidth(this.field_73727_a)) / 2, var6 / 2 - 4 + 8, 16777215);						
				Display.update();

				try {
					Thread.yield();
				} catch (Exception var14) {
					;
				}
			}
		}
	}

	/**
	 * called when there is no more progress to be had, both on completion and failure
	 */
	public void onNoMoreProgress() {}
}
