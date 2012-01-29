package net.minecraft.src;

import java.io.*;
import java.util.Map;

public class StatsSyncher {
	private volatile boolean isBusy;
	private volatile Map field_27437_b;
	private volatile Map field_27436_c;
	private StatFileWriter statFileWriter;
	private File unsentDataFile;
	private File dataFile;
	private File unsentTempFile;
	private File tempFile;
	private File unsentOldFile;
	private File oldFile;
	private Session theSession;
	private int field_27427_l;
	private int field_27426_m;

	public StatsSyncher(Session session, StatFileWriter statfilewriter, File file) {
		isBusy = false;
		field_27437_b = null;
		field_27436_c = null;
		field_27427_l = 0;
		field_27426_m = 0;
		unsentDataFile = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append("_unsent.dat").toString());
		dataFile = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append(".dat").toString());
		unsentOldFile = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append("_unsent.old").toString());
		oldFile = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append(".old").toString());
		unsentTempFile = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append("_unsent.tmp").toString());
		tempFile = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append(".tmp").toString());
		if (!session.username.toLowerCase().equals(session.username)) {
			func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append("_unsent.dat").toString(), unsentDataFile);
			func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append(".dat").toString(), dataFile);
			func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append("_unsent.old").toString(), unsentOldFile);
			func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append(".old").toString(), oldFile);
			func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append("_unsent.tmp").toString(), unsentTempFile);
			func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append(".tmp").toString(), tempFile);
		}
		statFileWriter = statfilewriter;
		theSession = session;
		if (unsentDataFile.exists()) {
			statfilewriter.func_27179_a(func_27415_a(unsentDataFile, unsentTempFile, unsentOldFile));
		}
		beginReceiveStats();
	}

	private void func_28214_a(File file, String s, File file1) {
		File file2 = new File(file, s);
		if (file2.exists() && !file2.isDirectory() && !file1.exists()) {
			file2.renameTo(file1);
		}
	}

	private Map func_27415_a(File file, File file1, File file2) {
		if (file.exists()) {
			return func_27408_a(file);
		}
		if (file2.exists()) {
			return func_27408_a(file2);
		}
		if (file1.exists()) {
			return func_27408_a(file1);
		}
		else {
			return null;
		}
	}

	private Map func_27408_a(File file) {
		BufferedReader bufferedreader = null;
		try {
			bufferedreader = new BufferedReader(new FileReader(file));
			String s = "";
			StringBuilder stringbuilder = new StringBuilder();
			while ((s = bufferedreader.readLine()) != null) {
				stringbuilder.append(s);
			}
			Map map = StatFileWriter.func_27177_a(stringbuilder.toString());
			return map;
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		finally {
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				}
				catch (Exception exception2) {
					exception2.printStackTrace();
				}
			}
		}
		return null;
	}

	private void func_27410_a(Map map, File file, File file1, File file2)
	throws IOException {
		PrintWriter printwriter = new PrintWriter(new FileWriter(file1, false));
		try {
			printwriter.print(StatFileWriter.func_27185_a(theSession.username, "local", map));
		}
		finally {
			printwriter.close();
		}
		if (file2.exists()) {
			file2.delete();
		}
		if (file.exists()) {
			file.renameTo(file2);
		}
		file1.renameTo(file);
	}

	public void beginReceiveStats() {
		if (isBusy) {
			throw new IllegalStateException("Can't get stats from server while StatsSyncher is busy!");
		}
		else {
			field_27427_l = 100;
			isBusy = true;
			(new ThreadStatSyncherReceive(this)).start();
			return;
		}
	}

	public void beginSendStats(Map map) {
		if (isBusy) {
			throw new IllegalStateException("Can't save stats while StatsSyncher is busy!");
		}
		else {
			field_27427_l = 100;
			isBusy = true;
			(new ThreadStatSyncherSend(this, map)).start();
			return;
		}
	}

	public void syncStatsFileWithMap(Map map) {
		for (int i = 30; isBusy && --i > 0;) {
			try {
				Thread.sleep(100L);
			}
			catch (InterruptedException interruptedexception) {
				interruptedexception.printStackTrace();
			}
		}

		isBusy = true;
		try {
			func_27410_a(map, unsentDataFile, unsentTempFile, unsentOldFile);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		finally {
			isBusy = false;
		}
	}

	public boolean func_27420_b() {
		return field_27427_l <= 0 && !isBusy && field_27436_c == null;
	}

	public void func_27425_c() {
		if (field_27427_l > 0) {
			field_27427_l--;
		}
		if (field_27426_m > 0) {
			field_27426_m--;
		}
		if (field_27436_c != null) {
			statFileWriter.func_27187_c(field_27436_c);
			field_27436_c = null;
		}
		if (field_27437_b != null) {
			statFileWriter.func_27180_b(field_27437_b);
			field_27437_b = null;
		}
	}

	static Map func_27422_a(StatsSyncher statssyncher) {
		return statssyncher.field_27437_b;
	}

	static File func_27423_b(StatsSyncher statssyncher) {
		return statssyncher.dataFile;
	}

	static File func_27411_c(StatsSyncher statssyncher) {
		return statssyncher.tempFile;
	}

	static File func_27413_d(StatsSyncher statssyncher) {
		return statssyncher.oldFile;
	}

	static void func_27412_a(StatsSyncher statssyncher, Map map, File file, File file1, File file2)
	throws IOException {
		statssyncher.func_27410_a(map, file, file1, file2);
	}

	static Map func_27421_a(StatsSyncher statssyncher, Map map) {
		return statssyncher.field_27437_b = map;
	}

	static Map func_27409_a(StatsSyncher statssyncher, File file, File file1, File file2) {
		return statssyncher.func_27415_a(file, file1, file2);
	}

	static boolean setBusy(StatsSyncher statssyncher, boolean flag) {
		return statssyncher.isBusy = flag;
	}

	static File getUnsentDataFile(StatsSyncher statssyncher) {
		return statssyncher.unsentDataFile;
	}

	static File getUnsentTempFile(StatsSyncher statssyncher) {
		return statssyncher.unsentTempFile;
	}

	static File getUnsentOldFile(StatsSyncher statssyncher) {
		return statssyncher.unsentOldFile;
	}
}
