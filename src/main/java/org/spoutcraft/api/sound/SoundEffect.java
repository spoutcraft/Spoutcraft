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
	CAVE_RANDOM(0, "ambient.cave.cave"),
	CAVE_MYSTERIOUS(1, "ambient.cave.cave", 0),
	CAVE_LIGHT_MYSTERIOUS(2, "ambient.cave.cave", 1),
	CAVE_SHADOW(3, "ambient.cave.cave", 2),
	CAVE_DEEP(4, "ambient.cave.cave", 3),
	CAVE_NEW_PASSAGE(5, "ambient.cave.cave", 4),
	CAVE_PASSING_SHADOW(6, "ambient.cave.cave", 5),
	CAVE_DARK_SHADOW(7, "ambient.cave.cave", 6),
	CAVE_FEAR(8, "ambient.cave.cave", 7),
	CAVE_DARK_MYSTERIOUS(9, "ambient.cave.cave", 8),
	CAVE_LARGE_FEAR(10, "ambient.cave.cave", 9),
	CAVE_RUMBLE(11, "ambient.cave.cave", 10),
	CAVE_SHORT_MYSTERIOUS(12, "ambient.cave.cave", 11),
	CAVE_MONSTER_ROAR(13, "ambient.cave.cave", 12),

	WEATHER_RAIN(14, "ambient.weather.rain"),
	WEATHER_RAIN_1(15, "ambient.weather.rain", 0),
	WEATHER_RAIN_2(16, "ambient.weather.rain", 1),
	WEATHER_RAIN_3(17, "ambient.weather.rain", 2),
	WEATHER_RAIN_4(18, "ambient.weather.rain", 3),

	WEATHER_THUNDER(19, "ambient.weather.thunder"),
	WEATHER_THUNDER_1(20, "ambient.weather.thunder", 0),
	WEATHER_THUNDER_2(21, "ambient.weather.thunder", 1),
	WEATHER_THUNDER_3(22, "ambient.weather.thunder", 2),

	/* Damage */
	FALL_BIG(23, "damage.fallbig"),
	FALL_SMALL(24, "damage.fallsmall"),

	HIT(25, "damage.hit"),
	HIT_1(26, "damage.hit", 0),
	HIT_2(27, "damage.hit", 1),
	HIT_3(28, "damage.hit", 2),

	/* Dig */
	DIG_CLOTH(29, "dig.cloth"),
	DIG_CLOTH_1(30, "dig.cloth", 0),
	DIG_CLOTH_2(31, "dig.cloth", 1),
	DIG_CLOTH_3(32, "dig.cloth", 2),
	DIG_CLOTH_4(33, "dig.cloth", 3),

	DIG_GRASS(34, "dig.grass"),
	DIG_GRASS_1(35, "dig.grass", 0),
	DIG_GRASS_2(36, "dig.grass", 1),
	DIG_GRASS_3(37, "dig.grass", 2),
	DIG_GRASS_4(38, "dig.grass", 3),

	DIG_GRAVEL(39, "dig.gravel"),
	DIG_GRAVEL_1(40, "dig.gravel", 0),
	DIG_GRAVEL_2(41, "dig.gravel", 1),
	DIG_GRAVEL_3(42, "dig.gravel", 2),
	DIG_GRAVEL_4(43, "dig.gravel", 3),

	DIG_SAND(44, "dig.sand"),
	DIG_SAND_1(45, "dig.sand", 0),
	DIG_SAND_2(46, "dig.sand", 1),
	DIG_SAND_3(47, "dig.sand", 2),
	DIG_SAND_4(48, "dig.sand", 3),

	DIG_SNOW(49, "dig.snow"),
	DIG_SNOW_1(50, "dig.snow", 0),
	DIG_SNOW_2(51, "dig.snow", 1),
	DIG_SNOW_3(52, "dig.snow", 2),
	DIG_SNOW_4(53, "dig.snow", 3),

	DIG_STONE(54, "dig.stone"),
	DIG_STONE_1(55, "dig.stone", 0),
	DIG_STONE_2(56, "dig.stone", 1),
	DIG_STONE_3(57, "dig.stone", 2),
	DIG_STONE_4(58, "dig.stone", 3),

	DIG_WOOD(59, "dig.wood"),
	DIG_WOOD_1(60, "dig.wood", 0),
	DIG_WOOD_2(61, "dig.wood", 1),
	DIG_WOOD_3(62, "dig.wood", 2),
	DIG_WOOD_4(63, "dig.wood", 3),

	/* Fire */
	FIRE(64, "fire.fire"),
	FIRE_IGNITE(65, "fire.ignite"),

	/* Fireworks */
	FIREWORK_BLAST(66, "fireworks.blast"),
	FIREWORK_BLAST_FAR(67, "fireworks.blast_far"),
	FIREWORK_LARGE_BLAST(68, "fireworks.largeBlast"),
	FIREWORK_LARGE_BLAST_FAR(69, "fireworks.largeBlast_far"),
	FIREWORK_LAUNCH(70, "fireworks.launch"),
	FIREWORK_TWINKLE(71, "fireworks.twinkle"),
	FIREWORK_TWINKLE_FAR(72, "fireworks.twinkle_far"),

	/* Liquid */
	LAVA(73, "liquid.lava"),
	LAVA_POP(74, "liquid.lavapop"),

	WATER_SPLASH(75, "liquid.splash"),
	WATER_SPLASH_1(76, "liquid.splash", 0),
	WATER_SPLASH_2(77, "liquid.splash", 1),

	SWIM(78, "liquid.swim"),
	SWIM_1(79, "liquid.swim", 0),
	SWIM_2(80, "liquid.swim", 1),
	SWIM_3(81, "liquid.swim", 2),
	SWIM_4(82, "liquid.swim", 3),

	WATER(83, "liquid.water"),

	/* Minecart */
	MINECART_BASE(84, "minecart.base"),
	MINECART_INSIDE(85, "minecart.inside"),

	/* Mob Sound Effects */
	BAT_DEATH(86, "mob.bat.death"),
	BAT_HURT(87, "mob.bat.hurt"),
	BAT_HURT_1(88, "mob.bat.hurt", 0),
	BAT_HURT_2(89, "mob.bat.hurt", 1),
	BAT_HURT_3(90, "mob.bat.hurt", 2),
	BAT_HURT_4(91, "mob.bat.hurt", 3),
	BAT_IDLE(92, "mob.bat.idle"),
	BAT_IDLE_1(93, "mob.bat.idle", 0),
	BAT_IDLE_2(94, "mob.bat.idle", 1),
	BAT_IDLE_3(95, "mob.bat.idle", 2),
	BAT_IDLE_4(96, "mob.bat.idle", 3),
	BAT_LOOP(97, "mob.bat.loop"),
	BAT_TAKEOFF(98, "mob.bat.takeoff"),

	BLAZE_BREATHE(99, "mob.blaze.breathe"),
	BLAZE_BREATHE_1(100, "mob.blaze.breathe", 0),
	BLAZE_BREATHE_2(101, "mob.blaze.breathe", 1),
	BLAZE_BREATHE_3(102, "mob.blaze.breathe", 2),
	BLAZE_BREATHE_4(103, "mob.blaze.breathe", 3),
	BLAZE_DEATH(104, "mob.blaze.death"),
	BLAZE_HIT(105, "mob.blaze.hit"),
	BLAZE_HIT_1(106, "mob.blaze.hit", 0),
	BLAZE_HIT_2(107, "mob.blaze.hit", 1),
	BLAZE_HIT_3(108, "mob.blaze.hit", 2),
	BLAZE_HIT_4(109, "mob.blaze.hit", 3),

	CAT_HISS(110, "mob.cat.hiss"),
	CAT_HISS_1(111, "mob.cat.hiss", 0),
	CAT_HISS_2(112, "mob.cat.hiss", 1),
	CAT_HISS_3(113, "mob.cat.hiss", 2),
	CAT_HITT(114, "mob.cat.hitt"),
	CAT_HITT_1(115, "mob.cat.hitt", 0),
	CAT_HITT_2(116, "mob.cat.hitt", 1),
	CAT_HITT_3(117, "mob.cat.hitt", 2),
	CAT_MEOW(118, "mob.cat.meow"),
	CAT_MEOW_1(119, "mob.cat.meow", 0),
	CAT_MEOW_2(120, "mob.cat.meow", 1),
	CAT_MEOW_3(121, "mob.cat.meow", 2),
	CAT_MEOW_4(122, "mob.cat.meow", 3),
	CAT_PURR(123, "mob.cat.purr"),
	CAT_PURR_1(124, "mob.cat.purr", 0),
	CAT_PURR_2(125, "mob.cat.purr", 1),
	CAT_PURR_3(126, "mob.cat.purr", 2),
	CAT_PURREOW(127, "mob.cat.purreow"),
	CAT_PURREOW_1(128, "mob.cat.purreow", 0),
	CAT_PURREOW_2(129, "mob.cat.purreow", 1),

	CHICKEN_HURT(130, "mob.chicken.hurt"),
	CHICKEN_HURT_1(131, "mob.chicken.hurt", 0),
	CHICKEN_HURT_2(132, "mob.chicken.hurt", 1),
	CHICKEN_PLOP(133, "mob.chicken.plop"),
	CHICKEN_SAY(134, "mob.chicken.say"),
	CHICKEN_SAY_1(135, "mob.chicken.say", 0),
	CHICKEN_SAY_2(136, "mob.chicken.say", 1),
	CHICKEN_SAY_3(137, "mob.chicken.say", 2),
	CHICKEN_STEP(138, "mob.chicken.step"),
	CHICKEN_STEP_1(139, "mob.chicken.step", 0),
	CHICKEN_STEP_2(140, "mob.chicken.step", 1),

	COW_HURT(141, "mob.cow.hurt"),
	COW_HURT_1(142, "mob.cow.hurt", 0),
	COW_HURT_2(143, "mob.cow.hurt", 1),
	COW_HURT_3(144, "mob.cow.hurt", 2),
	COW_SAY(145, "mob.cow.say"),
	COW_SAY_1(146, "mob.cow.say", 0),
	COW_SAY_2(147, "mob.cow.say", 1),
	COW_SAY_3(148, "mob.cow.say", 2),
	COW_SAY_4(149, "mob.cow.say", 3),
	COW_STEP(150, "mob.cow.step"),
	COW_STEP_1(151, "mob.cow.step", 0),
	COW_STEP_2(152, "mob.cow.step", 1),
	COW_STEP_3(153, "mob.cow.step", 2),
	COW_STEP_4(154, "mob.cow.step", 3),

	CREEPER_DEATH(155, "mob.creeper.death"),
	CREEPER_SAY(156, "mob.creeper.say"),
	CREEPER_SAY_1(157, "mob.creeper.say", 0),
	CREEPER_SAY_2(158, "mob.creeper.say", 1),
	CREEPER_SAY_3(159, "mob.creeper.say", 2),
	CREEPER_SAY_4(160, "mob.creeper.say", 3),

	ENDERDRAGON_END(161, "mob.enderdragon.end"),
	ENDERDRAGON_GROWL(162, "mob.enderdragon.growl"),
	ENDERDRAGON_GROWL_1(163, "mob.enderdragon.growl", 0),
	ENDERDRAGON_GROWL_2(164, "mob.enderdragon.growl", 1),
	ENDERDRAGON_GROWL_3(165, "mob.enderdragon.growl", 2),
	ENDERDRAGON_GROWL_4(166, "mob.enderdragon.growl", 3),
	ENDERDRAGON_HIT(167, "mob.enderdragon.hit"),
	ENDERDRAGON_HIT_1(168, "mob.enderdragon.hit", 0),
	ENDERDRAGON_HIT_2(169, "mob.enderdragon.hit", 1),
	ENDERDRAGON_HIT_3(170, "mob.enderdragon.hit", 2),
	ENDERDRAGON_HIT_4(171, "mob.enderdragon.hit", 3),
	ENDERDRAGON_WINGS(172, "mob.enderdragon.wings"),
	ENDERDRAGON_WINGS_1(173, "mob.enderdragon.wings", 0),
	ENDERDRAGON_WINGS_2(174, "mob.enderdragon.wings", 1),
	ENDERDRAGON_WINGS_3(175, "mob.enderdragon.wings", 2),
	ENDERDRAGON_WINGS_4(176, "mob.enderdragon.wings", 3),
	ENDERDRAGON_WINGS_5(177, "mob.enderdragon.wings", 4),
	ENDERDRAGON_WINGS_6(178, "mob.enderdragon.wings", 5),

	ENDERMEN_DEATH(179, "mob.endermen.death"),
	ENDERMEN_HIT(180, "mob.endermen.hit"),
	ENDERMEN_HIT_1(181, "mob.endermen.hit", 0),
	ENDERMEN_HIT_2(182, "mob.endermen.hit", 1),
	ENDERMEN_HIT_3(183, "mob.endermen.hit", 2),
	ENDERMEN_HIT_4(184, "mob.endermen.hit", 3),
	ENDERMEN_IDLE(185, "mob.endermen.idle"),
	ENDERMEN_IDLE_1(186, "mob.endermen.idle", 0),
	ENDERMEN_IDLE_2(187, "mob.endermen.idle", 1),
	ENDERMEN_IDLE_3(188, "mob.endermen.idle", 2),
	ENDERMEN_IDLE_4(189, "mob.endermen.idle", 3),
	ENDERMEN_IDLE_5(190, "mob.endermen.idle", 4),
	ENDERMEN_PORTAL(191, "mob.endermen.portal"),
	ENDERMEN_PORTAL_1(192, "mob.endermen.portal", 0),
	ENDERMEN_PORTAL_2(193, "mob.endermen.portal", 1),
	ENDERMEN_SCREAM(194, "mob.endermen.scream"),
	ENDERMEN_SCREAM_1(195, "mob.endermen.scream", 0),
	ENDERMEN_SCREAM_2(196, "mob.endermen.scream", 1),
	ENDERMEN_SCREAM_3(197, "mob.endermen.scream", 2),
	ENDERMEN_SCREAM_4(198, "mob.endermen.scream", 3),
	ENDERMEN_STARE(199, "mob.endermen.stare"),

	GHAST_AFFECTIONATE_SCREAM(200,	"mob.ghast.affectionate scream"),
	GHAST_CHARGE(201, "mob.ghast.charge"),
	GHAST_DEATH(202, "mob.ghast.death"),
	GHAST_FIREBALL(203, "mob.ghast.fireball"),
	GHAST_MOAN(204, "mob.ghast.moan"),
	GHAST_MOAN_1(205, "mob.ghast.moan", 0),
	GHAST_MOAN_2(206, "mob.ghast.moan", 1),
	GHAST_MOAN_3(207, "mob.ghast.moan", 2),
	GHAST_MOAN_4(208, "mob.ghast.moan", 3),
	GHAST_MOAN_5(209, "mob.ghast.moan", 4),
	GHAST_MOAN_6(210, "mob.ghast.moan", 5),
	GHAST_MOAN_7(211, "mob.ghast.moan", 6),
	GHAST_SCREAM(212, "mob.ghast.scream"),
	GHAST_SCREAM_1(213, "mob.ghast.scream", 0),
	GHAST_SCREAM_2(214, "mob.ghast.scream", 1),
	GHAST_SCREAM_3(215, "mob.ghast.scream", 2),
	GHAST_SCREAM_4(216, "mob.ghast.scream", 3),
	GHAST_SCREAM_5(217, "mob.ghast.scream", 4),

	IRONGOLEM_DEATH(218, "mob.irongolem.death"),
	IRONGOLEM_HIT(219, "mob.irongolem.hit"),
	IRONGOLEM_HIT_1(220, "mob.irongolem.hit", 0),
	IRONGOLEM_HIT_2(221, "mob.irongolem.hit", 1),
	IRONGOLEM_HIT_3(222, "mob.irongolem.hit", 2),
	IRONGOLEM_HIT_4(223, "mob.irongolem.hit", 3),
	IRONGOLEM_THROW(224, "mob.irongolem.throw"),
	IRONGOLEM_WALK(225, "mob.irongolem.walk"),
	IRONGOLEM_WALK_1(226, "mob.irongolem.walk", 0),
	IRONGOLEM_WALK_2(227, "mob.irongolem.walk", 1),
	IRONGOLEM_WALK_3(228, "mob.irongolem.walk", 2),
	IRONGOLEM_WALK_4(229, "mob.irongolem.walk", 3),

	MAGMACUBE_BIG(230, "mob.magmacube.big"),
	MAGMACUBE_BIG_1(231, "mob.magmacube.big", 0),
	MAGMACUBE_BIG_2(232, "mob.magmacube.big", 1),
	MAGMACUBE_BIG_3(233, "mob.magmacube.big", 2),
	MAGMACUBE_BIG_4(234, "mob.magmacube.big", 3),
	MAGMACUBE_JUMP(235, "mob.magmacube.jump"),
	MAGMACUBE_JUMP_1(236, "mob.magmacube.jump", 0),
	MAGMACUBE_JUMP_2(237, "mob.magmacube.jump", 1),
	MAGMACUBE_JUMP_3(238, "mob.magmacube.jump", 2),
	MAGMACUBE_JUMP_4(239, "mob.magmacube.jump", 3),
	MAGMACUBE_SMALL(240, "mob.magmacube.small"),
	MAGMACUBE_SMALL_1(241, "mob.magmacube.small", 0),
	MAGMACUBE_SMALL_2(242, "mob.magmacube.small", 1),
	MAGMACUBE_SMALL_3(243, "mob.magmacube.small", 2),
	MAGMACUBE_SMALL_4(244, "mob.magmacube.small", 3),
	MAGMACUBE_SMALL_5(245, "mob.magmacube.small", 4),

	PIG_DEATH(246, "mob.pig.death"),
	PIG_SAY(247, "mob.pig.say"),
	PIG_SAY_1(248, "mob.pig.say", 0),
	PIG_SAY_2(249, "mob.pig.say", 1),
	PIG_SAY_3(250, "mob.pig.say", 2),
	PIG_STEP(251, "mob.pig.step"),
	PIG_STEP_1(252, "mob.pig.step", 0),
	PIG_STEP_2(253, "mob.pig.step", 1),
	PIG_STEP_3(254, "mob.pig.step", 2),
	PIG_STEP_4(255, "mob.pig.step", 3),
	PIG_STEP_5(256, "mob.pig.step", 4),

	SHEEP_SAY(257, "mob.sheep.say"),
	SHEEP_SAY_1(258, "mob.sheep.say", 0),
	SHEEP_SAY_2(259, "mob.sheep.say", 1),
	SHEEP_SAY_3(260, "mob.sheep.say", 2),
	SHEEP_SHEAR(261, "mob.sheep.shear"),
	SHEEP_STEP(262, "mob.sheep.step"),
	SHEEP_STEP_1(263, "mob.sheep.step", 0),
	SHEEP_STEP_2(264, "mob.sheep.step", 1),
	SHEEP_STEP_3(265, "mob.sheep.step", 2),
	SHEEP_STEP_4(266, "mob.sheep.step", 3),
	SHEEP_STEP_5(267, "mob.sheep.step", 4),

	SILVERFISH_HIT(268, "mob.silverfish.hit"),
	SILVERFISH_HIT_1(269, "mob.silverfish.hit", 0),
	SILVERFISH_HIT_2(270, "mob.silverfish.hit", 1),
	SILVERFISH_HIT_3(271, "mob.silverfish.hit", 2),
	SILVERFISH_KILL(272, "mob.silverfish.kill"),
	SILVERFISH_SAY(273, "mob.silverfish.say"),
	SILVERFISH_SAY_1(274, "mob.silverfish.say", 0),
	SILVERFISH_SAY_2(275, "mob.silverfish.say", 1),
	SILVERFISH_SAY_3(276, "mob.silverfish.say", 2),
	SILVERFISH_SAY_4(277, "mob.silverfish.say", 3),
	SILVERFISH_STEP(278, "mob.silverfish.step"),
	SILVERFISH_STEP_1(279, "mob.silverfish.step", 0),
	SILVERFISH_STEP_2(280, "mob.silverfish.step", 1),
	SILVERFISH_STEP_3(281, "mob.silverfish.step", 2),
	SILVERFISH_STEP_4(282, "mob.silverfish.step", 3),

	SKELETON_DEATH(283, "mob.skeleton.death"),
	SKELETON_HURT(284, "mob.skeleton.hurt"),
	SKELETON_HURT_1(285, "mob.skeleton.hurt", 0),
	SKELETON_HURT_2(286, "mob.skeleton.hurt", 1),
	SKELETON_HURT_3(287, "mob.skeleton.hurt", 2),
	SKELETON_HURT_4(288, "mob.skeleton.hurt", 3),
	SKELETON_SAY(289, "mob.skeleton.say"),
	SKELETON_SAY_1(290, "mob.skeleton.say", 0),
	SKELETON_SAY_2(291, "mob.skeleton.say", 1),
	SKELETON_SAY_3(292, "mob.skeleton.say", 2),
	SKELETON_STEP(293, "mob.skeleton.step"),
	SKELETON_STEP_1(294, "mob.skeleton.step", 0),
	SKELETON_STEP_2(295, "mob.skeleton.step", 1),
	SKELETON_STEP_3(296, "mob.skeleton.step", 2),
	SKELETON_STEP_4(297, "mob.skeleton.step", 3),

	SLIME_ATTACK(298, "mob.slime.attack"),
	SLIME_ATTACK_1(299, "mob.slime.attack", 0),
	SLIME_ATTACK_2(300, "mob.slime.attack", 1),
	SLIME_BIG(301, "mob.slime.big"),
	SLIME_BIG_1(302, "mob.slime.big", 0),
	SLIME_BIG_2(303, "mob.slime.big", 1),
	SLIME_BIG_3(304, "mob.slime.big", 2),
	SLIME_BIG_4(305, "mob.slime.big", 3),
	SLIME_SMALL(306, "mob.slime.small"),
	SLIME_SMALL_1(307, "mob.slime.small", 0),
	SLIME_SMALL_2(308, "mob.slime.small", 1),
	SLIME_SMALL_3(309, "mob.slime.small", 2),
	SLIME_SMALL_4(310, "mob.slime.small", 3),
	SLIME_SMALL_5(311, "mob.slime.small", 4),

	SPIDER_DEATH(312, "mob.spider.death"),
	SPIDER_SAY(313, "mob.spider.say"),
	SPIDER_SAY_1(314, "mob.spider.say", 0),
	SPIDER_SAY_2(315, "mob.spider.say", 1),
	SPIDER_SAY_3(316, "mob.spider.say", 2),
	SPIDER_SAY_4(317, "mob.spider.say", 3),
	SPIDER_STEP(318, "mob.spider.step"),
	SPIDER_STEP_1(319, "mob.spider.step", 0),
	SPIDER_STEP_2(320, "mob.spider.step", 1),
	SPIDER_STEP_3(321, "mob.spider.step", 2),
	SPIDER_STEP_4(322, "mob.spider.step", 3),

	WITHER_DEATH(323, "mob.wither.death"),
	WITHER_HURT(324, "mob.wither.hurt"),
	WITHER_HURT_1(325, "mob.wither.hurt", 0),
	WITHER_HURT_2(326, "mob.wither.hurt", 1),
	WITHER_HURT_3(327, "mob.wither.hurt", 2),
	WITHER_HURT_4(328, "mob.wither.hurt", 3),
	WITHER_IDLE(329, "mob.wither.idle"),
	WITHER_IDLE_1(330, "mob.wither.idle", 0),
	WITHER_IDLE_2(331, "mob.wither.idle", 1),
	WITHER_IDLE_3(332, "mob.wither.idle", 2),
	WITHER_IDLE_4(333, "mob.wither.idle", 3),
	WITHER_SHOOT(334, "mob.wither.shoot"),
	WITHER_SPAWN(335, "mob.wither.spawn"),

	WOLF_BARK(336, "mob.wolf.bark"),
	WOLF_BARK_1(337, "mob.wolf.bark", 0),
	WOLF_BARK_2(338, "mob.wolf.bark", 1),
	WOLF_BARK_3(339, "mob.wolf.bark", 2),
	WOLF_DEATH(340, "mob.wolf.death"),
	WOLF_GROWL(341, "mob.wolf.growl"),
	WOLF_GROWL_1(342, "mob.wolf.growl", 0),
	WOLF_GROWL_2(343, "mob.wolf.growl", 1),
	WOLF_GROWL_3(344, "mob.wolf.growl", 2),
	WOLF_HOWL(345, "mob.wolf.howl"),
	WOLF_HOWL_1(346, "mob.wolf.howl", 0),
	WOLF_HOWL_2(347, "mob.wolf.howl", 1),
	WOLF_HURT_1(348, "mob.wolf.hurt", 0),
	WOLF_HURT_2(349, "mob.wolf.hurt", 1),
	WOLF_HURT_3(350, "mob.wolf.hurt", 2),
	WOLF_PANTING(351, "mob.wolf.panting"),
	WOLF_SHAKE(352, "mob.wolf.shake"),
	WOLF_STEP(353, "mob.wolf.step"),
	WOLF_STEP_1(354, "mob.wolf.step", 0),
	WOLF_STEP_2(355, "mob.wolf.step", 1),
	WOLF_STEP_3(356, "mob.wolf.step", 2),
	WOLF_STEP_4(357, "mob.wolf.step", 3),
	WOLF_STEP_5(358, "mob.wolf.step", 4),
	WOLF_WHINE(359, "mob.wolf.whine"),

	ZOMBIE_DEATH(360, "mob.zombie.death"),
	ZOMBIE_HURT(361, "mob.zombie.hurt"),
	ZOMBIE_HURT_1(362, "mob.zombie.hurt", 0),
	ZOMBIE_HURT_2(363, "mob.zombie.hurt", 1),
	ZOMBIE_INFECT(364, "mob.zombie.infect"),
	ZOMBIE_METAL(365, "mob.zombie.metal"),
	ZOMBIE_METAL_1(366, "mob.zombie.metal", 0),
	ZOMBIE_METAL_2(367, "mob.zombie.metal", 1),
	ZOMBIE_METAL_3(368, "mob.zombie.metal", 2),
	ZOMBIE_REMEDY(369, "mob.zombie.remedy"),
	ZOMBIE_SAY(370, "mob.zombie.say"),
	ZOMBIE_SAY_1(371, "mob.zombie.say", 0),
	ZOMBIE_SAY_2(372, "mob.zombie.say", 1),
	ZOMBIE_SAY_3(373, "mob.zombie.say", 2),
	ZOMBIE_STEP(374, "mob.zombie.step"),
	ZOMBIE_STEP_1(375, "mob.zombie.step", 0),
	ZOMBIE_STEP_2(376, "mob.zombie.step", 1),
	ZOMBIE_STEP_3(377, "mob.zombie.step", 2),
	ZOMBIE_STEP_4(378, "mob.zombie.step", 3),
	ZOMBIE_STEP_5(379, "mob.zombie.step", 4),
	ZOMBIE_UNFECT(380, "mob.zombie.unfect"),
	ZOMBIE_WOOD(381, "mob.zombie.wood"),
	ZOMBIE_WOOD_1(382, "mob.zombie.wood", 0),
	ZOMBIE_WOOD_2(383, "mob.zombie.wood", 1),
	ZOMBIE_WOOD_3(384, "mob.zombie.wood", 2),
	ZOMBIE_WOOD_4(385, "mob.zombie.wood", 3),
	ZOMBIE_WOOD_BREAK(386, "mob.zombie.woodbreak"),

	ZOMBIEPIG(387, "mob.zombiepig.zpig"),
	ZOMBIEPIG_1(388, "mob.zombiepig.zpig", 0),
	ZOMBIEPIG_2(389, "mob.zombiepig.zpig", 1),
	ZOMBIEPIG_3(390, "mob.zombiepig.zpig", 2),
	ZOMBIEPIG_4(391, "mob.zombiepig.zpig", 3),
	ZOMBIEPIG_ANGRY(392, "mob.zombiepig.zpigangry"),
	ZOMBIEPIG_ANGRY_1(393, "mob.zombiepig.zpigangry", 0),
	ZOMBIEPIG_ANGRY_2(394, "mob.zombiepig.zpigangry", 1),
	ZOMBIEPIG_ANGRY_3(395, "mob.zombiepig.zpigangry", 2),
	ZOMBIEPIG_ANGRY_4(396, "mob.zombiepig.zpigangry", 3),
	ZOMBIEPIG_DEATH(397, "mob.zombiepig.zpigdeath"),
	ZOMBIEPIG_HURT(398, "mob.zombiepig.zpighurt"),
	ZOMBIEPIG_HURT_1(399, "mob.zombiepig.zpighurt", 0),
	ZOMBIEPIG_HURT_2(400, "mob.zombiepig.zpighurt", 1),

	/* Notes */
	NOTE_BASS(401, "note.bass"),
	NOTE_BASS_ATTACK(402, "note.bassattack"),
	NOTE_BASS_DRUM(403, "note.bd"),
	NOTE_HARP(404, "note.harp"),
	NOTE_HAT(405, "note.hat"),
	NOTE_PLING(406, "note.pling"),
	NOTE_SNARE(407, "note.snare"),

	/* Portal */
	PORTAL(408, "portal.portal"),
	PORTAL_TRAVEL(409, "portal.travel"),
	PORTAL_TRIGGER(410, "portal.trigger"),

	/* Random Sound Effects */
	ANVIL_BREAK(411, "random.anvil_break"),
	ANVIL_LAND(412, "random.anvil_land"),
	ANVIL_USE(413, "random.anvil_use"),

	BOW(414, "random.bow"),
	BOW_HIT(415, "random.bowhit"),
	BOW_HIT_1(416, "random.bowhit", 0),
	BOW_HIT_2(417, "random.bowhit", 1),
	BOW_HIT_3(418, "random.bowhit", 2),
	BOW_HIT_4(419, "random.bowhit", 3),

	BREAK(420, "random.break"),
	BREATH(421, "random.breath"),
	BURP(422, "random.burp"),
	CHEST_CLOSE(423, "random.chestclosed"),
	CHEST_OPEN(424, "random.chestopen"),
	CLASSIC_HURT(425, "random.classic_hurt"),
	CLICK(426, "random.click"),
	DOOR_CLOSE(427, "random.door_close"),
	DOOR_OPEN(428, "random.door_open"),
	DRINK(429, "random.drink"),

	EAT(430, "random.eat"),
	EAT_1(431, "random.eat", 0),
	EAT_2(432, "random.eat", 1),
	EAT_3(433, "random.eat", 2),

	EXPLODE(434, "randome.explode"),
	EXPLODE_1(435, "random.explode", 0),
	EXPLODE_2(436, "random.explode", 1),
	EXPLODE_3(437, "random.explode", 2),
	EXPLODE_4(438, "random.explode", 3),

	FIZZ(439, "random.fizz"),
	FUSE(440, "random.fuse"),

	GLASS_BREAK(441, "random.glass"),
	GLASS_BREAK_1(442, "random.glass", 0),
	GLASS_BREAK_2(443, "random.glass", 1),
	GLASS_BREAK_3(444, "random.glass", 2),

	LEVEL_UP(445, "random.levelup"),
	ORB(446, "random.orb"),
	POP(447, "random.pop"),
	SPLASH(448, "random.splash"),
	SUCCESSFUL_HIT(449, "random.successful_hit"),
	WOOD_CLICK(450, "random.wood click"),

	/* Block Sound Effects */
	STEP_CLOTH(451, "step.cloth"),
	STEP_CLOTH_1(452, "step.cloth", 0),
	STEP_CLOTH_2(453, "step.cloth", 1),
	STEP_CLOTH_3(454, "step.cloth", 2),
	STEP_CLOTH_4(455, "step.cloth", 3),

	STEP_GRASS(456, "step.grass"),
	STEP_GRASS_1(457, "step.grass", 0),
	STEP_GRASS_2(458, "step.grass", 1),
	STEP_GRASS_3(459, "step.grass", 2),
	STEP_GRASS_4(460, "step.grass", 3),
	STEP_GRASS_5(461, "step.grass", 4),
	STEP_GRASS_6(462, "step.grass", 5),

	STEP_GRAVEL(463, "step.gravel"),
	STEP_GRAVEL_1(464, "step.gravel", 0),
	STEP_GRAVEL_2(465, "step.gravel", 1),
	STEP_GRAVEL_3(466, "step.gravel", 2),
	STEP_GRAVEL_4(467, "step.gravel", 3),

	STEP_LADDER(468, "step.ladder"),
	STEP_LADDER_1(469, "step.ladder", 0),
	STEP_LADDER_2(470, "step.ladder", 1),
	STEP_LADDER_3(471, "step.ladder", 2),
	STEP_LADDER_4(472, "step.ladder", 3),
	STEP_LADDER_5(473, "step.ladder", 4),

	STEP_SAND(474, "step.gravel"),
	STEP_SAND_1(475, "step.sand", 0),
	STEP_SAND_2(476, "step.sand", 1),
	STEP_SAND_3(477, "step.sand", 2),
	STEP_SAND_4(478, "step.sand", 3),
	STEP_SAND_5(479, "step.sand", 4),

	STEP_SNOW(480, "step.snow"),
	STEP_SNOW_1(481, "step.snow", 0),
	STEP_SNOW_2(482, "step.snow", 1),
	STEP_SNOW_3(483, "step.snow", 2),
	STEP_SNOW_4(484, "step.snow", 3),

	STEP_STONE(485, "step.stone"),
	STEP_STONE_1(486, "step.stone", 0),
	STEP_STONE_2(487, "step.stone", 1),
	STEP_STONE_3(488, "step.stone", 2),
	STEP_STONE_4(489, "step.stone", 3),
	STEP_STONE_5(490, "step.stone", 4),
	STEP_STONE_6(491, "step.stone", 5),

	STEP_WOOD(492, "step.wood"),
	STEP_WOOD_1(493, "step.wood", 0),
	STEP_WOOD_2(494, "step.wood", 1),
	STEP_WOOD_3(495, "step.wood", 2),
	STEP_WOOD_4(496, "step.wood", 3),
	STEP_WOOD_5(497, "step.wood", 4),
	STEP_WOOD_6(498, "step.wood", 5),

	/* Tile */
	PISTON_IN(499, "tile.piston.in"),
	PISTON_OUT(500, "tile.piston.out"),

	/* Custom */
	CUSTOM_EFFECT(501, "custom");

	private final int id;
	private final String name;
	private final int variationId;
	private static final Map<String, SoundEffect> lookupName = new HashMap<String, SoundEffect>();
	private static final Map<Integer, SoundEffect> lookupId = new HashMap<Integer, SoundEffect>();
	private static int last = 0;

	SoundEffect(final int id, final String name) {
		this.id = id;
		this.name = name;
		this.variationId = -1;
	}

	SoundEffect(final int id, final String name, final int variationId) {
		this.id = id;
		this.name = name;
		this.variationId = variationId;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getVariationId() {
		return variationId;
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
