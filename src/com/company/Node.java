package com.company;

import java.util.ArrayList;
import java.util.Date;

public class Node {
    public String hash;
    public String prevHash;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    private String data;
    private long timestamp;
    private int nonce;

    public Node (String prevHash, String data){
        this.prevHash = prevHash;
        this.data = data;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }
    public String calculateHash(){
        return StringUtil.getHash(data + prevHash + Long.toString(timestamp) + Integer.toString(nonce));
    }
    public void mineBlock(int difficulty){
        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring(0, difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined " + hash);
    }
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((prevHash != "0")) {
            if((!transaction.processTransaction())) {
                System.out.println("Transaction failed to process. Transaction discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to block");
        return true;
    }
}
