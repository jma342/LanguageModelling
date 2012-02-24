package models;

public class Pair<F, S> {

	private F first;
	private S second;
	
	Pair() {}
	
	Pair(F first, S second){
		this.first = first;
		this.second = second;
	}
	
	public F getFirst() {
		return first;
	}
	
	public S getSec() {
		return second;
	}	
	
	public void setFirst(F first) {
		this.first = first;
	}
	
	public void setSec(S second) {
		this.second = second;
	}
	
	public String toString(){
		String str = "(" + getFirst().toString() + ", " + getSec().toString() + ")";
		return str;
	}
	
   public int hashCode(){ 	
	   return (37 * first.hashCode() + 13 * second.hashCode() + 23);
   }
	
   public boolean equals(Pair<F,S> p) {
	   return ((first.equals(p.getFirst())) && (second.equals(p.getSec())));
   }
   
   public static void main(String[] args){
	   Pair<String, String> s1 = new Pair<String, String>("hi", "there");
	   Pair<String, String> s2 = new Pair<String, String>("there", "hi");
	   Pair<String, String> s3 = new Pair<String, String>("hi", "there");
	   
	   System.out.println(s1.equals(s2));
	   System.out.println(s1.equals(s3));
   }
   
}
