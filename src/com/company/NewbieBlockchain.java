package com.company;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class NewbieBlockchain {

    public static ArrayList <Node> blockchain = new ArrayList<Node>();

    public static boolean isBlockchainValid(){
        Node currentBlock, prevBlock;

        // loop through blockchain to check if hashes are valid
        for(int i=1; i<blockchain.size(); i++){
            currentBlock = blockchain.get(i);
            prevBlock = blockchain.get(i-1);
            if(!currentBlock.hash.equals(currentBlock.calculateHash())) return false;
            if(!currentBlock.prevHash.equals(prevBlock.hash)) return false;
        }
        return true;
    }

    public static void main(String[] args) {

        blockchain.add(new Node("0", "First Node"));
        blockchain.add(new Node(blockchain.get(blockchain.size()-1).hash, "Second Node"));
        blockchain.add(new Node(blockchain.get(blockchain.size()-1).hash, "Third Node"));

        String blockchainJSON = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJSON);
    }
}
