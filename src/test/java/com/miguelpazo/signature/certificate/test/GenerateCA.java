package com.miguelpazo.signature.certificate.test;

import com.miguelpazo.signature.*;
import com.miguelpazo.signature.model.Subject;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Miguel Pazo (http://miguelpazo.com/)
 */
public class GenerateCA {

    public GenerateCA() {
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
        String publicKey = path + "caPublic.key";
        String privateKey = path + "caPrivate.key";
        String csr = path + "reqCert.pem";
        String caCert = path + "caCert.crt";

        //Generate pair keys
        KeyPair keys = CertificateUtil.generatePairKeys(publicKey, privateKey);

        //Generate CSR (Certificate Signing Request)
        PKCS10CertificationRequest requestCert = CertificateUtil.generateCSR(keys, generateSubject(), csr);

        //Auto-Sign certificate with CA
        autoSignCSR(caCert, keys, requestCert);
    }

    public void autoSignCSR(String fileCertCA, KeyPair keys, PKCS10CertificationRequest requestCert) throws Exception {
        InputStream inStream = null;

        try {
            Calendar startDate = Calendar.getInstance();
            Calendar expiryDate = Calendar.getInstance();
            expiryDate.add(Calendar.YEAR, 1);

            BigInteger serialNumber = new BigInteger("123564879875416231576");

            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            X500Principal subjectName = new X500Principal(requestCert.getCertificationRequestInfo().getSubject().toString());

            certGen.setPublicKey(requestCert.getPublicKey());
            certGen.setSerialNumber(serialNumber);
            certGen.setIssuerDN(subjectName);
            certGen.setNotBefore(startDate.getTime());
            certGen.setNotAfter(expiryDate.getTime());
            certGen.setSubjectDN(subjectName);
            certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

            X509Certificate cert = certGen.generate(keys.getPrivate(), "BC");

            CertificateUtil.exportToPEM(cert, fileCertCA);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }

    private X500Principal generateSubject() {
        Subject subject = new Subject();
        subject.setCountry("PE");
        subject.setProvince("Lima");
        subject.setLocality("Lima");
        subject.setOrgName("MiguelPazo Company");
        subject.setOrgUnitName("Department of Certificates");
        subject.setCommonName("MiguelPazo");

        X500Principal subjectName = new X500Principal(subject.toString());

        return subjectName;
    }

}
