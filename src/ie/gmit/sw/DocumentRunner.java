package ie.gmit.sw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	private ShingleWorker shingleWorker;
	private List<Shingle> listShingles = new ArrayList<Shingle>();
	private Document document;
	
	
	public DocumentRunner() throws FileNotFoundException, IOException {
		// Initialise the Database with three text files
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

		int i = 0;
		File dir = new File("textFiles/");
		
		for(File f : dir.listFiles()){
			i++;
			shingleWorker = new ShingleWorker(f, "F"+i);
			// Delegate some work to the Shingle Worker and get an array list of shingles
			listShingles = shingleWorker.processShingle();
			//create a new document
			document = new Document(listShingles, "F"+i, f.getName());
			//Add the document at an array list of documents
			listDocuments.add(document);
			
		}
		System.out.println(listDocuments.size());		

				
	}
	
	private void addDocumentsToDatabase() {
		for (Document d: listDocuments){
			db.store(d); 
		}
		db.commit(); //Commits the transaction
		//db.rollback(); //Rolls back the transaction
	}
	
	private void showAllDocuments(){
		//An ObjectSet is a specialised List for storing results
		ObjectSet<Document> documents = db.query(Document.class);
		for (Document document : documents) {
			System.out.println("DocId: " + document.getDocId() + "\t ***Database ObjID: " + db.ext().getID(document) + "DOC Name: " + document.getName());

			//Removing objects from the database is as easy as adding them
			//db.delete(document);
			db.commit();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		new DocumentRunner();
	}

}