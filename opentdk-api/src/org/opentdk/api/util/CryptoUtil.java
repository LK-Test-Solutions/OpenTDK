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
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.StringUtils;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;

/**
 * Class to encode and decode strings.<br>
 * <br>
 * Usage:
 * 
 * <pre>
 * </pre>
 * 
 * @author LK Test Solutions
 *
 */
public class CryptoUtil {
	/**
	 * Used algorithm for the encryption. It is asynchronous which requires a private and a public key that
	 * can be generated with the {@link #generateKeyPair(int, String, String)} method. The public one
	 * gets used for encryption and only the private one can be used to decode.
	 */
	private static final String algorithm = "RSA";

	/**
	 * Generates a private key file and a public key file.
	 * 
	 * @param size
	 * @param privateKeyFullName
	 * @param publicKeyFullName
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

		String privateKeyContent = StringUtils.newStringUtf8(privateKey.getEncoded());
		FileUtil.writeOutputFile(privateKeyContent, privateKeyFullName);

		String publicKeyContent = StringUtils.newStringUtf8(publicKey.getEncoded());
		FileUtil.writeOutputFile(publicKeyContent, publicKeyFullName);

//		try (FileOutputStream fos = new FileOutputStream(publicKeyFullName)) {
//		    fos.write(publicKey.getEncoded());
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * Encrypts the committed string with the committed key file with the {@link #AES} algorithm. If the
	 * file does not exist, a key gets generated.
	 * 
	 * @param value   the input string to encrypt.
	 * @param keyFile the file that contains the key public that gets used for encryption.
	 * @return Null, if the key generation or encryption failed, the encrypted string otherwise.
	 */
	public static String encrypt(String value, File publicKeyFile) {
		String ret = null;
		byte[] publicKeyBytes = null;
		try {
			publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		}
		if (publicKeyBytes != null) {
			KeyFactory keyFactory = null;
			try {
				keyFactory = KeyFactory.getInstance(algorithm);
			} catch (NoSuchAlgorithmException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
			}
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			PublicKey publicKey = null;
			try {
				publicKey = keyFactory.generatePublic(publicKeySpec);
			} catch (InvalidKeySpecException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
			}
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
				byte[] secretMessageBytes = value.getBytes(StandardCharsets.UTF_8);
				byte[] encryptedMessageBytes = null;
				try {
					encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					MLogger.getInstance().log(Level.SEVERE, e);
				}
				if (encryptedMessageBytes != null) {
					ret = StringUtils.newStringUtf8(encryptedMessageBytes);
				}
			}
		}
		return ret;
	}

	/**
	 * Decodes the committed string with the committed key file with the {@link #AES} algorithm.
	 * 
	 * @param toDecrypt The string to encode.
	 * @param keyFile   The file that contains the key public that gets used for encryption.
	 * @return Null, if the decoding failed, the decoded string otherwise.
	 */
	public static String decrypt(String toDecrypt, File privateKeyFile) {
		String ret = null;
//		Cipher decryptCipher = Cipher.getInstance("RSA");
//		decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
//		
//	
//		byte[] decryptedMessageBytes = decryptCipher.doFinal(toDecrypt);
//		String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
		
		return ret;
	}

}
