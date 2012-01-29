package net.minecraft.src;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class StatFileWriter {
	private Map field_25102_a;
	private Map field_25101_b;
	private boolean field_27189_c;
	private StatsSyncher statsSyncher;

	public StatFileWriter(Session session, File file) {
		field_25102_a = new HashMap();
		field_25101_b = new HashMap();
		field_27189_c = false;
		File file1 = new File(file, "stats");
		if (!file1.exists()) {
			file1.mkdir();
		}
		File afile[] = file.listFiles();
		int i = afile.length;
		for (int j = 0; j < i; j++) {
			File file2 = afile[j];
			if (!file2.getName().startsWith("stats_") || !file2.getName().endsWith(".dat")) {
				continue;
			}
			File file3 = new File(file1, file2.getName());
			if (!file3.exists()) {
				System.out.println((new StringBuilder()).append("Relocating ").append(file2.getName()).toString());
				file2.renameTo(file3);
			}
		}

		statsSyncher = new StatsSyncher(session, this, file1);
	}

	public void readStat(StatBase statbase, int i) {
		writeStatToMap(field_25101_b, statbase, i);
		writeStatToMap(field_25102_a, statbase, i);
		field_27189_c = true;
	}

	private void writeStatToMap(Map map, StatBase statbase, int i) {
		Integer integer = (Integer)map.get(statbase);
		int j = integer != null ? integer.intValue() : 0;
		map.put(statbase, Integer.valueOf(j + i));
	}

	public Map func_27176_a() {
		return new HashMap(field_25101_b);
	}

	public void func_27179_a(Map map) {
		if (map == null) {
			return;
		}
		field_27189_c = true;
		StatBase statbase;
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext(); writeStatToMap(field_25102_a, statbase, ((Integer)map.get(statbase)).intValue())) {
			statbase = (StatBase)iterator.next();
			writeStatToMap(field_25101_b, statbase, ((Integer)map.get(statbase)).intValue());
		}
	}

	public void func_27180_b(Map map) {
		if (map == null) {
			return;
		}
		StatBase statbase;
		int i;
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext(); field_25102_a.put(statbase, Integer.valueOf(((Integer)map.get(statbase)).intValue() + i))) {
			statbase = (StatBase)iterator.next();
			Integer integer = (Integer)field_25101_b.get(statbase);
			i = integer != null ? integer.intValue() : 0;
		}
	}

	public void func_27187_c(Map map) {
		if (map == null) {
			return;
		}
		field_27189_c = true;
		StatBase statbase;
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext(); writeStatToMap(field_25101_b, statbase, ((Integer)map.get(statbase)).intValue())) {
			statbase = (StatBase)iterator.next();
		}
	}

	public static Map func_27177_a(String s) {
		HashMap hashmap = new HashMap();
		try {
			String s1 = "local";
			StringBuilder stringbuilder = new StringBuilder();
			J_JsonRootNode j_jsonrootnode = (new J_JdomParser()).parse(s);
			List list = j_jsonrootnode.getArrayNode(new Object[] {
			            "stats-change"
			        });
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				J_JsonNode j_jsonnode = (J_JsonNode)iterator.next();
				Map map = j_jsonnode.getFields();
				java.util.Map.Entry entry = (java.util.Map.Entry)map.entrySet().iterator().next();
				int i = Integer.parseInt(((J_JsonStringNode)entry.getKey()).getText());
				int j = Integer.parseInt(((J_JsonNode)entry.getValue()).getText());
				StatBase statbase = StatList.getOneShotStat(i);
				if (statbase == null) {
					System.out.println((new StringBuilder()).append(i).append(" is not a valid stat").toString());
				}
				else {
					stringbuilder.append(StatList.getOneShotStat(i).statGuid).append(",");
					stringbuilder.append(j).append(",");
					hashmap.put(statbase, Integer.valueOf(j));
				}
			}

			MD5String md5string = new MD5String(s1);
			String s2 = md5string.func_27369_a(stringbuilder.toString());
			if (!s2.equals(j_jsonrootnode.getStringValue(new Object[] {
			            "checksum"
			        }))) {
				System.out.println("CHECKSUM MISMATCH");
				return null;
			}
		}
		catch (J_InvalidSyntaxException j_invalidsyntaxexception) {
			j_invalidsyntaxexception.printStackTrace();
		}
		return hashmap;
	}

	public static String func_27185_a(String s, String s1, Map map) {
		StringBuilder stringbuilder = new StringBuilder();
		StringBuilder stringbuilder1 = new StringBuilder();
		boolean flag = true;
		stringbuilder.append("{\r\n");
		if (s != null && s1 != null) {
			stringbuilder.append("  \"user\":{\r\n");
			stringbuilder.append("    \"name\":\"").append(s).append("\",\r\n");
			stringbuilder.append("    \"sessionid\":\"").append(s1).append("\"\r\n");
			stringbuilder.append("  },\r\n");
		}
		stringbuilder.append("  \"stats-change\":[");
		StatBase statbase;
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext(); stringbuilder1.append(map.get(statbase)).append(",")) {
			statbase = (StatBase)iterator.next();
			if (!flag) {
				stringbuilder.append("},");
			}
			else {
				flag = false;
			}
			stringbuilder.append("\r\n    {\"").append(statbase.statId).append("\":").append(map.get(statbase));
			stringbuilder1.append(statbase.statGuid).append(",");
		}

		if (!flag) {
			stringbuilder.append("}");
		}
		MD5String md5string = new MD5String(s1);
		stringbuilder.append("\r\n  ],\r\n");
		stringbuilder.append("  \"checksum\":\"").append(md5string.func_27369_a(stringbuilder1.toString())).append("\"\r\n");
		stringbuilder.append("}");
		return stringbuilder.toString();
	}

	public boolean hasAchievementUnlocked(Achievement achievement) {
		return field_25102_a.containsKey(achievement);
	}

	public boolean canUnlockAchievement(Achievement achievement) {
		return achievement.parentAchievement == null || hasAchievementUnlocked(achievement.parentAchievement);
	}

	public int writeStat(StatBase statbase) {
		Integer integer = (Integer)field_25102_a.get(statbase);
		return integer != null ? integer.intValue() : 0;
	}

	public void func_27175_b() {
	}

	public void syncStats() {
		statsSyncher.syncStatsFileWithMap(func_27176_a());
	}

	public void func_27178_d() {
		if (field_27189_c && statsSyncher.func_27420_b()) {
			statsSyncher.beginSendStats(func_27176_a());
		}
		statsSyncher.func_27425_c();
	}
}
