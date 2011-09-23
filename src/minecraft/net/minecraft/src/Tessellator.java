package net.minecraft.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import net.minecraft.src.GLAllocation;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;

public class Tessellator {

	private static boolean convertQuadsToTriangles = false;
	private static boolean tryVBO = false;
	private ByteBuffer byteBuffer;
	private IntBuffer intBuffer;
	private FloatBuffer floatBuffer;
	private ShortBuffer field_35836_g;
	private int[] rawBuffer;
	private int vertexCount = 0;
	private double textureU;
	private double textureV;
	private int field_35837_l;
	private int color;
	private boolean hasColor = false;
	private boolean hasTexture = false;
	private boolean field_35838_p = false;
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
	//Spout Performance Start
	public static boolean isLoadingChunk = false;
	//Spout Performance End


	private Tessellator(int var1) {
		this.bufferSize = var1;
		this.byteBuffer = GLAllocation.createDirectByteBuffer(var1 * 4);
		this.intBuffer = this.byteBuffer.asIntBuffer();
		this.floatBuffer = this.byteBuffer.asFloatBuffer();
		this.field_35836_g = this.byteBuffer.asShortBuffer();
		this.rawBuffer = new int[var1];
		this.useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
		if(this.useVBO) {
			this.vertexBuffers = GLAllocation.createDirectIntBuffer(this.vboCount);
			ARBVertexBufferObject.glGenBuffersARB(this.vertexBuffers);
		}

	}

	public void draw() {
		if(!this.isDrawing) {
			throw new IllegalStateException("Not tesselating!");
		} else {
			this.isDrawing = false;
			//Spout Performance Start
			if (!isLoadingChunk) {
				GL11.glEnd();
			} else
				//Spout Performance End
			if(this.vertexCount > 0) {
				this.intBuffer.clear();
				this.intBuffer.put(this.rawBuffer, 0, this.rawBufferIndex);
				this.byteBuffer.position(0);
				this.byteBuffer.limit(this.rawBufferIndex * 4);
				if(this.useVBO) {
					this.vboIndex = (this.vboIndex + 1) % this.vboCount;
					ARBVertexBufferObject.glBindBufferARB('\u8892', this.vertexBuffers.get(this.vboIndex));
					ARBVertexBufferObject.glBufferDataARB('\u8892', this.byteBuffer, '\u88e0');
				}

				if(this.hasTexture) {
					if(this.useVBO) {
						GL11.glTexCoordPointer(2, 5126 /*GL_FLOAT*/, 32, 12L);
					} else {
						this.floatBuffer.position(3);
						GL11.glTexCoordPointer(2, 32, this.floatBuffer);
					}

					GL11.glEnableClientState('\u8078');
					
					//Spout Start
					if(textureOverride > 0)
						GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, textureOverride);
					//Spout End
				}

				if(this.field_35838_p) {
					GL13.glClientActiveTexture('\u84c1');
					if(this.useVBO) {
						GL11.glTexCoordPointer(2, 5122 /*GL_SHORT*/, 32, 24L);
					} else {
						this.field_35836_g.position(14);
						GL11.glTexCoordPointer(2, 32, this.field_35836_g);
					}

					GL11.glEnableClientState('\u8078');
					GL13.glClientActiveTexture('\u84c0');
				}

				if(this.hasColor) {
					if(this.useVBO) {
						GL11.glColorPointer(4, 5121 /*GL_UNSIGNED_BYTE*/, 32, 20L);
					} else {
						this.byteBuffer.position(20);
						GL11.glColorPointer(4, true, 32, this.byteBuffer);
					}

					GL11.glEnableClientState('\u8076');
				}

				if(this.hasNormals) {
					if(this.useVBO) {
						GL11.glNormalPointer(5121 /*GL_UNSIGNED_BYTE*/, 32, 24L);
					} else {
						this.byteBuffer.position(24);
						GL11.glNormalPointer(32, this.byteBuffer);
					}

					GL11.glEnableClientState('\u8075');
				}

				if(this.useVBO) {
					GL11.glVertexPointer(3, 5126 /*GL_FLOAT*/, 32, 0L);
				} else {
					this.floatBuffer.position(0);
					GL11.glVertexPointer(3, 32, this.floatBuffer);
				}

				GL11.glEnableClientState('\u8074');
				if(this.drawMode == 7 && convertQuadsToTriangles) {
					GL11.glDrawArrays(4, 0, this.vertexCount);
				} else {
					GL11.glDrawArrays(this.drawMode, 0, this.vertexCount);
				}

				GL11.glDisableClientState('\u8074');
				if(this.hasTexture) {
					GL11.glDisableClientState('\u8078');
				}

				if(this.field_35838_p) {
					GL13.glClientActiveTexture('\u84c1');
					GL11.glDisableClientState('\u8078');
					GL13.glClientActiveTexture('\u84c0');
				}

				if(this.hasColor) {
					GL11.glDisableClientState('\u8076');
				}

				if(this.hasNormals) {
					GL11.glDisableClientState('\u8075');
				}
			}

			this.reset();
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

	public void startDrawing(int var1) {
		if(this.isDrawing) {
			throw new IllegalStateException("Already tesselating!");
		} else {
			//Spout Performance Start
			if (!isLoadingChunk) {
				GL11.glBegin(var1);
			}
			//Spout Performance End
			this.isDrawing = true;
			this.reset();
			this.drawMode = var1;
			this.hasNormals = false;
			this.hasColor = false;
			this.hasTexture = false;
			this.field_35838_p = false;
			this.isColorDisabled = false;
		}
	}

	public void setTextureUV(double var1, double var3) {
		this.hasTexture = true;
		this.textureU = var1;
		this.textureV = var3;
		//Spout Performance Start
		if (!isLoadingChunk) {
			GL11.glTexCoord2f((float)var1, (float)var3);
		}
		//Spout Performance End
	}

	public void func_35835_b(int var1) {
		this.field_35838_p = true;
		this.field_35837_l = var1;
	}

	public void setColorOpaque_F(float var1, float var2, float var3) {
		this.setColorOpaque((int)(var1 * 255.0F), (int)(var2 * 255.0F), (int)(var3 * 255.0F));
	}

	public void setColorRGBA_F(float var1, float var2, float var3, float var4) {
		this.setColorRGBA((int)(var1 * 255.0F), (int)(var2 * 255.0F), (int)(var3 * 255.0F), (int)(var4 * 255.0F));
	}

	public void setColorOpaque(int var1, int var2, int var3) {
		this.setColorRGBA(var1, var2, var3, 255);
	}

	public void setColorRGBA(int var1, int var2, int var3, int var4) {
		if(!this.isColorDisabled) {
			if(var1 > 255) {
				var1 = 255;
			}

			if(var2 > 255) {
				var2 = 255;
			}

			if(var3 > 255) {
				var3 = 255;
			}

			if(var4 > 255) {
				var4 = 255;
			}

			if(var1 < 0) {
				var1 = 0;
			}

			if(var2 < 0) {
				var2 = 0;
			}

			if(var3 < 0) {
				var3 = 0;
			}

			if(var4 < 0) {
				var4 = 0;
			}

			this.hasColor = true;
			//Spout Performance Start
			if (!isLoadingChunk) {
				GL11.glColor4ub((byte)var1, (byte)var2, (byte)var3, (byte)var4);
			} else
			//Spout Performance End
			if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
				this.color = var4 << 24 | var3 << 16 | var2 << 8 | var1;
			} else {
				this.color = var1 << 24 | var2 << 16 | var3 << 8 | var4;
			}

		}
	}

	public void addVertexWithUV(double var1, double var3, double var5, double var7, double var9) {
		this.setTextureUV(var7, var9);
		this.addVertex(var1, var3, var5);
	}

	public void addVertex(double var1, double var3, double var5) {
		//Spout Performance Start
		if (!isLoadingChunk)
		{
			GL11.glVertex3f((float)(var1 + xOffset), (float)(var3 + yOffset), (float)(var5 + zOffset));
			return;
		}
		//Spout Performance End
		++this.addedVertices;
		if(this.drawMode == 7 && convertQuadsToTriangles && this.addedVertices % 4 == 0) {
			for(int var7 = 0; var7 < 2; ++var7) {
				int var8 = 8 * (3 - var7);
				if(this.hasTexture) {
					this.rawBuffer[this.rawBufferIndex + 3] = this.rawBuffer[this.rawBufferIndex - var8 + 3];
					this.rawBuffer[this.rawBufferIndex + 4] = this.rawBuffer[this.rawBufferIndex - var8 + 4];
				}

				if(this.field_35838_p) {
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

		if(this.field_35838_p) {
			this.rawBuffer[this.rawBufferIndex + 7] = this.field_35837_l;
		}

		if(this.hasColor) {
			this.rawBuffer[this.rawBufferIndex + 5] = this.color;
		}

		if(this.hasNormals) {
			this.rawBuffer[this.rawBufferIndex + 6] = this.normal;
		}

		this.rawBuffer[this.rawBufferIndex + 0] = Float.floatToRawIntBits((float)(var1 + this.xOffset));
		this.rawBuffer[this.rawBufferIndex + 1] = Float.floatToRawIntBits((float)(var3 + this.yOffset));
		this.rawBuffer[this.rawBufferIndex + 2] = Float.floatToRawIntBits((float)(var5 + this.zOffset));
		this.rawBufferIndex += 8;
		++this.vertexCount;
		if(this.vertexCount % 4 == 0 && this.rawBufferIndex >= this.bufferSize - 32) {
			this.draw();
			this.isDrawing = true;
		}

	}

	public void setColorOpaque_I(int var1) {
		int var2 = var1 >> 16 & 255;
		int var3 = var1 >> 8 & 255;
		int var4 = var1 & 255;
		this.setColorOpaque(var2, var3, var4);
	}

	public void setColorRGBA_I(int var1, int var2) {
		int var3 = var1 >> 16 & 255;
		int var4 = var1 >> 8 & 255;
		int var5 = var1 & 255;
		this.setColorRGBA(var3, var4, var5, var2);
	}

	public void disableColor() {
		this.isColorDisabled = true;
	}

	public void setNormal(float var1, float var2, float var3) {
		this.hasNormals = true;
		byte var4 = (byte)((int)(var1 * 127.0F));
		byte var5 = (byte)((int)(var2 * 127.0F));
		byte var6 = (byte)((int)(var3 * 127.0F));
		this.normal = var4 | var5 << 8 | var6 << 16;
		//Spout Performance Start
		if (!isLoadingChunk) {
			GL11.glNormal3b(var4, var5, var6);
		}
		//Spout Performance End
	}

	public void setTranslationD(double var1, double var3, double var5) {
		this.xOffset = var1;
		this.yOffset = var3;
		this.zOffset = var5;
	}

	public void setTranslationF(float var1, float var2, float var3) {
		this.xOffset += (double)var1;
		this.yOffset += (double)var2;
		this.zOffset += (double)var3;
	}

}
