package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeMap;

public class StringTranslate {

	private static StringTranslate instance = new StringTranslate();
	private Properties translateTable = new Properties();
	private TreeMap field_44027_c;
	private String field_44026_d;
	private boolean field_46111_e;

	private StringTranslate() {
		this.func_44021_d();
		this.func_44023_a("en_US");
	}

	public static StringTranslate getInstance() {
		return instance;
	}

	private void func_44021_d() {
		TreeMap var1 = new TreeMap();

		try {
			BufferedReader var2 = new BufferedReader(new InputStreamReader(StringTranslate.class.getResourceAsStream("/lang/languages.txt"), "UTF-8"));

			for (String var3 = var2.readLine(); var3 != null; var3 = var2.readLine()) {
				String[] var4 = var3.split("=");
				if (var4 != null && var4.length == 2) {
					var1.put(var4[0], var4[1]);
				}
			}
		}
		catch (IOException var5) {
			var5.printStackTrace();
			return;
		}

		this.field_44027_c = var1;
	}

	public TreeMap func_44022_b() {
		return this.field_44027_c;
	}

	private void func_44025_a(Properties var1, String var2) throws Exception {//Spout IOException -> Exception
		BufferedReader var3 = new BufferedReader(new InputStreamReader(StringTranslate.class.getResourceAsStream("/lang/" + var2 + ".lang"), "UTF-8"));

		for (String var4 = var3.readLine(); var4 != null; var4 = var3.readLine()) {
			var4 = var4.trim();
			if (!var4.startsWith("#")) {
				String[] var5 = var4.split("=");
				if (var5 != null && var5.length == 2) {
					var1.setProperty(var5[0], var5[1]);
				}
			}
		}

	}

	public void func_44023_a(String var1) {
		if (!var1.equals(this.field_44026_d)) {
			Properties var2 = new Properties();

			try {
				this.func_44025_a(var2, "en_US");
			}
			catch (Exception var8) { //Spout IOException -> Exception
				;
			}

			this.field_46111_e = false;
			if (!"en_US".equals(var1)) {
				try {
					this.func_44025_a(var2, var1);
					Enumeration var3 = var2.propertyNames();

					while (var3.hasMoreElements() && !this.field_46111_e) {
						Object var4 = var3.nextElement();
						Object var5 = var2.get(var4);
						if (var5 != null) {
							String var6 = var5.toString();

							for (int var7 = 0; var7 < var6.length(); ++var7) {
								if (var6.charAt(var7) >= 256) {
									this.field_46111_e = true;
									break;
								}
							}
						}
					}
				}
				catch (Exception var9) { //Spout IOException -> Exception
					var9.printStackTrace();
					return;
				}
			}

			this.field_44026_d = var1;
			this.translateTable = var2;
		}
	}

	public String func_44024_c() {
		return this.field_44026_d;
	}

	public boolean func_46110_d() {
		return this.field_46111_e;
	}

	public String translateKey(String var1) {
		return this.translateTable.getProperty(var1, var1);
	}

	public String translateKeyFormat(String var1, Object ... var2) {
		String var3 = this.translateTable.getProperty(var1, var1);
		return String.format(var3, var2);
	}

	public String translateNamedKey(String var1) {
		return this.translateTable.getProperty(var1 + ".name", "");
	}

	public static boolean func_46109_d(String var0) {
		return "ar_SA".equals(var0) || "he_IL".equals(var0);
	}

}
