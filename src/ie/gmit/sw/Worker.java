package ie.gmit.sw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.management.Query;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;

import xtea_db4o.XTEA;
import xtea_db4o.XTeaEncryptionStorage;

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

	@Override
	public void run() {
		while(true){
			inQ = GlobalQueue.getInQ();
			task = inQ.poll();
			
			if(task != null){			
				//Save Document To Database
				
				DocumentRunner dr;
				try {
					dr = new DocumentRunner();
					
					listDocuments = dr.getDocuments();
					
					//Calculate Jaccard
					cj = new CalculateJaccard(listDocuments, task.getDocument());
					listResults = cj.calculateJaccard();
					
					//dr.addDocumentsToDatabase(task.getDocument());
					//Shutdown Down The Database
					dr.closeDB();
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}	
				GlobalQueue.addToOutQueue(listResults);
				outQ = GlobalQueue.getOutQ();
				System.out.println("Out Q Size IS: " + outQ.size());
				
			}
			
		}
		
	}

}
