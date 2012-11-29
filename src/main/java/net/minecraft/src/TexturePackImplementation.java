package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

import com.pclewis.mcpatcher.MCPatcherUtils; // Spout HD

public abstract class TexturePackImplementation implements ITexturePack {

	/**
	 * Texture pack ID as returnd by generateTexturePackID(). Used only internally and not visible to the user.
	 */
	private final String texturePackID;

	/**
	 * The name of the texture pack's zip file/directory or "Default" for the builtin texture pack. Shown in the GUI.
	 */
	public final String texturePackFileName; // Spout HD private -> public

	/**
	 * File object for the texture pack's zip file in TexturePackCustom or the directory in TexturePackFolder.
	 */
	public File texturePackFile;  // Spout HD protected final -> public

	/**
	 * First line of texture pack description (from /pack.txt) displayed in the GUI
	 */
	protected String firstDescriptionLine;

	/**
	 * Second line of texture pack description (from /pack.txt) displayed in the GUI
	 */
	protected String secondDescriptionLine;

	/** The texture pack's thumbnail image loaded from the /pack.png file. */
	protected BufferedImage thumbnailImage;

	/** The texture id for this pcak's thumbnail image. */
	private int thumbnailTextureName;

	protected TexturePackImplementation(String par1Str, String par2Str) {
		this(par1Str, (File)null, par2Str);
	}

	protected TexturePackImplementation(String par1Str, File par2File, String par3Str) {
		this.thumbnailTextureName = -1;
		this.texturePackID = par1Str;
		this.texturePackFileName = par3Str;
		this.texturePackFile = par2File;
		this.loadThumbnailImage();
		this.loadDescription();
	}

	/**
	 * Truncate strings to at most 34 characters. Truncates description lines
	 */
	private static String trimStringToGUIWidth(String par0Str) {
		if (par0Str != null && par0Str.length() > 34) {
			par0Str = par0Str.substring(0, 34);
		}

		return par0Str;
	}

	/**
	 * Load and initialize thumbnailImage from the the /pack.png file.
	 */
	private void loadThumbnailImage() {
		InputStream var1 = null;

		try {
			var1 = this.getResourceAsStream("/pack.png");
			this.thumbnailImage = ImageIO.read(var1);
		} catch (IOException var11) {
			;
		} finally {
			try {
				var1.close();
			} catch (IOException var10) {
				;
			}
		}
	}

	/**
	 * Load texture pack description from /pack.txt file in the texture pack
	 */
	protected void loadDescription() {
		InputStream var1 = null;
		BufferedReader var2 = null;

		try {
			var1 = this.getResourceAsStream("/pack.txt");
			var2 = new BufferedReader(new InputStreamReader(var1));
			this.firstDescriptionLine = trimStringToGUIWidth(var2.readLine());
			this.secondDescriptionLine = trimStringToGUIWidth(var2.readLine());
		} catch (IOException var12) {
			;
		} finally {
			try {
				var2.close();
				var1.close();
			} catch (IOException var11) {
				;
			}
		}
	}

	/**
	 * Delete the OpenGL texture id of the pack's thumbnail image, and close the zip file in case of TexturePackCustom.
	 */
	public void deleteTexturePack(RenderEngine par1RenderEngine) {
		if (this.thumbnailImage != null && this.thumbnailTextureName != -1) {
			par1RenderEngine.deleteTexture(this.thumbnailTextureName);
		}
	}

	/**
	 * Bind the texture id of the pack's thumbnail image, loading it if necessary.
	 */
	public void bindThumbnailTexture(RenderEngine par1RenderEngine) {
		if (this.thumbnailImage != null) {
			if (this.thumbnailTextureName == -1) {
				this.thumbnailTextureName = par1RenderEngine.allocateAndSetupTexture(this.thumbnailImage);
			}

			par1RenderEngine.bindTexture(this.thumbnailTextureName);
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture("/gui/unknown_pack.png"));
		}
	}

	/**
	 * Gives a texture resource as InputStream.
	 */
	public InputStream getResourceAsStream(String par1Str) {
		return ITexturePack.class.getResourceAsStream(par1Str);
	}

	/**
	 * Get the texture pack ID
	 */
	public String getTexturePackID() {
		return this.texturePackID;
	}

	/**
	 * Get the file name of the texture pack, or Default if not from a custom texture pack
	 */
	public String getTexturePackFileName() {
		return this.texturePackFileName;
	}

	/**
	 * Get the first line of the texture pack description (read from the pack.txt file)
	 */
	public String getFirstDescriptionLine() {
		return this.firstDescriptionLine;
	}

	/**
	 * Get the second line of the texture pack description (read from the pack.txt file)
	 */
	public String getSecondDescriptionLine() {
		return this.secondDescriptionLine;
	}

	/**
	 * Return the texture pack's resolution (16 by default). Used only by PlayerUsageSnooper. Presumably meant to be
	 * overriden by HD texture mods.
	 */
	public int getTexturePackResolution() {
		return 16;
	}

	// Spout HD Start
	public void openTexturePackFile() {
		this.bindThumbnailTexture(MCPatcherUtils.getMinecraft().renderEngine);
	}

	public void closeTexturePackFile() {
		this.deleteTexturePack(MCPatcherUtils.getMinecraft().renderEngine);
	}
	// Spout HD End
}
