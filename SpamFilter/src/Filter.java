import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class Filter {
	
	final static int numberOfClasses = 2; 
	public static int sumHam, sumSpam = 0;
	
	public static File[] files;
	public static int totalEmails;
	public static int[] classEmails; // number of emails per class, classEmails[0] : no. of spam, classEmails[1] : no. of ham
	public static float[] classP;
	
	public static HashMap < String, Integer> spamWordFrequency = new HashMap<String, Integer>();
	public static HashMap < String, Integer> hamWordFrequency = new HashMap<String, Integer>();
	
	public static String path = "C://Users//CSoare//workspace//SpamFilter//data//train1//";
	public static String file = "C://Users//CSoare//workspace//SpamFilter//data//train1//ham2.txt";
	
	public static void main(String[] args) throws IOException{ 
		//findFile(getDirectory(path), file); // will be args[0], args[1]
		files = getDirectory(path);
		totalEmails = getTotalEmails(files);
		classEmails = getTrainingTotals(files);
		//System.out.println("totalEmails" + totalEmails);
		//System.out.println("spam: " + classEmails[0] + "ham: " + classEmails[1]);
		classP = getClassPriorProbability(classEmails, totalEmails);
		//System.out.println("spamP: " + classP[0] + " hamP: " + classP[1]);

		sumHam = getWordFrequency(hamWordFrequency, files, "ham");
		sumSpam = getWordFrequency(spamWordFrequency, files, "spam");
		System.out.println("spam: "+ sumSpam);
		System.out.println("ham: "+ sumHam);


	
	}
	
	public static int getTotalEmails(File[] files){	
		//spam + ham
		return files.length;	
	}
	
	public static int[] getTrainingTotals(File[] files){
		int[] classTotal = new int[numberOfClasses]; // total[0] = no. of spam, total[1] = no. of ham
		int spamCount = 0; int hamCount = 0; 
		for (int i = 0; i < files.length; i++){
			if (files[i].getName().startsWith("spam")){
				spamCount++;
			}
			if (files[i].getName().startsWith("ham")){
				hamCount++;
			}	
		}
		classTotal[0] = spamCount; classTotal[1] = hamCount;
		return classTotal;
	}
	
	public static float[] getClassPriorProbability(int[] classEmails, int totalEmails){ 
		float[] classProbabilities = new float[numberOfClasses];
		for (int i = 0; i < classEmails.length ; i++){
			classProbabilities[i] = (float) classEmails[i] / (float ) totalEmails ;
		}
		return classProbabilities;	
	}
	
	
	
	public static int getWordFrequency(HashMap <String, Integer> words, File[] files, String type){	
		int count = 0;
		for (File f : files) {
			if(f.getName().startsWith(type)){
				readFile(f.toString(),words);
			}
		}
		for (String key : words.keySet()){
			count += words.get(key);
		}
		return count;
	}
	
	public static float getConditionalProbability(HashMap <String, Integer> words, String word){
		float p = 0;
		return p;
		
	}
	
	
	
	//////////////////////////////////////////////////////////
	////// 					File reading                 /////
	//////////////////////////////////////////////////////////
	public static File[] getDirectory( String dirName){
    	File dir = new File(dirName);
    	return dir.listFiles(new FilenameFilter() { 
    	         public boolean accept(File dir, String filename)
    	              { return filename.endsWith(".txt"); }
    	} );
    }
      
	public static void readFile(String source, HashMap<String, Integer> m){
		try (BufferedReader br = new BufferedReader(new FileReader(source))) {
			String currentLine;
			ArrayList<String> words = new ArrayList<String>();
			
			while ((currentLine = br.readLine()) != null) {
				String [] line = currentLine.split(" ");
				for(int i =0; i<line.length;i++){
				    Integer freq = m.get(line[i]);
		            m.put(line[i], (freq == null) ? 1 : freq + 1); //neat code
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/*
    public static void findFile(File[] files, String file) throws IOException{
    	File fi = new File(file);
    	for (File f : files){
    		if((f.getCanonicalPath().equals(fi.getCanonicalPath())))
    			readFile(f.getCanonicalPath());
		}
    }
	*/
}
