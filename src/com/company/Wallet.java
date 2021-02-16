package com.company;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //only UTXOs owned by this wallet.

    public Wallet(){
        generateKeyPair();
    }
    public void generateKeyPair(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Transaction sendFund(PublicKey _receiver, float value) {
        if(getBalance() < value) {
            System.out.println("Not enough funds to send transaction. Transaction aborted.");
            return null;
        }

        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
        float total = 0;

        for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.ID));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _receiver , value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs){
            UTXOs.remove(input.transactionOutputID);
        }
        return newTransaction;
    }
    public float getBalance() {
        float total = 0;
        for(Map.Entry<String, TransactionOutput> item : PropertyBlockchain.UTXOs.entrySet()) {
            TransactionOutput UXTO = item.getValue();
            if(UXTO.isMine(publicKey)) {
                total += UXTO.value;
            }
        }
        return total;
    }

}
