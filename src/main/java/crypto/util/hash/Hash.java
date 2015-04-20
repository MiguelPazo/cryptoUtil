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
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public String generateHash(File file) {

        String hash = null;
        //converting file to bytes
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(file.exists() + "!!");
        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        byte[] bytes = bos.toByteArray();

        //Here print bytes to String
        //System.out.println(SecurityUtils.getHexString(bytes));
        //Hash bytes
        Security.addProvider(new BouncyCastleProvider());
        Digest messageDigestObj = new SHA1Digest();
        byte[] digest = new byte[messageDigestObj.getDigestSize()];
        messageDigestObj.update(bytes, 0, bytes.length);
        messageDigestObj.doFinal(digest, 0);
        //System.out.println(new String(Hex.encode(digest)));
        hash = new String(Base64.encode(digest)).replace("/", "").replace("=", "");
        return hash;
    }

}
