package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
// MCPatcher Start
import com.prupe.mcpatcher.TexturePackAPI;
// MCPatcher End
import com.prupe.mcpatcher.TexturePackChangeHandler;

public class TexturePackList {

	/**
	 * An instance of TexturePackDefault for the always available builtin texture pack.
	 */
	private static final ITexturePack defaultTexturePack = new TexturePackDefault();

	/** The Minecraft instance. */
	private final Minecraft mc;

	/** The directory the texture packs will be loaded from. */
	private final File texturePackDir;

	/** Folder for the multi-player texturepacks. Returns File. */
	private final File mpTexturePackFolder;

	/** The list of the available texture packs. */
	private List availableTexturePacks = new ArrayList();

	/**
	 * A mapping of texture IDs to TexturePackBase objects used by updateAvaliableTexturePacks() to avoid reloading texture
	 * packs that haven't changed on disk.
	 */
	private Map texturePackCache = new HashMap();

	/** The TexturePack that will be used. */
	// Spout Start - private to public
	public ITexturePack selectedTexturePack;
	// Spout End

	/** True if a texture pack is downloading in the background. */
	private boolean isDownloading;

	public TexturePackList(File par1File, Minecraft par2Minecraft) {
		this.mc = par2Minecraft;
		this.texturePackDir = new File(par1File, "texturepacks");
		this.mpTexturePackFolder = new File(par1File, "texturepacks-mp-cache");
		this.createTexturePackDirs();
		this.updateAvaliableTexturePacks();
	}

	/**
	 * Create the "texturepacks" and "texturepacks-mp-cache" directories if they don't already exist.
	 */
	// Spout Start - private to public
	public void createTexturePackDirs() {
	// Spout End
		if (!this.texturePackDir.isDirectory()) {
			this.texturePackDir.delete();
			this.texturePackDir.mkdirs();
		}

		if (!this.mpTexturePackFolder.isDirectory()) {
			this.mpTexturePackFolder.delete();
			this.mpTexturePackFolder.mkdirs();
		}
	}

	/**
	 * Sets the new TexturePack to be used, returning true if it has actually changed, false if nothing changed.
	 */
	public boolean setTexturePack(ITexturePack par1ITexturePack) {
		if (par1ITexturePack == this.selectedTexturePack) {
			// Spout Start
			System.out.println("TexturePackList: Returned same texture pack");
			// Spout End
			return false;
		} else {
			// Spout Start
			System.out.println("TexturePackList: Different texture pack selected");
			// Spout End
			this.isDownloading = false;
			this.selectedTexturePack = par1ITexturePack;
			this.mc.gameSettings.skin = par1ITexturePack.getTexturePackFileName();
			this.mc.gameSettings.saveOptions();
			// MCPatcher Start
			TexturePackChangeHandler.scheduleTexturePackRefresh();
			// MCPatcher End
			return true;
		}
	}

	/**
	 * filename must end in .zip
	 */
	public void requestDownloadOfTexture(String par1Str) {
		String var2 = par1Str.substring(par1Str.lastIndexOf("/") + 1);

		if (var2.contains("?")) {
			var2 = var2.substring(0, var2.indexOf("?"));
		}

		if (var2.endsWith(".zip")) {
			File var3 = new File(this.mpTexturePackFolder, var2);
			this.downloadTexture(par1Str, var3);
		}
	}

	private void downloadTexture(String par1Str, File par2File) {
		HashMap var3 = new HashMap();
		GuiProgress var4 = new GuiProgress();
		var3.put("X-Minecraft-Username", this.mc.session.username);
		var3.put("X-Minecraft-Version", "1.5.2");
		var3.put("X-Minecraft-Supported-Resolutions", "16");
		this.isDownloading = true;
		this.mc.displayGuiScreen(var4);
		HttpUtil.downloadTexturePack(par2File, par1Str, new TexturePackDownloadSuccess(this), var3, 10000000, var4);
	}

	/**
	 * Return true if a texture pack is downloading in the background.
	 */
	public boolean getIsDownloading() {
		return this.isDownloading;
	}

	/**
	 * Called from Minecraft.loadWorld() if getIsDownloading() returned true to prepare the downloaded texture for usage.
	 */
	public void onDownloadFinished() {
		this.isDownloading = false;
		this.updateAvaliableTexturePacks();
		this.mc.scheduleTexturePackRefresh();
	}

	/**
	 * check the texture packs the client has installed
	 */
	public void updateAvaliableTexturePacks() {
		ArrayList var1 = new ArrayList();
		this.selectedTexturePack = defaultTexturePack;
		var1.add(defaultTexturePack);
		Iterator var2 = this.getTexturePackDirContents().iterator();

		while (var2.hasNext()) {
			File var3 = (File)var2.next();
			String var4 = this.generateTexturePackID(var3);

			if (var4 != null) {
				Object var5 = (ITexturePack)this.texturePackCache.get(var4);

				if (var5 == null) {
					var5 = var3.isDirectory() ? new TexturePackFolder(var4, var3, defaultTexturePack) : new TexturePackCustom(var4, var3, defaultTexturePack);
					this.texturePackCache.put(var4, var5);
				}

				if (((ITexturePack)var5).getTexturePackFileName().equals(this.mc.gameSettings.skin)) {
					this.selectedTexturePack = (ITexturePack)var5;
				}

				var1.add(var5);
			}
		}

		this.availableTexturePacks.removeAll(var1);
		var2 = this.availableTexturePacks.iterator();

		while (var2.hasNext()) {
			ITexturePack var6 = (ITexturePack)var2.next();
			var6.deleteTexturePack(this.mc.renderEngine);
			this.texturePackCache.remove(var6.getTexturePackID());
		}

		this.availableTexturePacks = var1;
		// MCPatcher Start - This isn't needed, don't force texture pack unless asked to do so.
		//TexturePackAPI.ChangeHandler.checkForTexturePackChange();
		// MCPatcher End
	}

	/**
	 * Generate an internal texture pack ID from the file/directory name, last modification time, and file size. Returns
	 * null if the file/directory is not a texture pack.
	 */
	private String generateTexturePackID(File par1File) {
		return par1File.isFile() && par1File.getName().toLowerCase().endsWith(".zip") ? par1File.getName() + ":" + par1File.length() + ":" + par1File.lastModified() : (par1File.isDirectory() && (new File(par1File, "pack.txt")).exists() ? par1File.getName() + ":folder:" + par1File.lastModified() : null);
	}

	/**
	 * Return a List<File> of file/directories in the texture pack directory.
	 */
	private List getTexturePackDirContents() {
		return this.texturePackDir.exists() && this.texturePackDir.isDirectory() ? Arrays.asList(this.texturePackDir.listFiles()) : Collections.emptyList();
	}

	/**
	 * Returns a list of the available texture packs.
	 */
	public List availableTexturePacks() {
		return Collections.unmodifiableList(this.availableTexturePacks);
	}

	public ITexturePack getSelectedTexturePack() {
		return this.selectedTexturePack;
	}

	public boolean func_77300_f() {
		if (!this.mc.gameSettings.serverTextures) {
			return false;
		} else {
			ServerData var1 = this.mc.getServerData();
			return var1 == null ? true : var1.func_78840_c();
		}
	}

	public boolean getAcceptsTextures() {
		if (!this.mc.gameSettings.serverTextures) {
			return false;
		} else {
			ServerData var1 = this.mc.getServerData();
			return var1 == null ? false : var1.getAcceptsTextures();
		}
	}

	static boolean isDownloading(TexturePackList par0TexturePackList) {
		return par0TexturePackList.isDownloading;
	}

	/**
	 * Set the selectedTexturePack field (Inner class static accessor method).
	 */
	static ITexturePack setSelectedTexturePack(TexturePackList par0TexturePackList, ITexturePack par1ITexturePack) {
		return par0TexturePackList.selectedTexturePack = par1ITexturePack;
	}

	/**
	 * Generate an internal texture pack ID from the file/directory name, last modification time, and file size. Returns
	 * null if the file/directory is not a texture pack. (Inner class static accessor method).
	 */
	static String generateTexturePackID(TexturePackList par0TexturePackList, File par1File) {
		return par0TexturePackList.generateTexturePackID(par1File);
	}

	public static ITexturePack func_98143_h() { //Spout protected -> public
		return defaultTexturePack;
	}

	static Minecraft getMinecraft(TexturePackList par0TexturePackList) {
		return par0TexturePackList.mc;
	}

	// MCPatcher Start - Modified
	public TexturePackImplementation getSelectedTexturePackImplementation() {
		return (TexturePackImplementation)this.selectedTexturePack;
	}

	public boolean a(TexturePackImplementation var1) {
		return this.setTexturePack(var1);
	}

	public TexturePackImplementation getDefaultTexturePack() {
		return (TexturePackImplementation)defaultTexturePack;
	}
	// MCPatcher End

	// Spout Start
	public static int getTileSize(TexturePackImplementation var0) {
		//TODO broken
		return 16;
		/*int var1 = 0;
		Iterator var2 = expectedColumns.entrySet().iterator();
		while (var2.hasNext()) {
			Entry var3 = (Entry)var2.next();
			InputStream var4 = null;
			try {
				var4 = getResourceAsStream(var0, (String)var3.getKey());
				if (var4 != null) {
					BufferedImage var5 = ImageIO.read(var4);
					int var6 = var5.getWidth() / ((Integer)var3.getValue()).intValue();
					var1 = Math.max(var1, var6);
				}
			} catch (Exception var10) {
			} finally {
				MCPatcherUtils.close(var4);
			}
		}
		return var1 > 0 ? var1 : 16; */
	}
	// Spout End
}
