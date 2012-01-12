package com.pclewis.mcpatcher;

import java.io.Closeable;
import java.io.IOException;
import java.util.zip.ZipFile;

public class MCPatcherUtils {
	public static void close(Closeable closeable) {
		if(closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void close(ZipFile zip) {
		if(zip != null) {
			try {
				zip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
