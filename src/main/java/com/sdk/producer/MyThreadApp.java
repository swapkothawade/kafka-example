package com.sdk.producer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyThreadApp {
	ExecutorService service = Executors.newFixedThreadPool(10);

	public void executeTask() throws InterruptedException {
		List<Future<String>> futureList = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Future<String> future = service.submit(new MyTask(dateFormat));
			futureList.add(future);
			Thread.sleep(100);
		}
		futureList.stream().map(item -> {
			try {
				return item.get();
			} catch (InterruptedException e) {

				e.printStackTrace();
			} catch (ExecutionException e) {

				e.printStackTrace();
			}
			return null;
		}).forEach(System.out::println);

		
		
	}

	public static void main(String[] args1) {
		MyThreadApp app = new  MyThreadApp();
		try {
			app.executeTask();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class MyTask implements Callable<String> {
	private SimpleDateFormat dateFormat;

	public MyTask(SimpleDateFormat dateFormat) {
		super();
		this.dateFormat = dateFormat;
	}

	@Override
	public String call() throws Exception {
		String localDate = dateFormat.format(LocalDate.now());
		return localDate;
	}

}
