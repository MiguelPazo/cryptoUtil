package com.miguelpazo.signature;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
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

/**
 *
 * @author Miguel Pazo (http://miguelpazo.com/)
 */
public class CertificateUtil {

    public static PKCS10CertificationRequest generateCSR(KeyPair keyPair, X500Principal subjectName, String fileCSR) throws Exception {
        PKCS10CertificationRequest kpGen = new PKCS10CertificationRequest(
                "SHA1WITHRSA",
                subjectName,
                keyPair.getPublic(),
                null,
                keyPair.getPrivate());

        exportToPEM(kpGen, fileCSR);

        return kpGen;
    }

    public static KeyPair generatePairKeys(String publicKey, String privateKey) throws Exception {
        KeyPair pair = generateAndConvertRsaKeyPair(0x10001, 2048, 25);
        Key pubKey = pair.getPublic();
        Key privKey = pair.getPrivate();

        exportToPEM(pubKey, publicKey);
        exportToPEM(privKey, privateKey);

        return pair;
    }

    public static PrivateKey loadPrivKey(String privKeyFile) throws Exception {
        // Remove the first and last lines
        String key = readFileAsString(privKeyFile);
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

    public static void exportToPEM(Object obj, String fileName) throws Exception {
        OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(fileName));
        PEMWriter pem = new PEMWriter(output);
        pem.writeObject(obj);
        pem.close();
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
    private static KeyPair generateAndConvertRsaKeyPair(long publicExponent,
            int strength, int certainty) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

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

    private static String readFileAsString(String filePath) throws java.io.IOException {
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

}
