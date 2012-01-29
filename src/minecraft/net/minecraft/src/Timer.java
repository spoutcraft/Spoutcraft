package net.minecraft.src;

public class Timer {
	float ticksPerSecond;
	private double lastHRTime;
	public int elapsedTicks;
	public float renderPartialTicks;
	public float timerSpeed;
	public float elapsedPartialTicks;
	private long lastSyncSysClock;
	private long lastSyncHRClock;
	private long field_28132_i;
	private double timeSyncAdjustment;

	public Timer(float f) {
		timerSpeed = 1.0F;
		elapsedPartialTicks = 0.0F;
		timeSyncAdjustment = 1.0D;
		ticksPerSecond = f;
		lastSyncSysClock = System.currentTimeMillis();
		lastSyncHRClock = System.nanoTime() / 0xf4240L;
	}

	public void updateTimer() {
		long l = System.currentTimeMillis();
		long l1 = l - lastSyncSysClock;
		long l2 = System.nanoTime() / 0xf4240L;
		double d = (double)l2 / 1000D;
		if (l1 > 1000L) {
			lastHRTime = d;
		}
		else if (l1 < 0L) {
			lastHRTime = d;
		}
		else {
			field_28132_i += l1;
			if (field_28132_i > 1000L) {
				long l3 = l2 - lastSyncHRClock;
				double d2 = (double)field_28132_i / (double)l3;
				timeSyncAdjustment += (d2 - timeSyncAdjustment) * 0.20000000298023224D;
				lastSyncHRClock = l2;
				field_28132_i = 0L;
			}
			if (field_28132_i < 0L) {
				lastSyncHRClock = l2;
			}
		}
		lastSyncSysClock = l;
		double d1 = (d - lastHRTime) * timeSyncAdjustment;
		lastHRTime = d;
		if (d1 < 0.0D) {
			d1 = 0.0D;
		}
		if (d1 > 1.0D) {
			d1 = 1.0D;
		}
		elapsedPartialTicks += d1 * (double)timerSpeed * (double)ticksPerSecond;
		elapsedTicks = (int)elapsedPartialTicks;
		elapsedPartialTicks -= elapsedTicks;
		if (elapsedTicks > 10) {
			elapsedTicks = 10;
		}
		renderPartialTicks = elapsedPartialTicks;
	}
}
