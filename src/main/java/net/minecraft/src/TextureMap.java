package net.minecraft.src;

import com.prupe.mcpatcher.mod.CTMUtils;
import com.prupe.mcpatcher.mod.TileLoader;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class TextureMap implements IconRegister {

	/** 0 = terrain.png, 1 = items.png */
	private final int textureType;
	private final String textureName;
	private final String basePath;
	private final String textureExt;
	private final HashMap mapTexturesStiched = new HashMap();
	private BufferedImage missingImage = new BufferedImage(64, 64, 2);
	private TextureStitched missingTextureStiched;
	private Texture atlasTexture;
	private final List listTextureStiched = new ArrayList();
	private final Map textureStichedMap = new HashMap();

	public TextureMap(int par1, String par2, String par3Str, BufferedImage par4BufferedImage) {
		this.textureType = par1;
		this.textureName = par2;
		this.basePath = par3Str;
		this.textureExt = ".png";
		this.missingImage = par4BufferedImage;
	}

	public void refreshTextures() {
		this.textureStichedMap.clear();
		int var2;
		int var3;

		if (this.textureType == 0) {
			Block[] var1 = Block.blocksList;
			var2 = var1.length;

			for (var3 = 0; var3 < var2; ++var3) {
				Block var4 = var1[var3];

				if (var4 != null) {
					var4.registerIcons(this);
				}
			}

			Minecraft.getMinecraft().renderGlobal.registerDestroyBlockIcons(this);
			RenderManager.instance.updateIcons(this);
		}

		Item[] var19 = Item.itemsList;
		var2 = var19.length;

		for (var3 = 0; var3 < var2; ++var3) {
			Item var22 = var19[var3];

			if (var22 != null && var22.getSpriteNumber() == this.textureType) {
				var22.updateIcons(this);
			}
		}

		HashMap var20 = new HashMap();
		Stitcher var21 = TextureManager.instance().createStitcher(this.textureName);
		this.mapTexturesStiched.clear();
		this.listTextureStiched.clear();
		Texture var23 = TextureManager.instance().makeTexture("missingno", 2, this.missingImage.getWidth(), this.missingImage.getHeight(), 10496, 6408, 9728, 9728, false, this.missingImage);
		StitchHolder var24 = new StitchHolder(var23);
		var21.addStitchHolder(var24);
		var20.put(var24, Arrays.asList(new Texture[] {var23}));
		Iterator var5 = this.textureStichedMap.keySet().iterator();

		while (var5.hasNext()) {
			String var6 = (String)var5.next();
			String var7 = this.basePath + var6 + this.textureExt;
			List var8 = TextureManager.instance().createTexture(var7);

			if (!var8.isEmpty()) {
				StitchHolder var9 = new StitchHolder((Texture)var8.get(0));
				var21.addStitchHolder(var9);
				var20.put(var9, var8);
			}
		}

		try {
			CTMUtils.registerIcons(this, var21, this.textureName, var20);
			var21.doStitch();
		} catch (StitcherException var18) {
			throw var18;
		}

		this.atlasTexture = var21.getTexture();
		var5 = var21.getStichSlots().iterator();

		while (var5.hasNext()) {
			StitchSlot var26 = (StitchSlot)var5.next();
			StitchHolder var27 = var26.getStitchHolder();
			Texture var28 = var27.func_98150_a();
			String var29 = var28.getTextureName();
			List var10 = (List)var20.get(var27);
			TextureStitched var11 = (TextureStitched)this.textureStichedMap.get(var29);
			boolean var12 = false;

			if (var11 == null) {
				var12 = true;
				var11 = TextureStitched.makeTextureStitched(var29);

				if (!var29.equals("missingno")) {
					Minecraft.getMinecraft().getLogAgent().logWarning("Couldn\'t find premade icon for " + var29 + " doing " + this.textureName);
				}
			}

			var11.init(this.atlasTexture, var10, var26.getOriginX(), var26.getOriginY(), var27.func_98150_a().getWidth(), var27.func_98150_a().getHeight(), var27.isRotated());
			this.mapTexturesStiched.put(var29, var11);

			if (!var12) {
				this.textureStichedMap.remove(var29);
			}

			if (var10.size() > 1) {
				this.listTextureStiched.add(var11);
				String var13 = TileLoader.getOverridePath(this.basePath, var29, ".txt");
				ITexturePack var14 = Minecraft.getMinecraft().texturePackList.getSelectedTexturePack();
				boolean var15 = !var14.func_98138_b("/" + this.basePath + var29 + ".png", false);

				try {
					InputStream var16 = var14.func_98137_a("/" + var13, var15);
					//Minecraft.getMinecraft().getLogAgent().logInfo("Found animation info for: " + var13);
					var11.readAnimationInfo(new BufferedReader(new InputStreamReader(var16)));
				} catch (IOException var17) {
					;
				}
			}
		}

		this.missingTextureStiched = (TextureStitched)this.mapTexturesStiched.get("missingno");
		var5 = this.textureStichedMap.values().iterator();

		while (var5.hasNext()) {
			TextureStitched var25 = (TextureStitched)var5.next();
			var25.copyFrom(this.missingTextureStiched);
		}

		this.atlasTexture.writeImage("debug.stitched_" + this.textureName + ".png");
		this.atlasTexture.createTexture();
	}

	public void updateAnimations() {
		Iterator var1 = this.listTextureStiched.iterator();

		while (var1.hasNext()) {
			TextureStitched var2 = (TextureStitched)var1.next();
			var2.updateAnimation();
		}
	}

	public Texture getTexture() {
		return this.atlasTexture;
	}

	public Icon registerIcon(String par1Str) {
		if (par1Str == null) {
			(new RuntimeException("Don\'t register null!")).printStackTrace();
		}

		TextureStitched var2 = (TextureStitched)this.textureStichedMap.get(par1Str);

		if (var2 == null) {
			var2 = TextureStitched.makeTextureStitched(par1Str);
			this.textureStichedMap.put(par1Str, var2);
		}

		return var2;
	}

	public Icon getMissingIcon() {
		return this.missingTextureStiched;
	}
}
