package net.minecraft.src;

import java.util.*;

public abstract class ModelBase {
	public float onGround;
	public boolean isRiding;
	public List boxList;
	public boolean field_40301_k;
	private Map modelTextureMap;
	public int textureWidth;
	public int textureHeight;

	public ModelBase() {
		isRiding = false;
		boxList = new ArrayList();
		field_40301_k = true;
		modelTextureMap = new HashMap();
		textureWidth = 64;
		textureHeight = 32;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
	}

	public void setLivingAnimations(EntityLiving entityliving, float f, float f1, float f2) {
	}

	protected void setTextureOffset(String s, int i, int j) {
		modelTextureMap.put(s, new TextureOffset(i, j));
	}

	public TextureOffset func_40297_a(String s) {
		return (TextureOffset)modelTextureMap.get(s);
	}
}
