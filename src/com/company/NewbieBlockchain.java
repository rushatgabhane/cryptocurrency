package com.company;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class NewbieBlockchain {

    public static ArrayList <Node> blockchain = new ArrayList<Node>();
    public static int difficulty = 5;

    public static boolean isBlockchainValid(){
        Node currentBlock, prevBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        // loop through blockchain to check if hashes are valid
        for(int i=1; i<blockchain.size(); i++){
            currentBlock = blockchain.get(i);
            prevBlock = blockchain.get(i-1);
            if(!currentBlock.hash.equals(currentBlock.calculateHash())) return false;
            if(!currentBlock.prevHash.equals(prevBlock.hash)) return false;
            // check if block is mined
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        blockchain.add(new Node("0", "First Node"));
        blockchain.get(0).mineBlock(difficulty);
        blockchain.add(new Node(blockchain.get(blockchain.size()-1).hash, "Second Node"));
        blockchain.get(1).mineBlock(difficulty);
        blockchain.add(new Node(blockchain.get(blockchain.size()-1).hash, "Third Node"));
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("valid: " + isBlockchainValid());

        String blockchainJSON = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJSON);
    }
}
