package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.PotionHealth;

public class Potion {

	public static final Potion[] potionTypes = new Potion[32];
	public static final Potion field_35676_b = null;
	public static final Potion moveSpeed = (new Potion(1, false, 8171462)).setPotionName("potion.moveSpeed").setIconIndex(0, 0);
	public static final Potion moveSlowdown = (new Potion(2, true, 5926017)).setPotionName("potion.moveSlowdown").setIconIndex(1, 0);
	public static final Potion digSpeed = (new Potion(3, false, 14270531)).setPotionName("potion.digSpeed").setIconIndex(2, 0).func_40614_a(1.5D);
	public static final Potion digSlowdown = (new Potion(4, true, 4866583)).setPotionName("potion.digSlowDown").setIconIndex(3, 0);
	public static final Potion damageBoost = (new Potion(5, false, 9643043)).setPotionName("potion.damageBoost").setIconIndex(4, 0);
	public static final Potion heal = (new PotionHealth(6, false, 16262179)).setPotionName("potion.heal");
	public static final Potion harm = (new PotionHealth(7, true, 4393481)).setPotionName("potion.harm");
	public static final Potion jump = (new Potion(8, false, 7889559)).setPotionName("potion.jump").setIconIndex(2, 1);
	public static final Potion confusion = (new Potion(9, true, 5578058)).setPotionName("potion.confusion").setIconIndex(3, 1).func_40614_a(0.25D);
	public static final Potion regeneration = (new Potion(10, false, 13458603)).setPotionName("potion.regeneration").setIconIndex(7, 0).func_40614_a(0.25D);
	public static final Potion resistance = (new Potion(11, false, 10044730)).setPotionName("potion.resistance").setIconIndex(6, 1);
	public static final Potion fireResistance = (new Potion(12, false, 14981690)).setPotionName("potion.fireResistance").setIconIndex(7, 1);
	public static final Potion waterBreathing = (new Potion(13, false, 3035801)).setPotionName("potion.waterBreathing").setIconIndex(0, 2);
	public static final Potion invisibility = (new Potion(14, false, 8356754)).setPotionName("potion.invisibility").setIconIndex(0, 1).func_40616_h();
	public static final Potion blindness = (new Potion(15, true, 2039587)).setPotionName("potion.blindness").setIconIndex(5, 1).func_40614_a(0.25D);
	public static final Potion nightVision = (new Potion(16, false, 2039713)).setPotionName("potion.nightVision").setIconIndex(4, 1).func_40616_h();
	public static final Potion hunger = (new Potion(17, true, 5797459)).setPotionName("potion.hunger").setIconIndex(1, 1);
	public static final Potion weakness = (new Potion(18, true, 4738376)).setPotionName("potion.weakness").setIconIndex(5, 0);
	public static final Potion poison = (new Potion(19, true, 5149489)).setPotionName("potion.poison").setIconIndex(6, 0).func_40614_a(0.25D);
	public static final Potion field_35688_v = null;
	public static final Potion field_35687_w = null;
	public static final Potion field_35697_x = null;
	public static final Potion field_35696_y = null;
	public static final Potion field_35695_z = null;
	public static final Potion field_35667_A = null;
	public static final Potion field_35668_B = null;
	public static final Potion field_35669_C = null;
	public static final Potion field_35663_D = null;
	public static final Potion field_35664_E = null;
	public static final Potion field_35665_F = null;
	public static final Potion field_35666_G = null;
	public final int id;
	public String name = "";  //Spout HD
	private int statusIconIndex = -1;
	private final boolean isBadEffect;
	private double field_40624_L;
	private boolean field_40625_M;
	public int liquidColor; //Spout HD
	public int origColor; //Spout HD

	protected Potion(int var1, boolean var2, int var3) {
		this.id = var1;
		potionTypes[var1] = this;
		this.isBadEffect = var2;
		if (var2) {
			this.field_40624_L = 0.5D;
		}
		else {
			this.field_40624_L = 1.0D;
		}

		this.liquidColor = var3;
	}

	protected Potion setIconIndex(int var1, int var2) {
		this.statusIconIndex = var1 + var2 * 8;
		return this;
	}

	public int getId() {
		return this.id;
	}

	public void performEffect(EntityLiving var1, int var2) {
		if (this.id == regeneration.id) {
			if (var1.getEntityHealth() < var1.getMaxHealth()) {
				var1.heal(1);
			}
		}
		else if (this.id == poison.id) {
			if (var1.getEntityHealth() > 1) {
				var1.attackEntityFrom(DamageSource.magic, 1);
			}
		}
		else if (this.id == hunger.id && var1 instanceof EntityPlayer) {
			((EntityPlayer)var1).addExhaustion(0.025F * (float)(var2 + 1));
		}
		else if ((this.id != heal.id || var1.isEntityUndead()) && (this.id != harm.id || !var1.isEntityUndead())) {
			if (this.id == harm.id && !var1.isEntityUndead() || this.id == heal.id && var1.isEntityUndead()) {
				var1.attackEntityFrom(DamageSource.magic, 6 << var2);
			}
		}
		else {
			var1.heal(6 << var2);
		}

	}

	public void affectEntity(EntityLiving var1, EntityLiving var2, int var3, double var4) {
		int var6;
		if ((this.id != heal.id || var2.isEntityUndead()) && (this.id != harm.id || !var2.isEntityUndead())) {
			if (this.id == harm.id && !var2.isEntityUndead() || this.id == heal.id && var2.isEntityUndead()) {
				var6 = (int)(var4 * (double)(6 << var3) + 0.5D);
				if (var1 == null) {
					var2.attackEntityFrom(DamageSource.magic, var6);
				}
				else {
					var2.attackEntityFrom(DamageSource.causeIndirectMagicDamage(var2, var1), var6);
				}
			}
		}
		else {
			var6 = (int)(var4 * (double)(6 << var3) + 0.5D);
			var2.heal(var6);
		}

	}

	public boolean isInstant() {
		return false;
	}

	public boolean isReady(int var1, int var2) {
		if (this.id != regeneration.id && this.id != poison.id) {
			return this.id == hunger.id;
		}
		else {
			int var3 = 25 >> var2;
			return var3 > 0 ? var1 % var3 == 0 : true;
		}
	}

	public Potion setPotionName(String var1) {
		this.name = var1;
		Colorizer.setupPotion(this); //Spout HD
		return this;
	}

	public String getName() {
		return this.name;
	}

	public boolean hasStatusIcon() {
		return this.statusIconIndex >= 0;
	}

	public int getStatusIconIndex() {
		return this.statusIconIndex;
	}

	public boolean getIsBadEffect() {
		return this.isBadEffect;
	}

	public static String func_40620_a(PotionEffect var0) {
		int var1 = var0.getDuration();
		int var2 = var1 / 20;
		int var3 = var2 / 60;
		var2 %= 60;
		return var2 < 10 ? var3 + ":0" + var2 : var3 + ":" + var2;
	}

	protected Potion func_40614_a(double var1) {
		this.field_40624_L = var1;
		return this;
	}

	public double func_40610_g() {
		return this.field_40624_L;
	}

	public Potion func_40616_h() {
		this.field_40625_M = true;
		return this;
	}

	public boolean func_40612_i() {
		return this.field_40625_M;
	}

	public int getLiquidColor() {
		return this.liquidColor;
	}

}
