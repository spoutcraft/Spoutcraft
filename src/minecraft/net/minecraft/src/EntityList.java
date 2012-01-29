package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;  //Spout HD
import java.util.HashMap;
import java.util.Map;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityCaveSpider;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityDragon;
import net.minecraft.src.EntityEggInfo;
import net.minecraft.src.EntityEnderCrystal;
import net.minecraft.src.EntityEnderEye;
import net.minecraft.src.EntityEnderPearl;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityFireball;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityGiantZombie;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMagmaCube;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityMooshroom;
import net.minecraft.src.EntityPainting;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySmallFireball;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.EntitySnowman;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntitySquid;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityList {

	private static Map stringToClassMapping = new HashMap();
	private static Map classToStringMapping = new HashMap();
	private static Map IDtoClassMapping = new HashMap();
	private static Map classToIDMapping = new HashMap();
	public static HashMap field_44041_a = new HashMap();

	private static void addMapping(Class var0, String var1, int var2) {
		stringToClassMapping.put(var1, var0);
		classToStringMapping.put(var0, var1);
		IDtoClassMapping.put(Integer.valueOf(var2), var0);
		classToIDMapping.put(var0, Integer.valueOf(var2));
	}

	private static void func_46152_a(Class var0, String var1, int var2, int var3, int var4) {
		Colorizer.setupSpawnerEgg(var1, var2, var3, var4);  //Spout HD
		addMapping(var0, var1, var2);
		field_44041_a.put(Integer.valueOf(var2), new EntityEggInfo(var2, var3, var4));
	}

	public static Entity createEntityInWorld(String var0, World var1) {
		Entity var2 = null;

		try {
			Class var3 = (Class)stringToClassMapping.get(var0);
			if (var3 != null) {
				var2 = (Entity)var3.getConstructor(new Class[] {World.class}).newInstance(new Object[] {var1});
			}
		}
		catch (Exception var4) {
			var4.printStackTrace();
		}

		return var2;
	}

	public static Entity createEntityFromNBT(NBTTagCompound var0, World var1) {
		Entity var2 = null;

		try {
			Class var3 = (Class)stringToClassMapping.get(var0.getString("id"));
			if (var3 != null) {
				var2 = (Entity)var3.getConstructor(new Class[] {World.class}).newInstance(new Object[] {var1});
			}
		}
		catch (Exception var4) {
			var4.printStackTrace();
		}

		if (var2 != null) {
			var2.readFromNBT(var0);
		}
		else {
			System.out.println("Skipping Entity with id " + var0.getString("id"));
		}

		return var2;
	}

	public static Entity createEntity(int var0, World var1) {
		Entity var2 = null;

		try {
			Class var3 = (Class)IDtoClassMapping.get(Integer.valueOf(var0));
			if (var3 != null) {
				var2 = (Entity)var3.getConstructor(new Class[] {World.class}).newInstance(new Object[] {var1});
			}
		}
		catch (Exception var4) {
			var4.printStackTrace();
		}

		if (var2 == null) {
			System.out.println("Skipping Entity with id " + var0);
		}

		return var2;
	}

	public static int getEntityID(Entity var0) {
		return ((Integer)classToIDMapping.get(var0.getClass())).intValue();
	}

	public static String getEntityString(Entity var0) {
		return (String)classToStringMapping.get(var0.getClass());
	}

	public static String func_44040_a(int var0) {
		Class var1 = (Class)IDtoClassMapping.get(Integer.valueOf(var0));
		return var1 != null ? (String)classToStringMapping.get(var1) : null;
	}

	static {
		addMapping(EntityItem.class, "Item", 1);
		addMapping(EntityXPOrb.class, "XPOrb", 2);
		addMapping(EntityPainting.class, "Painting", 9);
		addMapping(EntityArrow.class, "Arrow", 10);
		addMapping(EntitySnowball.class, "Snowball", 11);
		addMapping(EntityFireball.class, "Fireball", 12);
		addMapping(EntitySmallFireball.class, "SmallFireball", 13);
		addMapping(EntityEnderPearl.class, "ThrownEnderpearl", 14);
		addMapping(EntityEnderEye.class, "EyeOfEnderSignal", 15);
		addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
		addMapping(EntityFallingSand.class, "FallingSand", 21);
		addMapping(EntityMinecart.class, "Minecart", 40);
		addMapping(EntityBoat.class, "Boat", 41);
		addMapping(EntityLiving.class, "Mob", 48);
		addMapping(EntityMob.class, "Monster", 49);
		func_46152_a(EntityCreeper.class, "Creeper", 50, 894731, 0);
		func_46152_a(EntitySkeleton.class, "Skeleton", 51, 12698049, 4802889);
		func_46152_a(EntitySpider.class, "Spider", 52, 3419431, 11013646);
		addMapping(EntityGiantZombie.class, "Giant", 53);
		func_46152_a(EntityZombie.class, "Zombie", 54, '\uafaf', 7969893);
		func_46152_a(EntitySlime.class, "Slime", 55, 5349438, 8306542);
		func_46152_a(EntityGhast.class, "Ghast", 56, 16382457, 12369084);
		func_46152_a(EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
		func_46152_a(EntityEnderman.class, "Enderman", 58, 1447446, 0);
		func_46152_a(EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
		func_46152_a(EntitySilverfish.class, "Silverfish", 60, 7237230, 3158064);
		func_46152_a(EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
		func_46152_a(EntityMagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
		addMapping(EntityDragon.class, "EnderDragon", 63);
		func_46152_a(EntityPig.class, "Pig", 90, 15771042, 14377823);
		func_46152_a(EntitySheep.class, "Sheep", 91, 15198183, 16758197);
		func_46152_a(EntityCow.class, "Cow", 92, 4470310, 10592673);
		func_46152_a(EntityChicken.class, "Chicken", 93, 10592673, 16711680);
		func_46152_a(EntitySquid.class, "Squid", 94, 2243405, 7375001);
		func_46152_a(EntityWolf.class, "Wolf", 95, 14144467, 13545366);
		func_46152_a(EntityMooshroom.class, "MushroomCow", 96, 10489616, 12040119);
		addMapping(EntitySnowman.class, "SnowMan", 97);
		func_46152_a(EntityVillager.class, "Villager", 120, 5651507, 12422002);
		addMapping(EntityEnderCrystal.class, "EnderCrystal", 200);
	}
}
