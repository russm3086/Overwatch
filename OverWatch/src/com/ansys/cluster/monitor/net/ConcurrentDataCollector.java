/**
 * 
 */
package com.ansys.cluster.monitor.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.data.factory.ClusterFactory;
import com.ansys.cluster.monitor.net.DataCollectorWorker.SrcType;

/**
 * @author rmartine
 *
 */
public class ConcurrentDataCollector {
	private String sourceClass = this.getClass().getName();
	private Logger logger = Logger.getLogger(sourceClass);
	private DataCollector dc;
	private int index;
	private int poolSize;
	private int timeOut;
	private TimeUnit tu;

	/**
	 * 
	 */
	public ConcurrentDataCollector(DataCollector dc, int index, int poolSize, int timeOut, TimeUnit tu) {

		setDc(dc);
		setIndex(index);
		setPoolSize(poolSize);
		setTimeOut(timeOut);
		setTu(tu);
	}

	public HashMap<SrcType, Payload> collect() {
		logger.entering(sourceClass, "collect");
		HashMap<SrcType, Payload> resultMap = new HashMap<SrcType, Payload>();

		List<DataCollectorWorker> tasks = new ArrayList<DataCollectorWorker>();

		tasks.add(new DataCollectorWorker(dc, SrcType.HOST_DATA, index));
		tasks.add(new DataCollectorWorker(dc, SrcType.JOB_DATA, index));
		tasks.add(new DataCollectorWorker(dc, SrcType.DETAILED_JOB_DATA, index));
		tasks.add(new DataCollectorWorker(dc, SrcType.QUOTA_DATA, index));

		logger.fine("Creating threadpool size: " + getPoolSize());
		ExecutorService service = Executors.newFixedThreadPool(getPoolSize());

		try {

			setStatusLabel("Connecting to cluster");
			logger.finer("ThreadPool time out set to " + timeOut + " " + tu);
			logger.info("Starting communication threadPool");

			long startTime = System.currentTimeMillis();

			List<Future<DataCollectorWorker>> resultList = service.invokeAll(tasks, timeOut, tu);

			long estimatedTime = System.currentTimeMillis() - startTime;

			logger.info("Total communication Elapse Time: " + estimatedTime + " ms.");

			for (Future<DataCollectorWorker> future : resultList) {

				logger.finer(future.toString() + " done: " + future.isDone());

				if (!future.isCancelled()) {

					logger.fine("Getting input: " + future.get().getSrcType());
					resultMap.put(future.get().getSrcType(), future.get().getPayLoad());
				} else {
					logger.fine(future.toString() + " Cancelled: " + future.isCancelled());
				}
			}

		} catch (InterruptedException | ExecutionException e) {

			logger.log(Level.SEVERE, "Error retieving data", e);
		}

		stopExector(service);

		logger.exiting(sourceClass, "collect");

		return resultMap;
	}

	private void stopExector(ExecutorService service) {

		service.shutdown();

		try {
			
			Thread.sleep(500);

			if (!service.isShutdown()) {

				logger.warning("Threadpool is not shutdown.  Forcing shutdown");
				service.shutdownNow();

				Thread.sleep(2500);

				logger.info("pool thread shutdown: " + service.isShutdown());

			} else {
				logger.fine("Threadpool shut down");
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Error Stopping threadpool", e);
		}

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
	 * @return the poolSize
	 */
	public int getPoolSize() {
		return poolSize;
	}

	/**
	 * @param poolSize the poolSize to set
	 */
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	/**
	 * @return the timeOut
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut the time=ut to set
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * @return the tu
	 */
	public TimeUnit getTu() {
		return tu;
	}

	/**
	 * @param tu the tu to set
	 */
	public void setTu(TimeUnit tu) {
		this.tu = tu;
	}

	private void setStatusLabel(String msg) {
		ClusterFactory.setStatusLabel(msg);
	}

}
