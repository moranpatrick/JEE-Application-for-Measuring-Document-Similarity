package ie.gmit.sw;

import java.util.List;
/**
 * An Document Class which are stored in the database.
 * 
 * @author Patrick Moran
 */
public class Document {
	private List<Shingle> shingles;
	private String docId;
	private String name;
	
	public Document(List<Shingle> shingles, String docId, String name) {
		super();
		this.shingles = shingles;
		this.docId = docId;
		this.name = name;
	}
	public List<Shingle> getShingles() {
		return shingles;
	}
	public void setShingles(List<Shingle> shingles) {
		this.shingles = shingles;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}
