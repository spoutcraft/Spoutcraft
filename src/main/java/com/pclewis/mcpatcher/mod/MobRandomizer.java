package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

public class MobRandomizer {
    private static final HashMap<EntityLiving, String> cache = new HashMap<EntityLiving, String>();

    static {
        TexturePackAPI.ChangeHandler.register(new TexturePackAPI.ChangeHandler(MCPatcherUtils.RANDOM_MOBS, 2) {
            @Override
            protected void onChange() {
                cache.clear();
                MobRuleList.clear();
                MobOverlay.reset();
            }
        });
    }

    public static String randomTexture(EntityLiving entity) {
        String texture = cache.get(entity);
        if (texture == null) {
            texture = randomTexture(entity, entity.getTexture());
            cache.put(entity, texture);
        }
        return texture;
    }

    public static String randomTexture(EntityLiving entity, String texture) {
        if (!texture.startsWith("/mob/") || !texture.endsWith(".png")) {
            return texture;
        }
        ExtraInfo info = ExtraInfo.getInfo(entity);
        MobRuleList list = MobRuleList.get(texture);
        return list.getSkin(info.skin, info.origX, info.origY, info.origZ, info.origBiome);
    }

    public static String randomTexture(Object entity, String texture) {
        if (entity instanceof EntityLiving) {
            return randomTexture((EntityLiving) entity, texture);
        } else {
            return texture;
        }
    }

    public static final class ExtraInfo {
        private static final String SKIN_TAG = "randomMobsSkin";
        private static final String ORIG_X_TAG = "origX";
        private static final String ORIG_Y_TAG = "origY";
        private static final String ORIG_Z_TAG = "origZ";

        private static final long MULTIPLIER = 0x5deece66dL;
        private static final long ADDEND = 0xbL;
        private static final long MASK = (1L << 48) - 1;

        private static Method getBiomeNameAt;
        private static final HashMap<Integer, ExtraInfo> allInfo = new HashMap<Integer, ExtraInfo>();
        private static final HashMap<WeakReference<EntityLiving>, ExtraInfo> allRefs = new HashMap<WeakReference<EntityLiving>, ExtraInfo>();
        private static final ReferenceQueue<EntityLiving> refQueue = new ReferenceQueue<EntityLiving>();

        private final int entityId;
        private final HashSet<WeakReference<EntityLiving>> references;
        private final long skin;
        private final int origX;
        private final int origY;
        private final int origZ;
        private String origBiome;

        static {
            try {
                Class<?> biomeHelperClass = Class.forName(MCPatcherUtils.BIOME_HELPER_CLASS);
                getBiomeNameAt = biomeHelperClass.getDeclaredMethod("getBiomeNameAt", Integer.TYPE, Integer.TYPE, Integer.TYPE);
            } catch (Throwable e) {
            }
        }

        ExtraInfo(EntityLiving entity) {
            this(entity, getSkinId(entity.entityId), (int) entity.posX, (int) entity.posY, (int) entity.posZ);
        }

        ExtraInfo(EntityLiving entity, long skin, int origX, int origY, int origZ) {
            entityId = entity.entityId;
            references = new HashSet<WeakReference<EntityLiving>>();
            this.skin = skin;
            this.origX = origX;
            this.origY = origY;
            this.origZ = origZ;
        }

        private void setBiome() {
            if (origBiome == null && getBiomeNameAt != null) {
                try {
                    String biome = (String) getBiomeNameAt.invoke(null, origX, origY, origZ);
                    if (biome != null) {
                        origBiome = biome.toLowerCase().replace(" ", "");
                    }
                } catch (Throwable e) {
                    getBiomeNameAt = null;
                    e.printStackTrace();
                }
            }
        }

        @Override
        public String toString() {
            return String.format("%s{%d, %d, %d, %d, %d, %s}", getClass().getSimpleName(), entityId, skin, origX, origY, origZ, origBiome);
        }

        private static void clearUnusedReferences() {
            synchronized (allInfo) {
                Reference<? extends EntityLiving> ref;
                while ((ref = refQueue.poll()) != null) {
                    ExtraInfo info = allRefs.get(ref);
                    if (info != null) {
                        info.references.remove(ref);
                        if (info.references.isEmpty()) {
                            allInfo.remove(info.entityId);
                        }
                    }
                    allRefs.remove(ref);
                }
            }
        }

        static ExtraInfo getInfo(EntityLiving entity) {
            ExtraInfo info;
            synchronized (allInfo) {
                clearUnusedReferences();
                info = allInfo.get(entity.entityId);
                if (info == null) {
                    info = new ExtraInfo(entity);
                    putInfo(entity, info);
                }
                boolean found = false;
                for (WeakReference<EntityLiving> ref : info.references) {
                    if (ref.get() == entity) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    WeakReference<EntityLiving> reference = new WeakReference<EntityLiving>(entity, refQueue);
                    info.references.add(reference);
                    allRefs.put(reference, info);
                }
                info.setBiome();
            }
            return info;
        }

        static void putInfo(EntityLiving entity, ExtraInfo info) {
            synchronized (allInfo) {
                allInfo.put(entity.entityId, info);
            }
        }

        static void clearInfo() {
            synchronized (allInfo) {
                allInfo.clear();
            }
        }

        private static long getSkinId(int entityId) {
            long n = entityId;
            n = n ^ (n << 16) ^ (n << 32) ^ (n << 48);
            n = MULTIPLIER * n + ADDEND;
            n = MULTIPLIER * n + ADDEND;
            n &= MASK;
            return (n >> 32) ^ n;
        }

        public static void readFromNBT(EntityLiving entity, NBTTagCompound nbt) {
            long skin = nbt.getLong(SKIN_TAG);
            if (skin != 0L) {
                int x = nbt.getInteger(ORIG_X_TAG);
                int y = nbt.getInteger(ORIG_Y_TAG);
                int z = nbt.getInteger(ORIG_Z_TAG);
                putInfo(entity, new ExtraInfo(entity, skin, x, y, z));
            }
        }

        public static void writeToNBT(EntityLiving entity, NBTTagCompound nbt) {
            synchronized (allInfo) {
                ExtraInfo info = allInfo.get(entity.entityId);
                if (info != null) {
                    nbt.setLong(SKIN_TAG, info.skin);
                    nbt.setInteger(ORIG_X_TAG, info.origX);
                    nbt.setInteger(ORIG_Y_TAG, info.origY);
                    nbt.setInteger(ORIG_Z_TAG, info.origZ);
                }
            }
        }
    }
}
