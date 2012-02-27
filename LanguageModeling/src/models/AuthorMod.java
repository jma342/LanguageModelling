package models;

/*
 * fields contain the models that have been created for tis model
 */
public class AuthorMod {

	public Unigrams ug= new Unigrams();
	public Bigrams bg= new Bigrams();
	public String id;
	public int trainSize;  //
	public int unks;   //might be unecessary

	
	
}
