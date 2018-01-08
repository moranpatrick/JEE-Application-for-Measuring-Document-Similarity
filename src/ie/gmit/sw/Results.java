package ie.gmit.sw;

public class Results {
	private String newDoc;
	private String oldDoc;
	private Double jaccardIndex;
	
	public Results(String newDoc, String oldDoc, Double jaccardIndex) {
		super();
		this.newDoc = newDoc;
		this.oldDoc = oldDoc;
		this.jaccardIndex = jaccardIndex;
	}

	public String getNewDoc() {
		return newDoc;
	}

	public void setNewDoc(String newDoc) {
		this.newDoc = newDoc;
	}

	public String getOldDoc() {
		return oldDoc;
	}

	public void setOldDoc(String oldDoc) {
		this.oldDoc = oldDoc;
	}

	public Double getJaccardIndex() {
		return jaccardIndex;
	}

	public void setJaccardIndex(Double jaccardIndex) {
		this.jaccardIndex = jaccardIndex;
	}	
}
