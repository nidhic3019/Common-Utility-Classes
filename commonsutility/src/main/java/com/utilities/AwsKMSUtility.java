/*
 *  @author
 *  Nidhi Chourasia created on 2018
 *
 */
package com.utilities;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static javax.crypto.Cipher.ENCRYPT_MODE;

/* Utility is used only when the message is Decrypted with Encoding Base64*/
public class AwsKMSUtility {
    private static final String KEYID = "arn:aws:kms:us-east-1:xxxxx:key/xxxxxxxxxxxxxxxxxxx";

    //These operations are designed to encrypt and decrypt data keys
    private AWSKMS awskms;

    public AwsKMSUtility() {
        this(AWSKMSClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build());
    }

    public AwsKMSUtility(AWSKMS awskms) {
        this.awskms = awskms;
    }

    public String decrypt(String base64EncodedValue) {
        DecryptRequest decryptRequest = new DecryptRequest()
                .withCiphertextBlob(
                        ByteBuffer.wrap(Base64.getDecoder()
                                .decode(base64EncodedValue)));
        DecryptResult decrypt = awskms.decrypt(decryptRequest);
        return String.valueOf(decrypt.getPlaintext().array());
    }

    public String encrypt(String key) {
        EncryptRequest encryptRequest = new EncryptRequest().withKeyId(KEYID).withPlaintext(
                ByteBuffer.wrap(Base64.getEncoder()
                        .encode(key.getBytes())));
        EncryptResult encrypt = awskms.encrypt(encryptRequest);
        return String.valueOf(encrypt.getCiphertextBlob().array());
    }

    public String encryptUsingCipher(String keyToEncrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        GenerateDataKeyRequest generateDataKeyRequest = new GenerateDataKeyRequest();
        generateDataKeyRequest.setKeyId(KEYID);
        //Length of the data encryptio key.generate 128-bit or 256-bit symmetric key
        generateDataKeyRequest.setKeySpec("AES_256");

        GenerateDataKeyResult generateDataKeyResult = awskms.generateDataKey(generateDataKeyRequest);
        ByteBuffer plaintext = generateDataKeyResult.getPlaintext();

        SecretKeySpec key = new SecretKeySpec(getByteArray(plaintext), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(ENCRYPT_MODE, key);
        byte[] encryptedKey = cipher.doFinal(keyToEncrypt.getBytes());

        return Base64.getEncoder().encodeToString(encryptedKey);
    }

    private static byte[] getByteArray(ByteBuffer b) {
        byte[] byteArray = new byte[b.remaining()];
        b.get(byteArray);
        return byteArray;
    }


}
