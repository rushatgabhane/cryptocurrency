// 20184147 - Rushat Gabhane - CSC
package com.company;

import java.security.PublicKey;

public class TransactionOutput {
    public String ID;
    public PublicKey receiver; // owner of new coins
    public float value;
    public String parentTransactionId;

    public TransactionOutput(PublicKey receiver, float value, String parentTransactionId) {
        this.receiver = receiver;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.ID = StringUtil.getHash(
                StringUtil.getStringFromKey(receiver)+
                Float.toString(value)+
                parentTransactionId
        );
    }

    // check if coin is yours
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == receiver);
    }
}
