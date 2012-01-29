package net.minecraft.src;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5String {
	private String field_27370_a;

	public MD5String(String s) {
		field_27370_a = s;
	}

	public String func_27369_a(String s) {
		try {
			String s1 = (new StringBuilder()).append(field_27370_a).append(s).toString();
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(s1.getBytes(), 0, s1.length());
			return (new BigInteger(1, messagedigest.digest())).toString(16);
		}
		catch (NoSuchAlgorithmException nosuchalgorithmexception) {
			throw new RuntimeException(nosuchalgorithmexception);
		}
	}
}
