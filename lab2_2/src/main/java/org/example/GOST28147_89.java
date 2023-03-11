package org.example;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class GOST28147_89 {

    public static String[][] change = new String[8][16];

    public static String encrypt(byte[] data, byte[] key) {
        initChange();
        byte[] xor32 = addModulo32(Arrays.copyOfRange(data, 0, 4), key);
        boolean[] bits = bytesToBits(xor32);
        String[] s = genS(bits);
        String[] changed = replace(s);
        String cyphered = toLeft11(changed);
        boolean[] bits1 = bytesToBits(Arrays.copyOfRange(data, 4, 8));
        String[] s1 = genS(bits1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s1.length; i++) {
            sb.append(s1[i]);
        }
        String xor = xor(cyphered, sb.toString());
        sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            sb.append(s[i]);
        }
        String res = xor + sb;
        BigInteger bigInteger = new BigInteger(res, 2);
        byte[] byteArray = bigInteger.toByteArray();
        return new String(byteArray);
    }

    public static byte[] addModulo32(byte[] a, byte[] b) {
        int carry = 0;
        byte[] result = new byte[a.length];
        for (int i = a.length - 1; i >= 0; i--) {
            int sum = (a[i] & 0xff) + (b[i] & 0xff) + carry;
            result[i] = (byte) (sum & 0xff);
            carry = sum >>> 8;
        }
        return result;
    }

    public static boolean[] bytesToBits(byte[] bytes) {
        boolean[] bits = new boolean[bytes.length * 8];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            int bitIndex = i * 8;
            for (int j = 0; j < 8; j++) {
                bits[bitIndex + j] = ((b >> (7 - j)) & 0x01) == 1;
            }
        }
        return bits;
    }

    public static String[] genS(boolean[] bits) {
        String[] res = new String[8];
        int c = 0;
        for (int i = 0; i < 32; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                if (i == 32) {
                    res[7] = sb.toString();
                    break;
                }
                sb.append(bits[i] ? '1' : '0');
                i++;
            }
            res[c] = sb.toString();
            c++;
        }
        return res;
    }

    public static String[] replace(String[] initial) {
        String[] res = new String[8];
        for (int i = 0; i < 8; i++) {
            System.out.println(i);
            int col = Integer.parseUnsignedInt(initial[i], 2);
            res[i] = change[i][col];
        }
        return res;
    }

    public static void initChange() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 16; j++) {
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < 4; k++) {
                    sb.append("" + ThreadLocalRandom.current().nextInt(0, 2));
                }
                change[i][j] = sb.toString();
            }
        }
    }

    public static String toLeft11(String[] initial) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < initial.length; i++) {
            sb.append(initial[i]);
        }
        String preRes = sb.toString();
        int shiftAmount = 11;
        int bitLength = preRes.length();
        int shiftedIndex = (bitLength - shiftAmount % bitLength) % bitLength;
        String shiftedBits = preRes.substring(shiftedIndex) + preRes.substring(0, shiftedIndex);
        return shiftedBits;
    }

    public static String xor(String bitString1, String bitString2) {
        byte[] bytes1 = new BigInteger(bitString1, 2).toByteArray();
        byte[] bytes2 = new BigInteger(bitString2, 2).toByteArray();

// выполним операцию XOR над массивами байтов
        byte[] result = new byte[Math.max(bytes1.length, bytes2.length)];
        for (int i = 0; i < result.length; i++) {
            byte b1 = (i < bytes1.length) ? bytes1[i] : 0;
            byte b2 = (i < bytes2.length) ? bytes2[i] : 0;
            result[i] = (byte) (b1 ^ b2);
        }

// преобразуем результат в строку битов
        return new BigInteger(1, result).toString(2);
    }
}
