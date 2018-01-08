package ie.gmit.sw;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GlobalQueue {
	private static BlockingQueue<Task> inQ;
	private static BlockingQueue<List<Results>> outQ;	
	
	private GlobalQueue(){
		
	}
	
	public static synchronized void init(){
		inQ = new ArrayBlockingQueue<Task>(100);
	    outQ = new ArrayBlockingQueue<List<Results>>(100);
	}

	public static BlockingQueue<Task> getInQ() {
		return inQ;
	}

	public static void setInQ(BlockingQueue<Task> inQ) {
		GlobalQueue.inQ = inQ;
	}

	public static BlockingQueue<List<Results>> getOutQ() {
		return outQ;
	}

	public static void setOutQ(BlockingQueue<List<Results>> outQ) {
		GlobalQueue.outQ = outQ;
	}
	
	public static void addToInQueue(Task t){
		
		GlobalQueue.inQ.add(t);
		
	}
	
	public static void addToOutQueue(List<Results> r){
		
		GlobalQueue.outQ.add(r);		
	}	
}

