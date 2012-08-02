package net.minecraft.src;

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
	private static final TexturePackBase field_77314_a = new TexturePackDefault();
	private final Minecraft field_77312_b;

	/** The directory the texture packs will be loaded from. */
	private final File texturePackDir;
	private final File field_77310_d;

	/** The list of the available texture packs. */
	private List availableTexturePacks = new ArrayList();
	private Map field_77308_f = new HashMap();

	/** The TexturePack that will be used. */
	private TexturePackBase selectedTexturePack;
	private boolean field_77315_h;

	public TexturePackList(File par1File, Minecraft par2Minecraft) {
		this.field_77312_b = par2Minecraft;
		this.texturePackDir = new File(par1File, "texturepacks");
		this.field_77310_d = new File(par1File, "texturepacks-mp-cache");
		this.func_77307_h();
		this.updateAvaliableTexturePacks();
	}

	private void func_77307_h() {
		if (!this.texturePackDir.isDirectory()) {
			this.texturePackDir.delete();
			this.texturePackDir.mkdirs();
		}

		if (!this.field_77310_d.isDirectory()) {
			this.field_77310_d.delete();
			this.field_77310_d.mkdirs();
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
			this.field_77312_b.gameSettings.skin = par1TexturePackBase.func_77538_c();
			this.field_77312_b.gameSettings.saveOptions();
			return true;
		}
	}

	public void func_77296_a(String par1Str) {
		String var2 = par1Str.substring(par1Str.lastIndexOf("/") + 1);

		if (var2.contains("?")) {
			var2 = var2.substring(0, var2.indexOf("?"));
		}

		if (var2.endsWith(".zip")) {
			File var3 = new File(this.field_77310_d, var2);
			this.func_77297_a(par1Str, var3);
		}
	}

	private void func_77297_a(String par1Str, File par2File) {
		HashMap var3 = new HashMap();
		GuiProgress var4 = new GuiProgress();
		var3.put("X-Minecraft-Username", this.field_77312_b.session.username);
		var3.put("X-Minecraft-Version", "1.3.1");
		var3.put("X-Minecraft-Supported-Resolutions", "16");
		this.field_77315_h = true;
		this.field_77312_b.displayGuiScreen(var4);
		HttpUtil.func_76182_a(par2File, par1Str, new TexturePackDownloadSuccess(this), var3, 10000000, var4);
	}

	public boolean func_77295_a() {
		return this.field_77315_h;
	}

	public void func_77304_b() {
		this.field_77315_h = false;
		this.updateAvaliableTexturePacks();
		this.field_77312_b.func_71395_y();
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

				if (((TexturePackBase)var5).func_77538_c().equals(this.field_77312_b.gameSettings.skin)) {
					this.selectedTexturePack = (TexturePackBase)var5;
				}

				var1.add(var5);
			}
		}

		this.availableTexturePacks.removeAll(var1);
		var2 = this.availableTexturePacks.iterator();

		while (var2.hasNext()) {
			TexturePackBase var6 = (TexturePackBase)var2.next();
			var6.func_77533_a(this.field_77312_b.renderEngine);
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

	public TexturePackBase func_77292_e() {
		return this.selectedTexturePack;
	}

	public boolean func_77300_f() {
		if (!this.field_77312_b.gameSettings.field_74356_s) {
			return false;
		} else {
			ServerData var1 = this.field_77312_b.func_71362_z();
			return var1 == null ? true : var1.func_78840_c();
		}
	}

	public boolean func_77298_g() {
		if (!this.field_77312_b.gameSettings.field_74356_s) {
			return false;
		} else {
			ServerData var1 = this.field_77312_b.func_71362_z();
			return var1 == null ? false : var1.func_78839_b();
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

	static Minecraft func_77306_b(TexturePackList par0TexturePackList) {
		return par0TexturePackList.field_77312_b;
	}
}
