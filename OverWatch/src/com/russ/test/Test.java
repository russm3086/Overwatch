/**
 * 
 */
package com.russ.test;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.json.JSONException;

import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.factory.Exporter;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

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
	 * @throws ClassNotFoundException 
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
	public static void main(String[] args) throws JSONException, ClassNotFoundException, IOException, URISyntaxException, JDOMException, InterruptedException, TransformerException, ParserConfigurationException {
		// TODO Auto-generated method stub

		SGE_MonitorProp mainProps = new SGE_MonitorProp();

		String propComments = SGE_DataConst.app_name + " v. " + SGE_DataConst.app_version;

		
		Exporter exporter = new Exporter(mainProps, "c:\\temp\\test.xml");

		exporter.exportXMLFile();
		
	}



}
