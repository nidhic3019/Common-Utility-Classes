/*
 * @author
 *  Nidhi Chourasia created on 2018.
 */

package com.utilities;

import java.util.Base64;

public class Base64EncoderUtility {

    public byte[] encode(byte[] data) {
        return Base64.getEncoder().encode(data);
    }

    public byte[] decode(byte[] data) {
        return Base64.getDecoder().decode(data);
    }

    public String encodeToString(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
