package net.minecraft.src;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatClickData {
	public static final Pattern field_50097_a = Pattern.compile("^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,3})(/\\S*)?$");
	private final FontRenderer field_50095_b;
	private final ChatLine field_50096_c;
	private final int field_50093_d;
	private final int field_50094_e;
	private final String field_50091_f;
	private final String field_50092_g;

	public ChatClickData(FontRenderer par1FontRenderer, ChatLine par2ChatLine, int par3, int par4) {
		this.field_50095_b = par1FontRenderer;
		this.field_50096_c = par2ChatLine;
		this.field_50093_d = par3;
		this.field_50094_e = par4;
		this.field_50091_f = par1FontRenderer.func_50107_a(par2ChatLine.message, par3);
		this.field_50092_g = this.func_50090_c();
	}

	public String func_50088_a() {
		return this.field_50092_g;
	}

	public URI func_50089_b() {
		String var1 = this.func_50088_a();
		if (var1 == null) {
			return null;
		} else {
			Matcher var2 = field_50097_a.matcher(var1);
			if (var2.matches()) {
				try {
					String var3 = var2.group(0);
					if (var2.group(1) == null) {
						var3 = "http://" + var3;
					}

					return new URI(var3);
				} catch (URISyntaxException var4) {
					Logger.getLogger("Minecraft").log(Level.SEVERE, "Couldn\'t create URI from chat", var4);
				}
			}

			return null;
		}
	}

	private String func_50090_c() {
		int var1 = this.field_50091_f.lastIndexOf(" ", this.field_50091_f.length()) + 1;
		if (var1 < 0) {
			var1 = 0;
		}

		int var2 = this.field_50096_c.message.indexOf(" ", var1);
		if (var2 < 0) {
			var2 = this.field_50096_c.message.length();
		}

		FontRenderer var10000 = this.field_50095_b;
		return FontRenderer.func_52014_d(this.field_50096_c.message.substring(var1, var2));
	}
	
	//Spout start
	public String getMessage() {
		return field_50096_c.message;
	}
	//Spout end
}