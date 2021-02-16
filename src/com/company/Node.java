package com.company;

import java.util.Date;

public class Node {
    public String hash;
    public String prevHash;

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
}
