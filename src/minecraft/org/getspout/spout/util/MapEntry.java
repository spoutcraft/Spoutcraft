package org.getspout.spout.util;

import java.util.Map;

class MapEntry<K,V> implements Map.Entry<K,V> {

	final private K key;
	private V value;

	MapEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		V old = this.value;
		this.value = value;
		return old;
	}

}