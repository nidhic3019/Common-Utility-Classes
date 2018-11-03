/*
 * @author
 *  Nidhi Chourasia created on 2018.
 */

package com.utilities;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Bz2Utility {

    public byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BZip2CompressorOutputStream bZip2CompressorOutputStream = new BZip2CompressorOutputStream(byteArrayOutputStream);

        byteArrayOutputStream.write(data);
        bZip2CompressorOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] deCompress(byte[] data) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BZip2CompressorInputStream bZip2CompressorInputStream = new BZip2CompressorInputStream(new ByteArrayInputStream(data));

        IOUtils.copy(bZip2CompressorInputStream, byteArrayOutputStream);
        bZip2CompressorInputStream.close();

        return byteArrayOutputStream.toByteArray();
    }
}
