package com.company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PropertyBlockchainTest {

    @Test
    @DisplayName("Should test if isBlockchainValid is working properly")
    void testDetectionOfInvalidBlockchain() {
        PropertyBlockchain.blockchain.add(new Node("0", "First Node"));
        PropertyBlockchain.blockchain.get(0).mineBlock(PropertyBlockchain.difficulty);
        PropertyBlockchain.blockchain.add(new Node(PropertyBlockchain.blockchain.get(PropertyBlockchain.blockchain.size()-1).hash, "Second Node"));
        PropertyBlockchain.blockchain.get(1).mineBlock(PropertyBlockchain.difficulty);
        PropertyBlockchain.blockchain.add(new Node(PropertyBlockchain.blockchain.get(PropertyBlockchain.blockchain.size()-1).hash, "Third Node"));
        PropertyBlockchain.blockchain.get(2).mineBlock(PropertyBlockchain.difficulty);

        assertTrue(PropertyBlockchain.isBlockchainValid());

        Random random = new Random();
        int index = random.nextInt(PropertyBlockchain.blockchain.size());
        // change a random node
        PropertyBlockchain.blockchain.set(index, new Node("0", "Hacked"));
        PropertyBlockchain.blockchain.get(index).mineBlock(PropertyBlockchain.difficulty);

        assertFalse(PropertyBlockchain.isBlockchainValid());
    }

}