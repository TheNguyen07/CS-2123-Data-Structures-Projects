package project5;

// project 5: starter code
// Edit distance between gene sequences

import java.util.ArrayList;
import java.util.Collections;
public class edit_distance {

	///// define the data structures to store the steps to turn the source into the target'
	///// use these ArrayList in your edit_dist() method, do NOT change them here
	//source symbol involved in each step
	public static ArrayList <Character> sourceSymbols = new ArrayList<Character>();
	//target symbol involved in each step
	public static ArrayList <Character> targetSymbols = new ArrayList<Character>();
	//Four possible actions to take in each step i
	//actions[i] = 0: step i is keep
	//actions[i] = 1: step i is substitution
	//actions[i] = 2: step i is insertion
	//actions[i] = 3: step i is deletion
	public static ArrayList <Integer> actions = new ArrayList<Integer>();
	
	//follow each part in the main method to test your program
	//Don't change any statements in the main method
	public static void main(String[] args) {
		String seq1 = "AGTC";
		String seq2 = "GCCA";
		String seq3 = "AGCTATTC";
		String seq4 = "GTTCAACG";

		sourceSymbols.clear();
		targetSymbols.clear();
		actions.clear();
		int dist12 = gene_dist(seq1, seq2);
		display_results(dist12);
		
		System.out.println();
		
		sourceSymbols.clear();
		targetSymbols.clear();
		actions.clear();
		int dist34 = gene_dist(seq3, seq4);
        display_results(dist34);

		System.out.println();

        //Switch the source and target, and observe the resulting distance and edits
        //Notice any similarities and differences compared with the original order?
        //Explain the observed similarities and differences
		sourceSymbols.clear();
		targetSymbols.clear();
		actions.clear();
		int dist21 = gene_dist(seq2, seq1);
		display_results(dist21);
		
		System.out.println();
		
		sourceSymbols.clear();
		targetSymbols.clear();
		actions.clear();
		int dist43 = gene_dist(seq4, seq3);
		display_results(dist43);

	}
	
	//complete this method
	//Output: the edit distance between the src and the tgt
	//Please also record your edit steps in the three pre-defined ArrayLists:
	//sourceSymbols, targetSymbols and actions
	public static int gene_dist(String src, String tgt) {
		// inserts a space before each string
		src = " " + src;
		tgt = " " + tgt;		

		// creates a new 2D array that serves as the matrix
		int[][] mem = new int[src.length()][tgt.length()];
		// adds the inital numbers to the matrix (the first row and the first column increments by 1)
		for(int i = 0; i < src.length();i++){
			mem[i][0] = i;
		}
		for(int j = 0; j < tgt.length();j++){
			mem[0][j] = j;
		}
        
		// builds the matrix
		int match = 0, ins = 0, del = 0, min = 0;
		for (int s = 1; s < src.length(); s++){ // for each source character, starting at index 1
			for (int t = 1; t < tgt.length(); t++){ // for each target character, starting at index 1
				if (src.charAt(s) == tgt.charAt(t)){ // check if the characters are the same
					match = mem[s-1][t-1]; // if they are, match becomes the number diagonal
				} else {
					match = mem[s-1][t-1] + 1; // if they aren't match becomes the number diagonal plus 1
				}
				ins = mem[s][t-1] + 1; // ins is 1 plus to the box to the left
				del = mem[s-1][t] + 1; // del is 1 plus the box above
				// finds the minimum
				if (match < ins && match < del){ // if match is less than ins and del
					min = match; // then it becomes the minimum number
				} else if (ins < del){ // otherwise, if ins is less than del
					min = ins; // then ins becomes the minimum
				} else {
					min = del; // last case, del becomes the minimum
				}
				mem[s][t] = min; // the current box becomes the minimum number
			}
		}
		int dist = mem[src.length()-1][tgt.length()-1]; // The edit distance
		
		int curr = dist, diag = dist;
		int s = src.length()-1, t = tgt.length()-1; // starts at the end
		while (s > 0 || t > 0){ // while the source or target has not reached index 0. This ensures that the extra space in fron of each string is not counted.
			// These if statements are used for end cases
			if((t-1) >= 0) { // if t-1 exists
				ins = mem[s][t-1]; // then decrement t
			} else {  // otherwise, set the variable to the max value so that it cannot be chosen
				ins = Integer.MAX_VALUE;
			}
			if((s-1) >= 0){ // if s-1 exists
				del = mem[s-1][t]; // then decrement s
			} else { // otherwise, set the variable to the max value so that it cannot be chosen
				del = Integer.MAX_VALUE;
			}
			if ((s-1)>=0 && (t-1 >=0)){ // checks that both s-1 and t-1 exists 
				diag = mem[s-1][t-1]; // if they do, decrement both to set the diagonal
			}
			if (diag <= ins && diag <= del){ // if diagonal is minimum, move there
				if (curr == diag){ // if diagonal is same number then actions.add(0)
					actions.add(0); // Keep
					sourceSymbols.add(src.charAt(s));
					targetSymbols.add(' ');
				} else {  // otherwise,
					actions.add(1); // Sub
					sourceSymbols.add(src.charAt(s));
					targetSymbols.add(tgt.charAt(t));
				}
				curr = mem[--s][--t]; // Moves to the diagonal
			} else if (ins <= del){ // if ins is minimum
				actions.add(2); // Insert
				sourceSymbols.add(' ');
				targetSymbols.add(tgt.charAt(t));
				curr = mem[s][--t]; // Moves to the left
			} else { // Delete
				actions.add(3);
				sourceSymbols.add(src.charAt(s));
				targetSymbols.add(' ');
				curr = mem[--s][t]; // Moves up
			}
		}
		// Reverses all of the ArrayLists to show in the correct order in the display function
		Collections.reverse(sourceSymbols);
		Collections.reverse(targetSymbols);
		Collections.reverse(actions);

		// Used for debugging to check if each array is correct, parallel to what should be printed before displaying
		// System.out.println("s: "+sourceSymbols.toString());
		// System.out.println("t: "+targetSymbols.toString());
		return dist; // returns the edit distance
	}
	
	//Function to print the results of edit distance algorithm
	//Do NOT change this function
	//The results must be displayed using this function !!!
	public static void display_results(int dist) {
		System.out.println("The edit distance is "+dist);
		System.out.println("The number of steps to turn the source into the target gene: " +
		actions.size());
		System.out.println("These steps are: ");
		for(int i=0; i<actions.size(); i++) {
			if(actions.get(i)==0)
				System.out.println("Keep "+sourceSymbols.get(i)+" unchanged");
			else if(actions.get(i)==1)
				System.out.println("Modify "+sourceSymbols.get(i)+" to "+targetSymbols.get(i));
			else if(actions.get(i)==2)
				System.out.println("Insert "+targetSymbols.get(i));
			else if(actions.get(i)==3)
				System.out.println("Delete "+sourceSymbols.get(i));
		}
		System.out.println("Done");
	}
}
