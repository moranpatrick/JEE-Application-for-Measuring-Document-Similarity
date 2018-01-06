package ie.gmit.sw;

import java.util.List;

public class Document {
	private List shingles;
	private String docId;
	private String name;
	
	public Document(List shingles, String docId, String name) {
		super();
		this.shingles = shingles;
		this.docId = docId;
		this.name = name;
	}
	public List getShingles() {
		return shingles;
	}
	public void setShingles(List shingles) {
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
