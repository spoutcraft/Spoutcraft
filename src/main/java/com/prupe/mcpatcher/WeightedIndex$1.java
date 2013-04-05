package com.prupe.mcpatcher;

final class WeightedIndex$1 extends WeightedIndex {
	WeightedIndex$1(int var1) {
		super(var1);
	}

	public int choose(long var1) {
		return this.mod(var1, this.size);
	}

	public String toString() {
		return "unweighted";
	}
}
