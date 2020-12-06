/**
 * 
 */
package com.ansys.cluster.monitor.net;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.factory.ClusterFactory;

/**
 * @author rmartine
 *
 */
public class DataCollectorWorker implements Callable<DataCollectorWorker> {

	public enum SrcType {
		HOST_DATA, JOB_DATA, DETAILED_JOB_DATA, QUOTA_DATA, FULL_DETAILED_JOB_DATA, QUEUE_DATA
	}

	private DataCollector dc;
	private SrcType srcType;
	private int index;
	private Payload payLoad = null;

	private String sourceClass = this.getClass().getName();
	private Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 */
	public DataCollectorWorker(DataCollector dc, SrcType srcType, int index) {
		setDc(dc);
		setSrcType(srcType);
		setIndex(index);
	}

	@Override
	public DataCollectorWorker call() throws Exception {

		String prefix = "Thread " + Thread.currentThread().getId() + " ";

		switch (getSrcType()) {

		case HOST_DATA:

			logger.info(prefix + "Getting host data");
			setStatusLabel("Getting host data");
			setPayLoad(dc.getHostsData(index));
			break;

		case JOB_DATA:

			logger.info(prefix + "Getting job data");
			setStatusLabel("Getting job data");
			setPayLoad(dc.getJobsData(index));
			break;

		case DETAILED_JOB_DATA:

			logger.info(prefix + "Getting detailed job data");
			setStatusLabel("Getting detailed job data");
			setPayLoad(dc.getDetailedJobsData(index));
			break;

		case QUOTA_DATA:

			logger.info(prefix + "Getting quota data");
			setStatusLabel("Getting quota data");
			setPayLoad(dc.getQuotaData(index));
			break;

		case FULL_DETAILED_JOB_DATA:

			logger.info(prefix + "Getting Full Detailed Jobs data");
			setStatusLabel("Getting Full Detailed Jobs data");
			setPayLoad(dc.getFullDetailedJobsData(index));
			break;

		case QUEUE_DATA:

			logger.info(prefix + "Getting queue data");
			setStatusLabel("Getting queue data");
			setPayLoad(dc.getQueueData(index));
			break;

		default:
			break;

		}

		return this;
	}

	/**
	 * @return the dc
	 */
	public DataCollector getDc() {
		return dc;
	}

	/**
	 * @param dc the dc to set
	 */
	public void setDc(DataCollector dc) {
		this.dc = dc;
	}

	/**
	 * @return the srcType
	 */
	public SrcType getSrcType() {
		return srcType;
	}

	/**
	 * @param srcType the srcType to set
	 */
	public void setSrcType(SrcType srcType) {
		this.srcType = srcType;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the payLoad
	 */
	public Payload getPayLoad() {
		return payLoad;
	}

	/**
	 * @param payLoad the payLoad to set
	 */
	public void setPayLoad(Payload payLoad) {
		this.payLoad = payLoad;
	}

	private void setStatusLabel(String msg) {
		ClusterFactory.setStatusLabel(msg);
	}

}
