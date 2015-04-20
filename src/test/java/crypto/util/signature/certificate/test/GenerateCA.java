package crypto.util.signature.certificate.test;

import crypto.util.signature.CertificateUtil;
import crypto.util.signature.model.Subject;
import java.io.File;
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
public class GenerateCA {

    private CertificateUtil certUtil;

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
        certUtil = CertificateUtil.getInstance();
    }

    @After
    public void tearDown() {
    }

//    @Test
    public void main() throws Exception {
        String path = "D:\\__Software\\openssl-1.0.2-x64_86-win64\\ca\\";

        File publicKey = new File(path + "caPublic.key");
        File privateKey = new File(path + "caPrivate.key");
        File csr = new File(path + "reqCert.pem");
        File caCert = new File(path + "caCert.crt");

        //Generate pair keys
        KeyPair keys = certUtil.generatePairKeys(publicKey, privateKey);

        //Generate CSR (Certificate Signing Request)
        PKCS10CertificationRequest requestCert = certUtil.generateCSR(keys, generateSubject(), csr);

        //Auto-Sign certificate with CA
        certUtil.autoSignCSR(caCert, keys, requestCert);
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
