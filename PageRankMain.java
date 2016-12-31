package pageRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class PageRankMain {

	static HashMap<String, ArrayList<String>> outLinks = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> inLinks = new HashMap<String,ArrayList<String>>();
	static TreeMap<String, Double> ranks = new TreeMap<String, Double>();
	static TreeMap<String, Double> prevRanks = new TreeMap<String, Double>();
	public static void main(String[] args)
	{
		outLinks = WebGraph.generateLinkMap();
		System.out.println(outLinks);
		inLinks = WebGraph.addInLinks();
		System.out.println("Inlinks: ");
		System.out.println(inLinks);
		initializeRanks();
		int iteration = 0;
		while(!isConverged(ranks, prevRanks))
		{
			System.out.println("Iteration: " + iteration);
			for(String rank : ranks.keySet())
				System.out.println(ranks.get(rank));
			computePagerank();
			iteration++;
		}
	}
	
	static void initializeRanks()
	{
		for(String url : outLinks.keySet())
		{
			ranks.put(url, 0.125); //Initialize to 1/|N| = 1/8
			prevRanks.put(url, 0.0);
		}
	}
	
	static void computePagerank()
	{
		TreeMap<String, Double> tempRanks = new TreeMap<String, Double>();
		for(String url: ranks.keySet())
		{
			Double temprank = 0.0;
			Double totalRank = 0.0;
			ArrayList<String> inlinkList = inLinks.get(url);
			if(inlinkList!=null)
			{
			for(String inlink : inlinkList)
			{
				temprank += ranks.get(inlink)/outLinks.get(inlink).size();
			}
			}
			totalRank = 0.15 + (0.85 * temprank);
			tempRanks.put(url, totalRank);
		}
		prevRanks = ranks;
		ranks = tempRanks;
	}
	
	static boolean isConverged(TreeMap<String, Double> m1, TreeMap<String, Double> m2)
	{
		boolean converged = true;
		for(String url : m1.keySet())
		{
			if(Math.abs(m1.get(url) - m2.get(url))>0.0001)
			{
				converged = false;
			}
		}
		return converged;
	}
	
}
