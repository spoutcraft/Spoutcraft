/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class CacheMap<K,V> {
	private HashMap<K,SoftKeyReference<V>> cache = new HashMap<K,SoftKeyReference<V>>();
	private final ReferenceQueue<V> refQueue = new ReferenceQueue<V>();

	public V remove(K key) {
		SoftKeyReference<V> r = cache.remove(key);
		if (r == null) {
			return null;
		}
		return r.get();
	}

	public V put(K key, V value) {
		processReferenceQueue();
		Reference<V> old = cache.get(key);
		V oldValue = old == null ? null : old.get();
		cache.put(key, new SoftKeyReference<V>(key, value, refQueue));
		return oldValue;
	}

	public boolean contains(K key) {
		return get(key) != null;
	}

	public V get(K key) {
		processReferenceQueue();
		Reference<V> current = cache.get(key);
		return current == null ? null : current.get();
	}

	private void processReferenceQueue() {
		Reference<? extends V> r;
		while ((r = refQueue.poll()) != null) {
			@SuppressWarnings("unchecked")
			SoftKeyReference<V> keyRef = (SoftKeyReference<V>)r;
			if (cache.get(keyRef.getKey()).equals(nullReference)) {
				cache.remove(keyRef.getKey());
			}
		}
	}

	private final SoftKeyReference<V> nullReference = new SoftKeyReference<V>(null, null, null);

	private static class SoftKeyReference<V> extends SoftReference<V> {
		private final Object key;

		SoftKeyReference(Object key, V value, ReferenceQueue<V> q) {
			super(value, q);
			this.key = key;
		}

		public Object getKey() {
			return key;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof SoftKeyReference)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Reference<V> s = (Reference<V>)o;

			return s.get() == null && get() == null;
		}
	}
}
