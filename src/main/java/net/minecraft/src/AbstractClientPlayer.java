package net.minecraft.src;

import net.minecraft.src.Minecraft;

import org.bukkit.ChatColor;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.special.Holiday;
import org.spoutcraft.client.special.Resources;
import org.spoutcraft.client.special.VIP;

public abstract class AbstractClientPlayer extends EntityPlayer {
	public static final ResourceLocation locationStevePng = new ResourceLocation("textures/entity/steve.png");
	private ThreadDownloadImageData downloadImageSkin;
	private ThreadDownloadImageData downloadImageCape;
	private ResourceLocation locationSkin;
	private ResourceLocation locationCape;
	public String customCapeUrl;
	public String customSkinUrl;
	public static boolean forceUpdate = false;
	
	public AbstractClientPlayer(World par1World, String par2Str) {
		super(par1World, par2Str);
		this.setupCustomSkin();
	}

	public void setupCustomSkin() {
		if (this.username != null && !this.username.isEmpty()) {
			this.locationSkin = getLocationSkin(this.username);
			this.locationCape = getLocationCape(this.username);
			this.downloadImageSkin = getDownloadImageSkin(this.locationSkin, this.username, this.customSkinUrl);
			this.downloadImageCape = getDownloadImageCape(this.locationCape, this.username, this.customCapeUrl);
		}
	}

	public ThreadDownloadImageData getTextureSkin() {
		return this.downloadImageSkin;
	}

	public ThreadDownloadImageData getTextureCape() {
		return this.downloadImageCape;
	}

	public ResourceLocation getLocationSkin() {
		return this.locationSkin;
	}

	public ResourceLocation getLocationCape() {
		return this.locationCape;
	}
	
	public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation par0ResourceLocation, String par1Str) {
		return getDownloadImage(par0ResourceLocation, getSkinUrl(par1Str, null), locationStevePng, new ImageBufferDownload());
	}
		
	public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation par0ResourceLocation, String par1Str, String customSkin) {
		return getDownloadImage(par0ResourceLocation, getSkinUrl(par1Str, customSkin), locationStevePng, new ImageBufferDownload());
	}

	public static ThreadDownloadImageData getDownloadImageCape(ResourceLocation par0ResourceLocation, String par1Str, String customCape) {
		return getDownloadImage(par0ResourceLocation, getCapeUrl(par1Str, customCape), (ResourceLocation)null, (IImageBuffer)null);
	}

	private static ThreadDownloadImageData getDownloadImage(ResourceLocation par0ResourceLocation, String par1Str, ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer) {
		TextureManager var4 = Minecraft.getMinecraft().getTextureManager();
		Object var5 = var4.getTexture(par0ResourceLocation);

		if (forceUpdate){
			var5 = null;
			forceUpdate = false;
		}
		
		if (var5 == null) {		
			var5 = new ThreadDownloadImageData(par1Str, par2ResourceLocation, par3IImageBuffer);
			var4.loadTexture(par0ResourceLocation, (TextureObject)var5);
		}
		return (ThreadDownloadImageData)var5;
	}

	public static String getSkinUrl(String par0Str, String par1Str) {
		if (par1Str != null) {
			return par1Str;
		} else {
			// Spout Start
			return String.format("http://cdn.spout.org/game/vanilla/skin/%s.png", new Object[] {StringUtils.stripControlCodes(par0Str)});
			// Spout End
		}
	}	

	public static String getCapeUrl(String par0Str, String par1Str) {
		String playerCloakUrl;
		String cleanUserName = ChatColor.stripColor(par0Str);
		VIP vip = Resources.getVIP(cleanUserName);
		
		if (par1Str != null) {
			playerCloakUrl = par1Str;
		} else {
			playerCloakUrl = String.format("http://skins.minecraft.net/MinecraftCloaks/%s.png", new Object[] {StringUtils.stripControlCodes(par0Str)});
		}
		
		if (vip != null && vip.getCape() != null) {
			playerCloakUrl = vip.getCape();
		} else {
			Holiday holiday = Resources.getHoliday();
			if (holiday != null && holiday.getCape() != null) {
				playerCloakUrl = holiday.getCape();
			}
		}		
		return playerCloakUrl;
	}

	public static ResourceLocation getLocationSkin(String par0Str) {
		return new ResourceLocation("skins/" + StringUtils.stripControlCodes(par0Str));
	}

	public static ResourceLocation getLocationCape(String par0Str) {
		return new ResourceLocation("cloaks/" + StringUtils.stripControlCodes(par0Str));
	}

	public static ResourceLocation getLocationSkull(String par0Str) {
		return new ResourceLocation("skull/" + StringUtils.stripControlCodes(par0Str));
	}
}
