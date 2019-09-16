/**
 * 
 */
package com.russ.test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		
		String msg ="{\r\n" + 
				"   name         dcv_slot_limit\r\n" + 
				"   description  \"limit slots on dcv queue\"\r\n" + 
				"   enabled      TRUE\r\n" + 
				"   limit        users {*} queues {dcv} to slots=1\r\n" + 
				"}\r\n" + 
				"{\r\n" + 
				"   name         vnc_slot_limit\r\n" + 
				"   description  \"limit slots on vnc queue\"\r\n" + 
				"   enabled      TRUE\r\n" + 
				"   limit        users {*} queues {vnc} to slots=1\r\n" + 
				"}\r\n" + 
				"{\r\n" + 
				"   name         dcv2017_slot_limit\r\n" + 
				"   description  \"limit slots on dcv 2017 queue\"\r\n" + 
				"   enabled      TRUE\r\n" + 
				"   limit        users {*} queues {dcv2017} to slots=1\r\n" + 
				"}\r\n" + 
				"{\r\n" + 
				"   name         ottc02lm_slot_limit\r\n" + 
				"   description  \"limit slots on ottc02lm\"\r\n" + 
				"   enabled      TRUE\r\n" + 
				"   limit        users {*} queues {ottc02lm} to slots=128\r\n" + 
				"}\r\n" + 
				"{\r\n" + 
				"   name         euc09_slot_limit\r\n" + 
				"   description  \"limit slots on euc09 queue\"\r\n" + 
				"   enabled      TRUE\r\n" + 
				"   limit        users {*} queues {euc09} to slots=320\r\n" + 
				"}\r\n" + 
				"{\r\n" + 
				"   name         ottdev01_slot_limit\r\n" + 
				"   description  \"limit slots on ottdev01\"\r\n" + 
				"   enabled      TRUE\r\n" + 
				"   limit        users {*} queues {ottdev01} to slots=320\r\n" + 
				"   limit        users {mkainz} queues {ottdev01} to slots=1\r\n" + 
				"}\r\n" + 
				"{\r\n" + 
				"   name         ottc02_slot_limit\r\n" + 
				"   description  \"limit slots on ottc02\"\r\n" + 
				"   enabled      TRUE\r\n" + 
				"   limit        users {*} queues {ottc02} to slots=384\r\n" + 
				"}\r\n" + 
				"{\r\n" + 
				"   name         ottc01_slot_limit\r\n" + 
				"   description  \"limit slots on ottc01 queue\"\r\n" + 
				"   enabled      TRUE\r\n" + 
				"   limit        users {*} queues {ottc01} to slots=392\r\n" + 
				"}\r\n";

		
		String regex ="limit\\s+ users \\{\\*\\} queues \\{(.*)\\} to slots=(.*)";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(msg);
	    
	    if(matcher.find()) {
	    	
	    	
	    	System.out.println("Found");
	    	while(matcher.find()) {
		    	System.out.println(matcher.group(1));
		    	System.out.println(matcher.group(2));
	    	}
	    		
	    	
	    	
	    	
	    }else {
	    	
	    	System.out.println("Not Found");
	    }
	    	

		
		
	}

}
