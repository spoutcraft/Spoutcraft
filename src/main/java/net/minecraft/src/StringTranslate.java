package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.IllegalFormatException;
import java.util.Properties;
import java.util.TreeMap;

public class StringTranslate {

	/** Is the private singleton instance of StringTranslate. */
	private static StringTranslate instance = new StringTranslate("en_US");

	/**
	 * Contains all key/value pairs to be translated - is loaded from '/lang/en_US.lang' when the StringTranslate is
	 * created.
	 */
	private Properties translateTable = new Properties();
	private TreeMap languageList;
	private String currentLanguage;
	private boolean isUnicode;

	public StringTranslate(String par1Str) {
		this.loadLanguageList();
		this.setLanguage(par1Str);
	}

	/**
	 * Return the StringTranslate singleton instance
	 */
	public static StringTranslate getInstance() {
		return instance;
	}

	private void loadLanguageList() {
		TreeMap var1 = new TreeMap();

		try {
			BufferedReader var2 = new BufferedReader(new InputStreamReader(StringTranslate.class.getResourceAsStream("/lang/languages.txt"), "UTF-8"));

			for (String var3 = var2.readLine(); var3 != null; var3 = var2.readLine()) {
				String[] var4 = var3.split("=");

				if (var4 != null && var4.length == 2) {
					var1.put(var4[0], var4[1]);
				}
			}
		} catch (Exception var5) { // Spout IOException -> Exception
			//var5.printStackTrace(); // Spout
			return;
		}

		this.languageList = var1;
		this.languageList.put("en_US", "English (US)");
	}

	public TreeMap getLanguageList() {
		return this.languageList;
	}

	private void loadLanguage(Properties par1Properties, String par2Str) throws IOException {
		BufferedReader var3 = new BufferedReader(new InputStreamReader(StringTranslate.class.getResourceAsStream("/lang/" + par2Str + ".lang"), "UTF-8"));

		for (String var4 = var3.readLine(); var4 != null; var4 = var3.readLine()) {
			var4 = var4.trim();

			if (!var4.startsWith("#")) {
				String[] var5 = var4.split("=");

				if (var5 != null && var5.length == 2) {
					par1Properties.setProperty(var5[0], var5[1]);
				}
			}
		}
		
		// Spout Start
		this.loadSpoutLanguage(par1Properties, par2Str);
		// Spout End
	}
	
	// Spout Start
	/**
	 * Identical to loadLanguage except looks for spout langs instead.
	 * Chose not to throw exception but catch internally so as not to interupt the existing process.
	 * @param properties
	 * @param lang
	 */
	private void loadSpoutLanguage(Properties properties, String lang) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(StringTranslate.class.getResourceAsStream("/lang/spout/" + lang + ".lang"), "UTF-8"));
		
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				line = line.trim();
				
				if (!line.startsWith("#")) {
					String[] keyValPair = line.split("=");
					
					if (keyValPair != null && keyValPair.length == 2) {
						properties.setProperty(keyValPair[0], keyValPair[1]);
					}
				}
			}
		} catch (Exception e) {
			//If a spout lang couldn't load it shouldnt be a big deal, since
			//the en_US will provide soft fallback, and I have gui's also using a hard fallback.
			System.out.println("Unable to load Spout lang: " + lang);
		}
	}
	// Spout End
	
	public void setLanguage(String par1Str) {
		if (!par1Str.equals(this.currentLanguage)) {
			Properties var2 = new Properties();

			try {
				this.loadLanguage(var2, "en_US");
			} catch (Exception var8) { // Spout IOException -> Exception
				;
			}

			this.isUnicode = false;

			if (!"en_US".equals(par1Str)) {
				try {
					this.loadLanguage(var2, par1Str);
					Enumeration var3 = var2.propertyNames();

					while (var3.hasMoreElements() && !this.isUnicode) {
						Object var4 = var3.nextElement();
						Object var5 = var2.get(var4);

						if (var5 != null) {
							String var6 = var5.toString();

							for (int var7 = 0; var7 < var6.length(); ++var7) {
								if (var6.charAt(var7) >= 256) {
									this.isUnicode = true;
									break;
								}
							}
						}
					}
				} catch (Exception var9) { // Spout IOException -> Exception
					//var9.printStackTrace(); // Spout
					return;
				}
			}

			this.currentLanguage = par1Str;
			this.translateTable = var2;
		}
	}

	public String getCurrentLanguage() {
		return this.currentLanguage;
	}

	public boolean isUnicode() {
		return this.isUnicode;
	}

	/**
	 * Translate a key to current language.
	 */
	public String translateKey(String par1Str) {
		return this.translateTable.getProperty(par1Str, par1Str);
	}
	
	// Spout Start
	/**
	 * Translate a key to a current language, with fallback if a key is not present.
	 * @param key
	 * @param fallback
	 * @return
	 */
	public String translateKey(String key, String fallback) {
		if (this.translateTable.containsKey(key)) {
			return translateKey(key);
		}
		return fallback;
	}
	// Spout End
	
	/**
	 * Translate a key to current language applying String.format()
	 */
	public String translateKeyFormat(String par1Str, Object ... par2ArrayOfObj) {
		String var3 = this.translateTable.getProperty(par1Str, par1Str);

		try {
			return String.format(var3, par2ArrayOfObj);
		} catch (IllegalFormatException var5) {
			return "Format error: " + var3;
		}
	}

	/**
	 * Translate a key with a extra '.name' at end added, is used by blocks and items.
	 */
	public String translateNamedKey(String par1Str) {
		return this.translateTable.getProperty(par1Str + ".name", "");
	}

	public static boolean isBidirectional(String par0Str) {
		return "ar_SA".equals(par0Str) || "he_IL".equals(par0Str);
	}
}
