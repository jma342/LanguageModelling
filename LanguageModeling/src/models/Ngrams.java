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

	//pass in instances of the bigram and unigram
	public static void BuildModels(String[] toks, Bigrams bg, Unigrams ug)
	{
	}

	/*
	 * takes a an array of strings, and empty instances of Bigrams and Unigrams.
	 * Returns the two Bigrams and Unigrams objects with bigrams and unigrams counted, 
	 *     but frequencies set to 0
	 */
	public static void indexText(Vector<String> toks, Bigrams bg, Unigrams ug){
		int tlength= toks.size();
		String cur= toks.elementAt(0);
		String last= "<s>";   //fix when we figure out start tokens once we set tokenized array
		ug.addNew(cur);
		ug.addNew(last);
		Pair<String, String> firstBg= new Pair<String, String>(last, cur);
		bg.addNew(firstBg);
		last= cur;
		for (int i= 1; i<tlength; i++){
			  cur= toks.elementAt(i);
			  Pair<String, String> loopBg= new Pair<String, String>(last, cur);
			  //three cases. 1) bigram has been seen
			  if (bg.containsBg(loopBg)){
				  bg.updateSeen(loopBg);
				  ug.updateSeen(cur);} 
			  //2) word seen, but not bigram
			  else{ if (ug.contains(cur)){
				  bg.addNew(loopBg);
				  ug.updateSeen(cur);} 
			  //3) new word entirely
			  else{ug.addNew(cur);
				   bg.addNew(loopBg); } 
			     }
			  last= cur;

		} // end loop
	}

	/*
	 * -recquires that instances of Bigrams and Unigrams were filled using indexText method
	 * -sets the frequency field in HT entries
	 */
	public static void setFreqs(int tokSize, Bigrams bgs, Unigrams ugs){
		//set unigram HT entries for frequency, calculated by normalzing UG count by number of tokens
		for(String uGram: ugs.getAll()){
			int ucount= ugs.getCount(uGram);
			double ufreq= ((double)ucount/tokSize);
			ugs.setFreq(uGram, ufreq);
		}

		//set bigram HT entries for frequency, 
		//calculated by normalizing BG count by prefix count
		for(Pair<String, String> bGram: bgs.getAll()){
			int bcount= bgs.getBgCount(bGram);
			int pcount= ugs.getCount(bGram.getFirst());  //sets p count to count of prefix from unigram table
			double bfreq= ((double)bcount)/pcount;
			bgs.setFreq(bGram, bfreq);
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
		Vector<String> toksV= new Vector<String>();
		String[] toks= {
				"the", "fat", "the", "fat", "friend", "ate",
				"twice", "and", "ate", "twice", "and", "ate", "twice"};
		
		for(String s: toks){
		toksV.add(s);	
		}
		
		Bigrams bgs= new Bigrams();
		Unigrams ugs= new Unigrams();
		int tokSize= toksV.size();
		System.out.println("toks in main method are: " + tokSize);

		indexText(toksV, bgs, ugs);
	for(String p: ugs.unigramHT.keySet()){
		System.out.print(p);
		System.out.println(ugs.getCount(p));

	}
		setFreqs(tokSize, bgs, ugs);
		System.out.println("unigrams");
		for(Pair<Integer, Double> p: ugs.unigramHT.values()){
			System.out.println(p.getFirst()+ " , freq: "+ p.getSec());
		}		
			System.out.println("bigrams");
			for(Pair<String, String> bg: bgs.getAll()){
			System.out.println("for " + bg.toString() + "count: " + bgs.getBgCount(bg) + " and freq: " + bgs.getBgfreq(bg));


			}
	for (String prefix: bgs.prefixHT.keySet()){
		System.out.print("bigram set for prefix: "+ prefix + " is:");
		for (Pair<String, String> bg: bgs.prefixHT.get(prefix)){
			System.out.println(bg.toString());

		}
	}


	}

}