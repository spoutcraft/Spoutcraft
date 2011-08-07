package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.lwjgl.opengl.GL11;
import net.minecraft.src.Tessellator;

public class GenericGradient extends GenericWidget implements Gradient {
	
	protected int color1 = 0xC0101010, color2 = 0xD0101010;
	
	public GenericGradient() {
		
	}
	
	public Gradient setTopColor(int color) {
		this.color1 = color;
		return this;
	}
	
	public Gradient setBottomColor(int color) {
		this.color2 = color;
		return this;
	}
	
	public int getTopColor() {
		return this.color1;
	}
	
	public int getBottomColor() {
		return this.color2;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.Gradient;
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 8;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		this.setTopColor(input.readInt());
		this.setBottomColor(input.readInt());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getTopColor());
		output.writeInt(getBottomColor());
	}
	
	@Override
	public void render() {
		float f = (float)(color1 >> 24 & 0xff) / 255F;
        float f1 = (float)(color1 >> 16 & 0xff) / 255F;
        float f2 = (float)(color1 >> 8 & 0xff) / 255F;
        float f3 = (float)(color1 & 0xff) / 255F;
        float f4 = (float)(color2 >> 24 & 0xff) / 255F;
        float f5 = (float)(color2 >> 16 & 0xff) / 255F;
        float f6 = (float)(color2 >> 8 & 0xff) / 255F;
        float f7 = (float)(color2 & 0xff) / 255F;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425 /*GL_SMOOTH*/);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(getWidth() + getX(), getY(), 0.0D);
        tessellator.addVertex(getX(), getY(), 0.0D);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(getX(), getHeight() + getY(), 0.0D);
        tessellator.addVertex(getWidth() + getX(), getHeight() + getY(), 0.0D);
        tessellator.draw();
        GL11.glShadeModel(7424 /*GL_FLAT*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}

}
