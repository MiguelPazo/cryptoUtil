/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.util.encription;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.PaddedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;

/**
 *
 * @author jlimachi
 */
public class Crypto {

    private static Crypto instance = null;

    protected Crypto() {
        // Exists only to defeat instantiation.
    }

    public static Crypto getInstance(String key) {
        if (instance == null) {
            instance = new Crypto(key);
        }
        return instance;
    }

    private BufferedBlockCipher cipher;
    private KeyParameter key;

    // Initialize the cryptographic engine.
    // The key array should be at least 8 bytes long.
    public Crypto(byte[] key) {
        /*
         cipher = new PaddedBlockCipher(
         new CBCBlockCipher(
         new DESEngine() ) );
         */

        cipher = new PaddedBlockCipher(
                new CBCBlockCipher(
                        new BlowfishEngine()));

        this.key = new KeyParameter(key);
    }

    // Initialize the cryptographic engine.
    // The string should be at least 8 chars long.
    public Crypto(String key) {
        this(key.getBytes());
    }

    // Private routine that does the gritty work.
    private byte[] callCipher(byte[] data)
            throws CryptoException {
        int size
                = cipher.getOutputSize(data.length);
        byte[] result = new byte[size];
        int olen = cipher.processBytes(data, 0,
                data.length, result, 0);
        olen += cipher.doFinal(result, olen);

        if (olen < size) {
            byte[] tmp = new byte[olen];
            System.arraycopy(
                    result, 0, tmp, 0, olen);
            result = tmp;
        }

        return result;
    }

    public String encrypt(String data) throws CryptoException {

        String result = null;

        if (data != null) {
            cipher.init(true, key);
            byte[] b = callCipher(data.getBytes());
            result = new String(Base64.encode(b));
        } else {
            return new String(new byte[0]);
        }
        return result;
    }

    public String encrypt(byte[] data) throws CryptoException {

        String result = null;

        if (data.length != 0) {
            cipher.init(true, key);
            byte[] b = callCipher(data);
            result = new String(Base64.encode(b));
        } else {
            return new String(new byte[0]);
        }
        return result;
    }

    public String decrypt(String data) throws CryptoException {

        String result = null;

        if (data != null) {
            cipher.init(false, key);
            byte[] decode = Base64.decode(data);
            byte[] b = callCipher(decode);
            result = new String(b);
        } else {
            return new String(new byte[0]);
        }
        return result;
    }

    public String decrypt(byte[] data) throws CryptoException {

        String result = null;

        if (data.length != 0) {
            cipher.init(false, key);
            byte[] decode = Base64.decode(data);
            byte[] b = callCipher(decode);
            result = new String(b);
        } else {
            return new String(new byte[0]);
        }
        return result;
    }

    public static void main(String[] args) throws CryptoException {
        Crypto crypto = Crypto.getInstance("EVA <3");
        System.out.println(crypto.encrypt("this is a message"));
        System.out.println(crypto.encrypt("this is a message".getBytes()));
        System.out.println(crypto.decrypt(crypto.encrypt("this is a message")));
        System.out.println(crypto.decrypt(crypto.encrypt("this is a message").getBytes()));

    }
}
