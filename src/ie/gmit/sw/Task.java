package ie.gmit.sw;
/**
 * Used to create a Task object which is then added and removed from the queues.
 * 
 * @author Patrick Moran
 */
public class Task {
	private Document document;
	
	public Task(Document document) {
		super();
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}	
}
