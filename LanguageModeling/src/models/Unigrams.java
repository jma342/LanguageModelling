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
			double freq= unigramHT.get(w).getFirst();
			return freq;
		}
		 
		System.out.println("***strange, :" + w + ": doesn't have a freq");
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
	 
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
	}
}
