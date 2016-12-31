package pageRank;

import java.util.ArrayList;
import java.util.HashMap;

public class WebGraph {

	static HashMap<String, ArrayList<String>> outLinks = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> inLinks = new HashMap<String, ArrayList<String>>();

	static HashMap<String, ArrayList<String>> generateLinkMap()
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
		
		return outLinks;
	}
	
	static HashMap<String, ArrayList<String>> addInLinks()
	{
		for(String url: outLinks.keySet())
		{
			ArrayList<String> out = outLinks.get(url);
			for(String outurl : out)
			{
				ArrayList<String> inlist;
				if(inLinks.containsKey(outurl))
				{
					inlist = inLinks.get(outurl);
				}
				else
				{
					inlist = new ArrayList<String>();
				}
				inlist.add(url);
				inLinks.put(outurl, inlist);
			}
		}
		return inLinks;
	}
}
