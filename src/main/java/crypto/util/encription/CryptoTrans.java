/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.util.encription;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author jlimachi
 */
public class CryptoTrans {

    private static CryptoTrans instance = null;
    private String iv = "fedcba9876543210"; // 16 bytes
    private String key = null;
    private Cipher cipher = null;
    private SecretKeySpec keySpec = null;
    private IvParameterSpec ivSpec = null;

    private CryptoTrans() {
        // Exists only to defeat instantiation.
    }

    public CryptoTrans(String key) throws Exception {
        this.key = key;
        int len = this.key.length();

        if (len < 16) {
            int addSpaces = 16 - len;
            for (int i = 0; i < addSpaces; i++) {
                this.key = this.key + " ";
            }
        } else {
            this.key = this.key.substring(0, 16);
        }

        this.keySpec = new SecretKeySpec(this.key.getBytes(), "AES");
        this.ivSpec = new IvParameterSpec(iv.getBytes());
        this.cipher = Cipher.getInstance("AES/CBC/NoPadding");
    }

    public static CryptoTrans getInstance(String key) throws Exception {
        if (instance == null) {
            instance = new CryptoTrans(key);
        }
        return instance;
    }

    public String encrypt(String data) throws Exception {
        int len = data.length();
        int q = len / 16;
        int addSpaces = ((q + 1) * 16) - len;

        for (int i = 0; i < addSpaces; i++) {
            data = data + " ";
        }

        this.cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());

        return bytesToHex(encrypted);
    }

    public String decrypt(String data) throws Exception {
        this.cipher.init(Cipher.DECRYPT_MODE, this.keySpec, this.ivSpec);
        byte[] outText = this.cipher.doFinal(hexToBytes(data));

        String decrData = new String(outText).trim();
        return decrData;
    }

    private String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        } else {
            int len = data.length;
            String str = "";
            for (int i = 0; i < len; i++) {
                if ((data[i] & 0xFF) < 16) {
                    str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
                } else {
                    str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
                }
            }
            return str;
        }
    }

    private byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

}