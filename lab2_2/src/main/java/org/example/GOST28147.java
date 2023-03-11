package org.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class GOST28147 {

    public static void save(byte[] data, String outputFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(data);
        }
    }
}
