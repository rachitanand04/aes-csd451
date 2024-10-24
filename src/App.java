import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String binary = toBinaryString(input);
        String text = toTexString(binary);
        System.out.println(text);
        scanner.close();
    }

    public static String toBinaryString(String message){
        byte[] bytes = message.getBytes();
        StringBuilder binary = new StringBuilder();
        for(byte b : bytes){
            int val = b;
            for(int i = 0; i < 8; i++){
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    public static String toTexString(String message){
        StringBuilder text = new StringBuilder();
        for(int i = 0; i < message.length(); i=i+8){
            int charCode = Integer.parseInt(message.substring(i, i+8),2);
            text.append((char)charCode);
        }
        return text.toString();
    }
}
