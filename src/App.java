import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        scanner.close();
        String message = "Two One Nine Two";
        String roundKey = "Thats my Kung Fu";
        byte[] bytes = message.getBytes();
        String[][] hexStrings = new String[4][4];
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                hexStrings[j][i] = Integer.toHexString(bytes[count]);
                count++;
            }
        }
        bytes = roundKey.getBytes();
        String[][] keyStrings = new String[4][4];
        count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                keyStrings[j][i] = Integer.toHexString(bytes[count]);
                count++;
            }
        }

        Encrypt encrypt = new Encrypt();
        encrypt.encryptString(hexStrings, keyStrings, roundKey);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print((char) (Integer.parseInt(hexStrings[j][i], 16)));

            }
        }
        System.out.println();
    }
}
