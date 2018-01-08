package ie.gmit.sw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * CalculateJaccard is responsible for determining the jaccard index of an uploaded
 * document compared to all the documents in the database and returning a list of results.
 * 
 * @author Patrick Moran
 */

public class CalculateJaccard {
	private List<Document> listDocuments = new ArrayList<Document>();
	private Document document;
	private Set<Integer> hashes = new TreeSet<Integer>();
	private List<Integer> oldDoc = new ArrayList<Integer>();
	private List<Integer> newDoc = new ArrayList<Integer>();
	private List<Integer> common = new ArrayList<Integer>();
	private List<Results> results = new ArrayList<Results>();
	private Results r;
	private double jaccard;
	
	public CalculateJaccard(List<Document> listDocuments, Document document) {
		this.listDocuments = listDocuments;
		this.document = document;
	}
	
	/**
	 * Calculates the Jaccard Index against each document in the database and 
	 * returns a list of Results.
	 * 
	 * @return List<Results>
	 */
	public List<Results> calculateJaccard(){
		
		//Generate Random Hashes
		hashes = generateHashes();
		
		// Loop Over All The Documents
		for(Document doc : listDocuments){
			newDoc = generateMinHashes(document);
			oldDoc = generateMinHashes(doc);

			// Calculate Jaccard Index
			
			// Add all the of the hashes from newDoc to the common list
			common.addAll(newDoc);
			// Keeps only The hashes that are the same in the oldDoc
			common.retainAll(oldDoc);
			
			/*
			 * Jaccard * J(A,B) = |A intersection B| / |A union B|
			 */
			
			jaccard = ((double)common.size()) / newDoc.size();
			//Create a Result
			r = new Results(document.getName(), doc.getName(), jaccard);
			// Add the Result to the ArrayList of Results
			results.add(r);		
		}
		
		return results;
	}
	
	/**
	 * Generates a Random Set of 200 Integers and Returns Them.
	 * 
	 * @return Set<Integer>
	 */
	public Set<Integer> generateHashes(){
		
		Random random = new Random();
		for(int i = 0; i < Global.getMaxHashes(); i++){
			hashes.add(random.nextInt()); // Create 200 Random Integers
		}		
		return hashes;			
	}
	
	/**
	 * Generates MinHashes, adds them to A list and returns them.
	 * 
	 * @param Document
	 * @return List<Integer>
	 */
	public List<Integer> generateMinHashes(Document d){
		List<Integer> temp = new ArrayList<Integer>();
		List<Shingle> shingles = new ArrayList<Shingle>();
		
		shingles = d.getShingles();
		
		for(Integer hash : hashes){
			int min = Integer.MAX_VALUE;
			
			for(int i = 0; i < shingles.size(); i++){
				int minHash = shingles.get(i).getHashCode() ^ hash; // XOR the shingle hashcode with the hash
				if(minHash < min){
					min = minHash;
				}
			}
			temp.add(min);
		}			
		return temp;
	}
}
