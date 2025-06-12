package com.coffee.coffee_diary.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class AccessToken {
    private final String userID;
    private final long exp;

    public AccessToken(String userID, long exp) {
        this.userID = userID;
        this.exp = exp;
    }

    private byte[] getPayload() {
        // Decode fields as bytes
        byte[] userIDBytes = this.userID.getBytes();
        byte[] expBytes = ByteBuffer.wrap(new byte[Long.BYTES]).putLong(this.exp).array();

        // Write bytes to a combined buffer
        byte[] payload = new byte[userIDBytes.length + Long.BYTES];
        System.arraycopy(userIDBytes, 0, payload, 0, userIDBytes.length);
        System.arraycopy(expBytes, 0, payload, userIDBytes.length, Long.BYTES);

        return payload;
    }

    public byte[] encode(String key) {
        try {
            // Encode payload
            Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
            byte[] encodedPayload = encoder.encode(this.getPayload());

            // Encode signature
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            hmacSha256.init(keySpec);
            byte[] encodedSignature = encoder.encode(hmacSha256.doFinal(encodedPayload));

            // Combine encoded payload and signature into a complete token
            byte[] encodedToken = new byte[encodedPayload.length + encodedSignature.length];
            System.arraycopy(encodedPayload, 0, encodedToken, 0, encodedPayload.length);
            System.arraycopy(encodedSignature, 0, encodedToken, encodedPayload.length, encodedSignature.length);

            return encodedToken;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return null;
        }
    }
}
