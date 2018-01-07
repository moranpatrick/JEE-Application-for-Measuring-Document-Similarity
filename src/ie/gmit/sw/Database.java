package ie.gmit.sw;

import java.util.List;

public interface Database {
	public void addDocumentsToDatabase(Document d);
	public List<Document> getDocuments();
	
}
