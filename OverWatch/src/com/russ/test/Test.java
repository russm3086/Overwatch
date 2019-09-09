/**
 * 
 */
package com.russ.test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.russ.util.encryption.AESEncryption;

/**
 * @author rmartine
 *
 */
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws ConfigurationException
	 * @throws IOException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException  {
		// TODO Auto-generated method stub

		
		String secretKey = AESEncryption.getSecretEncryptionKeyStrRev();
		
		System.out.println("Secret key: " + secretKey);
		
		String msg = "Hello World";
		System.out.println("Original: " + msg);
		
		String encryptedText = AESEncryption.encryptTextRev(msg, secretKey);
		System.out.println("Encrypted Text: " + encryptedText);
		
		String decryptedText = AESEncryption.decryptText(encryptedText, secretKey);
		System.out.println("Decrypted Text: " + decryptedText);

		
		
	}

}
