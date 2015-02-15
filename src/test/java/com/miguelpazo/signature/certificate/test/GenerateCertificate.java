package com.miguelpazo.signature.certificate.test;

import com.miguelpazo.signature.*;
import com.miguelpazo.signature.model.Subject;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Miguel Pazo (http://miguelpazo.com/)
 */
public class GenerateCertificate {

    private CertificateUtil certUtil;

    public GenerateCertificate() {
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
    public void testMain() throws Exception {
        String path = "D:\\__Software\\openssl-1.0.2-x64_86-win64\\ca\\";

        File csr = new File(path + "reqCert.pem");
        File caCert = new File(path + "caCert.crt");
        File caPrivateKey = new File(path + "caPrivate.key");

        File publicKey = new File(path + "public.key");
        File privateKey = new File(path + "private.key");
        File cert = new File(path + "cert.crt");

        //Generate pair keys
        KeyPair keys = certUtil.generatePairKeys(publicKey, privateKey);

        //Generate CSR (Certificate Signing Request)
        PKCS10CertificationRequest requestCert = certUtil.generateCSR(keys, generateSubject(), csr);

        //Sign certificate with CA
        certUtil.signWithCA(caCert, caPrivateKey, requestCert, cert);
    }

    private X500Principal generateSubject() throws IOException {
        Subject subject = new Subject();
        subject.setCountry("PE");
        subject.setProvince("Lima");
        subject.setLocality("San Juan de Lurigancho");
        subject.setCommonName("Miguel Rodrigo Pazo Sánchez");

        X500Principal subjectName = new X500Principal(subject.toString());

        return subjectName;
    }

}
