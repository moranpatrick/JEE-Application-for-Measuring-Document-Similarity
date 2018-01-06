package ie.gmit.sw;

import java.io.File;

public class Shingle {
	private int hashCode;
	private String docId;
	
	
	public Shingle(int hashCode, String id){
		this.hashCode = hashCode;
		this.docId = id;		
	}

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}	
	
}
