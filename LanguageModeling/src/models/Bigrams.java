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



public class Bigrams {

	 public Map<Pair<String, String>, Pair<Integer, Double>> bigramHT = new HashMap<Pair<String, String>, Pair<Integer, Double>>();
	 public Map<String, HashSet<Pair<String, String>>> prefixHT = new HashMap<String, HashSet<Pair<String, String>>>();
	 public Pair<String, String> refP= new Pair<String, String>();
	 
	 public int getBgCount(String pre, String w) {
		 Pair<String, String> bgkey = new Pair<String, String>(pre, w);
		 
		 int count = 0;
		 
		 if (bigramHT.containsKey(bgkey)){
			 count = bigramHT.get(bgkey).getFirst();
			 //System.out.println("+++count for: " + bgkey.toString() + " is " + count);
		 }
		 else { 
			 //System.out.println("*** Strange, no count reported for: " + bgkey.toString());
			 count = 1;
		 }
		 return count;
	 }
	 
	 public double getBgfreq(String pre, String w) {
		 Pair<String, String> bgkey = new Pair<String, String>( pre, w);
		 return bigramHT.get(bgkey).getSec();
	 }
	 
	 //returns a set of all keys in BG table
	 public Set<Pair<String, String>> getAll() {
		 Set<Pair<String, String>> all = new HashSet<Pair<String, String>>();
		 all.addAll(bigramHT.keySet());
		 return all; 
	 }
	
	 public void updateSeen(String pre, String w) {
		 Pair<String, String> bgkey = new Pair<String, String>( pre, w);
		 int count;
		 double freq;
		 
		 try{ 
			 count = bigramHT.get(bgkey).getFirst() + 1;
			 freq = bigramHT.get(bgkey).getSec();
		 }
		 catch (NullPointerException e) {
			 //System.out.println("values not set for (" + pre + "," + w + ")");
			 count = 1;
			 freq = 0.0;
		 }
		 
		 Pair<Integer, Double> bgVal= new Pair<Integer, Double>(count, freq);
		 bigramHT.put(bgkey, bgVal);			 
	 }
	 
	 public void addNew(String pre, String w) {
		 Pair<String, String> bgkey = new Pair<String, String>( pre, w);
		 
		 if (bigramHT.containsKey(bgkey)){  
			 this.updateSeen(pre, w);
			 return;
		 }
		 
		 Pair<Integer, Double> bgVal = new Pair<Integer, Double>();
		 bgVal.setFirst((Integer) 1);
		 bgVal.setSec((Double) 0.0);
		 
		 //System.out.println(bigramHT.put(bgkey, bgVal));
		 //System.out.println(bigramHT.get(bgkey).toString());
		 
		 this.addToPrfx(pre, w);
	 }
	 
	 public void setFreq(String pre, String w, Double f) {
		 Pair<String, String> bgkey= new Pair<String, String>( pre, w);
		 int count;
		 
		 try{
			 count = bigramHT.get(bgkey).getFirst();
		 }
		 catch (NullPointerException e) {
			 count = 1;
		 }
		 
		 Pair<Integer, Double> bgVal = new Pair<Integer, Double>(count, f);
		 bigramHT.put(bgkey, bgVal);
	 }	 
	 
	 public void addToPrfx(String pre, String w) {
		 Pair<String, String> pfVal = new Pair<String, String>(pre, w);
		 HashSet<Pair<String, String>> bgSet;
		 
		 if (prefixHT.containsKey(pre)) { 
			 bgSet = prefixHT.get(pre);
		 } 
		 else {
			 bgSet = new HashSet<Pair<String, String>>();
		 }
		 
		 bgSet.add(pfVal);
		 prefixHT.put(pre, bgSet);
	 }

	 public Boolean containsBg(String pre, String w) {
		 Pair<String, String> bgkey = new Pair<String, String>(pre, w);
		 return bigramHT.containsKey(bgkey);
	 }
	 
	 public static void main(String [ ] args) {
		 //Bigrams t = new Bigrams();
		 //t.addNew("this", "walk");
		 //t.updateSeen("this", "walk");
		 
		 //System.out.println(t.getBgCount("this" ,"walk"));
	 }
	 
}
