package com.russ.util.encryption;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * This example program shows how AES encryption and decryption can be done in
 * Java. Please note that secret key and encrypted text is unreadable binary and
 * hence in the following program we display it in hexadecimal format of the
 * underlying bytes.
 * 
 * @author Jayson
 */
public class AESEncryption {
	public static final String AES = "AES";

	/**
	 * Creates a decrypted value of the given encrypted {@code String} using reverse
	 * content of the given {@code String} version of {@code SecretKey}.
	 * 
	 * @param encryptedText {@code String}
	 * @param strKey        {@code String}
	 * @return {@code String}
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String decryptTextRev(String encryptedText, String strKey) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		String revKey = revString(strKey);
		String decryptText = decryptText(encryptedText, revKey);
		return decryptText;
	}

	/**
	 * Creates a decrypted value of the given encrypted {@code String} using the
	 * given {@code String} version of {@code SecretKey}.
	 * 
	 * @param encryptedText {@code String}
	 * @param secKey        {@code String}
	 * @return {@code String}
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public static String decryptText(String encryptedText, String strKey) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		SecretKey secKey = stringToSecretKey(strKey);
		byte[] cipherText = hexToBytes(encryptedText);
		String decryptedText = decryptText(cipherText, secKey);

		return decryptedText;
	}

	/**
	 * Creates a decrypted value of the given encrypted {@code String} using the
	 * given {@code SecretKey}
	 * 
	 * @param encryptedText {@code String}
	 * @param secKey        {@code SecretKey}
	 * @return {@code String}
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public static String decryptText(String encryptedText, SecretKey secKey) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		byte[] cipherText = hexToBytes(encryptedText);
		String decryptedText = decryptText(cipherText, secKey);

		return decryptedText;
	}

	/**
	 * Decrypts encrypted byte array using the key used for encryption.
	 * 
	 * @param byteCipherText {@code byte[]}
	 * @param secKey         {@code SecretKey}
	 * @return {@code String}
	 * @throws NoSuchPaddingException    - if transformation contains a padding
	 *                                   scheme that is not available.
	 * @throws NoSuchAlgorithmException  - - if transformation is null, empty, in an
	 *                                   invalid format, or if no Provider supports
	 *                                   a CipherSpi implementation for the
	 *                                   specified algorithm.
	 * @throws InvalidKeyException       - if the given key is inappropriate for
	 *                                   initializing this cipher, or requires
	 *                                   algorithm parameters that cannot be
	 *                                   determined from the given key, or if the
	 *                                   given key has a keysize that exceeds the
	 *                                   maximum allowable keysize (as determined
	 *                                   from the configured jurisdiction policy
	 *                                   files).
	 * @throws BadPaddingException       - if this cipher is in decryption mode, and
	 *                                   (un)padding has been requested, but the
	 *                                   decrypted data is not bounded by the
	 *                                   appropriate padding bytes
	 * @throws IllegalBlockSizeException - if this cipher is a block cipher, no
	 *                                   padding has been requested (only in
	 *                                   encryption mode), and the total input
	 *                                   length of the data processed by this cipher
	 *                                   is not a multiple of block size; or if this
	 *                                   encryption algorithm is unable to process
	 *                                   the input data provided.
	 *
	 */
	public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
		Cipher aesCipher = Cipher.getInstance(AES);
		aesCipher.init(Cipher.DECRYPT_MODE, secKey);
		byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
		return new String(bytePlainText);
	}

	/**
	 * Encrypts the given {@code String} using the given reverse order of the
	 * {@code String} representation of the {@code SecretKey}.
	 * 
	 * @param clearText {@code String}
	 * @param strKey    {@code String}
	 * @return {@code String}
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptTextRev(String clearText, String strKey)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {

		String revKey = revString(strKey);
		String encryptText = encryptText(clearText, revKey);
		return encryptText;

	}

	/**
	 * Encrypts the given {@code String} using the given {@code String}
	 * representation of the {@code SecretKey}.
	 * 
	 * @param clearText {@code String}
	 * @param strKey    {@code String}
	 * @return {@code String}
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptText(String clearText, String strKey)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {

		SecretKey secKey = stringToSecretKey(strKey);
		byte[] cipherText = encryptText(clearText, secKey);
		String encryptedText = bytesToHex(cipherText);

		return encryptedText;
	}

	/**
	 * Encrypts plainText in AES using the secret key
	 * 
	 * @param plainText {@code String}
	 * @param secKey    {@code SecretKey}
	 * @return {@code byte[]}
	 * @throws NoSuchPaddingException       - if transformation contains a padding
	 *                                      scheme that is not available.
	 * @throws NoSuchAlgorithmException     - - if transformation is null, empty, in
	 *                                      an invalid format, or if no Provider
	 *                                      supports a CipherSpi implementation for
	 *                                      the specified algorithm.
	 * @throws InvalidKeyException          - if the given key is inappropriate for
	 *                                      initializing this cipher, or requires
	 *                                      algorithm parameters that cannot be
	 *                                      determined from the given key, or if the
	 *                                      given key has a keysize that exceeds the
	 *                                      maximum allowable keysize (as determined
	 *                                      from the configured jurisdiction policy
	 *                                      files).
	 * @throws BadPaddingException          - if this cipher is in decryption mode,
	 *                                      and (un)padding has been requested, but
	 *                                      the decrypted data is not bounded by the
	 *                                      appropriate padding bytes
	 * @throws IllegalBlockSizeException    - if this cipher is a block cipher, no
	 *                                      padding has been requested (only in
	 *                                      encryption mode), and the total input
	 *                                      length of the data processed by this
	 *                                      cipher is not a multiple of block size;
	 *                                      or if this encryption algorithm is
	 *                                      unable to process the input data
	 *                                      provided.
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] encryptText(String plainText, SecretKey secKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
		Cipher aesCipher = Cipher.getInstance(AES);
		aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
		byte[] bytePlainText = plainText.getBytes("UTF-8");
		byte[] byteCipherText = aesCipher.doFinal(bytePlainText);
		return byteCipherText;
	}

	/**
	 * Converts the given {@code SecretKey} to a readable hex form in a
	 * {@code String}
	 * 
	 * @param secKey {@code SecretKey}
	 * @return {@code String}
	 */
	public static String secretKeyToString(SecretKey secKey) {

		byte[] key = secKey.getEncoded();
		String aesKey = bytesToHex(key);
		return aesKey;

	}

	/**
	 * Converts the string representation of the AES secret key to a
	 * {@code SecretKey} object.
	 * 
	 * @param strKey {@code String}
	 * @return {@code SecretKey}
	 */
	public static SecretKey stringToSecretKey(String strKey) {

		byte[] aesKey = hexToBytes(strKey);
		SecretKey secKey = new SecretKeySpec(aesKey, AES);
		return secKey;

	}

	/**
	 * Returns the {@code String} representation of the {@code SecretKey} in reverse
	 * sequence.
	 * 
	 * @return {@code String}
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSecretEncryptionKeyStrRev() throws NoSuchAlgorithmException {
		String key = getSecretEncryptionKeyString();
		String revKey = revString(key);
		return revKey;

	}

	/**
	 * Returns the {@code String} representation of the {@code SecretKey}
	 * 
	 * @return {@code String}
	 * @throws NoSuchAlgorithmException if no Provider supports a KeyGeneratorSpi
	 *                                  implementation for the specified algorithm.
	 * 
	 */
	public static String getSecretEncryptionKeyString() throws NoSuchAlgorithmException {

		SecretKey secKey = getSecretEncryptionKey();
		String key = secretKeyToString(secKey);
		return key;

	}

	/**
	 * Returns the AES encryption key. In your actual programs, this should be
	 * safely stored.
	 * 
	 * @return {@code SecretKey}
	 * @throws NoSuchAlgorithmException if no Provider supports a KeyGeneratorSpi
	 *                                  implementation for the specified algorithm.
	 * 
	 */
	public static SecretKey getSecretEncryptionKey() throws NoSuchAlgorithmException {
		KeyGenerator generator = KeyGenerator.getInstance(AES);
		generator.init(128); // The AES key size in number of bits
		SecretKey secKey = generator.generateKey();
		return secKey;
	}

	/**
	 * Causes this character sequence to be replaced by the reverse of the sequence.
	 * If there are any surrogate pairs included in the sequence, these are treated
	 * as single characters for the reverse operation.
	 * 
	 * @param string {@code String}
	 * @return {@code String}
	 */
	static String revString(String string) {

		StringBuilder sb = new StringBuilder(string);
		String revString = sb.reverse().toString();
		return revString;

	}

	/**
	 * Convert a binary byte array into readable hex form
	 * 
	 * @param hash
	 * @return
	 */
	private static String bytesToHex(byte[] hash) {
		StringBuilder sb = new StringBuilder();
		for (byte b : hash) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param hash
	 * @return
	 */
	private static byte[] hexToBytes(String hexString) {
		byte[] byteArray = new BigInteger(hexString, 16).toByteArray();
		if (byteArray[0] == 0) {
			byte[] output = new byte[byteArray.length - 1];
			System.arraycopy(byteArray, 1, output, 0, output.length);
			return output;
		}
		return byteArray;
	}
}