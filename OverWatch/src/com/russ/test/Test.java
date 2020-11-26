/**
 * 
 */
package com.russ.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.main.Main;
import com.ansys.cluster.monitor.settings.PropUtil;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.settings.LoggingUtil;

/**
 * @author rmartine
 *
 */
public class Test {
	private static String sourceClass = Test.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IllegalAccessException
	 */
	public static void main(String[] args) throws IllegalAccessException {
		// TODO Auto-generated method stub
		LoggingUtil.setLevel(Level.FINER);

		SGE_MonitorProp newProps = new SGE_MonitorProp();
		SGE_MonitorProp oldProps = new SGE_MonitorProp();

		List<String> listRegex = newProps.getDataRetentionRegexLst();
		
		PropUtil pUtil= new PropUtil();
		
		SGE_MonitorProp props = pUtil.mergeProps(newProps, oldProps, listRegex);
		
		System.out.println(props.getUsernameOverride());
		System.out.println(props.getGuiFontScaling());
		System.out.println(props.getAdminKey());
		System.out.println(props.getHeader());
		

	}


}
