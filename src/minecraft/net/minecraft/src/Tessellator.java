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


	private Tessellator(int size) {
		this.bufferSize = size;
		this.byteBuffer = GLAllocation.createDirectByteBuffer(size * 4);
		this.intBuffer = this.byteBuffer.asIntBuffer();
		this.floatBuffer = this.byteBuffer.asFloatBuffer();
		this.shortBuffer = this.byteBuffer.asShortBuffer();
		this.rawBuffer = new int[size];
		this.useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
		if(this.useVBO) {
			this.vertexBuffers = GLAllocation.createDirectIntBuffer(this.vboCount);
			ARBVertexBufferObject.glGenBuffersARB(this.vertexBuffers);
		}

	}

	public int draw() {
		if(!this.isDrawing) {
			throw new IllegalStateException("Not tesselating!");
		} else {
			this.isDrawing = false;
			if(this.vertexCount > 0) {
				this.intBuffer.clear();
				this.intBuffer.put(this.rawBuffer, 0, this.rawBufferIndex);
				this.byteBuffer.position(0);
				this.byteBuffer.limit(this.rawBufferIndex * 4);
				if(this.useVBO) {
					this.vboIndex = (this.vboIndex + 1) % this.vboCount;
					ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB , this.vertexBuffers.get(this.vboIndex));
					ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, this.byteBuffer, ARBVertexBufferObject.GL_STREAM_DRAW_ARB);
				}

				if(this.hasTexture) {
					if(this.useVBO) {
						GL11.glTexCoordPointer(2,GL11.GL_FLOAT, 32, 12L);
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

				if(this.hasBrightness) {
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapEnabled);
					if(this.useVBO) {
						GL11.glTexCoordPointer(2, GL11.GL_SHORT, 32, 28L);
					} else {
						this.shortBuffer.position(14);
						GL11.glTexCoordPointer(2, 32, this.shortBuffer);
					}

					GL11.glEnableClientState( GL11.GL_TEXTURE_COORD_ARRAY );
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapDisabled);
				}

				if(this.hasColor) {
					if(this.useVBO) {
						GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 32, 20L);
					} else {
						this.byteBuffer.position(20);
						GL11.glColorPointer(4, true, 32, this.byteBuffer);
					}

					GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
				}

				if(this.hasNormals) {
					if(this.useVBO) {
						GL11.glNormalPointer(GL11.GL_UNSIGNED_BYTE, 32, 24L);
					} else {
						this.byteBuffer.position(24);
						GL11.glNormalPointer(32, this.byteBuffer);
					}

					GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
				}

				if(this.useVBO) {
					GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0L);
				} else {
					this.floatBuffer.position(0);
					GL11.glVertexPointer(3, 32, this.floatBuffer);
				}

				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				if(this.drawMode == 7 && convertQuadsToTriangles) {
					GL11.glDrawArrays(4, 0, this.vertexCount);
				} else {
					GL11.glDrawArrays(this.drawMode, 0, this.vertexCount);
				}

				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
				if(this.hasTexture) {
					GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				}

				if(this.hasBrightness) {
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapEnabled);
					GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY );
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapDisabled);
				}

				if(this.hasColor) {
					GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
				}

				if(this.hasNormals) {
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

	public void startDrawing(int drawMode) {
		if(this.isDrawing) {
			throw new IllegalStateException("Already tesselating!");
		} else {
			this.isDrawing = true;
			this.reset();
			this.drawMode = drawMode;
			this.hasNormals = false;
			this.hasColor = false;
			this.hasTexture = false;
			this.hasBrightness = false;
			this.isColorDisabled = false;
		}
	}

	public void setTextureUV(double u, double v) {
		this.hasTexture = true;
		this.textureU = u;
		this.textureV = v;
	}

	public void setBlendTexture(int textureID) {
		this.useBlendTexture = true;
		this.blendTexture = textureID;
	}

	public void setColorOpaque_F(float r, float g, float b) {
		this.setColorOpaque((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F));
	}

	public void setColorRGBA_F(float r, float g, float b, float a) {
		this.setColorRGBA((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
	}

	public void setColorOpaque(int r, int g, int b) {
		this.setColorRGBA(r, g, b, 255);
	}

	public void setColorRGBA(int r, int g, int b, int a) {
		if(!this.isColorDisabled) {
			//RA: Clamp RGBA [0 - 255]
			if(r > 255) {
				r = 255;
			}

			if(g > 255) {
				g = 255;
			}

			if(b > 255) {
				b = 255;
			}

			if(a > 255) {
				a = 255;
			}

			if(r < 0) {
				r = 0;
			}

			if(g < 0) {
				g = 0;
			}

			if(b < 0) {
				b = 0;
			}

			if(a < 0) {
				a = 0;
			}

			this.hasColor = true;
			if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
				this.color = a << 24 | b << 16 | g << 8 | r;
			} else {
				this.color = r << 24 | g << 16 | b << 8 | a;
			}

		}
	}

	public void addVertexWithUV(double var1, double var3, double var5, double var7, double var9) {
		this.setTextureUV(var7, var9);
		this.addVertex(var1, var3, var5);
	}

	public void addVertex(double x, double y, double z) {
		++this.addedVertices;
		if(this.drawMode == 7 && convertQuadsToTriangles && this.addedVertices % 4 == 0) {
			for(int i = 0; i < 2; ++i) {
				int var8 = 8 * (3 - i);
				if(this.hasTexture) {
					this.rawBuffer[this.rawBufferIndex + 3] = this.rawBuffer[this.rawBufferIndex - var8 + 3];
					this.rawBuffer[this.rawBufferIndex + 4] = this.rawBuffer[this.rawBufferIndex - var8 + 4];
				}

				if(this.hasBrightness) {
					this.rawBuffer[this.rawBufferIndex + 7] = this.rawBuffer[this.rawBufferIndex - var8 + 7];
				}

				if(this.hasColor) {
					this.rawBuffer[this.rawBufferIndex + 5] = this.rawBuffer[this.rawBufferIndex - var8 + 5];
				}

				this.rawBuffer[this.rawBufferIndex + 0] = this.rawBuffer[this.rawBufferIndex - var8 + 0];
				this.rawBuffer[this.rawBufferIndex + 1] = this.rawBuffer[this.rawBufferIndex - var8 + 1];
				this.rawBuffer[this.rawBufferIndex + 2] = this.rawBuffer[this.rawBufferIndex - var8 + 2];
				++this.vertexCount;
				this.rawBufferIndex += 8;
			}
		}

		if(this.hasTexture) {
			this.rawBuffer[this.rawBufferIndex + 3] = Float.floatToRawIntBits((float)this.textureU);
			this.rawBuffer[this.rawBufferIndex + 4] = Float.floatToRawIntBits((float)this.textureV);
		}

		if(this.hasBrightness) {
			this.rawBuffer[this.rawBufferIndex + 7] = this.brightness;
		}

		if(this.hasColor) {
			this.rawBuffer[this.rawBufferIndex + 5] = this.color;
		}

		if(this.hasNormals) {
			this.rawBuffer[this.rawBufferIndex + 6] = this.normal;
		}

		this.rawBuffer[this.rawBufferIndex + 0] = Float.floatToRawIntBits((float)(x + this.xOffset));
		this.rawBuffer[this.rawBufferIndex + 1] = Float.floatToRawIntBits((float)(y + this.yOffset));
		this.rawBuffer[this.rawBufferIndex + 2] = Float.floatToRawIntBits((float)(z + this.zOffset));
		this.rawBufferIndex += 8;
		++this.vertexCount;
		if(this.vertexCount % 4 == 0 && this.rawBufferIndex >= this.bufferSize - 32) {
			this.draw();
			this.isDrawing = true;
		}

	}

	public void setColorOpaque_I(int color) {
		int r = color >> 16 & 255;
		int g = color >> 8 & 255;
		int b = color & 255;
		this.setColorOpaque(r, g, b);
	}

	public void setColorRGBA_I(int color, int alpha) {
		int r = color >> 16 & 255;
		int g = color >> 8 & 255;
		int b = color & 255;
		this.setColorRGBA(r, g, b, alpha);
	}

	public void disableColor() {
		this.isColorDisabled = true;
	}

	public void setNormal(float x, float y, float z) {
		this.hasNormals = true;
		byte xcomponent = (byte)((int)(x * 127.0F));
		byte ycomponent = (byte)((int)(y * 127.0F));
		byte zcomponent = (byte)((int)(z * 127.0F));
		this.normal = xcomponent | ycomponent << 8 | zcomponent << 16;
	}

	public void setTranslationD(double x, double y, double z) {
		this.xOffset = x;
		this.yOffset = y;
		this.zOffset = z;
	}

	public void setTranslationF(float x, float y, float z) {
		this.xOffset += (double)x;
		this.yOffset += (double)y;
		this.zOffset += (double)z;
	}

}
