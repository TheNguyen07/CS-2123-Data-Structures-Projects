package project2;

/*
 * CS2123 Project 2: DFS-based Topological Sorting 
 */

 import java.util.*;
 import java.io.*;
import java.lang.reflect.Array;
 
 public class TopoSort {
    public static Integer curLabel;
 
     public static void main(String[] args) throws FileNotFoundException {
         // The small built-in DAG G1
         // Use this graph to verify the correctness of your DFS-based topological sorting
         int cs143 = 0, cs321 = 1, cs322 = 2, cs142 = 3, cs370 = 4, cs341 = 5, cs326 = 6, cs378 = 7, cs401 = 8, cs421 = 9;
         int[][] g1 = { { cs321, cs341, cs370, cs378 }, { cs322, cs326 }, { cs401, cs421 }, { cs143 }, {}, { cs401 },
                 { cs401, cs421 }, { cs401 }, {}, {} };
         HashMap<Integer, ArrayList<Integer>> G1 = new HashMap<>();
         for (int i = 0; i < g1.length; i++) {
             ArrayList<Integer> neighbors = new ArrayList<Integer>();
             for (int j = 0; j < g1[i].length; j++) {
                 neighbors.add(g1[i][j]);
             }
             G1.put(i, neighbors);
         }
 
         String names[] = { "cs143", "cs321", "cs322", "cs142", "cs370", "cs341", "cs326", "cs378", "cs401", "cs421" };
 
         // Section 1: for project step 2, verify the correctness of your topo_sort() functions
         //     no need to change this section, just verify the correctness of the printout
         int[] topo_order = topo_sort(G1);
         for (int i = 0; i < names.length; i++) {
             System.out.printf("%s: %d, ", names[i], topo_order[i]);
         }
         System.out.println();

         int[] ind_order = ind_topo(G1);
         for (int i = 0; i < names.length; i++) {
             System.out.printf("%s: %d, ", names[i], ind_order[i]);
         }
         System.out.println();
 
         // Section 2: for project step3, performance comparison
         //   load each graph from DAG1.txt-DAG5.txt
         //   store each graph in memory using the format of G1
         //     Hint: you may need Java's File and Scanner classes to read in graph data 
         //   call your topo_sort and the provided ind_topo by passing each graph as the input to obtain the execution time (in milliseconds) 
         // 
         // Complete the code for performance comparison here  
         File[] file = new File[5];
         for(int i = 0; i < 5; i++){ // grabs each of the 5 DAG files
            file[i] = new File("C:\\Users\\power\\OneDrive - University of Tulsa\\CS - 2123\\project2\\DAG" + (i+1) + ".txt");
         }
         
         for(int f = 0; f < file.length; f++){
            Scanner sc = new Scanner(System.in);
            sc = new Scanner(file[f]);

            int size = 0;
            // gets the size of the current file
            while(sc.hasNext()){
                int temp = sc.nextInt();
                if (temp > size){
                    size = temp;
                }
            }

            // creates new hashmaps for both algorithms to not worry about bias
            HashMap<Integer, ArrayList<Integer>> DAG = new HashMap<>();
            HashMap<Integer, ArrayList<Integer>> DAGInd = new HashMap<>();

                for (int i = 0; i < size+1; i++) {
                    ArrayList<Integer> neighbors = new ArrayList<Integer>();
                    DAG.put(i, neighbors);
                    ArrayList<Integer> neighborsInd = new ArrayList<Integer>();
                    DAGInd.put(i, neighborsInd);
                }

            Scanner sc2 = new Scanner(System.in);
            sc2 = new Scanner(file[f]);

            // adds each node to the graphs
            while(sc2.hasNext()){
                int node = sc2.nextInt();
                int next = sc2.nextInt();
                DAG.get(node).add(next);
                DAGInd.get(node).add(next);
            }
            
            System.out.println(size);
            System.out.println("DFS");
            long start = System.currentTimeMillis(); // starts the time
            topo_sort(DAG); // runs the dfs algorithm
            long elapsed = System.currentTimeMillis() - start; // captures the elapsed time
            System.out.println(elapsed);

            System.out.println("Induction");
            start = System.currentTimeMillis();
            ind_topo(DAGInd);
            elapsed = System.currentTimeMillis() - start;
            System.out.println(elapsed + "\n");

            sc.close();
            sc2.close();
        }
     }
 
     // For project step1    
     // Input
     //    G as the input graph
     //    s as the current vertex to be explored 
     public static void dfs_topo(HashMap<Integer, ArrayList<Integer>> G, Integer s, HashSet<Integer> discovered, int[] L) {//you may modify the input argument if necessary
         //add your function body here
        discovered.add(s); // marks the vertex as explored

         for(Integer v : G.get(s)){ // for every edge in the current vertex,
            if(!discovered.contains(v)){ // if the edge is unexplored then,
                dfs_topo(G, v, discovered, L); // run the dfs algoroithm with the current edge
            }
         }
         L[s] = curLabel--; // label the position and work right to left
         return;
     }
 
     // For project step1
     // Input
     //    G as the input graph
     // Output
     //    L as the Java array to record the ordering of vertices. Example L[0] = 5 means node 0 is assigned topo sort number 5
     public static int[] topo_sort(HashMap<Integer, ArrayList<Integer>> G) {//you CANNOT change the input argument list
         int[] L = new int[G.size()];
         curLabel = G.size(); // keeps track of the ordering
         //add your function body here
         HashSet<Integer> discovered = new HashSet<>(); // keeps track of what has been discovered or not
         for (Integer v: G.keySet()) { // for every vertice in the graph, 
            if(!discovered.contains(v)){ // if it has not been explored yet then
                dfs_topo(G, v, discovered, L); // it will move onto the dfs algorithm
            }
         }
         return L;
     }
 
     // I. This is the provided function for induction-based topological 
     // sorting. You need to call this function and compare with your 
     // DFS-based topological sorting for performance     
     // II. DO NOT change any part of this function
     // III. This function destroys the input graph G because it 
     // removes one node in every iteration. During performance comparison, 
     // if you call this function before topo_sort(), you need to make a deep 
     // copy of your graph G and pass that copy to topo_sort() 
     public static int[] ind_topo(HashMap<Integer, ArrayList<Integer>> G) {
 
         int[] count = new int[G.size()]; //in-degree of each node
         for (int i = 0; i < G.size(); i++) {
             for (Integer v : G.get(i)) {
                 count[v]++;
             }
         }
 
         int[] L = new int[G.size()]; //an array to record the ordering of all nodes
         int k = 1; // topo order's value starts from 1 in induction-based method
         while (!G.isEmpty()) {
             Integer u = 0;
             for(Integer u1: G.keySet()) {
                 if(count[u1]==0) {
                     u=u1;
                     break; //break after finding a source node
                 }
             }
             L[u] = k; //assign a topo order value to this node
             k++;
             for (Integer v : G.get(u)) {
                 count[v]--;
             }
             G.remove(u); //remove this source node and its outgoing edges from G
         }
         return L;
     }
 }
 
 
 
 
