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

    @Test
    public void Encriptar() throws CryptoException {

        Crypto crypto = Crypto.getInstance(EvaKeys.ENCRYPT);
        String data = "jjdbc:oracle:thin:EVA_DEV/EVA_DEV@192.168.49.149:1521:BDD3S4";
        String dataEncrypt = crypto.encrypt(data);
        System.out.println(dataEncrypt);
        System.out.println(crypto.decrypt(dataEncrypt));

    }

    public static void main(String[] args) throws CryptoException {

        Crypto crypto = Crypto.getInstance(EvaKeys.ENCRYPT);
        String data = "jjdbc:oracle:thin:EVA_DEV/EVA_DEV@192.168.49.149:1521:BDD3S4";
        String dataEncrypt = crypto.encrypt(data);
        System.out.println(dataEncrypt);
        System.out.println(crypto.decrypt(dataEncrypt));
        
        System.out.println(crypto.decrypt("VFH/arruh2l8DViE3CtReMi5rLfSc1A/Nt2yfUwyFOaPf+0eVqYfFaUugLxkCQJctfGRn1TRGgLw3fO6UybgFdMVOrSvBk8uzAwo6EN5GxLTMNuCf1nn93r957sasbt1y2woD28RtD5Fsovp7z22f3NlPoQqyM8QVN+/knx/6Zc3X51o9BrQvcBHhsF8zDUKzbgifHuRjyCvPjcaiCwlJRyDOQt/JFQjADNVp6zV0lzs5G9+y6FWLGJDi8AiwSlkLTAkT2WZBRi50wOj295+0dKyzDCWYMEOn3r5S7h4gBkF+JvRXtEvDoSMw5sbujgMHZZPVf4ZPoE4ZLUK8fTYAIt/OtL2ONJAt3eZeI6N6T7P0INAuLnEgkua6aDsKW3APY0CJXPS3yu0XCFh2dIQJyrxwoYWhWA3inFMirH5qwZ2ShDrVppwkQj+Nvum311CGb/9E98S75QHqNMXrpwOxcytg9DAA5p8MWqtukh7A2+JLtloIUFA8RV73HfDwJdKDGUsmmwkIlikniOJ8zS9wfzMrN+WfgkMgXc54a75/xoItOIhhJs77zh5/GZERvM4CVAHEttfskGtac9c+UhWuRaIdHUaNnM5KQ5EuLm250QRa+CAC0+e13C60auKeZXh6tHEi4HubSogOqKHZHtXEDdUhQ5wxyANVR6YHs2XD0kcmGPMx9AzPWJKmsdtVFzb3jlwsa5Lx8BqrokgSz5yc3Zlw0u6iLj+Cw83uPH/M0QKWrTcFq8cRtvDYx1LM33m/bhxIQdm1rDLTepv3tFYHWBSQChURJZKrbRxO21zWfZayELLnTBuh/ih55HEMPqNFkksqwQJRLd0GHj4et8A1hkqtMdMqR/vn9zya2eNQliFV5n7ISserUacvo1aRlnbqpTnfmp2XhKqrihyGSWHH5xqSo9CUIxO7bG6nX+yC+2ttq9G2K3/09tR1KlX+8AsxELMH8Xy0ocY4xNI2RPb5CypRL2FmRj5/pTtE6WpRbH9dI4RmGCkfJiPXOewpsC04gqMfIOHaeZbjS3//jsQoNRoTYh3yDBzGio30JDYNkeSYxypjsS0ZicRwXCFhMspIl9w4ozTVBOBcWi79ablRl4yxJyMVcgojkeMZJLyB9LuxjmklCszunr6GhZnaR83"));
    }
}
