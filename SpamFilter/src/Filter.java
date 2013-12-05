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
	
	public static HashMap < String, Integer > spamWordFrequency = new HashMap<String, Integer>();
	public static HashMap < String, Integer > hamWordFrequency = new HashMap<String, Integer>();
	public static HashMap < String, Double[] > vocabulary = new HashMap<String, Double[]>();
	
	
	//public static String path = "C://Users//CSoare//workspace//SpamFilter//data//train1//";
	//public static String file = "C://Users//CSoare//workspace//SpamFilter//data//train1//ham2.txt";
	
	public static String path;// = "D://UNI//MachineLearning//spam//SpamFilter//data//train4";
	public static String file;// = "D://UNI//MachineLearning//spam//SpamFilter//data//test4//3.txt";
	
	
	public static void main(String[] args) throws IOException{ 
		path = args[0];
		file = args[1];
		trainBC();
//		for (String key : vocabulary.keySet()){
//			System.out.print(vocabulary.get(key)[0] + " ");System.out.print(vocabulary.get(key)[1] + " ");System.out.println(key);
//		}
		classify();
		


	
	}
	
	public static void classify(){
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			HashMap < String, Integer> countMail = new HashMap<String, Integer>();
			String currentLine;	
			double pSpam = Math.log(classP[0]);
			double pHam = Math.log(classP[1]);
			
			while ((currentLine = br.readLine()) != null) {
				String [] line = currentLine.split(" ");
				for(int i = 0; i < line.length; i++){
					
					Integer freq = countMail.get(line[i]);
		            countMail.put(line[i], (freq == null) ? 1 : freq + 1);
		        
				}
			}
			//System.out.println(countMail);
			for(String key : countMail.keySet())
			{		
				//System.out.println(key);
				if (vocabulary.get(key) == null){
					  //pSpam *= 1;   
					  //pHam *= 1;
					//System.out.println(vocabulary.get(key));
				}
				else{
					//System.out.println(key);
					//System.out.println(vocabulary.get(key);
					//System.out.println("priorSpam " + pSpam);
					//System.out.println("priorHam " +pHam);
					//System.out.println(vocabulary.get(key)[0]);
					//System.out.println(vocabulary.get(key)[1]);
					//System.out.println(Math.log(pSpam * vocabulary.get(key)[0]));
		            pSpam += countMail.get(key)*Math.log(vocabulary.get(key)[0]);
		            //System.out.println(Math.log(vocabulary.get(key)[0]));
		            pHam += countMail.get(key)*Math.log(vocabulary.get(key)[1]);
		            //System.out.println(Math.log(vocabulary.get(key)[1]));
		        	//System.out.println(pSpam);
					//System.out.println(pHam);
				}
			}
			
			System.out.println("ham - spam: " + (pHam - pSpam));
			
			if(pSpam>pHam)
				System.out.println("spam");
			else
				System.out.println("ham");
				
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	
	}

	
	/////////////////////////////////////////////////////////////
	//					TRAINING BAYESIAN CLASSIFIER    	   //
	/////////////////////////////////////////////////////////////
	
	public static void trainBC(){
		files = getDirectory(path);
		totalEmails = getTotalEmails(files);
		classEmails = getTrainingTotals(files);
		classP = getClassPriorProbability(classEmails, totalEmails); //P(class)
		sumHam = getWordFrequency(hamWordFrequency, files, "ham");
		sumSpam = getWordFrequency(spamWordFrequency, files, "spam");
		//System.out.println(sumHam);System.out.println(sumSpam);
		getConditionalProbability();
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
		System.out.println(words);
		for (String key : words.keySet()){
			count += words.get(key);
		}
		//System.out.println(count);
		return count;
		
	}
	
	public static void getConditionalProbability(){
		for (String word : vocabulary.keySet()){
			if(spamWordFrequency.containsKey(word)){
				vocabulary.get(word)[0] = ((double) spamWordFrequency.get(word) + 1 )/(vocabulary.size() + sumSpam)  ;
			}
			if(!spamWordFrequency.containsKey(word)){
				vocabulary.get(word)[0] = ((double) 0 + 1 )/(vocabulary.size() + sumSpam)  ;
			}
			if(hamWordFrequency.containsKey(word)){
				vocabulary.get(word)[1] = ((double) hamWordFrequency.get(word) + 1 )/(vocabulary.size() + sumHam)  ;
			}
			if(!hamWordFrequency.containsKey(word)){
				vocabulary.get(word)[1] = ((double) 0 + 1 )/(vocabulary.size() + sumHam)  ;
			}
		}		
	}
	
/////////////////////////////////////////////////////////////
//				END TRAINING BAYESIAN CLASSIFIER       	   //
/////////////////////////////////////////////////////////////
	
	
	
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
			
			while ((currentLine = br.readLine()) != null) {
				String [] line = currentLine.split(" ");
				for(int i =0; i<line.length;i++){
					//System.out.println(line[i]);
				    Integer freq = m.get(line[i]);
		            m.put(line[i], (freq == null) ? 1 : freq + 1); //neat code
		            
		            //add to vocabulary
		            if ( !vocabulary.containsKey(line[i]) ) {
		            	vocabulary.put(line[i], (new Double[] {0.0,0.0} ));
		            }
		            else{}//do nothing
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
