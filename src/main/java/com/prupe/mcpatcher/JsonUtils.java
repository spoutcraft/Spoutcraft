package com.prupe.mcpatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class JsonUtils {
	public static Gson newGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		return builder.create();
	}

	public static <T extends Object> T parseJson(File path, Class<T> cl) {
		if (path != null && path.isFile() && path.length() > 0L) {
			FileInputStream input = null;
			Object var4;

			try {
				input = new FileInputStream(path);
				Object e = parseJson((InputStream)input, cl);
				// ToDo:  return e; [incompatible types]
				return null;
			} catch (Throwable var8) {
				var8.printStackTrace();
				var4 = null;
			} finally {
				MCPatcherUtils.close((Closeable)input);
			}
//ToDo: 
			return null;  //var4
		} else {
			return null;
		}
	}

	public static <T extends Object> T parseJson(InputStream input, Class<T> cl) {
		if (input == null) {
			return null;
		} else {
			try {
				InputStreamReader e = new InputStreamReader(input);
				return newGson().fromJson(e, cl);
			} catch (Throwable var3) {
				var3.printStackTrace();
				return null;
			}
		}
	}

	public static JsonObject parseJson(File path) {
		FileReader input = null;
		JsonObject var3;

		try {
			input = new FileReader(path);
			JsonParser e = new JsonParser();
			var3 = e.parse(input).getAsJsonObject();
			return var3;
		} catch (Throwable var7) {
			var7.printStackTrace();
			var3 = null;
		} finally {
			MCPatcherUtils.close((Closeable)input);
		}

		return var3;
	}

	public static boolean writeJson(JsonElement json, File path) {
		PrintWriter output = null;
		boolean var4;

		try {
			output = new PrintWriter(path);
			Gson e = newGson();
			e.toJson(json, output);
			output.println();
			var4 = true;
			return var4;
		} catch (Throwable var8) {
			var8.printStackTrace();
			MCPatcherUtils.close((Closeable)output);
			path.delete();
			var4 = false;
		} finally {
			MCPatcherUtils.close((Closeable)output);
		}

		return var4;
	}

	public static boolean writeJson(Object object, File path) {
		PrintWriter output = null;
		boolean var4;

		try {
			output = new PrintWriter(path);
			Gson e = newGson();
			e.toJson(object, object.getClass(), output);
			output.println();
			var4 = true;
			return var4;
		} catch (Throwable var8) {
			var8.printStackTrace();
			MCPatcherUtils.close((Closeable)output);
			path.delete();
			var4 = false;
		} finally {
			MCPatcherUtils.close((Closeable)output);
		}

		return var4;
	}

	public static <T extends JsonElement> T cloneJson(T json) {
		//return (new JsonParser()).parse(json.toString());
		return null;
	}

	public static <T extends Object> T cloneJson(T json, Class<T> jsonClass) {
		Gson gson = newGson();
		return gson.fromJson(gson.toJson(json, jsonClass), jsonClass);
	}
}
