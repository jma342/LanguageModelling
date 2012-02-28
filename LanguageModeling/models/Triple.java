package models;
//build for the trigram
public class Triple <F,S,T> 
{

		private F first;
		private S second;
		private T third;
		
		Triple() {}
		
		Triple(F first, S second,T third)
		{
			this.first = first;
			this.second = second;
			this.third = third;
		}
		
		public F getFirst() {
			return first;
		}
		
		public S getSec() {
			return second;
		}
		
		public T getThird() {
			return third;
		}	
		
		public void setFirst(F first) {
			this.first = first;
		}
		
		public void setSec(S second) {
			this.second = second;
		}
		
		public void setThird(T third) {
			this.third = third;
		}
		
		public String toString(){
			String str = "(" + getFirst().toString() + ", " + getSec().toString() + ", " +
					getThird().toString() + ")";
			return str;
		}
		
	   public int hashCode(){ 	
		   return (37 * first.hashCode() + 13 * second.hashCode() + 17 * third.hashCode() + 23);
	   }
		
	   public boolean equals(Object o) { 
		   //System.out.println("in equals()");
		   if (o instanceof Triple) { 
		     Triple<?, ?,?> t1 = (Triple<?, ?,?>) o;
		     if ( t1.first.equals( this.first ) && t1.second.equals( this.second ) ) { 
		       return(true);
		     }
		   }
		   return(false);
		 }

		/*public int compareTo(Triple<F, S, T> o) {
			// TODO Auto-generated method stub
		   return -this.third.compareTo(o.third);
		}*/

	   public static void main(String[] args){
		   Pair<String, String> s1 = new Pair<String, String>("hi", "there");
		   Pair<String, String> s2 = new Pair<String, String>("there", "hi");
		   Pair<String, String> s3 = new Pair<String, String>("hi", "there");
		   
		   System.out.println(s1.equals(s2));
		   System.out.println(s1.equals(s3));
	   }
	   
	}
