package com.prupe.mcpatcher;

public abstract class WeightedIndex {
	private static final long K1 = -5435081209227447693L;
	private static final long KMUL = -7070675565921424023L;
	final int size;

	public static WeightedIndex create(int size) {
		return size <= 0 ? null : new WeightedIndex$1(size);
	}

	public static WeightedIndex create(int size, String weightList) {
		if (size > 0 && weightList != null) {
			int[] weights = new int[size];
			int sum1 = 0;
			boolean useWeight = false;
			String[] list = weightList.trim().split("\\s+");

			for (int sum = 0; sum < size; ++sum) {
				if (sum < list.length && list[sum].matches("^\\d+$")) {
					weights[sum] = Math.max(Integer.parseInt(list[sum]), 0);
				} else {
					weights[sum] = 1;
				}

				if (sum > 0 && weights[sum] != weights[0]) {
					useWeight = true;
				}

				sum1 += weights[sum];
			}

			if (useWeight && sum1 > 0) {
				return new WeightedIndex$2(size, sum1, weights);
			} else {
				return create(size);
			}
		} else {
			return create(size);
		}
	}

	protected WeightedIndex(int size) {
		this.size = size;
	}

	protected final int mod(long n, int modulus) {
		return (int)((n >> 32 ^ n) & 2147483647L) % modulus;
	}

	public abstract int choose(long var1);

	public static long hash128To64(int i, int j, int k, int l) {
		return hash128To64((long)i << 32 | (long)j, (long)k << 32 | (long)l);
	}

	public static long hash128To64(long a, long b) {
		a = shiftMix(a * -5435081209227447693L) * -5435081209227447693L;
		long c = b * -5435081209227447693L + mix128to64(a, b);
		long d = shiftMix(a + b);
		a = mix128to64(a, c);
		b = mix128to64(d, b);
		return a ^ b ^ mix128to64(b, a);
	}

	private static long shiftMix(long val) {
		return val ^ val >>> 47;
	}

	private static long mix128to64(long u, long v) {
		long a = shiftMix((u ^ v) * -7070675565921424023L);
		long b = shiftMix((u ^ a) * -7070675565921424023L);
		b *= -7070675565921424023L;
		return b;
	}
}
