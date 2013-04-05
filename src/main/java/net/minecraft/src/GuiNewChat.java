package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiNewChat extends Gui {

	/** The Minecraft instance. */
	private final Minecraft mc;

	/** A list of messages previously sent through the chat GUI */
	private final List sentMessages = new ArrayList();

	/** Chat lines to be displayed in the chat box */
	private final List chatLines = new ArrayList();
	private final List field_96134_d = new ArrayList();
	private int field_73768_d = 0;
	private boolean field_73769_e = false;

	public GuiNewChat(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
	}

	public void drawChat(int par1) {
		if (this.mc.gameSettings.chatVisibility != 2) {
			int var2 = this.func_96127_i();
			boolean var3 = false;
			int var4 = 0;
			int var5 = this.field_96134_d.size();
			float var6 = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

			if (var5 > 0) {
				if (this.getChatOpen()) {
					var3 = true;
				}

				float var7 = this.func_96131_h();
				int var8 = MathHelper.ceiling_float_int((float)this.func_96126_f() / var7);
				GL11.glPushMatrix();
				GL11.glTranslatef(2.0F, 20.0F, 0.0F);
				GL11.glScalef(var7, var7, 1.0F);
				int var9;
				int var11;
				int var14;

				for (var9 = 0; var9 + this.field_73768_d < this.field_96134_d.size() && var9 < var2; ++var9) {
					ChatLine var10 = (ChatLine)this.field_96134_d.get(var9 + this.field_73768_d);

					if (var10 != null) {
						var11 = par1 - var10.getUpdatedCounter();

						if (var11 < 200 || var3) {
							double var12 = (double)var11 / 200.0D;
							var12 = 1.0D - var12;
							var12 *= 10.0D;

							if (var12 < 0.0D) {
								var12 = 0.0D;
							}

							if (var12 > 1.0D) {
								var12 = 1.0D;
							}

							var12 *= var12;
							var14 = (int)(255.0D * var12);

							if (var3) {
								var14 = 255;
							}

							var14 = (int)((float)var14 * var6);
							++var4;

							if (var14 > 3) {
								byte var15 = 0;
								int var16 = -var9 * 9;
								drawRect(var15, var16 - 9, var15 + var8 + 4, var16, var14 / 2 << 24);
								GL11.glEnable(GL11.GL_BLEND);
								String var17 = var10.getChatLineString();

								if (!this.mc.gameSettings.chatColours) {
									var17 = StringUtils.stripControlCodes(var17);
								}

								this.mc.fontRenderer.drawStringWithShadow(var17, var15, var16 - 8, 16777215 + (var14 << 24));
							}
						}
					}
				}

				if (var3) {
					var9 = this.mc.fontRenderer.FONT_HEIGHT;
					GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
					int var18 = var5 * var9 + var5;
					var11 = var4 * var9 + var4;
					int var20 = this.field_73768_d * var11 / var5;
					int var13 = var11 * var11 / var18;

					if (var18 != var11) {
						var14 = var20 > 0 ? 170 : 96;
						int var19 = this.field_73769_e ? 13382451 : 3355562;
						drawRect(0, -var20, 2, -var20 - var13, var19 + (var14 << 24));
						drawRect(2, -var20, 1, -var20 - var13, 13421772 + (var14 << 24));
					}
				}

				GL11.glPopMatrix();
			}
		}
	}

	/**
	 * Clears the chat.
	 */
	public void clearChatMessages() {
		this.field_96134_d.clear();
		this.chatLines.clear();
		this.sentMessages.clear();
	}

	/**
	 * takes a String and prints it to chat
	 */
	public void printChatMessage(String par1Str) {
		this.printChatMessageWithOptionalDeletion(par1Str, 0);
	}

	/**
	 * prints the String to Chat. If the ID is not 0, deletes an existing Chat Line of that ID from the GUI
	 */
	public void printChatMessageWithOptionalDeletion(String par1Str, int par2) {
		this.func_96129_a(par1Str, par2, this.mc.ingameGUI.getUpdateCounter(), false);
		// Spout Start
		//this.mc.getLogAgent().logInfo("[CHAT] " + par1Str);
		// Spout End
	}

	private void func_96129_a(String par1Str, int par2, int par3, boolean par4) {
		boolean var5 = this.getChatOpen();
		boolean var6 = true;

		if (par2 != 0) {
			this.deleteChatLine(par2);
		}

		Iterator var7 = this.mc.fontRenderer.listFormattedStringToWidth(par1Str, MathHelper.floor_float((float)this.func_96126_f() / this.func_96131_h())).iterator();

		while (var7.hasNext()) {
			String var8 = (String)var7.next();

			if (var5 && this.field_73768_d > 0) {
				this.field_73769_e = true;
				this.scroll(1);
			}

			if (!var6) {
				var8 = " " + var8;
			}

			var6 = false;
			this.field_96134_d.add(0, new ChatLine(par3, var8, par2));
		}

		while (this.field_96134_d.size() > 100) {
			this.field_96134_d.remove(this.field_96134_d.size() - 1);
		}

		if (!par4) {
			this.chatLines.add(0, new ChatLine(par3, par1Str.trim(), par2));

			while (this.chatLines.size() > 100) {
				this.chatLines.remove(this.chatLines.size() - 1);
			}
		}
	}

	public void func_96132_b() {
		this.field_96134_d.clear();
		this.resetScroll();

		for (int var1 = this.chatLines.size() - 1; var1 >= 0; --var1) {
			ChatLine var2 = (ChatLine)this.chatLines.get(var1);
			this.func_96129_a(var2.getChatLineString(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
		}
	}

	/**
	 * Gets the list of messages previously sent through the chat GUI
	 */
	public List getSentMessages() {
		return this.sentMessages;
	}

	/**
	 * Adds this string to the list of sent messages, for recall using the up/down arrow keys
	 */
	public void addToSentMessages(String par1Str) {
		if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(par1Str)) {
			this.sentMessages.add(par1Str);
		}
	}

	/**
	 * Resets the chat scroll (executed when the GUI is closed)
	 */
	public void resetScroll() {
		this.field_73768_d = 0;
		this.field_73769_e = false;
	}

	/**
	 * Scrolls the chat by the given number of lines.
	 */
	public void scroll(int par1) {
		this.field_73768_d += par1;
		int var2 = this.field_96134_d.size();

		if (this.field_73768_d > var2 - this.func_96127_i()) {
			this.field_73768_d = var2 - this.func_96127_i();
		}

		if (this.field_73768_d <= 0) {
			this.field_73768_d = 0;
			this.field_73769_e = false;
		}
	}

	public ChatClickData func_73766_a(int par1, int par2) {
		if (!this.getChatOpen()) {
			return null;
		} else {
			ScaledResolution var3 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			int var4 = var3.getScaleFactor();
			float var5 = this.func_96131_h();
			int var6 = par1 / var4 - 3;
			int var7 = par2 / var4 - 25;
			var6 = MathHelper.floor_float((float)var6 / var5);
			var7 = MathHelper.floor_float((float)var7 / var5);

			if (var6 >= 0 && var7 >= 0) {
				int var8 = Math.min(this.func_96127_i(), this.field_96134_d.size());

				if (var6 <= MathHelper.floor_float((float)this.func_96126_f() / this.func_96131_h()) && var7 < this.mc.fontRenderer.FONT_HEIGHT * var8 + var8) {
					int var9 = var7 / (this.mc.fontRenderer.FONT_HEIGHT + 1) + this.field_73768_d;
					return new ChatClickData(this.mc.fontRenderer, (ChatLine)this.field_96134_d.get(var9), var6, var7 - (var9 - this.field_73768_d) * this.mc.fontRenderer.FONT_HEIGHT + var9);
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	/**
	 * Adds a message to the chat after translating to the client's locale.
	 */
	public void addTranslatedMessage(String par1Str, Object ... par2ArrayOfObj) {
		this.printChatMessage(StringTranslate.getInstance().translateKeyFormat(par1Str, par2ArrayOfObj));
	}

	/**
	 * @return {@code true} if the chat GUI is open
	 */
	public boolean getChatOpen() {
		return this.mc.currentScreen instanceof GuiChat;
	}

	/**
	 * finds and deletes a Chat line by ID
	 */
	public void deleteChatLine(int par1) {
		Iterator var2 = this.field_96134_d.iterator();
		ChatLine var3;

		do {
			if (!var2.hasNext()) {
				var2 = this.chatLines.iterator();

				do {
					if (!var2.hasNext()) {
						return;
					}

					var3 = (ChatLine)var2.next();
				} while (var3.getChatLineID() != par1);

				var2.remove();
				return;
			}

			var3 = (ChatLine)var2.next();
		} while (var3.getChatLineID() != par1);

		var2.remove();
	}

	public int func_96126_f() {
		return func_96128_a(this.mc.gameSettings.chatWidth);
	}

	public int func_96133_g() {
		return func_96130_b(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
	}

	public float func_96131_h() {
		return this.mc.gameSettings.chatScale;
	}

	public static final int func_96128_a(float par0) {
		short var1 = 320;
		byte var2 = 40;
		return MathHelper.floor_float(par0 * (float)(var1 - var2) + (float)var2);
	}

	public static final int func_96130_b(float par0) {
		short var1 = 180;
		byte var2 = 20;
		return MathHelper.floor_float(par0 * (float)(var1 - var2) + (float)var2);
	}

	public int func_96127_i() {
		return this.func_96133_g() / 9;
	}
}
