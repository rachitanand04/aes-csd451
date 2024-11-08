import java.util.Arrays;

public class PpmHeaderHandler {

    private static final int HEADER_SIZE = 15;
    private static final int FOOTER_SIZE = 0;

    private byte[] header;

    public void extractHeader(byte[] ppmBytes) {
        if (ppmBytes.length < HEADER_SIZE + FOOTER_SIZE) {
            throw new IllegalArgumentException("Invalid ppm byte stream.");
        }
        header = Arrays.copyOfRange(ppmBytes, 0, HEADER_SIZE);

    }

    public byte[] replaceHeader(byte[] newppmBytes) {
        if (newppmBytes.length < HEADER_SIZE + FOOTER_SIZE) {
            throw new IllegalArgumentException("Invalid ppm byte stream.");
        }

        byte[] result = Arrays.copyOf(newppmBytes, newppmBytes.length);

        System.arraycopy(header, 0, result, 0, HEADER_SIZE);

        return result;
    }
}
