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

    // returns true if transaction is possible
    public boolean processTransaction() {
        if(!verifySignature()) {
            System.out.println("Transaction signature failed to verify");
            return false;
        }
        // gather all transaction inputs
        for(TransactionInput i : inputs) {
            i.UTXO = PropertyBlockchain.UTXOs.get(i.transactionOutputID);
        }

        // generate transaction outputs
        float balance = getInputsValue() - value;
        transactionID = calculateHash();
        outputs.add(new TransactionOutput(this.receiver, value, transactionID));
        outputs.add(new TransactionOutput(this.sender, balance, transactionID));

        //add outputs to Unspent list
        for(TransactionOutput o : outputs) {
            PropertyBlockchain.UTXOs.put(o.ID , o);
        }
        //remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            PropertyBlockchain.UTXOs.remove(i.UTXO.ID);
        }

        return true;
    }
    // returns sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            total += i.UTXO.value;
        }
        return total;
    }

    // returns sum of outputs:
    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }
}
