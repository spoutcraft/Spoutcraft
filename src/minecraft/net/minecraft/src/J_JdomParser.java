package net.minecraft.src;

import java.io.*;

public final class J_JdomParser {
	public J_JdomParser() {
	}

	public J_JsonRootNode parse(Reader reader)
	throws J_InvalidSyntaxException, IOException {
		J_JsonListenerToJdomAdapter j_jsonlistenertojdomadapter = new J_JsonListenerToJdomAdapter();
		(new J_SajParser()).parse(reader, j_jsonlistenertojdomadapter);
		return j_jsonlistenertojdomadapter.getDocument();
	}

	public J_JsonRootNode parse(String s)
	throws J_InvalidSyntaxException {
		J_JsonRootNode j_jsonrootnode;
		try {
			j_jsonrootnode = parse(new StringReader(s));
		}
		catch (IOException ioexception) {
			throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", ioexception);
		}
		return j_jsonrootnode;
	}
}
