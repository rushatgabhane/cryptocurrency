package com.company;

import java.security.*;
import java.util.ArrayList;

public class Transaction {
    public String transactionID; // hash of transaction
    public float value;
    public PublicKey sender;
    public PublicKey receiver;

    public byte[] signature; // This is to prevent anyone else spending funds in our wallet

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; // nos. of transactions generated

    public Transaction(PublicKey sender, PublicKey receiver, float value, ArrayList<TransactionInput> inputs) {
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;
        this.inputs = inputs;
    }

    // Signs the data with privateKey so it can't be tampered
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) +
                StringUtil.getStringFromKey(receiver) +
                Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    // verify signature
    boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) +
                StringUtil.getStringFromKey(receiver) +
                Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public String calculateHash(){
        sequence++;
        return StringUtil.getHash(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(receiver) +
                        Float.toString(value) +
                        sequence
                );
    }

}
