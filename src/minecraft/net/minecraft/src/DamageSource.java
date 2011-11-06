package net.minecraft.src;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityDamageSource;
import net.minecraft.src.EntityDamageSourceIndirect;
import net.minecraft.src.EntityFireball;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;

public class DamageSource {

	public static DamageSource inFire = new DamageSource("inFire");
	public static DamageSource onFire = (new DamageSource("onFire")).func_35528_f();
	public static DamageSource lava = new DamageSource("lava");
	public static DamageSource inWall = (new DamageSource("inWall")).func_35528_f();
	public static DamageSource drown = (new DamageSource("drown")).func_35528_f();
	public static DamageSource starve = (new DamageSource("starve")).func_35528_f();
	public static DamageSource cactus = new DamageSource("cactus");
	public static DamageSource fall = new DamageSource("fall");
	public static DamageSource outOfWorld = (new DamageSource("outOfWorld")).func_35528_f().func_35531_g();
	public static DamageSource generic = (new DamageSource("generic")).func_35528_f();
	public static DamageSource explosion = new DamageSource("explosion");
	public static DamageSource magic = (new DamageSource("magic")).func_35528_f();
	private boolean field_35543_n = false;
	private boolean field_35544_o = false;
	private float field_35551_p = 0.3F;
	public String field_35546_m;


	public static DamageSource causeMobDamage(EntityLiving var0) {
		return new EntityDamageSource("mob", var0);
	}

	public static DamageSource causePlayerDamage(EntityPlayer var0) {
		return new EntityDamageSource("player", var0);
	}

	public static DamageSource causeArrowDamage(EntityArrow var0, Entity var1) {
		return new EntityDamageSourceIndirect("arrow", var0, var1);
	}

	public static DamageSource causeFireballDamage(EntityFireball var0, Entity var1) {
		return new EntityDamageSourceIndirect("fireball", var0, var1);
	}

	public static DamageSource causeThrownDamage(Entity var0, Entity var1) {
		return new EntityDamageSourceIndirect("thrown", var0, var1);
	}

	public boolean func_35534_b() {
		return this.field_35543_n;
	}

	public float func_35533_c() {
		return this.field_35551_p;
	}

	public boolean func_35529_d() {
		return this.field_35544_o;
	}

	protected DamageSource(String var1) {
		this.field_35546_m = var1;
	}

	public Entity func_35526_e() {
		return this.getEntity();
	}

	public Entity getEntity() {
		return null;
	}

	private DamageSource func_35528_f() {
		this.field_35543_n = true;
		this.field_35551_p = 0.0F;
		return this;
	}

	private DamageSource func_35531_g() {
		this.field_35544_o = true;
		return this;
	}

}
