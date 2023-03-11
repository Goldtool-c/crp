package org.example;

import org.bouncycastle.crypto.engines.GOST28147Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithSBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        String plain = "12345678aa";
        String key = "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
        System.out.println(key.getBytes().length);
        GOST28147Engine engine = new GOST28147Engine();//qweasdzxqweasdzxqweasdzxqweasdzx
        GOST28147_89.initChange();
        //{}
        byte[][] sboxByte = {{4, 3, 5, 3, 1, 2, 9, 8, 4, 3, 5, 3, 1, 2, 9, 8},
                {4, 3, 5, 3, 1, 2, 9, 8, 4, 3, 5, 3, 1, 2, 9, 8},
                {4, 3, 5, 3, 1, 2, 9, 8, 4, 3, 5, 3, 1, 2, 9, 8},
                {4, 3, 5, 3, 1, 2, 9, 8, 4, 3, 5, 3, 1, 2, 9, 8},
                {4, 3, 5, 3, 1, 2, 9, 8, 4, 3, 5, 3, 1, 2, 9, 8},
                {4, 3, 5, 3, 1, 2, 9, 8, 4, 3, 5, 3, 1, 2, 9, 8},
                {4, 3, 5, 3, 1, 2, 9, 8, 4, 3, 5, 3, 1, 2, 9, 8},
                {4, 3, 5, 3, 1, 2, 9, 8, 4, 3, 5, 3, 1, 2, 9, 8},};
       /* for (int i = 0; i < GOST28147_89.change.length; i++) {
            for (int j = 0; j < GOST28147_89.change[i].length; j++) {
                //sboxByte[i][j] = GOST28147_89.change[i][j].getBytes();
                System.out.println(i + " " + j);
            }
        }
*/
        KeyParameter keyParameter = new KeyParameter(key.getBytes());
        int numRows = sboxByte.length;
        int numCols = sboxByte[0].length;

        byte[] oneDArray = new byte[numRows * numCols];

        int index = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                oneDArray[index++] = sboxByte[i][j];
            }
        }
        //ParametersWithSBox params = new ParametersWithSBox(keyParameter, "wafgauihveu94hwi33hgt4jg4ljb43gjhl3ggwjerlkgnhjk34hgl43gjlk3h4g4".getBytes());
        System.out.println("wafgauihveu94hwi33hgt4jg4ljb43gjhl3ggwjerlkgnhjk34hgl43gjlk3h4g4".length());
        boolean[] lalala = GOST28147_89.bytesToBits("wafgauihveu94hwi33hgt4jg4ljb43gjhl3ggwjerlkgnhjk34hgl43gjlk3h4g4".getBytes());
        byte[] corBox = new byte[128];
        int c = 0;
        for (int i = 0; i < lalala.length; i += 4) {
            StringBuilder sb = new StringBuilder();
            String lala = sb.append(lalala[i] ? '1' : '0').append(lalala[i + 1] ? '1' : '0').
                    append(lalala[i + 2] ? '1' : '0').append(lalala[i + 3] ? '1' : '0').toString();
            corBox[c] = (byte) Integer.parseInt(lala, 2);
            c++;
        }

        System.out.println(corBox.length);
        ParametersWithSBox params = new ParametersWithSBox(keyParameter, corBox);
        engine.init(true, params);
        byte[] ciphertext = new byte[plain.getBytes().length];
        System.out.println("Original message: " + plain);
        engine.processBlock(ciphertext, 0, plain.getBytes(), 0);
        System.out.println("Encrypted message: " + new String(ciphertext));
        System.out.println("Decrypted message: " + plain);
        //System.out.println(encryptedData);
        /*String encryptedData = GOST28147_89.encrypt(decrypted.getBytes(), key.getBytes());*/
        GOST28147.save(new String(ciphertext).getBytes(), "src/main/java/org/example/оutput.txt");
    }

}

