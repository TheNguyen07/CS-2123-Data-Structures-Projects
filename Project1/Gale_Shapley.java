package project1;
/*
 * CS2123 Project 1: The Variants of Gale-Shapley Algorithm
 */
import java.util.*;


public class GS {

	public static void main(String[] args) {
		String[] menlist = {"xavier", "yancey", "zeus"};
		String[] womenlist = {"amy", "bertha", "clare"};
		
		HashMap<String, String[]> thepref = new HashMap<>(); //for standard GS algorithm input
		String[][] names = {
				{"amy", "bertha", "clare"}, //xavier
				{"bertha", "amy", "clare"}, //yancey
				{"amy", "bertha", "clare"}, //zeus
				{"yancey", "xavier", "zeus"}, //amy
				{"xavier", "yancey", "zeus"}, //bertha
				{"xavier", "yancey", "zeus"} //clare
		};
		thepref.put("xavier", names[0]);
		thepref.put("yancey", names[1]);
		thepref.put("zeus", names[2]);
		thepref.put("amy", names[3]);
		thepref.put("bertha", names[4]);
		thepref.put("clare", names[5]);
		
		String[][] blocked = { //for blocked GS algorithm
				{"xavier", "clare"}, {"zeus", "clare"}, {"zeus", "amy"}
		};
		
		HashMap<String, String[][]> thepreftie = new HashMap<>(); //for GS algorithm dealing with ties
		String[][][] names2 = {
				{{"bertha"}, {"amy"}, {"clare"}}, // x
				{{"amy", "bertha"}, {"clare"}}, // y
				{{"amy"}, {"bertha", "clare"}}, // z
				{{"zeus", "xavier", "yancey"}}, //a
				{{"zeus"}, {"xavier"}, {"yancey"}}, //b 
				{{"xavier", "yancey"}, {"zeus"}} //c
		};
		thepreftie.put("xavier", names2[0]);
		thepreftie.put("yancey", names2[1]);
		thepreftie.put("zeus", names2[2]);
		thepreftie.put("amy", names2[3]);
		thepreftie.put("bertha", names2[4]);
		thepreftie.put("clare", names2[5]);
		
		HashMap<String, String> match = gs(menlist, womenlist, thepref);
		System.out.print("{ ");
		for(String w: match.keySet()) {
			System.out.printf("%s: %s, ", w, match.get(w));
		}
		System.out.println("}");
		
		
		HashMap<String, String> match_block = gs_block(menlist, womenlist, thepref, blocked);
		System.out.print("{ ");
		for(String w: match_block.keySet()) {
			System.out.printf("%s: %s, ", w, match_block.get(w));
		}
		System.out.println("}");
		
		HashMap<String, String> match_tie = gs_tie(menlist, womenlist, thepreftie);
		System.out.print("{ ");
		for(String w: match_tie.keySet()) {
			System.out.printf("%s: %s, ", w, match_tie.get(w));
		}
		System.out.println("}");
	}
	
	/*
	 * Original Gale-Shapley Algorithm
	 * Inputs: men (array of men's names)
	 *         women (array of women's names)
	 *         pref (dictionary of preferences mapping names to list of preferred names in sorted order
	 * Output: a Dictionary (HashMap) of stable matches
	 */
	public static HashMap<String, String> gs(String[] men, String[] women, 
			HashMap<String, String[]> pref) {
		//Dictionary (HashMap) to store results
		//first String indicates the woman (key)
		//second String indicates the matched man (value)
		//Please study the use of Java HashMap class 
		HashMap<String, String> S = new HashMap<>(); 
		
		// build the rank dictionary
		HashMap<String, HashMap<String, Integer>> rank = new HashMap<>();
		for(String w: women) {
			HashMap<String, Integer> mrank = new HashMap<>();
			int i = 1;
			for(String m: pref.get(w)) {
				mrank.put(m, i);
				i++;
			}
			rank.put(w, mrank);
		}
		
		// create a pointer to the next woman to propose
		HashMap<String, Integer> prefptr = new HashMap<>();
		for(String m: men) {
			prefptr.put(m, 0); //all starting from 0
		}
		
		// create a freemen list as we did in class
		ArrayList<String> freemen = new ArrayList<String>();
		for(String m: men) {
			freemen.add(m);
		}
		
		//algorithm start here
		while(freemen.size()>0) {
			String m = freemen.remove(0); //always remove from the front end
			int currentPtr = prefptr.get(m);
			String w = pref.get(m)[currentPtr];
			prefptr.put(m, currentPtr+1);
			if(!S.containsKey(w)) S.put(w, m);
			else {
				String mprime = S.get(w);
				if(rank.get(w).get(m)<rank.get(w).get(mprime)) {
					S.put(w, m);
					freemen.add(mprime); //put the previous man back to freemen list
				}
				else {
					freemen.add(m); //put the current man back to freemen list
				}
			}
		}
		
		return S;
	}
	
	/*
	 * Modified Gale-Shapley Algorithm to handle unacceptable matches
	 * Inputs: men (array of men's names)
	 *         women (array of women's names)
	 *         pref (dictionary of preferences mapping names to list of preferred names in sorted order
	 *         blocked (2D array of blocked pairs)
	 * Output: a Dictionary (HashMap) of stable matches        
	 */
	public static HashMap<String, String> gs_block(String[] men, String[] women, 
			HashMap<String, String[]> pref, String[][] blocked) {
		//Dictionary (HashMap) to store results
		//first String indicates the woman (key)
		//second String indicates the matched man (value)
		//Please study the use of Java HashMap class 
		HashMap<String, String> S = new HashMap<>(); 
		//insert your solution here

		// build the rank dictionary
		// this is unchanged from the original Gale-Shapley Algorithm
		// this puts the rankings/preferences into new hashmaps to use
		HashMap<String, HashMap<String, Integer>> rank = new HashMap<>();
		for(String w: women) {
			HashMap<String, Integer> mrank = new HashMap<>();
			int i = 1;
			for(String m: pref.get(w)) {
				mrank.put(m, i);
				i++;
			}
			rank.put(w, mrank);
		}
		
		// create a pointer to the next woman to propose
		// this is unchanged from the original Gale-Shapley Algorithm
		// this puts all of the preference partners of the men into a new hashmap
		HashMap<String, Integer> prefptr = new HashMap<>();
		for(String m: men) {
			prefptr.put(m, 0); //all starting from 0
		}
		
		// create a freemen list as we did in class
		// this is unchanged from the original Gale-Shapley Algorithm
		// this creates a new ArrayList of each of the men so we can take them out one at a time to match them
		ArrayList<String> freemen = new ArrayList<String>();
		for(String m: men) {
			freemen.add(m);
		}
		
		//algorithm start here
		while(freemen.size()>0) { //while there are still men that are not paired with each other
			String m = freemen.remove(0); //always remove from the front end
			int currentPtr = prefptr.get(m); //gets the current partner of m
			String w = pref.get(m)[currentPtr]; //gets the woman that m prefers
			for (int i = 0; i < blocked[0].length; i++) { //for every pair in blocked
				if (blocked[i][0].equals(m) && blocked[i][1].equals(w)){ //this checks to make sure that the current man is not with the respective woman on the blocked list
					if (prefptr.get(m)+1 > pref.get(m).length){ // this ensures that there won't be an out of bounds error by checking that each man has proposed to each woman already
						freemen.add(m); //put the current man back to freemen list
					}
					continue; //this skips putting the pair into the set if there is a blocked pair
				}
			}
			// this is unchanged from the original Gale-Shapley algorithm
			prefptr.put(m, currentPtr+1);
			if(!S.containsKey(w)) S.put(w, m); // if the set does not have the current woman, then put the new pair in
			else { // if the set does have the women, then compare it to the man she is with and check preferences to see if anyone is ranked higher
				String mprime = S.get(w);
				if(rank.get(w).get(m)<rank.get(w).get(mprime)) {
					S.put(w, m);
					if (prefptr.get(m)+1 > pref.get(m).length) // this ensures that there won't be an out of bounds error by checking that each man has proposed to each woman already
					freemen.add(mprime); //put the previous man back to freemen list
				}
				else {
					if (prefptr.get(m) > pref.get(m).length) // this ensures that there won't be an out of bounds error by checking that each man has proposed to each woman already
					freemen.add(m); //put the current man back to freemen list
				}
			}
		}
		return S; // returns the completed matched set
	}
	
	/*
	 * Modified Gale-Shapley Algorithm to handle ties
	 * Inputs: men (array of men's names)
	 *         women (array of women's names)
	 *         preftie (dictionary of preferences mapping names to list of preferred names in sorted order
	 * Output: a Dictionary (HashMap) of stable matches
	 */
	public static HashMap<String, String> gs_tie(String[] men, String[] women, 
			HashMap<String, String[][]> preftie) {
		
		//Dictionary (HashMap) to store results
		//first String indicates the woman (key)
		//second String indicates the matched man (value)
		//Please study the use of Java HashMap class 
		HashMap<String, String> S = new HashMap<>(); 
		//insert your solution here

		// build the rank dictionary
		// this puts the rankings/preferences into new hashmaps to use
		HashMap<String, HashMap<String, Integer>> rank = new HashMap<>();
		for(String w: women) {
			HashMap<String, Integer> mrank = new HashMap<>();
			int i = 1;
			// this puts the tied list and puts them in the men rank to then put that in the full rank corresponding to the women
			for(String[] m: preftie.get(w)) { // there is an extra for loop due to the nature of the preftie hashmap
				for(String x: m){ 
					mrank.put(x, i);
				}
				i++;
			}
			rank.put(w, mrank);
		}
		
		// create a pointer to the next woman to propose
		// this is unchanged from the original Gale-Shapley Algorithm
		// this puts all of the preference partners into a new hashmap
		HashMap<String, Integer> prefptr = new HashMap<>();
		for(String m: men) {
			prefptr.put(m, 0); //all starting from 0
		}
		
		// create a freemen list as we did in class
		// this is unchanged from the original Gale-Shapley Algorithm
		// this creates a new ArrayList of each of the men so we can take them out one at a time to match them
		ArrayList<String> freemen = new ArrayList<String>();
		for(String m: men) {
			freemen.add(m);
		}
		
		//algorithm start here
		while(freemen.size()>0) {//while there are still men that are not paired with each other
			String m = freemen.remove(0); //always remove from the front end
			int currentPtr = prefptr.get(m); //gets the current partner of m
			String w = preftie.get(m)[currentPtr][0]; //gets the woman that m prefers, there is an added [0] to access the women in the preftie hash map since it has another added dimension.
			// the rest of this code is unchanged from the original Gale-Shapley Algorithm
			prefptr.put(m, currentPtr+1); 
			if(!S.containsKey(w)) S.put(w, m); // if the set does not have the current woman, then put the new pair in
			else { // if the set does have the women, then compare it to the man she is with and check preferences to see if anyone is ranked higher
				String mprime = S.get(w);
				if(rank.get(w).get(m)<rank.get(w).get(mprime)) {
					S.put(w, m);
					freemen.add(mprime); //put the previous man back to freemen list
				}
				else {
					freemen.add(m); //put the current man back to freemen list
				}
			}
		}
		return S; // returns the completed matched set
	}
}
