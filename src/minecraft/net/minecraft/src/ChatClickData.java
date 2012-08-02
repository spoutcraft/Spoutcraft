package net.minecraft.src;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatClickData {
	public static final Pattern pattern = Pattern.compile("^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,3})(/\\S*)?$");
	private final FontRenderer fontR;
	private final ChatLine line;
	private final int field_78312_d;
	private final int field_78313_e;
	private final String field_78310_f;
	private final String field_78311_g;

	public ChatClickData(FontRenderer par1FontRenderer, ChatLine par2ChatLine, int par3, int par4) {
		this.fontR = par1FontRenderer;
		this.line = par2ChatLine;
		this.field_78312_d = par3;
		this.field_78313_e = par4;
		this.field_78310_f = par1FontRenderer.trimStringToWidth(par2ChatLine.func_74538_a(), par3);
		this.field_78311_g = this.func_78307_h();
	}

	public String func_78309_f() {
		return this.field_78311_g;
	}

	public URI getURI() {
		String var1 = this.func_78309_f();
		if (var1 == null) {
			return null;
		} else {
			Matcher var2 = pattern.matcher(var1);
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

	private String func_78307_h() {
		int var1 = this.field_78310_f.lastIndexOf(" ", this.field_78310_f.length()) + 1;
		if (var1 < 0) {
			var1 = 0;
		}

		int var2 = this.line.func_74538_a().indexOf(" ", var1);
		if (var2 < 0) {
			var2 = this.line.func_74538_a().length();
		}

		FontRenderer var10000 = this.fontR;
		return StringUtils.func_76338_a(this.line.func_74538_a().substring(var1, var2));
	}
	
	//Spout start
	public String getMessage() {
		return line.func_74538_a();
	}
	//Spout end
}