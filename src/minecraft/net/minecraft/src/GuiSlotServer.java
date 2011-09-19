package net.minecraft.src;
/* Spout removed until we need it later

import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.ServerNBTStorage;
import net.minecraft.src.Tessellator;
import net.minecraft.src.ThreadPollServers;
import org.lwjgl.opengl.GL11;

class GuiSlotServer extends GuiSlot {

	// $FF: synthetic field
	final GuiMultiplayer field_35410_a;


	public GuiSlotServer(GuiMultiplayer var1) {
		super(var1.mc, var1.width, var1.height, 32, var1.height - 64, 36);
		this.field_35410_a = var1;
	}

	protected int getSize() {
		return GuiMultiplayer.func_35320_a(this.field_35410_a).size();
	}

	protected void elementClicked(int var1, boolean var2) {
		GuiMultiplayer.func_35326_a(this.field_35410_a, var1);
		boolean var3 = GuiMultiplayer.func_35333_b(this.field_35410_a) >= 0 && GuiMultiplayer.func_35333_b(this.field_35410_a) < this.getSize();
		GuiMultiplayer.func_35329_c(this.field_35410_a).enabled = var3;
		GuiMultiplayer.func_35334_d(this.field_35410_a).enabled = var3;
		GuiMultiplayer.func_35339_e(this.field_35410_a).enabled = var3;
		if(var2 && var3) {
			GuiMultiplayer.func_35332_b(this.field_35410_a, var1);
		}

	}

	protected boolean isSelected(int var1) {
		return var1 == GuiMultiplayer.func_35333_b(this.field_35410_a);
	}

	protected int getContentHeight() {
		return GuiMultiplayer.func_35320_a(this.field_35410_a).size() * 36;
	}

	protected void drawBackground() {
		this.field_35410_a.drawDefaultBackground();
	}

	protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
		ServerNBTStorage var6 = (ServerNBTStorage)GuiMultiplayer.func_35320_a(this.field_35410_a).get(var1);
		synchronized(GuiMultiplayer.func_35321_g()) {
			if(GuiMultiplayer.func_35338_m() < 5 && !var6.field_35790_f) {
				var6.field_35790_f = true;
				var6.field_35792_e = -2L;
				var6.field_35791_d = "";
				var6.field_35794_c = "";
				GuiMultiplayer.func_35331_n();
				(new ThreadPollServers(this, var6)).start();
			}
		}

		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, var6.field_35795_a, var2 + 2, var3 + 1, 16777215);
		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, var6.field_35791_d, var2 + 2, var3 + 12, 8421504);
		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, var6.field_35794_c, var2 + 215 - this.field_35410_a.fontRenderer.getStringWidth(var6.field_35794_c), var3 + 12, 8421504);
		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, var6.field_35793_b, var2 + 2, var3 + 12 + 11, 3158064);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.field_35410_a.mc.renderEngine.bindTexture(this.field_35410_a.mc.renderEngine.getTexture("/gui/icons.png"));
		boolean var7 = false;
		boolean var8 = false;
		String var9 = "";
		byte var12;
		int var13;
		if(var6.field_35790_f && var6.field_35792_e != -2L) {
			var12 = 0;
			var8 = false;
			if(var6.field_35792_e < 0L) {
				var13 = 5;
			} else if(var6.field_35792_e < 150L) {
				var13 = 0;
			} else if(var6.field_35792_e < 300L) {
				var13 = 1;
			} else if(var6.field_35792_e < 600L) {
				var13 = 2;
			} else if(var6.field_35792_e < 1000L) {
				var13 = 3;
			} else {
				var13 = 4;
			}

			if(var6.field_35792_e < 0L) {
				var9 = "(no connection)";
			} else {
				var9 = var6.field_35792_e + "ms";
			}
		} else {
			var12 = 1;
			var13 = (int)(System.currentTimeMillis() / 100L + (long)(var1 * 2) & 7L);
			if(var13 > 4) {
				var13 = 8 - var13;
			}

			var9 = "Polling..";
		}

		this.field_35410_a.drawTexturedModalRect(var2 + 205, var3, 0 + var12 * 10, 176 + var13 * 8, 10, 8);
		byte var10 = 4;
		if(this.field_35409_k >= var2 + 205 - var10 && this.field_35408_l >= var3 - var10 && this.field_35409_k <= var2 + 205 + 10 + var10 && this.field_35408_l <= var3 + 8 + var10) {
			GuiMultiplayer.func_35327_a(this.field_35410_a, var9);
		}
	}

}
*/