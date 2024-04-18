package com.benkitoucoders.ecommerce.utils;

import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressionUtil {

    public static byte[] compress(String data) {
        byte[] input = data.getBytes();
        byte[] output = new byte[input.length];

        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();
        int compressedLength = deflater.deflate(output);
        byte[] compressedData = new byte[compressedLength];
        System.arraycopy(output, 0, compressedData, 0, compressedLength);

        deflater.end();
        return compressedData;
    }

    public static String decompress(byte[] compressedData) {
        byte[] output = new byte[1024];

        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);
        StringBuilder decompressedData = new StringBuilder();

        try {
            while (!inflater.finished()) {
                int decompressedLength = inflater.inflate(output);
                decompressedData.append(new String(output, 0, decompressedLength));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inflater.end();
        }

        return decompressedData.toString();
    }
}
