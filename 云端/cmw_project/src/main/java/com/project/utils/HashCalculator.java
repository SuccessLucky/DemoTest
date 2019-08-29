package com.project.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCalculator {
	public static String getStrHash(String str) {
		String ret = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(str.getBytes());
			BigInteger bigInt = new BigInteger(1, digest.digest());
			ret = bigInt.toString(16);
		} catch (NoSuchAlgorithmException e) {
			ret = "unknown hash!";
		}

		return ret;
	}

}
