package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class MainMenu 
{
	public static void main(String[] args) 
	{
		String choice = "";
		System.out.println("1. Random Sentence Generation on Works of Shakespeare");
		System.out.println("2. Random Sentence Generation on excerpts from the Novel \"War and Peace\"");
		System.out.println("3. Smoothing and Perplexity on Works of Shakesspeare");
		System.out.println("4. Smoothing and Perplexity on on excerpts from the Novel \"War and Peace\"");
		System.out.println("5. Email Autor Prediction");

		System.out.print("\n");
		
		System.out.print("Please select language model feature to run: ");
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
		try 
		{
			choice = br.readLine();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		displayChoice(choice);
	}
	
	public static void displayChoice(String choice)
	{
		//shakespeare
		if(choice.equals("1") || choice.equals("2"))
		{
			System.out.print("\n");
			System.out.print("RANDOM SENTENCE GENERATION");
			System.out.println("\n\n");
			
			Tokenizer tokens = new Tokenizer();
			Vector<String> toksV= new Vector<String>();
			Ngrams ngram = new Ngrams();
			String filename = "";
			
			if(choice.equals("1"))
			{
				filename = "TrainingData\\DS3_train.txt";
			}
			else if(choice.equals("2"))
			{
				filename= "TrainingData\\DS4_train.txt";
			}
			
			toksV = tokens.tokenize("TrainingData\\DS4_train.txt");
			
			
			Bigrams bgs= new Bigrams();
			Unigrams ugs= new Unigrams();
			Trigrams tgs = new Trigrams();
			
			int tokSize= toksV.size();
			
			ngram.indexText(toksV, tgs,bgs, ugs);
			ngram.setFreqs(tokSize, tgs,bgs, ugs);
			
			System.out.println();
			System.out.println("UNIGRAM MODEL");
			System.out.println(" ");
			ngram.randomSentenceUnigrams(ugs,tokSize);
			
			System.out.println("\n\n");
			
			System.out.println("BIGRAM MODEL");
			System.out.println(" ");
			ngram.randomSentenceBigrams(ugs,bgs,tokSize);
			
			System.out.println("\n\n");
			
			System.out.println("TRIGRAM MODEL - EXTENSION");
			System.out.println(" ");
			ngram.randomSentenceTrigrams(ugs,bgs,tgs,tokSize);
		}
		
		//this is currently not working as perplexity is to be done on 
		//the test data
		//hence the <UNK> unknown word tags are necessary here. Therefore when
		//the application sees unknown words it crashes.
		else if(choice.equals("3") || choice.equals("4"))
		{
			Bigrams bgs = new Bigrams();
			Unigrams ugs= new Unigrams();
			Tokenizer tokens = new Tokenizer();
			Vector<String> toksV = new Vector<String>();
			Ngrams ngram = new Ngrams();
			String filename = "";
			
			System.out.print("\n");
			System.out.print("GOOD TURING SMOOTHING AND PERPLEXITY");
			System.out.println("\n\n");
			
			if(choice.equals("3"))
			{
				filename = "TrainingData\\DS3_test.txt";
			}
			else if(choice.equals("4"))
			{
				filename= "TrainingData\\DS4_test.txt";
			}
			
			toksV = tokens.tokenize(filename);
						
			int tokSize= toksV.size();
		
			ngram.indexText(toksV, bgs, ugs);
			ngram.setFreqs(tokSize, bgs, ugs);
			 
			System.out.println("Perplexity(unigram-smooth) = " + ngram.findPerplexity(filename, ugs, true));
			//System.out.println("Perplexity(unigram)        = " + findPerplexity("TrainingData\\DS3_train.txt", ugs, false));
			System.out.println("Perplexity(bigram-smooth)  = " + ngram.findPerplexity(filename, bgs, true));
			//System.out.println("Perplexity(bigram)         = " + findPerplexity("TrainingData\\DS3_train.txt", bgs, false));

		}
		else if(choice.equals("5"))
		{
			/*Nick you can insert your email author prediction's necessary function
			 * calls here in order to see the accuracy for the validation and test set
			 * of the enron_dataset
			 */
			
		}
	}

}
