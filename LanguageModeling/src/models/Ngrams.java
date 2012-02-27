/*
 * Notes: 
 *   - I defined my own data structure pair for our hash tables.
 *     It operates as an ordered pair, and you access the elements in my_pair by my_pair.getFirst() and my_pair.getSecond()
 *  
 *   - We need to make sure we handle beggining tokens in the random generate task
 *   
 */

package models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
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
	public static void indexText(Vector<String> toks, Bigrams bg, Unigrams ug)
	{
		int tlength= toks.size();
		String cur= toks.elementAt(0);
		String last= "<s>";   //fix when we figure out start tokens once we set tokenized array
		ug.addNew(cur);
		ug.addNew(last);
		Pair<String, String> firstBg= new Pair<String, String>(last, cur);
		bg.addNew(firstBg);
		last= cur;
		for (int i= 1; i<tlength; i++)
		{
			cur= toks.elementAt(i);
			
			if (!cur.equals("~"))  //everything else in this loop skips ~, including last element  
			{ 
				//handling of punctuation, creating 3 distinct end sentence tags.  Needs to be incorporated into random sentence generator 
				if(cur.equals(".") || cur.equals("?") || cur.equals("!") )
				{ if (toks.elementAt(i).equals("~"))
					{cur= "</" + cur + "s>";}
				}
				
				  
			  Pair<String, String> loopBg= new Pair<String, String>(last, cur);
			  //three cases. 1) bigram has been seen
			  if (bg.containsBg(loopBg))
			  {
				  bg.updateSeen(loopBg);
				  ug.updateSeen(cur);
			  } 
			  //2) word seen, but not bigram
			  else
			  { 
				  if (ug.contains(cur))
			  		{
					  bg.addNew(loopBg);
					  ug.updateSeen(cur);
					} 
			  //3) new word entirely
				  else
				  {
					  ug.addNew(cur);
					  bg.addNew(loopBg); 
				  } 
			  }
			  
			  last= cur;
			}
		} // end loop
	}

	/*
	 * -recquires that instances of Bigrams and Unigrams were filled using indexText method
	 * -sets the frequency field in HT entries
	 */
	public static void setFreqs(int tokSize, Bigrams bgs, Unigrams ugs)
	{
		//set unigram HT entries for frequency, calculated by normalzing UG count by number of tokens
		for(String uGram: ugs.getAll())
		{
			int ucount= ugs.getCount(uGram);
			double ufreq= ((double)ucount/tokSize);
			ugs.setFreq(uGram, ufreq);
		}

		//set bigram HT entries for frequency, 
		//calculated by normalizing BG count by prefix count
		for(Pair<String, String> bGram: bgs.getAll())
		{
			int bcount= bgs.getBgCount(bGram);
			int pcount= ugs.getCount(bGram.getFirst());  //sets p count to count of prefix from unigram table
			double bfreq= ((double)bcount)/pcount;
			bgs.setFreq(bGram, bfreq);
		}	
	}

	//jma342 - Feb 25th 3:30 AM
	public static void randomSentence(Bigrams bgs, Unigrams ugs,int corpusSize) 
	{
		Random generator = new Random();
		
		Vector<String> randomSentence = new Vector<String>();
		
		int randomNumber = generator.nextInt(corpusSize) + 1;
		int rangeUpperBound = 0;//holds the count value of the last word
		
		String keyChosen = "";
		
		//loops through the unigram and generaters random sentence until
		//'.','!','?' is reached
		do
		{
			
			keyChosen = "";
			
			for(String key: ugs.unigramHT.keySet())
			{
				//jma342- feb 25 3:35AM -- added for debugging purposes
				/*System.out.println("Random number:" + randomNumber
						+ "Key: " + key + " RangeUpperBound: " + 
						(ugs.unigramHT.get(key).getFirst() + rangeUpperBound));
*/				
				//jma342- feb 25 3:35AM -- added to pause screen
				/*try {
					int a = System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//the count value of the last word is added to the count value
				//of the current word to form the upperbound of the current word
				if(randomNumber <= (ugs.unigramHT.get(key).getFirst() + rangeUpperBound))
				{
					//if key is found based on the random number
					//being less than its upper found
					//the search ends and a new random number is generated
					//for a new search
					keyChosen = key;
					randomSentence.add(key);
					break;
				}
				
				//the count value of the current word is set here as it
				//will be seen as the last word for the next iteration of the loop
				rangeUpperBound += ugs.unigramHT.get(key).getFirst();
				
			}
			
			
			rangeUpperBound = 0;
			randomNumber = generator.nextInt(corpusSize) + 1;
			
		} while(!keyChosen.equals(".") && !keyChosen.equals("!") && !keyChosen.equals("?"));
		
		for(int count = 0; count < randomSentence.size();count++)
		{
			System.out.print(randomSentence.elementAt(count) + " ");
		}
		
	}

	// Good-Turing smoothing method for unigrams
	public static void smooth(Unigrams ugs) 
	{

		HashMap<Double, Double> unigramCount = new HashMap<Double, Double>();

		double totalCount = 0; 

		for (String uGram: ugs.getAll())
			totalCount += ugs.getCount(uGram);

		for (String uGram: ugs.getAll()) 
		{
			double count = ugs.getCount(uGram);

			if (!unigramCount.containsKey(count)) 
			{
				unigramCount.put(new Double(count), new Double(1));
			}
			else 
			{
				Double newCount = unigramCount.get(new Double(count));
				unigramCount.put(new Double(count), newCount + 1);
			}
		}

		Double k = new Double(1/totalCount);

		for (String uGram: ugs.getAll()) 
		{
			double count = ugs.getCount(uGram);

			if (!unigramCount.containsKey(new Double(count + 1)))
				continue;
			
			Double Pgt = k * (count + 1) * unigramCount.get(new Double(count + 1)) / unigramCount.get(new Double(count));
			ugs.setFreq(uGram, Pgt);
		}
	
		try {
			FileWriter fstream = new FileWriter("smooth-ug.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			System.out.println("***Good-Turing smoothing for unigrams started***");
			for (String uGram: ugs.getAll()) 
				out.write("{" + uGram + "}:"+ ugs.getFreq(uGram) + "\n");
			System.out.println("***Good-Turing smoothing for unigrams completed***");
			out.close();
		} 
		catch (Exception e) {
			 System.err.println("Smooth Unigram Error: " + e.getMessage());
		}
	}

	public static void smooth(Bigrams bgs) 
	{
		//TODO: Good-Turing Smoothing method for bigrams
		HashMap<Double, Double> bigramCount = new HashMap<Double, Double>();
		
		double totalCount = 0; 

		for (Pair<String, String> bGram: bgs.getAll())
			totalCount += bgs.getBgCount(bGram);

		for (Pair<String, String> bGram: bgs.getAll()) 
		{
			double count = bgs.getBgCount(bGram);

			if (!bigramCount.containsKey(count)) 
			{
				bigramCount.put(new Double(count), new Double(1));
			}
			else 
			{
				Double newCount = bigramCount.get(new Double(count));
				bigramCount.put(new Double(count), newCount + 1);
			}
		}

		Double k = new Double(1/totalCount);

		for (Pair<String, String> bGram: bgs.getAll())
		{
			double count = bgs.getBgCount(bGram);

			if (!bigramCount.containsKey(new Double(count + 1)))
				continue;
			
			Double Pgt = k * (count + 1) * bigramCount.get(new Double(count + 1)) / bigramCount.get(new Double(count));
			bgs.setFreq(bGram, Pgt);
		}
	
		try {
			FileWriter fstream = new FileWriter("smooth-bg.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			System.out.println("***Good-Turing smoothing for bigrams started***");
			for (Pair<String, String> bGram: bgs.getAll())
				out.write("{" + bGram.getFirst() + "," + bGram.getSec() + "}:" + bgs.getBgfreq(bGram) + "\n");
			System.out.println("***Good-Turing smoothing for bigrams completed***");
			out.close();
		} 
		catch (Exception e) {
			 System.err.println("Smooth Bigram Error: " + e.getMessage());
		}
	}

	//tricky question of inhereitence here-- what class for the arguement? model class?
	public static void findPerplexity(String[] testToks) 
	{
		//TODO: Perplexity implementation
	}

	public static void emailPrediction() 
	{
		//TODO: E-mail Prediction
	}

	public static void main(String[] args) 
	{
		//toks = input.tokenize();
		Tokenizer tokens = new Tokenizer();
		Vector<String> toksV= new Vector<String>();
		
		toksV = tokens.tokenize("TrainingData\\DS3_train.txt");
		
		String[] toks= 
		{
				"the", "fat", "the", "fat", "friend", "ate",
				"twice", "and", "ate", "twice", "and", "ate", "twice"
		};
		
		for(String s: toks)
		{
			toksV.add(s);	
		}
		
		Bigrams bgs= new Bigrams();
		Unigrams ugs= new Unigrams();
		int tokSize= toksV.size();
		System.out.println("toks in main method are: " + tokSize);
		
	
		indexText(toksV, bgs, ugs);
		
		for(String p: ugs.unigramHT.keySet())
		{
			System.out.print(p);
			System.out.println(ugs.getCount(p));
	
		}
		
		setFreqs(tokSize, bgs, ugs);
		System.out.println("unigrams");
		

		for(Pair<Integer, Double> p: ugs.unigramHT.values())
		{
			System.out.println(p.getFirst()+ " , freq: "+ p.getSec());
		}		
		

		System.out.println("bigrams");
		for(Pair<String, String> bg: bgs.getAll())
		{
			System.out.println("for " + bg.toString() + "count: " + bgs.getBgCount(bg) + " and freq: " + bgs.getBgfreq(bg));
		}
		
		for (String prefix: bgs.prefixHT.keySet())
		{
			System.out.print("bigram set for prefix: "+ prefix + " is:");
			for (Pair<String, String> bg: bgs.prefixHT.get(prefix))
			{
				System.out.println(bg.toString());
			}
		}
		smooth(ugs);
		smooth(bgs);
	}
	
/*	//jma342 - Feb25th 2:00am -- jamaal's main for debuggin while building random sentence generator
	public static void main(String[] args) 
	{
		Tokenizer tokens = new Tokenizer();
		Vector<String> toksV= new Vector<String>();
		
		toksV = tokens.tokenize("TrainingData\\DS3_train.txt");
		
		
		Bigrams bgs= new Bigrams();
		Unigrams ugs= new Unigrams();
		int tokSize= toksV.size();
		System.out.println("toks in main method are: " + tokSize);

		int a = 0;
		
		indexText(toksV, bgs, ugs);
		
		//jma342 - feb 25 1:40AM -- no need to see unigram words and counts
		for(String p: ugs.unigramHT.keySet())
		{
			System.out.print(p);
			System.out.println(ugs.getCount(p));
	
		}
		
		setFreqs(tokSize, bgs, ugs);
		System.out.println("unigrams");
		
		//jma342 - feb 25 1:40AM -- removed for the for loop subsequent
		for(Pair<Integer, Double> p: ugs.unigramHT.values())
		{
			System.out.println(p.getFirst()+ " , freq: "+ p.getSec());
		}		
		
		int iterator = 0;
		int overallCount = 0;
		for(String p: ugs.unigramHT.keySet())
		{
			System.out.print(iterator++ + ". " + p);
			System.out.println(" Count " + ugs.unigramHT.get(p).getFirst() + ", Freq: " + String.format("%.2g%n", ugs.unigramHT.get(p).getSec()));
			overallCount +=ugs.unigramHT.get(p).getFirst();
	
		}
		
		//jma342 -- feb 25th 2:00AM  -- ensuring that token counts total up to number of tokens
		System.out.println(overallCount);
		
		randomSentence(bgs,ugs,tokSize);
		
		//jma342- feb 25 1:40AM -- added to pause screen
		try {
			a = System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("bigrams");
		for(Pair<String, String> bg: bgs.getAll())
		{
			System.out.println("for " + bg.toString() + "count: " + bgs.getBgCount(bg) + " and freq: " + bgs.getBgfreq(bg));
		}
		
		for (String prefix: bgs.prefixHT.keySet())
		{
			System.out.print("bigram set for prefix: "+ prefix + " is:");
			for (Pair<String, String> bg: bgs.prefixHT.get(prefix))
			{
				System.out.println(bg.toString());
			}
		}
	}
*/

}
