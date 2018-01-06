package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {
	
	private BlockingQueue<Task> inQ;
	private BlockingQueue<Task> outQ;	
	private Task task;
	
	
	public Worker(BlockingQueue<Task> inQ, BlockingQueue<Task> outQ){
		this.inQ = inQ;
		this.outQ = outQ;
	}

	@Override
	public void run() {
		while(true){
			task = inQ.poll();
			
			if(task != null){			
				//Save Document To Database
				
				
				//Get Document from database
			}
			
		}
		
	}

}
