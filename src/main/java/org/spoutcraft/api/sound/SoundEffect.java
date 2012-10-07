/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.api.sound;

import java.util.HashMap;
import java.util.Map;

public enum SoundEffect {
	/* Ambient Sound Effects */
	CAVE_RANDOM(284, "ambient.cave.cave"),
	CAVE_MYSTERIOUS(0, "ambient.cave.cave", 0),
	CAVE_LIGHT_MYSTERIOUS(1, "ambient.cave.cave", 1),
	CAVE_SHADOW(2, "ambient.cave.cave", 2),
	CAVE_DEEP(3, "ambient.cave.cave", 3),
	CAVE_NEW_PASSAGE(4, "ambient.cave.cave", 4),
	CAVE_PASSING_SHADOW(5, "ambient.cave.cave", 5),
	CAVE_DARK_SHADOW(6, "ambient.cave.cave", 6),
	CAVE_FEAR(7, "ambient.cave.cave", 7),
	CAVE_DARK_MYSTERIOUS(8, "ambient.cave.cave", 8),
	CAVE_LARGE_FEAR(9, "ambient.cave.cave", 9),
	CAVE_RUMBLE(10, "ambient.cave.cave", 10),
	CAVE_SHORT_MYSTERIOUS(11, "ambient.cave.cave", 11),
	CAVE_MONSTER_ROAR(12, "ambient.cave.cave", 12),

	WEATHER_RAIN(285, "ambient.weather.rain"),
	WEATHER_RAIN_1(13, "ambient.weather.rain", 0),
	WEATHER_RAIN_2(14, "ambient.weather.rain", 1),
	WEATHER_RAIN_3(15, "ambient.weather.rain", 2),
	WEATHER_RAIN_4(16, "ambient.weather.rain", 3),

	WEATHER_THUNDER(286, "ambient.weather.thunder"),
	WEATHER_THUNDER_1(17, "ambient.weather.thunder", 0),
	WEATHER_THUNDER_2(18, "ambient.weather.thunder", 1),
	WEATHER_THUNDER_3(19, "ambient.weather.thunder", 2),

	/* Damage */
	FALL(287, "damage.fallbig"),
	FALL_1(20, "damage.fallbig", 0),
	FALL_2(21, "damage.fallbig", 1),
	FALL_3(22, "damage.fallsmall"),

	HURT_FLESH(288, "damage.hurtflesh"),
	HURT_FLESH_1(23, "damage.hurtflesh", 0),
	HURT_FLESH_2(24, "damage.hurtflesh", 1),
	HURT_FLESH_3(25, "damage.hurtflesh", 2),

	/* Fire */
	FIRE(26, "fire.fire"),
	FIRE_IGNITE(27, "fire.ignite"),

	/* Liquid */
	LAVA(28, "liquid.lava"),
	LAVA_POP(29, "liquid.lavapop"),
	WATER_SPLASH(30, "liquid.splash"),
	WATER(31, "liquid.water"),

	/* Mob Sound Effects */
	BLAZE_BREATHE(289, "mob.blaze.breathe"),
	BLAZE_BREATHE_1(283, "mob.blaze.breathe", 0),
	BLAZE_BREATHE_2(92, "mob.blaze.breathe", 1),
	BLAZE_BREATHE_3(93, "mob.blaze.breathe", 2),
	BLAZE_BREATHE_4(94, "mob.blaze.breathe", 3),

	BLAZE_DEATH(	95, "mob.blaze.death"),

	BLAZE_HIT(290, "mob.blaze.hit"),
	BLAZE_HIT_1(	96, "mob.blaze.hit", 0),
	BLAZE_HIT_2(	97, "mob.blaze.hit", 1),
	BLAZE_HIT_3(	98, "mob.blaze.hit", 2),
	BLAZE_HIT_4(	99, "mob.blaze.hit", 3),

	CAT_HISS(291, "mob.cat.hiss"),
	CAT_HISS_1(		100, "mob.cat.hiss", 0),
	CAT_HISS_2(		101, "mob.cat.hiss", 1),
	CAT_HISS_3(		102, "mob.cat.hiss", 2),

	CAT_HITT(292, "mob.cat.hitt"),
	CAT_HITT_1(		103, "mob.cat.hitt", 0),
	CAT_HITT_2(		104, "mob.cat.hitt", 1),
	CAT_HITT_3(		105, "mob.cat.hitt", 2),

	CAT_MEOW(293, "mob.cat.meow"),
	CAT_MEOW_1(		106, "mob.cat.meow", 0),
	CAT_MEOW_2(		107, "mob.cat.meow", 1),
	CAT_MEOW_3(		108, "mob.cat.meow", 2),
	CAT_MEOW_4(		109, "mob.cat.meow", 3),

	CAT_PURR(294, "mob.cat.purr"),
	CAT_PURR_1(		110, "mob.cat.purr", 0),
	CAT_PURR_2(		111, "mob.cat.purr", 1),
	CAT_PURR_3(		112, "mob.cat.purr", 2),

	CAT_PURREOW(295, "mob.cat.purreow"),
	CAT_PURREOW_1(	113, "mob.cat.purreow", 0),
	CAT_PURREOW_2(	114, "mob.cat.purreow", 1),

	ENDERMEN_DEATH(		115, "mob.endermen.death"),

	ENDERMEN_HIT(296, "mob.endermen.hit"),
	ENDERMEN_HIT_1(		116, "mob.endermen.hit", 0),
	ENDERMEN_HIT_2(		117, "mob.endermen.hit", 1),
	ENDERMEN_HIT_3(		118, "mob.endermen.hit", 2),
	ENDERMEN_HIT_4(		119, "mob.endermen.hit", 3),

	ENDERMEN_IDLE(297, "mob.endermen.idle"),
	ENDERMEN_IDLE_1(	120, "mob.endermen.idle", 0),
	ENDERMEN_IDLE_2(	121, "mob.endermen.idle", 1),
	ENDERMEN_IDLE_3(	122, "mob.endermen.idle", 2),
	ENDERMEN_IDLE_4(	123, "mob.endermen.idle", 3),
	ENDERMEN_IDLE_5(	124, "mob.endermen.idle", 4),

	ENDERMEN_PORTAL(	125, "mob.endermen.portal"),
	ENDERMEN_PORTAL_1(	298, "mob.endermen.portal", 0),
	ENDERMEN_PORTAL_2(	126, "mob.endermen.portal", 1),

	ENDERMEN_SCREAM(299, "mob.endermen.scream"),
	ENDERMEN_SCREAM_1(	127, "mob.endermen.scream", 0),
	ENDERMEN_SCREAM_2(	128, "mob.endermen.scream", 1),
	ENDERMEN_SCREAM_3(	129, "mob.endermen.scream", 2),
	ENDERMEN_SCREAM_4(	130, "mob.endermen.scream", 3),

	ENDERMEN_STARE(		131, "mob.endermen.stare"),

	GHAST_AFFECTIONATE_SCREAM(	131,	"mob.ghast.affectionate scream"),
	GHAST_CHARGE(				52,		"mob.ghast.charge"),
	GHAST_DEATH(				53,		"mob.ghast.death"),
	GHAST_FIREBALL(				132,	"mob.ghast.fireball"),

	GHAST_MOAN(300, "mob.ghast.moan"),
	GHAST_MOAN_1(				50,		"mob.ghast.moan", 0),
	GHAST_MOAN_2(				133,	"mob.ghast.moan", 1),
	GHAST_MOAN_3(				134,	"mob.ghast.moan", 2),
	GHAST_MOAN_4(				135,	"mob.ghast.moan", 3),
	GHAST_MOAN_5(				136,	"mob.ghast.moan", 4),
	GHAST_MOAN_6(				137,	"mob.ghast.moan", 5),
	GHAST_MOAN_7(				138,	"mob.ghast.moan", 6),

	GHAST_SCREAM(301, "mob.ghast.scream"),
	GHAST_SCREAM_1(				51,		"mob.ghast.scream", 0),
	GHAST_SCREAM_2(				139,	"mob.ghast.scream", 1),
	GHAST_SCREAM_3(				140,	"mob.ghast.scream", 2),
	GHAST_SCREAM_4(				141,	"mob.ghast.scream", 3),
	GHAST_SCREAM_5(				142,	"mob.ghast.scream", 4),

	IRONGOLEM_DEATH(	143, "mob.irongolem.death"),

	IRONGOLEM_HIT(302, "mob.irongolem.hit"),
	IRONGOLEM_HIT_1(	144, "mob.irongolem.hit", 0),
	IRONGOLEM_HIT_2(	145, "mob.irongolem.hit", 1),
	IRONGOLEM_HIT_3(	146, "mob.irongolem.hit", 2),
	IRONGOLEM_HIT_4(	147, "mob.irongolem.hit", 3),

	IRONGOLEM_THROW(	148, "mob.irongolem.throw"),

	IRONGOLEM_WALK(303, "mob.irongolem.walk"),
	IRONGOLEM_WALK_1(	149, "mob.irongolem.walk", 0),
	IRONGOLEM_WALK_2(	150, "mob.irongolem.walk", 1),
	IRONGOLEM_WALK_3(	151, "mob.irongolem.walk", 2),
	IRONGOLEM_WALK_4(	152, "mob.irongolem.walk", 3),

	MAGMACUBE_BIG(304, "mob.magmacube.big"),
	MAGMACUBE_BIG_1(	153, "mob.magmacube.big", 0),
	MAGMACUBE_BIG_2(	154, "mob.magmacube.big", 1),
	MAGMACUBE_BIG_3(	155, "mob.magmacube.big", 2),
	MAGMACUBE_BIG_4(	156, "mob.magmacube.big", 3),

	MAGMACUBE_JUMP(305, "mob.magmacube.jump"),
	MAGMACUBE_JUMP_1(	157, "mob.magmacube.jump", 0),
	MAGMACUBE_JUMP_2(	158, "mob.magmacube.jump", 1),
	MAGMACUBE_JUMP_3(	159, "mob.magmacube.jump", 2),
	MAGMACUBE_JUMP_4(	160, "mob.magmacube.jump", 3),

	MAGMACUBE_SMALL(306, "mob.magmacube.small"),
	MAGMACUBE_SMALL_1(	161, "mob.magmacube.small", 0),
	MAGMACUBE_SMALL_2(	162, "mob.magmacube.small", 1),
	MAGMACUBE_SMALL_3(	163, "mob.magmacube.small", 2),
	MAGMACUBE_SMALL_4(	164, "mob.magmacube.small", 3),
	MAGMACUBE_SMALL_5(	165, "mob.magmacube.small", 4),

	SILVERFISH_HIT(307, "mob.silverfish.hit"),
	SILVERFISH_HIT_1(	166, "mob.silverfish.hit", 0),
	SILVERFISH_HIT_2(	167, "mob.silverfish.hit", 1),
	SILVERFISH_HIT_3(	168, "mob.silverfish.hit", 2),

	SILVERFISH_KILL(	169, "mob.silverfish.kill"),

	SILVERFISH_SAY(308, "mob.silverfish.say"),
	SILVERFISH_SAY_1(	170, "mob.silverfish.say", 0),
	SILVERFISH_SAY_2(	171, "mob.silverfish.say", 1),
	SILVERFISH_SAY_3(	172, "mob.silverfish.say", 2),
	SILVERFISH_SAY_4(	173, "mob.silverfish.say", 3),

	SILVERFISH_STEP(309, "mob.silverfish.step"),
	SILVERFISH_STEP_1(	174, "mob.silverfish.step", 0),
	SILVERFISH_STEP_2(	175, "mob.silverfish.step", 1),
	SILVERFISH_STEP_3(	176, "mob.silverfish.step", 2),
	SILVERFISH_STEP_4(	177, "mob.silverfish.step", 3),

	WOLF_BARK(310, "mob.wolf.bark"),
	WOLF_BARK_1(		54,		"mob.wolf.bark", 0),
	WOLF_BARK_2(		178,	"mob.wolf.bark", 1),
	WOLF_BARK_3(		179,	"mob.wolf.bark", 2),

	WOLF_DEATH(			61,		"mob.wolf.death"),

	WOLF_GROWL(311, "mob.wolf.growl"),
	WOLF_GROWL_1(		55,		"mob.wolf.growl", 0),
	WOLF_GROWL_2(		180,	"mob.wolf.growl", 1),
	WOLF_GROWL_3(		181,	"mob.wolf.growl", 2),

	WOLF_HOWL(312, "mob.wolf.howl"),
	WOLF_HOWL_1(		56,		"mob.wolf.howl", 0),
	WOLF_HOWL_2(		182,	"mob.wolf.howl", 1),
	WOLF_HURT_1(		57,		"mob.wolf.hurt", 0),
	WOLF_HURT_2(		183,	"mob.wolf.hurt", 1),
	WOLF_HURT_3(		184,	"mob.wolf.hurt", 2),

	WOLF_PANTING(		58,		"mob.wolf.panting"),
	WOLF_WHINE(			59,		"mob.wolf.whine"),
	WOLF_SHAKE(			60,		"mob.wolf.shake"),

	ZOMBIE_METAL(313, "mob.zombie.metal"),
	ZOMBIE_METAL_1(		185, "mob.zombie.metal", 0),
	ZOMBIE_METAL_2(		186, "mob.zombie.metal", 1),
	ZOMBIE_METAL_3(		187, "mob.zombie.metal", 2),

	ZOMBIE_WOOD(314, "mob.zombie.wood"),
	ZOMBIE_WOOD_1(		188, "mob.zombie.wood", 0),
	ZOMBIE_WOOD_2(		189, "mob.zombie.wood", 1),
	ZOMBIE_WOOD_3(		190, "mob.zombie.wood", 2),
	ZOMBIE_WOOD_4(		191, "mob.zombie.wood", 3),

	ZOMBIE_WOOD_BREAK(	192, "mob.zombie.woodbreak"),

	ZOMBIEPIG(315, "mob.zombiepig.zpig"),
	ZOMBIEPIG_1(		62,		"mob.zombiepig.zpig", 0),
	ZOMBIEPIG_2(		193,	"mob.zombiepig.zpig", 1),
	ZOMBIEPIG_3(		194,	"mob.zombiepig.zpig", 2),
	ZOMBIEPIG_4(		195,	"mob.zombiepig.zpig", 3),

	ZOMBIEPIG_ANGRY(316, "mob.zombiepig.zpigangry"),
	ZOMBIEPIG_ANGRY_1(	64,		"mob.zombiepig.zpigangry", 0),
	ZOMBIEPIG_ANGRY_2(	196,	"mob.zombiepig.zpigangry", 1),
	ZOMBIEPIG_ANGRY_3(	197,	"mob.zombiepig.zpigangry", 2),
	ZOMBIEPIG_ANGRY_4(	198,	"mob.zombiepig.zpigangry", 3),

	ZOMBIEPIG_DEATH(	199,	"mob.zombiepig.zpigdeath"),

	ZOMBIEPIG_HURT(317, "mob.zombiepig.zpighurt"),
	ZOMBIEPIG_HURT_1(	63,		"mob.zombiepig.zpighurt", 0),
	ZOMBIEPIG_HURT_2(	200,	"mob.zombiepig.zpighurt", 1),

	CHICKEN(318, "mob.chicken"),
	CHICKEN_1(		32,		"mob.chicken", 0),
	CHICKEN_2(		201,	"mob.chicken", 1),
	CHICKEN_3(		202,	"mob.chicken", 2),

	CHICKEN_HURT(319, "mob.chickenhurt"),
	CHICKEN_HURT_1(	33,		"mob.chickenhurt", 0),
	CHICKEN_HURT_2(	203,	"mob.chickenhurt", 1),

	CHICKEN_PLOP(	204,	"mob.chickenplop"),

	COW(320, "mob.cow"),
	COW_1(		34,		"mob.cow", 0),
	COW_2(		205,	"mob.cow", 1),
	COW_3(		206,	"mob.cow", 2),
	COW_4(		207,	"mob.cow", 3),

	COW_HURT(321, "mob.cowhurt"),
	COW_HURT_1(	35,		"mob.cowhurt", 0),
	COW_HURT_2(	208,	"mob.cowhurt", 1),
	COW_HURT_3(	209,	"mob.cowhurt", 2),

	CREEPER(322, "mob.creeper"),
	CREEPER_1(		36,		"mob.creeper", 0),
	CREEPER_2(		210,	"mob.creeper", 1),
	CREEPER_3(		211,	"mob.creeper", 2),
	CREEPER_4(		212,	"mob.creeper", 3),

	CREEPER_HURT(	37,		"mob.creeperdeath"),

	PIG(323, "mob.pig"),
	PIG_1(		38,		"mob.pig", 0),
	PIG_2(		213,	"mob.pig", 1),
	PIG_3(		214,	"mob.pig", 2),

	PIG_HURT(	39,		"mob.pigdeath"),

	SHEEP(324, "mob.sheep"),
	SHEEP_1(40,		"mob.sheep", 0),
	SHEEP_2(215,	"mob.sheep", 1),
	SHEEP_3(216,	"mob.sheep", 2),

	SKELETON(325, "mob.skeleton"),
	SKELETON_1(			42,		"mob.skeleton", 0),
	SKELETON_2(			217,	"mob.skeleton", 1),
	SKELETON_3(			218,	"mob.skeleton", 2),

	SKELETON_DEATH(		219,	"mob.skeletondeath"),

	SKELETON_HURT(325, "mob.skeletonhurt"),
	SKELETON_HURT_1(	43,		"mob.skeletonhurt", 0),
	SKELETON_HURT_2(	220,	"mob.skeletonhurt", 1),
	SKELETON_HURT_3(	221,	"mob.skeletonhurt", 2),
	SKELETON_HURT_4(	222,	"mob.skeletonhurt", 3),

	SLIME(326, "mob.slime"),
	SLIME_1(		44,		"mob.slime", 0),
	SLIME_2(		223,	"mob.slime", 1),
	SLIME_3(		224,	"mob.slime", 2),
	SLIME_4(		225,	"mob.slime", 3),
	SLIME_5(		226,	"mob.slime", 4),

	SLIME_ATTACK(327, "mob.slimeattack"),
	SLIME_ATTACK_1(	45,		"mob.slimeattack", 0),
	SLIME_ATTACK_2(	227,	"mob.slimeattack", 1),

	SPIDER(328, "mob.spider"),
	SPIDER_1(		46,		"mob.spider", 0),
	SPIDER_2(		228,	"mob.spider", 1),
	SPIDER_3(		229,	"mob.spider", 2),
	SPIDER_4(		230,	"mob.spider", 3),

	SPIDER_HURT(	47, 	"mob.spiderdeath"),

	ZOMBIE(329, "mob.zombie"),
	ZOMBIE_1(		48,		"mob.zombie", 0),
	ZOMBIE_2(		231,	"mob.zombie", 1),
	ZOMBIE_3(		232,	"mob.zombie", 2),

	ZOMBIE_DEATH(	233,	"mob.zombiedeath"),

	ZOMBIE_HURT(330, "mob.zombiehurt"),
	ZOMBIE_HURT_1(	49,		"mob.zombiehurt", 0),
	ZOMBIE_HURT_2(	234,	"mob.zombiehurt", 1),

	/* Notes */
	NOTE_BASS(			235, "note.bass"),
	NOTE_BASS_ATTACK(	236, "note.bassattack"),
	NOTE_BASS_DRUM(		237, "note.bd"),
	NOTE_HARP(			238, "note.harp"),
	NOTE_HAT(			239, "note.hat"),
	NOTE_PLING(			240, "note.pling"),
	NOTE_SNARE(			241, "note.snare"),

	/* Portal */
	PORTAL(65, "portal.portal"),
	PORTAL_TRAVEL(66, "portal.travel"),
	PROTAL_TRIGGER(67, "portal.trigger"),

	/* Random Sound Effects */
	BOW(			75,		"random.bow"),

	BOW_HIT(331, "random.bowhit"),
	BOW_HIT_1(		242,	"random.bowhit", 0),
	BOW_HIT_2(		243,	"random.bowhit", 1),
	BOW_HIT_3(		244,	"random.bowhit", 2),
	BOW_HIT_4(		245,	"random.bowhit", 3),

	BREAK(			246,	"random.break"),
	BREATH(			76,		"random.breath"),
	BURP(			247,	"random.burp"),
	CHEST_CLOSE(	248,	"random.chestclosed"),
	CHEST_OPEN(		249,	"random.chestopen"),
	CLICK(			77,		"random.click"),
	DOOR_CLOSE(		78,		"random.door_close"),
	DOOR_OPEN(		79,		"random.door_open"),
	DRINK(			250,	"random.drink"),
	BOW_STRING(		80,		"random.drr"),

	EAT(332, "random.eat"),
	EAT_1(			251,	"random.eat", 0),
	EAT_2(			252,	"random.eat", 1),
	EAT_3(			253,	"random.eat", 2),

	EXPLODE(333, "randome.explode"),
	EXPLODE_1(		81,		"random.explode", 0),
	EXPLODE_2(		254,	"random.explode", 1),
	EXPLODE_3(		255,	"random.explode", 2),
	EXPLODE_4(		256,	"random.explode", 3),

	FIZZ(			82,		"random.fizz"),
	FUSE(			83,		"random.fuse"),

	GLASS_BREAK(334, "random.glass"),
	GLASS_BREAK_1(	84,		"random.glass", 0),
	GLASS_BREAK_2(	85,		"random.glass", 1),
	GLASS_BREAK_3(	86,		"random.glass", 2),

	HURT(			87,		"random.hurt"),
	LEVEL_UP(		257,	"random.levelup"),
	EXPLODE_OLD(	258,	"random.old_explode"),
	ORB(			259,	"random.orb"),
	POP(			88,		"random.pop"),
	SPLASH(			89,		"random.splash"),
	WOOD_CLICK(		90,		"random.wood click"),

	/* Block Sound Effects */
	CLOTH(335, "step.cloth"),
	CLOTH_1(	68,		"step.cloth", 0),
	CLOTH_2(	260,	"step.cloth", 1),
	CLOTH_3(	261,	"step.cloth", 2),
	CLOTH_4(	262,	"step.cloth", 3),

	GRASS(336, "step.grass"),
	GRASS_1(	69,		"step.grass", 0),
	GRASS_2(	263,	"step.grass", 1),
	GRASS_3(	264,	"step.grass", 2),
	GRASS_4(	265,	"step.grass", 3),

	GRAVEL(337, "step.gravel"),
	GRAVEL_1(	70,		"step.gravel", 0),
	GRAVEL_2(	266,	"step.gravel", 1),
	GRAVEL_3(	267,	"step.gravel", 2),
	GRAVEL_4(	268,	"step.gravel", 3),

	SAND(338, "step.gravel"),
	SAND_1(		71,		"step.sand", 0),
	SAND_2(		269,	"step.sand", 1),
	SAND_3(		270,	"step.sand", 2),
	SAND_4(		271,	"step.sand", 3),

	SNOW(339, "step.snow"),
	SNOW_1(		72,		"step.snow", 0),
	SNOW_2(		272,	"step.snow", 1),
	SNOW_3(		273,	"step.snow", 2),
	SNOW_4(		274,	"step.snow", 3),

	STONE(340, "step.stone"),
	STONE_1(	73,		"step.stone", 0),
	STONE_2(	275,	"step.stone", 1),
	STONE_3(	276,	"step.stone", 2),
	STONE_4(	277,	"step.stone", 3),

	WOOD(341, "step.wood"),
	WOOD_1(		74,		"step.wood", 0),
	WOOD_2(		278,	"step.wood", 1),
	WOOD_3(		279,	"step.wood", 2),
	WOOD_4(		280,	"step.wood", 3),

	/* Tile */
	PISTON_IN(	281, "tile.piston.in"),
	PISTON_OUT(	282, "tile.piston.out");

	private final int id;
	private final String name;
	private final int soundId;
	private static final Map<String, SoundEffect> lookupName = new HashMap<String, SoundEffect>();
	private static final Map<Integer, SoundEffect> lookupId = new HashMap<Integer, SoundEffect>();
	private static int last = 0;

	SoundEffect(final int id, final String name) {
		this.id = id;
		this.name = name;
		this.soundId = -1;
	}

	SoundEffect(final int id, final String name, final int soundId) {
		this.id = id;
		this.name = name;
		this.soundId = soundId;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getSoundId() {
		return soundId;
	}

	public static SoundEffect getSoundEffectFromId(int id) {
		return lookupId.get(id);
	}

	public static SoundEffect getSoundEffectFromName(String name) {
		return lookupName.get(name);
	}

	public static int getMaxId() {
		return last;
	}

	static {
		for (SoundEffect i : values()) {
			lookupName.put(i.getName(), i);
			lookupId.put(i.getId(), i);
			if (i.getId() > last) {
				last = i.getId();
			}
		}
	}
}
