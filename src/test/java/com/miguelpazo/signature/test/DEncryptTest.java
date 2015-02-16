/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguelpazo.signature.test;

import com.miguelpazo.signature.CertificateUtil;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

        File encryptFile = new File(path + "encryptFile");
        File decryptFile = new File(path + "decryptFile");
        
        PrivateKey privKey = certUtil.loadPrivKey(new File(path + "private.key"));
        PublicKey publicKey = certUtil.loadPublicKey(new File(path + "public.key"));

        String dataEncrypted = certUtil.encryptData(publicKey, data, encryptFile);
        String dataDecrypted = certUtil.decryptData(privKey, dataEncrypted, decryptFile);

        System.out.println("Data encryptes: " + dataEncrypted);
        System.out.println("Data decryptes: " + dataDecrypted);
    }

}
