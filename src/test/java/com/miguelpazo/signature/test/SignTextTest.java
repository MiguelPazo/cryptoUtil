package com.miguelpazo.signature.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
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
public class SignTextTest {

    public SignTextTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

//    @Test
    public void testMain() throws Exception {
        String path = "D:\\__Software\\openssl-1.0.2-x64_86-win64\\ca\\";
        String cert = path + "cert.p12";
        String keyPass = "";
        String dataCert = path + "dataCert";

        signText(cert, keyPass, dataCert);
    }

    public void signText(String certFile, String keyPass, String dataCertFile) throws Exception {
        String text = "This is a message";

        Security.addProvider(new BouncyCastleProvider());

        KeyStore ks = KeyStore.getInstance("pkcs12");
//        keystore = KeyStore.getInstance("JKS"); -> test with password
        ks.load(new FileInputStream(certFile), keyPass.toCharArray());
        String alias = (String) ks.aliases().nextElement();

        PrivateKey privKey = (PrivateKey) ks.getKey(alias, keyPass.toCharArray());
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

        CMSSignedDataGenerator sgen = new CMSSignedDataGenerator();
        sgen.addSigner(privKey, cert, CMSSignedDataGenerator.DIGEST_MD5);

        CMSSignedData csd = sgen.generate(new CMSProcessableByteArray(text.getBytes()), false, "BC");
        byte[] signedData = csd.getEncoded();
        byte[] signedDataB64 = Base64.encode(signedData);

        FileOutputStream out = new FileOutputStream(dataCertFile);
        out.write(signedDataB64);
        out.close();
    }
}
