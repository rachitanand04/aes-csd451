import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String message = "Two One Nine Two";
        String roundKey = "Thats my Kung Fu";
        // System.out.println("Enter 16 character plaintext");
        // message = scanner.nextLine();
        // System.out.println("Enter 16 character key");
        // roundKey = scanner.nextLine();
        scanner.close();

        byte[] bytes = message.getBytes();
        String[][] hexStrings = new String[4][4];
        String[][] temp = new String[4][4];
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                hexStrings[j][i] = Integer.toHexString(bytes[count]);
                temp[j][i] = Integer.toHexString(bytes[count]);
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

        System.out.println("Plaintext");
        hexPrint(temp);

        Encrypt encrypt = new Encrypt(true);
        encrypt.encryptString(hexStrings, keyStrings, roundKey);
        System.out.println("Ciphertext");
        hexPrint(hexStrings);

        Decrypt decrypt = new Decrypt(true);
        decrypt.decryptString(hexStrings, keyStrings, roundKey);
        System.out.println("Decrypted Message");
        hexPrint(hexStrings);
    }

    private static void hexPrint(String[][] message){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(message[j][i] + " ");
            }
        }
        System.out.println();
    }
}
