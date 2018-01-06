package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;

import xtea_db4o.XTEA;
import xtea_db4o.XTeaEncryptionStorage;

public class DocumentRunner {

	private ObjectContainer db = null;
	private List<Document> listDocuments = new ArrayList<Document>();
	
	
	public DocumentRunner() throws FileNotFoundException, IOException {
		init();
		
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().add(new TransparentActivationSupport()); //Real lazy. Saves all the config commented out below
		config.common().add(new TransparentPersistenceSupport()); //Lazier still. Saves all the config commented out below
		config.common().updateDepth(7); //Propagate updates
		
		//Use the XTea lib for encryption. The basic Db4O container only has a Caesar cypher... Dicas quod non est ita!
		config.file().storage(new XTeaEncryptionStorage("password", XTEA.ITERATIONS64));
	
		//Open a local database. Use Db4o.openServer(config, server, port) for full client / server
		db = Db4oEmbedded.openFile(config, "documents.data");
		
		
		addDocumentsToDatabase();
		showAllDocuments();
				
	}
		
	private void init() throws FileNotFoundException, IOException{
		// 1 - Get 3 Array Lists of shingles from the 3 stored files
		String textFile1 = "files/textFile1.txt";
					
		

		
		
		// 2 - Change Them Into Documents
		
		// 3 - Save Them To The Database
		

				
	}
	
	private void addDocumentsToDatabase() {
		for (Document d: listDocuments){
			db.store(d); //Adds the customer object to the database
		}
		db.commit(); //Commits the transaction
		//db.rollback(); //Rolls back the transaction
	}
	
	private void showAllDocuments(){
		//An ObjectSet is a specialised List for storing results
		ObjectSet<Document> documents = db.query(Document.class);
		for (Document document : documents) {
			System.out.println("DocId: " + document.getDocId() + "\t ***Database ObjID: " + db.ext().getID(document));

			//Removing objects from the database is as easy as adding them
			//db.delete(customer);
			db.commit();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		new DocumentRunner();
	}

}
