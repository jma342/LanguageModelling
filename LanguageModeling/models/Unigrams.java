package models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Unigrams {
	
	//publioc for debugging purposes, swithc back to private
	public Map<String, Pair<Integer, Double>> unigramHT = new HashMap<String, Pair<Integer, Double>>();
	
	/**
	 * @param args
	 */
	//yb85 - Feb 27th added to faciliate smoothing
	Unigrams() {
	}
	
	Unigrams(Unigrams ugs) {
		for (String uGram: ugs.getAll())
			if (!this.contains(uGram)) {
				this.addNew(uGram);
				this.setCount(uGram, ugs.getCount(uGram));
				this.setFreq(uGram, ugs.getFreq(uGram));
			}
	}
	//yb85 - Feb 27th added to faciliate smoothing
	
	public boolean contains(String w) 
	{
		return unigramHT.containsKey(w);
	}
	 
	public int getCount(String w)
	{
		if(unigramHT.containsKey(w)) 
		{ 
			int count = unigramHT.get(w).getFirst();
			return count;
		}
		 
		System.out.println("***strange, :" + w + ": doesn't have a count");
		return 1;
	}
	
	public double getFreq(String w)
	{
		if(unigramHT.containsKey(w)) 
		{ 
			double freq = unigramHT.get(w).getSec();
			return freq;
		}
		 
		System.out.println("***strange, :" + w + ": doesn't have a count");
		return 0;
	}
	 
	public Set<String> getAll() 
	{
		Set<String> all = new HashSet<String>();
		all.addAll(unigramHT.keySet());
		return all;
	}
	 
	public void addNew(String w)
	{
		Pair<Integer, Double> ugVal = new Pair<Integer, Double>(1, 0.0);
		unigramHT.put(w, ugVal);
	}
	 
	public void updateSeen(String w)
	{
		Pair<Integer, Double> ugVal;
		int count;
		double freq;
		 
		try 
		{
			count = unigramHT.get(w).getFirst() + 1;
			freq = unigramHT.get(w).getSec();
		}
		catch (NullPointerException e) 
		{
			System.out.println("No count values stored for " + w);
			count = 1;
			freq = 0;
		}
		 
		ugVal = new Pair<Integer, Double>(count, freq);
		unigramHT.put(w, ugVal);
	 }
	 
	public void setFreq(String w, double fre)
	{
		Pair<Integer, Double> ugVal;
		int count;
		 
		try 
		{
			count= unigramHT.get(w).getFirst();
		}
		catch (NullPointerException e) 
		{
			System.out.println("****Error for " + w + " in freq setting");
			count = 1;
		}
		ugVal = new Pair<Integer, Double>(count, fre);
		unigramHT.put(w, ugVal);
	 }
	 
	//yb85 - Feb 27th added to faciliate smoothing
	private void setCount(String w, int count) {
		Pair<Integer, Double> ugVal;
		double freq;
		
		try {
			freq = unigramHT.get(w).getSec();
		}
		catch (NullPointerException e) {
			System.out.println("****Error for " + w + " in count setting");
			freq = 0;
		}
		ugVal = new Pair<Integer, Double>(count, freq);
		unigramHT.put(w, ugVal);
	}
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
	}
}
