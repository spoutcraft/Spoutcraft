package com.prupe.mcpatcher;

final class WeightedIndex$2 extends WeightedIndex {
	final int val$sum;

	final int[] val$weights;

	WeightedIndex$2(int var1, int var2, int[] var3) {
		super(var1);
		this.val$sum = var2;
		this.val$weights = var3;
	}

	public int choose(long var1) {
		int var4 = this.mod(var1, this.val$sum);
		int var3;

		for (var3 = 0; var3 < this.size - 1 && var4 >= this.val$weights[var3]; ++var3) {
			var4 -= this.val$weights[var3];
		}

		return var3;
	}

	public String toString() {
		StringBuilder var1 = new StringBuilder();
		var1.append("%(");

		for (int var2 = 0; var2 < this.val$weights.length; ++var2) {
			if (var2 > 0) {
				var1.append(", ");
			}

			var1.append(String.format("%.1f", new Object[] {Double.valueOf(100.0D * (double)this.val$weights[var2] / (double)this.val$sum)}));
		}

		var1.append(")");
		return var1.toString();
	}
}
