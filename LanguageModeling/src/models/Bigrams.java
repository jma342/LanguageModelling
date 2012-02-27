package models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/* 
 * Notes:
 * I use pre and W as prefix and target word in order as they appear in text, and not the probability notation p(W|pre)
 * Note to Jamaal on topic of hashsets-- I learned while debugging, if define a s
 */
//testing


public class Bigrams {

	 public Map<Pair<String, String>, Pair<Integer, Double>> bigramHT = new HashMap<Pair<String, String>, Pair<Integer, Double>>();
	 public Map<String, HashSet<Pair<String, String>>> prefixHT = new HashMap<String, HashSet<Pair<String, String>>>();
	 
	 //return 0 if bigram is not in the table
	 public int getBgCount(Pair<String, String> bg)
	 {
		 int count;
		 if (bigramHT.containsKey(bg))
		 {
			 count= bigramHT.get(bg).getFirst();
		 }
		 else
		 { 
			 System.out.println(bg.toString() + "has not been seen, no value stored for count");
			 count= 0;
		 }
		 return count;
	 }
	 
	 public double getBgFreq(Pair<String, String> bg)
	 {
		 return bigramHT.get(bg).getSec();
	 }
	 
	 //returns a set of all keys in BG table
	 public Set<Pair<String, String>> getAll()
	 {
		Set<Pair<String, String>> all = new HashSet<Pair<String, String>>();
		all.addAll(bigramHT.keySet());
		return all;
	}



	//if bg not stored, does not update
	public void updateSeen(Pair<String, String> bg) 
	{		
		int count;
		double freq;
		try 
		{
			count = bigramHT.get(bg).getFirst() + 1;
			freq = bigramHT.get(bg).getSec();
			Pair<Integer, Double> bgVal = new Pair<Integer, Double>(count, freq);
			bigramHT.put(bg, bgVal);
		} 
		catch (NullPointerException e) 
		{
			System.out.println("update failed because could not find entry");
		}
	}
	 
	 public void addNew(Pair<String, String> bg)
	 {
		 if (bigramHT.containsKey(bg))
		 {  
			 this.updateSeen(bg);
			 System.out.println("not new, shifting to update");
		 	return;
		 }
		
		 Pair<Integer, Double> bgVal= new Pair<Integer, Double>();
		 bgVal.setFirst((Integer) 1);
		 bgVal.setSec((Double) 0.0);

		 //jma342----feb 25 1:36AM--no need to see every new bigram and its count and frequency
		 /*System.out.println(*/bigramHT.put(bg, bgVal);/*);
		 System.out.println(bigramHT.get(bg).toString());*/
		 this.addToPrfx(bg);
	 }
	
	//merely sets frequency entry for bigram entry, does not calculate. N-grams caculates frequencies
	public void setFreq(Pair<String, String> bg, Double f) 
	{
		int count;
		try 
		{
			count = bigramHT.get(bg).getFirst();
			Pair<Integer, Double> bgVal = new Pair<Integer, Double>(count, f);
			bigramHT.put(bg, bgVal);
		} 
		catch (NullPointerException e) 
		{
			System.out.println("failure in setting frequency, likwlt because value could not be found");
		}

	}
	
	//this table used in random sentence generation and email prediction. Filled in indexText method
	public void addToPrfx(Pair<String, String> bg) 
	{
		HashSet<Pair<String, String>> bgSet;
		if (prefixHT.containsKey(bg.getFirst())) 
		{
			bgSet = prefixHT.get(bg.getFirst());
		} 
		else 
		{
			bgSet = new HashSet<Pair<String, String>>();
		}
		bgSet.add(bg);
		prefixHT.put(bg.getFirst(), bgSet);
	}

	 public Boolean containsBg(Pair<String, String> bg)
	 {
		 return bigramHT.containsKey(bg);
	 }



	 	 
}
