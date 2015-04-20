package crypto.util.signature.test;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfPKCS7;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import crypto.util.signature.CertificateUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Miguel Pazo (http://miguelpazo.com/)
 */
public class SignPdfTest {

    private CertificateUtil certUtil;

    public SignPdfTest() {
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
        File certPfx = new File(path + "cert.p12");
        String pass = "";

        File pdfToSign = new File(path + "ARCHIVO.pdf");
        File pdfSigned = new File(path + "ARCHIVO_FIRMADO.pdf");

        signPdf(pdfToSign, pdfSigned, certPfx, pass);
//        verifySign();
    }

    public void signPdf(File pdfToSign, File pdfSigned, File certPfx, String pass) throws Exception {
        KeyStore ks = KeyStore.getInstance("pkcs12");
        ks.load(new FileInputStream(certPfx), pass.toCharArray());
        String alias = (String) ks.aliases().nextElement();
        PrivateKey key = (PrivateKey) ks.getKey(alias, pass.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);

        PdfReader reader = new PdfReader(pdfToSign.toString());
        FileOutputStream fout = new FileOutputStream(pdfSigned);

        // Add sign to Pdf file
        PdfStamper stp = PdfStamper.createSignature(reader, fout, '?');
        PdfSignatureAppearance sap = stp.getSignatureAppearance();
        sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
        sap.setReason("Firma PKCS12");
        sap.setLocation("Miguel Pazo");
        // Visible signed
        sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
        stp.close();
    }

//    public void verifySign() throws Exception {
//        Random rnd = new Random();
//
//        KeyStore kall = PdfPKCS7.loadCacertsKeyStore();
//        PdfReader reader = new PdfReader(pdfSigned);
//        AcroFields af = reader.getAcroFields();
//        ArrayList names = af.getSignatureNames();
//
//        for (int k = 0; k < names.size(); ++k) {
//            String name = (String) names.get(k);
//            int random = rnd.nextInt();
//            FileOutputStream out = new FileOutputStream("revision_" + random + "_" + af.getRevision(name) + ".pdf");
//
//            byte bb[] = new byte[8192];
//            InputStream ip = af.extractRevision(name);
//            int n = 0;
//            while ((n = ip.read(bb)) > 0) {
//                out.write(bb, 0, n);
//            }
//            out.close();
//            ip.close();
//
//            PdfPKCS7 pk = af.verifySignature(name);
//            Calendar cal = pk.getSignDate();
//            Certificate pkc[] = pk.getCertificates();
//            Object fails[] = PdfPKCS7.verifyCertificates(pkc, kall, null, cal);
//            if (fails == null) {
//                System.out.print(pk.getSignName());
//            } else {
//                System.out.print("Firma no válida");
//            }
//            File f = new File("revision_" + random + "_" + af.getRevision(name) + ".pdf");
//            f.delete();
//        }
//    }
}
