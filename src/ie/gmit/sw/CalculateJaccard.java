package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class CalculateJaccard {
	private List<Document> listDocuments = new ArrayList<Document>();
	private Document document;
	private Set<Integer> hashes = new TreeSet<Integer>();
	
	// Two Array Lists: One for the old documents and one for the new documents
	private List<Integer> oldDoc = new ArrayList<Integer>();
	private List<Integer> newDoc = new ArrayList<Integer>();
	
	//private final static int MAX_HASHES = 200;
	private double jaccard;
	private List<Integer> common = new ArrayList<Integer>();
	private List<Results> results = new ArrayList<Results>();
	private Results r;
	
	public CalculateJaccard(List<Document> listDocuments, Document document) {
		this.listDocuments = listDocuments;
		this.document = document;
	}
	
	public Set<Integer> generateHashes(){
		
		Random random = new Random();
		for(int i = 0; i < Global.getMaxHashes(); i++){
			hashes.add(random.nextInt());
		}		
		return hashes;			
	}
	
	public List<Results> calculateJaccard(){
		
		hashes = generateHashes();
		
		for(Document doc : listDocuments){
			newDoc = generateMinHashes(document);
			oldDoc = generateMinHashes(doc);

			// Calculate Jaccard Index
			
			common.addAll(newDoc);
			
			common.retainAll(oldDoc);
					
			jaccard = ((double)common.size()) / newDoc.size();
			r = new Results(document.getName(), doc.getName(), jaccard);
			results.add(r);
			System.out.println("Jaccard Index: " + jaccard);			
		}
		
		return results;
	}
	
	public List<Integer> generateMinHashes(Document d){
		List<Integer> temp = new ArrayList<Integer>();
		List<Shingle> shingles = new ArrayList<Shingle>();
		
		shingles = d.getShingles();
		
		for(Integer hash : hashes){
			int min = Integer.MAX_VALUE;
			
			for(int i = 0; i < shingles.size(); i++){
				int minHash = shingles.get(i).getHashCode() ^ hash;
				if(minHash < min){
					min = minHash;
				}
			}
			temp.add(min);
		}			
		return temp;
	}
}
