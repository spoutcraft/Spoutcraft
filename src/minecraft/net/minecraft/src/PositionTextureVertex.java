package net.minecraft.src;

public class PositionTextureVertex {
	public Vec3D vector3D;
	public float texturePositionX;
	public float texturePositionY;

	public PositionTextureVertex(float f, float f1, float f2, float f3, float f4) {
		this(Vec3D.createVectorHelper(f, f1, f2), f3, f4);
	}

	public PositionTextureVertex setTexturePosition(float f, float f1) {
		return new PositionTextureVertex(this, f, f1);
	}

	public PositionTextureVertex(PositionTextureVertex positiontexturevertex, float f, float f1) {
		vector3D = positiontexturevertex.vector3D;
		texturePositionX = f;
		texturePositionY = f1;
	}

	public PositionTextureVertex(Vec3D vec3d, float f, float f1) {
		vector3D = vec3d;
		texturePositionX = f;
		texturePositionY = f1;
	}
}
