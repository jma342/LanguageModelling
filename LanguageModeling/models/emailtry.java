/*
 * for unkowns, we can update the unigram model to store,
 * we can also store a value in the AuthorMod object, int unks. See AuthMod class. Right now I have both, see comments
 * 
 * (we should report out "novelty inducing words" if any word as prefix has particularly high unkown follow counts)
 * 
 * to do:
 * - handle unkowns
 * - check end conditions on bigrams during prediction
 * - try stemming, both for the training and the prediction
 
 */


package models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.tokenize.SimpleTokenizer;

import org.jsoup.Jsoup;

public class emailtry {
	
	//returns a dictionary of authors with indexed models 
	public static Map<String, AuthorMod> indexEmails(String fileName)
	{
		Map<String, AuthorMod> authBank= new HashMap<String, AuthorMod>();		    
		AuthorMod curAuth;
		//vars for for loop - one loop per email
		String curTok;
		String lastTok;
		
		try 
		{
			Scanner scanner = new Scanner(new FileInputStream(fileName));
		    try 
		    {
		      while (scanner.hasNextLine())
		      {
		   
		    	 String[] emailToks= SimpleTokenizer.INSTANCE.tokenize(scanner.nextLine());
		    	
		    	 lastTok = "<s>";
		    	 curTok = emailToks[3];  //maybe 2, check output
		    	 Pair<String, String> firstBg = new Pair<String, String>(lastTok, curTok);		    	 
		    	 //author check, possible set
		    	 String authID = emailToks[0] + emailToks[1] + emailToks[2];  //check output from the tokenizer	    	
		    	 if (authBank.containsKey(authID)){
		    		 curAuth = authBank.get(authID);
		    		 curAuth.trainSize = curAuth.trainSize + (emailToks.length-2);
		    		 curAuth.ug.updateSeen(lastTok);
		    	 } else {
		    		 curAuth = new AuthorMod();
		    		 curAuth.id = authID;
		    		 curAuth.trainSize = emailToks.length - 2;
			    	 curAuth.ug.addNew(lastTok);
		    	 }
		    	 
		    	 //initalize loop
		    	 if(curAuth.ug.contains(curTok)) {
		    		 curAuth.ug.updateSeen(curTok);
		    	 } 
		    	 else {		    
		    		 curAuth.ug.addNew(curTok);
		    	 }

		    	 if(curAuth.bg.containsBg(firstBg)) {
		    		 curAuth.bg.updateSeen(firstBg);
		    	 } else {
		    		 curAuth.bg.addNew(firstBg);
		    	 }
		    	  	 
		    	 //index for that author, code very similar to bigram/unigram
		    	 for(int i = 4; i < emailToks.length; i++)		    
		 		 {
		 			  curTok= emailToks[i];
		 			  Pair<String, String> loopBg = new Pair<String, String>(lastTok, curTok);
		 			  
		 			  //three cases. 
		 			  //1) bigram has been seen
		 			  if (curAuth.bg.containsBg(loopBg))
		 			  {
		 				  curAuth.bg.updateSeen(loopBg);
		 				  curAuth.ug.updateSeen(curTok);
		 			  } 
		 			  //2) word seen, but not bigram
		 			  else
		 			  { 
		 				  if (curAuth.ug.contains(curTok))
		 			  		{
		 					  curAuth.bg.addNew(loopBg);
		 					  curAuth.ug.updateSeen(curTok);
		 					} 
		 				  //3) new word entirely
		 				  else
		 				  {
		 					  curAuth.ug.addNew(curTok);
		 					  curAuth.bg.addNew(loopBg); 
		 				  } 
		 			  }	  
		 			  lastTok = curTok;
		 			} //end inner loop
		    	authBank.put(curAuth.id, curAuth); 
		 		}  //end outer loop      
		    }	
		    finally
		    {
		      scanner.close();
		    }
		}
		catch(Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		return authBank;
	}
	
	public static void setAuthFreq(Map<String, AuthorMod> models)
	{
		//set unigram HT entries for frequency, calculated by normalzing UG count by number of tokens
		for (AuthorMod curAuth: models.values()) 
		{
			for(String uGram: curAuth.ug.getAll()) 
			{
				int ucount = curAuth.ug.getCount(uGram);
/*>>>>	 		if (ucount<2) {curAuth.unks++;				*** unkown word handling
 				if (curAuth.ug.contains("<unk>"))           
					{  curAuth.ug.updateSeen("<unk>");}
					else{ curAuth.ug.addNew("<unk>");}
*/
				double ufreq= ((double)ucount/curAuth.trainSize);
				curAuth.ug.setFreq(uGram, ufreq);
			}

		//set bigram HT entries for frequency, 
		//calculated by normalizing BG count by prefix count
			for(Pair<String, String> bGram: curAuth.bg.getAll())
			{
/*	>>>				-first, need a test to see if the unigram count for either word in bigram is less than 2. If yes, we will replace with <unk> tag
							- code to get counts for each word in bigram would look like curAuth.ug.getCount(bgram.getFirst()) 
					-make new bg, with the count 1 word replaced with <Unk> tag.  Use the parametered Pair<String, String> constructor with arguements (word, <unk>) depending on which is unk (or both)
					-do an updateSeen(bigram) or addNew(bigram) on curAuth.bg depending on if this new bigram has been seen (use curAuth.bg.contains(**this new bigram**)) 
					-use data structure to remember what still needs updating (some type of set. we want something with no duplicates) 
					-remove original bigram, put updated bigram in set
					-skip rest of loop for this bigram, and update the freq for the set after the loop */
					
					int bcount = curAuth.bg.getBgCount(bGram);
					int pcount = curAuth.ug.getCount(bGram.getFirst());  //sets p count to count of prefix from unigram table
					double bfreq = ((double)bcount)/pcount;
					curAuth.bg.setFreq(bGram, bfreq);
			}
			//set frequncies for items we placed in set of bigrams containing the <unk> tag
		}
	}	
	

	
	public static List<Pair<String, String>> predictAuth(String fileName, int modelid, Map<String, AuthorMod> authModels) {
		List<Pair<String, String>> preds = new LinkedList<Pair<String, String>>();
		try 
		{
			Scanner scanner = new Scanner(new FileInputStream(fileName));
			try 
			{
				while (scanner.hasNextLine()) 
				{  
					String[] inputToks = SimpleTokenizer.INSTANCE.tokenize(scanner.nextLine());
			    	String actAuth = inputToks[0] + inputToks[1] + inputToks[2];  
					PriorityQueue<Pair<String, Double>> scoresQ = new PriorityQueue<Pair<String, Double>>();  //author ID (for print) and calculated score
					
			    	for(AuthorMod curAuth: authModels.values())
					{
			    	  Double score = 0.0;
					
			    	  switch(modelid) 
			    	  {
				    	  case 1:  //unigrams alone
				    		  for(int i = 3; i < inputToks.length; i++) {
				    			  String curTok= inputToks[i];
				    			  double freq= curAuth.ug.getFreq(curTok);
				    			  score += Math.log(freq);   //natural log
				    		  }
				    		  break;
				    	  case 2:  //bigrams alone
				    		  //initialize loop
				    		  String lastTok = "<s>";
				    		  String curTok = inputToks[3];
				    		  double firstFreq = curAuth.bg.getBgFreq(new Pair<String, String>(lastTok, curTok));
				    		  score = Math.log(firstFreq);
				    		  for(int i = 4; i < inputToks.length; i++) {  //**check end conditions on this loop
				    			  curTok = inputToks[i];
				    			  Double bgVal = curAuth.bg.getBgFreq(new Pair<String, String>(lastTok, curTok));
				    			  score += Math.log(bgVal);
				    			  lastTok = curTok;
				    		  }
				    		  break;
				    	  case 3:  //trigram alone
				    		  for(int i = 3; i < inputToks.length; i++) {
				    			  //TODO: implement alternate models.  Loops are placeholders
				    		  }
				    		  break;
				    	  case 4:  //another case of some kind... interpolation? 
				    		  for(int i = 3; i < inputToks.length; i++) {
				    			  //TODO: implement alternate models. Loops are just placeholders
				    		  }
				    		  break;
			    	  }	//ends 1 instance of reading input, repeated for each author	
			    	  scoresQ.add(new Pair<String, Double>(curAuth.id, score));
					} //end of loop through all authhors
		      	String predAuth = scoresQ.peek().getFirst();
		      	Pair<String, String> actVpredPair = new Pair<String, String>(actAuth, predAuth);
		      	preds.add(actVpredPair);
				}//end of loop that runs through each email
			} finally
			{
		      scanner.close();
		    }
		}
		catch(Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		return preds;  //return a list built up of predictions vs. actual
	}
	
	public static void main(String[] args)
	{
		//for debugging the indexing and frequency
		Map<String, AuthorMod> map = indexEmails("email_small.txt");
		setAuthFreq(map);   //currently using a static variable to test-- this makes me nervous
		
		for(AuthorMod auth: map.values()) {
			
			System.out.println(auth.id);
			
			for(Pair<String, String> bGram: auth.bg.getAll()) {
				System.out.println(bGram.toString() + " freq: " + auth.bg.getBgFreq(bGram));
			}
			System.out.println("unigrams");
			
			double ugFSum= 0;
			
			for(String uGram :auth.ug.getAll()){
				System.out.println(uGram.toString() + " count: " + auth.ug.getCount(uGram) + "and freq: " + auth.ug.unigramHT.get(uGram).getSec());
				ugFSum= ugFSum+ auth.ug.unigramHT.get(uGram).getSec();
			}
			System.out.println("Sum of probs of UGs: " + ugFSum);
		}
		
		Bigrams rick= map.get("rick-e").bg;
		Pair<String, String> rickbG = new Pair<String, String>("<s>", "conservative");
		System.out.println(rick.getBgCount(rickbG) + " and freq: " + rick.getBgFreq(rickbG) + " ug count for <s>: " + map.get("slicks-h").ug.getCount("<s>"));
	}

	
}

	
	