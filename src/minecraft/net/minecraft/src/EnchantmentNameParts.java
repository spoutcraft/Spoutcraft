package net.minecraft.src;

import java.util.Random;

public class EnchantmentNameParts {
	public static final EnchantmentNameParts field_40253_a = new EnchantmentNameParts();
	private Random field_40251_b;
	private String field_40252_c[];

	private EnchantmentNameParts() {
		field_40251_b = new Random();
		field_40252_c = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale ".split(" ");
	}

	public String func_40249_a() {
		int i = field_40251_b.nextInt(2) + 3;
		String s = "";
		for (int j = 0; j < i; j++) {
			if (j > 0) {
				s = (new StringBuilder()).append(s).append(" ").toString();
			}
			s = (new StringBuilder()).append(s).append(field_40252_c[field_40251_b.nextInt(field_40252_c.length)]).toString();
		}

		return s;
	}

	public void func_40250_a(long l) {
		field_40251_b.setSeed(l);
	}
}
