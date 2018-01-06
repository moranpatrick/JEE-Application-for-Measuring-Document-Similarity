package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

public class ShingleWorker {
	
	private Part part;
	private String docId;
	private Shingle shingle;
	private String[] words = null;
	private String line = null;
	
	private List<Shingle> listShingles = new ArrayList<Shingle>();
	
	public ShingleWorker(Part part, String docId){
		this.part = part;
		this.docId = docId;		
	}
		
	public List<Shingle> processShingle() throws IOException{
		int counter = 0; 
		
		/*Down in there while loop */
		BufferedReader br = new BufferedReader(new InputStreamReader(part.getInputStream()));

		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			
			
			//Ignores commas, periods, spaces and other punctuation
			words = line.split("\\W+");
			
			for(int i = 0; i < words.length; i++){
				counter++;
				
				sb.append(words[i]);
				if(counter == 3){
					System.out.println(sb.toString());
					shingle = new Shingle(sb.toString().hashCode(), docId);
					listShingles.add(shingle);
					counter = 0;
					sb.delete(0, sb.length());					
				}		
			}	
		}
		
		
		return listShingles;
	}
	
	
	
}
