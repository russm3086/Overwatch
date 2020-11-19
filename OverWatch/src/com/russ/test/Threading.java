/**
 * 
 */
package com.russ.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.ansys.cluster.monitor.net.Payload;

/**
 * @author rmartine
 *
 */
public class Threading {

	/**
	 * 
	 */
	public Threading() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {


		Threading threading = new Threading();
		threading.doWork();
		
	}
	
	public void doWork() throws InterruptedException, ExecutionException {
		
		ExecutorService service = Executors.newFixedThreadPool(100);

		List<Future<String>> allFutures = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Future<String> future = service.submit(new Task(i));
			allFutures.add(future);
		}

		service.shutdown();
		service.awaitTermination(30, TimeUnit.SECONDS);

		for (Future<String> future: allFutures) {

			String result = future.get();

			System.out.println(result);
		}
		
	}
	

	 class Task implements Callable<String> {

		int i;

		public Task(int i) {
			this.i = i;
		}

		@Override
		public String call() throws Exception {
			Thread.sleep(10000);

			String output = "index: " + i + " " + new Random().nextInt();

			return output;
		}

	}

}
