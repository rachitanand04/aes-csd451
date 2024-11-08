import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.io.File;

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

        Encrypt imageEncrypt = new Encrypt(false);

        byte[] byteArray = extractBytes("Images/lock.ppm");
        // System.out.println(Arrays.toString(byteArrayToHexArray(byteArray)));
        PpmHeaderHandler handler = new PpmHeaderHandler();
        // System.out.println(Arrays.toString(byteArray));
        
        // System.out.println(Arrays.toString(byteArray));
        handler.extractHeader(byteArray);
        String[] hexArray = byteArrayToHexArray(byteArray);
        String[] string = new String[16];

        for (int i = hexArray.length%16; i < hexArray.length; i += 16) {
            for (int j = 0; j < 16; j++) {
                string[j] = hexArray[i + j];
            }
            count = 0;
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    hexStrings[k][j] = string[count];
                    count++;
                }
            }
            count = 0;
            imageEncrypt.encryptString(hexStrings, keyStrings, roundKey);
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    hexArray[i + count] = hexStrings[k][j];
                    count++;
                }
            }
        }
        // System.out.println(Arrays.toString(hexArray));
        count = 0;
        File file = new File("output.ppm");
        byte[] imgBytes = hexArrayToByteArray(hexArray);
        // System.out.println(Arrays.toString(byteArrayToHexArray(imgBytes)));
        byte[] modified = handler.replaceHeader(imgBytes);
        
        // System.out.println(modified.length);
        Files.write(file.toPath(), modified);
        System.out.println("Image encrypted successfully");

    }

    private static void hexPrint(String[][] message) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(message[j][i] + " ");
            }
        }
        System.out.println();
    }

    public static byte[] extractBytes(String image) throws IOException {
        File imgPath = new File(image);
        byte[] fileContent = Files.readAllBytes(imgPath.toPath());
        return fileContent;
    }

    private static String[] byteArrayToHexArray(byte[] byteArray) {
        String[] hexArray = new String[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            hexArray[i] = String.format("%02x", byteArray[i] & 0xFF);
        }

        return hexArray;
    }

    public static byte[] hexArrayToByteArray(String[] hexArray) {
        byte[] byteArray = new byte[hexArray.length];
        for (int i = 0; i < hexArray.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(hexArray[i], 16);
        }

        return byteArray;
    }
}
