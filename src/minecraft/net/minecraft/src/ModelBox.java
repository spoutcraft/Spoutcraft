package net.minecraft.src;

public class ModelBox {
	private PositionTextureVertex field_40679_h[];
	private TexturedQuad field_40680_i[];
	public final float field_40678_a;
	public final float field_40676_b;
	public final float field_40677_c;
	public final float field_40674_d;
	public final float field_40675_e;
	public final float field_40672_f;
	public String field_40673_g;

	public ModelBox(ModelRenderer modelrenderer, int i, int j, float f, float f1, float f2, int k,
	        int l, int i1, float f3) {
		field_40678_a = f;
		field_40676_b = f1;
		field_40677_c = f2;
		field_40674_d = f + (float)k;
		field_40675_e = f1 + (float)l;
		field_40672_f = f2 + (float)i1;
		field_40679_h = new PositionTextureVertex[8];
		field_40680_i = new TexturedQuad[6];
		float f4 = f + (float)k;
		float f5 = f1 + (float)l;
		float f6 = f2 + (float)i1;
		f -= f3;
		f1 -= f3;
		f2 -= f3;
		f4 += f3;
		f5 += f3;
		f6 += f3;
		if (modelrenderer.mirror) {
			float f7 = f4;
			f4 = f;
			f = f7;
		}
		PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, f1, f2, 0.0F, 0.0F);
		PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f4, f1, f2, 0.0F, 8F);
		PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(f4, f5, f2, 8F, 8F);
		PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(f, f5, f2, 8F, 0.0F);
		PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, f1, f6, 0.0F, 0.0F);
		PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f4, f1, f6, 0.0F, 8F);
		PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(f4, f5, f6, 8F, 8F);
		PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(f, f5, f6, 8F, 0.0F);
		field_40679_h[0] = positiontexturevertex;
		field_40679_h[1] = positiontexturevertex1;
		field_40679_h[2] = positiontexturevertex2;
		field_40679_h[3] = positiontexturevertex3;
		field_40679_h[4] = positiontexturevertex4;
		field_40679_h[5] = positiontexturevertex5;
		field_40679_h[6] = positiontexturevertex6;
		field_40679_h[7] = positiontexturevertex7;
		field_40680_i[0] = new TexturedQuad(new PositionTextureVertex[] {
		            positiontexturevertex5, positiontexturevertex1, positiontexturevertex2, positiontexturevertex6
		        }, i + i1 + k, j + i1, i + i1 + k + i1, j + i1 + l, modelrenderer.textureWidth, modelrenderer.textureHeight);
		field_40680_i[1] = new TexturedQuad(new PositionTextureVertex[] {
		            positiontexturevertex, positiontexturevertex4, positiontexturevertex7, positiontexturevertex3
		        }, i + 0, j + i1, i + i1, j + i1 + l, modelrenderer.textureWidth, modelrenderer.textureHeight);
		field_40680_i[2] = new TexturedQuad(new PositionTextureVertex[] {
		            positiontexturevertex5, positiontexturevertex4, positiontexturevertex, positiontexturevertex1
		        }, i + i1, j + 0, i + i1 + k, j + i1, modelrenderer.textureWidth, modelrenderer.textureHeight);
		field_40680_i[3] = new TexturedQuad(new PositionTextureVertex[] {
		            positiontexturevertex2, positiontexturevertex3, positiontexturevertex7, positiontexturevertex6
		        }, i + i1 + k, j + i1, i + i1 + k + k, j + 0, modelrenderer.textureWidth, modelrenderer.textureHeight);
		field_40680_i[4] = new TexturedQuad(new PositionTextureVertex[] {
		            positiontexturevertex1, positiontexturevertex, positiontexturevertex3, positiontexturevertex2
		        }, i + i1, j + i1, i + i1 + k, j + i1 + l, modelrenderer.textureWidth, modelrenderer.textureHeight);
		field_40680_i[5] = new TexturedQuad(new PositionTextureVertex[] {
		            positiontexturevertex4, positiontexturevertex5, positiontexturevertex6, positiontexturevertex7
		        }, i + i1 + k + i1, j + i1, i + i1 + k + i1 + k, j + i1 + l, modelrenderer.textureWidth, modelrenderer.textureHeight);
		if (modelrenderer.mirror) {
			for (int j1 = 0; j1 < field_40680_i.length; j1++) {
				field_40680_i[j1].flipFace();
			}
		}
	}

	public void func_40670_a(Tessellator tessellator, float f) {
		for (int i = 0; i < field_40680_i.length; i++) {
			field_40680_i[i].draw(tessellator, f);
		}
	}

	public ModelBox func_40671_a(String s) {
		field_40673_g = s;
		return this;
	}
}
