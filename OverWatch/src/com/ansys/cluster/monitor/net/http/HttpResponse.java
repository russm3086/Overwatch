package com.ansys.cluster.monitor.net.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.Cluster;
import com.ansys.cluster.monitor.data.factory.ClusterFactory;
import com.russ.util.UnitCoversion;

import java.time.temporal.ChronoUnit;

/**
 * 
 */

/**
 * @author rmartine
 *
 */
public class HttpResponse {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private int responseCode = 0;
	private String responseMessage = new String();
	private String headerFields = new String();
	private Object output = null;
	private String contentType = new String();
	private long contentSize = 0;
	private String url;

	/**
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * 
	 */
	public HttpResponse(HttpURLConnection con, int bufferLength) throws IOException, ClassNotFoundException {
		logger.entering(sourceClass, "HttpResponse");
		StringBuilder fullResponseBuilder = new StringBuilder();

		setResponseCode(con.getResponseCode());
		setResponseMessage(con.getResponseMessage());
		setContentType(con.getContentType());
		setUrl(con.getURL().toString());

		con.getHeaderFields().entrySet().stream().filter(entry -> entry.getKey() != null).forEach(entry -> {

			fullResponseBuilder.append(entry.getKey()).append(": ");

			List<String> headerValues = entry.getValue();
			Iterator<String> it = headerValues.iterator();
			if (it.hasNext()) {
				fullResponseBuilder.append(it.next());

				while (it.hasNext()) {
					fullResponseBuilder.append(", ").append(it.next());
				}
			}

			fullResponseBuilder.append("\n");
		});

		if (getResponseCode() > 200) {

			String headerFields = readStrStream(con.getErrorStream(), bufferLength);
			fullResponseBuilder.append("Response: ").append(headerFields);

		} else {

			if (getContentType().toLowerCase().contains("overwatch") == true) {
				
				setOutput(readClusterStream(con.getInputStream(), bufferLength));
			} else {
				setOutput(readStrStream(con.getInputStream(), bufferLength));
			}

		}

		setHeaderFields(fullResponseBuilder.toString());
		logger.exiting(sourceClass, "HttpResponse");
	}

	protected ByteArrayOutputStream readInStream(InputStream stream, int bufferLength) throws IOException {
		logger.entering(sourceClass, "readInStream");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[bufferLength];
		int read;

		LocalDateTime now = LocalDateTime.now();

		try {
			while ((read = stream.read(buffer)) != -1) {
				logger.finer("Buffer read " + read + " bytes");

				if (read >= bufferLength * .85) {
					logger.info("Reaching or reached buffer limited of "
							+ UnitCoversion.humanReadableByteCount(bufferLength, false) + "\n\t read: "
							+ UnitCoversion.humanReadableByteCount(read, false));
				}

				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("Buffer content: " + new String(buffer, StandardCharsets.UTF_8));
				}
				os.write(buffer, 0, read);

				if (ChronoUnit.SECONDS.between(now, LocalDateTime.now()) > 10) {

					ClusterFactory.setStatusLabel(
							getUrl() + " downloaded: " + UnitCoversion.humanReadableByteCount(os.size(), false));
					now = LocalDateTime.now();

				}

			}
		} finally {
			stream.close();
			
			setContentSize(os.size());

		}

		logger.exiting(sourceClass, "readInStream");

		return os;
	}

	protected Cluster readClusterStream(InputStream stream, int bufferLength)
			throws ClassNotFoundException, IOException {
		logger.entering(sourceClass, "readClusterStream");

		ByteArrayOutputStream bos = readInStream(stream, bufferLength);
		ObjectInputStream in = null;

		try {
		
		byte[] bytes = bos.toByteArray();
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

			in = new ObjectInputStream(bis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cluster cluster = (Cluster) in.readObject();
		logger.exiting(sourceClass, "readClusterStream");

		return cluster;
	}

	protected String readStrStream(InputStream stream, int bufferLength) throws IOException {
		logger.entering(sourceClass, "readStrStream");
		ByteArrayOutputStream os = readInStream(stream, bufferLength);

		StringBuilder content = new StringBuilder();
		content.append(os.toString(StandardCharsets.UTF_8.displayName()));

		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(content.toString());
		}

		logger.exiting(sourceClass, "readStrStream");
		return content.toString();
	}

	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getFullMessage() {

		StringBuffer sb = new StringBuffer();
		sb.append("Code: " + getResponseCode() + "/n");
		sb.append("Message: " + getResponseMessage() + "/n");
		sb.append(getHeaderFields());

		return sb.toString();
	}

	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(String headerFields) {
		this.headerFields = headerFields;
	}

	public Object getOutput() {
		return output;
	}

	public void setOutput(Object output) {
		this.output = output;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the length
	 */
	public long getContentSize() {
		return contentSize;
	}

	/**
	 * @param length the length to set
	 */
	public void setContentSize(long length) {
		this.contentSize = length;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}


}
