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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.Level;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;

/**
 * Class to decode and encode passwords.<br>
 * <br>
 * Usage:
 * 
 * <pre>
 * String toEncrypt = "MyPassword";
 * String encrypted = CryptoUtil.encrypt(toEncrypt);
 * System.out.println("Encrypted password: " + encrypted);
 * </pre>
 * 
 * @author LK Test Solutions
 *
 */
public class CryptoUtil {

	public static final String AES = "AES";

	/**
	 * Encrypts the committed string with a generated key with the {@link #AES} algorithm.
	 * 
	 * @param value the input string to encrypt.
	 * @return Null, if the key generation or encryption failed, the encrypted string otherwise.
	 */
	public static String encrypt(String value) {
		return encrypt(value, new File("conf" + File.separator + "temp.key"));
	}

	/**
	 * Encrypts the committed string with the committed key file with the {@link #AES} algorithm. If the
	 * file does not exist, a key gets generated.
	 * 
	 * @param value   the input string to encrypt.
	 * @param keyFile the file that contains the key public that gets used for encryption.
	 * @return Null, if the key generation or encryption failed, the encrypted string otherwise.
	 */
	public static String encrypt(String value, File keyFile) {
		String ret = null;

		if (!keyFile.exists()) {
			KeyGenerator keyGen;
			try {
				keyGen = KeyGenerator.getInstance(AES);
			} catch (NoSuchAlgorithmException e) {
				MLogger.getInstance().log(Level.SEVERE, "KeyGenerator does not support the algorithm: " + AES);
				keyGen = null;
			}
			if (keyGen != null) {
				keyGen.init(128);
				SecretKey sk = keyGen.generateKey();
				FileWriter fw = null;
				try {
					fw = new FileWriter(keyFile);
					fw.write(CryptoUtil.byteArrayToHexString(sk.getEncoded()));
				} catch (IOException e) {
					MLogger.getInstance().log(Level.SEVERE, e);
				} finally {
					try {
						if (fw != null) {
							fw.flush();
							fw.close();
						}
					} catch (IOException e) {
						MLogger.getInstance().log(Level.SEVERE, e);
					}
				}
			}
		}
		if (keyFile.exists()) {
			SecretKeySpec sks = CryptoUtil.getSecretKeySpec(keyFile);
			if (sks != null) {
				Cipher cipher = null;
				try {
					cipher = Cipher.getInstance(AES);
				} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
					MLogger.getInstance().log(Level.SEVERE, e);
				}
				if (cipher != null) {
					try {
						cipher.init(1, (Key) sks, cipher.getParameters());
					} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
						MLogger.getInstance().log(Level.SEVERE, e);
					}
					byte[] encrypted = null;
					try {
						encrypted = cipher.doFinal(value.getBytes());
					} catch (IllegalBlockSizeException | BadPaddingException e) {
						MLogger.getInstance().log(Level.SEVERE, e);
					}
					if (encrypted != null) {
						ret = CryptoUtil.byteArrayToHexString(encrypted);
					}
				}
			}
			try {
				FileUtil.deleteFile(keyFile.getPath());
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
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
	public static String decrypt(String toDecrypt, File keyFile) {
		String ret = null;

		SecretKeySpec sks = CryptoUtil.getSecretKeySpec(keyFile);
		if (sks == null) {
			MLogger.getInstance().log(Level.SEVERE, "Key file not available. No decoding gets performed.");
		} else {
			Cipher cipher = null;
			try {
				cipher = Cipher.getInstance(AES);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
			if (cipher != null) {
				try {
					cipher.init(2, sks);
				} catch (InvalidKeyException e) {
					MLogger.getInstance().log(Level.SEVERE, e);
				}
				if (cipher != null) {
					byte[] decrypted = null;
					try {
						decrypted = cipher.doFinal(CryptoUtil.hexStringToByteArray(toDecrypt));
					} catch (IllegalBlockSizeException | BadPaddingException e) {
						MLogger.getInstance().log(Level.SEVERE, e);
					}
					if (decrypted != null) {
						ret = new String(decrypted);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * @param keyFile The file that contains the public key that gets used for encryption.
	 * @return SecretKeySpec object for further operations or null, if the key file could not be read or
	 *         {@link #AES} is not supported.
	 */
	private static SecretKeySpec getSecretKeySpec(File keyFile) {
		SecretKeySpec sks = null;
		byte[] key = CryptoUtil.readKeyFile(keyFile);
		if (key != null) {
			sks = new SecretKeySpec(key, AES);
		}
		return sks;
	}

	/**
	 * @param keyFile The file that contains the public key that gets used for encryption.
	 * @return The public key as byte array or null, if reading the key failed.
	 */
	private static byte[] readKeyFile(File keyFile) {
		byte[] ret = null;
		String keyValue = null;
		Scanner scanner = null;
		try {
			scanner = new Scanner(keyFile).useDelimiter("\\Z");
			keyValue = scanner.next();
		} catch (FileNotFoundException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		if (keyValue != null) {
			ret = CryptoUtil.hexStringToByteArray(keyValue);
		}
		return ret;
	}

	/**
	 * @param input The string that should be transformed.
	 * @return The committed byte array as string or null if the input is null or too large.
	 */
	private static String byteArrayToHexString(byte[] input) {
		String ret = null;
		if (input != null && input.length < Integer.MAX_VALUE) {
			StringBuffer sb = new StringBuffer(input.length * 2);
			for (int i = 0; i < input.length; ++i) {
				int v = input[i] & 255;
				if (v < 16) {
					sb.append('0');
				}
				sb.append(Integer.toHexString(v));
			}
			ret = sb.toString().toUpperCase();
		}
		return ret;
	}

	/**
	 * @param input The string that should be transformed.
	 * @return The committed string as byte array or null if the transformation could not be completed.
	 */
	private static byte[] hexStringToByteArray(String input) {
		byte[] ret = null;
		if (input != null && StringUtils.isNotBlank(input) && input.length() < Integer.MAX_VALUE) {
			ret = new byte[input.length() / 2];
			for (int i = 0; i < ret.length; ++i) {
				int index = i * 2;
				int v = 0;
				try {
					v = Integer.parseInt(input.substring(index, index + 2), 16);
				} catch (NumberFormatException e) {
					MLogger.getInstance().log(Level.SEVERE, e);
					ret = null;
					break;
				}
				ret[i] = (byte) v;
			}
		}
		return ret;
	}
}
