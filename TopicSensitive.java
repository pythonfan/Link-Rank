package pageRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class TopicSensitive {
	static int N = 8;
	static Double[][] matA = new Double[N][N];
	static TreeMap<String, ArrayList<String>> outLinks = new TreeMap<String, ArrayList<String>>();
	static ArrayList<String> urlList = new ArrayList<String>();
	static TreeMap<String, Integer> urlId = new TreeMap<String, Integer>();
	static HashMap<String, ArrayList<Double>> topicRank = new HashMap<String, ArrayList<Double>>();
	static TreeMap<String, Double> generalRank;
	static Double[] generalRankMat = new Double[N];
	static HashMap<String, Double> tempRank = new HashMap<String, Double>();
	static HashMap<String, Double> beforeTopicRank = new HashMap<String, Double>();
	static HashMap<String, ArrayList<String>> categories = new HashMap<String, ArrayList<String>>();
	public static void main(String args[])
	{
		PageRankMain.main(null);
		generalRank = PageRankMain.ranks;
		int rankcount=0;
		for(String url : generalRank.keySet())
		{
			generalRankMat[rankcount] = generalRank.get(url);
			rankcount++;
		}
		//Initialize categories
		initializeCategories();
		addOutLinks();
		initializeMat();
		System.out.println(urlId);
		for(int i=0; i<N; i++)
			{
			for(int j=0; j<N; j++)
				System.out.print(matA[i][j]+"\t");
			System.out.println("\n");
			}
	//	System.out.println("Temp rank:");
	//	System.out.println(tempRank);
		System.out.println("TopicRanks:");
		System.out.println(topicRank);
	}
	
	static void initializeMat()
	{
		int pos = 0;
		for(String url : outLinks.keySet())
		{
			urlList.add(url);
			urlId.put(url, pos);
			pos++;
		}
		for(int i=0; i< N; i++)
		{
			for(int j=0; j< N; j++)
			{
				matA[i][j] = 0.0;
			}
		}
		//Compute stochastic matrix 
		for(String url : outLinks.keySet())
		{
			ArrayList<String> outList = outLinks.get(url);
			int row, col;
			for(String link : outList)
			{
				row = urlId.get(url);
				col = urlId.get(link);
				matA[row][col] = (1.0/outLinks.get(link).size());
			}
		}
		//Multiply by 1-alpha
		for(int i=0; i<N; i++)
		{
			for(int j=0; j<N; j++)
			{
				matA[i][j] *= 0.15;
			}
		}
		//Multiply with general pagerank
		for(String url: urlId.keySet())
		{
			Double temp = 0.0;
			int row;
			for(int i=0; i<N; i++)
			{
				row = urlId.get(url);
				temp += (matA[row][i] * generalRankMat[i]);
			}
			
			tempRank.put(url,  temp);
 		}
		
		System.out.println("Before adding topic rank" + tempRank);
		
		//Add additional weight for docs within the category
		for(String categ : categories.keySet())
		{
			System.out.println(categ);
			ArrayList<String> categUrls = categories.get(categ);
			//Inititalize to tempranks
			beforeTopicRank = new HashMap<String, Double>();
			for(String url : tempRank.keySet())
			{
				beforeTopicRank.put(url, tempRank.get(url));
			}
			//Add 1/|C| to all pages within the category
			for(String url: categUrls)
			{
				Double curRank = tempRank.get(url);
				curRank += 0.85/categUrls.size();
				beforeTopicRank.put(url, curRank);
			}
			//Add tempRank to topicRanks of corresponding category
			for(String url: beforeTopicRank.keySet())
			{
				ArrayList<Double> rankList;
				if(topicRank.containsKey(url))
				{
					rankList = topicRank.get(url);
				}
				else
				{
					rankList = new ArrayList<Double>();
				}
				rankList.add(beforeTopicRank.get(url));
				topicRank.put(url, rankList);
			}
			
		}
	}

	static void addOutLinks()
	{
		ArrayList<String> linkList = new ArrayList<String>();
		linkList.add("D2");
		linkList.add("D3");
		linkList.add("D5");
		linkList.add("D7");
		outLinks.put("D1",linkList);
		linkList = new ArrayList<String>();
		linkList.add("D4");
		linkList.add("D7");
		outLinks.put("D2", linkList);
		linkList = new ArrayList<String>();
		linkList.add("D1");
		linkList.add("D5");
		linkList.add("D8");
		outLinks.put("D3", linkList);
		linkList = new ArrayList<String>();
		linkList.add("D5");
		linkList.add("D6");
		linkList.add("D7");
		outLinks.put("D4", linkList);
		linkList = new ArrayList<String>();
		linkList.add("D2");
		linkList.add("D4");
		linkList.add("D6");
		linkList.add("D8");
		outLinks.put("D5", linkList);
		linkList = new ArrayList<String>();
		linkList.add("D2");
		linkList.add("D3");
		linkList.add("D5");
		outLinks.put("D6", linkList);
		linkList = new ArrayList<String>();
		linkList.add("D1");
		linkList.add("D3");
		linkList.add("D5");
		outLinks.put("D7", linkList);
		linkList = new ArrayList<String>();
		linkList.add("D1");
		linkList.add("D2");
		linkList.add("D7");
		outLinks.put("D8", linkList);
	}
	static void initializeCategories()
	{
		ArrayList<String> categDocs = new ArrayList<String>();
		categDocs.add("D1");
		categDocs.add("D5");
		categories.put("cars", categDocs);
		categDocs = new ArrayList<String>();
		categDocs.add("D3");
		categDocs.add("D4");
		categDocs.add("D6");
		categDocs.add("D8");
		categories.put("software", categDocs);
		categDocs = new ArrayList<String>();
		categDocs.add("D2");
		categDocs.add("D7");
		categories.put("allergies", categDocs);
	}
}

