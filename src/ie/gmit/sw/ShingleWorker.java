package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;
/**
 * Creates a list of Shingles and Returns them
 * 
 * @author Patrick Moran
 */
public class ShingleWorker {
	
	private Part part;
	private String docId;
	private Shingle shingle;
	private String[] words = null;
	private String line = null;
	private File file;
	private BufferedReader br = null;
	
	private List<Shingle> listShingles = new ArrayList<Shingle>();
	
	/**
	 * Constructor Called if its a Part from the ServiceHandler 
	 * 
	 */
	public ShingleWorker(Part part, String docId){
		this.part = part;
		this.docId = docId;		
	}
	
	/**
	 * Constructor Called of its a File object from the DocumentRunner
	 * 
	 */
	public ShingleWorker(File f, String docId){
		this.file = f;
		this.docId = docId;
	}
		
	/**
	 * Creates and returns shingles either to the ServiceHandler or the DocumentRunner class.
	 * 
	 * @return List<Shingle>
	 * @throws IOException
	 */
	public List<Shingle> processShingle() throws IOException{
		int counter = 0; 
		
		if(part != null){
			//Use this buffered reader to create Shingles From The Users input
			br = new BufferedReader(new InputStreamReader(part.getInputStream()));
		}
		else{
			// Create Shingles for initial File Setup File in DB40 Runner
			br = new BufferedReader(new FileReader(file));
		}

		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
						
			//Ignores commas, periods, spaces and other punctuation
			words = line.split("\\W+");
			
			for(int i = 0; i < words.length; i++){
				counter++;
				
				sb.append(words[i]);
				if(counter == Global.getShingleSize()){
					shingle = new Shingle(sb.toString().hashCode(), docId);
					listShingles.add(shingle);
					counter = 0;
					sb.delete(0, sb.length());					
				}		
			}	
		}
		//Return ArrayList of Shingles	
		return listShingles;
	}
}
