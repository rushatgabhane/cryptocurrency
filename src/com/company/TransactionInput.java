// 20184147 - Rushat Gabhane - CSC
package com.company;

public class TransactionInput {
    public String transactionOutputID; // Reference to TransactionOutputs id
    public TransactionOutput UTXO; // unspent transaction output
    public TransactionInput (String transactionOutputID) {
        this.transactionOutputID = transactionOutputID;
    }
}
