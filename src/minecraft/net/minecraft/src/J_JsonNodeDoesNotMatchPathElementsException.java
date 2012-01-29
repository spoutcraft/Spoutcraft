package net.minecraft.src;

public final class J_JsonNodeDoesNotMatchPathElementsException extends J_JsonNodeDoesNotMatchJsonNodeSelectorException {
	private static final J_JsonFormatter JSON_FORMATTER = new J_CompactJsonFormatter();

	static J_JsonNodeDoesNotMatchPathElementsException jsonNodeDoesNotMatchPathElementsException(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, Object aobj[], J_JsonRootNode j_jsonrootnode) {
		return new J_JsonNodeDoesNotMatchPathElementsException(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, aobj, j_jsonrootnode);
	}

	private J_JsonNodeDoesNotMatchPathElementsException(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, Object aobj[], J_JsonRootNode j_jsonrootnode) {
		super(formatMessage(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, aobj, j_jsonrootnode));
	}

	private static String formatMessage(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, Object aobj[], J_JsonRootNode j_jsonrootnode) {
		return (new StringBuilder()).append("Failed to find ").append(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception.failedNode.toString()).append(" at [").append(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.getShortFormFailPath(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception.failPath)).append("] while resolving [").append(commaSeparate(aobj)).append("] in ").append(JSON_FORMATTER.format(j_jsonrootnode)).append(".").toString();
	}

	private static String commaSeparate(Object aobj[]) {
		StringBuilder stringbuilder = new StringBuilder();
		boolean flag = true;
		Object aobj1[] = aobj;
		int i = aobj1.length;
		for (int j = 0; j < i; j++) {
			Object obj = aobj1[j];
			if (!flag) {
				stringbuilder.append(".");
			}
			flag = false;
			if (obj instanceof String) {
				stringbuilder.append("\"").append(obj).append("\"");
			}
			else {
				stringbuilder.append(obj);
			}
		}

		return stringbuilder.toString();
	}
}
