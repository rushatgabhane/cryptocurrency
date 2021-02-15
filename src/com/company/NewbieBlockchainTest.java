package com.company;

import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewbieBlockchainTest {

    @Test
    void testDetectionOfInvalidBlockchain() {
        NewbieBlockchain.blockchain.add(new Node("0", "First Node"));
        NewbieBlockchain.blockchain.add(new Node(NewbieBlockchain.blockchain.get(NewbieBlockchain.blockchain.size()-1).hash, "Second Node"));
        NewbieBlockchain.blockchain.add(new Node(NewbieBlockchain.blockchain.get(NewbieBlockchain.blockchain.size()-1).hash, "Third Node"));

        assertTrue(NewbieBlockchain.isBlockchainValid());

        // change a random node
        NewbieBlockchain.blockchain.set(0, new Node("0", "Hacked"));

        assertFalse(NewbieBlockchain.isBlockchainValid());
    }

}