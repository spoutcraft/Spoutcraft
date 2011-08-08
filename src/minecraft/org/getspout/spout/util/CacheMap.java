package org.getspout.spout.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class CacheMap<K,V> {
	
	private HashMap<K,SoftKeyReference<V>> cache = new HashMap<K,SoftKeyReference<V>>();
	private final ReferenceQueue<V> refQueue = new ReferenceQueue<V>();
	
	public V remove(K key) {
		SoftKeyReference<V> r = cache.remove(key);
		if(r == null) {
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
		while((r = refQueue.poll()) != null) {
			@SuppressWarnings("unchecked")
			SoftKeyReference<V> keyRef = (SoftKeyReference<V>)r;
			if(cache.get(keyRef.getKey()).equals(nullReference)) {
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
			if(o == this) {
				return true;
			}
			if(!(o instanceof SoftKeyReference)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Reference<V> s = (Reference<V>)o;
			
			return s.get() == null && get() == null;
			
		}
		
	}
	
}
