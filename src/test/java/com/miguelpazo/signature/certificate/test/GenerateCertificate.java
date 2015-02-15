package com.miguelpazo.signature.certificate.test;

import com.miguelpazo.signature.*;
import com.miguelpazo.signature.model.Subject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
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
public class GenerateCertificate {

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
    }

    @After
    public void tearDown() {
    }

//    @Test
    public void testMain() throws Exception {
        String path = "D:\\__Software\\openssl-1.0.2-x64_86-win64\\ca\\";
        String csr = path + "reqCert.pem";
        String caCert = path + "caCert.crt";
        String caPrivateKey = path + "caPrivate.key";

        String publicKey = path + "public.key";
        String privateKey = path + "private.key";
        String cert = path + "cert.crt";

        //Generate pair keys
        KeyPair keys = CertificateUtil.generatePairKeys(publicKey, privateKey);

        //Generate CSR (Certificate Signing Request)
        PKCS10CertificationRequest requestCert = CertificateUtil.generateCSR(keys, generateSubject(), csr);

        //Sign certificate with CA
        signWithCA(caCert, caPrivateKey, requestCert, cert);
    }

    public void signWithCA(String caCertFile, String caPrivateKey, PKCS10CertificationRequest requestCert, String cert) throws Exception {
        InputStream inStream = null;

        try {
            inStream = new FileInputStream(caCertFile);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate caCert = (X509Certificate) cf.generateCertificate(inStream);

            Calendar startDate = Calendar.getInstance();
            Calendar expiryDate = Calendar.getInstance();
            expiryDate.add(Calendar.YEAR, 1);

            BigInteger serialNumber = new BigInteger("123564879875416231576");
            PrivateKey caKey = CertificateUtil.loadPrivKey(caPrivateKey);

            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            X500Principal subjectName = new X500Principal(requestCert.getCertificationRequestInfo().getSubject().toString());

            certGen.setPublicKey(requestCert.getPublicKey());
            certGen.setSerialNumber(serialNumber);
            certGen.setIssuerDN(caCert.getSubjectX500Principal());
            certGen.setNotBefore(startDate.getTime());
            certGen.setNotAfter(expiryDate.getTime());
            certGen.setSubjectDN(subjectName);
            certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

//            certGen.addExtension(X509Extensions.SubjectAlternativeName, false, new GeneralName(GeneralName.dNSName, "hola"));
            X509Certificate finalCert = certGen.generate(caKey, "BC");

            CertificateUtil.exportToPEM(finalCert, cert);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
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
