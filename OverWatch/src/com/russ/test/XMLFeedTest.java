/**
 * 
 */
package com.russ.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ansys.cluster.monitor.data.Host;
import com.ansys.cluster.monitor.data.JobMessage;
import com.ansys.cluster.monitor.data.NodeProp;
import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.gui.Console;
import com.ansys.cluster.monitor.main.Main;
import com.ansys.cluster.monitor.net.Connector;
import com.ansys.cluster.monitor.net.DataCollector;
import com.ansys.cluster.monitor.net.Payload;
import com.ansys.cluster.monitor.net.http.HttpConnection;
import com.ansys.cluster.monitor.net.http.HttpResponse;
import com.ansys.cluster.monitor.settings.ArgsSettings;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.FileStructure;
import com.russ.util.settings.SystemSettings;

/**
 * @author rmartine
 *
 */
public class XMLFeedTest {
	static String sourceClass = XMLFeedTest.class.getName();
	static Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	public XMLFeedTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		Logger logger = Logger.getLogger(Main.class.getName());
		logger.setLevel(Level.FINEST);

		try {

			SystemSettings systemSettings = new SystemSettings("etc/settings.properties");
			systemSettings.loadManager("etc/settings.properties");
			SGE_MonitorProp mainProps = new SGE_MonitorProp();
			mainProps.putAll(systemSettings.getMainProps());

			HttpConnection connection = new HttpConnection(mainProps);

			HttpResponse response = connection.httpRequest(mainProps.getClusterConnectionDetailedJobsUrl(0));

			// HttpResponse response =
			// connection.httpRequest(mainProps.getClusterConnectionHostUrl(0));

			if (response.getResponseCode() == 200) {

//				System.out.println(response.getContentType());

				if (response.getContentType().toUpperCase().contains("XML")) {

					SAXBuilder builder = new SAXBuilder();
					Document jdomDoc = (Document) builder.build(new StringReader(response.getOutput()));

					Payload payload = new Payload(jdomDoc);

					HashMap<Integer, NodeProp> map = createDetailedJobsProp(payload.getDocObject(), mainProps);

					// Element djobInfoElm = root.getChild("job_info");

					// Element djobInfoElm = root.getChild("djob_info");

					// Element messageElm = root.getChild(SGE_DataConst.json_message);

					map.size();
					
				}

			} else {

				throw new IOException(response.getFullMessage());

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Severe Error", e);
		}

	}

	private static HashMap<Integer, NodeProp> createDetailedJobsProp(Document document, SGE_MonitorProp mainProp) {

		Element root = document.getRootElement();
		Element djobInfoElm = root.getChild(SGE_DataConst.djob_info);
		
		logger.finer("Create detailed Job Properties Object");
		HashMap<Integer, NodeProp> hashMapProp = createDetailedJobsProp(djobInfoElm, mainProp);

		logger.finer("Retrieving " + SGE_DataConst.msg);
		Element messageElm = root.getChild(SGE_DataConst.msg);

		logger.finer("Create detailed Job Message Properties Object");
		ArrayList<JobMessage> listJobMsg= createJobMessages(messageElm);

		logger.finer("Addind Job messages to detailed Job");
		
		return hashMapProp;
	}

	private static ArrayList<JobMessage> createJobMessages(Element messagesElm) {
		logger.entering(sourceClass, "createJobMessages", messagesElm);
		
		ArrayList<JobMessage> listJobMsg = new ArrayList<JobMessage>();
		Element element = messagesElm.getChild(SGE_DataConst.xml_Element);
		List<Element> listElem = element.getChildren();

		for (Element elem : listElem) {

			JobMessage jobMsg = createJobMessage(elem);
			listJobMsg.add(jobMsg);
		}

		logger.exiting(sourceClass, "createJobMessages", listJobMsg);
		return listJobMsg;
	}

	private static JobMessage createJobMessage(Element messageElm) {
		logger.entering(sourceClass, "createJobMessage", messageElm);
		
		Element element = messageElm.getChild(SGE_DataConst.xml_Element);
		NodeProp prop = new NodeProp();
		JobMessage jobMsg = null;
		
		String value = element.getChild(SGE_DataConst.msg_msg_number).getValue();
		prop.putLog(SGE_DataConst.msg_msg_number, value);

		value = element.getChild(SGE_DataConst.msg_msg).getValue();
		prop.putLog(SGE_DataConst.msg_msg, value);
		
		Element jobList = element.getChild(SGE_DataConst.msg_job_list);
		Element jlSub = jobList.getChild(SGE_DataConst.xml_Element);
		
		List<Element> jobIdList = jlSub.getChildren();
		LinkedHashSet<Integer> list = new LinkedHashSet<Integer>();		
		
		for (Element jobId : jobIdList) {
			list.add(Integer.parseInt(jobId.getValue()));
		}
		
		prop.putLog(SGE_DataConst.msg_job_list, list);
		
		jobMsg = new JobMessage(prop);
		
		logger.exiting(sourceClass, "createJobMessage", jobMsg);
		return jobMsg;
	}

	private static HashMap<Integer, NodeProp> createDetailedJobsProp(Element djobInfoElm, SGE_MonitorProp mainProp) {
		logger.entering(sourceClass, "createDetailedJobProps");
		HashMap<Integer, NodeProp> hashMapProp = new HashMap<Integer, NodeProp>();

		logger.finer("Parsing " + djobInfoElm);
		List<Element> listElm = djobInfoElm.getChildren();

		for (Element elem : listElm) {

			NodeProp prop = createJobProp(elem);

			prop.setJobIdleThreshold(mainProp.getJobIdleThreshold());

			hashMapProp.put(prop.getJobNumber(), prop);
		}

		logger.exiting(sourceClass, "createDetailedJobsProp");
		return hashMapProp;
	}

	private static NodeProp createJobProp(Element element) {
		logger.entering(sourceClass, "createDetailedJobProp", element);
		NodeProp prop = new NodeProp();
		Object value = null;
		String key = null;
		logger.finest("Processing element: " + element);

		List<Element> listElm = element.getChildren();

		for (Element elem : listElm) {
			key = elem.getName();
			logger.finest("Retrieving element " + key);

			if (elem.getName().toLowerCase().contains("list")) {
				if (elem.getName().equalsIgnoreCase("JB_env_list")) {
					value = processEnvList(elem);
				} else {
					Element child = elem.getChild("element");
					value = createJobProp(child);
				}

			} else {
				value = elem.getValue();
			}

			prop.putLog(key, value);
			logger.finest("Saving property Key: " + (elem.getName() + " Value: " + elem.getValue()));
		}

		logger.exiting(sourceClass, "createDetailedJobProps");
		return prop;
	}

	private static ArrayList<NodeProp> processEnvList(Element element) {
		logger.entering(sourceClass, "processEnvList", element);
		ArrayList<NodeProp> listProp = new ArrayList<NodeProp>();

		List<Element> listElm = element.getChildren();

		for (Element elem : listElm) {
			NodeProp nodeProp = new NodeProp();
			List<Element> listSubElem = elem.getChildren();

			for (Element subElm : listSubElem) {
				nodeProp.putLog(subElm.getName(), subElm.getValue());
				logger.finest("Saving property Key: " + (subElm.getName() + " Value: " + subElm.getValue()));
			}
			listProp.add(nodeProp);
		}

		logger.exiting(sourceClass, "processEnvList", listProp);
		return listProp;
	}

}
