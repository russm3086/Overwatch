/**
 * 
 */
package com.ansys.cluster.monitor.net;

import java.io.Serializable;

import org.jdom2.Document;
import org.json.JSONObject;

import com.ansys.cluster.monitor.data.Cluster;

/**
 * @author rmartine
 *
 */
public class Payload implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1673144796737783954L;
	private JSONObject jsoObject;
	private Document docObject;
	private Cluster clusterObject;
	private String payloadType;
	private String srcType;

	/**
	 * 
	 */
	public Payload(JSONObject jsoObject) {

		setJsoObject(jsoObject);
		setPayloadType(SGE_ConnectConst.jsonType);

	}

	public Payload(Document docObject) {

		setDocObject(docObject);
		setPayloadType(SGE_ConnectConst.xmlType);

	}

	public Payload(Cluster cluster) {
		setClusterObject(cluster);
		setPayloadType(SGE_ConnectConst.clusterType);
	}

	public Payload() {
		setPayloadType(SGE_ConnectConst.emptyType);
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

	/**
	 * @return the clusterObject
	 */
	public Cluster getClusterObject() {
		return clusterObject;
	}

	/**
	 * @param clusterObject the clusterObject to set
	 */
	public void setClusterObject(Cluster clusterObject) {
		this.clusterObject = clusterObject;
	}

	/**
	 * @return the srcType
	 */
	public String getSrcType() {
		return srcType;
	}

	/**
	 * @param srcType the srcType to set
	 */
	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}

}
