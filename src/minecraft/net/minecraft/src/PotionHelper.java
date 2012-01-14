package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer; //Spout HD
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;

public class PotionHelper {

	public static final String field_40367_a = null;
	public static final String sugarEffect;
	public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
	public static final String spiderEyeEffect;
	public static final String fermentedSpiderEyeEffect;
	public static final String speckledMelonEffect;
	public static final String blazePowderEffect;
	public static final String magmaCreamEffect;
	public static final String redstoneEffect;
	public static final String glowstoneEffect;
	public static final String gunpowderEffect;
	private static final HashMap field_40370_l = new HashMap();
	private static final HashMap field_40371_m = new HashMap();
	private static final HashMap field_40368_n;
	private static final String[] potionPrefixes;

	public static boolean isBitOn(int var0, int var1) {
		return (var0 & 1 << var1) != 0;
	}

	private static int isFlagSet(int var0, int var1) {
		return isBitOn(var0, var1) ? 1 : 0;
	}

	private static int isFlagUnset(int var0, int var1) {
		return isBitOn(var0, var1) ? 0 : 1;
	}

	public static int func_40352_a(int var0) {
		return func_40351_a(var0, 5, 4, 3, 2, 1);
	}

	public static int func_40354_a(Collection var0) {
		int var1 = Colorizer.getWaterBottleColor(); //Spout HD
		if (var0 != null && !var0.isEmpty()) {
			float var2 = 0.0F;
			float var3 = 0.0F;
			float var4 = 0.0F;
			float var5 = 0.0F;
			Iterator var6 = var0.iterator();

			while (var6.hasNext()) {
				PotionEffect var7 = (PotionEffect)var6.next();
				int var8 = Potion.potionTypes[var7.getPotionID()].getLiquidColor();

				for (int var9 = 0; var9 <= var7.getAmplifier(); ++var9) {
					var2 += (float)(var8 >> 16 & 255) / 255.0F;
					var3 += (float)(var8 >> 8 & 255) / 255.0F;
					var4 += (float)(var8 >> 0 & 255) / 255.0F;
					++var5;
				}
			}

			var2 = var2 / var5 * 255.0F;
			var3 = var3 / var5 * 255.0F;
			var4 = var4 / var5 * 255.0F;
			return (int)var2 << 16 | (int)var3 << 8 | (int)var4;
		}
		else {
			return var1;
		}
	}

	public static int func_40358_a(int var0, boolean var1) {
		if (!var1) {
			if (field_40368_n.containsKey(Integer.valueOf(var0))) {
				return ((Integer)field_40368_n.get(Integer.valueOf(var0))).intValue();
			}
			else {
				int var2 = func_40354_a(getPotionEffects(var0, false));
				field_40368_n.put(Integer.valueOf(var0), Integer.valueOf(var2));
				return var2;
			}
		}
		else {
			return func_40354_a(getPotionEffects(var0, var1));
		}
	}

	public static String func_40359_b(int var0) {
		int var1 = func_40352_a(var0);
		return potionPrefixes[var1];
	}

	private static int func_40347_a(boolean var0, boolean var1, boolean var2, int var3, int var4, int var5, int var6) {
		int var7 = 0;
		if (var0) {
			var7 = isFlagUnset(var6, var4);
		}
		else if (var3 != -1) {
			if (var3 == 0 && getOnBitCount(var6) == var4) {
				var7 = 1;
			}
			else if (var3 == 1 && getOnBitCount(var6) > var4) {
				var7 = 1;
			}
			else if (var3 == 2 && getOnBitCount(var6) < var4) {
				var7 = 1;
			}
		}
		else {
			var7 = isFlagSet(var6, var4);
		}

		if (var1) {
			var7 *= var5;
		}

		if (var2) {
			var7 *= -1;
		}

		return var7;
	}

	private static int getOnBitCount(int var0) {
		int var1;
		for (var1 = 0; var0 > 0; ++var1) {
			var0 &= var0 - 1;
		}

		return var1;
	}

	private static int func_40355_a(String var0, int var1, int var2, int var3) {
		if (var1 < var0.length() && var2 >= 0 && var1 < var2) {
			int var4 = var0.indexOf(124, var1);
			int var5;
			int var17;
			if (var4 >= 0 && var4 < var2) {
				var5 = func_40355_a(var0, var1, var4 - 1, var3);
				if (var5 > 0) {
					return var5;
				}
				else {
					var17 = func_40355_a(var0, var4 + 1, var2, var3);
					return var17 > 0 ? var17 : 0;
				}
			}
			else {
				var5 = var0.indexOf(38, var1);
				if (var5 >= 0 && var5 < var2) {
					var17 = func_40355_a(var0, var1, var5 - 1, var3);
					if (var17 <= 0) {
						return 0;
					}
					else {
						int var18 = func_40355_a(var0, var5 + 1, var2, var3);
						return var18 <= 0 ? 0 : (var17 > var18 ? var17 : var18);
					}
				}
				else {
					boolean var6 = false;
					boolean var7 = false;
					boolean var8 = false;
					boolean var9 = false;
					boolean var10 = false;
					byte var11 = -1;
					int var12 = 0;
					int var13 = 0;
					int var14 = 0;

					for (int var15 = var1; var15 < var2; ++var15) {
						char var16 = var0.charAt(var15);
						if (var16 >= 48 && var16 <= 57) {
							if (var6) {
								var13 = var16 - 48;
								var7 = true;
							}
							else {
								var12 *= 10;
								var12 += var16 - 48;
								var8 = true;
							}
						}
						else if (var16 == 42) {
							var6 = true;
						}
						else if (var16 == 33) {
							if (var8) {
								var14 += func_40347_a(var9, var7, var10, var11, var12, var13, var3);
								var9 = false;
								var10 = false;
								var6 = false;
								var7 = false;
								var8 = false;
								var13 = 0;
								var12 = 0;
								var11 = -1;
							}

							var9 = true;
						}
						else if (var16 == 45) {
							if (var8) {
								var14 += func_40347_a(var9, var7, var10, var11, var12, var13, var3);
								var9 = false;
								var10 = false;
								var6 = false;
								var7 = false;
								var8 = false;
								var13 = 0;
								var12 = 0;
								var11 = -1;
							}

							var10 = true;
						}
						else if (var16 != 61 && var16 != 60 && var16 != 62) {
							if (var16 == 43 && var8) {
								var14 += func_40347_a(var9, var7, var10, var11, var12, var13, var3);
								var9 = false;
								var10 = false;
								var6 = false;
								var7 = false;
								var8 = false;
								var13 = 0;
								var12 = 0;
								var11 = -1;
							}
						}
						else {
							if (var8) {
								var14 += func_40347_a(var9, var7, var10, var11, var12, var13, var3);
								var9 = false;
								var10 = false;
								var6 = false;
								var7 = false;
								var8 = false;
								var13 = 0;
								var12 = 0;
								var11 = -1;
							}

							if (var16 == 61) {
								var11 = 0;
							}
							else if (var16 == 60) {
								var11 = 2;
							}
							else if (var16 == 62) {
								var11 = 1;
							}
						}
					}

					if (var8) {
						var14 += func_40347_a(var9, var7, var10, var11, var12, var13, var3);
					}

					return var14;
				}
			}
		}
		else {
			return 0;
		}
	}

	public static List getPotionEffects(int var0, boolean var1) {
		ArrayList var2 = null;
		Potion[] var3 = Potion.potionTypes;
		int var4 = var3.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			Potion var6 = var3[var5];
			if (var6 != null && (!var6.func_40612_i() || var1)) {
				String var7 = (String)field_40370_l.get(Integer.valueOf(var6.getId()));
				if (var7 != null) {
					int var8 = func_40355_a(var7, 0, var7.length(), var0);
					if (var8 > 0) {
						int var9 = 0;
						String var10 = (String)field_40371_m.get(Integer.valueOf(var6.getId()));
						if (var10 != null) {
							var9 = func_40355_a(var10, 0, var10.length(), var0);
							if (var9 < 0) {
								var9 = 0;
							}
						}

						if (var6.isInstant()) {
							var8 = 1;
						}
						else {
							var8 = 1200 * (var8 * 3 + (var8 - 1) * 2);
							var8 >>= var9;
							var8 = (int)Math.round((double)var8 * var6.func_40610_g());
							if ((var0 & 16384) != 0) {
								var8 = (int)Math.round((double)var8 * 0.75D + 0.5D);
							}
						}

						if (var2 == null) {
							var2 = new ArrayList();
						}

						var2.add(new PotionEffect(var6.getId(), var8, var9));
					}
				}
			}
		}

		return var2;
	}

	private static int brewBitOperations(int var0, int var1, boolean var2, boolean var3, boolean var4) {
		if (var4) {
			if (!isBitOn(var0, var1)) {
				return 0;
			}
		}
		else if (var2) {
			var0 &= ~(1 << var1);
		}
		else if (var3) {
			if ((var0 & 1 << var1) != 0) {
				var0 &= ~(1 << var1);
			}
			else {
				var0 |= 1 << var1;
			}
		}
		else {
			var0 |= 1 << var1;
		}

		return var0;
	}

	public static int brewPotionData(int var0, String var1) {
		byte var2 = 0;
		int var3 = var1.length();
		boolean var4 = false;
		boolean var5 = false;
		boolean var6 = false;
		boolean var7 = false;
		int var8 = 0;

		for (int var9 = var2; var9 < var3; ++var9) {
			char var10 = var1.charAt(var9);
			if (var10 >= 48 && var10 <= 57) {
				var8 *= 10;
				var8 += var10 - 48;
				var4 = true;
			}
			else if (var10 == 33) {
				if (var4) {
					var0 = brewBitOperations(var0, var8, var6, var5, var7);
					var7 = false;
					var5 = false;
					var6 = false;
					var4 = false;
					var8 = 0;
				}

				var5 = true;
			}
			else if (var10 == 45) {
				if (var4) {
					var0 = brewBitOperations(var0, var8, var6, var5, var7);
					var7 = false;
					var5 = false;
					var6 = false;
					var4 = false;
					var8 = 0;
				}

				var6 = true;
			}
			else if (var10 == 43) {
				if (var4) {
					var0 = brewBitOperations(var0, var8, var6, var5, var7);
					var7 = false;
					var5 = false;
					var6 = false;
					var4 = false;
					var8 = 0;
				}
			}
			else if (var10 == 38) {
				if (var4) {
					var0 = brewBitOperations(var0, var8, var6, var5, var7);
					var7 = false;
					var5 = false;
					var6 = false;
					var4 = false;
					var8 = 0;
				}

				var7 = true;
			}
		}

		if (var4) {
			var0 = brewBitOperations(var0, var8, var6, var5, var7);
		}

		return var0 & 32767;
	}

	public static int func_40351_a(int var0, int var1, int var2, int var3, int var4, int var5) {
		return (isBitOn(var0, var1) ? 16 : 0) | (isBitOn(var0, var2) ? 8 : 0) | (isBitOn(var0, var3) ? 4 : 0) | (isBitOn(var0, var4) ? 2 : 0) | (isBitOn(var0, var5) ? 1 : 0);
	}

	static {
		field_40370_l.put(Integer.valueOf(Potion.regeneration.getId()), "0 & !1 & !2 & !3 & 0+6");
		sugarEffect = "-0+1-2-3&4-4+13";
		field_40370_l.put(Integer.valueOf(Potion.moveSpeed.getId()), "!0 & 1 & !2 & !3 & 1+6");
		magmaCreamEffect = "+0+1-2-3&4-4+13";
		field_40370_l.put(Integer.valueOf(Potion.fireResistance.getId()), "0 & 1 & !2 & !3 & 0+6");
		speckledMelonEffect = "+0-1+2-3&4-4+13";
		field_40370_l.put(Integer.valueOf(Potion.heal.getId()), "0 & !1 & 2 & !3");
		spiderEyeEffect = "-0-1+2-3&4-4+13";
		field_40370_l.put(Integer.valueOf(Potion.poison.getId()), "!0 & !1 & 2 & !3 & 2+6");
		fermentedSpiderEyeEffect = "-0+3-4+13";
		field_40370_l.put(Integer.valueOf(Potion.weakness.getId()), "!0 & !1 & !2 & 3 & 3+6");
		field_40370_l.put(Integer.valueOf(Potion.harm.getId()), "!0 & !1 & 2 & 3");
		field_40370_l.put(Integer.valueOf(Potion.moveSlowdown.getId()), "!0 & 1 & !2 & 3 & 3+6");
		blazePowderEffect = "+0-1-2+3&4-4+13";
		field_40370_l.put(Integer.valueOf(Potion.damageBoost.getId()), "0 & !1 & !2 & 3 & 3+6");
		glowstoneEffect = "+5-6-7";
		field_40371_m.put(Integer.valueOf(Potion.moveSpeed.getId()), "5");
		field_40371_m.put(Integer.valueOf(Potion.digSpeed.getId()), "5");
		field_40371_m.put(Integer.valueOf(Potion.damageBoost.getId()), "5");
		field_40371_m.put(Integer.valueOf(Potion.regeneration.getId()), "5");
		field_40371_m.put(Integer.valueOf(Potion.harm.getId()), "5");
		field_40371_m.put(Integer.valueOf(Potion.heal.getId()), "5");
		field_40371_m.put(Integer.valueOf(Potion.resistance.getId()), "5");
		field_40371_m.put(Integer.valueOf(Potion.poison.getId()), "5");
		redstoneEffect = "-5+6-7";
		gunpowderEffect = "+14&13-13";
		field_40368_n = new HashMap();
		potionPrefixes = new String[] {"potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky"};
	}
}
