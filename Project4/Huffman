package project4;

/*
 * CS2123 Project 4: Huffman Coding Trie
 */

 import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
 
 public class Huffman{
    // COMPRESS
    // analyze input file, count occurrence of each symbol or character
    // build huffman trie bottom up
    // use priority queue to select groups of symbols with minimum total occurrences.
    // read the trie to get huffman coding (0's and 1's) for each symbol/character
    // store in hashmap

    // WRITE TO BIN FILE
    // read input file again symbol by symbol, translate to 0's and 1's using huffman coding
    // store the whole thing in a new bin file
    // calculate compression ratio, count # of symbols in input
    // compression ratio = bin file size in bytes/# of symbols in text
    // we want 0.5 - 0.75

    // DECOMPRESS
    // read bin file byte by byte and decode to restore all symbols and characters
    // store everything in new txt file
    // compare new txt file with original (should be identical)
 
     public static void main(String[] args) throws IOException {
        // Huffman Coding
        // for loop to run all of the books in one go rather than manually changing the number
        for (int booknum = 1; booknum <= 6; booknum++){
            System.out.println("book " + booknum);
            long start = System.currentTimeMillis(); // starts the time
            File book = new File("project4\\book" + booknum + ".txt");
            Scanner sc = new Scanner(book);
            FileReader read = new FileReader(book);
            HashMap<Character, Integer> freqs = new HashMap<>();
            PriorityQueue<Node> pQueue = new PriorityQueue<>();
            long counter = 0;
         
        // This is the first implementation to read a file
        // Both implementations run about the same time
        //  while(sc.hasNextLine()){ // while there is a still a line to scan
        //     // read each line
        //     char[] chars = sc.nextLine().toCharArray();
        //     // counts the freq of each character
        //     for(char c : chars){
        //         if (freqs.containsKey(c)){ // if the key exists, then it increments the frequency of that key
        //             freqs.put(c, freqs.get(c)+1);
        //         } else {
        //             freqs.put(c, 1); // if the key does not exist, then it creates a new key for the character and sets the frequenct to 1
        //         }
        //     }     
        //     if (sc.hasNextLine()){
        //         if (freqs.containsKey('\n')){ // added to ensure that the line breaks are counted
        //             freqs.put('\n', freqs.get('\n')+1);
        //         } else {
        //             freqs.put('\n', 1);
        //         }     
        //     }
        //  }

        // This is the second implementation to read a file
        char[] cbuf = new char[1024]; // buffer to read 1024 characters
        while (true){
            int cnt;
            try {
                cnt = read.read(cbuf); // This reads the book
                if (cnt == -1) break; // once count is -1, there are no more characters to read and breaks the while loop
                for (int i = 0; i < cnt; i++) {
                    Character c = cbuf[i]; // selects the character to read
                    if(freqs.containsKey(c)){  // if the key exists, then it increments the frequency of that key
                        freqs.put(c, freqs.get(c)+1);
                    } else {
                        freqs.put(c, 1); // if the key does not exist, then it creates a new key for the character and sets the frequenct to 1
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // creates a new node that contains the character and its respective frequency
         for(Map.Entry<Character, Integer> entry : freqs.entrySet()){
            Node n = new Node(entry.getKey(), entry.getValue());
            pQueue.add(n); // adds node to the priority queue
        }

         // combine for huffmann trie
         while (pQueue.size() > 1){
            Node one = pQueue.poll(), two = pQueue.poll();
            Node combine = combine(one, two);
            pQueue.add(combine);
         }
         // the root is the last node in the priority queue
         Node root = pQueue.poll();
         HashMap<Character, String> dictionary = new HashMap<>();
        // encodes the characters and puts them in the dictionary
         encoding(root, dictionary, "");

        //  // prints out the key-value pairs for the dictionary codes (used for debugging)
        //  for (Map.Entry<Character, String> entry : dictionary.entrySet()) {
        //     System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        // }     
        // // prints out the key-value pairs for the frequency (used for debugging)
        // for (Map.Entry<Character, Integer> entry : freqs.entrySet()) {
        //     System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        // }     

        // New bin file
        // read each encoding out of dictionary
        FileOutputStream newbin = new FileOutputStream("project4\\book" + booknum + ".bin"); // creates a new bin file for each book
        Scanner sc2 = new Scanner(book);
        String blah = new String("");
        while (sc2.hasNextLine()) { // while the book still has a line            
            char[] chars = sc2.nextLine().toCharArray();
            for (char c : chars){
                counter++; // counts each character
                blah += dictionary.get(c); // adds each code to the string
            }
             if(sc2.hasNextLine()) { // if there is a next line,
                blah += dictionary.get('\n');  // adds the code for a line break
                counter++; // counts the line break
            }
            while (blah.length()>=8){ // while there is still a byte in the line,
                String eight = blah.substring(0,8); // takes the first byte
                blah = blah.substring(8); // reduces the string by 8 bits
                try{
                    newbin.write(toByte(eight)); // converts the byte to an actual byte to be stored in the new bin file
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }

        // adds in the last byte
        while(blah.length() < 8){
            blah = blah + "0";
        }
        // writes the last byte
        try{
            newbin.write(toByte(blah));
            newbin.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 

        // Decompress Bin file
        // reads the book and gets all of the bytes
        byte[] moreBytes = Files.readAllBytes(Paths.get("project4\\book" + booknum + ".bin"));
        decode(root, moreBytes, booknum, counter);
    
        long elapsed = System.currentTimeMillis() - start; // captures the elapsed time
        System.out.println(elapsed); // elapsed time in milliseconds
        System.out.println(elapsed/1000); // elapsed time in seconds (truncated)
        sc.close();
        sc2.close();
        }
      }

     public static void encoding(Node curr, HashMap<Character, String> dict, String bits){
        if(!curr.isLeaf()){ // if the current node is not the leaf
            encoding(curr.left, dict, bits + "0"); // each left node is assigned 0
            encoding(curr.right, dict, bits + "1"); // each right node is assigned 1
        }else {
            // if the current node is a leaf node, then it stores the data and the encoding into the dictionary
            dict.put(curr.data, bits);
        }
     }

     public static void decode(Node root, byte[] bytes, int booknum, long counter) throws FileNotFoundException{
        Node curr = root;
        FileOutputStream newtxt = new FileOutputStream("project4\\book" + booknum + "copy.txt");
        // while the bits have not all been read
        for (int i = 0; i < bytes.length; i++) { // traverses through each byte
            String bits = toString(bytes[i]); // converts each byte into a string of bits
            for (int bit = 0; bit < 8; bit++) { // for each bit in the byte
                if(curr.isLeaf()){ // if the current node is a leaf, then the data is appended
                    // System.out.println("counter: "+counter);
                    if (counter == 0){ // once the counter has hit zero, the algorithm stops to not add extra characters
                        return;
                    }
                    try{
                        newtxt.write(("" + curr.data).getBytes()); // writes the data into the new txt file
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                    curr = root; // restarts at the root
                    counter--; // subtracts the counter
                    bit -= 1; // reduces the bit count so that it doesn't skip over a bit
                    continue;
                }
                // if the bit is 0, then go to the left
                // if the bit is not 0, then go to the right
                if(bits.charAt(bit) == '0'){
                    curr = curr.left;
                } else {
                    curr = curr.right;
                }
            }
        }
        return;
    }

    // Since I could not use getBytes() since it look up 8 times more storage
    // This uses the 2's complement to write each byte
     public static byte toByte(String line){
        byte b = 0;
        b += Integer.parseInt(line.substring(7, 8));
        b += 2*Integer.parseInt(line.substring(6, 7));
        b += 4*Integer.parseInt(line.substring(5, 6));
        b += 8*Integer.parseInt(line.substring(4, 5));
        b += 16*Integer.parseInt(line.substring(3, 4));
        b += 32*Integer.parseInt(line.substring(2, 3));
        b += 64*Integer.parseInt(line.substring(1, 2));
        b -= 128*Integer.parseInt(line.substring(0, 1));
        return b;
     }

     // takes in a byte and turns it into a string to be able to decode
     public static String toString(byte num){
        String str = "";
        if (num < 0){
            str += "1";
            num += 128;
        } else {
            str += "0";
        }
        str += num/64;
        str += (num % 64)/32;
        str += (num % 32)/16;
        str += (num % 16)/8;
        str += (num % 8)/4;
        str += (num % 4)/2;
        str += num % 2;
        return str;
     }

     // Node
     public static class Node implements Comparable<Node>{
        int freq;
        char data;
        Node left;
        Node right;

        public Node(char data, int freq){
            this.data = data;
            this.freq = freq;
        }

        // returns the frequency of the node
        public int getfreq(){
            return freq;
        }

        // checks if the current node is a leaf if it does not have a left or a right node
        public boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        @Override
        public int compareTo(Node o) {
            return this.getfreq() - o.getfreq();
        }
        
     }

     // combines two nodes together to build the trie and combines the frequencies
     public static Node combine(Node one, Node two){
        Node combined = new Node((char)'$', one.freq+two.freq);
        // the two combined nodes are now the left and right children
        combined.left = one;
        combined.right = two;
        return combined;
     }
    }
