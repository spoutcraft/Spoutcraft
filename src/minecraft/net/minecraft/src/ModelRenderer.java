package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class ModelRenderer {
	public float textureWidth;
	public float textureHeight;
	private int textureOffsetX;
	private int textureOffsetY;
	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;
	private boolean compiled;
	private int displayList;
	public boolean mirror;
	public boolean showModel;
	public boolean isHidden;
	public List cubeList;
	public List childModels;
	public final String boxName;
	private ModelBase baseModel;

	public ModelRenderer(ModelBase modelbase, String s) {
		textureWidth = 64F;
		textureHeight = 32F;
		compiled = false;
		displayList = 0;
		mirror = false;
		showModel = true;
		isHidden = false;
		cubeList = new ArrayList();
		baseModel = modelbase;
		modelbase.boxList.add(this);
		boxName = s;
		setTextureSize(modelbase.textureWidth, modelbase.textureHeight);
	}

	public ModelRenderer(ModelBase modelbase) {
		this(modelbase, null);
	}

	public ModelRenderer(ModelBase modelbase, int i, int j) {
		this(modelbase);
		setTextureOffset(i, j);
	}

	public void addChild(ModelRenderer modelrenderer) {
		if (childModels == null) {
			childModels = new ArrayList();
		}
		childModels.add(modelrenderer);
	}

	public ModelRenderer setTextureOffset(int i, int j) {
		textureOffsetX = i;
		textureOffsetY = j;
		return this;
	}

	public ModelRenderer addBox(String s, float f, float f1, float f2, int i, int j, int k) {
		s = (new StringBuilder()).append(boxName).append(".").append(s).toString();
		TextureOffset textureoffset = baseModel.func_40297_a(s);
		setTextureOffset(textureoffset.field_40734_a, textureoffset.field_40733_b);
		cubeList.add((new ModelBox(this, textureOffsetX, textureOffsetY, f, f1, f2, i, j, k, 0.0F)).func_40671_a(s));
		return this;
	}

	public ModelRenderer addBox(float f, float f1, float f2, int i, int j, int k) {
		cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, f, f1, f2, i, j, k, 0.0F));
		return this;
	}

	public void addBox(float f, float f1, float f2, int i, int j, int k, float f3) {
		cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, f, f1, f2, i, j, k, f3));
	}

	public void setRotationPoint(float f, float f1, float f2) {
		rotationPointX = f;
		rotationPointY = f1;
		rotationPointZ = f2;
	}

	public void render(float f) {
		if (isHidden) {
			return;
		}
		if (!showModel) {
			return;
		}
		if (!compiled) {
			compileDisplayList(f);
		}
		if (rotateAngleX != 0.0F || rotateAngleY != 0.0F || rotateAngleZ != 0.0F) {
			GL11.glPushMatrix();
			GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
			if (rotateAngleZ != 0.0F) {
				GL11.glRotatef(rotateAngleZ * 57.29578F, 0.0F, 0.0F, 1.0F);
			}
			if (rotateAngleY != 0.0F) {
				GL11.glRotatef(rotateAngleY * 57.29578F, 0.0F, 1.0F, 0.0F);
			}
			if (rotateAngleX != 0.0F) {
				GL11.glRotatef(rotateAngleX * 57.29578F, 1.0F, 0.0F, 0.0F);
			}
			GL11.glCallList(displayList);
			if (childModels != null) {
				for (int i = 0; i < childModels.size(); i++) {
					((ModelRenderer)childModels.get(i)).render(f);
				}
			}
			GL11.glPopMatrix();
		}
		else if (rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F) {
			GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
			GL11.glCallList(displayList);
			if (childModels != null) {
				for (int j = 0; j < childModels.size(); j++) {
					((ModelRenderer)childModels.get(j)).render(f);
				}
			}
			GL11.glTranslatef(-rotationPointX * f, -rotationPointY * f, -rotationPointZ * f);
		}
		else {
			GL11.glCallList(displayList);
			if (childModels != null) {
				for (int k = 0; k < childModels.size(); k++) {
					((ModelRenderer)childModels.get(k)).render(f);
				}
			}
		}
	}

	public void renderWithRotation(float f) {
		if (isHidden) {
			return;
		}
		if (!showModel) {
			return;
		}
		if (!compiled) {
			compileDisplayList(f);
		}
		GL11.glPushMatrix();
		GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
		if (rotateAngleY != 0.0F) {
			GL11.glRotatef(rotateAngleY * 57.29578F, 0.0F, 1.0F, 0.0F);
		}
		if (rotateAngleX != 0.0F) {
			GL11.glRotatef(rotateAngleX * 57.29578F, 1.0F, 0.0F, 0.0F);
		}
		if (rotateAngleZ != 0.0F) {
			GL11.glRotatef(rotateAngleZ * 57.29578F, 0.0F, 0.0F, 1.0F);
		}
		GL11.glCallList(displayList);
		GL11.glPopMatrix();
	}

	public void postRender(float f) {
		if (isHidden) {
			return;
		}
		if (!showModel) {
			return;
		}
		if (!compiled) {
			compileDisplayList(f);
		}
		if (rotateAngleX != 0.0F || rotateAngleY != 0.0F || rotateAngleZ != 0.0F) {
			GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
			if (rotateAngleZ != 0.0F) {
				GL11.glRotatef(rotateAngleZ * 57.29578F, 0.0F, 0.0F, 1.0F);
			}
			if (rotateAngleY != 0.0F) {
				GL11.glRotatef(rotateAngleY * 57.29578F, 0.0F, 1.0F, 0.0F);
			}
			if (rotateAngleX != 0.0F) {
				GL11.glRotatef(rotateAngleX * 57.29578F, 1.0F, 0.0F, 0.0F);
			}
		}
		else if (rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F) {
			GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
		}
	}

	private void compileDisplayList(float f) {
		displayList = GLAllocation.generateDisplayLists(1);
		GL11.glNewList(displayList, 4864 /*GL_COMPILE*/);
		Tessellator tessellator = Tessellator.instance;
		for (int i = 0; i < cubeList.size(); i++) {
			((ModelBox)cubeList.get(i)).func_40670_a(tessellator, f);
		}

		GL11.glEndList();
		compiled = true;
	}

	public ModelRenderer setTextureSize(int i, int j) {
		textureWidth = i;
		textureHeight = j;
		return this;
	}
}
