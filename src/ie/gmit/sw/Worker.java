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
	
	private BlockingQueue<Task> inQ = new ArrayBlockingQueue<Task>(10);
	private BlockingQueue<Task> outQ = new ArrayBlockingQueue<Task>(10);	
	private Task task;
	
	private List<Document> listDocuments = new ArrayList<Document>();
	private ObjectContainer db = null;
	
	
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
				
				DocumentRunner dr;
				try {
					dr = new DocumentRunner();
					dr.addDocumentsToDatabase(task.getDocument());
					listDocuments = dr.getDocuments();
					
					dr.showAllDocuments();
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}							
				
			}
			
		}
		
	}

}
