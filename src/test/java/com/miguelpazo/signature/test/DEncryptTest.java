/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguelpazo.signature.test;

import com.miguelpazo.signature.CertificateUtil;
import java.io.File;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Miguel Pazo (http://miguelpazo.com/)
 */
public class DEncryptTest {

    private CertificateUtil certUtil;

    public DEncryptTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        certUtil = CertificateUtil.getInstance();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void main() throws Exception {
        String path = "D:\\__Software\\openssl-1.0.2-x64_86-win64\\ca\\";
        String data = "Hola mundo í ó ñ";

        PrivateKey privKey = certUtil.loadPrivKey(new File(path + "private.key"));
        PublicKey publicKey = certUtil.loadPublicKey(new File(path + "public.key"));

        String dataEncrypted = encryptData(publicKey, data);
        String dataDecrypted = decryptData(privKey, dataEncrypted);

        System.out.println("Data encryptes: " + dataEncrypted);
        System.out.println("Data decryptes: " + dataDecrypted);
    }

    public String encryptData(Key pKey, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pKey);

        byte[] encrypted = cipher.doFinal(data.getBytes());
        byte[] encryptedVal = Base64.encode(encrypted);

        return new String(encryptedVal);
    }

    public String decryptData(Key pKey, String dataEncrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pKey);

        byte[] decodedBytes = Base64.decode(dataEncrypt.getBytes());
        byte[] original = cipher.doFinal(decodedBytes);

        return new String(original);
    }
}
