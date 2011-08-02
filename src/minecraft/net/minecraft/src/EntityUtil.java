package net.minecraft.src;
//Spout

public abstract class EntityUtil {
	public static void setMaximumAir(Entity entity, int time) {
		entity.maxAir = time;
	}
}