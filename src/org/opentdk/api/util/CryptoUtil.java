/* 
 * BSD 2-Clause License
 * 
 * Copyright (c) 2022, LK Test Solutions GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.opentdk.api.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.opentdk.api.logger.MLogger;

/**
 * Class to encode and decode strings.<br>
 * <br>
 * Usage:
 * 
 * <pre>
 * private static final String privateKey = "output/private.key";
 * private static final String publicKey = "output/public.key";
 * 	...
 * String encrypt = "test";
 * CryptoUtil.generateKeyPair(2048, privateKey, publicKey);
 * String secret = CryptoUtil.encrypt(encrypt, new File(publicKey));
 * String decode = CryptoUtil.decrypt(secret, new File(privateKey));
 * BaseRegression.testResult(decode, "Decoded string", "test");
 * </pre>
 * 
 * This generates a key pair in the relative folder output that encrypts strings to 256 byte (2048
 * bits / 8) secret strings. The public key gets used for encryption and the private key for
 * decoding.<br>
 * <br>
 * 
 * @author LK Test Solutions
 *
 */
public class CryptoUtil {
	/**
	 * Used algorithm for the encryption. It is asynchronous which requires a private and a public key
	 * that can be generated with the {@link #generateKeyPair(int, String, String)} method. The public
	 * one gets used for encryption and only the private one can be used to decode.
	 */
	private static final String algorithm = "RSA";

	/**
	 * Generates a private key file and a public key file.
	 * 
	 * @param size               this is the number of bits that the encrypted string will have when
	 *                           using the public key
	 * @param privateKeyFullName path and name for the private key file e.g. ./private.key
	 * @param publicKeyFullName  path and name for the public key file e.g. ./public.key
	 */
	public static void generateKeyPair(int size, String privateKeyFullName, String publicKeyFullName) {
		KeyPairGenerator generator = null;
		try {
			generator = KeyPairGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
			throw new RuntimeException(e);
		}
		generator.initialize(size);
		KeyPair pair = generator.generateKeyPair();

		PrivateKey privateKey = pair.getPrivate();
		PublicKey publicKey = pair.getPublic();

		try {
			FileUtils.writeByteArrayToFile(new File(privateKeyFullName), privateKey.getEncoded());
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		}
		try {
			FileUtils.writeByteArrayToFile(new File(publicKeyFullName), publicKey.getEncoded());
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		}
	}

	/**
	 * Encodes a string with a given public key that can only be decoded with the belonging private key.
	 * 
	 * @param toEncrypt     raw string that should be encrypted with the public key
	 * @param publicKeyFile full name of the public key file
	 * @return the encoded string in Base64 encoding to store it properly (the encryption itself only
	 *         generates a binary format)
	 */
	public static String encrypt(String toEncrypt, File publicKeyFile) {
		String ret = null;
		Key publicKey = CryptoUtil.getKey(publicKeyFile, Cipher.PUBLIC_KEY);
		if (publicKey != null) {
			Cipher encryptCipher = null;
			try {
				encryptCipher = Cipher.getInstance(algorithm);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
			}
			try {
				encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			} catch (InvalidKeyException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
			}
			byte[] secretMessageBytes = toEncrypt.getBytes(StandardCharsets.UTF_8);
			byte[] encryptedMessageBytes = null;
			try {
				encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
			if (encryptedMessageBytes != null) {
				ret = Base64.getEncoder().encodeToString(encryptedMessageBytes);
			}
		}
		return ret;
	}

	/**
	 * Decodes a string that was encoded with the {@link #encrypt(String, File)} method.
	 * 
	 * @param toDecrypt      Base64 encoded string that should be decoded with the private key
	 * @param privateKeyFile full name of the private key file
	 * @return the decoded string in raw format again
	 */
	public static String decrypt(String toDecrypt, File privateKeyFile) {
		String ret = null;
		Key privateKey = CryptoUtil.getKey(privateKeyFile, Cipher.PRIVATE_KEY);
		if (privateKey != null) {
			Cipher decryptCipher = null;
			try {
				decryptCipher = Cipher.getInstance(algorithm);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
			}
			try {
				decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
			} catch (InvalidKeyException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
			}
			byte[] secretMessageBytes = Base64.getDecoder().decode(toDecrypt);
			byte[] decryptedMessageBytes = null;
			try {
				decryptedMessageBytes = decryptCipher.doFinal(secretMessageBytes);
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
			if (decryptedMessageBytes != null) {
				ret = StringUtils.newStringUtf8(decryptedMessageBytes);
			}
		}
		return ret;
	}

	/**
	 * Generates a key object with a key file to use it for encryption in {@link #encrypt(String, File)}
	 * or {@link #decrypt(String, File)}.
	 * 
	 * @param keyFile full name (path + name) of the private or public key
	 * @param keyType 1: Cipher.PUBLIC_KEY, 2: Cipher.PRIVATE_KEY
	 * @return the key object or null if no key file could be detected
	 */
	private static Key getKey(File keyFile, int keyType) {
		Key ret = null;

		byte[] keyBytes = null;
		try {
			keyBytes = Files.readAllBytes(keyFile.toPath());
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		}
		if (keyBytes != null) {
			KeyFactory keyFactory = null;
			try {
				keyFactory = KeyFactory.getInstance(algorithm);
			} catch (NoSuchAlgorithmException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
			}
			EncodedKeySpec keySpec = null;
			try {
				if (keyType == Cipher.PRIVATE_KEY) {
					keySpec = new PKCS8EncodedKeySpec(keyBytes);
					ret = keyFactory.generatePrivate(keySpec);
				} else if (keyType == Cipher.PUBLIC_KEY) {
					keySpec = new X509EncodedKeySpec(keyBytes);
					ret = keyFactory.generatePublic(keySpec);
				}
			} catch (InvalidKeySpecException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
			}
		}
		return ret;
	}

}
