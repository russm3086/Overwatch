/**
 * 
 */
package com.ansys.cluster.monitor.net;

import org.jdom2.Document;
import org.json.JSONObject;

/**
 * @author rmartine
 *
 */
public class Payload {
	private JSONObject jsoObject;
	private Document docObject;
	private String payloadType;
	/**
	 * 
	 */
	public Payload(JSONObject jsoObject) {
		// TODO Auto-generated constructor stub
		
		setJsoObject(jsoObject);
		setPayloadType("JSON");
		
	}

	public Payload(Document docObject) {
		// TODO Auto-generated constructor stub
		
		setDocObject(docObject);
		setPayloadType("XML");
		
	}

	/**
	 * @return the jsoObject
	 */
	public JSONObject getJsoObject() {
		return jsoObject;
	}
	/**
	 * @param jsoObject the jsoObject to set
	 */
	public void setJsoObject(JSONObject jsoObject) {
		this.jsoObject = jsoObject;
	}
	/**
	 * @return the docObject
	 */
	public Document getDocObject() {
		return docObject;
	}
	/**
	 * @param docObject the docObject to set
	 */
	public void setDocObject(Document docObject) {
		this.docObject = docObject;
	}
	/**
	 * @return the payloadType
	 */
	public String getPayloadType() {
		return payloadType;
	}
	/**
	 * @param payloadType the payloadType to set
	 */
	public void setPayloadType(String payloadType) {
		this.payloadType = payloadType;
	}

}
