package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiSlot {

	private final Minecraft mc;
	private final int width;
	private final int height;
	protected final int top;
	protected final int bottom;
	private final int right;
	private final int left;
	protected final int slotHeight;
	private int scrollUpButtonID;
	private int scrollDownButtonID;
	protected int field_35409_k;
	protected int field_35408_l;
	private float initialClickY = -2.0F;
	private float scrollMultiplier;
	public float amountScrolled; //Spout private -> public
	private int selectedElement = -1;
	private long lastClicked = 0L;
	private boolean field_25123_p = true;
	private boolean field_27262_q;
	private int field_27261_r;


	public GuiSlot(Minecraft var1, int var2, int var3, int var4, int var5, int var6) {
		this.mc = var1;
		this.width = var2;
		this.height = var3;
		this.top = var4;
		this.bottom = var5;
		this.slotHeight = var6;
		this.left = 0;
		this.right = var2;
	}

	public void func_27258_a(boolean var1) {
		this.field_25123_p = var1;
	}

	protected void func_27259_a(boolean var1, int var2) {
		this.field_27262_q = var1;
		this.field_27261_r = var2;
		if(!var1) {
			this.field_27261_r = 0;
		}

	}

	protected abstract int getSize();

	protected abstract void elementClicked(int var1, boolean var2);
	
	protected void elementInfo(int var1) {};

	protected abstract boolean isSelected(int var1);

	protected int getContentHeight() {
		return this.getSize() * this.slotHeight + this.field_27261_r;
	}

	protected abstract void drawBackground();

	protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);

	protected void func_27260_a(int var1, int var2, Tessellator var3) {}

	protected void func_27255_a(int var1, int var2) {}

	protected void func_27257_b(int var1, int var2) {}

	public int func_27256_c(int var1, int var2) {
		int var3 = this.width / 2 - 110;
		int var4 = this.width / 2 + 110;
		int var5 = var2 - this.top - this.field_27261_r + (int)this.amountScrolled - 4;
		int var6 = var5 / this.slotHeight;
		return var1 >= var3 && var1 <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getSize()?var6:-1;
	}

	public void registerScrollButtons(List var1, int var2, int var3) {
		this.scrollUpButtonID = var2;
		this.scrollDownButtonID = var3;
	}

	private void bindAmountScrolled() {
		int var1 = this.getContentHeight() - (this.bottom - this.top - 4);
		if(var1 < 0) {
			var1 /= 2;
		}

		if(this.amountScrolled < 0.0F) {
			this.amountScrolled = 0.0F;
		}

		if(this.amountScrolled > (float)var1) {
			this.amountScrolled = (float)var1;
		}

	}

	public void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == this.scrollUpButtonID) {
				this.amountScrolled -= (float)(this.slotHeight * 2 / 3);
				this.initialClickY = -2.0F;
				this.bindAmountScrolled();
			} else if(var1.id == this.scrollDownButtonID) {
				this.amountScrolled += (float)(this.slotHeight * 2 / 3);
				this.initialClickY = -2.0F;
				this.bindAmountScrolled();
			}

		}
	}
	
	public void onClick(int var1, int var2, int var3) {
		int var8i = this.width / 2 + 111;
		int var9i = this.width / 2 + 122;
		int var10 = var2 - this.top - this.field_27261_r + (int)this.amountScrolled - 4;
		int var11 = var10 / this.slotHeight;
		int var4 = this.getSize();
		int info_top = var2 - ((var11 * slotHeight) + this.top + this.field_27261_r - (int)this.amountScrolled + 4);
		if(var1 >= var8i && var1 <= var9i && var11 >= 0 && var10 >= 0 && var11 < var4 && info_top < 13) {
			elementInfo(var11);
		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.field_35409_k = var1;
		this.field_35408_l = var2;
		this.drawBackground();
		int var4 = this.getSize();
		int var5 = this.width / 2 + 130;
		int var6 = var5 + 6;
		int var9;
		int var10;
		int var11;
		int var13;
		int var18;
		if(Mouse.isButtonDown(0)) {
			if(this.initialClickY == -1.0F) {
				boolean var7 = true;
				if(var2 >= this.top && var2 <= this.bottom) {
					int var8 = this.width / 2 - 110;
					var9 = this.width / 2 + 110;
					var10 = var2 - this.top - this.field_27261_r + (int)this.amountScrolled - 4;
					var11 = var10 / this.slotHeight;
					
					if(var1 >= var8 && var1 <= var9 && var11 >= 0 && var10 >= 0 && var11 < var4) {
						boolean var12 = var11 == this.selectedElement && System.currentTimeMillis() - this.lastClicked < 250L;
						this.elementClicked(var11, var12);
						this.selectedElement = var11;
						this.lastClicked = System.currentTimeMillis();
					} else if(var1 >= var8 && var1 <= var9 && var10 < 0) {
						this.func_27255_a(var1 - var8, var2 - this.top + (int)this.amountScrolled - 4);
						var7 = false;
					}

					if(var1 >= var5 && var1 <= var6) {
						this.scrollMultiplier = -1.0F;
						var18 = this.getContentHeight() - (this.bottom - this.top - 4);
						if(var18 < 1) {
							var18 = 1;
						}

						var13 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
						if(var13 < 32) {
							var13 = 32;
						}

						if(var13 > this.bottom - this.top - 8) {
							var13 = this.bottom - this.top - 8;
						}

						this.scrollMultiplier /= (float)(this.bottom - this.top - var13) / (float)var18;
					} else {
						this.scrollMultiplier = 1.0F;
					}

					if(var7) {
						this.initialClickY = (float)var2;
					} else {
						this.initialClickY = -2.0F;
					}
				} else {
					this.initialClickY = -2.0F;
				}
			} else if(this.initialClickY >= 0.0F) {
				this.amountScrolled -= ((float)var2 - this.initialClickY) * this.scrollMultiplier;
				this.initialClickY = (float)var2;
			}
		} else {
			while(Mouse.next()) {
				int var16 = Mouse.getEventDWheel();
				if(var16 != 0) {
					if(var16 > 0) {
						var16 = -1;
					} else if(var16 < 0) {
						var16 = 1;
					}

					this.amountScrolled += (float)(var16 * this.slotHeight / 2);
				}
			}
			this.initialClickY = -1.0F;
		}
		
		this.bindAmountScrolled();
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glDisable(2912 /*GL_FOG*/);
		Tessellator var16 = Tessellator.instance;
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/gui/background.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var17 = 32.0F;
		var16.startDrawingQuads();
		var16.setColorOpaque_I(2105376);
		var16.addVertexWithUV((double)this.left, (double)this.bottom, 0.0D, (double)((float)this.left / var17), (double)((float)(this.bottom + (int)this.amountScrolled) / var17));
		var16.addVertexWithUV((double)this.right, (double)this.bottom, 0.0D, (double)((float)this.right / var17), (double)((float)(this.bottom + (int)this.amountScrolled) / var17));
		var16.addVertexWithUV((double)this.right, (double)this.top, 0.0D, (double)((float)this.right / var17), (double)((float)(this.top + (int)this.amountScrolled) / var17));
		var16.addVertexWithUV((double)this.left, (double)this.top, 0.0D, (double)((float)this.left / var17), (double)((float)(this.top + (int)this.amountScrolled) / var17));
		var16.draw();
		var9 = this.width / 2 - 92 - 16;
		var10 = this.top + 4 - (int)this.amountScrolled;
		if(this.field_27262_q) {
			this.func_27260_a(var9, var10, var16);
		}
		
		int var14;
		for(var11 = 0; var11 < var4; ++var11) {
			var18 = var10 + var11 * this.slotHeight + this.field_27261_r;
			var13 = this.slotHeight - 4;
			if(var18 <= this.bottom && var18 + var13 >= this.top) {
				if(this.field_25123_p && this.isSelected(var11)) {
					var14 = this.width / 2 - 110;
					int var15 = this.width / 2 + 110;
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
					var16.startDrawingQuads();
					var16.setColorOpaque_I(8421504);
					var16.addVertexWithUV((double)var14, (double)(var18 + var13 + 2), 0.0D, 0.0D, 1.0D);
					var16.addVertexWithUV((double)var15, (double)(var18 + var13 + 2), 0.0D, 1.0D, 1.0D);
					var16.addVertexWithUV((double)var15, (double)(var18 - 2), 0.0D, 1.0D, 0.0D);
					var16.addVertexWithUV((double)var14, (double)(var18 - 2), 0.0D, 0.0D, 0.0D);
					var16.setColorOpaque_I(0);
					var16.addVertexWithUV((double)(var14 + 1), (double)(var18 + var13 + 1), 0.0D, 0.0D, 1.0D);
					var16.addVertexWithUV((double)(var15 - 1), (double)(var18 + var13 + 1), 0.0D, 1.0D, 1.0D);
					var16.addVertexWithUV((double)(var15 - 1), (double)(var18 - 1), 0.0D, 1.0D, 0.0D);
					var16.addVertexWithUV((double)(var14 + 1), (double)(var18 - 1), 0.0D, 0.0D, 0.0D);
					var16.draw();
					GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
				}

				this.drawSlot(var11, var9, var18, var13, var16);
			}
		}
		
		GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		byte var19 = 4;
		this.overlayBackground(0, this.top, 255, 255);
		this.overlayBackground(this.bottom, this.height, 255, 255);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glShadeModel(7425 /*GL_SMOOTH*/);
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		var16.startDrawingQuads();
		var16.setColorRGBA_I(0, 0);
		var16.addVertexWithUV((double)this.left, (double)(this.top + var19), 0.0D, 0.0D, 1.0D);
		var16.addVertexWithUV((double)this.right, (double)(this.top + var19), 0.0D, 1.0D, 1.0D);
		var16.setColorRGBA_I(0, 255);
		var16.addVertexWithUV((double)this.right, (double)this.top, 0.0D, 1.0D, 0.0D);
		var16.addVertexWithUV((double)this.left, (double)this.top, 0.0D, 0.0D, 0.0D);
		var16.draw();
		var16.startDrawingQuads();
		var16.setColorRGBA_I(0, 255);
		var16.addVertexWithUV((double)this.left, (double)this.bottom, 0.0D, 0.0D, 1.0D);
		var16.addVertexWithUV((double)this.right, (double)this.bottom, 0.0D, 1.0D, 1.0D);
		var16.setColorRGBA_I(0, 0);
		var16.addVertexWithUV((double)this.right, (double)(this.bottom - var19), 0.0D, 1.0D, 0.0D);
		var16.addVertexWithUV((double)this.left, (double)(this.bottom - var19), 0.0D, 0.0D, 0.0D);
		var16.draw();
		var18 = this.getContentHeight() - (this.bottom - this.top - 4);
		if(var18 > 0) {
			var13 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
			if(var13 < 32) {
				var13 = 32;
			}

			if(var13 > this.bottom - this.top - 8) {
				var13 = this.bottom - this.top - 8;
			}

			var14 = (int)this.amountScrolled * (this.bottom - this.top - var13) / var18 + this.top;
			if(var14 < this.top) {
				var14 = this.top;
			}
			
			var16.startDrawingQuads();
			var16.setColorRGBA_I(0, 255);
			var16.addVertexWithUV((double)var5, (double)this.bottom, 0.0D, 0.0D, 1.0D);
			var16.addVertexWithUV((double)var6, (double)this.bottom, 0.0D, 1.0D, 1.0D);
			var16.addVertexWithUV((double)var6, (double)this.top, 0.0D, 1.0D, 0.0D);
			var16.addVertexWithUV((double)var5, (double)this.top, 0.0D, 0.0D, 0.0D);
			var16.draw();
			var16.startDrawingQuads();
			var16.setColorRGBA_I(8421504, 255);
			var16.addVertexWithUV((double)var5, (double)(var14 + var13), 0.0D, 0.0D, 1.0D);
			var16.addVertexWithUV((double)var6, (double)(var14 + var13), 0.0D, 1.0D, 1.0D);
			var16.addVertexWithUV((double)var6, (double)var14, 0.0D, 1.0D, 0.0D);
			var16.addVertexWithUV((double)var5, (double)var14, 0.0D, 0.0D, 0.0D);
			var16.draw();
			var16.startDrawingQuads();
			var16.setColorRGBA_I(12632256, 255);
			var16.addVertexWithUV((double)var5, (double)(var14 + var13 - 1), 0.0D, 0.0D, 1.0D);
			var16.addVertexWithUV((double)(var6 - 1), (double)(var14 + var13 - 1), 0.0D, 1.0D, 1.0D);
			var16.addVertexWithUV((double)(var6 - 1), (double)var14, 0.0D, 1.0D, 0.0D);
			var16.addVertexWithUV((double)var5, (double)var14, 0.0D, 0.0D, 0.0D);
			var16.draw();
		}

		this.func_27257_b(var1, var2);
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		GL11.glShadeModel(7424 /*GL_FLAT*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glDisable(3042 /*GL_BLEND*/);
	}

	private void overlayBackground(int var1, int var2, int var3, int var4) {
		GL11.glPushMatrix(); //Spout
		Tessellator var5 = Tessellator.instance;
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/gui/background.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(0, 0, -0.1f); // Spout
		float var6 = 32.0F;
		var5.startDrawingQuads();
		var5.setColorRGBA_I(4210752, var4);
		var5.addVertexWithUV(0.0D, (double)var2, 0.0D, 0.0D, (double)((float)var2 / var6));
		var5.addVertexWithUV((double)this.width, (double)var2, 0.0D, (double)((float)this.width / var6), (double)((float)var2 / var6));
		var5.setColorRGBA_I(4210752, var3);
		var5.addVertexWithUV((double)this.width, (double)var1, 0.0D, (double)((float)this.width / var6), (double)((float)var1 / var6));
		var5.addVertexWithUV(0.0D, (double)var1, 0.0D, 0.0D, (double)((float)var1 / var6));
		var5.draw();
		GL11.glPopMatrix(); //Spout
	}
}
