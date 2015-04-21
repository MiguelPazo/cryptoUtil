/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.util.crypto.test;

import crypto.util.encription.Crypto;
import org.bouncycastle.crypto.CryptoException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pe.gob.onpe.eva.security.EvaKeys;

/**
 *
 * @author jlimachi
 */
public class CrytoTest {

    public CrytoTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void Encriptar() throws CryptoException {
        Crypto crypto = Crypto.getInstance(EvaKeys.ENCRYPT);
        String data = "jjdbc:oracle:thin:EVA_DEV/EVA_DEV@192.168.49.149:1521:BDD3S4";
        String dataEncrypt = crypto.encrypt(data);
        System.out.println(dataEncrypt);
        System.out.println(crypto.decrypt(dataEncrypt));

    }
}
