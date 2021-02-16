package com.company;

import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NewbieBlockchainTest {

    @Test
    @DisplayName("Should test if isBlockchainValid is working properly")
    void testDetectionOfInvalidBlockchain() {
        NewbieBlockchain.blockchain.add(new Node("0", "First Node"));
        NewbieBlockchain.blockchain.get(0).mineBlock(NewbieBlockchain.difficulty);
        NewbieBlockchain.blockchain.add(new Node(NewbieBlockchain.blockchain.get(NewbieBlockchain.blockchain.size()-1).hash, "Second Node"));
        NewbieBlockchain.blockchain.get(1).mineBlock(NewbieBlockchain.difficulty);
        NewbieBlockchain.blockchain.add(new Node(NewbieBlockchain.blockchain.get(NewbieBlockchain.blockchain.size()-1).hash, "Third Node"));
        NewbieBlockchain.blockchain.get(2).mineBlock(NewbieBlockchain.difficulty);

        assertTrue(NewbieBlockchain.isBlockchainValid());

        Random random = new Random();
        int index = random.nextInt(NewbieBlockchain.blockchain.size());
        // change a random node
        NewbieBlockchain.blockchain.set(index, new Node("0", "Hacked"));
        NewbieBlockchain.blockchain.get(index).mineBlock(NewbieBlockchain.difficulty);

        assertFalse(NewbieBlockchain.isBlockchainValid());
    }

}