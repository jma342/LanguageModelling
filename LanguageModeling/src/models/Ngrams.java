/*
 * Notes: 
 *   - I defined my own data structure pair for our hash tables.
 *     It operates as an ordered pair, and you access the elements in my_pair by my_pair.getFirst() and my_pair.getSecond()
 *  
 *   - We need to make sure we handle beggining tokens in the random generate task
 *   
 */

package models;

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
			/*  if (cur.equals(".")){
				  turn into a switch case
			  }*/
			  
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
	

	/*public static Bigrams smoothBg(Bigrams bgs, Unigrams ugs) {
		//TODO: Good-Turing smoothing method
		//vector<int> countUGram = new Vector<int>;

		int[] countUGram = new int[ugs.size()];
		int[] countBGram = new int[bgs.size()];
		
		Arrays.fill(countUGram, 0);
		Arrays.fill(countBGram, 0);
		
		for (String uGram: ugs.getAll()) {
			int idx = ugs.getCount(uGram);
			countUGram[idx]++;
		}
		
		for (Pair<String, String> bGram: bgs.getAll()) {
			int idx = bgs.getBgCount(bGram.getFirst(), bGram.getSec());
			countBGram[idx]++;
		}
		
		System.out.println("UGram Count:");
		for (int i = 0; i < countUGram.length; i++)
			System.out.println(i + ": " + countUGram[i]);
		
		System.out.println("BGram Count:");
		for (int i = 0; i < countBGram.length; i++)
			System.out.println(i + ": " + countBGram[i]);
		
		
	}*/
	
	//tricky question of inhereitence here-- what class for the arguement? model class?
	public static void findPerplexity(String[] testToks) {
		//TODO: Perplexity implementation
	}
	
	public static void emailPrediction() {
		//TODO: E-mail Prediction
	}
	
	public static void main(String[] args) {

		Tokenizer tok= new Tokenizer();
		Vector<String> testV= tok.tokenize("wsj.train");
		Bigrams bgs = new Bigrams();
		Unigrams ugs= new Unigrams();
		
		Ngrams.indexText(testV, bgs, ugs);
		Ngrams.setFreqs(testV.size(), bgs, ugs);
		
		for(Pair<String, String> bGram: bgs.bigramHT.keySet()){
			System.out.println("for " + bGram.toString() + " count: " + bgs.getBgCount(bGram) + " freq is: " + bgs.getBgfreq(bGram) );
			
		}

		
	}

}
