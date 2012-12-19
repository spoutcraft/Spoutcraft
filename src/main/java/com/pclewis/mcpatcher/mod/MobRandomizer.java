package com.pclewis.mcpatcher.mod;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;

//Spout Start
import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import org.spoutcraft.client.config.Configuration;
//Spout End

public class MobRandomizer {
	private static final MCLogger logger = MCLogger.getLogger("Random Mobs");
	private static final HashMap cache = new HashMap();

	public static String randomTexture(EntityLiving var0) {
		String var1 = (String)cache.get(var0);

		if (var1 == null) {
			var1 = randomTexture(var0, var0.getTexture());
			cache.put(var0, var1);
			logger.finer("entity %s using %s", new Object[] {var0, var1});
		}

		return var1;
	}

	public static String randomTexture(EntityLiving var0, String var1) {
		if (Configuration.isRandomMobTextures()) {
			if (var1.startsWith("/mob/") && var1.endsWith(".png")) {
				ExtraInfo var2 = ExtraInfo.getInfo(var0);
				MobRuleList var3 = MobRuleList.get(var1);
				System.out.println("SpoutDebug: RandomMobs Textures Applied.");
				return var3.getSkin(ExtraInfo.access$100(var2), ExtraInfo.access$200(var2), ExtraInfo.access$300(var2), ExtraInfo.access$400(var2), ExtraInfo.access$500(var2));			
			} else {
				System.out.println("SpoutDebug: RandomMobs Textures Skipped.");
				return var1;
			}
		} else {
			return var1;
		}		
	}

	public static String randomTexture(Object var0, String var1) {
		return var0 instanceof EntityLiving ? randomTexture((EntityLiving)var0, var1) : var1;
	}

	static HashMap access$000() {
		return cache;
	}

	static MCLogger access$600() {
		return logger;
	}

    static {
        TexturePackAPI.ChangeHandler.register(new TexturePackAPI.ChangeHandler(MCPatcherUtils.RANDOM_MOBS, 2) {
            @Override
            public void onChange() { //Spout Protected > Public
        		MobRandomizer.access$000().clear();
        		MobRuleList.clear();
        		MobOverlay.reset();
            }
        });
    }
    
    public static final class ExtraInfo {
    	private static final String SKIN_TAG = "randomMobsSkin";
    	private static final String ORIG_X_TAG = "origX";
    	private static final String ORIG_Y_TAG = "origY";
    	private static final String ORIG_Z_TAG = "origZ";
    	private static final long MULTIPLIER = 25214903917L;
    	private static final long ADDEND = 11L;
    	private static final long MASK = 281474976710655L;
    	private static Method getBiomeNameAt;
    	private static final HashMap allInfo = new HashMap();
    	private static final HashMap allRefs = new HashMap();
    	private static final ReferenceQueue refQueue = new ReferenceQueue();
    	private final int entityId;
    	private final HashSet references;
    	private final long skin;
    	private final int origX;
    	private final int origY;
    	private final int origZ;
    	private String origBiome;

    	ExtraInfo(EntityLiving var1) {
    		this(var1, getSkinId(var1.entityId), (int)var1.posX, (int)var1.posY, (int)var1.posZ);
    	}

    	ExtraInfo(EntityLiving var1, long var2, int var4, int var5, int var6) {
    		this.entityId = var1.entityId;
    		this.references = new HashSet();
    		this.skin = var2;
    		this.origX = var4;
    		this.origY = var5;
    		this.origZ = var6;
    	}

    	private void setBiome() {
    		if (this.origBiome == null && getBiomeNameAt != null) {
    			try {
    				String var1 = (String)getBiomeNameAt.invoke((Object)null, new Object[] {Integer.valueOf(this.origX), Integer.valueOf(this.origY), Integer.valueOf(this.origZ)});

    				if (var1 != null) {
    					this.origBiome = var1.toLowerCase().replace(" ", "");
    				}
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
    			Reference var1;

    			for (; (var1 = refQueue.poll()) != null; allRefs.remove(var1)) {
    				ExtraInfo var2 = (ExtraInfo)allRefs.get(var1);

    				if (var2 != null) {
    					var2.references.remove(var1);

    					if (var2.references.isEmpty()) {
    						MobRandomizer.access$600().finest("removing unused ref %d", new Object[] {Integer.valueOf(var2.entityId)});
    						allInfo.remove(Integer.valueOf(var2.entityId));
    					}
    				}
    			}
    		}
    	}

    	static ExtraInfo getInfo(EntityLiving var0) {
    		HashMap var2 = allInfo;

    		synchronized (allInfo) {
    			clearUnusedReferences();
    			ExtraInfo var1 = (ExtraInfo)allInfo.get(Integer.valueOf(var0.entityId));

    			if (var1 == null) {
    				var1 = new ExtraInfo(var0);
    				putInfo(var0, var1);
    			}

    			boolean var3 = false;
    			Iterator var4 = var1.references.iterator();

    			while (var4.hasNext()) {
    				WeakReference var5 = (WeakReference)var4.next();

    				if (var5.get() == var0) {
    					var3 = true;
    					break;
    				}
    			}

    			if (!var3) {
    				WeakReference var8 = new WeakReference(var0, refQueue);
    				var1.references.add(var8);
    				allRefs.put(var8, var1);
    				MobRandomizer.access$600().finest("added ref #%d for %d (%d entities)", new Object[] {Integer.valueOf(var1.references.size()), Integer.valueOf(var0.entityId), Integer.valueOf(allInfo.size())});
    			}

    			var1.setBiome();
    			return var1;
    		}
    	}

    	static void putInfo(EntityLiving var0, ExtraInfo var1) {
    		HashMap var2 = allInfo;

    		synchronized (allInfo) {
    			allInfo.put(Integer.valueOf(var0.entityId), var1);
    		}
    	}

    	static void clearInfo() {
    		HashMap var0 = allInfo;

    		synchronized (allInfo) {
    			allInfo.clear();
    		}
    	}

    	private static long getSkinId(int var0) {
    		long var1 = (long)var0;
    		var1 = var1 ^ var1 << 16 ^ var1 << 32 ^ var1 << 48;
    		var1 = 25214903917L * var1 + 11L;
    		var1 = 25214903917L * var1 + 11L;
    		var1 &= 281474976710655L;
    		return var1 >> 32 ^ var1;
    	}

    	public static void readFromNBT(EntityLiving var0, NBTTagCompound var1) {
    		long var2 = var1.getLong("randomMobsSkin");

    		if (var2 != 0L) {
    			int var4 = var1.getInteger("origX");
    			int var5 = var1.getInteger("origY");
    			int var6 = var1.getInteger("origZ");
    			putInfo(var0, new ExtraInfo(var0, var2, var4, var5, var6));
    		}
    	}

    	public static void writeToNBT(EntityLiving var0, NBTTagCompound var1) {
    		HashMap var2 = allInfo;

    		synchronized (allInfo) {
    			ExtraInfo var3 = (ExtraInfo)allInfo.get(Integer.valueOf(var0.entityId));

    			if (var3 != null) {
    				var1.setLong("randomMobsSkin", var3.skin);
    				var1.setInteger("origX", var3.origX);
    				var1.setInteger("origY", var3.origY);
    				var1.setInteger("origZ", var3.origZ);
    			}
    		}
    	}

    	static long access$100(ExtraInfo var0) {
    		return var0.skin;
    	}

    	static int access$200(ExtraInfo var0) {
    		return var0.origX;
    	}

    	static int access$300(ExtraInfo var0) {
    		return var0.origY;
    	}

    	static int access$400(ExtraInfo var0) {
    		return var0.origZ;
    	}

    	static String access$500(ExtraInfo var0) {
    		return var0.origBiome;
    	}

    	static {
    		try {
    			Class var0 = Class.forName("com.pclewis.mcpatcher.mod.BiomeHelper");
    			getBiomeNameAt = var0.getDeclaredMethod("getBiomeNameAt", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE});
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
}
