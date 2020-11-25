/**
 * 
 */
package com.ansys.cluster.monitor.gui;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.ansys.cluster.monitor.main.SGE_DataConst;
import com.russ.util.encryption.AESEncryption;

/**
 * @author rmartine
 *
 */
public class AdminMenuConfig {

	/**
	 * 
	 */
	private AdminMenuConfig() {
		// TODO Auto-generated constructor stub
	}

	public static String createKeyPassword(String userName) throws NoSuchAlgorithmException, InvalidKeyException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		String key = AESEncryption.getSecretEncryptionKeyStrRev();

		StringBuffer sb = new StringBuffer("key: ");
		sb.append(key);
		sb.append("\t");
		sb.append("Password: ");
		sb.append(AESEncryption.encryptTextRev(userName + SGE_DataConst.adminSuffx, key));

		return sb.toString();
	}

	public static boolean isAdmin(String key, String password, String userName) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		if (key != null && password != null && userName != null && !key.isEmpty() && !password.isEmpty()
				&& !userName.isEmpty()) {

			String unencrypted = AESEncryption.decryptTextRev(password, key);

			if (unencrypted.equalsIgnoreCase(userName + SGE_DataConst.adminSuffx))
				return true;
		}
		
		return false;
	}

}
