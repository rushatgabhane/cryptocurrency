package com.company;

import java.util.Date;

public class Node {
    public String hash;
    public String prevHash;

    private String data;
    private long timestamp;

    public Node (String prevHash, String data){
        this.prevHash = prevHash;
        this.data = data;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }
    public String calculateHash(){
        return Hash.getHash(data + prevHash + Long.toString(timestamp));
    }
}
