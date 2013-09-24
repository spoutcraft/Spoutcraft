package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class StringTranslate {
	private static final Pattern field_111053_a = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
	private static final Splitter field_135065_b = Splitter.on('=').limit(2);
	// ToDo: missing previous Spoutcraft API code, requires re-write.
	
	/** Is the private singleton instance of StringTranslate. */
	// Spout Start
	public static StringTranslate instance = new StringTranslate();
	// Spout End
	private Map languageList = Maps.newHashMap();

	public StringTranslate() {
		try {
			InputStream var1 = StringTranslate.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
			Iterator var2 = IOUtils.readLines(var1, Charsets.UTF_8).iterator();

			while (var2.hasNext()) {
				String var3 = (String)var2.next();

				if (!var3.isEmpty() && var3.charAt(0) != 35) {
					String[] var4 = (String[])Iterables.toArray(field_135065_b.split(var3), String.class);

					if (var4 != null && var4.length == 2) {
						String var5 = var4[0];
						String var6 = field_111053_a.matcher(var4[1]).replaceAll("%$1s");
						this.languageList.put(var5, var6);
					}
				}
			}
		} catch (IOException var7) {
			;
		}
	}

	/**
	 * Return the StringTranslate singleton instance
	 */
	static StringTranslate getInstance() {
		return instance;
	}

	public static synchronized void func_135063_a(Map par0Map) {
		instance.languageList.clear();
		instance.languageList.putAll(par0Map);
	}

	/**
	 * Translate a key to current language.
	 */
	public synchronized String translateKey(String par1Str) {
		return this.func_135064_c(par1Str);
	}

	/**
	 * Translate a key to current language applying String.format()
	 */
	public synchronized String translateKeyFormat(String par1Str, Object ... par2ArrayOfObj) {
		String var3 = this.func_135064_c(par1Str);

		try {
			return String.format(var3, par2ArrayOfObj);
		} catch (IllegalFormatException var5) {
			return "Format error: " + var3;
		}
	}

	private String func_135064_c(String par1Str) {
		String var2 = (String)this.languageList.get(par1Str);
		return var2 == null ? par1Str : var2;
	}

	public synchronized boolean containsTranslateKey(String par1Str) {
		return this.languageList.containsKey(par1Str);
	}
}
