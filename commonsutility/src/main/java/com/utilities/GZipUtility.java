/*
 * @author
 *  Nidhi Chourasia created on 2018.
 */

package com.utilities;

import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

public class GZipUtility {

    //To unzip any zip File
    //Will keep both zipped and unzipped files on the location

    public String unzip(String sourceFilePath) throws IOException {
        String destinationFilePath = sourceFilePath.replace(".zip", "");
        Integer fileLength;
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(sourceFilePath));
             FileOutputStream fileOutputStream = new FileOutputStream(destinationFilePath);
        ) {
            byte[] buffer = new byte[1024];
            while ((fileLength = gzipInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, fileLength);
            }
        }
        return destinationFilePath;
    }

    public byte[] compress(byte[] data) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
        GzipCompressorOutputStream gzipCompressorOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);

        gzipCompressorOutputStream.write(data);
        gzipCompressorOutputStream.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        boolean isZipped = isZipped(bytes);
        System.out.println(isZipped);
        return bytes;
    }

    public String deCompress(byte[] compressedData) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line);
            }
            return output.toString();
        }
    }

    public static void main(String[] args) throws IOException {
        GZipUtility gzip = new GZipUtility();
        byte[] compress = gzip.compress(("Nidhi Chourasia").getBytes(StandardCharsets.UTF_8));
        System.out.println(Arrays.toString(compress));
        String decompressed = gzip.deCompress(compress);
        System.out.println(decompressed);
    }

    public static boolean isZipped(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }
}
