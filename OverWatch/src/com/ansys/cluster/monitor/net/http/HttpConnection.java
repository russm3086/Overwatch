/**
 * 
 */
package com.ansys.cluster.monitor.net.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.settings.SGE_MonitorProp;
import com.russ.util.UnitConversion;

/**
 * @author rmartine
 *
 */
public class HttpConnection {
	private String sourceClass = this.getClass().getName();
	private Logger logger = Logger.getLogger(sourceClass);

	private SGE_MonitorProp mainProps;

	/**
	 * 
	 */
	public HttpConnection(SGE_MonitorProp mainProps) {
		
		setMainProps(mainProps);
	}

	public HttpResponse httpRequest(String strUrl) throws IOException, ClassNotFoundException {

		return httpRequest(strUrl, mainProps.getClusterConnectionRequestMethod(),
				mainProps.getClusterConnectionRequestTimeOut(), mainProps.getClusterConnectionRequestReadTimeOut(),
				mainProps.getClusterConnectionRequestReadBuffer());

	}

	public HttpResponse httpRequest(String strUrl, String requestMethod, int connectionTimeOut, int readTimeOut,
			int bufferLength) throws IOException, ClassNotFoundException {
		logger.entering(sourceClass, "httpRequest");

		URL url = new URL(strUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(connectionTimeOut);
		con.setReadTimeout(readTimeOut);

		con.connect();

		logger.info("Connecting to " + strUrl + " \t Request Method: " + requestMethod);
		long startTime = System.currentTimeMillis();

		HttpResponse response = new HttpResponse(con, bufferLength);

		long estimatedTime = System.currentTimeMillis() - startTime;

		logger.info("Downloaded " + strUrl + "\t Download Size: "
				+ UnitConversion.humanReadableByteCount(response.getContentSize(), false) + " bytes\tElapse Time: "
				+ estimatedTime + " ms.");

		con.disconnect();
		logger.exiting(sourceClass, "httpRequest");
		return response;

	}

	protected SGE_MonitorProp getMainProps() {
		return mainProps;
	}

	protected void setMainProps(SGE_MonitorProp mainProps) {
		this.mainProps = mainProps;
	}

}
