import java.util.Arrays;

public class Encrypt {
    private Boolean printSteps;

    private static final int[][] Rcon = {
            { 0x01, 0x00, 0x00, 0x00 },
            { 0x02, 0x00, 0x00, 0x00 },
            { 0x04, 0x00, 0x00, 0x00 },
            { 0x08, 0x00, 0x00, 0x00 },
            { 0x10, 0x00, 0x00, 0x00 },
            { 0x20, 0x00, 0x00, 0x00 },
            { 0x40, 0x00, 0x00, 0x00 },
            { 0x80, 0x00, 0x00, 0x00 },
            { 0x1b, 0x00, 0x00, 0x00 },
            { 0x36, 0x00, 0x00, 0x00 }
    };

    public Encrypt(Boolean printSteps) {
        this.printSteps = printSteps;
    }

    public void encryptString(String[][] message, String[][] key, String keyString) {
        addRoundKey(message, key);
        printState(message, "Add key");
        String[][] expandedKeys = keyExpansion(keyString);
        for (int i = 0; i < 9; i++) {
            if(printSteps) System.out.println("Round " + (i + 1));

            performSubstitution(message);
            printState(message,"Substitution");

            shiftRows(message);
            printState(message, "Shift rows");

            mixColumns(message);
            printState(message, "Mix Columns");

            addRoundKey(message, expandedKeys, i + 1);
            printState(message, "addRoundKey");
        }
        if(printSteps) System.out.println("Round 10");
        performSubstitution(message);
        printState(message,"Substitution");

        shiftRows(message);
        printState(message,"Shift rows");

        addRoundKey(message, expandedKeys, 10);
        printState(message,"Add round key");
    }

    private void addRoundKey(String[][] state, String[][] roundKey) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = Integer.toHexString(
                        Integer.parseInt(state[i][j], 16) ^
                                Integer.parseInt(roundKey[i][j], 16));
            }
        }
    }

    private void addRoundKey(String[][] state, String[][] roundKey, int round) {
        String[][] key = new String[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                key[j][i] = roundKey[(round * 4) + i][j];
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = Integer.toHexString(
                        Integer.parseInt(state[i][j], 16) ^
                                Integer.parseInt(key[i][j], 16));
            }
        }
    }

    private String[][] keyExpansion(String key) {
        byte[] keyBytes = key.getBytes();
        String[][] expandedKeys = new String[44][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                expandedKeys[i][j] = Integer.toHexString(keyBytes[i * 4 + j] & 0xff);
            }
        }
        for (int i = 4; i < 44; i++) {
            String[] temp = expandedKeys[i - 1];
            if (i % 4 == 0) {
                temp = xorWords(subWord(rotWord(temp)), Rcon[(i / 4) - 1]);
            }
            expandedKeys[i] = xorWords(expandedKeys[i - 4], temp);
        }
        return expandedKeys;
    }

    private String[] rotWord(String[] word) {
        String[] rotatedWord = new String[4];
        for (int i = 0; i < 4; i++) {
            rotatedWord[i] = word[(i + 1) % 4];
        }
        return rotatedWord;
    }

    private String[] subWord(String[] word) {
        SBox sBox = new SBox();
        for (int i = 0; i < 4; i++) {
            word[i] = sBox.getSBoxValue(word[i]);
        }
        return word;
    }

    private String[] xorWords(String[] word1, int[] word2) {
        String[] result = new String[4];
        for (int i = 0; i < 4; i++) {
            result[i] = Integer.toHexString(
                    Integer.parseInt(word1[i], 16) ^ word2[i]);
        }
        return result;
    }

    private String[] xorWords(String[] word1, String[] word2) {
        String[] result = new String[4];
        for (int i = 0; i < 4; i++) {
            result[i] = Integer.toHexString(
                    Integer.parseInt(word1[i], 16) ^ Integer.parseInt(word2[i], 16));
        }
        return result;
    }

    private void performSubstitution(String[][] array) {
        SBox sBox = new SBox();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                array[i][j] = sBox.getSBoxValue(array[i][j]);
            }
        }
    }

    private void shiftRows(String[][] array) {
        for (int i = 0; i < 4; i++) {
            rotate(array[i], i);
        }
    }

    private void rotate(String[] array, int n) {
        String[] temp = new String[4];
        for (int i = 0; i < 4; i++) {
            temp[i] = array[i];
        }
        for (int i = 0; i < 4; i++) {
            array[i] = temp[(i + n) % 4];
        }
    }

    private final int[][] mixColumnMatrix = {
            { 0x02, 0x03, 0x01, 0x01 },
            { 0x01, 0x02, 0x03, 0x01 },
            { 0x01, 0x01, 0x02, 0x03 },
            { 0x03, 0x01, 0x01, 0x02 }
    };

    private void mixColumns(String[][] state) {
        int[][] intState = new int[4][4];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                intState[r][c] = Integer.parseInt(state[r][c], 16);
            }
        }
        int[][] temp = new int[4][4];
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 4; r++) {
                temp[r][c] = gMul(mixColumnMatrix[r][0], intState[0][c]) ^
                        gMul(mixColumnMatrix[r][1], intState[1][c]) ^
                        gMul(mixColumnMatrix[r][2], intState[2][c]) ^
                        gMul(mixColumnMatrix[r][3], intState[3][c]);
            }
        }
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                state[r][c] = String.format("%02x", temp[r][c]);
            }
        }
    }

    private int gMul(int a, int b) {
        int p = 0;
        int hiBitSet;
        for (int counter = 0; counter < 8; counter++) {
            if ((b & 1) != 0) {
                p ^= a;
            }
            hiBitSet = (a & 0x80);
            a <<= 1;
            if (hiBitSet != 0) {
                a ^= 0x1b; // x^8 + x^4 + x^3 + x + 1
            }
            b >>= 1;
        }
        return p & 0xFF;
    }

    private void printState(String[][] message, String label) {
        if(printSteps){
            System.out.println(label);
            for (String[] strings : message) {
                System.out.println(Arrays.toString(strings));
            }
        }
    }
}
