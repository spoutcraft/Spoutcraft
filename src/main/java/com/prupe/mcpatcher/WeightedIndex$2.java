package com.prupe.mcpatcher;

final class WeightedIndex$2 extends WeightedIndex {
	final int val$sum;

	final int[] val$weights;

	WeightedIndex$2(int x0, int var2, int[] var3) {
		super(x0);
		this.val$sum = var2;
		this.val$weights = var3;
	}

	public int choose(long key) {
		int m = this.mod(key, this.val$sum);
		int index;

		for (index = 0; index < this.size - 1 && m >= this.val$weights[index]; ++index) {
			m -= this.val$weights[index];
		}

		return index;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("%(");

		for (int i = 0; i < this.val$weights.length; ++i) {
			if (i > 0) {
				sb.append(", ");
			}

			sb.append(String.format("%.1f", new Object[] {Double.valueOf(100.0D * (double)this.val$weights[i] / (double)this.val$sum)}));
		}

		sb.append(")");
		return sb.toString();
	}
}
