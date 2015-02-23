package com.miguelpazo.signature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.x509.X509V3CertificateGenerator;

/**
 *
 * @author Miguel Pazo (http://miguelpazo.com/)
 */
public class CertificateUtil {

    public static CertificateUtil instance;

    private CertificateUtil() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static CertificateUtil getInstance() {
        if (instance == null) {
            instance = new CertificateUtil();
        }

        return instance;
    }

    public String encryptData(Key pKey, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pKey);

        byte[] encrypted = cipher.doFinal(data.getBytes());
        byte[] encryptedVal = Base64.encode(encrypted);

        return new String(encryptedVal);
    }

    public String encryptData(Key pKey, String data, File file) throws Exception {
        String dataEncrypt = encryptData(pKey, data);
        exportToFile(dataEncrypt, file);

        return dataEncrypt;
    }

    public String decryptData(Key pKey, String dataEncrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pKey);

        byte[] decodedBytes = Base64.decode(dataEncrypt.getBytes());
        byte[] original = cipher.doFinal(decodedBytes);

        return new String(original);
    }

    public String decryptData(Key pKey, String dataEncrypt, File file) throws Exception {
        String dataDecrypt = decryptData(pKey, dataEncrypt);
        exportToFile(dataDecrypt, file);

        return dataDecrypt;
    }

    public void signWithCA(File caCertFile, File caPrivateKey, PKCS10CertificationRequest requestCert, File cert) throws Exception {
        InputStream inStream = null;

        try {
            inStream = new FileInputStream(caCertFile);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate caCert = (X509Certificate) cf.generateCertificate(inStream);

            Calendar startDate = Calendar.getInstance();
            Calendar expiryDate = Calendar.getInstance();
            expiryDate.add(Calendar.YEAR, 1);

            BigInteger serialNumber = new BigInteger("123564879875416231576");
            PrivateKey caKey = loadPrivKey(caPrivateKey);

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

            exportToPEM(finalCert, cert);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }

    public void autoSignCSR(File caCertFile, KeyPair caKeys, PKCS10CertificationRequest csr) throws Exception {
        InputStream inStream = null;

        try {
            Calendar startDate = Calendar.getInstance();
            Calendar expiryDate = Calendar.getInstance();
            expiryDate.add(Calendar.YEAR, 1);

            BigInteger serialNumber = new BigInteger("123564879875416231576");

            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            X500Principal subjectName = new X500Principal(csr.getCertificationRequestInfo().getSubject().toString());

            certGen.setPublicKey(csr.getPublicKey());
            certGen.setSerialNumber(serialNumber);
            certGen.setIssuerDN(subjectName);
            certGen.setNotBefore(startDate.getTime());
            certGen.setNotAfter(expiryDate.getTime());
            certGen.setSubjectDN(subjectName);
            certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

            X509Certificate cert = certGen.generate(caKeys.getPrivate(), "BC");

            exportToPEM(cert, caCertFile);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }

    public PKCS10CertificationRequest generateCSR(KeyPair keyPair, X500Principal subjectName, File fileCSR) throws Exception {
        PKCS10CertificationRequest kpGen = new PKCS10CertificationRequest(
                "SHA1WITHRSA",
                subjectName,
                keyPair.getPublic(),
                null,
                keyPair.getPrivate());

        exportToPEM(kpGen, fileCSR);

        return kpGen;
    }

    public KeyPair generatePairKeys(File publicKey, File privateKey) throws Exception {
        KeyPair pair = generateAndConvertRsaKeyPair(0x10001, 2048, 25);
        Key pubKey = pair.getPublic();
        Key privKey = pair.getPrivate();

        exportToPEM(pubKey, publicKey);
        exportToPEM(privKey, privateKey);

        return pair;
    }

    public PrivateKey loadPrivKey(File privateKey) throws Exception {
        // Remove the first and last lines
        String key = readFileAsString(privateKey);
        String privKeyPEM = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "");

        // Base64 decode the data
        byte[] encoded = Base64.decode(privKeyPEM);

        // PKCS8 decode the encoded RSA private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);

        // Display the results
        return privKey;
    }

    public PublicKey loadPublicKey(File publicKeyFile) throws Exception {
        // Remove the first and last lines
        String key = readFileAsString(publicKeyFile);
        String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");

        // Base64 decode the data
        byte[] encoded = Base64.decode(publicKeyPEM);

        // PKCS8 decode the encoded RSA private key
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(keySpec);

        // Display the results
        return publicKey;
    }

    /**
     * Generates and converts an RSA key pair
     *
     * @param publicExponent public exponent
     * @param strength key strength
     * @param certainty certainty that p is prime. The higher the value the
     * slower the key generation
     * @return key pair
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidKeySpecException
     */
    private KeyPair generateAndConvertRsaKeyPair(long publicExponent,
            int strength, int certainty) throws Exception {
        RSAKeyGenerationParameters genParam = new RSAKeyGenerationParameters(
                BigInteger.valueOf(publicExponent), new SecureRandom(),
                strength, certainty);

        AsymmetricCipherKeyPairGenerator kpg = new RSAKeyPairGenerator();
        kpg.init(genParam);

        AsymmetricCipherKeyPair kp = kpg.generateKeyPair();

        RSAPrivateCrtKeyParameters rsapckp = (RSAPrivateCrtKeyParameters) kp.getPrivate();

        RSAKeyParameters rsakp = (RSAKeyParameters) kp.getPublic();

        KeyFactory fact = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);

        RSAPrivateCrtKeySpec prvKeySpecs
                = new RSAPrivateCrtKeySpec(rsapckp.getModulus(),
                        rsapckp.getPublicExponent(), rsapckp.getExponent(),
                        rsapckp.getP(), rsapckp.getQ(), rsapckp.getDP(),
                        rsapckp.getDQ(), rsapckp.getQInv());

        PrivateKey privateKey = fact.generatePrivate(prvKeySpecs);

        RSAPublicKeySpec pubKeySpecs = new RSAPublicKeySpec(rsakp.getModulus(), rsakp.getExponent());
        PublicKey publicKey = fact.generatePublic(pubKeySpecs);

        return new KeyPair(publicKey, privateKey);
    }

    public String readFileAsString(File filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;

        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();

        return fileData.toString();
    }

    private void exportToPEM(Object obj, File fileName) throws Exception {
        if (!fileName.getParentFile().exists()) {
            fileName.getParentFile().mkdirs();
        }

        PEMWriter pem = null;

        try {
            OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(fileName));
            pem = new PEMWriter(output);
            pem.writeObject(obj);
        } finally {
            if (pem != null) {
                pem.close();
            }
        }
    }

    public void exportToFile(String data, File fileName) throws Exception {
        if (!fileName.getParentFile().exists()) {
            fileName.getParentFile().mkdirs();
        }

        BufferedWriter output = null;

        try {
            output = new BufferedWriter(new FileWriter(fileName));
            output.write(data);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
}
