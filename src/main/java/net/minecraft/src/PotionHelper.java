package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeItem;
// MCpatcher End

public class PotionHelper {
	public static final String field_77924_a = null;
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
	public static final String goldenCarrotEffect;
	private static final HashMap potionRequirements = new HashMap();

	/** Potion effect amplifier map */
	private static final HashMap potionAmplifiers = new HashMap();
	private static final HashMap field_77925_n;

	/** An array of possible potion prefix names, as translation IDs. */
	private static final String[] potionPrefixes;

	/**
	 * Is the bit given set to 1?
	 */
	public static boolean checkFlag(int par0, int par1) {
		return (par0 & 1 << par1) != 0;
	}

	/**
	 * Returns 1 if the flag is set, 0 if it is not set.
	 */
	private static int isFlagSet(int par0, int par1) {
		return checkFlag(par0, par1) ? 1 : 0;
	}

	/**
	 * Returns 0 if the flag is set, 1 if it is not set.
	 */
	private static int isFlagUnset(int par0, int par1) {
		return checkFlag(par0, par1) ? 0 : 1;
	}

	public static int func_77909_a(int par0) {
		return func_77908_a(par0, 5, 4, 3, 2, 1);
	}

	/**
	 * Given a {@link Collection}<{@link PotionEffect}> will return an Integer color.
	 */
	public static int calcPotionLiquidColor(Collection par0Collection) {
		// MCPatcher Start
		int var1 = ColorizeItem.getWaterBottleColor();
		// MCPatcher End

		if (par0Collection != null && !par0Collection.isEmpty()) {
			float var2 = 0.0F;
			float var3 = 0.0F;
			float var4 = 0.0F;
			float var5 = 0.0F;
			Iterator var6 = par0Collection.iterator();

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
		} else {
			return var1;
		}
	}

	public static boolean func_82817_b(Collection par0Collection) {
		Iterator var1 = par0Collection.iterator();
		PotionEffect var2;

		do {
			if (!var1.hasNext()) {
				return true;
			}

			var2 = (PotionEffect)var1.next();
		} while (var2.getIsAmbient());

		return false;
	}

	public static int func_77915_a(int par0, boolean par1) {
		if (!par1) {
			if (field_77925_n.containsKey(Integer.valueOf(par0))) {
				return ((Integer)field_77925_n.get(Integer.valueOf(par0))).intValue();
			} else {
				int var2 = calcPotionLiquidColor(getPotionEffects(par0, false));
				field_77925_n.put(Integer.valueOf(par0), Integer.valueOf(var2));
				return var2;
			}
		} else {
			return calcPotionLiquidColor(getPotionEffects(par0, par1));
		}
	}

	public static String func_77905_c(int par0) {
		int var1 = func_77909_a(par0);
		return potionPrefixes[var1];
	}

	private static int func_77904_a(boolean par0, boolean par1, boolean par2, int par3, int par4, int par5, int par6) {
		int var7 = 0;

		if (par0) {
			var7 = isFlagUnset(par6, par4);
		} else if (par3 != -1) {
			if (par3 == 0 && countSetFlags(par6) == par4) {
				var7 = 1;
			} else if (par3 == 1 && countSetFlags(par6) > par4) {
				var7 = 1;
			} else if (par3 == 2 && countSetFlags(par6) < par4) {
				var7 = 1;
			}
		} else {
			var7 = isFlagSet(par6, par4);
		}

		if (par1) {
			var7 *= par5;
		}

		if (par2) {
			var7 *= -1;
		}

		return var7;
	}

	/**
	 * Count the number of bits in an integer set to ON.
	 */
	private static int countSetFlags(int par0) {
		int var1;

		for (var1 = 0; par0 > 0; ++var1) {
			par0 &= par0 - 1;
		}

		return var1;
	}

	private static int parsePotionEffects(String par0Str, int par1, int par2, int par3) {
		if (par1 < par0Str.length() && par2 >= 0 && par1 < par2) {
			int var4 = par0Str.indexOf(124, par1);
			int var5;
			int var17;

			if (var4 >= 0 && var4 < par2) {
				var5 = parsePotionEffects(par0Str, par1, var4 - 1, par3);

				if (var5 > 0) {
					return var5;
				} else {
					var17 = parsePotionEffects(par0Str, var4 + 1, par2, par3);
					return var17 > 0 ? var17 : 0;
				}
			} else {
				var5 = par0Str.indexOf(38, par1);

				if (var5 >= 0 && var5 < par2) {
					var17 = parsePotionEffects(par0Str, par1, var5 - 1, par3);

					if (var17 <= 0) {
						return 0;
					} else {
						int var18 = parsePotionEffects(par0Str, var5 + 1, par2, par3);
						return var18 <= 0 ? 0 : (var17 > var18 ? var17 : var18);
					}
				} else {
					boolean var6 = false;
					boolean var7 = false;
					boolean var8 = false;
					boolean var9 = false;
					boolean var10 = false;
					byte var11 = -1;
					int var12 = 0;
					int var13 = 0;
					int var14 = 0;

					for (int var15 = par1; var15 < par2; ++var15) {
						char var16 = par0Str.charAt(var15);

						if (var16 >= 48 && var16 <= 57) {
							if (var6) {
								var13 = var16 - 48;
								var7 = true;
							} else {
								var12 *= 10;
								var12 += var16 - 48;
								var8 = true;
							}
						} else if (var16 == 42) {
							var6 = true;
						} else if (var16 == 33) {
							if (var8) {
								var14 += func_77904_a(var9, var7, var10, var11, var12, var13, par3);
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
						} else if (var16 == 45) {
							if (var8) {
								var14 += func_77904_a(var9, var7, var10, var11, var12, var13, par3);
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
						} else if (var16 != 61 && var16 != 60 && var16 != 62) {
							if (var16 == 43 && var8) {
								var14 += func_77904_a(var9, var7, var10, var11, var12, var13, par3);
								var9 = false;
								var10 = false;
								var6 = false;
								var7 = false;
								var8 = false;
								var13 = 0;
								var12 = 0;
								var11 = -1;
							}
						} else {
							if (var8) {
								var14 += func_77904_a(var9, var7, var10, var11, var12, var13, par3);
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
							} else if (var16 == 60) {
								var11 = 2;
							} else if (var16 == 62) {
								var11 = 1;
							}
						}
					}

					if (var8) {
						var14 += func_77904_a(var9, var7, var10, var11, var12, var13, par3);
					}

					return var14;
				}
			}
		} else {
			return 0;
		}
	}

	/**
	 * Returns a list of effects for the specified potion damage value.
	 */
	public static List getPotionEffects(int par0, boolean par1) {
		ArrayList var2 = null;
		Potion[] var3 = Potion.potionTypes;
		int var4 = var3.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			Potion var6 = var3[var5];

			if (var6 != null && (!var6.isUsable() || par1)) {
				String var7 = (String)potionRequirements.get(Integer.valueOf(var6.getId()));

				if (var7 != null) {
					int var8 = parsePotionEffects(var7, 0, var7.length(), par0);

					if (var8 > 0) {
						int var9 = 0;
						String var10 = (String)potionAmplifiers.get(Integer.valueOf(var6.getId()));

						if (var10 != null) {
							var9 = parsePotionEffects(var10, 0, var10.length(), par0);

							if (var9 < 0) {
								var9 = 0;
							}
						}

						if (var6.isInstant()) {
							var8 = 1;
						} else {
							var8 = 1200 * (var8 * 3 + (var8 - 1) * 2);
							var8 >>= var9;
							var8 = (int)Math.round((double)var8 * var6.getEffectiveness());

							if ((par0 & 16384) != 0) {
								var8 = (int)Math.round((double)var8 * 0.75D + 0.5D);
							}
						}

						if (var2 == null) {
							var2 = new ArrayList();
						}

						PotionEffect var11 = new PotionEffect(var6.getId(), var8, var9);

						if ((par0 & 16384) != 0) {
							var11.setSplashPotion(true);
						}

						var2.add(var11);
					}
				}
			}
		}

		return var2;
	}

	/**
	 * Does bit operations for brewPotionData, given data, the index of the bit being operated upon, whether the bit will
	 * be removed, whether the bit will be toggled (NOT), or whether the data field will be set to 0 if the bit is not
	 * present.
	 */
	private static int brewBitOperations(int par0, int par1, boolean par2, boolean par3, boolean par4) {
		if (par4) {
			if (!checkFlag(par0, par1)) {
				return 0;
			}
		} else if (par2) {
			par0 &= ~(1 << par1);
		} else if (par3) {
			if ((par0 & 1 << par1) == 0) {
				par0 |= 1 << par1;
			} else {
				par0 &= ~(1 << par1);
			}
		} else {
			par0 |= 1 << par1;
		}

		return par0;
	}

	/**
	 * Generate a data value for a potion, given its previous data value and the encoded string of new effects it will
	 * receive
	 */
	public static int applyIngredient(int par0, String par1Str) {
		byte var2 = 0;
		int var3 = par1Str.length();
		boolean var4 = false;
		boolean var5 = false;
		boolean var6 = false;
		boolean var7 = false;
		int var8 = 0;

		for (int var9 = var2; var9 < var3; ++var9) {
			char var10 = par1Str.charAt(var9);

			if (var10 >= 48 && var10 <= 57) {
				var8 *= 10;
				var8 += var10 - 48;
				var4 = true;
			} else if (var10 == 33) {
				if (var4) {
					par0 = brewBitOperations(par0, var8, var6, var5, var7);
					var7 = false;
					var5 = false;
					var6 = false;
					var4 = false;
					var8 = 0;
				}

				var5 = true;
			} else if (var10 == 45) {
				if (var4) {
					par0 = brewBitOperations(par0, var8, var6, var5, var7);
					var7 = false;
					var5 = false;
					var6 = false;
					var4 = false;
					var8 = 0;
				}

				var6 = true;
			} else if (var10 == 43) {
				if (var4) {
					par0 = brewBitOperations(par0, var8, var6, var5, var7);
					var7 = false;
					var5 = false;
					var6 = false;
					var4 = false;
					var8 = 0;
				}
			} else if (var10 == 38) {
				if (var4) {
					par0 = brewBitOperations(par0, var8, var6, var5, var7);
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
			par0 = brewBitOperations(par0, var8, var6, var5, var7);
		}

		return par0 & 32767;
	}

	public static int func_77908_a(int par0, int par1, int par2, int par3, int par4, int par5) {
		return (checkFlag(par0, par1) ? 16 : 0) | (checkFlag(par0, par2) ? 8 : 0) | (checkFlag(par0, par3) ? 4 : 0) | (checkFlag(par0, par4) ? 2 : 0) | (checkFlag(par0, par5) ? 1 : 0);
	}

	static {
		potionRequirements.put(Integer.valueOf(Potion.regeneration.getId()), "0 & !1 & !2 & !3 & 0+6");
		sugarEffect = "-0+1-2-3&4-4+13";
		potionRequirements.put(Integer.valueOf(Potion.moveSpeed.getId()), "!0 & 1 & !2 & !3 & 1+6");
		magmaCreamEffect = "+0+1-2-3&4-4+13";
		potionRequirements.put(Integer.valueOf(Potion.fireResistance.getId()), "0 & 1 & !2 & !3 & 0+6");
		speckledMelonEffect = "+0-1+2-3&4-4+13";
		potionRequirements.put(Integer.valueOf(Potion.heal.getId()), "0 & !1 & 2 & !3");
		spiderEyeEffect = "-0-1+2-3&4-4+13";
		potionRequirements.put(Integer.valueOf(Potion.poison.getId()), "!0 & !1 & 2 & !3 & 2+6");
		fermentedSpiderEyeEffect = "-0+3-4+13";
		potionRequirements.put(Integer.valueOf(Potion.weakness.getId()), "!0 & !1 & !2 & 3 & 3+6");
		potionRequirements.put(Integer.valueOf(Potion.harm.getId()), "!0 & !1 & 2 & 3");
		potionRequirements.put(Integer.valueOf(Potion.moveSlowdown.getId()), "!0 & 1 & !2 & 3 & 3+6");
		blazePowderEffect = "+0-1-2+3&4-4+13";
		potionRequirements.put(Integer.valueOf(Potion.damageBoost.getId()), "0 & !1 & !2 & 3 & 3+6");
		goldenCarrotEffect = "-0+1+2-3+13&4-4";
		potionRequirements.put(Integer.valueOf(Potion.nightVision.getId()), "!0 & 1 & 2 & !3 & 2+6");
		potionRequirements.put(Integer.valueOf(Potion.invisibility.getId()), "!0 & 1 & 2 & 3 & 2+6");
		glowstoneEffect = "+5-6-7";
		potionAmplifiers.put(Integer.valueOf(Potion.moveSpeed.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.digSpeed.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.damageBoost.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.regeneration.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.harm.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.heal.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.resistance.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.poison.getId()), "5");
		redstoneEffect = "-5+6-7";
		gunpowderEffect = "+14&13-13";
		field_77925_n = new HashMap();
		potionPrefixes = new String[] {"potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky"};
	}
}
