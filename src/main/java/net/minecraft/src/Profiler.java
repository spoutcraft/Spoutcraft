package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
// Spout Start
import net.minecraft.client.Minecraft;
// Spout End

public class Profiler {

	/** List of parent sections */
	private final List sectionList = new ArrayList();

	/** List of timestamps (System.nanoTime) */
	private final List timestampList = new ArrayList();

	/** Flag profiling enabled */
	public boolean profilingEnabled = false;

	/** Current profiling section */
	private String profilingSection = "";

	/** Profiling map */
	private final Map profilingMap = new HashMap();

	/**
	 * Clear profiling.
	 */
	public void clearProfiling() {
		this.profilingMap.clear();
		this.profilingSection = "";
		this.sectionList.clear();
	}

	/**
	 * Start section
	 */
	public void startSection(String par1Str) {
		// Spout Start
		if (this.profilingEnabled && Thread.currentThread() == Minecraft.mainThread) {
		// Spout End
			if (this.profilingSection.length() > 0) {
				this.profilingSection = this.profilingSection + ".";
			}

			this.profilingSection = this.profilingSection + par1Str;
			this.sectionList.add(this.profilingSection);
			this.timestampList.add(Long.valueOf(System.nanoTime()));
		}
	}

	/**
	 * End section
	 */
	public void endSection() {
		// Spout Start
		if (this.profilingEnabled && Thread.currentThread() == Minecraft.mainThread) {
		// Spout End
			long var1 = System.nanoTime();
			long var3 = ((Long)this.timestampList.remove(this.timestampList.size() - 1)).longValue();
			this.sectionList.remove(this.sectionList.size() - 1);
			long var5 = var1 - var3;

			if (this.profilingMap.containsKey(this.profilingSection)) {
				this.profilingMap.put(this.profilingSection, Long.valueOf(((Long)this.profilingMap.get(this.profilingSection)).longValue() + var5));
			} else {
				this.profilingMap.put(this.profilingSection, Long.valueOf(var5));
			}

			if (var5 > 100000000L) {
				System.out.println("Something\'s taking too long! \'" + this.profilingSection + "\' took aprox " + (double)var5 / 1000000.0D + " ms");
			}

			this.profilingSection = !this.sectionList.isEmpty() ? (String)this.sectionList.get(this.sectionList.size() - 1) : "";
		}
	}

	/**
	 * Get profiling data
	 */
	public List getProfilingData(String par1Str) {
		if (!this.profilingEnabled) {
			return null;
		} else {
			long var3 = this.profilingMap.containsKey("root") ? ((Long)this.profilingMap.get("root")).longValue() : 0L;
			long var5 = this.profilingMap.containsKey(par1Str) ? ((Long)this.profilingMap.get(par1Str)).longValue() : -1L;
			ArrayList var7 = new ArrayList();

			if (par1Str.length() > 0) {
				par1Str = par1Str + ".";
			}

			long var8 = 0L;
			Iterator var10 = this.profilingMap.keySet().iterator();

			while (var10.hasNext()) {
				String var11 = (String)var10.next();

				if (var11.length() > par1Str.length() && var11.startsWith(par1Str) && var11.indexOf(".", par1Str.length() + 1) < 0) {
					var8 += ((Long)this.profilingMap.get(var11)).longValue();
				}
			}

			float var21 = (float)var8;

			if (var8 < var5) {
				var8 = var5;
			}

			if (var3 < var8) {
				var3 = var8;
			}

			Iterator var20 = this.profilingMap.keySet().iterator();
			String var12;

			while (var20.hasNext()) {
				var12 = (String)var20.next();

				if (var12.length() > par1Str.length() && var12.startsWith(par1Str) && var12.indexOf(".", par1Str.length() + 1) < 0) {
					long var13 = ((Long)this.profilingMap.get(var12)).longValue();
					double var15 = (double)var13 * 100.0D / (double)var8;
					double var17 = (double)var13 * 100.0D / (double)var3;
					String var19 = var12.substring(par1Str.length());
					var7.add(new ProfilerResult(var19, var15, var17));
				}
			}

			var20 = this.profilingMap.keySet().iterator();

			while (var20.hasNext()) {
				var12 = (String)var20.next();
				this.profilingMap.put(var12, Long.valueOf(((Long)this.profilingMap.get(var12)).longValue() * 999L / 1000L));
			}

			if ((float)var8 > var21) {
				var7.add(new ProfilerResult("unspecified", (double)((float)var8 - var21) * 100.0D / (double)var8, (double)((float)var8 - var21) * 100.0D / (double)var3));
			}

			Collections.sort(var7);
			var7.add(0, new ProfilerResult(par1Str, 100.0D, (double)var8 * 100.0D / (double)var3));
			return var7;
		}
	}

	/**
	 * End current section and start a new section
	 */
	public void endStartSection(String par1Str) {
		this.endSection();
		this.startSection(par1Str);
	}

	public String getNameOfLastSection() {
		return this.sectionList.size() == 0 ? "[UNKNOWN]" : (String)this.sectionList.get(this.sectionList.size() - 1);
	}
}
