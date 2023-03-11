package org.example;

public class Decryptor {
    //public static String decrypt(String data)
    public static byte[] gost2814789Decrypt(byte[] encryptedData, byte[] key, byte[] replacementTable) {
        // Convert the key to bytes
        byte[] keyBytes = new byte[32];
        System.arraycopy(key, 0, keyBytes, 0, Math.min(key.length, 32));

        // Generate the key schedule
        byte[] keySchedule = generateKeySchedule(keyBytes, replacementTable);

        // Divide the encrypted data into blocks
        int blockCount = encryptedData.length / 8;
        int[] blocks = new int[blockCount];
        for (int i = 0; i < blockCount; i++) {
            blocks[i] = bytesToInt(encryptedData, i * 8);
        }

        // Decrypt each block
        for (int i = 0; i < blockCount; i++) {
            int[] block = {blocks[i], blocks[i + 1]};

            // XOR the block with the second 8 bytes of the key schedule
            block[1] ^= keySchedule[8];

            // Perform the 32 rounds of decryption in reverse order
            for (int round = 31; round >= 0; round--) {
                int roundKey = keySchedule[round];
                int tmp = block[1];
                block[1] = (block[1] ^ f(block[0] + roundKey, replacementTable)) & 0xFFFFFFFF;
                block[1] = (block[1] << 11) | (block[1] >>> 21);
                block[1] ^= block[0];
                block[0] = tmp;
            }

            // XOR the block with the first 8 bytes of the key schedule
            block[0] ^= keySchedule[0];

            // Swap the two 4-byte halves of the block
            int tmp = block[0];
            block[0] = block[1];
            block[1] = tmp;

            // Write the decrypted block back to the array
            blocks[i] = block[0];
            blocks[i + 1] = block[1];
        }

        // Combine the decrypted blocks into a single byte array
        byte[] decryptedData = new byte[encryptedData.length];
        for (int i = 0; i < blockCount; i++) {
            intToBytes(blocks[i], decryptedData, i * 8);
        }

        return decryptedData;
    }

    private static int f(int x, byte[] replacementTable) {
        int result = 0;
        for (int i = 0; i < 8; i++) {
            result ^= replacementTable[(x >>> (4 * i)) & 0xF];
        }
        return result;
    }

    private static byte[] generateKeySchedule(byte[] key, byte[] replacementTable) {
        byte[] keySchedule = new byte[32];
        byte[] keyBytes = new byte[32];
        for (int i = 0; i < 32; i++) {
            keyBytes[i] = (byte) (key[i] & 0xFF);
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                keySchedule[i * 4 + j] = (byte) bytesToInt(keyBytes, i * 4 + j);
            }
        }
        for (int i = 0; i < 4; i++) {
            keySchedule[i + 8] = keySchedule[i];
        }
        for (int i = 4; i < 8; i++) {
            keySchedule[i + 8] = (byte) ((keySchedule[i + 4] + bytesToInt(replacementTable, i * 4)) & 0xFFFFFFFF);
        }
        for (int i = 8; i < 24; i++) {
            keySchedule[i + 8] = (byte) ((keySchedule[i - 8] + bytesToInt(replacementTable, (i % 8) * 4)) & 0xFFFFFFFF);
        }
        for (int i = 24; i < 32; i++) {
            keySchedule[i + 8] = (byte) ((keySchedule[i - 8] + bytesToInt(replacementTable, ((i - 24) % 8) * 4)) & 0xFFFFFFFF);
        }
        return keySchedule;
    }

    private static int bytesToInt(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) << 24) |
                ((bytes[offset + 1] & 0xFF) << 16) |
                ((bytes[offset + 2] & 0xFF) << 8) |
                (bytes[offset + 3] & 0xFF);
    }

    private static void intToBytes(int value, byte[] bytes, int offset) {
        bytes[offset] = (byte) (value >>> 24);
        bytes[offset + 1] = (byte) (value >>> 16);
        bytes[offset + 2] = (byte) (value >>> 8);
        bytes[offset + 3] = (byte) value;
    }
}
