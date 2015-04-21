/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.util.hash.test;

import crypto.util.hash.Hash;
import java.io.File;
import java.io.IOException;
import org.bouncycastle.util.encoders.Base64;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jlimachi
 */
public class HashTest {

    public HashTest() {
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
    public void hashTest() throws IOException {

        Hash h = Hash.getInstance();
        String versionH = h.generateHash(new File("D:\\Eva Seleccion\\installevanselect.exe"));
        System.out.println(versionH);
        System.out.println(new String(Base64.decode(versionH)));
        System.out.println(h.generateHash("this is a message"));

    }
}
