package ie.gmit.sw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created in the ServiceHanlder Constructor, Implements Runnable and spawns a new thread. Takes a task from 
 * the In Queue, retrieves a list of results from the CalculateJaccard Class, then stores the document in the 
 * database. Adds the results to the Out Queue.
 * 
 * @author Patrick Moran
 */
public class Worker implements Runnable {
	
	private BlockingQueue<Task> inQ = new ArrayBlockingQueue<Task>(100);
	private BlockingQueue<List<Results>> outQ = new ArrayBlockingQueue<List<Results>>(100);	
	private Task task;
	private List<Document> listDocuments = new ArrayList<Document>();
	private CalculateJaccard cj;
	private List<Results> listResults = new ArrayList<Results>();
	
	
	public Worker(BlockingQueue<Task> inQ, BlockingQueue<List<Results>> outQ){
		this.inQ = inQ;
		this.outQ = outQ;
	}

	/**
	 * Creates a new Instance of the Document Runner to store in the database. Handles Tasks in and out
	 * of the Queues.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Override
	public void run() {
		while(true){
			inQ = GlobalQueue.getInQ();
			task = inQ.poll();
			
			if(task != null){							
				DocumentRunner dr;
				try {
					dr = new DocumentRunner();
					
					listDocuments = dr.getDocuments();
					
					//Calculate Jaccard
					cj = new CalculateJaccard(listDocuments, task.getDocument());
					listResults = cj.calculateJaccard();
					
					//Add to the Database
					dr.addDocumentsToDatabase(task.getDocument());
					
					//Shutdown Down The Database
					dr.closeDB();
				} catch (FileNotFoundException e) {				
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}	
				//Add the list of results to the Global Out Q
				GlobalQueue.addToOutQueue(listResults);
				outQ = GlobalQueue.getOutQ();				
			}			
		}		
	}
}
