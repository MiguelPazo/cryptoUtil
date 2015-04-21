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
        String data = "jjdbc:oracle:thin:EVA_CARGA/EVA_CARGA@192.168.49.149:1521:BDD3S4";
        String dataEncrypt = crypto.encrypt(data);
        System.out.println(dataEncrypt);
        System.out.println(crypto.decrypt(dataEncrypt));

    }

    public static void main(String[] args) throws CryptoException {
        Crypto crypto = Crypto.getInstance(EvaKeys.ENCRYPT);
        String data = "VFH/arruh2l8DViE3CtReMi5rLfSc1A/Nt2yfUwyFObdsWS8STXxrE/"
                + "VLmBhbhAH16EaCBnoW6SGKM58sSgQiBkQuWNjfzf6Xhgds2WA14DKKLigeIE7Yfpza/"
                + "WT42Q6/A2Sm+Wsx9eQ3bJMe9gGwGU+611PW6dc/"
                + "R67HAYjKDMXJoSSx4wrBGVUPDwwKpSYrsnixZ1REPS2t8VGb0aR5mTIsWur1"
                + "dr2gpURTlmxJB4qaVBheXhUTOYr0TBmV4D5Rbcwa6Nfsb5QuW2+cHkSjVs1YWc"
                + "dtE2Q3mdrlda+XleWLsQoSczbA60Vtv6hb7bX9yfRS35BhRJ7cMiXJoD1aM7CYeQ16c"
                + "MVoDP55YO5BUvc0ss+S7th3JSsTLBpv/FxDJGGGXno00tVqCSBBEq7Oeh264DeLNblY"
                + "lTfxMM0nHA2E+iaantCbuTcOCkiQFMKB54ivNWBqR4TZvjpF3O/5kIPgePSkKZdQLQg72e"
                + "JchLrs6JU4SP8ssSDi7txX7uAHAG76llSKhmGjMOvmWtw4IZmM3WCpqIJKYDJZ8oL"
                + "yy6qpVBfuGDjY2ospGkX8o+bN8DdLOlpYjkSFqVS/BzJ5IRJ/WP8/HX8K+vfgjYLMiMoiG"
                + "DGCfp+CALbQwEeZSsetphtXvQyjH491avE0AH/vn1XbIzz2a7535d6uP/cJpMtZ1/5Caz5"
                + "knWx/y53Ch6OC+fA98HTQtiEsh9vR4kYdWGd3gArGubygST7aHuVi1NTESm92NASXbB9kp"
                + "mEgyv0W7XW28tJ2DYa1O6YhoGWge84GjgiH41NtGO/JxKheHpoyJ1/EfMBg660fy8A3sTK"
                + "WOZeY88fgWaRop8Vk3WG18iBCBwwlllUeydkbVnv7GKJBQJlqqKVxxj89sRfPt/4TQHnow"
                + "YZuHoZB401lBfULbrwiMLDWBuwkTfJPbIGr7q4EkbrU0ZPmYQ3GKRAksclSa+QFJlY8cJZhjJ"
                + "A2ZOYrliKrAanCphsZVngNbBymqBolgjEK/UzloPpb+lCp2OziXrInylIr4XHuGUHvoDC"
                + "+a2xWF04W5CZ+nts6BKIR9eDhvRasvZuNsB8gLH1vOtxyLYIiobTPlPzFZj4LXGcllfS1"
                + "U/Vl2BQ5rpJSKwTyWPiDsirzJQ9oKhM9Xy8m1oq";
        String dataEncrypt = crypto.decrypt(data);
        System.out.println(dataEncrypt);
        //System.out.println(crypto.decrypt(dataEncrypt));
    }
}
