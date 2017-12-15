package com.hualing.qrcodetracker.aframework.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.Security;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import it.sauronsoftware.base64.Base64;

public class Cipher {

	public static String BASE64Encrypt(byte[] buffer) {
		return BASE64.encrypt(buffer);
	}

	public static byte[] BASE64Decrypt(String str) {
		return BASE64.decrypt(str);
	}

	public static String AESEncrypt(String str) {
		return AES.encrypt(str);
	}

	public static String AESDecrypt(String str) {
		return AES.decrypt(str);
	}

	public static String MD5(String s) {
		return MD5.encrypt(s);
	}

	public static String MD5(byte[] buffer) {
		return MD5.encrypt(buffer);
	}
	public static void setAESKey(String key){
		if(key==null||key.length()!=16){
			throw new RuntimeException("密钥格式错误");
		}
		AES.sKey=key;
	}
	static class MD5 {
		public static String encrypt(String str) {
			try {
				return encrypt(str.getBytes("UTF-8"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public static String encrypt(byte[] buffer) {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
			try {
				byte[] btInput = buffer;
				// 获得MD5摘要算法的 MessageDigest 对象
				MessageDigest mdInst = MessageDigest.getInstance("MD5");
				// 使用指定的字节更新摘要
				mdInst.update(btInput);
				// 获得密文
				byte[] md = mdInst.digest();
				// 把密文转换成十六进制的字符串形式
				int j = md.length;
				char str[] = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					str[k++] = hexDigits[byte0 >>> 4 & 0xf];
					str[k++] = hexDigits[byte0 & 0xf];
				}
				return new String(str);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class AES {
		private static String sKey = "wWw.CloudCns.cOm";
		private static final byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		public static String decrypt(String str) {
			try {
				Security.addProvider(new BouncyCastleProvider());
				SecretKeySpec sKeySpec = new SecretKeySpec(sKey.getBytes("utf-8"), "AES");
				javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
				cipher.init(javax.crypto.Cipher.DECRYPT_MODE, sKeySpec, new IvParameterSpec(iv));
				byte[] buffer = BASE64Decrypt(str);
				byte[] dec = cipher.doFinal(buffer);
				str = new String(dec, "utf-8");
				return str;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public static String encrypt(String str) {
			try {
				Security.addProvider(new BouncyCastleProvider());
				SecretKeySpec sKeySpec = new SecretKeySpec(sKey.getBytes("utf-8"), "AES");
				javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
				cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, sKeySpec, new IvParameterSpec(iv));

				byte[] buffer = str.getBytes("utf-8");
				byte[] enc = cipher.doFinal(buffer);
				str = BASE64Encrypt(enc);
				return str;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class BASE64 {
		private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

		public static String encrypt(byte[] buffer) {
			try {
				return new String(Base64.encode(buffer), "utf-8");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public static byte[] decrypt(String str) {
			try {
				return Base64.decode(str.getBytes("utf-8"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
