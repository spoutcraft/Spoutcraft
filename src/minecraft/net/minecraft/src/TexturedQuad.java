package net.minecraft.src;

public class TexturedQuad {
	public PositionTextureVertex vertexPositions[];
	public int nVertices;
	private boolean invertNormal;

	public TexturedQuad(PositionTextureVertex apositiontexturevertex[]) {
		nVertices = 0;
		invertNormal = false;
		vertexPositions = apositiontexturevertex;
		nVertices = apositiontexturevertex.length;
	}

	public TexturedQuad(PositionTextureVertex apositiontexturevertex[], int i, int j, int k, int l, float f, float f1) {
		this(apositiontexturevertex);
		float f2 = 0.0F / f;
		float f3 = 0.0F / f1;
		apositiontexturevertex[0] = apositiontexturevertex[0].setTexturePosition((float)k / f - f2, (float)j / f1 + f3);
		apositiontexturevertex[1] = apositiontexturevertex[1].setTexturePosition((float)i / f + f2, (float)j / f1 + f3);
		apositiontexturevertex[2] = apositiontexturevertex[2].setTexturePosition((float)i / f + f2, (float)l / f1 - f3);
		apositiontexturevertex[3] = apositiontexturevertex[3].setTexturePosition((float)k / f - f2, (float)l / f1 - f3);
	}

	public void flipFace() {
		PositionTextureVertex apositiontexturevertex[] = new PositionTextureVertex[vertexPositions.length];
		for (int i = 0; i < vertexPositions.length; i++) {
			apositiontexturevertex[i] = vertexPositions[vertexPositions.length - i - 1];
		}

		vertexPositions = apositiontexturevertex;
	}

	public void draw(Tessellator tessellator, float f) {
		Vec3D vec3d = vertexPositions[1].vector3D.subtract(vertexPositions[0].vector3D);
		Vec3D vec3d1 = vertexPositions[1].vector3D.subtract(vertexPositions[2].vector3D);
		Vec3D vec3d2 = vec3d1.crossProduct(vec3d).normalize();
		tessellator.startDrawingQuads();
		if (invertNormal) {
			tessellator.setNormal(-(float)vec3d2.xCoord, -(float)vec3d2.yCoord, -(float)vec3d2.zCoord);
		}
		else {
			tessellator.setNormal((float)vec3d2.xCoord, (float)vec3d2.yCoord, (float)vec3d2.zCoord);
		}
		for (int i = 0; i < 4; i++) {
			PositionTextureVertex positiontexturevertex = vertexPositions[i];
			tessellator.addVertexWithUV((float)positiontexturevertex.vector3D.xCoord * f, (float)positiontexturevertex.vector3D.yCoord * f, (float)positiontexturevertex.vector3D.zCoord * f, positiontexturevertex.texturePositionX, positiontexturevertex.texturePositionY);
		}

		tessellator.draw();
	}
}
