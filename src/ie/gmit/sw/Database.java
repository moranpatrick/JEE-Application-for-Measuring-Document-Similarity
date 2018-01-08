package ie.gmit.sw;

import java.util.List;

/**
 * An Interface describing how to add and retrieve documents from the DB40 database
 * 
 * @author Patrick Moran
 */

public interface Database {
	public void addDocumentsToDatabase(Document d);
	public List<Document> getDocuments();
}
