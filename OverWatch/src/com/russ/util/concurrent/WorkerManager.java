/**
 * 
 */
package com.russ.util.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

/**
 * @author rmartine
 *
 */
public class WorkerManager {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);

	protected volatile ConcurrentLinkedQueue<QueuableWorker<?, ?>> queue;
	protected volatile boolean _executing;
	protected volatile int queueSize;
	protected ExecutorService executor;

	public WorkerManager(int queueSize, ExecutorService executor) {
		queue = new ConcurrentLinkedQueue<QueuableWorker<?, ?>>();
		this.queueSize = queueSize;
		this.executor = executor;
	}

	public synchronized void queueExecution(QueuableWorker<?, ?> worker) {
		if (queueSize > queue.size()) {

			logger.fine("Queueing worker");
			queue.add(worker);
			if (!_executing)
				executeNext();
		} else {

			logger.fine("Queue has hit its limits " + queueSize + " ignoring all request");
		}
	}

	public synchronized void executeNext() {
		SwingWorker<?, ?> worker = queue.poll();
		if (worker != null) {
			setExecuting(true);
			logger.severe("WorkerManager: Starting the next worker... (" + queue.size() + " more queued)");
			executor.submit(worker);
		}
	}

	public void workerDone() {
		setExecuting(false);
		executeNext();
	}

	public void setExecuting(boolean b) {
		_executing = b;
	}

}
