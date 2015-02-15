package com.miguelpazo.signature.certificate.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Miguel Pazo (http://miguelpazo.com/)
 */
public class CertificateReaderTest {

    public CertificateReaderTest() {
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

    @Test
    public void main() throws Exception {
        String path = "D:\\__Software\\openssl-1.0.2-x64_86-win64\\ca\\";
        File cert = new File(path + "cert.p12");

        readCertificate(cert);
    }

    public void readCertificate(File certFile) throws Exception {
        InputStream inStream = null;

        try {
            inStream = new FileInputStream(certFile);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);

            System.out.println("getSubjectDN =>" + cert.getSubjectDN().toString());
            System.out.println("getSubjectAlternativeNames =>" + cert.getSubjectAlternativeNames());
            System.out.println("getSubjectUniqueID =>" + cert.getSubjectUniqueID());
            System.out.println("getSubjectX500Principal =>" + cert.getSubjectX500Principal());
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
}
