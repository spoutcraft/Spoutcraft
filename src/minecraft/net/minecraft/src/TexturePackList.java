package net.minecraft.src;
//Spout HD Start
import com.pclewis.mcpatcher.mod.TextureUtils;
//Spout HD End
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class TexturePackList {
	private List availableTexturePacks = new ArrayList();
	public TexturePackBase defaultTexturePack = new TexturePackDefault(); //Spout private -> public
	public TexturePackBase selectedTexturePack;
	private Map field_6538_d = new HashMap();
	private Minecraft mc;
	private File texturePackDir;
	private String currentTexturePack;

	public TexturePackList(Minecraft par1Minecraft, File par2File) {
		this.mc = par1Minecraft;
		this.texturePackDir = new File(par2File, "texturepacks");
		if (!this.texturePackDir.exists()) {
			this.texturePackDir.mkdirs();
		}

		this.currentTexturePack = par1Minecraft.gameSettings.skin;
		this.updateAvaliableTexturePacks();
		this.selectedTexturePack.func_6482_a();
	}

	public boolean setTexturePack(TexturePackBase par1TexturePackBase) {
		if (par1TexturePackBase == this.selectedTexturePack) {
			return false;
		} else {
			this.selectedTexturePack.closeTexturePackFile();
			this.currentTexturePack = par1TexturePackBase.texturePackFileName;
			this.selectedTexturePack = par1TexturePackBase;
			this.mc.gameSettings.skin = this.currentTexturePack;
			this.mc.gameSettings.saveOptions();
			this.selectedTexturePack.func_6482_a();
//Spout HD Start
			TextureUtils.setTileSize();
			this.mc.renderEngine.setTileSize(this.mc);
			TextureUtils.setFontRenderer();
//Spout HD End
			return true;
		}
	}
//Spout Start
	public void updateAvaliableTexturePacks() {
		ArrayList<TexturePackBase> texturePacks = new ArrayList<TexturePackBase>();
		this.selectedTexturePack = null;
		texturePacks.add(this.defaultTexturePack);
		this.updateAvaliableTexturePacks(this.texturePackDir, texturePacks);
		this.updateAvaliableTexturePacks(new File(Minecraft.getAppDir("minecraft"), "texturepacks"), texturePacks);
		
		if(this.selectedTexturePack == null) {
			this.selectedTexturePack = this.defaultTexturePack;
		}

		this.availableTexturePacks.removeAll(texturePacks);
		Iterator i = this.availableTexturePacks.iterator();

		while(i.hasNext()) {
			TexturePackBase texturePack = (TexturePackBase)i.next();
			texturePack.unbindThumbnailTexture(this.mc);
			this.field_6538_d.remove(texturePack.texturePackID);
		}

		this.availableTexturePacks = texturePacks;
	}
	
	public void updateAvaliableTexturePacks(File directory, List<TexturePackBase> texturePacks) {
		List var1 = texturePacks;
		if(directory.exists() && directory.isDirectory()) {
			File[] var2 = directory.listFiles();

			File[] var3 = var2;
			int var4 = var2.length;

			for(int var5 = 0; var5 < var4; ++var5) {
				File var6 = var3[var5];
				if(var6.isFile() && var6.getName().toLowerCase().endsWith(".zip")) {
					String var7 = var6.getName() + ":" + var6.length() + ":" + var6.lastModified();

					try {
						if(!this.field_6538_d.containsKey(var7)) {
							TexturePackCustom var8 = new TexturePackCustom(var6);
							var8.texturePackID = var7;
							this.field_6538_d.put(var7, var8);
							var8.func_6485_a(this.mc);
						}

						TexturePackBase var12 = (TexturePackBase)this.field_6538_d.get(var7);
						if(var12.texturePackFileName.equals(this.currentTexturePack)) {
							this.selectedTexturePack = var12;
						}

						var1.add(var12);
					}
					catch (IOException var9) {
						var9.printStackTrace();
					}
				}
 				else if (var6.isDirectory() && (new File(var6, "pack.txt")).exists()) {
					String var7 = var6.getName() + ":folder:" + var6.lastModified();

					try {
						if (!this.field_6538_d.containsKey(var7)) {
							TexturePackFolder var8 = new TexturePackFolder(var6);
							var8.texturePackID = var7;
							this.field_6538_d.put(var7, var8);
							var8.func_6485_a(this.mc);
						}

						TexturePackBase var14 = (TexturePackBase)this.field_6538_d.get(var7);
						if (var14.texturePackFileName.equals(this.currentTexturePack)) {
							this.selectedTexturePack = var14;
						}

						var1.add(var14);
					} catch (IOException var9) {
						var9.printStackTrace();
					}
				}
			}
		}
	}
//Spout End
	public List availableTexturePacks() {
		return new ArrayList(this.availableTexturePacks);
	}
}
