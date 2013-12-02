import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FilenameFilter;

public class Filter {
	
	public static File[] files;
	public static int totalEmails;
	public static int totalSpam;
	public static int totalHam;
	public static int[] classesTotal;
	
	public static String path = "C://Users//CSoare//workspace//SpamFilter//data//train1//";
	public static String file = "C://Users//CSoare//workspace//SpamFilter//data//train1//spam1.txt";
	
	public static void main(String[] args) throws IOException{ 
		//findFile(getDirectory(path), file); // will be args[0], args[1]
		files = getDirectory(path);
		totalEmails = getTotalEmails(files);
		classesTotal = getTrainingTotals(files);
		//System.out.println(classesTotal);
		//System.out.println(total);
		float spamP = getClassPriorProbability(classesTotal[0], totalEmails);
		//System.out.println("no. of spam " + classesTotal[0] + " totalEmails " + totalEmails);
		System.out.println(spamP);
	}
	
	public static int getTotalEmails(File[] files){		
		return files.length;	
	}
	
	public static int[] getTrainingTotals(File[] files){
		int[] classTotal = new int[2]; // total[0] = no. of spam, total[1] = no. of ham
		int spamCount = 0; int hamCount = 0; 
		for (int i = 0; i < files.length; i++){
			if (files[i].getName().startsWith("s")){
				spamCount++;
			}
			if (files[i].getName().startsWith("h")){
				hamCount++;
			}	
		}
		classTotal[0] = spamCount; classTotal[1] = hamCount;
		return classTotal;
	}
	
	public static float getClassPriorProbability(int classEmails, int totalEmails){ 
		// getClassPriorProbability(classesTotal[0], totalEmails) for spam probability
		// getClassPriorProbability(classesTotal[1], totalEmails) for ham probability
		return (float) classEmails / (float ) totalEmails ;	
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
    
    public static void findFile(File[] files, String file) throws IOException{
    	File fi = new File(file);
    	for (File f : files){
    		if((f.getCanonicalPath().equals(fi.getCanonicalPath())))
    			readFile(f.getCanonicalPath());
		}
    }
	    
	public static void readFile(String source){
		try (BufferedReader br = new BufferedReader(new FileReader(source))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
