/*sorts by value of second element
 * higher magnitude considered higher priority in priority queue.  Therefore the high scores is stored at head
 */

package models;

public class Pair<F,S extends Comparable<S>> implements Comparable<Pair< F,S>> {

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

   public int compareTo(Pair<F, S> o) 
   {
	return -this.second.compareTo(o.second);
	}   

   public static void main(String[] args){
	   Pair<String, String> s1 = new Pair<String, String>("hi", "there");
	   Pair<String, String> s2 = new Pair<String, String>("there", "hi");
	   Pair<String, String> s3 = new Pair<String, String>("hi", "there");
	   
	   System.out.println(s1.equals(s2));
	   System.out.println(s1.equals(s3));
   }

   
}
