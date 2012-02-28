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


public class Trigrams {

	 public Map<Triple<String, String,String>, Pair<Integer, Double>> trigramHT = new HashMap<Triple<String, String,String>, Pair<Integer, Double>>();
	 public Map<Pair<String,String>, HashSet<Triple<String, String,String>>> prefixHT = new HashMap<Pair<String,String>, HashSet<Triple<String, String,String>>>();
	 
	 //return 0 if trigram is not in the table
	 public int getTgCount(Triple<String, String,String> tg)
	 {
		 int count;
		 if (trigramHT.containsKey(tg))
		 {
			 count= trigramHT.get(tg).getFirst();
		 }
		 else
		 { 
			 System.out.println(tg.toString() + "has not been seen, no value stored for count");
			 count= 0;
		 }
		 return count;
	 }
	 
	 public double getBgFreq(Triple<String, String,String> tg)
	 {
		 return trigramHT.get(tg).getSec();
	 }
	 
	 //returns a set of all keys in BG table
	 public Set<Triple<String, String,String>> getAll()
	 {
		Set<Triple<String, String,String>> all = new HashSet<Triple<String, String,String>>();
		all.addAll(trigramHT.keySet());
		return all;
	}

	//if tg not stored, does not update
	public void updateSeen(Triple<String, String, String> tg) 
	{		
		int count;
		double freq;
		try 
		{
			count = trigramHT.get(tg).getFirst() + 1;
			freq = trigramHT.get(tg).getSec();
			Pair<Integer, Double> tgVal = new Pair<Integer, Double>(count, freq);
			trigramHT.put(tg, tgVal);
		} 
		catch (NullPointerException e) 
		{
			System.out.println("update failed because could not find entry");
		}
	}
	 
	 public void addNew(Triple<String, String,String> tg)
	 {
		 if (trigramHT.containsKey(tg))
		 {  
			 this.updateSeen(tg);
			 System.out.println("not new, shifting to update");
		 	return;
		 }
		
		 Pair<Integer, Double> tgVal= new Pair<Integer, Double>();
		 tgVal.setFirst((Integer) 1);
		 tgVal.setSec((Double) 0.0);

		 //jma342----feb 25 1:36AM--no need to see every new trigram and its count and frequency
		 /*System.out.println(*/trigramHT.put(tg, tgVal);/*);
		 System.out.println(trigramHT.get(bg).toString());*/
		 this.addToPrfx(tg);
	 }
	
	//merely sets frequency entry for trigram entry, does not calculate. N-grams caculates frequencies
	public void setFreq(Triple<String, String,String> tg, Double f) 
	{
		int count;
		try 
		{
			count = trigramHT.get(tg).getFirst();
			Pair<Integer, Double> bgVal = new Pair<Integer, Double>(count, f);
			trigramHT.put(tg, bgVal);
		} 
		catch (NullPointerException e) 
		{
			System.out.println("failure in setting frequency, likwlt because value could not be found");
		}

	}
	
	//this table used in random sentence generation and email prediction. Filled in indexText method
	public void addToPrfx(Triple<String, String,String> tg) 
	{
		HashSet<Triple<String, String,String>> tgSet;
		
		Pair<String,String> tgPrefix  = new Pair<String,String>(tg.getFirst(),tg.getSec());
		
		if (prefixHT.containsKey(tgPrefix)) 
		{
			tgSet = prefixHT.get(tgPrefix);
		} 
		else 
		{
			tgSet = new HashSet<Triple<String, String,String>>();
		}
		
		tgSet.add(tg);
		prefixHT.put(tgPrefix, tgSet);
	}

	 public Boolean containstg(Triple<String, String,String> tg)
	 {
		 return trigramHT.containsKey(tg);
	 }



	 	 
}
