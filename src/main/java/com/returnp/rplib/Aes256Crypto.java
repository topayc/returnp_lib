package com.returnp.rplib;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Hex;


public class Aes256Crypto {

	private static String SECRET_KEY	= "ZFYzH5HOffrXc6MV";
	private final static String IV		= "2H4+HRD0Z6g1qmRw";
	
	public static String encode(String planContent, String inputSecretKey) {
		byte[] keyData = inputSecretKey.getBytes();
		return encodeProcess(planContent, keyData);
		
	}
	
	
	public static String encode(String planContent) {
		byte[] keyData = SECRET_KEY.getBytes();
		return encodeProcess(planContent, keyData);
	}
	 
	public static String encodeProcess(String planContent, byte[] keyData) {
		SecretKey secretKey = new SecretKeySpec(keyData, "AES");
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV.getBytes()));
	
			byte[] encrypted = c.doFinal(planContent.getBytes("UTF-8"));
			//String encContent = new String(Base64.encodeBase64(encrypted));
			String encContent = new String(Hex.encode(encrypted));
			return encContent;
		} catch (Exception ex) {
			return "";
		}
	}
	public static String decode(String encContent, String inputSecretKey) {
		byte[] keyData = inputSecretKey.getBytes();
		return decodeProcess(encContent, keyData);
	}
	
	public static String decode(String encContent) {
		byte[] keyData = SECRET_KEY.getBytes();
		return decodeProcess(encContent, keyData);
	}

	public static String decodeProcess(String encContent, byte[] keyData) {
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
	
			//byte[] byteStr = Base64.decodeBase64(encContent.getBytes());
			byte[] byteStr = Hex.decode(encContent);
			String decContent = new String(c.doFinal(byteStr),"UTF-8");
			return decContent;
		} catch (Exception ex) {
			return "";
		}
		 
		
	}
	
	public static String aes_encrypt(String planContent, String strKey) {
		byte[] keyData = null;
		try {
			keyData = Arrays.copyOf(strKey.getBytes("ASCII"), 16);
		} catch (Exception ex) {
			return "";
		}
		return aes_encrypt_process(planContent, keyData);	    
	}
	
	public static String aes_encrypt(String planContent) {
		byte[] keyData = null;
		try {
			keyData = Arrays.copyOf(SECRET_KEY.getBytes(), 16);
		} catch (Exception ex) {
			return "";
		}
		return aes_encrypt_process(planContent, keyData);
	}
	
	public static String aes_encrypt_process(String planContent, byte[] keyData) {
	    try {
	        SecretKey key = new SecretKeySpec(keyData, "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, key);

	        byte[] cleartext = planContent.getBytes("UTF-8");
	        byte[] ciphertextBytes = cipher.doFinal(cleartext);

	        // return new String(Hex.encodeHex(ciphertextBytes, false));
	        return new String(Hex.encode(ciphertextBytes));

	    } catch (Exception ex) {
	    	//System.out.println("AesCrypto.aes_encrypt_process Error:" + ex.toString());
	    	return "";
	    }
	}
	
	public static String aes_decrypt(String planContent, String strKey) {
		byte[] keyData = null;
		try {
			keyData = Arrays.copyOf(strKey.getBytes("ASCII"), 16);
		} catch (Exception ex) {
			return "";
		}
		return aes_decrypt_process(planContent, keyData);	    
	}
	
	public static String aes_decrypt(String encContent) {
		byte[] keyData = null;
		try {
			keyData = Arrays.copyOf(SECRET_KEY.getBytes(), 16);
		} catch (Exception ex) {
			//System.out.println("AesCrypto.aes_decrypt Error:" + ex.toString());
			return "";
		}
		return aes_decrypt_process(encContent, keyData);
	}
	
	public static String aes_decrypt_process(String encContent, byte[] keyData) {
	    try {
	        SecretKey key = new SecretKeySpec(keyData, "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.DECRYPT_MODE, key);

	        // byte[] cleartext = Hex.decodeHex(encContent.toCharArray());
	        byte[] cleartext = Hex.decode(encContent);
	        byte[] ciphertextBytes = cipher.doFinal(cleartext);
	        return new String(ciphertextBytes, "UTF-8");

	    } catch (Exception ex) {
	    	//System.out.println("AesCrypto.aes_decrypt_process Error:" + ex.toString());
	    	return "";
	    }
	}
}
