package com.prupe.mcpatcher.mob;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.NBTTagCompound;

public final class MobRandomizer$ExtraInfo {
	private static final String SKIN_TAG = "randomMobsSkin";
	private static final String ORIG_X_TAG = "origX";
	private static final String ORIG_Y_TAG = "origY";
	private static final String ORIG_Z_TAG = "origZ";
	private static final long MULTIPLIER = 25214903917L;
	private static final long ADDEND = 11L;
	private static final long MASK = 281474976710655L;
	private static Method getBiomeNameAt;
	private static final HashMap<Integer, MobRandomizer$ExtraInfo> allInfo = new HashMap();
	private static final HashMap<WeakReference<EntityLivingBase>, MobRandomizer$ExtraInfo> allRefs = new HashMap();
	private static final ReferenceQueue<EntityLivingBase> refQueue = new ReferenceQueue();
	private final int entityId;
	private final HashSet<WeakReference<EntityLivingBase>> references;
	private final long skin;
	private final int origX;
	private final int origY;
	private final int origZ;
	private String origBiome;

	MobRandomizer$ExtraInfo(EntityLivingBase entity) {
		this(entity, getSkinId(entity.entityId), (int)entity.posX, (int)entity.posY, (int)entity.posZ);
	}

	MobRandomizer$ExtraInfo(EntityLivingBase entity, long skin, int origX, int origY, int origZ) {
		this.entityId = entity.entityId;
		this.references = new HashSet();
		this.skin = skin;
		this.origX = origX;
		this.origY = origY;
		this.origZ = origZ;
	}

	private void setBiome() {
		if (this.origBiome == null && getBiomeNameAt != null) {
			try {
				this.origBiome = (String)getBiomeNameAt.invoke((Object)null, new Object[] {Integer.valueOf(this.origX), Integer.valueOf(this.origY), Integer.valueOf(this.origZ)});
			} catch (Throwable var2) {
				getBiomeNameAt = null;
				var2.printStackTrace();
			}
		}
	}

	public String toString() {
		return String.format("%s{%d, %d, %d, %d, %d, %s}", new Object[] {this.getClass().getSimpleName(), Integer.valueOf(this.entityId), Long.valueOf(this.skin), Integer.valueOf(this.origX), Integer.valueOf(this.origY), Integer.valueOf(this.origZ), this.origBiome});
	}

	private static void clearUnusedReferences() {
		HashMap var0 = allInfo;

		synchronized (allInfo) {
			Reference ref;

			for (; (ref = refQueue.poll()) != null; allRefs.remove(ref)) {
				MobRandomizer$ExtraInfo info = (MobRandomizer$ExtraInfo)allRefs.get(ref);

				if (info != null) {
					info.references.remove(ref);

					if (info.references.isEmpty()) {
						MobRandomizer.access$600().finest("removing unused ref %d", new Object[] {Integer.valueOf(info.entityId)});
						allInfo.remove(Integer.valueOf(info.entityId));
					}
				}
			}
		}
	}

	static MobRandomizer$ExtraInfo getInfo(EntityLivingBase entity) {
		HashMap var2 = allInfo;

		synchronized (allInfo) {
			clearUnusedReferences();
			MobRandomizer$ExtraInfo info = (MobRandomizer$ExtraInfo)allInfo.get(Integer.valueOf(entity.entityId));

			if (info == null) {
				info = new MobRandomizer$ExtraInfo(entity);
				putInfo(entity, info);
			}

			boolean found = false;
			Iterator reference = info.references.iterator();

			while (reference.hasNext()) {
				WeakReference ref = (WeakReference)reference.next();

				if (ref.get() == entity) {
					found = true;
					break;
				}
			}

			if (!found) {
				WeakReference reference1 = new WeakReference(entity, refQueue);
				info.references.add(reference1);
				allRefs.put(reference1, info);
				MobRandomizer.access$600().finest("added ref #%d for %d (%d entities)", new Object[] {Integer.valueOf(info.references.size()), Integer.valueOf(entity.entityId), Integer.valueOf(allInfo.size())});
			}

			info.setBiome();
			return info;
		}
	}

	static void putInfo(EntityLivingBase entity, MobRandomizer$ExtraInfo info) {
		HashMap var2 = allInfo;

		synchronized (allInfo) {
			allInfo.put(Integer.valueOf(entity.entityId), info);
		}
	}

	static void clearInfo() {
		HashMap var0 = allInfo;

		synchronized (allInfo) {
			allInfo.clear();
		}
	}

	private static long getSkinId(int entityId) {
		long n = (long)entityId;
		n = n ^ n << 16 ^ n << 32 ^ n << 48;
		n = 25214903917L * n + 11L;
		n = 25214903917L * n + 11L;
		n &= 281474976710655L;
		return n >> 32 ^ n;
	}

	public static void readFromNBT(EntityLivingBase entity, NBTTagCompound nbt) {
		long skin = nbt.getLong("randomMobsSkin");

		if (skin != 0L) {
			int x = nbt.getInteger("origX");
			int y = nbt.getInteger("origY");
			int z = nbt.getInteger("origZ");
			putInfo(entity, new MobRandomizer$ExtraInfo(entity, skin, x, y, z));
		}
	}

	public static void writeToNBT(EntityLivingBase entity, NBTTagCompound nbt) {
		HashMap var2 = allInfo;

		synchronized (allInfo) {
			MobRandomizer$ExtraInfo info = (MobRandomizer$ExtraInfo)allInfo.get(Integer.valueOf(entity.entityId));

			if (info != null) {
				nbt.setLong("randomMobsSkin", info.skin);
				nbt.setInteger("origX", info.origX);
				nbt.setInteger("origY", info.origY);
				nbt.setInteger("origZ", info.origZ);
			}
		}
	}

	static long access$100(MobRandomizer$ExtraInfo x0) {
		return x0.skin;
	}

	static int access$200(MobRandomizer$ExtraInfo x0) {
		return x0.origX;
	}

	static int access$300(MobRandomizer$ExtraInfo x0) {
		return x0.origY;
	}

	static int access$400(MobRandomizer$ExtraInfo x0) {
		return x0.origZ;
	}

	static String access$500(MobRandomizer$ExtraInfo x0) {
		return x0.origBiome;
	}

	static {
		try {
			Class e = Class.forName("com.prupe.mcpatcher.cc.BiomeHelper");
			getBiomeNameAt = e.getDeclaredMethod("getBiomeNameAt", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE});
			getBiomeNameAt.setAccessible(true);
		} catch (Throwable var1) {
			;
		}

		if (getBiomeNameAt == null) {
			MobRandomizer.access$600().warning("biome integration failed", new Object[0]);
		} else {
			MobRandomizer.access$600().fine("biome integration active", new Object[0]);
		}
	}
}
