package net.minecraft.src;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.prupe.mcpatcher.TileLoader;
import com.prupe.mcpatcher.hd.BorderedTexture;
import com.prupe.mcpatcher.hd.MipmapHelper;

public class TextureMap extends AbstractTexture implements TickableTextureObject, IconRegister {
	public static final ResourceLocation field_110575_b = new ResourceLocation("textures/atlas/blocks.png");
	public static final ResourceLocation field_110576_c = new ResourceLocation("textures/atlas/items.png");
	private final List listTextureStiched = Lists.newArrayList();
	private final Map field_110574_e = Maps.newHashMap();
	private final Map mapTexturesStiched = Maps.newHashMap();

	/** 0 = terrain.png, 1 = items.png */
	private final int textureType;
	private final String basePath;
	private final TextureAtlasSprite missingImage = new TextureAtlasSprite("missingno");

	public TextureMap(int par1, String par2Str) {
		this.textureType = par1;
		this.basePath = par2Str;
		this.func_110573_f();
	}

	private void func_110569_e() {
		this.missingImage.func_110968_a(Lists.newArrayList(new int[][] {TextureUtil.field_110999_b}));
		this.missingImage.func_110966_b(16);
		this.missingImage.func_110969_c(16);
	}

	public void func_110551_a(ResourceManager par1ResourceManager) throws IOException {
		this.func_110569_e();
		this.func_110571_b(par1ResourceManager);
	}

	public void func_110571_b(ResourceManager par1ResourceManager) {
		int var2 = Minecraft.getGLMaximumTextureSize();
		Stitcher var3 = new Stitcher(var2, var2, true);
		this.mapTexturesStiched.clear();
		this.listTextureStiched.clear();
		this.func_110573_f();
		TileLoader.registerIcons(this, this.basePath, this.field_110574_e);
		Iterator var4 = this.field_110574_e.entrySet().iterator();

		while (var4.hasNext()) {
			Entry var5 = (Entry)var4.next();
			ResourceLocation var6 = new ResourceLocation((String)var5.getKey());
			TextureAtlasSprite var7 = (TextureAtlasSprite)var5.getValue();
			ResourceLocation var8 = new ResourceLocation(var6.func_110624_b(), TileLoader.getOverridePath(this.basePath, var6.func_110623_a(), ".png"));

			try {
				var7.func_130100_a(par1ResourceManager.func_110536_a(var8));
			} catch (RuntimeException var13) {
				Minecraft.getMinecraft().getLogAgent().logSevere(String.format("Unable to parse animation metadata from %s: %s", new Object[] {var8, var13.getMessage()}));
				continue;
			} catch (IOException var14) {
				Minecraft.getMinecraft().getLogAgent().logSevere("Using missing texture, unable to load: " + var8);
				continue;
			}

			var3.func_110934_a(var7);
		}

		var3.func_110934_a(this.missingImage);

		try {
			var3.doStitch();
		} catch (StitcherException var12) {
			throw var12;
		}

		MipmapHelper.setupTexture(this.func_110552_b(), var3.func_110935_a(), var3.func_110936_b(), this.basePath);
		HashMap var15 = Maps.newHashMap(this.field_110574_e);
		Iterator var16 = var3.getStichSlots().iterator();
		TextureAtlasSprite var17;

		while (var16.hasNext()) {
			var17 = (TextureAtlasSprite)var16.next();
			String var18 = var17.getIconName();
			var15.remove(var18);
			this.mapTexturesStiched.put(var18, var17);

			try {
				int[] var10000 = var17.func_110965_a(0);
				int var10001 = var17.getOriginX();
				int var10002 = var17.getOriginY();
				int var10003 = var17.func_130010_a();
				int var10004 = var17.func_110967_i();
				boolean var10005 = false;
				boolean var10006 = false;
				MipmapHelper.copySubTexture(var10000, var10001, var10002, var10003, var10004, this.basePath);
			} catch (Throwable var11) {
				CrashReport var9 = CrashReport.makeCrashReport(var11, "Stitching texture atlas");
				CrashReportCategory var10 = var9.makeCategory("Texture being stitched together");
				var10.addCrashSection("Atlas path", this.basePath);
				var10.addCrashSection("Sprite", var17);
				throw new ReportedException(var9);
			}

			if (var17.func_130098_m()) {
				this.listTextureStiched.add(var17);
			} else {
				var17.func_130103_l();
			}
		}

		var16 = var15.values().iterator();

		while (var16.hasNext()) {
			var17 = (TextureAtlasSprite)var16.next();
			var17.copyFrom(this.missingImage);
		}
	}

	private void func_110573_f() {
		this.field_110574_e.clear();
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

		Item[] var5 = Item.itemsList;
		var2 = var5.length;

		for (var3 = 0; var3 < var2; ++var3) {
			Item var6 = var5[var3];

			if (var6 != null && var6.getSpriteNumber() == this.textureType) {
				var6.registerIcons(this);
			}
		}
	}

	public TextureAtlasSprite func_110572_b(String par1Str) {
		TextureAtlasSprite var2 = (TextureAtlasSprite)this.mapTexturesStiched.get(par1Str);

		if (var2 == null) {
			var2 = this.missingImage;
		}

		return var2;
	}

	public void updateAnimations() {
		TextureUtil.bindTexture(this.func_110552_b());
		Iterator var1 = this.listTextureStiched.iterator();

		while (var1.hasNext()) {
			TextureAtlasSprite var2 = (TextureAtlasSprite)var1.next();
			var2.updateAnimation();
		}
	}

	public Icon registerIcon(String par1Str) {
		if (par1Str == null) {
			(new RuntimeException("Don\'t register null!")).printStackTrace();
		}

		Object var2 = (TextureAtlasSprite)this.field_110574_e.get(par1Str);

		if (var2 == null) {
			if (this.textureType == 1) {
				if (TileLoader.isSpecialTexture(this, par1Str, "clock")) {
					var2 = new TextureClock(par1Str);
				} else if (TileLoader.isSpecialTexture(this, par1Str, "compass")) {
					var2 = new TextureCompass(par1Str);
				} else {
					var2 = BorderedTexture.create(this.basePath, par1Str);
				}
			} else {
				var2 = BorderedTexture.create(this.basePath, par1Str);
			}

			this.field_110574_e.put(par1Str, var2);
		}

		return (Icon)var2;
	}

	public int func_130086_a() {
		return this.textureType;
	}

	public void func_110550_d() {
		this.updateAnimations();
	}
}
