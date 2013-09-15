package com.prupe.mcpatcher;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.AbstractResourcePack;
import net.minecraft.src.AbstractTexture;
import net.minecraft.src.DefaultResourcePack;
import net.minecraft.src.DynamicTexture;
import net.minecraft.src.FallbackResourceManager;
import net.minecraft.src.FileResourcePack;
import net.minecraft.src.Minecraft;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.ResourceManager;
import net.minecraft.src.ResourcePack;
import net.minecraft.src.SimpleReloadableResourceManager;
import net.minecraft.src.TextureManager;
import net.minecraft.src.TextureMap;
import net.minecraft.src.TextureObject;

public class TexturePackAPI {
	private static final MCLogger logger = MCLogger.getLogger("Texture Pack");
	public static final String DEFAULT_NAMESPACE = "minecraft";
	public static final String MCPATCHER_SUBDIR = "mcpatcher/";
	public static TexturePackAPI instance = new TexturePackAPI();

	public static List<ResourcePack> getResourcePacks(String namespace) {
		ArrayList list = new ArrayList();
		ResourceManager resourceManager = getResourceManager();

		if (resourceManager instanceof SimpleReloadableResourceManager) {
			Iterator i$ = ((SimpleReloadableResourceManager)resourceManager).field_110548_a.entrySet().iterator();

			while (i$.hasNext()) {
				Entry entry = (Entry)i$.next();

				if (namespace == null || namespace.equals(entry.getKey())) {
					FallbackResourceManager resourceManager1 = (FallbackResourceManager)entry.getValue();
					list.addAll(resourceManager1.field_110540_a);
				}
			}
		}

		Collections.reverse(list);
		return list;
	}

	public static ResourceManager getResourceManager() {
		return Minecraft.getMinecraft().func_110442_L();
	}

	public static boolean isDefaultTexturePack() {
		return getResourcePacks("minecraft").size() <= 1;
	}

	public static InputStream getInputStream(ResourceLocation resource) {
		return resource == null ? null : instance.getInputStreamImpl(resource);
	}

	public static boolean hasResource(ResourceLocation resource) {
		if (resource == null) {
			return false;
		} else if (resource.func_110623_a().endsWith(".png")) {
			return getImage(resource) != null;
		} else if (resource.func_110623_a().endsWith(".properties")) {
			return getProperties(resource) != null;
		} else {
			InputStream is = getInputStream(resource);
			MCPatcherUtils.close((Closeable)is);
			return is != null;
		}
	}

	public static BufferedImage getImage(ResourceLocation resource) {
		return resource == null ? null : instance.getImageImpl(resource);
	}

	public static Properties getProperties(ResourceLocation resource) {
		Properties properties = new Properties();
		return getProperties(resource, properties) ? properties : null;
	}

	public static boolean getProperties(ResourceLocation resource, Properties properties) {
		return resource != null && instance.getPropertiesImpl(resource, properties);
	}

	public static ResourceLocation transformResourceLocation(ResourceLocation resource, String oldExt, String newExt) {
		return new ResourceLocation(resource.func_110624_b(), resource.func_110623_a().replaceFirst(Pattern.quote(oldExt) + "$", newExt));
	}

	public static ResourceLocation parseResourceLocation(ResourceLocation baseResource, String path) {
		if (path != null && !path.equals("")) {
			if (path.startsWith("%blur%")) {
				path = path.substring(6);
			}

			if (path.startsWith("%clamp%")) {
				path = path.substring(7);
			}

			if (path.startsWith("/")) {
				path = path.substring(1);
			}

			if (path.startsWith("assets/minecraft/")) {
				path = path.substring(17);
			}

			int colon = path.indexOf(58);
			return colon >= 0 ? new ResourceLocation(path.substring(0, colon), path.substring(colon + 1)) : (path.startsWith("~/") ? new ResourceLocation(baseResource.func_110624_b(), "mcpatcher/" + path.substring(2)) : (path.startsWith("./") ? new ResourceLocation(baseResource.func_110624_b(), baseResource.func_110623_a().replaceFirst("[^/]+$", "") + path.substring(2)) : new ResourceLocation(baseResource.func_110624_b(), path)));
		} else {
			return null;
		}
	}

	public static ResourceLocation newMCPatcherResourceLocation(String path) {
		return new ResourceLocation("mcpatcher/" + path);
	}

	public static List<ResourceLocation> listResources(String directory, String suffix, boolean recursive, boolean directories, boolean sortByFilename) {
		if (suffix == null) {
			suffix = "";
		}

		ArrayList resources = new ArrayList();
		findResources("minecraft", directory, suffix, recursive, directories, resources);

		if (sortByFilename) {
			Collections.sort(resources, new TexturePackAPI$1());
		} else {
			Collections.sort(resources, new TexturePackAPI$2());
		}

		return resources;
	}

	private static void findResources(String namespace, String directory, String suffix, boolean recursive, boolean directories, Collection<ResourceLocation> resources) {
		Iterator i$ = getResourcePacks(namespace).iterator();

		while (i$.hasNext()) {
			ResourcePack resourcePack = (ResourcePack)i$.next();

			if (resourcePack instanceof FileResourcePack) {
				ZipFile base = ((FileResourcePack)resourcePack).field_110600_d;

				if (base != null) {
					findResources(base, namespace, "assets/" + namespace, directory, suffix, recursive, directories, resources);
				}
			} else {
				File base1;

				if (resourcePack instanceof DefaultResourcePack) {
					if ("minecraft".equals(namespace)) {
						base1 = ((DefaultResourcePack)resourcePack).field_110607_c;

						if (base1 != null && base1.isDirectory()) {
							findResources(base1, namespace, directory, suffix, recursive, directories, resources);
						}
					}
				} else if (resourcePack instanceof AbstractResourcePack) {
					base1 = ((AbstractResourcePack)resourcePack).field_110597_b;

					if (base1 != null && base1.isDirectory()) {
						base1 = new File(base1, "assets/" + namespace);

						if (base1.isDirectory()) {
							findResources(base1, namespace, directory, suffix, recursive, directories, resources);
						}
					}
				}
			}
		}
	}

	private static void findResources(ZipFile zipFile, String namespace, String root, String directory, String suffix, boolean recursive, boolean directories, Collection<ResourceLocation> resources) {
		String base = root + "/" + directory;
		Iterator i$ = Collections.list(zipFile.entries()).iterator();

		while (i$.hasNext()) {
			ZipEntry entry = (ZipEntry)i$.next();

			if (entry.isDirectory() == directories) {
				String name = entry.getName().replaceFirst("^/", "");

				if (name.startsWith(base) && name.endsWith(suffix)) {
					if (directory.equals("")) {
						if (recursive || !name.contains("/")) {
							resources.add(new ResourceLocation(namespace, name));
						}
					} else {
						String subpath = name.substring(base.length());

						if ((subpath.equals("") || subpath.startsWith("/")) && (recursive || subpath.equals("") || !subpath.substring(1).contains("/"))) {
							resources.add(new ResourceLocation(namespace, name.substring(root.length() + 1)));
						}
					}
				}
			}
		}
	}

	private static void findResources(File base, String namespace, String directory, String suffix, boolean recursive, boolean directories, Collection<ResourceLocation> resources) {
		File subdirectory = new File(base, directory);
		String[] list = subdirectory.list();

		if (list != null) {
			String pathComponent = directory.equals("") ? "" : directory + "/";
			String[] arr$ = list;
			int len$ = list.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				String s = arr$[i$];
				File entry = new File(subdirectory, s);
				String resourceName = pathComponent + s;

				if (entry.isDirectory()) {
					if (directories && s.endsWith(suffix)) {
						resources.add(new ResourceLocation(namespace, resourceName));
					}

					if (recursive) {
						findResources(base, namespace, pathComponent + s, suffix, recursive, directories, resources);
					}
				} else if (s.endsWith(suffix) && !directories) {
					resources.add(new ResourceLocation(namespace, resourceName));
				}
			}
		}
	}

	public static int getTextureIfLoaded(ResourceLocation resource) {
		if (resource == null) {
			return -1;
		} else {
			TextureObject texture = MCPatcherUtils.getMinecraft().func_110434_K().func_110581_b(resource);
			return texture instanceof AbstractTexture ? ((AbstractTexture)texture).field_110553_a : -1;
		}
	}

	public static boolean isTextureLoaded(ResourceLocation resource) {
		return getTextureIfLoaded(resource) >= 0;
	}

	public static void bindTexture(ResourceLocation resource) {
		MCPatcherUtils.getMinecraft().func_110434_K().func_110577_a(resource);
	}

	public static void bindTexture(int texture) {
		if (texture >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		}
	}

	public static void unloadTexture(ResourceLocation resource) {
		TextureManager textureManager = MCPatcherUtils.getMinecraft().func_110434_K();
		TextureObject texture = textureManager.func_110581_b(resource);

		if (texture != null && !(texture instanceof TextureMap) && !(texture instanceof DynamicTexture)) {
			if (texture instanceof AbstractTexture) {
				((AbstractTexture)texture).unloadGLTexture();
			}

			logger.finer("unloading texture %s", new Object[] {resource});
			textureManager.field_110585_a.remove(resource);
		}
	}

	public static void deleteTexture(int texture) {
		if (texture >= 0) {
			GL11.glDeleteTextures(texture);
		}
	}

	protected InputStream getInputStreamImpl(ResourceLocation resource) {
		try {
			return Minecraft.getMinecraft().func_110442_L().func_110536_a(resource).func_110527_b();
		} catch (IOException var3) {
			return null;
		}
	}

	protected BufferedImage getImageImpl(ResourceLocation resource) {
		InputStream input = getInputStream(resource);
		BufferedImage image = null;

		if (input != null) {
			try {
				image = ImageIO.read(input);
			} catch (IOException var8) {
				logger.error("could not read %s", new Object[] {resource});
				var8.printStackTrace();
			} finally {
				MCPatcherUtils.close((Closeable)input);
			}
		}

		return image;
	}

	protected boolean getPropertiesImpl(ResourceLocation resource, Properties properties) {
		if (properties != null) {
			InputStream input = getInputStream(resource);
			boolean e;

			try {
				if (input == null) {
					return false;
				}

				properties.load(input);
				e = true;
			} catch (IOException var8) {
				logger.error("could not read %s", new Object[] {resource});
				var8.printStackTrace();
				return false;
			} finally {
				MCPatcherUtils.close((Closeable)input);
			}

			return e;
		} else {
			return false;
		}
	}
}
