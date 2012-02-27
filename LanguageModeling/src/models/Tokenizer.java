package models;
import java.io.*;
import java.util.Scanner;
import java.util.Vector; 
import org.jsoup.Jsoup;
import opennlp.tools.tokenize.*;
import opennlp.tools.sentdetect.*;

public class Tokenizer 
{
	private String fileName = "";
	private Vector <String> tokens = new Vector<String>();
	private Vector <String> sentences = new Vector<String>();
	private SentenceModel model = null;
	
	InputStream modelIn = null;
	
	//a specific sentence model needs to be set
	//to initialise the sentence detector
	void setSentenceModel()
	{
		try 
		{
			modelIn = new FileInputStream("en-sent.bin");
		} 
		catch (FileNotFoundException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try 
		{
			model = new SentenceModel(modelIn);
		} 
		
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//uses the sentence detector to detect all of the sentences
	//in the file and separates each sentence into strings.
	//A '~' - tilde is added at the end of each sentence to mark
	//the end of a sentence. This is to ensure that termination characters(.,!,?)
	//actually do terminate a sentence.
	void detectSentences()
	{
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		    
		try 
		{
			Scanner scanner = new Scanner(new FileInputStream(fileName));
		
		    try 
		    {
		      while (scanner.hasNextLine())
		      {
		    	
		    	 // Jsoup.parse(scanner.nextLine()).text() -- this parses html tags from a string
		    	  
		    	 String sentences_perline[] = sentenceDetector.sentDetect(Jsoup.parse(scanner.nextLine()).text()); 
		     
		    	 for(int count = 0;count < sentences_perline.length;count++)
		    	 { 
		    		 sentences.add(sentences_perline[count] + "~");
		    	 }
		      }
		    }
		    finally
		    {
		      scanner.close();
		    }
		}
		catch(Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		
	}
	
	//tokenizes each of the sentences
	void tokeniseSentences()
	{
		for(int count = 0;count < sentences.size();count++)
	      {	 
	    	 String tokens_perline[] = SimpleTokenizer.INSTANCE.tokenize(sentences.elementAt(count));
	    	 
	    	 for(int innerCount = 0;innerCount < tokens_perline.length;innerCount++)
	    	 {
	    		 tokens.add(tokens_perline[innerCount]);
	    	 }
	      }
	}
	
	//calls necessary functions to tokenise passed in file
	Vector<String> tokenize(String file)
	{
		fileName = file;
		
		setSentenceModel();
		System.out.println("detecting Sentences");
		detectSentences();
		System.out.println("Sentences detected");
		
		System.out.println(" ");
		
		System.out.println("tokenising Sentences");
		tokeniseSentences();
		System.out.println("Sentences tokenised");
		
		return tokens;
		
	}
	

	public static void main(String[] args)
	{
		Tokenizer tok = new Tokenizer();
		
		System.out.println("tokenizing");
		Vector<String> tokens = new Vector<String>();
		tokens = tok.tokenize("wsj.train");
		System.out.println("tokenized");
		
		for(int count = 0;count < tokens.size();count++)
		{
			System.out.println(tokens.elementAt(count));
		}
		
	}

}

