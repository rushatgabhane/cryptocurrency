package com.company;

import com.google.gson.GsonBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PropertyBlockchain {

    public static ArrayList <Node> blockchain = new ArrayList<Node>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); // list of all unspent transactions.
    public static int difficulty = 5;

    public static Transaction genesisTransaction;

    public static Wallet regulatoryAuthority;
    public static Wallet walletAlice;
    public static Wallet walletBob;

    public static void main(String[] args) {

        // set bouncycastle as security provider
        Security.addProvider(new BouncyCastleProvider());

        regulatoryAuthority = new Wallet();
        walletAlice = new Wallet();
        walletBob = new Wallet();

        // hardcode 100% ownership of property to mediator
        genesisTransaction = new Transaction(regulatoryAuthority.publicKey, regulatoryAuthority.publicKey, 100f, null);
        genesisTransaction.generateSignature(regulatoryAuthority.privateKey);	 //manually sign the genesis transaction
        genesisTransaction.transactionID = "0"; //manually set the transaction id
        genesisTransaction.outputs.add(new TransactionOutput(
                genesisTransaction.receiver,
                genesisTransaction.value,
                genesisTransaction.transactionID)
        ); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.outputs.get(0).ID, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");
        Node genesis = new Node("0", "Regulatory Authority");
        genesis.addTransaction(genesisTransaction);
        addNode(genesis);

        System.out.println("reg: " + regulatoryAuthority.getBalance());


        //testing
        Node block1 = new Node(genesis.hash, "Tom");
        System.out.println("\nAlice's balance is: " + walletAlice.getBalance());
        System.out.println("\nAlice is Attempting to send shares (40%) to Bob...");
        block1.addTransaction(walletAlice.sendFunds(walletBob.publicKey, 40f));
        addNode(block1);
        System.out.println("\nAlice's balance is: " + walletAlice.getBalance());
        System.out.println("Bob's balance is: " + walletBob.getBalance());

        Node block2 = new Node(block1.hash, "Alice");
        System.out.println("\nAlice Attempting to send more funds (300%) than it has...");
        block2.addTransaction(walletAlice.sendFunds(walletBob.publicKey, 300f));
        addNode(block2);
        System.out.println("\nAlice's balance is: " + walletAlice.getBalance());
        System.out.println("Bob's balance is: " + walletBob.getBalance());

        Node block3 = new Node(block2.hash, "Bob");
        System.out.println("\nBob is Attempting to send funds (20%) to Alice...");
        block3.addTransaction(walletBob.sendFunds( walletAlice.publicKey, 20));
        System.out.println("\nAlice's balance is: " + walletAlice.getBalance());
        System.out.println("Bob's balance is: " + walletBob.getBalance());

        isBlockchainValid();

//        String blockchainJSON = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
//        System.out.println(blockchainJSON);
    }
    public static void addNode(Node newNode) {
        newNode.mineBlock(difficulty);
        blockchain.add(newNode);
    }
    public static boolean isBlockchainValid(){
        Node currentNode;
        Node previousNode;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(genesisTransaction.outputs.get(0).ID, genesisTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentNode = blockchain.get(i);
            previousNode = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentNode.hash.equals(currentNode.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousNode.hash.equals(currentNode.prevHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentNode.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for(int t=0; t <currentNode.transactions.size(); t++) {
                Transaction currentTransaction = currentNode.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputID);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputID);
                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.ID, output);
                }

                if( currentTransaction.outputs.get(0).receiver != currentTransaction.receiver) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if( currentTransaction.outputs.get(1).receiver != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }
}
