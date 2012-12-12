package com.pclewis.mcpatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TexturePackAPI {
    private static final MCLogger logger = MCLogger.getLogger("Texture Pack");

    public static TexturePackAPI instance = new TexturePackAPI();
    public static boolean loadFontFromTexturePack;

    private static final ArrayList<Field> textureMapFields = new ArrayList<Field>();

    private static TexturePackImplementation texturePack;

    static {
        try {
            for (Field field : RenderEngine.class.getDeclaredFields()) {
                if (HashMap.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    textureMapFields.add(field);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static TexturePackImplementation getTexturePack() {
        return texturePack;
    }

    static TexturePackImplementation getCurrentTexturePack() {
        Minecraft minecraft = MCPatcherUtils.getMinecraft();
        if (minecraft == null) {
            return null;
        }
        TexturePackList texturePackList = minecraft.texturePackList;
        if (texturePackList == null) {
            return null;
        }
        return texturePackList.getSelectedTexturePack();
    }

    public static boolean isDefaultTexturePack() {
        return getTexturePack() instanceof TexturePackDefault;
    }

    public static String[] parseTextureName(String s) {
        String[] result = new String[]{null, s};
        if (s.startsWith("##")) {
            result[0] = "##";
            result[1] = s.substring(2);
        } else if (s.startsWith("%")) {
            int index = s.indexOf('%', 1);
            if (index > 0) {
                result[0] = s.substring(0, index + 1);
                result[1] = s.substring(index + 1);
            }
        }
        return result;
    }

    public static InputStream getInputStream(String s) {
        return instance.getInputStreamImpl(s);
    }

    public static boolean hasResource(String s) {
        if (s.endsWith(".png")) {
            return getImage(s) != null;
        } else if (s.endsWith(".properties")) {
            return getProperties(s) != null;
        } else {
            InputStream is = getInputStream(s);
            MCPatcherUtils.close(is);
            return is != null;
        }
    }

    public static BufferedImage getImage(String s) {
        return instance.getImageImpl(s);
    }

    public static BufferedImage getImage(Object o, String s) {
        return getImage(s);
    }

    public static Properties getProperties(String s) {
        Properties properties = new Properties();
        if (getProperties(s, properties)) {
            return properties;
        } else {
            return null;
        }
    }

    public static boolean getProperties(String s, Properties properties) {
        return instance.getPropertiesImpl(s, properties);
    }

    public static String[] listResources(String directory, String suffix) {
        if (directory == null) {
            directory = "";
        }
        if (directory.startsWith("/")) {
            directory = directory.substring(1);
        }
        if (suffix == null) {
            suffix = "";
        }

        ArrayList<String> resources = new ArrayList<String>();
        if (texturePack instanceof TexturePackDefault) {
            // nothing
        } else if (texturePack instanceof TexturePackCustom) {
            ZipFile zipFile = ((TexturePackCustom) texturePack).texturePackZipFile;
            if (zipFile != null) {
                for (ZipEntry entry : Collections.list(zipFile.entries())) {
                    final String name = entry.getName();
                    if (name.startsWith(directory) && name.endsWith(suffix)) {
                        resources.add("/" + name);
                    }
                }
            }
        } else if (texturePack instanceof TexturePackFolder) {
            File folder = ((TexturePackFolder) texturePack).getFolder();
            if (folder != null && folder.isDirectory()) {
                String[] list = new File(folder, directory).list();
                if (list != null) {
                    for (String s : list) {
                        if (s.endsWith(suffix)) {
                            resources.add("/" + new File(new File(directory), s).getPath().replace('\\', '/'));
                        }
                    }
                }
            }
        }

        Collections.sort(resources);
        return resources.toArray(new String[resources.size()]);
    }

    public static int getTextureIfLoaded(String s) {
        RenderEngine renderEngine = MCPatcherUtils.getMinecraft().renderEngine;
        for (Field field : textureMapFields) {
            try {
                HashMap map = (HashMap) field.get(renderEngine);
                if (map != null) {
                    Object value = map.get(s);
                    if (value instanceof Integer) {
                        return (Integer) value;
                    }
                }
            } catch (IllegalAccessException e) {
            }
        }
        return -1;
    }

    public static boolean isTextureLoaded(String s) {
        return getTextureIfLoaded(s) >= 0;
    }

    public static int unloadTexture(String s) {
        int texture = getTextureIfLoaded(s);
        if (texture >= 0) {
            logger.finest("unloading texture %s", s);
            RenderEngine renderEngine = MCPatcherUtils.getMinecraft().renderEngine;
            renderEngine.deleteTexture(texture);
            for (Field field : textureMapFields) {
                try {
                    HashMap map = (HashMap) field.get(renderEngine);
                    if (map != null) {
                        map.remove(s);
                    }
                } catch (IllegalAccessException e) {
                }
            }
        }
        return texture;
    }

    public static String getTextureName(int texture) {
        if (texture >= 0) {
            RenderEngine renderEngine = MCPatcherUtils.getMinecraft().renderEngine;
            for (Field field : textureMapFields) {
                try {
                    HashMap map = (HashMap) field.get(renderEngine);
                    for (Object o : map.entrySet()) {
                        Map.Entry entry = (Map.Entry) o;
                        Object value = entry.getValue();
                        Object key = entry.getKey();
                        if (value instanceof Integer && key instanceof String && (Integer) value == texture) {
                            return (String) key;
                        }
                    }
                } catch (IllegalAccessException e) {
                }
            }
        }
        return null;
    }

    protected InputStream getInputStreamImpl(String s) {
        s = parseTextureName(s)[1];
        if (!loadFontFromTexturePack && s.startsWith("/font/")) {
            return TexturePackAPI.class.getResourceAsStream(s);
        } else if (texturePack == null) {
            TexturePackImplementation currentTexturePack = getCurrentTexturePack();
            if (currentTexturePack == null) {
                return TexturePackAPI.class.getResourceAsStream(s);
            } else {
                return currentTexturePack.getResourceAsStream(s);
            }
        } else {
            return texturePack.getResourceAsStream(s);
        }
    }

    protected BufferedImage getImageImpl(String s) {
        InputStream input = getInputStream(s);
        BufferedImage image = null;
        if (input != null) {
            try {
                image = ImageIO.read(input);
            } catch (IOException e) {
                logger.severe("could not read %s", s);
                e.printStackTrace();
            } finally {
                MCPatcherUtils.close(input);
            }
        }
        return image;
    }

    protected boolean getPropertiesImpl(String s, Properties properties) {
        if (properties != null) {
            InputStream input = getInputStream(s);
            try {
                if (input != null) {
                    properties.load(input);
                    return true;
                }
            } catch (IOException e) {
                logger.severe("could not read %s");
                e.printStackTrace();
            } finally {
                MCPatcherUtils.close(input);
            }
        }
        return false;
    }

    abstract public static class ChangeHandler {
        private static final ArrayList<ChangeHandler> handlers = new ArrayList<ChangeHandler>();
        private static boolean changing;

        private static final boolean autoRefreshTextures = MCPatcherUtils.getBoolean("autoRefreshTextures", false);
        private static long lastCheckTime;

        protected final String name;
        protected final int order;

        protected ChangeHandler(String name, int order) {
            this.name = name;
            this.order = order;
        }

        abstract protected void onChange();

        public static void register(ChangeHandler handler) {
            if (handler != null) {
                if (texturePack != null) {
                    try {
                        logger.info("initializing %s...", handler.name);
                        handler.onChange();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        logger.severe("%s initialization failed", handler.name);
                    }
                }
                handlers.add(handler);
                logger.fine("registered texture pack handler %s, priority %d", handler.name, handler.order);
                Collections.sort(handlers, new Comparator<ChangeHandler>() {
                    public int compare(ChangeHandler o1, ChangeHandler o2) {
                        return o1.order - o2.order;
                    }
                });
            }
        }

        public static void checkForTexturePackChange() {
            Minecraft minecraft = MCPatcherUtils.getMinecraft();
            if (minecraft == null) {
                return;
            }
            TexturePackList texturePackList = minecraft.texturePackList;
            if (texturePackList == null) {
                return;
            }
            TexturePackImplementation currentTexturePack = texturePackList.getSelectedTexturePack();
            if (currentTexturePack != texturePack) {
                changeTexturePack(currentTexturePack);
            } else if (currentTexturePack instanceof TexturePackCustom) {
                checkFileChange(texturePackList, (TexturePackCustom) currentTexturePack);
            }
        }

        private static void changeTexturePack(TexturePackImplementation newPack) {
            if (newPack != null && !changing) {
                changing = true;
                long timeDiff = -System.currentTimeMillis();
                Runtime runtime = Runtime.getRuntime();
                long memDiff = -(runtime.totalMemory() - runtime.freeMemory());

                if (texturePack == null) {
                    logger.info("\nsetting texture pack to %s", newPack.getTexturePackFileName());
                } else if (texturePack == newPack) {
                    logger.info("\nreloading texture pack %s", newPack.getTexturePackFileName());
                } else {
                    logger.info("\nchanging texture pack from %s to %s", texturePack.getTexturePackFileName(), newPack.getTexturePackFileName());
                }

                texturePack = newPack;
                for (ChangeHandler handler : handlers) {
                    try {
                        logger.info("refreshing %s...", handler.name);
                        handler.onChange();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        logger.severe("%s refresh failed", handler.name);
                    }
                }

                System.gc();
                timeDiff += System.currentTimeMillis();
                memDiff += runtime.totalMemory() - runtime.freeMemory();
                logger.info("done (%.3fs elapsed, mem usage %+.1fMB)\n", timeDiff / 1000.0, memDiff / 1048576.0);
                changing = false;
            }
        }

        private static boolean openTexturePackFile(TexturePackCustom pack) {
            if (pack.texturePackZipFile == null) {
                return false;
            }
            if (pack.texturePackFile.exists()) {
                return true;
            }
            InputStream input = null;
            OutputStream output = null;
            ZipFile newZipFile = null;
            try {
                pack.lastModified = pack.texturePackFile.lastModified();
                pack.tmpFile = File.createTempFile("tmpmc", ".zip");
                pack.tmpFile.deleteOnExit();
                MCPatcherUtils.close(pack.texturePackZipFile);
                input = new FileInputStream(pack.texturePackFile);
                output = new FileOutputStream(pack.tmpFile);
                byte[] buffer = new byte[65536];
                while (true) {
                    int nread = input.read(buffer);
                    if (nread <= 0) {
                        break;
                    }
                    output.write(buffer, 0, nread);
                }
                MCPatcherUtils.close(input);
                MCPatcherUtils.close(output);
                newZipFile = new ZipFile(pack.tmpFile);
                pack.origZip = pack.texturePackZipFile;
                pack.texturePackZipFile = newZipFile;
                newZipFile = null;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                MCPatcherUtils.close(input);
                MCPatcherUtils.close(output);
                MCPatcherUtils.close(newZipFile);
            }
            return true;
        }

        private static void closeTexturePackFile(TexturePackCustom pack) {
            if (pack.origZip != null) {
                MCPatcherUtils.close(pack.texturePackZipFile);
                pack.texturePackZipFile = pack.origZip;
                pack.origZip = null;
                pack.tmpFile.delete();
                pack.tmpFile = null;
            }
        }

        private static boolean checkFileChange(TexturePackList list, TexturePackCustom pack) {
            if (!autoRefreshTextures || !openTexturePackFile(pack)) {
                return false;
            }
            long now = System.currentTimeMillis();
            if (now - lastCheckTime < 1000L) {
                return false;
            }
            lastCheckTime = now;
            long lastModified = pack.texturePackFile.lastModified();
            if (lastModified == pack.lastModified || lastModified == 0 || pack.lastModified == 0) {
                return false;
            }
            ZipFile tmpZip = null;
            try {
                tmpZip = new ZipFile(pack.texturePackFile);
            } catch (IOException e) {
                // file is still being written
                return false;
            } finally {
                MCPatcherUtils.close(tmpZip);
            }
            closeTexturePackFile(pack);
            list.updateAvaliableTexturePacks();
            return true;
        }
    }
}
