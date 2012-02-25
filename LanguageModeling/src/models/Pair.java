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
	
   public boolean equals(Object o) { 
	   //System.out.println("in equals()");
	   if (o instanceof Pair) { 
	     Pair<?, ?> p1 = (Pair<?, ?>) o;
	     if ( p1.first.equals( this.first ) && p1.second.equals( this.second ) ) { 
	       return(true);
	     }
	   }
	   return(false);
	 }
   
   public static void main(String[] args){
	   Pair<String, String> s1 = new Pair<String, String>("hi", "there");
	   Pair<String, String> s2 = new Pair<String, String>("there", "hi");
	   Pair<String, String> s3 = new Pair<String, String>("hi", "there");
	   
	   System.out.println(s1.equals(s2));
	   System.out.println(s1.equals(s3));
   }
   
}
