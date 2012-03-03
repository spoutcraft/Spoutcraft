package net.minecraft.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.OpenGlHelper;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class Tessellator {

	private static boolean convertQuadsToTriangles = false;
	private static boolean tryVBO = false;
	private ByteBuffer byteBuffer;
	private IntBuffer intBuffer;
	private FloatBuffer floatBuffer;
	private ShortBuffer shortBuffer;
	private int[] rawBuffer;
	private int vertexCount = 0;
	private double textureU;
	private double textureV;
	private int brightness;
	private int color;
	private boolean hasColor = false;
	private boolean hasTexture = false;
	private boolean hasBrightness = false;
	private boolean hasNormals = false;
	private int rawBufferIndex = 0;
	private int addedVertices = 0;
	private boolean isColorDisabled = false;
	private int drawMode;
	private double xOffset;
	private double yOffset;
	private double zOffset;
	private int normal;
	public static final Tessellator instance = new Tessellator(2097152);
	private boolean isDrawing = false;
	private boolean useVBO = false;
	private IntBuffer vertexBuffers;
	private int vboIndex = 0;
	private int vboCount = 10;
	private int bufferSize;
	//Spout Start
	public int textureOverride = 0;
	//Spout End


	private Tessellator(int par1) {
		this.bufferSize = par1;
		this.byteBuffer = GLAllocation.createDirectByteBuffer(par1 * 4);
		this.intBuffer = this.byteBuffer.asIntBuffer();
		this.floatBuffer = this.byteBuffer.asFloatBuffer();
		this.shortBuffer = this.byteBuffer.asShortBuffer();
		this.rawBuffer = new int[par1];
		this.useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
		if (this.useVBO) {
			this.vertexBuffers = GLAllocation.createDirectIntBuffer(this.vboCount);
			ARBVertexBufferObject.glGenBuffersARB(this.vertexBuffers);
		}

	}

	public int draw() {
		if (!this.isDrawing) {
			throw new IllegalStateException("Not tesselating!");
		} else {
			this.isDrawing = false;
			if (this.vertexCount > 0) {
				this.intBuffer.clear();
				this.intBuffer.put(this.rawBuffer, 0, this.rawBufferIndex);
				this.byteBuffer.position(0);
				this.byteBuffer.limit(this.rawBufferIndex * 4);
				if (this.useVBO) {
					this.vboIndex = (this.vboIndex + 1) % this.vboCount;
					ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB , this.vertexBuffers.get(this.vboIndex));
					ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, this.byteBuffer, ARBVertexBufferObject.GL_STREAM_DRAW_ARB);
				}

				if (this.hasTexture) {
					if (this.useVBO) {
						GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12L);
					} else {
						this.floatBuffer.position(3);
						GL11.glTexCoordPointer(2, 32, this.floatBuffer);
					}

					GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY ); 

					//Spout Start
					if(textureOverride > 0)
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureOverride);
					//Spout End
				}

				if (this.hasBrightness) {
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapEnabled);
					if (this.useVBO) {
						GL11.glTexCoordPointer(2, GL11.GL_SHORT, 32, 28L);
					} else {
						this.shortBuffer.position(14);
						GL11.glTexCoordPointer(2, 32, this.shortBuffer);
					}

					GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapDisabled);
				}

				if (this.hasColor) {
					if (this.useVBO) {
						GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 32, 20L);
					} else {
						this.byteBuffer.position(20);
						GL11.glColorPointer(4, true, 32, this.byteBuffer);
					}

					GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
				}

				if (this.hasNormals) {
					if (this.useVBO) {
						GL11.glNormalPointer(GL11.GL_UNSIGNED_BYTE, 32, 24L);
					} else {
						this.byteBuffer.position(24);
						GL11.glNormalPointer(32, this.byteBuffer);
					}

					GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
				}

				if (this.useVBO) {
					GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0L);
				} else {
					this.floatBuffer.position(0);
					GL11.glVertexPointer(3, 32, this.floatBuffer);
				}

				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				if (this.drawMode == 7 && convertQuadsToTriangles) {
					GL11.glDrawArrays(4, 0, this.vertexCount);
				} else {
					GL11.glDrawArrays(this.drawMode, 0, this.vertexCount);
				}

				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
				if (this.hasTexture) {
					GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				}

				if (this.hasBrightness) {
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapEnabled);
					GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapDisabled);
				}

				if (this.hasColor) {
					GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
				}

				if (this.hasNormals) {
					GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
				}
			}

			int var1 = this.rawBufferIndex * 4;
			this.reset();
			return var1;
		}
	}

	private void reset() {
		this.vertexCount = 0;
		this.byteBuffer.clear();
		this.rawBufferIndex = 0;
		this.addedVertices = 0;
	}

	public void startDrawingQuads() {
		this.startDrawing(7);
	}

	public void startDrawing(int par1) {
		if (this.isDrawing) {
			throw new IllegalStateException("Already tesselating!");
		} else {
			this.isDrawing = true;
			this.reset();
			this.drawMode = par1;
			this.hasNormals = false;
			this.hasColor = false;
			this.hasTexture = false;
			this.hasBrightness = false;
			this.isColorDisabled = false;
		}
	}

	public void setTextureUV(double par1, double par3) {
		this.hasTexture = true;
		this.textureU = par1;
		this.textureV = par3;
	}

	public void setBrightness(int par1) {
		this.hasBrightness = true;
		this.brightness = par1;
	}

	public void setColorOpaque_F(float par1, float par2, float par3) {
		this.setColorOpaque((int)(par1 * 255.0F), (int)(par2 * 255.0F), (int)(par3 * 255.0F));
	}

	public void setColorRGBA_F(float par1, float par2, float par3, float par4) {
		this.setColorRGBA((int)(par1 * 255.0F), (int)(par2 * 255.0F), (int)(par3 * 255.0F), (int)(par4 * 255.0F));
	}

	public void setColorOpaque(int par1, int par2, int par3) {
		this.setColorRGBA(par1, par2, par3, 255);
	}

	public void setColorRGBA(int par1, int par2, int par3, int par4) {
		if (!this.isColorDisabled) {
			if (par1 > 255) {
				par1 = 255;
			}

			if (par2 > 255) {
				par2 = 255;
			}

			if (par3 > 255) {
				par3 = 255;
			}

			if (par4 > 255) {
				par4 = 255;
			}

			if (par1 < 0) {
				par1 = 0;
			}

			if (par2 < 0) {
				par2 = 0;
			}

			if (par3 < 0) {
				par3 = 0;
			}

			if (par4 < 0) {
				par4 = 0;
			}

			this.hasColor = true;
			if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
				this.color = par4 << 24 | par3 << 16 | par2 << 8 | par1;
			} else {
				this.color = par1 << 24 | par2 << 16 | par3 << 8 | par4;
			}

		}
	}

	public void addVertexWithUV(double par1, double par3, double par5, double par7, double par9) {
		this.setTextureUV(par7, par9);
		this.addVertex(par1, par3, par5);
	}

	public void addVertex(double par1, double par3, double par5) {
		++this.addedVertices;
		if (this.drawMode == 7 && convertQuadsToTriangles && this.addedVertices % 4 == 0) {
			for (int var7 = 0; var7 < 2; ++var7) {
				int var8 = 8 * (3 - var7);
				if (this.hasTexture) {
					this.rawBuffer[this.rawBufferIndex + 3] = this.rawBuffer[this.rawBufferIndex - var8 + 3];
					this.rawBuffer[this.rawBufferIndex + 4] = this.rawBuffer[this.rawBufferIndex - var8 + 4];
				}

				if (this.hasBrightness) {
					this.rawBuffer[this.rawBufferIndex + 7] = this.rawBuffer[this.rawBufferIndex - var8 + 7];
				}

				if (this.hasColor) {
					this.rawBuffer[this.rawBufferIndex + 5] = this.rawBuffer[this.rawBufferIndex - var8 + 5];
				}

				this.rawBuffer[this.rawBufferIndex + 0] = this.rawBuffer[this.rawBufferIndex - var8 + 0];
				this.rawBuffer[this.rawBufferIndex + 1] = this.rawBuffer[this.rawBufferIndex - var8 + 1];
				this.rawBuffer[this.rawBufferIndex + 2] = this.rawBuffer[this.rawBufferIndex - var8 + 2];
				++this.vertexCount;
				this.rawBufferIndex += 8;
			}
		}

		if (this.hasTexture) {
			this.rawBuffer[this.rawBufferIndex + 3] = Float.floatToRawIntBits((float)this.textureU);
			this.rawBuffer[this.rawBufferIndex + 4] = Float.floatToRawIntBits((float)this.textureV);
		}

		if (this.hasBrightness) {
			this.rawBuffer[this.rawBufferIndex + 7] = this.brightness;
		}

		if (this.hasColor) {
			this.rawBuffer[this.rawBufferIndex + 5] = this.color;
		}

		if (this.hasNormals) {
			this.rawBuffer[this.rawBufferIndex + 6] = this.normal;
		}

		this.rawBuffer[this.rawBufferIndex + 0] = Float.floatToRawIntBits((float)(par1 + this.xOffset));
		this.rawBuffer[this.rawBufferIndex + 1] = Float.floatToRawIntBits((float)(par3 + this.yOffset));
		this.rawBuffer[this.rawBufferIndex + 2] = Float.floatToRawIntBits((float)(par5 + this.zOffset));
		this.rawBufferIndex += 8;
		++this.vertexCount;
		if (this.vertexCount % 4 == 0 && this.rawBufferIndex >= this.bufferSize - 32) {
			this.draw();
			this.isDrawing = true;
		}

	}

	public void setColorOpaque_I(int par1) {
		int var2 = par1 >> 16 & 255;
		int var3 = par1 >> 8 & 255;
		int var4 = par1 & 255;
		this.setColorOpaque(var2, var3, var4);
	}

	public void setColorRGBA_I(int par1, int par2) {
		int var3 = par1 >> 16 & 255;
		int var4 = par1 >> 8 & 255;
		int var5 = par1 & 255;
		this.setColorRGBA(var3, var4, var5, par2);
	}

	public void disableColor() {
		this.isColorDisabled = true;
	}

	public void setNormal(float par1, float par2, float par3) {
		this.hasNormals = true;
		byte var4 = (byte)((int)(par1 * 127.0F));
		byte var5 = (byte)((int)(par2 * 127.0F));
		byte var6 = (byte)((int)(par3 * 127.0F));
		this.normal = var4 & 255 | (var5 & 255) << 8 | (var6 & 255) << 16;
	}

	public void setTranslationD(double par1, double par3, double par5) {
		this.xOffset = par1;
		this.yOffset = par3;
		this.zOffset = par5;
	}

	public void setTranslationF(float par1, float par2, float par3) {
		this.xOffset += (double)par1;
		this.yOffset += (double)par2;
		this.zOffset += (double)par3;
	}

}
