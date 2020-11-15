/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.main.SGE_DataConst;
import com.ansys.cluster.monitor.net.http.HttpConnection;
import com.ansys.cluster.monitor.net.http.HttpResponse;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.UnitConversion;
import com.russ.util.nio.ResourceLoader;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * 
 * @author rmartine
 * @since
 */
public class Connector {
	private String sourceClass = this.getClass().getName();
	private Logger logger = Logger.getLogger(sourceClass);
	private SGE_MonitorProp mainProps;

	/**
	 * 
	 */
	public Connector(SGE_MonitorProp mainProps) {
		// TODO Auto-generated constructor stub

		this.mainProps = mainProps;

	}

	public Payload getPayload(String strUrl) throws IOException, URISyntaxException, JDOMException,
			InterruptedException, TransformerException, ClassNotFoundException {
		logger.entering(sourceClass, "getPayload", strUrl);
		String strConnMethod = mainProps.getClusterConnectionRequestMethod().toUpperCase();
		Payload payLoad = null;

		logger.fine("Getting data from " + strUrl);

		switch (strConnMethod) {

		case SGE_DataConst.connTypeHttp:
			payLoad = connect(strUrl);
			break;

		case SGE_DataConst.connTypeFile:
			payLoad = getFile(strUrl);
			break;

		case SGE_DataConst.connTypeCMD:
			payLoad = executeCmd(strUrl, "xml");
			break;

		default:
			throw new URISyntaxException("Undefined data retrieval method ", strConnMethod);
		}

		logger.exiting(sourceClass, "getPayload");
		return payLoad;
	}

	public Payload executeCmd(String command, String contentType)
			throws IOException, InterruptedException, JDOMException, TransformerException {
		logger.entering(contentType, "executeCmd");

		ArrayList<String> list = new ArrayList<String>();

		checkShellDetail(mainProps.getClusterConnectionShellCmd(mainProps.getClusterIndex()), list);
		checkShellDetail(mainProps.getClusterConnectionShellArgCmd(mainProps.getClusterIndex()), list);

		list.add(command);

		StringBuilder sbCmd = new StringBuilder();

		for (String cmd : list) {

			sbCmd.append(cmd + " ");

		}

		logger.fine("Executing: " + sbCmd.toString());

		return createPayload(executeCmd(list, sbCmd.toString()), contentType);
	}

	private void checkShellDetail(String command, ArrayList<String> list) {

		if (command != null || command != "") {
			list.add(command);
		}
	}

	public String executeCmd(ArrayList<String> list, String command) throws IOException, InterruptedException {
		logger.entering(sourceClass, "executeCmd");

		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectErrorStream(true);
		builder.command(list);
		builder.directory(new File(System.getProperty("user.home")));

		long startTime = System.currentTimeMillis();
		Process process = builder.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		Iterator<String> it = reader.lines().iterator();

		StringBuilder sb = new StringBuilder();

		while (it.hasNext()) {

			String line = it.next();
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("Line: " + line);
			}

			sb.append(line);
		}

		process.waitFor();

		long estimatedTime = System.currentTimeMillis() - startTime;

		logger.info("Data Size: " + UnitConversion.humanReadableByteCount(sb.length(), false) + " Elapse Time: "
				+ estimatedTime + " ms.");

		if (process.exitValue() != 0) {
			throw new IOException("Error executing " + command + " Error: " + sb);
		}

		logger.exiting(sourceClass, "executeCmd");
		return sb.toString();
	}

	public Payload getFile(String filePath)
			throws IOException, URISyntaxException, JDOMException, TransformerException, ClassNotFoundException {
		logger.entering(sourceClass, "getFile", filePath);

		Path path = Paths.get(filePath);

		Payload payload = getFile(path);
		logger.exiting(sourceClass, "getFile");

		return payload;
	}

	public Payload getFile(Path filePath)
			throws IOException, URISyntaxException, JDOMException, TransformerException, ClassNotFoundException {
		logger.entering(sourceClass, "getFile", filePath);
		boolean contentDetected = false;

		logger.finer("Opening file: " + filePath);
		File file = ResourceLoader.readFile(filePath);
		String contentType = new String();
		logger.finer("Reading file " + filePath);
		Payload payload = null;

		if (mainProps.getClusterConnectionRequestContentType().equalsIgnoreCase(SGE_ConnectConst.clusterType)) {

			payload = readObjectFile(file, SGE_ConnectConst.clusterType);

		} else {

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			StringBuilder content = new StringBuilder();

			while (line != null) {
				logger.finest("Line read: " + line);
				if (!contentDetected) {
					contentType = contentType(line);
					if (contentType != SGE_ConnectConst.unknownType) {
						contentDetected = true;
					}
				}
				content.append(line);
				line = reader.readLine();
			}
			reader.close();

			logger.finer("Read file: " + filePath + "\t size: " + content.length());
			payload = createPayload(content.toString(), contentType);
		}

		return payload;
	}

	private Payload readObjectFile(File file, String contentType)
			throws IOException, ClassNotFoundException, JDOMException, TransformerException {

		FileInputStream dis = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(dis);
		return readObjectStream(in, contentType);
	}

	private Payload readObjectStream(ObjectInputStream in, String contentType)
			throws ClassNotFoundException, IOException, JDOMException, TransformerException {

		Object object = in.readObject();
		return createPayload(object, contentType);
	}

	public HttpResponse connectHttp(String url, SGE_MonitorProp mainProps) throws IOException, ClassNotFoundException {
		logger.entering(sourceClass, "connectHttp", url);

		HttpConnection connection = new HttpConnection(mainProps);
		HttpResponse response = connection.httpRequest(url);

		if (response.getResponseCode() == 200) {

			logger.finer("Success full connection to " + url + " http code" + response.getResponseMessage());

		} else {
			throw new java.io.IOException("Error connecting to " + url + "\n" + (response.getFullMessage()));
		}

		logger.exiting(sourceClass, "connectHttp");
		return response;
	}

	public JSONObject getJSON(String jsonString) {

		JSONTokener tokener = new JSONTokener(jsonString);
		JSONObject jsObject = new JSONObject(tokener);
		return jsObject;
	}

	public Document getXmlDocument(String xmlString) throws IOException, JDOMException {
		logger.entering(sourceClass, "getXmlDocument");

		SAXBuilder builder = new SAXBuilder();
		Document jdomDoc = (Document) builder.build(new StringReader(xmlString));

		logger.exiting(sourceClass, "getXmlDocument");
		return jdomDoc;
	}

	public String contentType(String contentType) {
		logger.entering(sourceClass, "contentType", contentType);
		String output = SGE_ConnectConst.unknownType;

		if (contentType.toUpperCase().contains(SGE_ConnectConst.xmlType)) {
			output = SGE_ConnectConst.xmlType;
		}
		if (contentType.toUpperCase().contains(SGE_ConnectConst.jsonType)) {
			output = SGE_ConnectConst.jsonType;
		}

		if (contentType.toUpperCase().contains(SGE_ConnectConst.overwatchType)) {
			output = SGE_ConnectConst.clusterType;
		}

		logger.exiting(sourceClass, "contentType", output);
		return output;
	}

	private Payload createPayload(Object source, String contentType)
			throws IOException, JDOMException, TransformerException {
		logger.entering(sourceClass, "createPayload");
		Payload payload = null;
		logger.finest("Source:\n" + source);

		switch (contentType.toUpperCase()) {

		case SGE_ConnectConst.xmlType:

			Document doc = getXmlDocument((String) source);
			payload = new Payload(doc);
			break;

		case SGE_ConnectConst.jsonType:
			JSONObject jsObject = getJSON((String) source);
			payload = new Payload(jsObject);
			break;

		case SGE_ConnectConst.clusterType:
			payload = new Payload((Cluster) source);
			break;

		}
		logger.exiting(sourceClass, "createPayload");
		return payload;
	}

	private Payload createPayload(HttpResponse response) throws IOException, JDOMException, TransformerException {
		logger.entering(sourceClass, "createPayload");
		String contentType = contentType(response.getContentType());

		logger.exiting(sourceClass, "createPayload");
		return createPayload(response.getOutput(), contentType);
	}

	public Payload connect(String url) throws IOException, JDOMException, TransformerException, ClassNotFoundException {
		Payload payload = null;
		int retries = mainProps.getClusterConnectionRetries();
		for (int i = 1; i < retries + 1; i++) {
			try {

				HttpResponse response = connectHttp(url, mainProps);
				logger.info("Download of " + url + " completed");
				payload = createPayload(response);
				break;
			} catch (IOException e) {
				logger.warning("Failed to connect to " + url + " \n Attempt " + i + " of " + retries);
			}

			long delay = TimeUnit.MILLISECONDS.convert(mainProps.getClusterConnectionRetriesDelay(),
					mainProps.getClusterConnectionRetriesDelayTimeUnitTU());

			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				logger.log(Level.FINER, "Error sleeping for connectiond", e);

				break;
			}
		}

		if (payload == null) {
			throw new java.io.IOException("Error connecting to " + url);
		}
		return payload;
	}

}
