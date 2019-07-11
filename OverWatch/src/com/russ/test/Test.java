/**
 * 
 */
package com.russ.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.jcraft.jsch.JSchException;
import com.russ.ssh.Execute;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.JSONException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.jdom2.Document;

/**
 * @author rmartine
 *
 */
public class Test {

	private static String sourceClass = Test.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);

	private final static String ugeHostsDetailCommand = "qhost -q -xml";
	private final static String ugeJobsDetailCommand = "qstat -j \\* -xml";
	private final static String ugeJobsSummaryCommand = "qstat -u \\* -xml";
	
	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws ConfigurationException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws JDOMException
	 * @throws JSONException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws JSchException 
	 * @throws InterruptedException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws URISyntaxException, JSONException, IOException, 
			ParserConfigurationException, TransformerException, JDOMException, JSchException, InterruptedException, SAXException {
		// TODO Auto-generated method stub
		
		Execute execute = new Execute();
		
		String command = "qstat -j '*' -xml";
		String output = execute.execute(command);
		Document doc = convertStringToXML(output);
		
		System.out.println("Command: " + command);
		System.out.println(doc);
		
		
	}

	public static Document convertStringToXML(String xmlString) throws JDOMException, IOException, ParserConfigurationException, SAXException{

		logger.entering(sourceClass, "convertStringToXML");
		
		SAXBuilder builder = new SAXBuilder();
		
		byte[] byteArray = xmlString.getBytes();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		
		Document doc = builder.build(bais);

		logger.info("Objects " + doc.getContentSize());
		
		logger.exiting(xmlString, "convertStringToXML");
		
		
		return doc;
	}



}
