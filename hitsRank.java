package pageRank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class hitsRank {

	private static HashMap<String,ArrayList<String>> outLinks;
	private static HashMap<String,ArrayList<String>> inLinks;
	private static TreeMap<String,Double> hubScore;
	private static TreeMap<String,Double> authScore;
	private static TreeMap<String,Double> prevAuthScore;
	public  static TreeSet<String> baseSet = new TreeSet<String>();
	public  static TreeSet<String> rootSet = new TreeSet<String>();
	public  static LinkedHashMap<String, Double> sortedUrls = new LinkedHashMap<String, Double>();
	public static void main(String[] args) 		
	    {
	    	outLinks = new HashMap<>();
	    	inLinks = new HashMap<>();
	    	hubScore = new TreeMap<String, Double>();
	    	authScore = new TreeMap<String, Double>();
	    	prevAuthScore = new TreeMap<String, Double>();
	    	makeLinkMap();
	    	rootSet.add("D1");
	    	rootSet.add("D2");
	    	rootSet.add("D3");
	    	rootSet.add("D4");
	    	rootSet.add("D5");
	    	rootSet.add("D6");
	    	rootSet.add("D7");
	    	rootSet.add("D8");
	    	baseSet = rootSet;
	    	initializeRanking();
	    	compute();
	    //	System.out.println(sortedUrls);
		}
		
		public static void makeLinkMap()
		{
			outLinks = WebGraph.generateLinkMap();
			inLinks = WebGraph.addInLinks();
		}
		
		public static void initializeRanking(){
			double initialRank = 1;
			for(String key :baseSet){
				if(outLinks.containsKey(key))
				{
					authScore.put(key, initialRank);
					hubScore.put(key, initialRank);
					prevAuthScore.put(key, 0.0);
				}
				else
				{
					authScore.put(key, 0.0);
					hubScore.put(key, 0.0);
				}
			}
		}
		
		public static void compute(){
		
			int iteration =0;
			while(!isConverged(prevAuthScore, authScore))
			{
				TreeMap<String,Double> newHubRank = calcHubScore();
				hubScore = newHubRank;
				TreeMap<String,Double> newAuthRank = calcAuthscore();
				prevAuthScore = authScore;
				authScore = newAuthRank;
				System.out.println("Iteration: "+ iteration);
				for(String link : hubScore.keySet())
					System.out.println(hubScore.get(link));
				System.out.println("auth scores:");
				for(String link : authScore.keySet())
					System.out.println(authScore.get(link));
				iteration++;
			}
			//Add authority scores for all the urls in the root set
			for(String url: rootSet)
			{
				sortedUrls.put(url, authScore.get(url));
			}
		}
		
		public static TreeMap<String,Double> calcHubScore(){
			TreeMap<String,Double> tempRank = new TreeMap<>();
			double normalizingFactor = 0;
			double maxhubscore = 0;
			for(String key :hubScore.keySet()){
				ArrayList<String> tempList = outLinks.get(key);
				double hubScore =0.0;
				if(tempList!=null){
					for(String dest : tempList){
	               	 if(baseSet.contains(dest))
						if(authScore.containsKey(dest))
	               		 hubScore+=authScore.get(dest);
						else
							System.out.println("NewURL not in authscore");
					}
				}
				//normalizingFactor += Math.pow(hubScore, 2);
				if(hubScore > maxhubscore)
					maxhubscore = hubScore;
				tempRank.put(key, hubScore);
			}
			//normalizingFactor =Math.sqrt(normalizingFactor);
			normalizingFactor = maxhubscore;
	        for(String key:tempRank.keySet()){
	        		if(normalizingFactor!=0)
	                    tempRank.put(key, (tempRank.get(key)/normalizingFactor));
	        		else
	        			tempRank.put(key, 0.0);
	        }
			return tempRank;
		}
		
		public static TreeMap<String,Double> calcAuthscore(){
			TreeMap<String,Double> tempRank = new TreeMap<>();
			double normalizingFactor = 0;
			double maxauthscore = 0;
			for(String key :authScore.keySet()){
				//if(!inLinks.containsKey(key))
				//{
				//	continue;
				//}
				ArrayList<String> tempList = inLinks.get(key);
				
				double authScore =0.0;
				if(tempList!=null)
				{
					for(String dest : tempList){
		           	 if(baseSet.contains(dest))
		           	 {
						authScore+=hubScore.get(dest);
		           	 }
					}
				}
				//normalizingFactor += Math.pow(authScore, 2);
				normalizingFactor = maxauthscore;
				if(authScore > maxauthscore)
					maxauthscore = authScore;
				tempRank.put(key, authScore);
			}
			//normalizingFactor =Math.sqrt(normalizingFactor);
	        for(String key:tempRank.keySet()){
	        	if(normalizingFactor!=0)
	                tempRank.put(key, (tempRank.get(key)/normalizingFactor));
	            else
	        	    tempRank.put(key, 0.0);
	        }
			
			return tempRank;
		}

		static boolean isConverged(TreeMap<String, Double> m1, TreeMap<String, Double> m2)
		{
			boolean converged = true;
			for(String url : m1.keySet())
			{
				if(Math.abs(m1.get(url) - m2.get(url))>0.00001)
				{
					converged = false;
				}
			}
			return converged;
		}

		//Read adjacency matrix into an ArrayList
		 public static ArrayList<String> getAdjacencyMatrix(String fileName){
			   ArrayList<String> lineList = new ArrayList<String>();
			   try{
				   FileReader fileReader = new FileReader(fileName);
				   BufferedReader bufferedReader = new BufferedReader(fileReader);
		            String line =null;
		            while((line = bufferedReader.readLine()) != null) {
		                lineList.add(line);
		            } 
		            bufferedReader.close();
		            fileReader.close();
			   } catch(Exception e){
				   e.printStackTrace();
			   }
			   return lineList;
		   }
}
