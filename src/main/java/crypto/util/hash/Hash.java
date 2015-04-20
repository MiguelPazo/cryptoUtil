/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.util.hash;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author jlimachi
 */
public class Hash {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String generateHash(File file) throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, FileNotFoundException {

        //converting file to bytes
        FileInputStream fis = new FileInputStream(file);
        //System.out.println(file.exists() + "!!");
        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        byte[] bytes = bos.toByteArray();

        //Here print bytes to String
        //System.out.println(Hash.getHexString(bytes));
        //Hash bytes
        Security.addProvider(new BouncyCastleProvider());
        Digest messageDigestObj = new SHA1Digest();
        byte[] digest = new byte[messageDigestObj.getDigestSize()];
        messageDigestObj.update(bytes, 0, bytes.length);
        messageDigestObj.doFinal(digest, 0);
        //System.out.println(new String(Hex.encode(digest)));

        return new String(Base64.encode(digest));
    }

    public static byte[] convertFileToBytes(File file) throws IOException {

        FileInputStream fis = new FileInputStream(file);
        //System.out.println(file.exists() + "!!");
        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        byte[] bytes = bos.toByteArray();

        String decoded = new String(bytes, "UTF-8");

        System.out.println(decoded);

        return bytes;

    }

    public static void convertBytesToFile(byte[] bytes, File file) throws FileNotFoundException, IOException {
        //below is the different part
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.flush();
        fos.close();

    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static final char[] BYTE2HEX = ("000102030405060708090A0B0C0D0E0F"
            + "101112131415161718191A1B1C1D1E1F"
            + "202122232425262728292A2B2C2D2E2F"
            + "303132333435363738393A3B3C3D3E3F"
            + "404142434445464748494A4B4C4D4E4F"
            + "505152535455565758595A5B5C5D5E5F"
            + "606162636465666768696A6B6C6D6E6F"
            + "707172737475767778797A7B7C7D7E7F"
            + "808182838485868788898A8B8C8D8E8F"
            + "909192939495969798999A9B9C9D9E9F"
            + "A0A1A2A3A4A5A6A7A8A9AAABACADAEAF"
            + "B0B1B2B3B4B5B6B7B8B9BABBBCBDBEBF"
            + "C0C1C2C3C4C5C6C7C8C9CACBCCCDCECF"
            + "D0D1D2D3D4D5D6D7D8D9DADBDCDDDEDF"
            + "E0E1E2E3E4E5E6E7E8E9EAEBECEDEEEF"
            + "F0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF").toCharArray();

    ; 

  public static String getHexString(byte[] bytes) {
        final int len = bytes.length;
        final char[] chars = new char[len << 1];
        int hexIndex;
        int idx = 0;
        int ofs = 0;
        while (ofs < len) {
            hexIndex = (bytes[ofs++] & 0xFF) << 1;
            chars[idx++] = BYTE2HEX[hexIndex++];
            chars[idx++] = BYTE2HEX[hexIndex];
        }
        return new String(chars);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {

        String uriJar1 = "D:\\Eva Seleccion\\installevanselect.exe";
        //String uriJar2 = "D:\\EVA\\02_Proceso_de_Desarrollo\\10_Fuentes\\EVA_Nacion\\evaSeleccion\\target\\evaSeleccion-1.0.jar";
        //String uriJar2 = "D:\\EVA\\02_Proceso_de_Desarrollo\\10_Fuentes\\EVA_Nacion\\evaPerso\\target\\evaPerso-1.1.jar";

        String hash = Hash.generateHash(new File(uriJar1));
        System.out.println(hash);
        //SecurityUtils.convertBytesToFile(bytes, new File(uriJar2));
        //SecurityUtils.generateHash(new File(uriJar2));

    }

}
