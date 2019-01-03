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

public class PasswordCodec {

	private Cipher deCipher;
	private Cipher enCipher;
	private SecretKeySpec key;
	private IvParameterSpec ivSpec;

	public PasswordCodec(String keyBytes) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		ivSpec = new IvParameterSpec("hsifgnik".getBytes());
		DESKeySpec dkey = new DESKeySpec(keyBytes.getBytes());
		key = new SecretKeySpec(dkey.getKey(), "DES");
		deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	}

	public String encrypt(String input) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		enCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		return Base64.encodeBase64String(enCipher.doFinal(input.getBytes()));
	}

	public String decrypt(String input) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		deCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		return new String(deCipher.doFinal(Base64.decodeBase64(input)));
	}
}
