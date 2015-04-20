package crypto.util.signature.test;

import crypto.util.signature.CertificateUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSSignedGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Miguel Pazo (http://miguelpazo.com/)
 */
public class SignTextTest {

    private CertificateUtil certUtil;

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
        certUtil = CertificateUtil.getInstance();
    }

    @After
    public void tearDown() {
    }

//    @Test
    public void main() throws Exception {
        String path = "D:\\__Software\\openssl-1.0.2-x64_86-win64\\ca\\";
        File fCertPfx = new File(path + "cert.p12");
        File fCertPem = new File(path + "cert.crt");
        File dataCert = new File(path + "dataCert");
        File privKey = new File(path + "private.key");
        String keyPass = "";

        String data = "Hola mundo";

        signData(data, fCertPem, privKey, keyPass);
    }

//    public void signData(String text, File certFile, File privKeyFile, String keyPass) throws Exception {
////    CertStore               certs...
////      CMSSignedDataGenerator    gen = new CMSSignedDataGenerator();
////
////      gen.addSigner(privKey, cert, CMSSignedGenerator.DIGEST_SHA1);
////      gen.addCertificatesAndCRLs(certs);
////
////      CMSSignedData           data = gen.generate(content, "BC");
//
//        //Get certificate X.509
//        InputStream inStream = new FileInputStream(certFile);
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//        X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
//
//        List certList = new ArrayList();
//        certList.add(cert);
//        Store certs = new JcaCertStore(certList);
//
//        PrivateKey privKey = certUtil.loadPrivKey(privKeyFile);
//
//        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
//        gen.addSigner(privKey, cert, CMSSignedGenerator.DIGEST_SHA1);
//        gen.addCertificates(certs);
//
//        CMSTypedData msg = new CMSProcessableByteArray(text.getBytes());
//
//        CMSSignedData data = gen.generate(msg, "BC");
//
//        System.out.println(data.getEncoded());
//
//    }

    public void signData(String text, File certFile, File privKeyFile, String keyPass) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        
        //Get certificate X.509
        InputStream inStream = new FileInputStream(certFile);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);

        //Open private key
        PrivateKey privKey = certUtil.loadPrivKey(privKeyFile);

        //Sign
        Signature signature = Signature.getInstance("SHA1WithRSA", "BC");
        signature.initSign(privKey);
        signature.update(text.getBytes());

        //Build CMS
        List certList = new ArrayList();
        CMSTypedData msg = new CMSProcessableByteArray(signature.sign());
        certList.add(cert);

        Store certs = new JcaCertStore(certList);
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1WithRSA").setProvider("BC").build(privKey);
        gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(sha1Signer, cert));
        gen.addCertificates(certs);

        CMSSignedData sigData = gen.generate(msg, false);

        System.out.println(Base64.encode(sigData.getEncoded()));

    }
    
//    public boolean validateSign(byte[] content ,byte[] b64SignedContent){ 
//    try{ 
//    byte signedContent[] = Base64.decode(b64SignedContent); 
//    CMSSignedData signedData = new CMSSignedData(signedContent); 
//    
//    if(signedData.getSignedContent() ==null){ 
//    CMSProcessableByteArray process = new CMSProcessableByteArray(content); 
//    signedData = new CMSSignedData(process,signedContent); 
//    } 
//    CertStore certStore = signedData.getCertificatesAndCRLs("Collection", "BC"); 
//    Iterator signersIterator = signedData.getSignerInfos().getSigners().iterator(); 
//    while(signersIterator.hasNext()){ 
//    X509Certificate senderCertificate = null; 
//                SignerInformation signer = (SignerInformation) signersIterator.next(); 
//                Iterator senderCertificatesIt = certStore.getCertificates(signer.getSID()).iterator(); 
//                senderCertificate = (X509Certificate)senderCertificatesIt.next(); 
//                    return signer.verify(senderCertificate, "BC"); 
//    } 
//    } 
//    catch(CMSException e){ 
//    log.debug("error",e); 
//                } 
//    catch(Throwable e){ 
//    log.debug("Error en verificación de firma",e); 
//    return false; 
//    } 
//    return false; 
//    } 
}
