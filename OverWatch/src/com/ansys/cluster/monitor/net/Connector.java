/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.ansys.cluster.monitor.net.http.HttpConnection;
import com.ansys.cluster.monitor.net.http.HttpResponse;
import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
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

	/**
	 * 
	 */
	public Connector() {
		// TODO Auto-generated constructor stub
	}


		
	public Payload executeCmd(String command, String contentType)
			throws IOException, InterruptedException, JDOMException {
			
			String[] arrCommand = command.split(" ");
			return createPayload(executeCmd(arrCommand), contentType);
		}
	
	public String executeCmd(String... command) throws IOException, InterruptedException {
		logger.fine("Executing: " + command);

		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectErrorStream(true);
		builder.command(command);
		builder.directory(new File(System.getProperty("user.home")));

		Process process = builder.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		StringBuilder sb = new StringBuilder();

		Iterator<String> it = reader.lines().iterator();

		while (it.hasNext()) {
			
			sb.append(it.next());
		}
		
		process.waitFor();
		
		if(process.exitValue()!=0) {
			
			StringBuilder sbCmd = new StringBuilder();
			
			for (String cmd : command) {
				
				sbCmd.append(cmd + " ");
				
			}
			
			throw new IOException("Error executing " + sbCmd +  sb);
		}
		
		return sb.toString();
	}

	
	public Payload getFile(String filePath) throws IOException, URISyntaxException, JDOMException {
		logger.entering(sourceClass, "getFile", filePath);
		
		Path path = Paths.get(filePath);

		Payload payload = getFile(path);
		logger.exiting(sourceClass, "getFile");
		
		return payload;
	}
	
	public Payload getFile(Path filePath) throws IOException, URISyntaxException, JDOMException {
		logger.entering(sourceClass, "getFile", filePath);
		boolean contentDetected = false;
		
		logger.finer("Opening file: " + filePath);
		File file = ResourceLoader.readFile(filePath);
		String contentType = new String();
		logger.finer("Reading file " + filePath);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		StringBuilder content = new StringBuilder();
		
		while (line != null) {
			logger.finest("Line read: " + line);
			if(!contentDetected) {
				contentType = contentType(line);
				if(contentType !=SGE_ConnectConst.unknownType) {
					contentDetected = true;
				}
			}
			content.append(line);
			line = reader.readLine();
		}
		reader.close();

		logger.finer("Read file: " + filePath + "\t size: " + content.length());
		return createPayload(content.toString(), contentType);

	}

	public HttpResponse connectHttp(String url, SGE_MonitorProp mainProps) throws IOException {
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

		logger.exiting(sourceClass, "contentType", output);
		return output;
	}

	private Payload createPayload(String source, String contentType) throws IOException, JDOMException {
		logger.entering(sourceClass, "createPayload");
		Payload payload = null;
		logger.finest("Source:\n" + source);
		
		switch (contentType) {

		case SGE_ConnectConst.xmlType:

			Document doc = getXmlDocument(source);
			payload = new Payload(doc);
			break;

		case SGE_ConnectConst.jsonType:
			JSONObject jsObject = getJSON(source);
			payload = new Payload(jsObject);
			break;
		}
		logger.exiting(sourceClass, "createPayload");
		return payload;
	}

	private Payload createPayload(HttpResponse response) throws IOException, JDOMException {
		logger.entering(sourceClass, "createPayload");
		String contentType = contentType(response.getContentType());

		logger.exiting(sourceClass, "createPayload");
		return createPayload(response.getOutput(), contentType);
	}

	public Payload connect(String url, SGE_MonitorProp mainProps) throws IOException, JDOMException {
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
