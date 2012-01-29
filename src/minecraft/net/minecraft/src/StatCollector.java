package net.minecraft.src;

public class StatCollector {
	private static StringTranslate localizedName = StringTranslate.getInstance();

	public StatCollector() {
	}

	public static String translateToLocal(String s) {
		return localizedName.translateKey(s);
	}

	public static String translateToLocalFormatted(String s, Object aobj[]) {
		return localizedName.translateKeyFormat(s, aobj);
	}
}
