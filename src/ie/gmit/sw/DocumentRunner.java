package ie.gmit.sw;

import java.io.File;

/**
 * Sets up DB40 Database, initialising it with three text files for comparison.
 * 
 * @author Patrick Moran
 */
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

public class DocumentRunner implements Database{

	private ObjectContainer db = null;
	private List<Document> listDocuments = new ArrayList<Document>();
	private ShingleWorker shingleWorker;
	private List<Shingle> listShingles = new ArrayList<Shingle>();
	private Document doc;
	
	/**
	 * Constructor Creates Database. Checks if there are any documents stored, if not adds three to the database by calling INIT().
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DocumentRunner() throws FileNotFoundException, IOException {
		
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().add(new TransparentActivationSupport()); //Real lazy. Saves all the config commented out below
		config.common().add(new TransparentPersistenceSupport()); //Lazier still. Saves all the config commented out below
		config.common().updateDepth(7); //Propagate updates
		
		//Use the XTea lib for encryption. The basic Db4O container only has a Caesar cypher... Dicas quod non est ita!
		config.file().storage(new XTeaEncryptionStorage("password", XTEA.ITERATIONS64));
	
		//Open a local database. Use Db4o.openServer(config, server, port) for full client / server
		db = Db4oEmbedded.openFile(config, "documents.data");
				
		// Retrieve All The Documents - If there are  Initialise the Database with three text files
		listDocuments = getDocuments();
		
		if(listDocuments.size() == 0){
			init();
		}
		showAllDocuments();							
	}
		
	/**
	 * Initialises the database with three text files stored locally.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void init() throws FileNotFoundException, IOException{
		System.out.println("IN INIT()");
		int i = 0;
		File dir = new File(Global.getFilePath());
		
		// Loop around the directory where the 3 files are stored
		for(File f : dir.listFiles()){
			i++; //Give Each File a different DocId
			shingleWorker = new ShingleWorker(f, "F"+i);
			// Delegate some work to the Shingle Worker and get an array list of shingles
			listShingles = shingleWorker.processShingle();
			//create a new document
			doc = new Document(listShingles, "F"+i, f.getName());
			//Add the document at an array list of documents
			listDocuments.add(doc);
			
		}
		addInitialFilesToDatabase();				
	}
	
	/**
	 * Adds The Initial Setup Files to the Database.
	 * 
	 */
	private void addInitialFilesToDatabase() {
		for (Document d: listDocuments){
			db.store(d); 
		}
		db.commit(); //Commits the transaction
	}
	
	/**
	 * Adds a document to the Database.
	 * 
	 */
	public void addDocumentsToDatabase(Document d) {		
		db.store(d); 
		db.commit(); //Commits the transaction	
	}
	
	/**
	 * Displays All Documents
	 * 
	 */
	public void showAllDocuments(){
		//An ObjectSet is a specialised List for storing results
		ObjectSet<Document> documents = db.query(Document.class);
		for (Document document : documents) {
			System.out.println("DocId: " + document.getDocId() + "\t ***Database ObjID: " + db.ext().getID(document) + "DOC Name: " + document.getName());

			//Removing objects from the database is as easy as adding them
			//db.delete(document);
			db.commit();
		}
	}
	
	/**
	 * Retrives all documents from the database.
	 * 
	 * @return List<Document>
	 */
	public List<Document> getDocuments(){
		//An ObjectSet is a specialised List for storing results
		List<Document> temp = new ArrayList<Document>();
		ObjectSet<Document> documents = db.query(Document.class);
		for (Document document : documents) {
			//System.out.println("DocId: " + document.getDocId() + "\t ***Database ObjID: " + db.ext().getID(document) + "DOC Name: " + document.getName());
			temp.add(document);
			//Removing objects from the database is as easy as adding them
			//db.delete(document);
			db.commit();
		}
		return temp;
	}
	
	/**
	 * Closes The Database
	 * 
	 */
	public void closeDB()
	{
		//Close The DB
		db.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		new DocumentRunner();
	}
}
