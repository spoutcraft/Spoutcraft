package com.prupe.mcpatcher;

import com.prupe.mcpatcher.WeightedIndex$1;
import com.prupe.mcpatcher.WeightedIndex$2;

public abstract class WeightedIndex {
	final int size;

	public static WeightedIndex create(int var0) {
		return var0 <= 0 ? null : new WeightedIndex$1(var0);
	}

	public static WeightedIndex create(int var0, String var1) {
		if (var0 > 0 && var1 != null) {
			int[] var2 = new int[var0];
			int var3 = 0;
			boolean var4 = false;
			String[] var5 = var1.trim().split("\\s+");

			for (int var6 = 0; var6 < var0; ++var6) {
				if (var6 < var5.length && var5[var6].matches("^\\d+$")) {
					var2[var6] = Math.max(Integer.parseInt(var5[var6]), 0);
				} else {
					var2[var6] = 1;
				}

				if (var6 > 0 && var2[var6] != var2[0]) {
					var4 = true;
				}

				var3 += var2[var6];
			}

			if (var4 && var3 > 0) {
				return new WeightedIndex$2(var0, var3, var2);
			} else {
				return create(var0);
			}
		} else {
			return create(var0);
		}
	}

	protected WeightedIndex(int var1) {
		this.size = var1;
	}

	protected final int mod(long var1, int var3) {
		return (int)((var1 >> 32 ^ var1) & 2147483647L) % var3;
	}

	public abstract int choose(long var1);
}
