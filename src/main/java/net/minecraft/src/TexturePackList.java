package net.minecraft.src;

import com.pclewis.mcpatcher.MCPatcherUtils; // Spout HD
import com.pclewis.mcpatcher.mod.TextureUtils; // Spout HD
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class TexturePackList {
	public static final TexturePackBase field_77314_a = new TexturePackDefault(); // Spout private -> public

	/** The Minecraft instance. */
	private final Minecraft mc;

	/** The directory the texture packs will be loaded from. */
	private final File texturePackDir;

	/** Folder for the multi-player texturepacks. Returns File. */
	private final File mpTexturePackFolder;

	/** The list of the available texture packs. */
	private List availableTexturePacks = new ArrayList();
	private Map field_77308_f = new HashMap();

	/** The TexturePack that will be used. */
	public TexturePackBase selectedTexturePack; // Spout private -> public
	private boolean field_77315_h;

	public TexturePackList(File par1File, Minecraft par2Minecraft) {
		this.mc = par2Minecraft;
		this.texturePackDir = new File(par1File, "texturepacks");
		this.mpTexturePackFolder = new File(par1File, "texturepacks-mp-cache");
		this.func_77307_h();
		this.updateAvaliableTexturePacks();
	}

	public void func_77307_h() { // Spout private -> public
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
	public boolean setTexturePack(TexturePackBase par1TexturePackBase) {
		if (par1TexturePackBase == this.selectedTexturePack) {
			return false;
		} else {
			this.field_77315_h = false;
			this.selectedTexturePack = par1TexturePackBase;
			this.mc.gameSettings.skin = par1TexturePackBase.func_77538_c();
			this.mc.gameSettings.saveOptions();
			// Spout HD
			TextureUtils.setTileSize();
			Minecraft var10000 = MCPatcherUtils.getMinecraft();
			var10000.renderEngine.setTileSize(var10000);
			TextureUtils.setFontRenderer();
			// Spout HD
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
		var3.put("X-Minecraft-Version", "1.3.2");
		var3.put("X-Minecraft-Supported-Resolutions", "16");
		this.field_77315_h = true;
		this.mc.displayGuiScreen(var4);
		HttpUtil.downloadTexturePack(par2File, par1Str, new TexturePackDownloadSuccess(this), var3, 10000000, var4);
	}

	public boolean func_77295_a() {
		return this.field_77315_h;
	}

	public void func_77304_b() {
		this.field_77315_h = false;
		this.updateAvaliableTexturePacks();
		this.mc.func_71395_y();
	}

	/**
	 * check the texture packs the client has installed
	 */
	public void updateAvaliableTexturePacks() {
		ArrayList var1 = new ArrayList();
		this.selectedTexturePack = field_77314_a;
		var1.add(field_77314_a);
		Iterator var2 = this.func_77299_i().iterator();

		while (var2.hasNext()) {
			File var3 = (File)var2.next();
			String var4 = this.func_77302_a(var3);

			if (var4 != null) {
				Object var5 = (TexturePackBase)this.field_77308_f.get(var4);

				if (var5 == null) {
					var5 = var3.isDirectory() ? new TexturePackFolder(var4, var3) : new TexturePackCustom(var4, var3);
					this.field_77308_f.put(var4, var5);
				}

				if (((TexturePackBase)var5).func_77538_c().equals(this.mc.gameSettings.skin)) {
					this.selectedTexturePack = (TexturePackBase)var5;
				}

				var1.add(var5);
			}
		}

		this.availableTexturePacks.removeAll(var1);
		var2 = this.availableTexturePacks.iterator();

		while (var2.hasNext()) {
			TexturePackBase var6 = (TexturePackBase)var2.next();
			var6.func_77533_a(this.mc.renderEngine);
			this.field_77308_f.remove(var6.func_77536_b());
		}

		this.availableTexturePacks = var1;
	}

	private String func_77302_a(File par1File) {
		return par1File.isFile() && par1File.getName().toLowerCase().endsWith(".zip") ? par1File.getName() + ":" + par1File.length() + ":" + par1File.lastModified() : (par1File.isDirectory() && (new File(par1File, "pack.txt")).exists() ? par1File.getName() + ":folder:" + par1File.lastModified() : null);
	}

	private List func_77299_i() {
		return this.texturePackDir.exists() && this.texturePackDir.isDirectory() ? Arrays.asList(this.texturePackDir.listFiles()) : Collections.emptyList();
	}

	/**
	 * Returns a list of the available texture packs.
	 */
	public List availableTexturePacks() {
		return Collections.unmodifiableList(this.availableTexturePacks);
	}

	public TexturePackBase getSelectedTexturePack() {
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

	static boolean func_77301_a(TexturePackList par0TexturePackList) {
		return par0TexturePackList.field_77315_h;
	}

	static TexturePackBase func_77303_a(TexturePackList par0TexturePackList, TexturePackBase par1TexturePackBase) {
		return par0TexturePackList.selectedTexturePack = par1TexturePackBase;
	}

	static String func_77291_a(TexturePackList par0TexturePackList, File par1File) {
		return par0TexturePackList.func_77302_a(par1File);
	}

	static Minecraft getMinecraft(TexturePackList par0TexturePackList) {
		return par0TexturePackList.mc;
	}
	// Spout HD Start
	public TexturePackImplementation getSelectedTexturePackImplementation() {
		return (TexturePackImplementation)this.selectedTexturePack;
	}

	public boolean a(TexturePackImplementation var1) {
		return this.setTexturePack(var1);
	}

	public TexturePackImplementation getDefaultTexturePack() {
		return (TexturePackImplementation)field_77314_a;
	}
	// Spout HD End
}
