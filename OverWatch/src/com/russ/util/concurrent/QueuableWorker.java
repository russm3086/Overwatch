/**
 * 
 */
package com.russ.util.concurrent;

import javax.swing.SwingWorker;

/**
 * @author rmartine
 *
 */
public abstract class QueuableWorker<T, V> extends SwingWorker<T, V> {
	protected WorkerManager manager;

	public QueuableWorker(WorkerManager manager) {
		this.manager = manager;
	}

	@Override
	protected abstract T doInBackground();

	@Override
	final protected void done() {
		completed();
		manager.workerDone();
	}

	protected abstract void completed();
}
