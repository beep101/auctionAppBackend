package com.example.demo.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.example.demo.exceptions.BadInitializatinException;
import com.example.demo.exceptions.InvalidDataException;

import java.nio.ByteBuffer;

@Component
public class DefaultPushMessageEncryptionUtil implements PushMessageEncryptionUtil {
	private static final int PADDING_SIZE_0=0;
	private static final byte[] P256_HEAD= Base64.getDecoder().decode("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgA");
	
	private final SecureRandom SECURE_RANDOM = new SecureRandom();
	
	private KeyPairGenerator keyPairGenerator;
	private KeyFactory keyFactory;
	
	public DefaultPushMessageEncryptionUtil() throws BadInitializatinException {
		try {
			this.keyPairGenerator= KeyPairGenerator.getInstance("EC");
		    this.keyPairGenerator.initialize(new ECGenParameterSpec("secp256r1"));
		    this.keyFactory=KeyFactory.getInstance("EC");
		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
			throw new BadInitializatinException();
		}
	}

	@Override
	public byte[] encrypt(String plainText, String authSecret, String publicKey) throws InvalidDataException {

	    java.security.KeyPair asKeyPair = this.keyPairGenerator.genKeyPair();
	    ECPublicKey asPublicKey = (ECPublicKey) asKeyPair.getPublic();
	    byte[] uncompressedASPublicKey = uncompressECPublicKey(asPublicKey);

	    ECPublicKey uaPublicKey=fromUncompressedECPublicKey(publicKey);

	    KeyAgreement keyAgreement;
		try {
			keyAgreement = KeyAgreement.getInstance("ECDH");
		    keyAgreement.init(asKeyPair.getPrivate());
		    keyAgreement.doPhase(uaPublicKey, true);
		} catch (NoSuchAlgorithmException | InvalidKeyException e1) {
			throw new InvalidDataException();
		}

	    byte[] ecdhSecret = keyAgreement.generateSecret();

	    byte[] salt = new byte[16];
	    this.SECURE_RANDOM.nextBytes(salt);

	    Mac hmacSHA256;
		try {
			hmacSHA256 = Mac.getInstance("HmacSHA256");
		    hmacSHA256.init(new SecretKeySpec(Base64.getUrlDecoder().decode(authSecret), "HmacSHA256"));
		} catch (NoSuchAlgorithmException | InvalidKeyException e1) {
			throw new InvalidDataException();
		}
	    byte[] prkKey = hmacSHA256.doFinal(ecdhSecret);

	    byte[] keyInfo = concat("WebPush: info\0".getBytes(StandardCharsets.UTF_8),uncompressECPublicKey(uaPublicKey), uncompressedASPublicKey);
	    
	    try {
			hmacSHA256.init(new SecretKeySpec(prkKey, "HmacSHA256"));
		} catch (InvalidKeyException e1) {
			throw new InvalidDataException();
		}
	    hmacSHA256.update(keyInfo);
	    hmacSHA256.update((byte) 1);
	    byte[] ikm = hmacSHA256.doFinal();

	    try {
			hmacSHA256.init(new SecretKeySpec(salt, "HmacSHA256"));
		} catch (InvalidKeyException e1) {
			throw new InvalidDataException();
		}
	    byte[] prk = hmacSHA256.doFinal(ikm);

	    byte[] cekInfo = "Content-Encoding: aes128gcm\0".getBytes(StandardCharsets.UTF_8);

	    try {
			hmacSHA256.init(new SecretKeySpec(prk, "HmacSHA256"));
		} catch (InvalidKeyException e1) {
			throw new InvalidDataException();
		}
	    hmacSHA256.update(cekInfo);
	    hmacSHA256.update((byte) 1);
	    byte[] cek = hmacSHA256.doFinal();
	    cek = Arrays.copyOfRange(cek, 0, 16);

	    byte[] nonceInfo = "Content-Encoding: nonce\0".getBytes(StandardCharsets.UTF_8);

	    try {
			hmacSHA256.init(new SecretKeySpec(prk, "HmacSHA256"));
		} catch (InvalidKeyException e1) {
			throw new InvalidDataException();
		}
	    hmacSHA256.update(nonceInfo);
	    hmacSHA256.update((byte) 1);
	    byte[] nonce = hmacSHA256.doFinal();
	    nonce = Arrays.copyOfRange(nonce, 0, 12);

	    Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/GCM/NoPadding");
		    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(cek, "AES"),new GCMParameterSpec(128, nonce));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e1) {
			throw new InvalidDataException();
		}

	    List<byte[]> inputs = new ArrayList<>();
	    byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
	    inputs.add(plainTextBytes);
	    inputs.add(new byte[] { 2 }); // padding delimiter

	    int padSize = Math.max(0, PADDING_SIZE_0 - plainTextBytes.length);
	    if (padSize > 0) {
	      inputs.add(new byte[padSize]);
	    }

	    byte[] encrypted;
		try {
			encrypted = cipher.doFinal(concat(inputs.toArray(new byte[0][])));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new InvalidDataException();
		}

	    ByteBuffer encryptedArrayLength = ByteBuffer.allocate(4);
	    encryptedArrayLength.putInt(encrypted.length);

	    byte[] header = concat(salt, encryptedArrayLength.array(),
	        new byte[] { (byte) uncompressedASPublicKey.length }, uncompressedASPublicKey);
	    
	    return concat(header, encrypted);
	}
	
	
	private byte[] uncompressECPublicKey(ECPublicKey publicKey) {
	    byte[] result = new byte[65];
	    byte[] encoded = publicKey.getEncoded();
	    System.arraycopy(encoded, P256_HEAD.length, result, 0,
	        encoded.length - P256_HEAD.length);
	    return result;
	}
	
	public ECPublicKey fromUncompressedECPublicKey(String encodedPublicKey) throws InvalidDataException{
		System.out.println(encodedPublicKey);
		byte[] w = Base64.getUrlDecoder().decode(encodedPublicKey);
		byte[] encodedKey = new byte[P256_HEAD.length + w.length];
		System.arraycopy(P256_HEAD, 0, encodedKey, 0, P256_HEAD.length);
		System.arraycopy(w, 0, encodedKey, P256_HEAD.length, w.length);

		X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
		try {
			return (ECPublicKey) this.keyFactory.generatePublic(ecpks);
		} catch (InvalidKeySpecException e) {
			throw new InvalidDataException();
		}
	}
	
	byte[] concat(byte[]... arrays) {
		int totalLength = 0;
		for (byte[] array : arrays) {
			totalLength += array.length;
		}
		
		byte[] result = new byte[totalLength];

		int currentIndex = 0;
		for (byte[] array : arrays) {
			System.arraycopy(array, 0, result, currentIndex, array.length);
			currentIndex += array.length;
		}
		
		return result;
	}
}
