package com.prupe.mcpatcher;

final class WeightedIndex$1 extends WeightedIndex {
	WeightedIndex$1(int x0) {
		super(x0);
	}

	public int choose(long key) {
		return this.mod(key, this.size);
	}

	public String toString() {
		return "unweighted";
	}
}
