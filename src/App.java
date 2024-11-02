import java.util.Arrays;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        scanner.close();
        String message = "absentmindedness";
        byte[] bytes = message.getBytes();
        String[][] hexStrings = new String[4][4];
        int count = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                hexStrings[i][j] = Integer.toHexString(bytes[count]);
                count++;
            }
        }
        for(String[] strings: hexStrings){
            System.out.println(Arrays.toString(strings));
        }
        System.out.println();
        performSubstitution(hexStrings);
        for(String[] strings: hexStrings){
            System.out.println(Arrays.toString(strings));
        }
        System.out.println();
        shiftRows(hexStrings);
        for(String[] strings: hexStrings){
            System.out.println(Arrays.toString(strings));
        }

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
    
    public static void performSubstitution(String[][] array){
        SBox sBox = new SBox();
        // String[][] outArray = new String[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                array[i][j] = sBox.getSBoxValue(array[i][j]);
            }
        }
    }

    public static void shiftRows(String[][] array){
        for(int i = 0; i < 4; i++){
            rotate(array[i], i);
        }
    }

    public static void rotate(String[] array, int n){
        String[] temp = new String[4];
        for(int i = 0; i < 4; i++){
            temp[i] = array[i];
        }
        for(int i = 0; i < 4; i++){
            array[i] = temp[(i+n)%4];
        }
    }
}
