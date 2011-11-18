package net.minecraft.src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.ImageBuffer;
import net.minecraft.src.MCHash;
import net.minecraft.src.TextureFX;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.ThreadDownloadImageData;
import org.lwjgl.opengl.GL11;
//Spout HD Start
import net.minecraft.client.Minecraft;
import com.pclewis.mcpatcher.mod.TextureUtils;
import com.pclewis.mcpatcher.mod.TileSize;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Dimension;
import org.lwjgl.opengl.GLContext;
//Spout HD End

public class RenderEngine {

	public static boolean useMipmaps = false;
	private HashMap textureMap = new HashMap();
	private HashMap textureDataMap = new HashMap(); // Stores raw texture data
	private MCHash textureNameToImageMap = new MCHash();
	private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
    //Spout HD Start
	private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(TileSize.int_glBufferSize);
	//Spout HD End
	public List textureList = new ArrayList(); //Spout private -> public
	private Map urlToImageDataMap = new HashMap();
	private GameSettings options;
	public boolean clampTexture = false;
	public boolean blurTexture = false;
	public TexturePackList texturePack; //Spout private -> public
	private BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);
    //Spout Start
	public TexturePackBase oldPack = null;
	//Spout End

	public RenderEngine(TexturePackList texturePacks, GameSettings options) {
		this.texturePack = texturePacks;
		this.options = options;
		Graphics missingTexture = this.missingTextureImage.getGraphics();
		missingTexture.setColor(Color.WHITE);
		missingTexture.fillRect(0, 0, 64, 64);
		missingTexture.setColor(Color.BLACK);
		missingTexture.drawString("missingtex", 1, 10);
		missingTexture.dispose();
	}

	public int[] getTextureContents(String textureName) {
		TexturePackBase texturePack = this.texturePack.selectedTexturePack;
		int[] textureContents = (int[])this.textureDataMap.get(textureName);
		if(textureContents != null) {
			return textureContents;
		} else {
			try {
				//Object var6 = null; //RA: what? this is not used
				if(textureName.startsWith("##")) {
					//Spout HD Start
					textureContents = this.getImageContentsAndAllocate(this.unwrapImageByColumns(TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(2))));
                    //Spout HD End
				} else if(textureName.startsWith("%clamp%")) {
					this.clampTexture = true;
					//Spout HD Start
					textureContents = this.getImageContentsAndAllocate(TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(7)));
                    //Spout HD End
					this.clampTexture = false;
				} else if(textureName.startsWith("%blur%")) {
					//Spout HD Start
					this.blurTexture = true;
					this.clampTexture = true;
					textureContents = this.getImageContentsAndAllocate(TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(6)));
					this.clampTexture = false;
					//Spout HD end
					this.blurTexture = false;
				} else {
					InputStream texturepackStream = texturePack.getResourceAsStream(textureName);
					if(texturepackStream == null) {
						textureContents = this.getImageContentsAndAllocate(this.missingTextureImage);
					} else {
						textureContents = this.getImageContentsAndAllocate(this.readTextureImage(texturepackStream));
					}
				}

				this.textureDataMap.put(textureName, textureContents);
				return textureContents;
			} catch (IOException e) {
				e.printStackTrace();
				int[] nullTextureData = this.getImageContentsAndAllocate(this.missingTextureImage);
				this.textureDataMap.put(textureName, nullTextureData);
				return nullTextureData;
			}
		}
	}

	private int[] getImageContentsAndAllocate(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] textureData = new int[width * height];
		image.getRGB(0, 0, width, height, textureData, 0, width);
		return textureData;
	}

	private int[] getImageContents(BufferedImage image, int[] textureData) {
//Spout HD end
		if(image == null) {
			return textureData;
		} else {
			int width = image.getWidth();
			int height = image.getHeight();
			image.getRGB(0, 0, width, height, textureData, 0, width);
			return textureData;
		}
//Spout HD start
	}

	public int getTexture(String textureName) {
		TexturePackBase texturePack = this.texturePack.selectedTexturePack;
		Integer textureID = (Integer)this.textureMap.get(textureName);
		if(textureID != null) {
			return textureID.intValue();
		} else {
			try {
				this.singleIntBuffer.clear();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var6 = this.singleIntBuffer.get(0);
				if(textureName.startsWith("##")) {
					//Spout HD Start
					this.setupTexture(this.unwrapImageByColumns(TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(2))), var6);
					//Spout HD End
				} else if(textureName.startsWith("%clamp%")) {
					this.clampTexture = true;
					//Spout HD Start
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(7)), var6);
					//Spout HD End
					this.clampTexture = false;
				} else if(textureName.startsWith("%blur%")) {
					//Spout HD Start
					this.blurTexture = true;
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(6)), var6);
					this.blurTexture = false;
					//Spout HD end
				} else if(textureName.startsWith("%blurclamp%")) {
					//Spout HD Start
					this.blurTexture = true;
					this.clampTexture = true;
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(11)), var6);
					this.blurTexture = false;
					this.clampTexture = false;
					//Spout HD end
				} else {
					//Spout HD Start
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(textureName), var6);
					//Spout HD End
				}

				this.textureMap.put(textureName, Integer.valueOf(var6));
				return var6;
			} catch (Exception e) {
				e.printStackTrace();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var4 = this.singleIntBuffer.get(0);
				this.setupTexture(this.missingTextureImage, var4);
				this.textureMap.put(textureName, Integer.valueOf(var4));
				return var4;
			}
		}
	}

	private BufferedImage unwrapImageByColumns(BufferedImage texture) {
		int numTiles = texture.getWidth() / 16;
		BufferedImage subTexture = new BufferedImage(16, texture.getHeight() * numTiles, 2);
		Graphics var4 = subTexture.getGraphics();

		for(int i = 0; i < numTiles; ++i) {
			var4.drawImage(texture, -i * 16, i * texture.getHeight(), (ImageObserver)null);
		}

		var4.dispose();
		return subTexture;
	}

	public int allocateAndSetupTexture(BufferedImage texture) {
		this.singleIntBuffer.clear();
		GLAllocation.generateTextureNames(this.singleIntBuffer);
		int textureID = this.singleIntBuffer.get(0);
		this.setupTexture(texture, textureID);
		this.textureNameToImageMap.addKey(textureID, texture);
		return textureID;
	}

	public void setupTexture(BufferedImage texture, int var2) {
//Spout HD Start
		if(texture != null) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
			if(useMipmaps) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR );
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			} else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			}

			if(this.blurTexture) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			}

			if(this.clampTexture) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
			} else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			}
			int textureWidth = texture.getWidth();
			int textureHeight = texture.getHeight();
			int[] texData = new int[textureWidth * textureHeight];
			byte[] texColors = new byte[textureWidth * textureHeight * 4];
			texture.getRGB(0, 0, textureWidth, textureHeight, texData, 0, textureWidth);

			
			int r;
			int g;
			int b;
			int a;
			int j;
			int var13;
			int var14;
			for(int i = 0; i < texData.length; ++i) {
				r = texData[i] >> 24 & 255;
				g = texData[i] >> 16 & 255;
				b = texData[i] >> 8 & 255;
				a = texData[i] & 255;
				if(this.options != null && this.options.anaglyph) {
					j = (g * 30 + b * 59 + a * 11) / 100;
					var13 = (g * 30 + b * 70) / 100;
					var14 = (g * 30 + a * 70) / 100;
					g = j;
					b = var13;
					a = var14;
				}

				texColors[i * 4 + 0] = (byte)g;
				texColors[i * 4 + 1] = (byte)b;
				texColors[i * 4 + 2] = (byte)a;
				texColors[i * 4 + 3] = (byte)r;
			}

			this.imageData = TextureUtils.getByteBuffer(this.imageData, texColors);
			
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureWidth, textureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
			if(useMipmaps) {
				for(int i = 1; i <= 4; ++i) {
					//slide the colors into proper bit positions?
					//IE:   r     g   b     a
					//      00 - 00 - 00 - 00
					// so you can do something like
					// int col = r + g + b + a;
					
					r = textureWidth >> i - 1;
					g = textureWidth >> i;
					b = textureHeight >> i;

					for(a = 0; a < g; ++a) {
						for(j = 0; j < b; ++j) {
							var13 = this.imageData.getInt((a * 2 + 0 + (j * 2 + 0) * r) * 4);
							var14 = this.imageData.getInt((a * 2 + 1 + (j * 2 + 0) * r) * 4);
							int var15 = this.imageData.getInt((a * 2 + 1 + (j * 2 + 1) * r) * 4);
							int var16 = this.imageData.getInt((a * 2 + 0 + (j * 2 + 1) * r) * 4);
							int var17 = this.alphaBlend(this.alphaBlend(var13, var14), this.alphaBlend(var15, var16));
							this.imageData.putInt((a + j * g) * 4, var17);
						}
					}

					GL11.glTexImage2D(GL11.GL_TEXTURE_2D, i, GL11.GL_RGBA, g, b, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
				}
			}

		}
	}
//Spout HD end
	public void createTextureFromBytes(int[] var1, int width, int height, int var4) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
		if(useMipmaps) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR );
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}

		if(this.blurTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		byte[] textureData = new byte[width * height * 4];

		for(int i = 0; i < var1.length; ++i) {
			int alpha = var1[i] >> 24 & 255;
			int red = var1[i] >> 16 & 255;
			int green = var1[i] >> 8 & 255;
			int blue = var1[i] & 255;
			if(this.options != null && this.options.anaglyph) {
				int anaglyphRed = (red * 30 + green * 59 + blue * 11) / 100;
				int anaglyphGreen = (red * 30 + green * 70) / 100;
				int anaglyphBlue = (red * 30 + blue * 70) / 100;
				red = anaglyphRed;
				green = anaglyphGreen;
				blue = anaglyphBlue;
			}

			textureData[i * 4 + 0] = (byte)red;
			textureData[i * 4 + 1] = (byte)green;
			textureData[i * 4 + 2] = (byte)blue;
			textureData[i * 4 + 3] = (byte)alpha;
		}

		//Spout HD Start
		this.imageData = TextureUtils.getByteBuffer(this.imageData, textureData);
        //Spout HD End
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
	}

	public void deleteTexture(int textureID) {
		this.textureNameToImageMap.removeObject(textureID);
		this.singleIntBuffer.clear();
		this.singleIntBuffer.put(textureID);
		this.singleIntBuffer.flip();
		GL11.glDeleteTextures(this.singleIntBuffer);
	}

	public int getTextureForDownloadableImage(String textureURL, String var2) {
		ThreadDownloadImageData textureData = (ThreadDownloadImageData)this.urlToImageDataMap.get(textureURL);
		if(textureData != null && textureData.image != null && !textureData.textureSetupComplete) {
			if(textureData.textureName < 0) {
				textureData.textureName = this.allocateAndSetupTexture(textureData.image);
			} else {
				this.setupTexture(textureData.image, textureData.textureName);
			}

			textureData.textureSetupComplete = true;
		}

		return textureData != null && textureData.textureName >= 0?textureData.textureName:(var2 == null?-1:this.getTexture(var2));
	}

	public ThreadDownloadImageData obtainImageData(String textureURL, ImageBuffer textureBuffer) {
		ThreadDownloadImageData textureData = (ThreadDownloadImageData)this.urlToImageDataMap.get(textureURL);
		if(textureData == null) {
			this.urlToImageDataMap.put(textureURL, new ThreadDownloadImageData(textureURL, textureBuffer));
		} else {
			++textureData.referenceCount;
		}

		return textureData;
	}

	public void releaseImageData(String textureName) {
		ThreadDownloadImageData textureData = (ThreadDownloadImageData)this.urlToImageDataMap.get(textureName);
		if(textureData != null) {
			--textureData.referenceCount;
			if(textureData.referenceCount == 0) {
                //Spout Start
				if (Minecraft.theMinecraft.theWorld != null) {
					List<EntityPlayer> players = Minecraft.theMinecraft.theWorld.playerEntities;
					for (EntityPlayer player : players) {
						if (player.skinUrl != null && player.skinUrl.equals(textureName)) {
							textureData.referenceCount++;
						}
						if (player.playerCloakUrl != null && player.playerCloakUrl.equals(textureName)) {
							textureData.referenceCount++;
						}
					}
				}
				if (textureData.referenceCount > 0) {
					return;
				}
				//Spout End
				if(textureData.textureName >= 0) {
					this.deleteTexture(textureData.textureName);
				}

				this.urlToImageDataMap.remove(textureName);
			}
		}

	}

	public void registerTextureFX(TextureFX var1) {
		//Spout HD start
		TextureUtils.registerTextureFX(this.textureList, var1);
		//Spout HD end
	}

	public void updateDynamicTextures() {
		int i;
		TextureFX textureEffect;
		int x;
		int y;
		int depth;
		int var6;
		int var7;
		int var8;
		int var9;
		int var10;
		int var11;
		int var12;
		for(i = 0; i < this.textureList.size(); ++i) {
			textureEffect = (TextureFX)this.textureList.get(i);
			textureEffect.anaglyphEnabled = this.options.anaglyph;
			textureEffect.onTick();
			//Spout HD Start
			this.imageData = TextureUtils.getByteBuffer(this.imageData, textureEffect.imageData);
            //Spout HD End
			textureEffect.bindImage(this);

			for(x = 0; x < textureEffect.tileSize; ++x) {
				for(y = 0; y < textureEffect.tileSize; ++y) {
					//Spout HD Start
					GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, textureEffect.iconIndex % 16 * TileSize.int_size + x * TileSize.int_size, textureEffect.iconIndex / 16 * TileSize.int_size + y * TileSize.int_size, TileSize.int_size, TileSize.int_size, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
                    //Spout HD End
					if(useMipmaps) {
						for(depth = 1; depth <= 4; ++depth) {
							var6 = 16 >> depth - 1;
							var7 = 16 >> depth;

							for(var8 = 0; var8 < var7; ++var8) {
								for(var9 = 0; var9 < var7; ++var9) {
									var10 = this.imageData.getInt((var8 * 2 + 0 + (var9 * 2 + 0) * var6) * 4);
									var11 = this.imageData.getInt((var8 * 2 + 1 + (var9 * 2 + 0) * var6) * 4);
									var12 = this.imageData.getInt((var8 * 2 + 1 + (var9 * 2 + 1) * var6) * 4);
									int var13 = this.imageData.getInt((var8 * 2 + 0 + (var9 * 2 + 1) * var6) * 4);
									int var14 = this.averageColor(this.averageColor(var10, var11), this.averageColor(var12, var13));
									this.imageData.putInt((var8 + var9 * var7) * 4, var14);
								}
							}

							GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, depth, textureEffect.iconIndex % 16 * var7, textureEffect.iconIndex / 16 * var7, var7, var7, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
						}
					}
				}
			}
		}

		for(i = 0; i < this.textureList.size(); ++i) {
			textureEffect = (TextureFX)this.textureList.get(i);
			if(textureEffect.textureId > 0) {
				//Spout HD Start
				this.imageData = TextureUtils.getByteBuffer(this.imageData, textureEffect.imageData);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureEffect.textureId);
				GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, TileSize.int_size, TileSize.int_size, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
                //Spout HD End
				if(useMipmaps) {
					for(x = 1; x <= 4; ++x) {
						y = 16 >> x - 1;
						depth = 16 >> x;

						for(var6 = 0; var6 < depth; ++var6) {
							for(var7 = 0; var7 < depth; ++var7) {
								var8 = this.imageData.getInt((var6 * 2 + 0 + (var7 * 2 + 0) * y) * 4);
								var9 = this.imageData.getInt((var6 * 2 + 1 + (var7 * 2 + 0) * y) * 4);
								var10 = this.imageData.getInt((var6 * 2 + 1 + (var7 * 2 + 1) * y) * 4);
								var11 = this.imageData.getInt((var6 * 2 + 0 + (var7 * 2 + 1) * y) * 4);
								var12 = this.averageColor(this.averageColor(var8, var9), this.averageColor(var10, var11));
								this.imageData.putInt((var6 + var7 * depth) * 4, var12);
							}
						}

						GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, x, 0, 0, depth, depth, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
					}
				}
			}
		}

	}

	private int averageColor(int color1, int color2) {
		//RA: These are the colors without Alpha channel (averaging alphas is bad)
		int var3 = (color1 & -16777216) >> 24 & 255;
		int var4 = (color2 & -16777216) >> 24 & 255;
		return (var3 + var4 >> 1 << 24) + ((color1 & 16711422) + (color2 & 16711422) >> 1);
	}

	private int alphaBlend(int color1, int color2) {
		int c1Alpha = (color1 & -16777216) >> 24 & 255;
		int c2Alpha = (color2 & -16777216) >> 24 & 255;
		short alpha = 255;
		if(c1Alpha + c2Alpha == 0) {
			c1Alpha = 1;
			c2Alpha = 1;
			alpha = 0;
		}

		int c1Blue = (color1 >> 16 & 255) * c1Alpha;
		int c1Green = (color1 >> 8 & 255) * c1Alpha;
		int c1Red = (color1 & 255) * c1Alpha;
		int c2Blue = (color2 >> 16 & 255) * c2Alpha;
		int c2Green = (color2 >> 8 & 255) * c2Alpha;
		int c2Red = (color2 & 255) * c2Alpha;
		int blue = (c1Blue + c2Blue) / (c1Alpha + c2Alpha);
		int green = (c1Green + c2Green) / (c1Alpha + c2Alpha);
		int red = (c1Red + c2Red) / (c1Alpha + c2Alpha);
		return alpha << 24 | blue << 16 | green << 8 | red;
	}

	public void refreshTextures() {
		TexturePackBase texturePack = this.texturePack.selectedTexturePack;
		Iterator texturePackIterator = this.textureNameToImageMap.func_35860_b().iterator();

		BufferedImage texture;
		while(texturePackIterator.hasNext()) {
			int var3 = ((Integer)texturePackIterator.next()).intValue();
			texture = (BufferedImage)this.textureNameToImageMap.lookup(var3);
			this.setupTexture(texture, var3);
		}

		ThreadDownloadImageData downloadedData;
		for(texturePackIterator = this.urlToImageDataMap.values().iterator(); texturePackIterator.hasNext(); downloadedData.textureSetupComplete = false) {
			downloadedData = (ThreadDownloadImageData)texturePackIterator.next();
		}

		texturePackIterator = this.textureMap.keySet().iterator();

		String textureName;
		while(texturePackIterator.hasNext()) {
			textureName = (String)texturePackIterator.next();

			try {
				//Spout HD Start
				if(textureName.startsWith("##")) {
					texture = this.unwrapImageByColumns(TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(2)));
				} else if(textureName.startsWith("%clamp%")) {
					this.clampTexture = true;
					texture = TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(7));
				} else if(textureName.startsWith("%blur%")) {
					this.blurTexture = true;
					texture = TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(6));
				} else if(textureName.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					texture = TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(11));
				} else {
					texture = TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName);
				}
				if (texture == null) {
					texturePackIterator.remove();
					continue;
				}
                //Spout HD End

				int textureID = ((Integer)this.textureMap.get(textureName)).intValue();
				this.setupTexture(texture, textureID);
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException e) {
				//Spout HD Start
				//Gracefully handle errors
				texturePackIterator.remove();
				//var6.printStackTrace();
				//Spout HD End
			}
		}

		texturePackIterator = this.textureDataMap.keySet().iterator();

		while(texturePackIterator.hasNext()) {
			textureName = (String)texturePackIterator.next();

			try {
				//Spout HD Start
				if(textureName.startsWith("##")) {
					texture = this.unwrapImageByColumns(TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(2)));
				} else if(textureName.startsWith("%clamp%")) {
					this.clampTexture = true;
					texture = TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(7));
				} else if(textureName.startsWith("%blur%")) {
					this.blurTexture = true;
					texture = TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName.substring(6));
				} else {
					texture = TextureUtils.getResourceAsBufferedImage(this, texturePack, textureName);
				}
                if (texture == null) {
                    texturePackIterator.remove();
                    continue;
                }
                //Spout HD End

				this.getImageContents(texture, (int[])this.textureDataMap.get(textureName));
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var6) {
				//Spout HD Start
				//Gracefully handle errors
				texturePackIterator.remove();
				//var6.printStackTrace();
				//Spout HD End
			}
		}

	}

	private BufferedImage readTextureImage(InputStream textureStream) throws IOException {
		BufferedImage texture = ImageIO.read(textureStream);
		textureStream.close();
		return texture;
	}

	public void bindTexture(int textureID) {
		if(textureID >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		}
	}

	//Spout HD Start
	public void setTileSize(Minecraft minecraft) {
		this.imageData = GLAllocation.createDirectByteBuffer(TileSize.int_glBufferSize);
		this.refreshTextures();
		TextureUtils.refreshTextureFX(this.textureList);
	}
}
