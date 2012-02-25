/*
 * Notes: 
 *   - I defined my own data structure pair for our hash tables.
 *     It operates as an ordered pair, and you access the elements in my_pair by my_pair.getFirst() and my_pair.getSecond()
 *  
 *   - We need to make sure we handle beggining tokens in the random generate task
 *   
 */

package models;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Ngrams {
	/**
	 * @param args
	 */
	
	//pass in instances of th bigram and unigram
	public static void BuildModels(String[] toks, Bigrams bg, Unigrams ug){
	}
	//help
	//helper function called by "build models"
	public static void indexBg(String[] toks, Bigrams bg, Unigrams ug)
	{
		int tlength = toks.length;
		String cur = toks[0];
		String last = "<s>";   //fix when we figure out start tokens once we set tokenized array
		
		ug.addNew(cur);
		ug.addNew(last);
		bg.addNew(last, cur);
		
		last = cur;
		
		for (int i = 1; i < tlength; i++) 
		{
			cur = toks[i];
				
			//three cases. 1) bigram has been seen
			if (bg.containsBg(last, cur)) 
			{
				bg.updateSeen(last, cur);
				ug.updateSeen(cur);
			}
			else {
				// 2) word seen, but not bigram
				if (ug.contains(cur)) 
				{
					bg.addNew(last, cur);
					ug.updateSeen(cur);
				}
				// 3) new word entirely
				else 
				{
					ug.addNew(cur);
					bg.addNew(last, cur);
				}
			}
			last = cur;
		} // end loop
	}
	
	public static void setFreqs(int tokSize, Bigrams bgs, Unigrams ugs) {
		//set unigram HT entries for frequency, calculated by normalzing UG count by number of tokens
		for (String uGram: ugs.getAll()) {
			int ucount = ugs.getCount(uGram);
			double ufreq = ((double)ucount/tokSize);
			ugs.setFreq(uGram, ufreq);
		}
		
		//test print
		/*for(Pair<String, String> bGram: bgs.getAll()) {
			System.out.println(bGram.toString());
		}*/
		
		//set bigram HT entries for frequency, calculated by normalizing BG count by prefix count
		for(Pair<String, String> bGram: bgs.getAll()) {
			int bcount = bgs.getBgCount(bGram.getFirst(), bGram.getSec());
			int pcount = ugs.getCount(bGram.getFirst()); //sets p count to count of prefix from unigram table
			
//			System.out.println("bigram count for "+ bGram.toString() + " is: "+ bcount+ ". And prefix, \""+ bGram.getFirst() + "\", count is: " + pcount);
			
			double bfreq = ((double)bcount/(double)pcount);
			bgs.setFreq(bGram.getFirst(), bGram.getSec(), bfreq);
		}	
	}
	
	public static void randomSentence(Bigrams bgs, Unigrams ugs) {
		//TODO: Random sentence generator
	}
	
	// Good-Turing smoothing method for unigrams
	public static void smooth(Unigrams ugs) {
		
		HashMap<Integer, Double> unigramCount = new HashMap<Integer, Double>();
		
		double totalCount = 0; 
		
		for (String uGram: ugs.getAll())
			totalCount += ugs.getCount(uGram);
		
		for (String uGram: ugs.getAll()) {
			int count = ugs.getCount(uGram);
			
			if (!unigramCount.containsKey(count)) {
				unigramCount.put(new Integer(count), new Double(1));
			}
			else {
				Double newCount = unigramCount.get(new Integer(count));
				unigramCount.put(new Integer(count), newCount + 1);
			}
		}
		
		Double k = new Double(1 / totalCount);
		
		for (String uGram: ugs.getAll()) {
			int count = ugs.getCount(uGram);
			
			Double Pgt = k * unigramCount.get(new Integer(count));
			ugs.setFreq(uGram, Pgt);
		}
		
	}

	public static void smooth(Bigrams ugs) {
		//TODO: Good-Turing Smoothing method for bigrams
	}

	//tricky question of inhereitence here-- what class for the arguement? model class?
	public static void findPerplexity(String[] testToks) {
		//TODO: Perplexity implementation
	}
	
	public static void emailPrediction() {
		//TODO: E-mail Prediction
	}
	
	public static void main(String[] args) {
		//toks = input.tokenize();
		String[] toks= { "the", "fat", "friend", "ate", "a", "large", "meal", "and", 
				"so", "large", "a",	"he", "smacked", "his", "fat", "friend", "ate", "and", "so"};
		
		Bigrams bgs = new Bigrams();
		Unigrams ugs = new Unigrams();
		
		int tokSize= toks.length;
		
		System.out.println("Tokens in main() method are: " + tokSize);

		indexBg(toks, bgs, ugs);
		
		/*for(String p: ugs.unigramHT.keySet()) {
			System.out.print(p);
			System.out.println(ugs.getCount(p));
		}
		
		setFreqs(tokSize, bgs, ugs);
		System.out.println("*** Unigrams:");
		
		for(Pair<Integer, Double> p: ugs.unigramHT.values()) {
			System.out.println(p.getFirst()+ " , freq: "+ p.getSec());
		}
		
		System.out.println("*** Bigrams:");
		
		for(Pair<Integer, Double> p: bgs.bigramHT.values()) {
			System.out.println(p.getFirst()+ " , freq: "+ p.getSec());
		}*/
		
		smoothBg(bgs, ugs);
	}

}
