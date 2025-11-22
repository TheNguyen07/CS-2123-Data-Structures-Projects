// Project 3: counting inversions with divide-and-conquer

import java.util.*;
import java.util.stream.IntStream;

public class countinvert {

	public static void main(String[] args) {
		// Part 1: test the correctness
		int[] Ain1 = {5, 4, 3, 2, 1};
		int[] Aout1 = new int[Ain1.length];
		long tc1 = SortAndCountInv(Ain1, Aout1);
		for(int x: Aout1) {
			System.out.print(x+" ");
		}
		System.out.println();
		System.out.println(tc1);
		
		// Part 2: compare the performance between divide-and-conquer and brute-force algorithms
		Random rand = new Random(System.currentTimeMillis());
		int inSize = 320000;
		IntStream stream = rand.ints(inSize, 0, 1000000);
		int[] Ain2 = stream.toArray();
		int[] Aout2 = new int[Ain2.length];

		long t1 = System.currentTimeMillis();
		long tc2 = SortAndCountInv(Ain2, Aout2);
		long t2 = System.currentTimeMillis();
		long bfc2 = bfInv(Ain2);
		long t3 = System.currentTimeMillis();
		System.out.println("Number of inversions found by the divide-and-conquer algorithm: "+tc2);
		System.out.println("Number of inversions found by the brute-force algorithm: "+bfc2);
		System.out.println("Running time of the divide-and-conquer algorithm: "+(t2-t1)/1000.0+" seconds.");
		System.out.println("Running time of the brute-force algorithm: "+(t3-t2)/1000.0+" seconds.");
		
		// Part 3: solve a practical problem
		// Suppose listX provides a correct ascending order of songs, find the number of inversions of listY
		String[] listX = {"F", "G", "A", "C", "B", "D", "E", "H"};
		String[] listY = {"A", "D", "B", "C", "H", "G", "E", "F"};
		long nInv = findInv(listX, listY);
		System.out.println("The number of inversions in list Y compared with list X is: " + nInv);
	}
	
	/*
	 * input: two sorted integer arrays L and R of same length
	 * input: a pre-defined integer array T to merge L and R
	 * return: the number of split inversions between L and R
	 * Note: in addition to return the number of split inversions, you should also 
	 * use T to store the merging of L and R 
	 */
	public static long MergeAndCountSplitInv(int[] L, int[] R, int[] T) {
		//fill in your code here
		// variables to keep track of inversions and array indexes 
		int i = 0, j = 0, splitInv = 0, idx = 0;

		// while the left and right indexes exist
		while (i < L.length && j < R.length){
			//if the left number is less then the right
			if (L[i] <= R[j]){
				T[idx++] = L[i++]; //then add the left to T and increase both indexes
			} else {
				// otherwise, add the right to T and increase the indexes
				T[idx++] = R[j++];
				// counts the inversions
				splitInv = splitInv + L.length - i;
			}
		}
		// adds the remaining numbers to T for the left and right sectiosn
		while (i != L.length) {
			T[idx++] = L[i++];
		}
		while (j != R.length) {
			T[idx++] = R[j++];
		}

		return splitInv; //replace 0 with the number of split inversions
	}
	
	/*
	 * input: unsorted integer array A
	 * input: a pre-defined integer array Aout to store the sorted elements from A
	 * return: the number of inversions in A
	 * Note: in addition to return the number of inversions in A, the sorted elements of A
	 * should be stored in Aout
	 */
	public static long SortAndCountInv(int[] A, int[] Aout) {
		//fill in your code here
		long inv = 0;
		// if the length of A is less than one, return the number of inversions as there is nothing else to merge
		if (A.length <= 1){
			Aout[0] = A[0];
			return inv;
		}

		//left and right arrays to split array A
		int[] L = new int[A.length/2];
		int[] R = new int[(A.length+1)/2];
		//empty arrays similar to Aout
		int[] Lout = new int[A.length/2];
		int[] Rout = new int[(A.length+1)/2];

		//distributes the left and right array numbers
		for(int i = 0; i < L.length; i++){
			L[i] = A[i];
		}
		for(int i = 0; i < R.length; i++){
			R[i] = A[i + (A.length/2)];
		}

		//splits the left and right halves
		inv += SortAndCountInv(L, Lout);
		inv += SortAndCountInv(R, Rout);
		//merges the left and right halves
		inv += MergeAndCountSplitInv(Lout, Rout, Aout);
		return inv; //replace 0 with the total number of inversions in A  
	}
	
	/*
	 * brute-force inversion counting
	 * Don't change this function
	 */
	public static long bfInv(int[] A) {
		long k = 0;
		for(int i=0; i<A.length-1; i++) {
			for(int j=i+1; j<A.length; j++) {
				if(A[i]>A[j]) k = k+1;
			}
		}
		return k;
	}
	
	/*
	 * Suppose list X provides a correct ascending order of its elements
	 * Find the inversion in list Y compared with list X
	 * Note: this function must invoke your SortAndCountInv() function
	 */
	public static long findInv(String[] X, String[] Y) {
		//fill in your code here
		int inv = 0;
	
		int[] A = new int[X.length];
		int[] Aout = new int[X.length];
		// iterates through each x and compares to each y
		for(int i = 0; i < X.length; i++){
			for (int j = 0; j < Y.length; j++) {
				if (X[i].equals(Y[j])) {  // if the x number equations a number in y,
					A[j] = i; // add the x index to A in the y index
				}
			}
		}
		// counts the inversions when x merges
		inv += SortAndCountInv(A, Aout);
		return inv; //replace 0 with the number of inversions in Y compared with X
	}
	
	
	

}
