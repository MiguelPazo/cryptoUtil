/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.util.signature.test;

import com.lowagie.text.pdf.PdfName;
import crypto.util.signature.CertificateUtil;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSSignedDataParser;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
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
public class SignDataTest {

    private CertificateUtil certUtil;

    public SignDataTest() {
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
        File fCertPfx = new File(path + "cert.p12");
        File dataSignedFile = new File(path + "dataSigned");
        String pass = "";

        String data = "Hola mundo í ó ñ";

        String dataSigned = signDataWithPfx(data, fCertPfx, pass, dataSignedFile);

        System.out.println("=========== DATA SIGNED ===========");
        System.out.println(dataSigned);

        verifyData(dataSigned);
    }

    public String signDataWithPfx(String data, File certPfx, String pass, File dataSignedFile) throws Exception {
        KeyStore ks = KeyStore.getInstance("pkcs12");
        ks.load(new FileInputStream(certPfx), pass.toCharArray());
        String alias = (String) ks.aliases().nextElement();

        PrivateKey key = (PrivateKey) ks.getKey(alias, pass.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);

        Signature signature = Signature.getInstance("SHA1WithRSA", "BC");
        signature.initSign(key);
//        signature.update(Base64.encode(data.getBytes()));
        signature.update(data.getBytes());

        //Build CMS
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
        List certList = new ArrayList();
        CMSTypedData msg = new CMSProcessableByteArray(signature.sign());
        certList.add(cert);

        Store certs = new JcaCertStore(certList);
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1WithRSA").setProvider("BC").build(key);
        gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(sha1Signer, cert));
        gen.addCertificates(certs);

        CMSSignedData sigData = gen.generate(msg, false);
        byte[] dataSigned = Base64.encode(sigData.getEncoded());
        String envelopedData = new String(dataSigned);

        certUtil.exportToFile(envelopedData, dataSignedFile);

        byte[] b = (byte[]) sigData.getSignedContent().getContent();
        String dataEncrypt = new String(Base64.encode(b));

        System.out.println("content => " + dataEncrypt);

        PublicKey pubKey = cert.getPublicKey();
        String dataFinal = certUtil.decryptData(pubKey, dataEncrypt);

        System.out.println(dataEncrypt);
//        System.out.println(dataFinal);

        return envelopedData;
    }

//    public void verifyData(String envelopedData) throws Exception {
//        CMSSignedData signedData = new CMSSignedData(Base64.decode(envelopedData.getBytes()));
//
//        CMSProcessable cmsProcesableContent = new CMSProcessableByteArray(Base64.decode(Sig_Bytes.getBytes()));
//        signedData = new CMSSignedData(cmsProcesableContent, Base64.decode(envelopedData.getBytes()));
//        // Verify signature
//        Store store = signedData.getCertificates();
//        SignerInformationStore signers = signedData.getSignerInfos();
//        Collection c = signers.getSigners();
//        Iterator it = c.iterator();
//        while (it.hasNext()) {
//            SignerInformation signer = (SignerInformation) it.next();
//            Collection certCollection = store.getMatches(signer.getSID());
//            Iterator certIt = certCollection.iterator();
//            X509CertificateHolder certHolder = (X509CertificateHolder) certIt.next();
//            X509Certificate certFromSignedData = new JcaX509CertificateConverter().setProvider(BC_PROVIDER).getCertificate(certHolder);
//            if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider(BC_PROVIDER).build(certFromSignedData))) {
//                System.out.println("Signature verified");
//            } else {
//                System.out.println("Signature verification failed");
//            }
//        }
//    }
    public void verifyData(String envelopedData) throws Exception {
        CMSSignedData cms = new CMSSignedData(Base64.decode(envelopedData.getBytes()));
        Store store = cms.getCertificates();

        SignerInformationStore signers = cms.getSignerInfos();
        Collection c = signers.getSigners();
        Iterator it = c.iterator();

//        Object content = cms.getSignedContent().getContent();
//        byte[] b = (byte[]) content;
//        byte[] dataSigned = Base64.encode(cms.getSignedContent());
        System.out.println(cms.getSignedContent());

        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            Collection certCollection = store.getMatches(signer.getSID());
            Iterator certIt = certCollection.iterator();

            X509CertificateHolder certHolder = (X509CertificateHolder) certIt.next();
            X509Certificate certFromSignedData = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);

            System.out.println("data => " + certFromSignedData.getSubjectDN().toString());

//            byte[] data = Base64.encode(signer.getContentDigest());
//            System.out.println(new String(data));
//            if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(certFromSignedData))) {
//                System.out.println("Signature verified");
//            } else {
//                System.out.println("Signature verification failed");
//            }
        }
    }
}
