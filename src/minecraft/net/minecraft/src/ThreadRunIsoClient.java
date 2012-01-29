package net.minecraft.src;

class ThreadRunIsoClient extends Thread {
	final CanvasIsomPreview field_1197_a;

	ThreadRunIsoClient(CanvasIsomPreview canvasisompreview) {
		field_1197_a = canvasisompreview;
	}

	public void run() {
		while (CanvasIsomPreview.func_1271_a(field_1197_a)) {
			field_1197_a.func_1265_d();
			try {
				Thread.sleep(1L);
			}
			catch (Exception exception) { }
		}
	}
}
