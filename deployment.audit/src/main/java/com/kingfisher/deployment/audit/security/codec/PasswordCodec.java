package com.kingfisher.deployment.audit.security.codec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

//@Component
public class PasswordCodec {

	//@Value("${ivParam}")
	String ivParam; // "hsifgnik"

	public String encrypt(String keyBytes, String input) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		IvParameterSpec ivSpec = new IvParameterSpec(ivParam.getBytes());
		DESKeySpec dkey = new DESKeySpec(keyBytes.getBytes());
		SecretKeySpec key = new SecretKeySpec(dkey.getKey(), "DES");
		Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		enCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		return Base64.encodeBase64String(enCipher.doFinal(input.getBytes()));
	}

	public String decrypt(String keyBytes, String input) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		IvParameterSpec ivSpec = new IvParameterSpec(ivParam.getBytes());
		DESKeySpec dkey = new DESKeySpec(keyBytes.getBytes());
		SecretKeySpec key = new SecretKeySpec(dkey.getKey(), "DES");
		Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		deCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		return new String(deCipher.doFinal(Base64.decodeBase64(input)));
	}
}
