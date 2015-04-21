/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.util.hash;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Security;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

/**
 *
 * @author jlimachi
 */
public class Hash {

    private static Hash instance = null;

    protected Hash() {
        // Exists only to defeat instantiation.
    }

    public static Hash getInstance() {
        if (instance == null) {
            instance = new Hash();
        }
        return instance;
    }

    public String generateHash(File file, Boolean replace) throws FileNotFoundException, IOException {
        String hash = null;
        //converting file to bytes
        FileInputStream fis = null;

        fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum); //no doubt here is 0
            //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
            System.out.println("read " + readNum + " bytes,");
        }

        byte[] bytes = bos.toByteArray();

        Security.addProvider(new BouncyCastleProvider());
        Digest messageDigestObj = new SHA1Digest();
        byte[] digest = new byte[messageDigestObj.getDigestSize()];
        messageDigestObj.update(bytes, 0, bytes.length);
        messageDigestObj.doFinal(digest, 0);
        hash = new String(Base64.encode(digest));

        if (replace) {
            hash = hash.replace("/", "").replace("=", "");
        }

        return hash;
    }

    public String generateHash(String data, Boolean replace) {
        String hash = null;
        Security.addProvider(new BouncyCastleProvider());
        Digest messageDigestObj = new SHA1Digest();
        byte[] digest = new byte[messageDigestObj.getDigestSize()];
        messageDigestObj.update(data.getBytes(), 0, data.getBytes().length);
        messageDigestObj.doFinal(digest, 0);

        hash = new String(Base64.encode(digest));

        if (replace) {
            hash = hash.replace("/", "").replace("=", "");
        }

        return hash;
    }

}
